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

import ve.org.bcv.rhei.negocio.ModifLoteSolProcesar;

/**
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com
 * 02/02/2015 13:54:02
 * Clase que modific< el lote de solicitud...
 */
public class ModifLoteSolControlador extends HttpServlet implements Serializable {
	private static Logger log = Logger.getLogger(ModifLoteSolControlador.class
			.getName());

	
	BeneficioEscolar beneficio = null;
	PeriodoEscolar periodo = null;
	
	
	String tablaBeneficios = "";
	String tablaPeriodos = "";
	String condicion = "A";
	
	String mensaje = "";
	String pagina = "/jsp/modifLoteSolPrincipal.jsp";
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
		
		ModifLoteSolProcesar procesar = new ModifLoteSolProcesar(this.sc);
		if (request.getSession(false) == null) {
			this.sesion = request.getSession(true);
			this.sesion.setAttribute("aviso", this.aviso);

			this.rd = request.getRequestDispatcher("/jsp/login.jsp");
			this.rd.forward(request, response);
		}else{
			RequestDispatcher despachador = null;
			if ("principal".equals(request.getParameter("principal.do"))) {
				procesar.verificarCambioSalarioMinimo(  request,   response);
				despachador = request.getRequestDispatcher(pagina);
				despachador.forward(request, response);
			} else if ("actualizar_lote".equals(request.getParameter("principal.do"))) {
				procesar.actualizarLote(request, response);
				procesar.verificarCambioSalarioMinimo(  request,   response);
				despachador = request.getRequestDispatcher(pagina);
				despachador.forward(request, response);
			}  
		} 
		
	}
	
	

}
