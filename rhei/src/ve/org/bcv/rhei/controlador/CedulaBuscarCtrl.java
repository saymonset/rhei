package ve.org.bcv.rhei.controlador;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ve.org.bcv.rhei.bean.ShowResultToView;

@WebServlet(description = "Buscar cedula", urlPatterns = { "/cedulaBuscarCtrl" })
public class CedulaBuscarCtrl extends HttpServlet implements Serializable {
	private static Logger log = Logger.getLogger(IntitucionCtrl.class.getName());

	String mensaje = "";
	String pagIrAfter = "";
	String cedulaBuscarPag = "/jsp/cedulaBuscar.jsp";


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

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		comun(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		comun(request, response);
	}

	protected void comun(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (request.getSession(false) == null) {
			this.sesion = request.getSession(true);
			this.sesion.setAttribute("aviso", this.aviso);
			this.rd = request.getRequestDispatcher("/jsp/login.jsp");
			this.rd.forward(request, response);
		} else {
			RequestDispatcher despachador = null;
			ShowResultToView showResultToView = new ShowResultToView();

			if ("cedulaIrToPag".equals(request.getParameter("principal.do"))) {
				String pagIr = "";
				pagIr = cedulaBuscarPag;
				request.setAttribute("showResultToView", showResultToView);
				despachador = request.getRequestDispatcher(pagIr);
				despachador.forward(request, response);
				/**
				 * LLenamos los datos del trabajador , beneficiario y datos extras del colegio
				 */
			}
		}

	}

}
