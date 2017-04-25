/**
 * 
 */
package com.bcv.services;

import java.util.List;

import com.bcv.model.DetalleDefHistorico;
import com.bcv.reporte.pagotributo.PagoTributoBean;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 22/08/2016 11:25:06
 * 2016
 * mail : oraclefedora@gmail.com
 */
public interface DetalleDefinititvoService {
	List<DetalleDefHistorico>  updateReporteStatusDeshacer(String observacion,String coRepStatus,List<PagoTributoBean> lista,String[] numeroDesactivar);
}
