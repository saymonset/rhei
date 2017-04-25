/**
 * 
 */
package com.bcv.dao.jdbc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;

import ve.org.bcv.rhei.bean.BeneficiarioValNom;

import com.bcv.model.Familiar;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 04/08/2015 09:36:27
 * 2015
 * mail : oraclefedora@gmail.com
 */
public interface FamiliarDao extends SimpleJDBCDao<Familiar> {
	List<Familiar> consultarFamiliarByNroSolicitudId(long nroIdSolicitud)	throws SQLException;
	 List<BeneficiarioValNom> getBeneficiarioValNom(int cedula,
				boolean rangoEdad) throws ServletException, IOException ;
	 Familiar consultarFamiliarBycedulaFamiliar(long cedulaFamiliar )	throws SQLException;
	 Familiar consultarConyuge(long cedulaEmpl)throws SQLException ;
	 boolean existeBeneficiario(String coStatus,String cedulaFamiliar)
				throws SQLException ;
}
