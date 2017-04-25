/**
 * 
 */
package com.bcv.servicesImpl;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import ve.org.bcv.rhei.bean.DocumentosBean;
import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.open.doc.DocumentOpenPagosTributosRporte;
import ve.org.bcv.rhei.report.ReportePathLogo;
import ve.org.bcv.rhei.report.by.benef.Reporte;
import ve.org.bcv.rhei.util.Constantes;
import ve.org.bcv.rhei.util.n2t;
import ve.org.bcv.rhei.util.pdf;

import com.bcv.dao.jdbc.ParametroDao;
import com.bcv.dao.jdbc.RelacionDePagosDao;
import com.bcv.dao.jdbc.SolicitudDao;
import com.bcv.dao.jdbc.TipoEmpleadoDao;
import com.bcv.dao.jdbc.impl.ParametroDaoImpl;
import com.bcv.dao.jdbc.impl.RelacionDePagosDaoImpl;
import com.bcv.dao.jdbc.impl.SolicitudDaoImpl;
import com.bcv.dao.jdbc.impl.TipoEmpleadoDaoImpl;
import com.bcv.model.Parametro;
import com.bcv.model.TipoEmpleado;
import com.bcv.reporte.pagotributo.MemorandoReport;
import com.bcv.reporte.relacionpago.ReporteByRpago;
import com.bcv.services.ReporteContableService;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco 24/08/2016 11:18:32 2016 mail : oraclefedora@gmail.com
 */
public class ReporteContableServiceImpl implements ReporteContableService {

	private ParametroDao parametroDao = new ParametroDaoImpl();
	private TipoEmpleadoDao tipoEmpleadoDao = new TipoEmpleadoDaoImpl();
	private SolicitudDao solicitudDao = new SolicitudDaoImpl();
	private BigDecimal montoTotal =   new BigDecimal(0);
	private int countJub = 0;
	public File reportesPorPartes(String definitivosReporte) throws Exception {
		ResourceBundle rb = ResourceBundle.getBundle("ve.org.bcv.rhei.util.bundle");
		DocumentOpenPagosTributosRporte documentOpenPagosTributosRporte = new DocumentOpenPagosTributosRporte();
		String companiaAnalista = "01";
		Map parameters = null;
		parameters = new HashMap();
		RelacionDePagosDao relacionDePagosDaoImpl = new RelacionDePagosDaoImpl();
		List<TipoEmpleado> tipoEmpleados = new ArrayList<TipoEmpleado>();
		StringTokenizer codReportDefinitivosstk = new StringTokenizer(definitivosReporte, ",");

		if (codReportDefinitivosstk != null) {
			boolean updateParameter = false;
			int pageReport = -9;// No se usa este numero
			boolean reporteOriginal = false;// No se usa la variable
			String ninoEspecial = "0";
			ShowResultToView showResultToView =null;
			montoTotal = new BigDecimal(0);
			countJub = 0;
			while (codReportDefinitivosstk.hasMoreTokens()) {
				String coRepStatusStr = (String) codReportDefinitivosstk.nextToken();
				tipoEmpleados = tipoEmpleadoDao.tipoEmpleadosByReporte(Integer.parseInt(coRepStatusStr));
				/** Si no es consulta individual */
				String numSolicitud = relacionDePagosDaoImpl.listNumSolicitudsReporteDefinitivoTransitorio(Integer.parseInt(coRepStatusStr));
				ninoEspecial = solicitudDao
						.regularOrtEspecial(numSolicitud);
				showResultToView = documentOpenPagosTributosRporte.reportesPorPartesShowResultToView(numSolicitud, companiaAnalista, Integer.parseInt(coRepStatusStr), ninoEspecial);
				
				montoTotal = montoTotal.add(showResultToView.getMontoTotalBiDec());
				countJub += showResultToView.getNumJubilados();
			}
		}

		boolean isEmpleado = false;
		boolean isObrero = false;
		for (TipoEmpleado te : tipoEmpleados) {
			String tipoEmpleado = te.getTipoEmp();
			if ("SOB".equalsIgnoreCase(tipoEmpleado) || "EMP".equalsIgnoreCase(tipoEmpleado) || "CON".equalsIgnoreCase(tipoEmpleado) || "JUB".equalsIgnoreCase(tipoEmpleado)
					|| "EJE".equalsIgnoreCase(tipoEmpleado)) {
				isEmpleado = true;
			} else if ("OBR".equalsIgnoreCase(tipoEmpleado) || "OBC".equalsIgnoreCase(tipoEmpleado)) {
				isObrero = true;
			}
		}

		Parametro parametro = null;
		parameters.put("tipoEmpl", "");
		int numeroRelacionPago = 0;
		parametro = parametroDao.findParametro(companiaAnalista, "EMP", "RHEI", "SEC_EMP", null);
		numeroRelacionPago = Integer.parseInt(parametro.getValorParametro());
		if (isEmpleado && isObrero) {
			parameters.put("tipoEmpl", rb.getString("reporte.micromemorando.tipoEmpl") + "," + rb.getString("reporte.micromemorando.tipoObrero"));
		} else if (isEmpleado) {
			parameters.put("tipoEmpl", rb.getString("reporte.micromemorando.tipoEmpl"));
		} else if (isObrero) {
			parametro = parametroDao.findParametro(companiaAnalista, "EMP", "RHEI", "SEC_OBR", null);
			numeroRelacionPago = Integer.parseInt(parametro.getValorParametro());
			parameters.put("tipoEmpl", rb.getString("reporte.micromemorando.tipoObrero"));
		}

		/** Pagos y tributos */
		String fileOut = "reporteContable.pdf";
		InputStream jrxml = null;
		// GENERAMOS EL REPORTE
		Reporte reporte = null;

		ReportePathLogo archivo = null;

		InputStream is = null;

		jrxml = ReporteByRpago.class.getResourceAsStream("MicroMemorando.jrxml");

		reporte = new MemorandoReport();
		InputStream microMemorando = ReporteByRpago.class.getResourceAsStream("MicroMemorando.jrxml");

		archivo = new ReportePathLogo();
		is = archivo.getClass().getResourceAsStream("logo_bcv.jpg");
		parameters.put("logo", is);

		parameters.put("cordUnidadContabilidad", rb.getString("reporte.cordUnidadContabilidad"));

		parametro = new Parametro();
		parametro = parametroDao.findParametro(companiaAnalista, "EMP", "RHEI", Constantes.CORCON, null);
		if (null != parametro) {
			parameters.put("cordUnidadContabilidad", parametro.getValorParametro());
		}

		parameters.put("letra1", rb.getString("reporte.micromemorando.letra1"));
		n2t numero = new n2t(numeroRelacionPago);
		String res = numero.convertirLetras(numeroRelacionPago);
		parameters.put("numReportePortipoEmpl", res + " (" + numeroRelacionPago + ")");
		parameters.put("letra2", rb.getString("reporte.micromemorando.letra2"));
		parameters.put("letra3", rb.getString("reporte.micromemorando.letra3"));
		parameters.put("letra4", rb.getString("reporte.micromemorando.letra4"));
		parameters.put("letra5", rb.getString("reporte.micromemorando.letra5"));

		n2t numeroJubilados = new n2t(countJub);
		String resJubilados = numeroJubilados.convertirLetras(countJub);

		parameters.put("numJubilados", resJubilados + " (" + countJub + ")");
		parameters.put("letra6", rb.getString("reporte.micromemorando.letra6"));

		Date fechaActual = new Date();
		SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyy");
		String hoy = formato.format(fechaActual);
		parameters.put("fechadiamesanio", hoy.toString());

		
		/*** Calculamos el total en el memorando con su formato *******/
		DecimalFormat df2 = new DecimalFormat(Constantes.FORMATO_DOUBLE, new DecimalFormatSymbols(new Locale("es", "VE")));
	 
		parameters.put("montoTotal", new String(df2.format(montoTotal.floatValue())));
		/*** Fin Calculamos el total en el memorando con su formato *******/
		
		
		parameters.put("fecha", hoy.toString());

		parameters.put("cordBenefSocioEconomic", rb.getString("reporte.cordBenefSocioEconomic"));
		parametro = parametroDao.findParametro(companiaAnalista, "EMP", "RHEI", Constantes.CORBEN, null);
		if (null != parametro) {
			parameters.put("cordBenefSocioEconomic", parametro.getValorParametro());
		}

		parameters.put("cordUnidadContabilidad", rb.getString("reporte.cordUnidadContabilidad"));
		parametro = parametroDao.findParametro(companiaAnalista, "EMP", "RHEI", Constantes.CORCON, null);
		if (null != parametro) {
			parameters.put("cordUnidadContabilidad", parametro.getValorParametro());
		}
		DocumentosBean documentosBean = reporte.generar(microMemorando, parameters, "microMemorando.pdf");
		File result = pdf.inputStreamToFile(documentosBean.getInputStream());
		/******** Fin Reporte memorando ************/

		return result;

	}

}
