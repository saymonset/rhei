package ve.org.bcv.rhei.controlador;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
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

import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.bean.ValorNombre;
import ve.org.bcv.rhei.negocio.MesesBean;
import ve.org.bcv.rhei.negocio.PagoBuscarDataIncluir;
import ve.org.bcv.rhei.negocio.PagoPagarII;
import ve.org.bcv.rhei.negocio.RegistrarSolicitudProcesar;
import ve.org.bcv.rhei.negocio.SolAction;
import ve.org.bcv.rhei.util.Constantes;

import com.bcv.comparator.SortByValueComparatorAsc;
import com.bcv.dao.jdbc.ConyugeTrabajoDao;
import com.bcv.dao.jdbc.FamiliarDao;
import com.bcv.dao.jdbc.ParametroDao;
import com.bcv.dao.jdbc.RelacionDePagosDao;
import com.bcv.dao.jdbc.impl.ConyugeTrabajoDaoImpl;
import com.bcv.dao.jdbc.impl.FamiliarDaoImpl;
import com.bcv.dao.jdbc.impl.ParametroDaoImpl;
import com.bcv.dao.jdbc.impl.RelacionDePagosDaoImpl;
import com.bcv.model.BeneficioEscolar;
import com.bcv.model.ConyugeTrabajo;
import com.bcv.model.Familiar;
import com.bcv.model.Parametro;
import com.bcv.model.PeriodoEscolar;

/**
 * Pago controlador registrar
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com
 * 31/03/2015 19:28:27
 * 
 */
public class PagosControladorReg  extends HttpServlet implements Serializable {
	 

	BeneficioEscolar beneficio = null;
	PeriodoEscolar periodo = null;

	String tablaBeneficios = "";
	String tablaPeriodos = "";
	String condicion = "A";

	String mensaje = "";
	String pagoConvRegistrar = "/jsp/pagoConvRegistrar.jsp";
 

	String pagAgregarParam = "/jsp/ .jsp";
	String pagModifyParam = "/jsp/ .jsp";
	String tabla = "";

	private ServletContext sc;
	private String aviso = "FinSesion";
	private RequestDispatcher rd = null;
	private HttpSession sesion = null;
	private ConyugeTrabajoDao conyugeTrabajoDao = new ConyugeTrabajoDaoImpl();
	private RelacionDePagosDao relacionDePagosDao= new RelacionDePagosDaoImpl();
	private ParametroDao parametroDao = new ParametroDaoImpl();
	private FamiliarDao familiarDao= new FamiliarDaoImpl();
	private static Logger log = Logger.getLogger(PagosControladorReg.class
			.getName());

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
		RegistrarSolicitudProcesar procesar = new RegistrarSolicitudProcesar(
				this.sc);
		if (request.getSession(false) == null) {
			this.sesion = request.getSession(true);
			this.sesion.setAttribute("aviso", this.aviso);
			this.rd = request.getRequestDispatcher("/jsp/login.jsp");
			this.rd.forward(request, response);
		} else {
			
			
			Parametro parametro=null;
			try {
				parametro = parametroDao.findParametro(Constantes.CODIGO_COMPANIA,
						"EMP", "RHEI", Constantes.URL_COMPLEMENTO, null);
				if (null != parametro) {
					request.setAttribute("url_complemento", parametro.getValorParametro());
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			
			RequestDispatcher despachador = null;
			SolAction solAction = null;
			ShowResultToView showResultToView = new ShowResultToView();
			 /*********INICIO REGISTRAR UN PAGO CONVENCIONAL********************************/
			 if ("pagoConvencional".equals(request
					.getParameter("principal.do"))) {
				String pagIr = "";
				pagIr = pagoConvRegistrar;
			
				 /** Tomamos el colegio seleccionado anteriormente*/
				String nroRifCentroEdu=request.getParameter("nroRifCentroEdu")!=null?(String)request.getParameter("nroRifCentroEdu"):"";
				showResultToView.setNroRifCentroEdu(nroRifCentroEdu);
				request.setAttribute("showResultToView", showResultToView);
				
				despachador = request.getRequestDispatcher(pagIr);
				despachador.forward(request, response);
				/**
				 * LLenamos los datos del trabajador , beneficiario y datos
				 * extras del colegio  y LA SOLIITUD A PAGAR
				 */
			} else if ("buscarData".equals(request
					.getParameter("principal.do"))) {
				
				solAction = new PagoBuscarDataIncluir(request, response,
						showResultToView, this.sc);
				showResultToView = solAction.ejecutar();
				
				
				

				  if (showResultToView.getMontoPeriodo()==null){
					  showResultToView.setMontoPeriodo(0d);
		            }
            if (showResultToView.getMontoMatricula()==null){
            	showResultToView.setMontoPeriodo(0d);
            }
				
				
       

				request.setAttribute("showResultToView", showResultToView);
				String pagIr = request.getParameter("accion") != null ? (String) request
						.getParameter("accion") : "";
				despachador = request.getRequestDispatcher("/jsp/" + pagIr);		
						
						if (!StringUtils.isEmpty(showResultToView.getMensaje())) {
							if (("fracaso".equalsIgnoreCase(showResultToView
									.getMensaje()))
									|| ("fracaso1".equalsIgnoreCase(showResultToView
											.getMensaje()))
									|| ("error".equalsIgnoreCase(showResultToView
											.getMensaje()))) {
								
								String pagIrAfter="";
							   
									pagIrAfter="accionPagConv";
								despachador = request.getRequestDispatcher("/intitucionCtrl?principal.do=institucionIrToPag&pagIrAfter="+pagIrAfter+"&mensajeError="+showResultToView
										.getMensaje());
							} 

						}
						
						
				
						/**************MESES******ESTOS SON LOS MESES PARA COMPLEMENTOS O REEMBOLSOS,.. SON PAGOS EXTRAORDINARIOS**/
						MesesBean mesesBean= new MesesBean();
						String[] mes=mesesBean.getMes();
						String[] meses = mesesBean.getMeses();
					

						//ValidNuRefPago
						List<ValorNombre> listadoMeses = new ArrayList<ValorNombre>();
						ValorNombre valorNombre = null;
					
						for (int j = 0; j <= 12; j++) {
							
							valorNombre = new ValorNombre(meses[j], mes[j]);
							//valorNombre = new ValorNombre(jsondata, mes[j]);
							listadoMeses.add(valorNombre);
						}
						Collections.sort(listadoMeses,new SortByValueComparatorAsc());
					    request.setAttribute("listadoMeses", listadoMeses);
						
						/***********FIN MESES***********/
						
					    /**Fin Lista de los recaudos a ser llenados pr el usuario*/
						/**Inicio Conyuge*/
						int cedula=0;
						if (!StringUtils.isEmpty(showResultToView.getCedula())&& StringUtils.isNumeric(showResultToView.getCedula())){
							cedula = new Integer(showResultToView.getCedula());
						}
						ConyugeTrabajo  conyugeTrabajo  =null;
						try {
							  conyugeTrabajo  =conyugeTrabajoDao.find(cedula);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						request.setAttribute("conyugeTrabajo", conyugeTrabajo);
							Familiar familiar=null;;
							try {
								familiar = familiarDao.consultarConyuge(new Long(cedula));
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							request.setAttribute("cedulaconyuge",showResultToView.getCedula());
							if (familiar!=null){
								request.setAttribute("nameconyuge", familiar.getNombre());
								request.setAttribute("apellidoconyuge", familiar.getApellido());	
							}
							
						/**Fin Conyuge*/
						
				 
					
					/**Fin Conyuge*/	
			
				despachador.forward(request, response);
				
				 
				/** Vamos pagar */
			}  else if ("convencionalPago".equals(request
					.getParameter("principal.do"))) {
				/** Pagamos y guardamos la factura*/
				solAction = new PagoPagarII(request, response,
						showResultToView, this.sc);
				showResultToView = solAction.ejecutar();
				/**Fin Pagamos y guardamos la factura*/
				
				request.setAttribute("showResultToView", showResultToView);
				String pagIr = request.getParameter("accion") != null ? (String) request
						.getParameter("accion") : "";
				despachador = request.getRequestDispatcher("/jsp/" + pagIr);
				despachador.forward(request, response);
				
				/*********FIN REGISTRAR UN PAGO CONVENCIONAL********************************/	 
				
				
				/** VAMOS A LA PAG CONSULTAR PAGO */
			} 
		}

	}

}
