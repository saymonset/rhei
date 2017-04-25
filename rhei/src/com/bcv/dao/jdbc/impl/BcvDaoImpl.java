/**
 * 
 */
package com.bcv.dao.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Named;

import org.apache.log4j.Logger;

import ve.org.bcv.rhei.util.Utilidades;

import com.bcv.dao.jdbc.BcvDao;
import com.bcv.dao.jdbc.ManejadorDB;
import com.bcv.dao.jdbc.SimpleJDBCDaoImpl;
import com.bcv.model.Beneficiario;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco 11/08/2015 10:39:25 2015 mail :
 *         oraclefedora@gmail.com
 */
@Named("bcvDao")
public class BcvDaoImpl extends SimpleJDBCDaoImpl<Beneficiario>
		implements
			BcvDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(Utilidades.class.getName());

	public String cargarCompaniaAnalista(int cedula) {
		ManejadorDB manejador = new ManejadorDB();
		Connection con = manejador.conexion();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String consulta = "";
		String companiaFisicaAnalista = "";
		try {
			consulta = "SELECT CO_CIA_FISICA FROM  PERSONAL.TODOS_EMPLEADOS WHERE CEDULA = ?";

			stmt = con.prepareStatement(consulta);
			stmt.setInt(1, cedula);
			rs = stmt.executeQuery();
			if (!rs.next()) {
				log.debug("El trabajador con cedula de identidad NÂ° " + cedula
						+ " no existe en la base de datos");
			} else {
				companiaFisicaAnalista = rs.getString("CO_CIA_FISICA");
				log.debug("CompaÃ±ia FÃ­sica Analista: "
						+ companiaFisicaAnalista);
			}
			stmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			liberarConexion(rs, stmt, con);

		}
		return companiaFisicaAnalista;
	}

	/* (non-Javadoc)
	 * @see com.bcv.dao.jdbc.BcvDao#sueldoMinimo()
	 */
	@Override
	public Double sueldoMinimo() {
		ManejadorDB manejador = new ManejadorDB();
		Connection con = manejador.conexion();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String consulta = "";
		Double sueldoMinimo = 0d;
		try {
			consulta = "select SUELDO_MINIMO from personal.grados h where h.grado='O01'";

			stmt = con.prepareStatement(consulta);
			rs = stmt.executeQuery();
			if (!rs.next()) {
				log.debug("El Sueldo minimo° "
						+ " no existe en la base de datos");
			} else {
				sueldoMinimo = rs.getDouble("SUELDO_MINIMO");
			 
			}
			stmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			liberarConexion(rs, stmt, con);

		}
		return sueldoMinimo;
	}

	
	/* (non-Javadoc)
	 * @see com.bcv.dao.jdbc.BcvDao#MontoUtiles()
	 */
//	@Override
	public Double MontoUtiles(String tipoEmpl) {
		ManejadorDB manejador = new ManejadorDB();
		Connection con = manejador.conexion();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder consulta = new StringBuilder();
		Double MontoUtiles = 0d;
		try {
			consulta.append("SELECT MAX(MONTO) as MONTO FROM PERSONAL.PARAMETROS_CONCEPTOS C WHERE  C.CODIGO_CONCEPTO='67'  AND TIPO_EMP=?");
			consulta.append(" AND C.FECHA_VIGENCIA = (SELECT MAX (D.FECHA_VIGENCIA)  FROM PERSONAL.PARAMETROS_CONCEPTOS D WHERE D.CODIGO_CONCEPTO='67'  AND D.TIPO_EMP=?)");
			
			
                    

			
			

			stmt = con.prepareStatement(consulta.toString());
			stmt.setString(1, tipoEmpl);
			stmt.setString(2, tipoEmpl);
			rs = stmt.executeQuery();
			if (!rs.next()) {
				log.debug("El Monto de Utiles Escolares° "
						+ " no existe en la base de datos");
			} else {
				MontoUtiles = rs.getDouble("MONTO");
			 
			}
			stmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			liberarConexion(rs, stmt, con);

		}
		return MontoUtiles;
	}
}
