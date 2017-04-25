package ve.org.bcv.rhei.controlador;

import java.io.IOException;
import java.io.Serializable;
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

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ve.org.bcv.rhei.bean.BeneficiarioValNom;
import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.bean.ValorNombre;
import ve.org.bcv.rhei.negocio.SolAction;
import ve.org.bcv.rhei.negocio.SolConsultarSearchInfoPartI;
import ve.org.bcv.rhei.util.Constantes;

import com.bcv.dao.jdbc.MovStSolicBeiDao;
import com.bcv.dao.jdbc.impl.MovStSolicBeiDaoImpl;

/**
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com
 * 16/04/2015 15:00:34
 * 
 */
public class BenefCodPeriodoPagoControlador extends HttpServlet implements
		Serializable {
	private static Logger log = Logger
			.getLogger(BenefCodPeriodoPagoControlador.class.getName());

	private ServletContext sc;
	private String aviso = "FinSesion";
	private RequestDispatcher rd = null;
	private HttpSession sesion = null;
	private MovStSolicBeiDao movStSolicBeiDao = new MovStSolicBeiDaoImpl();
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
			ShowResultToView showResultToView = new ShowResultToView();
			SolAction solAction = new SolConsultarSearchInfoPartI(request,
					response, showResultToView, sc);
			String pagIr = request.getParameter("accion") != null ? (String) request
					.getParameter("accion") : "";
			/**
			 * Buscamos Los Beneficiarios del empleado(Ninios) y Los centros de
			 * educacion inicial
			 */
			if ("buscarBenefAndCentrEduc".equals(request
					.getParameter("principal.do"))) {
				String cedula = request.getParameter("cedula") != null ? (String) request
						.getParameter("cedula") : "";
				String companiaAnalista = request.getSession().getAttribute(
						"companiaAnalista") != null ? (String) request
						.getSession().getAttribute("companiaAnalista") : "";

				/** Buscamos los beneficiarios y proveedores educativos */
				showResultToView = solAction.searchBenefCodPeriodoPagoMeses(
						cedula, companiaAnalista);
				
				/** Los beneficiarios, lo volvemo a filtar si es que vamos a realizar un pago, este viene en esta variable
				 * showResultToView.getBeneficiarios();**/
				/****Si es pago, los beneficiarios debe filtrar los que por lo menos que tengan rif del colegio, el pago de matricula y periodo
				 * que no sean igual a cero**************/	
				     
				if ("pagoActualizar.jsp".equalsIgnoreCase(pagIr)||"pagoConvConsultar.jsp".equalsIgnoreCase(pagIr) || "pagoConvRegistrar.jsp".equalsIgnoreCase(pagIr)){
					List<BeneficiarioValNom> benefPuedenPagar= new ArrayList<BeneficiarioValNom>();
					List<BeneficiarioValNom> benef=showResultToView.getBeneficiarios();
					if (benef!=null && !benef.isEmpty()){
						for (BeneficiarioValNom obj:benef){
							if (!StringUtils.isEmpty(obj.getValor()) && StringUtils.isNumeric(obj.getValor())){
						          if (movStSolicBeiDao.isSolicitudActivaParaRealizarPago(obj.getValor())){
						        	  benefPuedenPagar.add(obj);
						          }							
							}
						}
						showResultToView.setBeneficiarios( new ArrayList<BeneficiarioValNom>());
						showResultToView.setBeneficiarios(benefPuedenPagar);
						
					}
					
				 List<ValorNombre> statusLst=null;
					statusLst= new ArrayList<ValorNombre>();
					ValorNombre valorNombre = new ValorNombre(Constantes.CO_STATUS_ACTIVO,rb.getString("activo"));
					statusLst.add(valorNombre);
					valorNombre = new ValorNombre(Constantes.CO_STATUS_DESINCORPORADO,rb.getString("desincorporado"));
					statusLst.add(valorNombre);
					request.setAttribute("statusLst", statusLst);

					if(showResultToView.getBeneficiarios()==null || showResultToView.getBeneficiarios().isEmpty() || showResultToView.getBeneficiarios().size()==0){
						showResultToView.setMensaje("fracaso3");
					}
				
			
				}	

				request.setAttribute("showResultToView", showResultToView);

			
				despachador = request.getRequestDispatcher("/jsp/" + pagIr);
				despachador.forward(request, response);
				/** Vamos a la pag Incluir solicitud */
			}

		}

	}
	
	
	 

}
