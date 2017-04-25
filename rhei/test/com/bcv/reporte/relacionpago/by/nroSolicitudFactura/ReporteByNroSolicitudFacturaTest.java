/**
 * 
 */
package com.bcv.reporte.relacionpago.by.nroSolicitudFactura;

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

import com.bcv.reporte.relacionpago.FacturaRpago3Bean;
import com.bcv.reporte.relacionpago.ProveedorRpago1Bean;
import com.bcv.reporte.relacionpago.ReporteByRpago;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 12/08/2015 14:23:06
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class ReporteByNroSolicitudFacturaTest implements Reporte {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 
	 private static 	String numSolicitud="496";
	 private static String nuFactura="";//"F-489SepOctDicFebMar";
     private static String descripPeriodo="2017-2018";
     private static String companiaAnalista="01";


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
			
				 reporte= new ReporteByRpago( descripPeriodo,companiaAnalista,numSolicitud,nuFactura);
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
