/**
 * 
 */
package com.bcv.dao.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.report.by.benef.BeneficiarioBean2;

import com.bcv.dto.NuRefPago;
import com.bcv.model.Factura;
import com.bcv.model.RelacionDePagos;
import com.bcv.model.ReporteByDefinitivoOrTransitivo;
import com.bcv.reporte.relacionpago.ProveedorRpago1Bean;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco 03/07/2015 14:49:35 2015 mail :
 *         oraclefedora@gmail.com
 */
public interface RelacionDePagosDao extends SimpleJDBCDao<RelacionDePagos> {
	//				String special="0";
	List<ProveedorRpago1Bean> searchSolicitudesToReporte(int coFormaPago,
			int receptorPago, String descripPeriodo, String numSolicituds,
			String coStatus,String tipoEmpleado,String isToCreateReportDefinitivo,String ninoEspecial);
	List<Integer> searchSolicitudesToReporte(int coFormaPago, int receptorPago,
			String descripcionPeriodo);
	List<String> obtenerMesesPagados(int nroSolicitud, String periodoEscolar,
			int mes, int tipoDePago) throws SQLException;
	List<String> obtenerMesesPagados(int nroSolicitud, int tipoDePago)
			throws SQLException;
	List<String> obtenerMesesPagados(int nroSolicitud, int tipoDePago,
			int mesDeRegistro, String nroFactura, int idFactura)
			throws SQLException;
	Double calcularMontoFactura(int nroSolicitud, int mesDeRegistro,
			String nroFactura, int idFactura, int tipoDePago)
			throws SQLException;
	int obtenerReceptorPago(int nroSolicitud, int mesDeRegistro,
			String nroFactura, int tipoDePago) throws SQLException;
	List<BeneficiarioBean2> obtenerRelacionPagoYfactura(int nroSolicitud,
			int nuIdFactura, int inMesMatricula, String periodoEscolar,
			ShowResultToView showResultToView) throws SQLException;
	List<BeneficiarioBean2> obtenerRelacionPagoYfactura(int nroSolicitud,
			int nuIdFactura, String periodoEscolar,
			ShowResultToView showResultToView) throws SQLException;
	int obtenerReceptorPago(int nroSolicitud, int mesProrrateo,
			int periodoEscolar) throws SQLException;
	String guardarPago(Factura factura, Double montoBCV, Double montoMatriculaBCV,
			Double montoPeriodoBCV, int tipoDePago, String observaciones,
			List<Integer> mesMatriMen, int nroSolicitud,
			List<Integer> conceptoPago, int receptorPago, String tramite,
			int codigoPeriodo, String coFormaPago, String isPagado,String showCheckCompklement, String[] mesecomplemento,Map<String,Double> mesesPorComplIndividualMonto,
			String nombreUsuario)
			throws SQLException;
	boolean verificarPagoMatricula(int nroSolicitudVigente,
			String txDescriPeriodo) throws SQLException;
	String borrarPago(Factura factura, String[] mesesMatriPeriod,
			int codigoPeriodo, int nroSolicitud) throws SQLException;
	int updatePago(Double montoTotal, int nroIdFactura, int nroSolicitud,
			int receptorPago, int InMesMatricula, String formaPago)
			throws SQLException;
	void cargarAtributosPago(HttpServletRequest request, Factura factura,
			String tramite, Double montoTotal, int receptorPago, int tipoPago,
			String observaciones);
	String guardarCambiosEnPago(Factura factura, Double montoMatriculaBCV,
			Double montoPeriodoBCV, int tipoDePago, String observaciones,
			Vector<Integer> mesMatriMen, int nroSolicitud,
			Vector<Integer> conceptoPago, int receptorPago, String tramite,
			Timestamp fechaRegistroDePago, int codigoPeriodo)
			throws SQLException;
	Factura buscarDatosPago(int nroSolicitud, int mesDeBusqueda,
			String nroFactura, int receptorPago, int tipoDePago,
			RelacionDePagos relacionDePagos) throws SQLException;
	Double calcularMontoPagoBCV(Double montoMaximoBCV,
			String beneficioCompartido, Double montoConceptoPago);
	ShowResultToView buscardataPagoFactura(int nroSolicitud, int mesDeRegistro,
			int codigoPeriodo, int receptorPago,
			ShowResultToView showResultToView) throws SQLException;

	boolean verificarFactura(int nroSolicitud, int mesDeRegistro,
			int codigoPeriodo, int receptorPago) throws SQLException;
	double calcularMontoPago(int nroSolicitud, int codigoPeriodo, int tipoDePago)
			throws SQLException;
	int calcularCantidadRegistros(int nroSolicitud, int codigoPeriodo,
			int tipoDePago) throws SQLException;
	boolean verificarPagoMes(int nroSolicitud, int tipoDePago, int mes)
			throws SQLException;
	 int deletePago(int nuSolicitud,int nuIdFactura,int nuRefPago,String inComplemento)
				throws SQLException;
	 void SearchMontoBCV(int nuSolicitud,double montoBcv,String inBenefCompart,double moMatricula,double moPeriodo,Connection con);
	 ReporteByDefinitivoOrTransitivo reporteByDefinitivoOrTransitivo(String nroSolicitud,int coRepStatus) throws SQLException ;
	
	 String listNumSolicitudsReporteDefinitivoTransitorio(int coRepStatus)throws SQLException;
	 boolean existePagoDelBeneficiario(String coStatus,String cedula,String cedFam)throws SQLException;
	 RelacionDePagos numSolicitudPagoDelBeneficiario(String coStatus,String cedula,String cedulaFamiliar)	throws SQLException;
	 RelacionDePagos lastNumSolicitudPagoByCedula(String coStatus,String cedula,String cedulaFamiliar	)			throws SQLException;
	 ReporteByDefinitivoOrTransitivo reporteByConsulta(String nroSolicitud,String companiaAnalista) throws SQLException ; 
	 int deleteFacturaByRelacionPagoPago(int nuIdFactura)
				throws SQLException;
	 String updateToDefinitivo(String nombreReporte, int coFormaPago,
				int receptorPago, String descripPeriodo, 
				String coStatus, String tipoEmpleado,
				String filtrarByMesOrComplementoOrAmbos, String meses,String ninoEspecial,String nombreUsuario)
				throws Exception;
	 List<NuRefPago> nuRefPagosComplHistorico(long nuSolicitud,String meses,String periodoEscolar)throws Exception;
}
