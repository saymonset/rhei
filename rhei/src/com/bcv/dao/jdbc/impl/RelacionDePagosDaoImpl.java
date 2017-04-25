/**
 * 
 */
package com.bcv.dao.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.report.by.benef.BeneficiarioBean2;
import ve.org.bcv.rhei.util.Constantes;

import com.bcv.dao.jdbc.ManejadorDB;
import com.bcv.dao.jdbc.PeriodoEscolarDao;
import com.bcv.dao.jdbc.RelacionDePagosDao;
import com.bcv.dao.jdbc.ReporteEstatusDao;
import com.bcv.dao.jdbc.SimpleJDBCDaoImpl;
import com.bcv.dto.NuRefPago;
import com.bcv.model.Factura;
import com.bcv.model.RelacionDePagos;
import com.bcv.model.ReporteByDefinitivoOrTransitivo;
import com.bcv.reporte.relacionpago.ProveedorRpago1Bean;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco 03/07/2015 14:50:39 2015 mail :
 *         oraclefedora@gmail.com
 */
public class RelacionDePagosDaoImpl extends SimpleJDBCDaoImpl<RelacionDePagos> implements RelacionDePagosDao {

	private static Logger log = Logger.getLogger(SolicitudDaoImpl.class.getName());

	// sacar reporte contable
	// select r.* ,B.CO_EMPLEADO,TE.TIPO_EMP,TE.CEDULA from RHEI.RELACION_PAGOS R
	// inner join RHEI.SOLICITUD_BEI B on B.NU_SOLICITUD=R.NU_SOLICITUD
	// inner join PERSONAL.TODOS_EMPLEADOS te on TE.CODIGO_EMPLEADO=B.CO_EMPLEADO
	// where R.CO_REP_STATUS=471 and TE.TIPO_EMP<>'OBR'

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ReporteEstatusDao reporteEstatusDao = new ReporteEstatusDaoImpl();

	public ArrayList<String> obtenerMesesPagados(int nroSolicitud, String periodoEscolar, int mes, int tipoDePago) throws SQLException {
		ManejadorDB manejadorDB = new ManejadorDB();
		;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder(" SELECT R.NU_REF_PAGO AS NU_REF_PAGO  ");
		ArrayList<String> listadoMesesPagados = new ArrayList<String>();
		try {

			sql.append(" FROM RHEI.RELACION_PAGOS R INNER JOIN RHEI.PERIODO_ESCOLAR PE ON R.CO_PERIODO = PE.CO_PERIODO ");
			sql.append("   ");
			sql.append(" WHERE  R.CO_TI_PAGO in (1,3) ");

			sql.append("  AND R.IN_COMPLEMENTO='N' ");

			if (nroSolicitud > 0) {
				sql.append(" AND NU_SOLICITUD = ?  ");
			}
			if (!StringUtils.isEmpty(periodoEscolar)) {
				sql.append("  AND PE.TX_DESCRIP_PERIODO=?");
			}
			if (mes > 0) {
				sql.append(" AND (NU_REF_PAGO=? OR NU_REF_PAGO=?)");
			}

			stmt = con.prepareStatement(sql.toString());
			int cont = 0;
			if (nroSolicitud > 0) {
				cont++;
				stmt.setInt(cont, nroSolicitud);
			}
			if (!StringUtils.isEmpty(periodoEscolar)) {
				cont++;
				stmt.setString(cont, periodoEscolar);
			}
			/** no incluimos la matricla paga=-14 o no paga=14 */
			if (mes > 0) {
				cont++;
				stmt.setInt(cont, mes);
				/**
				 * Los negativos representan meses que ya se pagaron fuera del sistema, son casos
				 * excepcionales
				 **/
				cont++;
				stmt.setInt(cont, mes * -1);
			}

			rs = stmt.executeQuery();
			int mess = 0;
			if (rs.next()) {
				do {
					mess = rs.getInt("NU_REF_PAGO");
					/**
					 * Quitamos el negativo. Los negativos representan meses que ya se pagaron fuera
					 * del sistema, son casos excepcionales
					 **/
					if (mess < 0) {
						mess *= -1;
					}
					listadoMesesPagados.add(String.valueOf(mess));
				} while (

				rs.next());
			} else {
				listadoMesesPagados.clear();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return listadoMesesPagados;
	}

	/**
	 * @param nuSolicitud
	 * @param nuRefPagoStr
	 * @return
	 */
	public List<NuRefPago> nuRefPagosComplHistorico(long nuSolicitud, String meses, String periodoEscolar) throws Exception {
		List<NuRefPago> listadoNumRefPago = new ArrayList<NuRefPago>();
		StringBuilder sql = new StringBuilder();
		sql.append(
				" select F.MO_APORTE_BCV,F.MO_PERIODO,F.MO_MATRICULA,R.NU_REF_PAGO,R.MO_TOTAL_PAGO,F.FE_FACTURA from  RHEI.RELACION_PAGOS r inner join RHEI.FACTURA f on R.NU_ID_FACTURA=F.NU_ID_FACTURA ");
		sql.append("  inner join RHEI.PERIODO_ESCOLAR pe on PE.CO_PERIODO=R.CO_PERIODO ");
		sql.append("  where R.NU_SOLICITUD=? and R.NU_REF_PAGO>0  ");
		sql.append("  and PE.TX_DESCRIP_PERIODO='").append(periodoEscolar).append("' ");
		StringBuilder mes = new StringBuilder(" ");
		if (!StringUtils.isEmpty(meses)) {
			StringTokenizer stk = new StringTokenizer(meses, ",");

			mes.append(" and ( ");
			Boolean isFirst = true;
			while (stk.hasMoreTokens()) {
				if (!isFirst) {
					mes.append(" OR ");
				}
				isFirst = false;
				mes.append(" R.NU_REF_PAGO=").append((String) stk.nextToken());
			}
			mes.append(" ) ");
		}
		ManejadorDB manejadorDB = new ManejadorDB();
		;
		Connection con;

		con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {

			stmt = con.prepareStatement(sql.append(mes.toString()).toString());
			stmt.setLong(1, nuSolicitud);
			// stmt.setString(2, Constantes.IN_COMPLEMENTO);
			rs = stmt.executeQuery();
			if (rs.next()) {
				NuRefPago nuRefPago = null;
				do {
					nuRefPago = new NuRefPago();
					nuRefPago.setMes(rs.getString("NU_REF_PAGO"));
					nuRefPago.setFechaFactura(rs.getDate("FE_FACTURA"));
					nuRefPago.setMontoPago(rs.getBigDecimal("MO_TOTAL_PAGO"));
					nuRefPago.setMoAporteBcv(rs.getBigDecimal("MO_APORTE_BCV"));
					nuRefPago.setMoPeriodo(rs.getBigDecimal("MO_PERIODO"));
					nuRefPago.setMoMatricula(rs.getBigDecimal("MO_MATRICULA"));

					listadoNumRefPago.add(nuRefPago);
				} while (rs.next());
			} else {
				listadoNumRefPago.clear();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}

		return listadoNumRefPago;
	}

	public boolean existePagoDelBeneficiario(String coStatus, String cedula, String cedulaFamiliar) throws SQLException {
		boolean existe = false;
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			long ced = 0;
			if (!StringUtils.isEmpty(cedula) && StringUtils.isNumeric(cedula)) {
				ced = Integer.parseInt(cedula);
			}

			long cedFam = 0;
			if (!StringUtils.isEmpty(cedulaFamiliar) && StringUtils.isNumeric(cedulaFamiliar)) {
				cedFam = Integer.parseInt(cedulaFamiliar);
			}
			ManejadorDB manejadorDB = new ManejadorDB();
			;
			con = manejadorDB.coneccionPool();

			StringBuilder sql = new StringBuilder("");
			sql.append(" select  SB.NU_SOLICITUD from RHEI.SOLICITUD_BEI  sb ");
			sql.append(" inner join PERSONAL.TODOS_EMPLEADOS te ");
			sql.append(" on SB.CO_EMPLEADO=TE.CODIGO_EMPLEADO ");
			sql.append("  inner join   RHEI.MOV_ST_SOLIC_BEI msb on SB.NU_SOLICITUD=MSB.NU_SOLICITUD ");
			sql.append(" inner join RHEI.RELACION_PAGOS rp on  RP.NU_SOLICITUD=SB.NU_SOLICITUD ");
			sql.append("  where MSB.CO_STATUS=? and TE.CEDULA=? ");
			sql.append("  and SB.CEDULA_FAMILIAR= ?");
			stmt = con.prepareStatement(sql.toString());
			int posicion = 1;
			stmt.setString(posicion++, coStatus);
			stmt.setLong(posicion++, ced);
			stmt.setLong(posicion++, cedFam);

			rs = stmt.executeQuery();
			while (rs.next()) {
				existe = true;
				break;

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return existe;

	}

	public RelacionDePagos lastNumSolicitudPagoByCedula(String coStatus, String cedula, String cedulaFamiliar) throws SQLException {
		RelacionDePagos relacionDePagos = null;
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			long ced = 0;
			if (!StringUtils.isEmpty(cedula) && StringUtils.isNumeric(cedula)) {
				ced = Integer.parseInt(cedula);
			}
			long cedFam = 0;
			if (!StringUtils.isEmpty(cedulaFamiliar) && StringUtils.isNumeric(cedulaFamiliar)) {
				cedFam = Integer.parseInt(cedulaFamiliar);
			}

			ManejadorDB manejadorDB = new ManejadorDB();
			;
			con = manejadorDB.coneccionPool();

			StringBuilder sql = new StringBuilder("");
			sql.append(" select   unique(SB.NU_SOLICITUD) from RHEI.SOLICITUD_BEI  sb ");
			sql.append(" inner join PERSONAL.TODOS_EMPLEADOS te ");
			sql.append(" on SB.CO_EMPLEADO=TE.CODIGO_EMPLEADO ");
			sql.append("  inner join   RHEI.MOV_ST_SOLIC_BEI msb on SB.NU_SOLICITUD=MSB.NU_SOLICITUD ");
			sql.append(" inner join RHEI.RELACION_PAGOS rp on  RP.NU_SOLICITUD=SB.NU_SOLICITUD ");
			sql.append("  where MSB.CO_STATUS=? and TE.CEDULA=? ");
			if (cedFam > 0) {
				sql.append(" and sb.cedula_familiar=? ");
			}
			sql.append(" ORDER BY SB.NU_SOLICITUD DESC  ");
			stmt = con.prepareStatement(sql.toString());
			int posicion = 1;
			stmt.setString(posicion++, coStatus);
			stmt.setLong(posicion++, ced);
			if (cedFam > 0) {
				stmt.setLong(posicion++, cedFam);
			}
			rs = stmt.executeQuery();
			boolean isPrimeraVez = true;

			StringBuilder numSolicituds = new StringBuilder("");
			while (rs.next()) {
				if (!isPrimeraVez) {
					numSolicituds.append(",");
				}
				numSolicituds.append(rs.getInt("NU_SOLICITUD") + "");
				isPrimeraVez = false;

			}
			relacionDePagos = new RelacionDePagos();
			relacionDePagos.setNroSolicitudsSeparatedByComa(numSolicituds.toString());
			;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return relacionDePagos;

	}

	public RelacionDePagos numSolicitudPagoDelBeneficiario(String coStatus, String cedula, String cedulaFamiliar) throws SQLException {
		RelacionDePagos relacionDePagos = null;
		String result = null;
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			long ced = 0;
			if (!StringUtils.isEmpty(cedula) && StringUtils.isNumeric(cedula)) {
				ced = Integer.parseInt(cedula);
			}

			long cedFam = 0;
			if (!StringUtils.isEmpty(cedulaFamiliar) && StringUtils.isNumeric(cedulaFamiliar)) {
				cedFam = Integer.parseInt(cedulaFamiliar);
			}
			ManejadorDB manejadorDB = new ManejadorDB();
			;
			con = manejadorDB.coneccionPool();

			StringBuilder sql = new StringBuilder("");
			sql.append(" select  PE.TX_DESCRIP_PERIODO,SB.NU_SOLICITUD from RHEI.SOLICITUD_BEI  sb ");
			sql.append(" inner join PERSONAL.TODOS_EMPLEADOS te ");
			sql.append(" on SB.CO_EMPLEADO=TE.CODIGO_EMPLEADO ");
			sql.append("  inner join   RHEI.MOV_ST_SOLIC_BEI msb on SB.NU_SOLICITUD=MSB.NU_SOLICITUD ");
			sql.append(" inner join RHEI.RELACION_PAGOS rp on  RP.NU_SOLICITUD=SB.NU_SOLICITUD ");
			sql.append("     inner join RHEI.PERIODO_ESCOLAR pe on PE.CO_PERIODO=RP.CO_PERIODO ");
			sql.append("  where MSB.CO_PERIODO=PE.CO_PERIODO AND  MSB.CO_STATUS=? and TE.CEDULA=? ");
			sql.append("  and SB.CEDULA_FAMILIAR= ?");
			stmt = con.prepareStatement(sql.toString());
			int posicion = 1;
			stmt.setString(posicion++, coStatus);
			stmt.setLong(posicion++, ced);
			stmt.setLong(posicion++, cedFam);

			rs = stmt.executeQuery();
			while (rs.next()) {
				relacionDePagos = new RelacionDePagos();
				relacionDePagos.setNroSolicitud(rs.getInt("NU_SOLICITUD"));
				relacionDePagos.setTxDescriPeriodo(rs.getString("TX_DESCRIP_PERIODO"));
				;
				break;

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return relacionDePagos;

	}

	public ArrayList<String> obtenerMesesPagados(int nroSolicitud, int tipoDePago) throws SQLException {
		ManejadorDB manejadorDB = new ManejadorDB();
		;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "";
		ArrayList<String> listadoMesesPagados = new ArrayList<String>();
		try {
			log.debug("Entro en obtenerMesesPagados");
			log.debug("nroSolicitud=" + nroSolicitud);
			if (tipoDePago == 1) {
				sql = "SELECT R.NU_REF_PAGO AS NU_REF_PAGO, PARM.TX_OBSERVACIONES AS TX_OBSERVACIONES FROM RHEI.RELACION_PAGOS R INNER JOIN RHEI.PERIODO_ESCOLAR PE ON R.CO_PERIODO = PE.CO_PERIODO INNER JOIN RHEI.PARAMETRO PARM ON R.NU_REF_PAGO = to_number(PARM.TX_VALOR_PARAMETRO) WHERE NU_SOLICITUD = ? AND R.CO_TI_PAGO in (1,3) AND SYSDATE between PE.FE_INICIO and PE.FE_FIN AND PARM.CO_PARAMETRO between 'MES00' and 'MES12' ORDER BY TX_OBSERVACIONES ASC";
				log.debug("Se cargo el sql para tipo de Pago Convencional");
			} else if (tipoDePago == 2) {
				sql = "(SELECT R.NU_REF_PAGO AS NU_REF_PAGO, PARM.TX_OBSERVACIONES AS TX_OBSERVACIONES FROM RHEI.RELACION_PAGOS R INNER JOIN RHEI.PERIODO_ESCOLAR PE ON R.CO_PERIODO = PE.CO_PERIODO INNER JOIN RHEI.PARAMETRO PARM ON R.NU_REF_PAGO = to_number(PARM.TX_VALOR_PARAMETRO) WHERE NU_SOLICITUD = ? AND R.CO_TI_PAGO in (1,3) AND SYSDATE between PE.FE_INICIO and PE.FE_FIN AND PARM.CO_PARAMETRO between 'MES00' and 'MES12') MINUS (SELECT R.NU_REF_PAGO AS NU_REF_PAGO, PARM.TX_OBSERVACIONES AS TX_OBSERVACIONES FROM RHEI.RELACION_PAGOS R INNER JOIN RHEI.PERIODO_ESCOLAR PE ON R.CO_PERIODO = PE.CO_PERIODO INNER JOIN RHEI.PARAMETRO PARM ON R.NU_REF_PAGO = to_number(PARM.TX_VALOR_PARAMETRO) WHERE NU_SOLICITUD = "
						+ nroSolicitud + " " + "AND R.CO_TI_PAGO = 2 " + "AND SYSDATE between PE.FE_INICIO and PE.FE_FIN " + "AND PARM.CO_PARAMETRO between 'MES00' and 'MES12') " + "ORDER BY "
						+ "TX_OBSERVACIONES ASC";
				log.debug("Se cargo el sql para tipo de Ajuste");
			} else if (tipoDePago == 3) {
				sql = "(SELECT R.NU_REF_PAGO AS NU_REF_PAGO, PARM.TX_OBSERVACIONES AS TX_OBSERVACIONES FROM RHEI.RELACION_PAGOS R INNER JOIN RHEI.PERIODO_ESCOLAR PE ON R.CO_PERIODO = PE.CO_PERIODO INNER JOIN RHEI.PARAMETRO PARM ON R.NU_REF_PAGO = to_number(PARM.TX_VALOR_PARAMETRO) WHERE NU_SOLICITUD = ? AND R.CO_TI_PAGO = 1 AND SYSDATE between PE.FE_INICIO and PE.FE_FIN AND PARM.CO_PARAMETRO between 'MES00' and 'MES12' ) UNION (SELECT W.NU_REF_PAGO AS NU_REF_PAGO, W.TX_OBSERVACIONES AS TX_OBSERVACIONES FROM (SELECT least(M.MO_APORTE_BCV, decode(M.IN_BENEF_COMPART, 'S', (0.5 * M.MO_PERIODO  ), M.MO_PERIODO)) AS MENSUALIDAD_BCV, M.CO_PERIODO AS CO_PERIODO, M.NU_SOLICITUD AS NU_SOLICITUD FROM RHEI.MOV_ST_SOLIC_BEI M INNER JOIN RHEI.PERIODO_ESCOLAR PE ON M.CO_PERIODO = PE.CO_PERIODO WHERE M.NU_SOLICITUD = "
						+ nroSolicitud + " " + "AND SYSDATE between PE.FE_INICIO and PE.FE_FIN " + "AND M.FE_STATUS = (SELECT " + "MAX(M1.FE_STATUS) " + "FROM " + "RHEI.MOV_ST_SOLIC_BEI M1 "
						+ "WHERE " + "M1.NU_SOLICITUD = M.NU_SOLICITUD " + "AND M1.CO_PERIODO = M.CO_PERIODO ) " + ") V " + "INNER JOIN " + "(SELECT " + "R.NU_REF_PAGO AS NU_REF_PAGO, "
						+ "SUM(R.MO_TOTAL_PAGO) AS TOTAL, " + "R.NU_SOLICITUD AS NU_SOLICITUD, " + "R.CO_PERIODO AS CO_PERIODO, " + "PARM.TX_OBSERVACIONES AS TX_OBSERVACIONES " + "FROM "
						+ "RHEI.RELACION_PAGOS R " + "INNER JOIN RHEI.PARAMETRO PARM ON R.NU_REF_PAGO = to_number(PARM.TX_VALOR_PARAMETRO) "
						+ "INNER JOIN RHEI.PERIODO_ESCOLAR PE ON R.CO_PERIODO = PE.CO_PERIODO " + "WHERE " + "R.NU_SOLICITUD = " + nroSolicitud + " "
						+ "AND SYSDATE between PE.FE_INICIO and PE.FE_FIN " + "AND R.CO_TI_PAGO = 3 " + "AND PARM.CO_PARAMETRO between 'MES00' and 'MES12' " + "GROUP BY " + "R.NU_REF_PAGO, "
						+ "R.NU_SOLICITUD, " + "R.CO_PERIODO, " + "PARM.TX_OBSERVACIONES " + "ORDER BY " + "PARM.TX_OBSERVACIONES ASC " + ") W ON V.MENSUALIDAD_BCV  = W.TOTAL "
						+ "AND V.NU_SOLICITUD = W.NU_SOLICITUD " + "AND V.CO_PERIODO\t  = W.CO_PERIODO " + ") " + "ORDER BY " + "TX_OBSERVACIONES ASC ";
				log.debug("Se cargo el sql para tipo de Pago Prorrateo");
			}
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, nroSolicitud);
			rs = stmt.executeQuery();
			if (rs.next()) {
				do {
					listadoMesesPagados.add(String.valueOf(rs.getInt("NU_REF_PAGO")));
				} while (

				rs.next());
			} else {
				listadoMesesPagados.clear();
			}
			log.debug("Contenido de listadoMesesPagados :" + listadoMesesPagados);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return listadoMesesPagados;
	}

	public ArrayList<String> obtenerMesesPagados(int nroSolicitud, int tipoDePago, int mesDeRegistro, String nroFactura, int idFactura) throws SQLException {
		ManejadorDB manejadorDB = new ManejadorDB();
		;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "";
		ArrayList<String> listadoMesesPagados = new ArrayList<String>();
		log.debug("Valor de mesDeRegistro: " + mesDeRegistro);
		log.debug("Valor de nroFactura: " + nroFactura);
		try {
			log.debug("Entro en obtenerMesesPagados");

			sql = "SELECT NU_REF_PAGO FROM RHEI.RELACION_PAGOS R INNER JOIN RHEI.FACTURA F ON R.NU_ID_FACTURA = F.NU_ID_FACTURA INNER JOIN RHEI.PERIODO_ESCOLAR PE ON R.CO_PERIODO = PE.CO_PERIODO INNER JOIN RHEI.PARAMETRO PARM ON R.NU_REF_PAGO = to_number(PARM.TX_VALOR_PARAMETRO) WHERE R.NU_SOLICITUD = ? AND R.CO_TI_PAGO = ? AND NU_FACTURA = ? AND F.NU_ID_FACTURA = "
					+ idFactura + " " + "AND TO_NUMBER(TO_CHAR(FE_REG_PAGO,'MM')) = ? " + "AND SYSDATE between PE.FE_INICIO and PE.FE_FIN " + "AND PARM.CO_PARAMETRO between 'MES00' and 'MES12'"
					+ "ORDER BY PARM.TX_OBSERVACIONES ASC";
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, nroSolicitud);
			stmt.setInt(2, tipoDePago);
			stmt.setString(3, nroFactura);
			stmt.setInt(4, mesDeRegistro);

			rs = stmt.executeQuery();
			if (rs.next()) {
				log.debug("A");
				do {
					int aux = rs.getInt("NU_REF_PAGO");
					if (!rs.wasNull()) {
						listadoMesesPagados.add(String.valueOf(aux));
					}
				} while (

				rs.next());
			} else {
				listadoMesesPagados.clear();
			}
			log.debug("Contenido de listadoMesesPagados :" + listadoMesesPagados);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return listadoMesesPagados;
	}

	public Double calcularMontoFactura(int nroSolicitud, int mesDeRegistro, String nroFactura, int idFactura, int tipoDePago) throws SQLException {
		ManejadorDB manejadorDB = new ManejadorDB();
		;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "";
		Double montoAPagarPorFactura = Double.valueOf(0.0D);

		try {
			sql = "SELECT MO_TOTAL_PAGO FROM RHEI.RELACION_PAGOS P, RHEI.FACTURA F WHERE NU_SOLICITUD = ? AND P.NU_ID_FACTURA = ? AND P.NU_ID_FACTURA = F.NU_ID_FACTURA AND NU_FACTURA = ? AND TO_NUMBER(TO_CHAR(FE_REG_PAGO,'MM')) = ? AND CO_TI_PAGO = "
					+ tipoDePago;

			stmt = con.prepareStatement(sql);
			stmt.setInt(1, nroSolicitud);
			stmt.setInt(2, idFactura);
			stmt.setString(3, nroFactura);
			stmt.setInt(4, mesDeRegistro);

			rs = stmt.executeQuery();
			if (rs.next()) {
				do {
					Double aux = Double.valueOf(rs.getDouble("MO_TOTAL_PAGO"));
					if (!rs.wasNull()) {
						montoAPagarPorFactura = Double.valueOf(montoAPagarPorFactura.doubleValue() + aux.doubleValue());
					}
				} while (

				rs.next());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return montoAPagarPorFactura;
	}

	public int obtenerReceptorPago(int nroSolicitud, int mesDeRegistro, String nroFactura, int tipoDePago) throws SQLException {
		ManejadorDB manejadorDB = new ManejadorDB();
		;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "";
		int receptor = -1;

		try {
			log.debug("Entro en obtenerReceptorPago...");

			sql = "SELECT DISTINCT IN_RECEPTOR_PAGO FROM RHEI.RELACION_PAGOS P, RHEI.FACTURA F WHERE NU_SOLICITUD = ? AND P.NU_ID_FACTURA = F.NU_ID_FACTURA AND NU_FACTURA = ? AND TO_NUMBER(TO_CHAR(FE_REG_PAGO,'MM')) = ? AND CO_TI_PAGO = "
					+ tipoDePago;

			log.debug("Valor del query: " + sql);
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, nroSolicitud);
			stmt.setString(2, nroFactura);
			stmt.setInt(3, mesDeRegistro);

			rs = stmt.executeQuery();
			if (rs.next()) {
				receptor = rs.getInt("IN_RECEPTOR_PAGO");
				log.debug("El pago de la factura está dirigido a: " + receptor);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return receptor;
	}

	public int obtenerReceptorPago(int nroSolicitud, int mesProrrateo, int periodoEscolar) throws SQLException {
		ManejadorDB manejadorDB = new ManejadorDB();
		;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "";
		int receptor = -1;
		log.debug("Valor de nroSolicitud: " + nroSolicitud);
		log.debug("Valor de mesDeRegistro: " + mesProrrateo);
		log.debug("Valor de periodoEscolar: " + periodoEscolar);
		try {
			log.debug("Entro en obtenerReceptorPago...");
			sql = "SELECT DISTINCT IN_RECEPTOR_PAGO FROM RHEI.RELACION_PAGOS WHERE NU_SOLICITUD = ? AND IN_CONCEPTO_PAGO = 1 AND NU_REF_PAGO = ? AND CO_PERIODO = ?";

			log.debug("Valor del query: " + sql);
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, nroSolicitud);
			stmt.setInt(2, mesProrrateo);
			stmt.setInt(3, periodoEscolar);

			rs = stmt.executeQuery();
			if (rs.next()) {
				receptor = rs.getInt("IN_RECEPTOR_PAGO");
				log.debug("El pago está dirigido a: " + receptor);
			}
			log.debug("El pago está dirigido a: " + receptor);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return receptor;
	}

	public String guardarPago(Factura factura, Double montoBCV, Double montoMatriculaBCV, Double montoPeriodoBCV, int tipoDePago, String observaciones, List<Integer> mesMatriMen, int nroSolicitud,
			List<Integer> conceptoPago, int receptorPago, String tramite, int codigoPeriodo, String coFormaPago, String isPagado, String showCheckCompklement, String[] mesecomplemento,
			Map<String, Double> mesesPorComplIndividualMonto,String nombreUsuario) throws SQLException {
		String isComplemento = "N";
		String mensaje = "";
		ManejadorDB manejadorDB = new ManejadorDB();
		;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		/** Verificamos que sea un complemento */
		if (!StringUtils.isEmpty(showCheckCompklement)) {
			isComplemento = "S";
		}

		try {

			log.debug("isPagado=" + isPagado);
			/** SI ES PAGADO COLOCAMOS EL INDICADOR NEGATIVO */
			int pagado = 1;
			try {
				pagado = new Integer(isPagado) * (-1);
			} catch (Exception e) {
				pagado = 1;
			}

			String sql = "";
			String moneda = "044";

			int resultado = -1;
			/**
			 * LOS MONTOS DE REEMBOLSO O COMPLEMENTO LO TENEMOS GUARDADOS EN LAS FACTURAS
			 **/
			sql = "INSERT INTO RHEI.FACTURA (NU_ID_FACTURA, NU_FACTURA, FE_FACTURA, NU_RIF_PROVEEDOR, NU_CONTROL, MO_FACTURA, CO_MONEDA,TX_CONCEPTO_PAGO,MO_PAGO_ADICIONAL,CO_MONEDA_ADICIONA,MO_APORTE_BCV,MO_PERIODO,MO_MATRICULA) VALUES (RHEI.SEQ_NU_ID_FACTURA.NEXTVAL,?,TO_DATE(?, 'DD-MM-YYYY'),?,?,?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, factura.getNroFactura());
			stmt.setString(2, factura.getFechaFactura());
			stmt.setString(3, factura.getNroRifProv());
			stmt.setString(4, factura.getNroControl());
			stmt.setDouble(5, factura.getMontoFactura().doubleValue());
			stmt.setString(6, moneda);
			if ("S".equalsIgnoreCase(isComplemento)) {
				stmt.setString(7, factura.getTxtcomplemento());
				stmt.setDouble(8, Constantes.campoDoubleNoSeNecesitaMas);
			} else {
				stmt.setString(7, factura.getTxtcomplemento());
				stmt.setDouble(8, factura.getMontocomplemento().doubleValue());

			}
			stmt.setString(9, moneda);
			stmt.setDouble(10, montoBCV);
			stmt.setDouble(11, montoPeriodoBCV);
			stmt.setDouble(12, montoMatriculaBCV);
			resultado += stmt.executeUpdate();
			if (resultado == 0) {
				if (!StringUtils.isEmpty(showCheckCompklement)) {

					/**
					 * En caso que exista un complemento o reembolso, la matriz de mesMatriMen es
					 * vacio y la llenamos con mesCompelmehntoReembolso
					 *******/
					if (mesMatriMen == null || mesMatriMen.size() == 0) {
						mesMatriMen = new ArrayList<Integer>();
						if (mesecomplemento != null && mesecomplemento.length > 0) {
							for (int i = 0; i < mesecomplemento.length; i++) {
								try {
									int valorCompl = Integer.parseInt(mesecomplemento[i]);
									mesMatriMen.add(valorCompl);
								} catch (Exception e) {
									mesMatriMen.add(0);
								}

							}
						}
					}
				}

				if (mesMatriMen != null && mesMatriMen.size() > 0) {
					/** Solo se guardan los meses de la factura o matricula */
					sql = "INSERT INTO RHEI.RELACION_PAGOS (NU_SOLICITUD, NU_ID_FACTURA, IN_CONCEPTO_PAGO, IN_RECEPTOR_PAGO, IN_TRAMITE, CO_MONEDA, FE_REG_PAGO, NU_REF_PAGO, MO_TOTAL_PAGO, CO_PERIODO, CO_TI_PAGO, TX_OBSERVACIONES,CO_FORMA_PAGO,IN_COMPLEMENTO,NB_PAGADO_POR) VALUES (?,RHEI.SEQ_NU_ID_FACTURA.CURRVAL,?,?,?,?,SYSDATE,?,?,?,?,?,?,?,?)";
					for (int i = 0; i < mesMatriMen.size(); i++) {
						stmt = con.prepareStatement(sql);
						stmt.setInt(1, nroSolicitud);
						stmt.setInt(2, (Integer) conceptoPago.get(0));
						stmt.setInt(3, receptorPago);
						stmt.setString(4, tramite);

						stmt.setString(5, moneda);
						/** SI ES PAGADO COLOCAMOS EL INDICADOR NEGATIVO */
						int matPeriReembComp = (Integer) mesMatriMen.get(i) * pagado;
						if (pagado < 0 && matPeriReembComp == 0) {
							/**
							 * COMO MATRICULA SU INDICADOR ES 14, ENTONCES COLOCAMOS UN INDICADOR DE
							 * MATRICULA NEGATIVO QUE REPRESENTE MATRICULA PAGA
							 */
							matPeriReembComp = Constantes.MATRICULA_PAGADA;
						}
						stmt.setInt(6, matPeriReembComp);

						stmt.setInt(8, codigoPeriodo);
						stmt.setInt(9, tipoDePago);
						stmt.setString(10, observaciones);
						stmt.setString(11, coFormaPago);

						if ("S".equalsIgnoreCase(isComplemento)) {
							/*** Chequeamos si esta pagando matricula */
							Double valor = mesesPorComplIndividualMonto.get(Math.abs(matPeriReembComp) + "");
							if (valor == null) {
								valor = 0d;
							}
							System.out.println("valor=" + valor);
							stmt.setDouble(7, valor.doubleValue());
						} else {

							if (Math.abs(matPeriReembComp) - Math.abs(Constantes.MATRICULA_PAGADA) == 0) {
								stmt.setDouble(7, montoMatriculaBCV.doubleValue());
							} else {
								/*** Chequeamos si esta pagando periodo */
								stmt.setDouble(7, montoPeriodoBCV.doubleValue());
							}
						}
						stmt.setString(12, isComplemento);
						stmt.setString(13, nombreUsuario);
						resultado += stmt.executeUpdate();
					}
					if (resultado == mesMatriMen.size()) {
						log.debug("El método se ejecutó exitosamente!!!");
						mensaje = "Exito";
					} else {
						log.debug("El método fallo!!!");
						mensaje = "Fracaso";
					}
				} else {
					/**
					 * Si solo introducimos un complemento y sin mes o matricula,pasamos por aca
					 */
					/***
					 * CUANDO SE INSERTA REEMBOLSO O COMPLEMENTO SOLAMENTE, SE LLENA UNA FILA EM
					 * RELACION DE PAGOS INDICANDO EL IN_CONCEPTO_PAGO CONCEPTO DE PAGO QUE FUE SOLO
					 * POR REEMBOLSO COMPLEMENTO, IN_RECEPTOR_PAGO QUIEN RECIBE EL PAGO (CENTRO DE
					 * EDUCACION INICIAL O TRABAJADOR, NU_REF_PAGO
					 * =Constantes.REEMBOLSO_COMPLEMENTO_NU_REF_PAGO ) EL MO_TOTAL_PAGO ES CERO,
					 * PORQUE LOS MONTOS DE REEMBOLSO O COMPLEMENTO LO TENEMOS GUARDADOS EN LAS
					 * FACTURAS
					 */
					resultado = insertComplemento(stmt, con, tipoDePago, observaciones, nroSolicitud, receptorPago, tramite, codigoPeriodo, coFormaPago, pagado);
					mensaje = "Exito";
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		} finally {
			liberarConexion(rs, stmt, con);
		}

		return mensaje;
	}

	/**
	 * CUANDO SE INSERTA REEMBOLSO O COMPLEMENTO SOLAMENTE, SE LLENA UNA FILA EM RELACION DE PAGOS
	 * INDICANDO EL IN_CONCEPTO_PAGO CONCEPTO DE PAGO QUE FUE SOLO POR REEMBOLSO COMPLEMENTO,
	 * IN_RECEPTOR_PAGO QUIEN RECIBE EL PAGO (CENTRO DE EDUCACION INICIAL O TRABAJADOR, NU_REF_PAGO
	 * =Constantes.REEMBOLSO_COMPLEMENTO_NU_REF_PAGO ) EL MO_TOTAL_PAGO ES CERO, PORQUE LOS MONTOS
	 * DE REEMBOLSO O COMPLEMENTO LO TENEMOS GUARDADOS EN LAS FACTURAS
	 * 
	 * @param stmt
	 * @param con
	 * @param tipoDePago
	 * @param observaciones
	 * @param nroSolicitud
	 * @param receptorPago
	 * @param tramite
	 * @param codigoPeriodo
	 * @param coFormaPago
	 * @return
	 * @throws SQLException
	 */
	private int insertComplemento(PreparedStatement stmt, Connection con, int tipoDePago, String observaciones, int nroSolicitud, int receptorPago, String tramite, int codigoPeriodo,
			String coFormaPago, int pagado) throws SQLException {
		int resultado = -1;

		StringBuilder sql = new StringBuilder(
				"INSERT INTO RHEI.RELACION_PAGOS (NU_SOLICITUD, NU_ID_FACTURA, IN_CONCEPTO_PAGO, IN_RECEPTOR_PAGO, IN_TRAMITE, CO_MONEDA, FE_REG_PAGO, NU_REF_PAGO, MO_TOTAL_PAGO, CO_PERIODO, CO_TI_PAGO, TX_OBSERVACIONES,CO_FORMA_PAGO) VALUES (?,RHEI.SEQ_NU_ID_FACTURA.CURRVAL,?,?,?,?,SYSDATE,?,?,?,?,?,?)");

		stmt = con.prepareStatement(sql.toString());
		stmt.setInt(1, nroSolicitud);
		/** Concepto de pago, puede ser reembolso, complemento, matricula, mes */
		/** Colocamos uno como concepto de mes, en realidad es reembolso */
		stmt.setInt(2, Constantes.REEMBOLSO_COMPLEMENTO);
		stmt.setInt(3, receptorPago);
		stmt.setString(4, tramite);

		stmt.setString(5, Constantes.MONEDA);
		/** NU_REF_PAGO */
		/** SI ES PAGADO COLOCAMOS EL INDICADOR NEGATIVO */
		// stmt.setInt(6, Constantes.REEMBOLSO_COMPLEMENTO_NU_REF_PAGO*pagado);
		stmt.setInt(6, Constantes.REEMBOLSO_COMPLEMENTO_NU_REF_PAGO * pagado);

		/** MO_TOTAL_PAGO */
		/**
		 * eL MONTO TOTAL DE COMPLEMETO O REEMBOLSO ES CERO PORQUE/ EL MONTO Y LA OBSERVACION DEL
		 * COMPLEMENTO LO TENEMOS GUARDADO EN LA FACTURA
		 */
		stmt.setDouble(7, 0);
		stmt.setInt(8, codigoPeriodo);
		// tipoDePago == tipoPagoConvencional=1;
		stmt.setInt(9, tipoDePago);

		/**
		 * observaciones DE COMPLEMETO O REEMBOLSO ES VACIO PORQUE/ LA OBSERVACION DEL COMPLEMENTO
		 * LO TENEMOS GUARDADO EN LA FACTURA
		 */

		stmt.setString(10, observaciones);
		/** Forma de pago es cheque o otro tipo de pago */
		stmt.setString(11, coFormaPago);
		resultado += stmt.executeUpdate();

		return resultado;
	}

	public boolean verificarPagoMatricula(int nroSolicitudVigente, String txDescriPeriodo) throws SQLException {
		boolean respuesta = false;
		/** MATRICULA ES CODIGO 14 */
		StringBuilder consulta = new StringBuilder(" SELECT R.NU_REF_PAGO AS NU_REF_PAGO ");
		consulta.append(" FROM RHEI.RELACION_PAGOS R INNER JOIN RHEI.PERIODO_ESCOLAR PE ON R.CO_PERIODO = PE.CO_PERIODO ");
		consulta.append("  WHERE  R.CO_TI_PAGO in (1,3)  ");
		consulta.append("  AND ( ABS(NU_REF_PAGO)=14 ) AND R.IN_COMPLEMENTO='N' ");
		ManejadorDB manejadorDB = new ManejadorDB();
		;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			if (nroSolicitudVigente > 0) {
				consulta.append(" AND NU_SOLICITUD = ? ");
			}

			if (!StringUtils.isEmpty(txDescriPeriodo)) {
				consulta.append("  AND PE.TX_DESCRIP_PERIODO = ? ");
			}

			stmt = con.prepareStatement(consulta.toString());
			int cont = 0;
			if (nroSolicitudVigente > 0) {
				cont++;
				stmt.setInt(cont, nroSolicitudVigente);
			}
			if (!StringUtils.isEmpty(txDescriPeriodo)) {
				cont++;
				stmt.setString(cont, txDescriPeriodo);
			}

			rs = stmt.executeQuery();
			if (rs.next()) {
				respuesta = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return respuesta;
	}

	public String borrarPago(Factura factura, String[] mesesMatriPeriod, int codigoPeriodo, int nroSolicitud) throws SQLException {
		String sql = "";
		String mensaje = "";
		int cantidad = -1;
		int resultado = 0;
		ManejadorDB manejadorDB = new ManejadorDB();
		;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			sql = "DELETE FROM RHEI.RELACION_PAGOS WHERE NU_SOLICITUD = ? AND NU_ID_FACTURA = ? AND NU_REF_PAGO = ? AND CO_PERIODO = ?";
			if (mesesMatriPeriod[0].compareTo("-1") != 0) {
				cantidad = mesesMatriPeriod.length;
				for (int i = 0; i < mesesMatriPeriod.length; i++) {
					stmt = con.prepareStatement(sql);
					stmt.setInt(1, nroSolicitud);
					log.debug("nroSolicitud " + nroSolicitud);
					stmt.setInt(2, factura.getNroIdFactura());
					log.debug("factura.getNroIdFactura() " + factura.getNroIdFactura());
					stmt.setInt(3, Integer.parseInt(mesesMatriPeriod[i].trim()));
					log.debug("mesesMatriPeriod[" + i + "] = " + Integer.parseInt(mesesMatriPeriod[i].trim()));
					stmt.setInt(4, codigoPeriodo);
					resultado += stmt.executeUpdate();
					log.debug("Valor de la variable resultado: " + resultado);
				}
			}
			log.debug("Valor de la variable resultado al salir del for mesesMatriPeriod: " + resultado);
			if (resultado == cantidad) {
				log.debug("Exito!!!");
				mensaje = "Exito";
			} else {
				log.debug("Fracaso!!!");
				mensaje = "Fracaso";
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			liberarConexion(rs, stmt, con);
		}

		return mensaje;
	}

	public int deleteFacturaByRelacionPagoPago(int nuIdFactura) throws SQLException {
		ManejadorDB manejadorDB = new ManejadorDB();
		;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		StringBuilder sql = new StringBuilder("");
		int resultado = -1;
		try {
			sql.append("delete RHEI.RELACION_PAGOS rp where RP.NU_ID_FACTURA=?");

			pstmt = con.prepareStatement(sql.toString());
			int posici = 1;
			pstmt.setInt(posici++, nuIdFactura);

			resultado = pstmt.executeUpdate();
			log.debug("resultado=" + resultado);
		}

		catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(null, pstmt, con);
		}
		return resultado;

	}

	public int deletePago(int nuSolicitud, int nuIdFactura, int nuRefPago, String inComplemento) throws SQLException {
		ManejadorDB manejadorDB = new ManejadorDB();
		;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		StringBuilder sql = new StringBuilder("");
		int resultado = -1;
		try {
			sql.append("delete RHEI.RELACION_PAGOS rp where  RP.NU_SOLICITUD=?  and RP.NU_ID_FACTURA=? and RP.NU_REF_PAGO=? and RP.IN_COMPLEMENTO=? ");

			pstmt = con.prepareStatement(sql.toString());
			int posici = 1;
			pstmt.setInt(posici++, nuSolicitud);
			pstmt.setInt(posici++, nuIdFactura);
			pstmt.setInt(posici++, nuRefPago);
			pstmt.setString(posici++, inComplemento);

			resultado = pstmt.executeUpdate();
			log.debug("resultado=" + resultado);
		}

		catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(null, pstmt, con);
		}
		return resultado;

	}

	/**
	 * Actualizamos pago con solo el monto
	 * 
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public int updatePago(Double montoTotal, int nroIdFactura, int nroSolicitud, int receptorPago, int InMesMatricula, String formaPago) throws SQLException {
		ManejadorDB manejadorDB = new ManejadorDB();
		;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		StringBuilder sql = new StringBuilder("");
		int resultado = -1;
		try {
			sql.append("UPDATE RHEI.RELACION_PAGOS P ");
			// formaPago
			sql.append(" SET P.MO_TOTAL_PAGO= ? ");
			if (!StringUtils.isEmpty(formaPago)) {
				sql.append(" , P.CO_FORMA_PAGO=? ");
			}
			sql.append(" WHERE P.NU_ID_FACTURA=? ");
			sql.append(" AND P.NU_SOLICITUD=?  AND P.IN_RECEPTOR_PAGO = ?");
			sql.append(" AND P.NU_REF_PAGO = ? ");

			// showResultToView.setFormaPago(rs.getString("CO_FORMA_PAGO"));
			pstmt = con.prepareStatement(sql.toString());
			int posici = 1;
			pstmt.setDouble(posici++, montoTotal);
			if (!StringUtils.isEmpty(formaPago)) {
				pstmt.setString(posici++, formaPago);
			}
			pstmt.setInt(posici++, nroIdFactura);
			pstmt.setInt(posici++, nroSolicitud);
			/** Concepto de pago */
			pstmt.setInt(posici++, receptorPago);
			pstmt.setInt(posici++, InMesMatricula);

			resultado = pstmt.executeUpdate();
			log.debug("resultado=" + resultado);
		}

		catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(null, pstmt, con);
		}
		return resultado;

	}

	public String guardarCambiosEnPago(Factura factura, Double montoMatriculaBCV, Double montoPeriodoBCV, int tipoDePago, String observaciones, Vector<Integer> mesMatriMen, int nroSolicitud,
			Vector<Integer> conceptoPago, int receptorPago, String tramite, Timestamp fechaRegistroDePago, int codigoPeriodo) throws SQLException {
		String mensaje = "";
		ManejadorDB manejadorDB = new ManejadorDB();
		;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		try {
			String sql = "";
			String moneda = "044";

			int resultado = -1;
			log.debug("Valor de la variable resultado: " + resultado);
			log.debug("Estoy dentro del método guardarCambiosEnPago de la clase RelacionDePagos....");
			sql = "UPDATE RHEI.FACTURA SET NU_FACTURA = ?, FE_FACTURA = TO_DATE(?,'DD-MM-YYYY'), NU_RIF_PROVEEDOR = ?, NU_CONTROL = ?, MO_FACTURA = ?, CO_MONEDA = ? WHERE NU_ID_FACTURA = ?";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, factura.getNroFactura());
			stmt.setString(2, factura.getFechaFactura());
			stmt.setString(3, factura.getNroRifProv());
			stmt.setString(4, factura.getNroControl());
			stmt.setDouble(5, factura.getMontoFactura().doubleValue());
			stmt.setString(6, moneda);
			stmt.setInt(7, factura.getNroIdFactura());
			resultado += stmt.executeUpdate();
			log.debug("Valor de la variable resultado: " + resultado);
			if (resultado == 0) {
				log.debug("Ejecutando segunda parte del método guardar pago");
				sql = "INSERT INTO RHEI.RELACION_PAGOS (NU_SOLICITUD, NU_ID_FACTURA, IN_CONCEPTO_PAGO, IN_RECEPTOR_PAGO, IN_TRAMITE, CO_MONEDA, FE_REG_PAGO, NU_REF_PAGO, MO_TOTAL_PAGO, CO_PERIODO, CO_TI_PAGO, TX_OBSERVACIONES) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
				for (int i = 0; i < mesMatriMen.size(); i++) {
					stmt = con.prepareStatement(sql);
					stmt.setInt(1, nroSolicitud);
					stmt.setInt(2, factura.getNroIdFactura());
					if (tipoDePago == 1) {
						if (((Integer) mesMatriMen.elementAt(i)).intValue() == 0) {
							stmt.setInt(3, ((Integer) conceptoPago.elementAt(0)).intValue());
							log.debug("conceptoPago.elementAt(0) :" + conceptoPago.elementAt(0));
						} else if (conceptoPago.size() == 1) {
							stmt.setInt(3, ((Integer) conceptoPago.elementAt(0)).intValue());
							log.debug("conceptoPago.elementAt(0) :" + conceptoPago.elementAt(0));
						} else if (conceptoPago.size() == 2) {
							stmt.setInt(3, ((Integer) conceptoPago.elementAt(1)).intValue());
							log.debug("conceptoPago.elementAt(1) :" + conceptoPago.elementAt(1));
						}
					} else if ((tipoDePago == 2) || (tipoDePago == 3)) {
						stmt.setInt(3, ((Integer) conceptoPago.elementAt(0)).intValue());
					}
					stmt.setInt(4, receptorPago);
					log.debug("receptorPago " + receptorPago);
					stmt.setString(5, tramite);

					stmt.setString(6, moneda);
					log.debug("moneda " + moneda);
					stmt.setTimestamp(7, fechaRegistroDePago);
					log.debug("fechaRegistroDePago " + fechaRegistroDePago);
					stmt.setInt(8, ((Integer) mesMatriMen.elementAt(i)).intValue());
					log.debug("mesMatriMen.elementAt(" + i + ")= " + mesMatriMen.elementAt(i));
					if (tipoDePago == 1) {
						if (((Integer) mesMatriMen.elementAt(i)).intValue() == 0) {
							stmt.setDouble(9, montoMatriculaBCV.doubleValue());
							log.debug("Monto a Pagar por matricula: " + montoMatriculaBCV);
						} else {
							stmt.setDouble(9, montoPeriodoBCV.doubleValue());
							log.debug("Monto a Pagar por período: " + montoPeriodoBCV);
						}
					} else if ((tipoDePago == 2) || (tipoDePago == 3)) {
						stmt.setDouble(9, montoPeriodoBCV.doubleValue());
						log.debug("Monto a Pagar por período: " + montoPeriodoBCV);
					}
					stmt.setInt(10, codigoPeriodo);
					log.debug("codigoPeriodo: " + codigoPeriodo);
					stmt.setInt(11, tipoDePago);
					log.debug("tipoDePago: " + tipoDePago);
					stmt.setString(12, observaciones);
					log.debug("observaciones: " + observaciones);

					resultado += stmt.executeUpdate();
					log.debug("Valor de la variable resultado: " + resultado);
				}
			}
			log.debug("Valor de la variable resultado final: " + resultado);
			log.debug("Valor de la variable mesMatriMen.size(): " + mesMatriMen.size());
			if (resultado == mesMatriMen.size()) {
				log.debug("El método se ejecutó exitosamente!!!");
				mensaje = "Exito";
			} else {
				log.debug("El método fallo!!!");
				mensaje = "Fracaso";
			}
			log.debug("Valor de la variable mensaje: " + mensaje);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			liberarConexion(null, stmt, con);
		}

		return mensaje;
	}

	public void cargarAtributosPago(HttpServletRequest request, Factura factura, String tramite, Double montoTotal, int receptorPago, int tipoPago, String observaciones) {
		request.setAttribute("estatusPago", tramite);
		request.setAttribute("fechaFactura", factura.getFechaFactura());
		log.debug("Valor de fechaFactura: " + factura.getFechaFactura());
		request.setAttribute("nroFactura", factura.getNroFactura());
		log.debug("Valor de nroFactura: " + factura.getNroFactura());
		request.setAttribute("nroControl", factura.getNroControl());
		log.debug("Valor de nroControl: " + factura.getNroControl());
		request.setAttribute("nroRif", factura.getNroRifProv());
		log.debug("Valor de nroRif: " + factura.getNroRifProv());
		request.setAttribute("montoFactura", factura.getMontoFactura());
		log.debug("Valor de montoFactura: " + factura.getMontoFactura());
		request.setAttribute("montoTotal", montoTotal);
		request.setAttribute("nu_id_factura", Integer.valueOf(factura.getNroIdFactura()));
		log.debug("Valor de nu_id_factura: " + factura.getNroIdFactura());
		request.setAttribute("receptorPago", Integer.valueOf(receptorPago));
		request.setAttribute("tipoPago", Integer.valueOf(tipoPago));
		request.setAttribute("observaciones", observaciones);
		log.debug("Se cargaron los atributos a modificar del pago");
	}

	public Factura buscarDatosPago(int nroSolicitud, int mesDeBusqueda, String nroFactura, int receptorPago, int tipoDePago, RelacionDePagos relacionDePagos) throws SQLException {
		ManejadorDB manejadorDB = new ManejadorDB();
		;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Factura factura = new Factura();

		try {
			String sql = "";
			String condicion = "";
			boolean variosConceptos = false;

			log.debug("Valor de nroSolicitud: " + nroSolicitud);
			log.debug("Valor de mesDeRegistro: " + mesDeBusqueda);
			log.debug("Valor de nroFactura: " + nroFactura);
			log.debug("Valor de tipoDePago: " + tipoDePago);

			log.debug("Entre en el metodo buscarDatosPagos con parametros con, pstmt, nroSolicitud, mesDeBusqueda y nrofactura");
			if (tipoDePago == 2) {
				condicion = "AND IN_RECEPTOR_PAGO = " + receptorPago;
			} else if (tipoDePago == 3) {
				condicion = "AND P.FE_REG_PAGO = (SELECT MAX(P1.FE_REG_PAGO) FROM RHEI.RELACION_PAGOS P1 WHERE P1.NU_SOLICITUD                             = P.NU_SOLICITUD AND TO_NUMBER(TO_CHAR(P1.FE_REG_PAGO,'MM')) = TO_NUMBER(TO_CHAR(P.FE_REG_PAGO,'MM')) AND P1.CO_TI_PAGO                           = P.CO_TI_PAGO)";
			}
			sql = "SELECT NU_SOLICITUD, P.NU_ID_FACTURA, IN_CONCEPTO_PAGO, IN_TRAMITE, NU_FACTURA, FE_FACTURA, NU_CONTROL, CO_TI_PAGO, TX_OBSERVACIONES, MO_FACTURA, NU_RIF_PROVEEDOR FROM RHEI.RELACION_PAGOS P, RHEI.FACTURA F WHERE P.NU_ID_FACTURA = F.NU_ID_FACTURA AND P.NU_SOLICITUD = ? AND TO_NUMBER(TO_CHAR(P.FE_REG_PAGO,'MM')) = ? AND F.NU_FACTURA = ? AND P.CO_TI_PAGO = "
					+ tipoDePago + " " + condicion;
			log.debug("Valor del query: " + sql);

			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, nroSolicitud);
			pstmt.setInt(2, mesDeBusqueda);
			pstmt.setString(3, nroFactura);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				log.debug("Seteando en el objeto actual y en el objeto factura");
				do {
					if (!variosConceptos) {
						relacionDePagos.setTramite(rs.getString("IN_TRAMITE"));
						factura.setFechaFactura(rs.getString("FE_FACTURA"));
						factura.setNroFactura(rs.getString("NU_FACTURA"));
						factura.setNroRifProv(rs.getString("NU_RIF_PROVEEDOR"));
						if (rs.getString("NU_CONTROL") == null) {
							factura.setNroControl("");
						} else {
							factura.setNroControl(rs.getString("NU_CONTROL"));
						}
						relacionDePagos.setTipoPago(rs.getInt("CO_TI_PAGO"));
						relacionDePagos.setObservaciones(rs.getString("TX_OBSERVACIONES"));
						factura.setMontoFactura(Double.valueOf(rs.getDouble("MO_FACTURA")));
						factura.setNroIdFactura(rs.getInt("NU_ID_FACTURA"));
					}
					variosConceptos = true;
					List<Integer> conceptoPago = new ArrayList<Integer>();
					conceptoPago.add(rs.getInt("IN_CONCEPTO_PAGO"));
					relacionDePagos.setConceptoPagoArray(conceptoPago);
				} while (

				rs.next());
			} else {
				factura = null;
				log.debug("Valor del objeto factura: " + factura);
			}
			rs.close();
			log.debug("Saliendo del método buscarDatosPago con parámetros con, pstmt, nroSolicitud, mesDeBusqueda, nroFactura");

			log.debug("Valor del objeto factura: ");
			if (factura != null) {
				log.debug("fechaFactura: " + factura.getFechaFactura());
				log.debug("montoFactura: " + factura.getMontoFactura());
				log.debug("nroControl: " + factura.getNroControl());
				log.debug("nroFactura: " + factura.getNroFactura());
				log.debug("nroIdFactura: " + factura.getNroIdFactura());
				log.debug("nroRifProv: " + factura.getNroRifProv());
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			liberarConexion(null, pstmt, con);
		}

		return factura;
	}

	public Double calcularMontoPagoBCV(Double montoMaximoBCV, String beneficioCompartido, Double montoConceptoPago) {
		Double monto = Double.valueOf(0.0D);
		if (beneficioCompartido.equalsIgnoreCase("S")) {
			if (0.5D * montoConceptoPago.doubleValue() <= montoMaximoBCV.doubleValue()) {
				monto = Double.valueOf(0.5D * montoConceptoPago.doubleValue());
			} else {
				monto = montoMaximoBCV;
			}
		} else if (montoConceptoPago.doubleValue() <= montoMaximoBCV.doubleValue()) {
			monto = montoConceptoPago;
		} else {
			monto = montoMaximoBCV;
		}
		return monto;
	}

	public void SearchMontoBCV(int nuSolicitud, double montoBcv, String inBenefCompart, double moMatricula, double moPeriodo, Connection con) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			StringBuilder sql = new StringBuilder("");
			sql.append(
					"   select RP.NU_SOLICITUD,RP.NU_ID_FACTURA,RP.IN_CONCEPTO_PAGO,RP.IN_RECEPTOR_PAGO, RP.NU_REF_PAGO from RHEI.RELACION_PAGOS rp where RP.NU_SOLICITUD=? and rp.CO_REP_STATUS is null  ");

			stmt = con.prepareStatement(sql.toString());
			int posicion = 1;
			stmt.setInt(posicion++, nuSolicitud);
			rs = stmt.executeQuery();
			while (rs.next()) {
				int nuIdDactura = rs.getInt("NU_ID_FACTURA");
				int inConceptoPago = rs.getInt("IN_CONCEPTO_PAGO");
				int inReceptorPago = rs.getInt("IN_RECEPTOR_PAGO");
				int nuRefPago = rs.getInt("NU_REF_PAGO");

				UpdateMontoBCV(nuSolicitud, montoBcv, moPeriodo, moMatricula, inBenefCompart, nuIdDactura, inConceptoPago, inReceptorPago, nuRefPago, con);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, null);
		}
	}

	private int UpdateMontoBCV(int nuSolicitud, double montoBcv, Double moPeriodo, Double moMatricula, String inBenefCompart, int nuIdDactura, int inConceptoPago, int inReceptorPago, int nuRefPago,
			Connection con) {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int exito = -1;
		try {
			if (nuSolicitud == 514) {
				log.info(nuSolicitud);
			}
			Double montoConceptoPago = 0.0d;
			if (Math.abs(nuRefPago) - Math.abs(Constantes.MATRICULA_PAGADA) == 0) {
				montoConceptoPago = calcularMontoPagoBCV(montoBcv, inBenefCompart, moMatricula);
			} else {
				montoConceptoPago = calcularMontoPagoBCV(montoBcv, inBenefCompart, moPeriodo);
			}

			StringBuilder sql = new StringBuilder("");

			sql.append(" update RHEI.RELACION_PAGOS  set mo_total_pago=? ");
			sql.append(" where  NU_SOLICITUD=? and NU_ID_FACTURA=? and IN_CONCEPTO_PAGO=? and IN_RECEPTOR_PAGO=? and NU_REF_PAGO=? and CO_REP_STATUS is null ");
			stmt = con.prepareStatement(sql.toString());
			int posicion = 1;
			stmt.setDouble(posicion++, montoConceptoPago);
			stmt.setInt(posicion++, nuSolicitud);
			stmt.setInt(posicion++, nuIdDactura);
			stmt.setInt(posicion++, inConceptoPago);
			stmt.setInt(posicion++, inReceptorPago);
			stmt.setInt(posicion++, nuRefPago);
			exito = stmt.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			liberarConexion(rs, stmt, null);
		}
		return exito;
	}

	public ShowResultToView buscardataPagoFactura(int nroSolicitud, int mesDeRegistro, int codigoPeriodo, int receptorPago, ShowResultToView showResultToView) throws SQLException {
		ManejadorDB manejadorDB = new ManejadorDB();
		;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "";
		Boolean resp = Boolean.valueOf(false);

		log.debug("Valor de nroSolicitud: " + nroSolicitud);
		log.debug("Valor de mesDeRegistro: " + mesDeRegistro);
		log.debug("Valor de codigoPeriodo: " + codigoPeriodo);
		log.debug("Valor de receptorPago: " + receptorPago);
		try {
			log.debug("Entro en verificarFactura...");

			sql = "select r.NU_REF_PAGO from rhei.relacion_pagos r inner join rhei.factura f on r.nu_id_factura = f.nu_id_factura where r.nu_solicitud = ? and TO_NUMBER(TO_CHAR(r.FE_REG_PAGO,'MM')) = ?   and r.in_receptor_pago = ? and r.co_periodo = ? ";
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, nroSolicitud);
			stmt.setInt(2, mesDeRegistro);
			stmt.setInt(3, receptorPago);
			stmt.setInt(4, codigoPeriodo);
			rs = stmt.executeQuery();
			while (rs.next()) {
				showResultToView.setInMesMatricula(rs.getInt("NU_REF_PAGO"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}

		return showResultToView;
	}

	public boolean verificarFactura(int nroSolicitud, int mesDeRegistro, int codigoPeriodo, int receptorPago) throws SQLException {
		ManejadorDB manejadorDB = new ManejadorDB();
		;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "";
		Boolean resp = Boolean.valueOf(false);

		log.debug("Valor de nroSolicitud: " + nroSolicitud);
		log.debug("Valor de mesDeRegistro: " + mesDeRegistro);
		log.debug("Valor de codigoPeriodo: " + codigoPeriodo);
		log.debug("Valor de receptorPago: " + receptorPago);
		try {
			log.debug("Entro en verificarFactura...");

			sql = "select count(*) as cantidad from rhei.relacion_pagos r inner join rhei.factura f on r.nu_id_factura = f.nu_id_factura where r.nu_solicitud = " + nroSolicitud
					+ " and TO_NUMBER(TO_CHAR(r.FE_REG_PAGO,'MM')) = " + mesDeRegistro + "  and r.in_receptor_pago = " + receptorPago + " and r.co_periodo = " + codigoPeriodo + " ";

			stmt = con.prepareStatement(sql);
			// stmt.setInt(1, nroSolicitud);
			// stmt.setInt(2, mesDeRegistro);
			// stmt.setInt(3, receptorPago);
			// stmt.setInt(4, codigoPeriodo);

			rs = stmt.executeQuery();
			if (rs.next()) {
				log.debug("rs.getInt(\"cantidad\")=" + rs.getInt("cantidad"));
				if (rs.getInt("cantidad") > 0) {
					resp = Boolean.valueOf(true);
				} else if (rs.getInt("cantidad") == 0) {
					resp = Boolean.valueOf(false);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return resp.booleanValue();
	}

	public double calcularMontoPago(int nroSolicitud, int codigoPeriodo, int tipoDePago) throws SQLException {
		Double monto = Double.valueOf(0.0D);
		ManejadorDB manejadorDB = new ManejadorDB();
		;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "";
		String condicion = "";
		if (tipoDePago == 3) {
			condicion = "IN (?,2) AND NU_REF_PAGO = 8";
		} else {
			condicion = "= ?";
		}
		log.debug("Valor de nroSolicitud: " + nroSolicitud);
		log.debug("Valor de codigoPeriodo: " + codigoPeriodo);
		log.debug("Valor de tipoDePago: " + tipoDePago);
		try {
			log.debug("Entro en calcularMontoPago...");

			sql = "SELECT SUM(MO_TOTAL_PAGO) AS MONTO FROM RHEI.RELACION_PAGOS WHERE NU_SOLICITUD = ? AND CO_PERIODO   = ? AND CO_TI_PAGO " +

					condicion;

			stmt = con.prepareStatement(sql);
			stmt.setInt(1, nroSolicitud);
			stmt.setInt(2, codigoPeriodo);
			stmt.setInt(3, tipoDePago);

			rs = stmt.executeQuery();
			if (rs.next()) {
				monto = Double.valueOf(rs.getDouble("MONTO"));
				log.debug("Valor de monto: " + monto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return monto.doubleValue();
	}

	public int calcularCantidadRegistros(int nroSolicitud, int codigoPeriodo, int tipoDePago) throws SQLException {
		int cantidadRegistro = -1;
		ManejadorDB manejadorDB = new ManejadorDB();
		;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "";
		String condicion = "";
		if (tipoDePago == 3) {
			condicion = "AND NU_REF_PAGO = 8";
		}
		log.debug("Valor de nroSolicitud: " + nroSolicitud);
		log.debug("Valor de codigoPeriodo: " + codigoPeriodo);
		log.debug("Valor de tipoDePago: " + tipoDePago);
		try {
			log.debug("Entro en calcularCantidadRegistros...");

			sql = "SELECT COUNT(*) AS CANTIDAD_REGISTROS  FROM RHEI.RELACION_PAGOS WHERE NU_SOLICITUD = ? AND CO_PERIODO   = ? AND CO_TI_PAGO = ?  " +

					condicion;

			stmt = con.prepareStatement(sql);
			stmt.setInt(1, nroSolicitud);
			stmt.setInt(2, codigoPeriodo);
			stmt.setInt(3, tipoDePago);

			rs = stmt.executeQuery();
			if (rs.next()) {
				cantidadRegistro = rs.getInt("CANTIDAD_REGISTROS");
				log.debug("Valor de cantidadRegistro: " + cantidadRegistro);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return cantidadRegistro;
	}

	public boolean verificarPagoMes(int nroSolicitud, int tipoDePago, int mes) throws SQLException {
		ManejadorDB manejadorDB = new ManejadorDB();
		;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "";
		boolean resp = false;
		try {
			log.debug("Entro en verificarPagoMes");
			sql = "SELECT COUNT(*) AS CANTIDAD FROM RHEI.RELACION_PAGOS R INNER JOIN RHEI.PERIODO_ESCOLAR PE ON R.CO_PERIODO = PE.CO_PERIODO WHERE R.NU_SOLICITUD = ? AND R.CO_TI_PAGO = ? AND R.NU_REF_PAGO = ? AND SYSDATE between PE.FE_INICIO and PE.FE_FIN ";

			stmt = con.prepareStatement(sql);
			stmt.setInt(1, nroSolicitud);
			stmt.setInt(2, tipoDePago);
			stmt.setInt(3, mes);

			rs = stmt.executeQuery();
			if (rs.next()) {
				if (rs.getInt("CANTIDAD") > 0) {
					resp = true;
				} else if (rs.getInt("CANTIDAD") == 0) {
					resp = false;
				}
				log.debug("Valor de devuelto por la consulta :" + rs.getInt("CANTIDAD"));
			}
			log.debug("Valor de resp :" + resp);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return resp;
	}

	public List<ProveedorRpago1Bean> searchSolicitudesToReporte(int coFormaPago, int receptorPago, String descripPeriodo, String numSolicituds, String coStatus, String tipoEmpleado,
			String isToCreateReportDefinitivo, String ninoEspecial) {
		ManejadorDB manejador = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		manejador = new ManejadorDB();
		;
		con = manejador.conexion();

		List<ProveedorRpago1Bean> list = new ArrayList<ProveedorRpago1Bean>();
		StringBuilder sql = new StringBuilder();
		boolean isUpdateDefinitivo = false;
		sql.append(queryComun(isUpdateDefinitivo, descripPeriodo));
		// StringBuilder sql = new StringBuilder();
		// sql.append(" SELECT unique(BEI.NU_SOLICITUD), PCEI.NB_PROVEEDOR, BEI.NU_RIF_PROVEEDOR,
		// ");
		// sql.append(" PTE.TIPO_EMP, PTE.CEDULA,(PTE.NOMBRE1 || ' ' || PTE.NOMBRE2 ||' '||
		// PTE.APELLIDO1 || ' ' || PTE.APELLIDO2 || ' ('||PTE.TIPO_EMP ||')') as TRABAJADOR,
		// PTE.NU_EXTENSION_1 ");
		// sql.append(", (PFAMILIARES.NOMBRE1 || ' ' || PFAMILIARES.NOMBRE2 ||' '||
		// PFAMILIARES.APELLIDO1 || ' ' || PFAMILIARES.APELLIDO2) as NINIO ,RP.IN_RECEPTOR_PAGO ");
		// sql.append(" FROM RHEI.SOLICITUD_BEI BEI ");
		// sql.append(" INNER JOIN RHEI.RELACION_PAGOS RP ");
		// sql.append(" ON RP.NU_SOLICITUD=BEI.NU_SOLICITUD ");
		// sql.append(" INNER JOIN RHEI.PROVEEDOR_CEI pcei ");
		// sql.append(" ON PCEI.NU_RIF_PROVEEDOR =BEI.NU_RIF_PROVEEDOR");
		// sql.append(" INNER JOIN PERSONAL.TODOS_EMPLEADOS PTE ");
		// sql.append(" ON BEI.CO_EMPLEADO=PTE.CODIGO_EMPLEADO ");
		// sql.append(" INNER JOIN PERSONAL.TIPOS_EMPLEADOS TE ");
		// sql.append(" ON TE.TIPO_EMP=PTE.TIPO_EMP");
		// sql.append(" INNER JOIN RHEI.MOV_ST_SOLIC_BEI MSS ");
		// sql.append(" ON BEI.NU_SOLICITUD=MSS.NU_SOLICITUD ");
		// sql.append(" INNER JOIN RHEI.MOV_ST_SOLIC_BEI MST ON MST.NU_SOLICITUD=BEI.NU_SOLICITUD
		// ");
		// sql.append(" INNER JOIN FAMILIARES pfamiliares ON
		// PFAMILIARES.CEDULA_FAMILIAR=BEI.CEDULA_FAMILIAR ");
		// sql.append(" INNER JOIN RHEI.PERIODO_ESCOLAR PESC ON PESC.CO_PERIODO=RP.CO_PERIODO ");
		// sql.append(" WHERE ");
		// sql.append(" PFAMILIARES.NU_CEDULA =PTE.CEDULA AND");
		// sql.append(" BEI.CO_EMPLEADO=PFAMILIARES.CODIGO_EMPLEADO AND PFAMILIARES.PARENTESCO='D'
		// AND PFAMILIARES.NU_CEDULA=PTE.CEDULA ");

		boolean isAnd = true;

		if ("1".equals(ninoEspecial)) {
			if (isAnd) {
				sql.append(" AND ");
			}
			sql.append("  	     BEI.IN_TP_EDUCACION='E'");
			isAnd = true;
		} else if ("0".equals(ninoEspecial)) {
			if (isAnd) {
				sql.append(" AND ");
			}
			sql.append("  	     BEI.IN_TP_EDUCACION='R'");
			isAnd = true;
		}

		if (coFormaPago > -1) {
			if (isAnd) {
				sql.append(" AND ");
			}
			if (Constantes.FORMA_PAGO_AMBOS.equalsIgnoreCase(coFormaPago + "")) {
				sql.append("  	    (RP.CO_FORMA_PAGO = 1 OR RP.CO_FORMA_PAGO = 2) ");
			} else {
				sql.append("  	    RP.CO_FORMA_PAGO=? ");
			}

			isAnd = true;
		}

		if (!StringUtils.isEmpty(numSolicituds)) {
			if (isAnd) {
				sql.append(" AND ");
			}

			sql.append("    RP.NU_SOLICITUD IN ( ").append(numSolicituds).append(")");
			isAnd = true;
		}
		if (!StringUtils.isEmpty(coStatus)) {
			if (isAnd) {
				sql.append(" AND ");
			}
			sql.append("    upper(MST.CO_STATUS) = upper(?)");
			isAnd = true;
		}

		if (!StringUtils.isEmpty(tipoEmpleado)) {
			if (isAnd) {
				sql.append(" AND  ");
			}
			sql.append(" (");
			StringTokenizer stk = new StringTokenizer(tipoEmpleado, ",");
			boolean isPrimeraVez = true;
			while (stk.hasMoreTokens()) {
				String elem = stk.nextToken();
				if (!isPrimeraVez) {
					sql.append("   OR   ");
				}
				sql.append("      PTE.TIPO_EMP = ('").append(elem).append("')");
				isPrimeraVez = false;
			}
			sql.append(" )");
			isAnd = true;
		}

		// String PAGADONOPAGADOAMBOS_NO_PAGADO="0";
		// String PAGADONOPAGADOAMBOS_PAGADO="1";
		// String PAGADONOPAGADOAMBOS="2";

		if (Constantes.NO_PAGADO.equalsIgnoreCase(isToCreateReportDefinitivo)) {
			if (isAnd) {
				sql.append(" AND  ");
			}
			sql.append("  RP.CO_REP_STATUS IS NULL ");
			isAnd = true;
		} else if (Constantes.PAGADO.equalsIgnoreCase(isToCreateReportDefinitivo)) {
			if (isAnd) {
				sql.append(" AND  ");
			}
			sql.append("  RP.CO_REP_STATUS IS NOT NULL ");
			isAnd = true;
		}

		if (!StringUtils.isEmpty(descripPeriodo)) {
			if (isAnd) {
				sql.append(" AND ");
			}
			sql.append("    RP.CO_PERIODO IN (SELECT PE.CO_PERIODO FROM RHEI.PERIODO_ESCOLAR PE  WHERE PE.TX_DESCRIP_PERIODO=?) ");
			isAnd = true;
		}

		// receptorPago es 0 CENTRO DE EDUCACION INICIAL, 1 TRABAJADOR
		if (receptorPago > -1) {
			if (isAnd) {
				sql.append(" AND ");
			}

			sql.append("  	     RP.IN_RECEPTOR_PAGO=? ");

			isAnd = true;
		}

		// 0 CENTRO DE EDUCACION INICIAL, 1 TRABAJADOR
		if (0 == receptorPago) {
			sql.append("    ORDER BY PCEI.NB_PROVEEDOR  ASC ");
		} else if (1 == receptorPago) {
			sql.append("       ORDER BY TRABAJADOR  ASC ");
			/** Filtramos por centro de educacion o trabajador */
		}

		try {
			stmt = con.prepareStatement(sql.toString());
			int posicion = 1;

			if (coFormaPago > -1) {
				if (!Constantes.FORMA_PAGO_AMBOS.equalsIgnoreCase(coFormaPago + "")) {
					stmt.setInt(posicion++, coFormaPago);
				}
			}
			if (!StringUtils.isEmpty(coStatus)) {
				stmt.setString(posicion++, coStatus);
			}

			if (!StringUtils.isEmpty(descripPeriodo)) {
				stmt.setString(posicion++, descripPeriodo);
			}
			if (receptorPago > -1) {
				stmt.setInt(posicion++, receptorPago);
			}
			rs = stmt.executeQuery();

			ProveedorRpago1Bean proveedorRpago1Bean = null;
			while (rs.next()) {

				proveedorRpago1Bean = new ProveedorRpago1Bean();
				proveedorRpago1Bean.setTipoEmp(rs.getString("TIPO_EMP"));
				proveedorRpago1Bean.setNuSolicitud(rs.getLong("NU_SOLICITUD"));
				proveedorRpago1Bean.setNbProveedor(rs.getString("NB_PROVEEDOR"));
				proveedorRpago1Bean.setNuRifProveedor(rs.getString("NU_RIF_PROVEEDOR"));
				proveedorRpago1Bean.setCedula(rs.getString("CEDULA"));
				proveedorRpago1Bean.setTrabajador(rs.getString("TRABAJADOR"));
				proveedorRpago1Bean.setNinio(rs.getString("NINIO"));
				proveedorRpago1Bean.setNuExtension1(rs.getString("NU_EXTENSION_1"));
				proveedorRpago1Bean.setDescripPeriodo(descripPeriodo);

				list.add(proveedorRpago1Bean);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return list;

	}

	private String queryComun(boolean isUpdateDefinitivo,String descripPeriodo){

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT unique(BEI.NU_SOLICITUD), PCEI.NB_PROVEEDOR, BEI.NU_RIF_PROVEEDOR , ");
		if (isUpdateDefinitivo){
			sql.append(" RP.NU_REF_PAGO,RP.CO_REP_STATUS, ");
		}
		sql.append("  PTE.TIPO_EMP, PTE.CEDULA,(PTE.NOMBRE1 || '  ' || PTE.NOMBRE2 ||' '|| PTE.APELLIDO1 || '  ' || PTE.APELLIDO2 || ' ('||PTE.TIPO_EMP ||')') as TRABAJADOR, PTE.NU_EXTENSION_1 ");
		sql.append(", (PFAMILIARES.NOMBRE1 || '  ' || PFAMILIARES.NOMBRE2 ||' '|| PFAMILIARES.APELLIDO1 || '  ' || PFAMILIARES.APELLIDO2) as NINIO  ,RP.IN_RECEPTOR_PAGO ");
		sql.append("   FROM RHEI.SOLICITUD_BEI BEI ");
		sql.append("    INNER JOIN  RHEI.RELACION_PAGOS RP ");
		sql.append("              ON RP.NU_SOLICITUD=BEI.NU_SOLICITUD ");
		sql.append("     INNER JOIN  RHEI.PROVEEDOR_CEI pcei ");
		sql.append("              ON PCEI.NU_RIF_PROVEEDOR =BEI.NU_RIF_PROVEEDOR");
		sql.append("   INNER JOIN PERSONAL.TODOS_EMPLEADOS PTE ");
		sql.append("               ON BEI.CO_EMPLEADO=PTE.CODIGO_EMPLEADO ");
		sql.append("   INNER JOIN PERSONAL.TIPOS_EMPLEADOS TE ");
		sql.append("               ON TE.TIPO_EMP=PTE.TIPO_EMP");
		sql.append("   INNER JOIN RHEI.MOV_ST_SOLIC_BEI MSS ");
		sql.append("             ON BEI.NU_SOLICITUD=MSS.NU_SOLICITUD ");
		sql.append("   INNER JOIN  RHEI.MOV_ST_SOLIC_BEI  MST ON MST.NU_SOLICITUD=BEI.NU_SOLICITUD ");
		sql.append("   INNER JOIN  FAMILIARES  pfamiliares   ON PFAMILIARES.CEDULA_FAMILIAR=BEI.CEDULA_FAMILIAR ");
		sql.append("     INNER JOIN RHEI.PERIODO_ESCOLAR PESC ON PESC.CO_PERIODO=RP.CO_PERIODO     ");
		sql.append("   WHERE  ");
		if (descripPeriodo!=null && descripPeriodo.length()>0){
			sql.append("  PESC.TX_DESCRIP_PERIODO='").append(descripPeriodo).append("'");
			sql.append(" and ");
		}
		
		
		sql.append("    PFAMILIARES.NU_CEDULA =PTE.CEDULA   AND");
		sql.append("   BEI.CO_EMPLEADO=PFAMILIARES.CODIGO_EMPLEADO  AND PFAMILIARES.PARENTESCO='D' AND PFAMILIARES.NU_CEDULA=PTE.CEDULA ");
		return sql.toString();

		
	}

	public String updateToDefinitivo(String nombreReporte, int coFormaPago, int receptorPago, String descripPeriodo, String coStatus, String tipoEmpleado, String filtrarByMesOrComplementoOrAmbos,
			String meses, String ninoEspecial,String nombreUsuario) throws Exception {
		ManejadorDB manejador = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		manejador = new ManejadorDB();
		;
		con = manejador.conexion();
		String result = "";
		ReporteEstatusDao reporteEstatusDao = new ReporteEstatusDaoImpl();
		List<ProveedorRpago1Bean> list = new ArrayList<ProveedorRpago1Bean>();
		StringBuilder sql = new StringBuilder();
		boolean isUpdateDefinitivo = true;
		sql.append(queryComun(isUpdateDefinitivo, descripPeriodo));
		boolean isAnd = true;

		if (coFormaPago > -1) {
			if (isAnd) {
				sql.append(" AND ");
			}
			if (coFormaPago == 3) {
				/** cheque o transferencia */
				sql.append("  	    RP.CO_FORMA_PAGO=1 or RP.CO_FORMA_PAGO=2 ");
			} else {
				sql.append("  	    RP.CO_FORMA_PAGO=? ");
			}

			isAnd = true;
		}

		if (!StringUtils.isEmpty(coStatus)) {
			if (isAnd) {
				sql.append(" AND ");
			}
			sql.append("    upper(MST.CO_STATUS) = upper(?)");
			isAnd = true;
		}

		if ("1".equals(ninoEspecial)) {
			if (isAnd) {
				sql.append(" AND ");
			}
			sql.append("  	     BEI.IN_TP_EDUCACION='E'");
			isAnd = true;
		} else if ("0".equals(ninoEspecial)) {
			if (isAnd) {
				sql.append(" AND ");
			}
			sql.append("  	     BEI.IN_TP_EDUCACION='R'");
			isAnd = true;
		}

		if (!StringUtils.isEmpty(tipoEmpleado)) {
			if (isAnd) {
				sql.append(" AND  ");
			}
			sql.append(" (");
			StringTokenizer stk = new StringTokenizer(tipoEmpleado, ",");
			boolean isPrimeraVez = true;
			while (stk.hasMoreTokens()) {
				String elem = stk.nextToken();
				if (!isPrimeraVez) {
					sql.append("   OR   ");
				}
				sql.append("      PTE.TIPO_EMP = ('").append(elem).append("')");
				isPrimeraVez = false;
			}
			sql.append(" )");
			isAnd = true;
		}

		if (isAnd) {
			sql.append(" AND  ");
		}
		sql.append("  RP.CO_REP_STATUS IS NULL ");
		isAnd = true;

		if ("0".equalsIgnoreCase(filtrarByMesOrComplementoOrAmbos)) {
			if (isAnd) {
				sql.append(" AND  ");
			}
			sql.append("  	    IN_COMPLEMENTO='N' ");
			isAnd = true;
		}
		if ("1".equalsIgnoreCase(filtrarByMesOrComplementoOrAmbos)) {
			if (isAnd) {
				sql.append(" AND  ");
			}
			sql.append("  	    IN_COMPLEMENTO='S' ");
			isAnd = true;
		}
		if ("2".equalsIgnoreCase(filtrarByMesOrComplementoOrAmbos)) {
			if (isAnd) {
				sql.append(" AND  ");
			}
			sql.append("     ( IN_COMPLEMENTO='N' OR IN_COMPLEMENTO='S' ) ");
			isAnd = true;
		}

		if (!StringUtils.isEmpty(descripPeriodo)) {
			if (isAnd) {
				sql.append(" AND ");
			}
			sql.append("  RP.CO_PERIODO   IN (SELECT PE.CO_PERIODO FROM RHEI.PERIODO_ESCOLAR PE  WHERE PE.TX_DESCRIP_PERIODO=?) ");
			isAnd = true;
		}

		if (receptorPago > -1) {
			if (isAnd) {
				sql.append(" AND ");
			}
			sql.append("  	     RP.IN_RECEPTOR_PAGO=? ");
			isAnd = true;
		}

		// 0 CENTRO DE EDUCACION INICIAL, 1 TRABAJADOR
		if (0 == receptorPago) {

			sql.append("    ORDER BY PCEI.NB_PROVEEDOR  ASC ");
		} else {
			sql.append("       ORDER BY TRABAJADOR  ASC ");
		}

		try {
			stmt = con.prepareStatement(sql.toString());
			int posicion = 1;

			/** 3 es ambos(cheque o transferencia) */
			if (coFormaPago > -1 && coFormaPago != 3) {

				stmt.setInt(posicion++, coFormaPago);
			}
			if (!StringUtils.isEmpty(coStatus)) {
				stmt.setString(posicion++, coStatus);
			}

			if (!StringUtils.isEmpty(descripPeriodo)) {
				stmt.setString(posicion++, descripPeriodo);
			}
			if (receptorPago > -1) {
				stmt.setInt(posicion++, receptorPago);
			}
			rs = stmt.executeQuery();
			con.setAutoCommit(false);
			reporteEstatusDao.guardar(nombreReporte, receptorPago + "", con,nombreUsuario);
			while (rs.next()) {
				String numSoli = rs.getString("NU_SOLICITUD");
				String mes = rs.getString("NU_REF_PAGO");

				String resultAux = reporteEstatusDao.updateReporteStatusII(numSoli, mes, descripPeriodo, receptorPago, coFormaPago, filtrarByMesOrComplementoOrAmbos, con);

				// String resultAux = updateTransitivoToReporte(nombreReporte, coFormaPago,
				// receptorPago, descripPeriodo, numSoliIndividual, coStatus,
				// tipoEmpleado, filtrarByMesOrComplementoOrAmbos, meses,con);

				if (StringUtils.isEmpty(result) || "0".equalsIgnoreCase(result)) {
					result = resultAux;
				}

				resultAux = null;

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (!StringUtils.isEmpty(result) && !"0".equalsIgnoreCase(result)) {
				con.commit();
			} else {
				con.rollback();
				result = "reporte.no.hay.registros";
			}
			liberarConexion(rs, stmt, con);
		}
		return result;

	}

	public String updateTransitivoToReporte(String nombreReporte, int coFormaPago, int receptorPago, String descripPeriodo, String numSolicituds, String coStatus, String tipoEmpleado,
			String filtrarByMesOrComplementoOrAmbos, String meses, Connection con) throws Exception {

		ReporteEstatusDao reporteEstatusDao = new ReporteEstatusDaoImpl();

		String resultado = "";

		if (nombreReporte != null) {
			try {

				if (!StringUtils.isEmpty(numSolicituds)) {

					if (!StringUtils.isEmpty(meses)) {
						StringTokenizer mesesStk = new StringTokenizer(meses, ",");
						String mes = "";
						while (mesesStk.hasMoreTokens()) {
							mes = (String) mesesStk.nextToken();

							String resultAux = reporteEstatusDao.updateReporteStatusII(numSolicituds, mes, descripPeriodo, receptorPago, coFormaPago, filtrarByMesOrComplementoOrAmbos, con);
							if (StringUtils.isEmpty(resultado) || "0".equalsIgnoreCase(resultado)) {
								resultado = resultAux;
							}
							resultAux = null;

						}

					}

				}
			} finally {

				liberarConexion(null, null, null);

			}
		}

		return resultado;
	}

	/**
	 * @param coFormaPago
	 * @param receptorPago
	 * @param descripcionPeriodo
	 * @param numSolicituds
	 * @param coStatus
	 * @return
	 */

	public List<Integer> searchSolicitudesToReporte(int coFormaPago, int receptorPago, String descripcionPeriodo) {

		List<Integer> numSolicitudes = new ArrayList<Integer>();
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT UNIQUE(RP.NU_SOLICITUD) FROM RHEI.RELACION_PAGOS RP ");
		sql.append(" INNER JOIN  RHEI.PERIODO_ESCOLAR PE ");
		sql.append(" ON RP.CO_PERIODO=PE.CO_PERIODO ");
		sql.append(" WHERE ");
		boolean isAnd = false;

		if (coFormaPago > 0) {
			sql.append(" RP.CO_FORMA_PAGO=? ");
			isAnd = true;
		}
		if (isAnd) {
			sql.append(" AND ");
		}
		if (receptorPago > 0) {
			sql.append(" RP.IN_RECEPTOR_PAGO=? ");
			isAnd = true;
		}
		if (isAnd) {
			sql.append(" AND ");
		}
		if (!StringUtils.isEmpty(descripcionPeriodo)) {
			sql.append(" PE.TX_DESCRIP_PERIODO=? ");
			isAnd = true;
		}

		sql.append("  ORDER BY RP.NU_SOLICITUD DESC ");
		try {
			ManejadorDB manejadorDB = new ManejadorDB();
			;
			con = manejadorDB.coneccionPool();
			int posicion = 1;
			stmt = con.prepareStatement(sql.toString());
			if (coFormaPago > 0) {
				stmt.setInt(posicion++, coFormaPago);
			}
			if (receptorPago > 0) {
				stmt.setInt(posicion++, receptorPago);
			}
			if (!StringUtils.isEmpty(descripcionPeriodo)) {
				stmt.setString(posicion++, descripcionPeriodo);
			}

			rs = stmt.executeQuery();
			while (rs.next()) {
				numSolicitudes.add(rs.getInt("NU_SOLICITUD"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}

		return numSolicitudes;

	}

	/**
	 * 
	 * 
	 * @param nroSolicitud
	 * @param nuIdFactura
	 * @param inMesMatricula
	 * @param periodoEscolar
	 * @param showResultToView
	 * @return
	 * @throws SQLException
	 */
	public List<BeneficiarioBean2> obtenerRelacionPagoYfactura(int nroSolicitud, int nuIdFactura, int inMesMatricula, String periodoEscolar, ShowResultToView showResultToView) throws SQLException {
		ManejadorDB manejadorDB = new ManejadorDB();
		;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<BeneficiarioBean2> reporteMatriculaPeriodoLst = new ArrayList<BeneficiarioBean2>();
		int posicion = 0;
		StringBuilder sql = new StringBuilder("");
		log.debug("Valor de nroSolicitud: " + nroSolicitud);
		/** Colocamos esta variable TRAMITE por default */
		/** FIN Colocamos esta variable TRAMITE por default */

		try {
			sql.append(
					" SELECT P.CO_FORMA_PAGO, BEI.NU_SOLICITUD,P.NU_REF_PAGO,PARAM.TX_OBSERVACIONES MES_TITLE,PE.TX_DESCRIP_PERIODO,F.NU_ID_FACTURA,F.NU_FACTURA  NROFACTURA,F.NU_RIF_PROVEEDOR,to_char(F.FE_FACTURA,'DD-MM-YYYY') FECHAFACTURA,F.NU_CONTROL NROCONTROL, ");
			sql.append(" PFAMILIARES.CEDULA_FAMILIAR cedulaFamiliar,PFAMILIARES.NOMBRE1 || '  ' || PFAMILIARES.NOMBRE2 nombreFlia,");
			sql.append(" PFAMILIARES.APELLIDO1 || ' ' || PFAMILIARES.APELLIDO2 apellidoFlia,");
			sql.append(" P.IN_TRAMITE ESTATUSPAGO,to_char(P.FE_REG_PAGO,'DD-MM-YYYY') FECHAREGISTRO,P.CO_PERIODO CODIGOPERIODO, ");
			sql.append(" P.NU_SOLICITUD, P.NU_ID_FACTURA, IN_CONCEPTO_PAGO, IN_TRAMITE, FE_FACTURA, NU_CONTROL,P.IN_RECEPTOR_PAGO RECEPTORPAGO,");
			sql.append(" F.MO_FACTURA MONTOFACTURA ,P.MO_TOTAL_PAGO MONTOTOTAL ");
			sql.append("  FROM  RHEI.RELACION_PAGOS P, RHEI.FACTURA F,RHEI.PERIODO_ESCOLAR PE,RHEI.SOLICITUD_BEI bei,RHEI.PARAMETRO PARAM ");
			sql.append(",PERSONAL.FAMILIARES  pfamiliares");
			sql.append(" WHERE P.NU_ID_FACTURA = F.NU_ID_FACTURA  AND PE.CO_PERIODO= P.CO_PERIODO ");

			sql.append(" AND BEI.NU_SOLICITUD=P.NU_SOLICITUD  ");
			sql.append(" AND PARAM.TX_VALOR_PARAMETRO=ABS(p.NU_REF_PAGO)  AND PARAM.CO_PARAMETRO LIKE 'MES%' ");
			sql.append(" AND PFAMILIARES.CODIGO_EMPLEADO=BEI.CO_EMPLEADO AND  PFAMILIARES.CEDULA_FAMILIAR=BEI.CEDULA_FAMILIAR ");
			sql.append(" AND param.CO_COMPANIA='").append(showResultToView.getCompania()).append("'").append(" AND PARAM.CO_TIPO_BENEFICIO='").append(Constantes.TIPOBENEFICIO).append("' ");

			if (!StringUtils.isEmpty(showResultToView.getCedula()) && StringUtils.isNumeric(showResultToView.getCedula())) {
				long cedula = new Long(showResultToView.getCedula());
				if (cedula > 0) {
					sql.append("AND BEI.CO_EMPLEADO IN ( SELECT CODIGO_EMPLEADO FROM PERSONAL.TODOS_EMPLEADOS WHERE CEDULA=");
					sql.append(cedula).append(")");
				}

			}

			if (nroSolicitud > 0) {
				sql.append(" AND P.NU_SOLICITUD = ? ");
			}
			if (nuIdFactura > 0) {
				sql.append(" AND F.NU_ID_FACTURA = ? ");
			}
			/**
			 * Si no es la busqueda por numero de factura, entonces podemos buscar por matricula y
			 * beneficiario
			 */
			if (inMesMatricula >= 0) {
				sql.append(" AND ABS(P.NU_REF_PAGO) = ? ");
			}

			if (!StringUtils.isEmpty(periodoEscolar)) {
				sql.append("AND P.CO_PERIODO IN (select PE2.CO_PERIODO from RHEI.PERIODO_ESCOLAR PE2 WHERE PE2.TX_DESCRIP_PERIODO=? )");
			}
			sql.append(" ORDER BY P.NU_REF_PAGO ASC ");

			stmt = con.prepareStatement(sql.toString());
			if (nroSolicitud > 0) {
				posicion++;
				stmt.setInt(posicion, nroSolicitud);
			}
			if (nuIdFactura > 0) {
				posicion++;
				stmt.setInt(posicion, nuIdFactura);
			}
			/** 0 EQUAL MATRICULA, 1 AL 12 ES DE ENERO A FEBRERO */
			if (inMesMatricula >= 0) {
				posicion++;
				stmt.setInt(posicion, inMesMatricula);
			}
			if (!StringUtils.isEmpty(periodoEscolar)) {
				posicion++;
				stmt.setString(posicion, periodoEscolar);
			}

			rs = stmt.executeQuery();
			boolean hayData = false;

			if (rs.next()) {

				hayData = true;
				/** Obtenemos el rif del Centro de educacion inicial */
				showResultToView.setNroSolicitud(rs.getInt("NU_SOLICITUD"));
				showResultToView.setNroRifCentroEdu(rs.getString("NU_RIF_PROVEEDOR"));
				/** Fecha Registro: */
				showResultToView.setFechaActual(rs.getString("FECHAREGISTRO"));
				/** Si es vacio estatus pago, lo sobreescribimos por bd */
				showResultToView.setEstatusPago(rs.getString("ESTATUSPAGO"));
				if (Constantes.T.equalsIgnoreCase(showResultToView.getEstatusPago())) {
					showResultToView.setEstatusPago(Constantes.TRAMITADO);
				} else if (Constantes.P.equalsIgnoreCase(showResultToView.getEstatusPago())) {
					showResultToView.setEstatusPago(Constantes.TRAMITADO);
				}

				/** Fecha Factura: */
				showResultToView.setFechaFactura(rs.getString("FECHAFACTURA"));
				/** N� Factura: */
				showResultToView.setNroFactura(rs.getString("NROFACTURA"));
				showResultToView.setNuIdFactura(rs.getInt("NU_ID_FACTURA"));

				/** N� Control */
				showResultToView.setNroControl(rs.getString("NROCONTROL"));

				/** Monto de factura */
				showResultToView.setMontoFactura(rs.getDouble("MONTOFACTURA"));

				/** Receptorpago */
				showResultToView.setReceptorPago(rs.getString("RECEPTORPAGO"));

				showResultToView.setCodigoPeriodo(rs.getInt("CODIGOPERIODO"));
				showResultToView.setPeriodoEscolar(rs.getString("TX_DESCRIP_PERIODO"));

				/** Monto total */
				showResultToView.setMontoTotal(rs.getDouble("MONTOTOTAL"));

				/**
				 * Buscamos el nombre del mes a traves del enum Esto si lo traenmos filrtrado de la
				 * vista
				 */
				inMesMatricula = rs.getInt("NU_REF_PAGO");
				showResultToView.setInMesMatricula(inMesMatricula);

				showResultToView.setFormaPago(rs.getString("CO_FORMA_PAGO"));

				/** Fin Buscamos el nombre del mes a traves del enum */
				/** Primer bloque del rep�rte **/

			}
			if (!hayData) {
				showResultToView.setError("1");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return reporteMatriculaPeriodoLst;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bcv.dao.jdbc.RelacionDePagosDao#obtenerRelacionPagoYfactura(int, int,
	 * java.lang.String, ve.org.bcv.rhei.bean.ShowResultToView)
	 */
	@Override
	public List<BeneficiarioBean2> obtenerRelacionPagoYfactura(int nroSolicitud, int nuIdFactura, String periodoEscolar, ShowResultToView showResultToView) throws SQLException {
		ManejadorDB manejadorDB = new ManejadorDB();
		;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<BeneficiarioBean2> reporteMatriculaPeriodoLst = new ArrayList<BeneficiarioBean2>();
		int posicion = 0;
		StringBuilder sql = new StringBuilder("");
		log.debug("Valor de nroSolicitud: " + nroSolicitud);
		/** Colocamos esta variable TRAMITE por default */
		/** FIN Colocamos esta variable TRAMITE por default */

		try {
			sql.append(
					" SELECT P.CO_FORMA_PAGO, BEI.NU_SOLICITUD,P.NU_REF_PAGO,PARAM.TX_OBSERVACIONES MES_TITLE,PE.TX_DESCRIP_PERIODO,F.NU_ID_FACTURA,F.NU_FACTURA  NROFACTURA,F.NU_RIF_PROVEEDOR,to_char(F.FE_FACTURA,'DD-MM-YYYY') FECHAFACTURA,F.NU_CONTROL NROCONTROL, ");
			sql.append(" PFAMILIARES.CEDULA_FAMILIAR cedulaFamiliar,PFAMILIARES.NOMBRE1 || '  ' || PFAMILIARES.NOMBRE2 nombreFlia,");
			sql.append(" PFAMILIARES.APELLIDO1 || ' ' || PFAMILIARES.APELLIDO2 apellidoFlia,");
			sql.append(" P.IN_TRAMITE ESTATUSPAGO,to_char(P.FE_REG_PAGO,'DD-MM-YYYY') FECHAREGISTRO,P.CO_PERIODO CODIGOPERIODO, ");
			sql.append(" P.NU_SOLICITUD, P.NU_ID_FACTURA, IN_CONCEPTO_PAGO, IN_TRAMITE, FE_FACTURA, NU_CONTROL,P.IN_RECEPTOR_PAGO RECEPTORPAGO,");
			sql.append(" F.MO_FACTURA MONTOFACTURA ,P.MO_TOTAL_PAGO MONTOTOTAL ");
			sql.append("  FROM  RHEI.RELACION_PAGOS P, RHEI.FACTURA F,RHEI.PERIODO_ESCOLAR PE,RHEI.SOLICITUD_BEI bei,RHEI.PARAMETRO PARAM ");
			sql.append(",PERSONAL.FAMILIARES  pfamiliares");
			sql.append(" WHERE P.NU_ID_FACTURA = F.NU_ID_FACTURA  AND PE.CO_PERIODO= P.CO_PERIODO ");

			sql.append(" AND BEI.NU_SOLICITUD=P.NU_SOLICITUD  ");
			sql.append(" AND PARAM.TX_VALOR_PARAMETRO=ABS(p.NU_REF_PAGO)  AND PARAM.CO_PARAMETRO LIKE 'MES%' ");
			sql.append(" AND PFAMILIARES.CODIGO_EMPLEADO=BEI.CO_EMPLEADO AND  PFAMILIARES.CEDULA_FAMILIAR=BEI.CEDULA_FAMILIAR ");
			sql.append(" AND param.CO_COMPANIA='").append(showResultToView.getCompania()).append("'").append(" AND PARAM.CO_TIPO_BENEFICIO='").append(Constantes.TIPOBENEFICIO).append("' ");

			if (!StringUtils.isEmpty(showResultToView.getCedula()) && StringUtils.isNumeric(showResultToView.getCedula())) {
				long cedula = new Long(showResultToView.getCedula());
				if (cedula > 0) {
					sql.append("AND BEI.CO_EMPLEADO IN ( SELECT CODIGO_EMPLEADO FROM PERSONAL.TODOS_EMPLEADOS WHERE CEDULA=");
					sql.append(cedula).append(")");
				}

			}

			if (nroSolicitud > 0) {
				sql.append(" AND P.NU_SOLICITUD = ? ");
			}
			if (nuIdFactura > 0) {
				sql.append(" AND F.NU_ID_FACTURA = ? ");
			}

			if (!StringUtils.isEmpty(periodoEscolar)) {
				sql.append("AND P.CO_PERIODO IN (select PE2.CO_PERIODO from RHEI.PERIODO_ESCOLAR PE2 WHERE PE2.TX_DESCRIP_PERIODO=? )");
			}
			sql.append(" ORDER BY P.NU_REF_PAGO ASC ");

			stmt = con.prepareStatement(sql.toString());
			if (nroSolicitud > 0) {
				posicion++;
				stmt.setInt(posicion, nroSolicitud);
			}
			if (nuIdFactura > 0) {
				posicion++;
				stmt.setInt(posicion, nuIdFactura);
			}

			if (!StringUtils.isEmpty(periodoEscolar)) {
				posicion++;
				stmt.setString(posicion, periodoEscolar);
			}

			rs = stmt.executeQuery();
			boolean hayData = false;

			if (rs.next()) {

				hayData = true;
				/** Obtenemos el rif del Centro de educacion inicial */
				showResultToView.setNroSolicitud(rs.getInt("NU_SOLICITUD"));
				if (StringUtils.isEmpty(showResultToView.getNroRifCentroEdu())) {
					showResultToView.setNroRifCentroEdu(rs.getString("NU_RIF_PROVEEDOR"));
				}
				/** Fecha Registro: */
				showResultToView.setFechaActual(rs.getString("FECHAREGISTRO"));
				/** Si es vacio estatus pago, lo sobreescribimos por bd */
				showResultToView.setEstatusPago(rs.getString("ESTATUSPAGO"));
				if (Constantes.T.equalsIgnoreCase(showResultToView.getEstatusPago())) {
					showResultToView.setEstatusPago(Constantes.TRAMITADO);
				} else if (Constantes.P.equalsIgnoreCase(showResultToView.getEstatusPago())) {
					showResultToView.setEstatusPago(Constantes.TRAMITADO);
				}

				/** Fecha Factura: */
				showResultToView.setFechaFactura(rs.getString("FECHAFACTURA"));
				/** N� Factura: */
				showResultToView.setNroFactura(rs.getString("NROFACTURA"));
				showResultToView.setNuIdFactura(rs.getInt("NU_ID_FACTURA"));

				/** N� Control */
				showResultToView.setNroControl(rs.getString("NROCONTROL"));

				/** Monto de factura */
				showResultToView.setMontoFactura(rs.getDouble("MONTOFACTURA"));

				/** Receptorpago */
				showResultToView.setReceptorPago(rs.getString("RECEPTORPAGO"));

				showResultToView.setCodigoPeriodo(rs.getInt("CODIGOPERIODO"));
				showResultToView.setPeriodoEscolar(rs.getString("TX_DESCRIP_PERIODO"));

				/** Monto total */
				showResultToView.setMontoTotal(rs.getDouble("MONTOTOTAL"));

				/**
				 * Buscamos el nombre del mes a traves del enum Esto si lo traenmos filrtrado de la
				 * vista
				 */
				showResultToView.setInMesMatricula(rs.getInt("NU_REF_PAGO"));

				showResultToView.setFormaPago(rs.getString("CO_FORMA_PAGO"));

				/** Fin Buscamos el nombre del mes a traves del enum */
				/** Primer bloque del rep�rte **/

			}
			if (!hayData) {
				showResultToView.setError("1");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return reporteMatriculaPeriodoLst;
	}

	public ReporteByDefinitivoOrTransitivo reporteByDefinitivoOrTransitivo(String nroSolicitud, int coRepStatus) throws SQLException {
		ReporteByDefinitivoOrTransitivo reporteByDefinitivoOrTransitivo = null;
		ManejadorDB manejadorDB = new ManejadorDB();
		;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder("");
		;
		sql.append(" select RP.NU_SOLICITUD, rp.nu_ref_pago  as mes,PE.TX_DESCRIP_PERIODO  as descripPeriodo, RP.IN_RECEPTOR_PAGO as receptorPago,RP.CO_FORMA_PAGO coFormaPago1, ");
		sql.append("    MST.CO_STATUS AS coStatus, PTE.TIPO_EMP as tipoEmpleado,ST.NB_TIPO,  RP.IN_COMPLEMENTO ");
		sql.append("        from RHEI.RELACION_PAGOS rp ");
		sql.append("       inner join  RHEI.PERIODO_ESCOLAR pe ");
		sql.append("     on RP.CO_PERIODO=PE.CO_PERIODO ");
		sql.append("      inner join RHEI.MOV_ST_SOLIC_BEI MST ");
		sql.append("        on MST.NU_SOLICITUD=RP.NU_SOLICITUD ");
		sql.append("   inner join  RHEI.SOLICITUD_BEI BEI ");
		sql.append("              on RP.NU_SOLICITUD=BEI.NU_SOLICITUD ");
		sql.append("  inner join  PERSONAL.TODOS_EMPLEADOS PTE ");
		sql.append("       on PTE.CODIGO_EMPLEADO=BEI.CO_EMPLEADO ");
		sql.append("               inner join  RHEI.REPORTE_STATUS ST ON ST.CO_REP_STATUS=RP.CO_REP_STATUS  ");
		sql.append("    where RP.NU_SOLICITUD in (").append(nroSolicitud).append(")    ");
		if (coRepStatus > 0) {
			sql.append("         AND ST.CO_REP_STATUS=").append(coRepStatus);
		}
		sql.append("   order by RP.NU_SOLICITUD ");
		try {
			stmt = con.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			boolean isPrimeravez = true;
			String isComplemento = "";
			/** Forma ded pago puede ser por transferencia o por cheque (1 op 2) */
			/**
			 * Si el definitivo lo hicieron con forma de pago 1 y 2 (ambo), chequearemos, si es
			 * asi.. colocaremos 3
			 */
			boolean formaPagoCambio = false;
			int coFormaPago1 = -1;
			while (rs.next()) {
				if (isPrimeravez) {
					reporteByDefinitivoOrTransitivo = new ReporteByDefinitivoOrTransitivo();
					if (coRepStatus > 0) {
						reporteByDefinitivoOrTransitivo.setToReportDefinitivo(true);
					}
					reporteByDefinitivoOrTransitivo.setNroSolicitud(nroSolicitud);
					reporteByDefinitivoOrTransitivo.setMeses(rs.getString("mes"));
					reporteByDefinitivoOrTransitivo.setDescripPeriodo(rs.getString("descripPeriodo"));
					reporteByDefinitivoOrTransitivo.setReceptorPago(rs.getInt("receptorPago"));
					reporteByDefinitivoOrTransitivo.setFiltrarByMesOrComplementoOrAmbos(Integer.parseInt(Constantes.FILTRARBYMESORCOMPLEMENTOORAMBOS_AMBOS));
					isComplemento = rs.getString("IN_COMPLEMENTO");
					if (Constantes.IN_COMPLEMENTO.equalsIgnoreCase(isComplemento)) {
						reporteByDefinitivoOrTransitivo.setFiltrarByMesOrComplementoOrAmbos(Integer.parseInt(Constantes.FILTRARBYMESORCOMPLEMENTOORAMBOS_REEMBOLSO));
					} else if (Constantes.NO_IN_COMPLEMENTO.equalsIgnoreCase(isComplemento)) {
						reporteByDefinitivoOrTransitivo.setFiltrarByMesOrComplementoOrAmbos(Integer.parseInt(Constantes.FILTRARBYMESORCOMPLEMENTOORAMBOS_MATRICULA));
					}

					reporteByDefinitivoOrTransitivo.setCoFormaPago1(rs.getInt("coFormaPago1"));
					reporteByDefinitivoOrTransitivo.setCoStatus(rs.getString("coStatus"));
					reporteByDefinitivoOrTransitivo.setTipoEmpleado(rs.getString("tipoEmpleado"));
					isPrimeravez = false;
				} else {
					reporteByDefinitivoOrTransitivo.setMeses(reporteByDefinitivoOrTransitivo.getMeses() + "," + rs.getString("mes"));
					/**
					 * verificamos si la formz de pago cambia, si cambia es porque se pago con
					 * cheque y con transferencia.. es decir ambos
					 */
					coFormaPago1 = rs.getInt("coFormaPago1");
				}
				/**
				 * chequeamos si esta pagando por cheaue y transferencia.. es decir, ambos .. Si es
				 * ambos.. colocamos forma de pago igual a tres
				 */
				if (coFormaPago1 != -1 && coFormaPago1 != reporteByDefinitivoOrTransitivo.getCoFormaPago1()) {
					reporteByDefinitivoOrTransitivo.setCoFormaPago1(new Integer(Constantes.FORMA_PAGO_AMBOS));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}

		return reporteByDefinitivoOrTransitivo;
	}

	public ReporteByDefinitivoOrTransitivo reporteByConsulta(String nroSolicitud, String companiaAnalista) throws SQLException {
		ReporteByDefinitivoOrTransitivo reporteByDefinitivoOrTransitivo = null;
		ManejadorDB manejadorDB = new ManejadorDB();
		;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder("");
		;
		sql.append(" select  RP.NU_SOLICITUD, rp.nu_ref_pago  as mes,PE.TX_DESCRIP_PERIODO  as descripPeriodo, RP.IN_RECEPTOR_PAGO as receptorPago,RP.CO_FORMA_PAGO coFormaPago1, ");
		sql.append("    MST.CO_STATUS AS coStatus, PTE.TIPO_EMP as tipoEmpleado  ");
		sql.append("        from RHEI.RELACION_PAGOS rp ");
		sql.append("       inner join  RHEI.PERIODO_ESCOLAR pe ");
		sql.append("     on RP.CO_PERIODO=PE.CO_PERIODO ");
		sql.append("      inner join RHEI.MOV_ST_SOLIC_BEI MST ");
		sql.append("        on MST.NU_SOLICITUD=RP.NU_SOLICITUD ");
		sql.append("   inner join  RHEI.SOLICITUD_BEI BEI ");
		sql.append("              on RP.NU_SOLICITUD=BEI.NU_SOLICITUD ");
		sql.append("  inner join  PERSONAL.TODOS_EMPLEADOS PTE ");
		sql.append("       on PTE.CODIGO_EMPLEADO=BEI.CO_EMPLEADO ");
		sql.append("    where RP.NU_SOLICITUD in (").append(nroSolicitud).append(")    ");
		sql.append("   order by RP.NU_SOLICITUD ");
		try {
			stmt = con.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			boolean isPrimeravez = true;
			while (rs.next()) {
				if (isPrimeravez) {
					reporteByDefinitivoOrTransitivo = new ReporteByDefinitivoOrTransitivo();
					reporteByDefinitivoOrTransitivo.setNroSolicitud(nroSolicitud);
					reporteByDefinitivoOrTransitivo.setDescripPeriodo(rs.getString("descripPeriodo"));
					reporteByDefinitivoOrTransitivo.setCoFormaPago1(rs.getInt("coFormaPago1"));
					reporteByDefinitivoOrTransitivo.setCoStatus(rs.getString("coStatus"));
					reporteByDefinitivoOrTransitivo.setReceptorPago(rs.getInt("receptorPago"));
					reporteByDefinitivoOrTransitivo.setTipoEmpleado(rs.getString("tipoEmpleado"));
					reporteByDefinitivoOrTransitivo.setCompaniaAnalista(companiaAnalista);

					isPrimeravez = false;
				} else {
					reporteByDefinitivoOrTransitivo.setMeses(reporteByDefinitivoOrTransitivo.getMeses() + "," + rs.getString("mes"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}

		return reporteByDefinitivoOrTransitivo;
	}

	/**
	 * Solicitud que estan en el deparatamento de relacion de pagos
	 * 
	 * @param defTranstCodigo
	 * @return
	 * @throws SQLException
	 */
	public String listNumSolicitudsReporteDefinitivoTransitorio(int defTranstCodigo) throws SQLException {
		ManejadorDB manejadorDB = new ManejadorDB();
		;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		StringBuilder listaNroSolicitud = new StringBuilder("");

		StringBuilder sql = new StringBuilder("");
		try {

			sql.append("select  R.nu_solicitud from RHEI.RELACION_PAGOS R inner join  RHEI.SOLICITUD_BEI B on R.NU_SOLICITUD=B.NU_SOLICITUD ");
			sql.append("         inner join   RHEI.PROVEEDOR_CEI prov on B.NU_RIF_PROVEEDOR=PROV.NU_RIF_PROVEEDOR where R.CO_REP_STATUS=? order by PROV.NB_PROVEEDOR ASC ");

			// sql.append("SELECT unique( rp.nu_solicitud) FROM RHEI.RELACION_PAGOS rp where
			// RP.CO_REP_STATUS=? order by rp.NU_SOLICITUD desc");
			stmt = con.prepareStatement(sql.toString());
			int posicion = 1;
			stmt.setInt(posicion++, defTranstCodigo);
			rs = stmt.executeQuery();
			if (rs != null) {
				boolean isPrimeravez = true;
				Map<Integer, Integer> unico = new HashMap<Integer, Integer>();
				while (rs.next()) {
					int numSolic = rs.getInt("nu_solicitud");

					if (!unico.containsKey(numSolic)) {
						unico.put(numSolic, numSolic);
						if (isPrimeravez) {
							listaNroSolicitud.append(numSolic);
							isPrimeravez = false;
						} else {
							listaNroSolicitud.append(",").append(numSolic);
						}
					}

				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return listaNroSolicitud.toString();
	}

}
