/**
 * 
 */
package com.bcv.services;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 30/10/2015 09:45:08
 * 2015
 * mail : oraclefedora@gmail.com
 */
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import ve.org.bcv.rhei.report.by.benef.Reporte;
import ve.org.bcv.rhei.util.Constantes;

import com.bcv.dao.jdbc.RelacionDePagosDao;
import com.bcv.dao.jdbc.impl.RelacionDePagosDaoImpl;
import com.bcv.model.RelacionDePagos;
import com.bcv.model.ReporteByDefinitivoOrTransitivo;
import com.bcv.reporte.relacionpago.ProveedorRpago1Bean;
import com.bcv.reporte.relacionpago.ReporteByRpago;
@Path("/consultaPagoServicio")
public class ConsultaServicio {
 
	 private RelacionDePagosDao relacionDePagosDao=new RelacionDePagosDaoImpl();
	 @GET
		@Path("/get/{status}/{cedulaEmpleado}/{cedulaFamiliar}")
		@Produces("application/json")
		public ReporteByDefinitivoOrTransitivo getProductInJSON(@PathParam("status") final String status,@PathParam("cedulaEmpleado") final String cedulaEmpleado,@PathParam("cedulaFamiliar") final String cedulaFamiliar) throws SQLException {

		 ReporteByDefinitivoOrTransitivo reporteByDefinitivoOrTransitivo = new ReporteByDefinitivoOrTransitivo();
			RelacionDePagos relacionDePagos=relacionDePagosDao.lastNumSolicitudPagoByCedula(status,cedulaEmpleado,cedulaFamiliar);
			if (relacionDePagos!=null){
				String numSolicitud=relacionDePagos.getNroSolicitudsSeparatedByComa();
				 reporteByDefinitivoOrTransitivo =relacionDePagosDao.reporteByConsulta(numSolicitud,"01"); 
			}
			return reporteByDefinitivoOrTransitivo; 
		}
	 

	 @GET
		@Path("/get/{status}/{cedulaEmpleado}/{cedulaFamiliar}/{compania}")
		@Produces("application/json")
		public List<ProveedorRpago1Bean> relacionPagosJSON(@PathParam("status") final String status,@PathParam("cedulaEmpleado") final String cedulaEmpleado,
				@PathParam("cedulaFamiliar") final String cedulaFamiliar,@PathParam("compania") final String compania) throws SQLException {
		 Reporte reporte = null;
		 List<ProveedorRpago1Bean> lista=null;
		 ReporteByDefinitivoOrTransitivo reporteByDefinitivoOrTransitivo = new ReporteByDefinitivoOrTransitivo();
			RelacionDePagos relacionDePagos=relacionDePagosDao.lastNumSolicitudPagoByCedula(status,cedulaEmpleado,cedulaFamiliar);
			if (relacionDePagos!=null){
				String numSolicitud=relacionDePagos.getNroSolicitudsSeparatedByComa();
				 reporteByDefinitivoOrTransitivo =relacionDePagosDao.reporteByConsulta(numSolicitud,compania); 
				 if (reporteByDefinitivoOrTransitivo!=null){
					 String ninoEspecial="";
						 int coRepStatus=0;         
						 String isToCreateReportDefinitivo=Constantes.PAGADO_NOPAGADO_AMBOS;
						 reporte= new ReporteByRpago(  reporteByDefinitivoOrTransitivo.getDescripPeriodo(), reporteByDefinitivoOrTransitivo.getCompaniaAnalista() , reporteByDefinitivoOrTransitivo.getReceptorPago(), reporteByDefinitivoOrTransitivo.getCoFormaPago1(), reporteByDefinitivoOrTransitivo.getNroSolicitud() , reporteByDefinitivoOrTransitivo.getCoStatus(),reporteByDefinitivoOrTransitivo.getTipoEmpleado(),reporteByDefinitivoOrTransitivo.getMeses(),reporteByDefinitivoOrTransitivo.getFiltrarByMesOrComplementoOrAmbos()+"",coRepStatus,isToCreateReportDefinitivo,ninoEspecial);
						 lista= (List<ProveedorRpago1Bean>)reporte.searchObjects();
					 
				 }
			}
			return lista; 
		}
	 
	 
	// 0 CENTRO DE EDUCACION INICIAL, 1 TRABAJADOR
				
				
			 
}
