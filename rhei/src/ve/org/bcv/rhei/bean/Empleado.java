package ve.org.bcv.rhei.bean;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.bcv.dao.jdbc.ManejadorDB;

/**
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com
 * 26/01/2015 15:55:10
 * Listamos Empleados
 */
public class Empleado implements Serializable {
	private static Logger log = Logger.getLogger(Empleado.class
			.getName());

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String valor;
	private String nombre;
	private List<Empleado> empleados;
	
	
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public List<Empleado> getEmpleados() {
		
		if (empleados==null|| empleados.isEmpty() || empleados.size()==0 ){
			empleados=llenarData();
		}
		return empleados;
	}
	public void setEmpleados(List<Empleado> empleados) {
		this.empleados = empleados;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	 
	
	/**
	 * LLenamos los Tipos de datos
	 */
	public List<Empleado> llenarData(){
		ManejadorDB manejador = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		 
		ResultSet rs = null;
		manejador =  new ManejadorDB();;
		con = manejador.conexion();
		String sql = "SELECT TIPO_EMP, DESCRIPCION "
				+ " FROM PERSONAL.TIPOS_EMPLEADOS ORDER BY DESCRIPCION";
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			empleados=new ArrayList<Empleado>();
			Empleado empleado=new Empleado();
			while (rs.next()) {
				 empleado=new Empleado();
				empleado.setValor(rs.getString("TIPO_EMP"));
				empleado.setNombre(rs.getString("DESCRIPCION"));
				empleados.add(empleado);
			}
			empleado=new Empleado();
			empleado.setValor("%");
			empleado.setNombre("Todos");
			empleados.add(empleado);


		} catch (Exception e) {
			log.error(e.toString());
		}finally{
			try {
				rs.close();
			} catch (SQLException e1) {
			}
			try {
				pstmt.close();
			} catch (SQLException e1) {
			}
			try {
				con.close();
			} catch (SQLException e) {
			 
			}
		}
	
		
		
		
		  
		return empleados;
	}
}
