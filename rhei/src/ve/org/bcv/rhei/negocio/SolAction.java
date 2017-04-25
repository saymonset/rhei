package ve.org.bcv.rhei.negocio;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.bcv.comparator.SortByValueComparatorAsc;
import com.bcv.dao.jdbc.BeneficiarioDao;
import com.bcv.dao.jdbc.CentroEducacionInicialDao;
import com.bcv.dao.jdbc.FamiliarDao;
import com.bcv.dao.jdbc.PeriodoEscolarDao;
import com.bcv.dao.jdbc.RelacionDePagosDao;
import com.bcv.dao.jdbc.SolicitudDao;
import com.bcv.dao.jdbc.TrabajadorDao;
import com.bcv.dao.jdbc.impl.BeneficiarioDaoImpl;
import com.bcv.dao.jdbc.impl.CentroEducacionInicialDaoImpl;
import com.bcv.dao.jdbc.impl.FamiliarDaoImpl;
import com.bcv.dao.jdbc.impl.PeriodoEscolarDaoImpl;
import com.bcv.dao.jdbc.impl.RelacionDePagosDaoImpl;
import com.bcv.dao.jdbc.impl.SolicitudDaoImpl;
import com.bcv.dao.jdbc.impl.TrabajadorDaoImpl;
import com.bcv.model.Beneficiario;
import com.bcv.model.CentroEducacionInicial;
import com.bcv.model.PeriodoEscolar;
import com.bcv.model.RelacionDePagos;
import com.bcv.model.Solicitud;
import com.bcv.model.Trabajador;
import com.bcv.reporte.relacionpago.FacturaRpago3Bean;
import com.bcv.reporte.relacionpago.FamiliarRpago2Bean;
import com.bcv.reporte.relacionpago.ProveedorRpago1Bean;
import com.enums.Mes;

import ve.org.bcv.rhei.bean.BeneficiarioValNom;
import ve.org.bcv.rhei.bean.FormaPagoValNom;
import ve.org.bcv.rhei.bean.Proveedor;
import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.bean.ValorNombre;
import ve.org.bcv.rhei.report.by.benef.BeneficiarioBean2;
import ve.org.bcv.rhei.report.by.benef.EmpleadosBean1;
import ve.org.bcv.rhei.report.by.benef.FacturaBean3;
import ve.org.bcv.rhei.util.Constantes;

/**
 * Implementamos el patron comando para realizar solicitudes
 * 
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com 19/02/2015 14:59:56
 * 
 */
public abstract class SolAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private ShowResultToView showResultToView;
	private ServletContext sc;
	private String companiaAnalista = "";
	private RelacionDePagosDao relacionDePagosDao= new RelacionDePagosDaoImpl();
	private FamiliarDao familiarDao = new FamiliarDaoImpl();
	private BeneficiarioDao beneficiarioDao= new BeneficiarioDaoImpl();
	private CentroEducacionInicialDao centroEducacionInicialDao = new CentroEducacionInicialDaoImpl();
	private PeriodoEscolarDao periodoEscolarDao = new PeriodoEscolarDaoImpl();
	private SolicitudDao solicitudDao= new SolicitudDaoImpl();
	



	private RegistrarSolicitudProcesar procesar;

	public abstract ShowResultToView ejecutar() throws ServletException,
			IOException;

	private static Logger log = Logger.getLogger(SolAction.class.getName());
	 
	/**
	 * Carga Inicial de las variables..
	 * 
	 * @param request
	 * @param response
	 * @param showResultToView
	 * @param sc
	 * @param accion
	 * @throws ServletException
	 * @throws IOException
	 */
	public SolAction(HttpServletRequest request, HttpServletResponse response,
			ShowResultToView showResultToView, ServletContext sc, String accion)
			throws ServletException, IOException {
		super();
		this.request = request;
		this.response = response;
		this.sc = sc;
		this.showResultToView = showResultToView;
		procesar = new RegistrarSolicitudProcesar(sc);

		/**
		 * Buscamos codigo del empleado, Codigo del beneficiario que es la
		 * cedula familiar , un codigo generado por bd, Nrif Centro de educacion
		 * inicial y numSolicitud
		 ***/
		//showResultToView = searchCodigosPrincipales(showResultToView, request);
	 
		String cedula = "";
		String codEmp = "";
		String codigoBenef = "";
		String nroRifCentroEdu = "";
		String numSolicitud = "";

		try {
			try {
				numSolicitud = request.getParameter("numSolicitud") != null ? (String) request
						.getParameter("numSolicitud") : "";
						
						
						codigoBenef = request.getParameter("codigoBenef")!=null?(String)request.getParameter("codigoBenef"):"";
						cedula= request.getParameter("cedula")!=null?(String)request.getParameter("cedula"):"";
				 
						 try {
							 if (!StringUtils.isEmpty(cedula) && !StringUtils.isEmpty(codigoBenef)){
								 RelacionDePagos relacionDePagos=relacionDePagosDao.numSolicitudPagoDelBeneficiario(Constantes.ACTIVO_EMPLEADO, cedula,codigoBenef);
								 if (relacionDePagos!=null){
									 showResultToView.setPeriodoEscolar(relacionDePagos.getTxDescriPeriodo());
									 numSolicitud=relacionDePagos.getNroSolicitud()+"";
								 }
							 }
							
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}		
						
			    if (StringUtils.isEmpty(numSolicitud)|| "0".equalsIgnoreCase(numSolicitud)){
			    	/**Esta variable la usamos nombreAjax en  solicIngrBlke1WithNroFactura.jsp, hay colocamos el nroSolicitud*/
			    	numSolicitud = request.getParameter("nombreAjax") != null ? (String) request
							.getParameter("nombreAjax") : "";
			    }			
				showResultToView.setNumSolicitud(numSolicitud);
				
				

				/*** Obtenemos factura del request */
				showResultToView
						.setNroFactura(request.getParameter("factura") != null ? (String) request
								.getParameter("factura") : "");

				if (!StringUtils
						.isEmpty(request.getParameter("periodoEscolar"))) {
					showResultToView.setPeriodoEscolar(request
							.getParameter("periodoEscolar"));
				}

				HttpSession sesion = null;
				sesion = request.getSession(false);

				if (StringUtils.isEmpty(numSolicitud) || numSolicitud == null) {
					cedula = request.getParameter("cedula") != null ? (String) request
							.getParameter("cedula") : "";

					if (!StringUtils.isEmpty(cedula)
							&& StringUtils.isNumeric(cedula)) {
						TrabajadorDao trabajadorDao = new TrabajadorDaoImpl();
						codEmp = trabajadorDao.codigoEmpleadoBycedula(new Integer(cedula));
					} else {
						codEmp = request.getParameter("codEmp") != null ? (String) request
								.getParameter("codEmp") : "";

					}

					codigoBenef = request.getParameter("codigoBenef") != null ? (String) request
							.getParameter("codigoBenef") : "";
							
							
							Solicitud solicitud=null;
							 try {
								 solicitud=solicitudDao.BscarSolConCodEmpCodBenf(codEmp,
										 codigoBenef);
								 if (solicitud!=null){
									 numSolicitud= solicitud.getNroSolicitud()+"";
									 nroRifCentroEdu=solicitud.getNroRifCentroEdu(); 
								 }
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}		
					
							 if (StringUtils.isEmpty(nroRifCentroEdu)){
									nroRifCentroEdu = request.getParameter("nroRifCentroEdu") != null ? (String) request
											.getParameter("nroRifCentroEdu") : nroRifCentroEdu;				 
							 }
//
							 if (StringUtils.isEmpty(showResultToView.getPeriodoEscolar())) {
								 PeriodoEscolar periodoEscolar=periodoEscolarDao.findByMvStatusSolicitudActiva(numSolicitud);
								 showResultToView.setPeriodoEscolar(periodoEscolar.getDescripcion());
								}
				

					showResultToView.setCedula(cedula);
					showResultToView.setCodEmp(codEmp);
					showResultToView.setCodigoBenef(codigoBenef);
					showResultToView.setCedBenef(codigoBenef);
					showResultToView.setNroRifCentroEdu(nroRifCentroEdu);
				} else {
					
					ShowResultToView showResultToViewBd=solicitudDao.searchEmpleado(numSolicitud);
					if (showResultToViewBd!=null){
						showResultToView.setCedula(showResultToViewBd.getCedula());
						showResultToView.setCodEmp(showResultToViewBd.getCodEmp());
						showResultToView.setCodigoBenef(showResultToViewBd.getCodigoBenef());
						showResultToView.setNroRifCentroEdu(showResultToViewBd.getNroRifCentroEdu());
						showResultToView.setNroSolicitud(showResultToViewBd.getNroSolicitud());
					}
					
					showResultToViewBd=null;
					
				}
				String companiaAnalista = sesion
						.getAttribute("companiaAnalista") != null ? (String) sesion
						.getAttribute("companiaAnalista") : "";
				showResultToView.setCompania(companiaAnalista);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			 
		}
		this.showResultToView = showResultToView;

	}

	public SolAction() {
	}
	
	
	/**
	 * @param showResultView
	 * @return
	 */
	public ShowResultToView obtenerDatosDelTrabajador(ShowResultToView showResultView){
		try {
			/**Datos del trabajador*/
					TrabajadorDao trabajadorDao = new TrabajadorDaoImpl();
				Trabajador trab = new Trabajador();
				String cedula =showResultToView.getCedula();
				ArrayList<String> listaFiltros = new ArrayList();
				ArrayList<String> duplaParametroTipo = new ArrayList();
				int cedulaInt=0;
				cedulaInt=0;
				if (!StringUtils.isEmpty(cedula)
						&& StringUtils.isNumeric(cedula)) {
					cedulaInt=Integer.parseInt(cedula);
				}
				/** obtenemos el tipo de nomina */
				
			
					trab.setTipoNomina(trabajadorDao.obtenerTipoNomina(cedulaInt));
					/**
					 * Codigo del empleado lo colocamos en el objeto trabajador para
					 * obtener sus datos.
					 */
					if ((showResultToView.getCodEmp() != null)
							&& (!"".equals(showResultToView.getCodEmp().toString()) && StringUtils
									.isNumeric(showResultToView.getCodEmp()
											.toString()))) {
						trab.setCodigoEmpleado(Integer.parseInt(showResultToView
								.getCodEmp()));
						if ((showResultToView.getNumSolicitud() != null)
								&& (!showResultToView.getNumSolicitud().toString()
										.equals(""))) {
							listaFiltros = (ArrayList<String>) trabajadorDao.buscarFiltros(trab.getCodigoEmpleado(),
									trab.getTipoNomina());
						} else {
							listaFiltros = (ArrayList<String>) trabajadorDao.buscarFiltros(cedulaInt);
						}
						if (listaFiltros != null && !listaFiltros.isEmpty()
								&& listaFiltros.size() > 0) {
							duplaParametroTipo = (ArrayList<String>) trabajadorDao.buscarParametro(
									(String) listaFiltros.get(0),
									(String) listaFiltros.get(1),
									Constantes.TIPOBENEFICIO,
									Constantes.NOMBREPARAMETRO);

						}
						if (duplaParametroTipo != null
								&& duplaParametroTipo.size() == 2) {
					 
							try {
								double MTOBCV = new Double(duplaParametroTipo.get(0));
								showResultToView.setMontoBCV(MTOBCV);
							} catch (Exception e) {
								double MTOBCV = Double.valueOf(Double
										.parseDouble(duplaParametroTipo.get(0)
												.toString().replace(".", "")
												.replace(",", ".")));
								showResultToView.setMontoBCV(MTOBCV);
							 
								
							}
							
						}

						/** Buscamos el trabajador */
						trab=trabajadorDao.buscarTrabajador(cedulaInt);
						showResultToView.setCedula(trab.getCedula() + "");
						/** Guardamos el trabajador en varible a la view */
						showResultToView = trab
								.cargarAtributosTrabajador(showResultToView);
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		return showResultView;
	 
	}
	
	
	/**
	 * Buscamos el centro educativo
	 *  * El nro de rif del centro educativo y la localidad del centro
		 * educativo los desglosamos y llnamos el objeto centroEducativo con el
		 * nro de rif
	 * 
	 * @param showResultToView
	 * @return
	 * @throws SQLException
	 */
	public ShowResultToView searchCentroEducativo(
			ShowResultToView showResultToView) throws SQLException {
		ResourceBundle rb = ResourceBundle.getBundle("ve.org.bcv.rhei.util.bundle");
		int posicion = 0;
		CentroEducacionInicial centroEducativo = new CentroEducacionInicial();
		String localidad = "";
		/**
		 * El nro de rif del centro educativo y la localidad del centro
		 * educativo los desglosamos y llnamos el objeto centroEducativo con el
		 * nro de rif
		 */
		if (!"0".equalsIgnoreCase(showResultToView.getNroRifCentroEdu())&&!StringUtils.isEmpty(showResultToView.getNroRifCentroEdu())) {
			//posicion = showResultToView.getNroRifCentroEdu().indexOf("-");
			//if (true) {
				/**
				 * llnamos el objeto centroEducativo con el nro de rif*
				 * */
				centroEducativo.setNroRif(showResultToView.getNroRifCentroEdu());
				localidad = "";
				centroEducativo=centroEducacionInicialDao.buscarCentroByLocalidad(localidad,centroEducativo.getNroRif());
			    showResultToView.setNroRif(centroEducativo.getNroRif());
			    showResultToView.setNombreCentro(centroEducativo.getNombreCentro());
			    showResultToView.setLocalidadBCV(centroEducativo.getLocalidad());
			    showResultToView.setTlfCentro(centroEducativo.getTelefono());
			    showResultToView.setEmail(centroEducativo.getCorreo());
			}else{
				showResultToView.setNroRifEmpty(true);
				showResultToView.setNroRif(rb.getString("inst.rif.por.ingresar"));
			    showResultToView.setNombreCentro("");
			    showResultToView.setLocalidadBCV(rb.getString("inst.rif.por.ingresar"));
			    showResultToView.setTlfCentro("");
			    showResultToView.setEmail(rb.getString("inst.rif.por.ingresar"));
			}

		return showResultToView;

	}
	
	/**
	 * Nombre y valor del proveedor para el select de html
	 * @param showResultToView
	 * @return
	 */
	public ShowResultToView obtenerProveedores(ShowResultToView showResultToView) {
		/** Solo los proveedores qiue son colegios */
		List<Proveedor> proveedors = new ArrayList<Proveedor>();
		@SuppressWarnings("unused")
		String error = "";
		String mensaje = "";
		try {
			proveedors = centroEducacionInicialDao.listaProvEducativos(companiaAnalista);
			if (proveedors == null || proveedors.isEmpty()
					|| proveedors.size() == 0) {
				error = "2";
				mensaje = "fracaso4";

				showResultToView.setMensaje(mensaje);
			}
			showResultToView.setListadoProv(proveedors);
		} catch (ServletException e) {
			mensaje = "fracaso4";

			e.printStackTrace();
		} catch (IOException e) {
			mensaje = "fracaso4";

			e.printStackTrace();
		}
		
		return showResultToView;
	}

	/**
	 * Cargando las variables iniciales de la aplicacion
	 * 
	 * @param request
	 * @param response
	 * @param showResultToView
	 * @param sc
	 * @param accion
	 * @throws ServletException
	 * @throws IOException
	 */
	public void cargaTrabajadorBeneficiarioCentroEdu(HttpServletRequest request,
			HttpServletResponse response, ShowResultToView showResultToView,
			ServletContext sc, String accion) throws ServletException,
			IOException {
	
		
		
		
		

		try {
			/**Carga data del trabajador*/
			showResultToView=obtenerDatosDelTrabajador(showResultToView);
			/**Carga data de los proveedores*/
			showResultToView= obtenerProveedores(showResultToView) ;
			/** Buscamos el centro educativo */
			showResultToView = searchCentroEducativo(showResultToView);
			/** Buscamos el beneficiario */
			showResultToView = buscarBeneficiarios(showResultToView,
					request, accion);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		

			
			
	
		
		
		
		
		/**
		 * llenamos las listas de los select en los html
		 **/

		/**
		 * Begin Puras funciones llenan las variables de las vista con each
		 */
		showResultToView.setFormaPagoValNoms(listaDepagos());

		showResultToView.setTipoInstituciones(tipoInstituciones());

		showResultToView.setTipoEducacions(tipoEducacions());

		showResultToView.setNivelEscolaridades(nivelEscolaridades());
		Solicitud solicitud = new Solicitud();
		solicitud.setNroSolicitud(showResultToView.getNroSolicitud());

		try {
			solicitud=solicitudDao.consultarSolicitud(solicitud.getNroSolicitud());
 
			if (solicitud==null &&  !Constantes.INCLUIR.equalsIgnoreCase(accion ) && !Constantes.DESINCORPORAR.equalsIgnoreCase(accion)) {
				showResultToView.setError(Constantes.NO_HAY_DATA);
				showResultToView.setViene(null);
			}else{
				    if (solicitud==null){
				    	solicitud= new Solicitud();
				    }
					showResultToView = cargarRestoInfo(solicitud, showResultToView);	
				 
			}
			
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 
		showResultToView.setPeriodoPagos(periodoPagos());

		 
	}

	 

	public String obtenerNameMes(int indMes) {
		String name = "";
		/** Buscamos el nombre del mes a traves del enum */
		for (Mes mes : Mes.values()) {
			if (mes.getValue() == indMes) {
				name = mes.name();
				
				if (name.length() > 0 && name.charAt(name.length()-1)=='P') {
				      name = name.substring(0, name.length()-1);
				    }
				
				break;
			}
		}
		return name;
	}

	public int obtenerIdMes(String name) {
		int id = -1;
		/** Buscamos el nombre del mes a traves del enum */
		for (Mes mes : Mes.values()) {
			if (mes.name().equalsIgnoreCase(name)) {
				id = mes.getValue(); // name=mes.name();
				break;
			}
		}
		return id;
	}

	/**
	 * Buscamos la data del trabajador y del centro de educacion inicial
	 * 
	 * @param accion
	 * @param cedula
	 * @param codEmp
	 * @param codigoBenef
	 * @param nroRifCentroEdu
	 * @param numSolicitud
	 * @param request
	 * @throws ServletException
	 * @throws IOException
	 */
	protected ShowResultToView buscarBeneficiarios(
			ShowResultToView showResultToView, HttpServletRequest request,
			String accion) throws ServletException, IOException {
		String mensaje = "";
	 
		mensaje = "";
		String cedula =showResultToView.getCedula();
		int cedulaInt=0;
		try {

			Beneficiario benef = new Beneficiario();
		 
			

		 
			String error = "";
			
			List<BeneficiarioValNom> beneficiarios = new ArrayList<BeneficiarioValNom>();

			cedulaInt=0;
			if (!StringUtils.isEmpty(cedula)
					&& StringUtils.isNumeric(cedula)) {
				cedulaInt=Integer.parseInt(cedula);
			}
	
			try {
			
				/**
				 * Codigo beneficiario lo colocamos en el objeto beneficiario
				 * para obtener sus datos.
				 */
				int codBenef=-1;
				if ((showResultToView.getCodigoBenef() != null)
						&& (!"".equalsIgnoreCase(showResultToView
								.getCodigoBenef()))
						&& StringUtils.isNumeric(showResultToView
								.getCodigoBenef())) {
				codBenef=Integer.parseInt(showResultToView
						.getCodigoBenef());
				}

			
				benef=beneficiarioDao.buscarBeneficiario(codBenef);

				/** Rango de edad Menores a 7 anios */
				boolean rangoEdad = false;
				beneficiarios = familiarDao.getBeneficiarioValNom(cedulaInt,
						rangoEdad);
				if (beneficiarios == null || beneficiarios.isEmpty()
						|| beneficiarios.size() == 0) {
					error = "1";
					mensaje = "fracaso3";

					showResultToView.setMensaje(mensaje);
				}else{
					
					List<BeneficiarioValNom> beneficiariosFiltroIncluir = new ArrayList<BeneficiarioValNom>();
					List<BeneficiarioValNom> beneficiariosFiltroConsultar = new ArrayList<BeneficiarioValNom>();
					for (BeneficiarioValNom obj:beneficiarios){
						boolean existe=familiarDao.existeBeneficiario(Constantes.ACTIVO_EMPLEADO, obj.getValor());
						if ( !existe){
							beneficiariosFiltroIncluir.add(obj);
						}else{
							beneficiariosFiltroConsultar.add(obj);
						}
					}
					beneficiarios =  new ArrayList<BeneficiarioValNom>();
					if (Constantes.INCLUIR.equalsIgnoreCase(accion)){
						beneficiarios.addAll(beneficiariosFiltroIncluir);
					}else{
						beneficiarios.addAll(beneficiariosFiltroConsultar);
					}
				}

				showResultToView.setBeneficiarios(beneficiarios);

			
				
				
				showResultToView.setError(error);

				
			
				

				/**
				 * Cargamos los request de trabajador, beneficirio, centyro
				 * educativo
				 */
			if (benef!=null){
				
				SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
				showResultToView.setCedulaFamiliar(String.valueOf(benef.getCodigo()));
				showResultToView.setApellidoFamiliar(benef.getApellido());
				showResultToView.setNombreFamiliar(benef.getNombre());
				showResultToView.setFechaNacimiento(format.format(benef.getFechaNacimento())
						.toString());
				showResultToView.setEdad(benef.getEdad()+ "");
				showResultToView.setEstatus(benef.getStatus());
			}
				
 

			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			 
		}
		log.info(showResultToView.getViene());
		return showResultToView;
	}

	/**
	 * 
	 * Buscamos si en la lista empleados hay beneficiarios
	 * 
	 * @param objs
	 * @return
	 */
	public boolean existBenefFact(List<EmpleadosBean1> objs) {
		boolean isBenefFact = false;
		for (EmpleadosBean1 emp : objs) {
			List<BeneficiarioBean2> benfs = emp.getBeneficiarios();
			if (benfs != null && benfs.size() > 0 && !benfs.isEmpty()) {
				for (BeneficiarioBean2 benf : benfs) {
					List<FacturaBean3> fact = benf.getFacturas();
					if (fact != null && fact.size() > 0 && !fact.isEmpty()) {
						isBenefFact = true;
						break;
					}
				}

			}
		}

		return isBenefFact;

	}
	
	/**
	 * Chqueamos que exista almenos un reporte de pagos
	 * @param objs
	 * @return
	 */
	public boolean existRpago(List<ProveedorRpago1Bean> objs) {
		boolean isBenefFact = false;
		for (ProveedorRpago1Bean emp : objs) {
			List<FamiliarRpago2Bean> benfs = emp.getFamiliares();
			if (benfs != null && benfs.size() > 0 && !benfs.isEmpty()) {
				for (FamiliarRpago2Bean benf : benfs) {
					List<FacturaRpago3Bean> fact = benf.getFacturas();
					if (fact != null && fact.size() > 0 && !fact.isEmpty()) {
						isBenefFact = true;
						break;
					}
				}

			}
		}

		return isBenefFact;

	}

	
	
	/**
	 * Cargadmos la solicitud si existe
	 * 
	 * @param showResultToView
	 * @param accion
	 * @return
	 * @throws SQLException
	 */
	public ShowResultToView buscarNroSolicitud(
			ShowResultToView showResultToView, String accion)
			throws SQLException {
		/** Consultamos solicitud inicio */
		/******************************************************/
		boolean pasaPorBuscarSolicitud = false;
		Solicitud solicitud = new Solicitud();
		String nroRifCentroEdu = showResultToView.getNroRifCentroEdu();
		/**
		 * Si traemos un rif del colegio y la accion no es incluir, significa
		 * que debe existir esa solicitud para este colegio
		 */
		if (showResultToView.getNroSolicitud() > 0) {
			solicitud.setNroSolicitud(showResultToView.getNroSolicitud());
			pasaPorBuscarSolicitud = true;
		} else {
			/** Inicio Buscamos el numero de solicitud **/
			if (!Constantes.INCLUIR.equalsIgnoreCase(accion)
					&& !StringUtils.isEmpty(nroRifCentroEdu)) {
				solicitud.setNroSolicitud(0);
				/** Solo validacion */
				/**
				 * Buscamos la solicitud con codigo del empleado, codigo del
				 * beneficiario y numero rif del colegio
				 */
				
				solicitud=solicitudDao.BscarSolConCodEmpCodBenfNrif(showResultToView.getCodEmp(),
						showResultToView.getCodigoBenef(), nroRifCentroEdu);
 
				showResultToView = searchCentroEducativo(showResultToView);
				if (solicitud.getNroSolicitud() > 0) {
					pasaPorBuscarSolicitud = true;
				}
			} else {
				/**
				 * Buscamos solo por codigo Empleado y la cedula del familiarb a
				 * vr si tiene una solicitud activa
				 */
				/** Solo validacion */
				solicitud = solicitudDao.BscarSolConCodEmpCodBenf(
						showResultToView.getCodEmp(),
						showResultToView.getCodigoBenef());
				pasaPorBuscarSolicitud = true;
			}
			/** Fin Buscamos el numero de solicitud **/
		}

		/**
		 * Si existe una solicitud activa , la cargamos con el numero de
		 * solicitud que acabamos de encontrar
		 */
		if (pasaPorBuscarSolicitud) {
			solicitud=solicitudDao.consultarSolicitud(solicitud.getNroSolicitud());
			
		}

		/** Consultamos solicitud fin */
		/**************************************************/
		if ((!Constantes.INCLUIR.equalsIgnoreCase(accion))&&(solicitud==null || !pasaPorBuscarSolicitud)) {
			showResultToView.setError(Constantes.NO_HAY_DATA);
			showResultToView.setViene(null);
		}else{
			if (solicitud==null){
				solicitud= new Solicitud();
			}
			/** PERIODO ESCOLAR ENTRE OTRAS COSAS */
			showResultToView = cargarRestoInfo(solicitud, showResultToView);
			showResultToView.setViene("1");
		}

		return showResultToView;
	}

	/**
	 * 
	 * Carga los meses pagados y la matricula y Buscamos los conceptos de pago
	 * ..
	 * 
	 * @param codEmplado
	 * @param codigoBenef
	 * @param request
	 * @param showResultToView
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public ShowResultToView cargaMesesMatriculaConceptoDePago(
			HttpServletRequest request, ShowResultToView showResultToView,int nroSolicitud,String periodoEscolar)
			throws ServletException, IOException {
		ResourceBundle rb = ResourceBundle.getBundle("ve.org.bcv.rhei.util.bundle");
		String viene = "1";
		try {
			boolean isMatricula = false;
			boolean isPeriodo = false;

			ArrayList<String> listaMesesPagados = new ArrayList<String>();
 
		
			/** FIN Si no hay data en la bd, inicializamos valores por defecto */
			MesesBean mesesBean= new MesesBean();
			String[] mes=mesesBean.getMes();
			
			String[] meses =mesesBean.getMeses();
			
			String[] mesesAux = mesesBean.getMesesAux();

			/** BUSCAMOS SI LA MATRICULA HA SIDO PAGADA */
			boolean isVerifPagoMatricula = relacionDePagosDao.verificarPagoMatricula(
					nroSolicitud,
					periodoEscolar);
			if (isVerifPagoMatricula) {
				meses[0] = "*";
				isMatricula = true;
			}
			/** FIN BUSCAMOS SI LA MATRICULA HA SIDO PAGADA */
			int tipoPago = 0;
			if (!StringUtils.isEmpty(request.getParameter("tipoPago"))
					&& StringUtils.isNumeric(request.getParameter("tipoPago"))) {
				tipoPago = new Integer(request.getParameter("tipoPago"));
			}
			/** BUSCAMOS LOS MESES PAGADOS */
			int traemeAllMeses = -1;
			showResultToView.getPeriodoEscolar();
			listaMesesPagados = (ArrayList<String>) relacionDePagosDao.obtenerMesesPagados(
					nroSolicitud, periodoEscolar, traemeAllMeses, tipoPago);
			try {

				// ********************begin***************/
				/** LOS MESES PAGADOS LOS SE�ALAMOS CON UN ASTERISCO */
				if ((listaMesesPagados != null)
						&& ((listaMesesPagados.size() != 0))) {
					for (int j = 0; j <= 12; j++) {
						for (int k = 0; k < listaMesesPagados.size(); k++) {
							if (meses[j].equals(listaMesesPagados.get(k))) {
								if ( !"14".equalsIgnoreCase(meses[j])) {
									isPeriodo = true;
								}
								meses[j] = "*";
								break;
							}
						}
					}
				}

				// String mes; BUSCAR L MRES QUE SE ETA MOSTRANDO EN COPNSULTAR

				List<ValorNombre> listadoMesesPorPagar = new ArrayList<ValorNombre>();
				List<ValorNombre> listadoMesesPagados = new ArrayList<ValorNombre>();
				ValorNombre valorNombre = null;
				for (int j = 0; j <= 12; j++) {
					valorNombre = new ValorNombre(mesesAux[j], mes[j]);
					if (!meses[j].equals("*")) {
						listadoMesesPorPagar.add(valorNombre);
					} else {
						listadoMesesPagados.add(valorNombre);
					}
				}
				Collections.sort(listadoMesesPagados,new SortByValueComparatorAsc());
				showResultToView.setListadoMesesPorPagar(listadoMesesPorPagar);
				showResultToView.setListadoMesesPagados(listadoMesesPagados);
				/** FIN Sacamos los meses */

				/** Buscamos los conceptos de pago */
				List<ValorNombre> receptorPagos = new ArrayList<ValorNombre>();
				valorNombre = null;
				valorNombre = new ValorNombre("0",rb.getString("cei"));
				receptorPagos.add(valorNombre);
				valorNombre = new ValorNombre("1",rb.getString("trabajador"));
				receptorPagos.add(valorNombre);
				showResultToView.setReceptorPagos(receptorPagos);
				showResultToView.setMatricula(isMatricula);
				showResultToView.setPeriodo(isPeriodo);
				/** FIN Concepto de Pago */

				log.debug("Cargando atributos...");
				showResultToView.setViene(viene);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();			
		}

		
		return showResultToView;
	}

	public ShowResultToView cargarRestoInfo(Solicitud solicitud,
			ShowResultToView showResultToView) throws SQLException {

		/** Cargar todos los periodos escolares */
		List<ValorNombre> allPeriodos = periodoEscolarDao.tipoPeriodosEscolares();
		if (allPeriodos == null || allPeriodos.size() == 0
				|| allPeriodos.isEmpty()) {
			/**
			 * Si no existe un periodo escolar activo.. colocaremos este mensaje
			 * de error
			 */
			showResultToView.setMensaje("errorPeriodoScolar");
		} else {
			showResultToView.setPeriodoEscolares(allPeriodos);
		}

		String fecha = null;
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		if (solicitud.getFechaSolicitudOriginal() != null
				&& solicitud.getFechaSolicitudOriginal().getTime() > 0
				&& !StringUtils.isEmpty(solicitud.getFechaSolicitudOriginal()
						+ "")) {
			fecha = format.format(new Date(solicitud
					.getFechaSolicitudOriginal().getTime()));
			showResultToView.setFechaSolicitud(fecha);
		}
		if (solicitud.getFechaSolicitud() != null
				&& solicitud.getFechaSolicitud().getTime() > 0
				&& !StringUtils.isEmpty(solicitud.getFechaSolicitud() + "")) {
			fecha = format.format(new Date(solicitud.getFechaSolicitud()
					.getTime()));
			showResultToView.setFechaActual(fecha);
		}

		/**
		 * LLenamos un periodo escolar si no tiene valor con la lista e periodos
		 * escolares
		 */
		if (StringUtils.isEmpty(getShowResultToView().getPeriodoEscolar())) {
			if (getShowResultToView().getPeriodoEscolares() != null
					&& !getShowResultToView().getPeriodoEscolares().isEmpty()
					&& getShowResultToView().getPeriodoEscolares().size() > 0) {
				getShowResultToView().setPeriodoEscolar(
						getShowResultToView().getPeriodoEscolares().get(0)
								.getValor());
				
			}
		}
		/** Agarramos el periodo especifico de la solicitud */

		if (showResultToView.getNroSolicitud() > 0) {
			List<String> periodos = solicitudDao
					.cargarPeriodoByNroSolicitud(showResultToView
							.getNroSolicitud());
			if (periodos != null && periodos.size() > 0 && !periodos.isEmpty()) {
				showResultToView.setPeriodoEscolar(periodos.get(0));
			}
		}
		if (!StringUtils.isEmpty(solicitud.getNb_status())) {
			showResultToView.setNb_status(solicitud.getNb_status());
		}
		if (!StringUtils.isEmpty(solicitud.getNroSolicitud() + "")
				&& solicitud.getNroSolicitud() > 0) {
			showResultToView.setNroSolicitud(solicitud.getNroSolicitud());

		}
		if (!StringUtils.isEmpty(solicitud.getNroSolicitud() + "")
				&& solicitud.getNroSolicitud() > 0) {
			showResultToView.setNumSolicitud(solicitud.getNroSolicitud() + "");
		}

		if (!StringUtils.isEmpty(solicitud.getTipoCentroEdu())) {
			showResultToView.setTipoInstitucion(solicitud.getTipoCentroEdu());
		}

		if (!StringUtils.isEmpty(solicitud.getTipoEducacion())) {
			showResultToView.setTipoEducacion(solicitud.getTipoEducacion());

		}

		if (!StringUtils.isEmpty(solicitud.getPeriodoDePago())) {
			showResultToView.setPeriodoPago(solicitud.getPeriodoDePago());

		}

		if (!StringUtils.isEmpty(solicitud.getFormaDePago())) {
			showResultToView.setFormaPago(solicitud.getFormaDePago());

		}

		if (!StringUtils.isEmpty(solicitud.getCodigoEmpleado() + "")
				&& solicitud.getCodigoEmpleado() > 0) {
			showResultToView.setCodEmp(solicitud.getCodigoEmpleado() + "");

		}

		if (!StringUtils.isEmpty(solicitud.getCedulaFamiliar() + "")
				&& solicitud.getCedulaFamiliar() > 0) {
			showResultToView.setCedBenef(solicitud.getCedulaFamiliar() + "");
		}

		if (!StringUtils.isEmpty(solicitud.getNroRifCentroEdu())) {
			showResultToView.setNroRifCentroEdu(solicitud.getNroRifCentroEdu());
		}

		if (!StringUtils.isEmpty(solicitud.getNivelEscolar())) {
			showResultToView.setNivelEscolaridad(solicitud.getNivelEscolar());
		}

		if (solicitud.getMontoPeriodo() != null
				&& !StringUtils.isEmpty(solicitud.getMontoPeriodo() + "")
				&& solicitud.getMontoPeriodo() > 0) {
			showResultToView.setMontoPeriodo(solicitud.getMontoPeriodo());
		} else if (solicitud.getMontoPeriodo() == null) {
			showResultToView.setMontoPeriodo(0d);
		}

		if (solicitud.getMontoMatricula() != null
				&& !StringUtils.isEmpty(solicitud.getMontoMatricula() + "")
				&& solicitud.getMontoMatricula() > 0) {
			showResultToView.setMontoMatricula(solicitud.getMontoMatricula());
		} else if (solicitud.getMontoMatricula() == null) {
			showResultToView.setMontoMatricula(0d);
		}

		if (!StringUtils.isEmpty(solicitud.getComparteBeneficio())) {
			showResultToView.setBenefCompartido(solicitud
					.getComparteBeneficio());

		}

		if (!StringUtils.isEmpty(solicitud.getTipoEmpresa())) {
			showResultToView.setTipoEmpresa(solicitud.getTipoEmpresa());

		}
		if (solicitud.getMontoEmpresa() != null
				&& !StringUtils.isEmpty(solicitud.getMontoEmpresa() + "")
				&& solicitud.getMontoEmpresa() > 0) {
			showResultToView.setMontoAporteEmp(solicitud.getMontoEmpresa());

		} else if (showResultToView.getMontoAporteEmp() == null) {
			showResultToView.setMontoAporteEmp(0d);
		}

		if (!StringUtils.isEmpty(solicitud.getCo_status())) {
			showResultToView.setCo_status(solicitud.getCo_status());

		}

		if (solicitud.getCo_status() != null
				&& solicitud.getCo_status().length() > 0) {
			char y = solicitud.getCo_status().charAt(0);
			switch (y) {
			case 'A':
				showResultToView.setCo_statusCmplto("Activa");
				break;
			case 'C':
				showResultToView.setCo_statusCmplto("Actualizada");
				break;
			case 'R':
				showResultToView.setCo_statusCmplto("Renovada");
				break;
			case 'D':
				showResultToView.setCo_statusCmplto("Desincorporada");
				break;
			case 'M':
				showResultToView
						.setCo_statusCmplto("Actualizaci&oacute;n Masiva");
				break;
			default:
				showResultToView.setCo_statusCmplto("");
				break;
			}
		}

		if (solicitud.getLocalidadCEI() != null
				&& solicitud.getLocalidadCEI().length() > 0) {
			char y = solicitud.getLocalidadCEI().charAt(0);
			switch (y) {
			case 'C':
				showResultToView.setLocalidad("Caracas");
				break;
			case 'M':
				showResultToView.setLocalidad("Maracaibo");
				break;
			case 'Y':
				showResultToView.setLocalidad("Maracay");
				break;
			default:
				showResultToView.setLocalidad("");
				break;
			}
		}

		/** Fin Requesr propios del metodo */

		return showResultToView;

		/** Fin LLenamos la data que va para la vista */
	}

	

	/**
	 * @param cedula
	 * @param rangoEdad
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	private List<BeneficiarioValNom> getBeneficiarioPago(int cedula
			) throws ServletException, IOException {
		Trabajador trabajador = new Trabajador();
		ArrayList<String> duplaParametroTipo = null;
		trabajador.setCedula(0);
		List<BeneficiarioValNom> beneficiarios = new ArrayList<BeneficiarioValNom>();
		int edadMin = -1;
		int edadMax = -1;
		ArrayList<String> listaFiltros = null;
		ArrayList<String> listaFlia = null;
		trabajador.setCedula(cedula);
		TrabajadorDao trabajadorDao = new TrabajadorDaoImpl();
		try {
			listaFiltros = (ArrayList<String>) trabajadorDao
					.buscarFiltros(cedula);

			/** CBUSCAMOS EL PARAMETRO EDAD MIN Y EDAD MAX */
			/** Obtenemos el codigo de la compania */
			String compania = listaFiltros != null ? (String) listaFiltros
					.get(0) : "";
			String tipoEmp = listaFiltros.get(1) != null ? (String) listaFiltros
					.get(1) : "";

			/** Inicio Obtenemos el valor de la edad minima */
			duplaParametroTipo = (ArrayList<String>) trabajadorDao
					.buscarParametro(compania, tipoEmp,
							Constantes.TIPOBENEFICIO, Constantes.EDADIN);
			if (duplaParametroTipo != null && !duplaParametroTipo.isEmpty()
					&& duplaParametroTipo.size() > 0) {
				edadMin = Integer
						.parseInt(duplaParametroTipo != null ? ((String) duplaParametroTipo
								.get(0)).toString() : "");
			}

			/** Fin Obtenemos el valor de la edad minima */
			/** Inicio Obtenemos el valor de la edad maxima */
			duplaParametroTipo = (ArrayList<String>) trabajadorDao
					.buscarParametro(
							listaFiltros.get(0) != null ? (String) listaFiltros
									.get(0) : "",
							listaFiltros.get(1) != null ? (String) listaFiltros
									.get(1) : "", Constantes.TIPOBENEFICIO,
							Constantes.EDADFI);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (duplaParametroTipo != null && !duplaParametroTipo.isEmpty()
				&& duplaParametroTipo.size() > 0) {
			edadMax = Integer
					.parseInt(duplaParametroTipo != null ? (String) duplaParametroTipo
							.get(0) : "");
		}
		/** Fin Obtenemos el valor de la edad maxima */

		/** Inicio De la tabla PERSONAL.FAMILIARES , */
 
			listaFlia = beneficiarioDao.buscarBeneficiarioSinRestriccion(trabajador
					.getCedula());
			// ArrayList<String> buscarBeneficiarioSinRestriccion(int cedulaTrabajador)
		 
		/** Fin De la tabla PERSONAL.FAMILIARE */

		if ((listaFlia != null) && (listaFlia.size() != 0)) {
			BeneficiarioValNom benef = null;
			for (int i = 0; i < listaFlia.size(); i += 3) {
				benef = new BeneficiarioValNom();
				benef.setValor((String) listaFlia.get(i).toString());
				benef.setNombre((String) listaFlia.get(i).toString() + " - "
						+ (String) listaFlia.get(i + 1) + " "
						+ (String) listaFlia.get(i + 2));
				beneficiarios.add(benef);

			}
		}
		return beneficiarios;
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
	public ShowResultToView traerSoloBlk3(String codEmplado,
			String codigoBenef, HttpServletRequest request,
			ShowResultToView showResultToView) throws ServletException,
			IOException {

		try {
			log.debug(" 2.1 showResultToView.getMontoAporteEmp()="
					+ showResultToView.getMontoAporteEmp());

			/** Buscamos el numero de solicitud */

			String nivelEscolaridad = request.getParameter("nivelEscolaridad") != null ? (String) request
					.getParameter("nivelEscolaridad") : "";

			if ((showResultToView.getNivelEscolaridad() == null || StringUtils
					.isEmpty(showResultToView.getNivelEscolaridad()))
					|| !StringUtils.isEmpty(nivelEscolaridad)) {
				showResultToView.setNivelEscolaridad(nivelEscolaridad);
			}

			String montoMatricula = request.getParameter("montoMatricula") != null ? (String) request
					.getParameter("montoMatricula") : "";

			showResultToView.setMontoMatricula(0.0);
			if (!StringUtils.isEmpty(montoMatricula)
					&& StringUtils.isNumeric(montoMatricula)) {
				showResultToView.setMontoMatricula(new Double(montoMatricula));
			}

			showResultToView.setMontoPeriodo(0.0);
			String montoPeriodo = request.getParameter("montoPeriodo") != null ? (String) request
					.getParameter("montoPeriodo") : "";
			if (!StringUtils.isEmpty(montoPeriodo)
					&& StringUtils.isNumeric(montoPeriodo)) {
				showResultToView.setMontoPeriodo(new Double(montoPeriodo));
			}

			String montoAporteEmp = request.getParameter("montoAporteEmp") != null ? (String) request
					.getParameter("montoAporteEmp") : "";
			if (StringUtils.isEmpty(montoAporteEmp)) {
				montoAporteEmp = request.getParameter("montoAporteEmpHidden") != null ? (String) request
						.getParameter("montoAporteEmpHidden") : "";
			}
			showResultToView.setMontoAporteEmp(0.0);
			if (!StringUtils.isEmpty(montoAporteEmp)
					&& StringUtils.isNumeric(montoAporteEmp)) {
				showResultToView.setMontoAporteEmp(new Double(montoAporteEmp));
			}

			// request.setAttribute("montoAporteEmp", montoAporteEmp);
			String benefCompartido = request.getParameter("benefCompartido") != null ? (String) request
					.getParameter("benefCompartido") : "";
			// request.setAttribute("benefCompartido", benefCompartido);
			showResultToView.setBenefCompartido(benefCompartido);

			String tipoEmpresa = request.getParameter("tipoEmpresa") != null ? (String) request
					.getParameter("tipoEmpresa") : "";
			if (StringUtils.isEmpty(tipoEmpresa)) {
				tipoEmpresa = request.getParameter("tipoEmpresaHidden") != null ? (String) request
						.getParameter("tipoEmpresaHidden") : "";
			}
			// request.setAttribute("tipoEmpresa", tipoEmpresa);
			showResultToView.setTipoEmpresa(tipoEmpresa);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return showResultToView;
	}

	/**
	 * Chequeamos que la solicitus este activa
	 * 
	 * @param codEmplado
	 * @param codigoBenef
	 * @return
	 */
	public boolean activaSolicitud(ShowResultToView showResultToView) {

		boolean isActiva = false;

		Solicitud solicitud = new Solicitud();
		try {
			isActiva = solicitudDao.activaSolicitud(showResultToView.getCodEmp(),
					showResultToView.getCodigoBenef());
			if (isActiva) {
				solicitud = solicitudDao.BscarSolConCodEmpCodBenf(
						showResultToView.getCodEmp(),
						showResultToView.getCodigoBenef());
				showResultToView.setNroSolicitud(solicitud.getNroSolicitud());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		solicitud = null;
		return isActiva;
	}

 
	// }

	/**
	 * Retun tip of Intitute
	 * 
	 * @return
	 */
	private List<ValorNombre> tipoInstituciones() {
		List<ValorNombre> objetos = new ArrayList<ValorNombre>();

		ValorNombre valorNombre = new ValorNombre("S", "No Convenido");
		objetos.add(valorNombre);
		valorNombre = new ValorNombre("N", "Convenido");
		objetos.add(valorNombre);
		return objetos;
	}

	/**
	 * 
	 * Educacion option
	 * 
	 * @return
	 */
	private List<ValorNombre> tipoEducacions() {
		List<ValorNombre> objetos = new ArrayList<ValorNombre>();

		ValorNombre valorNombre = new ValorNombre("E", "Especial");
		objetos.add(valorNombre);
		valorNombre = new ValorNombre("R", "Regular");
		objetos.add(valorNombre);

		return objetos;
	}

	/**
	 * 
	 * Lista de meses
	 * 
	 * @return
	 */
	public List<ValorNombre> meses() {
		List<ValorNombre> objetos = new ArrayList<ValorNombre>();
		/** Buscamos en BD */
		String[] mes = { "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE",
				"ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO",
				"AGOSTO" };
		ValorNombre valorNombre = null;
		for (int i = 0; i < 12; i++) {
			/** Ultimos meses (mes 9 a mes 12) de septiembre a Diciembre */
			if (i <= 3) {
				valorNombre = new ValorNombre((i + 9) + "", mes[i]);
			} else {
				/** Primeros meses (mes 1 a mes 8) de Enero a Agosto */
				valorNombre = new ValorNombre((i - 3) + "", mes[i]);
			}
			objetos.add(valorNombre);
		}
		return objetos;
	}
	

	private List<ValorNombre> nivelEscolaridades() {
		List<ValorNombre> objetos = new ArrayList<ValorNombre>();

		ValorNombre valorNombre = new ValorNombre("0", "Maternal");
		objetos.add(valorNombre);
		valorNombre = new ValorNombre("1", "1er. Nivel");
		objetos.add(valorNombre);
		valorNombre = new ValorNombre("2", "2do.. Nivel");
		objetos.add(valorNombre);
		valorNombre = new ValorNombre("3", "3er. Nivel");
		objetos.add(valorNombre);
		valorNombre = new ValorNombre("4", "Educ. Basica");
		objetos.add(valorNombre);
		valorNombre = new ValorNombre("5", "Educ. Media");
		objetos.add(valorNombre);
		valorNombre = new ValorNombre("6", "Universitario");
		objetos.add(valorNombre);
		valorNombre = new ValorNombre("7", "Otros");
		objetos.add(valorNombre);
				 
		return objetos;
	}

	private List<ValorNombre> periodoPagos() {
		List<ValorNombre> objetos = new ArrayList<ValorNombre>();

		ValorNombre valorNombre = new ValorNombre("1", "Mensual");
		objetos.add(valorNombre);
		valorNombre = new ValorNombre("2", "Bimensual");
		objetos.add(valorNombre);
		valorNombre = new ValorNombre("3", "Trimestral");
		objetos.add(valorNombre);
		valorNombre = new ValorNombre("4", "Cuatrimestral");
		objetos.add(valorNombre);
		valorNombre = new ValorNombre("5", "Semestral");
		objetos.add(valorNombre);
		valorNombre = new ValorNombre("6", "Anual");
		objetos.add(valorNombre);
		return objetos;
	}

	private List<FormaPagoValNom> listaDepagos() {
		FormaPagoValNom formaPagoValNom = null;
		List<FormaPagoValNom> formaPagoValNoms = new ArrayList<FormaPagoValNom>();
		List<String> listaFormaPago;
		try {
			listaFormaPago = solicitudDao.cargarFormaPago();
			if ((listaFormaPago != null) && (listaFormaPago.size() > 0)) {
				for (int i = 0; i < listaFormaPago.size(); i += 2) {
					formaPagoValNom = new FormaPagoValNom();
					formaPagoValNom.setValor(listaFormaPago.get(i) + "");
					formaPagoValNom.setNombre(listaFormaPago.get(i + 1) + "");
					formaPagoValNoms.add(formaPagoValNom);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return formaPagoValNoms;
	}

	 
	/**
	 * @param cedula
	 * @param companiaAnalista
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	 
	/**
	 * @param cedula
	 * @param companiaAnalista
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	/**
	 * @param cedula
	 * @param companiaAnalista
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public ShowResultToView searchBenefCodPeriodoPagoMeses(String cedula,
			String companiaAnalista) throws ServletException, IOException {

		String mensaje = "";

		ShowResultToView showResultToView = new ShowResultToView();
		Trabajador trabajador = new Trabajador();
		trabajador.setCedula(0);
		List<BeneficiarioValNom> beneficiarios = new ArrayList<BeneficiarioValNom>();

		if (!StringUtils.isEmpty(cedula)) {
			if (StringUtils.isNumeric(cedula)) {
				trabajador.setCedula(Integer.parseInt(cedula));
			}
		}

		/**
		 * obtenemos el codEmp dentro del objeto trabajadores si existe el
		 * empleado
		 */
		TrabajadorDao trabajadorDao = new TrabajadorDaoImpl();
		try {
			if (trabajadorDao.consultar(companiaAnalista, "consultar",trabajador.getCedula())) {
				boolean rangoEdad = false;
				/**
				 * INICIO Buscamos los NINOS beneficiarios en table
				 * PERSONAL.FAMILIARES
				 */
				beneficiarios = getBeneficiarioPago(trabajador.getCedula());
				if (beneficiarios == null || beneficiarios.isEmpty()
						|| beneficiarios.size() == 0) {
					mensaje = "El trabajador no tiene familiares aptos, su estatus es Egresado o la solicitud fue desincorporada";
					// request.setAttribute("mensaje", this.mensaje);
				}else{
					List<BeneficiarioValNom> beneficiariosFiltroConsultar = new ArrayList<BeneficiarioValNom>();
					for (BeneficiarioValNom benef:beneficiarios){
						boolean existe=false;
						try {
							existe = relacionDePagosDao.existePagoDelBeneficiario(Constantes.ACTIVO_EMPLEADO, trabajador.getCedula()+"",benef.getValor());
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if ( existe){
							beneficiariosFiltroConsultar.add(benef);
						}
					}
					beneficiarios =  new ArrayList<BeneficiarioValNom>();
					 
						beneficiarios.addAll(beneficiariosFiltroConsultar);
					 showResultToView.setBeneficiarios(beneficiarios);
					
					
				}
				/** FIN Buscamos los beneficiarios en table PERSONAL.FAMILIARES */

			} else {
				log.debug("No entro en el método consultar de la clase trabajador");
				mensaje = ("No existe un trabajador con cédula de identidad "
						+ trabajador.getCedula()
						+ ", el estatus del trabajador consultado es egresado, <br/> o no tiene permiso sobre " + "los datos de ese trabajador");
				// request.setAttribute("mensaje", this.mensaje);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/** Buscamos los periodos escolares */
		showResultToView.setPeriodoEscolares(periodoEscolarDao
				.tipoPeriodosEscolares());
		/** Buscamos Fin los periodos escolares */
		try {
			showResultToView
					.setEstadosSolicitudLst(solicitudDao.estadosSolicitudLst());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/** Buscamos los meses */
		showResultToView.setMeses(meses());
		/**SI ES MENOS 14 (-14) IMPLICA QUWE LA MATRICULA FUE PAGADA FUERA DELS SISTEMA*/
		/**SE MULTIPLICA POR MENOS UNO PARA OBTENER EL VALOR DE MATRICULA QUE NO A SIDO PAGADA FUERA DEL SISTEMA*/
		ValorNombre valorNombre = new ValorNombre((Constantes.MATRICULA_PAGADA*-1)+"",
				Constantes.MATRICULA);
		showResultToView.getMeses().add(new Integer(Constantes.CERO), valorNombre);
		/** Colocamos el mes en -1 para selecccione */
		showResultToView.setInMesMatricula(-1);
		/** Fin Buscamos los meses */
		showResultToView.setMensaje(mensaje);
		showResultToView.setCedula(String.valueOf(trabajador.getCedula()));
		showResultToView.setCodEmp(String.valueOf(trabajador
				.getCodigoEmpleado()));
		showResultToView.setBeneficiarios(beneficiarios);
		if (beneficiarios == null || beneficiarios.isEmpty()
				|| beneficiarios.size() == 0) {
			showResultToView.setMensaje("fracaso4");
		}
		return showResultToView;
	}

	 

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public ShowResultToView getShowResultToView() {
		return showResultToView;
	}

	public void setShowResultToView(ShowResultToView showResultToView) {
		this.showResultToView = showResultToView;
	}

	public RegistrarSolicitudProcesar getProcesar() {
		return procesar;
	}

	public void setProcesar(RegistrarSolicitudProcesar procesar) {
		this.procesar = procesar;
	}

	public String getCompaniaAnalista() {
		return companiaAnalista;
	}

	public void setCompaniaAnalista(String companiaAnalista) {
		this.companiaAnalista = companiaAnalista;
	}

	public ServletContext getSc() {
		return sc;
	}

	public void setSc(ServletContext sc) {
		this.sc = sc;
	}

}
