package ve.org.bcv.rhei.controlador;

import java.io.IOException;
import java.io.Serializable;
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

import ve.org.bcv.rhei.bean.BeneficiarioValNom;
import ve.org.bcv.rhei.bean.Proveedor;
import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.negocio.SolAction;
import ve.org.bcv.rhei.negocio.SolConsultarSearchInfoPartI;
import ve.org.bcv.rhei.util.Constantes;

import com.bcv.dao.jdbc.CentroEducacionInicialDao;
import com.bcv.dao.jdbc.FamiliarDao;
import com.bcv.dao.jdbc.MovStSolicBeiDao;
import com.bcv.dao.jdbc.PeriodoEscolarDao;
import com.bcv.dao.jdbc.TrabajadorDao;
import com.bcv.dao.jdbc.impl.CentroEducacionInicialDaoImpl;
import com.bcv.dao.jdbc.impl.FamiliarDaoImpl;
import com.bcv.dao.jdbc.impl.MovStSolicBeiDaoImpl;
import com.bcv.dao.jdbc.impl.PeriodoEscolarDaoImpl;
import com.bcv.dao.jdbc.impl.TrabajadorDaoImpl;
import com.bcv.model.CentroEducacionInicial;
import com.bcv.model.PeriodoEscolar;
import com.bcv.model.Trabajador;

/**
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com 16/04/2015 15:00:19
 * 
 */
public class BenefAndCentrEducControlador extends HttpServlet
		implements
			Serializable {
	private static Logger log = Logger
			.getLogger(BenefAndCentrEducControlador.class.getName());

	private ServletContext sc;
	private String aviso = "FinSesion";
	private RequestDispatcher rd = null;
	private HttpSession sesion = null;
	private TrabajadorDao trabajadorDao = new TrabajadorDaoImpl();
	private FamiliarDao familiarDao = new FamiliarDaoImpl();
	private CentroEducacionInicialDao centroEducacionInicialDao = new CentroEducacionInicialDaoImpl();
	private PeriodoEscolarDao periodoEscolarDao = new PeriodoEscolarDaoImpl();
	private MovStSolicBeiDao movStSolicBeiDao = new MovStSolicBeiDaoImpl();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

		if (request.getSession(false) == null) {
			this.sesion = request.getSession(true);
			this.sesion.setAttribute("aviso", this.aviso);
			this.rd = request.getRequestDispatcher("/jsp/login.jsp");
			this.rd.forward(request, response);
		} else {
			this.sesion = request.getSession(false);
			
			String pagIr = request.getParameter("accion") != null
					? (String) request.getParameter("accion")
					: "";
					
			RequestDispatcher despachador = null;
			ShowResultToView showResultToView = new ShowResultToView();

			String cedula = request.getParameter("cedula") != null
					? (String) request.getParameter("cedula")
					: "";

			/** Buscamos codigo del empleado */
			String codEmp = "";
			try {
				if (!StringUtils.isEmpty(cedula)
						&& StringUtils.isNumeric(cedula)) {
					codEmp = trabajadorDao.codigoEmpleadoBycedula(new Integer(
							cedula));
				} else {
					codEmp = request.getParameter("codEmp") != null
							? (String) request.getParameter("codEmp")
							: "";
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/** Fin Buscamos la cedula y codigo del empleado */

			/** Buscamos codigo beneficiario del empleado */
			String codigoBenef = request.getParameter("codigoBenef") != null
					? (String) request.getParameter("codigoBenef")
					: "";
			/** Buscamos compania */
			String companiaAnalista = "01";
			if (sesion != null) {
				companiaAnalista = sesion.getAttribute("companiaAnalista") != null
						? (String) sesion.getAttribute("companiaAnalista")
						: "01";
			if (StringUtils.isEmpty(companiaAnalista)){
				 companiaAnalista = "01";
			}
			}
			/** Buscamos Rif centro de eduicacion inicial */
			String nroRifCentroEdu = request.getParameter("nroRifCentroEdu") != null
					? (String) request.getParameter("nroRifCentroEdu")
					: "";

			// SolAction solAction= new
			// SolConsultarSearchInfoPartI(request,response,showResultToView,sc);
			/**
			 * Buscamos Los Beneficiarios del empleado(Ninios) y Los centros de
			 * educacion inicial
			 */
			if ("buscarBenefAndCentrEduc".equals(request
					.getParameter("principal.do"))) {

				/** inicio hacemos la busqueda solicitud */
				/********/
				/********/
				/** fin hacemos la busqueda solicitud */

				/** Buscamos los beneficiarios y proveedores educativos */
				String mensaje = "";

				Trabajador trabajador = new Trabajador();
				trabajador.setCedula(0);
				List<Proveedor> proveedors = new ArrayList<Proveedor>();
				List<BeneficiarioValNom> beneficiarios = new ArrayList<BeneficiarioValNom>();

				if (!StringUtils.isEmpty(cedula)) {
					if (StringUtils.isNumeric(cedula)) {
						trabajador.setCedula(Integer.parseInt(cedula));
					}
				}

				/**
				 * obtenemos el codEmp dentro del objeto trabajadores si existe
				 * el empleado
				 */
				// TrabajadorDao trabajadorDao = new TrabajadorDaoImpl();
				try {
					if (trabajadorDao.consultar(companiaAnalista, "consultar",
							trabajador.getCedula())) {
						boolean rangoEdad = false;
						/**
						 * INICIO Buscamos los NINOS beneficiarios en table
						 * PERSONAL.FAMILIARES
						 */
						beneficiarios = familiarDao.getBeneficiarioValNom(
								trabajador.getCedula(), rangoEdad);
						if (beneficiarios == null || beneficiarios.isEmpty()
								|| beneficiarios.size() == 0) {
							mensaje ="beneficiario_no_hay";// "El trabajador no tiene familiares aptos, su estatus es Egresado o la solicitud fue desincorporada";
							// request.setAttribute("mensaje", this.mensaje);
						}else{
							List<BeneficiarioValNom> beneficiariosFiltroIncluir = new ArrayList<BeneficiarioValNom>();
							List<BeneficiarioValNom> beneficiariosFiltroConsultar = new ArrayList<BeneficiarioValNom>();
							for (BeneficiarioValNom benef:beneficiarios){
								boolean existe=familiarDao.existeBeneficiario(Constantes.ACTIVO_EMPLEADO, benef.getValor());
								if ( !existe){
									beneficiariosFiltroIncluir.add(benef);
								}else{
									beneficiariosFiltroConsultar.add(benef);
								}
							}
							beneficiarios =  new ArrayList<BeneficiarioValNom>();
							if ("solicitudIncluir.jsp".equalsIgnoreCase(pagIr)){
								if (beneficiariosFiltroIncluir!=null && beneficiariosFiltroIncluir.size()>0 && !beneficiariosFiltroIncluir.isEmpty()){
									beneficiarios.addAll(beneficiariosFiltroIncluir);
								}else{
									mensaje ="beneficiario_no_hay";
								}

							}else{
								if (beneficiariosFiltroConsultar!=null && beneficiariosFiltroConsultar.size()>0 && !beneficiariosFiltroConsultar.isEmpty()){
									beneficiarios.addAll(beneficiariosFiltroConsultar);
								}else{
									mensaje ="beneficiario_no_hay";
								}
								
							}
						}
						
						
						/** Los beneficiarios, lo volvemo a filtar si es que vamos a realizar un pago, **/
						/****Si es pago, los beneficiarios debe filtrar los que por lo menos que tengan rif del colegio, el pago de matricula y periodo
						 * que no sean igual a cero**************/	
						     
						if ("pagoActualizar.jsp".equalsIgnoreCase(pagIr)||"pagoConvConsultar.jsp".equalsIgnoreCase(pagIr) || "pagoConvRegistrar.jsp".equalsIgnoreCase(pagIr)){
							List<BeneficiarioValNom> benefPuedenPagar= new ArrayList<BeneficiarioValNom>();
							List<BeneficiarioValNom> benef=beneficiarios;
							if (benef!=null && !benef.isEmpty()){
								for (BeneficiarioValNom obj:benef){
									if (!StringUtils.isEmpty(obj.getValor()) && StringUtils.isNumeric(obj.getValor())){
								          if (movStSolicBeiDao.isSolicitudActivaParaRealizarPago(obj.getValor())){
								        	  benefPuedenPagar.add(obj);
								          }							
									}
								}
								beneficiarios=new ArrayList<BeneficiarioValNom>();
								beneficiarios.addAll(benefPuedenPagar);
								 
							}
							
							if(beneficiarios==null || beneficiarios.isEmpty() || beneficiarios.size()==0){
								showResultToView.setMensaje("beneficiario_no_hay");
								
							}

						}
						
						/**
						 * FIN Buscamos los beneficiarios en table
						 * PERSONAL.FAMILIARES
						 */
						/**
						 * Buscamos los proveedores educativos en la vista
						 * RHEI.PROVEEDOR_CEI
						 */
						CentroEducacionInicial centroEduc = new CentroEducacionInicial();
						proveedors = centroEducacionInicialDao
								.listaProvEducativos(companiaAnalista);
						if (proveedors == null || proveedors.isEmpty()
								|| proveedors.size() == 0) {
							mensaje = "fracasoeduc";
							// request.setAttribute("mensaje", this.mensaje);
						}
					} else {
						log.debug("No entro en el método consultar de la clase trabajador");
						String situacionEmpleado=trabajadorDao.SituacionEmpleadosGeneral(trabajador.getCedula());
						if (situacionEmpleado==null){
							mensaje = "solicitud.ced.fail";	
						}else{
							if (Constantes.ACTIVO_EMPLEADO.equalsIgnoreCase(situacionEmpleado)){
								mensaje="solicitud.ced.fail.activo_empleado";
							}else if (Constantes.VACACION_EMPLEADO.equalsIgnoreCase(situacionEmpleado)){
								mensaje="solicitud.ced.fail.vacacion_empleado";
							}else if (Constantes.EGRESADO_EMPLEADO.equalsIgnoreCase(situacionEmpleado)){
								mensaje="solicitud.ced.fail.egresado_empleado";
							}else if (Constantes.SUSPENDIDO_EMPLEADO.equalsIgnoreCase(situacionEmpleado)){
								mensaje="solicitud.ced.fail.suspendido_empleado";
							}else if (Constantes.EMPLEADO_SIN_SOLICITUD.equalsIgnoreCase(situacionEmpleado)){
									mensaje="solicitud.ced.fail.empleado_sin_solicitud";
							}else{
								mensaje = "solicitud.ced.fail";
							}
						}
						
						
					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				/** Buscamos los periodos escolares */
				showResultToView.setPeriodoEscolares(periodoEscolarDao
						.tipoPeriodosEscolares());
				if (StringUtils.isEmpty(showResultToView.getPeriodoEscolar())) {
					if (showResultToView.getPeriodoEscolares() != null
							&& !showResultToView.getPeriodoEscolares()
									.isEmpty()
							&& showResultToView.getPeriodoEscolares().size() > 0) {
						showResultToView.setPeriodoEscolar(showResultToView
								.getPeriodoEscolares().get(0).getValor());
					}
				}
				/** Buscamos Fin los periodos escolares */
				/** Buscamos los meses */
				SolAction solAction = new SolConsultarSearchInfoPartI();
				showResultToView.setMeses(solAction.meses());
				/** Fin Buscamos los meses */
				if (null!=mensaje && !"".equalsIgnoreCase(mensaje.trim())){
					showResultToView.setMensaje(mensaje);		
				}
			
				showResultToView.setCedula(String.valueOf(trabajador
						.getCedula()));
				showResultToView.setCodEmp(String.valueOf(trabajador
						.getCodigoEmpleado()));
				showResultToView.setBeneficiarios(beneficiarios);
				showResultToView.setListadoProv(proveedors);

				/** Tomamos el colegio seleccionado anteriormente */
				showResultToView.setNroRifCentroEdu(nroRifCentroEdu);
				request.setAttribute("showResultToView", showResultToView);
				
				
				despachador = request.getRequestDispatcher("/jsp/" + pagIr);
				despachador.forward(request, response);
				/** Vamos a la pag Incluir solicitud */
			}

		}

	}

}
