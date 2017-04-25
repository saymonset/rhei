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
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.bcv.dao.jdbc.ManejadorDB;
import com.bcv.dao.jdbc.SimpleJDBCDaoImpl;
import com.bcv.dao.jdbc.TipoEmpleadoDao;
import com.bcv.model.TipoEmpleado;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 21/08/2015 09:34:18
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class TipoEmpleadoDaoImpl extends SimpleJDBCDaoImpl<TipoEmpleado> implements
TipoEmpleadoDao {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private static Logger log = Logger.getLogger(TipoEmpleadoDaoImpl.class.getName());
ManejadorDB manejadorDB=new ManejadorDB();;

/*
 * 
 * (non-Javadoc)
 * 
 * @see com.bcv.dao.jdbc.TrabajadorDao#cargarTipoTrabajador()
 */
public List<TipoEmpleado> tipoEmpleadosList() throws SQLException {
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	  con = manejadorDB.coneccionPool();

	ArrayList<TipoEmpleado> tipoEmpleados= new ArrayList<TipoEmpleado>();
	try {
		String sql = "SELECT tipo_Emp,descripcion,clasificacion,codigo_Contable,codigo_Cia,codigo_Presupuestario FROM PERSONAL.TIPOS_EMPLEADOS te order by TE.TIPO_EMP ";
		stmt = con.prepareStatement(sql);
		rs = stmt.executeQuery();
		TipoEmpleado tipoEmpleado=null;
		while (rs.next()) {
			tipoEmpleado= new TipoEmpleado();
			tipoEmpleado.setTipoEmp(rs.getString("tipo_Emp"));
			tipoEmpleado.setDescripcion(rs.getString("descripcion"));
			tipoEmpleado.setClasificacion(rs.getString("clasificacion"));
			tipoEmpleado.setCodigoContable(rs.getInt("codigo_Contable"));
			tipoEmpleado.setCodigoCia(rs.getInt("codigo_Cia"));
			tipoEmpleado.setCodigoPresupuestario(rs.getInt("codigo_Presupuestario"));
			tipoEmpleados.add(tipoEmpleado);
		}
	} catch (SQLException e) {
		throw new SQLException(e);
	} finally {
		liberarConexion(rs, stmt, con);
	}
	return tipoEmpleados;
}


public List<TipoEmpleado> tipoEmpleadosByReporte(int coRepStatus ) throws SQLException {
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	  con = manejadorDB.coneccionPool();

	ArrayList<TipoEmpleado> tipoEmpleados= new ArrayList<TipoEmpleado>();
	try {
		StringBuilder sql = new StringBuilder(" select unique(TE.TIPO_EMP) from RHEI.RELACION_PAGOS R ");
		sql.append(" inner join RHEI.SOLICITUD_BEI B on B.NU_SOLICITUD=R.NU_SOLICITUD ");
		sql.append(" inner join PERSONAL.TODOS_EMPLEADOS te on TE.CODIGO_EMPLEADO=B.CO_EMPLEADO ");
		sql.append(" where R.CO_REP_STATUS=?");
		stmt = con.prepareStatement(sql.toString());
		stmt.setInt(1, coRepStatus);
		rs = stmt.executeQuery();
		TipoEmpleado tipoEmpleado=null;
		while (rs.next()) {
			tipoEmpleado= new TipoEmpleado();
			tipoEmpleado.setTipoEmp(rs.getString("tipo_Emp"));
			tipoEmpleados.add(tipoEmpleado);
		}
	} catch (SQLException e) {
		throw new SQLException(e);
	} finally {
		liberarConexion(rs, stmt, con);
	}
	return tipoEmpleados;
}









public String tipoEmpleadoSolo(String cedula) throws SQLException {
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	  con = manejadorDB.coneccionPool();
		String tipoEmpleado=null;

	try {
		StringBuilder sql = new StringBuilder("SELECT tipo_emp FROM PERSONAL.TODOS_EMPLEADOS where cedula=? ");
		 
		stmt = con.prepareStatement(sql.toString());
		int posicion=1;
		
			stmt.setString(posicion++, cedula);
		rs = stmt.executeQuery();
		while (rs.next()) {
			tipoEmpleado=rs.getString("tipo_Emp");
		}
	} catch (SQLException e) {
		throw new SQLException(e);
	} finally {
		liberarConexion(rs, stmt, con);
	}
	return tipoEmpleado;
}


public TipoEmpleado tipoEmpleado(String tipoEmp) throws SQLException {
	
	if ("OBR,OBC".equalsIgnoreCase(tipoEmp)){
		tipoEmp="OBR";
	}else if ("EMP,CON,JUB,EJE".equalsIgnoreCase(tipoEmp)){
		tipoEmp="EMP";
	}else if ("SOB".equalsIgnoreCase(tipoEmp)){
		tipoEmp="SOB";
	}else if ("EMP".equalsIgnoreCase(tipoEmp)){
		 tipoEmp="EMP";
	}else if ("CON".equalsIgnoreCase(tipoEmp)){
		 tipoEmp="EMP";
	}else if ("JUB".equalsIgnoreCase(tipoEmp)){
		 tipoEmp="EMP";
	}else if ("EJE".equalsIgnoreCase(tipoEmp)){
		 tipoEmp="EMP";
	}else if ("OBR".equalsIgnoreCase(tipoEmp)){
		 tipoEmp="OBR";
	}else if ("OBC".equalsIgnoreCase(tipoEmp)){
		 tipoEmp="OBC";
	}else{
		 tipoEmp="EMP";
	}
	
	Connection con = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	  con = manejadorDB.coneccionPool();
		TipoEmpleado tipoEmpleado=null;

	try {
		StringBuilder sql = new StringBuilder("SELECT tipo_Emp,descripcion,clasificacion,codigo_Contable,codigo_Cia,codigo_Presupuestario FROM PERSONAL.TIPOS_EMPLEADOS te ");
		if (!StringUtils.isEmpty(tipoEmp)){
			StringTokenizer stk = new StringTokenizer(tipoEmp,",");
			String tipo="";
			
			boolean isPrimeraVez=true;
			while (stk.hasMoreTokens()){
				tipo=(String)stk.nextToken();
				if (isPrimeraVez){
					sql.append(" where ");
				}else{
					sql.append(" or ");					
				}
				sql.append(" lower(tipo_emp)=lower('").append(tipo).append("')");
				isPrimeraVez=false;
			}
			
		}
		stmt = con.prepareStatement(sql.toString());
		 
		rs = stmt.executeQuery();
		while (rs.next()) {
			tipoEmpleado= new TipoEmpleado();
			tipoEmpleado.setSiglas(rs.getString("descripcion"));
			tipoEmpleado.setTipoEmp(rs.getString("tipo_Emp"));
			tipoEmpleado.setDescripcion(rs.getString("descripcion"));
			tipoEmpleado.setClasificacion(rs.getString("clasificacion"));
			tipoEmpleado.setCodigoContable(rs.getInt("codigo_Contable"));
			tipoEmpleado.setCodigoCia(rs.getInt("codigo_Cia"));
			tipoEmpleado.setCodigoPresupuestario(rs.getInt("codigo_Presupuestario"));
		}
	} catch (SQLException e) {
		throw new SQLException(e);
	} finally {
		liberarConexion(rs, stmt, con);
	}
	return tipoEmpleado;
}




}
