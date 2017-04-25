/**
 * 
 */
package com.bcv.dao.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bcv.dao.jdbc.CompaniaDao;
import com.bcv.dao.jdbc.ManejadorDB;
import com.bcv.dao.jdbc.SimpleJDBCDaoImpl;
import com.bcv.model.Compania;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 08/09/2015 10:00:57
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class CompaniaDaoImpl  extends SimpleJDBCDaoImpl<Compania> implements CompaniaDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public Compania consultarCompania(String codigoCia) throws SQLException {
		Compania compania = null;
		ManejadorDB manejadorDB=new ManejadorDB();;
		Connection con = manejadorDB.coneccionPool();	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder("");
		try {
			sql.append(" SELECT C.CODIGO_CIA, INITCAP(C.NOMBRE_CIA) as NOMBRE_CIA FROM PERSONAL.COMPANIAS ");
			sql.append(" C WHERE C.CODIGO_CIA=?  ");
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, codigoCia);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				compania = new Compania();
				compania.setNombreCia(rs.getString("NOMBRE_CIA"));
				compania.setCodigoCia(rs.getString("CODIGO_CIA"));
			}
		}

		catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return compania;
	}
	
}
