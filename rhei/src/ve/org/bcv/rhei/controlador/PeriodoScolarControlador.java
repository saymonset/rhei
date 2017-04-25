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

import ve.org.bcv.rhei.negocio.PeriodoScolarProcesar;

/**
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com
 * 30/01/2015 15:07:11
 * 
 */
public class PeriodoScolarControlador extends HttpServlet implements Serializable {
	private static Logger log = Logger.getLogger(PeriodoScolarControlador.class
			.getName());

	
	BeneficioEscolar beneficio = null;
	PeriodoEscolar periodo = null;
	
	
	String tablaBeneficios = "";
	String tablaPeriodos = "";
	String condicion = "A";
	
	String mensaje = "";
	String pagina = "/jsp/periodoScolarPrincipal.jsp";
	String pagAgregar  = "/jsp/periodoScolarAgregar.jsp";
	String pagModify  = "/jsp/periodoScolarModify.jsp";
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
		PeriodoScolarProcesar	 periodoScolarProcesar = new PeriodoScolarProcesar(this.sc);
		
		if (request.getSession(false) == null) {
			this.sesion = request.getSession(true);
			this.sesion.setAttribute("aviso", this.aviso);

			this.rd = request.getRequestDispatcher("/jsp/login.jsp");
			this.rd.forward(request, response);
		}else{
			RequestDispatcher despachador = null;
		    if ("lista".equals(request.getParameter("principal.do"))) {
				periodoScolarProcesar.parametrosByBeneficio(request, response);
				despachador = request.getRequestDispatcher(pagina);
				despachador.forward(request, response);
			} else if ("blank".equals(request.getParameter("principal.do"))) {
				periodoScolarProcesar.agregarParamByBenef( request,
						 response);
				despachador = request.getRequestDispatcher(pagAgregar );
				despachador.forward(request, response);
			} else if ("add".equals(request.getParameter("principal.do"))) {
				periodoScolarProcesar.save(request, response);
				despachador = request.getRequestDispatcher(pagAgregar );
				despachador.forward(request, response);
			}else if ("find".equals(request.getParameter("principal.do"))) {
			 
				periodoScolarProcesar.findParametro(request, response);
				periodoScolarProcesar.llenarTdatoCompaniaEmp(request);
				despachador = request.getRequestDispatcher(pagModify );
				despachador.forward(request, response);
			}else if ("modify".equals(request.getParameter("principal.do"))) {
				periodoScolarProcesar.save(request, response);
				periodoScolarProcesar.findParametro(request, response);
				periodoScolarProcesar.llenarTdatoCompaniaEmp(request);
				despachador = request.getRequestDispatcher(pagModify );
				despachador.forward(request, response);
			}else if ("delete".equals(request.getParameter("principal.do"))) {

				periodoScolarProcesar.deletePeriodoEscolar(request, response);
				
				periodoScolarProcesar.parametrosByBeneficio(request, response);
				despachador = request.getRequestDispatcher(pagina);
				despachador.forward(request, response);
					
				} else if ("borrar".equals(request.getParameter("principal.do"))) {
					periodoScolarProcesar.agregarParamByBenef( request,
							 response);
					despachador = request.getRequestDispatcher("/jsp/pruebasAngular.jsp" );
					despachador.forward(request, response);
				} 
		} 
		
	}
	
	

}
