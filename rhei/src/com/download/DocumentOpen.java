package com.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alfresco.main.ExceuteCmisMain;
import com.exception.ExceptionCmis;

/**
 * Servlet implementation class DocumentOpen
 */
public class DocumentOpen extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(DocumentOpen.class);
 

	private enum MimeType {
		pdf, doc, dot, pps, ppt, xl, xls, xml, txt;
	}

	 
	/**
	 * Tamaño del Buffer por defecto.
	 */
	private static final int NUM = 512;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DocumentOpen() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	process(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		process(request, response);
	}

	private void process(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ResourceBundle rb = ResourceBundle.getBundle("ve.org.bcv.rhei.util.bundle");
		String path= request.getParameter("path")!=null?(String)request.getParameter("path"):"";
		log.info("path="+path);
		log.info(request.getParameter("nameFile"));
		String nameFile = request.getParameter("nameFile")!=null?(String)request.getParameter("nameFile"):"";
		String ext = request.getParameter("ext")!=null?(String)request.getParameter("ext"):"";
		
		Document doc = null;
		ExceuteCmisMain exceuteCmisMain = new ExceuteCmisMain(rb.getString("alfreco.hostwithcmis")
				,
				rb.getString("alfresco.user"), rb.getString("alfresco.passwd"));
		 
			File f = null;
			FileWriter escribir = null;
			OutputStream outStream = null;
			try {
				String rutaDoc=rb.getString("alfresco.homeUser")+"/"+path;
				log.info("rutaDoc="+rutaDoc+",nameFile.pdf="+nameFile+"."+ext);
				doc = exceuteCmisMain.findDocumento(rb.getString("alfresco.homeUser")+"/"+path,
						nameFile+"."+ext);

				outStream = response.getOutputStream();

				
				f = fileDesdeStream(doc.getContentStream().getStream(),
						getNameFile(doc.getName()), getExtFile(doc.getName()));
			} catch (Exception e) {
				//if (e.getMessage().equalsIgnoreCase("alfrescoFuenteNula")) {
					f = new File("fichero.txt");
					f.createNewFile();
					if (f.exists()) {
						f.delete();
						f = new File("fichero.txt");
						f.createNewFile();
					}

					escribir = new FileWriter(f, true);

					// Escribimos en el archivo con el metodo write
					escribir.write("alfrescoFuenteNula");
					escribir.write(e.getMessage());

					// Cerramos la conexion
					escribir.close();
			//	}
				// TODO Auto-generated catch block
				// e.printStackTrace();
			} finally {
				try {
					// Cerramos la conexion
					escribir.flush();
					escribir.close();
				} catch (Exception ignore) {
					// TODO: handle exception
				}
			}

			FileInputStream inStream = null;
			try {

				response.setContentType(getMimeTypeFile(doc.getName()));
				response.setContentLength((int) f.length());

				response.setHeader("Content-disposition",
						"attachment; filename=\"" + doc.getName() + "\"");

				byte[] buf = new byte[8192];
				inStream = new FileInputStream(f);
				int sizeRead = 0;
				while ((sizeRead = inStream.read(buf, 0, buf.length)) > 0) {
					outStream.write(buf, 0, sizeRead);
				}
				inStream.close();
				outStream.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {

					inStream.close();
				} catch (Exception ignore) {
					// TODO: handle exception
				}
				try {
					outStream.flush();
					outStream.close();
				} catch (Exception ignore) {
					// TODO: handle exception
				}
				if (f.exists()) {
					f.delete();
				}

			}

	 

	}

	public final File fileDesdeStream(final InputStream fuente, String nombre,
			final String extension) throws Exception {
		byte[] buf = new byte[NUM];
		int len;
		File archivo = null;
		OutputStream out = null;
		if (fuente == null) {
			throw new Exception("alfrescoFuenteNula");
		}
		try {
			if (nombre.indexOf(".") != -1) {
				nombre = nombre.substring(0, nombre.indexOf("."));
			}
			archivo = new File(nombre + "." + extension);
			// File.createTempFile(
			// "archivo_" + identificador + "_", "." + extension);
			archivo.deleteOnExit();
			out = new FileOutputStream(archivo);
			while ((len = fuente.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
		} catch (IOException ex) {

		} finally {
			try {
				out.flush();
				out.close();
			} catch (Exception ignore) {
				// TODO: handle exception
			}

		}
		return archivo;
	}

	public static String getExtFile(String fileName) {
		String ext = "";
		if (!isEmptyOrNull(fileName)) {
			int mid = fileName.lastIndexOf(".");
			ext = fileName.substring(mid + 1, fileName.length());
		}
		return ext;
	}

	public static String getNameFile(String fileName) {
		String fname = "";
		if (!isEmptyOrNull(fileName)) {
			int mid = fileName.lastIndexOf(".");
			fname = fileName.substring(0, mid);
		}
		return fname;
	}

	public static boolean isEmptyOrNull(String valor) {
		boolean swVacioCadena = (valor == null || valor.trim().length() == 0
				|| valor.trim().equalsIgnoreCase("null") || valor.trim()
				.equalsIgnoreCase("#000000"));
		return swVacioCadena;
	}

	public static String getMimeTypeFile(String fileName) throws Exception {
		String mimeType = "";
		boolean sw = false;

		String value = ""; // assume input
		MimeType mimeTypEnum = MimeType.valueOf(getExtFile(fileName)); // surround
																		// with
																		// try/catch

		switch (mimeTypEnum) {
		case pdf:
			mimeType = "application/pdf";
			sw = true;
			break;
		case doc:
			mimeType = "application/msword";
			sw = true;
			break;
		case dot:
			mimeType = "application/msword";
			sw = true;
			break;
		case pps:
			mimeType = "application/mspowerpoint";
			sw = true;
			break;
		case ppt:
			mimeType = "application/mspowerpoint";
			sw = true;
			break;
		case xls:
			mimeType = "application/excel";
			sw = true;
			break;
		case xml:
			mimeType = "text/xml";
			sw = true;
			break;
		case txt:
			mimeType = "text/plain";
			sw = true;
			break;
		}

		if (!sw) {
			throw new Exception("Extension no soportada "
					+ getExtFile(fileName));
		}

		return mimeType;
	}

}
