package com.bcv.dao.jdbc.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import com.bcv.dao.jdbc.ManejadorDB;
import com.bcv.dao.jdbc.ReporteBecaUtileDao;
import com.bcv.dao.jdbc.SimpleJDBCDaoImpl;
import com.bcv.model.ReporteBecaUtile;

@Named("reporteBecaUtileDao")
public class ReporteBecaUtileDaoImpl extends SimpleJDBCDaoImpl<ReporteBecaUtile> implements ReporteBecaUtileDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Contiene la informacion de un procedimiento almacenado ejecutado.
	 */
	protected CallableStatement callStmt;

	public List<ReporteBecaUtile> reporteBecaDesactivados(String periodoScolar) throws SQLException {
		List<ReporteBecaUtile> objs = new ArrayList<ReporteBecaUtile>();
		ManejadorDB manejadorDB = new ManejadorDB();
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		
		
		
		StringBuilder sql = new StringBuilder();

		sql.append("  select unique(BU.TX_OBSERVACION),PE.TX_DESCRIP_PERIODO from RHEI.REPORTE_BECA_UTILE bu ");
		sql.append("   inner join RHEI.PERIODO_ESCOLAR pe on BU.CO_PERIODO=PE.CO_PERIODO ");
		sql.append(" where BU.CO_PERIODO=? ");
		ReporteBecaUtile obj = null;
		try {
			stmt = con.prepareStatement(sql.toString());
			stmt.setString(1, periodoScolar);
			rs = stmt.executeQuery();

			while (rs.next()) {

				obj = new ReporteBecaUtile();
				obj.setTxObservacion(rs.getString("TX_OBSERVACION"));
				obj.setPeriodo(rs.getString("TX_DESCRIP_PERIODO"));
				obj.setPeriodo(rs.getString("TX_DESCRIP_PERIODO"));
				objs.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
			liberarConexion(rs, stmt, null);
		} finally {
			liberarConexion(rs, stmt, null);
		}
		return objs;
	}

	public List<ReporteBecaUtile> reporteBecaUtileDesincorporado(String periodoScolar, String observacion) throws SQLException {
		List<ReporteBecaUtile> objs = new ArrayList<ReporteBecaUtile>();
		ManejadorDB manejadorDB = new ManejadorDB();
		;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();

		if (null == observacion) {
			observacion = "";
		}

 

		sql.append(" SELECT TIPOE.tipo_emp,E.CO_PERIODO,PERS.CEDULA,S.CEDULA_FAMILIAR ,INITCAP(pers.APELLIDO1||' '||pers.APELLIDO2 ||' '|| pers.NOMBRE1||' '||pers.NOMBRE2 ) AS trabajador, ");
		sql.append(" INITCAP(fam.APELLIDO1||' '||fam.APELLIDO2 ||' '|| fam.NOMBRE1||' '||fam.NOMBRE2 ) AS nombreNino FROM RHEI.SOLICITUD_BEI S   ");
		sql.append(" INNER JOIN PERSONAL.TODOS_EMPLEADOS PERS ON PERS.CODIGO_EMPLEADO=S.CO_EMPLEADO  INNER JOIN RHEI.MOV_ST_SOLIC_BEI MOV ON MOV.NU_SOLICITUD=S.NU_SOLICITUD ");
		sql.append(" INNER JOIN RHEI.PERIODO_ESCOLAR E ON E.CO_PERIODO=MOV.CO_PERIODO   INNER JOIN PERSONAL.FAMILIARES  fam on FAM.CEDULA_FAMILIAR=S.CEDULA_FAMILIAR  ");
		sql.append(" INNER JOIN PERSONAL.TIPOS_EMPLEADOS TIPOE ON TIPOE.TIPO_EMP=PERS.TIPO_EMP ");
		sql.append(" WHERE E.TX_DESCRIP_PERIODO=? ");
		/** Si el status es (A), chequea los usuarios que le faltan becas */
		sql.append(" and PERS.CEDULA in ");
		sql.append(
				"( select NU_CEDULA from RHEI.REPORTE_BECA_UTILE becaUtil where becaUtil.NU_CEDULA=PERS.CEDULA and BECAUTIL.CO_PERIODO=E.CO_PERIODO  and BECAUTIL.NU_CI_FAMILIAR=S.CEDULA_FAMILIAR and  lower(becaUtil.TX_OBSERVACION)=?  )");
		sql.append(" and FAM.NU_CEDULA=PERS.CEDULA ");
		sql.append(" and MOV.CO_STATUS='A' ");
		sql.append(" order by pers.APELLIDO1");

		try {
			stmt = con.prepareStatement(sql.toString());
			stmt.setString(1, periodoScolar);
			stmt.setString(2, observacion.toLowerCase());
			rs = stmt.executeQuery();
			ReporteBecaUtile obj = null;
			int columnCount = 0;
			while (rs.next()) {

				obj = new ReporteBecaUtile();
				int cedula = rs.getInt("CEDULA");
				int cedulaFamiliar = rs.getInt("CEDULA_FAMILIAR");
				String trabajador = rs.getString("trabajador");
				String nombreNino = rs.getString("nombreNino");
				obj = new ReporteBecaUtile();
				obj.setTrabajador(trabajador);
				obj.setPeriodo(periodoScolar);
				obj.setNuCedula(cedula);
				obj.setNuCiFamiliar(cedulaFamiliar);
				obj.setNombreNino(nombreNino);
				obj.setTipoEmpleado(rs.getString("tipo_emp"));
				obj.setColumnCount(++columnCount);

				objs.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
			liberarConexion(rs, stmt, null);
		} finally {
			liberarConexion(rs, stmt, null);
		}
		return objs;
	}

	public List<ReporteBecaUtile> reporteBecaUtileDao(String periodoScolar, String tipoEmpleado, String status) throws SQLException {
		ManejadorDB manejadorDB = new ManejadorDB();
		;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<ReporteBecaUtile> objs = new ArrayList<ReporteBecaUtile>();
		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT TIPOE.tipo_emp,E.CO_PERIODO,PERS.CEDULA,S.CEDULA_FAMILIAR ,INITCAP(pers.APELLIDO1||' '||pers.APELLIDO2 ||' '|| pers.NOMBRE1||' '||pers.NOMBRE2 ) AS trabajador, ");
		sql.append(" INITCAP(fam.APELLIDO1||' '||fam.APELLIDO2 ||' '|| fam.NOMBRE1||' '||fam.NOMBRE2 ) AS nombreNino FROM RHEI.SOLICITUD_BEI S   ");
		sql.append(" INNER JOIN PERSONAL.TODOS_EMPLEADOS PERS ON PERS.CODIGO_EMPLEADO=S.CO_EMPLEADO  INNER JOIN RHEI.MOV_ST_SOLIC_BEI MOV ON MOV.NU_SOLICITUD=S.NU_SOLICITUD ");
		sql.append(" INNER JOIN RHEI.PERIODO_ESCOLAR E ON E.CO_PERIODO=MOV.CO_PERIODO   INNER JOIN PERSONAL.FAMILIARES  fam on FAM.CEDULA_FAMILIAR=S.CEDULA_FAMILIAR  ");
		sql.append(" INNER JOIN PERSONAL.TIPOS_EMPLEADOS TIPOE ON TIPOE.TIPO_EMP=PERS.TIPO_EMP ");
		sql.append(" WHERE E.TX_DESCRIP_PERIODO=? ");
		sql.append("   AND TIPOE.TIPO_EMP=? ");
		/** Si el status es (A), chequea los usuarios que le faltan becas */
		if ("A".equalsIgnoreCase(status)) {
			sql.append(" and PERS.CEDULA not in ");
		} else {
			sql.append(" and PERS.CEDULA in ");
		}
		sql.append(
				"( select NU_CEDULA from RHEI.REPORTE_BECA_UTILE becaUtil where becaUtil.NU_CEDULA=PERS.CEDULA and BECAUTIL.CO_PERIODO=E.CO_PERIODO  and BECAUTIL.NU_CI_FAMILIAR=S.CEDULA_FAMILIAR )");
		sql.append(" and FAM.NU_CEDULA=PERS.CEDULA ");
		sql.append(" and MOV.CO_STATUS='A' ");
		sql.append(" order by pers.APELLIDO1");
		try {
			stmt = con.prepareStatement(sql.toString());
			stmt.setString(1, periodoScolar);
			stmt.setString(2, tipoEmpleado);
			rs = stmt.executeQuery();
			/** Si el status es para los que se le asignaron becas */
			ReporteBecaUtile obj = null;
			int columnCount = 0;
			while (rs.next()) {

				int cedula = rs.getInt("CEDULA");
				int cedulaFamiliar = rs.getInt("CEDULA_FAMILIAR");
				String trabajador = rs.getString("trabajador");
				String nombreNino = rs.getString("nombreNino");
				obj = new ReporteBecaUtile();
				obj.setTipoEmpleado(rs.getString("tipo_emp"));
				obj.setTrabajador(trabajador);
				obj.setPeriodo(periodoScolar);
				obj.setNuCedula(cedula);
				obj.setNuCiFamiliar(cedulaFamiliar);
				obj.setNombreNino(nombreNino);
				obj.setColumnCount(++columnCount);
				objs.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
			liberarConexion(rs, stmt, null);
		} finally {
			liberarConexion(rs, stmt, null);
		}
		return objs;
	}

	public void updateReporteBecaUtileDao(String periodoScolar, String txObservacion, String tipoEmpleado) throws SQLException {
		ManejadorDB manejadorDB = new ManejadorDB();
		;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<ReporteBecaUtile> objs = new ArrayList<ReporteBecaUtile>();
		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT E.CO_PERIODO,PERS.CEDULA,S.CEDULA_FAMILIAR ,INITCAP(pers.APELLIDO1||' '||pers.APELLIDO2 ||' '|| pers.NOMBRE1||' '||pers.NOMBRE2 ) AS trabajador, ");
		sql.append(" INITCAP(fam.APELLIDO1||' '||fam.APELLIDO2 ||' '|| fam.NOMBRE1||' '||fam.NOMBRE2 ) AS nombreNino FROM RHEI.SOLICITUD_BEI S   ");
		sql.append(" INNER JOIN PERSONAL.TODOS_EMPLEADOS PERS ON PERS.CODIGO_EMPLEADO=S.CO_EMPLEADO  INNER JOIN RHEI.MOV_ST_SOLIC_BEI MOV ON MOV.NU_SOLICITUD=S.NU_SOLICITUD ");
		sql.append(" INNER JOIN RHEI.PERIODO_ESCOLAR E ON E.CO_PERIODO=MOV.CO_PERIODO   INNER JOIN PERSONAL.FAMILIARES  fam on FAM.CEDULA_FAMILIAR=S.CEDULA_FAMILIAR  ");
		sql.append("     INNER JOIN PERSONAL.TIPOS_EMPLEADOS TIPOE ON TIPOE.TIPO_EMP=PERS.TIPO_EMP ");
		sql.append(" WHERE E.TX_DESCRIP_PERIODO=? ");
		/** Si el status es (A), chequea los usuarios que le faltan becas */
		sql.append(" and PERS.CEDULA not in ");
		sql.append(
				"( select NU_CEDULA from RHEI.REPORTE_BECA_UTILE becaUtil where becaUtil.NU_CEDULA=PERS.CEDULA and BECAUTIL.CO_PERIODO=E.CO_PERIODO  and BECAUTIL.NU_CI_FAMILIAR=S.CEDULA_FAMILIAR )");
		sql.append(" and FAM.NU_CEDULA=PERS.CEDULA ");
		sql.append(" and MOV.CO_STATUS='A' ");
		sql.append(" and PERS.TIPO_EMP=? ");
		sql.append(" order by pers.APELLIDO1");
		try {
			stmt = con.prepareStatement(sql.toString());
			stmt.setString(1, periodoScolar);
			stmt.setString(2, tipoEmpleado);
			rs = stmt.executeQuery();
			/** Si el status es para los que se le asignaron becas */
			ReporteBecaUtile obj = null;
			StringBuilder sqlnsert;
			// IN_REPORTE IS 'Contiene informacion del indicador que determina si la
			// solicitud esta activa o inactiva. Los posibles valores son: 0 Inactivas y
			// 1 Activas.';
			int inReporte = 1;
			String rheiUtilesescolaresProc = "{call RHEI.utilesEscolares (?, ?)}";
			while (rs.next()) {
				int coPeriodo = rs.getInt("CO_PERIODO");
				int cedula = rs.getInt("CEDULA");
				int cedulaFamiliar = rs.getInt("CEDULA_FAMILIAR");
				String trabajador = rs.getString("trabajador");
				String nombreNino = rs.getString("nombreNino");
				
				
				
				callStmt = con.prepareCall(rheiUtilesescolaresProc);
				callStmt.setInt(1, cedula);
				callStmt.setString(2, nombreNino);
				
				// execute getDBUSERByUserId store procedure
				callStmt.executeUpdate();
				
				
				
				sqlnsert = new StringBuilder();
				pstmt = con.prepareStatement("SELECT RHEI.SEQ_CO_CONYUGE.NEXTVAL AS valor from dual");
				ResultSet result = pstmt.executeQuery();
				int coBecaUtile = 0;
				if (result != null) {
					while (result.next()) {
						coBecaUtile = result.getInt("valor");
					}
					sqlnsert.append("INSERT INTO RHEI.REPORTE_BECA_UTILE (CO_REP_BECAS, CO_PERIODO, NU_CEDULA, IN_REPORTE,TX_OBSERVACION, NU_CI_FAMILIAR) VALUES (?,?,?,?,?,?)");

					// otro insert para alimentear la tabla nomina

					pstmt = con.prepareStatement(sqlnsert.toString());
					int posi = 1;
					pstmt.setInt(posi++, coBecaUtile);
					pstmt.setInt(posi++, coPeriodo);
					pstmt.setInt(posi++, cedula);
					pstmt.setInt(posi++, inReporte);
					pstmt.setString(posi++, txObservacion+" ("+tipoEmpleado+")");
					pstmt.setInt(posi++, cedulaFamiliar);
					int resultado = pstmt.executeUpdate();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			liberarConexion(rs, stmt, null);
		} finally {
			liberarConexion(rs, stmt, null);
			if (null != pstmt) {
				pstmt.close();
			}
		}

	}

}
