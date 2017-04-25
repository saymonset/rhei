/**
 * 
 */
package com.bcv.servicesImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.bcv.dao.jdbc.DefinititvoHistoricoDao;
import com.bcv.dao.jdbc.ReporteEstatusDao;
import com.bcv.dao.jdbc.impl.DefinititvoHistoricoDaoImpl;
import com.bcv.dao.jdbc.impl.ReporteEstatusDaoImpl;
import com.bcv.model.DetalleDefHistorico;
import com.bcv.reporte.pagotributo.PagoTributoBean;
import com.bcv.services.DetalleDefinititvoService;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco 22/08/2016 14:13:58 2016 mail : oraclefedora@gmail.com
 */
public class DetalleDefinititvoServiceImpl implements DetalleDefinititvoService {
	private DefinititvoHistoricoDao definititvoHistoricoDao = new DefinititvoHistoricoDaoImpl();
	private ReporteEstatusDao reporteEstatusDao = new ReporteEstatusDaoImpl();
	public List<DetalleDefHistorico>  updateReporteStatusDeshacer(String observacion,String coRepStatus,List<PagoTributoBean> lista, String[] numeroDesactivar) {
		List<DetalleDefHistorico> listaHistorico=new ArrayList<DetalleDefHistorico>();
		if (numeroDesactivar != null && numeroDesactivar.length > 0) {
			DetalleDefHistorico detalleDefinititvo =null;
			
				try {
					
					/**Guardamos historico en bd del numSolicitud Eliminado que se encuentra en el  definitivo */
					for (PagoTributoBean obj:lista){
						String numSol=obj.getNuSolicitud()+"";
						for (int i=0;i<numeroDesactivar.length;i++){
							if (numSol.equalsIgnoreCase(numeroDesactivar[i])){
								reporteEstatusDao.updateReporteStatusDeshacer(numeroDesactivar[i]);
								  detalleDefinititvo = new DetalleDefHistorico();
								  detalleDefinititvo.setCoRepStatus(coRepStatus);
									detalleDefinititvo.setConcepto(obj.getConcepto());
									detalleDefinititvo.setMontoMensualPagTrab(new BigDecimal(
											Double.valueOf(Double
													.parseDouble(obj.getMontoMensualPagTrab()
															.replace(".", "").replace(",", ".")))
											));
								 
									detalleDefinititvo.setMontoTotal(new BigDecimal(
											Double.valueOf(Double
													.parseDouble(obj.getMontoTotalStr()
															.replace(".", "").replace(",", ".")))
											));
									
									
									detalleDefinititvo.setNombreColegio(obj.getNombreColegio());
									detalleDefinititvo.setNombreNino(obj.getNombreNino());
									detalleDefinititvo.setTrabajador(obj.getTrabajador());
									detalleDefinititvo.setObservacion(observacion);
									definititvoHistoricoDao.insert(detalleDefinititvo);
								break;
							}
						}
					
					}
					//listaHistorico=definititvoHistoricoDao.select( coRepStatus);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
		}
		return listaHistorico;
	}

}
