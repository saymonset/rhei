/**
 * 
 */
package com.bcv.dao.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.bcv.dao.jdbc.EmpleadoInfoDAO;
import com.bcv.dao.jdbc.ManejadorDB;
import com.bcv.dao.jdbc.SimpleJDBCDaoImpl;
import com.bcv.model.EmpleadoInfo;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 30/09/2015 11:02:42
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class EmpleadoInfoDAOImpl  extends SimpleJDBCDaoImpl<EmpleadoInfo>  implements EmpleadoInfoDAO {

	
 

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/**
 * Obtenemos la direccion del empleado
 * @param cedula
 * @return
 * @throws SQLException
 */
public EmpleadoInfo empleadoDireccion(int cedula) throws SQLException {
	EmpleadoInfo empleadoDireccion=null;
	ManejadorDB manejadorDB=new ManejadorDB();;
	Connection con = manejadorDB.coneccionPool();	
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	StringBuilder sql = new StringBuilder("");
	try {
		sql.append(" SELECT ED.NB_URBANIZACION , ED.NB_CALLE_ESQ, ED.NB_VIVIENDA FROM PERSONAL.EMPLEADO_DIRECCION ed where ED.NU_CEDULA=? ");
		 
		pstmt = con.prepareStatement(sql.toString());
		pstmt.setInt(1, cedula);
		rs = pstmt.executeQuery();
		if (rs.next()) {
			empleadoDireccion = new EmpleadoInfo();
			empleadoDireccion.setNbUrbanizacion(rs.getString("NB_URBANIZACION"));
			empleadoDireccion.setNbCalleEsq(rs.getString("NB_CALLE_ESQ"));
			empleadoDireccion.setNbVivienda(rs.getString("NB_VIVIENDA"));
		}
	}

	catch (SQLException e) {
		e.printStackTrace();
	} finally {
		liberarConexion(rs, pstmt, con);
	}
	return empleadoDireccion;
}



/**
 * @param cedula
 * @return
 * @throws SQLException
 */
public EmpleadoInfo contratoFechas(int cedula) throws SQLException {
	EmpleadoInfo empleadoInfo=null;
	ManejadorDB manejadorDB=new ManejadorDB();;
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
		
	try {
		con = manejadorDB.coneccionPool();
		String sql = "";
		sql = "SELECT c.desde, c.hasta FROM PERSONAL.CONTRATOS c where C.CODIGO_EMPLEADO in (SELECT pt.codigo_empleado FROM PERSONAL.TODOS_EMPLEADOS pt where PT.CEDULA=?) order by C.HASTA desc";

		pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, cedula);

		rs = pstmt.executeQuery();
		if (rs.next()) {
			empleadoInfo= new EmpleadoInfo();
			empleadoInfo.setDesde(rs.getDate("desde"));
			empleadoInfo.setHasta(rs.getDate("hasta"));
		}
 
	} catch (Exception e) {
		// TODO: handle exception
	} finally {
		liberarConexion(rs, pstmt, con);
	}
	return empleadoInfo;
}


public boolean isVigenteEmpleado(int cedula) throws SQLException {
	EmpleadoInfo empleadoInfo=null;
	empleadoInfo=contratoFechas(cedula) ;
	int dias=0;
	if (empleadoInfo!=null){
		dias=getDays(empleadoInfo.getHasta(), new Date());	
	}
	
	return dias>=0;
}

private int getDays(Date fecha1, Date fecha2){
	
	final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

	int diferencia = (int) ((fecha1.getTime() - fecha2.getTime())/ DAY_IN_MILLIS );
	
	return diferencia;
}



}
