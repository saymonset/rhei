package ve.org.bcv.rhei.negocio;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

import ve.org.bcv.rhei.bean.Compania;
import ve.org.bcv.rhei.bean.Empleado;
import ve.org.bcv.rhei.bean.TiposDeDato;
import ve.org.bcv.rhei.util.Constantes;
import ve.org.bcv.rhei.util.Paginador;

import com.bcv.dao.jdbc.BeneficioEscolarDao;
import com.bcv.dao.jdbc.ParametroDao;
import com.bcv.dao.jdbc.impl.BeneficioEscolarDaoImpl;
import com.bcv.dao.jdbc.impl.MovStSolicBeiDaoImpl;
import com.bcv.dao.jdbc.impl.ParametroDaoImpl;
import com.bcv.model.Parametro;

public class ParametroProcesar extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ParametroProcesar.class
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
	private String enlace = "";
	private String filtroParametro = "";
	private Parametro parametro = null;
	private int cuantosReg = 0;
	private int indMenor = 0;
	private int indMayor = Constantes.paginas;
	private String tablaParametros = "";
	private String titulo = "";
	private BeneficioEscolarDao beneficioEscolarDao = new BeneficioEscolarDaoImpl();
	private ParametroDao parametroDao= new ParametroDaoImpl();
	private MovStSolicBeiDaoImpl movStSolicBeiDao=new MovStSolicBeiDaoImpl();

	public ParametroProcesar(ServletContext sc) {
		super();
		this.sc = sc;
	}

	public void init(ServletConfig config) throws ServletException {
		this.sc = config.getServletContext();
	}

	public void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	public void save(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String tabla = "";
		String operacion = "";
		String beneficioEscolar = "";
		String enlaceVolver = "";

		String filtroParametro = "";

	

		Parametro parametro = null;

		if (request.getParameter("tabla") != null) {
			tabla = request.getParameter("tabla");
		}
		if (request.getParameter("operacion") != null) {
			operacion = request.getParameter("operacion");
		}
		if (request.getParameter("beneficioEscolar") != null) {
			beneficioEscolar = request.getParameter("beneficioEscolar");
		}
		if (request.getParameter("filtroParametro") != null) {
			filtroParametro = request.getParameter("filtroParametro");
		}

		if (tabla.compareToIgnoreCase("parametro") == 0) {
			enlaceVolver =

			"<a href=\"/rhei/ManejadorVariable?beneficioEscolar="
					+ beneficioEscolar
					+ "&operacion="
					+ operacion
					+ "&tabla="
					+ tabla
					+ "&filtroParametro="
					+ filtroParametro
					+ "\" tabindex=\"\" "
					+ "title=\"Volver\" accesskey=\"A\">Pulse aqu&iacute; para Volver</a>";
		}
		log.debug("Valor de enlaceVolver: " + enlaceVolver);
		try {
			if (tabla.compareToIgnoreCase("parametro") == 0) {
				parametro = new Parametro();

				parametro.setCodigoCompania(request.getParameter("compania"));
				parametro.setTipoEmpleado(request.getParameter("tipoEmp"));
				parametro.setCodigoParametro(request.getParameter(
						"nombreParametro"));
				parametro.setTipoBeneficio(request
						.getParameter("tipoBeneficio"));
				parametro.setFechaVigencia(request
						.getParameter("fechaVigencia"));
				parametro.setObservaciones(request.getParameter("descripcion")
						);
				parametro
						.setTipoDatoParametro(request.getParameter("tipoDato"));
				parametro.setValorParametro(request.getParameter(
						"valorParametro"));
				
				
				
				if ("MTOBCV".equalsIgnoreCase(parametro.getCodigoParametro())){
					if (StringUtils.isNumeric(parametro.getValorParametro())){
						try {
							parametro
							.setValorParametro(Double.valueOf(Double
									.parseDouble(parametro.getValorParametro()
											.toString().replace(".", "")
											.replace(",", ".")))+"");	
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}
				if ("MTOUTILES".equalsIgnoreCase(parametro.getCodigoParametro())){
					if (StringUtils.isNumeric(parametro.getValorParametro())){
						try {
							parametro
							.setValorParametro(Double.valueOf(Double
									.parseDouble(parametro.getValorParametro()
											.toString().replace(".", "")
											.replace(",", ".")))+"");	
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}
				
				
				
				
				
				
				if (((parametroDao.existeParametro(parametro.getCodigoCompania(),parametro.getTipoEmpleado(),parametro.getTipoBeneficio(),parametro.getCodigoParametro(),parametro.getFechaVigencia())) && (operacion
						.compareToIgnoreCase("crear") != 0))
						|| (!parametroDao.existeParametro(parametro.getCodigoCompania(),parametro.getTipoEmpleado(),parametro.getTipoBeneficio(),parametro.getCodigoParametro(),parametro.getFechaVigencia()))) {
					try {
						
						
						this.mensaje = parametroDao.guardarParametro(operacion,parametro.getCodigoCompania(),parametro.getTipoEmpleado(),parametro.getTipoBeneficio(),parametro.getCodigoParametro(),parametro.getFechaVigencia(),parametro.getValorParametro(),
								parametro.getTipoDatoParametro(),parametro.getObservaciones());
					} catch (Exception e) {
						String existeConstraint = e.toString();

						if (existeConstraint
								.contains("SQLIntegrityConstraintViolationException")) {
							this.mensaje = "Constraint";
							this.infoError = existeConstraint;
						} else {
							this.mensaje = "Error";
						}

						request.setAttribute("mensaje", "Error");
					}

					if (this.mensaje.compareToIgnoreCase("Exito") == 0) {
						
						
						
						/**Actualizarmos el monto de bcv para la tabla MOV_ST_SOLIC_BEI, e internamente a este metodo actualizamos la tabla  RHEI.RELACION_PAGOS */
						if (parametro.getCodigoParametro().compareToIgnoreCase("MTOBCV") == 0) {
							movStSolicBeiDao.SearchMontoBCV(parametro.getCodigoCompania(),Double.valueOf(Double
									.parseDouble(parametro.getValorParametro().replace(".", "")
											.replace(",", "."))));
						}
						
						
						
					 
						request.setAttribute("mensaje", this.mensaje);
					} else {
						request.setAttribute("parametro", parametro);
						request.setAttribute("mensaje", this.mensaje);
					}
				} else if ((parametroDao.existeParametro(parametro.getCodigoCompania(),parametro.getTipoEmpleado(),parametro.getTipoBeneficio(),parametro.getCodigoParametro(),parametro.getFechaVigencia()))
						&& (operacion.compareToIgnoreCase("crear") == 0)) {
					request.setAttribute("parametro", parametro);
					request.setAttribute("mensaje", "Error");
				}
				request.setAttribute("beneficioEscolar", beneficioEscolar);
			}
			request.setAttribute("infoError", infoError);
			request.setAttribute("tabla", tabla);
			request.setAttribute("operacion", operacion);

			request.setAttribute("viene2", "viene2");
			request.setAttribute("enlaceVolver", enlaceVolver);

		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
		}
	}

	public void findParametro(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Parametro parametro = null;
		boolean isParametroMTBCV=false;

		try {
		 
	 
			parametro = new Parametro();
			/** begin PK BD */
			String compania = "";
			if (request.getParameter("compania") != null) {
				compania = request.getParameter("compania");
				request.setAttribute("compania", compania);
			}
			parametro.setCodigoCompania(compania);
			
			String formatNumerico="false";
			if (request.getParameter("formatNumerico") != null) {
				formatNumerico = request.getParameter("formatNumerico");
			}
			

			String tipoEmp = "";
			if (request.getParameter("tipoEmp") != null) {
				tipoEmp = request.getParameter("tipoEmp");
				request.setAttribute("tipoEmp", tipoEmp);
			}
			parametro.setTipoEmpleado(tipoEmp);

			String tipoBeneficio = "";
			if (request.getParameter("tipoBeneficio") != null) {
				tipoBeneficio = request.getParameter("tipoBeneficio");
				request.setAttribute("tipoBeneficio", tipoBeneficio);
				request.setAttribute("beneficioEscolar", tipoBeneficio);
			}
			parametro.setTipoBeneficio(tipoBeneficio);

			String fechaVigencia = "";
			if (request.getParameter("fechaVigencia") != null) {
				fechaVigencia = request.getParameter("fechaVigencia");
				request.setAttribute("fechaVigencia", fechaVigencia);
			}
			parametro.setFechaVigencia(fechaVigencia);

			String codParametro = "";
			if (request.getParameter("nombreParametro") != null) {
				codParametro = request.getParameter("nombreParametro")
						;
				request.setAttribute("nombreParametro", codParametro);
			}
			/** end PK BD */

			/** Chequeamos y obtenemos los valores de BD */
			parametro.setCodigoParametro(codParametro);
			parametro = parametroDao.findParametro(parametro.getCodigoCompania(),parametro.getTipoEmpleado(),parametro.getTipoBeneficio(),parametro.getCodigoParametro(),parametro.getFechaVigencia());
			if ("true".equalsIgnoreCase(formatNumerico)){
				/*** Calculamos el total *******/
				DecimalFormat df2 = new DecimalFormat(Constantes.FORMATO_DOUBLE,
						new DecimalFormatSymbols(new Locale("es", "VE")));
				if (parametro!=null && StringUtils.isNumeric(parametro.getValorParametro())){
					BigDecimal value = new BigDecimal(parametro.getValorParametro());
					parametro.setValorParametro(new String(df2.format(value.floatValue())));
				}
				
				/*** Fin Calculamos el total en el memorando con su formato *******/
			}
			/** (-1) significa que esta vacio */
			if (!"-1".equalsIgnoreCase(parametro.getCodigoCompania())) {
				request.setAttribute("compania", parametro.getCodigoCompania());
				 
				//MTOBCV
				
				if ("MTOBCV".equalsIgnoreCase(parametro.getCodigoParametro())) {
					isParametroMTBCV=true;
				} 
				request.setAttribute("isParametroMTBCV", isParametroMTBCV);
				request.setAttribute("tipoEmp", parametro.getTipoEmpleado());
				request.setAttribute("tipoBeneficio",
						parametro.getTipoBeneficio());
				request.setAttribute("beneficioEscolar", tipoBeneficio);

				request.setAttribute("fechaVigencia",
						parametro.getFechaVigencia());
				request.setAttribute("nombreParametro",
						parametro.getCodigoParametro());
				request.setAttribute("tipoDato",
						parametro.getTipoDatoParametro());
				request.setAttribute("valorParametro",
						parametro.getValorParametro());
				request.setAttribute("descripcion",
						parametro.getObservaciones());

			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
		}
	}
	
	
	public void deleteParametro(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Parametro parametro = null;
		try {
			parametro = new Parametro();
			/** begin PK BD */
			String compania = "";
			if (request.getParameter("compania") != null) {
				compania = request.getParameter("compania");
				request.setAttribute("compania", compania);
			}
			parametro.setCodigoCompania(compania);

			String tipoEmp = "";
			if (request.getParameter("tipoEmp") != null) {
				/**sI ES VACION ES PORQUE VIENE TODOS %*/
				tipoEmp = request.getParameter("tipoEmp");
				if ("".equalsIgnoreCase(tipoEmp)){
					tipoEmp="%";
				}
				
				request.setAttribute("tipoEmp", tipoEmp);
			}
			parametro.setTipoEmpleado(tipoEmp);

			String tipoBeneficio = "";
			if (request.getParameter("tipoBeneficio") != null) {
				tipoBeneficio = request.getParameter("tipoBeneficio");
				request.setAttribute("tipoBeneficio", tipoBeneficio);
				request.setAttribute("beneficioEscolar", tipoBeneficio);
			}
			parametro.setTipoBeneficio(tipoBeneficio);

			String fechaVigencia = "";
			if (request.getParameter("fechaVigencia") != null) {
				fechaVigencia = request.getParameter("fechaVigencia");
				request.setAttribute("fechaVigencia", fechaVigencia);
			}
			parametro.setFechaVigencia(fechaVigencia);

			String codParametro = "";
			if (request.getParameter("nombreParametro") != null) {
				codParametro = request.getParameter("nombreParametro")
						;
				request.setAttribute("nombreParametro", codParametro);
			}
			/** end PK BD */

			/** Chequeamos y obtenemos los valores de BD */
			parametro.setCodigoParametro(codParametro);
			int result = parametroDao.deleteParametro(parametro.getCodigoCompania(),parametro.getTipoEmpleado(),parametro.getTipoBeneficio(),parametro.getCodigoParametro(),parametro.getFechaVigencia());
			if (result>0) {
				request.setAttribute("exito","exito");
				 
			}else{
				request.setAttribute("fracaso","fracaso");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
		 
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
		TiposDeDato tiposDeDato = new TiposDeDato();
		List<TiposDeDato> tiposDeDatos = tiposDeDato.getTiposDeDatos();
		request.setAttribute("tiposDeDatos", tiposDeDatos);
		
		Compania compania = new Compania();
		String filtro = request.getSession().getAttribute("companiaAnalista") != null ? request
				.getSession().getAttribute("companiaAnalista").toString()
				: "";
		request.setAttribute("listadoCompanias", compania.llenarData(filtro));

		Empleado obj = new Empleado();
		request.setAttribute("listadoEmpleados", obj.getEmpleados());
	}

	public void agregarParamByBenef(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.texto = "Agregar Par&aacute;metro";
		this.beneficioEscolar = "";
		if (request.getParameter("beneficioEscolar") != null) {
			this.beneficioEscolar = request.getParameter("beneficioEscolar");
			request.setAttribute("beneficioEscolar", this.beneficioEscolar);
		}

	}

	public void parametrosByBeneficio(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		primeravez(request, response);
		this.companiaAnalista = "";
		 
	 
		companiaAnalista = request.getSession().getAttribute(
				"companiaAnalista") != null ? (String) request
				.getSession().getAttribute("companiaAnalista") : "01";
				/**The company always be 01*/
				companiaAnalista="01";
	 
		this.beneficioEscolar = "";
		if (request.getParameter("beneficioEscolar") != null) {
			this.beneficioEscolar = request.getParameter("beneficioEscolar");
		}

		this.texto = "Agregar Par&aacute;metro";
		this.enlace = ("<tr><td colspan=\"1\"><a href=\"/rhei/ParametroControlador?principalParametro.do=blankParam&beneficioEscolar="
				+ this.beneficioEscolar
				+ "&operacion=crear"
				+ "&tabla=parametro&filtroParametro="
				+ this.filtroParametro
				+ "&" + "accesskey=\"A\">" + this.texto + "</a></td></tr>");
		log.debug("enlace=" + enlace);

		this.filtroParametro = "";
		if (request.getParameter("filtroParametro") != null) {
			this.filtroParametro = request.getParameter("filtroParametro");
					;
		}

		this.parametro = new Parametro();

		/** numero de Registros que tiene la consulta */
		if (request.getParameter("cuantosReg") != null) {
			this.cuantosReg = Integer.valueOf(request
					.getParameter("cuantosReg"));
		} else {
			this.cuantosReg = parametroDao.cuantosSql(this.beneficioEscolar,
					this.filtroParametro, this.companiaAnalista);
		}
		this.indMenor = 0;
		if (request.getParameter("indMenor") != null) {
			this.indMenor = Integer.valueOf(request.getParameter("indMenor"));
		}
		this.indMayor = Constantes.paginas;
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
         /**BEGIN LISTA DE TODOS LOS PARAMETROS****************************/
		this.tablaParametros = this.parametroDao.generadorTablaParametros(
				this.beneficioEscolar, "parametro", this.filtroParametro,
				this.companiaAnalista, this.indMenor, this.indMayor);
	    /**END LISTA DE TODOS LOS PARAMETROS****************************/

		if (this.tablaParametros.equalsIgnoreCase("No hay registros")) {
			if (this.filtroParametro.compareTo("") == 0) {
				this.mensaje = ("No hay parámetros registrados en el sistema para el beneficio escolar "
						+ this.beneficioEscolar + ".");
			} else {
				this.mensaje = ("No existe el par&aacute;metro "
						+ this.filtroParametro + " para el beneficio escolar "
						+ this.beneficioEscolar + ".");
			}
			request.setAttribute("mensaje", this.mensaje);
		}
		request.setAttribute("beneficioEscolar", this.beneficioEscolar);
		request.setAttribute("filtroParametro", this.filtroParametro);
		request.setAttribute("viene", "viene");
		request.setAttribute("vieneParametro", "viene");
		request.setAttribute("cuantosReg", cuantosReg);
		request.setAttribute("indMenor", indMenor);
		request.setAttribute("indMayor", indMayor);
		request.setAttribute("enlace", this.enlace);
		request.setAttribute("tablaParametros", tablaParametros);
		request.setAttribute("titulo", this.titulo);

	}

	public void primeravez(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		List<String> listaBeneficio = new ArrayList<String>();
		listaBeneficio = beneficioEscolarDao.buscarBeneficiosEscolares();
		request.setAttribute("listaBeneficio", listaBeneficio);
		request.setAttribute("tabla", "parametro");
	}

	public static Logger getLog() {
		return log;
	}

	public static void setLog(Logger log) {
		ParametroProcesar.log = log;
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

	public String getEnlace() {
		return enlace;
	}

	public void setEnlace(String enlace) {
		this.enlace = enlace;
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
