/**
 * 
 */
package com.bcv.dao.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;

import com.bcv.model.BeneficioEscolar;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 10/08/2015 15:29:02
 * 2015
 * mail : oraclefedora@gmail.com
 */
public interface BeneficioEscolarDao {
	  ArrayList<String> buscarBeneficiosEscolares() ;
	  ArrayList<String> buscarBeneficioEscolar(int indMenor, int indMayor) ;
	  int cuantosSql(String tablaBD) ;
	  String generadorTablaBeneficioEscolar(String tablaBD, int indMenor,
				int indMayor);
	  String guardarBeneficioEscolar(String accion,String codigoBeneficio,String descripcion,String fechaRegistro,String condicion ) throws SQLException ;
	  boolean existeBeneficioEscolar(String codigoBeneficio) throws SQLException ;
	  BeneficioEscolar findParametro(String codigoBeneficio) throws SQLException ;
	  int delete(String codigoBeneficio) ;
	  

}
