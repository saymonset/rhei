/**
 * 
 */
package com.bcv.dao.jdbc;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 03/09/2015 10:38:45
 * 2015
 * mail : oraclefedora@gmail.com
 */
public interface MovStSolicBeiDao {
	void SearchMontoBCV(String codigoCompania,Double montoBcv);
	 boolean isSolicitudActivaParaRealizarPago(String cedulaFamiliar);
}
