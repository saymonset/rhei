/**
 * 
 */
package com.bcv.dao.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import ve.org.bcv.rhei.util.Utilidades;

import com.bcv.dao.jdbc.BeneficioEscolarDao;
import com.bcv.dao.jdbc.ManejadorDB;
import com.bcv.dao.jdbc.SimpleJDBCDaoImpl;
import com.bcv.model.BeneficioEscolar;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco 10/08/2015 15:29:27 2015 mail :
 *         oraclefedora@gmail.com
 */
public class BeneficioEscolarDaoImpl
		extends
			SimpleJDBCDaoImpl<BeneficioEscolar> implements BeneficioEscolarDao {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(BeneficioEscolarDaoImpl.class
			.getName());

	public ArrayList<String> buscarBeneficiosEscolares() {
		ManejadorDB manejador = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ArrayList<String> listaBeneficiosEscolares = new ArrayList();
		String status = "A";
		String sql = "";
		try {
			manejador = new ManejadorDB();;
			con = manejador.conexion();
			log.debug("Entro en buscarBeneficiosEscolares");
			sql = "SELECT CO_TIPO_BENEFICIO FROM RHEI.BENEFICIO_ESCOLAR WHERE IN_STATUS=? ORDER BY CO_TIPO_BENEFICIO ASC";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, status);

			rs = stmt.executeQuery();
			while (rs.next()) {
				listaBeneficiosEscolares.add(String.valueOf(rs
						.getString("CO_TIPO_BENEFICIO")));
			}
			log.debug("Contenido de listaBeneficiosEscolares :"
					+ listaBeneficiosEscolares);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return listaBeneficiosEscolares;
	}

	public ArrayList<String> buscarBeneficioEscolar(int indMenor, int indMayor) {
		ManejadorDB manejador = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ArrayList<String> listadoBeneficioEscolar = new ArrayList();
		String sql = "";
		StringBuilder sqlBuilder = new StringBuilder("");
		try {
			manejador = new ManejadorDB();;
			con = manejador.conexion();
			log.debug("Entro en buscarBeneficioEscolar");
			sql = "SELECT CO_TIPO_BENEFICIO, TX_BENEFICIO, TO_CHAR(FE_REGISTRO, 'DD-MM-YYYY') AS FECHA, IN_STATUS FROM RHEI.BENEFICIO_ESCOLAR";

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

			rs = stmt.executeQuery();
			while (rs.next()) {
				listadoBeneficioEscolar.add(rs.getString("CO_TIPO_BENEFICIO"));
				listadoBeneficioEscolar.add(rs.getString("TX_BENEFICIO"));
				listadoBeneficioEscolar.add(rs.getString("FECHA"));
				listadoBeneficioEscolar.add(rs.getString("IN_STATUS"));
			}
			log.debug("Contenido de listadoBeneficioEscolar :"
					+ listadoBeneficioEscolar);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return listadoBeneficioEscolar;
	}

	public int cuantosSql(String tablaBD) {
		ManejadorDB manejador = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		int cuantos = 0;
		String sql = "";

		if (tablaBD != null && !"".equalsIgnoreCase(tablaBD)) {
			sql = "SELECT count(*)  as cuantos FROM " + tablaBD;
		} else {
			sql = "SELECT count(*)  as cuantos FROM RHEI.BENEFICIO_ESCOLAR";
		}

		try {
			manejador = new ManejadorDB();;
			con = manejador.conexion();
			stmt = con.prepareStatement(sql);
			log.debug("Query: " + sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				cuantos = rs.getInt("cuantos");
			}
			log.debug("Contenido de cuantos en SQL :" + cuantos);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return cuantos;
	}

	public String generadorTablaBeneficioEscolar(String tablaBD, int indMenor,
			int indMayor) {
		String encabezado = "<table class=\"anchoTabla3\" ><caption align=\"top\"><h2>Lista de Beneficios Escolares</h2></caption><tr><th width=\"10%\">Nombre</th><th width=\"60%\">Descripci&oacute;n</th><th width=\"20%\">Fecha de Registro</th><th width=\"10%\">Estado</th><th>Eliminar</th></tr>";

		String endTable = "";
		String tabla = encabezado;
		ArrayList<String> listaBeneficios = null;
		String estilo = "";
		String contenedor = "";
		listaBeneficios = buscarBeneficioEscolar(indMenor, indMayor);
		if ((listaBeneficios != null) && (listaBeneficios.size() != 0)) {
			for (int i = 0; i < listaBeneficios.size(); i += 4) {

				estilo = Utilidades.modificarEstiloTextArea("mayuscula",
						((String) listaBeneficios.get(i + 1)).length(), 100);
				if (estilo.compareTo("style=\"line-height: 32px;\"") == 0) {
					contenedor = (String) listaBeneficios.get(i + 1);

				} else {
					contenedor = "<textarea name=\"valorBeneficioEscolar"
							+ i
							+ "\"  tabindex=\"\" id=\"\" cols=\"100\" rows=\"2\" onfocus=\"blur()\" "
							+ estilo + ">"
							+ (String) listaBeneficios.get(i + 1)
							+ "</textarea>";

				}
				tabla = tabla
						+ "<tr><td><a href=\"/rhei/benefScolarControlador?principal.do=findParametro&"
						+ "&co_tipo_beneficio="
						+ (String) listaBeneficios.get(i)
						+ "&other=''\">"
						+ (String) listaBeneficios.get(i)
						+ "</a></td>"
						+ "<td>"
						+ contenedor
						+ "</td>"
						+ "<td>"
						+ (String) listaBeneficios.get(i + 2)
						+ "</td>"
						+ "<td>"
						+ (String) listaBeneficios.get(i + 3)
						+ "</td><td><a href=\"javascript:void(0)\" onclick=\"deleteBeneficiario('"
						+ (String) listaBeneficios.get(i) + "');\" >" + "..."
						+ "</a></td></tr>";
			}
			tabla = tabla + "</table>";
			endTable = "<table><tr><th  style=\"background: white;\" width=\"44%\"></th><th  style=\"background: white;\" width=\"3%\"><a href=\"javascript:void(0)\"  onclick=\"paginandoBeneficios('p')\" ><input type=\"image\" src=\"/rhei/imagenes/arrow-first.gif\" alt=\"Primero\" /></a></th><th style=\"background: white;\" width=\"3%\" ><a  href=\"javascript:void(0)\"  onclick=\"paginandoBeneficios('a')\" ><input type=\"image\" src=\"/rhei/imagenes/arrow-fr.gif\" alt=\"Anterior\" /></a></th><th style=\"background: white;\" width=\"3%\"><a href=\"javascript:void(0)\"  onclick=\"paginandoBeneficios('s')\" ><input type=\"image\" src=\"/rhei/imagenes/arrow-ff.gif\" alt=\"Siguiente\" /></a></th><th style=\"background: white;\" width=\"3%\"><a href=\"javascript:void(0)\"  onclick=\"paginandoBeneficios('u')\" ><input type=\"image\" src=\"/rhei/imagenes/arrow-last.gif\" alt=\"Ultimo\" /></a></th><th  style=\"background: white;\" width=\"44%\"></th></tr>";
			endTable += "</table>";
			tabla += "<br>" + endTable;

			log.debug("Contenido de tabla: " + tabla);
		} else if (listaBeneficios.size() == 0) {
			tabla = "No hay registros";
		}
		return tabla;
	}

	public String guardarBeneficioEscolar(String accion,String codigoBeneficio,String descripcion,String fechaRegistro,String condicion ) throws SQLException {
		ManejadorDB manejador = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String mensaje = "";
		try {
			manejador = new ManejadorDB();;
			con = manejador.conexion();
			int resultado = 0;

			String sql = "";

			log.debug("Estoy dentro del método guardarBeneficoEscolar de la clase BeneficioEscolar....");
			if (accion.compareToIgnoreCase("crear") == 0) {
				sql = "INSERT INTO RHEI.BENEFICIO_ESCOLAR (CO_TIPO_BENEFICIO, TX_BENEFICIO, FE_REGISTRO, IN_STATUS) VALUES (?, ?, TO_DATE(?, 'DD-MM-YYYY'), ?)";
			} else if (accion.compareToIgnoreCase("modificar") == 0) {
				sql = "UPDATE RHEI.BENEFICIO_ESCOLAR SET TX_BENEFICIO = ?, IN_STATUS = ? WHERE CO_TIPO_BENEFICIO=? ";
			}
			stmt = con.prepareStatement(sql);
			if (accion.compareToIgnoreCase("crear") == 0) {
				stmt.setString(1, codigoBeneficio);
				stmt.setString(2, descripcion);
				stmt.setString(3, fechaRegistro);
				stmt.setString(4, condicion);
				resultado = stmt.executeUpdate();
				log.debug("Valor de la variable resultado: " + resultado);
				if (resultado == 1) {
					log.debug("El método se ejecutó exitosamente!!!");
					mensaje = "Exito";
				} else {
					log.debug("El método fallo!!!");
					mensaje = "Fracaso";
				}
			} else if (accion.compareToIgnoreCase("modificar") == 0) {
				stmt.setString(1, descripcion);
				stmt.setString(2, condicion);
				stmt.setString(3, codigoBeneficio);
				resultado = stmt.executeUpdate();
				log.debug("Valor de la variable resultado: " + resultado);
				if (resultado == 1) {
					log.debug("El método se ejecutó exitosamente!!!");
					mensaje = "Exito";
				} else {
					log.debug("El método fallo!!!");
					mensaje = "Fracaso";
				}
			}
		} catch (Exception e) {
			e.toString();
		} finally {
			liberarConexion(rs, stmt, con);
		}

		return mensaje;
	}

	public boolean existeBeneficioEscolar(String codigoBeneficio) throws SQLException {
		boolean resultado = false;

		ManejadorDB manejador = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			manejador = new ManejadorDB();;
			con = manejador.conexion();
			int numeroRegistros = -1;
			String sql = "";
			log.debug("Estoy dentro del método existeBeneficioEscolar de la clase BeneficioEscolar....");

			sql = "SELECT COUNT(*) FROM RHEI.BENEFICIO_ESCOLAR WHERE CO_TIPO_BENEFICIO=?";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, codigoBeneficio);

			rs = stmt.executeQuery();
			if (rs.next()) {
				numeroRegistros = rs.getInt(1);
				if (numeroRegistros == 1) {
					resultado = true;
				} else {
					resultado = false;
				}
			} else {
				resultado = false;
			}
			log.debug("Contenido de numeroRegistros :" + numeroRegistros);
			log.debug("Contenido de resultado :" + resultado);
			rs.close();
		} finally {
			liberarConexion(rs, stmt, con);
		}

		return resultado;
	}

	public BeneficioEscolar findParametro(String codigoBeneficio) throws SQLException {
		BeneficioEscolar obj = new BeneficioEscolar();
		boolean resultado = false;
		int numeroRegistros = -1;
		String sql = "";
		ManejadorDB manejador = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			manejador = new ManejadorDB();;
			con = manejador.conexion();
			sql = "SELECT CO_TIPO_BENEFICIO,TX_BENEFICIO,TO_CHAR(FE_REGISTRO, 'DD-MM-YYYY') AS FECHA,IN_STATUS FROM RHEI.BENEFICIO_ESCOLAR WHERE CO_TIPO_BENEFICIO=? ";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, codigoBeneficio);

			rs = stmt.executeQuery();
			while (rs.next()) {
				obj.setCodigoBeneficio(rs.getString("CO_TIPO_BENEFICIO"));
				obj.setDescripcion(rs.getString("TX_BENEFICIO"));

				obj.setFechaRegistro(rs.getString("FECHA"));
				obj.setCondicion(rs.getString("IN_STATUS"));
			}

			log.debug("Contenido de numeroRegistros :" + numeroRegistros);
			log.debug("Contenido de resultado :" + resultado);
			rs.close();
		} finally {
			liberarConexion(rs, stmt, con);
		}

		return obj;
	}

	public int delete(String codigoBeneficio) {
		int numeroRegistros = -1;
		String sql = "";
		ManejadorDB manejador1 = null;
		Connection con1 = null;
		PreparedStatement stmt1 = null;
		manejador1 = new ManejadorDB();;
		con1 = manejador1.conexion();

		sql = "DELETE FROM RHEI.BENEFICIO_ESCOLAR WHERE CO_TIPO_BENEFICIO=? ";

		try {
			stmt1 = con1.prepareStatement(sql);
			stmt1.setString(1, codigoBeneficio);
			numeroRegistros = stmt1.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			liberarConexion(null, stmt1, con1);
		}

		return numeroRegistros;
	}

}
