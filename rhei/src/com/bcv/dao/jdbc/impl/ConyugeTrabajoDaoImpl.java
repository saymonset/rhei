 
package com.bcv.dao.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bcv.dao.jdbc.ConyugeTrabajoDao;
import com.bcv.dao.jdbc.ManejadorDB;
import com.bcv.dao.jdbc.SimpleJDBCDaoImpl;
import com.bcv.model.ConyugeTrabajo;
import com.bcv.model.EmpleadoInfo;
import com.bcv.model.Solicitud;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 10/09/2015 15:01:43
 * 2015
 * mail : oraclefedora@gmail.com
 */

public class ConyugeTrabajoDaoImpl  extends SimpleJDBCDaoImpl<ConyugeTrabajo> implements
ConyugeTrabajoDao {

	
	 
	
	private static final long serialVersionUID = 1L;
	
 
	/* (non-Javadoc)
	 * @see com.bcv.dao.jdbc.ConyugeTrabajoDao#guardar(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ConyugeTrabajo guardar(int ciConyuge, String nombreEmpresa,
			String telefonoEmpresa,String correoConyuge,  String nuTlfTrabajo)
			throws SQLException {
		boolean existeInBd=true;
		if (find(ciConyuge)==null){
			existeInBd=false;
		}
		ManejadorDB manejadorDB=new ManejadorDB();;
		Connection con = manejadorDB.coneccionPool();	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder("");
		ConyugeTrabajo  conyugeTrabajo  = new ConyugeTrabajo ();
		try {
			
		
			conyugeTrabajo.setCiConyuge(ciConyuge);
			conyugeTrabajo.setCorreoConyuge(correoConyuge);
			conyugeTrabajo.setNombreEmpresa(nombreEmpresa);
			conyugeTrabajo.setNuTlfTrabajo(nuTlfTrabajo);
			conyugeTrabajo.setTelefonoEmpresa(telefonoEmpresa);
			if (!existeInBd){
				pstmt = con.prepareStatement("SELECT RHEI.SEQ_CO_CONYUGE.NEXTVAL AS valor from dual");
				ResultSet result = pstmt.executeQuery();
				int coConyuge = 0;
				if (result != null) {
				while (result.next()) {
					coConyuge = result.getInt("valor");
				}
					sql.append(" INSERT INTO RHEI.CONYUGE_TRABAJO (CO_CONYUGE,CI_CONYUGE,NB_EMPRESA,NU_TLF_EMPRESA,TX_CORREO_CONY,NU_TLF_TRABAJO) VALUES (?,?,?,?,?,?)");
					pstmt = con.prepareStatement(sql.toString());
					int posicion=1;
					pstmt.setInt(posicion++, coConyuge);
					pstmt.setInt(posicion++, ciConyuge);
					pstmt.setString(posicion++, nombreEmpresa);
					pstmt.setString(posicion++, telefonoEmpresa);
					pstmt.setString(posicion++, correoConyuge);
					pstmt.setString(posicion++, nuTlfTrabajo);
					int resp= pstmt.executeUpdate();
				}
			}else{
				sql.append(" UPDATE RHEI.CONYUGE_TRABAJO SET NB_EMPRESA=?,NU_TLF_EMPRESA=?,TX_CORREO_CONY=?,NU_TLF_TRABAJO=? WHERE CI_CONYUGE=?");
				pstmt = con.prepareStatement(sql.toString());
				int posicion=1;
				pstmt.setString(posicion++, nombreEmpresa);
				pstmt.setString(posicion++, telefonoEmpresa);
				pstmt.setString(posicion++, correoConyuge);
				pstmt.setString(posicion++, nuTlfTrabajo);
				pstmt.setInt(posicion++, ciConyuge);
				int resp= pstmt.executeUpdate();
			}
		
		
			
		 
		}

		catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		
		
		
		
		return conyugeTrabajo;
	}
	
	public ConyugeTrabajo find(int ciConyuge)
			throws SQLException {
		ConyugeTrabajo  conyugeTrabajo =null;
		StringBuilder sql = new StringBuilder("");
	    sql.append(" SELECT CO_CONYUGE,CI_CONYUGE,NB_EMPRESA,NU_TLF_EMPRESA,TX_CORREO_CONY,NU_TLF_TRABAJO ");
	    sql.append("    FROM  RHEI.CONYUGE_TRABAJO CT WHERE CT.CI_CONYUGE=? ");
		Connection con =null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ManejadorDB manejadorDB=new ManejadorDB();;
		try {
			 con = manejadorDB.coneccionPool();

			sql.append("");
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, ciConyuge);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				conyugeTrabajo = new ConyugeTrabajo ();
				conyugeTrabajo.setCodigoConyuge(rs.getInt("CO_CONYUGE"));
				conyugeTrabajo.setCiConyuge(rs.getInt("CI_CONYUGE"));
				conyugeTrabajo.setNombreEmpresa(rs.getString("NB_EMPRESA"));
				conyugeTrabajo.setTelefonoEmpresa(rs.getString("NU_TLF_EMPRESA"));
				conyugeTrabajo.setCorreoConyuge(rs.getString("TX_CORREO_CONY"));
				conyugeTrabajo.setNuTlfTrabajo(rs.getString("NU_TLF_TRABAJO"));
			}
			rs.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}

		return conyugeTrabajo;
	}
	
	
 
 
	
 
}
