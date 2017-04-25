/**
 * 
 */
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
import ve.org.bcv.rhei.util.Constantes;

import com.bcv.reporte.relacionpago.FacturaRpago3Bean;
import com.bcv.reporte.relacionpago.FamiliarRpago2Bean;
import com.bcv.reporte.relacionpago.ProveedorRpago1Bean;
import com.bcv.reporte.relacionpago.ReporteByRpago;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 24/08/2015 09:21:45
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class MemorandoReport  implements Reporte,Serializable {
	
	 
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private DocumentosBean documentosBean = new DocumentosBean();

	/** 
	 * numSolicituds is a String large . it is contain values of numSolictud Consult example 1,2,3,4,,598.201,333
	 * Metdo que me dara reportes por reembolso o complemento
	 * @param descripPeriodo
	 * @param companiaAnalista
	 * @param receptorPago
	 */
	public MemorandoReport( ){
		 
	}

	
	
	public DocumentosBean generar(InputStream jrxml, Map<String, Object> parameters,
			String fileOut) {
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
				/**Chequearemos que la factura tenga un nuemro de control por lo menos..*/
				try {
					/**Buscaremos todos los objetos*****/
					
						Collection collection = searchObjects();
						jasperPrint = JasperFillManager.fillReport(jasperReport,
								parameters, new JRBeanCollectionDataSource(
										collection));
						 byte[]bytes=JasperExportManager.exportReportToPdf( jasperPrint);
						 InputStream myInputStream = new ByteArrayInputStream(bytes); 
						 documentosBean = new DocumentosBean();
						documentosBean.setInputStream(myInputStream);
						documentosBean.setDocFileWithExtension(fileOut);
				} catch (Exception e) {
				}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("EXCEPTION: " + ex.toString());
		}
		return documentosBean;
		
	}
	
	

	 
	public List<MemorandoBean> searchObjects() {
		List<MemorandoBean> objs= new ArrayList<MemorandoBean>();
		MemorandoBean memorandoBean = new MemorandoBean();
		memorandoBean.setMonto(39555d);
		objs.add(memorandoBean);
		return objs;
	}

	/* (non-Javadoc)
	 * @see ve.org.bcv.rhei.report.by.benef.Reporte#ejecutar()
	 */
	@Override
	public ShowResultToView ejecutar() throws ServletException, IOException {
		 
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
