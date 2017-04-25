package ve.org.bcv.rhei.negocio;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ve.org.bcv.rhei.bean.TipoEstado;
import ve.org.bcv.rhei.util.Constantes;
import ve.org.bcv.rhei.util.Paginador;
import ve.org.bcv.rhei.util.Utilidades;

import com.bcv.dao.jdbc.BeneficioEscolarDao;
import com.bcv.dao.jdbc.ManejadorDB;
import com.bcv.dao.jdbc.impl.BeneficioEscolarDaoImpl;
import com.bcv.model.BeneficioEscolar;

/**
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com
 * 16/04/2015 15:01:01
 * 
 */
public class BenefScolarProcesar extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(BenefScolarProcesar.class
			.getName());
	private ServletContext sc;
	 
	private RequestDispatcher rd = null;
	private HttpSession sesion = null;
	 
	private String mensaje = "";
	private String infoError = "";
	private String aviso = "FinSesion";
	private String beneficioEscolar = "";

	private String companiaAnalista = "";

	private String filtroParametro = "";
	 
	private int cuantosReg = 0;
	private int indMenor = 0;
	private int indMayor = Constantes.paginas;
	private String tablaParametros = "";
	private BeneficioEscolarDao beneficioEscolarDao = new BeneficioEscolarDaoImpl();

	public BenefScolarProcesar(ServletContext sc) {
		super();
		this.sc = sc;
	}

	public void init(ServletConfig config) throws ServletException {
		this.sc = config.getServletContext();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	public void save(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		 
		try {
			 
			BeneficioEscolar beneficio = new BeneficioEscolar();
			String operacion = request.getParameter("operacion");
			beneficio.setCodigoBeneficio(request
					.getParameter("nombreBeneficio")!=null?request
					.getParameter("nombreBeneficio").toUpperCase():"");
			beneficio.setDescripcion(request.getParameter("valorBeneficio")!=null?request.getParameter("valorBeneficio")
					.toUpperCase():"");
			beneficio.setFechaRegistro(request.getParameter("fechaRegistro")!=null?request.getParameter("fechaRegistro"):"");
			beneficio.setCondicion(request.getParameter("estado")!=null?request.getParameter("estado"):"");
			if (((beneficioEscolarDao.existeBeneficioEscolar(beneficio.getCodigoBeneficio() )) && (operacion
					.compareToIgnoreCase("crear") != 0))
					|| (!beneficioEscolarDao.existeBeneficioEscolar( beneficio.getCodigoBeneficio()))) {
				mensaje = beneficioEscolarDao.guardarBeneficioEscolar(  operacion, beneficio.getCodigoBeneficio(), beneficio.getDescripcion(), beneficio.getFechaRegistro(), beneficio.getCondicion());
				if (this.mensaje.compareToIgnoreCase("Exito") == 0) {
					 
					request.setAttribute("mensaje", this.mensaje);
				} else {
					request.setAttribute("beneficio", beneficio);
					request.setAttribute("mensaje", this.mensaje);
				}
			} else if ((beneficioEscolarDao.existeBeneficioEscolar(  beneficio.getCodigoBeneficio()))
					&& (operacion.compareToIgnoreCase("crear") == 0)) {
				request.setAttribute("beneficio", beneficio);
				request.setAttribute("mensaje", "Error");
			}
			request.setAttribute("viene2", "viene2");
			 
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
		 
		}

	}
	
	
	
	/**
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	 
			BeneficioEscolar beneficio = new BeneficioEscolar();
			String co_tipo_beneficio=request.getParameter("co_tipo_beneficio")!=null?(String)request.getParameter("co_tipo_beneficio"):"";
			beneficio.setCodigoBeneficio(co_tipo_beneficio); 
			int num=beneficioEscolarDao.delete(beneficio.getCodigoBeneficio());
			if (num>0){
				request.setAttribute("exito", "exito");	
			}else{
				request.setAttribute("fracaso", "fracaso");
				request.setAttribute("co_tipo_beneficio", co_tipo_beneficio);
			}
	}
	
	

	public void findParametro(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	 
	
		BeneficioEscolar beneficio = new BeneficioEscolar();

		try {
	 

			
			
			/** begin PK BD */
			String co_tipo_beneficio = "";
			if (request.getParameter("co_tipo_beneficio") != null) {
				co_tipo_beneficio = request.getParameter("co_tipo_beneficio");
				beneficio.setCodigoBeneficio(co_tipo_beneficio);
			}
			/**Es el nombre que contiene la vista*/
			if (beneficio.getCodigoBeneficio()==null || "".equalsIgnoreCase(beneficio.getCodigoBeneficio())){
				beneficio.setCodigoBeneficio(request.getParameter("nombreBeneficio"));
			}
			 
			/** end PK BD */

			/** Chequeamos y obtenemos los valores de BD */
			beneficio = beneficioEscolarDao.findParametro(beneficio.getCodigoBeneficio() );
		 
				request.setAttribute("nombreBeneficio", beneficio.getCodigoBeneficio());
				request.setAttribute("valorBeneficio", beneficio.getDescripcion());
				request.setAttribute("fechaRegistro", beneficio.getFechaRegistro());
				request.setAttribute("estado", beneficio.getCondicion());
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			 
		}
	}

	public void blanquearCampos(HttpServletRequest request) {
		  request.setAttribute("nombreBeneficio","");
			request.setAttribute("valorBeneficio","");
			request.setAttribute("fechaRegistro","");
			request.setAttribute("estado","");
	}

	/**
	 * LLenamos tipo de dato, compania y empleados
	 */
	public void llenarTdatoCompaniaEmp(HttpServletRequest request) {
		TipoEstado tipoEstado = new TipoEstado();
		request.setAttribute("tipoEstados", tipoEstado.getTipoEstados());
	}

	public void agregarParamByBenef(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	public void parametrosByBeneficio(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void primeravez(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

//		String enlace = ("<tr><td colspan=\"1\"><a href=\"/rhei/benefScolarControlador?principal.do=blankParam"
//				+ "\" tabindex=\"\" "
//				+ "title=\""
//				+ "Agregar Beneficio Escolar"
//				+ "\" accesskey=\"A\">"
//				+ "Agregar Beneficio Escolar" + "</a></td></tr>");
		BeneficioEscolar beneficio = new BeneficioEscolar();

		/** numero de Registros que tiene la consulta */
		if (request.getParameter("cuantosReg") != null) {
			this.cuantosReg = Integer.valueOf(request
					.getParameter("cuantosReg"));
		} else {
			this.cuantosReg = beneficioEscolarDao.cuantosSql("RHEI.BENEFICIO_ESCOLAR");
		}
		if (request.getParameter("indMenor") != null) {
			this.indMenor = Integer.valueOf(request.getParameter("indMenor"));
		}
		if (request.getParameter("indMayor") != null) {
			this.indMayor = Integer.valueOf(request.getParameter("indMayor"));
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

		/**Generamos la tabla de beneficios escolares*/
		String tablaBeneficios = beneficioEscolarDao.generadorTablaBeneficioEscolar(
				"beneficioEscolar", this.indMenor, this.indMayor);
		if (tablaBeneficios.equalsIgnoreCase("No hay registros")) {
			request.setAttribute("mensaje",
					"No hay beneficios escolares registrados en el sistema ");
		}
		request.setAttribute("tablaBeneficios", tablaBeneficios);
		request.setAttribute("viene", "viene");
		request.setAttribute("cuantosReg", cuantosReg);
		request.setAttribute("indMenor", indMenor);
		request.setAttribute("indMayor", indMayor);
	}

	public ServletContext getSc() {
		return sc;
	}

	public void setSc(ServletContext sc) {
		this.sc = sc;
	}
 

	public RequestDispatcher getRd() {
		return rd;
	}

	public void setRd(RequestDispatcher rd) {
		this.rd = rd;
	}

	public HttpSession getSesion() {
		return sesion;
	}

	public void setSesion(HttpSession sesion) {
		this.sesion = sesion;
	}

	 

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getInfoError() {
		return infoError;
	}

	public void setInfoError(String infoError) {
		this.infoError = infoError;
	}

	public String getAviso() {
		return aviso;
	}

	public void setAviso(String aviso) {
		this.aviso = aviso;
	}

	public String getBeneficioEscolar() {
		return beneficioEscolar;
	}

	public void setBeneficioEscolar(String beneficioEscolar) {
		this.beneficioEscolar = beneficioEscolar;
	}

	public String getCompaniaAnalista() {
		return companiaAnalista;
	}

	public void setCompaniaAnalista(String companiaAnalista) {
		this.companiaAnalista = companiaAnalista;
	}

	public String getFiltroParametro() {
		return filtroParametro;
	}

	public void setFiltroParametro(String filtroParametro) {
		this.filtroParametro = filtroParametro;
	}

 

	public int getCuantosReg() {
		return cuantosReg;
	}

	public void setCuantosReg(int cuantosReg) {
		this.cuantosReg = cuantosReg;
	}

	public int getIndMenor() {
		return indMenor;
	}

	public void setIndMenor(int indMenor) {
		this.indMenor = indMenor;
	}

	public int getIndMayor() {
		return indMayor;
	}

	public void setIndMayor(int indMayor) {
		this.indMayor = indMayor;
	}

	public String getTablaParametros() {
		return tablaParametros;
	}

	public void setTablaParametros(String tablaParametros) {
		this.tablaParametros = tablaParametros;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}