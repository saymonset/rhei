/**
 * 
 */
package com.bcv.dao.jdbc;

import java.sql.SQLException;
import java.util.List;

import com.bcv.model.Factura;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 03/07/2015 14:23:13
 * 2015
 * mail : oraclefedora@gmail.com
 */
public interface FacturaDao extends SimpleJDBCDao<Factura> {
	Factura consultarFacturaByNuSolNuRefPag(String nroFactura,int nuSolicitud,int nuRefPago) throws SQLException ;
	 String buscarFacturaByMesNroSolictud(int nroSolicitud, int mes,String periodo) throws SQLException ;
	 int updateFactura(  Double montoFactura ,String nroFactura,String nroControl,int nroIdFactura) throws SQLException ;
	 Factura consultarFacturaById(int nroIdFactura) throws SQLException ;
	 Factura consultarFactura(String nroFactura, int nroSolicitud,String periodoScolar) throws SQLException ;
	 int buscarIdFacturaByNuFactura(String nroFactura) throws SQLException ;
	 List<Integer> mesesPorIdFactura(int nroIdFactura) throws SQLException;
	  List<Factura> mesesPorNroFactura(String nroFactura) throws SQLException ;
	 List<Factura> consultarFacturasByNroSolicitudId(int nroIdSolicitud,int coRepStatus)
				throws SQLException;
	 List<Long> facturasByNroSolicitud(long nuSolicitud,String nufactura)throws SQLException;
	 List<Factura> factByIdFacRecPagoFormPagoPerNumSolComp(long facturasId,int receptorPago,int coFormaPago
				,String descripPeriodo,long nuSolicitud,String companiaAnalista,String meses, String filtrarByMesOrComplementoOrAmbos)throws SQLException;;
				 String buscarFacturaByMesNroSolictud(int nroSolicitud,String periodo) throws SQLException ;
				 int deleteFactura(int nuIdFactura)throws SQLException;
				 List<Factura> facturasByNumSolicitudMatriculaPeriodo(
							long numSolicitud, String inComplemento, boolean isActualizar,String periodoScolar) throws SQLException ;
				 List<Factura> mesesPorNroFactura(String nroFactura,String complemento)
							throws SQLException ;
}
