package ve.org.bcv.rhei.negocio;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.util.Constantes;

import com.bcv.dao.jdbc.FacturaDao;
import com.bcv.dao.jdbc.PeriodoEscolarDao;
import com.bcv.dao.jdbc.RelacionDePagosDao;
import com.bcv.dao.jdbc.impl.FacturaDaoImpl;
import com.bcv.dao.jdbc.impl.PeriodoEscolarDaoImpl;
import com.bcv.dao.jdbc.impl.RelacionDePagosDaoImpl;
import com.bcv.model.Factura;
import com.bcv.model.PeriodoEscolar;
import com.bcv.model.RelacionDePagos;

/**
 * Actualizamos un pago
 * 
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com 18/03/2015 14:03:26
 * 
 */
public class PagoActualizar extends SolAction implements Serializable {
	private static Logger log = Logger.getLogger(PagoPagarII.class.getName());
	private FacturaDao facturaDao= new FacturaDaoImpl();
	private RelacionDePagosDao relacionDePagosDao= new RelacionDePagosDaoImpl();
	private PeriodoEscolarDao periodoEscolarDao = new PeriodoEscolarDaoImpl();
	private String deleteNuSolicitudNuFactNuRefPagoInComplemento;

	public PagoActualizar(HttpServletRequest request,
			HttpServletResponse response, ShowResultToView showResultToView,
			ServletContext sc) throws ServletException, IOException {
		super(request, response, showResultToView, sc,
				Constantes.PAGOACTUALIZAR);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ShowResultToView ejecutar() throws ServletException, IOException {
 
		int exito = -1;
		try {
 		        	this.deleteNuSolicitudNuFactNuRefPagoInComplemento =getRequest().getParameter("keyNuSolicitudNuFactNuRefPagoInComplemento")!=null?(String)getRequest().getParameter("keyNuSolicitudNuFactNuRefPagoInComplemento"):null;
					if (!StringUtils.isEmpty(this.deleteNuSolicitudNuFactNuRefPagoInComplemento)){
						exito= deleteNuSolicitudNuFactNuRefPagoInComplemento(this.deleteNuSolicitudNuFactNuRefPagoInComplemento);	
					}
					 /**Buscamos los datos en bd nuevamente para mostrarlos por pantalla */
					
						SolAction solAction = new PagoBuscarToActualizar(
								getRequest(), getResponse(),
								getShowResultToView(), this.getSc());
						solAction.ejecutar();
						if (exito >=0){
							 getShowResultToView().setMensaje("exito");
						}else{
							 getShowResultToView().setMensaje("fracaso3");
						}
					getShowResultToView().setAccion(Constantes.PAGOACTUALIZAR);

					getShowResultToView().setDisabled(Constantes.DISABLED);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			 
		}
		return getShowResultToView();
	}
	 
	
	private int deleteNuSolicitudNuFactNuRefPagoInComplemento(String nuIdFacturaStr) throws SQLException{
		
 
		
		 int exito = 0;
		  int nuIdFactura=0;
		  if (!StringUtils.isEmpty(nuIdFacturaStr)){
			  nuIdFactura= Integer.parseInt(nuIdFacturaStr);
		  }
		 try {
			 exito= relacionDePagosDao.deleteFacturaByRelacionPagoPago(  nuIdFactura);
			 facturaDao.deleteFactura(nuIdFactura);		
		} catch (Exception e) {
		}
	
					
		
	//	  int exito = facturaDao.updateFactura(factura.getMontoFactura(),factura.getNroFactura(),factura.getNroControl(),factura.getNroIdFactura());
			if (exito >= 0) {
				//exito = relacionDePagosDao.updatePago(pago.getMontoTotal(),pago.getNroIdFactura(),pago.getNroSolicitud(),pago.getReceptorPago(),pago.getInMesMatricula(),formaPago);
				if (exito >= 0) {
				}else {
					exito = -1;
				}	
			} else {
				exito = -1;
			}
			return exito; 
	}
	

	/**
	 * @param codEmplado
	 * @param codigoBenef
	 * @param request
	 * @param showResultToView
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
//	public ShowResultToView pagoConvencionalPagar(String codEmplado,
//			String codigoBenef, HttpServletRequest request,
//			ShowResultToView showResultToView) throws ServletException,
//			IOException {
//		String viene = "1";
//		String accion = "";
//		String flag = "false";
//		String nroFactura = "";
//		String periodo = "";
//		int tipoPago = 0;
//		int receptorPago = -1;
//		int ced = 0;
//		int codBenef = 0;
//		String mensaje = "";
//
//		Factura factura = new Factura();
//		RelacionDePagos pago = new RelacionDePagos();
//		/**Guardamos meses de los complementos ys us montos individuales*/
//	    Map<String,Double> mesesPorComplIndividualMonto = new HashMap<String,Double>();
//	 
//		try {
//			
//			String showCheckCompklement = request.getParameter("showCheckCompklement")!=null?(String)request.getParameter("showCheckCompklement"):null;
//			String[] mesecomplemento = request.getParameterValues("mesecomplemento");
//		 
//			if (!StringUtils.isEmpty(showResultToView.getCedula())) {
//				ced = new Integer(showResultToView.getCedula());
//			}
//			if (!StringUtils.isEmpty(showResultToView.getCodigoBenef())) {
//				codBenef = new Integer(showResultToView.getCodigoBenef());
//			}
//			/** Viene por Hidden .. valor 1 en este caso si es convencional */
//			if (!StringUtils.isEmpty(request.getParameter("tipoPago"))
//					&& StringUtils.isNumeric(request.getParameter("tipoPago"))) {
//				tipoPago = Integer.parseInt(request.getParameter("tipoPago"));
//			}
//			
//			String isPagado = request.getParameter("pagado")==null?"":(String)request.getParameter("pagado");
//
//			/** Nro Factura */
//			log.debug("nroFactura: " + request.getParameter("nroFactura"));
//			factura.setNroFactura(request.getParameter("nroFactura"));
//			log.debug("nro de factura: " + factura.getNroFactura());
//
//			/** Fecha Factura */
//			log.debug("fechaFactura: " + request.getParameter("fechaFactura"));
//			factura.setFechaFactura(request.getParameter("fechaFactura"));
//			log.debug("fecha factura: " + factura.getFechaFactura());
//
//			/** nroRif */
//			log.debug("nroRif: " + request.getParameter("nroRif"));
//			factura.setNroRifProv(request.getParameter("nroRif"));
//			log.debug("nro de rif: " + factura.getNroRifProv());
//
//			/** nro de control */
//			log.debug("nro de control: " + request.getParameter("nroControl"));
//			if (request.getParameter("nroControl").equals("")) {
//				factura.setNroControl("");
//			} else {
//				factura.setNroControl(request.getParameter("nroControl"));
//			}
//
//			log.debug("nro de control: " + factura.getNroControl());
//
//			/** monto factura */
//			log.debug("monto factura: " + request.getParameter("montoFactura"));
//			factura.setMontoFactura(Double.valueOf(Double.parseDouble(request
//					.getParameter("montoFactura").toString().replace(".", "")
//					.replace(",", "."))));
//
//			log.debug("monto factura: " + factura.getMontoFactura());
//
//			log.debug("nro de solicitud: "
//					+ request.getParameter("nroSolicitud"));
//			pago.setNroSolicitud(Integer.parseInt(request
//					.getParameter("nroSolicitud")));
//
//			log.debug("nro de solicitud: " + pago.getNroSolicitud());
//
//			/** Estamos pagando los meses ------------ */
//			//int conceptoPago = -1;
//			 List<Integer> mesMatriMenArray = new ArrayList<Integer>();
//			 List<Integer>  conceptoPagoArray = new ArrayList<Integer>();
//			String[] mesesPorPagar = (String[]) null;
//			if (request.getParameterValues("mesesPorPagar") != null) {
//				mesesPorPagar = request.getParameterValues("mesesPorPagar");
//				
//				
//				
//				
//				
//				
//				  /**Guardamos meses de los complementos ys us montos individuales*/
//			    
//				if (mesesPorPagar==null || mesesPorPagar.length==0){
//					if (mesecomplemento!=null){
//						/**Aqui analizaremos todos lso meses complementos*/
//						Enumeration<String> parameterNames = request.getParameterNames();
//						while (parameterNames.hasMoreElements()) {
//							String paramName = parameterNames.nextElement();
//							if ( super.obtenerIdMes(paramName)>0){
//								 System.out.println(paramName+"=paramName");
//									String[] paramValues = request.getParameterValues(paramName);
//									for (int i = 0; i < paramValues.length; i++) {
//										String paramValue = paramValues[i];
//										if (!StringUtils.isEmpty(paramValue)){
//											Double montIndividual=Double.valueOf(Double.parseDouble(paramValue.toString().replace(".", "")
//													.replace(",", ".")));
//											mesesPorComplIndividualMonto.put(super.obtenerIdMes(paramName)+"", montIndividual);
//										}
//									 
//									}	
//							}
//							
//
//						}
//						
//						
//							mesesPorPagar=mesecomplemento;
//					}
//				}
//				
//			 
//				
//				int valorMatricOrMes = 0;
//				boolean isPrimeraVez=false;
//				for (int j = 0; j < mesesPorPagar.length; j++) {
//					valorMatricOrMes = Integer.parseInt(mesesPorPagar[j]);
//					mesMatriMenArray.add(valorMatricOrMes);
//					     /**conceptoPago Matricula*/
//					/**matricula pagada es negativo por ser ya pagada, se multiplica por menos uno para que de positivo la matricula pagada*/
//					if (valorMatricOrMes==(Constantes.MATRICULA_PAGADA*-1)){
//						/**Concepto de pago para matricula es cero*/
//						conceptoPagoArray.add(0);
//						/**conceptoPago Mensualidad*/
//					}else if ((valorMatricOrMes>0 && (valorMatricOrMes!=(Constantes.MATRICULA_PAGADA*-1))) && !isPrimeraVez){
//						isPrimeraVez=true;
//						conceptoPagoArray.add(1);
//					}
//				}
//				pago.setMesMatriMenArray(mesMatriMenArray);
//			}  
//			
//			pago.setConceptoPagoArray(conceptoPagoArray);
//
//			 
//			/** FIN Estamos pagando los meses ------------ */
//
//			pago.setTramite(request.getParameter("estatusPago"));
//
//			SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
//			java.util.Date dateObject = dateFormatter.parse(request
//					.getParameter("fechaRegistro"));
//			pago.setFechaRegistroDePago(new java.sql.Timestamp(dateObject
//					.getTime()));
//
//			
//			
//			PeriodoEscolar periodoEscolar = new PeriodoEscolar();
//			periodoEscolar.setDescripcion(request.getParameter("periodoEscolar"));
//			if (StringUtils.isEmpty(periodoEscolar.getDescripcion())){
//				periodoEscolar.setDescripcion(request.getParameter("periodoEscolarAux"));
//			}
//			try {
//				periodoEscolar = periodoEscolarDao.findPeriodoByDescripcion(periodoEscolar.getDescripcion());
//				if (periodoEscolar!=null){
//					pago.setCodigoPeriodo( Integer.parseInt(periodoEscolar.getCodigoPeriodo().toString()));
//					 
//				}
//			} catch (SQLException e) {
//				pago.setCodigoPeriodo(0);
//			}
//			pago.setReceptorPago(Integer.parseInt(request.getParameter(
//					"receptorPago").toString()));
//
//			Double montoMatriculaBCV = Double.valueOf(0.0D);
//			Double montoPeriodoBCV = Double.valueOf(0.0D);
//
//			String observaciones = "";
//
//			pago.setMontoBCV(Double.valueOf(Double.parseDouble(request
//					.getParameter("mo_bcv"))));
//			pago.setMontoMatricula(Double.valueOf(Double.parseDouble(request
//					.getParameter("mo_matricula"))));
//			pago.setMontoPeriodo(Double.valueOf(Double.parseDouble(request
//					.getParameter("mo_periodo"))));
//			pago.setBeneficioCompartido(request
//					.getParameter("beneficioCompartido"));
//			log.debug("beneficio compartido? : "
//					+ pago.getBeneficioCompartido());
//
//			montoMatriculaBCV = relacionDePagosDao.calcularMontoPagoBCV(pago.getMontoBCV(),
//					pago.getBeneficioCompartido(), pago.getMontoMatricula());
//			montoPeriodoBCV = relacionDePagosDao.calcularMontoPagoBCV(pago.getMontoBCV(),
//					pago.getBeneficioCompartido(), pago.getMontoPeriodo());
//
//			int mes = -1;
//			mes = Integer.parseInt(pago.getFechaRegistroDePago().toString()
//					.substring(5, 7));
//pago.setCoFormaPago(request.getParameter("formaPago")==null?"":(String)request.getParameter("formaPago"));
//			/** GUARDAMOS PAGO */
//				mensaje = relacionDePagosDao.guardarPago( factura,
//						montoMatriculaBCV, montoPeriodoBCV, tipoPago,
//						observaciones,pago.getMesMatriMenArray(),pago.getNroSolicitud(),pago.getConceptoPagoArray(),pago.getReceptorPago(),pago.getTramite(),pago.getCodigoPeriodo(),pago.getCoFormaPago()
//						,isPagado,  showCheckCompklement,   mesecomplemento,mesesPorComplIndividualMonto);
//				if (mensaje.equals("Exito")) {
//					mensaje = "Los datos asociados al pago han sido guardados exitosamente ";
//					showResultToView.setMensaje(mensaje);
//					showResultToView.setMensajeExito("exito");
//				} else {
//					mensaje = "No se pudo guardar los datos del pago";
//					showResultToView.setMensaje(mensaje);
//				}
//	 
//		} catch (Exception e) {
//			log.error(e.toString());
//			;
//		} finally {
//		 
//		}
//
//		return showResultToView;
//	}

	public String getDeleteNuSolicitudNuFactNuRefPagoInComplemento() {
		return deleteNuSolicitudNuFactNuRefPagoInComplemento;
	}

	public void setDeleteNuSolicitudNuFactNuRefPagoInComplemento(
			String deleteNuSolicitudNuFactNuRefPagoInComplemento) {
		this.deleteNuSolicitudNuFactNuRefPagoInComplemento = deleteNuSolicitudNuFactNuRefPagoInComplemento;
	}

}