/**
 * 
 */
package com.bcv.dao.jdbc;

import java.sql.SQLException;
import java.util.List;

import com.bcv.model.TipoEmpleado;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 21/08/2015 09:28:37
 * 2015
 * mail : oraclefedora@gmail.com
 */
public interface TipoEmpleadoDao extends SimpleJDBCDao<TipoEmpleado> {
	List<TipoEmpleado> tipoEmpleadosList() throws SQLException ;
	TipoEmpleado tipoEmpleado(String tipoEmp) throws SQLException ;
	String tipoEmpleadoSolo(String cedula) throws SQLException ;
	 List<TipoEmpleado> tipoEmpleadosByReporte(int coRepStatus ) throws SQLException ;

}
