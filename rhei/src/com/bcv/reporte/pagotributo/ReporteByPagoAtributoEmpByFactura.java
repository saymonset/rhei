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
 * 25/08/2015 13:53:38
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class ReporteByPagoAtributoEmpByFactura implements Reporte,Serializable {
	
	 

 
	private String descripPeriodo;
	private String companiaAnalista;
	private String numSolicituds;
	private String nuFactura;
	
   private DocumentosBean documentosBean = new DocumentosBean();
   private ShowResultToView showResultToView;
   private double montoTotal;
   private List<PagoTributoBeanEmp>  list=null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public ReporteByPagoAtributoEmpByFactura(String descripPeriodo,String companiaAnalista,String numSolicituds,String nuFactura ){
		this.numSolicituds=numSolicituds;
		this.nuFactura=nuFactura;
		this.descripPeriodo=descripPeriodo;
		this.companiaAnalista=companiaAnalista;
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
					List<PagoTributoBeanEmp> objs=searchObjects();
						Collection collection = objs;
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
			System.out.println("EXCEPTION: " + ex.toString());
		}
		return documentosBean;
		
	}
	
	

	 
	public List<PagoTributoBeanEmp> searchObjects() {
		
		if (list==null || list.isEmpty() || list.size()==0){
			 Reporte reporte= null;
			 reporte= new ReporteByRpago( descripPeriodo,companiaAnalista,numSolicituds,nuFactura);
				List<ProveedorRpago1Bean> objs=(List<ProveedorRpago1Bean>) reporte.searchObjects();
				
				
				/** Buscamos la data en BD*/
				/**Buscaremos todos los objetos*****/
			 
				PagoTributoBeanEmp pagoTributoEmp=null;
				List<PagoTributoBeanEmp> pagoTributoEmpLst= new ArrayList<PagoTributoBeanEmp>();
				  montoTotal=0;
				for (ProveedorRpago1Bean pb:objs){
					List<FamiliarRpago2Bean> familiares=pb.getFamiliares();
					for(FamiliarRpago2Bean frb:familiares){
						List<FacturaRpago3Bean> facturas = frb.getFacturas();
						for (FacturaRpago3Bean fact:facturas){
							pagoTributoEmp= new PagoTributoBeanEmp();
							pagoTributoEmp.setNombreColegio(pb.getNbProveedor());
							pagoTributoEmp.setCedula(pb.getCedula());
							pagoTributoEmp.setTrabajador(pb.getTrabajador());
							pagoTributoEmp.setNombreNino(frb.getNombreFlia());
							pagoTributoEmp.setMontoMensualPagTrab(fact.getMontoPeriodoBCV());
							pagoTributoEmp.setConcepto(fact.getTxObservaciones()+", "+fact.getTxConceptoPago());
							pagoTributoEmp.setMonto(fact.getMoFactura()+fact.getMoPagoAdicional());
							pagoTributoEmp.setCodigo(Constantes.DBS+"-"+pb.getNuRifProveedor()+"-"+pb.getDescripPeriodo());
							montoTotal+=pagoTributoEmp.getMonto();
							pagoTributoEmp.setMontoTotal(montoTotal);
							pagoTributoEmpLst.add(pagoTributoEmp);
						}
					}
					
				}
			
			list= pagoTributoEmpLst;	
			
			
		}

		
		
			
			
			
			
		return list ;
	}

	/* (non-Javadoc)
	 * @see ve.org.bcv.rhei.report.by.benef.Reporte#ejecutar()
	 */
	@Override
	public ShowResultToView ejecutar() throws ServletException, IOException {
		if (showResultToView==null){
			showResultToView=new ShowResultToView();
		}
			list= searchObjects();	
		
		showResultToView.setMontoTotal(montoTotal);
		return showResultToView;
	}



	public ShowResultToView getShowResultToView() {
		return showResultToView;
	}



	public void setShowResultToView(ShowResultToView showResultToView) {
		this.showResultToView = showResultToView;
	}

	public List<FacturaRpago3Bean> searchFacturas(String descripPeriodo,
			long nuSolicitud, String companiaAnalista,
			String filtrarByMesOrComplementoOrAmbos, int receptorPago,
			int coFormaPago, String meses,int coRepStatus) {
		// TODO Auto-generated method stub
		return null;
	}

}
