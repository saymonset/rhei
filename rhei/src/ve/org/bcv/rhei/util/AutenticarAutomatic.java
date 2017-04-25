/**
 * 
 */
package ve.org.bcv.rhei.util;

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

 
/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 07/04/2016 14:35:53
 * 2016
 * mail : oraclefedora@gmail.com
 */
public class AutenticarAutomatic   extends HttpServlet implements Serializable {
	 
		 
		private ServletContext sc;
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		  private static String inicio = "/jsp/principal.jsp";

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
		
			HttpSession sesion = null;
			   sesion = request.getSession(true);
             String	companiaAnalista="01";
             sesion.setAttribute("companiaAnalista", companiaAnalista);
             String nombreUsuario =request.getParameter("nombreUsuario")!=null?(String)request.getParameter("nombreUsuario"):"";
             sesion.setAttribute("nombreUsuario", nombreUsuario);
             String cedulaUsuario =request.getParameter("cedulaUsuario")!=null?(String)request.getParameter("cedulaUsuario"):"";
             sesion.setAttribute("cedulaUsuario", cedulaUsuario);
             String grupo =request.getParameter("grupo")!=null?(String)request.getParameter("grupo"):"";
             sesion.setAttribute("grupo", grupo);
	           boolean isAdmin=false;
	           if("GC_User_RHEI_ADMIN".equalsIgnoreCase(grupo)){
	        	   isAdmin=true;
	           }
	           sesion.setAttribute("isAdmin", isAdmin);
	       	sesion.setAttribute("autenticarAutomatic", "true");
	       	RequestDispatcher rd = request.getRequestDispatcher(inicio);
	         rd.forward(request, response);
 
		
		 

		}

	}