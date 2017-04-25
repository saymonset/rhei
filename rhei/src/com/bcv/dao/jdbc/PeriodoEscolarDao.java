/**
 * 
 */
package com.bcv.dao.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ve.org.bcv.rhei.bean.ValorNombre;

import com.bcv.model.PeriodoEscolar;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco 11/08/2015 09:13:26 2015 mail :
 *         oraclefedora@gmail.com
 */
public interface PeriodoEscolarDao {
	int cuantosSql();
	ArrayList<String> buscarPeriodoEscolar(int indMenor, int indMayor);
	String buscarPeriodoEscolar(String codigoPeriodo);
	String buscarPeriodoEscolarByNuFactura(String nu_factura);
	String generadorTablaPeriodoEscolar(String tablaBD, int indMenor,
			int indMayor);
	String guardarPeriodoEscolar(String accion, String descripcion,
			String fechaInicio, String fechaFin, String condicion,
			String codigoPeriodo) throws SQLException;
	PeriodoEscolar findParametro(String codigoPeriodo) throws SQLException;
	PeriodoEscolar findPeriodoByDescripcion(String descripcion)
			throws SQLException;
	PeriodoEscolar findPeriodoByDescripcion(String descripcion ,String condicion)
			throws SQLException ;
	List<ValorNombre> tipoPeriodosEscolares();
	ArrayList<String> cargarPeriodoEscolar();
	int delete(String codigoPeriodo) throws SQLException;
	 PeriodoEscolar findPeriodoByDescripcionLast()	throws SQLException ;
	 PeriodoEscolar findByMvStatusSolicitudActiva(String numSolicitud)
				throws SQLException;

}
