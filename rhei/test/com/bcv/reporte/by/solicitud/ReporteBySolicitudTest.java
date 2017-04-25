package com.bcv.reporte.by.solicitud;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import ve.org.bcv.rhei.bean.Recaudo;
import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.negocio.SolConsultarSearchInfoPartI;
import ve.org.bcv.rhei.report.ReportePathLogo;
import ve.org.bcv.rhei.report.by.benef.Reporte;

import com.fileupload.ListRecaudos;

public class ReporteBySolicitudTest {
	public static void main(String[] args) {
		ReporteBySolicitudTest reportTest= new ReporteBySolicitudTest();
		reportTest.testReport();
	}

	public void testReport() {
		ShowResultToView showResultToView=null;
		SolConsultarSearchInfoPartI solConsultarSearchInfoPartI;
		try {
			int nroSolicitudNotKnow=0;
			/**Se consulta el empleado con el beneiciario con que se hizo la solicitud**/
			solConsultarSearchInfoPartI = new SolConsultarSearchInfoPartI(
					"14176742","14674","11843","01","J306356029-C",nroSolicitudNotKnow);
			showResultToView=solConsultarSearchInfoPartI.ejecutar();
			List<Recaudo> lista= new ArrayList<Recaudo>();
			ListRecaudos listRecaudos = new ListRecaudos();
			boolean titleComeResourcebundle=false;
			lista =listRecaudos.listRecaudos("2016-2017/J306356029-C/14176742/11843",titleComeResourcebundle);
			showResultToView.setRecaudos(lista);
			/**Fin Se consulta el empleado con el beneiciario con que se hizo la solicitud**/
			  Reporte reporte= new ReporteBySolicitudTestImpl(showResultToView);
			  InputStream jrxml = ReporteBySolicitudTest.class.getResourceAsStream(
						"reportSolicitudDatosPrincipales1.jrxml");
			  String fileOut="reporte.pdf";
			  Map parameters = new HashMap();
			  ReportePathLogo archivo = new ReportePathLogo();
    		    InputStream is = archivo.getClass().getResourceAsStream(
				"logo_bcv.jpg"); 
    		    parameters.put("logo",is);
			  reporte.generar(jrxml,parameters,fileOut);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	 
	}

}
