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

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.bcv.model.BeneficioEscolar;
import com.bcv.model.PeriodoEscolar;

import ve.org.bcv.rhei.negocio.ParametroProcesar;
 
/**
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com
 * 26/01/2015 19:58:28
 *  Controlador para gestionar parametros
 */
public class ParametroControlador extends HttpServlet implements Serializable {
	private static Logger log = Logger.getLogger(ParametroControlador.class
			.getName());

	
	BeneficioEscolar beneficio = null;
	PeriodoEscolar periodo = null;
	
	
	String tablaBeneficios = "";
	String tablaPeriodos = "";
	String condicion = "A";
	
	String mensaje = "";
	String pagina = "/jsp/parametroPrincipal.jsp";
	String pagAgregarParam = "/jsp/parametroAgregar.jsp";
	String pagModifyParam = "/jsp/parametroModify.jsp";
	String tabla = "";

	
	
	
	
	private ServletContext sc;
	private String aviso = "FinSesion";
	private RequestDispatcher rd = null;
	private HttpSession sesion = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	 public void init(ServletConfig config)
		     throws ServletException
		   {
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
		ParametroProcesar parametroProcesar = new ParametroProcesar(this.sc);
		
		if (request.getSession(false) == null) {
			this.sesion = request.getSession(true);
			this.sesion.setAttribute("aviso", this.aviso);

			this.rd = request.getRequestDispatcher("/jsp/login.jsp");
			this.rd.forward(request, response);
		}else{
		 
			String princi="";
			if (request!=null &&request.getParameter("principalParametro.do")!=null &&!StringUtils.isEmpty(request.getParameter("principalParametro.do"))){
				princi=(String)request.getParameter("principalParametro.do");
			}
			 
			RequestDispatcher despachador = null;
			if ("principalParam".equals(request.getParameter("principalParametro.do"))) {
				parametroProcesar.primeravez(request, response);
				despachador = request.getRequestDispatcher(pagina);
				despachador.forward(request, response);
			} else if ("listaDeParam".equals(request.getParameter("principalParametro.do"))) {
				parametroProcesar.parametrosByBeneficio(request, response);
				despachador = request.getRequestDispatcher(pagina);
				despachador.forward(request, response);
			} else if ("blankParam".equals(request.getParameter("principalParametro.do"))) {
				parametroProcesar.agregarParamByBenef( request,
						 response);
				parametroProcesar.llenarTdatoCompaniaEmp(request);
				despachador = request.getRequestDispatcher(pagAgregarParam);
				despachador.forward(request, response);
			} else if ("addParam".equals(request.getParameter("principalParametro.do"))) {
				parametroProcesar.save(request, response);
				parametroProcesar.agregarParamByBenef( request,
						 response);
				parametroProcesar.llenarTdatoCompaniaEmp(request);
				parametroProcesar.findParametro(request, response);
				parametroProcesar.blanquearCampos( request);
				despachador = request.getRequestDispatcher(pagAgregarParam);
				despachador.forward(request, response);
			}else if ("findParametro".equals(request.getParameter("principalParametro.do"))) {
				ParametroProcesar guardarParametro = new ParametroProcesar(this.sc);
				guardarParametro.findParametro(request, response);
				parametroProcesar.llenarTdatoCompaniaEmp(request);
				despachador = request.getRequestDispatcher(pagModifyParam);
				despachador.forward(request, response);
			}else if ("modifyParam".equals(request.getParameter("principalParametro.do"))) {
				parametroProcesar.save(request, response);
				parametroProcesar.llenarTdatoCompaniaEmp(request);
				parametroProcesar.findParametro(request, response);
				 
				despachador = request.getRequestDispatcher(pagModifyParam);
				despachador.forward(request, response);
			}else if ("delete".equals(request.getParameter("principalParametro.do"))) {
				ParametroProcesar guardarParametro = new ParametroProcesar(this.sc);
				guardarParametro.deleteParametro(request, response);
				/***/
				parametroProcesar.parametrosByBeneficio(request, response);
				despachador = request.getRequestDispatcher(pagina);
				despachador.forward(request, response);
			}
			
			
		} 
		
	}
	
	

}
