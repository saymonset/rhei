/**
 * 
 */
package com.bcv.dao.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.bcv.dao.jdbc.ManejadorDB;
import com.bcv.dao.jdbc.RecaudoDao;
import com.bcv.dao.jdbc.SimpleJDBCDaoImpl;

import ve.org.bcv.rhei.bean.Recaudo;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 22/07/2015 14:24:57
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class RecaudosDaoImpl extends SimpleJDBCDaoImpl<Recaudo>
implements RecaudoDao {
private static Logger log = Logger.getLogger(RecaudosDaoImpl.class
	.getName());

/**
* 
*/
private static final long serialVersionUID = 1L;

/* (non-Javadoc)
 * @see com.bcv.dao.jdbc.RecaudoDao#recaudosLst()
 */
public List<Recaudo> recaudosLst() throws SQLException {
	ManejadorDB manejadorDB=new ManejadorDB();;
	Connection con = manejadorDB.coneccionPool();	
	PreparedStatement stmt = null;
	ResultSet rs = null;
	List<Recaudo> recaudosLst = new ArrayList<Recaudo>();
	StringBuilder sql = new StringBuilder("");
	/** FIN Colocamos esta variable TRAMITE por default */

	try {
		sql.append(" select  CO_RECAUDO,NB_DOCUMENTO,TX_DOCUMENTO,IN_OBLIGATORIO,IN_ALFRESCO,DI_HOME_ALFRESCO  from RHEI.RECAUDO R ORDER BY R.CO_RECAUDO");
	 

		stmt = con.prepareStatement(sql.toString());
		rs = stmt.executeQuery();
	    Recaudo recaudo =null;
		while (rs.next()) {
			recaudo = new Recaudo();
			
			recaudo = new Recaudo();
			recaudo.setAlfresco(true);
			recaudo.setExt("pdf");
			
			recaudo.setNombre(rs.getString("NB_DOCUMENTO"));
			recaudo.setDescripcion(rs.getString("TX_DOCUMENTO"));
			String obligato= rs.getString("IN_OBLIGATORIO");
			boolean isObligatorio=false;
			if (obligato!=null && "S".equalsIgnoreCase(obligato)){
				isObligatorio=true;
			}
			recaudo.setObligatorio(isObligatorio);
			obligato=null;
			recaudo.setDiHomeAlfresco(rs.getString("DI_HOME_ALFRESCO"));
			recaudosLst.add(recaudo); 

		}
	 

	} catch (SQLException e) {
		e.printStackTrace();
	} finally {
		liberarConexion(rs, stmt, con);
	}
	return recaudosLst;
}


public Recaudo recaudosByName(String name) throws SQLException {
	ManejadorDB manejadorDB=new ManejadorDB();;
	Connection con = manejadorDB.coneccionPool();	
	PreparedStatement stmt = null;
	 Recaudo recaudo =null;
	ResultSet rs = null;
	StringBuilder sql = new StringBuilder("");
	/** FIN Colocamos esta variable TRAMITE por default */

	try {
		sql.append(" select  CO_RECAUDO,NB_DOCUMENTO,TX_DOCUMENTO,IN_OBLIGATORIO,IN_ALFRESCO,DI_HOME_ALFRESCO  from RHEI.RECAUDO R ");
		sql.append("  WHERE R.NB_DOCUMENTO=?");
	 

		stmt = con.prepareStatement(sql.toString());
		int param=1;
		stmt.setString(param++, name);
		rs = stmt.executeQuery();
	   
		if (rs.next()) {
			recaudo = new Recaudo();
			
			recaudo = new Recaudo();
			recaudo.setAlfresco(true);
			recaudo.setExt("pdf");
			
			recaudo.setNombre(rs.getString("NB_DOCUMENTO"));
			recaudo.setDescripcion(rs.getString("TX_DOCUMENTO"));
			String obligato= rs.getString("IN_OBLIGATORIO");
			boolean isObligatorio=false;
			if (obligato!=null && "S".equalsIgnoreCase(obligato)){
				isObligatorio=true;
			}
			recaudo.setObligatorio(isObligatorio);
			obligato=null;
			recaudo.setDiHomeAlfresco(rs.getString("DI_HOME_ALFRESCO"));

		}
	 

	} catch (SQLException e) {
		e.printStackTrace();
	} finally {
		liberarConexion(rs, stmt, con);
	}
	return recaudo;
}

  
 
 

}
