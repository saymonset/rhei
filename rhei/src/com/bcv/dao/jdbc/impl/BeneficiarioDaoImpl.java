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
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.bean.ValorNombre;
import ve.org.bcv.rhei.util.Constantes;

import com.bcv.dao.jdbc.BeneficiarioDao;
import com.bcv.dao.jdbc.ManejadorDB;
import com.bcv.dao.jdbc.SimpleJDBCDaoImpl;
import com.bcv.model.Beneficiario;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco 10/08/2015 15:03:46 2015 mail :
 *         oraclefedora@gmail.com
 */
public class BeneficiarioDaoImpl extends SimpleJDBCDaoImpl<Beneficiario>
		implements
			BeneficiarioDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(BeneficiarioDaoImpl.class
			.getName());
	/**
	 * BUSCAMOS LOS BENEFICIARIOS QUE TENGAN DETERMINADA EDAD
	 * 
	 * @param cedulaTrabajador
	 * @param edadMin
	 * @param edadMax
	 * @return
	 */
	public ArrayList<String> buscarBeneficiarioII(int cedulaTrabajador,
			int edadMin, int edadMax) {
		ManejadorDB manejador = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		manejador = new ManejadorDB();;
		con = manejador.conexion();
		ArrayList<String> listaBeneficiario = new ArrayList();

		try {
			StringBuilder sql = new StringBuilder(
					" SELECT  DISTINCT F.CEDULA_FAMILIAR, INITCAP(F.NOMBRE1) AS NOMBRE1, INITCAP(F.APELLIDO1) AS APELLIDO1 ");
			sql.append(" FROM  PERSONAL.FAMILIARES F where F.NU_CEDULA= ?  ");
			sql.append(" AND TRUNC((MONTHS_BETWEEN(SYSDATE,F.FECHA_NACIMIENTO))/12) BETWEEN ? AND ? ");
			sql.append(" AND F.PARENTESCO=? AND F.STATUS =?");
			sql.append(" ORDER BY F.CEDULA_FAMILIAR ASC ");
			stmt = con.prepareStatement(sql.toString());
			stmt.setInt(1, cedulaTrabajador);
			stmt.setInt(2, edadMin);
			stmt.setInt(3, edadMax);
			stmt.setString(4, Constantes.PARENTESCO);
			stmt.setString(5, Constantes.STATUS);

			rs = stmt.executeQuery();
			while (rs.next()) {
				listaBeneficiario.add(String.valueOf(rs
						.getInt("CEDULA_FAMILIAR")));
				listaBeneficiario.add(String.valueOf(rs.getString("NOMBRE1")));
				listaBeneficiario
						.add(String.valueOf(rs.getString("APELLIDO1")));
			}
			log.debug("Contenido de listaBeneficiario :" + listaBeneficiario);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return listaBeneficiario;
	}
	
	/**
	 * @param cedulaTrabajador
	 * @param edadMin
	 * @param edadMax
	 * @return
	 */
	public List<ValorNombre> buscarBeneficiarioByCedula(
			int cedulaTrabajador) {
		ManejadorDB manejador = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		manejador = new ManejadorDB();;
		con = manejador.conexion();
		List<ValorNombre> listaBeneficiario = new ArrayList<ValorNombre>();

		try {
			log.debug("Entro en buscarBeneficiario");
			String sql = "SELECT CEDULA_FAMILIAR, INITCAP(NOMBRE1) AS NOMBRE1, INITCAP(APELLIDO1) AS APELLIDO1 FROM  PERSONAL.FAMILIARES WHERE NU_CEDULA =? AND STATUS=? AND PARENTESCO=?  ORDER BY CEDULA_FAMILIAR ASC";
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, cedulaTrabajador);
			stmt.setString(2, Constantes.STATUS);
			stmt.setString(3, Constantes.PARENTESCO);
			rs = stmt.executeQuery();
			ValorNombre valorNombre=null;
			while (rs.next()) {
				valorNombre= new ValorNombre(String.valueOf(rs
						.getInt("CEDULA_FAMILIAR")),String.valueOf(rs.getString("NOMBRE1")) +" "+String.valueOf(rs.getString("APELLIDO1")));
				listaBeneficiario.add(valorNombre);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return listaBeneficiario;
	}

	/**
	 * @param cedulaTrabajador
	 * @param edadMin
	 * @param edadMax
	 * @return
	 */
	public ArrayList<String> buscarBeneficiarioSinRestriccion(
			int cedulaTrabajador) {
		ManejadorDB manejador = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		manejador = new ManejadorDB();;
		con = manejador.conexion();
		ArrayList<String> listaBeneficiario = new ArrayList<String>();

		try {
			log.debug("Entro en buscarBeneficiario");
			String sql = "SELECT CEDULA_FAMILIAR, INITCAP(NOMBRE1) AS NOMBRE1, INITCAP(APELLIDO1) AS APELLIDO1 FROM  PERSONAL.FAMILIARES WHERE NU_CEDULA =? AND STATUS=? AND PARENTESCO=?  ORDER BY CEDULA_FAMILIAR ASC";
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, cedulaTrabajador);
			stmt.setString(2, Constantes.STATUS);
			stmt.setString(3, Constantes.PARENTESCO);

			rs = stmt.executeQuery();
			while (rs.next()) {
				listaBeneficiario.add(String.valueOf(rs
						.getInt("CEDULA_FAMILIAR")));
				listaBeneficiario.add(String.valueOf(rs.getString("NOMBRE1")));
				listaBeneficiario
						.add(String.valueOf(rs.getString("APELLIDO1")));
			}
			log.debug("Contenido de listaBeneficiario :" + listaBeneficiario);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return listaBeneficiario;
	}

	public ArrayList<String> buscarBeneficiario(int cedulaTrabajador,
			int edadMin, int edadMax) {
		ManejadorDB manejador = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		manejador = new ManejadorDB();;
		con = manejador.conexion();
		ArrayList<String> listaBeneficiario = new ArrayList();

		try {
			log.debug("Entro en buscarBeneficiario");
			String sql = "SELECT CEDULA_FAMILIAR, INITCAP(NOMBRE1) AS NOMBRE1, INITCAP(APELLIDO1) AS APELLIDO1 FROM  PERSONAL.FAMILIARES WHERE NU_CEDULA =? AND STATUS=? AND PARENTESCO=? AND TRUNC((MONTHS_BETWEEN(SYSDATE,FECHA_NACIMIENTO))/12) BETWEEN ? AND ? ORDER BY CEDULA_FAMILIAR ASC";
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, cedulaTrabajador);
			stmt.setString(2, Constantes.STATUS);
			stmt.setString(3, Constantes.PARENTESCO);
			stmt.setInt(4, edadMin);
			stmt.setInt(5, edadMax);

			rs = stmt.executeQuery();
			while (rs.next()) {
				listaBeneficiario.add(String.valueOf(rs
						.getInt("CEDULA_FAMILIAR")));
				listaBeneficiario.add(String.valueOf(rs.getString("NOMBRE1")));
				listaBeneficiario
						.add(String.valueOf(rs.getString("APELLIDO1")));
			}
			log.debug("Contenido de listaBeneficiario :" + listaBeneficiario);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return listaBeneficiario;
	}

	public ArrayList<String> buscarBeneficiarioConSolicitudII(
			int cedulaTrabajador) {
		ManejadorDB manejador = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		manejador = new ManejadorDB();;
		con = manejador.conexion();
		ArrayList<String> listaBeneficiarioConSolicitud = new ArrayList<String>();
		try {

			StringBuilder sql = new StringBuilder(
					" SELECT  DISTINCT F.CEDULA_FAMILIAR, INITCAP(F.NOMBRE1) AS NOMBRE1, INITCAP(F.APELLIDO1) AS APELLIDO1 ");
			sql.append(" FROM  PERSONAL.FAMILIARES F where F.NU_CEDULA= ?  ");
			sql.append(" AND F.PARENTESCO=? ");
			sql.append("  AND F.STATUS =?");
			sql.append(" ORDER BY F.CEDULA_FAMILIAR ASC ");
			stmt = con.prepareStatement(sql.toString());
			stmt.setInt(1, cedulaTrabajador);
			stmt.setString(2, Constantes.PARENTESCO);
			stmt.setString(3, Constantes.STATUS);

			rs = stmt.executeQuery();
			while (rs.next()) {
				listaBeneficiarioConSolicitud.add(String.valueOf(rs
						.getInt("CEDULA_FAMILIAR")));
				listaBeneficiarioConSolicitud.add(String.valueOf(rs
						.getString("NOMBRE1")));
				listaBeneficiarioConSolicitud.add(String.valueOf(rs
						.getString("APELLIDO1")));
			}
			log.debug("Contenido de listaBeneficiarioConSolicitud :"
					+ listaBeneficiarioConSolicitud);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return listaBeneficiarioConSolicitud;
	}

	
//	aqu� es donde sacamos los datos del beneficiario 
	
	public Beneficiario buscarBeneficiario(int codigo) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		Beneficiario beneficiario= null;

		ResultSet rs = null;
		try {
			ManejadorDB manejador = new ManejadorDB();;
			con = manejador.conexion();

			log.debug("Entre en el método buscarBeneficiario con parametros con y pstmt");
			log.debug("Codigo beneficiario: " + codigo);
			String sql = "SELECT CEDULA_FAMILIAR, INITCAP(NOMBRE1||' '||NOMBRE2) AS NOMBRE, INITCAP(APELLIDO1||' '||APELLIDO2) AS APELLIDO, FECHA_NACIMIENTO,TRUNC((MONTHS_BETWEEN(SYSDATE,FECHA_NACIMIENTO))/12) AS EDAD, 'Activo' AS STATUS FROM PERSONAL.FAMILIARES WHERE CEDULA_FAMILIAR = ?";

			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, codigo);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				beneficiario= new Beneficiario();
				log.debug("Seteando al objeto beneficiario");
				beneficiario.setCodigo(codigo);
				beneficiario.setNombre(rs.getString("NOMBRE").toUpperCase());
				beneficiario.setApellido(rs.getString("APELLIDO").toUpperCase());
				beneficiario.setFechanacimento(rs.getDate("FECHA_NACIMIENTO"));
				beneficiario.setEdad(Integer.parseInt(rs.getString("EDAD")));
				beneficiario.setStatus(rs.getString("STATUS"));
			}

			log.debug("Saliendo del metodo buscarBeneficiario con parametros con y pstmt");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return beneficiario;
	}

	public boolean verificarSolicitud(int codigoEmpleado, String operacion,int codigo) {
		boolean respuesta = true;
		String consulta = "";
		ManejadorDB manejador = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		manejador = new ManejadorDB();;
		con = manejador.conexion();
		label444 : try {
			if ((operacion.equals("incluir"))
					|| (operacion.equals("consultar"))
					|| (operacion.equals("actualizar"))) {
				consulta = "SELECT MAX(NU_SOLICITUD) AS NU_SOLICITUD, COUNT(*) FROM RHEI.SOLICITUD_BEI WHERE CO_EMPLEADO = ? AND CEDULA_FAMILIAR = ? ";

				log.debug("Codigo del familiar dentro del metodo verificarSolicitud: "
						+ codigo);
				log.debug("Codigo del empleado dentro del metodo verificarSolicitud: "
						+ codigoEmpleado);
				stmt = con.prepareStatement(consulta);
				stmt.setInt(1, codigoEmpleado);
				stmt.setInt(2, codigo);
				rs = stmt.executeQuery();
				if ((rs.next()) && (rs.getInt(2) != 0)) {
					log.debug("Valor devuelto por la consulta: "
							+ rs.getString("NU_SOLICITUD"));
					int nroSolicitud = rs.getInt("NU_SOLICITUD");
					consulta = "SELECT CO_STATUS FROM RHEI.MOV_ST_SOLIC_BEI WHERE NU_SOLICITUD   = ? AND FE_STATUS = (SELECT MAX(FE_STATUS) FROM RHEI.MOV_ST_SOLIC_BEI WHERE NU_SOLICITUD = ?)";

					stmt = con.prepareStatement(consulta);
					stmt.setInt(1, nroSolicitud);
					stmt.setInt(2, nroSolicitud);
					rs = stmt.executeQuery();
					rs.next();
					if (!rs.getString("CO_STATUS").equals("D")) {
						log.debug("La última solicitud asociada a esa dupla tiene status diferente a D");
						respuesta = true;
						break label444;
					}
					log.debug("La solicitud más reciente asociada a esa dupla tiene status igual a D");
					if (operacion.equals("consultar")) {
						respuesta = true;
					}
					if ((operacion.equals("incluir"))
							|| (operacion.equals("actualizar"))) {
						respuesta = false;
						break label444;
					}
				} else {
					log.debug("No hay solicitud asociada a esa dupla");
					respuesta = false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		log.debug("Valor de la variable respuesta: " + respuesta);
		return respuesta;
	}

	public  int calcularEdad(String fecha_nac) {
		Date fechaActual = new Date();
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		String hoy = formato.format(fechaActual);
		log.debug("Valor de la variable hoy: " + hoy);
		log.debug("Valor de la variable fecha_nac: " + fecha_nac);
		String[] dat1 = fecha_nac.split("-");
		String[] dat2 = hoy.split("-");
		int anos = Integer.parseInt(dat2[0]) - Integer.parseInt(dat1[0]);
		log.debug("Valor de la variable anos: " + anos);
		int mes = Integer.parseInt(dat2[1]) - Integer.parseInt(dat1[1]);
		if (mes < 0) {
			anos--;
			log.debug("Valor de la variable anos: " + anos);
		} else if (mes == 0) {
			int dia = Integer.parseInt(dat2[2]) - Integer.parseInt(dat1[2]);
			if (dia > 0) {
				anos--;
				log.debug("Valor de la variable anos: " + anos);
			}
		}
		log.debug("Valor final de la variable anos: " + anos);
		return anos;
	}

	public ShowResultToView cargarAtributosBeneficiario(
			ShowResultToView showResultToView,int codigo,String apellido,String nombre,String fechaNacimento,int edad,String status) {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		showResultToView.setCedulaFamiliar(String.valueOf(codigo));
		showResultToView.setApellidoFamiliar(apellido);
		showResultToView.setNombreFamiliar(nombre);
		showResultToView.setFechaNacimiento(format.format(fechaNacimento)
				.toString());
		showResultToView.setEdad(edad + "");
		showResultToView.setEstatus(status);
		return showResultToView;
	}

}
