package ve.org.bcv.rhei.negocio;

import java.io.IOException;
import java.sql.ResultSet;
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
import org.apache.log4j.Logger;

import ve.org.bcv.rhei.bean.TipoEstado;
import ve.org.bcv.rhei.util.Constantes;
import ve.org.bcv.rhei.util.Paginador;

import com.bcv.dao.jdbc.BeneficioEscolarDao;
import com.bcv.dao.jdbc.PeriodoEscolarDao;
import com.bcv.dao.jdbc.impl.BeneficioEscolarDaoImpl;
import com.bcv.dao.jdbc.impl.PeriodoEscolarDaoImpl;
import com.bcv.model.BeneficioEscolar;
import com.bcv.model.Parametro;
import com.bcv.model.PeriodoEscolar;

/**
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com 16/04/2015 14:59:15
 * 
 */
public class PeriodoScolarProcesar extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(PeriodoScolarProcesar.class
			.getName());
	private ServletContext sc;

	private RequestDispatcher rd = null;
	private HttpSession sesion = null;
	private ResultSet rs = null;
	private String mensaje = "";
	private String infoError = "";
	private String aviso = "FinSesion";
	private String beneficioEscolar = "";
	private String texto = "";
	private String companiaAnalista = "";
	// private String enlace = "";
	private String filtroParametro = "";
	private Parametro parametro = null;
	private int cuantosReg = 0;
	private int indMenor = 0;
	private int indMayor = Constantes.paginas;
	private String tablaParametros = "";
	private String titulo = "";
	private String tablaPeriodos = "";
	private BeneficioEscolarDao beneficioEscolarDao = new BeneficioEscolarDaoImpl();
	private PeriodoEscolarDao periodoEscolarDao = new PeriodoEscolarDaoImpl();

	public PeriodoScolarProcesar(ServletContext sc) {
		super();
		this.sc = sc;
	}

	public void init(ServletConfig config) throws ServletException {
		this.sc = config.getServletContext();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	public void save(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String horaInicio = " 08:00:00";
		String horaFin = " 16:00:00";
		PeriodoEscolar periodo = new PeriodoEscolar();
		String operacion = "";
		if (request.getParameter("operacion") != null) {
			operacion = request.getParameter("operacion");
		}
		                                               
		periodo.setCodigoPeriodo(request.getParameter("codigoPeriodo"));
		periodo.setDescripcion(request.getParameter("descripcionPeriodo"));
		periodo.setFechaInicio(request.getParameter("fechaInicio") + horaInicio);
		periodo.setFechaFin(request.getParameter("fechaFin") + horaFin);
		periodo.setCondicion(request.getParameter("estado"));

		String enlaceVolver =

		"<a href=\"/rhei/ManejadorPeridEsclar?&operacion="
				+ operacion
				+ "&tabla="
				+ "periodoEscolar"
				+ "\" tabindex=\"\" "
				+ "title=\"Volver\" accesskey=\"A\">Pulse aqu&iacute; para Volver</a>";

		try {
	 
			
			/**Chqueamos que no exista una descripciion igual*/
			PeriodoEscolar periodoAux = new PeriodoEscolar();
			periodoAux.setDescripcion(periodo.getDescripcion());
			periodoAux.setCodigoPeriodo(null);
			 if (operacion.compareToIgnoreCase("modificar") == 0) {
				 periodoAux=periodoEscolarDao.findPeriodoByDescripcion( periodo.getDescripcion(),periodo.getCondicion());
			 }else{
				 periodoAux=periodoEscolarDao.findPeriodoByDescripcion( periodo.getDescripcion());	 
			 }
			
			if (periodoAux!=null&&!StringUtils.isEmpty(periodoAux.getCodigoPeriodo())){
				request.setAttribute("viene2", "viene2");
				request.setAttribute("mensaje","Fracaso");
			}else{
 
				this.mensaje = periodoEscolarDao.guardarPeriodoEscolar( 
						operacion,periodo.getDescripcion(),periodo.getFechaInicio(),periodo.getFechaFin(),periodo.getCondicion(),periodo.getCodigoPeriodo());
				if (this.mensaje.compareToIgnoreCase("Exito") == 0) {
					request.setAttribute("mensaje", this.mensaje);
				} else {
					request.setAttribute("periodo", periodo);
					request.setAttribute("mensaje", this.mensaje);
				}

				request.setAttribute("tabla", "periodoEscolar");
				request.setAttribute("operacion", operacion);

				request.setAttribute("viene2", "viene2");
				request.setAttribute("enlaceVolver", enlaceVolver);	
			}

			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}  
	}
	public void deletePeriodoEscolar(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PeriodoEscolar periodoEscolar = null;

		try {

			periodoEscolar = new PeriodoEscolar();
			/** begin PK BD */
			String codigoPeriodo = "";
			if (request.getParameter("codigoPeriodo") != null) {
				codigoPeriodo = request.getParameter("codigoPeriodo");

			}

			/** end PK BD */

			/** Chequeamos y obtenemos los valores de BD */

			periodoEscolar.setCodigoPeriodo(codigoPeriodo);
			int resp = periodoEscolarDao.delete(periodoEscolar.getCodigoPeriodo());
			if (resp >= 0) {
				request.setAttribute("exito", "exito");

			} else {
				request.setAttribute("fracaso", "fracaso");
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	public void findParametro(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PeriodoEscolar periodoEscolar = null;

		try {

			periodoEscolar = new PeriodoEscolar();
			/** begin PK BD */
			String codigoPeriodo = "";
			if (request.getParameter("codigoPeriodo") != null) {
				codigoPeriodo = request.getParameter("codigoPeriodo");

			}

			/** end PK BD */

			/** Chequeamos y obtenemos los valores de BD */

			periodoEscolar.setCodigoPeriodo(codigoPeriodo);
			periodoEscolar = periodoEscolarDao.findParametro(periodoEscolar.getCodigoPeriodo());
			if (periodoEscolar != null
					&& !"".equalsIgnoreCase(periodoEscolar.getCodigoPeriodo())) {
				request.setAttribute("codigoPeriodo",
						periodoEscolar.getCodigoPeriodo());
				request.setAttribute("descripcionPeriodo",
						periodoEscolar.getDescripcion());
				request.setAttribute("fechaInicio",
						periodoEscolar.getFechaInicio());
				request.setAttribute("fechaFin", periodoEscolar.getFechaFin());

				request.setAttribute("estado", periodoEscolar.getCondicion());
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	public void blanquearCampos(HttpServletRequest request) {
		/** No son pk parabuscar por bd */

		request.setAttribute("title", "");
		request.setAttribute("tipoDato", "");
		request.setAttribute("valorParametro", "");

		request.setAttribute("descripcion", "");
		request.setAttribute("fechaVigencia", "");
		request.setAttribute("nombreParametro", "");

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

	/**
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void parametrosByBeneficio(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if (request.getParameter("titulo") != null) {
			this.titulo = request.getParameter("titulo");
		}
		if (request.getParameter("companiaAnalista") != null) {
			this.companiaAnalista = request.getParameter("companiaAnalista");
		}

		if (request.getParameter("filtroParametro") != null) {
			this.filtroParametro = request.getParameter("filtroParametro")
					.toUpperCase();
		}

		PeriodoEscolar periodo = new PeriodoEscolar();

		/** numero de Registros que tiene la consulta */
		if (request.getParameter("cuantosReg") != null) {
			this.cuantosReg = Integer.valueOf(request
					.getParameter("cuantosReg"));
		} else {
			this.cuantosReg = periodoEscolarDao.cuantosSql();
		}
		if (request.getParameter("indMenor") != null) {
			this.indMenor = Integer.valueOf(request.getParameter("indMenor"));
		}
		if (request.getParameter("indMayor") != null) {
			this.indMayor = Integer.valueOf(request.getParameter("indMayor"));
		}
		String irPara = request.getParameter("irPara") != null
				? (String) request.getParameter("irPara")
				: "";
		if (!"".equalsIgnoreCase(irPara)) {
			Paginador paginador = new Paginador();
			paginador = paginador.devolverSegunPeticion(indMenor, indMayor,
					cuantosReg, irPara.charAt(0));
			this.indMenor = paginador.getIndMenor();
			this.indMayor = paginador.getIndMayor();
		}

		/** Agregamos el titulo a la tabla y la tabla */
		this.tablaPeriodos = periodoEscolarDao.generadorTablaPeriodoEscolar(
				"periodoEscolar", this.indMenor, this.indMayor);
		if (this.tablaPeriodos.equalsIgnoreCase("No hay registros")) {
			this.mensaje = "No hay per&iacute;odos escolares registrados en el sistema ";
			request.setAttribute("mensaje", this.mensaje);
		}

		if (!StringUtils.isEmpty(request.getParameter("descripcion"))) {
			request.setAttribute("descripcion",
					(String) request.getParameter("descripcion"));
		}

		request.setAttribute("tablaPeriodos", this.tablaPeriodos);
		request.setAttribute("titulo", this.titulo);

		request.setAttribute("viene", "viene");
		request.setAttribute("cuantosReg", cuantosReg);
		request.setAttribute("indMenor", indMenor);
		request.setAttribute("indMayor", indMayor);

	}

	public void primeravez(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		List<String> listaBeneficio = new ArrayList<String>();
		BeneficioEscolar beneficio = new BeneficioEscolar();
		listaBeneficio = beneficioEscolarDao.buscarBeneficiosEscolares();
		request.setAttribute("listaBeneficio", listaBeneficio);
		request.setAttribute("tabla", "parametro");
	}

	public static Logger getLog() {
		return log;
	}

	public static void setLog(Logger log) {
		PeriodoScolarProcesar.log = log;
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

	public ResultSet getRs() {
		return rs;
	}

	public void setRs(ResultSet rs) {
		this.rs = rs;
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

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
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

	public Parametro getParametro() {
		return parametro;
	}

	public void setParametro(Parametro parametro) {
		this.parametro = parametro;
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

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
