package com.bcv.reporte.pagotributo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
import ve.org.bcv.rhei.bean.DocumentosBean;
import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.report.by.benef.Reporte;

public class ReporteBecaUtileGenerarReporte  implements Reporte, Serializable {
	private String descripPeriodo;
	private String  status;
	private boolean isListado;
	private DocumentosBean documentosBean = new DocumentosBean();
	 private List<com.bcv.model.ReporteBecaUtile> objs ; 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ReporteBecaUtileGenerarReporte(String descripPeriodo,String  status,boolean isListado,List<com.bcv.model.ReporteBecaUtile> objs  ) {
		super();
		this.descripPeriodo = descripPeriodo;
		this.status=status;
		this.isListado=isListado;
		this.objs=objs;
	}

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
			 	List<com.bcv.model.ReporteBecaUtile> objAuxs=new ArrayList<com.bcv.model.ReporteBecaUtile>();
			 	if (objs==null){
			 		objs.add(new com.bcv.model.ReporteBecaUtile());
			 	}
				if (isListado){
					objAuxs.addAll(objs);
				}else{
					if (objs!=null){
						/**Solo necesitamos un objeto para que el reporte salga una sola vez */
						try {
							objAuxs.add(objs.get(0));	
						} catch (Exception e) {
							//objs.add(new com.bcv.model.ReporteBecaUtile());
						}
						
					}
				}
	
				Collection collection = objAuxs;
				jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(collection));
				byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
				InputStream myInputStream = new ByteArrayInputStream(bytes);

				documentosBean = new DocumentosBean();
				documentosBean.setInputStream(myInputStream);
				documentosBean.setDocFileWithExtension(fileOut);
			} catch (Exception e) {
				System.out.println("EXCEPTION 0: " + e.toString());
			}
		} catch (Exception ex) {
			System.out.println("EXCEPTION: " + ex.toString());
		}
		return documentosBean;

	}


	public List<ReporteBecaUtileGenerarReporte> searchObjects() {
	 
		return null;
	}
	
	
	
	
	
	

	public ShowResultToView ejecutar() throws ServletException, IOException {
	 
		return null;
	}

	public List<FacturaRpago3Bean> searchFacturas(String descripPeriodo, long nuSolicitud, String companiaAnalista, String filtrarByMesOrComplementoOrAmbos, int receptorPago, int coFormaPago,
			String meses, int coRepStatus) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDescripPeriodo() {
		return descripPeriodo;
	}

	public void setDescripPeriodo(String descripPeriodo) {
		this.descripPeriodo = descripPeriodo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public DocumentosBean getDocumentosBean() {
		return documentosBean;
	}

	public void setDocumentosBean(DocumentosBean documentosBean) {
		this.documentosBean = documentosBean;
	}

 

	public boolean isListado() {
		return isListado;
	}

	public void setListado(boolean isListado) {
		this.isListado = isListado;
	}



}
