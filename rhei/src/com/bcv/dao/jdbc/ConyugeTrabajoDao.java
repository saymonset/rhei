/**
 * 
 */
package com.bcv.dao.jdbc;

import java.sql.SQLException;

import com.bcv.model.ConyugeTrabajo;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 10/09/2015 14:57:34
 * 2015
 * mail : oraclefedora@gmail.com
 */
public interface ConyugeTrabajoDao {
	
	ConyugeTrabajo guardar(int ciConyuge, String nombreEmpresa,
			String telefonoEmpresa,String txCorreoCony,  String nuTlfTrabajo)
			throws SQLException;
	ConyugeTrabajo find(int ciConyuge)
			throws SQLException ;
	
}
