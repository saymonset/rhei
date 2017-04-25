package com.bcv.servicesImpl;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.bcv.dao.jdbc.BcvDao;
import com.bcv.dao.jdbc.ParametroDao;
import com.bcv.dao.jdbc.PeriodoEscolarDao;
import com.bcv.dao.jdbc.impl.ParametroDaoImpl;
import com.bcv.dao.jdbc.impl.PeriodoEscolarDaoImpl;
import com.bcv.model.Parametro;
import com.bcv.model.PeriodoEscolar;
import com.bcv.reporte.pagotributo.MemorandoReport;
import com.bcv.reporte.pagotributo.PagoTributoCEI;
import com.bcv.services.ReporteUtlilesService;

import ve.org.bcv.rhei.bean.DocumentosBean;
import ve.org.bcv.rhei.report.ReportePathLogo;
import ve.org.bcv.rhei.report.by.benef.Reporte;
import ve.org.bcv.rhei.util.Constantes;
import ve.org.bcv.rhei.util.pdf;

public class ReporteMemoListBecaServiceImpl implements ReporteUtlilesService {
	private ParametroDao parametroDao = new ParametroDaoImpl();
	private PeriodoEscolarDao periodoEscolarDao = new PeriodoEscolarDaoImpl();
	@Inject
	private BcvDao bcvDao;

	public File ReporteMemoListBeca() throws Exception {
//		ResourceBundle rb = ResourceBundle.getBundle("ve.org.bcv.rhei.util.bundle");
//		Map parameters = null;
//		parameters = new HashMap();
//		Parametro parametro = null;
//
//		/** Pagos y tributos */
//		// GENERAMOS EL REPORTE
//		Reporte reporte = null;
//		ReportePathLogo archivo = null;
//		String companiaAnalista = "01";
//		InputStream is = null;
//
//		reporte = new MemorandoReport();
//		InputStream reporteUtiles = PagoTributoCEI.class.getResourceAsStream("ReporteUtiles.jrxml");
//
//		archivo = new ReportePathLogo();
//		is = archivo.getClass().getResourceAsStream("logo_bcv.jpg");
//		parameters.put("logo", is);
//
//		parametro = new Parametro();
//
//		PeriodoEscolar periodoEscolar = periodoEscolarDao.findPeriodoByDescripcionLast();
//
//		parameters.put("descripPeriodo", "");
//		parameters.put("tipoEmpl", "");
//		if (periodoEscolar != null && !StringUtils.isEmpty(periodoEscolar.getDescripcion())) {
//			parameters.put("descripPeriodo", periodoEscolar.getDescripcion());
//		}
//
//		// System.out.println(rb==null);
//
//		parameters.put("FIRMA", rb.getString("reporte.microUtiles"));
//		parametro = parametroDao.findParametro(companiaAnalista, "EMP", "RHEI", Constantes.FIRMA, null);
//		if (null != parametro) {
//			parameters.put("FIRMA", parametro.getValorParametro());
//		}
//
//		parameters.put("FIRREP", rb.getString("reporte.microUtiles"));
//		parametro = parametroDao.findParametro(companiaAnalista, "EMP", "RHEI", Constantes.FIRREP, null);
//		if (null != parametro) {
//			parameters.put("FIRREP", parametro.getValorParametro());
//		}
//		parameters.put("departamentofirma", rb.getString("reporte.microUtiles"));
//		parametro = parametroDao.findParametro(companiaAnalista, "EMP", "RHEI", Constantes.departamentofirma, null);
//		if (null != parametro) {
//			parameters.put("departamentofirma", parametro.getValorParametro());
//		}
//
//		parameters.put("utiles1", rb.getString("reporte.microUtiles.utiles1"));
//		parameters.put("utiles2", rb.getString("reporte.microUtiles.utiles2"));
//		parameters.put("utiles3", rb.getString("reporte.microUtiles.utiles3"));
//		parameters.put("montoUtiles", "0");
//
//		DecimalFormat df2 = new DecimalFormat(Constantes.FORMATO_DOUBLE, new DecimalFormatSymbols(new Locale("es", "VE")));
//		BigDecimal value = new BigDecimal(bcvDao.MontoUtiles());
//		String valorParametro = new String(df2.format(value.floatValue()));
//
//		if (null != valorParametro) {
//			parameters.put("montoUtiles", valorParametro);
//		}
//
//
//		Date fechaActual = new Date();
//		SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyy");
//		String hoy = formato.format(fechaActual);
//		parameters.put("fechadiamesanio", hoy.toString());
//
//		parameters.put("fecha", hoy.toString());
//
//		DocumentosBean documentosBean = reporte.generar(reporteUtiles, parameters, "ReporteUtiles.pdf");
//		File result = pdf.inputStreamToFile(documentosBean.getInputStream());
		/******** Fin Reporte memoUtiles ************/

//		return result;
		return null;

	}

	public File reportesPorPartes(String numSolicitudPorPartes) throws Exception {
		File s = ReporteMemoListBeca();
		return s;
	}

}
