package com.test.delete;

import java.math.BigDecimal;

import net.sf.jasperreports.engine.JRException;

import net.sf.jasperreports.engine.JasperReport;

import net.sf.jasperreports.engine.design.JasperDesign;

import net.sf.jasperreports.engine.util.JRLoader;

import net.sf.jasperreports.engine.xml.JRXmlWriter;

public class ConvertJasper {
	public static String sourcePath, destinationPath, xml;

	public static JasperDesign jd = new JasperDesign();

	public static void main(String[] args) {

		String h="2.822,00";
		
		
//		BigDecimal g = new BigDecimal();
//		System.out.println(g);
//		sourcePath = "D:/simon/weblogic1035/user_projects/domains/rhcs/inforeportes/reporteMei9.jasper";
//
//		destinationPath = "D:/simon/weblogic1035/user_projects/domains/rhcs/inforeportes/reporteMei9.jrxml";
//
//		try {
//
//			JasperReport report = (JasperReport) JRLoader
//					.loadObject(sourcePath);
//
//			JRXmlWriter.writeReport(report, destinationPath, "UTF-8");
//System.out.println("Finished");
//		} catch (JRException e) {
//
//			e.printStackTrace();
//
//		}
	}
}
