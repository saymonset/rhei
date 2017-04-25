/**
 * 
 */
package com.bcv.dao.jdbc;


/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 11/08/2015 10:38:52
 * 2015
 * mail : oraclefedora@gmail.com
 */
public interface BcvDao  {
	String cargarCompaniaAnalista(int cedula);
    Double sueldoMinimo();
    Double MontoUtiles(String tipoEmpl);
	

}
