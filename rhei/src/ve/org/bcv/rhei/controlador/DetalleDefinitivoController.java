/**
 * 
 */
package ve.org.bcv.rhei.controlador;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ve.org.bcv.rhei.bean.DocumentosBean;
import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.open.doc.DocumentOpenRporte;
import ve.org.bcv.rhei.report.ReportePathLogo;
import ve.org.bcv.rhei.report.by.benef.Reporte;
import ve.org.bcv.rhei.util.Constantes;

import com.bcv.dao.jdbc.DefinititvoHistoricoDao;
import com.bcv.dao.jdbc.ParametroDao;
import com.bcv.dao.jdbc.PeriodoEscolarDao;
import com.bcv.dao.jdbc.RelacionDePagosDao;
import com.bcv.dao.jdbc.SolicitudDao;
import com.bcv.dao.jdbc.TipoEmpleadoDao;
import com.bcv.dao.jdbc.impl.DefinititvoHistoricoDaoImpl;
import com.bcv.dao.jdbc.impl.ParametroDaoImpl;
import com.bcv.dao.jdbc.impl.PeriodoEscolarDaoImpl;
import com.bcv.dao.jdbc.impl.RelacionDePagosDaoImpl;
import com.bcv.dao.jdbc.impl.SolicitudDaoImpl;
import com.bcv.dao.jdbc.impl.TipoEmpleadoDaoImpl;
import com.bcv.model.DetalleDefHistorico;
import com.bcv.model.ReporteByDefinitivoOrTransitivo;
import com.bcv.model.TipoEmpleado;
import com.bcv.reporte.pagotributo.PagoTributoBean;
import com.bcv.reporte.pagotributo.PagoTributoBeanEmp;
import com.bcv.reporte.pagotributo.PagoTributoCEI;
import com.bcv.reporte.pagotributo.ReporteByPagoAtributoCEI;
import com.bcv.reporte.pagotributo.ReporteByPagoAtributoEmp;
import com.bcv.services.DetalleDefinititvoService;
import com.bcv.servicesImpl.DetalleDefinititvoServiceImpl;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco 18/08/2016 14:35:49 2016 mail :
 *         oraclefedora@gmail.com
 */
@WebServlet(description = "DetalleDefinitivoController", urlPatterns = {"/detalleDefinitivoController"})
public class DetalleDefinitivoController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(DocumentOpenRporte.class
			.getName());
	private ServletContext sc;
	private SolicitudDao solicitudDao = new SolicitudDaoImpl();
	private PeriodoEscolarDao periodoEscolarDao = new PeriodoEscolarDaoImpl();
	private TipoEmpleadoDao tipoEmpleadoDao = new TipoEmpleadoDaoImpl();
	private RelacionDePagosDao relacionDePagosDaoImpl = new RelacionDePagosDaoImpl();
	
	private DefinititvoHistoricoDao definititvoHistoricoDao = new DefinititvoHistoricoDaoImpl();

	private ParametroDao parametroDao = new ParametroDaoImpl();
	private DetalleDefinititvoService detalleDefinititvoService = new DetalleDefinititvoServiceImpl();

	private enum MimeType {
		pdf, doc, dot, pps, ppt, xl, xls, xml, txt;
	}
	private ShowResultToView showResultToView;
	/**
	 * Tamaño del Buffer por defecto.
	 */
	private static final int NUM = 512;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DetalleDefinitivoController() {
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
			
			
			if (("1"
					.equals(request.getParameter("keyReport")))) {
				/** Consultamos.. */
				try {

					int keyReport = request.getParameter("keyReport") != null
							? Integer.parseInt(request.getParameter("keyReport"))
							: 0;
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
						case 1 :
							String definitivoTransitorioHidden = request
									.getParameter("definitivoTransitorioHidden") != null
									? (String) request
											.getParameter("definitivoTransitorioHidden")
									: "0";
							int coRepStatus = 0;
							try {
								coRepStatus = Integer
										.parseInt(definitivoTransitorioHidden);
							} catch (Exception e) {
								// TODO: handle exception
							}

							/** Si es consulta individual */
							String numSolicitud = request
									.getParameter("nroSolicitud");

							if (StringUtils.isEmpty(numSolicitud)) {
								try {
									/** Si no es consulta individual */
									numSolicitud = relacionDePagosDaoImpl
											.listNumSolicitudsReporteDefinitivoTransitorio(coRepStatus);
								} catch (SQLException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}

							}
							String ninoEspecial = request
									.getParameter("ninoEspecial") != null
									? (String) request.getParameter("ninoEspecial")
									: "";
							if (StringUtils.isEmpty(ninoEspecial)) {

								ninoEspecial = solicitudDao
										.regularOrtEspecial(numSolicitud);

							}

							RequestDispatcher despachador = null;
							String pag = request.getParameter("accion") != null
									? (String) request.getParameter("accion")
									: "";
							String pagIr = "/jsp/" + pag;

							String companiaAnalista = session
									.getAttribute("companiaAnalista") != null
									? (String) request.getSession().getAttribute(
											"companiaAnalista")
									: "01";
							int pageReport = 1;
							List<PagoTributoBean> list = reportesPorPartes(numSolicitud,
									companiaAnalista, pageReport, coRepStatus,
									reporteOriginal, ninoEspecial);
							List<DetalleDefHistorico> listaHistorico=definititvoHistoricoDao.select( coRepStatus+"");
							 session.setAttribute("listaHistorico",listaHistorico);
							 session.setAttribute("lista",list);
							 request.setAttribute("coRepStatus", coRepStatus);
							request.setAttribute("lista", list);
							pagIr = "/jsp/detalleDefHistorico.jsp";
							despachador = request.getRequestDispatcher(pagIr);
							despachador.forward(request, response);

							break;

						case 5 :

							break;
						default :

							break;
					}

					/** FIN HACEMOS EL REPORTE */

				} catch (Exception e) {
					e.printStackTrace();
				}			
			}else if ("eliminarDefinitivo".equalsIgnoreCase(request.getParameter("principal.do"))) {
				String[] numeroDesactivar= request.getParameterValues("numeroDesactivar");
				String coRepStatus = request.getParameter("coRepStatus")!=null?(String)request.getParameter("coRepStatus"):null;
				String observacion = request.getParameter("observacion")!=null?(String)request.getParameter("observacion"):null;
				List<PagoTributoBean> lista =session.getAttribute("lista")!=null?(List<PagoTributoBean>)session.getAttribute("lista"):null;
				try {
					
					detalleDefinititvoService.updateReporteStatusDeshacer(observacion,coRepStatus,lista, numeroDesactivar);
					//
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				RequestDispatcher despachador = null;
				despachador = request.getRequestDispatcher("/reporteDefinitivoTransitorioControlador?principal.do=reportByPagosandTributos&detalleDefinitivoController=true");
				try {
					despachador.forward(request, response);
				} catch (ServletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

//			if (("1"
//					.equals(request.getParameter("keyReport")))) {

		
		}
	}
	
	

	/**
	 * Generamos el reporte a partir de las solicitudes
	 * 
	 * @param numSolicitudPorPartes
	 * @return
	 * @throws Exception
	 */
	private List<PagoTributoBean> reportesPorPartes(String numSolicitudPorPartes,
			String companiaAnalista, int pageReport, int coRepStatus,
			boolean reporteCopia, String ninoEspecial) throws Exception {
		ResourceBundle rb = ResourceBundle
				.getBundle("ve.org.bcv.rhei.util.bundle");
		List<PagoTributoBean> list = new ArrayList<PagoTributoBean>();

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

			/**
			 * filtrarByMesOrComplementoOrAmbos: 0 es matricla , 1 es reembolso
			 * y 2 es ambos
			 **/
			String filtrarByMesOrComplementoOrAmbos = Constantes.FILTRARBYMESORCOMPLEMENTOORAMBOS_AMBOS;
			// reporteByDefinitivoOrTransitivo
			// .getFiltrarByMesOrComplementoOrAmbos() + "";

			/** Pagos y tributos */

			TipoEmpleado tipoEmpleadoBean = tipoEmpleadoDao
					.tipoEmpleado(tipoEmpleado);

			// 0 CENTRO DE EDUCACION INICIAL, 1 TRABAJADOR
			if (0 == receptorPago) {

				ReporteByPagoAtributoCEI reporteCEI = new ReporteByPagoAtributoCEI(
						descripPeriodo, companiaAnalista, receptorPago,
						coFormaPago1, numSolicitudPorPartes.toString(),
						coStatus, tipoEmpleado, meses,
						filtrarByMesOrComplementoOrAmbos, coRepStatus,
						isToCreateReportDefinitivo, ninoEspecial);
				reporteCEI.ejecutar();
				List<?> list2 = reporteCEI.getList();
				for (Object p : list2) {
					if (p instanceof PagoTributoCEI) {
						PagoTributoCEI p1 = (PagoTributoCEI) p;
						DecimalFormat df2 = new DecimalFormat(
								Constantes.FORMATO_DOUBLE,
								new DecimalFormatSymbols(new Locale("es", "VE")));
						BigDecimal value = new BigDecimal(p1.getMonto());
						p1.setMontoTotalStr(df2.format(value.floatValue()));;
						list.add(p1);
					}

				}

			} else {
				// 1 TRABAJADOR

				ReporteByPagoAtributoEmp reporteEmp = new ReporteByPagoAtributoEmp(
						descripPeriodo, companiaAnalista, receptorPago,
						coFormaPago1, numSolicitudPorPartes.toString(),
						coStatus, tipoEmpleado, meses,
						filtrarByMesOrComplementoOrAmbos, coRepStatus,
						isToCreateReportDefinitivo, ninoEspecial);
				reporteEmp.ejecutar();

				List<?> list2 = reporteEmp.getList();
				for (Object p : list2) {
					if (p instanceof PagoTributoBean) {
						PagoTributoBean p1 = (PagoTributoBean) p;
						DecimalFormat df2 = new DecimalFormat(
								Constantes.FORMATO_DOUBLE,
								new DecimalFormatSymbols(new Locale("es", "VE")));
						BigDecimal value = new BigDecimal(p1.getMonto());
						p1.setMontoTotalStr(df2.format(value.floatValue()));;
						list.add(p1);
					} 

				}

			}
		}

		return list;

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
			case pdf :
				mimeType = "application/pdf";
				sw = true;
				break;
			case doc :
				mimeType = "application/msword";
				sw = true;
				break;
			case dot :
				mimeType = "application/msword";
				sw = true;
				break;
			case pps :
				mimeType = "application/mspowerpoint";
				sw = true;
				break;
			case ppt :
				mimeType = "application/mspowerpoint";
				sw = true;
				break;
			case xls :
				mimeType = "application/excel";
				sw = true;
				break;
			case xml :
				mimeType = "text/xml";
				sw = true;
				break;
			case txt :
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
