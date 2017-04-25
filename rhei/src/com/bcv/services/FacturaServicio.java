/**
 * 
 */
package com.bcv.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.bcv.dao.jdbc.impl.FacturaDaoImpl;
import com.bcv.model.Factura;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 30/10/2015 13:06:36
 * 2015
 * mail : oraclefedora@gmail.com
 */
//@Path("/facturaServicio")
public class FacturaServicio {
private FacturaDaoImpl facturaDao= new FacturaDaoImpl();
	 @GET
		@Path("/get/{nroIdSolicitud}")
		@Produces("application/json")
		public List<Factura> getProductInJSON(@PathParam("nroIdSolicitud") final int nroIdSolicitud) throws SQLException {
		 List<Factura> facturas=null;
		  boolean isActualizar=false;
//			List<Factura> facturasNoComplementos=facturaDao .facturasByNumSolicitudMatriculaPeriodo(nroIdSolicitud, Constantes.NO_IN_COMPLEMENTO,isActualizar);
//			List<Factura> facturasStanConComplementos=facturaDao .facturasByNumSolicitudMatriculaPeriodo(nroIdSolicitud, Constantes.IN_COMPLEMENTO,isActualizar);
			 facturas = new ArrayList<Factura>();
//			 facturas.addAll(facturasNoComplementos);
//			 facturas.addAll(facturasStanConComplementos);
		  
			return facturas; 
		}
}
