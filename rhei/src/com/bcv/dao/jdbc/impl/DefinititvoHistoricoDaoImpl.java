/**
 * 
 */
package com.bcv.dao.jdbc.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.bcv.dao.jdbc.DefinititvoHistoricoDao;
import com.bcv.dao.jdbc.ManejadorDB;
import com.bcv.dao.jdbc.SimpleJDBCDaoImpl;
import com.bcv.model.DetalleDefHistorico;

import ve.org.bcv.rhei.util.Constantes;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco 22/08/2016 15:34:11 2016 mail :
 *         oraclefedora@gmail.com
 */
public class DefinititvoHistoricoDaoImpl extends SimpleJDBCDaoImpl<DetalleDefHistorico>
		implements DefinititvoHistoricoDao {

	public DetalleDefHistorico insert(DetalleDefHistorico detalleDef) throws Exception {
		ManejadorDB manejadorDB = new ManejadorDB();
		;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int resultado = 0;
		StringBuilder sql = new StringBuilder("");
		// System.out.println("detalleDef.getCodigo()="+detalleDef.getCoDefHistorico());
		// System.out.println("detalleDef.getConcepto()="+detalleDef.getConcepto());
		// System.out.println("detalleDef.getCoRepStatus()="+detalleDef.getCoRepStatus());
		// System.out.println("detalleDef.getMontoMensualPagTrab()="+detalleDef.getMontoMensualPagTrab());
		// System.out.println("detalleDef.getMontoTotalStr()="+detalleDef.getMontoTotal());
		// System.out.println("detalleDef.getNombreColegio()="+detalleDef.getNombreColegio());
		// System.out.println("detalleDefgetNombreNino()="+detalleDef.getNombreNino());
		// System.out.println("detalleDef.getTrabajador()="+detalleDef.getTrabajador());
		// System.out.println("detalleDef.getObservacion()="+detalleDef.getObservacion());
		try {

			/** Sacamos la secuencia */
			pstmt = con.prepareStatement("SELECT RHEI.SEQ_CO_DEF_REP_HIST.NEXTVAL AS valor from dual");

			ResultSet result = pstmt.executeQuery();
			int co_def_rep_hist = 0;
			if (result != null) {
				while (result.next()) {
					co_def_rep_hist = result.getInt("valor");
				}
			}

			sql = new StringBuilder("");
			sql.append(
					"INSERT INTO RHEI.DEF_REP_HISTORICO (CO_REP_STATUS,CO_DEF_REP_HIST,NB_COLEGIO , NB_TRABAJADOR, NB_NINO,MO_MENS_PAG_TRAB,NB_CONCEPTO_PAGO,MO_TOTAL_FACT,FE_FACTURA,TX_OBSERVACION) ");
			sql.append(" VALUES (?,?,?,?,?,?,?,?,?,?)");
			pstmt = con.prepareStatement(sql.toString());

			pstmt.setInt(1, Integer.parseInt(detalleDef.getCoRepStatus()));
			pstmt.setInt(2, co_def_rep_hist);
			pstmt.setString(3, detalleDef.getNombreColegio());
			pstmt.setString(4, detalleDef.getTrabajador());
			pstmt.setString(5, detalleDef.getNombreNino());
			pstmt.setBigDecimal(6, detalleDef.getMontoMensualPagTrab());
			pstmt.setString(7, detalleDef.getConcepto());
			pstmt.setBigDecimal(8, detalleDef.getMontoTotal());
			java.util.Date date = new java.util.Date();
			pstmt.setTimestamp(9, new Timestamp(date.getTime()));
			pstmt.setString(10, detalleDef.getObservacion());
			resultado += pstmt.executeUpdate();
		}

		catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return null;
	}

	public List<DetalleDefHistorico> select(String coRepStatus) throws Exception {

		ManejadorDB manejadorDB = new ManejadorDB();
		;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder("");

		List<DetalleDefHistorico> lista = new ArrayList<DetalleDefHistorico>();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

		try {

			if (coRepStatus != null && coRepStatus.length() > 0) {
				sql.append(
						" SELECT CO_REP_STATUS,CO_DEF_REP_HIST,ED.NB_COLEGIO , ED.NB_TRABAJADOR, ED.NB_NINO,ED.MO_MENS_PAG_TRAB,ED.NB_CONCEPTO_PAGO,ED.MO_TOTAL_FACT,ED.FE_FACTURA,ED.TX_OBSERVACION FROM RHEI.DEF_REP_HISTORICO ED WHERE ED.CO_REP_STATUS=? ");
				pstmt = con.prepareStatement(sql.toString());
				pstmt.setInt(1, Integer.parseInt(coRepStatus));
				rs = pstmt.executeQuery();
				DetalleDefHistorico detalleDefinititvo = null;
				while (rs.next()) {
					detalleDefinititvo = new DetalleDefHistorico();
					detalleDefinititvo.setCoRepStatus(rs.getInt("CO_REP_STATUS") + "");
					detalleDefinititvo.setCoDefHistorico(rs.getInt("CO_DEF_REP_HIST"));
					detalleDefinititvo.setConcepto(rs.getString("NB_CONCEPTO_PAGO"));
					detalleDefinititvo.setMontoMensualPagTrab(rs.getBigDecimal("MO_MENS_PAG_TRAB"));
					/***
					 * Calculamos el total en el memorando con su formato
					 *******/
					DecimalFormat df2 = new DecimalFormat(Constantes.FORMATO_DOUBLE,
							new DecimalFormatSymbols(new Locale("es", "VE")));
					BigDecimal value = detalleDefinititvo.getMontoMensualPagTrab();
					/**
					 * Se usa en el metodo ejcutar de esta clase para llevarnos
					 * el monto total en bigDecimal
					 */
					detalleDefinititvo.setMontoMensualPagTrabStr(new String(df2.format(value.floatValue())));
					/***
					 * Fin Calculamos el total en el memorando con su formato
					 *******/

					detalleDefinititvo.setMontoTotal(rs.getBigDecimal("MO_TOTAL_FACT"));

					/***
					 * Calculamos el total en el memorando con su formato
					 *******/
					df2 = new DecimalFormat(Constantes.FORMATO_DOUBLE,
							new DecimalFormatSymbols(new Locale("es", "VE")));
					value = detalleDefinititvo.getMontoTotal();
					/**
					 * Se usa en el metodo ejcutar de esta clase para llevarnos
					 * el monto total en bigDecimal
					 */
					detalleDefinititvo.setMontoTotalStr(new String(df2.format(value.floatValue())));
					/***
					 * Fin Calculamos el total en el memorando con su formato
					 *******/

					detalleDefinititvo.setNombreColegio(rs.getString("NB_COLEGIO"));
					detalleDefinititvo.setNombreNino(rs.getString("NB_NINO"));
					detalleDefinititvo.setTrabajador(rs.getString("NB_TRABAJADOR"));
					detalleDefinititvo.setFecha(rs.getDate("FE_FACTURA"));
					detalleDefinititvo.setFechaStr(format.format(detalleDefinititvo.getFecha()));
					detalleDefinititvo.setObservacion(rs.getString("TX_OBSERVACION"));
					lista.add(detalleDefinititvo);
				}
			}

		}

		catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}

		return lista;
	}

}
