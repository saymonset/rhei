/**
 * 
 */
package com.bcv.dao.jdbc.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;

import ve.org.bcv.rhei.bean.BeneficiarioValNom;
import ve.org.bcv.rhei.util.Constantes;

import com.bcv.dao.jdbc.BeneficiarioDao;
import com.bcv.dao.jdbc.FamiliarDao;
import com.bcv.dao.jdbc.ManejadorDB;
import com.bcv.dao.jdbc.SimpleJDBCDaoImpl;
import com.bcv.dao.jdbc.TrabajadorDao;
import com.bcv.model.Beneficiario;
import com.bcv.model.Familiar;
import com.bcv.model.Trabajador;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 04/08/2015 09:42:40
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class FamiliarDaoImpl  extends SimpleJDBCDaoImpl<Familiar> implements
FamiliarDao {

/**
* 
*/
private static final long serialVersionUID = 1L;
private BeneficiarioDao beneficiarioDao= new BeneficiarioDaoImpl();


/**
 * Consultamos los familiares por numero de solicitud
 * @param nroIdSolicitud
 * @return
 * @throws SQLException
 */
public List<Familiar> consultarFamiliarByNroSolicitudId(long nroIdSolicitud )
		throws SQLException {
	List<Familiar> familiares = new ArrayList<Familiar>();
	ManejadorDB manejadorDB=new ManejadorDB();;
	Connection con = manejadorDB.coneccionPool();	
	ResultSet rs = null;
	PreparedStatement stmt = null;
	StringBuilder sql = new StringBuilder("");
	sql.append(" SELECT  SBEI.NU_SOLICITUD, (PFAMILIARES.CEDULA_FAMILIAR),  (PFAMILIARES.NOMBRE1 || '  ' || PFAMILIARES.NOMBRE2 || '  ' ||  PFAMILIARES.APELLIDO1 || ' ' || PFAMILIARES.APELLIDO2 ) NOMBREFLIA ,PFAMILIARES.FECHA_NACIMIENTO, ");
	sql.append("   TRUNC((MONTHS_BETWEEN(SYSDATE,FECHA_NACIMIENTO))/12) AS EDAD, MSS.MO_PERIODO,MSS.MO_MATRICULA,MSS.MO_APORTE_BCV ");
	sql.append("                               FROM PERSONAL.FAMILIARES  pfamiliares "); 
	sql.append("                                          INNER JOIN RHEI.SOLICITUD_BEI SBEI "); 
	sql.append("                                                      ON PFAMILIARES.CEDULA_FAMILIAR=SBEI.CEDULA_FAMILIAR ");
	sql.append("                                           INNER JOIN RHEI.MOV_ST_SOLIC_BEI MSS ON SBEI.NU_SOLICITUD=MSS.NU_SOLICITUD "); 
	sql.append("                               WHERE SBEI.NU_SOLICITUD=? ");  
	sql.append("                             AND SBEI.CO_EMPLEADO=PFAMILIARES.CODIGO_EMPLEADO AND PFAMILIARES.PARENTESCO='D'  ");
	
	try {
		Familiar familiar = null;
		  boolean isAnd=true;
			 
			sql.append("                                              ORDER BY             PFAMILIARES.CEDULA_FAMILIAR DESC ");      
		stmt=con.prepareStatement(sql.toString());
		int posicion=1;
	    stmt.setLong(posicion++, nroIdSolicitud);
	    
	    
	  
	  
	    
	    
		rs = stmt.executeQuery();
		while (rs.next()) {
		familiar = new Familiar();
		familiar.setNuSolicitud(nroIdSolicitud);
		familiar.setNombreFlia(rs.getString("NOMBREFLIA"));
		familiar.setFechaNacimiento(rs
				.getDate("FECHA_NACIMIENTO"));
		familiar.setEdad(rs.getInt("EDAD"));
		familiar.setMoPeriodo(rs.getDouble("MO_PERIODO"));
		familiar.setMoMatricula(rs.getDouble("MO_MATRICULA"));
		familiar
				.setMoAporteBcv(rs.getDouble("MO_APORTE_BCV"));
		familiares.add(familiar);
		}

	} catch (SQLException e) {
		e.printStackTrace();
	} finally {
		liberarConexion(rs, stmt, con);
	}
	return familiares;
}





public Familiar consultarConyuge(long cedulaEmpl)
		throws SQLException {
	ManejadorDB manejadorDB=new ManejadorDB();;
	Connection con = manejadorDB.coneccionPool();	
	ResultSet rs = null;
	PreparedStatement stmt = null;
	StringBuilder sql = new StringBuilder("");
	sql.append(" SELECT F.CEDULA_FAMILIAR, F.NOMBRE1 ||' '|| F.NOMBRE2 AS NOMBRE,F.APELLIDO1 ||' '|| F.APELLIDO2 AS APELLIDO ");
	sql.append(" FROM  PERSONAL.FAMILIARES F WHERE F.NU_CEDULA =? AND F.STATUS=? AND F.PARENTESCO=? ");  
	
	Familiar familiar = null;
	try {
		stmt=con.prepareStatement(sql.toString());
		int posicion=1;
	    stmt.setLong(posicion++, cedulaEmpl);
	    stmt.setString(posicion++, Constantes.STATUS);
	    stmt.setString(posicion++, Constantes.CONYUGE);
	    
	  
	    
	    
		rs = stmt.executeQuery();
		while (rs.next()) {
		familiar = new Familiar();
		familiar.setNombre(rs.getString("NOMBRE"));
		familiar.setApellido(rs.getString("APELLIDO"));
		familiar.setCedulaFamiliar(rs.getString("CEDULA_FAMILIAR"));
		
		}

	} catch (SQLException e) {
		e.printStackTrace();
	} finally {
		liberarConexion(rs, stmt, con);
	}
	return familiar;
}


/**
 * Consultamos los familiares por numero de solicitud
 * @param nroIdSolicitud
 * @return
 * @throws SQLException
 */
public Familiar consultarFamiliarBycedulaFamiliar(long cedulaFamiliar )
		throws SQLException {
	ManejadorDB manejadorDB=new ManejadorDB();;
	Connection con = manejadorDB.coneccionPool();	
	ResultSet rs = null;
	PreparedStatement stmt = null;
	StringBuilder sql = new StringBuilder("");
	sql.append(" SELECT  SBEI.NU_SOLICITUD, (PFAMILIARES.CEDULA_FAMILIAR),  (PFAMILIARES.NOMBRE1 || '  ' || PFAMILIARES.NOMBRE2 || '  ' ||  PFAMILIARES.APELLIDO1 || ' ' || PFAMILIARES.APELLIDO2 ) NOMBREFLIA ,PFAMILIARES.FECHA_NACIMIENTO, ");
	sql.append("   TRUNC((MONTHS_BETWEEN(SYSDATE,FECHA_NACIMIENTO))/12) AS EDAD, MSS.MO_PERIODO,MSS.MO_MATRICULA,MSS.MO_APORTE_BCV ");
	sql.append("                               FROM PERSONAL.FAMILIARES  pfamiliares "); 
	sql.append("                                          INNER JOIN RHEI.SOLICITUD_BEI SBEI "); 
	sql.append("                                                      ON PFAMILIARES.CEDULA_FAMILIAR=SBEI.CEDULA_FAMILIAR ");
	sql.append("                                           INNER JOIN RHEI.MOV_ST_SOLIC_BEI MSS ON SBEI.NU_SOLICITUD=MSS.NU_SOLICITUD "); 
	sql.append("                               WHERE PFAMILIARES.CEDULA_FAMILIAR=? ");       

 
	Familiar familiar = null;
	try {
	
		  boolean isAnd=true;
			 
			sql.append("                                              ORDER BY             PFAMILIARES.CEDULA_FAMILIAR DESC ");      
		stmt=con.prepareStatement(sql.toString());
		int posicion=1;
	    stmt.setLong(posicion++, cedulaFamiliar);
	    
	  
	    
	    
		rs = stmt.executeQuery();
		while (rs.next()) {
		familiar = new Familiar();
		familiar.setNuSolicitud(rs.getInt("NU_SOLICITUD"));
		familiar.setNombreFlia(rs.getString("NOMBREFLIA"));
		familiar.setFechaNacimiento(rs
				.getDate("FECHA_NACIMIENTO"));
		familiar.setEdad(rs.getInt("EDAD"));
		familiar.setMoPeriodo(rs.getDouble("MO_PERIODO"));
		familiar.setMoMatricula(rs.getDouble("MO_MATRICULA"));
		familiar
				.setMoAporteBcv(rs.getDouble("MO_APORTE_BCV"));
		
		}

	} catch (SQLException e) {
		e.printStackTrace();
	} finally {
		liberarConexion(rs, stmt, con);
	}
	return familiar;
}


public boolean existeBeneficiario(String coStatus,String cedulaFamiliar)
		throws SQLException {
	boolean existe=false;
	Connection con = null;
	ResultSet rs = null;
	PreparedStatement stmt = null;
	try {
		long ceduFam=0;
		if (!StringUtils.isEmpty(cedulaFamiliar) && StringUtils.isNumeric(cedulaFamiliar)){
			ceduFam = Integer.parseInt(cedulaFamiliar);
		}
		ManejadorDB manejadorDB=new ManejadorDB();;
	con = manejadorDB.coneccionPool();	

		StringBuilder sql = new StringBuilder("");
		sql.append(" select SB.NU_SOLICITUD from RHEI.SOLICITUD_BEI  sb ");
		sql.append("     inner join  RHEI.MOV_ST_SOLIC_BEI msb on SB.NU_SOLICITUD=MSB.NU_SOLICITUD and MSB.CO_STATUS=? ");     
		sql.append("    where SB.CEDULA_FAMILIAR=?");
			stmt=con.prepareStatement(sql.toString());
			int posicion=1;
			stmt.setString(posicion++, coStatus);
		    stmt.setLong(posicion++, ceduFam);
		    
		  
		    
		    
			rs = stmt.executeQuery();
			while (rs.next()) {
				existe=true;
				break;
			
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			liberarConexion(rs, stmt, con);
		}
		return existe;
  
}






public List<BeneficiarioValNom> getBeneficiarioValNom(int cedula,
		boolean rangoEdad) throws ServletException, IOException {
	Trabajador trabajador = new Trabajador();
	Beneficiario beneficiario = new Beneficiario();
	ArrayList<String> duplaParametroTipo = null;
	trabajador.setCedula(0);
	List<BeneficiarioValNom> beneficiarios = new ArrayList<BeneficiarioValNom>();
	int edadMin = -1;
	int edadMax = -1;
	ArrayList<String> listaFiltros = null;
	ArrayList<String> listaFlia = null;
	trabajador.setCedula(cedula);
	TrabajadorDao trabajadorDao = new TrabajadorDaoImpl();
	try {
		listaFiltros = (ArrayList<String>) trabajadorDao
				.buscarFiltros(cedula);

		/** CBUSCAMOS EL PARAMETRO EDAD MIN Y EDAD MAX */
		if (listaFiltros != null && listaFiltros.size() > 1) {
			/** Obtenemos el codigo de la compania */
			String compania = listaFiltros != null ? (String) listaFiltros
					.get(0) : "";
			String tipoEmp = listaFiltros.get(1) != null ? (String) listaFiltros
					.get(1) : "";

			/** Inicio Obtenemos el valor de la edad minima */

			duplaParametroTipo = (ArrayList<String>) trabajadorDao
					.buscarParametro(compania, tipoEmp,
							Constantes.TIPOBENEFICIO, Constantes.EDADIN);

			if (duplaParametroTipo != null && duplaParametroTipo.size() > 0
					&& !duplaParametroTipo.isEmpty()) {
				edadMin = Integer
						.parseInt(duplaParametroTipo != null ? ((String) duplaParametroTipo
								.get(0)).toString() : "");
			}

			/** Fin Obtenemos el valor de la edad minima */
			/** Inicio Obtenemos el valor de la edad maxima */
			duplaParametroTipo = (ArrayList<String>) trabajadorDao
					.buscarParametro(
							listaFiltros.get(0) != null ? (String) listaFiltros
									.get(0) : "",
							listaFiltros.get(1) != null ? (String) listaFiltros
									.get(1) : "", Constantes.TIPOBENEFICIO,
							Constantes.EDADFI);
			if (duplaParametroTipo != null && duplaParametroTipo.size() > 0
					&& !duplaParametroTipo.isEmpty()) {
				edadMax = Integer
						.parseInt(duplaParametroTipo != null ? (String) duplaParametroTipo
								.get(0) : "");
			}

			/** Fin Obtenemos el valor de la edad maxima */

			/** Inicio De la tabla PERSONAL.FAMILIARES , */
			if (rangoEdad) {
				/**
				 * Buscamos lista de familiares por ranfgo de edad min y
				 * edad max
				 */
				listaFlia = beneficiarioDao.buscarBeneficiarioII(
						trabajador.getCedula(), edadMin, edadMax);
			} else {
				listaFlia = beneficiarioDao
						.buscarBeneficiarioConSolicitudII(trabajador
								.getCedula());
			}
			/** Fin De la tabla PERSONAL.FAMILIARE */

			if ((listaFlia != null) && (listaFlia.size() != 0)) {
				BeneficiarioValNom benef = null;
				for (int i = 0; i < listaFlia.size(); i += 3) {
					benef = new BeneficiarioValNom();
					benef.setValor((String) listaFlia.get(i).toString());
					benef.setNombre((String) listaFlia.get(i).toString()
							+ " - " + (String) listaFlia.get(i + 1) + " "
							+ (String) listaFlia.get(i + 2));
					beneficiarios.add(benef);

				}
			}
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	return beneficiarios;
}

}
