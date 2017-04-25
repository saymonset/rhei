package ve.org.bcv.rhei.negocio;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.bcv.dao.jdbc.ManejadorDB;
import com.bcv.dao.jdbc.TrabajadorDao;
import com.bcv.dao.jdbc.impl.TrabajadorDaoImpl;
import com.bcv.model.Solicitud;
import com.bcv.model.Trabajador;

import ve.org.bcv.rhei.util.Compania;
import ve.org.bcv.rhei.util.Constantes;
import ve.org.bcv.rhei.util.Utilidades;

/**
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com
 * 02/02/2015 14:18:01
 * PROCESAMOS MODIFI POR LOTE
 * 
 */
public class ModifLoteSolProcesar extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ModifLoteSolProcesar.class
			.getName());
	private ServletContext sc;
 
	private RequestDispatcher rd = null;
	private HttpSession sesion = null;
	private ResultSet rs = null;
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

	public ModifLoteSolProcesar(ServletContext sc) {
		super();
		this.sc = sc;
	}
	
	/**
	 * Cantidad de combinaciones compañia-tipo trabajador 
	 * @return
	 */
	private int cantCombCiaTipoTrab(){
		int cantCombCiaTipoTrab = 0;
		Compania bcv = Compania.BCV;
		
		TrabajadorDao trabajadorDao = new TrabajadorDaoImpl();
		
		   try {
			cantCombCiaTipoTrab = (trabajadorDao.cargarCompania(bcv.getCodCompania()+"").size()/2)
			    		*(trabajadorDao.cargarTipoTrabajador().size()/2) ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cantCombCiaTipoTrab;
	}
	
	/**
	 * Verificamos el cambio de salario minimo.
	 * @return
	 */
	public void verificarCambioSalarioMinimo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	    String fecha = (String)format.format(now);  
		Solicitud solicitud = new Solicitud();
		int combCiaTipoTrab =cantCombCiaTipoTrab();
		String resp=null;//solicitud.verificarCambioSalarioMinimo("RHEI", combCiaTipoTrab);
		request.setAttribute("combCiaTipoTrab", resp);
		
		if ("Exito".equalsIgnoreCase(resp)){
   			    ArrayList<String> listaSolicitud = new ArrayList<String>();
		        listaSolicitud =null;// solicitud.buscarSolicitud("D","RHEI","MTOBCV");
		        //Colocar aqui el código para verificar duplicidas de datos de trabajadores
		        String periodoEscolar = listaSolicitud!=null?listaSolicitud.size()>9?listaSolicitud.get(9):"":"";
		        String listaFiltrada =null;// solicitud.filtrarSolicitud(listaSolicitud);
		        String tabla = null;//solicitud.generadorTablaSolicitudes(listaSolicitud);
		    	request.setAttribute("listaSolicitud", listaSolicitud);
		    	request.setAttribute("periodoEscolar", periodoEscolar);
		    	request.setAttribute("listaFiltrada", listaFiltrada);
		    	request.setAttribute("tabla", tabla);
		    	request.setAttribute("fecha",   fecha.substring(0,10));
		}
	}

	public void init(ServletConfig config) throws ServletException {
		this.sc = config.getServletContext();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void save(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
 

	}

	protected void findParametro(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	 

	 
	}

	public void blanquearCampos(HttpServletRequest request) {
		   
	}

	/**
	 * LLenamos tipo de dato, compania y empleados
	 */
	public void llenarTdatoCompaniaEmp(HttpServletRequest request) {
		 
	}

	public void agregarParamByBenef(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	public void actualizarLote(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String[]  listaSolicitud = request.getParameter("listaSolicitud").split("&");
//		for (int i = 0; i < listaSolicitud.length - 1; i += 2) {
//			log.debug("Solicitud Nro. " + listaSolicitud[i]
//					+ " Valor aporte BCV " + listaSolicitud[(i + 1)]);
//		}
		Solicitud solicitud = new Solicitud();
		TrabajadorDao trabajadorDao = new TrabajadorDaoImpl();
	int cantidadRegistros=0;
	try {
		
		String companiaAnalista = request.getSession().getAttribute(
				"companiaAnalista") != null ? (String) request
				.getSession().getAttribute("companiaAnalista") : "01";
		
		cantidadRegistros = trabajadorDao.cargarCompania(companiaAnalista).size()
					/ 2 * (trabajadorDao.cargarTipoTrabajador().size() / 2);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
		String periodoEscolar = request.getParameter("periodoEscolar");
		String fecha = request.getParameter("fecha");
		
		this.mensaje = null;
//		solicitud.guardarCambiosEnSolicitud(
//				listaSolicitud, periodoEscolar, fecha,
//				cantidadRegistros);
		if (this.mensaje.equals("Fracaso")) {
			this.mensaje = "No se pudo realizar la actualizaciÃ³n masiva de las solicitudes";
		} else {
			this.mensaje = "ActualizaciÃ³n masiva de solicitudes procesadas exitosamente";
			request.setAttribute("mensajeExito", "exito");
			String enlace = "<tr><td colspan=\"1\"><a href=\"/rhei/jsp/relacionPagosACentros.jsp?periodoEscolar=si&reporte=reporteMei4.jasper\" title=\"Reporte ActualizaciÃ³n Masiva de Solicitudes\"+ accesskey=\"R\">Ver reporte</a></td></tr>";

			request.setAttribute("enlace", enlace);
		}
		request.setAttribute("mensaje", this.mensaje);
	
		
		
	}

	public void primeravez(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		 
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