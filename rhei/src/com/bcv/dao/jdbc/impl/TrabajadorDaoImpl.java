package com.bcv.dao.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ve.org.bcv.rhei.bean.Proveedor;
import ve.org.bcv.rhei.util.Utilidades;

import com.bcv.dao.jdbc.ManejadorDB;
import com.bcv.dao.jdbc.SimpleJDBCDaoImpl;
import com.bcv.dao.jdbc.TrabajadorDao;
import com.bcv.model.Trabajador;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco 02/07/2015 10:00:28 2015 mail :
 *         oraclefedora@gmail.com
 */
public class TrabajadorDaoImpl extends SimpleJDBCDaoImpl<Trabajador> implements TrabajadorDao {
	private static Logger log = Logger.getLogger(TrabajadorDaoImpl.class.getName());
	ManejadorDB manejadorDB = new ManejadorDB();;

	/**
	 * @param dataSource
	 */
	public TrabajadorDaoImpl() {
		super();

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.bcv.dao.jdbc.TrabajadorDao#cargarTipoTrabajador()
	 */
	public ArrayList<String> cargarTipoTrabajador() throws SQLException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		con = manejadorDB.coneccionPool();

		ArrayList<String> tipoTrabajador = new ArrayList<String>();
		try {
			String sql = "SELECT TIPO_EMP, INITCAP(DESCRIPCION) FROM PERSONAL.TIPOS_EMPLEADOS ORDER BY CODIGO_CONTABLE ASC";
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				tipoTrabajador.add(rs.getString("TIPO_EMP"));
				tipoTrabajador.add(rs.getString("INITCAP(DESCRIPCION)"));
			}
			log.debug("Contenido de tipo trabajador :" + tipoTrabajador);
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return tipoTrabajador;
	}

	/**
	 * @param companiaAnalista
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<String> cargarCompania(String companiaAnalista) throws SQLException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		con = manejadorDB.coneccionPool();
		String sql = "";
		ArrayList<String> listaCompanias = new ArrayList<String>();
		try {
			if (companiaAnalista != null && companiaAnalista.compareTo("01") == 0) {
				sql = "SELECT CODIGO_CIA, INITCAP(NOMBRE_CIA) FROM PERSONAL.COMPANIAS ORDER BY CODIGO_CIA ASC";
			} else {
				sql = "SELECT CODIGO_CIA, INITCAP(NOMBRE_CIA) FROM PERSONAL.COMPANIAS WHERE CODIGO_CIA = '" + companiaAnalista + "' " + "ORDER BY " + "CODIGO_CIA ASC";
			}
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				listaCompanias.add(rs.getString("CODIGO_CIA"));
				listaCompanias.add(rs.getString("INITCAP(NOMBRE_CIA)"));
			}
			log.debug("Contenido de compañia :" + listaCompanias);
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return listaCompanias;
	}

	/**
	 * @param compania
	 * @param tipoEmp
	 * @param tipoBeneficio
	 * @param nombreParametro
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<String> buscarParametro(String compania, String tipoEmp, String tipoBeneficio, String nombreParametro) throws SQLException {
		compania = "01";
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		con = manejadorDB.coneccionPool();
		String sql = "";
		ArrayList<String> dupla = new ArrayList<String>();
		try {
			log.debug("Entro en buscarParametro");
			// sql = "select P.TX_VALOR_PARAMETRO, P.IN_TIPO_PARAMETRO from RHEI.parametro P where
			// P.CO_COMPANIA = ? and P.TIPO_EMP = ? and P.co_tipo_beneficio = ? and P.co_parametro =
			// ?";
			sql = "select P.TX_VALOR_PARAMETRO, P.IN_TIPO_PARAMETRO from RHEI.parametro P where P.CO_COMPANIA = ?  and P.co_tipo_beneficio = ? and P.co_parametro = ?";
			int posicion = 1;
			stmt = con.prepareStatement(sql);
			stmt.setString(posicion++, compania);
			// stmt.setString(2, tipoEmp);
			stmt.setString(posicion++, tipoBeneficio);
			stmt.setString(posicion++, nombreParametro);

			rs = stmt.executeQuery();
			while (rs.next()) {
				dupla.add(rs.getString("TX_VALOR_PARAMETRO"));
				dupla.add(rs.getString("IN_TIPO_PARAMETRO"));
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return dupla;
	}

	/**
	 * @param cedula
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<String> buscarFiltros(int cedula) throws SQLException {
		Connection con1 = null;

		con1 = manejadorDB.coneccionPool();
		String sql = "";
		ArrayList<String> listaFiltros = new ArrayList<String>();
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;
		try {
			log.debug("Entro en buscarFiltros");

			sql = "SELECT CO_CIA_FISICA, TIPO_EMP FROM PERSONAL.TODOS_EMPLEADOS WHERE CEDULA = ? ";

			stmt2 = con1.prepareStatement(sql);
			stmt2.setInt(1, cedula);

			rs2 = stmt2.executeQuery();
			while (rs2.next()) {
				listaFiltros.add(rs2.getString("CO_CIA_FISICA"));
				listaFiltros.add(rs2.getString("TIPO_EMP"));
			}
			log.debug("Contenido de listaFiltros :" + listaFiltros);
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			liberarConexion(rs2, stmt2, con1);
		}
		return listaFiltros;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bcv.dao.jdbc.TrabajadorDao#buscarFiltros(int, java.lang.String)
	 */
	public ArrayList<String> buscarFiltros(int codigoEmpleado, String tipoNomina) throws SQLException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		con = manejadorDB.coneccionPool();
		String sql = "";
		ArrayList<String> listaFiltros = new ArrayList<String>();
		try {
			sql = "SELECT CO_CIA_FISICA, TIPO_EMP FROM   PERSONAL.TODOS_EMPLEADOS WHERE CODIGO_EMPLEADO = ? AND TIPO_NOMINA = ?";
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, codigoEmpleado);
			stmt.setString(2, tipoNomina);

			rs = stmt.executeQuery();
			while (rs.next()) {
				listaFiltros.add(rs.getString("CO_CIA_FISICA"));
				listaFiltros.add(rs.getString("TIPO_EMP"));
			}
			log.debug("Contenido de listaFiltros :" + listaFiltros);
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return listaFiltros;
	}

	/**
	 * @param companiaAnalista
	 * @param cedula
	 * @return
	 * @throws SQLException
	 */
	public String consultar(String companiaAnalista, int cedula) throws SQLException {
		String compania = null;
		String respuesta = "";
		String consulta = "SELECT CO_CIA_FISICA FROM    PERSONAL.TODOS_EMPLEADOS  WHERE CEDULA = ?";
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		con = manejadorDB.coneccionPool();
		label425: try {
			stmt = con.prepareStatement(consulta);
			stmt.setInt(1, cedula);
			rs = stmt.executeQuery();
			if (rs.next()) {
				compania = rs.getString("CO_CIA_FISICA");
				log.debug("El trabajador con cedula de identidad N° " + cedula + " existe en la base de datos");
				if (companiaAnalista.compareTo("01") != 0) {
					if (companiaAnalista.compareTo("02") == 0) {
						if (compania.compareToIgnoreCase("02") == 0) {
							respuesta = "Exito";
							log.debug("El analista está facultado para consultar la cédula '" + cedula + "'");
						} else {
							log.debug("El analista no está facultado para consultar la cédula '" + cedula + "'");
							respuesta = "Sin privilegio";
						}
					}
					if (companiaAnalista.compareTo("03") == 0) {
						if (compania.compareToIgnoreCase("03") == 0) {
							respuesta = "Exito";
							log.debug("El analista está facultado para consultar la cédula '" + cedula + "'");
							break label425;
						}
						log.debug("El analista no está facultado para consultar la cédula '" + cedula + "'");
						respuesta = "Sin privilegio";
						break label425;
					}
				} else {
					respuesta = "Exito";
					log.debug("El analista está facultado para consultar cualquier trabajador registrado en el sistema");
					break label425;
				}
			} else {
				log.debug("El trabajador con cedula de identidad N° " + cedula + " no existe en la base de datos");
				respuesta = "No existe";
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			liberarConexion(rs, stmt, con);
		}

		return respuesta;
	}

	/**
	 * Encontraremos codigo_empleado por cedula
	 * 
	 * @param cedula
	 * @return
	 * @throws SQLException
	 */
	public String cedulaFamiliarByNuSolicitud(int numSolicitud) throws SQLException {
		String cedula_familiar = "";
		StringBuilder sql = new StringBuilder();
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		con = manejadorDB.coneccionPool();

		try {
			sql.append("");
			sql.append("  SELECT cedula_familiar FROM RHEI.SOLICITUD_BEI where  nu_solicitud=?");

			stmt = con.prepareStatement(sql.toString());
			stmt.setInt(1, numSolicitud);
			rs = stmt.executeQuery();
			if (rs.next()) {
				cedula_familiar = rs.getString("cedula_familiar");
			}

		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return cedula_familiar;
	}

	/**
	 * Encontraremos codigo_empleado por cedula
	 * 
	 * @param cedula
	 * @return
	 * @throws SQLException
	 */
	public String codigoEmpleadoByNuSolicitud(int numSolicitud) throws SQLException {
		String codigo_empleado = "";
		StringBuilder sql = new StringBuilder();
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		con = manejadorDB.coneccionPool();

		try {
			sql.append("");
			sql.append("  SELECT co_empleado FROM RHEI.SOLICITUD_BEI where  nu_solicitud=?");

			stmt = con.prepareStatement(sql.toString());
			stmt.setInt(1, numSolicitud);
			rs = stmt.executeQuery();
			if (rs.next()) {
				codigo_empleado = rs.getString("co_empleado");
			}

		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return codigo_empleado;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bcv.dao.jdbc.TrabajadorDao#consultar(int, java.lang.String, java.lang.String)
	 */
	public String consultar(int nro_solicitud, String companiaAnalista, String tipoNomina) throws SQLException {
		String situacion = null;
		String compania = null;
		int codigoEmpleado = 0;
		String respuesta = "";
		String consulta = "";
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		con = manejadorDB.coneccionPool();

		label837: try {
			consulta = "SELECT CO_EMPLEADO FROM RHEI.SOLICITUD_BEI WHERE NU_SOLICITUD = ? AND TI_NOMINA = ? ";

			stmt = con.prepareStatement(consulta);
			stmt.setInt(1, nro_solicitud);
			stmt.setString(2, tipoNomina);
			rs = stmt.executeQuery();
			if (!rs.next()) {
				respuesta = "La solicitud N° " + nro_solicitud + " no existe en el sistema.";
			} else {
				codigoEmpleado = rs.getInt("CO_EMPLEADO");

				consulta = "SELECT SITUACION, CO_CIA_FISICA FROM  PERSONAL.TODOS_EMPLEADOS T INNER JOIN RHEI.SOLICITUD_BEI S  ON T.TIPO_NOMINA = S.TI_NOMINA WHERE CODIGO_EMPLEADO = ? AND T.TIPO_NOMINA = ? ";

				stmt = con.prepareStatement(consulta);
				stmt.setInt(1, codigoEmpleado);
				stmt.setString(2, tipoNomina);
				rs = stmt.executeQuery();
				if (rs.next()) {
					situacion = rs.getString("SITUACION");
					compania = rs.getString("CO_CIA_FISICA");
				}
				log.debug("Compañia del Empleado: " + compania);
				if (companiaAnalista.compareTo("01") != 0) {
					if (companiaAnalista.compareTo("02") == 0) {
						if (compania.compareToIgnoreCase("02") == 0) {
							log.debug("El analista está facultado para consultar o actualizar esta solicitud.");
							if (situacion.equalsIgnoreCase("E")) {
								respuesta = "El trabajador asociado a la solicitud N° " + nro_solicitud + " tiene condición de Egresado.";
							} else {
								respuesta = "Exito";
							}
						} else {
							respuesta = "Usted no está facultado para consultar o actualizar esta solicitud.";
						}
					}
					if (companiaAnalista.compareTo("03") == 0) {
						if (compania.compareToIgnoreCase("03") == 0) {
							log.debug("El analista está facultado para consultar o actualizar esta solicitud.");
							if (situacion.equalsIgnoreCase("E")) {
								respuesta = "El trabajador asociado a la solicitud N° " + nro_solicitud + " tiene condición de Egresado.";
								break label837;
							}
							respuesta = "Exito";
							break label837;
						}
						respuesta = "Usted no está facultado para consultar o actualizar esta solicitud.";
						break label837;
					}
				} else {
					log.debug("El analista está facultado para consultar o actualizar cualquier solicitud registrada en el sistema");
					if (situacion.equalsIgnoreCase("E")) {
						respuesta = "El trabajador asociado a la solicitud N° " + nro_solicitud + " tiene condición de Egresado.";
					} else {
						respuesta = "Exito";
					}
				}
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return respuesta;
	}

	/**
	 * Encontraremos codigo_empleado por cedula
	 * 
	 * @param cedula
	 * @return
	 * @throws SQLException
	 */
	public String codigoEmpleadoBycedula(int cedula) throws SQLException {
		String codigo_empleado = "";
		StringBuilder sql = new StringBuilder();
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		con = manejadorDB.coneccionPool();

		try {
			sql.append("");
			sql.append(" SELECT codigo_empleado  FROM PERSONAL.TODOS_EMPLEADOS WHERE  cedula=? ");

			stmt = con.prepareStatement(sql.toString());
			stmt.setInt(1, cedula);
			rs = stmt.executeQuery();
			if (rs.next()) {
				codigo_empleado = rs.getString("codigo_empleado");
			}

		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return codigo_empleado;
	}

	/**
	 * Encontraremos la cedula por numero de factura
	 * 
	 * @return
	 * @throws SQLException
	 */
	public String cedulaByNroFactura(String nu_factura) throws SQLException {
		String cedula = "";
		StringBuilder sql = new StringBuilder();
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		con = manejadorDB.coneccionPool();
		try {
			sql.append("");
			sql.append(" SELECT cedula  FROM PERSONAL.TODOS_EMPLEADOS WHERE  codigo_empleado in ( ");
			sql.append(" 		   select bei.co_empleado from RHEI.SOLICITUD_BEI bei where BEI.NU_SOLICITUD in (select RP.NU_SOLICITUD from RHEI.RELACION_PAGOS rp ");
			sql.append(" 		  where RP.NU_ID_FACTURA in (select f.nu_id_factura from rhei.factura f where f.nu_factura=?)   ) ");
			sql.append(" 		  ) ");

			stmt = con.prepareStatement(sql.toString());
			stmt.setString(1, nu_factura);
			rs = stmt.executeQuery();
			if (rs.next()) {
				cedula = rs.getString("cedula");
			}

		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return cedula;
	}

	/**
	 * @param busqueda
	 * @param codigoEmpleado
	 * @param cedula
	 * @param tipoNomina
	 * @return
	 * @throws SQLException
	 */
	public Trabajador buscarTrabajador(String busqueda, int codigoEmpleado, int cedula, String tipoNomina) throws SQLException {

		Trabajador trabajador = new Trabajador();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			con = manejadorDB.coneccionPool();
			String sql = "";
			String tipoEmpleado = "";
			log.debug("Entre en el metodo buscarTrabajador con parametros con, pstmt, y busqueda");
			if (busqueda.equals("codigoEmpleado")) {
				sql = "SELECT DISTINCT T.CODIGO_EMPLEADO, T.CEDULA, INITCAP(T.NOMBRE1||' '||T.NOMBRE2) AS NOMBRE, INITCAP(T.APELLIDO1||' '||T.APELLIDO2) AS APELLIDO, DECODE(T.SITUACION, 'A', 'Activo', 'V', 'Vacaciones', 'E','Egresado','S','Suspendido','NO ASOCIADO') AS SITUACION, INITCAP(TE.DESCRIPCION) AS TIPO_EMP, T.TIPO_EMP AS TIPO, T.FECHA_INGRESO, INITCAP(T.DESCRIPCION) AS DESCRIPCION, INITCAP(T.NOMBRE_CARGO) AS NOMBRE_CARGO, INITCAP(TC.NOMBRE_CIA) AS COMPANIA, T.TIPO_NOMINA AS TIPO_NOMINA FROM PERSONAL.TODOS_EMPLEADOS T INNER JOIN RHEI.SOLICITUD_BEI S  ON T.CODIGO_EMPLEADO = S.CO_EMPLEADO AND T.TIPO_NOMINA = S.TI_NOMINA INNER JOIN PERSONAL.TIPOS_EMPLEADOS TE ON T.TIPO_EMP = TE.TIPO_EMP INNER JOIN PERSONAL.COMPANIAS TC ON T.CODIGO_CIA = TC.CODIGO_CIA WHERE T.CODIGO_EMPLEADO = ? AND T.TIPO_NOMINA = ? ";
			}
			if (busqueda.equals("cedula")) {
				sql = "SELECT DISTINCT T.CODIGO_EMPLEADO, T.CEDULA, INITCAP(T.NOMBRE1||' '||T.NOMBRE2) AS NOMBRE, INITCAP(T.APELLIDO1||' '||T.APELLIDO2) AS APELLIDO, DECODE(T.SITUACION, 'A', 'Activo', 'V', 'Vacaciones', 'E','Egresado','S','Suspendido','NO ASOCIADO') AS SITUACION, INITCAP(TE.DESCRIPCION) AS TIPO_EMP, T.TIPO_EMP AS TIPO, T.FECHA_INGRESO, INITCAP(T.DESCRIPCION) AS DESCRIPCION, INITCAP(T.NOMBRE_CARGO) AS NOMBRE_CARGO, INITCAP(TC.NOMBRE_CIA) AS COMPANIA, T.TIPO_NOMINA AS TIPO_NOMINA FROM PERSONAL.TODOS_EMPLEADOS T INNER JOIN RHEI.SOLICITUD_BEI S  ON T.CODIGO_EMPLEADO = S.CO_EMPLEADO AND T.TIPO_NOMINA = S.TI_NOMINA INNER JOIN PERSONAL.TIPOS_EMPLEADOS TE ON T.TIPO_EMP = TE.TIPO_EMP INNER JOIN PERSONAL.COMPANIAS TC ON T.CODIGO_CIA = TC.CODIGO_CIA WHERE T.CEDULA = ?";
			}
			pstmt = con.prepareStatement(sql);
			if (busqueda.equals("codigoEmpleado")) {
				pstmt.setInt(1, codigoEmpleado);
				pstmt.setString(2, tipoNomina);
			}
			if (busqueda.equals("cedula")) {
				pstmt.setInt(1, cedula);
			}
			rs = pstmt.executeQuery();
			if (rs.next()) {
				log.debug("Seteando al objeto trabajador");
				trabajador.setCodigoEmpleado(Integer.parseInt(rs.getString("CODIGO_EMPLEADO")));
				trabajador.setCedula(Integer.parseInt(rs.getString("CEDULA")));
				trabajador.setNombre(rs.getString("NOMBRE"));
				trabajador.setApellido(rs.getString("APELLIDO"));
				trabajador.setSituacion(rs.getString("SITUACION"));
				trabajador.setTipoEmpleado(rs.getString("TIPO_EMP"));
				trabajador.setFechaIngreso(rs.getDate("FECHA_INGRESO"));
				trabajador.setUbicacion(rs.getString("DESCRIPCION"));
				trabajador.setCargo(Utilidades.convertidorCadena(rs.getString("NOMBRE_CARGO")));
				trabajador.setCompania(rs.getString("COMPANIA"));
				tipoEmpleado = rs.getString("TIPO");
				trabajador.setTipoNomina(rs.getString("TIPO_NOMINA"));
			}
			if ((tipoEmpleado.equalsIgnoreCase("CON")) || (tipoEmpleado.equalsIgnoreCase("OBC"))) {
				rs = null;
				log.debug("Si el trabajador es contratado aparece este aviso");

				sql = "SELECT HASTA FROM PERSONAL.CONTRATOS WHERE CODIGO_EMPLEADO =(SELECT CODIGO_EMPLEADO FROM PERSONAL.TODOS_EMPLEADOS WHERE CEDULA = ?) AND DESDE = TO_DATE('" +

						trabajador.getFechaIngreso() + "','rrrr-mm-dd')";
				log.debug("sql= " + sql);
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, cedula);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					trabajador.setFechaHasta(rs.getDate("HASTA"));
				} else {
					trabajador.setFechaHasta(null);
				}
			} else {
				trabajador.setFechaHasta(null);
			}
			rs.close();
			trabajador.setNroExpediente("DBS-" + trabajador.getCedula());
			log.debug("Saliendo del metodo buscarTrabajador con parametros con, pstmt y busqueda");

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return trabajador;
	}

	/**
	 * @param cedula
	 * @return
	 * @throws SQLException
	 */
	public Trabajador contratoFechas(int cedula) throws SQLException {
		Trabajador trabajador = new Trabajador();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		con = manejadorDB.coneccionPool();
		try {

			String sql = "";
			log.debug("Entre en el metodo buscarTrabajador con parametros con, pstmt");

			sql = "SELECT c.desde, c.hasta FROM PERSONAL.CONTRATOS c where C.CODIGO_EMPLEADO in (SELECT pt.codigo_empleado FROM PERSONAL.TODOS_EMPLEADOS pt where PT.CEDULA=?) order by C.HASTA desc";

			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, cedula);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				log.debug("Seteando al objeto trabajador");
				trabajador.setCodigoEmpleado(Integer.parseInt(rs.getString("CODIGO_EMPLEADO")));
				trabajador.setCedula(Integer.parseInt(rs.getString("CEDULA")));
				trabajador.setNombre(rs.getString("NOMBRE").toLowerCase());
				trabajador.setApellido(rs.getString("APELLIDO"));
				trabajador.setSituacion(rs.getString("SITUACION"));
				trabajador.setTipoEmpleado(rs.getString("TIPO_EMP").toLowerCase());
				trabajador.setFechaIngreso(rs.getDate("FECHA_INGRESO"));
				trabajador.setUbicacion(rs.getString("DESCRIPCION"));
				trabajador.setCargo(Utilidades.convertidorCadena(rs.getString("NOMBRE_CARGO")));
				trabajador.setCompania(rs.getString("COMPANIA"));
			}

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return trabajador;
	}

	/**
	 * @param cedula
	 * @return
	 * @throws SQLException
	 */
	public Trabajador buscarTrabajadorByCodEmp(int codigoEmpleado) throws SQLException {
		Trabajador trabajador = new Trabajador();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		con = manejadorDB.coneccionPool();
		try {

			String sql = "";
			String tipoEmpleado = "";
			log.debug("Entre en el metodo buscarTrabajador con parametros con, pstmt");

			sql = "SELECT DISTINCT T.CODIGO_EMPLEADO, T.CEDULA, INITCAP(T.NOMBRE1||' '||T.NOMBRE2) AS NOMBRE, INITCAP(T.APELLIDO1||' '||T.APELLIDO2) AS APELLIDO,T.NU_EXTENSION_1,T.TX_EMAIL_PROPIO,T.TX_EMAIL_BCV, DECODE(T.SITUACION, 'A', 'Activo', 'V', 'Vacaciones', 'E','Egresado','S','Suspendido','NO ASOCIADO') AS SITUACION, INITCAP(TE.DESCRIPCION) AS TIPO_EMP, T.TIPO_EMP AS TIPO, T.FECHA_INGRESO, INITCAP(T.DESCRIPCION) AS DESCRIPCION, INITCAP(T.NOMBRE_CARGO) AS NOMBRE_CARGO, INITCAP(TC.NOMBRE_CIA) AS COMPANIA, T.TIPO_NOMINA AS TIPO_NOMINA FROM PERSONAL.TODOS_EMPLEADOS T INNER JOIN PERSONAL.TIPOS_EMPLEADOS TE ON T.TIPO_EMP = TE.TIPO_EMP INNER JOIN PERSONAL.COMPANIAS TC ON T.CODIGO_CIA = TC.CODIGO_CIA WHERE T.CODIGO_EMPLEADO = ?";

			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, codigoEmpleado);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				log.debug("Seteando al objeto trabajador");
				trabajador.setCodigoEmpleado(Integer.parseInt(rs.getString("CODIGO_EMPLEADO")));
				trabajador.setCedula(Integer.parseInt(rs.getString("CEDULA")));
				trabajador.setNombre(rs.getString("NOMBRE"));
				trabajador.setApellido(rs.getString("APELLIDO"));
				trabajador.setSituacion(rs.getString("SITUACION"));
				trabajador.setTipoEmpleado(rs.getString("TIPO_EMP"));
				trabajador.setFechaIngreso(rs.getDate("FECHA_INGRESO"));
				trabajador.setUbicacion(rs.getString("DESCRIPCION"));
				trabajador.setCargo(Utilidades.convertidorCadena(rs.getString("NOMBRE_CARGO")));
				trabajador.setCompania(rs.getString("COMPANIA"));
				tipoEmpleado = rs.getString("TIPO");
				trabajador.setTipoNomina(rs.getString("TIPO_NOMINA"));

				trabajador.setTlfNumExt(rs.getString("NU_EXTENSION_1"));
				trabajador.setEmailPropio(rs.getString("TX_EMAIL_PROPIO"));
				trabajador.setEmailBcv(rs.getString("TX_EMAIL_BCV"));

			}
			if ((tipoEmpleado.equalsIgnoreCase("CON")) || (tipoEmpleado.equalsIgnoreCase("OBC"))) {
				rs = null;
				log.debug("Si el trabajador es contratado aparece este aviso");

				sql = "SELECT HASTA FROM PERSONAL.CONTRATOS WHERE CODIGO_EMPLEADO =? AND DESDE = TO_DATE('" +

						trabajador.getFechaIngreso() + "','rrrr-mm-dd')";
				log.debug("sql= " + sql);
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, codigoEmpleado);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					log.debug("Seteando el campo hasta del objeto trabajador por ser CONTRATADO");
					trabajador.setFechaHasta(rs.getDate("HASTA"));

				} else {
					trabajador.setFechaHasta(null);
				}
			} else {
				trabajador.setFechaHasta(null);
			}
			rs.close();
			trabajador.setNroExpediente("DBS-" + trabajador.getCedula());
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return trabajador;
	}

	/**
	 * @param cedula
	 * @return
	 * @throws SQLException
	 */
	public Trabajador buscarTrabajador(int cedula) throws SQLException {
		Trabajador trabajador = new Trabajador();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		con = manejadorDB.coneccionPool();
		try {

			String sql = "";
			String tipoEmpleado = "";
			log.debug("Entre en el metodo buscarTrabajador con parametros con, pstmt");

			sql = "SELECT DISTINCT T.CODIGO_EMPLEADO, T.CEDULA, INITCAP(T.NOMBRE1||' '||T.NOMBRE2) AS NOMBRE, INITCAP(T.APELLIDO1||' '||T.APELLIDO2) AS APELLIDO,T.NU_EXTENSION_1,T.TX_EMAIL_PROPIO,T.TX_EMAIL_BCV, DECODE(T.SITUACION, 'A', 'Activo', 'V', 'Vacaciones', 'E','Egresado','S','Suspendido','NO ASOCIADO') AS SITUACION, INITCAP(TE.DESCRIPCION) AS TIPO_EMP, T.TIPO_EMP AS TIPO, T.FECHA_INGRESO, INITCAP(T.DESCRIPCION) AS DESCRIPCION, INITCAP(T.NOMBRE_CARGO) AS NOMBRE_CARGO, INITCAP(TC.NOMBRE_CIA) AS COMPANIA, T.TIPO_NOMINA AS TIPO_NOMINA FROM PERSONAL.TODOS_EMPLEADOS T INNER JOIN PERSONAL.TIPOS_EMPLEADOS TE ON T.TIPO_EMP = TE.TIPO_EMP INNER JOIN PERSONAL.COMPANIAS TC ON T.CODIGO_CIA = TC.CODIGO_CIA WHERE T.CEDULA = ?";

			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, cedula);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				log.debug("Seteando al objeto trabajador");
				trabajador.setCodigoEmpleado(Integer.parseInt(rs.getString("CODIGO_EMPLEADO")));
				trabajador.setCedula(Integer.parseInt(rs.getString("CEDULA")));
				trabajador.setNombre(rs.getString("NOMBRE"));
				trabajador.setApellido(rs.getString("APELLIDO"));
				trabajador.setSituacion(rs.getString("SITUACION"));
				trabajador.setTipoEmpleado(rs.getString("TIPO_EMP"));
				trabajador.setFechaIngreso(rs.getDate("FECHA_INGRESO"));
				trabajador.setUbicacion(rs.getString("DESCRIPCION"));
				trabajador.setCargo(Utilidades.convertidorCadena(rs.getString("NOMBRE_CARGO")));
				trabajador.setCompania(rs.getString("COMPANIA"));
				tipoEmpleado = rs.getString("TIPO");
				trabajador.setTipoEmpleadoCod(rs.getString("TIPO"));
				trabajador.setTipoNomina(rs.getString("TIPO_NOMINA"));

				trabajador.setTlfNumExt(rs.getString("NU_EXTENSION_1"));
				trabajador.setEmailPropio(rs.getString("TX_EMAIL_PROPIO"));
				trabajador.setEmailBcv(rs.getString("TX_EMAIL_BCV"));

			}
			if ((tipoEmpleado.equalsIgnoreCase("CON")) || (tipoEmpleado.equalsIgnoreCase("OBC"))) {
				rs = null;
				log.debug("Si el trabajador es contratado aparece este aviso");

				sql = "SELECT HASTA FROM PERSONAL.CONTRATOS WHERE CODIGO_EMPLEADO =(SELECT CODIGO_EMPLEADO FROM PERSONAL.TODOS_EMPLEADOS WHERE CEDULA = ?) AND DESDE = TO_DATE('" +

						trabajador.getFechaIngreso() + "','rrrr-mm-dd')";
				log.debug("sql= " + sql);
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, cedula);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					log.debug("Seteando el campo hasta del objeto trabajador por ser CONTRATADO");
					trabajador.setFechaHasta(rs.getDate("HASTA"));

				} else {
					trabajador.setFechaHasta(null);
				}
			} else {
				trabajador.setFechaHasta(null);
			}
			rs.close();
			trabajador.setNroExpediente("DBS-" + trabajador.getCedula());
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return trabajador;
	}

	/**
	 * @param companiaAnalista
	 * @param operacion
	 * @param numSolicitud
	 * @param tipoNomina
	 * @return
	 * @throws SQLException
	 */
	public boolean consultar(String companiaAnalista, String operacion, int numSolicitud, String tipoNomina) throws SQLException {
		boolean respuesta = false;
		String consulta = "";
		String situacion = "";
		String companiaTrabajador = "";
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		con = manejadorDB.coneccionPool();
		if ((operacion.compareToIgnoreCase("consultar") == 0) || (operacion.compareToIgnoreCase("consultarPago") == 0)) {
			consulta = "SELECT SITUACION, CO_CIA_FISICA FROM   PERSONAL.TODOS_EMPLEADOS  WHERE CODIGO_EMPLEADO = (SELECT CO_EMPLEADO FROM RHEI.SOLICITUD_BEI WHERE NU_SOLICITUD = ?)AND TIPO_NOMINA = ? ";
		} else {
			consulta = "SELECT SITUACION, CO_CIA_FISICA FROM PERSONAL.TODOS_EMPLEADOS WHERE CODIGO_EMPLEADO = (SELECT CO_EMPLEADO FROM RHEI.SOLICITUD_BEI WHERE NU_SOLICITUD = ?)AND TIPO_NOMINA = ? ";
		}
		try {
			stmt = con.prepareStatement(consulta);
			stmt.setInt(1, numSolicitud);
			stmt.setString(2, tipoNomina);
			rs = stmt.executeQuery();
			if (!rs.next()) {
				respuesta = false;
			} else {
				situacion = rs.getString("SITUACION");
				companiaTrabajador = rs.getString("CO_CIA_FISICA");
				if ((companiaAnalista.compareTo("01") == 0) || ((companiaAnalista.compareTo("01") != 0) && (companiaAnalista.compareTo(companiaTrabajador) == 0))) {
					if ((operacion.compareToIgnoreCase("consultar") == 0) || (operacion.compareToIgnoreCase("consultarPago") == 0)) {
						respuesta = true;
					} else if (!situacion.equalsIgnoreCase("E")) {

						respuesta = true;
					} else {

						respuesta = false;
					}
				} else if ((companiaAnalista.compareTo("01") != 0) && (companiaAnalista.compareTo(companiaTrabajador) != 0)) {

					respuesta = false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return respuesta;
	}

	/**
	 * @param cedula
	 * @return
	 * @throws SQLException
	 */
	public String obtenerTipoNomina(int cedula) throws SQLException {
		String sql = "";
		String tipoNomina = "";
		sql = "SELECT TIPO_NOMINA FROM   PERSONAL.TODOS_EMPLEADOS  WHERE CEDULA = ? ";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		con = manejadorDB.coneccionPool();
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, cedula);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				tipoNomina = rs.getString("TIPO_NOMINA");
			}
			rs.close();

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return tipoNomina;
	}

	/**
	 * Tabla donde tiene usuarios que trabajan en el banco, incluidos los String
	 * ACTIVO_EMPLEADO="A"; String VACACION_EMPLEADO="V"; String EGRESADO_EMPLEADO="E"; String
	 * SUSPENDIDO_EMPLEADO="S";
	 * 
	 * @param companiaAnalista
	 * @param operacion
	 * @param cedula
	 * @return
	 * @throws SQLException
	 */
	public String SituacionEmpleadosGeneral(int cedula) throws SQLException {
		String respuesta = null;
		String consulta = "";
		String situacion = "";
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		con = manejadorDB.coneccionPool();

		consulta = "SELECT SITUACION  FROM    PERSONAL.EMPLEADOS_GENERAL   WHERE CEDULA = ?";
		try {
			stmt = con.prepareStatement(consulta);
			stmt.setInt(1, cedula);
			rs = stmt.executeQuery();
			if (!rs.next()) {
				log.debug("El trabajador con cedula de identidad N° " + cedula + " no existe en la base de datos");
				respuesta = null;
			} else {
				situacion = rs.getString("SITUACION");
				respuesta = situacion;
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			liberarConexion(rs, stmt, con);
		}

		return respuesta;
	}

	/**
	 * @param companiaAnalista
	 * @param operacion
	 * @param cedula
	 * @return
	 * @throws SQLException
	 */
	public boolean consultar(String companiaAnalista, String operacion, int cedula) throws SQLException {
		boolean respuesta = false;
		String consulta = "";
		String situacion = "";
		String companiaTrabajador = "";
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		con = manejadorDB.coneccionPool();
		if ((operacion.compareToIgnoreCase("consultar") == 0) || (operacion.compareToIgnoreCase("consultarPago") == 0)) {
			consulta = "SELECT SITUACION, CODIGO_EMPLEADO, CO_CIA_FISICA FROM    PERSONAL.TODOS_EMPLEADOS  WHERE CEDULA = ?";
		} else {
			consulta = "SELECT SITUACION, CODIGO_EMPLEADO, CO_CIA_FISICA FROM    PERSONAL.TODOS_EMPLEADOS  WHERE CEDULA = ?";
		}
		try {
			stmt = con.prepareStatement(consulta);
			stmt.setInt(1, cedula);
			rs = stmt.executeQuery();
			if (!rs.next()) {
				log.debug("El trabajador con cedula de identidad N° " + cedula + " no existe en la base de datos");
				respuesta = false;
			} else {
				situacion = rs.getString("SITUACION");
				companiaTrabajador = rs.getString("CO_CIA_FISICA");
				if ((companiaAnalista.compareTo("01") == 0) || ((companiaAnalista.compareTo("01") != 0) && (companiaAnalista.compareTo(companiaTrabajador) == 0))) {
					if ((operacion.compareToIgnoreCase("consultar") == 0) || (operacion.compareToIgnoreCase("consultarPago") == 0)) {
						respuesta = true;
					} else if (!situacion.equalsIgnoreCase("E")) {
						log.debug("El trabajador con cedula de identidad N° " + cedula + " tiene estatus distinto de egresado");
						respuesta = true;
					} else {
						log.debug("La situacion del trabajador con cedula de identidad N° " + cedula + " es " + situacion);
						respuesta = false;
					}
				} else if ((companiaAnalista.compareTo("01") != 0) && (companiaAnalista.compareTo(companiaTrabajador) != 0)) {
					log.debug("Usted no tiene los permisos necesarios para ver los datos asociados al trabajador con c&eacute;dula de identidad N° " +

							cedula);
					respuesta = false;
				}
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			liberarConexion(rs, stmt, con);
		}

		return respuesta;
	}

	public List<Proveedor> buscarCedulas(String nombre1, String apellido1) {
		List<Proveedor> proveedors = new ArrayList<Proveedor>();
		ManejadorDB manejador = new ManejadorDB();
		;
		Connection con = manejador.conexion();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder("");
		try {

			if (null == nombre1) {
				nombre1 = "";
			}
			if (null == apellido1) {
				apellido1 = "";
			}

			
			sql.append(" SELECT DISTINCT T.CODIGO_EMPLEADO, T.CEDULA, INITCAP(T.NOMBRE1||' '||T.NOMBRE2) AS NOMBRE, ");
			sql.append(" INITCAP(T.APELLIDO1||' '||T.APELLIDO2) AS APELLIDO,T.NU_EXTENSION_1,T.TX_EMAIL_PROPIO,T.TX_EMAIL_BCV, "); 
			sql.append(" DECODE(T.SITUACION, 'A', 'Activo', 'V', 'Vacaciones', 'E','Egresado','S','Suspendido','NO ASOCIADO') AS SITUACION from    PERSONAL.TODOS_EMPLEADOS T ");
			
			sql.append(" where  ");
			boolean swOr = false;
			if (!"".equalsIgnoreCase(nombre1)) {
				sql.append("( ").append("  lower(t.nombre1) like '").append(nombre1.toLowerCase()).append("%'").append(" or ").append("  lower(t.nombre2) like '").append(nombre1.toLowerCase()).append("%'").append(" )");
				swOr = true;
			}
			if (swOr) {
				sql.append(" and ");
			}
			if (!"".equalsIgnoreCase(apellido1)) {
				//sql.append(" lower(t.apellido1) like '").append(apellido1.toLowerCase()).append("%'");
				sql.append("( ").append("  lower(t.apellido1) like '").append(apellido1.toLowerCase()).append("%'").append(" or ").append("  lower(t.apellido2) like '").append(apellido1.toLowerCase()).append("%'").append(" )");
			}

			sql.append(" ORDER BY APELLIDO ASC ");

			stmt = con.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			Proveedor prov = null;
			while (rs.next()) {
				prov = new Proveedor();
				prov.setValor(rs.getString("CEDULA"));
				prov.setNombre(rs.getString("APELLIDO") + " " + rs.getString("NOMBRE"));
				proveedors.add(prov);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return proveedors;
	}

}
