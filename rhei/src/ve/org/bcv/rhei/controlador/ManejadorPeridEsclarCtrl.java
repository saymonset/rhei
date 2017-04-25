package ve.org.bcv.rhei.controlador;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ve.org.bcv.rhei.util.Constantes;
import ve.org.bcv.rhei.util.Paginador;

import com.bcv.dao.jdbc.PeriodoEscolarDao;
import com.bcv.dao.jdbc.impl.PeriodoEscolarDaoImpl;
import com.bcv.model.BeneficioEscolar;
import com.bcv.model.Parametro;
import com.bcv.model.PeriodoEscolar;

/**
 * Manejando el peiodo escolar
 * @author Ing Simon Rodriguez 2015 01 14
 * @Mail saymon_set@hotmail.com
 *
 */
public class ManejadorPeridEsclarCtrl extends HttpServlet {
	private static final long serialVersionUID = 2365899959514740397L;
	private static Logger log = Logger.getLogger(ManejadorPeridEsclarCtrl.class
			.getName());
	Parametro parametro = null;
	BeneficioEscolar beneficio = null;
	PeriodoEscolar periodo = null;
	String beneficioEscolar = "";
	String tablaParametros = "";
	String tablaBeneficios = "";
	String tablaPeriodos = "";
	String condicion = "A";
	String enlace = "";
	String mensaje = "";
	String pagina = "/jsp/PrincipalVariable.jsp";
	String tabla = "";
	private int cuantosReg = 0;
	private int indMenor = 0;
	private int indMayor = Constantes.paginas;
	String titulo = "";
	String companiaAnalista = "";
	String texto = "";
	String filtroParametro = "";
	private PeriodoEscolarDao periodoEscolarDao = new PeriodoEscolarDaoImpl();
	private ServletContext sc;

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
	 * Gestionamos las varibles de entrada para cerar una tabla de registros
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void comun(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
 
		if (request.getParameter("tabla") != null) {
			this.tabla = request.getParameter("tabla");
		}
		if (request.getParameter("titulo") != null) {
			this.titulo = request.getParameter("titulo");
		}
		if (request.getParameter("companiaAnalista") != null) {
			this.companiaAnalista = request.getParameter("companiaAnalista");
		}

		this.texto = "Agregar Per&iacute;odo Escolar";

		this.enlace = ("<tr><td colspan=\"1\"><a href=\"/rhei/jsp/AdministrarVariable.jsp?operacion=crear&tabla="
				+ this.tabla
				+ "\" tabindex=\"\" "
				+ "title=\""
				+ this.texto
				+ "\" accesskey=\"A\">" + this.texto + "</a></td></tr>");

		if (request.getParameter("filtroParametro") != null) {
			this.filtroParametro = request.getParameter("filtroParametro")
					.toUpperCase();
		}
		if (this.tabla.compareToIgnoreCase("periodoEscolar") == 0) {
			this.periodo = new PeriodoEscolar();
			
			
			/** numero de Registros que tiene la consulta */
			if (request.getParameter("cuantosReg") != null) {
				this.cuantosReg = Integer.valueOf(request
						.getParameter("cuantosReg"));
			} else {
				this.cuantosReg = periodoEscolarDao.cuantosSql();
			}
			if (request.getParameter("indMenor") != null) {
				this.indMenor = Integer.valueOf(request
						.getParameter("indMenor"));
			}
			if (request.getParameter("indMayor") != null) {
				this.indMayor = Integer.valueOf(request
						.getParameter("indMayor"));
			}
			String irPara = request.getParameter("irPara") != null ? (String) request
					.getParameter("irPara") : "";
			if (!"".equalsIgnoreCase(irPara)) {
				Paginador paginador = new Paginador();
				paginador = paginador.devolverSegunPeticion(indMenor, indMayor,
						cuantosReg, irPara.charAt(0));
				this.indMenor = paginador.getIndMenor();
				this.indMayor = paginador.getIndMayor();
			}
			
			
			
			
			this.tablaPeriodos =periodoEscolarDao
					.generadorTablaPeriodoEscolar(this.tabla, this.indMenor, this.indMayor);
			if (this.tablaPeriodos.equalsIgnoreCase("No hay registros")) {
				this.mensaje = "No hay per&iacute;odos escolares registrados en el sistema ";
				request.setAttribute("mensaje", this.mensaje);
			} else {
				request.setAttribute("tablaPeriodos", this.tablaPeriodos);
			}
		} else {
			this.mensaje = "Problema con el valor del par&aacute;metro 'tabla'";
		}
		request.setAttribute("enlace", this.enlace);
		request.setAttribute("titulo", this.titulo);

		request.setAttribute("viene", "viene");
		request.setAttribute("cuantosReg", cuantosReg);
		request.setAttribute("indMenor", indMenor);
		request.setAttribute("indMayor", indMayor);
		log.debug("Contenido de enlace: " + this.enlace);

		log.debug("redirigiendo a la pagina -> " + this.pagina);
		RequestDispatcher despacher = this.sc.getRequestDispatcher(this.pagina);
		log.debug("RequestDispatcher: --> " + despacher);
		log.debug("--> SALIO DEL METODO doPost() del servlet AdministrarVariables");
		despacher.forward(request, response);
	}
}
