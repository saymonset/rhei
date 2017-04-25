/**
 * 
 */
package com.bcv.dao.jdbc;

import java.util.List;

import com.bcv.model.DetalleDefHistorico;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 22/08/2016 14:57:18
 * 2016
 * mail : oraclefedora@gmail.com
 */
public interface DefinititvoHistoricoDao {
	DetalleDefHistorico insert(DetalleDefHistorico detalleDef) throws Exception;
	List<DetalleDefHistorico> select(String coRepStatus) throws Exception;
}
