/**
 * 
 */
package com.bcv.dao.jdbc;

import java.sql.SQLException;
import java.util.List;

import com.bcv.model.Trabajador;

import ve.org.bcv.rhei.bean.Proveedor;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 02/07/2015 10:00:14
 * 2015
 * mail : oraclefedora@gmail.com
 */
public interface TrabajadorDao  extends SimpleJDBCDao<Trabajador> {
	 List<String> cargarTipoTrabajador() throws SQLException;
	 List<String> cargarCompania(String companiaAnalista) throws SQLException;
	 List<String> buscarParametro(String compania, String tipoEmp, String tipoBeneficio, String nombreParametro) throws SQLException;
	 List<String> buscarFiltros(int cedula) throws SQLException;
	 List<String> buscarFiltros(int codigoEmpleado, String tipoNomina) throws SQLException;

	String codigoEmpleadoBycedula(int cedula) throws SQLException;
	String cedulaByNroFactura(String nu_factura) throws SQLException;
	Trabajador buscarTrabajador(String busqueda, int codigoEmpleado,
			int cedula, String tipoNomina) throws SQLException;
	Trabajador buscarTrabajador(int cedula) throws SQLException ;
	String consultar(String companiaAnalista, int cedula) throws SQLException;
	String consultar(int nro_solicitud, String companiaAnalista, String tipoNomina) throws SQLException;
	boolean consultar(String companiaAnalista, String operacion, int numSolicitud,String tipoNomina) throws SQLException;
	String obtenerTipoNomina(int cedula) throws SQLException ;
	boolean consultar(String companiaAnalista, String operacion,
			int cedula) throws SQLException ;
	Trabajador buscarTrabajadorByCodEmp(int codigoEmpleado) throws SQLException ;
	String codigoEmpleadoByNuSolicitud(int numSolicitud) throws SQLException ;
	String cedulaFamiliarByNuSolicitud(int numSolicitud) throws SQLException;
	  String SituacionEmpleadosGeneral(int cedula) throws SQLException;
		 List<Proveedor> buscarCedulas(String nombre1,String apellido1);
}
