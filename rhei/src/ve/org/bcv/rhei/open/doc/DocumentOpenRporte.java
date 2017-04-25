package ve.org.bcv.rhei.open.doc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.bcv.comparator.SortByValueComparatorAsc;
import com.bcv.dao.jdbc.BcvDao;
import com.bcv.dao.jdbc.ParametroDao;
import com.bcv.dao.jdbc.PeriodoEscolarDao;
import com.bcv.dao.jdbc.RelacionDePagosDao;
import com.bcv.dao.jdbc.ReporteBecaUtileDao;
import com.bcv.dao.jdbc.ReporteEstatusDao;
import com.bcv.dao.jdbc.SolicitudDao;
import com.bcv.dao.jdbc.TipoEmpleadoDao;
import com.bcv.dao.jdbc.impl.ParametroDaoImpl;
import com.bcv.dao.jdbc.impl.PeriodoEscolarDaoImpl;
import com.bcv.dao.jdbc.impl.RelacionDePagosDaoImpl;
import com.bcv.dao.jdbc.impl.ReporteBecaUtileDaoImpl;
import com.bcv.dao.jdbc.impl.ReporteEstatusDaoImpl;
import com.bcv.dao.jdbc.impl.SolicitudDaoImpl;
import com.bcv.dao.jdbc.impl.TipoEmpleadoDaoImpl;
import com.bcv.model.Parametro;
import com.bcv.model.RelacionDePagos;
import com.bcv.model.ReporteByDefinitivoOrTransitivo;
import com.bcv.model.TipoEmpleado;
import com.bcv.reporte.pagotributo.MemorandoReport;
import com.bcv.reporte.pagotributo.PagoTributoBean;
import com.bcv.reporte.pagotributo.ReporteBecaUtileGenerarReporte;
import com.bcv.reporte.pagotributo.ReporteByPagoAtributoCEI;
import com.bcv.reporte.pagotributo.ReporteByPagoAtributoEmp;
import com.bcv.reporte.relacionpago.ProveedorRpago1Bean;

import ve.org.bcv.rhei.bean.DocumentosBean;
import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.bean.ValorNombre;
import ve.org.bcv.rhei.negocio.MesesBean;
import ve.org.bcv.rhei.report.ReportePathLogo;
import ve.org.bcv.rhei.report.by.benef.Reporte;
import ve.org.bcv.rhei.util.Constantes;
import ve.org.bcv.rhei.util.pdf;

/**
 * Servlet implementation class DocumentOpen
 */
public class DocumentOpenRporte extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(DocumentOpenRporte.class.getName());
	private ServletContext sc;
	private SolicitudDao solicitudDao = new SolicitudDaoImpl();
	private PeriodoEscolarDao periodoEscolarDao = new PeriodoEscolarDaoImpl();
	private ReporteEstatusDao reporteEstatusDao = new ReporteEstatusDaoImpl();
	private TipoEmpleadoDao tipoEmpleadoDao = new TipoEmpleadoDaoImpl();
	private ParametroDao parametroDao = new ParametroDaoImpl();
	private RelacionDePagosDao relacionDePagosDao = new RelacionDePagosDaoImpl();
    private ReporteBecaUtileDao reporteBecaUtileDao = new ReporteBecaUtileDaoImpl();
	@Inject
	private BcvDao bcvDao;

	private enum MimeType {
		pdf, doc, dot, pps, ppt, xl, xls, xml, txt;
	}

	private ShowResultToView showResultToView;
	/**
	 * TamaÃ±o del Buffer por defecto.
	 */
	private static final int NUM = 512;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DocumentOpenRporte() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		process(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ResourceBundle rb = ResourceBundle.getBundle("ve.org.bcv.rhei.util.bundle");
		RequestDispatcher rd = null;
		HttpSession session = request.getSession(false);
		String aviso = "FinSesion";
		if (request.getSession(false) == null) {
			session = request.getSession(true);
			session.setAttribute("aviso", aviso);
			rd = request.getRequestDispatcher("/jsp/login.jsp");
			try {
				rd.forward(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			 String nombreUsuario=session.getAttribute("nombreUsuario")!=null?(String)session.getAttribute("nombreUsuario"):null;
			String ninoEspecial = request.getParameter("ninoEspecial") != null ? (String) request.getParameter("ninoEspecial") : "";

			String companiaAnalista = session.getAttribute("companiaAnalista") != null ? (String) request.getSession().getAttribute("companiaAnalista") : "01";


			DocumentosBean documentosBean = new DocumentosBean();
			RequestDispatcher despachador = null;
			String pag = request.getParameter("accion") != null ? (String) request.getParameter("accion") : "";
			String pagIr = "/jsp/" + pag;

			/** Periodo escolar **/
			String periodoEscolar = null;
			// // Begin checkOrAvisoCredito

			//
			// // Begin receptorPago
			String receptorPagoStr = request.getParameter("receptorPago") != null ? (String) request.getParameter("receptorPago") : null;
			int receptorPago = -1;
			//
			String tipoEmpleado = request.getParameter("tipoEmpleado") != null ? (String) request.getParameter("tipoEmpleado") : null;
			/** Consultamos.. */
			try {

				int keyReport = request.getParameter("keyReport") != null ? Integer.parseInt(request.getParameter("keyReport")) : 0;
				Reporte reporte = null;
				String fileOut = "reporte.pdf";
				InputStream jrxml = null;
				Map parameters = null;
				ReportePathLogo archivo = null;
				InputStream is = null;
				String coStatus = null;
				String numSolicitud = null;
				/** HACEMOS EL REPORTE */
				switch (keyReport) {
				case 1:
					/**
					 * 
					 * Si buscamos por persona individual que es por cedula, los datos seguros a
					 * mandar es:
					 * 
					 * descripPeriodo filtrarByMesOrComplementoOrAmbos=2 (0 es filtrar para los no
					 * complementos, 1 es filtrar para los complemento, 2 es hacer el query sin
					 * filtrar) coStatus companiaAnalista receptorPago (Centro de educacion inicial
					 * o trabajador) coFormaPagoStr (Forma de pago, cheque o reembolso)
					 * cedulaEmpleado
					 * 
					 */
					String meses = request.getParameter("meses") != null ? (String) request.getParameter("meses") : "";

					String isToCreateReportDefinitivo = request.getParameter("pagadoNoPagadoAmboSearchs") != null ? (String) request.getParameter("pagadoNoPagadoAmboSearchs")
							: Constantes.PAGADO_NOPAGADO_AMBOS;

					/** Si no se escoje ningun mes, agarramos todos */
					if (StringUtils.isEmpty(meses)) {
						/************** MESES ********/
						meses = "";
						MesesBean mesesBean = new MesesBean();
						String[] mes = mesesBean.getMes();
						String[] mesesArray = mesesBean.getMeses();

						List<ValorNombre> listadoMeses = new ArrayList<ValorNombre>();
						ValorNombre valorNombre = null;
						for (int j = 0; j <= 12; j++) {
							valorNombre = new ValorNombre(mesesArray[j], mes[j]);
							listadoMeses.add(valorNombre);

						}
						Collections.sort(listadoMeses, new SortByValueComparatorAsc());
						boolean firstTime = true;
						for (ValorNombre vn : listadoMeses) {
							if (!firstTime) {
								meses += ',';
							}
							firstTime = false;
							meses += vn.getValor();
						}
						/*********** FIN MESES ***********/
					}

					String descripPeriodo = request.getParameter("periodoEscolar") != null ? (String) request.getParameter("periodoEscolar") : "";
					;
					String filtrarByMesOrComplementoOrAmbos = request.getParameter("filtrarByMesOrComplementoOrAmbos") != null ? (String) request.getParameter("filtrarByMesOrComplementoOrAmbos") : "";
					coStatus = request.getParameter("status") != null ? (String) request.getParameter("status") : "A";
					if (rb.getString("activo").equalsIgnoreCase(coStatus)) {
						coStatus = Constantes.CO_STATUS_ACTIVO;
					} else if (rb.getString("desincorporado").equalsIgnoreCase(coStatus)) {
						coStatus = Constantes.CO_STATUS_DESINCORPORADO;
					}

					companiaAnalista = session.getAttribute("companiaAnalista") != null ? (String) request.getSession().getAttribute("companiaAnalista") : "01";
					/** Centro de educacion inicial o trabajador */
					receptorPago = -1;
					receptorPagoStr = request.getParameter("receptorPago") != null ? (String) request.getParameter("receptorPago") : null;
					if (!StringUtils.isEmpty(receptorPagoStr) && StringUtils.isNumeric(receptorPagoStr)) {
						receptorPago = new Integer(receptorPagoStr);
					}
					/** Forma de pago, cheque o reembolso */
					String coFormaPagoStr = request.getParameter("coFormaPago") != null ? (String) request.getParameter("coFormaPago") : null;
					int coFormaPago1 = -1;
					if (!StringUtils.isEmpty(coFormaPagoStr) && StringUtils.isNumeric(coFormaPagoStr)) {
						coFormaPago1 = new Integer(coFormaPagoStr);
					}

					tipoEmpleado = request.getParameter("tipoEmpleado") != null ? (String) request.getParameter("tipoEmpleado") : null;

					/**
					 * Si es obtenido la cedula, entonces el tipo de empleado lo agarramos por la
					 * cedula
					 */
					String cedulaEmpleado = request.getParameter("cedulaEmpleado") != null ? (String) request.getParameter("cedulaEmpleado") : null;

					if (!StringUtils.isEmpty(cedulaEmpleado)) {
						tipoEmpleado = tipoEmpleadoDao.tipoEmpleadoSolo(cedulaEmpleado);
					}

					numSolicitud = request.getParameter("numSolicituds") != null ? (String) request.getParameter("numSolicituds") : "";

					/***
					 * Si viene la solicitud vacia, buscamos por cedula a ver si por cedula aparece
					 ***/
					if (!StringUtils.isEmpty(cedulaEmpleado)) {
						String cedulaFamiliar = null;
						RelacionDePagos relacionDePagos = relacionDePagosDao.lastNumSolicitudPagoByCedula(coStatus, cedulaEmpleado, cedulaFamiliar);
						if (relacionDePagos != null) {
							if (StringUtils.isEmpty(numSolicitud)) {
								numSolicitud = relacionDePagos.getNroSolicitudsSeparatedByComa();
							}

							if (!StringUtils.isEmpty(numSolicitud)) {
								ReporteByDefinitivoOrTransitivo reporteByDefinitivoOrTransitivo = relacionDePagosDao.reporteByConsulta(numSolicitud, companiaAnalista);
								if (null != reporteByDefinitivoOrTransitivo) {
									tipoEmpleado = reporteByDefinitivoOrTransitivo.getTipoEmpleado();
								}
							}

						}
					}

					// CREAMOS LOS ARCHIVOM PARA P´ROCESARLOS EN PDF
					// RECEPTORPAGO ES 0 CENTRO DE EDUCACION INICIAL, 1 TRABAJADOR
					List<File> addAllFile = new ArrayList<File>();
					// RECEPTORPAGO ES 2, SIGNIFICA QUE HAY QUE PROCESARLOS POR AMBOS CENTRO DE
					// EDUCACION INICIAL, 1 TRABAJADOR
					boolean conMemorandum = true;
					if (receptorPago == 2) {
						conMemorandum = false;
						int centroEducacionInicialRecpetorpago = 0;
						List<File> addAllFileCentroEducacionInicial = createFileBYReceptorPagos(numSolicitud, meses, descripPeriodo, rb, filtrarByMesOrComplementoOrAmbos, coStatus, companiaAnalista,
								centroEducacionInicialRecpetorpago, coFormaPago1, tipoEmpleado, isToCreateReportDefinitivo, cedulaEmpleado, conMemorandum, ninoEspecial);

						int trabajadorRecpetorpago = 1;
						List<File> addAllFileTrabajador = createFileBYReceptorPagos(numSolicitud, meses, descripPeriodo, rb, filtrarByMesOrComplementoOrAmbos, coStatus, companiaAnalista,
								trabajadorRecpetorpago, coFormaPago1, tipoEmpleado, isToCreateReportDefinitivo, cedulaEmpleado, conMemorandum, ninoEspecial);

						if (addAllFileCentroEducacionInicial != null && addAllFileCentroEducacionInicial.size() > 0) {
							addAllFile.addAll(addAllFileCentroEducacionInicial);
						}

						if (addAllFileTrabajador != null && addAllFileTrabajador.size() > 0) {
							addAllFile.addAll(addAllFileTrabajador);
						}

					} else {
						addAllFile = createFileBYReceptorPagos(numSolicitud, meses, descripPeriodo, rb, filtrarByMesOrComplementoOrAmbos, coStatus, companiaAnalista, receptorPago, coFormaPago1,
								tipoEmpleado, isToCreateReportDefinitivo, cedulaEmpleado, conMemorandum, ninoEspecial);
					}
					// FIN CREAMOS LOS ARCHIVOM PARA P´ROCESARLOS EN PDF

					if (addAllFile != null && addAllFile.size() > 0) {
						File resultTotal = pdf.pegarArchivosPDF(addAllFile, "reportPagoTributo");
						log.info("resultTotal.getAbsoluteFile()=" + resultTotal.getAbsoluteFile());
						/****** Total del reporte *********/
						documentosBean.setInputStream(pdf.FileToInputStream(resultTotal));
						documentosBean.setDocFileWithExtension(fileOut);
						generarReportePagos(documentosBean, request, despachador, response, pagIr);
						/****** Fin Total del reporte *********/
					} else {
						showResultToView = new ShowResultToView();

						showResultToView.setMensaje("fracaso3");
						request.setAttribute("showResultToView", showResultToView);
						despachador = request.getRequestDispatcher("/reporteConsultaIndividualControlador?principal.do=reportConsulta");
						despachador.forward(request, response);
					}

					break;

				/**** Reporte de pago y tributo *********/
				case 2:

					// mesMatrCompl
					descripPeriodo = request.getParameter("periodoEscolar") != null ? (String) request.getParameter("periodoEscolar") : "";
					;
					filtrarByMesOrComplementoOrAmbos = request.getParameter("filtrarByMesOrComplementoOrAmbos") != null ? (String) request.getParameter("filtrarByMesOrComplementoOrAmbos") : "";

					coStatus = request.getParameter("status") != null ? (String) request.getParameter("status") : "A";
					if (rb.getString("activo").equalsIgnoreCase(coStatus)) {
						coStatus = Constantes.CO_STATUS_ACTIVO;
					} else if (rb.getString("desincorporado").equalsIgnoreCase(coStatus)) {
						coStatus = Constantes.CO_STATUS_DESINCORPORADO;
					}

					companiaAnalista = session.getAttribute("companiaAnalista") != null ? (String) request.getSession().getAttribute("companiaAnalista") : "01";
					receptorPago = -1;
					receptorPagoStr = request.getParameter("receptorPago") != null ? (String) request.getParameter("receptorPago") : null;
					if (!StringUtils.isEmpty(receptorPagoStr) && StringUtils.isNumeric(receptorPagoStr)) {
						receptorPago = new Integer(receptorPagoStr);
					}
					coFormaPagoStr = request.getParameter("coFormaPago") != null ? (String) request.getParameter("coFormaPago") : null;
					coFormaPago1 = -1;
					if (!StringUtils.isEmpty(coFormaPagoStr) && StringUtils.isNumeric(coFormaPagoStr)) {
						coFormaPago1 = new Integer(coFormaPagoStr);
					}
					tipoEmpleado = request.getParameter("tipoEmpleado") != null ? (String) request.getParameter("tipoEmpleado") : null;
					numSolicitud = request.getParameter("numSolicituds") != null ? (String) request.getParameter("numSolicituds") : "";
					/***** Ordenamos ********/

					/************** MESES ********/
					meses = "";
					MesesBean mesesBean = new MesesBean();
					String[] mes = mesesBean.getMes();
					String[] mesesArray = mesesBean.getMeses();

					List<ValorNombre> listadoMeses = new ArrayList<ValorNombre>();
					ValorNombre valorNombre = null;
					for (int j = 0; j <= 12; j++) {
						valorNombre = new ValorNombre(mesesArray[j], mes[j]);
						listadoMeses.add(valorNombre);

					}
					Collections.sort(listadoMeses, new SortByValueComparatorAsc());
					boolean firstTime = true;
					for (ValorNombre vn : listadoMeses) {
						if (!firstTime) {
							meses += ',';
						}
						firstTime = false;
						meses += vn.getValor();
					}
					/*********** FIN MESES ***********/

					String nombreDefinitivoTransitorio = request.getParameter("nombreDefinitivoTransitorio") != null ? (String) request.getParameter("nombreDefinitivoTransitorio") : null;

					String exito = relacionDePagosDao.updateToDefinitivo(nombreDefinitivoTransitorio, coFormaPago1, receptorPago, descripPeriodo, coStatus, tipoEmpleado,
							filtrarByMesOrComplementoOrAmbos, meses, ninoEspecial,nombreUsuario);

					if (!StringUtils.isEmpty(exito) && !"0".equalsIgnoreCase(exito) && !"reporte.no.hay.registros".equalsIgnoreCase(exito)) {
						request.setAttribute("reportedefinitivotransitorio", "exito");
					} else {

						if ("reporte.no.hay.registros".equalsIgnoreCase(exito)) {
							request.setAttribute("reportedefinitivotransitorio", "reporte.no.hay.registros");
						} else {
							request.setAttribute("reportedefinitivotransitorio", "error");
							request.setAttribute("result", exito);
						}

					}

					request.setAttribute("nombreDefinitivoTransitorio", nombreDefinitivoTransitorio);
					/**
					 * Este null, viene de DocumentOpenReport.java, lo colocamos de parametro
					 * periodoEscolar,nombreDefinitivoTransitorio =null para listar nuevamente la
					 * lista de reportes definitivos
					 */
					String valor = null;
					despachador = request
							.getRequestDispatcher("/reporteDefinitivoTransitorioControlador?principal.do=reportByPagosandTributos&periodoEscolar=" + valor + "&nombreDefinitivoTransitorio=" + valor);

					despachador.forward(request, response);

					break;
				case 3:
					
					String periodoScolar=request.getParameter("periodoEscolar");
					String txObservacion=request.getParameter("txObservacion");
					
					
					String status=request.getParameter("status");
					boolean isListado=true;
					List<File> addFile = new ArrayList<File>();
					Reporte reporteUtile = null;
					File fileDetalle = null;
					List<com.bcv.model.ReporteBecaUtile> objs= null;
					if ("A".equalsIgnoreCase(status)){
						 objs= reporteBecaUtileDao.reporteBecaUtileDao(periodoScolar, tipoEmpleado, status);	
					}else if ("D".equalsIgnoreCase(status)){
						 objs= reporteBecaUtileDao.reporteBecaUtileDesincorporado(periodoScolar, txObservacion);
					
					}
					
					if ("".equalsIgnoreCase(tipoEmpleado)|| null==tipoEmpleado && null!=objs && objs.size()>0){
						tipoEmpleado=objs.get(0).getTipoEmpleado();
					}
				
					/******** lista utiles ************/
					int numerRegistros = new Integer(rb.getString("reporte.numero.registroUtiles"));
					List<com.bcv.model.ReporteBecaUtile> objsAux= new ArrayList<com.bcv.model.ReporteBecaUtile>(); 
					int columnCount=0;
					int count=0;
					int numPage=0;
					Parametro parametro =null;
					for (com.bcv.model.ReporteBecaUtile obj:objs){
						columnCount++;
						count++;
						obj.setColumnCount(columnCount);
						objsAux.add(obj);
						/** Cada 10 veces se genera un reporte **/
						if ((count - numerRegistros) == 0) {
							numPage++;
							jrxml = PagoTributoBean.class.getResourceAsStream("reporteListUtiles.jrxml");
							fileOut = "reporteUtiles.pdf";
							parameters = new HashMap();
							archivo = new ReportePathLogo();
							is = archivo.getClass().getResourceAsStream("logo_bcv.jpg");
							parameters.put("numPage",numPage);
							parameters.put("logo", is);
							parameters.put("descripPeriodo", periodoScolar);
							parameters.put("cordAdministrativo", rb.getString("reporte.cordAdministrativo"));
							parametro = parametroDao.findParametro(companiaAnalista, "EMP", "RHEI", Constantes.CORADM, null);
							if (null != parametro) {
								parameters.put("cordAdministrativo", parametro.getValorParametro());
							}

							//parameters.put("CORADM", "");
							parameters.put("titulo", rb.getString("reporte.microUtilesLista.titulo"));
							reporteUtile = new ReporteBecaUtileGenerarReporte(periodoScolar, status,isListado,objsAux);
							documentosBean = reporteUtile.generar(jrxml, parameters, fileOut);
							fileDetalle = pdf.inputStreamToFile(documentosBean.getInputStream());
							addFile.add(fileDetalle);
							count=0;
						    objsAux= new ArrayList<com.bcv.model.ReporteBecaUtile>();
								
							
						}
					}
					if (objsAux!=null && objsAux.size()>0){
						numPage++;
						jrxml = PagoTributoBean.class.getResourceAsStream("reporteListUtiles.jrxml");
						fileOut = "reporteUtiles.pdf";
						parameters = new HashMap();
						archivo = new ReportePathLogo();
						parameters.put("numPage",numPage);
						is = archivo.getClass().getResourceAsStream("logo_bcv.jpg");
						parameters.put("logo", is);
						parameters.put("descripPeriodo", periodoScolar);
						parameters.put("cordAdministrativo", rb.getString("reporte.cordAdministrativo"));
						parametro = parametroDao.findParametro(companiaAnalista, "EMP", "RHEI", Constantes.CORADM, null);
						if (null != parametro) {
							parameters.put("cordAdministrativo", parametro.getValorParametro());
						}
						
						
//						parameters.put("CORADM", "");
						parameters.put("titulo", rb.getString("reporte.microUtilesLista.titulo"));
						reporteUtile = new ReporteBecaUtileGenerarReporte(periodoScolar, status,isListado,objsAux);
						documentosBean = reporteUtile.generar(jrxml, parameters, fileOut);
						fileDetalle = pdf.inputStreamToFile(documentosBean.getInputStream());
						addFile.add(fileDetalle);
						
					}
					
					
					
					
					/******** memorando lista utiles ************/
					parameters = new HashMap();
					
					 isListado=false;
					  reporteUtile = new ReporteBecaUtileGenerarReporte(periodoScolar, status,isListado,objs);
					jrxml = PagoTributoBean.class.getResourceAsStream("memorandoUtiles.jrxml");
					fileOut = "reporteUtiles.pdf";
 
					/**Sumo los parametros al reporte */
					parameters= parmetroddds( periodoScolar, companiaAnalista, rb,tipoEmpleado );
					
					
					
					documentosBean = reporteUtile.generar(jrxml, parameters, fileOut);
					  fileDetalle = pdf.inputStreamToFile(documentosBean.getInputStream());
					addFile.add(fileDetalle);
					
					/******** Copia memorando lista utiles************/
					/**Sumo los parametros al reporte */
					parameters= parmetroddds( periodoScolar, companiaAnalista, rb ,tipoEmpleado);
					parameters.put("FIRREP", "");
					jrxml = PagoTributoBean.class.getResourceAsStream("memorandoUtiles.jrxml");
					fileOut = "reporteUtiles.pdf";
					documentosBean = reporteUtile.generar(jrxml, parameters, fileOut);
					File fileDetalleCopia = pdf.inputStreamToFile(documentosBean.getInputStream());
					addFile.add(fileDetalleCopia);

					/******** Fin copia************/

					/********* Unimos los dos archivos ************************************/
					File result = pdf.pegarArchivosPDF(addFile, "reportPagoTributo");
					/********* Fin Unimos los dos archivos ************************************/
					
					if ("A".equalsIgnoreCase(status)){
						reporteBecaUtileDao.updateReporteBecaUtileDao(periodoScolar,txObservacion, tipoEmpleado);
					}

					documentosBean.setInputStream(pdf.FileToInputStream(result));
					documentosBean.setDocFileWithExtension(fileOut);
					procesarFileShow(documentosBean, response);


					break;

				default:

					break;
				}

				/** FIN HACEMOS EL REPORTE */

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	
	private HashMap parmetroddds(String periodoScolar,String companiaAnalista,ResourceBundle rb ,String tipoEmpleado) throws SQLException{
		//	String companiaAnalista = session.getAttribute("companiaAnalista") != null ? (String) request.getSession().getAttribute("companiaAnalista") : "01";
		SimpleDateFormat formateador = new SimpleDateFormat("dd MMMMM yyyy", new Locale("es", "VE"));
			
		HashMap parameters = new HashMap();
			InputStream is = null;
			ReportePathLogo archivo = new ReportePathLogo();
			is = archivo.getClass().getResourceAsStream("logo_bcv.jpg");
			parameters.put("logo", is);
			// Esto muestra la fecha actual en pantalla, más o menos así 26/10/2006
			parameters.put("fecha", formateador.format(new Date()));	
			Date fechaActual = new Date();
			SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyy");
			String hoy = formato.format(fechaActual);
			parameters.put("utiles1", rb.getString("reporte.microUtiles.utiles1"));
			parameters.put("descripPeriodo", periodoScolar);
			parameters.put("utiles2", rb.getString("reporte.microUtiles.utiles2"));
			parameters.put("montoUtiles", rb.getString("reporte.microUtiles.montoUtiles"));
			
			DecimalFormat df2 = new DecimalFormat(Constantes.FORMATO_DOUBLE, new DecimalFormatSymbols(new Locale("es", "VE")));
			BigDecimal value = new BigDecimal(bcvDao.MontoUtiles(tipoEmpleado));
			String valorParametro = new String(df2.format(value.floatValue()));

			if (null != valorParametro) {
			    parameters.put("montoUtiles", valorParametro);
		    }
			
			parameters.put("utiles3", rb.getString("reporte.microUtiles.utiles3"));
			parameters.put("FIRMA", rb.getString("reporte.memorando.atentamentefirma"));
			
			parameters.put("departamentofirma", rb.getString("reporte.microUtiles.departamentofirma"));
			
			Parametro parametro = parametroDao.findParametro(companiaAnalista, "EMP", "RHEI", Constantes.departamentofirma, null);
				if (null != parametro) {
					parameters.put("departamentofirma", parametro.getValorParametro());
				}
			
				parameters.put("cordAdministrativo", rb.getString("reporte.cordAdministrativo"));
				parametro = parametroDao.findParametro(companiaAnalista, "EMP", "RHEI", Constantes.CORADM, null);
				if (null != parametro) {
					parameters.put("cordAdministrativo", parametro.getValorParametro());
				}
			parameters.put("FIRREP", rb.getString("reporte.microUtiles"));
			 parametro = parametroDao.findParametro(companiaAnalista, "EMP", "RHEI", Constantes.FIRREP, null);
			if (null != parametro) {
				parameters.put("FIRREP", parametro.getValorParametro());
			}
			
 
			parameters.put("tipoEmpl", rb.getString("reporte.memorando.tipoEmpl"));
			if (tipoEmpleado != null) {
				parameters.put("tipoEmpl", tipoEmpleado);
			}
			
			
			return parameters;
		}
		
	
	
	private List<File> createFileBYReceptorPagos(String numSolicitud, String meses, String descripPeriodo, ResourceBundle rb, String filtrarByMesOrComplementoOrAmbos, String coStatus,
			String companiaAnalista, int receptorPago, int coFormaPago1, String tipoEmpleado, String isToCreateReportDefinitivo, String cedulaEmpleado, boolean conMemorandum, String ninoEspecial)
			throws Exception {

		/***
		 * Si viene la solicitud, buscamos todas las solicitudes que cumpla la condicionm
		 ***/
		if (StringUtils.isEmpty(cedulaEmpleado) && StringUtils.isEmpty(numSolicitud)) {
			String numSolicitudsEmpty = null;

			List<ProveedorRpago1Bean> proveedorRpago1Bean = relacionDePagosDao.searchSolicitudesToReporte(coFormaPago1, receptorPago, descripPeriodo, numSolicitudsEmpty, coStatus, tipoEmpleado,
					isToCreateReportDefinitivo, ninoEspecial);
			StringBuilder numSolicitudDirect = new StringBuilder("");
			boolean firstTime = true;
			for (ProveedorRpago1Bean vn : proveedorRpago1Bean) {
				if (!firstTime) {
					numSolicitudDirect.append(",");
				}
				firstTime = false;
				numSolicitudDirect.append(vn.getNuSolicitud());

			}
			numSolicitud = numSolicitudDirect.toString();
		}

		List<File> addAllFile = new ArrayList<File>();
		if (!StringUtils.isEmpty(numSolicitud)) {

			StringTokenizer stk = new StringTokenizer(numSolicitud, ",");
			StringBuilder numSolicitudPorPartes = new StringBuilder("");
			int i = 0;
			File result = null;
			boolean putComma = false;
			int numerRegistros = new Integer(rb.getString("reporte.numero.registro"));
			int pageReport = 1;
			while (stk.hasMoreTokens()) {
				i++;
				String numSoli = null;
				if (!putComma) {
					/**
					 * getNumSolicitudOnly->Separamos el numero de solicitud del nombre con el
					 * separador Constantes.SEPARADORENTRENUMSOLINOMBRE
					 ***/
					numSoli = getNumSolicitudOnly((String) stk.nextToken());
					numSolicitudPorPartes.append(numSoli);
					putComma = true;
				} else {
					/**
					 * getNumSolicitudOnly->Separamos el numero de solicitud del nombre con el
					 * separador Constantes.SEPARADORENTRENUMSOLINOMBRE
					 ***/
					numSoli = getNumSolicitudOnly((String) stk.nextToken());
					numSolicitudPorPartes.append(",");
					numSolicitudPorPartes.append(numSoli);

				}
				if ((i - numerRegistros) == 0) {

					result = reportesPorPartes(numSolicitudPorPartes.toString(), meses, descripPeriodo, filtrarByMesOrComplementoOrAmbos, coStatus, companiaAnalista, receptorPago, coFormaPago1,
							tipoEmpleado, pageReport, isToCreateReportDefinitivo, conMemorandum, ninoEspecial);
					addAllFile.add(result);
					i = 0;
					numSolicitudPorPartes = new StringBuilder("");
					putComma = false;
					pageReport++;
				}

			}
			if (!StringUtils.isEmpty(numSolicitudPorPartes)) {
				result = reportesPorPartes(numSolicitudPorPartes.toString(), meses, descripPeriodo, filtrarByMesOrComplementoOrAmbos, coStatus, companiaAnalista, receptorPago, coFormaPago1,
						tipoEmpleado, pageReport, isToCreateReportDefinitivo, conMemorandum, ninoEspecial);
				addAllFile.add(result);
			}
		}
		return addAllFile;

	}

	/**
	 * Separamos el numero de solicitud del nombre con el separador
	 * Constantes.SEPARADORENTRENUMSOLINOMBRE
	 * 
	 * @param separadorentrenumsolinombre
	 * @return
	 */
	private String ordenarAllNumSolicituds(String numSolicituds) {

		if (!StringUtils.isEmpty(numSolicituds)) {
			StringTokenizer separador = new StringTokenizer(numSolicituds, ",");
			List<String> ordenar = new ArrayList<String>();
			while (separador.hasMoreTokens()) {
				ordenar.add((String) separador.nextToken());
			}

			Collections.sort(ordenar);
			numSolicituds = ordenar.toString();
		}

		return numSolicituds;
	}

	/**
	 * Separamos el numero de solicitud del nombre con el separador
	 * Constantes.SEPARADORENTRENUMSOLINOMBRE
	 * 
	 * @param separadorentrenumsolinombre
	 * @return
	 */
	private String getNumSolicitudOnly(String separadorentrenumsolinombre) {
		String numSol = null;
		if (!StringUtils.isEmpty(separadorentrenumsolinombre)) {
			StringTokenizer separador = new StringTokenizer(separadorentrenumsolinombre, Constantes.SEPARADORENTRENUMSOLINOMBRE);
			if (separador != null) {
				numSol = (String) separador.nextToken();
				separador = null;
			}
		}

		return numSol;
	}

	private File reportesPorPartes(String numSolicitudPorPartes, String meses, String descripPeriodo, String filtrarByMesOrComplementoOrAmbos, String coStatus, String companiaAnalista,
			int receptorPago, int coFormaPago1, String tipoEmpleado, int pageReport, String isToCreateReportDefinitivo, boolean conMemorandum, String ninoEspecial) throws Exception {
		ResourceBundle rb = ResourceBundle.getBundle("ve.org.bcv.rhei.util.bundle");
		/** Pagos y tributos */
		String fileOut = "reporte.pdf";
		InputStream jrxml = null;
		// GENERAMOS EL REPORTE
		Reporte reporte = null;
		Map parameters = null;
		ReportePathLogo archivo = null;

		InputStream is = null;

		TipoEmpleado tipoEmpleadoBean = tipoEmpleadoDao.tipoEmpleado(tipoEmpleado);
		String tipoEmpleadoToReporte = "";
		if (null != tipoEmpleadoBean) {
			tipoEmpleadoToReporte = "(" + tipoEmpleadoBean.getTipoEmp() + ") ";
		}

		parameters = new HashMap();
		Parametro parametro = new Parametro();
		// 0 CENTRO DE EDUCACION INICIAL, 1 TRABAJADOR
		if (0 == receptorPago) {
			jrxml = PagoTributoBean.class.getResourceAsStream("reportPagoAndTributosCEI.jrxml");
			int coRepStatus = 0;
			reporte = new ReporteByPagoAtributoCEI(descripPeriodo, companiaAnalista, receptorPago, coFormaPago1, numSolicitudPorPartes.toString(), coStatus, tipoEmpleado, meses,
					filtrarByMesOrComplementoOrAmbos, coRepStatus, isToCreateReportDefinitivo, ninoEspecial);

			parametro = parametroDao.findParametro(companiaAnalista, "EMP", "RHEI", Constantes.NUMCT2, null);
			if (null != parametro) {
				parameters.put("numCta", parametro.getValorParametro());
			}

			if ("1".equals(ninoEspecial)) {
				parameters.put("titulo", rb.getString("reporte.cei.titulo_especial") + " (" + rb.getString("reporte.consulta.titulo") + ")");
			} else if ("0".equals(ninoEspecial)) {
				parameters.put("titulo", rb.getString("reporte.cei.titulo") + " (" + rb.getString("reporte.consulta.titulo") + ")");
			}

		} else if (1 == receptorPago) {
			// 1 TRABAJADOR
			int coRepStatus = 0;
			jrxml = PagoTributoBean.class.getResourceAsStream("reportPagoAndTributosEmp.jrxml");
			reporte = new ReporteByPagoAtributoEmp(descripPeriodo, companiaAnalista, receptorPago, coFormaPago1, numSolicitudPorPartes.toString(), coStatus, tipoEmpleado, meses,
					filtrarByMesOrComplementoOrAmbos, coRepStatus, isToCreateReportDefinitivo, ninoEspecial);

			parametro = parametroDao.findParametro(companiaAnalista, "EMP", "RHEI", Constantes.NUMCT1, null);
			if (null != parametro) {
				parameters.put("numCta", parametro.getValorParametro());
			}

			if ("1".equals(ninoEspecial)) {
				parameters.put("titulo", rb.getString("reporte.cei.trabajador") + tipoEmpleadoToReporte + rb.getString("reporte.cei.trabajador2_E"));
			} else if ("0".equals(ninoEspecial)) {
				parameters.put("titulo", rb.getString("reporte.cei.trabajador") + tipoEmpleadoToReporte + rb.getString("reporte.cei.trabajador2"));
			}

		} else if (2 == receptorPago) {
			// TRABAJADOR Y CENTRO DE EDUCACION INICIAL
			int coRepStatus = 0;
			jrxml = PagoTributoBean.class.getResourceAsStream("reportPagoAndTributosEmp.jrxml");
			reporte = new ReporteByPagoAtributoEmp(descripPeriodo, companiaAnalista, receptorPago, coFormaPago1, numSolicitudPorPartes.toString(), coStatus, tipoEmpleado, meses,
					filtrarByMesOrComplementoOrAmbos, coRepStatus, isToCreateReportDefinitivo, ninoEspecial);

			parametro = parametroDao.findParametro(companiaAnalista, "EMP", "RHEI", Constantes.NUMCT1, null);
			if (null != parametro) {
				parameters.put("numCta", parametro.getValorParametro());
			}
			parameters.put("asunto", rb.getString("reporte.memorando.asunto.emp"));

			parameters.put("titulo", rb.getString("reporte.cei.trabajador") + " (" + rb.getString("reporte.consulta.titulo") + ")");

			// parameters
			// .put("titulo", rb.getString("reporte.cei.titulo")+"("+tipoEmpleadoToReporte+")
			// "+rb.getString("reporte.cei.trabajador2"));

		}
		parametro = parametroDao.findParametro(companiaAnalista, "EMP", "RHEI", Constantes.NOMBREPARAMETRO, null);
		parameters.put("moAporteBcv", "");
		if (null != parametro) {
			// DecimalFormat df2 = new DecimalFormat(Constantes.FORMATO_DOUBLE,
			// new DecimalFormatSymbols(new Locale("es", "VE")));
			// BigDecimal value = new BigDecimal(parametro.getValorParametro());
			// String moAporteBcv= new String(df2.format(value.floatValue()));
			parameters.put("moAporteBcv", parametro.getValorParametro());
		}

		parameters.put("tipoEmpl", "");

		/******** Reporte primeros 10 registros ************/
		showResultToView = reporte.ejecutar();

		archivo = new ReportePathLogo();
		is = archivo.getClass().getResourceAsStream("logo_bcv.jpg");
		parameters.put("logo", is);
		parameters.put("descripPeriodo", descripPeriodo);

		// "#,###,###,##0.00

		// parameters.put("total", showResultToView.getMontoTotal());

		/*** Calculamos el total en el memorando con su formato *******/
		DecimalFormat df2 = new DecimalFormat(Constantes.FORMATO_DOUBLE, new DecimalFormatSymbols(new Locale("es", "VE")));
		BigDecimal value = new BigDecimal(showResultToView.getMontoTotal());
		parameters.put("total", new String(df2.format(value.floatValue())));
		/*** Fin Calculamos el total en el memorando con su formato *******/

		parameters.put("cordAdministrativo", rb.getString("reporte.cordAdministrativo"));
		parametro = parametroDao.findParametro(companiaAnalista, "EMP", "RHEI", Constantes.CORADM, null);
		if (null != parametro) {
			parameters.put("cordAdministrativo", parametro.getValorParametro());
		}

		parameters.put("cordUnidadContabilidad", rb.getString("reporte.cordUnidadContabilidad"));
		parametro = parametroDao.findParametro(companiaAnalista, "EMP", "RHEI", Constantes.CORCON, null);
		if (null != parametro) {
			parameters.put("cordUnidadContabilidad", parametro.getValorParametro());
		}

		parameters.put("cordBenefSocioEconomic", rb.getString("reporte.cordBenefSocioEconomic"));
		parametro = parametroDao.findParametro(companiaAnalista, "EMP", "RHEI", Constantes.CORBEN, null);
		if (null != parametro) {
			parameters.put("cordBenefSocioEconomic", parametro.getValorParametro());
		}

		parameters.put("pageReport", pageReport);

		DocumentosBean documentosBean = reporte.generar(jrxml, parameters, fileOut);
		File fileDetalle = pdf.inputStreamToFile(documentosBean.getInputStream());
		/******** Fin Reporte primeros 10 registros ************/

		/******** Reporte memorando ************/
		reporte = new MemorandoReport();
		InputStream memojrxml= null;
		parameters = new HashMap();
		// 0 CENTRO DE EDUCACION INICIAL, 1 TRABAJADOR
		if (0 == receptorPago) {
			parameters.put("asunto", rb.getString("reporte.memorando.asunto"));
			 memojrxml = PagoTributoBean.class.getResourceAsStream("memorando.jrxml");
		 if ("1".equals(ninoEspecial)) {
			parameters.put("asunto", rb
					.getString("reporte.memorando.asunto_special"));
			 memojrxml = PagoTributoBean.class
					.getResourceAsStream("memorandoEsp.jrxml");}
		}else {
			parameters.put("asunto", rb.getString("reporte.memorando.asunto.emp"));
		    memojrxml = PagoTributoBean.class.getResourceAsStream("memorandoReemb.jrxml");
		}

		archivo = new ReportePathLogo();
		parameters.put("titulo", rb.getString("reporte.memorando.titulo") + " (" + rb.getString("reporte.consulta.titulo") + ")");

		/*** Calculamos el total en el memorando con su formato *******/
		df2 = new DecimalFormat(Constantes.FORMATO_DOUBLE, new DecimalFormatSymbols(new Locale("es", "VE")));
		value = new BigDecimal(showResultToView.getMontoTotal());
		parameters.put("totalmemorando", new String(df2.format(value.floatValue())));
		/*** Fin Calculamos el total en el memorando con su formato *******/
		//
		SimpleDateFormat formateador = new SimpleDateFormat("dd MMMMM yyyy", new Locale("es", "VE"));

		// Esto muestra la fecha actual en pantalla, más o menos así 26/10/2006
		parameters.put("fecha", formateador.format(new Date()));

		//
		archivo = new ReportePathLogo();
		is = archivo.getClass().getResourceAsStream("logo_bcv.jpg");
		parameters.put("logo", is);
		parameters.put("descripPeriodo", descripPeriodo);// parameters.put("descripPeriodo",
															// "2016-2017");
		parameters.put("de", rb.getString("reporte.memorando.de"));
		parameters.put("para", rb.getString("reporte.memorando.para"));

		parameters.put("parrafo2", rb.getString("reporte.memorando.parrafo2"));
		parameters.put("bloque0", rb.getString("reporte.memorando.bloque0"));
		parameters.put("tipoEmpl", rb.getString("reporte.memorando.tipoEmpl"));
		if (tipoEmpleadoBean != null) {
			parameters.put("tipoEmpl", tipoEmpleadoBean.getSiglas());
		}

		parameters.put("bloque1.1", rb.getString("reporte.memorando.bloque1.1"));
		Date fechaActual = new Date();
		SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyy");
		String hoy = formato.format(fechaActual);
		parameters.put("fechadiamesanio", hoy.toString());
		parameters.put("bloque1.2", rb.getString("reporte.memorando.bloque1.2"));
		parameters.put("bloque5.1", rb.getString("reporte.memorando.bloque5.1"));
		parameters.put("bloque5.2", rb.getString("reporte.memorando.bloque5.2"));
		parameters.put("bloque1", rb.getString("reporte.memorando.bloque1"));
		parameters.put("bloque2", rb.getString("reporte.memorando.bloque2"));
		parameters.put("bloque3", rb.getString("reporte.memorando.bloque3"));
		parameters.put("bloque5", rb.getString("reporte.memorando.bloque5"));
		parameters.put("atentamentefirma", rb.getString("reporte.memorando.atentamentefirma"));
		// parameters.put("departamentofirma",
		// rb.getString("reporte.memorando.departamentofirma"));
		parameters.put("pageReport", pageReport);
		com.bcv.model.Parametro firrep = parametroDao.findAbreviaturaFirmasReporte();
		parameters.put("FIRREP", "");
		if (firrep != null && !StringUtils.isEmpty(firrep.getValorParametro())) {
			parameters.put("FIRREP", firrep.getValorParametro());
		}
		parameters.put("departamentofirma", rb.getString("reporte.microUtiles"));
		parametro = parametroDao.findParametro(companiaAnalista, "EMP", "RHEI", Constantes.departamentofirma, null);
		if (null != parametro) {
			parameters.put("departamentofirma", parametro.getValorParametro());
		}
		documentosBean = reporte.generar(memojrxml, parameters, "memorando.pdf");
		File memojrxml1 = pdf.inputStreamToFile(documentosBean.getInputStream());
		/******** Fin Reporte memorando ************/

		/********* Unimos los dos archivos ************************************/
		List<File> addFile = new ArrayList<File>();
		addFile.add(fileDetalle);
		if (conMemorandum) {
			addFile.add(memojrxml1);

		}
		File result = pdf.pegarArchivosPDF(addFile, "reportPagoTributo");
		/********* Fin Unimos los dos archivos ************************************/

		return result;

	}

	private void generarReportePagos(DocumentosBean documentosBean, HttpServletRequest request, RequestDispatcher despachador, HttpServletResponse response, String pagIr)
			throws ServletException, IOException {

		// VARIABLE (SOLACTION) USADA PARA DEVOLVER EL SHOWRESULTTOVIEW

		if (documentosBean != null && documentosBean.getInputStream() != null && documentosBean.getDocFileWithExtension() != null && documentosBean.getDocFileWithExtension() != null) {
			procesarFileShow(documentosBean, response);
		} else {
			showResultToView = new ShowResultToView();
			try {
				showResultToView.setEstadosSolicitudLst(solicitudDao.estadosSolicitudLst());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			showResultToView.setPeriodoEscolares(periodoEscolarDao.tipoPeriodosEscolares());
			showResultToView.setMensaje("fracaso3");
			request.setAttribute("showResultToView", showResultToView);
			despachador = request.getRequestDispatcher(pagIr);
			despachador.forward(request, response);
		}
	}

	private void procesarFileShow(DocumentosBean documentosBean, HttpServletResponse response) throws IOException {

		FileInputStream inStream = null;
		OutputStream outStream = response.getOutputStream();
		File f = null;
		FileWriter escribir = null;
		InputStream is = null;
		try {

			f = fileDesdeStream(documentosBean.getInputStream(), getNameFile(documentosBean.getDocFileWithExtension()), getExtFile(documentosBean.getDocFileWithExtension()));

			response.setContentType(getMimeTypeFile(documentosBean.getDocFileWithExtension()));
			response.setContentLength((int) f.length());

			response.setHeader("Content-disposition", "attachment; filename=\"" + documentosBean.getDocFileWithExtension() + "\"");

			byte[] buf = new byte[8192];
			inStream = new FileInputStream(f);
			int sizeRead = 0;
			while ((sizeRead = inStream.read(buf, 0, buf.length)) > 0) {
				outStream.write(buf, 0, sizeRead);
			}
			inStream.close();
			outStream.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				// Cerramos la conexion
				escribir.flush();
				escribir.close();
			} catch (Exception ignore) {
				// TODO: handle exception
			}
			try {

				inStream.close();
			} catch (Exception ignore) {
				// TODO: handle exception
			}
			try {
				outStream.flush();
				outStream.close();
			} catch (Exception ignore) {
				// TODO: handle exception
			}
			if (f != null && f.exists()) {
				f.delete();
			}

		}

	}

	public final File fileDesdeStream(final InputStream fuente, String nombre, final String extension) throws Exception {
		byte[] buf = new byte[NUM];
		int len;
		File archivo = null;
		OutputStream out = null;
		if (fuente == null) {
			throw new Exception("alfrescoFuenteNula");
		}
		try {
			if (nombre.indexOf(".") != -1) {
				nombre = nombre.substring(0, nombre.indexOf("."));
			}
			archivo = new File(nombre + "." + extension);
			// File.createTempFile(
			// "archivo_" + identificador + "_", "." + extension);
			archivo.deleteOnExit();
			out = new FileOutputStream(archivo);
			while ((len = fuente.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
		} catch (IOException ex) {

		} finally {
			try {
				out.flush();
				out.close();
			} catch (Exception ignore) {
				// TODO: handle exception
			}

		}
		return archivo;
	}

	public static String getExtFile(String fileName) {
		String ext = "";
		if (!isEmptyOrNull(fileName)) {
			int mid = fileName.lastIndexOf(".");
			ext = fileName.substring(mid + 1, fileName.length());
		}
		return ext;
	}

	public static String getNameFile(String fileName) {
		String fname = "";
		if (!isEmptyOrNull(fileName)) {
			int mid = fileName.lastIndexOf(".");
			fname = fileName.substring(0, mid);
		}
		return fname;
	}

	public static boolean isEmptyOrNull(String valor) {
		boolean swVacioCadena = (valor == null || valor.trim().length() == 0 || valor.trim().equalsIgnoreCase("null") || valor.trim().equalsIgnoreCase("#000000"));
		return swVacioCadena;
	}

	public static String getMimeTypeFile(String fileName) throws Exception {
		String mimeType = "";
		boolean sw = false;

		String value = ""; // assume input
		MimeType mimeTypEnum = MimeType.valueOf(getExtFile(fileName)); // surround
																		// with
																		// try/catch

		switch (mimeTypEnum) {
		case pdf:
			mimeType = "application/pdf";
			sw = true;
			break;
		case doc:
			mimeType = "application/msword";
			sw = true;
			break;
		case dot:
			mimeType = "application/msword";
			sw = true;
			break;
		case pps:
			mimeType = "application/mspowerpoint";
			sw = true;
			break;
		case ppt:
			mimeType = "application/mspowerpoint";
			sw = true;
			break;
		case xls:
			mimeType = "application/excel";
			sw = true;
			break;
		case xml:
			mimeType = "text/xml";
			sw = true;
			break;
		case txt:
			mimeType = "text/plain";
			sw = true;
			break;
		}

		if (!sw) {
			throw new Exception("Extension no soportada " + getExtFile(fileName));
		}

		return mimeType;
	}

}
