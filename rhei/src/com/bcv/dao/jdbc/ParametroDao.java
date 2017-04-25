/**
 * 
 */
package com.bcv.dao.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;

import com.bcv.model.Parametro;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 11/08/2015 08:28:45
 * 2015
 * mail : oraclefedora@gmail.com
 */
public interface ParametroDao {
	ArrayList<String> buscarParametros(String beneficioEscolar,
			String filtroParametro, String companiaAnalista, int indMenor,
			int indMayor);
	int cuantosSql(String beneficioEscolar, String filtroParametro,
			String companiaAnalista);
	String generadorTablaParametros(String beneficioEscolar,
			String tablaBD, String filtroParametro, String companiaAnalista,
			int indMenor, int indMayor);
	 boolean buscarParametro(String tipoBeneficio,String codigoParametro)
				throws SQLException;
	 String guardarParametro(
				String accion,String codigoCompania,String tipoEmpleado,String tipoBeneficio,String codigoParametro,String fechaVigencia,
				String valorParametro,String tipoDatoParametro,String observaciones) throws SQLException;
	 boolean existeParametro(String codigoCompania,String tipoEmpleado,String tipoBeneficio,String codigoParametro,String fechaVigencia)
				throws SQLException;
	 Parametro findParametro(String codigoCompania,String tipoEmpleado,String tipoBeneficio,String codigoParametro,String fechaVigencia)
				throws SQLException ;
	 int deleteParametro(String codigoCompania,String tipoEmpleado,String tipoBeneficio,String codigoParametro,String fechaVigencia)
				throws SQLException;
	 Parametro findAbreviaturaFirmasReporte()
				throws SQLException ;
	 Parametro findNB_FirmaReporte() throws SQLException ;
	
}
