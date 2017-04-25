/**
 * 
 */
package com.bcv.reporte.by.solicitud;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bcv.services.ReporteContableService;
import com.bcv.servicesImpl.ReporteContableServiceImpl;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 24/08/2016 10:29:27
 * 2016
 * mail : oraclefedora@gmail.com
 */
@WebServlet(description = "open file", urlPatterns = {"/reporteContableOpenServlet"})
public class ReporteContableOpenServlet  extends HttpServlet implements Serializable {
	private ReporteContableService reporteContableServiceImpl= new ReporteContableServiceImpl();
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doSame(req, res);
	}
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doSame(req, res);
	}

	public void doSame(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String reporteContableDefinitivo=request.getParameter("reporteContableDefinitivo")!=null?(String)request.getParameter("reporteContableDefinitivo"):"";
		 File f=null;
		try {
			f=reporteContableServiceImpl.reportesPorPartes(reporteContableDefinitivo);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		OutputStream outStream = response.getOutputStream();
	    
	//	System.out.println("reporteContableDefinitivo="+request.getParameter("reporteContableDefinitivo"));
		
		String fileName = "ReporteContable.pdf";
		response.setContentType("application/pdf");
		response.setContentLength((int) f.length());
		response.setHeader("Content-disposition",
				"attachment; filename=\"" +fileName+   "\"");

		byte[] buf = new byte[8192];
		FileInputStream inStream = new FileInputStream(f);
		int sizeRead = 0;
		while ((sizeRead = inStream.read(buf, 0, buf.length)) > 0) {
			outStream.write(buf, 0, sizeRead);
		}
		inStream.close();
		outStream.close();

	}
	
	
	
	
	
	
	
	
}
