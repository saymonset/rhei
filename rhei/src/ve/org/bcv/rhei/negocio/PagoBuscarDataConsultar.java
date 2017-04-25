package ve.org.bcv.rhei.negocio;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.bean.ValorNombre;
import ve.org.bcv.rhei.report.by.benef.BeneficiarioBean2;
import ve.org.bcv.rhei.util.Constantes;

import com.bcv.comparator.SortByValueComparatorAsc;
import com.bcv.dao.jdbc.FacturaDao;
import com.bcv.dao.jdbc.PeriodoEscolarDao;
import com.bcv.dao.jdbc.RelacionDePagosDao;
import com.bcv.dao.jdbc.SolicitudDao;
import com.bcv.dao.jdbc.impl.FacturaDaoImpl;
import com.bcv.dao.jdbc.impl.PeriodoEscolarDaoImpl;
import com.bcv.dao.jdbc.impl.RelacionDePagosDaoImpl;
import com.bcv.dao.jdbc.impl.SolicitudDaoImpl;
import com.bcv.model.Factura;
import com.bcv.model.Solicitud;
import com.enums.Mes;

/**
 * OBTENEMOS EL PAGO Y FACTURA
 * 
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com 13/03/2015 16:04:51
 * 
 */
public class PagoBuscarDataConsultar extends SolAction implements Serializable {
	private FacturaDao facturaDao = new FacturaDaoImpl();
	private RelacionDePagosDao relacionDePagosDao = new RelacionDePagosDaoImpl();
	private SolicitudDao solicitudDao = new SolicitudDaoImpl();
	private static Logger log = Logger.getLogger(PagoBuscarDataConsultar.class
			.getName());
	private PeriodoEscolarDao periodoEscolarDao = new PeriodoEscolarDaoImpl();

	public PagoBuscarDataConsultar(HttpServletRequest request,
			HttpServletResponse response, ShowResultToView showResultToView,
			ServletContext sc) throws ServletException, IOException {
		super(request, response, showResultToView, sc, Constantes.PAGOCONSULTAR);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ShowResultToView ejecutar() throws ServletException, IOException {
		ResourceBundle rb = ResourceBundle.getBundle("ve.org.bcv.rhei.util.bundle");
		String nroFactura=null;
		int nroSolicitud=getShowResultToView().getNroSolicitud();
		/**Viene por nro Factura*/
		if (!StringUtils.isEmpty(getShowResultToView().getNroFactura())){
			nroFactura=getShowResultToView().getNroFactura();
		}
		else{
			/**Viene por cedula, cedula famuiliar , mes y año periodo*/
			/** Carga solicitud I */
			try {
				if (!StringUtils.isEmpty(getShowResultToView().getCodEmp()) && !StringUtils.isEmpty(getShowResultToView().getCodigoBenef()) 
						 && !StringUtils.isEmpty(getShowResultToView().getPeriodoEscolar())){
					
					Solicitud solicitud = solicitudDao.solicitudBylastCodEmpAndFamily(							
							new Integer(getShowResultToView().getCodEmp()),
							new Integer(getShowResultToView().getCodigoBenef()),getShowResultToView().getPeriodoEscolar());
					nroSolicitud=solicitud.getNroSolicitud();
					nroFactura=facturaDao.buscarFacturaByMesNroSolictud(solicitud.getNroSolicitud(),getShowResultToView().getPeriodoEscolar());
				}
			} catch (SQLException e) {
				 nroFactura=null;
				e.printStackTrace();
			}
			
			
		}
		/**showResultToViewToReportLst usada para reportes*/
		List<BeneficiarioBean2> reporteMatriculaPeriodoLst= new ArrayList<BeneficiarioBean2>();
		try {
			
			/**Si solo vamos a buscar por factura*/
			/**CARGAMOS FACTURA*/
			if (!StringUtils.isEmpty(nroFactura)){
				getShowResultToView().setNroFactura(nroFactura);
				Factura factura = new Factura();	
				factura.setNroFactura(nroFactura);
				if (!StringUtils.isEmpty(nroFactura) && nroSolicitud>0){
					factura=facturaDao.consultarFactura(factura.getNroFactura(),nroSolicitud,getShowResultToView().getPeriodoEscolar());
				}
				
				
				/**--------------Obtenemos Relacion Pagos----------------------------*/
				/** En la relacion de pago tenemos el numero de solicitud */
				if (factura!=null && factura.getNroIdFactura()>0){
					reporteMatriculaPeriodoLst=relacionDePagosDao.obtenerRelacionPagoYfactura(
							nroSolicitud,factura.getNroIdFactura(), 
							getShowResultToView().getPeriodoEscolar(), getShowResultToView());
					/**showResultToViewToReportLst usada para reportes*/
					getShowResultToView().setReporteMatriculaPeriodoLst(reporteMatriculaPeriodoLst);
					
					if (!"1".equals(getShowResultToView().getError())){
						/**Obtenemos la solicitud por numero de solicitud encontrada*/
					
						Solicitud solicitud=solicitudDao.cedulaEmplYOtrosSolicitud(getShowResultToView().getNroSolicitud())  ;
						if (solicitud!=null && solicitud.getNroSolicitud()>0){
							getShowResultToView().setCodEmp(solicitud.getCodigoEmpleado()+"");
							getShowResultToView().setCodigoBenef(solicitud.getCedulaFamiliar()+"");
							getShowResultToView().setCedula(solicitud.getCedula()+"");
							getShowResultToView().setNroRifCentroEdu(solicitud.getNroRifCentroEdu()
								 );
							
							/**Si existe una solicitud activa , la cargamos 
							 * con el numero de solicitud que acabamos de encontrar*/
							solicitud =solicitudDao.consultarSolicitud( solicitud.getNroSolicitud()); 
							if (solicitud==null ) {
								getShowResultToView().setError(Constantes.NO_HAY_DATA);
								getShowResultToView().setViene(null);
								enviarMsgError(getShowResultToView());
								return getShowResultToView();
							}else{
								super.cargarRestoInfo(solicitud,getShowResultToView());
								/**Consultamos solicitud fin*/
								/**************************************************/
								
								/** Si no hay data en la bd, inicializamos valores por defecto */
								if (StringUtils.isEmpty(getShowResultToView().getFechaRegistro())) {
									getShowResultToView()
											.setFechaRegistro(getShowResultToView().getFechaActual());
								}
								if (StringUtils.isEmpty(getShowResultToView().getNroFactura())) {
									getShowResultToView().setNroFactura(Constantes.PENDIENTE_ENTREGAF);
								}
								if (StringUtils.isEmpty(getShowResultToView().getNroControl())) {
									getShowResultToView().setNroControl(Constantes.PENDIENTE_ENTREGAC);
								}
								
									/**Carga Relacion de pago y Factura II*/
									super.cargaMesesMatriculaConceptoDePago( getRequest(),
											getShowResultToView(),solicitud.getNroSolicitud(),getShowResultToView().getPeriodoEscolar());
									
								
									
									/** por referencia cargamos data en getShowResultToView*/
									super.searchCentroEducativo(getShowResultToView());
									/**Buscamos datos que no se pudieron cargar en el constructor pdre pomn no tener data iniciales como cedula empleado*/
									super.cargaTrabajadorBeneficiarioCentroEdu(super.getRequest(),
											super.getResponse(), super.getShowResultToView(),super.getSc(),Constantes.PAGOCONSULTAR);
									boolean isPeriodo=false;
									boolean isMatricula=false;
									/**Buscaremos todos los meses pagados por una factura*/
									try {
										
										//List<Factura> mesesByFacturas= new ArrayList<Factura>();
										List<ValorNombre> listadoMesPagByFact= new ArrayList<ValorNombre>();
										List<ValorNombre> listadoMesPagByFactOnlyComplemento= new ArrayList<ValorNombre>();
										if (factura!=null){
											
											listadoMesPagByFact= noComplementos(factura);
											listadoMesPagByFactOnlyComplemento= complementos(factura);
										 
										}
										
										
										
										Collections.sort(listadoMesPagByFact,new SortByValueComparatorAsc());
										getShowResultToView().setListadoMesPagByFact(listadoMesPagByFact);
										
										Collections.sort(listadoMesPagByFactOnlyComplemento,new SortByValueComparatorAsc());
										getShowResultToView().setListadoMesPagByFactOnlyComplemento(listadoMesPagByFactOnlyComplemento);
										/**Buscaremos fin todos los meses pagados por una factura*/	
										
										 
										/**Buscamos datos que no se pudieron cargar en el constructor pdre pomn no tener data iniciales como cedula empleado*/
										super.cargaTrabajadorBeneficiarioCentroEdu(super.getRequest(),
												super.getResponse(), super.getShowResultToView(),super.getSc(),Constantes.PAGOCONSULTAR);
										/**Buscamos los periodos escolares*/
										getShowResultToView().setPeriodoEscolares(periodoEscolarDao.tipoPeriodosEscolares());
											/**Buscamos Fin los periodos escolares*/
										 /**Buscamos los meses*/
										getShowResultToView().setMeses(super.meses());
										/**SI ES MENOS 14 (-14) IMPLICA QUWE LA MATRICULA FUE PAGADA FUERA DELS SISTEMA*/
										/**SE MULTIPLICA POR MENOS UNO PARA OBTENER EL VALOR DE MATRICULA QUE NO A SIDO PAGADA FUERA DEL SISTEMA*/
										ValorNombre valorNombre = new ValorNombre ((Constantes.MATRICULA_PAGADA*-1)+"",Constantes.MATRICULA);
										/**Cero es la primera posicion del select */
										getShowResultToView().getMeses().add(new Integer(Constantes.CERO),valorNombre);
										 /**Fin Buscamos los meses*/
										/**Buscamos el nombre del mes a traves del enum
										 * Esto si lo traenmos filrtrado de la vista
										 * */
										log.info("getShowResultToView().getInMesMatricula()="+getShowResultToView().getInMesMatricula());
											getShowResultToView().setMes(super.obtenerNameMes(getShowResultToView().getInMesMatricula()));
											if (!StringUtils.isEmpty(getShowResultToView().getMes())){
												getShowResultToView().setInMesMatricula(super.obtenerIdMes(getShowResultToView().getMes()));	
											}
											getShowResultToView().setCheckedMatricula(isMatricula?Constantes.CHECKED:"");
											getShowResultToView().setCheckedPeriodo(isPeriodo?Constantes.CHECKED:"");
									 
											
								 
											
											
										/**Fin Buscamos el nombre del mes a traves del enum*/
										getShowResultToView().setAccion(Constantes.PAGOCONSULTAR);
										getShowResultToView().setDisabled(Constantes.DISABLED);
										getShowResultToView().setDisabledRegPagos(Constantes.DISABLED);
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
							}
							
						
					}else{
						/**Consultamos solicitud fin*/
						/**************************************************/
						enviarMsgError(getShowResultToView());
					} 
					}else{
						/**Consultamos solicitud fin*/
						/**************************************************/
						enviarMsgError(getShowResultToView());
					}
				}else{
					/**Consultamos solicitud fin*/
					/**************************************************/
					enviarMsgError(getShowResultToView());
				}
				/**Enc--------------Fin Obtenemos Relacion Pagos----------------------------*/
			}
			/**END CARGAMOS FACTURA*/
			/** CARGAMOS LA SOLICITUD */
 
				if (!"1".equalsIgnoreCase(getShowResultToView().getViene())){
  
					enviarMsgError(getShowResultToView());
				}
				
				
				
		//		}
			/**END  CARGAMOS LA SOLICITUD */
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return getShowResultToView();
	}
	public List<ValorNombre> noComplementos(Factura factura)
			throws SQLException {
		List<ValorNombre> listadoMesPagByFact = new ArrayList<ValorNombre>();
		// List<ValorNombre> listadoMesPagByFactOnlyComplemento= new
		// ArrayList<ValorNombre>();
		boolean isPeriodo = false;
		boolean isMatricula = false;
		List<Factura> mesesByFacturas = mesesByFacturas = facturaDao
				.mesesPorNroFactura(factura.getNroFactura(),
						Constantes.NO_IN_COMPLEMENTO);
		if (mesesByFacturas != null && mesesByFacturas.size() > 0) {
			ValorNombre valorNombre = null;
			// Map<Integer,Integer> unicaFactura = new
			// HashMap<Integer,Integer>();
			int siEsNegativoEspagado = 0; //
			DecimalFormat df2 = new DecimalFormat("#,##0.00");
			String montoPagoAdicional = "";
			for (Factura fact : mesesByFacturas) {
				if (Math.abs(fact.getNuRefPago()) == Math
						.abs(Constantes.MATRICULA_PAGADA)
						 ) {
					isMatricula = true;
				} else {
					isPeriodo = true;
				}

				try {
					montoPagoAdicional = df2.format(fact.getMoTotalPago());
				} catch (Exception e) {
					montoPagoAdicional = fact.getMoTotalPago() + "";
					e.printStackTrace();
				}

				siEsNegativoEspagado = fact.getNuRefPago();
				String corcheteAbre = "";
				String corcheteCierre = "";
				if (siEsNegativoEspagado < 0) {
					/** Si es negativo es porque ya esta pago ************/
					corcheteAbre = "[ - ";
					corcheteCierre = " ] ";
				}
				/***** Chequear estos comentariios **********/
				if (fact.getTxConceptoPago() == null) {
					fact.setTxConceptoPago("");
				}
				/** Si es complemento, lleva una S */
				/***** FIN Chequear estos comentariios **********/
					/***
					 * NU_SOLICITUD, NU_REF_PAGO , IN_COMPLEMENTO es la clave
					 * para eliminar referencia de pago
					 **/
					valorNombre = new ValorNombre(fact.getNuSolicitud() + ","
							+ fact.getNroIdFactura() + ","
							+ fact.getNuRefPago() + ","
							+ fact.getInComplemento(), corcheteAbre
							+ super.obtenerNameMes(fact.getNuRefPago())
							+ corcheteCierre);
					listadoMesPagByFact.add(valorNombre);// listadoMesPagByFactOnlyComplemento.add(valorNombre);
			}
		}

		factura = null;
		return listadoMesPagByFact;

	}

	public List<ValorNombre> complementos(Factura factura) throws SQLException {
		// List<ValorNombre> listadoMesPagByFact= new ArrayList<ValorNombre>();
		List<ValorNombre> listadoMesPagByFactOnlyComplemento = new ArrayList<ValorNombre>();
		List<Factura> mesesByFacturas = mesesByFacturas = facturaDao
				.mesesPorNroFactura(factura.getNroFactura(),
						Constantes.IN_COMPLEMENTO);
		if (mesesByFacturas != null && mesesByFacturas.size() > 0) {
			ValorNombre valorNombre = null;
			int siEsNegativoEspagado = 0; //
			DecimalFormat df2 = new DecimalFormat("#,##0.00");
			String montoPagoAdicional = "";
			for (Factura fact : mesesByFacturas) {

				try {
					montoPagoAdicional = df2.format(fact.getMoTotalPago());
				} catch (Exception e) {
					montoPagoAdicional = fact.getMoTotalPago() + "";
					e.printStackTrace();
				}

				siEsNegativoEspagado = fact.getNuRefPago();
				String corcheteAbre = "";
				String corcheteCierre = "";
				if (siEsNegativoEspagado < 0) {
					/** Si es negativo es porque ya esta pago ************/
					corcheteAbre = "[ - ";
					corcheteCierre = " ] ";
				}
				/***** Chequear estos comentariios **********/
				if (fact.getTxConceptoPago() == null) {
					fact.setTxConceptoPago("");
				}

				/***
				 * NU_SOLICITUD, NU_REF_PAGO , IN_COMPLEMENTO es la clave para
				 * eliminar referencia de pago
				 **/

				valorNombre = new ValorNombre(fact.getNuSolicitud() + ","
						+ fact.getNroIdFactura() + "," + fact.getNuRefPago()
						+ "," + fact.getInComplemento(), corcheteAbre
						+ super.obtenerNameMes(Math.abs(fact.getNuRefPago()))
						+ " " + (String) fact.getTxConceptoPago().trim()
						+ corcheteCierre);

				listadoMesPagByFactOnlyComplemento.add(valorNombre);

			}
		}

		factura = null;
		return listadoMesPagByFactOnlyComplemento;

	}

	private void enviarMsgError(ShowResultToView showResultToView) {
		showResultToView.setError(Constantes.NO_HAY_DATA);
		showResultToView.setViene(null);
		showResultToView.setMensaje("fracaso3");

	}

}