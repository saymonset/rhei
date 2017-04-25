/**
 * 
 */
package com.bcv.reporte.pagotributo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.commons.lang3.StringUtils;

import ve.org.bcv.rhei.bean.DocumentosBean;
import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.report.by.benef.Reporte;
import ve.org.bcv.rhei.util.Constantes;

import com.bcv.reporte.relacionpago.FacturaRpago3Bean;
import com.bcv.reporte.relacionpago.FamiliarRpago2Bean;
import com.bcv.reporte.relacionpago.ProveedorRpago1Bean;
import com.bcv.reporte.relacionpago.ReporteByRpago;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco 22/08/2015 10:30:27 2015 mail : oraclefedora@gmail.com
 */
public class ReporteByPagoAtributoCEI implements Reporte, Serializable {

	private String ninoEspecial;
	private String descripPeriodo;
	private String companiaAnalista;
	private int receptorPago = -1;// 0 CENTRO DE EDUCACION INICIAL, 1 TRABAJADOR
	private int coFormaPago = -1; // 1 ES CHEQUE, 2 ES AVISO DE CREDITO
	private String numSolicituds;
	private String coStatus = "A";
	private String tipoEmpleado;
	private DocumentosBean documentosBean = new DocumentosBean();
	private ShowResultToView showResultToView;
	private double montoTotal;
	private BigDecimal montoTotalBiDec;
	private List<PagoTributoCEI> list = null;
	private String meses;
	private String filtrarByMesOrComplementoOrAmbos;
	private int coRepStatus;
	private String isToCreateReportDefinitivo;
	private int numJubilados = 0;
	public int getCoRepStatus() {
		return coRepStatus;
	}

	public void setCoRepStatus(int coRepStatus) {
		this.coRepStatus = coRepStatus;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * numSolicituds is a String large . it is contain values of numSolictud Consult example 1,2,3,4,,598.201,333 Metdo que me dara reportes por reembolso o complemento
	 * 
	 * @param descripPeriodo
	 * @param companiaAnalista
	 * @param receptorPago
	 */
	public ReporteByPagoAtributoCEI(String descripPeriodo, String companiaAnalista, int receptorPago, int checkOrAvisoCredito, String numSolicituds, String coStatus, String tipoEmpleado,
			String meses, String filtrarByMesOrComplementoOrAmbos, int coRepStatus, String isToCreateReportDefinitivo, String ninoEspecial) {
		this.ninoEspecial = ninoEspecial;
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
	public ShowResultToView ejecutar() throws ServletException, IOException {
		if (showResultToView == null) {
			showResultToView = new ShowResultToView();
		}
		list = searchObjects();
		showResultToView.setMontoTotalBiDec(montoTotalBiDec);
		showResultToView.setMontoTotal(montoTotal);
		showResultToView.setNumJubilados(numJubilados);
		return showResultToView;
	}

	public List<PagoTributoCEI> searchObjects() {

		if (list == null || list.isEmpty() || list.size() == 0) {
			Reporte reporte = null;
			/** ACA SE PROCESARA TODAS LAS VARIABLES................. */
			reporte = new ReporteByRpago(descripPeriodo, companiaAnalista, receptorPago, coFormaPago, numSolicituds, coStatus, tipoEmpleado, meses, filtrarByMesOrComplementoOrAmbos, coRepStatus,
					isToCreateReportDefinitivo, ninoEspecial);
			/** searchObjects ACA SE EJECUTA TODAS LAS VARIABLES................. */
			List<ProveedorRpago1Bean> objs = (List<ProveedorRpago1Bean>) reporte.searchObjects();

			/** Buscamos la data en BD */
			/** Buscaremos todos los objetos *****/

			PagoTributoCEI pagoTributoCEI = null;
			List<PagoTributoCEI> pagoTributoCEILst = new ArrayList<PagoTributoCEI>();
			montoTotal = 0;
			StringBuilder concepto = null;
			numJubilados = 0;
			for (ProveedorRpago1Bean pb : objs) {
				if (pb.getTipoEmp() != null && pb.getTipoEmp().equalsIgnoreCase("JUB")) {
					numJubilados += 1;
				}
				List<FamiliarRpago2Bean> familiares = pb.getFamiliares();
				for (FamiliarRpago2Bean frb : familiares) {
					List<FacturaRpago3Bean> facturas = frb.getFacturas();
					for (FacturaRpago3Bean fact : facturas) {
						pagoTributoCEI = new PagoTributoCEI();
						pagoTributoCEI.setNuSolicitud(fact.getNuSolicitud());
						pagoTributoCEI.setNombreColegio(pb.getNbProveedor());
						pagoTributoCEI.setRif(pb.getNuRifProveedor());
						pagoTributoCEI.setTrabajador(pb.getTrabajador());
						pagoTributoCEI.setNombreNino(frb.getNombreFlia());
						pagoTributoCEI.setMontoMensualPagTrab(fact.getMontoPeriodoBCV());
						pagoTributoCEI.setMoAporteBcv(frb.getMoAporteBcv());

						concepto = new StringBuilder();
						boolean isComa = false;
						if (!StringUtils.isEmpty(fact.getTxObservaciones())) {
							if (isComa) {
								concepto.append(",");
							}
							concepto.append(fact.getTxObservaciones());
							isComa = true;
						}
						if (!StringUtils.isEmpty(fact.getTxConceptoPago())) {
							if (isComa) {
								concepto.append(",");
							}

							concepto.append(fact.getTxConceptoPago());
							isComa = true;
						}

						pagoTributoCEI.setConcepto(concepto.toString());
						pagoTributoCEI.setMonto(fact.getMoFactura() + fact.getMoPagoAdicional());
						pagoTributoCEI.setCodigo(Constantes.DBS + "-" + pb.getNuRifProveedor()); // +"-"+pb.getDescripPeriodo() borrado por David Velasquez
						montoTotal += pagoTributoCEI.getMonto();
						pagoTributoCEI.setMontoTotal(montoTotal);
						/*** Calculamos el total en el memorando con su formato *******/
						DecimalFormat df2 = new DecimalFormat(Constantes.FORMATO_DOUBLE, new DecimalFormatSymbols(new Locale("es", "VE")));
						BigDecimal value = new BigDecimal(pagoTributoCEI.getMontoTotal());
						/**Se usa en el metodo ejcutar de esta clase para llevarnos el monto total en bigDecimal*/
						montoTotalBiDec=value;
						pagoTributoCEI.setMontoTotalBiDec(value);
						pagoTributoCEI.setMontoTotalStr(new String(df2.format(value.floatValue())));
						/*** Fin Calculamos el total en el memorando con su formato *******/

						pagoTributoCEILst.add(pagoTributoCEI);
					}
				}

			}

			list = pagoTributoCEILst;
		}

		if (list != null && list.size() > 0) {
			Collections.sort(list, new Comparator<PagoTributoCEI>() {
				public int compare(PagoTributoCEI obj1, PagoTributoCEI obj2) {
					return obj1.getNombreColegio().compareTo(obj2.getNombreColegio());
				}
			});
		}

		return list;
	}

	// TODO Auto-generated method stub

	public DocumentosBean generar(InputStream jrxml, Map<String, Object> parameters, String fileOut) {
		try {

			JasperDesign jasperDesign = null;
			JasperReport jasperReport = null;
			JasperPrint jasperPrint = null;
			// /**Load the JRXML TO GENERATE JASPER REPORT*/
			jasperDesign = JRXmlLoader.load(jrxml);
			// /**Load the JRXML TO GENERATE JASPER REPORT*/
			/** put the datasource and the parameters en jasperreport */
			jasperReport = JasperCompileManager.compileReport(jasperDesign);
			/** send to print or xls, pdf, csv etc */

			/** Chequearemos que la factura tenga un nuemro de control por lo menos.. */
			try {
				/** Buscaremos todos los objetos *****/
				List<PagoTributoCEI> objs = searchObjects();

				Collection collection = objs;
				jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(collection));
				byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
				InputStream myInputStream = new ByteArrayInputStream(bytes);

				documentosBean = new DocumentosBean();
				documentosBean.setInputStream(myInputStream);
				documentosBean.setDocFileWithExtension(fileOut);
			} catch (Exception e) {
			}
		} catch (Exception ex) {
			System.out.println("EXCEPTION: " + ex.toString());
		}
		return documentosBean;

	}

	public ShowResultToView getShowResultToView() {
		return showResultToView;
	}

	public void setShowResultToView(ShowResultToView showResultToView) {
		this.showResultToView = showResultToView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ve.org.bcv.rhei.report.by.benef.Reporte#searchFacturas(java.lang.String, long, java.lang.String, java.lang.String, int, int, java.lang.String)
	 */
	@Override
	public List<FacturaRpago3Bean> searchFacturas(String descripPeriodo, long nuSolicitud, String companiaAnalista, String filtrarByMesOrComplementoOrAmbos, int receptorPago, int coFormaPago,
			String meses, int coRepStatus) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getNinoEspecial() {
		return ninoEspecial;
	}

	public void setNinoEspecial(String ninoEspecial) {
		this.ninoEspecial = ninoEspecial;
	}

	public String getDescripPeriodo() {
		return descripPeriodo;
	}

	public void setDescripPeriodo(String descripPeriodo) {
		this.descripPeriodo = descripPeriodo;
	}

	public String getCompaniaAnalista() {
		return companiaAnalista;
	}

	public void setCompaniaAnalista(String companiaAnalista) {
		this.companiaAnalista = companiaAnalista;
	}

	public int getReceptorPago() {
		return receptorPago;
	}

	public void setReceptorPago(int receptorPago) {
		this.receptorPago = receptorPago;
	}

	public int getCoFormaPago() {
		return coFormaPago;
	}

	public void setCoFormaPago(int coFormaPago) {
		this.coFormaPago = coFormaPago;
	}

	public String getNumSolicituds() {
		return numSolicituds;
	}

	public void setNumSolicituds(String numSolicituds) {
		this.numSolicituds = numSolicituds;
	}

	public String getCoStatus() {
		return coStatus;
	}

	public void setCoStatus(String coStatus) {
		this.coStatus = coStatus;
	}

	public String getTipoEmpleado() {
		return tipoEmpleado;
	}

	public void setTipoEmpleado(String tipoEmpleado) {
		this.tipoEmpleado = tipoEmpleado;
	}

	public DocumentosBean getDocumentosBean() {
		return documentosBean;
	}

	public void setDocumentosBean(DocumentosBean documentosBean) {
		this.documentosBean = documentosBean;
	}

	public double getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(double montoTotal) {
		this.montoTotal = montoTotal;
	}

	public List<PagoTributoCEI> getList() {
		return list;
	}

	public void setList(List<PagoTributoCEI> list) {
		this.list = list;
	}

	public String getMeses() {
		return meses;
	}

	public void setMeses(String meses) {
		this.meses = meses;
	}

	public String getFiltrarByMesOrComplementoOrAmbos() {
		return filtrarByMesOrComplementoOrAmbos;
	}

	public void setFiltrarByMesOrComplementoOrAmbos(String filtrarByMesOrComplementoOrAmbos) {
		this.filtrarByMesOrComplementoOrAmbos = filtrarByMesOrComplementoOrAmbos;
	}

	public String getIsToCreateReportDefinitivo() {
		return isToCreateReportDefinitivo;
	}

	public void setIsToCreateReportDefinitivo(String isToCreateReportDefinitivo) {
		this.isToCreateReportDefinitivo = isToCreateReportDefinitivo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
