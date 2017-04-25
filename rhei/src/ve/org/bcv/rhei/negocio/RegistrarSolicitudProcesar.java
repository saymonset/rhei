package ve.org.bcv.rhei.negocio;

import java.io.IOException;
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

import com.bcv.dao.jdbc.SolicitudDao;
import com.bcv.dao.jdbc.TrabajadorDao;
import com.bcv.dao.jdbc.impl.SolicitudDaoImpl;
import com.bcv.dao.jdbc.impl.TrabajadorDaoImpl;
import com.bcv.model.Parametro;
import com.bcv.model.Solicitud;
import com.bcv.model.Trabajador;

import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.util.Constantes;

/**
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com 04/02/2015 19:23:19 Procesamos solicitudes
 * 
 */
public class RegistrarSolicitudProcesar extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger
			.getLogger(RegistrarSolicitudProcesar.class.getName());
	private ServletContext sc;

	private RequestDispatcher rd = null;
	private HttpSession sesion = null;

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
	String tablaPeriodos = "";

	public RegistrarSolicitudProcesar(ServletContext sc) {
		super();
		this.sc = sc;
	}

	public void init(ServletConfig config) throws ServletException {
		this.sc = config.getServletContext();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}








	

	

	


	

	
	
	
	

	/**
	 * 
	 * LLenamos los dos cuerpos ultimos de actualizar y consultar
	 * 
	 * @param codEmplado
	 * @param codigoBenef
	 * @param request
	 * @param showResultToView
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public ShowResultToView generarInfoToDesincorporar(String codEmplado,
			String codigoBenef, HttpServletRequest request,
			ShowResultToView showResultToView) throws ServletException,
			IOException {
		try {
			SolicitudDao solicitudDao=new SolicitudDaoImpl();
			
			Trabajador trabajador = new Trabajador();
			Solicitud solicitud = null;
			showResultToView.setMensaje(null);

			/** Buscamos el numero de solicitud */
			solicitud = solicitudDao.BscarSolConCodEmpCodBenf(codEmplado,
					codigoBenef);
			/** Con el numero de solicitud, buscmops la solicityud completa */
			if (solicitud!=null && solicitud.getNroSolicitud()>0){
				solicitud=solicitudDao.consultarSolicitud(solicitud.getNroSolicitud());
				if ((solicitud==null)) {
					showResultToView.setError(Constantes.NO_HAY_DATA);
					showResultToView.setViene(null);
					return showResultToView; 
				}	
			}
			
			
			
			
			String accion = "";
			int cedula = 0;
			int codigoBenefInt = 0;

			accion = request.getParameter("accion");
			log.debug("Valor parámetro accion: " + accion);
			if (!StringUtils.isEmpty(showResultToView.getCedula())
					&& StringUtils.isNumeric(showResultToView.getCedula())) {
				cedula = Integer.parseInt(showResultToView.getCedula());
			}

			log.debug("Valor parámetro cedula: " + cedula);

			if (!StringUtils.isEmpty(showResultToView.getCodigoBenef())
					&& StringUtils.isNumeric(showResultToView.getCodigoBenef())) {
				codigoBenefInt = Integer.parseInt(showResultToView
						.getCodigoBenef());
				log.debug("Valor parámetro codigo beneficiario: "
						+ codigoBenefInt);
			}

			String encabezado = "<tr><td colspan=\"10\" class=\"sin_linea\"><span class=\"nota\">La(s) solicitud(es) que cumple(n) con los criterios de búsqueda seleccionados es (son) la (las) siguiente(s):</span></td></tr><tr><td>&nbsp;</td></tr><tr><td colspan=\"10\" align=\"left\"><input type=\"button\" class=\"boton_color\" name=\"selecTodos\" value=\"Seleccionar todos\" tabindex=\"7\" id=\"7\" onclick=\"seleccionar_todo(document.formulario1,'D',1);\"/>&nbsp;<input type=\"button\" class=\"boton_color\" name=\"eliminarSeleccion\" value=\"Eliminar selecci&oacute;n\" tabindex=\"8\" id=\"8\" onclick=\"seleccionar_todo(document.formulario1,'D',0);\"/>&nbsp;<input type=\"button\" class=\"boton_color\" name=\"desincorporar\" value=\"Desincorporar\" tabindex=\"9\" id=\"9\" onclick=\"verificarEleccion();\" /></td></tr><tr align=\"center\"><td colspan=\"10\"><table class=\"anchoTabla5\" cellspacing=\"5\" cellpadding=\"2\" border=\"1\" ><tr bgcolor=\"#009999\"><th width=\"2\">&nbsp;</th><th width=\"40\">C&Eacute;DULA</th><th width=\"20\">N° SOLICITUD</th><th width=\"100\">NOMBRE EMPLEADO</th><th width=\"20\">SITUACI&Oacute;N</th><th width=\"130\">C&Oacute;DIGO BENEFICIARIO</th><th width=\"100\">NOMBRE BENEFICIARIO</th><th width=\"25\">EDAD</th><th width=\"55\">NIVEL ESCOLAR</th><th width=\"67\">ESTATUS SOLICITUD</th>";
			log.debug("Cargando encabezado de la tabla Aptos para desincorporación");
			String tabla = encabezado;

			List<String> listaReg = new ArrayList<String>();
			if ((cedula >= 0) && (codigoBenefInt >= 0)) {
				trabajador.setCedula(cedula);
				log.debug("Cédula del trabajador: " + trabajador.getCedula());
				log.debug("Compañía del Analista : "
						+ request.getSession().getAttribute("companiaAnalista")
								.toString());
				log.debug("Accion : " + accion);
				TrabajadorDao trabajadorDao = new TrabajadorDaoImpl();
				if (trabajadorDao.consultar(
						request.getSession().getAttribute("companiaAnalista")
								.toString(), accion,cedula)) {
					trabajador=trabajadorDao.buscarTrabajador(cedula);
					log.debug("Valor del código del empleado: "
							+ trabajador.getCodigoEmpleado());
					solicitud.setCodigoEmpleado(trabajador.getCodigoEmpleado());
				}
				solicitud.setCedulaFamiliar(codigoBenefInt);
				//int codigoEmpleado,int cedulaFamiliar
				int nroSolicitud=solicitudDao.obtenerNumeroSolicitud(solicitud.getCodigoEmpleado(),solicitud.getCedulaFamiliar());
				solicitud.setNroSolicitud(nroSolicitud);
				//solicitud.obtenerNumeroSolicitud();
				log.debug("Número de solicitud: "
						+ solicitud.getNroSolicitud());
				listaReg=solicitudDao.buscarSolicitud("cedula",solicitud.getCodigoEmpleado(),solicitud.getCedulaFamiliar(),solicitud.getNroSolicitud());
				if (listaReg != null && listaReg.size() > 0
						&& !listaReg.isEmpty()) {
					log.debug("Lista no vacia:" + listaReg);
					for (int i = 0; i < listaReg.size(); i += 11) {
						log.debug("listaReg.get(" + i + "):"
								+ (String) listaReg.get(i));
						log.debug("listaReg.get(" + i + "):"
								+ (String) listaReg.get(i + 1));
						log.debug("listaReg.get(" + i + "):"
								+ (String) listaReg.get(i + 2));
						log.debug("listaReg.get(" + i + "):"
								+ (String) listaReg.get(i + 3));
						log.debug("listaReg.get(" + i + "):"
								+ (String) listaReg.get(i + 4));

						log.debug("listaReg.get(" + i + "):"
								+ (String) listaReg.get(i + 6));
						log.debug("listaReg.get(" + i + "):"
								+ (String) listaReg.get(i + 7));
						log.debug("listaReg.get(" + i + "):"
								+ (String) listaReg.get(i + 8));
						log.debug("listaReg.get(" + i + "):"
								+ (String) listaReg.get(i + 9));
						log.debug("listaReg.get(" + i + "):"
								+ (String) listaReg.get(i + 10));
						tabla = tabla
								+ "<tr align=\"center\"><td><input name=\"borrar\" type=\"checkbox\"  value=\""
								+ (String) listaReg.get(i + 2)
								+ "\"  /></td><td>"
								+ (String) listaReg.get(i + 10) + "</td><td>"
								+ (String) listaReg.get(i + 2) + "</td><td>"
								+ (String) listaReg.get(i + 6) + "</td><td>"
								+ (String) listaReg.get(i + 7) + "</td>"
								+ "<td>" + (String) listaReg.get(i + 1)
								+ "</td><td>" + (String) listaReg.get(i + 8)
								+ "</td>" + "<td>"
								+ (String) listaReg.get(i + 9) + "</td><td>"
								+ (String) listaReg.get(i + 4) + "</td>"
								+ "<td>" + (String) listaReg.get(i + 3)
								+ "</td></tr>";
					}
					tabla = tabla
							+ "<tr><td colspan=\"10\"><input type=\"hidden\" class=\"boton_color2\" name=\"accion\" value=\"desincorporar\" /></td></tr></table></td></tr><tr><td>&nbsp;</td></tr>";
				 
					showResultToView.setTablaGenerada(tabla);
					 
				} else {
					String mensaje = "El trabajador no tiene solicitud o su última solicitud tiene estatus DESINCORPORADO ";
					showResultToView.setMensaje(mensaje);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return showResultToView;
	}

	

	
	



	public void blanquearCampos(HttpServletRequest request) {
		/** No son pk parabuscar por bd */

	}

	public static Logger getLog() {
		return log;
	}

	public static void setLog(Logger log) {
		RegistrarSolicitudProcesar.log = log;
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