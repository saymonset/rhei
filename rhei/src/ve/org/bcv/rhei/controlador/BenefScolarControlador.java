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

import com.bcv.model.BeneficioEscolar;
import com.bcv.model.PeriodoEscolar;

import ve.org.bcv.rhei.negocio.BenefScolarProcesar;

/**
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com
 * 29/01/2015 13:44:30
 * Controlador de Beneficios Escolares ... 
 */
public class BenefScolarControlador extends HttpServlet implements Serializable {
	private static Logger log = Logger.getLogger(BenefScolarControlador.class
			.getName());

	
	BeneficioEscolar beneficio = null;
	PeriodoEscolar periodo = null;
	
	
	String tablaBeneficios = "";
	String tablaPeriodos = "";
	String condicion = "A";
	
	String mensaje = "";
	String pagina = "/jsp/benefScolarPrincipal.jsp";
	String pagAgregar = "/jsp/benefScolarAgregar.jsp";
	String pagModify = "/jsp/benefScolarModify.jsp";
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
		BenefScolarProcesar benefScolarProcesar = new BenefScolarProcesar(this.sc);
		
		if (request.getSession(false) == null) {
			this.sesion = request.getSession(true);
			this.sesion.setAttribute("aviso", this.aviso);

			this.rd = request.getRequestDispatcher("/jsp/login.jsp");
			this.rd.forward(request, response);
		}else{
			RequestDispatcher despachador = null;
			if ("principal".equals(request.getParameter("principal.do"))) {
				benefScolarProcesar.primeravez(request, response);
				despachador = request.getRequestDispatcher(pagina);
				despachador.forward(request, response);
			}  else if ("blankParam".equals(request.getParameter("principal.do"))) {
				benefScolarProcesar.blanquearCampos( request);
				despachador = request.getRequestDispatcher(pagAgregar);
				despachador.forward(request, response);
			} else if ("addParam".equals(request.getParameter("principal.do"))) {
				benefScolarProcesar.save(request, response);
				benefScolarProcesar.blanquearCampos( request);
				despachador = request.getRequestDispatcher(pagAgregar);
				despachador.forward(request, response);
			}else if ("findParametro".equals(request.getParameter("principal.do"))) {
				benefScolarProcesar.findParametro(request, response);
				benefScolarProcesar.llenarTdatoCompaniaEmp(request);
				despachador = request.getRequestDispatcher(pagModify);
				despachador.forward(request, response);
			}else if ("modifyParam".equals(request.getParameter("principal.do"))) {
				benefScolarProcesar.save(request, response);
				benefScolarProcesar.llenarTdatoCompaniaEmp(request);
				benefScolarProcesar.findParametro(request, response);
				despachador = request.getRequestDispatcher(pagModify);
				despachador.forward(request, response);
			}else if ("delete".equals(request.getParameter("principal.do"))) {
				benefScolarProcesar.delete(request, response);
				benefScolarProcesar.primeravez(request, response);
				despachador = request.getRequestDispatcher(pagina);
				despachador.forward(request, response);
			}
		} 
		
	}
	
	

}
