package ve.org.bcv.rhei.controlador;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ve.org.bcv.rhei.bean.ShowResultToView;

public class IntitucionCtrl extends HttpServlet implements Serializable {
	private static Logger log = Logger.getLogger(IntitucionCtrl.class
			.getName());

 
	String mensaje = "";
	String pagIrAfter ="";
	String institucionPag = "/jsp/institucion.jsp";
	String accionIncluir="/rhei/solicitudControladorIncluir?principal.do=incluirIrToPag";
	String accionConsultar="/rhei/solicitudControladorConsultar?principal.do=consultarIrToPag";
	String accionActualizar="/rhei/solicitudControladorActualizar?principal.do=actualizarIrToPag";
	String accionDesincorporar="/rhei/solicitudDesincorporarControlador?principal.do=desincorporarIrToPag";
	String accionPagConv="/rhei/pagosControladorReg?principal.do=pagoConvencional";
	String accionActualizarColegio="/rhei/solicitudControladorActualizar?principal.do=actualizarColegio";
 
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
		 
		if (request.getSession(false) == null) {
			this.sesion = request.getSession(true);
			this.sesion.setAttribute("aviso", this.aviso);
			this.rd = request.getRequestDispatcher("/jsp/login.jsp");
			this.rd.forward(request, response);
		} else {
			RequestDispatcher despachador = null;
			ShowResultToView showResultToView = new ShowResultToView();
			
			String nombre = request.getParameter("nombre");
			log.info("nombre="+nombre);
			
			mensaje=request.getParameter("mensajeError")!=null?(String)request.getParameter("mensajeError"):null;
			showResultToView.setMensaje(mensaje); 
			  if ("institucionIrToPag".equals(request
					.getParameter("principal.do"))) {
				String pagIr = "";
				pagIr = institucionPag;
				pagIrAfter=request.getParameter("pagIrAfter")!=null?(String)request.getParameter("pagIrAfter"):"";
				log.info("pagIrAfter="+pagIrAfter);
				/**Pag donde iremos despues de enciotrar el rif del colegio*/
				if ("accionIncluir".equalsIgnoreCase(pagIrAfter)){
					request.setAttribute("titleBuscar", "solicitud.incluir");
					pagIrAfter=accionIncluir;
				}else	if ("accionConsultar".equalsIgnoreCase(pagIrAfter)){
					request.setAttribute("numSolicitudPage", "solicitudConsultar.jsp");
					request.setAttribute("titleBuscar", "solicitud.consultar");
					pagIrAfter=accionConsultar;
				}else	if ("accionActualizar".equalsIgnoreCase(pagIrAfter)){
					
					request.setAttribute("numSolicitudPage", "solicitudActualizar.jsp");
					request.setAttribute("titleBuscar", "solicitud.actualizar");
					pagIrAfter=accionActualizar;
				}else	if ("accionDesincorporar".equalsIgnoreCase(pagIrAfter)){
					request.setAttribute("numSolicitudPage", "solicitudDesincorporar.jsp");
					request.setAttribute("titleBuscar", "solicitud.desincorporar");
					pagIrAfter=accionDesincorporar;
				}else	if ("accionPagConv".equalsIgnoreCase(pagIrAfter)){
					request.setAttribute("numSolicitudPage", "pagoConvRegistrar.jsp");
					request.setAttribute("titleBuscar", "pagos.convencionales");
					pagIrAfter=accionPagConv;
					         
				}else	if ("accionActualizarColegio".equalsIgnoreCase(pagIrAfter)){
					request.setAttribute("numSolicitudPage", "solicitudActualizar.jsp");
					request.setAttribute("titleBuscar", "solicitud.actualizar");
					String numSolicitud=request.getParameter("numSolicitud")!=null?(String)request.getParameter("numSolicitud"):"";
					request.setAttribute("numSolicitud", numSolicitud);
					request.setAttribute("actualizarColegio", "true");
					pagIrAfter=accionActualizarColegio;
					
				}
				
				showResultToView.setPagIrAfter(pagIrAfter);
				request.setAttribute("showResultToView", showResultToView);
				despachador = request.getRequestDispatcher(pagIr);
				despachador.forward(request, response);
				/**
				 * LLenamos los datos del trabajador , beneficiario y datos
				 * extras del colegio
				 */
			  }
		}

	}

}
