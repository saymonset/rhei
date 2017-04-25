package com.bcv.reporte.by.solicitud;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
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

import org.apache.log4j.Logger;

import com.bcv.reporte.by.solicitud.ReporteBySolicitud;
import com.bcv.reporte.relacionpago.FacturaRpago3Bean;

import ve.org.bcv.rhei.bean.DocumentosBean;
import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.report.by.benef.EmpleadosBean1;
import ve.org.bcv.rhei.report.by.benef.Reporte;

/**
 * @author sirodrig
 *
 */
public class ReporteBySolicitudTestImpl implements Reporte {
	private ShowResultToView showResultToView;
	private static Logger log = Logger.getLogger(ReporteBySolicitudTestImpl.class
			.getName());
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	   
	  

	public ReporteBySolicitudTestImpl(ShowResultToView showResultToView) {
		super();
         this.showResultToView=showResultToView;
	}
	  
	
	   
	
	
	public DocumentosBean generar(InputStream jrxml,
			Map<String, Object> parameters, String fileOut) {
		try {
			JasperDesign jasperDesign = null;
			JasperReport jasperReport = null;
			JasperPrint jasperPrint = null;
//			/**Load the JRXML TO GENERATE JASPER REPORT*/
			jasperDesign = JRXmlLoader.load(jrxml);
//			/**Load the JRXML TO GENERATE JASPER REPORT*/
			/**put the datasource and the parameters en jasperreport*/
			jasperReport = JasperCompileManager.compileReport(jasperDesign);
			/**send to print or xls, pdf, csv etc*/
			
			ReporteBySolicitud reporte= new ReporteBySolicitud( this.showResultToView);
			List<ShowResultToView> objs=reporte.searchObjects();
			@SuppressWarnings("rawtypes")
			Collection collection = objs;
			jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
			jasperPrint = JasperFillManager.fillReport(jasperReport,
					parameters, new JRBeanCollectionDataSource(
							collection));
			JasperExportManager.exportReportToPdfFile(jasperPrint, fileOut);
			JasperViewer.viewReport(jasperPrint);
		} catch (Exception ex) {
			System.out.println("EXCEPTION: " + ex);
		}
		return null;
	}









	@Override
	public List<EmpleadosBean1> searchObjects() {
		// TODO Auto-generated method stub
		return null;
	}





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
			int coFormaPago, String meses,int coRepStatu) {
		// TODO Auto-generated method stub
		return null;
	}




 




 




 






 
}
