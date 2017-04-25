/**
 * 
 */
package ve.org.bcv.rhei.controlador;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ve.org.bcv.rhei.bean.FormaPagoValNom;
import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.bean.ValorNombre;
import ve.org.bcv.rhei.negocio.MesesBean;
import ve.org.bcv.rhei.util.Constantes;
import ve.org.bcv.rhei.util.Paginador;

import com.bcv.comparator.SortByValueComparatorAsc;
import com.bcv.dao.jdbc.BeneficioEscolarDao;
import com.bcv.dao.jdbc.ParametroDao;
import com.bcv.dao.jdbc.PeriodoEscolarDao;
import com.bcv.dao.jdbc.ReporteEstatusDao;
import com.bcv.dao.jdbc.SolicitudDao;
import com.bcv.dao.jdbc.TipoEmpleadoDao;
import com.bcv.dao.jdbc.impl.BeneficioEscolarDaoImpl;
import com.bcv.dao.jdbc.impl.ParametroDaoImpl;
import com.bcv.dao.jdbc.impl.PeriodoEscolarDaoImpl;
import com.bcv.dao.jdbc.impl.ReporteEstatusDaoImpl;
import com.bcv.dao.jdbc.impl.SolicitudDaoImpl;
import com.bcv.dao.jdbc.impl.TipoEmpleadoDaoImpl;
import com.bcv.model.ReporteEstatus;
import com.bcv.model.TipoEmpleado;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco 13/10/2015 09:52:26 2015 mail :
 *         oraclefedora@gmail.com
 */
public class ReporteDefinitivoTransitorioControlador extends HttpServlet
		implements
			Serializable {
	private static Logger log = Logger
			.getLogger(ReporteDefinitivoTransitorioControlador.class.getName());

	String mensaje = "";

	String listaDefinitivos0 = "/jsp/listaDefinitivos0.jsp";
	String reporteDefinitvo0 = "/jsp/reporteDefinitvo0.jsp";

	private ServletContext sc;
	private String aviso = "FinSesion";
	private RequestDispatcher rd = null;
	private HttpSession sesion = null;
	private ReporteEstatusDao reporteEstatusDao = new ReporteEstatusDaoImpl();
	private BeneficioEscolarDao beneficioEscolarDao = new BeneficioEscolarDaoImpl();
	private ParametroDao parametroDao = new ParametroDaoImpl();
	private PeriodoEscolarDao periodoEscolarDao = new PeriodoEscolarDaoImpl();
	private TipoEmpleadoDao tipoEmpleadoDao = new TipoEmpleadoDaoImpl();
	private SolicitudDao solicitudDao = new SolicitudDaoImpl();
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void init(ServletConfig config) throws ServletException {
		this.sc = config.getServletContext();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		comun(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		comun(request, response);
	}

	protected void comun(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		ResourceBundle rb = ResourceBundle
				.getBundle("ve.org.bcv.rhei.util.bundle");
		if (request.getSession(false) == null) {
			this.sesion = request.getSession(true);
			this.sesion.setAttribute("aviso", this.aviso);
			this.rd = request.getRequestDispatcher("/jsp/login.jsp");
			this.rd.forward(request, response);
		} else {
			RequestDispatcher despachador = null;
			ShowResultToView showResultToView = new ShowResultToView();
			 this.sesion = request.getSession(false);
			/* Este se exucuta la primer< vez invocada por menu* */
			if ("reportByPagosandTributos".equals(request
					.getParameter("principal.do"))) {
				/**Viene de desincorporar algunas solicitudes del defiitivo*/
				String comeDetalleDefinitivoController=request.getParameter("detalleDefinitivoController")!=null?(String)request.getParameter("detalleDefinitivoController"):null;
				if ("true".equalsIgnoreCase(comeDetalleDefinitivoController)){
					request.setAttribute("reportedefinitivotransitorio", "comeDetalleDefinitivoController");
				}

				/** Se lista la tabla HTML */
				procesarTabla(request, response);
				request.setAttribute("periodoEscolares",
						periodoEscolarDao.tipoPeriodosEscolares());
				String pagIr = "";
				request.setAttribute("showResultToView", showResultToView);
				pagIr = listaDefinitivos0;
				despachador = request.getRequestDispatcher(pagIr);
				despachador.forward(request, response);
			} else if ("listaDeParam".equals(request
					.getParameter("principal.do"))) {
				/** Se recrea la tabla */

				despachador = request
						.getRequestDispatcher("/reporteDefinitivoTransitorioControlador?principal.do=reportByPagosandTributos");
				despachador.forward(request, response);
			} else if ("generarReporte".equals(request
					.getParameter("principal.do"))) {
				despachador = request
						.getRequestDispatcher("/reporteNumSolicitudServlet");
				despachador.forward(request, response);
			} else if ("updateDefinitivoReporte".equals(request
					.getParameter("principal.do"))) {
				String definitivoTransitorioHidden = request
						.getParameter("definitivoTransitorioHidden") != null
						? (String) request
								.getParameter("definitivoTransitorioHidden")
						: "0";
				int coRepStatus = 0;
				try {
					coRepStatus = Integer.parseInt(definitivoTransitorioHidden);
					String nombreUsuario=this.sesion.getAttribute("nombreUsuario")!=null?(String)this.sesion.getAttribute("nombreUsuario"):"";
					reporteEstatusDao.updateDefinitivoReporte(coRepStatus,nombreUsuario);
				} catch (Exception e) {
					log.error(e.toString());
				}

				despachador = request
						.getRequestDispatcher("/reporteDefinitivoTransitorioControlador?principal.do=reportByPagosandTributos");
				despachador.forward(request, response);
			} else if ("deleteTransitorioReporte".equals(request
					.getParameter("principal.do"))) {
				String definitivoTransitorioHidden = request
						.getParameter("definitivoTransitorioHidden") != null
						? (String) request
								.getParameter("definitivoTransitorioHidden")
						: "0";
				int coRepStatus = 0;
				try {
					coRepStatus = Integer.parseInt(definitivoTransitorioHidden);
					reporteEstatusDao.deleteTransitorioReporte(coRepStatus);
				} catch (Exception e) {
					log.error(e.toString());
				}

				despachador = request
						.getRequestDispatcher("/reporteDefinitivoTransitorioControlador?principal.do=reportByPagosandTributos");
				despachador.forward(request, response);
			}
			if ("reportDefinitivo".equals(request.getParameter("principal.do"))) {
				
				List<ValorNombre> statusLst = null;
				statusLst = new ArrayList<ValorNombre>();
				ValorNombre valorNombre = new ValorNombre(
						Constantes.CO_STATUS_ACTIVO, rb.getString("activo"));
				statusLst.add(valorNombre);
				valorNombre = new ValorNombre(Constantes.CO_STATUS_DESINCORPORADO,
						rb.getString("desincorporado"));
				statusLst.add(valorNombre);
				request.setAttribute("statusLst", statusLst);
				
				  valorNombre = null;
				/** Buscamos los tipos de empleados */
				List<ValorNombre> tipoEmpleados = new ArrayList<ValorNombre>();
				try {
					List<TipoEmpleado> tipoEmpleadosBd = tipoEmpleadoDao
							.tipoEmpleadosList();
					valorNombre = null;
					for (TipoEmpleado te : tipoEmpleadosBd) {
						valorNombre = new ValorNombre(te.getTipoEmp(),
								te.getDescripcion());
						tipoEmpleados.add(valorNombre);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.setAttribute("tipoEmpleados", tipoEmpleados);

				/** Buscamos los conceptos de pago */
				List<ValorNombre> receptorPagos = new ArrayList<ValorNombre>();
				valorNombre = null;
				valorNombre = new ValorNombre("0", rb.getString("cei"));
				receptorPagos.add(valorNombre);
				valorNombre = new ValorNombre("1", rb.getString("trabajador"));
				receptorPagos.add(valorNombre);
				showResultToView.setReceptorPagos(receptorPagos);

				/** Buscamos los conceptos de pago */
				// coFormaPago=-1; //1 ES CHEQUE, 2 ES AVISO DE CREDITO
				FormaPagoValNom formaPagoValNom = null;
				List<FormaPagoValNom> formaPagoValNoms = new ArrayList<FormaPagoValNom>();
				List<String> listaFormaPago;
				try {
					listaFormaPago = solicitudDao.cargarFormaPago();
					if ((listaFormaPago != null) && (listaFormaPago.size() > 0)) {
						for (int i = 0; i < listaFormaPago.size(); i += 2) {
							formaPagoValNom = new FormaPagoValNom();
							formaPagoValNom
									.setValor(listaFormaPago.get(i) + "");
							formaPagoValNom.setNombre(listaFormaPago.get(i + 1)
									+ "");
							formaPagoValNoms.add(formaPagoValNom);
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/***Ambos**/
				formaPagoValNom = new FormaPagoValNom();
				formaPagoValNom
						.setValor(Constantes.FORMA_PAGO_AMBOS);
				formaPagoValNom.setNombre(rb.getString("reporte.ambos"));
				formaPagoValNoms.add(formaPagoValNom);
				/****/
				showResultToView.setFormaPagoValNoms(formaPagoValNoms);

				/************** MESES ********/
				MesesBean mesesBean = new MesesBean();
				String[] mes = mesesBean.getMes();
				String[] meses = mesesBean.getMeses();

				List<ValorNombre> listadoMeses = new ArrayList<ValorNombre>();
				valorNombre = null;
				for (int j = 0; j <= 12; j++) {
					valorNombre = new ValorNombre(meses[j], mes[j]);
					listadoMeses.add(valorNombre);
				}
				Collections.sort(listadoMeses, new SortByValueComparatorAsc());
				request.setAttribute("listadoMeses", listadoMeses);

				/*********** FIN MESES ***********/

				String pagIr = "";
				showResultToView.setPeriodoEscolares(periodoEscolarDao
						.tipoPeriodosEscolares());
				if (StringUtils.isEmpty(showResultToView.getPeriodoEscolar())) {
					if (showResultToView.getPeriodoEscolares() != null
							&& !showResultToView.getPeriodoEscolares()
									.isEmpty()
							&& showResultToView.getPeriodoEscolares().size() > 0) {
						showResultToView.setPeriodoEscolar(showResultToView
								.getPeriodoEscolares().get(0).getValor());
					}
				}

				/*****
				 * End******verificamos si viene de reporte definitiovo
				 * transitorio
				 ****/
				if (request.getParameter("vieneFromReporteByPagoAndTributo") != null) {
					String vieneFromReporteByPagoAndTributo = (String) request
							.getParameter("vieneFromReporteByPagoAndTributo");
					if ("1".equalsIgnoreCase(vieneFromReporteByPagoAndTributo)) {
						request.setAttribute(
								"vieneFromReporteByPagoAndTributo",
								vieneFromReporteByPagoAndTributo);
						String nombreDefinitivoTransitorio = request
								.getParameter("nombreDefinitivoTransitorio") != null
								? (String) request
										.getParameter("nombreDefinitivoTransitorio")
								: null;
						request.setAttribute("nombreDefinitivoTransitorio",
								nombreDefinitivoTransitorio);
					}
					/**
					 * Fin*********verificamos si viene de reporte definitiovo
					 * transitorio
					 ****/

					request.setAttribute("showResultToView", showResultToView);
					pagIr = reporteDefinitvo0;
					despachador = request.getRequestDispatcher(pagIr);
					despachador.forward(request, response);
				}
			}
		}

	}

	public void procesarTabla(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String periodoEscolar = request.getParameter("periodoEscolar") != null
				? (String) request.getParameter("periodoEscolar")
				: null;

		/**
		 * Este null, viene de DocumentOpenReport.java, lo colocamos de
		 * parametro periodoEscolar,nombreDefinitivoTransitorio =null para
		 * listar nuevamente la lista de reportes definitivos
		 */
		if ("null".equalsIgnoreCase(periodoEscolar)) {
			periodoEscolar = null;
		}

		String nombreDefinitivoTransitorio = request
				.getParameter("nombreDefinitivoTransitorio") != null
				? request.getParameter("nombreDefinitivoTransitorio")
						.toUpperCase() : null;

		/**
		 * Este null, viene de DocumentOpenReport.java, lo colocamos de
		 * parametro periodoEscolar,nombreDefinitivoTransitorio =null para
		 * listar nuevamente la lista de reportes definitivos
		 */
		if ("null".equalsIgnoreCase(nombreDefinitivoTransitorio)) {
			nombreDefinitivoTransitorio = null;
		}

		int cuantosReg = 0;
		/** numero de Registros que tiene la consulta */
		if (request.getParameter("cuantosReg") != null) {
			cuantosReg = Integer.valueOf(request.getParameter("cuantosReg"));
		} else {
			cuantosReg = reporteEstatusDao.cuantosSql();
		}
		int indMenor = 0;
		if (request.getParameter("indMenor") != null) {
			indMenor = Integer.valueOf(request.getParameter("indMenor"));
		}
		int indMayor = Constantes.paginas;
		if (request.getParameter("indMayor") != null) {
			indMayor = Integer.valueOf(request.getParameter("indMayor"));
		}
		String irPara = request.getParameter("irPara") != null
				? (String) request.getParameter("irPara")
				: "";
		if (!"".equalsIgnoreCase(irPara)) {
			Paginador paginador = new Paginador();
			paginador = paginador.devolverSegunPeticion(indMenor, indMayor,
					cuantosReg, irPara.charAt(0));
			indMenor = paginador.getIndMenor();
			indMayor = paginador.getIndMayor();
		}
		/** BEGIN LISTA DE TODOS LOS PARAMETROS ****************************/
		String tablaParametros = generadorTablaPagosTributos(
				"/reporteDefinitivoTransitorioControlador?principal.do=reportByPagosandTributos",
				nombreDefinitivoTransitorio, indMenor, indMayor,
				periodoEscolar, request);
		/** END LISTA DE TODOS LOS PARAMETROS ****************************/

		request.setAttribute("periodoEscolar", periodoEscolar);
		request.setAttribute("nombreDefinitivoTransitorio",
				nombreDefinitivoTransitorio);
		request.setAttribute("cuantosReg", cuantosReg);
		request.setAttribute("indMenor", indMenor);
		request.setAttribute("indMayor", indMayor);
		request.setAttribute("tablaParametros", tablaParametros);

	}

	public String generadorTablaPagosTributos(String servletIr,
			String filtroParametro, int indMenor, int indMayor,
			String periodoEscolar, HttpServletRequest request) {

		ResourceBundle rb = ResourceBundle
				.getBundle("ve.org.bcv.rhei.util.bundle");
		

		String endTable = "";
		String tabla = "";

		List<ReporteEstatus> reporteStatusLst = null;
		boolean isAllDefinitivos = reporteEstatusDao.isAllDefinitivos();
		request.setAttribute("isAllDefinitivos", isAllDefinitivos);
		if (isAllDefinitivos) {
			/**SON LOS REPORTES DEFINITIVO*/
			reporteStatusLst = reporteEstatusDao.searchReporteEstatus(
					periodoEscolar, filtroParametro, indMenor, indMayor);

		} else {
			/**SON LOS REPORTES QUE FALTAN POR FIRMAR PARA SER STATUS DEFINITIVO*/
			reporteStatusLst = reporteEstatusDao.searchTransitorio();
		}
   boolean isPrimeravez=true;
		if ((reporteStatusLst != null) && (reporteStatusLst.size() != 0)) {
			for (ReporteEstatus re : reporteStatusLst) {
			
				if (isPrimeravez){
					String encabezado = "<table class=\"anchoTabla8\"><caption align=\"top\">"
							+ ""
							+ "</caption><tr><th width=\"40%\">"
							+ rb.getString("nombreReport")
							+ "</th><th width=\"25%\">"
							+ rb.getString("periodo")
							+ "</th><th width=\"15%\">"
							+ rb.getString("reporteDetalle")
							+ "</th><th width=\"10%\" class=\"centrado\">"
							+ rb.getString("usuario")
							+ "</th>"
							+"<th width=\"5%\" class=\"centrado\">"
								+ rb.getString("visualizar") + "</th>";
//					previo de subir definitivo
					if ("0".equalsIgnoreCase(re.getStatus())) {
						encabezado+="<th width=\"5%\" class=\"centrado\">"
								+ rb.getString("subir") + "</th>"
								+"<th width=\"5%\" class=\"centrado\">"
								+ rb.getString("repo.cancelar") + "</th>";
								
					}else if ("1".equalsIgnoreCase(re.getStatus())) {
//						Es el definitivo						
						encabezado+="<th width=\"10%\" class=\"centrado\">"
								+ rb.getString("rep.repcontable") + "</th>";
					}
							
					tabla = encabezado+"</tr>";
					isPrimeravez=false;
				}
				
				
				
				

				tabla = tabla + "<tr><td>" + re.getNombre() + "</td>" + "<td>"
						+ re.getTxDescriPeriodo() + "</td>";
				
//				reporteDetalle reparamos el reporte,  generarDetalleDefinitivoController
				tabla += "		<td><a href=\"#d\" "
						+ "			onclick=\"javascript: generarDetalleDefinitivoController("
						+ re.getCoReporstatus()
						+ "); return false;\"> "
						+ "				Detalles"
						+ "		</a></td> "
						+ "<td>" + re.getNbCreadoPor() + "</td>";
//				previo de subir definitivo
				if ("0".equalsIgnoreCase(re.getStatus())) {
					tabla += "		<td><a href=\"#d\" "
							+ "			onclick=\"javascript: generarReportePagoTributo("
							+ re.getCoReporstatus()
							+ "); return false;\"> "
							+ "				<input type=\"image\" src=\"imagenes/add_attachment.gif\" "
							+ "				alt=\"download\" /> "
							+ "		</a></td> "
							+ "		<td><a href=\"#d\" "
							+ "			onclick=\"javascript: updateDefinitivoReporte("
							+ re.getCoReporstatus()
							+ "); return false;\"> "
							+ "				<input type=\"image\" src=\"imagenes/CheckIn_icon.gif\" "
							+ "				alt=\"download\" /> "
							+ "		</a></td> "
							+ "		<td><a href=\"#d\" "
							+ "			onclick=\"javascript: deleteTransitorioReporte("
							+ re.getCoReporstatus()
							+ "); return false;\"> "
							+ "				<input type=\"image\" src=\"imagenes/deploy_failed.gif\" "
							+ "				alt=\"download\" /> " + "		</a></td> ";
				}
				if ("1".equalsIgnoreCase(re.getStatus())) {
//					Es el definitivo
					tabla += "		<td><a href=\"#d\" "
							+ "			onclick=\"javascript: generarReportePagoTributo("
							+ re.getCoReporstatus()
							+ "); return false;\"> "
							+ "				<input type=\"image\" src=\"imagenes/pdf.png\" alt=\"download\" /> "
							+ "		</a></td> ";
					tabla += "		<td><input type=\"checkbox\"   name=\"reporteContableCheck\" value=\""+re.getCoReporstatus()+"\" /></td> ";
					                                                      
					
				}

				tabla += "</tr>";
			}

			tabla = tabla + "</table>";

		} else {

			tabla = tabla

					+ "<tr><td colspan=3 class=aviso>"
					+ rb.getString("reporte.definitivo.transitorio.nohayregistro")
					+ "</td> ";
			tabla += "</tr>";
			tabla = tabla + "</table>";
		}

		endTable = "<table><tr><th  style=\"background: white;\" width=\"44%\"></th><th  style=\"background: white;\" width=\"3%\"><a href=\"javascript:void(0)\"  onclick=\"paginandoReporteDefinitivo('p','"
				+ servletIr
				+ "','"
				+ filtroParametro
				+ "')\" ><input type=\"image\" src=\"/rhei/imagenes/arrow-first.gif\" alt=\"Primero\" /></a></th><th style=\"background: white;\" width=\"3%\" ><a  href=\"javascript:void(0)\"  onclick=\"paginandoReporteDefinitivo('a','"
				+ servletIr
				+ "','"
				+ filtroParametro
				+ "')\" ><input type=\"image\" src=\"/rhei/imagenes/arrow-fr.gif\" alt=\"Anterior\" /></a></th><th style=\"background: white;\" width=\"3%\"><a href=\"javascript:void(0)\"  onclick=\"paginandoReporteDefinitivo('s','"
				+ servletIr
				+ "','"
				+ filtroParametro
				+ "')\" ><input type=\"image\" src=\"/rhei/imagenes/arrow-ff.gif\" alt=\"Siguiente\" /></a></th><th style=\"background: white;\" width=\"3%\"><a href=\"javascript:void(0)\"  onclick=\"paginandoReporteDefinitivo('u','"
				+ servletIr
				+ "','"
				+ filtroParametro
				+ "')\" ><input type=\"image\" src=\"/rhei/imagenes/arrow-last.gif\" alt=\"Ultimo\" /></a></th><th  style=\"background: white;\" width=\"44%\"></th></tr>";
		endTable += "</table>";

		if (isAllDefinitivos) {
			tabla += "<br>" + endTable;
		} else {
			tabla += "<br>";
		}
		/****** Si son todos definitivos, podemos seguir realizando reportes *******/

		return tabla;
	}
	
	///////////////////////////////DENTRO DE ESTE BLOQUE CREADO POR DAVID VELASQUEZ/////////////////////////////////////
	
	
	
	///////////////////////////////HASTA AQUÍ LO CREADO POR DAVID VELASQUEZ/////////////////////////////////////
	
	
	

	private void primeravez(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		List<String> listaBeneficio = new ArrayList<String>();
		listaBeneficio = beneficioEscolarDao.buscarBeneficiosEscolares();
		request.setAttribute("listaBeneficio", listaBeneficio);
		request.setAttribute("tabla", "parametro");
	}

}
