/**
 * 
 */
package com.bcv.dao.jdbc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;

import com.bcv.model.CentroEducacionInicial;

import ve.org.bcv.rhei.bean.Proveedor;
import ve.org.bcv.rhei.bean.ShowResultToView;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 10/08/2015 15:47:35
 * 2015
 * mail : oraclefedora@gmail.com
 */
public interface CentroEducacionInicialDao {
	 List<Proveedor> listaProvEducativos(String companiaAnalista)
				throws ServletException, IOException;
	 List<Proveedor> buscarCentroEducacionFiltro(String co_cia_analista,String nombre);
 
	 ShowResultToView cargarAtributosCentroEducativo(ShowResultToView showResultToView,String nroRif,String nombreCentro,String localidad,String telefono,String correo);
	 CentroEducacionInicial buscarCentroByRif( String nroRif)
		      throws SQLException;
	 CentroEducacionInicial buscarCentroByLocalidad(  String localidad,String nroRif)
   	      throws SQLException;
	 CentroEducacionInicial infoCentroEducativo(  String nroRif) 	      throws SQLException;
}
