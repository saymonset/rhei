/**
 * 
 */
package com.bcv.dao.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bcv.model.ReporteEstatus;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco 09/10/2015 08:22:50 2015 mail :
 *         oraclefedora@gmail.com
 */
public interface ReporteEstatusDao {
	List<ReporteEstatus> searchReporteEstatus(String txDescriPeriodo,String filtro, int indMenor,int indMayor);
	ArrayList<String> buscarParametros(String beneficioEscolar,
			String filtroParametro, String companiaAnalista, int indMenor,
			int indMayor);

	int cuantosSql();
	 long updateDefinitivoReporte(long coRepStatus,String nombreUsuario)	throws SQLException;
	 long deleteTransitorioReporte(long coRepStatus)throws SQLException;	
	 String updateReporteStatus(long codigoReporteStatus,String numSolicitudPorPartes,String meses,String descripPeriodo,int receptorPago,
				int coFormaPago1,String filtrarByMesOrComplementoOrAmbos,Connection con) throws Exception;
	 boolean isAllDefinitivos();
	 List<ReporteEstatus> searchTransitorio();
	 boolean existeReporteStatusNameAnio(String name,String anio);
	 long deleteReporteStatusBad(int coRepStatus)	throws SQLException;
	 String nameReporteStatusNameAnio(int coRepStatus);	 
	 ReporteEstatus reporteStatus(int coRepStatus);
		long guardar(String nombreReporte,String receptorPago,Connection con,String nombreUsuario )throws SQLException;
		String updateReporteStatusII(String numSoli,String mes,String descripPeriodo,int receptorPago,int coFormaPago1,String filtrarByMesOrComplementoOrAmbos,Connection con) throws Exception;
		String updateReporteStatusDeshacer(String numSoli) throws Exception;
		String nombreReporte(long coRepStatus);
}
