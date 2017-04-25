package ve.org.bcv.rhei.controlador;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
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

import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.negocio.SolAction;
import ve.org.bcv.rhei.negocio.SolActualizar;
import ve.org.bcv.rhei.negocio.SolActualizarSearchInfoPartI;

import com.bcv.dao.jdbc.ConyugeTrabajoDao;
import com.bcv.dao.jdbc.FamiliarDao;
import com.bcv.dao.jdbc.SolicitudDao;
import com.bcv.dao.jdbc.impl.ConyugeTrabajoDaoImpl;
import com.bcv.dao.jdbc.impl.FamiliarDaoImpl;
import com.bcv.dao.jdbc.impl.SolicitudDaoImpl;
import com.bcv.model.BeneficioEscolar;
import com.bcv.model.ConyugeTrabajo;
import com.bcv.model.Familiar;
import com.bcv.model.PeriodoEscolar;
import com.fileupload.ListRecaudos;
import com.fileupload.UploadFile;

/**
 * Actualizamos la solicitud
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com
 * 31/03/2015 14:11:36
 * 
 */
public class SolicitudControladorActualizar extends HttpServlet implements Serializable {
	private static Logger log = Logger.getLogger(SolicitudControladorActualizar.class.getName());
	private ConyugeTrabajoDao conyugeTrabajoDaoImpl = new ConyugeTrabajoDaoImpl();
	private FamiliarDao familiarDao= new FamiliarDaoImpl();
	private SolicitudDao solicitudDao = new SolicitudDaoImpl();
	BeneficioEscolar beneficio = null;
	PeriodoEscolar periodo = null;

	String tablaBeneficios = "";
	String tablaPeriodos = "";
	String condicion = "A";

	String mensaje = "";
 
	String actualizarPag = "/jsp/solicitudActualizar.jsp";
	
	String pagAgregarParam = "/jsp/ .jsp";
	String pagModifyParam = "/jsp/ .jsp";
	String tabla = "";

	private ServletContext sc;
	private String aviso = "FinSesion";
	private RequestDispatcher rd = null;
	private HttpSession sesion = null;
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
		ResourceBundle rb = ResourceBundle.getBundle("ve.org.bcv.rhei.util.bundle");
		if (request.getSession(false) == null) {
			this.sesion = request.getSession(true);
			this.sesion.setAttribute("aviso", this.aviso);
			this.rd = request.getRequestDispatcher("/jsp/login.jsp");
			this.rd.forward(request, response);
		} else {
			RequestDispatcher despachador = null;
			SolAction solAction = null;
			ShowResultToView showResultToView = new ShowResultToView();
			showResultToView.setUrlAccion("/rhei/solicitudControladorActualizar");
			    if ("actualizarIrToPag".equals(request
					.getParameter("principal.do"))) {
			    	/** Tomamos el colegio seleccionado anteriormente*/
					String nroRifCentroEdu=request.getParameter("nroRifCentroEdu")!=null?(String)request.getParameter("nroRifCentroEdu"):"";
					showResultToView.setNroRifCentroEdu(nroRifCentroEdu);
					request.setAttribute("showResultToView", showResultToView);
				despachador = request.getRequestDispatcher(actualizarPag);
				despachador.forward(request, response);
				/**
				 * para Actualizar, consultamos primero la info del trabajador ,
				 * beneficiario y datos extras del colegio
				 */
			} else if ("actualizarInfoDataTrabBenef".equals(request
					.getParameter("principal.do"))) {

				solAction = new SolActualizarSearchInfoPartI(request, response,
						showResultToView, this.sc);
				showResultToView = solAction.ejecutar();
				/**Lanzamos un msg si viene de actualizar colegio*/
				String vieneFromActualizarColegio=request.getParameter("vieneFromActualizarColegio");
				if (!StringUtils.isEmpty(vieneFromActualizarColegio)){
					showResultToView.setMensaje(vieneFromActualizarColegio);
				}
				/**Fin Lanzamos un msg si viene de actualizar colegio*/
				/**Lista de los recaudos a ser llenados pr el usuario*/
				ListRecaudos listRecaudos = new ListRecaudos();
			 				
				 StringBuilder path=new StringBuilder("");
				 path.append(rb.getString("alfresco.plantillasolicitud"));
			    boolean readOnly=false;
				 boolean titleComeResourcebundle=true;
 				String recaudos=listRecaudos.generateRecaudos( path.toString(),readOnly,titleComeResourcebundle);
				request.setAttribute("recaudos", recaudos);
				/**Fin Lista de los recaudos a ser llenados pr el usuario*/
				
				/**Inicio Conyuge*/
				int cedula=0;
				if (!StringUtils.isEmpty(showResultToView.getCedula())&& StringUtils.isNumeric(showResultToView.getCedula())){
					cedula = new Integer(showResultToView.getCedula());
				}
				ConyugeTrabajo  conyugeTrabajo  =null;
				try {
					  conyugeTrabajo  =conyugeTrabajoDaoImpl.find(cedula);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.setAttribute("conyugeTrabajo", conyugeTrabajo);
				if (!StringUtils.isEmpty(showResultToView.getCedula())&& StringUtils.isNumeric(showResultToView.getCedula())){
					Familiar familiar=null;;
					try {
						familiar = familiarDao.consultarConyuge(new Long(showResultToView.getCedula()));
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
					
					request.setAttribute("cedulaconyuge",showResultToView.getCedula());
					if (familiar!=null){
						request.setAttribute("nameconyuge", familiar.getNombre());
						request.setAttribute("apellidoconyuge", familiar.getApellido());		
					}
				
				}
				/**Fin Conyuge*/
				
				
				request.setAttribute("showResultToView", showResultToView);
				String pagIr = request.getParameter("accion") != null ? (String) request
						.getParameter("accion") : "";
				despachador = request.getRequestDispatcher("/jsp/" + pagIr);
				
				
				if (!StringUtils.isEmpty(showResultToView.getMensaje())) {
					if (("fracaso".equalsIgnoreCase(showResultToView
							.getMensaje()))
							|| ("fracaso1".equalsIgnoreCase(showResultToView
									.getMensaje()))
							|| ("error".equalsIgnoreCase(showResultToView
									.getMensaje()))) {
						
						String pagIrAfter="accionActualizar";
						despachador = request.getRequestDispatcher("/intitucionCtrl?principal.do=institucionIrToPag&pagIrAfter="+pagIrAfter+"&mensajeError="+showResultToView
								.getMensaje());
					} 

				}			
				
				
				despachador.forward(request, response);
				/** Guardamos los datos de incluir la solicitud... */
			} else if ("actualizarSolicitud".equals(request
					.getParameter("principal.do"))) {

				showResultToView = new ShowResultToView();

				/** Inicio actualizar en bd Solicitud */
				solAction = new SolActualizar(request, response,
						showResultToView, this.sc);
				showResultToView = solAction.ejecutar();
				String saveOurMessage = showResultToView.getMensaje();//request.getAttribute("mensaje") != null ? (String) request
						//.getAttribute("mensaje") : "";
				/** Fin incluir actualizar en bd */

				/** Buscamos informacion para mostrar por pantalla inicio */
				solAction = new SolActualizarSearchInfoPartI(request, response,
						showResultToView, this.sc);
				showResultToView = solAction.ejecutar();
				/** Buscamos informacion para mostrar por pantalla fin */
				/** --------------------------------------------- */
				showResultToView.setMensaje(saveOurMessage);
				request.setAttribute("showResultToView", showResultToView);
				
				
				/**Lista de los recaudos a ser llenados pr el usuario*/
				ListRecaudos listRecaudos = new ListRecaudos();
			 				
				 StringBuilder path=new StringBuilder("");
			    path.append(showResultToView.getPeriodoEscolar()).append("/").append(showResultToView.getNroRifCentroEdu()).append("/").append(showResultToView.getCedula()).append("/").append(showResultToView.getCedBenef());
				log.info("path.toString()="+path.toString());
			    boolean readOnly=false;
			    boolean titleComeResourcebundle=true;
 				String recaudos=listRecaudos.generateRecaudos( path.toString(),readOnly,titleComeResourcebundle);
				request.setAttribute("recaudos", recaudos);
				/**Fin Lista de los recaudos a ser llenados pr el usuario*/
					/**Inicio Conyuge*/
					int cedula=0;
					if (!StringUtils.isEmpty(showResultToView.getCedula())&& StringUtils.isNumeric(showResultToView.getCedula())){
						cedula = new Integer(showResultToView.getCedula());
					}
					ConyugeTrabajo  conyugeTrabajo  =null;
					try {
						  conyugeTrabajo  =conyugeTrabajoDaoImpl.find(cedula);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					request.setAttribute("conyugeTrabajo", conyugeTrabajo);
						Familiar familiar=null;;
						try {
							familiar = familiarDao.consultarConyuge(new Long(cedula));
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
						request.setAttribute("cedulaconyuge",showResultToView.getCedula());	
						if (familiar!=null){
						request.setAttribute("nameconyuge", familiar.getNombre());
						request.setAttribute("apellidoconyuge", familiar.getApellido());
						}
					/**Fin Conyuge*/
					
			 
				
				/**Fin Conyuge*/
				
				
				
				
				
				
				String pagIr = request.getParameter("accion") != null ? (String) request
						.getParameter("accion") : "";
				despachador = request.getRequestDispatcher("/jsp/" + pagIr);
				despachador.forward(request, response);
				/** Vamos a la pag Actualizar Solicitud */
			}  else if ("uploadFile".equals(request
					.getParameter("principal.do"))) {
		 
				String periodoEscolarValor = request
						.getParameter("periodoEscolarValor") != null ? (String) request
						.getParameter("periodoEscolarValor") : "";
				String rif = request.getParameter("rif") != null ? (String) request
						.getParameter("rif") : "";
				String cedula = request.getParameter("cedula") != null ? (String) request
						.getParameter("cedula") : "";
				String codBenef = request.getParameter("codBenef") != null ? (String) request
						.getParameter("codBenef") : "";

				StringBuilder path = new StringBuilder("");
				path.append(periodoEscolarValor).append("/").append(rif)
						.append("/").append(cedula).append("/")
						.append(codBenef);
				
				
				UploadFile uploadFile= new UploadFile();
				uploadFile.comun(request,response, path.toString());		
				ListRecaudos listRecaudos = new ListRecaudos();
				
			

				listRecaudos.comun(request, response, path.toString());
 
				/** Guardamos los datos de actualizar la solicitud... */
			} else if ("listRecaudos".equals(request
					.getParameter("principal.do"))) {
		 
				String periodoEscolarValor = request
						.getParameter("periodoEscolarValor") != null ? (String) request
						.getParameter("periodoEscolarValor") : "";
				String rif = request.getParameter("rif") != null ? (String) request
						.getParameter("rif") : "";
				String cedula = request.getParameter("cedula") != null ? (String) request
						.getParameter("cedula") : "";
				String codBenef = request.getParameter("codBenef") != null ? (String) request
						.getParameter("codBenef") : "";

				StringBuilder path = new StringBuilder("");
				path.append(periodoEscolarValor).append("/").append(rif)
						.append("/").append(cedula).append("/")
						.append(codBenef);
	 		
				ListRecaudos listRecaudos = new ListRecaudos();
				listRecaudos.comun(request, response, path.toString());
 
				/** Guardamos los datos de actualizar la solicitud... */
			} else    if ("actualizarColegio".equals(request
					.getParameter("principal.do"))) {
		    	/** Tomamos el colegio seleccionado anteriormente*/
				String nroRifCentroEdu=request.getParameter("nroRifCentroEdu")!=null?(String)request.getParameter("nroRifCentroEdu"):"";
				showResultToView.setNroRifCentroEdu(nroRifCentroEdu);
				String numSolicitud=request.getParameter("numSolicitud")!=null?(String)request.getParameter("numSolicitud").trim():"";
				int numSol=0;
				try {
					numSol= Integer.parseInt(numSolicitud);
				} catch (Exception e) {
					// TODO: handle exception
				}
				int resultado= -1;
				if (!"0".equalsIgnoreCase(nroRifCentroEdu)){
					resultado= solicitudDao.updateColegioSolicitud(nroRifCentroEdu,numSol);
				}
				
				if (resultado>0){
					showResultToView.setMensaje("exito");	
				}else{
					showResultToView.setMensaje("solicitud.falta.colegio");
				}
				request.setAttribute("showResultToView", showResultToView);
			despachador = request.getRequestDispatcher("/solicitudControladorActualizar?principal.do=actualizarInfoDataTrabBenef&vieneFromActualizarColegio="+showResultToView.getMensaje());
			despachador.forward(request, response);
			/**
			 * para Actualizar, consultamos primero la info del trabajador ,
			 * beneficiario y datos extras del colegio
			 */
		}

		}

	}

}