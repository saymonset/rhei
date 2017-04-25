/**
 * 
 */
package com.bcv.dao.jdbc;

import java.sql.SQLException;

import com.bcv.model.EmpleadoInfo;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 30/09/2015 11:02:04
 * 2015
 * mail : oraclefedora@gmail.com
 */
public interface EmpleadoInfoDAO {
	 EmpleadoInfo empleadoDireccion(int cedula) throws SQLException ;
	 EmpleadoInfo contratoFechas(int cedula) throws SQLException ;
	 boolean isVigenteEmpleado(int cedula) throws SQLException ;
}
