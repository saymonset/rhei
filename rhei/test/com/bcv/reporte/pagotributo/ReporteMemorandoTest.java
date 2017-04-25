/**
 * 
 */
package com.bcv.reporte.pagotributo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;

import com.bcv.reporte.relacionpago.FacturaRpago3Bean;

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

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 24/08/2015 09:38:59
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class ReporteMemorandoTest implements Reporte {
// com.bcv.reporte.pagotributo.ReporteMemorandoTest
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 
	 private static 	String numSolicitud="504,503,500";
	 private static String coStatus="A";
	 private static String tipoEmpleado="";
    private static String descripPeriodo="2017-2018";
    private static String companiaAnalista="01";
    
    private static int coFormaPago=1;
    private static int receptorPago=0;
 
	public static void main(String[] args) {
		ResourceBundle rb = ResourceBundle.getBundle("ve.org.bcv.rhei.util.bundle");
		  Reporte reporte= null;
		  InputStream jrxml = PagoTributoBean.class.getResourceAsStream(
					"reportPagoAndTributosCEI.jrxml");
		  InputStream memojrxml = PagoTributoBean.class.getResourceAsStream(
					"memorando.jrxml");
		  String fileOut="reporte.pdf";
		
		  
		  try {
				JasperDesign jasperDesign = null;
				JasperReport jasperReport = null;
				JasperPrint jasperPrint = null;
//				/**Load the JRXML TO GENERATE JASPER REPORT*/
				jasperDesign = JRXmlLoader.load(memojrxml);
//				/**Load the JRXML TO GENERATE JASPER REPORT*/
				/**put the datasource and the parameters en jasperreport*/
				jasperReport = JasperCompileManager.compileReport(jasperDesign);
				/**send to print or xls, pdf, csv etc*/
				
				List<MemorandoBean> objs= new ArrayList<MemorandoBean>();
				MemorandoBean memorandoBean = new MemorandoBean();
				memorandoBean.setMonto(39555d);
				objs.add(memorandoBean);
				Collection collection = objs;
				 
				  Map parameters = new HashMap();
		 
				 reporte=new MemorandoReport();
				 memojrxml = PagoTributoBean.class.getResourceAsStream(
							"memorando.jrxml");
			 	    		    parameters.put("titulo",rb.getString("reporte.memorando.titulo"));
	    		    parameters.put("total",new Double(rb.getString("reporte.memorando.total")));
	    		    ReportePathLogo archivo = new ReportePathLogo();
	    		    InputStream is = archivo.getClass().getResourceAsStream(
					"logo_bcv.jpg"); 
	    		    parameters.put("logo",is);
				  parameters.put("descripPeriodo", "2016-2017");
				  parameters.put("de", rb.getString("reporte.memorando.de"));
				  parameters.put("para", rb.getString("reporte.memorando.para"));
				  parameters.put("asunto", rb.getString("reporte.memorando.asunto"));
				  parameters.put("parrafo2", rb.getString("reporte.memorando.parrafo2"));
				  
				  parameters.put("bloque0", rb.getString("reporte.memorando.bloque0"));
				  parameters.put("tipoEmpl", rb.getString("reporte.memorando.tipoEmpl"));
				  parameters.put("bloque1", rb.getString("reporte.memorando.bloque1"));
				  parameters.put("bloque1.1", rb.getString("reporte.memorando.bloque1.1"));
				  parameters.put("bloque2", rb.getString("reporte.memorando.bloque2"));
				  parameters.put("bloque3", rb.getString("reporte.memorando.bloque3"));
				  parameters.put("bloque5", rb.getString("reporte.memorando.bloque5"));
				  parameters.put("bloque5.1", rb.getString("reporte.memorando.bloque5.1"));
				  parameters.put("atentamentefirma", rb.getString("reporte.memorando.atentamentefirma"));
				  parameters.put("departamentofirma", rb.getString("reporte.memorando.departamentofirma"));
				  
 
				  
				  
				jasperPrint = JasperFillManager.fillReport(jasperReport,
						parameters, new JRBeanCollectionDataSource(
								collection));
				JasperExportManager.exportReportToPdfFile(jasperPrint, fileOut);
				JasperViewer.viewReport(jasperPrint);
			} catch (Exception ex) {
				System.out.println("EXCEPTION: " + ex);
			}
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
