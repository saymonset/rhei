package ve.org.bcv.rhei.controlador;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
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

import org.apache.log4j.Logger;

import ve.org.bcv.rhei.bean.BeneficiarioValNom;
import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.negocio.SolAction;
import ve.org.bcv.rhei.negocio.SolBuscarInfo;
import ve.org.bcv.rhei.negocio.SolIncluir;
import ve.org.bcv.rhei.util.Constantes;

import com.bcv.dao.jdbc.EmpleadoInfoDAO;
import com.bcv.dao.jdbc.FamiliarDao;
import com.bcv.dao.jdbc.TrabajadorDao;
import com.bcv.dao.jdbc.impl.EmpleadoInfoDAOImpl;
import com.bcv.dao.jdbc.impl.FamiliarDaoImpl;
import com.bcv.dao.jdbc.impl.TrabajadorDaoImpl;
import com.bcv.model.BeneficioEscolar;
import com.bcv.model.PeriodoEscolar;
import com.bcv.model.Trabajador;
import com.fileupload.ListRecaudos;
import com.fileupload.UploadFile;

/**
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com
 * 16/04/2015 15:05:14
 * 
 */
public class SolicitudControladorIncluir extends HttpServlet implements Serializable {
	private TrabajadorDao trabajadorDao= new TrabajadorDaoImpl();
	private EmpleadoInfoDAO empleadoInfoDAO= new EmpleadoInfoDAOImpl();
	private static Logger log = Logger.getLogger(SolicitudControladorIncluir.class
			.getName());

	BeneficioEscolar beneficio = null;
	PeriodoEscolar periodo = null;

	String tablaBeneficios = "";
	String tablaPeriodos = "";
	String condicion = "A";

	String mensaje = "";
	String incluirPag = "/jsp/solicitudIncluir.jsp";
 
	String tabla = "";

	private ServletContext sc;
	private String aviso = "FinSesion";
	private RequestDispatcher rd = null;
	private HttpSession sesion = null;
	private boolean isAdmin;
	private String cedulaUsuario;
	private List<BeneficiarioValNom> beneficiarios = new ArrayList<BeneficiarioValNom>();
	private FamiliarDao familiarDao = new FamiliarDaoImpl();
 
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

	/**
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
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
			showResultToView.setUrlAccion("/rhei/solicitudControladorIncluir");
			/**verificamos si el usuario es administrador del sistema*/
			sesion = request.getSession(false);
			isAdmin= sesion.getAttribute("isAdmin")!=null?(Boolean)sesion.getAttribute("isAdmin"):false;
			cedulaUsuario= sesion.getAttribute("cedulaUsuario")!=null?(String)sesion.getAttribute("cedulaUsuario"):"";
			request.setAttribute("isAdmin", isAdmin); 
			
			  if ("incluirIrToPag".equals(request
					.getParameter("principal.do"))) {
				  
				  /**si es cero, esta en check**/   
				  String sinInstitucionCheck= request.getParameter("sinInstitucionCheck");
				  String nroRifCentroEdu=request.getParameter("nroRifCentroEdu")!=null?(String)request.getParameter("nroRifCentroEdu"):"";
				  /**sinInstitucionCheck si es cero, esta en check, o si nroRifCentroEdu !=null o no es cero**/
				 if ("0".equalsIgnoreCase(sinInstitucionCheck)||(nroRifCentroEdu!=null && !"0".equalsIgnoreCase(nroRifCentroEdu))){
					 String pagIr = "";
						pagIr = incluirPag;
						/** Tomamos el colegio seleccionado anteriormente*/
						showResultToView.setNroRifCentroEdu(nroRifCentroEdu);
						
						/**Si no es administrador, colocamos la cedula de forma automtica*/
						if (!isAdmin){
							/**Buscamos los beneficiarios automaticamente*/
							showResultToView.setCedula(cedulaUsuario);
							int cedula=0;
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
							beneficiarios = familiarDao.getBeneficiarioValNom(
									cedula, rangoEdad);
							if (beneficiarios == null || beneficiarios.isEmpty()
									|| beneficiarios.size() == 0) {
								
								showResultToView.setMensaje("beneficiario_no_hay");
							}else{
								List<BeneficiarioValNom> beneficiariosFiltroIncluir = new ArrayList<BeneficiarioValNom>();
								for (BeneficiarioValNom benef:beneficiarios){
									boolean existe=false;
									try {
										existe = familiarDao.existeBeneficiario(Constantes.ACTIVO_EMPLEADO, benef.getValor());
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									/**Beneficiarios para incluir*/
									if ( !existe){
										beneficiariosFiltroIncluir.add(benef);
									} 
								}
								beneficiarios =  new ArrayList<BeneficiarioValNom>();
								beneficiarios.addAll(beneficiariosFiltroIncluir);
								showResultToView.setBeneficiarios(beneficiarios);		
								

							}
							/**Fin Buscamos los beneficiarios automaticamente*/	
							
							
						} 
						request.setAttribute("showResultToView", showResultToView);
						nroRifCentroEdu=null;
						despachador = request.getRequestDispatcher(pagIr);
						despachador.forward(request, response);	 
				 }else{
					  /**sinInstitucionCheck si es cero, esta en check **/
					 if (!"0".equalsIgnoreCase(sinInstitucionCheck)){
	    				request.setAttribute("mensajeError", "solicitud.falta.colegio");
						despachador = request.getRequestDispatcher("/intitucionCtrl?principal.do=institucionIrToPag&pagIrAfter=accionIncluir");
						despachador.forward(request, response);
					 }
				 }
				
				/**
				 * LLenamos los datos del trabajador , beneficiario y datos
				 * extras del colegio
				 */
			} else if ("incluirInfoDataTrabBenef".equals(request
					.getParameter("principal.do"))) {
				
				boolean isVigente=true;
				String cedula=request.getParameter("cedula")!=null?(String)request.getParameter("cedula"):"";
				int ced=0;
				try {
					ced= Integer.parseInt(cedula);
				} catch (Exception e) {
					// TODO: handle exception
				}
				Trabajador trabajador=null;
				try {
					trabajador=trabajadorDao.buscarTrabajador(ced);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (trabajador!=null){
					if (("CON".equalsIgnoreCase(trabajador.getTipoEmpleadoCod()))){
						try {
							isVigente=empleadoInfoDAO.isVigenteEmpleado(ced);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
				if(isVigente){
					solAction = new SolBuscarInfo(request, response,
							showResultToView, this.sc);
					
					showResultToView = solAction.ejecutar();
					
					if (!"fracaso".equalsIgnoreCase(showResultToView.getMensaje())){
						/**Lista de los recaudos a ser llenados pr el usuario*/
						ListRecaudos listRecaudos = new ListRecaudos();
						 StringBuilder path=new StringBuilder("");
					 	 
						 path.append(rb.getString("alfresco.plantillasolicitud"));
					    
					    boolean readOnly=false;
					    boolean titleComeResourcebundle=true;
		 				String recaudos=listRecaudos.generateRecaudos( path.toString(),readOnly,titleComeResourcebundle);
						request.setAttribute("recaudos", recaudos);
						/**Fin Lista de los recaudos a ser llenados pr el usuario*/		
					}
				
				
				}else{
					showResultToView=new ShowResultToView();
					showResultToView.setMensaje("failVigenteContrato");
				}
				
				request.setAttribute("accion",Constantes.INCLUIR);
				request.setAttribute("disabledBenefCompartido","disabled");
				/**Se hace para evitar que los links de cambio de colegio aparezca en caso que no alla seleccionado ningun colegio*/
				showResultToView.setNroRifEmpty(false);
				request.setAttribute("showResultToView", showResultToView);
				String pagIr = request.getParameter("accion") != null ? (String) request
						.getParameter("accion") : "";
						
				despachador = request.getRequestDispatcher("/jsp/" + pagIr);
				despachador.forward(request, response);
			
				/** Vamos a  incluir solicitud */
			}else if ("incluirSolicitud".equals(request
					.getParameter("principal.do"))) {
				showResultToView = new ShowResultToView();

				/** Inicio incluir Solicitud */
				solAction = new SolIncluir(request, response, showResultToView,
						this.sc);
				
				
				 
				
				showResultToView = solAction.ejecutar();
				
 


				/** --------------------------------------------- */

				request.setAttribute("showResultToView", showResultToView);
				/**Si es exito, nos vamos a consultar*/
				log.info("showResultToView.getMensaje()="+showResultToView.getMensaje());
				if ("exito".equalsIgnoreCase(showResultToView.getMensaje())){
					
					//String pagIr ="solicitudConsultar.jsp";
					despachador=request.getRequestDispatcher("/solicitudControladorConsultar?principal.do=consultInfoDataTrabBenef&incluir=exito");  
					despachador.forward(request, response);  
				} else{
					String pagIr = request.getParameter("accion") != null ? (String) request
							.getParameter("accion") : "";
					despachador = request.getRequestDispatcher("/jsp/" + pagIr);
					despachador.forward(request, response);
				}
						
				
				/** Guardamos los datos de actualizar la solicitud... */
			} else if ("uploadFile".equals(request
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
			} 

		}

	}

}
