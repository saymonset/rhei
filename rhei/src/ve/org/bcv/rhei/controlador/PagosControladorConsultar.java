package ve.org.bcv.rhei.controlador;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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

import ve.org.bcv.rhei.bean.FormaPagoValNom;
import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.bean.ValorNombre;
import ve.org.bcv.rhei.negocio.PagoBuscarDataConsultar;
import ve.org.bcv.rhei.negocio.SolAction;
import ve.org.bcv.rhei.report.by.benef.Reporte;
import ve.org.bcv.rhei.util.Constantes;

import com.bcv.dao.jdbc.BcvDao;
import com.bcv.dao.jdbc.FacturaDao;
import com.bcv.dao.jdbc.RelacionDePagosDao;
import com.bcv.dao.jdbc.SolicitudDao;
import com.bcv.dao.jdbc.TipoEmpleadoDao;
import com.bcv.dao.jdbc.impl.BcvDaoImpl;
import com.bcv.dao.jdbc.impl.FacturaDaoImpl;
import com.bcv.dao.jdbc.impl.RelacionDePagosDaoImpl;
import com.bcv.dao.jdbc.impl.SolicitudDaoImpl;
import com.bcv.dao.jdbc.impl.TipoEmpleadoDaoImpl;
import com.bcv.model.BeneficioEscolar;
import com.bcv.model.Factura;
import com.bcv.model.PeriodoEscolar;
import com.bcv.reporte.pagotributo.PagoTributoBeanEmp;
import com.bcv.reporte.pagotributo.PagoTributoCEI;
import com.bcv.reporte.pagotributo.ReporteByPagoAtributoCEI;
import com.bcv.reporte.pagotributo.ReporteByPagoAtributoEmp;

/**
 * Consultamos pago
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com
 * 31/03/2015 20:14:22
 * 
 */
public class PagosControladorConsultar extends HttpServlet implements Serializable {
	private static Logger log = Logger.getLogger(PagosControladorConsultar.class
			.getName());
	 
	BeneficioEscolar beneficio = null;
	PeriodoEscolar periodo = null;

	String tablaBeneficios = "";
	String tablaPeriodos = "";
	String condicion = "A";

	String mensaje = "";
 
	String consultarPagoIrToPag = "/jsp/pagoConvConsultar.jsp";
 

	String pagAgregarParam = "/jsp/ .jsp";
	String pagModifyParam = "/jsp/ .jsp";
	String tabla = "";

	private ServletContext sc;
	private String aviso = "FinSesion";
	private RequestDispatcher rd = null;
	private HttpSession sesion = null;
	private RelacionDePagosDao relacionDePagosDao = new RelacionDePagosDaoImpl();
	private FacturaDao facturaDao= new FacturaDaoImpl();
	private TipoEmpleadoDao tipoEmpleadoDao= new TipoEmpleadoDaoImpl();
	private BcvDao bcvDao = new BcvDaoImpl();
	private SolicitudDao solicitudDao = new SolicitudDaoImpl();
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
		
		
		ResourceBundle rb = ResourceBundle.getBundle("ve.org.bcv.rhei.util.bundle");
		if (request.getSession(false) == null) {
			this.sesion = request.getSession(true);
			this.sesion.setAttribute("aviso", this.aviso);
			this.rd = request.getRequestDispatcher("/jsp/login.jsp");
			this.rd.forward(request, response);
		} else {
			RequestDispatcher despachador = null;
			SolAction solAction = null;
			ShowResultToView showResultToView = new ShowResultToView();
			   if ("consultarPago".equals(request
					.getParameter("principal.do"))) {
				String pagIr = "";
				pagIr = consultarPagoIrToPag;
				despachador = request.getRequestDispatcher(pagIr);
				despachador.forward(request, response);
				/**
				 * LLenamos los datos del trabajador , beneficiario y datos
				 * extras del colegio
				 */
			}   else  if ("buscarDataPago".equals(request
					.getParameter("principal.do"))) {
			 
				/**OBTENEMOS EL PAGO Y FACTURA*/
				int nroSolicitud=0;
				/**Esta variable la usamos nombreAjax en  solicIngrBlke1WithNroFactura.jsp, hay colocamos el nroSolicitud*/
				/**It is numeric of nroSolicitud , it ios come frome ajax search*/
				if (!StringUtils.isEmpty(request.getParameter("nombreAjax"))){
					nroSolicitud= new Integer(request.getParameter("nombreAjax"));
				}
			 
				showResultToView.setNroSolicitud(nroSolicitud);
				solAction = new PagoBuscarDataConsultar(request, response,
						showResultToView, this.sc);
				showResultToView = solAction.ejecutar();
		 
			 
				String ninoEspecial = request.getParameter("ninoEspecial")!=null?(String)request.getParameter("ninoEspecial"):"";
				
				int numSolicituds=showResultToView.getNroSolicitud();
				
	 
				 List<Factura> facturas=null;
				 
					List<Factura> facturasNoComplementos=null;
					List<Factura> facturasStanConComplementos=null;
					try {
						boolean isActualizar=false;
						
						facturasNoComplementos = facturaDao .facturasByNumSolicitudMatriculaPeriodo(numSolicituds, Constantes.FILTRARBYMESORCOMPLEMENTOORAMBOS_MATRICULA,isActualizar,showResultToView.getPeriodoEscolar());
						facturasStanConComplementos=facturaDao .facturasByNumSolicitudMatriculaPeriodo(numSolicituds, Constantes.FILTRARBYMESORCOMPLEMENTOORAMBOS_REEMBOLSO,isActualizar,showResultToView.getPeriodoEscolar());
						
							/**filtrarByMesOrComplementoOrAmbos: 0 es matricla , 1 es reembolso y 2 es ambos**/
						String filtrarByMesOrComplementoOrAmbos="2";
					    int receptorPago=-1;// 0 CENTRO DE EDUCACION INICIAL, 1 TRABAJADOR
 					    int coFormaPago=-1; //1 ES CHEQUE, 2 ES AVISO DE CREDITO
						String meses=null;
						String status="A";
						if( showResultToView.getNb_status().equalsIgnoreCase(status)){
							status=Constantes.CO_STATUS_ACTIVO;
						}else if( showResultToView.getNb_status().equalsIgnoreCase(status)){
							status=Constantes.CO_STATUS_DESINCORPORADO;
						}
						String tipoEmpleado="EMP";
						String compania="01";
						if (!StringUtils.isEmpty(showResultToView.getCedula())){
							 tipoEmpleado=tipoEmpleadoDao.tipoEmpleadoSolo(showResultToView.getCedula()) ;
							 compania=bcvDao.cargarCompaniaAnalista(Integer.parseInt(showResultToView.getCedula()));
						}

						 String isToCreateReportDefinitivo=Constantes.PAGADO_NOPAGADO_AMBOS;
						int coRepStatus=0;
						receptorPago=0;//  0 CENTRO DE EDUCACION INICIAL
						Reporte reporte= new ReporteByPagoAtributoCEI(  showResultToView.getPeriodoEscolar(),  compania,  receptorPago,  coFormaPago, numSolicituds+"",  status ,tipoEmpleado,meses,filtrarByMesOrComplementoOrAmbos,coRepStatus,isToCreateReportDefinitivo,ninoEspecial);
						List<PagoTributoCEI> listCEI=(List<PagoTributoCEI>) reporte.searchObjects();
						 request.setAttribute("listCEI", listCEI);
						receptorPago=1;// 1 TRABAJADOR
						reporte= new ReporteByPagoAtributoEmp(showResultToView.getPeriodoEscolar(),  compania,  receptorPago,  coFormaPago, numSolicituds+"", status ,tipoEmpleado,meses,filtrarByMesOrComplementoOrAmbos,coRepStatus,isToCreateReportDefinitivo,ninoEspecial);
						List<PagoTributoBeanEmp> listEMP=(List<PagoTributoBeanEmp>) reporte.searchObjects();
						 request.setAttribute("listEMP", listEMP);
						
						
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				 
					 facturas = new ArrayList<Factura>();
					 if (facturasNoComplementos!=null){
						 facturas.addAll(facturasNoComplementos);	 
					 }
					 if (facturasStanConComplementos!=null){
						 facturas.addAll(facturasStanConComplementos);	 
					 }

						/** Buscamos los conceptos de pago */
						List<ValorNombre> receptorPagos = new ArrayList<ValorNombre>();
						ValorNombre	valorNombre = null;
						valorNombre = new ValorNombre("0",rb.getString("cei"));
						receptorPagos.add(valorNombre);
						valorNombre = new ValorNombre("1",rb.getString("trabajador"));
						receptorPagos.add(valorNombre);
						valorNombre = new ValorNombre("2",rb.getString("reporte.ambos"));
						receptorPagos.add(valorNombre);
						showResultToView.setReceptorPagos(receptorPagos);
						
						
						/** Buscamos los conceptos de pago */
						// coFormaPago=-1; //1 ES CHEQUE, 2 ES AVISO DE CREDITO
						FormaPagoValNom formaPagoValNom = null;
						List<FormaPagoValNom> formaPagoValNoms = new ArrayList<FormaPagoValNom>();
						List<String> listaFormaPago;
						try {
							listaFormaPago = solicitudDao.cargarFormaPago();
							if ((listaFormaPago != null) && (listaFormaPago.size() > 0)) {
								for (int i = 0; i < listaFormaPago.size(); i += 2) {
									formaPagoValNom = new FormaPagoValNom();
									formaPagoValNom
											.setValor(listaFormaPago.get(i) + "");
									formaPagoValNom.setNombre(listaFormaPago.get(i + 1)
											+ "");
									formaPagoValNoms.add(formaPagoValNom);
								}
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						/***Ambos**/
						formaPagoValNom = new FormaPagoValNom();
						formaPagoValNom
								.setValor(Constantes.FORMA_PAGO_AMBOS);
						formaPagoValNom.setNombre(rb.getString("reporte.ambos"));
						formaPagoValNoms.add(formaPagoValNom);
						/****/
						showResultToView.setFormaPagoValNoms(formaPagoValNoms);

						
						
					 
			    request.setAttribute("facturas", facturas);
				request.setAttribute("showResultToView", showResultToView);
				String pagIr = "";
				pagIr = consultarPagoIrToPag;
				despachador = request.getRequestDispatcher(pagIr);
				despachador.forward(request, response);
				/**
				 * LLenamos los datos del trabajador , beneficiario y datos
				 * extras del colegio
				 */
			}
			 /*********FIN CONSULTAR UN PAGO CONVENCIONAL********************************/
			 

		}

	}

}
