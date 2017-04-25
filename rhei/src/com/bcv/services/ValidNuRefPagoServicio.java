/**
 * 
 */
package com.bcv.services;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.commons.lang3.StringUtils;

import com.bcv.dao.jdbc.RelacionDePagosDao;
import com.bcv.dao.jdbc.impl.RelacionDePagosDaoImpl;
import com.bcv.dto.NuRefPago;
import com.bcv.dto.ValidNuRefPago;
import com.enums.Mes;

import ve.org.bcv.rhei.util.Constantes;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco 01/03/2016 08:48:36 2016 mail :
 *         oraclefedora@gmail.com
 */
@Path("/validNuRefPagoServicio")
public class ValidNuRefPagoServicio {
	private RelacionDePagosDao relacionDePagosDao = new RelacionDePagosDaoImpl();
	// @POST
	// @Produces("application/json")
	// public ValidNuRefPago getData(int nroSolicitud,double montoBcv)
	// {
	//
	// /**Trabajamos con json
	// * */
	// List<NuRefPago> nuRefPagoLst= null;
	// ValidNuRefPago validNuRefPago=null;
	// try {
	// nuRefPagoLst=
	// relacionDePagosDao.nuRefPagosComplHistorico(nroSolicitud,"");
	// } catch (NumberFormatException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// if (nuRefPagoLst==null ){
	// nuRefPagoLst= new ArrayList<NuRefPago>();
	// }
	// validNuRefPago = new ValidNuRefPago();
	// validNuRefPago.setHistorico(nuRefPagoLst);
	// validNuRefPago.setBcvVigencia(new Date());
	// BigDecimal b = new BigDecimal(montoBcv);
	// validNuRefPago.setBcvMonto(b);
	// /**Fin Trabajamos con json*/
	// return validNuRefPago;
	// }

	@GET
	@Path("/{nroSolicitud}/{meses}/{montoBCV}/{moMatricula}/{moPeriodo}/{periodoEscolar}")
	@Produces("application/json")
	public ValidNuRefPago getJson(@PathParam("nroSolicitud") long nroSolicitud,
			@PathParam("meses") String meses,
			@PathParam("montoBCV") String montoBCV,@PathParam("moMatricula") String moMatricula,@PathParam("moPeriodo") String moPeriodo
			,@PathParam("periodoEscolar") String periodoEscolar) {

		List<NuRefPago> nuRefSinPagoLst = new ArrayList<NuRefPago>();;
		List<NuRefPago> nuRefPagoLst = new ArrayList<NuRefPago>();;;
		ValidNuRefPago validNuRefPago = null;
		/**Iera fase --- cargar data***********************************************************/
		/**Tenemos el monto del bcv*/
		BigDecimal montoBCVBigDec = new BigDecimal(montoBCV);
		BigDecimal moMatriculaBigDec = new BigDecimal(moMatricula);
		BigDecimal moPeriodoBigDec = new BigDecimal(moPeriodo);
		try {
			/** Agarrar los que estan en base de datos */
			nuRefPagoLst = relacionDePagosDao.nuRefPagosComplHistorico(
					nroSolicitud, meses,periodoEscolar);
			/**
			 * LLenar los que no se encontarron en BD porque no han sido pagados teniendo
			 * como referencia los que fueron pagados
			 */
			if (!StringUtils.isEmpty(meses)) {
				/**recorremos cada mes y chequeamos...*/
				StringTokenizer stk = new StringTokenizer(meses, ",");

				while (stk.hasMoreTokens()) {
					boolean encontrado = false;
					String mesnoPagado = (String) stk.nextToken();
					for (NuRefPago nuRefPago : nuRefPagoLst) {
						/** si entra es porque el mes fue pagado*/
						if (nuRefPago.getMes().equalsIgnoreCase(mesnoPagado)) {
							encontrado = true;
							break;
						}
					}
					/** si entra es porque el mes NO fue pagado*/
					if (!encontrado) {
						NuRefPago nuRefPago = new NuRefPago();
						nuRefPago.setMoAporteBcv(montoBCVBigDec);
						nuRefPago.setMoMatricula(moMatriculaBigDec);
						nuRefPago.setMoPeriodo(moPeriodoBigDec);
						nuRefPago.setMes(mesnoPagado);
						nuRefPago.setFechaFactura(null);
						nuRefPago.setMontoPago(new BigDecimal(0d));
						nuRefSinPagoLst.add(nuRefPago);
					}
				}

			}

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		if (nuRefPagoLst == null) {
			nuRefPagoLst = new ArrayList<NuRefPago>();
		}
		/** agregamos los que no han sido pagados tambien */
		nuRefPagoLst.addAll(nuRefSinPagoLst);
		/**END Iera fase --- cargar data***********************************************************/
		
		/**II fase --- acumulamos el monto pagado por cada mes o matricula***********************************************************/
		/***
		 * Hacemos el acumulativo de dinero por cada mes y lo agrupamos para que
		 * sean unicos
		 */
		Map<String, NuRefPago> unicos = new HashMap<String, NuRefPago>();
		for (NuRefPago nuRefPago : nuRefPagoLst) {
			/**Si no esta el mes, lo guardamos..*/
			if (!unicos.containsKey(nuRefPago.getMes())) {
				unicos.put(nuRefPago.getMes(), nuRefPago);
			} else {
				/**Si esta el mes, sumamos el monto que tenia mas el siguiente para acumularlo..*/
				NuRefPago nuRefPagoAux = unicos.get(nuRefPago.getMes());
				BigDecimal big = nuRefPagoAux.getMontoPago().add(
						nuRefPago.getMontoPago());
				nuRefPagoAux.setMontoPago(big);
				unicos.put(nuRefPago.getMes(), nuRefPagoAux);
			}
		}
		/**END II fase --- acumulamos el monto pagado por cada mes o matricula***********************************************************/
	
		/** Por cada valos del mes, colocamos su nombre **/
		BigDecimal montoAcumuladoPorMes;
	
		BigDecimal acumuladorAllMes = new BigDecimal(0);
		BigDecimal acumuladorBCV = new BigDecimal(0);
		nuRefPagoLst = new ArrayList<NuRefPago>();
		/**Tenemos en el map unicos. todo los meses y cada mes con el acumulado de montoPago*/
		for (Map.Entry<String, NuRefPago> entry : unicos.entrySet()) {
			String mesKey = entry.getKey();
			NuRefPago pagoPorMes = (NuRefPago) entry.getValue();
			pagoPorMes.setMes(obtenerNameMes(new Integer(mesKey)));
			pagoPorMes.setMesKey(pagoPorMes.getMes() + mesKey);
			/**Se trabajara con el monto de matricula, bcv y periodo actuales que vienen como parametros*/
			pagoPorMes.setMoAporteBcv(montoBCVBigDec);
			pagoPorMes.setMoMatricula(moMatriculaBigDec);
			pagoPorMes.setMoPeriodo(moPeriodoBigDec);
			/**Fin se trabajara con el monto de matricula, bcv y periodo actuales que vienen como parametros*/
			
			if (Constantes.MATRICULA_KEY.equalsIgnoreCase(mesKey)){
				/**chequearemos por matricula*/
				/** El monto del aporte del bcv debe ser menor o igual al monto de la matricula*/
				 if ( pagoPorMes.getMoMatricula().compareTo(pagoPorMes.getMoAporteBcv()) < 0 ){
					 pagoPorMes.setMoAporteBcv(pagoPorMes.getMoMatricula());
				 }
			}else{
				/**chequearemos por periodo*/
				/** El monto del aporte del bcv debe ser menor o igual al monto del periodo*/
				 if ( pagoPorMes.getMoPeriodo().compareTo(pagoPorMes.getMoAporteBcv()) < 0 ){
					 pagoPorMes.setMoAporteBcv(pagoPorMes.getMoPeriodo());
				 }
			}
			/**montoBCVBigDec es un auxiliar del monto bcv para restarlos por el acumulado del monto de mes pago
			 * si la resta es negativa, no podremos pagar mas
			 * */
			montoAcumuladoPorMes = pagoPorMes.getMoAporteBcv().subtract(pagoPorMes.getMontoPago());
			pagoPorMes.setIsCanPayMes(montoAcumuladoPorMes.intValue() > 0);
			nuRefPagoLst.add(pagoPorMes);
			/**recuperamos el monto original del bcv*/
			montoBCVBigDec =  pagoPorMes.getMoAporteBcv();
			/**acumulamos el monto del bcv*/
			acumuladorBCV = acumuladorBCV.add(montoBCVBigDec);
			/**acumulamos de todos los meses*/
			acumuladorAllMes = acumuladorAllMes.add(pagoPorMes.getMontoPago());
		}

		/**acumuladorBCV es el monto bcv para restarlos por el acumulado del monto de mes pago acumuladorByMes
		 * si la resta es negativa, no podremos pagar mas
		 * */
		MathContext mc = new MathContext(2); // 2 precision
		BigDecimal montoAcumuladoPorMesTotal = acumuladorBCV.subtract(acumuladorAllMes, mc);

		validNuRefPago = new ValidNuRefPago();
	
		validNuRefPago.setIsCanPayTotal(montoAcumuladoPorMesTotal.intValue() > 0);
		validNuRefPago.setHistorico(nuRefPagoLst);
		validNuRefPago.setBcvVigencia(new Date());
		validNuRefPago.setAcumuladorBCV(acumuladorBCV);
		validNuRefPago.setAcumuladorByMes(acumuladorAllMes);
		validNuRefPago.setBcvMonto(montoBCVBigDec);

		return validNuRefPago;
	}

	public String obtenerNameMes(int indMes) {
		String name = "";
		/** Buscamos el nombre del mes a traves del enum */
		for (Mes mes : Mes.values()) {
			if (mes.getValue() == indMes) {
				name = mes.name();

				if (name.length() > 0 && name.charAt(name.length() - 1) == 'P') {
					name = name.substring(0, name.length() - 1);
				}

				break;
			}
		}
		return name;
	}

}
