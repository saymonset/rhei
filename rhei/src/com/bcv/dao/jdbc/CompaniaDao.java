/**
 * 
 */
package com.bcv.dao.jdbc;

import java.sql.SQLException;

import com.bcv.model.Compania;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 08/09/2015 10:00:19
 * 2015
 * mail : oraclefedora@gmail.com
 */
public interface CompaniaDao {
	Compania consultarCompania(String codigoCia) throws SQLException ;

}
