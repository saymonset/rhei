package ve.org.bcv.rhei.controlador;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.inject.Named;
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

import ve.org.bcv.rhei.bean.BeneficiarioValNom;
import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.negocio.SolAction;
import ve.org.bcv.rhei.negocio.SolConsultarSearchInfoPartI;
import ve.org.bcv.rhei.util.Constantes;

import com.bcv.dao.jdbc.ConyugeTrabajoDao;
import com.bcv.dao.jdbc.FamiliarDao;
import com.bcv.dao.jdbc.SolicitudDao;
import com.bcv.dao.jdbc.TrabajadorDao;
import com.bcv.dao.jdbc.impl.ConyugeTrabajoDaoImpl;
import com.bcv.dao.jdbc.impl.FamiliarDaoImpl;
import com.bcv.dao.jdbc.impl.SolicitudDaoImpl;
import com.bcv.dao.jdbc.impl.TrabajadorDaoImpl;
import com.bcv.model.BeneficioEscolar;
import com.bcv.model.ConyugeTrabajo;
import com.bcv.model.Familiar;
import com.bcv.model.PeriodoEscolar;
import com.bcv.model.Solicitud;
import com.bcv.model.Trabajador;
import com.fileupload.ListRecaudos;
import com.fileupload.UploadFile;

/**
 * Consultamos una solicitud
 * 
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com 31/03/2015 13:13:54
 * 
 */
@Named
public class SolicitudControladorConsultar extends HttpServlet
		implements
			Serializable {
	private static Logger log = Logger
			.getLogger(SolicitudControladorConsultar.class.getName());
	private ConyugeTrabajoDao conyugeTrabajoDaoImpl = new ConyugeTrabajoDaoImpl();
	private FamiliarDao familiarDao = new FamiliarDaoImpl();
	// @Inject
	// private TrabajadorDao trabajadorDao;
	private TrabajadorDao trabajadorDao = new TrabajadorDaoImpl();
	private SolicitudDao solicitudDao = new SolicitudDaoImpl();

	BeneficioEscolar beneficio = null;
	PeriodoEscolar periodo = null;

	String tablaBeneficios = "";
	String tablaPeriodos = "";
	String condicion = "A";

	String mensaje = "";

	String consultarPag = "/jsp/solicitudConsultar.jsp";

	String pagAgregarParam = "/jsp/ .jsp";
	String pagModifyParam = "/jsp/ .jsp";
	String tabla = "";

	private ServletContext sc;
	private String aviso = "FinSesion";
	private RequestDispatcher rd = null;
	private HttpSession sesion = null;
	private boolean isAdmin;
	private String cedulaUsuario;
	private List<BeneficiarioValNom> beneficiarios = new ArrayList<BeneficiarioValNom>();

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
			SolAction solAction = null;
			this.sesion = request.getSession(false);

			sesion = request.getSession(false);
			isAdmin = sesion.getAttribute("isAdmin") != null ? (Boolean) sesion
					.getAttribute("isAdmin") : false;
			cedulaUsuario = sesion.getAttribute("cedulaUsuario") != null
					? (String) sesion.getAttribute("cedulaUsuario")
					: "";
			request.setAttribute("isAdmin", isAdmin);

			ShowResultToView showResultToView = new ShowResultToView();
			showResultToView
					.setUrlAccion("/rhei/solicitudControladorConsultar");
			/** Buscamos Rif centro de eduicacion inicial */
			String nroRifCentroEdu = request.getParameter("nroRifCentroEdu") != null
					? (String) request.getParameter("nroRifCentroEdu")
					: "";

			if (("consultarIrToPag"
					.equals(request.getParameter("principal.do")))) {
				String pagIr = "";
				pagIr = consultarPag;

				/**
				 * Si no es administrador, colocamos la cedula de forma
				 * automtica
				 */
				if (!isAdmin) {
					/** Buscamos los beneficiarios automaticamente */
					showResultToView.setCedula(cedulaUsuario);
					int cedula = 0;
					try {
						cedula = new Integer(cedulaUsuario);
					} catch (Exception e) {
						// TODO: handle exception
					}

					boolean rangoEdad = false;
					/**
					 * INICIO Buscamos los NINOS beneficiarios en table
					 * PERSONAL.FAMILIARES
					 */
					beneficiarios = familiarDao.getBeneficiarioValNom(cedula,
							rangoEdad);
					if (beneficiarios == null || beneficiarios.isEmpty()
							|| beneficiarios.size() == 0) {

						showResultToView.setMensaje("beneficiario_no_hay");
					} else {
						List<BeneficiarioValNom> beneficiariosFiltroConsultar = new ArrayList<BeneficiarioValNom>();
						for (BeneficiarioValNom benef : beneficiarios) {
							boolean existe = false;
							try {
								existe = familiarDao.existeBeneficiario(
										Constantes.ACTIVO_EMPLEADO,
										benef.getValor());
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							/** Beneficiarios para incluir */
							if (existe) {
								beneficiariosFiltroConsultar.add(benef);
							}
						}
						beneficiarios = new ArrayList<BeneficiarioValNom>();
						beneficiarios.addAll(beneficiariosFiltroConsultar);
						showResultToView.setBeneficiarios(beneficiarios);

					}
				}
				/** Fin Buscamos los beneficiarios automaticamente */

				/** Fin Buscamos los beneficiarios automaticamente */

				/** Tomamos el colegio seleccionado anteriormente */
				showResultToView.setNroRifCentroEdu(nroRifCentroEdu);
				request.setAttribute("showResultToView", showResultToView);
				despachador = request.getRequestDispatcher(pagIr);
				despachador.forward(request, response);
				/** Mostramos todos los beneficiarios del empleado */
				/**
				 * consultamos info del trabajador , beneficiario y datos extras
				 * del colegio
				 */
			} else if ("consultInfoDataTrabBenef".equals(request
					.getParameter("principal.do"))) {
				/** Buscamos la cedula */
				String cedula = request.getParameter("cedula") != null
						? (String) request.getParameter("cedula")
						: "";
				String numSolicitud = request.getParameter("numSolicitud") != null
						? (String) request.getParameter("numSolicitud")
						: "0";

				/** Buscamos codigo del empleado */
				String codEmp = "";
				try {
					if (!StringUtils.isEmpty(cedula)
							&& StringUtils.isNumeric(cedula)) {
						codEmp = trabajadorDao
								.codigoEmpleadoBycedula(new Integer(cedula));
					} else {
						codEmp = request.getParameter("codEmp") != null
								? (String) request.getParameter("codEmp")
								: "";
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (StringUtils.isEmpty(codEmp)
						&& !StringUtils.isEmpty(numSolicitud)) {

					try {
						codEmp = trabajadorDao
								.codigoEmpleadoByNuSolicitud(new Integer(
										numSolicitud));
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				Trabajador trabajador = null;

				if (StringUtils.isNumeric(codEmp)
						&& StringUtils.isEmpty(cedula)) {
					try {
						trabajador = trabajadorDao
								.buscarTrabajadorByCodEmp(new Integer(codEmp));
						cedula = trabajador.getCedula() + "";
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				/** Fin Buscamos la cedula y codigo del empleado */

				/** Buscamos codigo beneficiario del empleado */
				String codigoBenef = request.getParameter("codigoBenef") != null
						? (String) request.getParameter("codigoBenef")
						: "";
				// String cedulaFamiliarByNuSolicitud(int numSolicitud)
				if (StringUtils.isEmpty(codigoBenef)
						&& !StringUtils.isEmpty(numSolicitud)) {
					try {
						codigoBenef = trabajadorDao
								.cedulaFamiliarByNuSolicitud(new Integer(
										numSolicitud));
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				Solicitud solicitud = null;
				try {
					solicitud = solicitudDao.BscarSolConCodEmpCodBenf(codEmp,
							codigoBenef);
					if (solicitud != null) {
						numSolicitud = solicitud.getNroSolicitud() + "";
						nroRifCentroEdu = solicitud.getNroRifCentroEdu();
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				/** Buscamos compania */
				String companiaAnalista = sesion
						.getAttribute("companiaAnalista") != null
						? (String) sesion.getAttribute("companiaAnalista")
						: "";
				/** inicio hacemos la busqueda solicitud */
				/********/

				solAction = new SolConsultarSearchInfoPartI(cedula, codEmp,
						codigoBenef, companiaAnalista, nroRifCentroEdu,
						new Integer(numSolicitud));
				showResultToView = solAction.ejecutar();
				/** Carga data del trabajador */

				numSolicitud = showResultToView.getNumSolicitud();

				ShowResultToView showResultToViewBd = null;
				if (!StringUtils.isEmpty(numSolicitud)) {
					showResultToViewBd = solicitudDao
							.searchEmpleado(numSolicitud);
					if (showResultToViewBd != null) {
						cedula = showResultToViewBd.getCedula();
						codEmp = showResultToViewBd.getCodEmp();
						codigoBenef = showResultToViewBd.getCodigoBenef();
						nroRifCentroEdu = showResultToViewBd
								.getNroRifCentroEdu();

						if (!StringUtils
								.isEmpty(showResultToViewBd.getCedula())
								&& StringUtils.isNumeric(showResultToViewBd
										.getCedula())) {
							Familiar familiar = null;;
							try {
								familiar = familiarDao
										.consultarConyuge(new Long(
												showResultToViewBd.getCedula()));
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (familiar != null) {
								request.setAttribute("cedulaconyuge",
										showResultToViewBd.getCedula());
								request.setAttribute("nameconyuge",
										familiar.getNombre());
								request.setAttribute("apellidoconyuge",
										familiar.getApellido());

							}
						}
					}

				}

				/********/
				/** fin hacemos la busqueda solicitud */
				if (!"fracaso1".equalsIgnoreCase(showResultToView.getMensaje())) {
					/** Lista de los recaudos a ser llenados pr el usuario */
					ListRecaudos listRecaudos = new ListRecaudos();
					StringBuilder path = new StringBuilder("");

					path.append(rb.getString("alfresco.plantillasolicitud"));
					boolean readOnly = false;
					/** Recaudos guardados en alfresco */
					boolean titleComeResourcebundle = true;
					String recaudos = listRecaudos.generateRecaudos(
							path.toString(), readOnly, titleComeResourcebundle);
					request.setAttribute("recaudos", recaudos);
					/** Fin Lista de los recaudos a ser llenados pr el usuario */
				}

				/**
				 * Se hace para evitar que los links de cambio de colegio
				 * aparezca en caso que no alla seleccionado ningun colegio
				 */
				showResultToView.setNroRifEmpty(false);
				request.setAttribute("showResultToView", showResultToView);
				String pagIr = "";
				String incluir = request.getParameter("incluir") != null
						? (String) request.getParameter("incluir")
						: "";
				if (!"".equalsIgnoreCase(incluir)) {
					pagIr = "solicitudConsultar.jsp";
				} else {
					pagIr = request.getParameter("accion") != null
							? (String) request.getParameter("accion")
							: "";
				}
				/*** Si venimos de incluir, esta trae un valor de exito **********/
				request.setAttribute("incluir", incluir);

				ConyugeTrabajo conyugeTrabajo = null;
				try {
					if (showResultToViewBd != null
							&& !StringUtils.isEmpty(showResultToViewBd
									.getCedula())
							&& StringUtils.isNumeric(showResultToViewBd
									.getCedula())) {
						conyugeTrabajo = conyugeTrabajoDaoImpl.find(Integer
								.parseInt(showResultToViewBd.getCedula()));
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				request.setAttribute("conyugeTrabajo", conyugeTrabajo);

				despachador = request.getRequestDispatcher("/jsp/" + pagIr);
				if (!StringUtils.isEmpty(showResultToView.getMensaje())) {
					if (("fracaso".equalsIgnoreCase(showResultToView
							.getMensaje()))
							|| ("fracaso1".equalsIgnoreCase(showResultToView
									.getMensaje()))
							|| ("error".equalsIgnoreCase(showResultToView
									.getMensaje()))) {

						String pagIrAfter = "";
						if ("solicitudConsultar.jsp".equalsIgnoreCase(pagIr)) {
							pagIrAfter = "accionConsultar";
						} else if ("solicitudActualizar.jsp"
								.equalsIgnoreCase(pagIr)) {
							pagIrAfter = "accionActualizar";
						}
						despachador = request
								.getRequestDispatcher("/intitucionCtrl?principal.do=institucionIrToPag&pagIrAfter="
										+ pagIrAfter
										+ "&mensajeError="
										+ showResultToView.getMensaje());
					}

				}
				despachador.forward(request, response);

				/** Vamos a la pag Actualizar Solicitud */
			} else if ("uploadFile"
					.equals(request.getParameter("principal.do"))) {

				String periodoEscolarValor = request
						.getParameter("periodoEscolarValor") != null
						? (String) request.getParameter("periodoEscolarValor")
						: "";
				String rif = request.getParameter("rif") != null
						? (String) request.getParameter("rif")
						: "";
				String cedula = request.getParameter("cedula") != null
						? (String) request.getParameter("cedula")
						: "";
				String codBenef = request.getParameter("codBenef") != null
						? (String) request.getParameter("codBenef")
						: "";

				StringBuilder path = new StringBuilder("");
				path.append(periodoEscolarValor).append("/").append(rif)
						.append("/").append(cedula).append("/")
						.append(codBenef);

				UploadFile uploadFile = new UploadFile();
				uploadFile.comun(request, response, path.toString());
				ListRecaudos listRecaudos = new ListRecaudos();

				listRecaudos.comunConsultOnly(request, response,
						path.toString());

				/** Guardamos los datos de actualizar la solicitud... */
			} else if ("listRecaudos".equals(request
					.getParameter("principal.do"))) {

				String periodoEscolarValor = request
						.getParameter("periodoEscolarValor") != null
						? (String) request.getParameter("periodoEscolarValor")
						: "";
				String rif = request.getParameter("rif") != null
						? (String) request.getParameter("rif")
						: "";
				String cedula = request.getParameter("cedula") != null
						? (String) request.getParameter("cedula")
						: "";
				String codBenef = request.getParameter("codBenef") != null
						? (String) request.getParameter("codBenef")
						: "";

				StringBuilder path = new StringBuilder("");
				path.append(periodoEscolarValor).append("/").append(rif)
						.append("/").append(cedula).append("/")
						.append(codBenef);

				ListRecaudos listRecaudos = new ListRecaudos();
				listRecaudos.comunConsultOnly(request, response,
						path.toString());

				/** Guardamos los datos de actualizar la solicitud... */
			}

		}

	}

}
