/**
 * 
 */
package com.bcv.reporte.by.solicitud;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fileupload.UploadFile;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco 18/08/2016 10:53:51 2016 mail :
 *         oraclefedora@gmail.com
 */
@WebServlet(description = "open file", urlPatterns = {"/openFileServlet"})
public class OpenFileServlet extends HttpServlet implements Serializable {

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
		OutputStream outStream = response.getOutputStream();
	    File f=null;
		try {
			   f = new File( getServletContext().getRealPath("/pdf/FORMATOAUTORIZACIONPERSONAJURIDICANATURAL.pdf") );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String fileName = "FORMATOAUTORIZACIONPERSONAJURIDICANATURAL.pdf";
		response.setContentType("application/pdf");
		response.setContentLength((int) f.length());
//		response.setHeader("Content-disposition", "inline; filename=\""
//				+ fileName + "\"");
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
