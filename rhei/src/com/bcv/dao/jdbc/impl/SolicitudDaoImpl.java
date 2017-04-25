/**
 * 
 */
package com.bcv.dao.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.bean.ValorNombre;
import ve.org.bcv.rhei.util.Constantes;

import com.bcv.dao.jdbc.BeneficiarioDao;
import com.bcv.dao.jdbc.ConyugeTrabajoDao;
import com.bcv.dao.jdbc.ManejadorDB;
import com.bcv.dao.jdbc.SimpleJDBCDaoImpl;
import com.bcv.dao.jdbc.SolicitudDao;
import com.bcv.dao.jdbc.TrabajadorDao;
import com.bcv.model.Beneficiario;
import com.bcv.model.ConyugeTrabajo;
import com.bcv.model.Solicitud;
import com.bcv.model.Trabajador;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco 02/07/2015 15:29:30 2015 mail :
 *         oraclefedora@gmail.com
 */
public class SolicitudDaoImpl extends SimpleJDBCDaoImpl<Solicitud>
		implements
			SolicitudDao {
	private BeneficiarioDao beneficiarioDao = new BeneficiarioDaoImpl();
	private ConyugeTrabajoDao conyugeTrabajoDao = new ConyugeTrabajoDaoImpl();
	private TrabajadorDao trabajadorDao = new TrabajadorDaoImpl();
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(SolicitudDaoImpl.class
			.getName());
	ManejadorDB manejadorDB = new ManejadorDB();;

	/**
	 * @param dataSource
	 */
	public SolicitudDaoImpl() {
		super();
	}
	
	
	/**
	 * Por numero de solicitud, buscamos si es renovacion o primera vez
	 * 
	 * @param nroSolicitud
	 * @return
	 * @throws SQLException
	 */
	public boolean isRenovacion(int nroSolicitud)
			throws SQLException {
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean isrenov=false;
		 String numCodEmpl=trabajadorDao.codigoEmpleadoByNuSolicitud(nroSolicitud);
		try {

			StringBuilder sql = new StringBuilder("");

			sql.append(" SELECT mss.CO_STATUS FROM RHEI.SOLICITUD_BEI sbei inner join "); 
			sql.append(" RHEI.MOV_ST_SOLIC_BEI mss on SBEI.NU_SOLICITUD=MSS.NU_SOLICITUD ");
			sql.append(" where SBEI.CO_EMPLEADO=? and MSS.CO_STATUS='").append(Constantes.CO_STATUS_DESINCORPORADO).append("'"); 
			
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, numCodEmpl);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				isrenov=true;
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return isrenov;
	}

	/**
	 * Por numero de solicitud, tenemos el periodo escolar
	 * 
	 * @param nroSolicitud
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<String> cargarPeriodoByNroSolicitud(int nroSolicitud)
			throws SQLException {
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<String> dupla = new ArrayList<String>();
		try {

			String sql = "SELECT P.CO_PERIODO, P.TX_DESCRIP_PERIODO  FROM RHEI.MOV_ST_SOLIC_BEI M, RHEI.PERIODO_ESCOLAR P WHERE  M.NU_SOLICITUD = ? AND M.CO_PERIODO = P.CO_PERIODO ORDER BY M.CO_PERIODO ASC, M.FE_STATUS ASC";

			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, nroSolicitud);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				dupla.add(String.valueOf(rs.getString("TX_DESCRIP_PERIODO")));
				dupla.add(String.valueOf(rs.getString("TX_DESCRIP_PERIODO")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return dupla;
	}
	
 

	/**
	 * @param origen
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<String> cargarPeriodo(String origen) throws SQLException {

		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "";
		@SuppressWarnings("unused")
		int diasPrecargaSolicitud = 0;
		@SuppressWarnings("unchecked")
		ArrayList<String> dupla = new ArrayList();
		if (origen.compareTo("PagoNoConvencional") == 0) {
			diasPrecargaSolicitud = 0;
		} else if (origen.compareTo("Solicitud") == 0) {
			diasPrecargaSolicitud = 30;
		}
		try {
			log.debug("Entro en cargarPeriodo");
			sql = "SELECT P.CO_PERIODO, P.TX_DESCRIP_PERIODO FROM RHEI.PERIODO_ESCOLAR P WHERE   P.IN_PERIODO_ESCOLAR = 'A' ORDER BY P.TX_DESCRIP_PERIODO";

			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				dupla.add(String.valueOf(rs.getString("TX_DESCRIP_PERIODO")));
				dupla.add(String.valueOf(rs.getString("TX_DESCRIP_PERIODO")));
			}
			log.debug("Contenido de listaFiltros :" + dupla);

		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return dupla;
	}

	/**
	 * @param estatusSolicitud
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<String> cargarPeriodoEscolar(String estatusSolicitud)
			throws SQLException {
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String fecha = "";
		ArrayList<String> periodosEscolares = new ArrayList<String>();
		try {
			String sql = "SELECT DISTINCT M.CO_PERIODO AS CO_PERIODO , P.TX_DESCRIP_PERIODO AS TX_DESCRIP_PERIODO , M.FE_STATUS AS FE_STATUS FROM RHEI.MOV_ST_SOLIC_BEI M, RHEI.PERIODO_ESCOLAR P WHERE M.CO_STATUS      = ? AND M.CO_PERIODO = P.CO_PERIODO ORDER BY M.CO_PERIODO ASC, M.FE_STATUS ASC";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, estatusSolicitud);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				periodosEscolares.add(String.valueOf(rs.getInt("CO_PERIODO")));
				periodosEscolares.add(rs.getString("TX_DESCRIP_PERIODO"));
				fecha = rs.getTimestamp("FE_STATUS").toString();
				fecha = fecha.substring(0, fecha.indexOf("."));
				fecha = fecha.replace(" ", "_");
				periodosEscolares.add(fecha);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return periodosEscolares;
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<String> cargarFormaPago() throws SQLException {
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<String> maneraPago = new ArrayList<String>();
		try {
			String sql = "SELECT CO_FORMA_PAGO, TX_FORMA_PAGO FROM RHEI.FORMA_DE_PAGO WHERE CO_FORMA_PAGO='1' OR CO_FORMA_PAGO='2' ORDER BY CO_FORMA_PAGO DESC";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				maneraPago.add(rs.getString("CO_FORMA_PAGO"));
				maneraPago.add(rs.getString("TX_FORMA_PAGO"));
			}
			log.debug("Contenido de maneras de pago :" + maneraPago);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return maneraPago;
	}
	

	/**
	 * 
	 * Lista de periodos anuales
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<ValorNombre> estadosSolicitudLst() throws SQLException {
		List<ValorNombre> objetos = new ArrayList<ValorNombre>();
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT CO_STATUS, INITCAP(NB_STATUS) FROM RHEI.ST_SOLICITUD_BEI  SBEI WHERE SBEI.CO_STATUS IN ('A','D') ORDER BY CO_STATUS ASC";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			ValorNombre valorNombre = null;
			while (rs.next()) {
				valorNombre = new ValorNombre(rs.getString("CO_STATUS"),
						rs.getString("INITCAP(NB_STATUS)"));
				objetos.add(valorNombre);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return objetos;
	}
	
	
	
	public ShowResultToView searchEmpleado(String numSolicitud){
		Connection con =null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ShowResultToView showResultToView=null;
		StringBuilder sql = new StringBuilder("");
		sql.append("SELECT S.CO_EMPLEADO AS codEmp, S.CEDULA_FAMILIAR AS  codigoBenef, S.NU_RIF_PROVEEDOR AS nroRifCentroEdu ,");
		sql.append(" TE.CEDULA AS cedula ");
		sql.append(" FROM RHEI.SOLICITUD_BEI S INNER JOIN  PERSONAL.TODOS_EMPLEADOS TE ON S.CO_EMPLEADO=TE.CODIGO_EMPLEADO ");
		sql.append("  WHERE S.NU_SOLICITUD=   ? ");

		try {
			con = manejadorDB.coneccionPool();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, numSolicitud);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				showResultToView= new ShowResultToView();
				showResultToView.setCedula(rs.getString("cedula"));
				showResultToView.setCodEmp(rs.getString("codEmp"));
				showResultToView.setCodigoBenef(rs
						.getString("codigoBenef"));
				showResultToView.setNroRifCentroEdu(rs
						.getString("nroRifCentroEdu"));
				if (StringUtils.isNumeric(numSolicitud)) {
					int numSolicitudInt = new Integer(numSolicitud);
					showResultToView.setNroSolicitud(numSolicitudInt);
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		
		return showResultToView;
	}
	
	
	

	/**
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<String> cargarEstadosSolicitud() throws SQLException {

		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<String> listaEstadosSolicitud = new ArrayList<String>();
		try {
			String sql = "SELECT CO_STATUS, INITCAP(NB_STATUS) FROM RHEI.ST_SOLICITUD_BEI ORDER BY CO_STATUS ASC";

			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				listaEstadosSolicitud.add(rs.getString("CO_STATUS"));
				listaEstadosSolicitud.add(rs.getString("INITCAP(NB_STATUS)"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return listaEstadosSolicitud;
	}
	
	
	
	
	/**
	 * El ultimo codigo de solicitud de la ultimo mes en la factura
	 * 
	 * @param codEmplado
	 * @param codigoBenef
	 * @param mes
	 * @return
	 */
	public String regularOrtEspecial( String numSolicitud)
			throws SQLException {
		StringBuilder consulta = new StringBuilder("");
        String ninoEspecial="0";
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = manejadorDB.coneccionPool();
			pstmt = null;
			rs = null;
			consulta.append(" select R.IN_TP_EDUCACION FROM RHEI.SOLICITUD_BEI  R ");
			
			StringTokenizer st = new StringTokenizer(numSolicitud,",");
			String token="";
			while (st.hasMoreTokens()){
				  token= st.nextToken();
				  consulta.append("  where R.NU_SOLICITUD =").append("'").append(token).append("'");
				  break;
			}
			pstmt = con.prepareStatement(consulta.toString());
		 
			rs = pstmt.executeQuery();
			if ((rs.next())) {
				String regularCeroEspecialUno=rs.getString("IN_TP_EDUCACION");
				if ("R".equalsIgnoreCase(regularCeroEspecialUno)) {
					ninoEspecial="0";
				}else if ("E".equalsIgnoreCase(regularCeroEspecialUno)) {
					ninoEspecial="1";
				} 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return ninoEspecial;
	}
	
	/**
	 * El ultimo codigo de solicitud de la ultimo mes en la factura
	 * 
	 * @param codEmplado
	 * @param codigoBenef
	 * @param mes
	 * @return
	 */
	public Solicitud solicitudBylastCodEmpAndFamily(int codEmplado,
			int codigoBenef,  String txDescripPeriodo)
			throws SQLException {
		StringBuilder consulta = new StringBuilder("");

		Solicitud solicitud = new Solicitud();
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = manejadorDB.coneccionPool();
			pstmt = null;
			rs = null;
			consulta.append("  select SBEI.NU_SOLICITUD from RHEI.SOLICITUD_BEI SBEI INNER JOIN RHEI.RELACION_PAGOS RP ");
			consulta.append(" ON RP.NU_SOLICITUD=SBEI.NU_SOLICITUD    where ");
			consulta.append(" SBEI.CO_EMPLEADO=? and ");
			consulta.append(" SBEI.CEDULA_FAMILIAR =?   ");
			consulta.append(" AND RP.CO_PERIODO IN (select PE2.CO_PERIODO from RHEI.PERIODO_ESCOLAR PE2 WHERE PE2.TX_DESCRIP_PERIODO=? ) ");
			consulta.append(" order by SBEI.nu_solicitud desc");

			pstmt = con.prepareStatement(consulta.toString());
			int posicion = 1;
			pstmt.setInt(posicion++, codEmplado);
			pstmt.setInt(posicion++, codigoBenef);
			pstmt.setString(posicion++, txDescripPeriodo);

			rs = pstmt.executeQuery();
			if ((rs.next())) {
				solicitud.setNroSolicitud(rs.getInt("NU_SOLICITUD"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return solicitud;
	}

	/**
	 * El ultimo codigo de solicitud de la ultimo mes en la factura
	 * 
	 * @param codEmplado
	 * @param codigoBenef
	 * @param mes
	 * @return
	 */
	public Solicitud solicitudBylastCodEmpAndFamily(int codEmplado,
			int codigoBenef, int mes, String txDescripPeriodo)
			throws SQLException {
		StringBuilder consulta = new StringBuilder("");

		Solicitud solicitud = new Solicitud();
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = manejadorDB.coneccionPool();
			pstmt = null;
			rs = null;
			consulta.append("  select SBEI.NU_SOLICITUD from RHEI.SOLICITUD_BEI SBEI INNER JOIN RHEI.RELACION_PAGOS RP ");
			consulta.append(" ON RP.NU_SOLICITUD=SBEI.NU_SOLICITUD    where ");
			consulta.append("  RP.nu_ref_pago=? AND");
			consulta.append(" SBEI.CO_EMPLEADO=? and ");
			consulta.append(" SBEI.CEDULA_FAMILIAR =?   ");
			consulta.append(" AND RP.CO_PERIODO IN (select PE2.CO_PERIODO from RHEI.PERIODO_ESCOLAR PE2 WHERE PE2.TX_DESCRIP_PERIODO=? ) ");
			consulta.append(" order by SBEI.nu_solicitud desc");

			pstmt = con.prepareStatement(consulta.toString());
			int posicion = 1;
			pstmt.setInt(posicion++, mes);
			pstmt.setInt(posicion++, codEmplado);
			pstmt.setInt(posicion++, codigoBenef);
			pstmt.setString(posicion++, txDescripPeriodo);

			rs = pstmt.executeQuery();
			if ((rs.next())) {
				solicitud.setNroSolicitud(rs.getInt("NU_SOLICITUD"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return solicitud;
	}
	
	
	
	/**
	 * 
	 * Obtenemos el status de la solicitud, numero de solicitrud
	 * 
	 * @param codEmplado
	 * @param codigoBenef
	 * @return
	 * @throws SQLException
	 */
	public List<Solicitud> BscarNrosSolicitudesWithCodEmpStatusCodBenef(String codEmplado,
			String status, String codigoBenef) throws SQLException {
		StringBuilder consulta = new StringBuilder("");
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Solicitud> result= new ArrayList<Solicitud>();
		/**
		 * Codigo del empleado lo colocamos en el objeto trabajador para obtener
		 * sus datos.
		 */
		Solicitud solicitud = null;
		try {
			consulta = new StringBuilder("");
			consulta.append(" select UNIQUE(SBEI.NU_SOLICITUD) from RHEI.SOLICITUD_BEI SBEI INNER JOIN RHEI.MOV_ST_SOLIC_BEI MSBEI ");
			consulta.append(" ON SBEI.NU_SOLICITUD=MSBEI.NU_SOLICITUD ");
			consulta.append(" INNER JOIN RHEI.RELACION_PAGOS RP ");
			consulta.append(" ON RP.NU_SOLICITUD= SBEI.NU_SOLICITUD ");
			consulta.append(" WHERE  "); 
			boolean isAnd=false;
			if ((codEmplado != null) && (!codEmplado.toString().equals(""))
					&& StringUtils.isNumeric(codEmplado)) {
				if(isAnd){
					consulta.append(" AND ");		
				}
			consulta.append(" SBEI.CO_EMPLEADO  = ?  ");
			isAnd=true;
			}
			
			if (!StringUtils.isEmpty(status)){
				if(isAnd){
					consulta.append(" AND ");		
				}
				consulta.append("    MSBEI.CO_STATUS=?");	
				isAnd=true;
			}
			
			if (!StringUtils.isEmpty(codigoBenef)){
				if(isAnd){
					consulta.append(" AND ");		
				}
				consulta.append("     SBEI.CEDULA_FAMILIAR=?");	
				isAnd=true;
			}
			
			
	
			consulta.append(" ORDER BY SBEI.NU_SOLICITUD DESC ");
			
			pstmt = con.prepareStatement(consulta.toString());
			int posicion=1;
			if ((codEmplado != null) && (!codEmplado.toString().equals(""))
					&& StringUtils.isNumeric(codEmplado)) {
				pstmt.setInt(posicion++, Integer.parseInt(codEmplado));
			}
			if (!StringUtils.isEmpty(status)){
				pstmt.setString(posicion++,status);
			}
			if (!StringUtils.isEmpty(codigoBenef)){
				pstmt.setString(posicion++,codigoBenef);
			}
		 
			rs = pstmt.executeQuery();
			while ((rs.next())) {
				 solicitud = new Solicitud();
				solicitud.setNroSolicitud(rs.getInt("NU_SOLICITUD"));
				result.add(solicitud);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return result;
	}

	/**
	 * 
	 * Obtenemos el status de la solicitud, numero de solicitrud
	 * 
	 * @param codEmplado
	 * @param codigoBenef
	 * @return
	 * @throws SQLException
	 */
	public Solicitud BscarSolConCodEmpCodBenf(String codEmplado,
			String codigoBenef) throws SQLException {
		StringBuilder consulta = new StringBuilder("");
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		/**
		 * Codigo del empleado lo colocamos en el objeto trabajador para obtener
		 * sus datos.
		 */
		Solicitud solicitud = new Solicitud();
		if ((codEmplado != null) && (!codEmplado.toString().equals(""))
				&& StringUtils.isNumeric(codEmplado)) {
			solicitud.setCodigoEmpleado(Integer.parseInt(codEmplado));
		}
		/**
		 * Codigo beneficiario lo colocamos en el objeto beneficiario para
		 * obtener sus datos.
		 */
		if ((codigoBenef != null) && (!codigoBenef.equalsIgnoreCase(""))) {
			solicitud.setCedulaFamiliar(Integer.parseInt(codigoBenef));
		}
		try {
			consulta = new StringBuilder(
					"SELECT S.nu_rif_proveedor,S.NU_SOLICITUD, M.CO_STATUS, DECODE(IN_NIVEL_ESCOLAR, '0', 'Maternal', '1','1° Nivel', '2','2° Nivel','3','3° Nivel','NO ASOCIADO') AS IN_NIVEL_ESCOLAR, PE.TX_DESCRIP_PERIODO,ST.NB_STATUS  FROM RHEI.SOLICITUD_BEI S, RHEI.MOV_ST_SOLIC_BEI M, RHEI.PERIODO_ESCOLAR PE, RHEI.ST_SOLICITUD_BEI ST WHERE S.NU_SOLICITUD \t= M.NU_SOLICITUD AND M.CO_PERIODO \t= PE.CO_PERIODO AND M.CO_STATUS     = ST.CO_STATUS AND S.NU_SOLICITUD = (SELECT MAX(NU_SOLICITUD) FROM RHEI.SOLICITUD_BEI WHERE CO_EMPLEADO = ? AND CEDULA_FAMILIAR = ?) AND FE_STATUS \t= (SELECT MAX(FE_STATUS) FROM RHEI.SOLICITUD_BEI S1, RHEI.MOV_ST_SOLIC_BEI M1 WHERE S1.NU_SOLICITUD = M1.NU_SOLICITUD AND S1.NU_SOLICITUD = S.NU_SOLICITUD)");
			pstmt = con.prepareStatement(consulta.toString());
			pstmt.setInt(1, solicitud.getCodigoEmpleado());
			pstmt.setInt(2, solicitud.getCedulaFamiliar());
			rs = pstmt.executeQuery();
			if ((rs.next())) {
				solicitud.setTextoPeriodo(rs.getString("TX_DESCRIP_PERIODO"));
				solicitud.setNroSolicitud(rs.getInt("NU_SOLICITUD"));
				solicitud.setCo_status(rs.getString("CO_STATUS"));
				solicitud.setNivelEscolar(rs.getString("IN_NIVEL_ESCOLAR"));
				solicitud.setNb_status(rs.getString("NB_STATUS"));
				solicitud.setNroRifCentroEdu(rs.getString("nu_rif_proveedor"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return solicitud;
	}

	/**
	 * 
	 * Obtenemos el status de la solicitud, numero de solicitrud
	 * 
	 * @param codEmplado
	 * @param codigoBenef
	 * @return
	 * @throws SQLException
	 */
	public Solicitud cedulaEmplYOtrosSolicitud(int nroSolicitud)
			throws SQLException {
		StringBuilder consulta = new StringBuilder("");
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		/**
		 * Codigo del empleado lo colocamos en el objeto trabajador para obtener
		 * sus datos.
		 */
		Solicitud solicitud = new Solicitud();
		if (nroSolicitud > 0) {
			solicitud.setNroSolicitud(nroSolicitud);
			try {

				StringBuilder sql = new StringBuilder("");
				sql.append("SELECT S.CO_EMPLEADO AS codEmp, S.CEDULA_FAMILIAR AS  codigoBenef, S.NU_RIF_PROVEEDOR AS nroRifCentroEdu ,");
				sql.append(" TE.CEDULA AS cedula ");
				sql.append(" FROM RHEI.SOLICITUD_BEI S INNER JOIN  PERSONAL.TODOS_EMPLEADOS TE ON S.CO_EMPLEADO=TE.CODIGO_EMPLEADO ");
				sql.append("  WHERE S.NU_SOLICITUD=   ? ");

				consulta = new StringBuilder(sql.toString());
				pstmt = con.prepareStatement(consulta.toString());
				pstmt.setInt(1, solicitud.getNroSolicitud());
				rs = pstmt.executeQuery();
				if ((rs.next())) {
					solicitud.setCodigoEmpleado(rs.getInt("codEmp"));
					solicitud.setCedulaFamiliar(rs.getInt("codigoBenef"));
					solicitud.setCedula(rs.getInt("cedula"));
					solicitud.setNroRifCentroEdu(rs
							.getString("nroRifCentroEdu"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				liberarConexion(rs, pstmt, con);
			}
		}

		return solicitud;
	}

	/**
	 * 
	 * Obtenemos el status de la solicitud, numero de solicitrud
	 * 
	 * @param codEmplado
	 * @param codigoBenef
	 * @return
	 * @throws SQLException
	 */
	public Solicitud BscarSolConCodEmpCodBenfNrif(String codEmplado,
			String codigoBenef, String nroRifCentroEdu) throws SQLException {
		StringBuilder consulta = new StringBuilder("");
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		/**
		 * Codigo del empleado lo colocamos en el objeto trabajador para obtener
		 * sus datos.
		 */
		Solicitud solicitud = new Solicitud();
		if ((codEmplado != null) && (!codEmplado.toString().equals(""))) {
			solicitud.setCodigoEmpleado(Integer.parseInt(codEmplado));
		}
		/**
		 * Codigo beneficiario lo colocamos en el objeto beneficiario para
		 * obtener sus datos.
		 */
		if ((codigoBenef != null) && (!codigoBenef.equalsIgnoreCase(""))) {
			solicitud.setCedulaFamiliar(Integer.parseInt(codigoBenef));
		}
		if (!StringUtils.isEmpty(nroRifCentroEdu)) {
			solicitud.setNroRifCentroEdu(nroRifCentroEdu);
		}

		try {
			consulta = new StringBuilder("");
			consulta.append(" SELECT S.NU_SOLICITUD, M.CO_STATUS, DECODE(M.IN_NIVEL_ESCOLAR, '0', 'Maternal', '1','1° Nivel', '2','2° Nivel','3','3° Nivel','NO ASOCIADO') AS IN_NIVEL_ESCOLAR, ");
			consulta.append("  PE.TX_DESCRIP_PERIODO,ST.NB_STATUS FROM RHEI.SOLICITUD_BEI S ");
			consulta.append("                                                                          INNER JOIN RHEI.MOV_ST_SOLIC_BEI M ");
			consulta.append(" 	                                                                                  ON S.NU_SOLICITUD=M.NU_SOLICITUD ");
			consulta.append("                                                                           INNER JOIN  RHEI.PERIODO_ESCOLAR PE ");
			consulta.append("                                                                           ON PE.CO_PERIODO=M.CO_PERIODO ");
			consulta.append("                                                                           INNER JOIN  RHEI.ST_SOLICITUD_BEI ST "); 
			consulta.append("                                                                           ON ST.CO_STATUS=M.CO_STATUS ");
			consulta.append("                                                                  WHERE   S.nu_rif_proveedor=?");
			consulta.append("                                                                       AND   S.CO_EMPLEADO=? ");
			consulta.append("                                                                       AND   S.CEDULA_FAMILIAR = ? ");
			consulta.append("                                                                       ORDER BY S.NU_SOLICITUD DESC ");
			
			
			pstmt = con.prepareStatement(consulta.toString());
			pstmt.setString(1, solicitud.getNroRifCentroEdu());
			pstmt.setInt(2, solicitud.getCodigoEmpleado());
			pstmt.setInt(3, solicitud.getCedulaFamiliar());

			rs = pstmt.executeQuery();
			if ((rs.next())) {
				solicitud.setTextoPeriodo(rs.getString("TX_DESCRIP_PERIODO"));
				solicitud.setNroSolicitud(rs.getInt("NU_SOLICITUD"));
				solicitud.setCo_status(rs.getString("CO_STATUS"));
				solicitud.setNivelEscolar(rs.getString("IN_NIVEL_ESCOLAR"));
				solicitud.setNb_status(rs.getString("NB_STATUS"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return solicitud;
	}

	/**
	 * Chequeamos que la solicitus este activa
	 * 
	 * @param codEmplado
	 * @param codigoBenef
	 * @return
	 * @throws SQLException
	 */
	public boolean activaSolicitud(String codEmplado, String codigoBenef)
			throws SQLException {

		boolean isActiva = false;

		Solicitud solicitud = BscarSolConCodEmpCodBenf(codEmplado, codigoBenef);

		if (solicitud != null && !StringUtils.isEmpty(solicitud.getCo_status())
				&& !"D".equalsIgnoreCase(solicitud.getCo_status())) {
			isActiva = true;
		}
		solicitud = null;
		return isActiva;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bcv.dao.jdbc.SolicitudDao#buscarSolicitud(java.lang.String,
	 * java.lang.String, java.lang.String, int)
	 */
	public ArrayList<String> buscarSolicitud(String filtro, int codigoEmpleado,
			int cedulaFamiliar, int nroSolicitud) throws SQLException {
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Trabajador trab = new Trabajador();
		Beneficiario benef = new Beneficiario();
		String sql = "";
		@SuppressWarnings("rawtypes")
		ArrayList<String> registro = new ArrayList<String>();
		TrabajadorDao trabajadorDao = new TrabajadorDaoImpl();
		try {
			if (filtro.equals("cedula")) {
				registro.add(String.valueOf(codigoEmpleado));

				registro.add(String.valueOf(cedulaFamiliar));

				sql = "SELECT S.NU_SOLICITUD, INITCAP(NB_STATUS) AS NB_STATUS , DECODE(IN_NIVEL_ESCOLAR, '0', 'Maternal', '1','1° Nivel', '2','2° Nivel','3','3° Nivel','NO ASOCIADO') AS IN_NIVEL_ESCOLAR, PE.TX_DESCRIP_PERIODO FROM RHEI.SOLICITUD_BEI S, RHEI.MOV_ST_SOLIC_BEI M, RHEI.PERIODO_ESCOLAR PE, RHEI.ST_SOLICITUD_BEI ST WHERE S.NU_SOLICITUD = M.NU_SOLICITUD AND M.CO_PERIODO = PE.CO_PERIODO AND M.CO_STATUS     = ST.CO_STATUS AND S.NU_SOLICITUD = (SELECT MAX(NU_SOLICITUD) FROM RHEI.SOLICITUD_BEI WHERE CO_EMPLEADO = "
						+ codigoEmpleado
						+ " AND CEDULA_FAMILIAR = "
						+ cedulaFamiliar
						+ ") AND FE_STATUS = (SELECT MAX(FE_STATUS) FROM RHEI.SOLICITUD_BEI S1, RHEI.MOV_ST_SOLIC_BEI M1 WHERE S1.NU_SOLICITUD = M1.NU_SOLICITUD AND S1.NU_SOLICITUD = S.NU_SOLICITUD)";
				pstmt = con.prepareStatement(sql);

				rs = pstmt.executeQuery();
				if (rs.next()) {
					registro.add(String.valueOf(rs.getInt("NU_SOLICITUD")));
					log.debug("Valor del estatus de la solicitud: "
							+ rs.getString("NB_STATUS"));
					registro.add(rs.getString("NB_STATUS"));
					registro.add(rs.getString("IN_NIVEL_ESCOLAR"));
					registro.add(rs.getString("TX_DESCRIP_PERIODO"));
					if (!((String) registro.get(3))
							.equalsIgnoreCase("Desincorporado")) {
						log.debug("Aparece si el estatus es distinto a Desincorporado");

						trab.setCodigoEmpleado(new Integer(codigoEmpleado));
						trab.setTipoNomina(obtenerTipoNomina(con, pstmt,
								nroSolicitud));
						trab = trabajadorDao.buscarTrabajador("codigoEmpleado",
								trab.getCodigoEmpleado(), trab.getCedula(),
								trab.getTipoNomina());

						benef = beneficiarioDao.buscarBeneficiario(new Integer(
								cedulaFamiliar));

						registro.add(trab.getNombre());
						registro.add(trab.getSituacion());

						registro.add(benef.getNombre());
						registro.add(String.valueOf(beneficiarioDao
								.calcularEdad(benef.getFechaNacimento()
										.toString())));
						registro.add(String.valueOf(trab.getCedula()));
					} else {
						log.debug("Aparece si el estatus es Desincorporado");
						registro.clear();
					}
				} else {
					log.debug("Aparece si la dupla empleado-famiiar no tiene solicitud");
					registro.clear();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return registro;
	}

	/**
	 * @param con
	 * @param pstmt
	 * @param nroSolicitud
	 * @return
	 * @throws SQLException
	 */
	public String obtenerTipoNomina(Connection con, PreparedStatement pstmt,
			int nroSolicitud) throws SQLException {
		ResultSet rs = null;
		String sql = "";
		String tipoNomina = "";

		log.debug("Entré en el método buscarSolicitud con parámetros con, pstmt");
		sql = "SELECT TI_NOMINA AS TIPO_NOMINA FROM RHEI.SOLICITUD_BEI WHERE NU_SOLICITUD = ? ";

		pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, nroSolicitud);

		rs = pstmt.executeQuery();
		if (rs.next()) {
			tipoNomina = rs.getString("TIPO_NOMINA");
		}
		rs.close();
		return tipoNomina;
	}

	/**
	 * @param estatus
	 * @param beneficio
	 * @param parametro
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<String> buscarSolicitud(String estatus, String beneficio,
			String parametro) throws SQLException {
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean bandera = false;
		ArrayList<String> lista = new ArrayList<String>();
		String sql = "SELECT S.NU_SOLICITUD AS NU_SOLICITUD, E.CEDULA AS CEDULA , INITCAP(E.NOMBRE1||' '||E.APELLIDO1) AS NOMBRE_TRABAJADOR, E.CO_CIA_FISICA AS CO_CIA_FISICA, E.TIPO_EMP AS TIPO_EMP, S.CEDULA_FAMILIAR AS CEDULA_FAMILIAR, INITCAP(F.NOMBRE1||' '||F.APELLIDO1) AS NOMBRE_BENEFICIARIO, M.CO_STATUS AS CO_STATUS, INITCAP(T.NB_STATUS) AS NB_STATUS, M.CO_PERIODO AS CO_PERIODO, M.MO_PERIODO AS MO_PERIODO, M.MO_APORTE_BCV AS MONTO_BCV_ANTERIOR, P.TX_VALOR_PARAMETRO AS MONTO_BCV_NUEVO FROM  RHEI.SOLICITUD_BEI S INNER JOIN RHEI.MOV_ST_SOLIC_BEI M ON S.NU_SOLICITUD = M.NU_SOLICITUD INNER JOIN RHEI.ST_SOLICITUD_BEI T ON T.CO_STATUS = M.CO_STATUS INNER JOIN RHEI.PERIODO_ESCOLAR P ON P.CO_PERIODO = M.CO_PERIODO INNER JOIN PERSONAL.TODOS_EMPLEADOS E ON E.CODIGO_EMPLEADO = S.CO_EMPLEADO AND E.TIPO_NOMINA = S.TI_NOMINA INNER JOIN PERSONAL.FAMILIARES F ON F.CEDULA_FAMILIAR = S.CEDULA_FAMILIAR INNER JOIN RHEI.PARAMETRO P ON  P.CO_COMPANIA = E.CO_CIA_FISICA AND P.TIPO_EMP=E.TIPO_EMP WHERE S.NU_SOLICITUD  in (SELECT MAX(S1.NU_SOLICITUD) FROM RHEI.SOLICITUD_BEI S1 WHERE S1.NU_SOLICITUD = M.NU_SOLICITUD) AND FE_STATUS in   (SELECT MAX(M1.FE_STATUS) FROM RHEI.MOV_ST_SOLIC_BEI M1 WHERE M1.CO_PERIODO   = M.CO_PERIODO AND M1.NU_SOLICITUD = M.NU_SOLICITUD ) AND P.CO_PERIODO in (SELECT  CO_PERIODO  FROM RHEI.PERIODO_ESCOLAR WHERE IN_PERIODO_ESCOLAR = 'A' AND SYSDATE BETWEEN FE_INICIO AND FE_FIN) AND P.CO_TIPO_BENEFICIO = ? AND P.CO_PARAMETRO = ? AND P.FE_VIGENCIA  in (SELECT MAX(P2.FE_VIGENCIA) FROM RHEI.PARAMETRO P2 WHERE P2.CO_COMPANIA           = E.CO_CIA_FISICA AND P2.TIPO_EMP          = E.TIPO_EMP AND P2.CO_TIPO_BENEFICIO = P.CO_TIPO_BENEFICIO AND P2.CO_PARAMETRO      = P.CO_PARAMETRO AND P2.FE_VIGENCIA      <= SYSDATE ) GROUP BY S.NU_SOLICITUD, E.CEDULA, INITCAP(E.NOMBRE1||' '||E.APELLIDO1), E.CO_CIA_FISICA, E.TIPO_EMP, S.CEDULA_FAMILIAR, INITCAP(F.NOMBRE1||' '||F.APELLIDO1), M.CO_STATUS, T.NB_STATUS, M.CO_PERIODO, M.MO_PERIODO, M.MO_APORTE_BCV, P.TX_VALOR_PARAMETRO HAVING M.CO_STATUS != ? ORDER BY E.CEDULA ASC";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, beneficio);
			pstmt.setString(2, parametro);
			pstmt.setString(3, estatus);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				lista.add(String.valueOf(rs.getInt("NU_SOLICITUD")));
				lista.add(String.valueOf(rs.getInt("CEDULA")));
				lista.add(rs.getString("NOMBRE_TRABAJADOR").toLowerCase());
				lista.add(rs.getString("CO_CIA_FISICA"));
				lista.add(rs.getString("TIPO_EMP").toLowerCase());
				lista.add(String.valueOf(rs.getInt("CEDULA_FAMILIAR")));
				lista.add(rs.getString("NOMBRE_BENEFICIARIO").toLowerCase());
				lista.add(rs.getString("CO_STATUS"));
				lista.add(rs.getString("NB_STATUS"));
				lista.add(String.valueOf(rs.getInt("CO_PERIODO")));
				lista.add(String.valueOf(rs.getDouble("MO_PERIODO")));
				lista.add(String.valueOf(rs.getDouble("MONTO_BCV_ANTERIOR")));
				lista.add(String.valueOf(rs.getDouble("MONTO_BCV_NUEVO")));
				bandera = true;
			}
			if (!bandera) {
				log.debug("Aparece si la consulta no devuelve registros");
				lista.clear();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		log.debug("Tamaño lista :" + lista.size());
		return lista;
	}

	/**
	 * @param lista
	 * @return
	 */
	public String filtrarSolicitud(ArrayList<String> lista) {
		String listaFiltrada = "";
		if (!lista.isEmpty()) {
			for (int i = 0; i < lista.size(); i += 13) {
				listaFiltrada = listaFiltrada + (String) lista.get(i);
				listaFiltrada = listaFiltrada + "&";
				listaFiltrada = listaFiltrada + (String) lista.get(i + 12);
				if (i + 12 != lista.size() - 1) {
					listaFiltrada = listaFiltrada + "&";
				}
			}
		} else {
			log.debug("Aparece el parametro de entrada 'lista' está vacía");
			lista.clear();
		}
		log.debug("Lista filtrada: " + listaFiltrada);
		return listaFiltrada;
	}

	/**
	 * @param lista
	 * @return
	 */
	public String generadorTablaSolicitudes(ArrayList<String> lista) {
		log.debug("Tamaño lista :" + lista.size());
		String encabezado = "<tr><th width=\"90px\">C&eacute;dula</th><th width=\"142px\">Nombre Trabajador</th><th width=\"98px\">C&oacute;digo Beneficiario</th><th width=\"142px\">Nombre Beneficiario</th><th width=\"68px\">Monto Per&iacute;odo</th><th width=\"99px\">Monto Aporte BCV Actual</th><th width=\"62px\">Estatus Solicitud</th></tr>";

		String tabla = encabezado;
		if ((lista != null) && (!lista.isEmpty())) {
			for (int i = 0; i < lista.size(); i += 13) {
				tabla = tabla + "<tr><td >" + (String) lista.get(i + 1)
						+ "</td>" + "<td>" + (String) lista.get(i + 2)
						+ "</td>" + "<td >" + (String) lista.get(i + 5)
						+ "</td>" + "<td>" + (String) lista.get(i + 6)
						+ "</td>" + "<td >" + (String) lista.get(i + 10)
						+ "</td>" + "<td >" + (String) lista.get(i + 11)
						+ "</td>" + "<td>" + (String) lista.get(i + 8)
						+ "</td></tr>";
			}
			tabla = tabla + "</table>";
		} else if (lista.isEmpty()) {
			log.debug("No se encontraron solicitudes aptas para actualizar el monto aporte máximo posible BCV");

			tabla = "No hay registros de solicitudes aptas para actualizar el monto aporte máximo posible BCV";
			log.debug("Mensaje contiene:" + tabla);
		}
		return tabla;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bcv.dao.jdbc.SolicitudDao#verificarNroSolicitud(int)
	 */
	public int verificarNroSolicitud(int nroSolicitud) throws SQLException {

		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int codEmpleado = 0;
		int cedFamiliar = 0;
		int nroSolicitudMax = 0;
		String sql = "";
		try {

			sql = "SELECT CO_EMPLEADO, CEDULA_FAMILIAR FROM RHEI.SOLICITUD_BEI WHERE NU_SOLICITUD = ? ";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, nroSolicitud);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				codEmpleado = rs.getInt("CO_EMPLEADO");
				cedFamiliar = rs.getInt("CEDULA_FAMILIAR");
			}

			sql = "SELECT NU_SOLICITUD FROM RHEI.SOLICITUD_BEI WHERE NU_SOLICITUD =(SELECT MAX(NU_SOLICITUD) FROM RHEI.SOLICITUD_BEI WHERE CO_EMPLEADO = ? AND CEDULA_FAMILIAR = ?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, codEmpleado);
			pstmt.setInt(2, cedFamiliar);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				nroSolicitudMax = rs.getInt("NU_SOLICITUD");
				if (nroSolicitudMax > nroSolicitud) {
					nroSolicitud = nroSolicitudMax;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return nroSolicitud;
	}

	public int obtenerNumeroSolicitud(int codigoEmpleado, int cedulaFamiliar)
			throws SQLException {
		Connection con = manejadorDB.coneccionPool();
		int nroSolicitud = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			sql = "SELECT S.NU_SOLICITUD FROM RHEI.SOLICITUD_BEI S WHERE S.NU_SOLICITUD =(SELECT MAX(NU_SOLICITUD) FROM RHEI.SOLICITUD_BEI WHERE CO_EMPLEADO = ? AND CEDULA_FAMILIAR = ?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, codigoEmpleado);
			pstmt.setInt(2, cedulaFamiliar);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				nroSolicitud = rs.getInt("NU_SOLICITUD");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return nroSolicitud;
	}

	/**
	 * @param solicitante
	 * @param periodo
	 * @param codigoEmpleado
	 * @param cedulaFamiliar
	 * @throws SQLException
	 */
	public int obtenerNumeroSolicitud(String solicitante, String periodo,
			int codigoEmpleado, int cedulaFamiliar) throws SQLException {
		int nroSolicitud = 0;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		if ((solicitante.equalsIgnoreCase("registrarPago"))
				|| (solicitante.equalsIgnoreCase("actualizarPago"))
				|| (solicitante.equalsIgnoreCase("registrarPagoNoConvencional"))
				|| (solicitante
						.equalsIgnoreCase("actualizarPagoNoConvencional"))) {
			sql = "SELECT S.NU_SOLICITUD FROM RHEI.SOLICITUD_BEI S WHERE S.NU_SOLICITUD = (SELECT MAX(S1.NU_SOLICITUD) FROM RHEI.SOLICITUD_BEI S1 INNER JOIN RHEI.MOV_ST_SOLIC_BEI M ON S1.NU_SOLICITUD = M.NU_SOLICITUD WHERE S1.CO_EMPLEADO = ? AND S1.CEDULA_FAMILIAR = ? AND M.CO_PERIODO = (SELECT M1.CO_PERIODO FROM RHEI.MOV_ST_SOLIC_BEI M1 WHERE M1.NU_SOLICITUD =S1.NU_SOLICITUD AND M1.FE_STATUS = (SELECT MAX(M2.FE_STATUS) FROM RHEI.MOV_ST_SOLIC_BEI M2 WHERE M2.NU_SOLICITUD = M1.NU_SOLICITUD)))";
		} else if ((solicitante.equalsIgnoreCase("consultarPago"))
				|| (solicitante.equalsIgnoreCase("consultarPagoNoConvencional"))) {
			sql =

			"SELECT S.NU_SOLICITUD FROM RHEI.SOLICITUD_BEI S WHERE S.NU_SOLICITUD =(SELECT MAX(S1.NU_SOLICITUD) FROM RHEI.SOLICITUD_BEI S1 INNER JOIN RHEI.MOV_ST_SOLIC_BEI M ON S1.NU_SOLICITUD = M.NU_SOLICITUD WHERE S1.CO_EMPLEADO = ? AND S1.CEDULA_FAMILIAR = ? AND M.CO_PERIODO ="
					+ Integer.parseInt(periodo) + ")";
		}
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, codigoEmpleado);
			pstmt.setInt(2, cedulaFamiliar);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				nroSolicitud = rs.getInt("NU_SOLICITUD");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return nroSolicitud;
	}

	public int obtenerCodigoEmpleado(int nroSolicitud) throws SQLException {
		int codigoEmpleado = 0;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			sql = "SELECT CO_EMPLEADO FROM  RHEI.SOLICITUD_BEI WHERE NU_SOLICITUD = ? ";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, nroSolicitud);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				codigoEmpleado = rs.getInt("CO_EMPLEADO");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return codigoEmpleado;
	}

	public int obtenerCedulaEmpleado(int nroSolicitud) throws SQLException {
		int cedula = 0;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			sql = "SELECT CEDULA FROM PERSONAL.TODOS_EMPLEADOS E INNER JOIN RHEI.SOLICITUD_BEI S ON E.CODIGO_EMPLEADO  = S.CO_EMPLEADO AND E.TIPO_NOMINA = S.TI_NOMINA WHERE S.NU_SOLICITUD = ? ";

			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, nroSolicitud);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				cedula = rs.getInt("CEDULA");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return cedula;
	}

	public int obtenerCodigoFamiliar(int nroSolicitud) throws SQLException {
		int cedulaFamiliar = 0;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;

		ResultSet rs = null;

		String sql = "";
		try {
			sql = "SELECT CEDULA_FAMILIAR FROM RHEI.SOLICITUD_BEI WHERE NU_SOLICITUD = ? ";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, nroSolicitud);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				cedulaFamiliar = rs.getInt("CEDULA_FAMILIAR");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return cedulaFamiliar;
	}

	public Solicitud buscarSolicitud(int nroSolicitud) throws SQLException {
		Solicitud solicitud = null;
		String sql = "";
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			sql = "SELECT FE_SOLICITUD, CO_EMPLEADO, CEDULA_FAMILIAR,  NU_RIF_PROVEEDOR, IN_LOCALIDAD_CEI, IN_TP_EDUCACION, IN_PROV_CONVENIDO, IN_PERIOD_PAGO, CO_STATUS, FE_STATUS, IN_NIVEL_ESCOLAR, IN_BENEF_COMPART, IN_TIPO_EMPRESA, MO_APORTE_BCV, MO_PERIODO, MO_MATRICULA, MO_EMPRESA_REPRES, CO_PERIODO FROM RHEI.SOLICITUD_BEI S, RHEI.MOV_ST_SOLIC_BEI M WHERE S.NU_SOLICITUD = M.NU_SOLICITUD AND S.NU_SOLICITUD = ? AND M.FE_STATUS = (SELECT MAX(M1.FE_STATUS) FROM RHEI.MOV_ST_SOLIC_BEI M1 WHERE M1.NU_SOLICITUD = M.NU_SOLICITUD AND M1.CO_STATUS!='D')";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, nroSolicitud);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				solicitud = new Solicitud();
				solicitud.setCodigoEmpleado(Integer.parseInt(rs
						.getString("CO_EMPLEADO")));
				solicitud.setCedulaFamiliar(Integer.parseInt(rs
						.getString("CEDULA_FAMILIAR")));

				solicitud.setFechaSolicitudOriginal(rs
						.getTimestamp("FE_SOLICITUD"));
				solicitud.setNroRifCentroEdu(rs.getString("NU_RIF_PROVEEDOR"));
				solicitud.setLocalidadCEI(rs.getString("IN_LOCALIDAD_CEI"));
				solicitud.setTipoEducacion(rs.getString("IN_TP_EDUCACION"));
				solicitud.setNivelEscolar(rs.getString("IN_NIVEL_ESCOLAR"));
				solicitud.setTipoCentroEdu(rs.getString("IN_PROV_CONVENIDO"));
				solicitud.setPeriodoDePago(rs.getString("IN_PERIOD_PAGO"));
				solicitud.setMontoAporteBCV(Double.valueOf(Double
						.parseDouble(rs.getString("MO_APORTE_BCV"))));
				solicitud.setMontoPeriodo(Double.valueOf(Double.parseDouble(rs
						.getString("MO_PERIODO"))));
				solicitud.setMontoMatricula(Double.valueOf(Double
						.parseDouble(rs.getString("MO_MATRICULA"))));
				solicitud
						.setComparteBeneficio(rs.getString("IN_BENEF_COMPART"));
				solicitud.setTipoEmpresa(rs.getString("IN_TIPO_EMPRESA"));
				solicitud.setMontoEmpresa(Double.valueOf(Double.parseDouble(rs
						.getString("MO_EMPRESA_REPRES"))));
				solicitud.setCodigoPeriodo(rs.getInt("CO_PERIODO"));

				solicitud.setFechaSolicitud(rs.getTimestamp("FE_STATUS"));
				solicitud.setCo_status(rs.getString("CO_STATUS"));
			}
			rs.close();
			solicitud.setTextoPeriodo(buscarFormatoDelPeriodo(con, pstmt,
					solicitud.getCodigoPeriodo()));
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			liberarConexion(rs, pstmt, con);
		}

		return solicitud;
	}

	public String buscarFormatoDelPeriodo(Connection con,
			PreparedStatement pstmt, int codigoPeriodo) throws SQLException {
		String sql = "";
		String formato = "";
		ResultSet rs = null;
		sql = "SELECT TX_DESCRIP_PERIODO FROM RHEI.PERIODO_ESCOLAR WHERE CO_PERIODO = ?";

		pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, codigoPeriodo);

		rs = pstmt.executeQuery();
		if (rs.next()) {
			log.debug("Seteando el miembro textoPeriodo del objeto solicitud");
			formato = rs.getString("TX_DESCRIP_PERIODO");
		}
		rs.close();
		log.debug("Saliendo del método buscarFormatoDelPeriodo con parámetros con, pstmt,codigoPeriodo");
		return formato;
	}

	public Solicitud buscarSolicitud(String operacion, int nroSolicitud)
			throws SQLException {
		String sql = "";
		Solicitud solicitud = null;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			if (operacion.equals("consultar")) {
				sql = "SELECT FE_SOLICITUD, CO_EMPLEADO, CEDULA_FAMILIAR,  NU_RIF_PROVEEDOR, IN_LOCALIDAD_CEI, IN_TP_EDUCACION, IN_PROV_CONVENIDO, IN_PERIOD_PAGO, CO_STATUS, FE_STATUS, IN_NIVEL_ESCOLAR, IN_BENEF_COMPART, IN_TIPO_EMPRESA, MO_APORTE_BCV, MO_PERIODO, MO_MATRICULA, MO_EMPRESA_REPRES, M.CO_PERIODO, P.TX_DESCRIP_PERIODO FROM RHEI.SOLICITUD_BEI S, RHEI.MOV_ST_SOLIC_BEI M, RHEI.PERIODO_ESCOLAR P WHERE S.NU_SOLICITUD = M.NU_SOLICITUD AND S.NU_SOLICITUD = ? AND M.CO_PERIODO = P.CO_PERIODO                        AND M.FE_STATUS = (SELECT MAX(M1.FE_STATUS) FROM RHEI.MOV_ST_SOLIC_BEI M1 WHERE M1.NU_SOLICITUD = M.NU_SOLICITUD)";
			}
			if ((operacion.equals("desincorporar"))
					|| (operacion.equals("actualizar"))) {
				sql = "SELECT FE_SOLICITUD, CO_EMPLEADO, CEDULA_FAMILIAR,  NU_RIF_PROVEEDOR, IN_LOCALIDAD_CEI, IN_TP_EDUCACION, IN_PROV_CONVENIDO, IN_PERIOD_PAGO, CO_STATUS, FE_STATUS, IN_NIVEL_ESCOLAR, IN_BENEF_COMPART, IN_TIPO_EMPRESA, MO_APORTE_BCV, MO_PERIODO, MO_MATRICULA, MO_EMPRESA_REPRES, M.CO_PERIODO, P.TX_DESCRIP_PERIODO FROM RHEI.SOLICITUD_BEI S, RHEI.MOV_ST_SOLIC_BEI M, RHEI.PERIODO_ESCOLAR P WHERE S.NU_SOLICITUD = M.NU_SOLICITUD AND S.NU_SOLICITUD = ? AND M.CO_PERIODO = P.CO_PERIODO AND M.CO_STATUS != 'D' AND M.FE_STATUS = (SELECT MAX(M1.FE_STATUS) FROM RHEI.MOV_ST_SOLIC_BEI M1 WHERE M1.NU_SOLICITUD = M.NU_SOLICITUD AND M1.CO_STATUS != 'D')";
			}

			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, nroSolicitud);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				solicitud = new Solicitud();
				solicitud.setNroSolicitud(nroSolicitud);
				solicitud.setCodigoEmpleado(Integer.parseInt(rs
						.getString("CO_EMPLEADO")));
				solicitud.setCedulaFamiliar(Integer.parseInt(rs
						.getString("CEDULA_FAMILIAR")));

				solicitud.setFechaSolicitudOriginal(rs
						.getTimestamp("FE_SOLICITUD"));
				solicitud.setNroRifCentroEdu(rs.getString("NU_RIF_PROVEEDOR"));
				solicitud.setTipoEducacion(rs.getString("IN_TP_EDUCACION"));
				solicitud.setNivelEscolar(rs.getString("IN_NIVEL_ESCOLAR"));
				solicitud.setTipoCentroEdu(rs.getString("IN_PROV_CONVENIDO"));
				solicitud.setPeriodoDePago(rs.getString("IN_PERIOD_PAGO"));

				solicitud.setMontoAporteBCV(Double.valueOf(Double
						.parseDouble(rs.getString("MO_APORTE_BCV"))));

				solicitud.setMontoPeriodo(Double.valueOf(Double.parseDouble(rs
						.getString("MO_PERIODO"))));
				solicitud.setMontoMatricula(Double.valueOf(Double
						.parseDouble(rs.getString("MO_MATRICULA"))));
				solicitud
						.setComparteBeneficio(rs.getString("IN_BENEF_COMPART"));
				solicitud.setTipoEmpresa(rs.getString("IN_TIPO_EMPRESA"));
				solicitud.setMontoEmpresa(Double.valueOf(Double.parseDouble(rs
						.getString("MO_EMPRESA_REPRES"))));

				solicitud.setFechaSolicitud(rs.getTimestamp("FE_STATUS"));
				solicitud.setCo_status(rs.getString("CO_STATUS"));
				solicitud.setCodigoPeriodo(rs.getInt("CO_PERIODO"));
				solicitud.setTextoPeriodo(rs.getString("TX_DESCRIP_PERIODO"));
				solicitud.setLocalidadCEI(rs.getString("IN_LOCALIDAD_CEI"));
			}
			rs.close();

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			liberarConexion(rs, pstmt, con);
		}

		return solicitud;
	}
	
	
	public int updateColegioSolicitud(String numRif ,int nroSolicitud){
		int resultado=-1;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder("");;
		try {
			con = manejadorDB.coneccionPool();
			sql.append("UPDATE RHEI.SOLICITUD_BEI SET NU_RIF_PROVEEDOR=? ");
			sql.append(" WHERE NU_SOLICITUD=? ");
			pstmt = con.prepareStatement(sql.toString());
			int posicion=1;
			pstmt.setString(posicion++, numRif);
			pstmt.setInt(posicion++, nroSolicitud);
			resultado=pstmt.executeUpdate();
		}catch (SQLException e) {
				e.printStackTrace();
			} finally {
				liberarConexion(rs, pstmt, con);
			}
		return resultado;
	}

	/**
	 * Consultamos la solicitud
	 * 
	 * @throws SQLException
	 */
	public Solicitud consultarSolicitud(int nroSolicitud) throws SQLException {
		Solicitud solicitud = null;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {

			sql = "SELECT S.NU_SOLICITUD,FE_SOLICITUD, CO_EMPLEADO, CEDULA_FAMILIAR,  NU_RIF_PROVEEDOR, IN_LOCALIDAD_CEI, IN_TP_EDUCACION, IN_PROV_CONVENIDO, IN_PERIOD_PAGO, M.CO_STATUS,ST.NB_STATUS, FE_STATUS, IN_NIVEL_ESCOLAR, IN_BENEF_COMPART, IN_TIPO_EMPRESA, MO_APORTE_BCV, MO_PERIODO, MO_MATRICULA, MO_EMPRESA_REPRES, M.CO_PERIODO, P.TX_DESCRIP_PERIODO FROM RHEI.SOLICITUD_BEI S, RHEI.MOV_ST_SOLIC_BEI M, RHEI.ST_SOLICITUD_BEI ST, RHEI.PERIODO_ESCOLAR P WHERE S.NU_SOLICITUD = M.NU_SOLICITUD AND S.NU_SOLICITUD = ? AND M.CO_PERIODO = P.CO_PERIODO  AND ST.CO_STATUS=M.CO_STATUS                      AND M.FE_STATUS = (SELECT MAX(M1.FE_STATUS) FROM RHEI.MOV_ST_SOLIC_BEI M1 WHERE M1.NU_SOLICITUD = M.NU_SOLICITUD)";

			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, nroSolicitud);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				solicitud = new Solicitud();

				solicitud.setNroSolicitud(Integer.parseInt(rs
						.getString("NU_SOLICITUD")));
				solicitud.setCodigoEmpleado(Integer.parseInt(rs
						.getString("CO_EMPLEADO")));
				solicitud.setCedulaFamiliar(Integer.parseInt(rs
						.getString("CEDULA_FAMILIAR")));
				solicitud.setFechaSolicitudOriginal(rs
						.getTimestamp("FE_SOLICITUD"));
				solicitud.setNroRifCentroEdu(rs.getString("NU_RIF_PROVEEDOR"));
				solicitud.setTipoEducacion(rs.getString("IN_TP_EDUCACION"));
				solicitud.setNivelEscolar(rs.getString("IN_NIVEL_ESCOLAR"));
				solicitud.setTipoCentroEdu(rs.getString("IN_PROV_CONVENIDO"));
				solicitud.setPeriodoDePago(rs.getString("IN_PERIOD_PAGO"));

				solicitud.setMontoAporteBCV(Double.valueOf(Double
						.parseDouble(rs.getString("MO_APORTE_BCV"))));

				solicitud.setMontoPeriodo(Double.valueOf(Double.parseDouble(rs
						.getString("MO_PERIODO"))));
				solicitud.setMontoMatricula(Double.valueOf(Double
						.parseDouble(rs.getString("MO_MATRICULA"))));
				solicitud
						.setComparteBeneficio(rs.getString("IN_BENEF_COMPART"));
				solicitud.setTipoEmpresa(rs.getString("IN_TIPO_EMPRESA"));
				solicitud.setMontoEmpresa(Double.valueOf(Double.parseDouble(rs
						.getString("MO_EMPRESA_REPRES"))));

				solicitud.setFechaSolicitud(rs.getTimestamp("FE_STATUS"));
				solicitud.setNb_status(rs.getString("NB_STATUS"));
				solicitud.setCo_status(rs.getString("CO_STATUS"));
				solicitud.setCodigoPeriodo(rs.getInt("CO_PERIODO"));
				solicitud.setTextoPeriodo(rs.getString("TX_DESCRIP_PERIODO"));
				solicitud.setLocalidadCEI(rs.getString("IN_LOCALIDAD_CEI"));
			}
		}

		catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return solicitud;
	}

	public HttpServletRequest cargarAtributosSolicitud(
			HttpServletRequest request, String operacion, Solicitud solicitud) {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		request.setAttribute("nroSolicitud",
				String.valueOf(solicitud.getNroSolicitud()));
		request.setAttribute("fechaOriginal",
				format.format(solicitud.getFechaSolicitudOriginal()).toString());
		log.debug("fechaOriginal: " + solicitud.getFechaSolicitudOriginal());
		request.setAttribute("statusSol",
				String.valueOf(solicitud.getCo_status()));
		request.setAttribute("nroRif", solicitud.getNroRifCentroEdu());
		request.setAttribute("tipoInst", solicitud.getTipoCentroEdu());

		request.setAttribute("tipoEducacion", solicitud.getTipoEducacion());
		request.setAttribute("nivelEscolar", solicitud.getNivelEscolar());
		request.setAttribute("periodoPago", solicitud.getPeriodoDePago());
		request.setAttribute("formaPago", solicitud.getFormaDePago());
		request.setAttribute("montoPeriodo", solicitud.getMontoPeriodo());
		request.setAttribute("montoMatricula", solicitud.getMontoMatricula());
		if ((operacion.equals("consultar"))
				|| (operacion.equals("consultarPago"))
				|| (operacion.equals("actualizarPago"))
				|| (operacion.equals("registrarPago"))
				|| (operacion.equals("actualizarPagoNoConvencional"))
				|| (operacion.equals("registrarPagoNoConvencional"))) {
			request.setAttribute("montoBCV", solicitud.getMontoAporteBCV());
		}
		request.setAttribute("co_status",
				String.valueOf(solicitud.getCo_status()));
		request.setAttribute("fechaActual",
				format.format(solicitud.getFechaSolicitud()).toString());
		request.setAttribute("benefCompartido",
				solicitud.getComparteBeneficio());
		request.setAttribute("tipoEmpresa", solicitud.getTipoEmpresa());
		request.setAttribute("montoEmpresa", solicitud.getMontoEmpresa());
		request.setAttribute("codigoPeriodo",
				Integer.valueOf(solicitud.getCodigoPeriodo()));
		request.setAttribute("formatoPeriodo", solicitud.getTextoPeriodo());
		if ((operacion.equals("consultar")) || (operacion.equals("actualizar"))) {
			if (solicitud.getLocalidadCEI().compareToIgnoreCase("C") == 0) {
				request.setAttribute("localidadCEI", "Caracas");
			} else if (solicitud.getLocalidadCEI().compareToIgnoreCase("M") == 0) {
				request.setAttribute("localidadCEI", "Maracaibo");
			} else if (solicitud.getLocalidadCEI().compareToIgnoreCase("Y") == 0) {
				request.setAttribute("localidadCEI", "Maracay");
			}
		}
		return request;
	}

	public String guardarSolicitud(String siglaExpediente, String tipoNomina,
			int codigoEmpleado, int cedulaFamiliar, String formaDePago,
			String nroRifCentroEdu, String tipoEducacion, String tipoCentroEdu,
			String periodoDePago, String localidadCEI, String co_status,
			String nivelEscolar, String comparteBeneficio, String tipoEmpresa,
			Double montoAporteBCV, Double montoPeriodo, Double montoMatricula,
			Double montoEmpresa, int codigoPeriodo,ConyugeTrabajo conyugeTrabajo) throws SQLException {
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		
		if (StringUtils.isEmpty(comparteBeneficio)){
			comparteBeneficio="N";
		}
		
		ResultSet rs = null;
		String sql = "";
		String moneda = "044";
		String mensaje = "";
		int resultado = 0;

		try {

			sql = "SELECT CO_EMPLEADO FROM RHEI.BENEFICIARIO_EI WHERE CO_EMPLEADO = ? AND CEDULA_FAMILIAR = ? ";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, codigoEmpleado);
			pstmt.setInt(2, cedulaFamiliar);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				resultado++;
				log.debug("Valor de la variable resultado: " + resultado);
			} else {
				sql = "INSERT INTO RHEI.BENEFICIARIO_EI (CO_EMPLEADO, CEDULA_FAMILIAR) VALUES (?,?)";

				pstmt = con.prepareStatement(sql);

				pstmt.setInt(1, codigoEmpleado);
				pstmt.setInt(2, cedulaFamiliar);

				resultado += pstmt.executeUpdate();
			}
			if (resultado == 1) {
				log.debug("El registro fue insertado en la tabla RHEI.BENEFICIARIO_EI o ya existía en la misma");
				sql = "SELECT CO_EMPLEADO, CO_SIGLA_EXPED TI_NOMINA FROM RHEI.SIGLA_EXPED_BEI WHERE CO_EMPLEADO=? AND TI_NOMINA = ? ";
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, codigoEmpleado);
				pstmt.setString(2, tipoNomina);
				rs = pstmt.executeQuery();
				if (!rs.next()) {
					sql = "INSERT INTO RHEI.SIGLA_EXPED_BEI (CO_EMPLEADO, CO_SIGLA_EXPED, TI_NOMINA) VALUES (?,?,?)";
					pstmt = con.prepareStatement(sql);
					pstmt.setInt(1, codigoEmpleado);
					pstmt.setString(2, siglaExpediente);
					pstmt.setString(3, tipoNomina);

					resultado += pstmt.executeUpdate();
				} else {
					resultado++;
				}
				if (resultado == 2) {
					sql = "INSERT INTO RHEI.SOLICITUD_BEI (NU_SOLICITUD, FE_SOLICITUD, CO_EMPLEADO, CEDULA_FAMILIAR,  NU_RIF_PROVEEDOR, IN_TP_EDUCACION, IN_PROV_CONVENIDO, IN_PERIOD_PAGO, IN_LOCALIDAD_CEI, TI_NOMINA) VALUES (RHEI.SEQ_NU_SOLICITUD_BEI.NEXTVAL,SYSDATE,?,?,?,?,?,?,?,?)";
					pstmt = con.prepareStatement(sql);
					int posi = 1;
					pstmt.setInt(posi++, codigoEmpleado);
					pstmt.setInt(posi++, cedulaFamiliar);
					pstmt.setString(posi++, nroRifCentroEdu);
					pstmt.setString(posi++, tipoEducacion!=null?tipoEducacion:"R");
					pstmt.setString(posi++, tipoCentroEdu!=null?tipoCentroEdu:"N");
					pstmt.setString(posi++, periodoDePago!=null?periodoDePago:"1");
					
					pstmt.setString(posi++, localidadCEI);
					pstmt.setString(posi++, tipoNomina);

					resultado += pstmt.executeUpdate();
					log.debug("Valor de la variable resultado: " + resultado);
					if (resultado == 3) {
						
						pstmt = con.prepareStatement("SELECT RHEI.SEQ_NU_SOLICITUD_BEI.CURRVAL AS valor from dual");
								ResultSet result = pstmt.executeQuery();
								int nuSolicitud = 0;
								if (result != null) {
								while (result.next()) {
									nuSolicitud = result.getInt("valor");
								}
								}
						
						
						
						log.debug("El registro fue insertado en la tabla RHEI.SOLICITUD_BEI");
						sql = "INSERT INTO RHEI.MOV_ST_SOLIC_BEI (NU_SOLICITUD, CO_STATUS, FE_STATUS, IN_NIVEL_ESCOLAR, IN_BENEF_COMPART, IN_TIPO_EMPRESA, MO_APORTE_BCV, MO_PERIODO, MO_MATRICULA, MO_EMPRESA_REPRES, CO_MONEDA, CO_PERIODO) VALUES (?,?,SYSDATE,?,?,?,?,?,?,?,?,?)";

						pstmt = con.prepareStatement(sql);
						posi = 1;
						pstmt.setInt(posi++, nuSolicitud);
						pstmt.setString(posi++, co_status);
						pstmt.setString(posi++, nivelEscolar);
						pstmt.setString(posi++, comparteBeneficio);
						pstmt.setString(posi++, tipoEmpresa);
						pstmt.setDouble(posi++, montoAporteBCV.doubleValue());
						pstmt.setDouble(posi++, montoPeriodo.doubleValue());
						pstmt.setDouble(posi++, montoMatricula.doubleValue());
						pstmt.setDouble(posi++, montoEmpresa.doubleValue());
						pstmt.setString(posi++, moneda);
						pstmt.setInt(posi++, codigoPeriodo);

						resultado += pstmt.executeUpdate();
						log.debug("Variable resultado :" + resultado);
						if (resultado == 4) {
							 /**Insertamos datos extras del conyuge*/
							conyugeTrabajoDao.guardar(conyugeTrabajo.getCiConyuge(), conyugeTrabajo.getNombreEmpresa(), conyugeTrabajo.getTelefonoEmpresa(), conyugeTrabajo.getCorreoConyuge(), conyugeTrabajo.getNuTlfTrabajo());
							
							resultado +=1;// pstmt.executeUpdate();
							if (resultado == 5) {
							mensaje = "Se le ha registrado el beneficio al familiar con código "
									+ cedulaFamiliar
									+ " del empleado con código "
									+ codigoEmpleado;
							}

						} else {
							log.debug("Problemas en la inserción del registro en la tabla RHEI.MOV_ST_SOLIC_BEI");
						}
					} else {
						log.debug("Problemas en la inserción en la tabla RHEI.SOLICITUD_BEI");
					}
				} else {
					log.debug("Ya el trabajador tiene abierto un expediente");
				}
			} else {
				log.debug("Ya el trabajador tiene una solicitud para ese familiar");
			}
		} catch (SQLException e) {
			try {
				/**
				 * NU_RIF_PROVEEDOR   VARCHAR2(14 CHAR)          NOT NULL,
                  IN_TP_EDUCACION    VARCHAR2(1 CHAR)           DEFAULT 'R'                   NOT NULL,
  IN_PROV_CONVENIDO  VARCHAR2(1 CHAR)           DEFAULT 'N'                   NOT NULL,
  IN_PERIOD_PAGO     VARCHAR2(1 CHAR)           DEFAULT '1'                   NOT NULL,
  IN_LOCALIDAD_CEI   VARCHAR2(1 CHAR)           DEFAULT 'C'                   NOT NULL,
  TI_NOMINA          VARCHAR2(3 CHAR)           DEFAULT 'REG'                 NOT NULL
				 * **/
				e.printStackTrace();
				log.debug("Ocurrió un error en alguna de las consultas");
				mensaje = "Error en inserciones";
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return mensaje;
	}

	public String updateCambiosEnSolicitud(String co_status,
			String nivelEscolar, Double montoPeriodo, Double montoMatricula,
			int codigoPeriodo, String comparteBeneficio, String tipoEmpresa,
			Double montoEmpresa, int nroSolicitud) throws SQLException {
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		String moneda = "044";
		String mensaje = "";
		int resultado = 0;
		try {
			log.debug("Estoy dentro del método guardarCambiosEnSolicitud....");

			sql = "UPDATE RHEI.MOV_ST_SOLIC_BEI SET   CO_STATUS=?, IN_NIVEL_ESCOLAR=?,    MO_PERIODO=?, MO_MATRICULA=?,  CO_PERIODO=?, IN_BENEF_COMPART=?, IN_TIPO_EMPRESA=?, MO_EMPRESA_REPRES =?  WHERE NU_SOLICITUD=?";

			pstmt = con.prepareStatement(sql);
			
		 
			if (StringUtils.isEmpty(comparteBeneficio)){
				comparteBeneficio="N";
			}

			pstmt.setString(1, co_status);
			pstmt.setString(2, nivelEscolar);
			pstmt.setDouble(3, montoPeriodo.doubleValue());
			pstmt.setDouble(4, montoMatricula.doubleValue());
			pstmt.setInt(5, codigoPeriodo);
			pstmt.setString(6, comparteBeneficio);
			pstmt.setString(7, tipoEmpresa);
			pstmt.setDouble(8, montoEmpresa.doubleValue());
			pstmt.setInt(9, nroSolicitud);
			resultado = pstmt.executeUpdate();
			if (resultado == 1) {
				log.debug("Las actualizaciones fueron insertadas en la tabla RHEI.MOV_ST_SOLIC_BEI");
				mensaje = "Se le ha registrado los cambios a la solicitud Nro. "
						+ nroSolicitud + " del beneficio de educación inicial";
			} else {
				log.debug("Ocurrió un problema en la inserción de la actualización de la solicitud en la tabla RHEI.MOV_ST_SOLIC_BEI");
			}
		} catch (SQLException e1) {
			try {
				log.debug("Ocurrió un error en la inserción");
				mensaje = "Error en la insercion";
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			e1.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return mensaje;
	}

	public String guardarCambiosEnSolicitud(int nroSolicitud, String co_status,
			String nivelEscolar, String comparteBeneficio, String tipoEmpresa,
			Double montoAporteBCV, Double montoPeriodo, Double montoMatricula,
			Double montoEmpresa, int codigoPeriodo) throws SQLException {
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		String moneda = "044";
		String mensaje = "";
		int resultado = 0;
		try {
			log.debug("Estoy dentro del método guardarCambiosEnSolicitud....");

			sql = "INSERT INTO RHEI.MOV_ST_SOLIC_BEI (NU_SOLICITUD, CO_STATUS, FE_STATUS,IN_NIVEL_ESCOLAR, IN_BENEF_COMPART, IN_TIPO_EMPRESA, MO_APORTE_BCV,MO_PERIODO, MO_MATRICULA, MO_EMPRESA_REPRES, CO_MONEDA, CO_PERIODO) VALUES (?,?,SYSDATE,?,?,?,?,?,?,?,?,?)";
			if (StringUtils.isEmpty(comparteBeneficio)){
				comparteBeneficio="N";
			}
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, nroSolicitud);
			pstmt.setString(2, co_status);
			pstmt.setString(3, nivelEscolar);
			pstmt.setString(4, comparteBeneficio);
			pstmt.setString(5, tipoEmpresa);
			pstmt.setDouble(6, montoAporteBCV.doubleValue());
			pstmt.setDouble(7, montoPeriodo.doubleValue());
			pstmt.setDouble(8, montoMatricula.doubleValue());
			pstmt.setDouble(9, montoEmpresa.doubleValue());
			pstmt.setString(10, moneda);
			pstmt.setInt(11, codigoPeriodo);
			resultado = pstmt.executeUpdate();
			if (resultado == 1) {
				log.debug("Las actualizaciones fueron insertadas en la tabla RHEI.MOV_ST_SOLIC_BEI");
				mensaje = "Se le ha registrado los cambios a la solicitud Nro. "
						+ nroSolicitud + " del beneficio de educación inicial";
			} else {
				log.debug("Ocurrió un problema en la inserción de la actualización de la solicitud en la tabla RHEI.MOV_ST_SOLIC_BEI");
			}
		} catch (SQLException e1) {
			try {
				log.debug("Ocurrió un error en la inserción");
				mensaje = "Error en la insercion";
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			e1.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return mensaje;
	}

	public String guardarCambiosEnMovStSolicBei(String co_status,
			String nivelEscolar, Double montoPeriodo, Double montoMatricula,
			int codigoPeriodo, String comparteBeneficio, String tipoEmpresa,
			Double montoEmpresa, int nroSolicitud) throws SQLException {
		return updateCambiosEnSolicitud(co_status, nivelEscolar, montoPeriodo,
				montoMatricula, codigoPeriodo, comparteBeneficio, tipoEmpresa,
				montoEmpresa, nroSolicitud);
	}

	public String updateCambiosEnSolicitudBEI(String formaDePago,
			String nroRifCentroEdu, String tipoEducacion, String tipoCentroEdu,
			String periodoDePago, int nroSolicitud) throws SQLException {
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;

		ResultSet rs = null;
		String sql = "";
		String mensaje = "";
		int resultado = 0;

		try {

			sql = "UPDATE RHEI.SOLICITUD_BEI SET   NU_RIF_PROVEEDOR =? , IN_TP_EDUCACION =?, IN_PROV_CONVENIDO=?, IN_PERIOD_PAGO=?  WHERE NU_SOLICITUD=?";
			pstmt = con.prepareStatement(sql);
			int posic = 1;
			pstmt.setString(posic++, nroRifCentroEdu);
			pstmt.setString(posic++, tipoEducacion);
			pstmt.setString(posic++, tipoCentroEdu);
			pstmt.setString(posic++, periodoDePago);

			pstmt.setInt(posic++, nroSolicitud);
			resultado += pstmt.executeUpdate();
			if (resultado == 1) {
				mensaje = Constantes.EXITO;
			}
		} catch (SQLException e) {
			try {
				log.debug("Ocurrió un error en alguna de las consultas");
				mensaje = "errorUpdateCambiosEnSolicitudBEI";
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return mensaje;
	}

	public String guardarCambiosEnSolicitud(String[] listadoNroSolicitudes)
			throws SQLException {
		String sql = "";
		String moneda = "044";
		String mensaje = "";
		String accion = "desincorporar";

		int resultado = 0;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;

		if (listadoNroSolicitudes != null && listadoNroSolicitudes.length <= 0) {
			mensaje = "error";
			return mensaje;
		}

		ResultSet rs = null;
		try {
			Solicitud solicitud = null;
			for (int j = 0; j < listadoNroSolicitudes.length; j++) {
				log.debug("Cargando datos de la solicitud en el objeto solicitud...");
				solicitud = buscarSolicitud(accion,
						Integer.parseInt(listadoNroSolicitudes[j]));
				// buscarSolicitud(con, pstmt, accion);
				solicitud.setCo_status("D");
				log.debug("Estoy dentro del método guardarCambiosEnSolicitud...");

				sql = "UPDATE RHEI.MOV_ST_SOLIC_BEI SET CO_STATUS=? WHERE NU_SOLICITUD=?";

				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, solicitud.getCo_status());
				pstmt.setInt(2, solicitud.getNroSolicitud());

				resultado += pstmt.executeUpdate();
			}
			if (resultado == listadoNroSolicitudes.length) {
				log.debug("Las desincorporaciones fueron insertadas en la tabla RHEI.MOV_ST_SOLIC_BEI");

				mensaje = "Se ha realizado exitosamente la operacón de desincorporación";
			} else {
				log.debug("Ocurrió un problema en la operación de desincorporación");
			}
		} catch (SQLException e1) {
			try {
				log.debug("Ocurrió un error en la inserción");
				mensaje = "error";
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			e1.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return mensaje;
	}
	
	
	public String desincorporarSolicitudByAnio(String codAnio)
			throws SQLException {
		String sql = "";
	 
		String mensaje = "";
	
		int resultado = 0;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;
 

		ResultSet rs = null;
		try {
			 
 
	 

				sql = "UPDATE RHEI.MOV_ST_SOLIC_BEI SET CO_STATUS=? WHERE co_periodo=?";

				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, Constantes.CO_STATUS_DESINCORPORADO);
				pstmt.setString(2, codAnio);

				resultado += pstmt.executeUpdate();
		 
				mensaje = "Se ha realizado exitosamente la operacón de desincorporación";
			 
		} catch (SQLException e1) {
			try {
				log.debug("Ocurrió un error en la inserción");
				mensaje = "error";
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			e1.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return mensaje;
	}

	public String addMovStSolicitudBEI(String[] listadoNroSolicitudes)
			throws SQLException {
		String sql = "";
		String moneda = "044";
		String mensaje = "";
		String accion = "desincorporar";

		int resultado = 0;
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement pstmt = null;

		if (listadoNroSolicitudes != null && listadoNroSolicitudes.length <= 0) {
			mensaje = "error";
			return mensaje;
		}

		ResultSet rs = null;
		try {
			Solicitud solicitud = null;
			for (int j = 0; j < listadoNroSolicitudes.length; j++) {
				log.debug("Cargando datos de la solicitud en el objeto solicitud...");
				solicitud = buscarSolicitud(accion,
						Integer.parseInt(listadoNroSolicitudes[j]));
				// buscarSolicitud(con, pstmt, accion);
				solicitud.setCo_status("D");
				log.debug("Estoy dentro del método guardarCambiosEnSolicitud...");

				sql = "INSERT INTO RHEI.MOV_ST_SOLIC_BEI (NU_SOLICITUD, CO_STATUS, FE_STATUS,IN_NIVEL_ESCOLAR, IN_BENEF_COMPART, IN_TIPO_EMPRESA, MO_APORTE_BCV,MO_PERIODO, MO_MATRICULA, MO_EMPRESA_REPRES, CO_MONEDA, CO_PERIODO) VALUES (?,?,SYSDATE,?,?,?,?,?,?,?,?,?)";

				pstmt = con.prepareStatement(sql);

				pstmt.setInt(1, solicitud.getNroSolicitud());
				pstmt.setString(2, solicitud.getCo_status());
				if (StringUtils.isEmpty(solicitud.getComparteBeneficio())){
					solicitud.setComparteBeneficio("N");
				}

				pstmt.setString(3, solicitud.getNivelEscolar());
				pstmt.setString(4, solicitud.getComparteBeneficio());
				pstmt.setString(5, solicitud.getTipoEmpresa());
				pstmt.setDouble(6, solicitud.getMontoAporteBCV().doubleValue());
				pstmt.setDouble(7, solicitud.getMontoPeriodo().doubleValue());
				pstmt.setDouble(8, solicitud.getMontoMatricula().doubleValue());
				pstmt.setDouble(9, solicitud.getMontoEmpresa().doubleValue());
				pstmt.setString(10, moneda);
				pstmt.setInt(11, solicitud.getCodigoPeriodo());

				resultado += pstmt.executeUpdate();
			}
			if (resultado == listadoNroSolicitudes.length) {
				log.debug("Las desincorporaciones fueron insertadas en la tabla RHEI.MOV_ST_SOLIC_BEI");

				mensaje = "Se ha realizado exitosamente la operacón de desincorporación";
			} else {
				log.debug("Ocurrió un problema en la operación de desincorporación");
			}
		} catch (SQLException e1) {
			try {
				log.debug("Ocurrió un error en la inserción");
				mensaje = "error";
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			e1.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return mensaje;
	}

	public String guardarCambiosEnSolicitud(String[] listaSolicitud,
			String periodoEscolar, String fecha, int cantidadRegistros)
			throws SQLException {
		String sql_select = "SELECT M.IN_NIVEL_ESCOLAR, M.IN_BENEF_COMPART, M.IN_TIPO_EMPRESA, M.MO_PERIODO, M.MO_MATRICULA, M.MO_EMPRESA_REPRES, M.CO_MONEDA FROM RHEI.MOV_ST_SOLIC_BEI M WHERE M.NU_SOLICITUD  = ? AND M.FE_STATUS = (SELECT MAX(M1.FE_STATUS) FROM RHEI.MOV_ST_SOLIC_BEI M1 WHERE M1.CO_PERIODO         =     ? AND M1.NU_SOLICITUD  =     M.NU_SOLICITUD AND M1.CO_STATUS     NOT IN ('D'))";
		String sql_insert = "INSERT INTO RHEI.MOV_ST_SOLIC_BEI (NU_SOLICITUD, CO_STATUS, FE_STATUS, IN_NIVEL_ESCOLAR, IN_BENEF_COMPART, IN_TIPO_EMPRESA, MO_APORTE_BCV, MO_PERIODO, MO_MATRICULA, MO_EMPRESA_REPRES, CO_MONEDA, CO_PERIODO) VALUES (?,?,SYSDATE,?, ?,?,?,?, ?,?,?,?)";
		String mensaje = "";
		String moneda = "";
		String codigoEstatus = "M";
		int contador = 0;

		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Solicitud solicitud = null;
		try {

			for (int j = 0; j < listaSolicitud.length; j += 2) {
				solicitud = new Solicitud();
				solicitud.setNroSolicitud(Integer.parseInt(listaSolicitud[j]
						.toString()));
				solicitud.setMontoAporteBCV(Double.valueOf(Double
						.parseDouble(listaSolicitud[(j + 1)].toString())));

				stmt = con.prepareStatement(sql_select);
				stmt.setInt(1, solicitud.getNroSolicitud());
				stmt.setString(2, periodoEscolar);

				rs = stmt.executeQuery();
				if (rs.next()) {
					solicitud.setNivelEscolar(rs.getString("IN_NIVEL_ESCOLAR"));
					solicitud.setComparteBeneficio(rs
							.getString("IN_BENEF_COMPART"));
					solicitud.setTipoEmpresa(rs.getString("IN_TIPO_EMPRESA"));
					solicitud.setMontoPeriodo(Double.valueOf(rs
							.getDouble("MO_PERIODO")));
					solicitud.setMontoMatricula(Double.valueOf(rs
							.getDouble("MO_MATRICULA")));
					solicitud.setMontoEmpresa(Double.valueOf(rs
							.getDouble("MO_EMPRESA_REPRES")));
					moneda = rs.getString("CO_MONEDA");

					stmt = con.prepareStatement(sql_insert);
					stmt.setInt(1, solicitud.getNroSolicitud());
					stmt.setString(2, codigoEstatus);
					stmt.setString(3, solicitud.getNivelEscolar());
					stmt.setString(4, solicitud.getComparteBeneficio());
					stmt.setString(5, solicitud.getTipoEmpresa());
					stmt.setDouble(6, solicitud.getMontoAporteBCV()
							.doubleValue());
					stmt.setDouble(7, solicitud.getMontoPeriodo().doubleValue());
					stmt.setDouble(8, solicitud.getMontoMatricula()
							.doubleValue());
					stmt.setDouble(9, solicitud.getMontoEmpresa().doubleValue());
					stmt.setString(10, moneda);
					stmt.setInt(11, Integer.parseInt(periodoEscolar));
					contador += stmt.executeUpdate();
				} else {
					log.debug("Problemas con la busqueda de los datos del movimiento de la solicitud");

					break;
				}
			}
			if (contador == listaSolicitud.length / 2) {
				contador = 0;
				sql_insert = "UPDATE RHEI.PARAMETRO SET IN_ST_PROCESAMIENT = 'E' WHERE CO_PARAMETRO = 'MTOBCV' AND CO_TIPO_BENEFICIO = 'RHEI' AND IN_ST_PROCESAMIENT = 'P'";
				stmt = con.prepareStatement(sql_insert);
				contador = stmt.executeUpdate();
				if (contador == cantidadRegistros) {
					mensaje = "Exito";
				} else {
					log.debug("Ocurrió un problema en el cambio del estatus del parámetro MTOBCV");
				}
			} else {
				log.debug("Ocurrió un problema en la operación de actualización masiva");
			}
		} catch (SQLException e1) {
			try {
				log.debug("Ocurrió un problema en la operación de actualización masiva");
				mensaje = "Fracaso";
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			e1.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return mensaje;
	}

	public String verificarCambioSalarioMinimo(String tipoBeneficio,
			int cantCombCiaTipoTrab) throws SQLException {
		Connection con = manejadorDB.coneccionPool();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int cantidadRegistros = 0;
		String respuesta = "";
		String sql = "SELECT COUNT(*) FROM RHEI.PARAMETRO WHERE CO_PARAMETRO = 'MTOBCV' AND CO_TIPO_BENEFICIO = ? AND IN_ST_PROCESAMIENT = 'P'";
		try {
			stmt = con.prepareStatement(sql);
			stmt.setString(1, tipoBeneficio);
			rs = stmt.executeQuery();
			if (rs.next()) {
				cantidadRegistros = rs.getInt(1);
			}
			if (cantidadRegistros == cantCombCiaTipoTrab) {
				respuesta = "Exito";
			} else if (cantidadRegistros == 0) {
				respuesta = "Fracaso";
			} else {
				respuesta = "Aviso";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return respuesta;
	}

}
