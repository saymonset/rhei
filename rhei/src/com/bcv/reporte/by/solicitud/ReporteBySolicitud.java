package com.bcv.reporte.by.solicitud;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
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

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.bcv.reporte.relacionpago.FacturaRpago3Bean;

import ve.org.bcv.rhei.bean.DocumentosBean;
import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.report.by.benef.Reporte;

/**
 * Gweneramos el reporte por solicitud
 * @author sirodrig
 *
 */
public class ReporteBySolicitud    implements Reporte,Serializable {
	private static final long serialVersionUID = 1L;
	private ShowResultToView showResultToView;
	private List<ShowResultToView> objs=null;
	private static Logger log = Logger
			.getLogger(ReporteBySolicitud.class.getName());
	/**
	 * 
	 */
 
   
 

	public ReporteBySolicitud(ShowResultToView showResultToView){
	 this.showResultToView= showResultToView;
	}
	/**
	 * 
	 */

	 
	
	 

	 
	public DocumentosBean generar(InputStream jrxml, Map<String, Object> parameters,
			String fileOut) {
		DocumentosBean documentosBean = new DocumentosBean();
		try {
			if ( showResultToView!=null && !StringUtils.isEmpty(showResultToView.getCedula())){
		 
				
				JasperDesign jasperDesign = null;
				JasperReport jasperReport = null;
				JasperPrint jasperPrint = null;
//				/**Load the JRXML TO GENERATE JASPER REPORT*/
				jasperDesign = JRXmlLoader.load(jrxml);
//				/**Load the JRXML TO GENERATE JASPER REPORT*/
				/**put the datasource and the parameters en jasperreport*/
				jasperReport = JasperCompileManager.compileReport(jasperDesign);
				/**send to print or xls, pdf, csv etc*/
 
			
				/**Chequearemos que la factura tenga un nuemro de control por lo menos..*/
				try {
					
				 
						/**Fin Chequearemos que la factura tenga un nuemro de control por lo menos..*/
						 
						Collection collection = searchObjects();
						jasperPrint = JasperFillManager.fillReport(jasperReport,
								parameters, new JRBeanCollectionDataSource(
										collection));
						 byte[]bytes=JasperExportManager.exportReportToPdf( jasperPrint);
						 InputStream myInputStream = new ByteArrayInputStream(bytes); 
						documentosBean.setInputStream(myInputStream);
						documentosBean.setDocFileWithExtension(fileOut);
						/**Tendremos el reorte para mostrarlo con un servlet con el propertie documentosBean*/
						getShowResultToView().setDocumentosBean(documentosBean);
						 
				 
				} catch (Exception e) {
					 
				}
				
				
				
			}else{
				
			}
			
		} catch (Exception ex) {
			System.out.println("EXCEPTION: " + ex);
		}
		return null;
	}

	@Override
	public List<ShowResultToView>  searchObjects() {
		objs=new ArrayList<ShowResultToView>();
		objs.add(showResultToView);
		return objs;
	}

	public ShowResultToView getShowResultToView() {
		return showResultToView;
	}

	public void setShowResultToView(ShowResultToView showResultToView) {
		this.showResultToView = showResultToView;
	}

	public static Logger getLog() {
		return log;
	}

	public static void setLog(Logger log) {
		ReporteBySolicitud.log = log;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public ShowResultToView ejecutar() throws ServletException, IOException {
		return this.getShowResultToView();
	}
	public List<FacturaRpago3Bean> searchFacturas(String descripPeriodo,
			long nuSolicitud, String companiaAnalista,
			String filtrarByMesOrComplementoOrAmbos, int receptorPago,
			int coFormaPago, String meses,int coRepStatus) {
		// TODO Auto-generated method stub
		return null;
	}
}
