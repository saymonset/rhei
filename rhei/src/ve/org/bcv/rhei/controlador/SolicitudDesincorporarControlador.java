package ve.org.bcv.rhei.controlador;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import ve.org.bcv.rhei.bean.BeneficiarioValNom;
import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.bean.ValorNombre;
import ve.org.bcv.rhei.negocio.SolAction;
import ve.org.bcv.rhei.negocio.SolDesinCorpPartII;
import ve.org.bcv.rhei.negocio.SolDesinCorpSearchInfoPartI;
import ve.org.bcv.rhei.util.Constantes;

import com.bcv.dao.jdbc.FamiliarDao;
import com.bcv.dao.jdbc.PeriodoEscolarDao;
import com.bcv.dao.jdbc.impl.FamiliarDaoImpl;
import com.bcv.dao.jdbc.impl.PeriodoEscolarDaoImpl;
import com.bcv.model.BeneficioEscolar;
import com.bcv.model.PeriodoEscolar;

/**
 * Controlador sobre la solicitud de una solicitud del centro de educacion
 * inicial
 * 
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com 04/02/2015 19:07:21
 * 
 */
public class SolicitudDesincorporarControlador extends HttpServlet implements Serializable {
 
	BeneficioEscolar beneficio = null;
	PeriodoEscolar periodo = null;

	String tablaBeneficios = "";
	String tablaPeriodos = "";
	String condicion = "A";

	String mensaje = "";
 
	String desincorporarPag = "/jsp/solicitudDesincorporar.jsp";

	String pagAgregarParam = "/jsp/ .jsp";
	String pagModifyParam = "/jsp/ .jsp";
	String tabla = "";

	private ServletContext sc;
	private String aviso = "FinSesion";
	private RequestDispatcher rd = null;
	private HttpSession sesion = null;
	private FamiliarDao familiarDao = new FamiliarDaoImpl();
	private PeriodoEscolarDao periodoEscolarDao = new PeriodoEscolarDaoImpl();
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
			SolAction solAction = null;
			ShowResultToView showResultToView = new ShowResultToView();
			  if ("desincorporarIrToPag".equals(request
					.getParameter("principal.do"))) {
				  
				  /** Cargar todos los periodos escolares */
					List<ValorNombre> allPeriodos = periodoEscolarDao.tipoPeriodosEscolares();
					if (allPeriodos == null || allPeriodos.size() == 0
							|| allPeriodos.isEmpty()) {
						/**
						 * Si no existe un periodo escolar activo.. colocaremos este mensaje
						 * de error
						 */
						showResultToView.setMensaje("errorPeriodoScolar");
					} else {
						showResultToView.setPeriodoEscolares(allPeriodos);
					}
				  
				  /** Tomamos el colegio seleccionado anteriormente*/
					String nroRifCentroEdu=request.getParameter("nroRifCentroEdu")!=null?(String)request.getParameter("nroRifCentroEdu"):"";
					showResultToView.setNroRifCentroEdu(nroRifCentroEdu);
					request.setAttribute("showResultToView", showResultToView);
				despachador = request.getRequestDispatcher(desincorporarPag);
				despachador.forward(request, response);
				/**
				 * para desincorporar, consultamos primero la info del trabajador ,
				 * beneficiario y datos extras del colegio
				 */
			} else if ("desincorporarInfoDataTrabBenef".equals(request
					.getParameter("principal.do"))) {
//				GENERAMOS LA TABLA DE DESINCORPORAR HTML
				solAction = new SolDesinCorpSearchInfoPartI(request, response,
						showResultToView, this.sc);
				showResultToView = solAction.ejecutar();
				  /** Tomamos el colegio seleccionado anteriormente*/
				String nroRifCentroEdu=request.getParameter("nroRifCentroEdu")!=null?(String)request.getParameter("nroRifCentroEdu"):"";
				showResultToView.setNroRifCentroEdu(nroRifCentroEdu);
			
			
				int ced=0;
				if (!StringUtils.isEmpty(showResultToView.getCedula())&& StringUtils.isNumeric(showResultToView.getCedula())){
					ced=Integer.parseInt(showResultToView.getCedula());
				}
				boolean rangoEdad=false;
				List<BeneficiarioValNom> beneficiarios = familiarDao.getBeneficiarioValNom(
						ced, rangoEdad);
						
						List<BeneficiarioValNom> beneficiariosFiltroIncluir = new ArrayList<BeneficiarioValNom>();
					List<BeneficiarioValNom> beneficiariosFiltroConsultar = new ArrayList<BeneficiarioValNom>();
					for (BeneficiarioValNom benef:beneficiarios){
						boolean existe=false;
						try {
							existe = familiarDao.existeBeneficiario(Constantes.ACTIVO_EMPLEADO, benef.getValor());
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if ( existe){
							beneficiariosFiltroConsultar.add(benef);
						}
					}
					beneficiarios =  new ArrayList<BeneficiarioValNom>();
					 
						beneficiarios.addAll(beneficiariosFiltroConsultar);
					 showResultToView.setBeneficiarios(beneficiarios);
				
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
								
								String pagIrAfter="";
								if ("solicitudDesincorporar.jsp".equalsIgnoreCase(pagIr)){
									pagIrAfter="accionDesincorporar";
								} 
								StringBuilder sb= new StringBuilder("fracaso="); 
								despachador = request.getRequestDispatcher("/intitucionCtrl?principal.do=institucionIrToPag&pagIrAfter="+pagIrAfter+"&mensajeError="+showResultToView
										.getMensaje());
							} 

						}			
						
			
				despachador.forward(request, response);
				/** Guardamos los datos de incluir la solicitud... */
			}else if ("desincorporarSolicitud".equals(request
					.getParameter("principal.do"))) {

				showResultToView = new ShowResultToView();
				/** Inicio desincorporacion en bd Solicitud */
				solAction = new SolDesinCorpPartII(request, response, showResultToView,
						this.sc);
				showResultToView = solAction.ejecutar();
				  /** Tomamos el colegio seleccionado anteriormente*/
				String nroRifCentroEdu=request.getParameter("nroRifCentroEdu")!=null?(String)request.getParameter("nroRifCentroEdu"):"";
				showResultToView.setNroRifCentroEdu(nroRifCentroEdu);

				request.setAttribute("showResultToView", showResultToView);
				despachador = request.getRequestDispatcher(desincorporarPag);
				despachador.forward(request, response);
			/** Vamos a la pag desincorporacion Solicitud por año */
			}else if ("desincorporarSolicitudByAnio".equals(request
					.getParameter("principal.do"))) {

				showResultToView = new ShowResultToView();
				String periodoEscolarValor = request.getParameter("periodoEscolarValor")!=null?(String)request.getParameter("periodoEscolarValor"):"";
				if (!StringUtils.isEmpty(periodoEscolarValor)){
					/** Inicio desincorporacion en bd Solicitud */
					SolDesinCorpPartII solAction2 = new SolDesinCorpPartII(request, response, showResultToView,
							this.sc);
					showResultToView = solAction2.ejecutarByAnioDesincorporar(periodoEscolarValor);	
				}
				
				  /** Tomamos el colegio seleccionado anteriormente*/
				String nroRifCentroEdu=request.getParameter("nroRifCentroEdu")!=null?(String)request.getParameter("nroRifCentroEdu"):"";
				showResultToView.setNroRifCentroEdu(nroRifCentroEdu);

				request.setAttribute("showResultToView", showResultToView);
				despachador = request.getRequestDispatcher(desincorporarPag);
				despachador.forward(request, response);
			/** Vamos a la pag desincorporacion Solicitud */
		} 

		}

	}

}
