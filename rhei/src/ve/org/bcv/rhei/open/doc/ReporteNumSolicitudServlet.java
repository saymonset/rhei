package ve.org.bcv.rhei.open.doc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

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
import ve.org.bcv.rhei.bean.Recaudo;
import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.negocio.SolAction;
import ve.org.bcv.rhei.negocio.SolConsultarSearchInfoPartI;
import ve.org.bcv.rhei.report.ReportePathLogo;
import ve.org.bcv.rhei.report.by.benef.Reporte;
import ve.org.bcv.rhei.util.Constantes;

import com.bcv.dao.jdbc.CentroEducacionInicialDao;
import com.bcv.dao.jdbc.CompaniaDao;
import com.bcv.dao.jdbc.EmpleadoInfoDAO;
import com.bcv.dao.jdbc.FamiliarDao;
import com.bcv.dao.jdbc.PeriodoEscolarDao;
import com.bcv.dao.jdbc.SolicitudDao;
import com.bcv.dao.jdbc.TrabajadorDao;
import com.bcv.dao.jdbc.impl.CentroEducacionInicialDaoImpl;
import com.bcv.dao.jdbc.impl.CompaniaDaoImpl;
import com.bcv.dao.jdbc.impl.ConyugeTrabajoDaoImpl;
import com.bcv.dao.jdbc.impl.EmpleadoInfoDAOImpl;
import com.bcv.dao.jdbc.impl.FamiliarDaoImpl;
import com.bcv.dao.jdbc.impl.PeriodoEscolarDaoImpl;
import com.bcv.dao.jdbc.impl.SolicitudDaoImpl;
import com.bcv.dao.jdbc.impl.TrabajadorDaoImpl;
import com.bcv.model.CentroEducacionInicial;
import com.bcv.model.ConyugeTrabajo;
import com.bcv.model.EmpleadoInfo;
import com.bcv.model.Familiar;
import com.bcv.model.Trabajador;
import com.bcv.reporte.by.solicitud.ReporteBySolicitud;
import com.fileupload.ListRecaudos;

/**
 * Servlet implementation class DocumentOpen
 */
public class ReporteNumSolicitudServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger
			.getLogger(ReporteNumSolicitudServlet.class.getName());
	private ServletContext sc;
	private SolicitudDao solicitudDao = new SolicitudDaoImpl();
	private TrabajadorDao trabajadorDao = new TrabajadorDaoImpl();
	private PeriodoEscolarDao periodoEscolarDao = new PeriodoEscolarDaoImpl();
	private FamiliarDao familiarDao = new FamiliarDaoImpl();
	private CompaniaDao companiaDao = new CompaniaDaoImpl();
	private EmpleadoInfoDAO empleadoDireccionDAO = new EmpleadoInfoDAOImpl();
	private CentroEducacionInicialDao centroEducacionInicialDao = new CentroEducacionInicialDaoImpl();
	private ConyugeTrabajoDaoImpl conyugeTrabajoDaoImpl = new ConyugeTrabajoDaoImpl();

	private enum MimeType {
		pdf, doc, dot, pps, ppt, xl, xls, xml, txt;
	}
	private ShowResultToView showResultToView;
	/**
	 * Tama√±o del Buffer por defecto.
	 */
	private static final int NUM = 512;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ReporteNumSolicitudServlet() {
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

			String companiaAnalista = session.getAttribute("companiaAnalista") != null
					? (String) request.getSession().getAttribute(
							"companiaAnalista")
					: "01";

			DocumentosBean documentosBean = new DocumentosBean();
			RequestDispatcher despachador = null;
			String pag = request.getParameter("accion") != null
					? (String) request.getParameter("accion")
					: "";
			String pagIr = "/jsp/" + pag;

			String periodoEscolar = request
					.getParameter("periodoEscolarHidden") != null
					? (String) request.getParameter("periodoEscolarHidden")
					: null;
			String cedula = request.getParameter("cedulaHidden") != null
					? (String) request.getParameter("cedulaHidden")
					: null;
			String codigoBenef = request.getParameter("codigoBenefHidden") != null
					? (String) request.getParameter("codigoBenefHidden")
					: null;
			String nroRifCentroEdu = request
					.getParameter("nroRifCentroEduHidden") != null
					? (String) request.getParameter("nroRifCentroEduHidden")
					: null;

			log.info("periodoEscolar=" + periodoEscolar);
			log.info("cedula=" + cedula);
			log.info("codigoBenef=" + codigoBenef);
			log.info("nroRifCentroEdu=" + nroRifCentroEdu);
			/** Consultamos.. */
			try {

				int keyReport = request.getParameter("keyReport") != null
						? Integer.parseInt(request.getParameter("keyReport"))
						: 0;
				Reporte reporte = null;
				/** HACEMOS EL REPORTE */
				switch (keyReport) {

					case 1 :
						/** Rporte por Solicitud */
						/** Consultamos la solicitud */
						String codEmp = trabajadorDao
								.codigoEmpleadoBycedula(new Integer(cedula));

						int nroSolicitudNotKnow = 0;
						SolAction solAction = new SolConsultarSearchInfoPartI(
								cedula, codEmp, codigoBenef, companiaAnalista,
								nroRifCentroEdu, nroSolicitudNotKnow);
						showResultToView = solAction.ejecutar();
						Familiar familiar = null;
						if (!StringUtils.isEmpty(codigoBenef)) {
							familiar = familiarDao
									.consultarFamiliarBycedulaFamiliar(new Integer(
											codigoBenef));
						}

						/** Fin Consultamos la solicitud */
						/** Consultamos los recaudos */
						StringBuilder path = new StringBuilder("");
						path.append(periodoEscolar).append("/")
								.append(nroRifCentroEdu).append("/")
								.append(cedula).append("/").append(codigoBenef);
						log.info("path=" + path);

						List<Recaudo> lista = new ArrayList<Recaudo>();
						ListRecaudos listRecaudos = new ListRecaudos();
						boolean titleComeResourcebundle = false;
						lista = listRecaudos.listRecaudos(path.toString(),
								titleComeResourcebundle);
						showResultToView.setRecaudos(lista);
						reporte = new ReporteBySolicitud(showResultToView);
						/**
						 * Si la longitud del colegion es menor a dos, implica
						 * que falta el numero de rif y emn el reporte sera
						 * vacio
						 */
						if (showResultToView != null
								&& showResultToView.getNroRifCentroEdu() != null
								&& showResultToView.getNroRifCentroEdu()
										.length() < 2) {
							showResultToView.setNroRifCentroEdu("");
						}
						/** Fin Consultamos los recaudos */
						/** Generamos el reporte */
						generarReporteSolicitud(reporte, solAction,
								showResultToView, documentosBean, request,
								despachador, response, pagIr, familiar);

						break;
					default :

						break;
				}

				/** FIN HACEMOS EL REPORTE */

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private void generarReporteSolicitud(Reporte reporte, SolAction solAction,
			ShowResultToView showResultToView, DocumentosBean documentosBean,
			HttpServletRequest request, RequestDispatcher despachador,
			HttpServletResponse response, String pagIr, Familiar familiar)
			throws ServletException, IOException {
		ResourceBundle rb = ResourceBundle
				.getBundle("ve.org.bcv.rhei.util.bundle");
		InputStream jrxml = ReporteBySolicitud.class
				.getResourceAsStream("reportSolicitudDatosPrincipales1.jrxml");
		String fileOut = "reporteSolicitud.pdf";
		int cedu = 0;
		if (!StringUtils.isEmpty(showResultToView.getCedula())) {
			cedu = Integer.parseInt(showResultToView.getCedula().trim());
		}
		EmpleadoInfo empleadoDireccion = null;
		Familiar datoBasicosConyuge = null;
		try {
			empleadoDireccion = empleadoDireccionDAO.empleadoDireccion(cedu);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Map parameters = new HashMap();

		/** Inicio Conyuge */
		int numSolicitudInt = 0;
		if (StringUtils.isNumeric(showResultToView.getNumSolicitud())) {
			numSolicitudInt = new Integer(showResultToView.getNumSolicitud());
		}
		ConyugeTrabajo conyugeTrabajo = null;
		try {
			conyugeTrabajo = conyugeTrabajoDaoImpl.find(cedu);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Trabajador trabajador = null;
		if (!StringUtils.isEmpty(showResultToView.getCedula())
				&& StringUtils.isNumeric(showResultToView.getCedula())) {

			try {
				datoBasicosConyuge = familiarDao.consultarConyuge(new Long(
						showResultToView.getCedula()));
				trabajador = trabajadorDao.buscarTrabajador(new Integer(
						showResultToView.getCedula()));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			parameters.put("nombreconyuge", "");
			parameters.put("apellidoconyuge", "");
			if (datoBasicosConyuge != null
					&& !StringUtils.isEmpty(datoBasicosConyuge.getNombre())) {
				parameters.put("nombreconyuge", datoBasicosConyuge.getNombre()
						.trim()); //toLowerCase() borrado por David Velasquez
				request.setAttribute("nameconyuge",
						datoBasicosConyuge.getNombre());
			}
			if (datoBasicosConyuge != null
					&& !StringUtils.isEmpty(datoBasicosConyuge.getApellido())) {
				parameters.put("apellidoconyuge", datoBasicosConyuge
						.getApellido());//toLowerCase() borrado por David Velasquez
				request.setAttribute("apellidoconyuge",
						datoBasicosConyuge.getApellido());
			}
			if (datoBasicosConyuge != null
					&& !StringUtils.isEmpty(datoBasicosConyuge
							.getCedulaFamiliar())) {
				parameters.put("ciConyuge",
						datoBasicosConyuge.getCedulaFamiliar());
				request.setAttribute("cedulaconyuge",
						datoBasicosConyuge.getCedulaFamiliar() + "");

			}

					
			parameters.put("cedulaconyuge", "");
			if (conyugeTrabajo != null) {
				parameters.put("cedulaconyuge", conyugeTrabajo.getCiConyuge());

				request.setAttribute("conyugeTrabajo", conyugeTrabajo);
			}

		}
		/** Fin Conyuge */

		if (!StringUtils.isEmpty(showResultToView.getNivelEscolaridad())
				&& StringUtils
						.isNumeric(showResultToView.getNivelEscolaridad())) {
			parameters.put("nivelEscolaridad", nivelEscolaridaNombre(Integer
					.parseInt(showResultToView.getNivelEscolaridad())));
		}

		if (showResultToView.getMontoMatricula() != null
				&& !StringUtils.isEmpty(showResultToView.getMontoMatricula()
						+ "") && showResultToView.getMontoMatricula() > 0) {
			parameters.put("matricula", showResultToView.getMontoMatricula());

		}
		if (showResultToView.getMontoPeriodo() != null
				&& !StringUtils
						.isEmpty(showResultToView.getMontoPeriodo() + "")
				&& showResultToView.getMontoPeriodo() > 0) {
			parameters.put("mensualidad", showResultToView.getMontoPeriodo());

		}
		/*** Buscamos la residencia *********/
		parameters.put("localidadResidencia", "");
		if (empleadoDireccion != null) {
			String nbUrb = empleadoDireccion.getNbUrbanizacion() != null
					? (String) empleadoDireccion.getNbUrbanizacion().trim()
							.toLowerCase()
					: "";
			String calle = empleadoDireccion.getNbCalleEsq() != null
					? (String) empleadoDireccion.getNbCalleEsq().trim()
							.toLowerCase()
					: "";
			String vivienda = empleadoDireccion.getNbVivienda() != null
					? (String) empleadoDireccion.getNbVivienda().trim()
							.toLowerCase()
					: "";
			parameters.put("localidadResidencia", nbUrb + " " + calle + " "
					+ vivienda);
		}
		try {
			empleadoDireccion = empleadoDireccionDAO.empleadoDireccion(cedu);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		/***** Fecha de contrato si la tiene ******/
		EmpleadoInfo empleadoFechaContrato = null;
		try {
			empleadoFechaContrato = empleadoDireccionDAO.contratoFechas(cedu);
			if (("CON".equalsIgnoreCase(trabajador.getTipoEmpleadoCod()))) {
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				Date fechaHasta = null;
				Date fechaDesde = null;
				if (empleadoFechaContrato != null) {
					if (empleadoFechaContrato.getDesde() != null) {
						fechaDesde = empleadoFechaContrato.getDesde();
					}
					if (empleadoFechaContrato.getHasta() != null) {
						fechaHasta = empleadoFechaContrato.getHasta();
					}
					parameters.put(
							"vigenciaContrato",
							format.format(fechaDesde) + " - "
									+ format.format(fechaHasta));
				}

			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		/**** Obtenemos el centro de educacion inicial, info sobre este ********/
		CentroEducacionInicial centroEducacionInicial = null;
		try {
			centroEducacionInicial = centroEducacionInicialDao
					.infoCentroEducativo(showResultToView.getNroRif());
			if (centroEducacionInicial != null) {
				parameters.put("direccionColegio",
						centroEducacionInicial.getDireccionCentro());
				StringBuilder tlf1 = new StringBuilder("");
				tlf1.append(centroEducacionInicial.getTelefono());
				if (!StringUtils.isEmpty(tlf1.toString())) {
					tlf1.append("-");
				}
				tlf1.append(centroEducacionInicial.getTelefono2());
				if (!StringUtils.isEmpty(tlf1.toString())) {
					parameters.put("tlfCentro", tlf1.toString());
				}

			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ReportePathLogo archivo = new ReportePathLogo();
		InputStream is = archivo.getClass().getResourceAsStream("logo_bcv.jpg");
		parameters.put("logo", is);
		/** Si es contratado , colocamos el checkbox */
		if (trabajador != null) {
			if (("CON".equalsIgnoreCase(trabajador.getTipoEmpleadoCod()))) {
				is = archivo.getClass().getResourceAsStream("chekListOn.jpg");
				parameters.put("imgContrato", is);
			} else {
				is = archivo.getClass().getResourceAsStream("chekListOff.jpg");
				parameters.put("imgContrato", is);
			}
		}
		/** Colocamos si es primera vez o renovacion */
		try {
			if (solicitudDao.isRenovacion(numSolicitudInt)) {
				is = archivo.getClass().getResourceAsStream("chekListOn.jpg");
				parameters.put("imgRenovacion", is);
			} else {
				is = archivo.getClass().getResourceAsStream("chekListOn.jpg");
				parameters.put("imgPrimeravez", is);
			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/** Fin Colocamos si es primera vez o renovacion */
		
//		  && centroEducacionInicial.getTipoEducacion()==Constantes.tipoEduc

		String titulo = "";
		String recaudo = "";
		String recaudo1 = "";
		String recaudo2 = "";
		String recaudo3 = "";
		String recaudo4 = "";
		String recaudo5 = "";
		String recaudo6 = "";
		
 
		String tipoEduc=showResultToView.getTipoEducacion();

		if ("E".equalsIgnoreCase(tipoEduc) || familiar != null && familiar.getEdad() >= Constantes.EDAD_ESPECIAL) {
			titulo = rb.getString("solicitud.titulo.especial");
			recaudo = rb.getString("solicitud.recaudo.titulo.especial");
			recaudo1 = rb.getString("solicitud.recaudo1.especial");
			recaudo2 = rb.getString("solicitud.recaudo2.especial");
			recaudo3 = rb.getString("solicitud.recaudo3.especial");
			recaudo4 = rb.getString("solicitud.recaudo4.especial");
			recaudo5 = rb.getString("solicitud.recaudo5.especial");
			recaudo6 = rb.getString("solicitud.recaudo6.especial");
			is = archivo.getClass().getResourceAsStream("imgblanco.jpg");
			parameters.put("imgblanco", is);
		} else {
			titulo = rb.getString("solicitud.titulo.normal");
			recaudo = rb.getString("solicitud.recaudo.titulo.normal");
			recaudo1 = rb.getString("solicitud.recaudo1.normal");
			recaudo2 = rb.getString("solicitud.recaudo2.normal");
			recaudo3 = rb.getString("solicitud.recaudo3.normal");
			recaudo4 = rb.getString("solicitud.recaudo4.normal");
			recaudo5 = rb.getString("solicitud.recaudo5.normal");
			recaudo6 = rb.getString("solicitud.recaudo6.normal");
			is = archivo.getClass().getResourceAsStream("imgrecaudo.jpg");
			parameters.put("imgrecaudo", is);
 
		}
		parameters.put("titulo", titulo);
		parameters.put("recaudo", recaudo);
		parameters.put("recaudo1", recaudo1);
		parameters.put("recaudo2", recaudo2);
		parameters.put("recaudo3", recaudo3);
		parameters.put("recaudo4", recaudo4);
		parameters.put("recaudo5", recaudo5);
		parameters.put("recaudo6", recaudo6);
		
		
		
	
		/** Cambiamos los recaudos seg˙n su tipo de educacion Inicial o Especial */

		// GENERAMOS EL REPORTE
		reporte.generar(jrxml, parameters, fileOut);
		// VARIABLE (SOLACTION) USADA PARA DEVOLVER EL SHOWRESULTTOVIEW
		showResultToView = reporte.ejecutar();
		documentosBean = showResultToView.getDocumentosBean();
		if (documentosBean != null && documentosBean.getInputStream() != null
				&& documentosBean.getDocFileWithExtension() != null
				&& documentosBean.getDocFileWithExtension() != null) {

			procesarFileShow(documentosBean, response);
		} else {
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

	private String nivelEscolaridaNombre(int nivel) {
		String name = "";
		switch (nivel) {
			case 0 :
				name = "Maternal";
				break;
			case 1 :
				name = "1er. Nivel";
				break;
			case 2 :
				name = "2do. Nivel";
				break;
			case 3 :
				name = "3er. Nivel";
				break;
			case 4 :
				name = "Educ. Basica";
				break;
			case 5 :
				name = "Educ. Media";
				break;
			case 6 :
				name = "Universitario";
				break;
			case 7 :
				name = "Otros";
				break;
			 
			default :
				name = "Maternal";
				break;
		}

		return name;
	}

	private void procesarFileShow(DocumentosBean documentosBean,
			HttpServletResponse response) throws IOException {

		FileInputStream inStream = null;
		OutputStream outStream = response.getOutputStream();
		File f = null;
		FileWriter escribir = null;
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
