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

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ve.org.bcv.rhei.bean.ValorNombre;
import ve.org.bcv.rhei.util.Utilidades;

import com.bcv.dao.jdbc.ManejadorDB;
import com.bcv.dao.jdbc.PeriodoEscolarDao;
import com.bcv.dao.jdbc.SimpleJDBCDaoImpl;
import com.bcv.model.PeriodoEscolar;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco 11/08/2015 09:13:53 2015 mail :
 *         oraclefedora@gmail.com
 */
public class PeriodoEscolarDaoImpl extends SimpleJDBCDaoImpl<PeriodoEscolar>
		implements
			PeriodoEscolarDao {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(PeriodoEscolarDaoImpl.class
			.getName());

	/**
	 * Cuantos registros devuelve la consulta
	 * 
	 * @return
	 */
	public int cuantosSql() {
		ManejadorDB manejador = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		int cuantos = 0;
		String sql = "";
		sql = "SELECT count(*)  as cuantos FROM RHEI.PERIODO_ESCOLAR ORDER BY CO_PERIODO ASC";
		try {
			manejador = new ManejadorDB();;
			con = manejador.conexion();
			log.debug("Entro en buscarParametros");
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

	public ArrayList<String> buscarPeriodoEscolar(int indMenor, int indMayor) {
		ManejadorDB manejador = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ArrayList<String> listadoPeriodoEscolar = new ArrayList();
		String sql = "";
		StringBuilder sqlBuilder = new StringBuilder("");
		try {
			manejador = new ManejadorDB();;
			con = manejador.conexion();
			log.debug("Entro en buscarPeriodoEscolar");
			sql = "SELECT CO_PERIODO, TX_DESCRIP_PERIODO, TO_CHAR(FE_INICIO, 'DD-MM-YYYY HH24:MI:SS') AS FE_INICIO, TO_CHAR(FE_FIN, 'DD-MM-YYYY HH24:MI:SS') AS FE_FIN, IN_PERIODO_ESCOLAR FROM RHEI.PERIODO_ESCOLAR ORDER BY CO_PERIODO ASC";

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
				listadoPeriodoEscolar.add(String.valueOf(rs
						.getInt("CO_PERIODO")));
				listadoPeriodoEscolar.add(rs.getString("TX_DESCRIP_PERIODO"));
				listadoPeriodoEscolar.add(rs.getString("FE_INICIO"));
				listadoPeriodoEscolar.add(rs.getString("FE_FIN"));
				listadoPeriodoEscolar.add(rs.getString("IN_PERIODO_ESCOLAR"));
			}
			log.debug("Contenido de listadoBeneficioEscolar :"
					+ listadoPeriodoEscolar);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return listadoPeriodoEscolar;
	}

	public String buscarPeriodoEscolar(String codigoPeriodo) {
		ManejadorDB manejador = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String descripcion = "";
		String sql = "";
		try {
			manejador = new ManejadorDB();;
			con = manejador.conexion();
			log.debug("Entro en buscarPeriodoEscolar(String codigoPeriodo)");
			sql = "SELECT TX_DESCRIP_PERIODO FROM RHEI.PERIODO_ESCOLAR WHERE CO_PERIODO = "
					+

					codigoPeriodo;

			stmt = con.prepareStatement(sql);

			rs = stmt.executeQuery();
			if (rs.next()) {
				descripcion = rs.getString("TX_DESCRIP_PERIODO");
			}
			log.debug("Contenido de descripcion :" + descripcion);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return descripcion;
	}

	/**
	 * Buscaremos el preiodo escolar por numero de factura
	 * 
	 * @return
	 */
	public String buscarPeriodoEscolarByNuFactura(String nu_factura) {
		ManejadorDB manejador = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String descripcion = "";
		StringBuilder sql = new StringBuilder("");
		try {
			manejador = new ManejadorDB();;
			con = manejador.conexion();

			sql.append(" SELECT PE.TX_DESCRIP_PERIODO FROM  RHEI.PERIODO_ESCOLAR PE where PE.CO_PERIODO in ( ");
			sql.append(" select RP.CO_PERIODO from RHEI.RELACION_PAGOS rp ");
			sql.append(" where RP.NU_ID_FACTURA in (select f.nu_id_factura from rhei.factura f where f.nu_factura=?))");
			stmt = con.prepareStatement(sql.toString());
			stmt.setString(1, nu_factura);
			rs = stmt.executeQuery();
			if (rs.next()) {
				descripcion = rs.getString("TX_DESCRIP_PERIODO");
			}
			log.debug("Contenido de descripcion :" + descripcion);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return descripcion;
	}

	public String generadorTablaPeriodoEscolar(String tablaBD, int indMenor,
			int indMayor) {
		String periodo_lista = "Lista de Per&iacute;odos Escolares";
		String descripcion = "Descripci&oacute;n";
		String fechaInicio = "Fecha de Inicio";
		String fechaDeFin = "Fecha de Fin";
		String estado = "Estado";
		String encabezado = "<table class=\"anchoTabla5\" ><caption align=\"top\"><h2>"
				+ periodo_lista
				+ "</h2></caption><tr><th width=\"2%\">C&oacute;digo</th><th width=\"48%\">"
				+ descripcion
				+ "</th><th width=\"29%\">"
				+ fechaInicio
				+ "</th><th width=\"29%\">"
				+ fechaDeFin
				+ "</th><th width=\"1%\">"
				+ estado
				+ "</th><th>Eliminar</th></tr>";
		String endTable = "";
		String tabla = encabezado;
		ArrayList<String> listaPeriodos = new ArrayList();
		listaPeriodos = buscarPeriodoEscolar(indMenor, indMayor);
		if ((listaPeriodos != null) && (listaPeriodos.size() != 0)) {
			for (int i = 0; i < listaPeriodos.size(); i += 5) {

				tabla = tabla
						+ "<tr><td class=\"texto_der\"><a href=\"/rhei/periodoScolarControlador?principal.do=find&codigoPeriodo="
						+ (String) listaPeriodos.get(i)
						+ "\" title=\""
						+ (String) listaPeriodos.get(i + 1)
						+ "\">"
						+ (String) listaPeriodos.get(i)
						+ "</a></td>"
						+ "<td>"
						+ (String) listaPeriodos.get(i + 1)
						+ "</td>"
						+ "<td>"
						+ (String) listaPeriodos.get(i + 2)
						+ "</td>"
						+ "<td>"
						+ (String) listaPeriodos.get(i + 3)
						+ "</td>"
						+ "<td>"
						+ (String) listaPeriodos.get(i + 4)
						+ "</td><td><a href=\"/rhei/periodoScolarControlador?principal.do=delete&codigoPeriodo="
						+ (String) listaPeriodos.get(i) + "&descripcion="
						+ listaPeriodos.get(i + 1)
						+ "\" onClick=\"return deletePeriodo();\"" + " >"
						+ "..." + "</a></td></tr>";
			}
			tabla = tabla + "</table>";

			endTable = "<table><tr><th  style=\"background: white;\" width=\"44%\"></th><th  style=\"background: white;\" width=\"3%\"><a href=\"javascript:void(0)\"  onclick=\"paginandoPeriodoScolar('p')\" ><input type=\"image\" src=\"/rhei/imagenes/arrow-first.gif\" alt=\"Primero\" /></a></th><th style=\"background: white;\" width=\"3%\" ><a  href=\"javascript:void(0)\"  onclick=\"paginandoPeriodoScolar('a')\" ><input type=\"image\" src=\"/rhei/imagenes/arrow-fr.gif\" alt=\"Anterior\" /></a></th><th style=\"background: white;\" width=\"3%\"><a href=\"javascript:void(0)\"  onclick=\"paginandoPeriodoScolar('s')\" ><input type=\"image\" src=\"/rhei/imagenes/arrow-ff.gif\" alt=\"Siguiente\" /></a></th><th style=\"background: white;\" width=\"3%\"><a href=\"javascript:void(0)\"  onclick=\"paginandoPeriodoScolar('u')\" ><input type=\"image\" src=\"/rhei/imagenes/arrow-last.gif\" alt=\"Ultimo\" /></a></th><th  style=\"background: white;\" width=\"44%\"></th></tr>";
			endTable += "</table>";
			tabla += "<br>" + endTable;

			log.debug("Contenido de tabla: " + tabla);
		} else if (listaPeriodos.size() == 0) {
			log.debug("No hay per&iacute;odos escolares registrados en la base de datos del sistema");
			tabla = "No hay registros";
			log.debug("Mensaje contiene:" + tabla);
		}
		return tabla;
	}

	public String guardarPeriodoEscolar(String accion, String descripcion,
			String fechaInicio, String fechaFin, String condicion,
			String codigoPeriodo) throws SQLException {
		ManejadorDB manejador = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		int resultado = 0;
		String mensaje = "";
		String sql = "";

		try {
			manejador = new ManejadorDB();;
			con = manejador.conexion();
			log.debug("Estoy dentro del método guardarPeriodoEscolar de la clase PeriodoEscolar....");
			if (accion.compareToIgnoreCase("crear") == 0) {
				sql = "INSERT INTO RHEI.PERIODO_ESCOLAR (CO_PERIODO, TX_DESCRIP_PERIODO, FE_INICIO, FE_FIN, IN_PERIODO_ESCOLAR)VALUES (RHEI.SEQ_PERIODO_ESCOLAR.NEXTVAL, ?, TO_DATE(?, 'DD-MM-YYYY HH24:MI:SS'), TO_DATE(?, 'DD-MM-YYYY HH24:MI:SS'),?)";
			} else if (accion.compareToIgnoreCase("modificar") == 0) {
				sql = "UPDATE RHEI.PERIODO_ESCOLAR SET TX_DESCRIP_PERIODO = ?, IN_PERIODO_ESCOLAR = ? WHERE CO_PERIODO=? ";
			}
			stmt = con.prepareStatement(sql);
			if (accion.compareToIgnoreCase("crear") == 0) {
				stmt.setString(1, descripcion);
				stmt.setString(2, fechaInicio);
				stmt.setString(3, fechaFin);
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
				stmt.setInt(3, Integer.parseInt(codigoPeriodo));

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
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return mensaje;
	}
	
	
	public PeriodoEscolar findByMvStatusSolicitudActiva(String numSolicitud)
			throws SQLException {
		ManejadorDB manejador = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PeriodoEscolar obj = new PeriodoEscolar();
		try {
			 manejador =new ManejadorDB();;
		     con = manejador.conexion();
			
		
			StringBuilder sql = new StringBuilder();
			sql.append(" select pe.CO_PERIODO,pe.TX_DESCRIP_PERIODO,pe.FE_INICIO,pe.FE_FIN,pe.IN_PERIODO_ESCOLAR from RHEI.PERIODO_ESCOLAR pe ");
			sql.append(" inner join RHEI.MOV_ST_SOLIC_BEI m  on M.CO_PERIODO=pe.CO_PERIODO ");
			sql.append("  where M.NU_SOLICITUD=? and M.CO_STATUS='A' ");

			

			stmt = con.prepareStatement(sql.toString());
			stmt.setString(1, numSolicitud);

			rs = stmt.executeQuery();
			while (rs.next()) {
				obj.setCodigoPeriodo(String.valueOf(rs.getInt("CO_PERIODO")));
				obj.setDescripcion(rs.getString("TX_DESCRIP_PERIODO"));
				obj.setFechaInicio(rs.getString("FE_INICIO"));
				obj.setFechaFin(rs.getString("FE_FIN"));
				obj.setCondicion(rs.getString("IN_PERIODO_ESCOLAR"));
			}
			rs.close();
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			liberarConexion(rs, stmt, con);
		}
		
		return obj;
	}
	
	



	public PeriodoEscolar findParametro(String codigoPeriodo)
			throws SQLException {
		ManejadorDB manejador = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PeriodoEscolar obj = new PeriodoEscolar();
		try {
			 manejador =new ManejadorDB();;
		     con = manejador.conexion();
			
		
			String sql = "";

			sql = "SELECT CO_PERIODO, TX_DESCRIP_PERIODO, TO_CHAR(FE_INICIO, 'DD-MM-YYYY') AS FE_INICIO, TO_CHAR(FE_FIN, 'DD-MM-YYYY') AS FE_FIN, IN_PERIODO_ESCOLAR FROM RHEI.PERIODO_ESCOLAR  WHERE CO_PERIODO=?  ORDER BY CO_PERIODO ASC";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, codigoPeriodo);

			rs = stmt.executeQuery();
			while (rs.next()) {
				obj.setCodigoPeriodo(String.valueOf(rs.getInt("CO_PERIODO")));
				obj.setDescripcion(rs.getString("TX_DESCRIP_PERIODO"));
				obj.setFechaInicio(rs.getString("FE_INICIO"));
				obj.setFechaFin(rs.getString("FE_FIN"));
				obj.setCondicion(rs.getString("IN_PERIODO_ESCOLAR"));
			}
			rs.close();
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			liberarConexion(rs, stmt, con);
		}
		
		return obj;
	}
	

	public PeriodoEscolar findPeriodoByDescripcion(String descripcion )
			throws SQLException {
		ManejadorDB manejador =new ManejadorDB();;
		Connection con = manejador.conexion();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PeriodoEscolar obj = null;
		try {
		String sql = "";
		sql="SELECT * FROM RHEI.PERIODO_ESCOLAR WHERE LOWER(TX_DESCRIP_PERIODO)=?";

		stmt = con.prepareStatement(sql);
		String desc="";
		if (!StringUtils.isEmpty(descripcion)){
			desc=descripcion.toLowerCase();
		}
		stmt.setString(1, desc);

		rs = stmt.executeQuery();
		while (rs.next()) {
			obj=new PeriodoEscolar();
			obj.setCodigoPeriodo(String.valueOf(rs.getInt("CO_PERIODO")));
			obj.setDescripcion(rs.getString("TX_DESCRIP_PERIODO"));
			obj.setFechaInicio(rs.getString("FE_INICIO"));
			obj.setFechaFin(rs.getString("FE_FIN"));
			obj.setCondicion(rs.getString("IN_PERIODO_ESCOLAR"));
		}
		 
		}catch(Exception e){
			e.printStackTrace();
			
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return obj;
	}
	
	
	
	public PeriodoEscolar findPeriodoByDescripcionLast()
			throws SQLException {
		ManejadorDB manejador =new ManejadorDB();;
		Connection con = manejador.conexion();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PeriodoEscolar obj = null;
		try {
		StringBuilder  sql = new StringBuilder("SELECT CO_PERIODO,TX_DESCRIP_PERIODO FROM RHEI.PERIODO_ESCOLAR ORDER BY TX_DESCRIP_PERIODO DESC ");
	 

		stmt = con.prepareStatement(sql.toString());
	 
	 

		rs = stmt.executeQuery();
		if (rs.next()) {
			obj=new PeriodoEscolar();
			obj.setCodigoPeriodo(String.valueOf(rs.getInt("CO_PERIODO")));
			obj.setDescripcion(rs.getString("TX_DESCRIP_PERIODO"));
		}
		 rs.close();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return obj;
	}
	
	
	
	public PeriodoEscolar findPeriodoByDescripcion(String descripcion ,String condicion)
			throws SQLException {
		ManejadorDB manejador =new ManejadorDB();;
		Connection con = manejador.conexion();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PeriodoEscolar obj = null;
		try {
		StringBuilder  sql = new StringBuilder("SELECT * FROM RHEI.PERIODO_ESCOLAR WHERE LOWER(TX_DESCRIP_PERIODO)=? ");
		if (!StringUtils.isEmpty(condicion)){
			sql.append(" AND lower(IN_PERIODO_ESCOLAR)=?");	
		}
		

		stmt = con.prepareStatement(sql.toString());
		String desc="";
		if (!StringUtils.isEmpty(descripcion)){
			desc=descripcion.toLowerCase();
		}
		int numero=1;
		stmt.setString(numero++, desc);
		if (!StringUtils.isEmpty(condicion)){
		stmt.setString(numero++, condicion.toLowerCase());
		}

		rs = stmt.executeQuery();
		while (rs.next()) {
			obj=new PeriodoEscolar();
			obj.setCodigoPeriodo(String.valueOf(rs.getInt("CO_PERIODO")));
			obj.setDescripcion(rs.getString("TX_DESCRIP_PERIODO"));
			obj.setFechaInicio(rs.getString("FE_INICIO"));
			obj.setFechaFin(rs.getString("FE_FIN"));
			obj.setCondicion(rs.getString("IN_PERIODO_ESCOLAR"));
		}
		 rs.close();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return obj;
	}
	
	
	

	/**
	 * 
	 * Lista de periodos anuales
	 * 
	 * @return
	 */
	public List<ValorNombre> tipoPeriodosEscolares() {
	 
		 List<ValorNombre>  objetos= new ArrayList<ValorNombre>();
		 /**Buscamos en BD*/
	 
		 List<String>  listadoPeriodos = cargarPeriodoEscolar();
          if ((listadoPeriodos != null) && (listadoPeriodos.size() > 0))
          {
        	  ValorNombre valorNombre = null;
        	Map<String,String> unico = new HashMap<String,String>();   
            for (int i = 0; i < listadoPeriodos.size(); i += 2)
            {
            	if (!unico.containsKey((String)listadoPeriodos.get(i+1))){
                    valorNombre= new ValorNombre((String)listadoPeriodos.get(i),(String)listadoPeriodos.get(i+1));
                    objetos.add(valorNombre);
            		unico.put((String)listadoPeriodos.get(i+1), (String)listadoPeriodos.get(i+1));
            	}
            }
          
          }
		return objetos;
	}
	

	
	public ArrayList<String> cargarPeriodoEscolar() {
		ManejadorDB manejador =new ManejadorDB();;
		Connection con = manejador.conexion();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<String> periodosEscolares = new ArrayList<String>();
		try {
			log.debug("Entro en el método cargarPeriodoEscolar de la clase Solicitud ");

			StringBuilder sql = new StringBuilder("SELECT CO_PERIODO, TX_DESCRIP_PERIODO FROM RHEI.PERIODO_ESCOLAR ORDER BY TX_DESCRIP_PERIODO DESC");

			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				periodosEscolares.add(rs.getString("TX_DESCRIP_PERIODO"));
				periodosEscolares.add(rs.getString("TX_DESCRIP_PERIODO"));
			}
			log.debug("Contenido de periodos escolares :" + periodosEscolares);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, pstmt, con);
		}
		return periodosEscolares;
	}
 


	
	
	
	public int delete(String codigoPeriodo )
			throws SQLException {
		ManejadorDB manejador =new ManejadorDB();;
		Connection con = manejador.conexion();
		PreparedStatement stmt = null;
		int respuesta = -1;
		String sql = "";
		try {
			sql = "DELETE FROM RHEI.PERIODO_ESCOLAR  WHERE CO_PERIODO=?  ";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, codigoPeriodo);
			respuesta = stmt.executeUpdate();
		} catch (Exception e) {
			respuesta = -1;
		}finally {
			liberarConexion(null, stmt, con);
		}

		return respuesta;
	}

}
