/**
 * 
 */
package ve.org.bcv.rhei.controlador;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
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
import ve.org.bcv.rhei.negocio.MesesBean;
import ve.org.bcv.rhei.util.Constantes;

import com.bcv.comparator.SortByValueComparatorAsc;
import com.bcv.dao.jdbc.PeriodoEscolarDao;
import com.bcv.dao.jdbc.ReporteEstatusDao;
import com.bcv.dao.jdbc.SolicitudDao;
import com.bcv.dao.jdbc.TipoEmpleadoDao;
import com.bcv.dao.jdbc.impl.PeriodoEscolarDaoImpl;
import com.bcv.dao.jdbc.impl.ReporteEstatusDaoImpl;
import com.bcv.dao.jdbc.impl.SolicitudDaoImpl;
import com.bcv.dao.jdbc.impl.TipoEmpleadoDaoImpl;
import com.bcv.model.TipoEmpleado;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco 29/10/2015 11:06:50 2015 mail :
 *         oraclefedora@gmail.com
 */
public class ReporteConsultaIndividualControlador extends HttpServlet
		implements
			Serializable {
	private static Logger log = Logger
			.getLogger(ReporteConsultaIndividualControlador.class.getName());

	String mensaje = "";

	String reportePagina = "/jsp/reporteConsultaIndividual0.jsp";

	private ServletContext sc;
	private String aviso = "FinSesion";
	private RequestDispatcher rd = null;
	private HttpSession sesion = null;
	private SolicitudDao solicitudDao = new SolicitudDaoImpl();
	private PeriodoEscolarDao periodoEscolarDao = new PeriodoEscolarDaoImpl();
	private TipoEmpleadoDao tipoEmpleadoDao = new TipoEmpleadoDaoImpl();
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

		ResourceBundle rb = ResourceBundle
				.getBundle("ve.org.bcv.rhei.util.bundle");
		if (request.getSession(false) == null) {
			this.sesion = request.getSession(true);
			this.sesion.setAttribute("aviso", this.aviso);
			this.rd = request.getRequestDispatcher("/jsp/login.jsp");
			this.rd.forward(request, response);
		} else {
			RequestDispatcher despachador = null;
			
			ShowResultToView showResultToView = null; 	
			showResultToView=request.getAttribute("showResultToView")!=null?(ShowResultToView)request.getAttribute("showResultToView"):null;
			if (showResultToView==null){
				showResultToView = new ShowResultToView();
			}
			
		
			List<ValorNombre> stadosSolicitudLst = null;
			try {
				stadosSolicitudLst = solicitudDao.estadosSolicitudLst();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			showResultToView.setEstadosSolicitudLst(stadosSolicitudLst);
			request.setAttribute("showResultToView", showResultToView);

			List<ValorNombre> statusLst = null;
			statusLst = new ArrayList<ValorNombre>();
			ValorNombre valorNombre = new ValorNombre(
					Constantes.CO_STATUS_ACTIVO, rb.getString("activo"));
			statusLst.add(valorNombre);
			valorNombre = new ValorNombre(Constantes.CO_STATUS_DESINCORPORADO,
					rb.getString("desincorporado"));
			statusLst.add(valorNombre);
			request.setAttribute("statusLst", statusLst);

		//	if ("reportConsulta".equals(request.getParameter("principal.do"))) {

				/** Buscamos los tipos de empleados */
				List<ValorNombre> tipoEmpleados = new ArrayList<ValorNombre>();
				try {
					List<TipoEmpleado> tipoEmpleadosBd = tipoEmpleadoDao
							.tipoEmpleadosList();
					valorNombre = null;
					for (TipoEmpleado te : tipoEmpleadosBd) {
						valorNombre = new ValorNombre(te.getTipoEmp(),
								te.getDescripcion());
						tipoEmpleados.add(valorNombre);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.setAttribute("tipoEmpleados", tipoEmpleados);

				/** Buscamos los conceptos de pago */
				List<ValorNombre> receptorPagos = new ArrayList<ValorNombre>();
				valorNombre = null;
				valorNombre = new ValorNombre("0", rb.getString("cei"));
				receptorPagos.add(valorNombre);
				valorNombre = new ValorNombre("1", rb.getString("trabajador"));
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

				/************** MESES ********/
				MesesBean mesesBean = new MesesBean();
				String[] mes = mesesBean.getMes();
				String[] meses = mesesBean.getMeses();

				List<ValorNombre> listadoMeses = new ArrayList<ValorNombre>();
				valorNombre = null;
				for (int j = 0; j <= 12; j++) {
					valorNombre = new ValorNombre(meses[j], mes[j]);
					listadoMeses.add(valorNombre);
				}
				Collections.sort(listadoMeses, new SortByValueComparatorAsc());
				request.setAttribute("listadoMeses", listadoMeses);

				/*********** FIN MESES ***********/

				String pagIr = "";
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

				/*****
				 * End******verificamos si viene de reporte definitiovo
				 * transitorio
				 ****/
				if (request.getParameter("vieneFromReporteByPagoAndTributo") != null) {
					String vieneFromReporteByPagoAndTributo = (String) request
							.getParameter("vieneFromReporteByPagoAndTributo");
					if ("1".equalsIgnoreCase(vieneFromReporteByPagoAndTributo)) {
						request.setAttribute(
								"vieneFromReporteByPagoAndTributo",
								vieneFromReporteByPagoAndTributo);
						String nombreDefinitivoTransitorio = request
								.getParameter("nombreDefinitivoTransitorio") != null
								? (String) request
										.getParameter("nombreDefinitivoTransitorio")
								: null;
						request.setAttribute("nombreDefinitivoTransitorio",
								nombreDefinitivoTransitorio);
					}
					/**
					 * Fin*********verificamos si viene de reporte definitiovo
					 * transitorio
					 ****/
				}
				request.setAttribute("showResultToView", showResultToView);
				pagIr = reportePagina;
				despachador = request.getRequestDispatcher(pagIr);
				despachador.forward(request, response);
			 
			/********* FIN ACTUALIZAR PAGO CONVENCIONAL ********************************/
		}

	}

}
