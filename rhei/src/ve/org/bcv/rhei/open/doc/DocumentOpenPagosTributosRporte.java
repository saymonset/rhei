/**
 * 
 */
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ve.org.bcv.rhei.bean.DocumentosBean;
import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.report.ReportePathLogo;
import ve.org.bcv.rhei.report.by.benef.Reporte;
import ve.org.bcv.rhei.util.Constantes;
import ve.org.bcv.rhei.util.pdf;

import com.bcv.dao.jdbc.ParametroDao;
import com.bcv.dao.jdbc.PeriodoEscolarDao;
import com.bcv.dao.jdbc.RelacionDePagosDao;
import com.bcv.dao.jdbc.SolicitudDao;
import com.bcv.dao.jdbc.TipoEmpleadoDao;
import com.bcv.dao.jdbc.impl.ParametroDaoImpl;
import com.bcv.dao.jdbc.impl.PeriodoEscolarDaoImpl;
import com.bcv.dao.jdbc.impl.RelacionDePagosDaoImpl;
import com.bcv.dao.jdbc.impl.ReporteEstatusDaoImpl;
import com.bcv.dao.jdbc.impl.SolicitudDaoImpl;
import com.bcv.dao.jdbc.impl.TipoEmpleadoDaoImpl;
import com.bcv.model.Parametro;
import com.bcv.model.ReporteByDefinitivoOrTransitivo;
import com.bcv.model.ReporteEstatus;
import com.bcv.model.TipoEmpleado;
import com.bcv.reporte.pagotributo.MemorandoReport;
import com.bcv.reporte.pagotributo.PagoTributoBean;
import com.bcv.reporte.pagotributo.ReporteByPagoAtributoCEI;
import com.bcv.reporte.pagotributo.ReporteByPagoAtributoEmp;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco 13/10/2015 15:24:38 2015 mail :
 *         oraclefedora@gmail.com
 */
public class DocumentOpenPagosTributosRporte extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(DocumentOpenRporte.class
			.getName());
	private ServletContext sc;
	private SolicitudDao solicitudDao = new SolicitudDaoImpl();
	private PeriodoEscolarDao periodoEscolarDao = new PeriodoEscolarDaoImpl();
	private TipoEmpleadoDao tipoEmpleadoDao = new TipoEmpleadoDaoImpl();
	private RelacionDePagosDao relacionDePagosDaoImpl = new RelacionDePagosDaoImpl();
	private ReporteEstatusDaoImpl reporteEstatusDao = new ReporteEstatusDaoImpl();

	private ParametroDao parametroDao = new ParametroDaoImpl();

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
	public DocumentOpenPagosTributosRporte() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		process(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	private void process(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(false);

		RequestDispatcher rd = null;

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

			ResourceBundle rb = ResourceBundle
					.getBundle("ve.org.bcv.rhei.util.bundle");

			/** Consultamos.. */
			try {

				int keyReport = request.getParameter("keyReport") != null ? Integer
						.parseInt(request.getParameter("keyReport")) : 0;
				Reporte reporte = null;

				String fileOut = "reporte.pdf";
				InputStream jrxml = null;
				Map parameters = null;
				ReportePathLogo archivo = null;
				InputStream is = null;
				String coStatus = null;
				boolean reporteOriginal = false;
				/** HACEMOS EL REPORTE */
				switch (keyReport) {
				case 1:
					String definitivoTransitorioHidden = request
							.getParameter("definitivoTransitorioHidden") != null ? (String) request
							.getParameter("definitivoTransitorioHidden") : "0";
					int defTranstCodigo = 0;
					try {
						defTranstCodigo = Integer
								.parseInt(definitivoTransitorioHidden);
						fileOut = reporteEstatusDao
								.nombreReporte(defTranstCodigo) + ".pdf";
					} catch (Exception e) {
						// TODO: handle exception
					}

					/** Si es consulta individual */
					String numSolicitud = request.getParameter("nroSolicitud");

					if (StringUtils.isEmpty(numSolicitud)) {
						try {
							/** Si no es consulta individual */
							numSolicitud = relacionDePagosDaoImpl
									.listNumSolicitudsReporteDefinitivoTransitorio(defTranstCodigo);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					}
					String ninoEspecial = request.getParameter("ninoEspecial") != null ? (String) request
							.getParameter("ninoEspecial") : "";
					if (StringUtils.isEmpty(ninoEspecial)) {

						ninoEspecial = solicitudDao
								.regularOrtEspecial(numSolicitud);

					}
					DocumentosBean documentosBean = new DocumentosBean();
					RequestDispatcher despachador = null;
					String pag = request.getParameter("accion") != null ? (String) request
							.getParameter("accion") : "";
					String pagIr = "/jsp/" + pag;

					/** Periodo escolar **/
					String periodoEscolar = null;
					if (!StringUtils.isEmpty(request
							.getParameter("periodoEscolar"))) {
						periodoEscolar = request.getParameter("periodoEscolar");
					}

					String status = request.getParameter("status") != null ? (String) request
							.getParameter("status") : null;
					// Begin checkOrAvisoCredito
					String coFormaPago = request.getParameter("coFormaPago") != null ? (String) request
							.getParameter("coFormaPago") : null;
					int checkOrAvisoCredito = -1;
					if (!StringUtils.isEmpty(coFormaPago)
							&& StringUtils.isNumeric(coFormaPago)) {
						checkOrAvisoCredito = new Integer(coFormaPago);
					}
					// End checkOrAvisoCredito

					// Begin receptorPago
					String receptorPagoStr = request
							.getParameter("receptorPago") != null ? (String) request
							.getParameter("receptorPago") : null;
					int receptorPago = -1;
					if (!StringUtils.isEmpty(receptorPagoStr)
							&& StringUtils.isNumeric(receptorPagoStr)) {
						receptorPago = new Integer(receptorPagoStr);
					}
					// End receptorPago

					String tipoEmpleado = request.getParameter("tipoEmpleado") != null ? (String) request
							.getParameter("tipoEmpleado") : null;
					String companiaAnalista = session
							.getAttribute("companiaAnalista") != null ? (String) request
							.getSession().getAttribute("companiaAnalista")
							: "01";

					StringTokenizer stk = new StringTokenizer(numSolicitud, ",");
					StringBuilder numSolicitudPorPartes = new StringBuilder("");
					int i = 0;
					File result = null;
					List<File> addAllFile = new ArrayList<File>();
					boolean putComma = false;
					int numerRegistros = new Integer(
							rb.getString("reporte.numero.registro"));
					int pageReport = 1;

					while (stk.hasMoreTokens()) {
						i++;
						String numSoli = null;
						if (!putComma) {
							/**
							 * getNumSolicitudOnly->Separamos el numero de
							 * solicitud del nombre con el separador
							 * Constantes.SEPARADORENTRENUMSOLINOMBRE
							 ***/
							numSoli = getNumSolicitudOnly((String) stk
									.nextToken());
							numSolicitudPorPartes.append(numSoli);
							putComma = true;
						} else {
							/**
							 * getNumSolicitudOnly->Separamos el numero de
							 * solicitud del nombre con el separador
							 * Constantes.SEPARADORENTRENUMSOLINOMBRE
							 ***/
							numSoli = getNumSolicitudOnly((String) stk
									.nextToken());
							numSolicitudPorPartes.append(",");
							numSolicitudPorPartes.append(numSoli);

						}
						/** Cada 10 veces se genera un reporte **/
						if ((i - numerRegistros) == 0) {
							/** AQUI SE GENERA EL DOCUMENTO */
							boolean updateParameter = true;
							result = reportesPorPartes(
									numSolicitudPorPartes.toString(),
									companiaAnalista, defTranstCodigo,
									ninoEspecial, updateParameter);
							addAllFile.add(result);
							i = 0;
							numSolicitudPorPartes = new StringBuilder("");
							putComma = false;
							pageReport++;
						}
					}
					if (!StringUtils.isEmpty(numSolicitudPorPartes)) {
						/** AQUI SE GENERA EL DOCUMENTO */
						boolean updateParameter = true;
						result = reportesPorPartes(
								numSolicitudPorPartes.toString(),
								companiaAnalista, defTranstCodigo,
								ninoEspecial, updateParameter);
						addAllFile.add(result);
					}

					File resultTotal = pdf.pegarArchivosPDF(addAllFile,
							"reportPagoTributo");
					log.info("resultTotal.getAbsoluteFile()="
							+ resultTotal.getAbsoluteFile());

					/****** Total del reporte *********/
					documentosBean.setInputStream(pdf
							.FileToInputStream(resultTotal));
					documentosBean.setDocFileWithExtension(fileOut);
					generarReportePagos(documentosBean, request, despachador,
							response, pagIr, reporteOriginal);
					/****** Fin Total del reporte *********/

					break;

				case 5:

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
			StringTokenizer separador = new StringTokenizer(
					separadorentrenumsolinombre,
					Constantes.SEPARADORENTRENUMSOLINOMBRE);
			if (separador != null) {
				numSol = (String) separador.nextToken();
				separador = null;
			}
		}

		return numSol;
	}

	public ShowResultToView reportesPorPartesShowResultToView(
			String numSolicitudPorPartes, String companiaAnalista,
			int coRepStatus, String ninoEspecial) throws Exception {
		boolean updateParameter = false;
		showResultToView = new ShowResultToView();
		reportesPorPartes(numSolicitudPorPartes, companiaAnalista, coRepStatus,
				ninoEspecial, updateParameter);
		return showResultToView;
	}

	/**
	 * Generamos el reporte a partir de las solicitudes
	 * 
	 * @param numSolicitudPorPartes
	 * @return
	 * @throws Exception
	 */
	private File reportesPorPartes(String numSolicitudPorPartes,
			String companiaAnalista, int coRepStatus, String ninoEspecial, 
			boolean updateParameterGenerarReporte) throws Exception {
		ResourceBundle rb = ResourceBundle
				.getBundle("ve.org.bcv.rhei.util.bundle");
		int pageReport = 0;
		File result = null;
		if (numSolicitudPorPartes != null
				&& !StringUtils.isEmpty(numSolicitudPorPartes)) {
			/** Obtenemos un numero de solicitud del grupo */
			StringTokenizer stkNumberSolicitud = new StringTokenizer(
					numSolicitudPorPartes, ",");
			String numSoliStr = "";
			if (stkNumberSolicitud.hasMoreTokens()) {
				numSoliStr = (String) stkNumberSolicitud.nextToken();
			}

			ReporteByDefinitivoOrTransitivo reporteByDefinitivoOrTransitivo = relacionDePagosDaoImpl
					.reporteByDefinitivoOrTransitivo(numSolicitudPorPartes,
							coRepStatus);
			if (reporteByDefinitivoOrTransitivo == null) {
				reporteByDefinitivoOrTransitivo = new ReporteByDefinitivoOrTransitivo();
			}
			reporteByDefinitivoOrTransitivo.isToCreateReportDefinitivo();
			String isToCreateReportDefinitivo = Constantes.PAGADO_NOPAGADO_AMBOS;
			String meses = reporteByDefinitivoOrTransitivo.getMeses();
			String descripPeriodo = reporteByDefinitivoOrTransitivo
					.getDescripPeriodo();
			int receptorPago = reporteByDefinitivoOrTransitivo
					.getReceptorPago();
			int coFormaPago1 = reporteByDefinitivoOrTransitivo
					.getCoFormaPago1();
			String coStatus = "";
			String tipoEmpleado = reporteByDefinitivoOrTransitivo
					.getTipoEmpleado();
			String tipoEmpleadoToReporte = "";
			if ("EMP".equalsIgnoreCase(tipoEmpleado)
					|| "CON".equalsIgnoreCase(tipoEmpleado)
					|| "JUB".equalsIgnoreCase(tipoEmpleado)
					|| "EJE".equalsIgnoreCase(tipoEmpleado)) {
				tipoEmpleado = "EMP,CON,JUB,EJE";
				tipoEmpleadoToReporte = "EMP";
			} else if ("OBR".equalsIgnoreCase(tipoEmpleado)
					|| "OBC".equalsIgnoreCase(tipoEmpleado)) {
				tipoEmpleado = "OBR,OBC";
				tipoEmpleadoToReporte = "OBR";
			} else if ("SOB".equalsIgnoreCase(tipoEmpleado)) {
				tipoEmpleado = "SOB";
				tipoEmpleadoToReporte = "SOB";
			}

			Parametro parametro = null;
			if ("OBR,OBC".equalsIgnoreCase(tipoEmpleado)) {
				parametro = parametroDao.findParametro(companiaAnalista, "EMP",
						"RHEI", "SEC_OBR", null);
				pageReport = Integer.parseInt(parametro.getValorParametro());
				parametro.setValorParametro((pageReport + 1) + "");
				if (updateParameterGenerarReporte) {
					parametroDao.guardarParametro("modificar",
							parametro.getCodigoCompania(),
							parametro.getTipoEmpleado(),
							parametro.getTipoBeneficio(),
							parametro.getCodigoParametro(),
							parametro.getFechaVigencia(),
							parametro.getValorParametro(),
							parametro.getTipoDatoParametro(),
							parametro.getObservaciones());
				}

			} else {
				parametro = parametroDao.findParametro(companiaAnalista, "EMP",
						"RHEI", "SEC_EMP", null);
				pageReport = Integer.parseInt(parametro.getValorParametro());
				parametro.setValorParametro((pageReport + 1) + "");
				if (updateParameterGenerarReporte) {
					parametroDao.guardarParametro("modificar",
							parametro.getCodigoCompania(),
							parametro.getTipoEmpleado(),
							parametro.getTipoBeneficio(),
							parametro.getCodigoParametro(),
							parametro.getFechaVigencia(),
							parametro.getValorParametro(),
							parametro.getTipoDatoParametro(),
							parametro.getObservaciones());
				}

			}

			/**
			 * filtrarByMesOrComplementoOrAmbos: 0 es matricla , 1 es reembolso
			 * y 2 es ambos
			 **/
			String filtrarByMesOrComplementoOrAmbos = Constantes.FILTRARBYMESORCOMPLEMENTOORAMBOS_AMBOS;

			/** Pagos y tributos */
			String fileOut = "reporte.pdf";
			InputStream jrxml = null;
			// GENERAMOS EL REPORTE
			Reporte reporte = null;
			Map parameters = null;
			ReportePathLogo archivo = null;

			InputStream is = null;

			TipoEmpleado tipoEmpleadoBean = tipoEmpleadoDao
					.tipoEmpleado(tipoEmpleado);

			parameters = new HashMap();

			// 0 CENTRO DE EDUCACION INICIAL, 1 TRABAJADOR
			if (0 == receptorPago) {
				jrxml = PagoTributoBean.class
						.getResourceAsStream("reportPagoAndTributosCEI.jrxml");
				reporte = new ReporteByPagoAtributoCEI(descripPeriodo,
						companiaAnalista, receptorPago, coFormaPago1,
						numSolicitudPorPartes.toString(), coStatus,
						tipoEmpleado, meses, filtrarByMesOrComplementoOrAmbos,
						coRepStatus, isToCreateReportDefinitivo, ninoEspecial);
				parametro = new Parametro();
				parametro = parametroDao.findParametro(companiaAnalista, "EMP",
						"RHEI", Constantes.NUMCT2, null);
				if (null != parametro) {
					parameters.put("numCta", parametro.getValorParametro());
				}

				if ("1".equals(ninoEspecial)) {

					parameters
							.put("titulo",
									rb.getString("reporte.cei.titulo_especial")
											+ "("
											+ tipoEmpleadoToReporte
											+ ") "
											+ rb.getString("reporte.cei.trabajador2_E"));
				} else if ("0".equals(ninoEspecial)) {

					parameters.put(
							"titulo",
							rb.getString("reporte.cei.titulo") + "("
									+ tipoEmpleadoToReporte + ") "
									+ rb.getString("reporte.cei.trabajador2"));

				}

				parameters.put("asunto",
						rb.getString("reporte.memorando.asunto"));

			} else {
				// 1 TRABAJADOR
				jrxml = PagoTributoBean.class.getResourceAsStream("reportPagoAndTributosEmp.jrxml");
				                                                   
				reporte = new ReporteByPagoAtributoEmp(descripPeriodo,
						companiaAnalista, receptorPago, coFormaPago1,
						numSolicitudPorPartes.toString(), coStatus,
						tipoEmpleado, meses, filtrarByMesOrComplementoOrAmbos,
						coRepStatus, isToCreateReportDefinitivo, ninoEspecial);
				parametro = new Parametro();
				parametro = parametroDao.findParametro(companiaAnalista, "EMP",
						"RHEI", Constantes.NUMCT1, null);
				if (null != parametro) {
					parameters.put("numCta", parametro.getValorParametro());
				}

				if ("1".equals(ninoEspecial)) {
					parameters
							.put("titulo",
									rb.getString("reporte.cei.trabajador")
											+ "("
											+ tipoEmpleadoToReporte
											+ ") "
											+ rb.getString("reporte.cei.trabajador2_E"));

				} else if ("0".equals(ninoEspecial)) {
					parameters.put(
							"titulo",
							rb.getString("reporte.cei.trabajador") + "("
									+ tipoEmpleadoToReporte + ") "
									+ rb.getString("reporte.cei.trabajador2"));
				}

				parameters.put("asunto",
						rb.getString("reporte.memorando.asunto.emp"));

			}
			
			parameters.put("tipoEmpl", null!=tipoEmpleadoToReporte?tipoEmpleadoToReporte.toLowerCase():"");

			/******** Reporte primeros 10 registros ************/
			showResultToView = reporte.ejecutar();

			/** Si vamos agenerar el reporte.. pasa por aca.. */
			if (updateParameterGenerarReporte) {
				archivo = new ReportePathLogo();
				is = archivo.getClass().getResourceAsStream("logo_bcv.jpg");
				parameters.put("logo", is);
				parameters.put("descripPeriodo", descripPeriodo);

				/*** Calculamos el total en el memorando con su formato *******/
				DecimalFormat df2 = new DecimalFormat(
						Constantes.FORMATO_DOUBLE, new DecimalFormatSymbols(
								new Locale("es", "VE")));
				BigDecimal value = new BigDecimal(0d);
				 try {
						 value = new BigDecimal(
								showResultToView.getMontoTotal());
						parameters.put("total",
								new String(df2.format(value.floatValue())));
					
				} catch (Exception e) {
					parameters.put("total",
							"0");

				}
				/*** Fin Calculamos el total en el memorando con su formato *******/

				ReporteEstatus reporteStatus = reporteEstatusDao
						.reporteStatus(coRepStatus);

				parameters.put("cordAdministrativo",
						rb.getString("reporte.cordAdministrativo"));

				parametro = parametroDao.findParametro(companiaAnalista, "EMP",
						"RHEI", Constantes.CORADM, null);
				if (null != reporteStatus
						&& !StringUtils.isEmpty(reporteStatus
								.getNbCoordAdminist())) {
					parameters.put("cordAdministrativo",
							reporteStatus.getNbCoordAdminist());
				} else if (null != parametro) {
					parameters.put("cordAdministrativo",
							parametro.getValorParametro());
				}

				parameters.put("cordUnidadContabilidad",
						rb.getString("reporte.cordUnidadContabilidad"));
				parametro = parametroDao.findParametro(companiaAnalista, "EMP",
						"RHEI", Constantes.CORCON, null);
				if (null != reporteStatus
						&& !StringUtils.isEmpty(reporteStatus
								.getNbUnidadContabil())) {
					parameters.put("cordUnidadContabilidad",
							reporteStatus.getNbUnidadContabil());
				} else if (null != parametro) {
					parameters.put("cordUnidadContabilidad",
							parametro.getValorParametro());
				}

				parameters.put("cordBenefSocioEconomic",
						rb.getString("reporte.cordBenefSocioEconomic"));
				parametro = parametroDao.findParametro(companiaAnalista, "EMP",
						"RHEI", Constantes.CORBEN, null);
				if (null != reporteStatus
						&& !StringUtils.isEmpty(reporteStatus
								.getNbCoordBenefSoc())) {
					parameters.put("cordBenefSocioEconomic",
							reporteStatus.getNbCoordBenefSoc());
				} else if (null != parametro) {
					parameters.put("cordBenefSocioEconomic",
							parametro.getValorParametro());
				}
				parameters.put("pageReport", pageReport);

				/** Buscamos el monto global del BCV SUELDO MINIMO **/
				/*** Calculamos el total en el memorando con su formato *******/

				parametro = parametroDao.findParametro(companiaAnalista, "EMP",
						"RHEI", Constantes.NOMBREPARAMETRO, null);
				parameters.put("moAporteBcv", "");
				if (null != parametro) {
					parameters
							.put("moAporteBcv", parametro.getValorParametro());
				}

				/** Fin **/
				
				

				DocumentosBean documentosBean = reporte.generar(jrxml,
						parameters, fileOut);
				File fileDetalle = pdf.inputStreamToFile(documentosBean
						.getInputStream());
				
				
				
				
				
				
				
				/******** Fin Reporte primeros 10 registros ************/
				/******** Fin Reporte primeros 10 registros ************/
				/******** Fin Reporte primeros 10 registros ************/
				/******** Fin Reporte primeros 10 registros ************/

				/******** Reporte memorando ****COPIA ********/
				reporte = new MemorandoReport();
				InputStream memojrxml= null;
				parameters = new HashMap();

				// 0 CENTRO DE EDUCACION INICIAL, 1 TRABAJADOR
				if (0 == receptorPago) {
					if ("1".equals(ninoEspecial)) {
						parameters.put("asunto", rb
								.getString("reporte.memorando.asunto_special"));
						 memojrxml = PagoTributoBean.class
								.getResourceAsStream("memorandoEsp.jrxml");
						 parameters.put("bloqueEsp1",
									rb.getString("reporte.memorando.bloqueEsp1"));
					} else if ("0".equals(ninoEspecial)) {
						parameters.put("asunto",
								rb.getString("reporte.memorando.asunto"));
						memojrxml = PagoTributoBean.class
								.getResourceAsStream("memorando.jrxml");
					}
				} else {
					parameters.put("asunto",
							rb.getString("reporte.memorando.asunto.emp"));
					 memojrxml = PagoTributoBean.class
							.getResourceAsStream("memorandoReemb.jrxml");
				}

				archivo = new ReportePathLogo();
				parameters.put("titulo",
						rb.getString("reporte.memorando.titulo"));

				/*** Calculamos el total en el memorando con su formato *******/
				df2 = new DecimalFormat(Constantes.FORMATO_DOUBLE,
						new DecimalFormatSymbols(new Locale("es", "VE")));
				value = new BigDecimal(showResultToView.getMontoTotal());
				parameters.put("totalmemorando",
						new String(df2.format(value.floatValue())));
				/*** Fin Calculamos el total en el memorando con su formato *******/
				archivo = new ReportePathLogo();
				is = archivo.getClass().getResourceAsStream("logo_bcv.jpg");
				parameters.put("logo", is);
				parameters.put("descripPeriodo", descripPeriodo);// parameters.put("descripPeriodo",
																	// "2016-2017");
				parameters.put("de", rb.getString("reporte.memorando.de"));
				parameters.put("para", rb.getString("reporte.memorando.para"));

				parameters.put("parrafo2",
						rb.getString("reporte.memorando.parrafo2"));
				parameters.put("bloque0",
						rb.getString("reporte.memorando.bloque0"));
				parameters.put("tipoEmpl",
						rb.getString("reporte.memorando.tipoEmpl")+ 
						tipoEmpleadoToReporte + rb.getString("reporte.cei.trabajador2"));
				
					
				if (tipoEmpleadoBean != null) {
					parameters.put("tipoEmpl", null!=tipoEmpleadoBean.getSiglas()?tipoEmpleadoBean.getSiglas().toLowerCase():"");
				}

				parameters.put("bloque1.1",
						rb.getString("reporte.memorando.bloque1.1"));
				parameters.put("bloque1.2",
						rb.getString("reporte.memorando.bloque1.2"));
				parameters.put("bloque5.1",
						rb.getString("reporte.memorando.bloque5.1"));
				parameters.put("bloque5.2",
						rb.getString("reporte.memorando.bloque5.2"));
				parameters.put("bloque1",
						rb.getString("reporte.memorando.bloque1"));
				parameters.put("bloque2",
						rb.getString("reporte.memorando.bloque2"));
				parameters.put("bloque3",
						rb.getString("reporte.memorando.bloque3"));
				parameters.put("bloque5",
						rb.getString("reporte.memorando.bloque5"));

				parameters.put("FIRREP", "");

				if (reporteStatus != null) {
					if (!StringUtils.isEmpty(reporteStatus.getNbAbrevReporte())) {
						parameters.put("FIRREP",
								reporteStatus.getNbAbrevReporte());
					}
					if (!StringUtils.isEmpty(reporteStatus.getNbFirmaReporte())) {
						parameters.put("atentamentefirma",
								reporteStatus.getNbFirmaReporte());
					}

				}

 
				parameters.put("pageReport", pageReport);
				
				parameters.put("departamentofirma", rb.getString("reporte.microUtiles"));
				parametro = parametroDao.findParametro(companiaAnalista, "EMP", "RHEI", Constantes.departamentofirma, null);
					if (null != parametro) {
					parameters.put("departamentofirma", parametro.getValorParametro());
				}

				SimpleDateFormat formateador = new SimpleDateFormat(
						"dd MMMMM yyyy", new Locale("es", "VE"));

				// Esto muestra la fecha actual en pantalla, más o menos así
				// 26/10/2006
				parameters.put("fecha", formateador.format(new Date()));

				Date fechaActual = new Date();
				SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyy");
				String hoy = formato.format(fechaActual);
				parameters.put("fechadiamesanio", hoy.toString());
				documentosBean = reporte.generar(memojrxml, parameters,
						"memorando.pdf");
				File memojrxml1 = pdf.inputStreamToFile(documentosBean
						.getInputStream());
				/******** Fin Reporte memorando ****COPIA ********/

				/******** Reporte memorando ****SIN COPIA ********/
				reporte = new MemorandoReport();
				
				parameters = new HashMap();

				// 0 CENTRO DE EDUCACION INICIAL, 1 TRABAJADOR
				if (0 == receptorPago) {
					if ("1".equals(ninoEspecial)) {
						parameters.put("asunto", rb
								.getString("reporte.memorando.asunto_special"));
						 memojrxml = PagoTributoBean.class
								.getResourceAsStream("memorandoEsp.jrxml");
						 parameters.put("bloqueEsp1",
									rb.getString("reporte.memorando.bloqueEsp1"));
					} else if ("0".equals(ninoEspecial)) {
						parameters.put("asunto",
								rb.getString("reporte.memorando.asunto"));
						memojrxml = PagoTributoBean.class
								.getResourceAsStream("memorando.jrxml");
					}
				} else {
					parameters.put("asunto",
							rb.getString("reporte.memorando.asunto.emp"));
					 memojrxml = PagoTributoBean.class
							.getResourceAsStream("memorandoReemb.jrxml");
				}

				archivo = new ReportePathLogo();
				parameters.put("titulo",
						rb.getString("reporte.memorando.titulo"));

				/*** Calculamos el total en el memorando con su formato *******/
				df2 = new DecimalFormat(Constantes.FORMATO_DOUBLE,
						new DecimalFormatSymbols(new Locale("es", "VE")));
				value = new BigDecimal(showResultToView.getMontoTotal());
				parameters.put("totalmemorando",
						new String(df2.format(value.floatValue())));
				/*** Fin Calculamos el total en el memorando con su formato *******/
				archivo = new ReportePathLogo();
				is = archivo.getClass().getResourceAsStream("logo_bcv.jpg");
				parameters.put("logo", is);
				parameters.put("descripPeriodo", descripPeriodo);// parameters.put("descripPeriodo",
																	// "2016-2017");
				parameters.put("de", rb.getString("reporte.memorando.de"));
				parameters.put("para", rb.getString("reporte.memorando.para"));

				parameters.put("parrafo2",
						rb.getString("reporte.memorando.parrafo2"));
				parameters.put("bloque0",
						rb.getString("reporte.memorando.bloque0"));
				parameters.put("tipoEmpl",
						rb.getString("reporte.memorando.tipoEmpl"));
				if (tipoEmpleadoBean != null) {
					parameters.put("tipoEmpl",
							null!=tipoEmpleadoBean.getDescripcion()?tipoEmpleadoBean.getDescripcion().toLowerCase():"");
				}

				parameters.put("bloque1.1",
						rb.getString("reporte.memorando.bloque1.1"));
				parameters.put("bloque1.2",
						rb.getString("reporte.memorando.bloque1.2"));
				parameters.put("bloque5.1",
						rb.getString("reporte.memorando.bloque5.1"));
				parameters.put("bloque5.2",
						rb.getString("reporte.memorando.bloque5.2"));
				parameters.put("bloque1",
						rb.getString("reporte.memorando.bloque1"));
				parameters.put("bloque2",
						rb.getString("reporte.memorando.bloque2"));
				parameters.put("bloque3",
						rb.getString("reporte.memorando.bloque3"));
				parameters.put("bloque5",
						rb.getString("reporte.memorando.bloque5"));

				parameters.put("FIRREP", "");
				reporteStatus = reporteEstatusDao.reporteStatus(coRepStatus);
				if (reporteStatus != null) {

					if (!StringUtils.isEmpty(reporteStatus.getNbFirmaReporte())) {
						parameters.put("atentamentefirma",
								reporteStatus.getNbFirmaReporte());
					}

				}

 
				parameters.put("pageReport", pageReport);
				
				parameters.put("departamentofirma", rb.getString("reporte.microUtiles"));
				parametro = parametroDao.findParametro(companiaAnalista, "EMP", "RHEI", Constantes.departamentofirma, null);
					if (null != parametro) {
					parameters.put("departamentofirma", parametro.getValorParametro());
				}

				formateador = new SimpleDateFormat("dd MMMMM yyyy", new Locale(
						"es", "VE"));

				// Esto muestra la fecha actual en pantalla, más o menos así
				// 26/10/2006
				parameters.put("fecha", formateador.format(new Date()));

				fechaActual = new Date();
				formato = new SimpleDateFormat("ddMMyyyy");
				hoy = formato.format(fechaActual);
				parameters.put("fechadiamesanio", hoy.toString());
				documentosBean = reporte.generar(memojrxml, parameters,
						"memorando.pdf");
				File memojrxml2 = pdf.inputStreamToFile(documentosBean
						.getInputStream());
				/******** Fin Reporte memorando *SIN COPIA ***********/

				/********* Unimos los dos archivos ************************************/
				List<File> addFile = new ArrayList<File>();
				addFile.add(fileDetalle);
				addFile.add(memojrxml1);
				addFile.add(memojrxml2);
				result = pdf.pegarArchivosPDF(addFile, "reportPagoTributo");
				/********* Fin Unimos los dos archivos *************SIN COPIA ************************/
			}

		}

		return result;

	}

	private void generarReportePagos(DocumentosBean documentosBean,
			HttpServletRequest request, RequestDispatcher despachador,
			HttpServletResponse response, String pagIr, boolean reporteOriginal)
			throws ServletException, IOException {

		// VARIABLE (SOLACTION) USADA PARA DEVOLVER EL SHOWRESULTTOVIEW

		if (documentosBean != null && documentosBean.getInputStream() != null
				&& documentosBean.getDocFileWithExtension() != null
				&& documentosBean.getDocFileWithExtension() != null) {
			procesarFileShow(documentosBean, response, reporteOriginal);
		} else {
			showResultToView = new ShowResultToView();
			try {
				showResultToView.setEstadosSolicitudLst(solicitudDao
						.estadosSolicitudLst());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			showResultToView.setPeriodoEscolares(periodoEscolarDao
					.tipoPeriodosEscolares());
			showResultToView.setMensaje("fracaso3");
			request.setAttribute("showResultToView", showResultToView);
			despachador = request.getRequestDispatcher(pagIr);
			despachador.forward(request, response);
		}
	}

	private void procesarFileShow(DocumentosBean documentosBean,
			HttpServletResponse response, boolean reporteOriginal)
			throws IOException {
		FileInputStream inStream = null;
		OutputStream outStream = response.getOutputStream();
		File f = null;
		FileWriter escribir = null;
		InputStream is = null;
		try {

			f = fileDesdeStream(documentosBean.getInputStream(),
					getNameFile(documentosBean.getDocFileWithExtension()),
					getExtFile(documentosBean.getDocFileWithExtension()));

			response.setContentType(getMimeTypeFile(documentosBean
					.getDocFileWithExtension()));
			response.setContentLength((int) f.length());

			response.setHeader("Content-disposition", "attachment; filename=\""
					+ documentosBean.getDocFileWithExtension() + "\"");

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

	public final File fileDesdeStream(final InputStream fuente, String nombre,
			final String extension) throws Exception {
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
		boolean swVacioCadena = (valor == null || valor.trim().length() == 0
				|| valor.trim().equalsIgnoreCase("null") || valor.trim()
				.equalsIgnoreCase("#000000"));
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
			throw new Exception("Extension no soportada "
					+ getExtFile(fileName));
		}

		return mimeType;
	}

}
