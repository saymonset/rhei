/**
 * 
 */
package com.bcv.reporte.relacionpago.by.complemento_reembolso.check_avisocredito;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
import net.sf.jasperreports.view.JasperViewer;
import ve.org.bcv.rhei.bean.DocumentosBean;
import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.report.ReportePathLogo;
import ve.org.bcv.rhei.report.by.benef.Reporte;
import ve.org.bcv.rhei.util.Constantes;

import com.bcv.reporte.relacionpago.FacturaRpago3Bean;
import com.bcv.reporte.relacionpago.ProveedorRpago1Bean;
import com.bcv.reporte.relacionpago.ReporteByRpago;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 31/07/2015 10:28:17
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class ReporteByRelacionPagosByReemBolsoComplementoCheckAvisoCreditoTest implements Reporte {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String descripPeriodo="2016-2017";
	private static String companiaAnalista="01";
	private static int receptorPago=-1;// 0 CENTRO DE EDUCACION INICIAL, 1 TRABAJADOR
	private static int  coFormaPago=-1; //1 ES CHEQUE, 2 ES AVISO DE CREDITO
	 private static 	String numSolicitud="446";
	 private static String tipoEmpleado="";
	    private static String meses;
	  	private static String filtrarByMesOrComplementoOrAmbos;


	public static void main(String[] args) {
	 
		  Reporte reporte= null;
		  InputStream jrxml = ReporteByRpago.class.getResourceAsStream(
					"reportRelacionPagoPrincipales1.jrxml");
		  String fileOut="reporte.pdf";
		
		  
		  try {
				JasperDesign jasperDesign = null;
				JasperReport jasperReport = null;
				JasperPrint jasperPrint = null;
//				/**Load the JRXML TO GENERATE JASPER REPORT*/
				jasperDesign = JRXmlLoader.load(jrxml);
//				/**Load the JRXML TO GENERATE JASPER REPORT*/
				/**put the datasource and the parameters en jasperreport*/
				jasperReport = JasperCompileManager.compileReport(jasperDesign);
				/**send to print or xls, pdf, csv etc*/
				int coRepStatus=0;
				String status=Constantes.CO_STATUS_ACTIVO;
				String isToCreateReportDefinitivo=Constantes.PAGADO_NOPAGADO_AMBOS;
				String special="0";
				 reporte= new ReporteByRpago("2016-2017","01",receptorPago,coFormaPago,numSolicitud,status,tipoEmpleado,meses,filtrarByMesOrComplementoOrAmbos,coRepStatus,isToCreateReportDefinitivo,special);
				List<ProveedorRpago1Bean> objs=(List<ProveedorRpago1Bean>) reporte.searchObjects();
				Collection collection = objs;
				  Map parameters = new HashMap();
				  ReportePathLogo archivo = new ReportePathLogo();
	    		    InputStream is = archivo.getClass().getResourceAsStream(
					"logo_bcv.jpg"); 
	    		    parameters.put("logo",is);
				  parameters.put("descripPeriodo", "2016-2017");
				jasperPrint = JasperFillManager.fillReport(jasperReport,
						parameters, new JRBeanCollectionDataSource(
								collection));
				JasperExportManager.exportReportToPdfFile(jasperPrint, fileOut);
				JasperViewer.viewReport(jasperPrint);
			} catch (Exception ex) {
				System.out.println("EXCEPTION: " + ex);
			}
		  
		 // reporte.generar(jrxml,parameters,fileOut);
	}

 
//	
	public DocumentosBean generar(InputStream jrxml,
			Map<String, Object> parameters, String fileOut) {
		 
		return null;
	}
//	
	 
 
	/**
	 * @param descripPeriodo
	 * @param companiaAnalista
	 * @param receptorPago
	 */
	public ReporteByRelacionPagosByReemBolsoComplementoCheckAvisoCreditoTest() {
		super();
	}
	/* (non-Javadoc)
	 * @see ve.org.bcv.rhei.report.by.benef.Reporte#searchObjects()
	 */
	@Override
	public List<?> searchObjects() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ve.org.bcv.rhei.report.by.benef.Reporte#ejecutar()
	 */
	@Override
	public ShowResultToView ejecutar() throws ServletException, IOException {
		// TODO Auto-generated method stub
		return null;
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



	public static long getSerialversionuid() {
		return serialVersionUID;
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

 

	 

 

}
