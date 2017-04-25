/**
 * 
 */
package com.bcv.reporte.pagotributo;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
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
import ve.org.bcv.rhei.util.Constantes;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 21/08/2015 13:38:38
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class ReporteByPagoAndTributoCEITest  implements Reporte {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 
	 private static 	String numSolicitud="517,518,519,520,521,522,523,524,525,526,527,528";
	 private static String coStatus="A";
	 private static String tipoEmpleado="OBR";
     private static String descripPeriodo="2019-2020";
     private static String companiaAnalista="01";
     private static String meses="12";//"12,14,01,02,03,04,05,06,07,08,09,10,11";
  	private static String filtrarByMesOrComplementoOrAmbos="0";
     private static int coFormaPago=1;
     private static int receptorPago=0;
//     private int receptorPago=-1;// 0 CENTRO DE EDUCACION INICIAL, 1 TRABAJADOR
//     private int coFormaPago=-1; //1 ES CHEQUE, 2 ES AVISO DE CREDITO

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
				jasperDesign = JRXmlLoader.load(jrxml);
//				/**Load the JRXML TO GENERATE JASPER REPORT*/
				/**put the datasource and the parameters en jasperreport*/
				jasperReport = JasperCompileManager.compileReport(jasperDesign);
				/**send to print or xls, pdf, csv etc*/
				int coRepStatus=0;
				String isToCreateReportDefinitivo=Constantes.PAGADO_NOPAGADO_AMBOS;
				String special="0";
				 reporte= new ReporteByPagoAtributoCEI(  descripPeriodo,  companiaAnalista,  receptorPago,  coFormaPago,  numSolicitud,  coStatus ,tipoEmpleado,meses,filtrarByMesOrComplementoOrAmbos,coRepStatus, isToCreateReportDefinitivo,special);
				List<PagoTributoCEI> objs=(List<PagoTributoCEI>) reporte.searchObjects();
				 
				
				Collection collection = objs;
				double total=0.0d;
				if (objs!=null){
					total = objs.get(0).getMontoTotal();
				}
				  Map parameters = new HashMap();
				  
				  ReportePathLogo archivo = new ReportePathLogo();
	    		    InputStream is = archivo.getClass().getResourceAsStream(
					"logo_bcv.jpg"); 
	    		    parameters.put("logo",is);
	    		    parameters.put("titulo",rb.getString("reporte.cei.titulo"));
	    		    parameters.put("total",total);
				  parameters.put("descripPeriodo", "2016-2017");
				   parameters.put("cordAdministrativo",rb.getString("reporte.cordAdministrativo"));
				   parameters.put("cordUnidadContabilidad",rb.getString("reporte.cordUnidadContabilidad"));
				   parameters.put("cordBenefSocioEconomic",rb.getString("reporte.cordBenefSocioEconomic"));
				   
				   /** modificado por david */
				   parameters.put("cordBenefSocioEconomic",rb.getString("reporte.cordBenefSocioEconomic"));
				   /** modificado por david */
				   
				  jrxml = PagoTributoBean.class.getResourceAsStream(
							"reportPagoAndTributosCEI.jrxml");
//				  DocumentosBean documentosBean=reporte.generar(jrxml, parameters, "reporte.pdf");
//				 File fileDetalle=pdf.inputStreamToFile(documentosBean.getInputStream());
				 
				 
		 
				  
				  
				jasperPrint = JasperFillManager.fillReport(jasperReport,
						parameters, new JRBeanCollectionDataSource(
								collection));
				JasperExportManager.exportReportToPdfFile(jasperPrint, fileOut);
				JasperViewer.viewReport(jasperPrint);
			} catch (Exception ex) {
				System.out.println("EXCEPTION: " + ex);
			}
	}
	
	private static boolean isReportImageOK(InputStream logoFile) //throws Exception
	{
	boolean isImageOk = false;

	Image image = null;
	try
	{
	image = ImageIO.read(logoFile);
	//logoFile.reset();
	}
	catch (IOException e)
	{
		e.printStackTrace();
	System.out.println("IO exception ");
	//throw new Exception(e);
	}

	if (image == null)
	{
		System.out.println("Image incorrect ");
	}
	else
	{
		System.out.println("Image correct ");
	isImageOk = true;
	}

	return isImageOk;
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
