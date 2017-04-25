/**
 */
package com.bcv.reporte.relacionpago;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ve.org.bcv.rhei.bean.DocumentosBean;
import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.negocio.SolAction;
import ve.org.bcv.rhei.report.by.benef.Reporte;
import ve.org.bcv.rhei.util.Constantes;

import com.bcv.comparator.SortProveedorRpago1BeanNumRifAsc;
import com.bcv.dao.jdbc.FacturaDao;
import com.bcv.dao.jdbc.RelacionDePagosDao;
import com.bcv.dao.jdbc.SolicitudDao;
import com.bcv.dao.jdbc.impl.FacturaDaoImpl;
import com.bcv.dao.jdbc.impl.FamiliarDaoImpl;
import com.bcv.dao.jdbc.impl.RelacionDePagosDaoImpl;
import com.bcv.dao.jdbc.impl.SolicitudDaoImpl;
import com.bcv.dto.FacturaMontoMesHistorico;
import com.bcv.model.Factura;
import com.bcv.model.Familiar;
import com.bcv.model.Solicitud;
import com.enums.Mes;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco 06/07/2015 14:03:30 2015 mail :
 *         oraclefedora@gmail.com
 */
public class ReporteByRpago extends SolAction implements Reporte, Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger
			.getLogger(ReporteByRpago.class.getName());
	/**
	 * 
	 */
    private String ninoEspecial;
	private String descripPeriodo;
	private String companiaAnalista;
	private int receptorPago = -1;// 0 CENTRO DE EDUCACION INICIAL, 1 TRABAJADOR
	private int coFormaPago = -1; // 1 ES CHEQUE, 2 ES AVISO DE CREDITO
	private DocumentosBean documentosBean = new DocumentosBean();
	private String numSolicituds;
	private String coStatus = "A";
	private RelacionDePagosDao relacionDePagosDao = new RelacionDePagosDaoImpl();
	private FamiliarDaoImpl familiarDao = new FamiliarDaoImpl();
	private FacturaDao facturaDao = new FacturaDaoImpl();
	private String nuFactura;
	private String tipoEmpleado;
	private String meses;
	private String filtrarByMesOrComplementoOrAmbos;
	private int coRepStatus;
	private String isToCreateReportDefinitivo;
	private SolicitudDao solicitudDao;
	

	public ReporteByRpago() {

	}

	public ReporteByRpago(HttpServletRequest request,
			HttpServletResponse response, ShowResultToView showResultToView,
			ServletContext sc) throws ServletException, IOException {
		super(request, response, showResultToView, sc, Constantes.REPORTE_BENEF);

		if (!StringUtils.isEmpty(super.getShowResultToView()
				.getPeriodoEscolar())) {
			this.descripPeriodo = super.getShowResultToView()
					.getPeriodoEscolar();
		}

		companiaAnalista = request.getSession()
				.getAttribute("companiaAnalista") != null ? (String) request
				.getSession().getAttribute("companiaAnalista") : "01";

	}

	/**
	 * Buscamos empleado por cedula, el empleado debe tener por lo menois una
	 * solicitud
	 * 
	 * @return
	 */
	public List<ProveedorRpago1Bean> searchObjects() {

		List<ProveedorRpago1Bean> proveedorRpago1BeanLst = new ArrayList<ProveedorRpago1Bean>();
		try {
			List<ProveedorRpago1Bean> proveedorRpago1BeanLstBd = relacionDePagosDao
					.searchSolicitudesToReporte(this.coFormaPago,
							this.receptorPago, this.descripPeriodo,
							this.numSolicituds, this.coStatus,
							this.tipoEmpleado, this.isToCreateReportDefinitivo,this.ninoEspecial);

			for (ProveedorRpago1Bean prb : proveedorRpago1BeanLstBd) {
				prb.setFamiliares(searchFamiliares(prb, coRepStatus));
				proveedorRpago1BeanLst.add(prb);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		/*** Ordenamnospor colegio **/
		if (proveedorRpago1BeanLst != null && !proveedorRpago1BeanLst.isEmpty()) {
			Collections.sort(proveedorRpago1BeanLst,
					new SortProveedorRpago1BeanNumRifAsc());
		}

		return proveedorRpago1BeanLst;
	}

	/**
	 * numSolicituds is a String large . it is contain values of numSolictud
	 * Consult example 1,2,3,4,,598.201,333 Metdo que me dara reportes por
	 * reembolso o complemento
	 * 
	 * @param descripPeriodo
	 * @param companiaAnalista
	 * @param receptorPago
	 */
	public ReporteByRpago(String descripPeriodo, String companiaAnalista,
			int receptorPago, int checkOrAvisoCredito, String numSolicituds,
			String coStatus, String tipoEmpleado, String meses,
			String filtrarByMesOrComplementoOrAmbos, int coRepStatus,
			String isToCreateReportDefinitivo,String ninoEspecial) {
		this.ninoEspecial=ninoEspecial;
		this.descripPeriodo = descripPeriodo;
		this.companiaAnalista = companiaAnalista;
		this.receptorPago = receptorPago;
		this.coFormaPago = checkOrAvisoCredito;
		this.numSolicituds = numSolicituds;
		this.coStatus = coStatus;
		this.tipoEmpleado = tipoEmpleado;
		this.meses = meses;
		this.filtrarByMesOrComplementoOrAmbos = filtrarByMesOrComplementoOrAmbos;
		this.coRepStatus = coRepStatus;
		this.isToCreateReportDefinitivo = isToCreateReportDefinitivo;
		
	}

	/**
	 * Realizaremos la busqueda por familiar
	 * 
	 * @param descripPeriodo
	 * @param companiaAnalista
	 * @param cedula
	 * @param codigoBenef
	 * @param coStatus
	 */
	public ReporteByRpago(String descripPeriodo, String companiaAnalista,
			String coStatus) {
		this.descripPeriodo = descripPeriodo;
		this.companiaAnalista = companiaAnalista;
		this.coStatus = coStatus;
	}

	/**
	 * Realizaremos la busqueda por numero de solicitud y numero de factura
	 * 
	 * @param numSolicituds
	 * @param nuFactura
	 */
	public ReporteByRpago(String descripPeriodo, String companiaAnalista,
			String numSolicituds, String nuFactura) {
		this.numSolicituds = numSolicituds;
		this.nuFactura = nuFactura;
		this.descripPeriodo = descripPeriodo;
		this.companiaAnalista = companiaAnalista;
	}

	/*
	 * Nos vamos a la vista actualizar con todo los datos llenos (non-Javadoc)
	 * 
	 * @see ve.org.bcv.rhei.controlador.SolAction#ejecutar()
	 */
	public ShowResultToView ejecutar() throws ServletException, IOException {
		return super.getShowResultToView();
	}

	public DocumentosBean generar(InputStream jrxml,
			Map<String, Object> parameters, String fileOut) {
		try {

			if (!StringUtils.isEmpty(descripPeriodo)) {

				JasperDesign jasperDesign = null;
				JasperReport jasperReport = null;
				JasperPrint jasperPrint = null;
				// /**Load the JRXML TO GENERATE JASPER REPORT*/
				jasperDesign = JRXmlLoader.load(jrxml);
				// /**Load the JRXML TO GENERATE JASPER REPORT*/
				/** put the datasource and the parameters en jasperreport */
				jasperReport = JasperCompileManager.compileReport(jasperDesign);
				/** send to print or xls, pdf, csv etc */

				/** Buscamos la data en BD */
				/** Buscaremos todos los objetos *****/
				List<ProveedorRpago1Bean> objs = searchObjects();
				/**
				 * Chequearemos que la factura tenga un nuemro de control por lo
				 * menos..
				 */
				try {

					if (!super.existRpago(objs)) {
					} else {
						/**
						 * Fin Chequearemos que la factura tenga un nuemro de
						 * control por lo menos..
						 */

						Collection collection = objs;
						jasperPrint = JasperFillManager.fillReport(
								jasperReport, parameters,
								new JRBeanCollectionDataSource(collection));
						byte[] bytes = JasperExportManager
								.exportReportToPdf(jasperPrint);
						InputStream myInputStream = new ByteArrayInputStream(
								bytes);
						documentosBean = new DocumentosBean();
						documentosBean.setInputStream(myInputStream);
						documentosBean.setDocFileWithExtension(fileOut);
					}
				} catch (Exception e) {
				}

			} else {
				super.getShowResultToView().setMensaje("fracaso3");
			}

		} catch (Exception ex) {
			System.out.println("EXCEPTION: " + ex.toString());
		}
		return documentosBean;

	}

	/**
	 * Buscaremos todos los beneficiarios segun la cedula del empleado y su
	 * periodo escolar
	 * 
	 * @return
	 */
	public List<FamiliarRpago2Bean> searchFamiliares(
			ProveedorRpago1Bean proveedorRpago1Bean, int coRepStatus) {
		List<FamiliarRpago2Bean> familiarRpago2BeanLst = new ArrayList<FamiliarRpago2Bean>();
		/** Para Cada DatosPrincip buscamos sus beneficiarios */
		/** DEberia buscarse en bd, luego por cada uno llenar su data interna */
		try {

			List<Familiar> familiares = familiarDao
					.consultarFamiliarByNroSolicitudId(proveedorRpago1Bean
							.getNuSolicitud());
			for (Familiar familia : familiares) {

				familia.setFacturas(searchFacturasByBeneficiario(familia,
						coRepStatus));
				familiarRpago2BeanLst.add(familia);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}

		return familiarRpago2BeanLst;
	}

	/**
	 * Retornamos lista de facturas por cada beneficiario Debes tener las
	 * variables de instancia beneficiario y periodo escolar
	 * 
	 * @return lista de beneficiarios que tienen facturas
	 */
	public List<FacturaRpago3Bean> searchFacturasByBeneficiario(
			FamiliarRpago2Bean familiarRpago2Bean, int coRepStatus) {

		List<FacturaRpago3Bean> facturaReportes = searchFacturas(
				this.descripPeriodo, familiarRpago2Bean.getNuSolicitud(),
				this.companiaAnalista, this.filtrarByMesOrComplementoOrAmbos,
				this.receptorPago, this.coFormaPago, this.meses, coRepStatus);

		return facturaReportes;
	}

	public List<FacturaRpago3Bean> searchFacturas(String descripPeriodo,
			long nuSolicitud, String companiaAnalista,
			String filtrarByMesOrComplementoOrAmbos, int receptorPago,
			int coFormaPago, String meses, int coRepStatus) {
		ResourceBundle rb = ResourceBundle
				.getBundle("ve.org.bcv.rhei.util.bundle");
		List<FacturaRpago3Bean> facturaReportes = new ArrayList<FacturaRpago3Bean>();
		/** buscamos por relacion de pagos */
		StringBuilder acumObservaciones = new StringBuilder("");
		double acumMo_factura = 0d;
		double acumMo_pago_adicional = 0d;
		StringBuilder acumTx_concepto_pago = new StringBuilder("");;
		Map<Long, FacturaMontoMesHistorico> facturaMontoMesHistoricoUnico = new HashMap<Long, FacturaMontoMesHistorico>();
		
		String complmntoS="";;
		/**End El monto del periodo bcv complemnento que sale al reporte*/
		try {
			/**
			 * buscamos primero las facturas, luego por cada factura sacamos la
			 * relacion de pagos
			 */

			List<Factura> facturas = facturaDao
					.consultarFacturasByNroSolicitudId(
							Integer.parseInt(nuSolicitud + ""), coRepStatus);

			if (facturas != null && !facturas.isEmpty() && facturas.size() > 0) {

				FacturaMontoMesHistorico facturaMontoMesHistorico = null;
				for (Factura obj : facturas) {
					long facturasId = obj.getNroIdFactura();

					List<Factura> facturasFilttradas = facturaDao
							.factByIdFacRecPagoFormPagoPerNumSolComp(
									facturasId, receptorPago, coFormaPago,
									descripPeriodo, nuSolicitud,
									companiaAnalista, meses,
									filtrarByMesOrComplementoOrAmbos);

					double mo_factura = 0d;
					double mo_pago_adicional = 0d;
					String txComplementoReembolso = null;
					/**
					 * Los meses y las matriculas si son negativos, ya fueron
					 * pagados y no se sumaran en el reporte
					 */
					int negativo = 0;
					Map<String, String> unicoConceptoPago = new HashMap<String, String>();
					String abrevComplemento = rb.getString("complemento");
					if (rb.getString("complemento").length() > 3) {
						abrevComplemento = rb.getString("complemento")
								.substring(0, 4);
					}
					
				
					
					/** Inicio del for **/
				 
					for (Factura fact : facturasFilttradas) {

						negativo = fact.getNuRefPago();

						/**
						 * LAS OBSERVACIONES SON LOS MESES O MATRICULA Y ESTOS
						 * SI AGARRAMOS EL CONCEPTO
						 */
						/** Si es negativo es porque ya esta pago ************/
						String corcheteAbre = "";
						String corcheteCierre = "";
						if (negativo < 0) {
							corcheteAbre = "[ - ";
							corcheteCierre = " ] ";
						}
						if (fact.getTxConceptoPago() == null) {
							fact.setTxConceptoPago("");
						}
						/**
						 * Si es complemento, agarramos los conceptos en
						 * facturas
						 */
						if ("S".equalsIgnoreCase(fact.getInComplemento())) {
							
							/**El monto del periodo bcv complemnento que sale al reporte*/
							complmntoS=fact.getInComplemento();
							
							/**
							 * filtrarByMesOrComplementoOrAmbos: 0 es matricla ,
							 * 1 es reembolso y 2 es ambos
							 **/
							if ("1".equalsIgnoreCase(filtrarByMesOrComplementoOrAmbos)
									|| "2".equalsIgnoreCase(filtrarByMesOrComplementoOrAmbos)) {
								/** El concepto se toma una sol< vez */

								/***
								 * Calculamos el total en el memorando con su
								 * formato
								 *******/
								DecimalFormat df2 = new DecimalFormat(
										Constantes.FORMATO_DOUBLE,
										new DecimalFormatSymbols(new Locale(
												"es", "VE")));
								BigDecimal value = new BigDecimal(
										fact.getMoTotalPago());
								String montoComplToReport = df2.format(value
										.floatValue());
								if (!unicoConceptoPago.containsKey(fact
										.getTxConceptoPago())) {

									txComplementoReembolso = fact
											.getTxConceptoPago() != null
											? (String) fact.getTxConceptoPago()
													.trim()
													+ " "
													+ abrevComplemento
													+ " "
													+ corcheteAbre
													+ super.obtenerNameMes(
															Math.abs(fact
																	.getNuRefPago()))
															.substring(0, 3)
													+ "("
													+ montoComplToReport
													+ ")" + corcheteCierre + ""
											: "";
									unicoConceptoPago.put(
											fact.getTxConceptoPago(),
											fact.getTxConceptoPago());
								} else {
									txComplementoReembolso =  " "
											+ corcheteAbre
											+ super.obtenerNameMes(
													Math.abs(fact
															.getNuRefPago()))
													.substring(0, 3) + "("
											+ montoComplToReport + ")"
											+ corcheteCierre + "";
								}
							}
						}
						/**
						 * Si no es complemento agarramos las observaciones en
						 * relacion de pagos
						 */
						//if ((!"S".equalsIgnoreCase(fact.getInComplemento())) || ("S".equalsIgnoreCase(fact.getInComplemento()))) {
						if ((!"S".equalsIgnoreCase(fact.getInComplemento())) ) {

							/**
							 * vamos a buscar el historico de los montos bcv
							 * para cada factura
							 */
							/**Si es matricula, usamos una factura id ficticia, para hacerla diferent a las demas
							 * y en el reporte me salga separada
							 * */
                             boolean isFactura=(Mes.MATRICULA.getValue()-Math.abs(fact.getNuRefPago())==0);
                             long facturasIdMatr=-14;
                             if (isFactura){
                            	 facturasId=facturasIdMatr;
                             }
							if (!facturaMontoMesHistoricoUnico
									.containsKey(facturasId)) {
								facturaMontoMesHistorico = new FacturaMontoMesHistorico();
								facturaMontoMesHistoricoUnico.put(facturasId,
										facturaMontoMesHistorico);
							}
							facturaMontoMesHistorico = facturaMontoMesHistoricoUnico
									.get(facturasId);
							
							facturaMontoMesHistorico.setMontoBCV(obj
									.getMontoBCV());
							
							facturaMontoMesHistorico.setMontoMatriculaBCV(obj
									.getMontoMatriculaBCV());
							/**Si es matricula, colocamos el monto de la matricula*/
							if (isFactura){
								facturaMontoMesHistorico.setMontoPeriodoBCV(facturaMontoMesHistorico
										.getMontoMatriculaBCV());
							}else{
								/**Si es periodo, colocamos el monto del periodo*/
								facturaMontoMesHistorico.setMontoPeriodoBCV(obj
										.getMontoPeriodoBCV());
							}
						
							facturaMontoMesHistorico.setNuIdFactura(facturasId
									+ "");

							String coma = "";
							if (null != facturaMontoMesHistorico.getMeses()
									&& !"".equalsIgnoreCase(facturaMontoMesHistorico
											.getMeses())) {
								coma = facturaMontoMesHistorico.getMeses()
										+ ",";
							}
							String nameMes = super.obtenerNameMes(
									Math.abs(fact.getNuRefPago())).substring(0,
									3);
							facturaMontoMesHistorico.setMeses(coma + nameMes);
							facturaMontoMesHistoricoUnico.put(facturasId,
									facturaMontoMesHistorico);

							/**
							 * fin vamos a buscar el historico de los montos bcv
							 * para cada factura
							 */

							/**
							 * filtrarByMesOrComplementoOrAmbos: 0 es matricla ,
							 * 1 es reembolso y 2 es ambos
							 **/
							if ("0".equalsIgnoreCase(filtrarByMesOrComplementoOrAmbos)
									|| "2".equalsIgnoreCase(filtrarByMesOrComplementoOrAmbos)) {

								if (!StringUtils.isEmpty(acumObservaciones
										.toString())
										&& fact != null
										&& !StringUtils.isEmpty(fact
												.getTxObservaciones())) {
									acumObservaciones.append(" , ");
								}

								/** En las observaciones vienen los meses */
								if (fact.getTxObservaciones().length() > 3) {
									acumObservaciones
											.append(fact.getTxObservaciones() != null
													? corcheteAbre
															+ (String) fact
																	.getTxObservaciones()
																	.trim()
																	.substring(
																			0,
																			3)
															+ corcheteCierre
													: " ");
								} else {
									acumObservaciones
											.append(fact.getTxObservaciones() != null
													? corcheteAbre
															+ (String) fact
																	.getTxObservaciones()
																	.trim()
															+ corcheteCierre
													: " ");
								}

							}
						}

						/** Si no es pagado, es positivo, sumamos los montos */
						if (negativo > 0) {
							/**
							 * POR CADA FACTURA TENEMOS EL MISMO MO_TOTAL_PAGO,
							 * MO_FACTURA,MO_PAGO_ADICIONAL,TX_CONCEPTO_PAGO
							 */
							if ("N".equalsIgnoreCase(fact.getInComplemento())) {
								/**
								 * filtrarByMesOrComplementoOrAmbos: 0 es
								 * matricla , 1 es reembolso y 2 es ambos
								 **/
								if ("0".equalsIgnoreCase(filtrarByMesOrComplementoOrAmbos)
										|| "2".equalsIgnoreCase(filtrarByMesOrComplementoOrAmbos)) {
									mo_factura += fact.getMoTotalPago();
								}
							}
							if ("S".equalsIgnoreCase(fact.getInComplemento())) {
								/**
								 * filtrarByMesOrComplementoOrAmbos: 0 es
								 * matricla , 1 es reembolso y 2 es ambos
								 **/
								if ("1".equalsIgnoreCase(filtrarByMesOrComplementoOrAmbos)
										|| "2".equalsIgnoreCase(filtrarByMesOrComplementoOrAmbos)) {
									mo_pago_adicional += fact.getMoTotalPago();;
								}
								
							}
						}
						/** Meses para complemento */
						if (txComplementoReembolso != null
								&& !StringUtils.isEmpty(txComplementoReembolso)
								&& !StringUtils.isEmpty(acumTx_concepto_pago
										.toString())) {
							acumTx_concepto_pago.append(",");
						}
						acumTx_concepto_pago
								.append(txComplementoReembolso == null
										? ""
										: txComplementoReembolso);
					}
					/** Fin del for ***/
                    
					acumMo_factura += mo_factura;
					/**Inicio.. Si es complemento, debemos llevar el monto de la factura igualmente..*/
//					if (isComplemento &&acumMo_factura==0){
//						acumMo_factura +=mo_pago_adicional;
//					}
					/**Fin..Si es complemento, debemos llevar el monto de la factura igualmente..*/
					acumMo_pago_adicional += mo_pago_adicional;
				}

			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
		}

		FacturaRpago3Bean facturaRpago3Bean = null;
		facturaRpago3Bean = new FacturaRpago3Bean();

		FacturaMontoMesHistorico facturaMontoMesHistorico = null;
		StringBuilder montoPeriodoBCVHistoricoStr = new StringBuilder("");
		Iterator it = facturaMontoMesHistoricoUnico.keySet().iterator();
		/**
		 * Si nunca vario el monto, entonces,solo se colocara el monto sin los
		 * meses...
		 */
		String montoPeriodo = "";
		String montoPeriodoAux = "";
		boolean isMontoVario = false;
		while (it.hasNext()) {
			if (!"".equalsIgnoreCase(montoPeriodoBCVHistoricoStr.toString())) {
				montoPeriodoBCVHistoricoStr.append(",");
				montoPeriodoBCVHistoricoStr.append(" ");
			}
			Long key = (Long) it.next();
			facturaMontoMesHistorico = facturaMontoMesHistoricoUnico.get(key);

			/*** Calculamos el total en el memorando con su formato *******/
			DecimalFormat df2 = new DecimalFormat(Constantes.FORMATO_DOUBLE,
					new DecimalFormatSymbols(new Locale("es", "VE")));
			BigDecimal value = new BigDecimal(
					facturaMontoMesHistorico.getMontoPeriodoBCV());
			/*** Fin Calculamos el total en el memorando con su formato *******/
			montoPeriodo = new String(df2.format(value.floatValue()));
			if ("".equalsIgnoreCase(montoPeriodoAux)) {
				montoPeriodoAux = montoPeriodo;
				/** Si los montos variaron en algun momento */
			} else if (!montoPeriodoAux.equalsIgnoreCase(montoPeriodo)) {
				isMontoVario = true;
			}
			montoPeriodoBCVHistoricoStr.append("[")
					.append(facturaMontoMesHistorico.getMeses()).append(" ")
					.append(" (").append(montoPeriodo).append(")").append("]");
		}
		/**
		 * Si   vario el monto, entonces,  se colocara el monto con los
		 * meses...
		 */
		if (isMontoVario) {
			facturaRpago3Bean.setMontoPeriodoBCV(montoPeriodoBCVHistoricoStr
					.toString());
		} else {
			/**
			 * Si nunca vario el monto, entonces,solo se colocara el monto sin los
			 * meses...
			 */
			facturaRpago3Bean.setMontoPeriodoBCV(montoPeriodo);
		}
		
		
		/**El monto del periodo bcv complemnento que sale al reporte*/
		Double montoPeriodoBcvComplemento=0d;
		/**
		 * Si es complemento, agarramos los conceptos en
		 * facturas
		 */
		/**El monto del periodo bcv complemnento que sale al reporte*/
		if ("S".equalsIgnoreCase(complmntoS)) {
			
			try {
				solicitudDao=new SolicitudDaoImpl();
				Solicitud solicitud=solicitudDao.buscarSolicitud(Integer.parseInt(nuSolicitud + ""));
				if (solicitud!=null && solicitud.getMontoPeriodo()!=null){
					montoPeriodoBcvComplemento=solicitud.getMontoPeriodo();	
				}
				
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (montoPeriodoBcvComplemento!=0){
				/*** Calculamos el total en el memorando con su formato *******/
				DecimalFormat df2 = new DecimalFormat(Constantes.FORMATO_DOUBLE,
						new DecimalFormatSymbols(new Locale("es", "VE")));
				BigDecimal value = new BigDecimal(
						montoPeriodoBcvComplemento);
				/*** Fin Calculamos el total en el memorando con su formato *******/
				montoPeriodo = new String(df2.format(value.floatValue()));
				facturaRpago3Bean.setMontoPeriodoBCV(montoPeriodo);	
			}
			
		}
		/**End El monto del periodo bcv complemnento que sale al reporte*/
		facturaRpago3Bean.setNuSolicitud(nuSolicitud);
		facturaRpago3Bean.setTxObservaciones(acumObservaciones.toString());
		facturaRpago3Bean.setMoFactura(acumMo_factura);
		facturaRpago3Bean.setMoPagoAdicional(acumMo_pago_adicional);
		facturaRpago3Bean.setTxConceptoPago(acumTx_concepto_pago.toString());
		facturaReportes.add(facturaRpago3Bean);
		return facturaReportes;
	}

	public static Logger getLog() {
		return log;
	}

	public String getNinoEspecial() {
		return ninoEspecial;
	}

	public void setNinoEspecial(String ninoEspecial) {
		this.ninoEspecial = ninoEspecial;
	}

}
