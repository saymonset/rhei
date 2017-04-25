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
 * @author Ing Simon Alberto Rodriguez Pacheco 22/08/2015 13:11:12 2015 mail :
 *         oraclefedora@gmail.com
 */
public class ReporteByPagoAtributoEmp implements Reporte, Serializable {

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
	private List<PagoTributoBeanEmp> list = null;
	private String meses;
	private String filtrarByMesOrComplementoOrAmbos;
	private int coRepStatus;
	private String isToCreateReportDefinitivo;
	private String ninoEspecial;
	private int numJubilados=0;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * numSolicituds is a String large . it is contain values of numSolictud
	 * Consult example 1,2,3,4,,598.201,333 Metdo que me dara reportes por
	 * reembolso o complemento
	 * 
	 * @param descripPeriodo
	 * @param companiaAnalista
	 * @param receptorPago
	 */
	public ReporteByPagoAtributoEmp(String descripPeriodo,
			String companiaAnalista, int receptorPago, int checkOrAvisoCredito,
			String numSolicituds, String coStatus, String tipoEmpleado,
			String meses, String filtrarByMesOrComplementoOrAmbos,int coRepStatus,String isToCreateReportDefinitivo,String ninoEspecial) {
		this.ninoEspecial= ninoEspecial;
		this.descripPeriodo = descripPeriodo;
		this.companiaAnalista = companiaAnalista;
		this.receptorPago = receptorPago;
		this.coFormaPago = checkOrAvisoCredito;
		this.numSolicituds = numSolicituds;
		this.coStatus = coStatus;
		this.tipoEmpleado = tipoEmpleado;
		this.meses = meses;
		this.filtrarByMesOrComplementoOrAmbos = filtrarByMesOrComplementoOrAmbos;
		this.coRepStatus=coRepStatus;
		this.isToCreateReportDefinitivo=isToCreateReportDefinitivo;
	}
	/***Aqui se busca la data****/
	public List<PagoTributoBeanEmp> searchObjects() {
   /**sI LIST ES VACIO.... ENTRAMOS..*/
		if (list == null || list.isEmpty() || list.size() == 0) {
			Reporte reporte = null;
			reporte = new ReporteByRpago(descripPeriodo, companiaAnalista,
					receptorPago, coFormaPago, numSolicituds, coStatus,
					tipoEmpleado, meses, filtrarByMesOrComplementoOrAmbos,coRepStatus,isToCreateReportDefinitivo,ninoEspecial);
			List<ProveedorRpago1Bean> objs = (List<ProveedorRpago1Bean>) reporte
					.searchObjects();

			/** Buscamos la data en BD */
			/** Buscaremos todos los objetos *****/

			PagoTributoBeanEmp pagoTributoEmp = null;
			List<PagoTributoBeanEmp> pagoTributoEmpLst = new ArrayList<PagoTributoBeanEmp>();
			montoTotal = 0;
			StringBuilder concepto= null;
			numJubilados=0;
			for (ProveedorRpago1Bean pb : objs) {
				if (pb.getTipoEmp()!=null&&pb.getTipoEmp().equalsIgnoreCase("JUB")){
					numJubilados+=1;
				}
			 
				List<FamiliarRpago2Bean> familiares = pb.getFamiliares();
				for (FamiliarRpago2Bean frb : familiares) {
					List<FacturaRpago3Bean> facturas = frb.getFacturas();
					for (FacturaRpago3Bean fact : facturas) {
					 
						pagoTributoEmp = new PagoTributoBeanEmp();
						pagoTributoEmp.setNuSolicitud(fact.getNuSolicitud());
						pagoTributoEmp.setNombreColegio(pb.getNbProveedor());
						pagoTributoEmp.setCedula(pb.getCedula());
						pagoTributoEmp.setTrabajador(pb.getTrabajador());
						pagoTributoEmp.setNombreNino(frb.getNombreFlia());
						pagoTributoEmp.setMontoMensualPagTrab(fact.getMontoPeriodoBCV());
						pagoTributoEmp.setMoAporteBcv(frb.getMoAporteBcv());
						concepto= new StringBuilder();
						boolean isComa=false;
						if (!StringUtils.isEmpty(fact.getTxObservaciones())){
							if (isComa){
								concepto.append(",");
							}
							concepto.append(fact.getTxObservaciones());
							isComa=true;
						}
						if (!StringUtils.isEmpty(fact.getTxConceptoPago())){
							if (isComa){
								concepto.append(",");
							}

							concepto.append(fact.getTxConceptoPago());
							isComa=true;
						}

						pagoTributoEmp.setConcepto(concepto.toString());
					 
						pagoTributoEmp.setMonto(fact.getMoFactura()
								+ fact.getMoPagoAdicional());
						pagoTributoEmp.setCodigo(Constantes.DBS + "-"
								+ pb.getNuRifProveedor()); // + "-"+ pb.getDescripPeriodo() modificado por David Velasquez 
						montoTotal += pagoTributoEmp.getMonto();
						pagoTributoEmp.setMontoTotal(montoTotal);
						  /***Calculamos el total en el memorando con su formato*******/   		    
		    		    DecimalFormat df2 = new DecimalFormat(     Constantes.FORMATO_DOUBLE, 	      new DecimalFormatSymbols(new Locale("es", "VE")));
		    	    BigDecimal value = new BigDecimal(pagoTributoEmp.getMontoTotal());
		    	    /**Se usa en el metodo ejcutar de esta clase para llevarnos el monto total en bigDecimal*/
					montoTotalBiDec=value;
		    	    pagoTributoEmp.setMontoTotalBiDec(value);
		    	    pagoTributoEmp.setMontoTotalStr(new String(df2.format(value.floatValue())));
	                /***Fin Calculamos el total en el memorando con su formato*******/
						pagoTributoEmpLst.add(pagoTributoEmp);
					}
				}

			}

			list = pagoTributoEmpLst;
		}
		if (list!=null && list.size()>0){
			Collections.sort(list, new Comparator<PagoTributoBeanEmp>() {
			    public int compare(PagoTributoBeanEmp obj1, PagoTributoBeanEmp obj2) {
			    	 return obj1.getNombreColegio().compareTo(obj2.getNombreColegio());
			    }
			});
		}


		return list;
	}


	public DocumentosBean generar(InputStream jrxml,
			Map<String, Object> parameters, String fileOut) {
		try {

			JasperDesign jasperDesign = null;
			JasperReport jasperReport = null;
			JasperPrint jasperPrint = null;
			// /**Load the JRXML TO GENERATE JASPER REPORT*/
			//jrxml = ReporteByRpago.class.getResourceAsStream("MicroMemorando.jrxml");
		//	jrxml = PagoTributoBean.class.getResourceAsStream("reportPagoAndTributosEmp.jrxml");
			jasperDesign = JRXmlLoader.load(jrxml);
			
			// /**Load the JRXML TO GENERATE JASPER REPORT*/
			/** put the datasource and the parameters en jasperreport */
			jasperReport = JasperCompileManager.compileReport(jasperDesign);
			/** send to print or xls, pdf, csv etc */

			/**
			 * Chequearemos que la factura tenga un nuemro de control por lo
			 * menos..
			 */
			try {
				

				/** Buscaremos todos los objetos *****/
				List<PagoTributoBeanEmp> objs = searchObjects();
				Collection collection = objs;
				jasperPrint = JasperFillManager.fillReport(jasperReport,
						parameters, new JRBeanCollectionDataSource(collection));
				byte[] bytes = JasperExportManager
						.exportReportToPdf(jasperPrint);
				InputStream myInputStream = new ByteArrayInputStream(bytes);
				documentosBean = new DocumentosBean();
				documentosBean.setInputStream(myInputStream);
				documentosBean.setDocFileWithExtension(fileOut);
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		} catch (Exception ex) {
			System.out.println("EXCEPTION: " + ex.toString());
		}
		return documentosBean;

	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ve.org.bcv.rhei.report.by.benef.Reporte#ejecutar()
	 */
	@Override
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

	public ShowResultToView getShowResultToView() {
		return showResultToView;
	}

	public void setShowResultToView(ShowResultToView showResultToView) {
		this.showResultToView = showResultToView;
	}
	/* (non-Javadoc)
	 * @see ve.org.bcv.rhei.report.by.benef.Reporte#searchFacturas(java.lang.String, long, java.lang.String, java.lang.String, int, int, java.lang.String)
	 */
	@Override
	public List<FacturaRpago3Bean> searchFacturas(String descripPeriodo,
			long nuSolicitud, String companiaAnalista,
			String filtrarByMesOrComplementoOrAmbos, int receptorPago,
			int coFormaPago, String meses,int coRepStatus) {
		// TODO Auto-generated method stub
		return null;
	}
	public String getNinoEspecial() {
		return ninoEspecial;
	}
	public void setNinoEspecial(String ninoEspecial) {
		this.ninoEspecial = ninoEspecial;
	}
	public List<PagoTributoBeanEmp> getList() {
		return list;
	}
	public void setList(List<PagoTributoBeanEmp> list) {
		this.list = list;
	}
	 

	/* (non-Javadoc)
	 * @see ve.org.bcv.rhei.report.by.benef.Reporte#generar(java.io.InputStream, java.util.Map, java.lang.String, java.util.List)
	 */
	 
}
