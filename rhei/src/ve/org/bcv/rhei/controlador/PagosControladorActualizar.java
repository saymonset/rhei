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

import org.apache.log4j.Logger;

import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.negocio.PagoActualizar;
import ve.org.bcv.rhei.negocio.PagoBuscarToActualizar;
import ve.org.bcv.rhei.negocio.SolAction;
import ve.org.bcv.rhei.util.Constantes;

import com.bcv.dao.jdbc.FacturaDao;
import com.bcv.dao.jdbc.impl.FacturaDaoImpl;
import com.bcv.model.BeneficioEscolar;
import com.bcv.model.Factura;
import com.bcv.model.PeriodoEscolar;

/**
 * procesamos los pagos para actualizar
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com
 * 27/02/2015 18:52:11
 * 
 */
public class PagosControladorActualizar extends HttpServlet implements Serializable {
	private static Logger log = Logger.getLogger(PagosControladorActualizar.class
			.getName());

	BeneficioEscolar beneficio = null;
	PeriodoEscolar periodo = null;

	String tablaBeneficios = "";
	String tablaPeriodos = "";
	String condicion = "A";

	String mensaje = "";
 
	String actualizarPagoIrToPag = "/jsp/pagoActualizar.jsp";
 
 

	String pagAgregarParam = "/jsp/ .jsp";
	String pagModifyParam = "/jsp/ .jsp";
	String tabla = "";

	private ServletContext sc;
	private String aviso = "FinSesion";
	private RequestDispatcher rd = null;
	private HttpSession sesion = null;
	private FacturaDao facturaDao= new FacturaDaoImpl();
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
			RequestDispatcher despachador = null;
			SolAction solAction = null;
			ShowResultToView showResultToView = new ShowResultToView();
			  if ("actualizarPagoIr".equals(request
					.getParameter("principal.do"))) {
				String pagIr = "";
				pagIr = actualizarPagoIrToPag;
				despachador = request.getRequestDispatcher(pagIr);
				despachador.forward(request, response);
				 
			}   else  if ("buscarDatActualizar".equals(request
					.getParameter("principal.do"))) {
				
		 
				solAction = new PagoBuscarToActualizar(request, response,
						showResultToView, this.sc);
				showResultToView = solAction.ejecutar();
				
				int numSolicituds=showResultToView.getNroSolicitud();
				
				 
				 List<Factura> facturas=null;
				 
					List<Factura> facturasNoComplementos=null;
					List<Factura> facturasStanConComplementos=null;
					try {
						boolean isActualizar=true;
						facturasNoComplementos = facturaDao .facturasByNumSolicitudMatriculaPeriodo(numSolicituds, Constantes.FILTRARBYMESORCOMPLEMENTOORAMBOS_MATRICULA,isActualizar,showResultToView.getPeriodoEscolar());
						facturasStanConComplementos=facturaDao .facturasByNumSolicitudMatriculaPeriodo(numSolicituds, Constantes.FILTRARBYMESORCOMPLEMENTOORAMBOS_REEMBOLSO,isActualizar,showResultToView.getPeriodoEscolar());
				
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				 
					 facturas = new ArrayList<Factura>();
					 facturas.addAll(facturasNoComplementos);
					 facturas.addAll(facturasStanConComplementos);
//				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//				String jsondata = ow.writeValueAsString(facturas);
//				
					 request.setAttribute("facturas", facturas);
				
				
				request.setAttribute("showResultToView", showResultToView);
				String pagIr = "";
				pagIr = actualizarPagoIrToPag;
				despachador = request.getRequestDispatcher(pagIr);
				despachador.forward(request, response);
			 
			} else  if ("actualizar".equals(request
					.getParameter("principal.do"))) {
				/**OBTENEMOS EL PAGO Y FACTURA*/
	
				PagoActualizar pagoActualizar= new PagoActualizar(request, response,
				showResultToView, this.sc);
				showResultToView = pagoActualizar.ejecutar();	
				request.setAttribute("showResultToView", showResultToView);
				
				String pagIr = "";
				pagIr = actualizarPagoIrToPag;
				despachador = request.getRequestDispatcher(pagIr);
				despachador.forward(request, response);
			}
			 /*********FIN ACTUALIZAR  PAGO CONVENCIONAL********************************/ 

		}

	}

}
