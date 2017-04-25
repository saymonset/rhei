/**
 * 
 */
package com.bcv.dao.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ve.org.bcv.rhei.util.Constantes;

import com.bcv.dao.jdbc.ManejadorDB;
import com.bcv.dao.jdbc.ParametroDao;
import com.bcv.dao.jdbc.PeriodoEscolarDao;
import com.bcv.dao.jdbc.ReporteEstatusDao;
import com.bcv.dao.jdbc.SimpleJDBCDaoImpl;
import com.bcv.model.Parametro;
import com.bcv.model.PeriodoEscolar;
import com.bcv.model.ReporteEstatus;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 09/10/2015 08:24:24
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class ReporteEstatusDaoImpl extends SimpleJDBCDaoImpl<ReporteEstatus>
		implements
			ReporteEstatusDao {
	private PeriodoEscolarDao periodoEscolarDao = new PeriodoEscolarDaoImpl();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger
			.getLogger(ReporteEstatusDaoImpl.class.getName());
	private ParametroDao parametroDao = new ParametroDaoImpl(); 
	
	public String nombreReporte(long coRepStatus) {
		ManejadorDB manejador = new ManejadorDB();
		Connection con = manejador.conexion();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String consulta = "";
		String nbRepStatus = "";
		try {
			consulta = "SELECT R.NB_REP_STATUS FROM  RHEI.REPORTE_STATUS R WHERE R.CO_REP_STATUS=?";

			stmt = con.prepareStatement(consulta);
			stmt.setLong(1, coRepStatus);
			rs = stmt.executeQuery();
			if (rs.next()) {
				nbRepStatus = rs.getString("NB_REP_STATUS");
			}
			stmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			liberarConexion(rs, stmt, con);

		}
		return nbRepStatus;
	}
	
	
	public ArrayList<String> buscarParametros(String beneficioEscolar,
			String filtroParametro, String companiaAnalista, int indMenor,
			int indMayor)

	{
		ManejadorDB manejador=new ManejadorDB();;
		Connection con = null;	
		ResultSet rs = null;
		PreparedStatement stmt = null;
	
		ArrayList<String> listadoParametros = new ArrayList<String>();
		String sql = "";
		StringBuilder sqlBuilder = new StringBuilder("");
		String condicion = "";
		if (filtroParametro!=null && !filtroParametro.isEmpty() && !"".equals(filtroParametro)){
			condicion= " AND CO_PARAMETRO LIKE ('" + filtroParametro
					+ "%')  ORDER BY CO_COMPANIA";
		}
	
		if (companiaAnalista.compareTo("01") == 0) {
			sql =

			"SELECT  CO_COMPANIA, TIPO_EMP, CO_TIPO_BENEFICIO, CO_PARAMETRO, TO_CHAR(FE_VIGENCIA, 'DD-MM-YYYY') AS FECHA, TX_VALOR_PARAMETRO, IN_TIPO_PARAMETRO, TX_OBSERVACIONES FROM RHEI.PARAMETRO WHERE CO_TIPO_BENEFICIO = ? "
					+ condicion;

			sqlBuilder.append(" SELECT * ");
			sqlBuilder.append(" FROM ( SELECT tmp.*, rownum rn ");
			sqlBuilder.append("          FROM (  ");
			sqlBuilder.append(sql.toString());
			sqlBuilder.append("                ) tmp ");
			sqlBuilder.append("                       WHERE rownum <=").append(
					indMayor);
			sqlBuilder.append("                    ) ");
			sqlBuilder.append("                WHERE rn >=").append(indMenor)
					.append("");

		} else {
			sql =

			"SELECT CO_COMPANIA, TIPO_EMP, CO_TIPO_BENEFICIO, CO_PARAMETRO, TO_CHAR(FE_VIGENCIA, 'DD-MM-YYYY') AS FECHA, TX_VALOR_PARAMETRO, IN_TIPO_PARAMETRO, TX_OBSERVACIONES FROM RHEI.PARAMETRO WHERE CO_TIPO_BENEFICIO=? AND CO_COMPANIA='"
					+ companiaAnalista + "' " + condicion;

			sqlBuilder.append(" SELECT * ");
			sqlBuilder.append(" FROM ( SELECT tmp.*, rownum rn ");
			sqlBuilder.append("          FROM (  ");
			sqlBuilder.append(sql.toString());
			sqlBuilder.append("                ) tmp ");
			sqlBuilder.append("                       WHERE rownum <=").append(
					indMayor);
			sqlBuilder.append("                    ) ");
			sqlBuilder.append("                WHERE rn >=").append(indMenor)
					.append("");

		}
		try {
			con = manejador.coneccionPool();	
			log.debug("Entro en buscarParametros");
			stmt = con.prepareStatement(sqlBuilder.toString());
			if (beneficioEscolar.compareTo("%") != 0) {
				stmt.setString(1, beneficioEscolar);
			}
			log.debug("Query: " + sqlBuilder.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				listadoParametros.add(rs.getString("CO_COMPANIA"));
				listadoParametros.add(rs.getString("TIPO_EMP"));
				listadoParametros.add(rs.getString("CO_TIPO_BENEFICIO"));
				listadoParametros.add(rs.getString("CO_PARAMETRO"));
				listadoParametros.add(rs.getString("FECHA"));
				listadoParametros.add(rs.getString("TX_VALOR_PARAMETRO"));
				listadoParametros.add(rs.getString("IN_TIPO_PARAMETRO"));
				listadoParametros.add(rs.getString("TX_OBSERVACIONES"));

			}
			log.debug("Contenido de listadoParametros :" + listadoParametros);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return listadoParametros;
	}
	
	
	public List<ReporteEstatus> searchReporteEstatus(String txDescriPeriodo,String filtro, int indMenor,
			int indMayor) {
		ManejadorDB manejador = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		manejador = new ManejadorDB();;
		con = manejador.conexion();

		List<ReporteEstatus> list = new ArrayList<ReporteEstatus>();
		StringBuilder sql = new StringBuilder();
		boolean where=false;
		boolean and=false;
		
		sql.append(" SELECT UNIQUE(RS.CO_REP_STATUS),RS.NB_REP_STATUS,RS.ST_REPORTE,PE.TX_DESCRIP_PERIODO,RS.NB_CREADO_POR FROM RHEI.RELACION_PAGOS RP ");
				sql.append("   INNER JOIN RHEI.PERIODO_ESCOLAR PE  ");
						sql.append(" ON RP.CO_PERIODO=PE.CO_PERIODO  ");
								sql.append(" INNER JOIN RHEI.REPORTE_STATUS RS  ");
										sql.append(" ON RS.CO_REP_STATUS=RP.CO_REP_STATUS  ");
										
										if (!StringUtils.isEmpty(txDescriPeriodo)){
											if (!where){
												sql.append(" WHERE ");
												where=true;
											}
											if (and){
												sql.append(" AND ");
											}
											and=true;
											sql.append("  PE.TX_DESCRIP_PERIODO=?  ");
										}
										
										if (!StringUtils.isEmpty(filtro)){
											if (!where){
												sql.append(" WHERE ");
												where=true;
											}
											if (and){
												sql.append(" AND ");
											}
											and=true;
											sql.append("  lower(RS.NB_REP_STATUS) LIKE '%").append(filtro.toLowerCase()).append("%'");
										}
										
//									 
										
										
										
		try {
			
			sql.append(" order BY CO_REP_STATUS desc ");
			
			StringBuilder sqlBuilder = new StringBuilder("");
			sqlBuilder.append(" SELECT * ");
			sqlBuilder.append(" FROM ( SELECT tmp.*, rownum rn ");
			sqlBuilder.append("          FROM (  ");
			sqlBuilder.append(sql.toString());
			sqlBuilder.append("                ) tmp ");
			sqlBuilder.append("                       WHERE rownum <=").append(
					indMayor);
			sqlBuilder.append("                    ) ");
			sqlBuilder.append("                WHERE rn >=").append(indMenor)
					.append("");
			stmt = con.prepareStatement(sqlBuilder.toString());
			if (!StringUtils.isEmpty(txDescriPeriodo)){
				stmt.setString(1, txDescriPeriodo);
			}
			ReporteEstatus reporteEstatus = null;
			rs = stmt.executeQuery();
			if (rs!=null){
				while (rs.next()) {
					reporteEstatus = new ReporteEstatus();
					reporteEstatus.setCoReporstatus(rs.getInt("CO_REP_STATUS"));
					reporteEstatus.setNombre(rs.getString("NB_REP_STATUS"));
					reporteEstatus.setStatus(rs.getString("ST_REPORTE"));
					reporteEstatus.setTxDescriPeriodo(rs.getString("TX_DESCRIP_PERIODO"));
					reporteEstatus.setNbCreadoPor(rs.getString("NB_CREADO_POR")==null?"":rs.getString("NB_CREADO_POR"));
					list.add(reporteEstatus);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return list;
	}
	
	
	public List<ReporteEstatus> searchTransitorio() {
		ManejadorDB manejador = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		manejador = new ManejadorDB();;
		con = manejador.conexion();
		List<ReporteEstatus> list = new ArrayList<ReporteEstatus>();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT UNIQUE(RS.CO_REP_STATUS),RS.NB_REP_STATUS,RS.ST_REPORTE,PE.TX_DESCRIP_PERIODO,RS.NB_CREADO_POR FROM RHEI.RELACION_PAGOS RP ");
		sql.append("   INNER JOIN RHEI.PERIODO_ESCOLAR PE  ");
				sql.append(" ON RP.CO_PERIODO=PE.CO_PERIODO  ");
						sql.append(" INNER JOIN RHEI.REPORTE_STATUS RS  ");
								sql.append(" ON RS.CO_REP_STATUS=RP.CO_REP_STATUS  ");
								sql.append(" where RS.ST_REPORTE =").append(Constantes.REPORTE_BENEFSOCIOECONOMICOTOPAGOTRIBUTO_TRANS);
								sql.append(" order by RS.CO_REP_STATUS desc ");
								 
		
		
		try {
			stmt = con.prepareStatement(sql.toString());
			ReporteEstatus reporteEstatus = null;
			rs = stmt.executeQuery();
			if (rs!=null){
				while (rs.next()) {
					reporteEstatus = new ReporteEstatus();
					reporteEstatus.setCoReporstatus(rs.getInt("CO_REP_STATUS"));
					reporteEstatus.setNombre(rs.getString("NB_REP_STATUS"));
					reporteEstatus.setStatus(rs.getString("ST_REPORTE"));
					reporteEstatus.setTxDescriPeriodo(rs.getString("TX_DESCRIP_PERIODO"));
					reporteEstatus.setNbCreadoPor(rs.getString("NB_CREADO_POR")==null?"":rs.getString("NB_CREADO_POR"));
					list.add(reporteEstatus);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return list;
	}
	
	
	/* (non-Javadoc)
	 * @see com.bcv.dao.jdbc.ReporteEstatusDao#updateDefinitivoReporte(long)
	 */
	public long updateDefinitivoReporte(long coRepStatus,String nombreUsuario)
			throws SQLException {
	 
		ManejadorDB manejadorDB=new ManejadorDB();;
		Connection con = manejadorDB.coneccionPool();	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ReporteEstatus  reporteEstatus  = new ReporteEstatus ();
		long result = 0;
		try {
			reporteEstatus.setStatus(Constantes.REPORTE_BENEFSOCIOECONOMICOTOPAGOTRIBUTO_DEF);
			reporteEstatus.setCoReporstatus(coRepStatus);
				pstmt = con.prepareStatement("UPDATE RHEI.REPORTE_STATUS SET ST_REPORTE=?,NB_CREADO_POR=?  WHERE CO_REP_STATUS=?");
				int posicion=1;
				pstmt.setString(posicion++, reporteEstatus.getStatus());
				pstmt.setString(posicion++, nombreUsuario);
				pstmt.setLong(posicion++,reporteEstatus.getCoReporstatus());
				result= pstmt.executeUpdate();
				 
		}
		catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return result;
	}
	
	public long deleteTransitorioReporte(long coRepStatus)
			throws SQLException {
	 
		ManejadorDB manejadorDB=new ManejadorDB();;
		Connection con = manejadorDB.coneccionPool();	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		long result = 0;
		try {
				pstmt = con.prepareStatement("UPDATE RHEI.RELACION_PAGOS SET CO_REP_STATUS=NULL WHERE CO_REP_STATUS=?");
				int posicion=1;
				pstmt.setLong(posicion++,coRepStatus);
				result= pstmt.executeUpdate();
				 
					pstmt = con.prepareStatement("DELETE RHEI.REPORTE_STATUS WHERE CO_REP_STATUS=?");
					posicion=1;
					pstmt.setLong(posicion++,coRepStatus);
					result= pstmt.executeUpdate();
			 
				 
		}
		catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return result;
	}
	
	public long deleteReporteStatusBad(int coRepStatus)
			throws SQLException {
	 
		ManejadorDB manejadorDB=new ManejadorDB();;
		Connection con = manejadorDB.coneccionPool();	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		long result = 0;
		try {
				pstmt = con.prepareStatement("delete from RHEI.REPORTE_STATUS    where CO_REP_STATUS not in  (select RP.CO_REP_STATUS from RHEI.RELACION_PAGOS rp  )  and CO_REP_STATUS=?");
				int posicion=1;
				pstmt.setInt(posicion++, coRepStatus);
				result= pstmt.executeUpdate();
				//and CO_REP_STATUS=63
		}
		catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return result;
	}
	
	
	 
	
	
 
 
	public long guardar(String nombreReporte,String receptorPago,Connection con,String nombreUsuario )
			throws SQLException {
	 
		Parametro parametro = parametroDao.findParametro(Constantes.CODIGO_COMPANIA,"EMP","RHEI",Constantes.CORADM,null);
	    String NB_COORD_ADMINIST=parametro.getValorParametro();
	    parametro = parametroDao.findParametro(Constantes.CODIGO_COMPANIA,"EMP","RHEI",Constantes.CORCON,null);
	    String NB_UNIDAD_CONTABIL =parametro.getValorParametro();
	    parametro = parametroDao.findParametro(Constantes.CODIGO_COMPANIA,"EMP","RHEI",Constantes.CORBEN,null);
	    String NB_COORD_BENEF_SOC=parametro.getValorParametro();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder("");
		ReporteEstatus  reporteEstatus  = new ReporteEstatus ();
		long codigo = 0;
		try {
			reporteEstatus.setNombre(nombreReporte);
			reporteEstatus.setStatus(Constantes.REPORTE_BENEFSOCIOECONOMICOTOPAGOTRIBUTO_TRANS);
			 
				pstmt = con.prepareStatement("SELECT RHEI.SEQ_CO_REP_STATUS.NEXTVAL AS valor from dual");
				ResultSet result = pstmt.executeQuery();
				
				if (result != null) {
				while (result.next()) {
					codigo = result.getInt("valor");
				}
					sql.append(" INSERT INTO RHEI.REPORTE_STATUS (CO_REP_STATUS,NB_REP_STATUS,NB_TIPO,ST_REPORTE,NB_FIRMA_REPORTE,NB_ABREV_REPORTE,NB_COORD_ADMINIST,NB_UNIDAD_CONTABIL,NB_COORD_BENEF_SOC,NB_CREADO_POR) "
							+ "VALUES (?,?,?,?,?,?,?,?,?,?)");
					//NB_CREADO_POR
					pstmt = con.prepareStatement(sql.toString());
					int posicion=1;
					pstmt.setLong(posicion++, codigo);
					pstmt.setString(posicion++, reporteEstatus.getNombre());
					pstmt.setString(posicion++, receptorPago);
					pstmt.setString(posicion++, reporteEstatus.getStatus());
					pstmt.setString(posicion++,parametroDao.findNB_FirmaReporte().getValorParametro());
					pstmt.setString(posicion++,parametroDao.findAbreviaturaFirmasReporte().getValorParametro());
					pstmt.setString(posicion++,NB_COORD_ADMINIST);
					pstmt.setString(posicion++,NB_UNIDAD_CONTABIL);
					pstmt.setString(posicion++,NB_COORD_BENEF_SOC);
					pstmt.setString(posicion++,nombreUsuario);
					
					pstmt.executeUpdate();
				}
		}
		catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt,null);
		}
		return codigo;
	}
	
	
	
	
	
	
	
	
	public String updateReporteStatusDeshacer(String numSoli) throws Exception{
	   String resp=null;
		 ManejadorDB manejadorDB=new ManejadorDB();;
			Connection con = manejadorDB.coneccionPool();	
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				StringBuilder sql = null;
				
				try {
			        	
			        	sql = new StringBuilder("");
			        	sql.append("UPDATE RHEI.RELACION_PAGOS SET CO_REP_STATUS=null WHERE NU_SOLICITUD  =").append(numSoli);
						pstmt = con.prepareStatement(sql.toString());
						int resultSql=pstmt.executeUpdate();
						resp=resultSql+"";
				}
				catch (SQLException e) {
					throw new Exception(e);
				} finally {
					liberarConexion(rs, pstmt,con);
				}			
		return  resp;
	}
	
	
	
	public String updateReporteStatusII(String numSoli,String mes,String descripPeriodo,int receptorPago,
			int coFormaPago1,String filtrarByMesOrComplementoOrAmbos,Connection con) throws Exception{
	 
		
		 PeriodoEscolar periodoEscolar= periodoEscolarDao.findPeriodoByDescripcion(descripPeriodo);
		 String resultado="";
		
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				StringBuilder sql = new StringBuilder("");
				
				try {
			 
			 
					
					pstmt = con.prepareStatement("SELECT RHEI.SEQ_CO_REP_STATUS.CURRVAL AS valor from dual");
					ResultSet result = pstmt.executeQuery();
					int codigoReporteStatus = 0;
					if (result != null) {
					while (result.next()) {
						codigoReporteStatus = result.getInt("valor");
					}
					}
			
				 
					 
						sql = new StringBuilder("");
						sql.append("UPDATE RHEI.RELACION_PAGOS SET CO_REP_STATUS=? WHERE NU_SOLICITUD  =").append(numSoli).append(" AND NU_REF_PAGO =").append(mes);
						sql.append("  and co_periodo=").append(periodoEscolar.getCodigoPeriodo());
						sql.append("  and in_receptor_pago=").append(receptorPago);
						if (coFormaPago1==3){
							sql.append("  and (co_forma_pago=1 or co_forma_pago=2)");
						}else{
							sql.append("  and co_forma_pago=").append(coFormaPago1);	
						}
						
						if ("0".equalsIgnoreCase(filtrarByMesOrComplementoOrAmbos)){
							sql.append("  	  and IN_COMPLEMENTO='N' ");
						}
						if ("1".equalsIgnoreCase(filtrarByMesOrComplementoOrAmbos)){
							sql.append("  	  and IN_COMPLEMENTO='S' ");
						}
						if ("2".equalsIgnoreCase(filtrarByMesOrComplementoOrAmbos)){
							sql.append("   and ( IN_COMPLEMENTO='N' OR IN_COMPLEMENTO='S' ) ");
						}
						sql.append(" and CO_REP_STATUS IS NULL ");
							pstmt = con.prepareStatement(sql.toString());
							pstmt.setLong(1, codigoReporteStatus);
							int resultSql=pstmt.executeUpdate();
							if (resultSql>0){
								resultado=resultSql+"";
							}
						 
				 
					
						 
				}
				catch (SQLException e) {
					e.printStackTrace();
				} finally {
					liberarConexion(rs, pstmt,null);
				}	 

			
			
	
		return  resultado;
	}
	
	
	
	public String updateReporteStatus(long codigoReporteStatus,String numSoli,String mes,String descripPeriodo,int receptorPago,
			int coFormaPago1,String filtrarByMesOrComplementoOrAmbos,Connection con) throws Exception{
		
		 PeriodoEscolar periodoEscolar= periodoEscolarDao.findPeriodoByDescripcion(descripPeriodo);
		 String resultado="";
		
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				StringBuilder sql = new StringBuilder("");
				
				try {
					
				
				
					
					pstmt = con.prepareStatement("SELECT RHEI.SEQ_CO_REP_STATUS.CURRVAL AS valor from dual");
					ResultSet result = pstmt.executeQuery();
					codigoReporteStatus = 0;
					if (result != null) {
					while (result.next()) {
						codigoReporteStatus = result.getInt("valor");
					}
					}
			
				 
					 
						sql = new StringBuilder("");
						sql.append("UPDATE RHEI.RELACION_PAGOS SET CO_REP_STATUS=? WHERE NU_SOLICITUD  =").append(numSoli).append(" AND NU_REF_PAGO =").append(mes);
						sql.append("  and co_periodo=").append(periodoEscolar.getCodigoPeriodo());
						sql.append("  and in_receptor_pago=").append(receptorPago);
						sql.append("  and co_forma_pago=").append(coFormaPago1);
						if ("0".equalsIgnoreCase(filtrarByMesOrComplementoOrAmbos)){
							sql.append("  	  and IN_COMPLEMENTO='N' ");
						}
						if ("1".equalsIgnoreCase(filtrarByMesOrComplementoOrAmbos)){
							sql.append("  	  and IN_COMPLEMENTO='S' ");
						}
						if ("2".equalsIgnoreCase(filtrarByMesOrComplementoOrAmbos)){
							sql.append(" ");
						}
							pstmt = con.prepareStatement(sql.toString());
							pstmt.setLong(1, codigoReporteStatus);
							int resultSql=pstmt.executeUpdate();
							if (resultSql>0){
								resultado=resultSql+"";
							}
						 
				 
					
						 
				}
				catch (SQLException e) {
					e.printStackTrace();
				} finally {
					liberarConexion(rs, pstmt,null);
				}	 

			
			
	
		return  resultado;
	}
	
	
	
	public String existeReporteStatus(String numSoli,String mes,String descripPeriodo,int receptorPago,
			int coFormaPago1,String filtrarByMesOrComplementoOrAmbos,Connection con) throws Exception{
		String result="";
		 PeriodoEscolar periodoEscolar= periodoEscolarDao.findPeriodoByDescripcion(descripPeriodo);
	 
		
	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder("");
		
		
		
		
		try { 
			sql.append("SELECT CO_REP_STATUS FROM  RHEI.RELACION_PAGOS WHERE NU_SOLICITUD =").append(numSoli).append(" AND NU_REF_PAGO =").append(mes);
			sql.append("  and co_periodo=").append(periodoEscolar.getCodigoPeriodo());
			sql.append("  and in_receptor_pago=").append(receptorPago);
			sql.append("  and co_forma_pago=").append(coFormaPago1);
			if ("0".equalsIgnoreCase(filtrarByMesOrComplementoOrAmbos)){
				sql.append("  	  and IN_COMPLEMENTO='N' ");
			}
			if ("1".equalsIgnoreCase(filtrarByMesOrComplementoOrAmbos)){
				sql.append("  	  and IN_COMPLEMENTO='S' ");
			}
			if ("2".equalsIgnoreCase(filtrarByMesOrComplementoOrAmbos)){
				sql.append(" ");
			}
			sql.append(" AND CO_REP_STATUS IS NOT NULL");
			pstmt = con.prepareStatement(sql.toString());
			rs= pstmt.executeQuery();
			if (rs.next()){
			  int resultadoInt=rs.getInt("CO_REP_STATUS");
			  result=nameReporteStatusNameAnio(resultadoInt);
			}
			
				 
		}
		catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, null);
		}
		return result;
	}
	
	
	
	/**
	 * numero de registros que me trae la consuilta
	 * 
	 */
	public int cuantosSql() {
		ManejadorDB manejador=new ManejadorDB();;
		Connection con = null;	
		ResultSet rs = null;
		PreparedStatement stmt = null;
	 
		int cuantos = 0;
		String sql = "";
		
			sql = "SELECT count( UNIQUE(CO_REP_STATUS)) as cuantos  FROM RHEI.RELACION_PAGOS rp where RP.CO_REP_STATUS is not null";
		try {
			con = manejador.coneccionPool();
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				cuantos = rs.getInt("cuantos");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return cuantos;
	}
	
	public boolean isAllDefinitivos() {
		ManejadorDB manejador=new ManejadorDB();;
		Connection con = null;	
		ResultSet rs = null;
		PreparedStatement stmt = null;
	    boolean isAllDefinitivos=true;
		StringBuilder sql = new StringBuilder("");
		
		    sql.append("  SELECT count( UNIQUE(rp.CO_REP_STATUS)) as cuantos FROM RHEI.RELACION_PAGOS RP ");
		    sql.append("    inner join RHEI.REPORTE_STATUS rs ");
		    		sql.append("  on RP.CO_REP_STATUS=RS.CO_REP_STATUS where Rs.ST_REPORTE =").append(Constantes.REPORTE_BENEFSOCIOECONOMICOTOPAGOTRIBUTO_TRANS);
			
		try {
			con = manejador.coneccionPool();
			stmt = con.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			if (rs.next()) {
				int cuantos=rs.getInt("cuantos");
				log.info("cuantos="+cuantos);
				if (cuantos>0){
					isAllDefinitivos=false;	
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return isAllDefinitivos;
	}
	
	
	public String nameReporteStatusNameAnio(int coRepStatus){
		
        StringBuilder result= new StringBuilder("");
        ManejadorDB manejador=new ManejadorDB();;
		Connection con = null;	
		ResultSet rs = null;
		PreparedStatement stmt = null;
		StringBuilder sql = new StringBuilder("");
		
			
			sql.append( " select rs.NB_REP_STATUS,PESC.TX_DESCRIP_PERIODO from RHEI.REPORTE_STATUS rs "); 
			sql.append( " inner join RHEI.RELACION_PAGOS RP ");
					sql.append( " ON RP.CO_REP_STATUS=RS.CO_REP_STATUS ");
							sql.append( " inner join RHEI.PERIODO_ESCOLAR pesc ");
									sql.append( " on PESC.CO_PERIODO=RP.CO_PERIODO ");
											sql.append( " WHERE RS.co_rep_status=? ");
		try {
			con = manejador.coneccionPool();
			stmt = con.prepareStatement(sql.toString());
			int posicion=1;
			stmt.setInt(posicion++, coRepStatus);
			rs = stmt.executeQuery();
			if (rs.next()) {
				result.append(rs.getString("NB_REP_STATUS")).append(" ").append(rs.getString("TX_DESCRIP_PERIODO"));	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return result.toString();
}
	
public ReporteEstatus reporteStatus(int coRepStatus){
		
	ReporteEstatus reporteEstatus= null;
        ManejadorDB manejador=new ManejadorDB();;
		Connection con = null;	
		ResultSet rs = null;
		PreparedStatement stmt = null;
		StringBuilder sql = new StringBuilder("");
		
			
			sql.append( "select RS.NB_COORD_ADMINIST,RS.NB_UNIDAD_CONTABIL,RS.NB_COORD_BENEF_SOC, RS.CO_REP_STATUS,RS.NB_REP_STATUS,RS.ST_REPORTE,RS.NB_TIPO,RS.NB_ABREV_REPORTE,RS.NB_FIRMA_REPORTE from RHEI.REPORTE_STATUS rs"); 
											sql.append( " WHERE RS.co_rep_status=? ");
		try {
			con = manejador.coneccionPool();
			stmt = con.prepareStatement(sql.toString());
			int posicion=1;
			stmt.setInt(posicion++, coRepStatus);
			rs = stmt.executeQuery();
			if (rs.next()) {
				reporteEstatus = new ReporteEstatus();
				reporteEstatus.setNombre(rs.getString("NB_REP_STATUS"));
				reporteEstatus.setCoReporstatus(coRepStatus);
				reporteEstatus.setStatus(rs.getString("ST_REPORTE"));
				reporteEstatus.setNbTipo(rs.getString("NB_TIPO"));
				reporteEstatus.setNbAbrevReporte(rs.getString("NB_ABREV_REPORTE"));
				reporteEstatus.setNbFirmaReporte(rs.getString("NB_FIRMA_REPORTE"));
				reporteEstatus.setNbCoordAdminist(rs.getString("NB_COORD_ADMINIST"));
				reporteEstatus.setNbUnidadContabil(rs.getString("NB_UNIDAD_CONTABIL"));
				reporteEstatus.setNbCoordBenefSoc(rs.getString("NB_COORD_BENEF_SOC"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return reporteEstatus;
}
	
	public boolean existeReporteStatusNameAnio(String name,String anio){
		
                       
                       ManejadorDB manejador=new ManejadorDB();;
               		Connection con = null;	
               		ResultSet rs = null;
               		PreparedStatement stmt = null;
               	    boolean existe=false;
               		StringBuilder sql = new StringBuilder("");
               		
               			
               			sql.append( " select rs.NB_REP_STATUS from RHEI.REPORTE_STATUS rs "); 
               			sql.append( " inner join RHEI.RELACION_PAGOS RP ");
               					sql.append( " ON RP.CO_REP_STATUS=RS.CO_REP_STATUS ");
               							sql.append( " inner join RHEI.PERIODO_ESCOLAR pesc ");
               									sql.append( " on PESC.CO_PERIODO=RP.CO_PERIODO ");
               											sql.append( " WHERE lower(RS.NB_REP_STATUS)=lower(?) ");
               													sql.append( " AND PESC.TX_DESCRIP_PERIODO=? ");
               		try {
               			con = manejador.coneccionPool();
               			stmt = con.prepareStatement(sql.toString());
               			int posicion=1;
               			stmt.setString(posicion++, name);
               			stmt.setString(posicion++, anio);
               			rs = stmt.executeQuery();
               			if (rs.next()) {
               					existe=true;	
               			}
               		} catch (SQLException e) {
               			e.printStackTrace();
               		} finally {
               			liberarConexion(rs, stmt, con);
               		}
               		return existe;
                       
		
	}
   
	

}
