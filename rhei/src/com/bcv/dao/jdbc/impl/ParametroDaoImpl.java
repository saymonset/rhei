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
import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ve.org.bcv.rhei.util.Constantes;
import ve.org.bcv.rhei.util.Utilidades;

import com.bcv.dao.jdbc.ManejadorDB;
import com.bcv.dao.jdbc.ParametroDao;
import com.bcv.dao.jdbc.SimpleJDBCDaoImpl;
import com.bcv.model.Parametro;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 11/08/2015 08:29:14
 * 2015
 * mail : oraclefedora@gmail.com
 */
@Named("parametroDao")
public class ParametroDaoImpl  extends SimpleJDBCDaoImpl<Parametro> implements
ParametroDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ParametroDaoImpl.class.getName());

	
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
			condicion= " AND LOWER(CO_PARAMETRO) LIKE ('" + filtroParametro.toLowerCase()
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
	
	
	/**
	 * numero de registros que me trae la consuilta
	 * 
	 * @param beneficioEscolar
	 * @param filtroParametro
	 * @param companiaAnalista
	 * @return
	 */
	public int cuantosSql(String beneficioEscolar, String filtroParametro,
			String companiaAnalista) {
		ManejadorDB manejador=new ManejadorDB();;
		Connection con = null;	
		ResultSet rs = null;
		PreparedStatement stmt = null;
		companiaAnalista="01";
	 
		int cuantos = 0;
		String sql = "";

		String condicion = " AND CO_PARAMETRO LIKE ('" + filtroParametro
				+ "%')";

		if (companiaAnalista.compareTo("01") == 0) {
			sql = "SELECT   count(*)  as cuantos   FROM RHEI.PARAMETRO WHERE CO_TIPO_BENEFICIO = ? "
					+ condicion;
		} else {
			sql = "SELECT   count(*)  as cuantos FROM RHEI.PARAMETRO WHERE CO_TIPO_BENEFICIO=? AND CO_COMPANIA='"
					+ companiaAnalista + "' " + condicion;
		}
		try {
			con = manejador.coneccionPool();
			stmt = con.prepareStatement(sql);
			if (beneficioEscolar.compareTo("%") != 0) {
				stmt.setString(1, beneficioEscolar);
				log.debug("Query: " + sql);
			}
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
	
	
	public String generadorTablaParametros(String beneficioEscolar,
			String tablaBD, String filtroParametro, String companiaAnalista,
			int indMenor, int indMayor) {
		String encabezado = "<table class=\"anchoTabla5\"><caption align=\"top\"><h2>Tabla de Par&aacute;metros del Sistema</h2></caption><tr><th width=\"8%\">Nombre</th><th width=\"8%\">Tipo de Dato</th><th width=\"28%\" class=\"centrado\">Valor</th><th width=\"10%\">Fecha de Vigencia</th><th width=\"18%\">Asociado al Beneficio Escolar</th><th width=\"13%\">Aplica a la compa&ntilde;ia</th><th width=\"15%\">Aplica al Tipo de Empleado</th><th>Eliminar</th></tr>";

		String endTable = "";
		String tabla = encabezado;
		String contenedor = "";
		String estilo = "";
		ArrayList<String> listaParametros = null;

		listaParametros = buscarParametros(beneficioEscolar, filtroParametro,
				companiaAnalista, indMenor, indMayor);
		if ((listaParametros != null) && (listaParametros.size() != 0)) {
			String name="";
			boolean formatNumerico=false;
			for (int i = 0; i < listaParametros.size(); i += 8) {
				formatNumerico=false;
				estilo = Utilidades.modificarEstiloTextArea("mayuscula",
						((String) listaParametros.get(i + 5)).length(), 60);
				if (estilo.compareTo("style=\"line-height: 32px;\"") == 0) {
					contenedor = (String) listaParametros.get(i + 5);
					log.debug("Valor de estilo: " + estilo);
				} else {
					contenedor = "<textarea name=\"valorParametro"
							+ i
							+ "\"  tabindex=\"\" id=\"\" cols=\"60\" rows=\"2\" onfocus=\"blur()\" "
							+ estilo + ">"
							+ (String) listaParametros.get(i + 5)
							+ "</textarea>";
				}
				String tipoEmpleado=(String)listaParametros.get(i + 1);
				if ("%".equalsIgnoreCase(listaParametros.get(i + 1))){
					/**sI TODOS %, HAY QUE COLOCAR VACIO PORQUE DA ERROR CON HREF DE HTML*/
					tipoEmpleado="";
				}
				
				name=(String) listaParametros.get(i + 3);
				if ("MTOBCV".equalsIgnoreCase(name)){
					if (StringUtils.isNumeric(contenedor)){
						/*** Calculamos el total en el memorando con su formato *******/
						DecimalFormat df2 = new DecimalFormat(Constantes.FORMATO_DOUBLE,
								new DecimalFormatSymbols(new Locale("es", "VE")));
						BigDecimal value = new BigDecimal(contenedor);
						contenedor=new String(df2.format(value.floatValue()));
						formatNumerico=true;
						/*** Fin Calculamos el total en el memorando con su formato *******/
					}
				}
//MTOBCV
				tabla = tabla
						+ "<tr><td><a href=\"ParametroControlador?principalParametro.do=findParametro&operacion=modificar&tabla="
						+ tablaBD + "&beneficioEscolar=" + beneficioEscolar
						+ "&compania=" + (String) listaParametros.get(i)
						+ "&tipoEmp=" + (String) listaParametros.get(i + 1)
						+ "&tipoBeneficio="
						+ (String) listaParametros.get(i + 2)
						+ "&nombreParametro="
						+ name
						+ "&fechaVigencia="
						+ (String) listaParametros.get(i + 4) + "&test="+listaParametros.get(i + 5)+"&formatNumerico="+formatNumerico+"&other=''\">"
						+name + "</a></td>"
						+ "<td>" + (String) listaParametros.get(i + 6)
						+ "</td>" + "<td>" + contenedor + "</td>" + "<td>"
						+ (String) listaParametros.get(i + 4) + "</td>"
						+ "<td>" + (String) listaParametros.get(i + 2)
						+ "</td>" + "<td>" + (String) listaParametros.get(i)
						+ "</td>" + "<td>"
						+ (String) listaParametros.get(i + 1) + "</td>"
						
						//						PARA ELIMINAR, PERO SE QUITO..beneficioEscolar SOLO SE HACE CON QUERYS EL ADMINISTRADOR
//				        +"<td><a href=\"ParametroControlador?principalParametro.do=delete&operacion=modificar&tabla="
//						+ tablaBD + "&beneficioEscolar=" + beneficioEscolar
//						+ "&compania=" + (String) listaParametros.get(i)
//						+ "&tipoEmp=" + tipoEmpleado
//						+ "&tipoBeneficio="
//						+ (String) listaParametros.get(i + 2)
//						+ "&nombreParametro="
//						+ (String) listaParametros.get(i + 3)
//						+ "&fechaVigencia="
//						+ (String) listaParametros.get(i + 4) +"&other=''\"  onClick=\"return deleteParam()\">"
//						+ "..." + "</a></td>";
				+"</tr>";
				
		

			}
			tabla = tabla + "</table>";
			
			endTable = "<table><tr><th  style=\"background: white;\" width=\"44%\"></th><th  style=\"background: white;\" width=\"3%\"><a href=\"javascript:void(0)\"  onclick=\"paginandoParametros('p','"
					+ beneficioEscolar
					+ "','"+filtroParametro+"')\" ><input type=\"image\" src=\"/rhei/imagenes/arrow-first.gif\" alt=\"Primero\" /></a></th><th style=\"background: white;\" width=\"3%\" ><a  href=\"javascript:void(0)\"  onclick=\"paginandoParametros('a','"
					+ beneficioEscolar
					+ "','"+filtroParametro+"')\" ><input type=\"image\" src=\"/rhei/imagenes/arrow-fr.gif\" alt=\"Anterior\" /></a></th><th style=\"background: white;\" width=\"3%\"><a href=\"javascript:void(0)\"  onclick=\"paginandoParametros('s','"
					+ beneficioEscolar
					+ "','"+filtroParametro+"')\" ><input type=\"image\" src=\"/rhei/imagenes/arrow-ff.gif\" alt=\"Siguiente\" /></a></th><th style=\"background: white;\" width=\"3%\"><a href=\"javascript:void(0)\"  onclick=\"paginandoParametros('u','"
					+ beneficioEscolar
					+ "','"+filtroParametro+"')\" ><input type=\"image\" src=\"/rhei/imagenes/arrow-last.gif\" alt=\"Ultimo\" /></a></th><th  style=\"background: white;\" width=\"44%\"></th></tr>";
			endTable += "</table>";
			tabla += "<br>" + endTable;

			log.debug("Contenido de tabla: " + tabla);
		} else if (listaParametros.size() == 0) {
			log.debug("No se encontraron parametros asociados al beneficio escolar");
			tabla = "No hay registros";
			log.debug("Mensaje contiene:" + tabla);
		}
		return tabla;
	}

	
	


	public boolean buscarParametro(String tipoBeneficio,String codigoParametro)
			throws SQLException {
		String sql = "SELECT * FROM RHEI.PARAMETRO WHERE CO_TIPO_BENEFICIO = ? AND CO_PARAMETRO = ?";
		ManejadorDB manejador=new ManejadorDB();;
		Connection con = null;	
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		boolean respuesta = false;
		try {
			con = manejador.coneccionPool();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, tipoBeneficio);
			pstmt.setString(1, codigoParametro);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				log.debug("Ya existe ese parametro en la base de datos");
				respuesta = true;
			}
			rs.close();
		} catch (Exception e) {
		}finally {
			liberarConexion(rs, pstmt, con);
		}
		
		return respuesta;
	}
	
	

	public String guardarParametro(
			String accion,String codigoCompania,String tipoEmpleado,String tipoBeneficio,String codigoParametro,String fechaVigencia,
			String valorParametro,String tipoDatoParametro,String observaciones) throws SQLException {
		
		ManejadorDB manejador=new ManejadorDB();;
		Connection con = null;	
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int resultado = 0;
		String mensaje = "";
		String sql = "";

		log.debug("Estoy dentro del método guardarParametro de la clase Parametro....");
		if (accion.compareToIgnoreCase("crear") == 0) {
			sql = "INSERT INTO RHEI.PARAMETRO (CO_COMPANIA, TIPO_EMP, CO_TIPO_BENEFICIO, CO_PARAMETRO, FE_VIGENCIA, TX_VALOR_PARAMETRO, IN_TIPO_PARAMETRO, TX_OBSERVACIONES, IN_ST_PROCESAMIENT) VALUES (?, ?, ?, ?, TO_DATE(?, 'DD-MM-YYYY'), ?, ?, ?, ?)";
		} else if (accion.compareToIgnoreCase("modificar") == 0) {
			sql = "UPDATE RHEI.PARAMETRO SET TX_VALOR_PARAMETRO   =?, IN_TIPO_PARAMETRO   =?, TX_OBSERVACIONES    =? , FE_VIGENCIA       =TO_DATE(?, 'DD-MM-YYYY HH24:MI:SS') WHERE CO_COMPANIA        =? AND TIPO_EMP          =? AND CO_TIPO_BENEFICIO =? AND CO_PARAMETRO      =? ";
		}
		try {
			con = manejador.coneccionPool();
			stmt = con.prepareStatement(sql);
			if (accion.compareToIgnoreCase("crear") == 0) {
				stmt.setString(1, codigoCompania);
				stmt.setString(2, tipoEmpleado);
				stmt.setString(3, tipoBeneficio);
				stmt.setString(4, codigoParametro);
				stmt.setString(5, fechaVigencia);
				stmt.setString(6, valorParametro);
				stmt.setString(7, tipoDatoParametro);
				stmt.setString(8, observaciones);
				if (codigoParametro.compareToIgnoreCase("MTOBCV") == 0) {
					stmt.setString(9, "P");
				} else {
					stmt.setString(9, "N");
				}
				if (codigoParametro.compareToIgnoreCase("MTOUTILES") == 0) {
					stmt.setString(10, "P");
				} else {
					stmt.setString(10, "N");
				}
				resultado = stmt.executeUpdate();
				log.debug("Valor de la variable resultado opcion crear: "
						+ resultado);
				if (resultado == 1) {
					log.debug("El método se ejecutó exitosamente!!!");
					mensaje = "Exito";
				} else {
					log.debug("El método fallo!!!");
					mensaje = "Fracaso";
				}
			} else {
				if (accion.compareToIgnoreCase("modificar") == 0) {
					stmt.setString(1, valorParametro);
					stmt.setString(2, tipoDatoParametro);
					stmt.setString(3, observaciones);
					stmt.setString(4, fechaVigencia);
					stmt.setString(5, codigoCompania);
					stmt.setString(6, tipoEmpleado);
					stmt.setString(7, tipoBeneficio);
					stmt.setString(8, codigoParametro);
					
					try {
						resultado = stmt.executeUpdate();
					
						
					} catch (Exception e) {
						e.printStackTrace();
						log.debug(e.toString());
					}

					log.debug("Valor de la variable resultado opcion modificar: "
							+ resultado);
				}
				if (resultado == 1) {
					log.debug("El método se ejecutó exitosamente!!!");
					mensaje = "Exito";
				} else {
					log.debug("El método fallo!!!");
					mensaje = "Fracaso";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			liberarConexion(rs, stmt, con);
		}
		
		return mensaje;
	}
	

	

	public boolean existeParametro(String codigoCompania,String tipoEmpleado,String tipoBeneficio,String codigoParametro,String fechaVigencia)
			throws SQLException {
		ManejadorDB manejador=new ManejadorDB();
		Connection con = null;	
		ResultSet rs = null;
		PreparedStatement stmt = null;
		boolean resultado = false;
		try {
			con = manejador.coneccionPool();
			int numeroRegistros = -1;
			String sql = "";
			sql = "SELECT COUNT(*) FROM RHEI.PARAMETRO WHERE CO_COMPANIA        = ? AND TIPO_EMP          = ? AND CO_TIPO_BENEFICIO = ? AND CO_PARAMETRO      = ? AND FE_VIGENCIA       = TO_DATE(?, 'DD-MM-YYYY HH24:MI:SS')";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, codigoCompania);
			stmt.setString(2, tipoEmpleado);
			stmt.setString(3, tipoBeneficio);
			stmt.setString(4, codigoParametro);
			stmt.setString(5, fechaVigencia);

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
			rs.close();
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			liberarConexion(rs, stmt, con);
		}
		
		return resultado;
	}

	
	
	
	public Parametro findAbreviaturaFirmasReporte()
			throws SQLException {
		String codigoCompania="01";
		ManejadorDB manejador=new ManejadorDB();
		Connection con = null;	
		ResultSet rs = null;
		PreparedStatement stmt = null;
		Parametro param = new Parametro();
		param.setCodigoCompania("-1"); // it is empty
		boolean resultado = false;
		int numeroRegistros = -1;
		String sql = "";

		sql = "SELECT CO_COMPANIA, TIPO_EMP, CO_TIPO_BENEFICIO, CO_PARAMETRO, TO_CHAR(FE_VIGENCIA, 'DD-MM-YYYY') AS FECHA, TX_VALOR_PARAMETRO, IN_TIPO_PARAMETRO, TX_OBSERVACIONES  "
				+ "FROM RHEI.PARAMETRO WHERE CO_COMPANIA        = ? AND TIPO_EMP          = ? AND CO_TIPO_BENEFICIO = ? AND CO_PARAMETRO      = ? ";
try {
	con = manejador.coneccionPool();
	stmt = con.prepareStatement(sql);
	stmt.setString(1, codigoCompania);
	stmt.setString(2, "EMP");
	stmt.setString(3,"RHEI" );
	stmt.setString(4, "FIRREP");

	rs = stmt.executeQuery();
	while (rs.next()) {
		param.setCodigoCompania(rs.getString("CO_COMPANIA"));
		param.setTipoEmpleado(rs.getString("TIPO_EMP"));
		param.setTipoBeneficio(rs.getString("CO_TIPO_BENEFICIO"));
		param.setCodigoParametro(rs.getString("CO_PARAMETRO"));
		param.setFechaVigencia(rs.getString("FECHA"));
		param.setValorParametro(rs.getString("TX_VALOR_PARAMETRO"));
		param.setTipoDatoParametro(rs.getString("IN_TIPO_PARAMETRO"));
		param.setObservaciones(rs.getString("TX_OBSERVACIONES"));

	}

	log.debug("Contenido de numeroRegistros :" + numeroRegistros);
	log.debug("Contenido de resultado :" + resultado);
	rs.close();
} catch (Exception e) {
	// TODO: handle exception
}finally {
	liberarConexion(rs, stmt, con);
}
		
		return param;
	}
	
	public Parametro findNB_FirmaReporte()
			throws SQLException {
		String codigoCompania="01";
		ManejadorDB manejador=new ManejadorDB();
		Connection con = null;	
		ResultSet rs = null;
		PreparedStatement stmt = null;
		Parametro param = new Parametro();
		param.setCodigoCompania("-1"); // it is empty
		boolean resultado = false;
		int numeroRegistros = -1;
		String sql = "";

		sql = "SELECT CO_COMPANIA, TIPO_EMP, CO_TIPO_BENEFICIO, CO_PARAMETRO, TO_CHAR(FE_VIGENCIA, 'DD-MM-YYYY') AS FECHA, TX_VALOR_PARAMETRO, IN_TIPO_PARAMETRO, TX_OBSERVACIONES  "
				+ "FROM RHEI.PARAMETRO WHERE CO_COMPANIA        = ? AND TIPO_EMP          = ? AND CO_TIPO_BENEFICIO = ? AND CO_PARAMETRO      = ? ";
try {
	con = manejador.coneccionPool();
	stmt = con.prepareStatement(sql);
	stmt.setString(1, codigoCompania);
	stmt.setString(2, "EMP");
	stmt.setString(3,"RHEI" );
	stmt.setString(4, "FIRMA");

	rs = stmt.executeQuery();
	while (rs.next()) {
		param.setCodigoCompania(rs.getString("CO_COMPANIA"));
		param.setTipoEmpleado(rs.getString("TIPO_EMP"));
		param.setTipoBeneficio(rs.getString("CO_TIPO_BENEFICIO"));
		param.setCodigoParametro(rs.getString("CO_PARAMETRO"));
		param.setFechaVigencia(rs.getString("FECHA"));
		param.setValorParametro(rs.getString("TX_VALOR_PARAMETRO"));
		param.setTipoDatoParametro(rs.getString("IN_TIPO_PARAMETRO"));
		param.setObservaciones(rs.getString("TX_OBSERVACIONES"));

	}

	log.debug("Contenido de numeroRegistros :" + numeroRegistros);
	log.debug("Contenido de resultado :" + resultado);
	rs.close();
} catch (Exception e) {
	// TODO: handle exception
}finally {
	liberarConexion(rs, stmt, con);
}
		
		return param;
	}
	
	

	public Parametro findParametro(String codigoCompania,String tipoEmpleado,String tipoBeneficio,String codigoParametro,String fechaVigencia)
			throws SQLException {
		if (null == codigoCompania ||  "".equalsIgnoreCase(codigoCompania)){
			codigoCompania=Constantes.CODIGO_COMPANIA;
		}
		ManejadorDB manejador=new ManejadorDB();
		Connection con = null;	
		ResultSet rs = null;
		PreparedStatement stmt = null;
		Parametro param = new Parametro();
		param.setCodigoCompania("-1"); // it is empty
		boolean resultado = false;
		int numeroRegistros = -1;
		String sql = "";

		sql = "SELECT CO_COMPANIA, TIPO_EMP, CO_TIPO_BENEFICIO, CO_PARAMETRO, TO_CHAR(FE_VIGENCIA, 'DD-MM-YYYY') AS FECHA, TX_VALOR_PARAMETRO, IN_TIPO_PARAMETRO, TX_OBSERVACIONES  "
				+ "FROM RHEI.PARAMETRO WHERE CO_COMPANIA        = ? AND TIPO_EMP          = ? AND CO_TIPO_BENEFICIO = ? AND CO_PARAMETRO      = ?";
try {
	con = manejador.coneccionPool();
	stmt = con.prepareStatement(sql);
	stmt.setString(1, codigoCompania);
	stmt.setString(2, tipoEmpleado);
	stmt.setString(3, tipoBeneficio);
	stmt.setString(4, codigoParametro);
	//stmt.setString(5, fechaVigencia);

	rs = stmt.executeQuery();
	while (rs.next()) {
		param.setCodigoCompania(rs.getString("CO_COMPANIA"));
		param.setTipoEmpleado(rs.getString("TIPO_EMP"));
		param.setTipoBeneficio(rs.getString("CO_TIPO_BENEFICIO"));
		param.setCodigoParametro(rs.getString("CO_PARAMETRO"));
		param.setFechaVigencia(rs.getString("FECHA"));
		param.setValorParametro(rs.getString("TX_VALOR_PARAMETRO"));
		param.setTipoDatoParametro(rs.getString("IN_TIPO_PARAMETRO"));
		param.setObservaciones(rs.getString("TX_OBSERVACIONES"));

	}

	log.debug("Contenido de numeroRegistros :" + numeroRegistros);
	log.debug("Contenido de resultado :" + resultado);
	rs.close();
} catch (Exception e) {
	// TODO: handle exception
}finally {
	liberarConexion(rs, stmt, con);
}
		
		return param;
	}
	
	public double montoBcv (String compania)throws SQLException{
		ManejadorDB manejador=new ManejadorDB();
		Connection con = null;	
		ResultSet rs = null;
		PreparedStatement stmt = null;
		Parametro param = new Parametro();
		param.setCodigoCompania("-1"); // it is empty
		double monto=0.0d;
		StringBuilder sql = new StringBuilder("");
		sql.append("select tx_valor_parametro from RHEI.PARAMETRO p where P.CO_PARAMETRO=?   AND P.CO_COMPANIA=?");

		
try {
	con = manejador.coneccionPool();
	stmt = con.prepareStatement(sql.toString());
	int posicion=1;
	stmt.setString(posicion++, Constantes.NOMBREPARAMETRO);
	stmt.setString(posicion++, compania);

	rs = stmt.executeQuery();
	if (rs.next()){
		monto=rs.getDouble("tx_valor_parametro");
	}
} catch (Exception e) {
	e.printStackTrace();
}finally {
	liberarConexion(rs, stmt, con);
}
		
	return monto;	
	}

	public int deleteParametro(String codigoCompania,String tipoEmpleado,String tipoBeneficio,String codigoParametro,String fechaVigencia)
			throws SQLException {
		ManejadorDB manejador=new ManejadorDB();
		Connection con = null;	
		ResultSet rs = null;
		PreparedStatement stmt = null;
		Parametro param = new Parametro();
		param.setCodigoCompania("-1"); // it is empty
		int numeroRegistros = -1;
		String sql = "";

		sql = " DELETE  "
				+ " FROM RHEI.PARAMETRO WHERE CO_COMPANIA        = ? AND TIPO_EMP          = ? AND CO_TIPO_BENEFICIO = ? AND CO_PARAMETRO      = ? AND FE_VIGENCIA       = TO_DATE(?, 'DD-MM-YYYY HH24:MI:SS')";
try {
	con = manejador.coneccionPool();
	stmt = con.prepareStatement(sql);
	stmt.setString(1, codigoCompania);
	stmt.setString(2, tipoEmpleado);
	stmt.setString(3, tipoBeneficio);
	stmt.setString(4, codigoParametro);
	stmt.setString(5, fechaVigencia);

	numeroRegistros = stmt.executeUpdate();
} catch (Exception e) {
	// TODO: handle exception
}finally {
	liberarConexion(rs, stmt, con);
}
		
		 
		return numeroRegistros;
	}


}
