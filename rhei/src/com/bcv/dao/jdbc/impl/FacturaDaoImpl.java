/**
 * 
 */
package com.bcv.dao.jdbc.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

import ve.org.bcv.rhei.util.Constantes;

import com.bcv.dao.jdbc.FacturaDao;
import com.bcv.dao.jdbc.ManejadorDB;
import com.bcv.dao.jdbc.ReporteEstatusDao;
import com.bcv.dao.jdbc.SimpleJDBCDaoImpl;
import com.bcv.model.Factura;
import com.bcv.model.ReporteEstatus;
import com.enums.Mes;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco 03/07/2015 14:24:16 2015 mail :
 *         oraclefedora@gmail.com
 */
public class FacturaDaoImpl extends SimpleJDBCDaoImpl<Factura>
		implements
			FacturaDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ACTUALIZAMOS SOLO EL MONTO DE LA FACTURA
	 * 
	 * @throws SQLException
	 */
	public int updateFactura(Double montoFactura, String nroFactura,
			String nroControl, int nroIdFactura) throws SQLException {
		ManejadorDB manejadorDB = new ManejadorDB();;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		StringBuilder sql = new StringBuilder("");
		int resultado = -1;
		try {
			sql.append(" UPDATE RHEI.FACTURA F ");
			sql.append(" SET F.MO_FACTURA = ?, F.NU_FACTURA = ?, F.NU_CONTROL = ? WHERE F.NU_ID_FACTURA = ?  ");
			pstmt = con.prepareStatement(sql.toString());

			pstmt.setDouble(1, montoFactura);
			pstmt.setString(2, nroFactura);
			pstmt.setString(3, nroControl);

			pstmt.setInt(4, nroIdFactura);
			resultado += pstmt.executeUpdate();

		}

		catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(null, pstmt, con);
		}
		return resultado;
	}

	public int deleteFactura(int nuIdFactura) throws SQLException {
		ManejadorDB manejadorDB = new ManejadorDB();;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		StringBuilder sql = new StringBuilder("");
		int resultado = -1;
		try {
			sql.append("delete from RHEI.FACTURA f where F.NU_ID_FACTURA=?");

			pstmt = con.prepareStatement(sql.toString());
			int posici = 1;
			pstmt.setInt(posici++, nuIdFactura);
			resultado = pstmt.executeUpdate();
		}

		catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			liberarConexion(null, pstmt, con);
		}
		return resultado;

	}
	
	/* (non-Javadoc)
	 * @see com.bcv.dao.jdbc.FacturaDao#consultarFacturasByNroSolicitudId(int, int)
	 */
	public List<Factura> consultarFacturasByNroSolicitudId(int nroIdSolicitud,int coRepStatus)
			throws SQLException {
		Factura factura = null;
		List<Factura> facturas = new ArrayList<Factura>();
		ManejadorDB manejadorDB = new ManejadorDB();;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder("");
		try {
			sql.append("SELECT F.MO_APORTE_BCV,F.MO_PERIODO,F.MO_MATRICULA, F.NU_ID_FACTURA ,F.NU_FACTURA,F.FE_FACTURA,F.NU_RIF_PROVEEDOR,F.NU_CONTROL,F.MO_FACTURA,F.CO_MONEDA  FROM RHEI.FACTURA F  INNER JOIN RHEI.RELACION_PAGOS RP");
			sql.append(" ON F.NU_ID_FACTURA=RP.NU_ID_FACTURA where RP.NU_SOLICITUD=?");
			if (coRepStatus>0){
				sql.append("   and RP.CO_REP_STATUS=").append(coRepStatus);
			}
			sql.append("    order by F.NU_ID_FACTURA asc ");
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, nroIdSolicitud);
			rs = pstmt.executeQuery();
			if (rs != null) {
				Map<String, String> unico = new HashMap<String, String>();
				while (rs.next()) {
					if (!unico.containsKey(rs.getString("NU_ID_FACTURA"))) {
						unico.put(rs.getString("NU_ID_FACTURA"),
								rs.getString("NU_ID_FACTURA"));
						factura = new Factura();
						factura.setNroIdFactura(Integer.parseInt(rs
								.getString("NU_ID_FACTURA")));
						factura.setNroFactura(rs.getString("NU_FACTURA"));
						factura.setFechaFactura(rs.getString("FE_FACTURA"));
						factura.setNroRifProv(rs.getString("NU_RIF_PROVEEDOR"));
						factura.setNroControl(rs.getString("NU_CONTROL"));
						factura.setMontoFactura(Double.valueOf(Double
								.parseDouble(rs.getString("MO_FACTURA"))));
						
						factura.setMontoBCV(rs.getDouble("MO_APORTE_BCV"));
						factura.setMontoPeriodoBCV(rs.getDouble("MO_PERIODO"));
						factura.setMontoMatriculaBCV(rs.getDouble("MO_MATRICULA"));
						facturas.add(factura);

					}
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return facturas;
	}

	public List<Factura> facturasByNumSolicitudMatriculaPeriodo(
			long numSolicitud, String filtrarByMesOrComplementoOrAmbos,
			boolean isActualizar,String periodoEscolar) throws SQLException {
	 
		ResourceBundle rb = ResourceBundle
				.getBundle("ve.org.bcv.rhei.util.bundle");
		ReporteEstatusDao reporteEstatusDao = new ReporteEstatusDaoImpl();
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder("");
		StringBuilder sqlDetalle = new StringBuilder("");

		sql.append(" SELECT  unique(FCT.NU_ID_FACTURA)   ");
		sql.append("  FROM  RHEI.RELACION_PAGOS RP   INNER JOIN RHEI.SOLICITUD_BEI SBEI ON RP.NU_SOLICITUD=SBEI.NU_SOLICITUD ");
		sql.append("      INNER JOIN RHEI.FACTURA FCT ON FCT.NU_ID_FACTURA=RP.NU_ID_FACTURA ");
		 sql.append("  inner join RHEI.PERIODO_ESCOLAR pe on PE.CO_PERIODO=RP.CO_PERIODO ");
		sql.append(" WHERE        SBEI.NU_SOLICITUD=?   ");
		sql.append("  and PE.TX_DESCRIP_PERIODO='").append(periodoEscolar).append("' ");
		if ("0".equalsIgnoreCase(filtrarByMesOrComplementoOrAmbos)) {
			sql.append("  	  and RP.IN_COMPLEMENTO='N' ");
		}
		if ("1".equalsIgnoreCase(filtrarByMesOrComplementoOrAmbos)) {
			sql.append("  	  and RP.IN_COMPLEMENTO='S' ");
		}
		if ("2".equalsIgnoreCase(filtrarByMesOrComplementoOrAmbos)) {
			sql.append(" ");
		}
		if (isActualizar) {
			sql.append(" AND RP.CO_REP_STATUS is null ");
		}
		sql.append("  ORDER BY  FCT.NU_ID_FACTURA desc      ");
		List<Factura> facturas = new ArrayList<Factura>();
		try {
			ManejadorDB manejadorDB = new ManejadorDB();;
			con = manejadorDB.coneccionPool();
			stmt = con.prepareStatement(sql.toString());
			int posicion = 1;
			stmt.setLong(posicion++, numSolicitud);

			rs = stmt.executeQuery();

			while (rs.next()) {
				long nuIdFactura = rs.getLong("NU_ID_FACTURA");
				sqlDetalle = new StringBuilder("");
				
				sqlDetalle
						.append(" SELECT  NB_PAGADO_POR ,  rp.mo_total_pago as MO_APORTE_BCV, rfdp.TX_FORMA_PAGO, FCT.NU_ID_FACTURA, fct.nu_factura,FCT.FE_FACTURA,FCT.NU_CONTROL,RP.NU_REF_PAGO,RP.IN_RECEPTOR_PAGO,RP.CO_REP_STATUS , ");
				sqlDetalle
						.append("  RP.IN_COMPLEMENTO,FCT.TX_CONCEPTO_PAGO,FCT.MO_PAGO_ADICIONAL,RP.TX_OBSERVACIONES,RP.MO_TOTAL_PAGO  FROM  RHEI.RELACION_PAGOS RP   INNER JOIN RHEI.SOLICITUD_BEI SBEI ON RP.NU_SOLICITUD=SBEI.NU_SOLICITUD ");
				sqlDetalle
						.append("      INNER JOIN RHEI.FACTURA FCT ON FCT.NU_ID_FACTURA=RP.NU_ID_FACTURA ");
				sqlDetalle.append(" inner join RHEI.FORMA_DE_PAGO rfdp on  RP.CO_FORMA_PAGO=rfdp.CO_FORMA_PAGO ");
				sqlDetalle.append(" WHERE       FCT.NU_ID_FACTURA=").append(
						nuIdFactura);

				if (isActualizar) {
					sql.append(" AND RP.CO_REP_STATUS is null ");
				}

				sqlDetalle
						.append("  ORDER BY  FCT.NU_ID_FACTURA desc ,FCT.FE_FACTURA desc     ");

				stmt = con.prepareStatement(sqlDetalle.toString());
				ResultSet resultSet = stmt.executeQuery();
				Factura factura = null;
				StringBuilder mesesStr = new StringBuilder("");
				String noComplementObservaciones = null;
				String complementoObservaciones = null;
				String nuFactura = "";
			 
				double montoFactura = 0d;
				int inReceptorPago = 0;
				Date fechaFactura = null;
				int statusReporteDefinitivo=0;
			//	boolean checkOnlyUnaVez=true;
				String nombReportPagDef="";
				String txFormaPago="";
				int codigoReporteDefinitivo=0;
				String moAporteBcv="";
				String nbPagadoPor = "";
				while (resultSet.next()) {
					int mes = resultSet.getInt("NU_REF_PAGO");
					moAporteBcv=resultSet.getString("MO_APORTE_BCV");	
					txFormaPago=resultSet.getString("TX_FORMA_PAGO");
					nbPagadoPor = resultSet.getString("NB_PAGADO_POR")!=null?resultSet.getString("NB_PAGADO_POR"):"";
					String corcheteAbre = "";
					String corcheteCierre = "";
					if (mes < 0) {
						/** Si es negativo es porque ya esta pago ************/
						corcheteAbre = "[ - ";
						corcheteCierre = " ] ";
					}
					/**isPagoWhenCoRepStatusNotNull, Chequea siempre que sea verdadero, si hay por lo menos uno que sea falso, bno chequea mas y 
					 * el pago queda falso "NO HA PAGADO EN PAGO Y TRIBUTOS"
					 * **/
					//if (checkOnlyUnaVez){
					codigoReporteDefinitivo=resultSet.getInt("CO_REP_STATUS");
					ReporteEstatus reporteEstatus=reporteEstatusDao.reporteStatus(codigoReporteDefinitivo);
					if (reporteEstatus!=null){
						nombReportPagDef= reporteEstatus.getNombre();
						codigoReporteDefinitivo=(int)reporteEstatus.getCoReporstatus();
						if (!StringUtils.isEmpty(reporteEstatus.getStatus())){
							statusReporteDefinitivo=Integer.parseInt(reporteEstatus.getStatus());	
						}
						
					}
					
					/**El total pagado por cada mes*/
					if (mes > 0) {
						/** Acumulamos el monto **/
						montoFactura += resultSet
								.getDouble("MO_TOTAL_PAGO");
					} else {
						montoFactura += (resultSet
								.getDouble("MO_TOTAL_PAGO") * (-1));
					}
					
					noComplementObservaciones = resultSet
							.getString("TX_OBSERVACIONES");
					complementoObservaciones = resultSet
							.getString("TX_CONCEPTO_PAGO");

					inReceptorPago = resultSet.getInt("IN_RECEPTOR_PAGO");

					fechaFactura = resultSet.getDate("FE_FACTURA");

					String factComplemento = resultSet
							.getString("IN_COMPLEMENTO");
					nuFactura = resultSet.getString("nu_factura");
					if (!StringUtils.isEmpty(mesesStr)) {
						mesesStr.append(",");
					}

					 String abrevComplemento=rb.getString("complemento");
					   if (rb.getString("complemento").length()>3){
						   abrevComplemento=rb.getString("complemento").substring(0,4);
					   }
					   
					  
			    	    /*** Si es complemento ****/
					if (factComplemento
							.equalsIgnoreCase(Constantes.IN_COMPLEMENTO)) {
						double montoFacturaCompl=0;
						/**El total pagado por cada mes*/
						if (mes > 0) {
							/** Acumulamos el monto **/
							montoFacturaCompl = resultSet
									.getDouble("MO_TOTAL_PAGO");
						} else {
							montoFacturaCompl = (resultSet
									.getDouble("MO_TOTAL_PAGO") * (-1));
						}
						 DecimalFormat df2 = new DecimalFormat(     Constantes.FORMATO_DOUBLE, 	      new DecimalFormatSymbols(new Locale("es", "VE")));
				    	    BigDecimal value = new BigDecimal(montoFacturaCompl);
				    	    String   montoComplToReport=df2.format(value.floatValue());
					//	txComplementoReembolso =    abrevComplemento+" "+corcheteAbre+ super.obtenerNameMes(Math.abs(fact.getNuRefPago())).substring(0,3)+"("+montoComplToReport+")" +corcheteCierre+"";
						
						mesesStr.append(abrevComplemento).append(" ").append(corcheteAbre)
								.append(obtenerNameMes(Math.abs(mes)).substring(0,3)).append("(").append(montoComplToReport).append(")")
								.append(corcheteCierre);

					}/*** Si no es complemento ****/
					else if (factComplemento
							.equalsIgnoreCase(Constantes.NO_IN_COMPLEMENTO)) {
						mesesStr.append(corcheteAbre)
								.append(obtenerNameMes(Math.abs(mes)))
								.append(corcheteCierre);
					}
					
					

				}

				if (resultSet != null) {
					resultSet.close();
				}
				StringBuilder observaciones = new StringBuilder();
				if (!StringUtils.isEmpty(noComplementObservaciones)) {
					observaciones.append(noComplementObservaciones);
				}
				if (!StringUtils.isEmpty(observaciones)) {
					observaciones.append(",");
				}
				if (!StringUtils.isEmpty(complementoObservaciones)) {
					observaciones.append(complementoObservaciones);
				}
				if (!StringUtils.isEmpty(mesesStr)
						&& !StringUtils.isEmpty(observaciones)) {
					observaciones.append(",");
				}
				if (!StringUtils.isEmpty(mesesStr)) {
					observaciones.append(mesesStr);
				}

				factura = new Factura();
				
				factura.setNbPagadoPor(nbPagadoPor);
				
				factura.setReembolso(Constantes.FILTRARBYMESORCOMPLEMENTOORAMBOS_REEMBOLSO.equalsIgnoreCase(filtrarByMesOrComplementoOrAmbos));
				
				/*** Calculamos el total en el memorando con su formato *******/
				DecimalFormat df2 = new DecimalFormat(Constantes.FORMATO_DOUBLE,
						new DecimalFormatSymbols(new Locale("es", "VE")));
				BigDecimal value = new BigDecimal(moAporteBcv);
				factura.setMoAporteBcv(
						new String(df2.format(value.floatValue()))); 
				/*** Fin Calculamos el total en el memorando con su formato *******/
				factura.setCoRepStatus(codigoReporteDefinitivo);
				factura.setTxFormaPago(txFormaPago);
				factura.setStatusReporteDefinitivo(statusReporteDefinitivo);
				factura.setNombReportPagDef(nombReportPagDef);
				factura.setInReceptorPago(inReceptorPago);
				if (inReceptorPago - 1 == 0) {
					factura.setStringReceptorPago(rb.getString("trabajador"));
				} else if (inReceptorPago == 0) {
					factura.setStringReceptorPago(rb.getString("cei"));
				}

				if (fechaFactura != null) {
					SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
					factura.setFechaFactura(format.format(fechaFactura)
							.toString());
				}

				factura.setNroIdFactura(Integer.parseInt(nuIdFactura + ""));
				factura.setNroFactura(nuFactura);
				factura.setTxObservaciones(observaciones.toString());
				factura.setMontoFactura(montoFactura);
				/*** Calculamos el total en el memorando con su formato *******/
				 df2 = new DecimalFormat(
						Constantes.FORMATO_DOUBLE, new DecimalFormatSymbols(
								new Locale("es", "VE")));
				 value = new BigDecimal(factura.getMontoFactura());
				factura.setStringMontoFactura(new String(df2.format(value
						.floatValue())));
				/*** Fin Calculamos el total en el memorando con su formato *******/

				facturas.add(factura);
			}
		} finally {
			liberarConexion(rs, stmt, con);
		}

		return facturas;

	}
	
	
	 
	
	

	public List<Factura> facturasByNumSolicitudMatriculaPeriodoBackup(
			long numSolicitud, String filtrarByMesOrComplementoOrAmbos)
			throws SQLException {
		ResourceBundle rb = ResourceBundle
				.getBundle("ve.org.bcv.rhei.util.bundle");
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder("");
		StringBuilder sqlDetalle = new StringBuilder("");

		sql.append(" SELECT  unique(FCT.NU_ID_FACTURA)   ");
		sql.append("  FROM  RHEI.RELACION_PAGOS RP   INNER JOIN RHEI.SOLICITUD_BEI SBEI ON RP.NU_SOLICITUD=SBEI.NU_SOLICITUD ");
		sql.append("      INNER JOIN RHEI.FACTURA FCT ON FCT.NU_ID_FACTURA=RP.NU_ID_FACTURA ");
		sql.append(" WHERE        SBEI.NU_SOLICITUD=? and RP.IN_COMPLEMENTO=?   ");
		if ("0".equalsIgnoreCase(filtrarByMesOrComplementoOrAmbos)) {
			sql.append("  	  and RP.IN_COMPLEMENTO='N' ");
		}
		if ("1".equalsIgnoreCase(filtrarByMesOrComplementoOrAmbos)) {
			sql.append("  	  and RP.IN_COMPLEMENTO='S' ");
		}
		if ("2".equalsIgnoreCase(filtrarByMesOrComplementoOrAmbos)) {
			sql.append(" ");
		}
		sql.append("  ORDER BY  FCT.NU_ID_FACTURA asc      ");
		List<Factura> facturas = new ArrayList<Factura>();
		try {
			ManejadorDB manejadorDB = new ManejadorDB();;
			con = manejadorDB.coneccionPool();
			stmt = con.prepareStatement(sql.toString());
			int posicion = 1;
			stmt.setLong(posicion++, numSolicitud);

			rs = stmt.executeQuery();

			while (rs.next()) {
				long nuIdFactura = rs.getLong("NU_ID_FACTURA");
				sqlDetalle = new StringBuilder("");
				sqlDetalle
						.append(" SELECT fct.nu_factura,FCT.FE_FACTURA,FCT.NU_CONTROL,RP.NU_REF_PAGO,RP.IN_RECEPTOR_PAGO, ");
				sqlDetalle
						.append("  RP.IN_COMPLEMENTO,FCT.TX_CONCEPTO_PAGO,FCT.MO_PAGO_ADICIONAL,RP.TX_OBSERVACIONES,RP.MO_TOTAL_PAGO  FROM  RHEI.RELACION_PAGOS RP   INNER JOIN RHEI.SOLICITUD_BEI SBEI ON RP.NU_SOLICITUD=SBEI.NU_SOLICITUD ");
				sqlDetalle
						.append("      INNER JOIN RHEI.FACTURA FCT ON FCT.NU_ID_FACTURA=RP.NU_ID_FACTURA ");
				sqlDetalle.append(" WHERE       FCT.NU_ID_FACTURA=").append(
						nuIdFactura);

				sqlDetalle.append("  ORDER BY  FCT.nu_factura asc      ");

				stmt = con.prepareStatement(sqlDetalle.toString());
				ResultSet resultSet = stmt.executeQuery();
				Factura factura = null;
				StringBuilder mesesStr = new StringBuilder("");
				String noComplementObservaciones = null;
				String complementoObservaciones = null;
				String nuFactura = "";
				double complementoPago = 0d;
				double noComplementoPago = 0d;
				int inReceptorPago = 0;

				while (resultSet.next()) {
					int mes = resultSet.getInt("NU_REF_PAGO");
					noComplementObservaciones = resultSet
							.getString("TX_OBSERVACIONES");
					complementoObservaciones = resultSet
							.getString("TX_CONCEPTO_PAGO");

					inReceptorPago = resultSet.getInt("IN_RECEPTOR_PAGO");

					String factComplemento = resultSet
							.getString("IN_COMPLEMENTO");
					nuFactura = resultSet.getString("nu_factura");
					if (!StringUtils.isEmpty(mesesStr)) {
						mesesStr.append(",");
					}

					if (factComplemento
							.equalsIgnoreCase(Constantes.IN_COMPLEMENTO)) {
						mesesStr.append(" COMPLEMENTO ").append(
								obtenerNameMes(Math.abs(mes)));
						/** Siempre ser< el mismo monto **/
						complementoPago = resultSet
								.getDouble("MO_PAGO_ADICIONAL");

					}/*** Si no es complemento ****/
					else if (factComplemento
							.equalsIgnoreCase(Constantes.NO_IN_COMPLEMENTO)) {
						mesesStr.append(obtenerNameMes(mes));
						/** Acumulamos el monto **/
						noComplementoPago += resultSet
								.getDouble("MO_TOTAL_PAGO");
					}

				}

				if (resultSet != null) {
					resultSet.close();
				}
				StringBuilder observaciones = new StringBuilder();
				if (!StringUtils.isEmpty(noComplementObservaciones)) {
					observaciones.append(noComplementObservaciones);
				}
				if (!StringUtils.isEmpty(observaciones)) {
					observaciones.append(",");
				}
				if (!StringUtils.isEmpty(complementoObservaciones)) {
					observaciones.append(complementoObservaciones);
				}
				if (!StringUtils.isEmpty(mesesStr)) {
					observaciones.append(",");
				}
				if (!StringUtils.isEmpty(mesesStr)) {
					observaciones.append(mesesStr);
				}

				factura = new Factura();
				factura.setInReceptorPago(inReceptorPago);
				if (inReceptorPago - 1 == 0) {
					factura.setStringReceptorPago(rb.getString("trabajador"));
				} else if (inReceptorPago == 0) {
					factura.setStringReceptorPago(rb.getString("cei"));
				}
				factura.setNroIdFactura(Integer.parseInt(nuIdFactura + ""));
				factura.setNroFactura(nuFactura);
				factura.setTxObservaciones(observaciones.toString());
				factura.setMontoFactura(complementoPago + noComplementoPago);
				/*** Calculamos el total en el memorando con su formato *******/
				DecimalFormat df2 = new DecimalFormat(
						Constantes.FORMATO_DOUBLE, new DecimalFormatSymbols(
								new Locale("es", "VE")));
				BigDecimal value = new BigDecimal(factura.getMontoFactura());
				factura.setStringMontoFactura(new String(df2.format(value
						.floatValue())));
				/*** Fin Calculamos el total en el memorando con su formato *******/

				facturas.add(factura);
			}
		} finally {
			liberarConexion(rs, stmt, con);
		}

		return facturas;

	}

	public String obtenerNameMes(int indMes) {
		String name = "";
		/** Buscamos el nombre del mes a traves del enum */
		for (Mes mes : Mes.values()) {
			if (mes.getValue() == indMes) {
				name = mes.name();

				if (name.length() > 0 && name.charAt(name.length() - 1) == 'P') {
					name = name.substring(0, name.length() - 1);
				}

				break;
			}
		}
		return name;
	}

	/**
	 * @param facturasId
	 * @param receptorPago
	 * @param coFormaPago
	 * @param descripPeriodo
	 * @param nuSolicitud
	 * @param companiaAnalista
	 * @return
	 */
	public List<Factura> factByIdFacRecPagoFormPagoPerNumSolComp(
			long facturasId, int receptorPago, int coFormaPago,
			String descripPeriodo, long nuSolicitud, String companiaAnalista,
			String meses, String filtrarByMesOrComplementoOrAmbos)
			throws SQLException {
		/**La compañia siempre va a ser 01***/
		companiaAnalista="01"; 
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder mesComplementoSalir = new StringBuilder("");
		StringBuilder sql = new StringBuilder("");
		sql.append("    SELECT  RP.IN_COMPLEMENTO,RP.MO_TOTAL_PAGO, RP.NU_REF_PAGO,PARAM.TX_OBSERVACIONES,FCT.MO_APORTE_BCV,FCT.MO_PERIODO,FCT.MO_MATRICULA,FCT.MO_FACTURA,FCT.TX_CONCEPTO_PAGO,FCT.MO_PAGO_ADICIONAL ");
		sql.append(" FROM  RHEI.RELACION_PAGOS RP ");
		sql.append(" INNER JOIN RHEI.SOLICITUD_BEI SBEI ON RP.NU_SOLICITUD=SBEI.NU_SOLICITUD ");
		sql.append(" INNER JOIN RHEI.FACTURA FCT ON FCT.NU_ID_FACTURA=RP.NU_ID_FACTURA ");
		sql.append(" INNER JOIN RHEI.PARAMETRO PARAM ON PARAM.TX_VALOR_PARAMETRO=ABS(RP.NU_REF_PAGO) ");
		sql.append(" WHERE PARAM.CO_PARAMETRO LIKE 'MES%' ");
		sql.append(" AND RP.CO_PERIODO  IN (SELECT PE.CO_PERIODO FROM RHEI.PERIODO_ESCOLAR PE  ");
		sql.append(" WHERE PE.TX_DESCRIP_PERIODO=?)  ");
		sql.append("  AND PARAM.CO_TIPO_BENEFICIO='")
				.append(Constantes.TIPOBENEFICIO).append("'");
		sql.append("   AND SBEI.NU_SOLICITUD=? ");
		sql.append("   AND PARAM.CO_COMPANIA=? ");
		sql.append(" AND FCT.NU_ID_FACTURA in(?) ");
		if (receptorPago > -1) {
		 
				sql.append("  	   AND RP.IN_RECEPTOR_PAGO=? ");	
			 
			
		}
		if (coFormaPago > -1) {
			if (Constantes.FORMA_PAGO_AMBOS.equalsIgnoreCase(coFormaPago+"")){
				sql.append("  	 AND   (RP.CO_FORMA_PAGO = 1 OR RP.CO_FORMA_PAGO = 2) ");
			}else{
				sql.append("  	 AND   RP.CO_FORMA_PAGO=? ");
			}
		
		}

		if ("0".equalsIgnoreCase(filtrarByMesOrComplementoOrAmbos)) {
			sql.append("  	  and RP.IN_COMPLEMENTO='N' ");
		}
		if ("1".equalsIgnoreCase(filtrarByMesOrComplementoOrAmbos)) {
			sql.append("  	  and RP.IN_COMPLEMENTO='S' ");
		}
		if ("2".equalsIgnoreCase(filtrarByMesOrComplementoOrAmbos)) {
			sql.append(" and (RP.IN_COMPLEMENTO='N' or RP.IN_COMPLEMENTO='S')");
		}

		if (!StringUtils.isEmpty(meses)) {

			StringTokenizer mesStk = new StringTokenizer(meses, ",");
			sql.append("  AND  ABS ( RP.NU_REF_PAGO ) IN  (");
			sql.append(" ");
			while (mesStk.hasMoreTokens()) {
				int mesInt = new Integer(mesStk.nextToken());
				sql.append(mesInt);
				if (mesStk.hasMoreTokens()) {
					sql.append(",");
				}
			}

			sql.append(" ) ");
		}
	 
			sql.append("    ORDER BY  FCT.NU_ID_FACTURA ASC      ");			
		 
		List<Factura> facturas = new ArrayList<Factura>();
		try {
			ManejadorDB manejadorDB = new ManejadorDB();;
			con = manejadorDB.coneccionPool();
			stmt = con.prepareStatement(sql.toString());
			int posicion = 1;
			stmt.setString(posicion++, descripPeriodo);
			stmt.setLong(posicion++, nuSolicitud);
			stmt.setString(posicion++, companiaAnalista);
			stmt.setLong(posicion++, facturasId);
			if (receptorPago > -1) {
				stmt.setInt(posicion++, receptorPago);
			}
			if (coFormaPago > -1) {
				if (!Constantes.FORMA_PAGO_AMBOS.equalsIgnoreCase(coFormaPago
						+ "")) {
					stmt.setInt(posicion++, coFormaPago);
				}
			}

			Factura factura = null;
			rs = stmt.executeQuery();
			while (rs.next()) {
				factura = new Factura();
				factura.setNuRefPago(rs.getInt("NU_REF_PAGO"));
				factura.setTxObservaciones(rs.getString("TX_OBSERVACIONES"));
				factura.setTxConceptoPago(rs.getString("TX_CONCEPTO_PAGO"));
				factura.setMontoFactura(rs.getDouble("MO_FACTURA"));
				factura.setMoTotalPago(rs.getDouble("MO_TOTAL_PAGO"));
				/**Historicos de como se pago la factura*/
				factura.setMontoBCV(rs.getDouble("MO_APORTE_BCV"));
				factura.setMontoPeriodoBCV(rs.getDouble("MO_PERIODO"));
				factura.setMontoMatriculaBCV(rs.getDouble("MO_MATRICULA"));

				factura.setInComplemento(rs.getString("IN_COMPLEMENTO"));
				facturas.add(factura);

			}
		} finally {
			liberarConexion(rs, stmt, con);
		}

		return facturas;
	}

	public Factura consultarFacturaById(int nroIdFactura) throws SQLException {
		Factura factura = null;
		ManejadorDB manejadorDB = new ManejadorDB();;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder("");
		try {
			sql.append("SELECT F.NU_ID_FACTURA,F.NU_FACTURA,F.FE_FACTURA,F.NU_RIF_PROVEEDOR,");
			sql.append(" F.NU_CONTROL,F.MO_FACTURA,F.CO_MONEDA FROM RHEI.FACTURA F WHERE F.NU_ID_FACTURA=? ORDER BY NU_FACTURA ");
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, nroIdFactura);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				factura = new Factura();
				factura.setNroIdFactura(Integer.parseInt(rs
						.getString("NU_ID_FACTURA")));
				factura.setNroFactura(rs.getString("NU_FACTURA"));
				factura.setFechaFactura(rs.getString("FE_FACTURA"));
				factura.setNroRifProv(rs.getString("NU_RIF_PROVEEDOR"));
				factura.setNroControl(rs.getString("NU_CONTROL"));
				factura.setMontoFactura(Double.valueOf(Double.parseDouble(rs
						.getString("MO_FACTURA"))));
			}
		}

		catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return factura;
	}

	/**
	 * Consultar factgura por nroFactura, nuSolicitud y nuRefPago
	 * 
	 * @param nroFactura
	 * @param nuSolicitud
	 * @param nuRefPago
	 * @return
	 * @throws SQLException
	 */
	public Factura consultarFacturaByNuSolNuRefPag(String nroFactura,
			int nuSolicitud, int nuRefPago) throws SQLException {
		Factura factura = null;
		ManejadorDB manejadorDB = new ManejadorDB();;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder("");
		try {

			sql.append(" SELECT  F.TX_CONCEPTO_PAGO,F.MO_PAGO_ADICIONAL , F.NU_ID_FACTURA,F.NU_FACTURA,F.FE_FACTURA,F.NU_RIF_PROVEEDOR, F.NU_CONTROL,F.MO_FACTURA,F.CO_MONEDA ");
			sql.append("  FROM RHEI.FACTURA F ");
			sql.append("  INNER JOIN  RHEI.RELACION_PAGOS RP ");
			sql.append("  ON F.NU_ID_FACTURA=RP.NU_ID_FACTURA ");
			sql.append("  WHERE  LOWER(F.NU_FACTURA)=LOWER(?) ");
			sql.append("  AND RP.NU_SOLICITUD=? ");
			sql.append("  AND RP.NU_REF_PAGO=? ");
			sql.append("  ORDER BY nu_id_factura  desc ");

			pstmt = con.prepareStatement(sql.toString());
			int posicion = 1;
			pstmt.setString(posicion++, nroFactura);
			pstmt.setInt(posicion++, nuSolicitud);
			pstmt.setInt(posicion++, nuRefPago);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				factura = new Factura();
				factura.setNroIdFactura(Integer.parseInt(rs
						.getString("NU_ID_FACTURA")));
				factura.setNroFactura(rs.getString("NU_FACTURA"));
				factura.setFechaFactura(rs.getString("FE_FACTURA"));
				factura.setNroRifProv(rs.getString("NU_RIF_PROVEEDOR"));
				factura.setNroControl(rs.getString("NU_CONTROL"));
				factura.setMontoFactura(Double.valueOf(Double.parseDouble(rs
						.getString("MO_FACTURA"))));
				factura.setTxtcomplemento(rs.getString("TX_CONCEPTO_PAGO"));
				factura.setMontocomplemento(Double.valueOf(Double
						.parseDouble(rs.getString("MO_PAGO_ADICIONAL"))));
			}
		}

		catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return factura;
	}

	public Factura consultarFactura(String nroFactura, int nuSolicitud,String periodo)
			throws SQLException {
		Factura factura = null;
		ManejadorDB manejadorDB = new ManejadorDB();;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder("");
		try {
			sql.append(" SELECT  F.TX_CONCEPTO_PAGO,F.MO_PAGO_ADICIONAL , F.NU_ID_FACTURA,F.NU_FACTURA,F.FE_FACTURA,F.NU_RIF_PROVEEDOR, F.NU_CONTROL,F.MO_FACTURA,F.CO_MONEDA ");
			sql.append("  FROM RHEI.FACTURA F ");
			sql.append("  INNER JOIN  RHEI.RELACION_PAGOS RP ");
			sql.append("  ON F.NU_ID_FACTURA=RP.NU_ID_FACTURA ");
			sql.append("  WHERE  LOWER(F.NU_FACTURA)=LOWER(?) ");
			sql.append("  AND RP.NU_SOLICITUD=? ");
			sql.append(" AND  RP.CO_PERIODO IN (select PE2.CO_PERIODO from RHEI.PERIODO_ESCOLAR PE2 WHERE PE2.TX_DESCRIP_PERIODO=? )  ");
			sql.append("  ORDER BY nu_id_factura  desc ");

			pstmt = con.prepareStatement(sql.toString());

			int posicion = 1;
			pstmt.setString(posicion++, nroFactura);
			pstmt.setInt(posicion++, nuSolicitud);
			pstmt.setString(posicion++, periodo);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				factura = new Factura();
				factura.setNroIdFactura(Integer.parseInt(rs
						.getString("NU_ID_FACTURA")));
				factura.setNroFactura(rs.getString("NU_FACTURA"));
				factura.setFechaFactura(rs.getString("FE_FACTURA"));
				factura.setNroRifProv(rs.getString("NU_RIF_PROVEEDOR"));
				factura.setNroControl(rs.getString("NU_CONTROL"));
				factura.setMontoFactura(Double.valueOf(Double.parseDouble(rs
						.getString("MO_FACTURA"))));
				factura.setTxtcomplemento(rs.getString("TX_CONCEPTO_PAGO"));
				factura.setMontocomplemento(Double.valueOf(Double
						.parseDouble(rs.getString("MO_PAGO_ADICIONAL"))));
			}
		}

		catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return factura;
	}

	/**
	 * Buscaremos el idFactura a traves del numeroFactura
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int buscarIdFacturaByNuFactura(String nroFactura)
			throws SQLException {
		int nroIdFactura = 0;
		ManejadorDB manejadorDB = new ManejadorDB();;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder("");
		try {
			sql.append("SELECT F.NU_ID_FACTURA ");
			sql.append("  FROM RHEI.FACTURA F WHERE F.nu_factura=?  ");

			stmt = con.prepareStatement(sql.toString());
			stmt.setString(1, nroFactura);
			rs = stmt.executeQuery();
			if (rs.next()) {
				nroIdFactura = rs.getInt("NU_ID_FACTURA");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return nroIdFactura;
	}

	public List<Long> facturasByNroSolicitud(long nuSolicitud, String nuFactura)
			throws SQLException {
		ManejadorDB manejadorDB = new ManejadorDB();;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Long> facturas = new ArrayList<Long>();
		boolean isAnd = false;
		try {
			StringBuilder sqlFactura = new StringBuilder("");
			sqlFactura.append(" SELECT unique(FCT.NU_ID_FACTURA) ");
			sqlFactura.append(" FROM  RHEI.RELACION_PAGOS RP ");
			sqlFactura
					.append("  INNER JOIN RHEI.SOLICITUD_BEI SBEI ON RP.NU_SOLICITUD=SBEI.NU_SOLICITUD ");
			sqlFactura
					.append("  INNER JOIN RHEI.FACTURA FCT ON FCT.NU_ID_FACTURA=RP.NU_ID_FACTURA ");
			sqlFactura.append(" WHERE ");

			sqlFactura.append("       SBEI.NU_SOLICITUD=? ");
			isAnd = true;
			if (!StringUtils.isEmpty(nuFactura)) {
				if (isAnd) {
					sqlFactura.append(" and ");
				}
				sqlFactura.append("    lower(FCT.nu_factura)=lower(?) ");
			}
			sqlFactura.append("    ORDER BY  FCT.NU_ID_FACTURA asc        ");
			stmt = con.prepareStatement(sqlFactura.toString());
			int posicion = 1;
			stmt.setLong(posicion++, nuSolicitud);
			if (!StringUtils.isEmpty(nuFactura)) {
				stmt.setString(posicion++, nuFactura);
			}
			rs = stmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					facturas.add(rs.getLong("NU_ID_FACTURA"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return facturas;
	}

	/**
	 * buscamos primero las facturas, luego por cada factura sacamos la relacion
	 * de pagos
	 */

	/**
	 * Busxcar factura por mes y numero de solicitud
	 * 
	 * @param nroSolicitud
	 * @param mes
	 * @return
	 * @throws SQLException
	 */
	public String buscarFacturaByMesNroSolictud(int nroSolicitud,String periodo)
			throws SQLException {
		String result = "";
		ManejadorDB manejadorDB = new ManejadorDB();;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder("");
		try {
			sql.append("select F.NU_FACTURA from RHEI.RELACION_PAGOS rp, RHEI.FACTURA f where F.NU_ID_FACTURA=RP.NU_ID_FACTURA ");
			sql.append("  and rp.nu_solicitud=? ");
			sql.append(" AND RP.CO_PERIODO IN (select PE2.CO_PERIODO from RHEI.PERIODO_ESCOLAR PE2 WHERE PE2.TX_DESCRIP_PERIODO=? )  ");
			stmt = con.prepareStatement(sql.toString());
			stmt.setInt(1, nroSolicitud);
			stmt.setString(2, periodo);
			rs = stmt.executeQuery();
			if (rs.next()) {
				result = rs.getString("NU_FACTURA");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return result;
	}

	/**
	 * Busxcar factura por mes y numero de solicitud
	 * 
	 * @param nroSolicitud
	 * @param mes
	 * @return
	 * @throws SQLException
	 */
	public String buscarFacturaByMesNroSolictud(int nroSolicitud, int mes,String periodo)
			throws SQLException {
		String result = "";
		ManejadorDB manejadorDB = new ManejadorDB();;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder("");
		try {
			sql.append("select F.NU_FACTURA from RHEI.RELACION_PAGOS rp, RHEI.FACTURA f where F.NU_ID_FACTURA=RP.NU_ID_FACTURA ");
			sql.append("  and rp.nu_solicitud=? and abs(rp.nu_ref_pago)=abs(?)");
			sql.append(" AND RP.CO_PERIODO IN (select PE2.CO_PERIODO from RHEI.PERIODO_ESCOLAR PE2 WHERE PE2.TX_DESCRIP_PERIODO=? )  ");
			stmt = con.prepareStatement(sql.toString());
			stmt.setInt(1, nroSolicitud);
			stmt.setInt(2, mes);
			stmt.setString(3, periodo);
			rs = stmt.executeQuery();
			if (rs.next()) {
				result = rs.getString("NU_FACTURA");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return result;
	}

	public List<Integer> mesesPorIdFactura(int idFactura) throws SQLException {
		List<Integer> result = new ArrayList<Integer>();
		ManejadorDB manejadorDB = new ManejadorDB();;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder("");
		try {
			sql.append(" select nu_ref_pago as NU_REF_PAGO from RHEI.RELACION_PAGOS rp, RHEI.FACTURA f where F.NU_ID_FACTURA=RP.NU_ID_FACTURA ");
			sql.append("        and F.NU_ID_FACTURA=?");
			stmt = con.prepareStatement(sql.toString());
			stmt.setInt(1, idFactura);
			rs = stmt.executeQuery();
			while (rs.next()) {
				result.add(rs.getInt("NU_REF_PAGO"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return result;
	}
	
	
	public List<Factura> mesesPorNroFactura(String nroFactura,String complemento)
			throws SQLException {
		List<Factura> result = new ArrayList<Factura>();
		ManejadorDB manejadorDB = new ManejadorDB();;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder("");
		try {
			
			boolean swAnd=false;
			sql.append("      select RP.MO_TOTAL_PAGO,RP.NU_SOLICITUD,RP.IN_COMPLEMENTO,F.NU_ID_FACTURA,RP.NU_REF_PAGO as NU_REF_PAGO,F.TX_CONCEPTO_PAGO,F.MO_PAGO_ADICIONAL from RHEI.RELACION_PAGOS rp ");
			sql.append("    inner join  RHEI.FACTURA f ");
			sql.append("  on RP.NU_ID_FACTURA=F.NU_ID_FACTURA ");
			
			if (!StringUtils.isEmpty(nroFactura)) {
				sql.append("        where lower(f.nu_factura)=?");
				swAnd=true;
			}
			if (swAnd){
				sql.append("  AND ");
			}else{
				sql.append(" where ");
			}
			sql.append(" RP.IN_COMPLEMENTO='").append(complemento).append("'");

			sql.append(" ORDER BY F.NU_ID_FACTURA");

			stmt = con.prepareStatement(sql.toString());
			if (!StringUtils.isEmpty(nroFactura)) {
				stmt.setString(1, nroFactura.toLowerCase());
			}
			rs = stmt.executeQuery();
			Factura factura = null;
			while (rs.next()) {
				factura = new Factura();
				factura.setNuSolicitud(rs.getLong("NU_SOLICITUD"));
				factura.setInComplemento(rs.getString("IN_COMPLEMENTO"));
				factura.setTxConceptoPago(rs.getString("TX_CONCEPTO_PAGO"));
				factura.setNuRefPago(rs.getInt("NU_REF_PAGO"));
				factura.setMoTotalPago(rs.getDouble("MO_TOTAL_PAGO"));
				//factura.setMoPagoAdicional(rs.getDouble("MO_PAGO_ADICIONAL"));
				factura.setNroIdFactura(rs.getInt("NU_ID_FACTURA"));
				result.add(factura);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bcv.dao.jdbc.FacturaDao#mesesPorNroFactura(java.lang.String)
	 */
	public List<Factura> mesesPorNroFactura(String nroFactura)
			throws SQLException {
		List<Factura> result = new ArrayList<Factura>();
		ManejadorDB manejadorDB = new ManejadorDB();;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder("");
		try {
			// sql.append(" select rp.nu_ref_pago as NU_REF_PAGO from RHEI.RELACION_PAGOS rp, RHEI.FACTURA f where F.NU_ID_FACTURA=RP.NU_ID_FACTURA ");

			sql.append("      select RP.NU_SOLICITUD,RP.IN_COMPLEMENTO,F.NU_ID_FACTURA,RP.NU_REF_PAGO as NU_REF_PAGO,F.TX_CONCEPTO_PAGO,F.MO_PAGO_ADICIONAL from RHEI.RELACION_PAGOS rp ");
			sql.append("    inner join  RHEI.FACTURA f ");
			sql.append("  on RP.NU_ID_FACTURA=F.NU_ID_FACTURA ");
			if (!StringUtils.isEmpty(nroFactura)) {
				sql.append("        where lower(f.nu_factura)=?");
			}

			sql.append(" ORDER BY F.NU_ID_FACTURA");

			stmt = con.prepareStatement(sql.toString());
			if (!StringUtils.isEmpty(nroFactura)) {
				stmt.setString(1, nroFactura.toLowerCase());
			}
			rs = stmt.executeQuery();
			Factura factura = null;
			while (rs.next()) {
				factura = new Factura();
				factura.setNuSolicitud(rs.getLong("NU_SOLICITUD"));
				factura.setInComplemento(rs.getString("IN_COMPLEMENTO"));
				factura.setTxConceptoPago(rs.getString("TX_CONCEPTO_PAGO"));
				factura.setNuRefPago(rs.getInt("NU_REF_PAGO"));
			//	factura.setMoPagoAdicional(rs.getDouble("MO_PAGO_ADICIONAL"));
				factura.setNroIdFactura(rs.getInt("NU_ID_FACTURA"));
				result.add(factura);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return result;
	}

}
