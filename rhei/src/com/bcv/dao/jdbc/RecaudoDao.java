/**
 * 
 */
package com.bcv.dao.jdbc;

import java.sql.SQLException;
import java.util.List;

import ve.org.bcv.rhei.bean.Recaudo;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 22/07/2015 14:23:04
 * 2015
 * mail : oraclefedora@gmail.com
 */
public interface RecaudoDao extends SimpleJDBCDao<Recaudo> {
	 List<Recaudo> recaudosLst() throws SQLException;
	 Recaudo recaudosByName(String name) throws SQLException ;

}
