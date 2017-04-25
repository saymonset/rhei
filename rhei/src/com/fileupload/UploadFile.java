package com.fileupload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.alfresco.main.ExceuteCmisMain;
public class UploadFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//String workingDirectory = System.getProperty("user.dir");
	private final String UPLOAD_DIRECTORY = System.getProperty("user.dir");
	private static final int NUM = 1024 * 1024 * 8;
 
	private static Logger log = Logger.getLogger(UploadFile.class
			.getName());
 
	public void comun(HttpServletRequest request,
			HttpServletResponse response,String path) throws ServletException, IOException {

	String alfrescoOption = request.getParameter("alfrescoOption")!=null?(String)request.getParameter("alfrescoOption"):"";
	ResourceBundle rb = ResourceBundle.getBundle("ve.org.bcv.rhei.util.bundle");
   
    //++++++	+;
		
		try {
			List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
			File _upFileFile =null;
			for (FileItem item : items) {
				
				if (item.isFormField()) {
					//ajaxUpdateResult += "Field " + item.getFieldName() + " with value: " + item.getString() + " is successfully read\n\r";
				} else {
					String fileName = item.getName();
					InputStream content = item.getInputStream();

					response.setContentType("text/plain");
 
					  try {
						  
						  _upFileFile = new File(UPLOAD_DIRECTORY + File.separator + fileName);

							String ext = getExtFile(fileName);
							_upFileFile = fileDesdeStream(content, alfrescoOption, ext);
						    
							ExceuteCmisMain exceuteCmisMain= new ExceuteCmisMain(rb.getString("alfreco.hostwithcmis")
									,
									rb.getString("alfresco.user"), rb.getString("alfresco.passwd"));
							/**Crearemos la carpeta a montar el documento***/
						 							CreateFolderCmis createFolderCmis = new CreateFolderCmis();
							if ( !createFolderCmis.folderFind( path.toString())){
								log.info("No existe carpeta");
								createFolderCmis.createFolder(rb.getString("alfresco.homeUser"),path.toString());	
							}
							
							String homeAlfrescoPath=rb.getString("alfresco.homeUser")+"/"+path.toString();
							try {
								
								exceuteCmisMain.uploadFileCmis(homeAlfrescoPath, _upFileFile);
							} catch (Exception e) {
								exceuteCmisMain.uploadVersionFileCmis(
										homeAlfrescoPath, _upFileFile,_upFileFile.getName());

							}
				 
					} catch (Exception e) {
						// TODO: handle exception
					}finally{
				              _upFileFile.delete();
					}
				}
			}
		} catch (FileUploadException e) {
			throw new ServletException("Parsing file upload failed.", e);
		}

		response.getWriter().print("");
	}
	
	
	 
	
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String periodoEscolarValor = request.getParameter("periodoEscolarValor")!=null?(String)request.getParameter("periodoEscolarValor"):"";
		String rif = request.getParameter("rif")!=null?(String)request.getParameter("rif"):"";
		String cedula = request.getParameter("cedula")!=null?(String)request.getParameter("cedula"):"";
		String codBenef = request.getParameter("codBenef")!=null?(String)request.getParameter("codBenef"):"";

		StringBuilder path = new StringBuilder("");
		path.append(periodoEscolarValor).append("/").append(rif)
				.append("/").append(cedula).append("/")
				.append(codBenef);
		comun(request,
				response,path.toString());
}
	
	

	/**
	 * @param Valida si es vacio o nulo el valor a pasar
	 * @return true si no es vacio
	 */
	public static boolean isEmptyOrNull(String valor) {
		boolean swVacioCadena = (valor == null || valor.trim().length() == 0
				|| valor.trim().equalsIgnoreCase("null") || valor.trim()
				.equalsIgnoreCase("#000000"));
		return swVacioCadena;
	}
	
	/**
	 * @param filename , es el nombre de un archivo con su extension
	 * @return  retorna la extension del nombre de un archivo
	 */
	public static String getExtFile(String fileName) {
		String ext = "";
		if (!isEmptyOrNull(fileName)) {
			int mid = fileName.lastIndexOf(".");
			ext = fileName.substring(mid + 1, fileName.length());
		}
		return ext;
	}

	/**
	 * @param fileName es el nombre de un archivo con su extension
	 * @return nombre del archivo solamente
	 */
	public static String getNameFile(String fileName) {
		String fname = "";
		if (!isEmptyOrNull(fileName)) {
			int mid = fileName.lastIndexOf(".");
			fname = fileName.substring(0, mid);
		}
		return fname;
	}
	
	public static final File fileDesdeStream(final InputStream fuente,
			String nombre, final String extension) throws Exception {
		byte[] buf = new byte[NUM];
		int len;
		File archivo = null;
		OutputStream out=null;
		if (fuente == null) {
			throw new Exception("error.sis.fuente_invalida, fuente nula");
		}
		try {
			if (nombre.indexOf(".") != -1) {
				nombre = nombre.substring(0, nombre.indexOf("."));
			}
			archivo = new File(nombre + "." + extension);

			archivo.deleteOnExit();
			  out = new FileOutputStream(archivo);
			while ((len = fuente.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.flush();
			out.close();
			out = null;
			System.gc();
		} catch (IOException ex) {

		}finally{
			if (out!=null){
				out.flush();
				out.close();
				out = null;
				System.gc();
			}
			
		}
		return archivo;
	}
	 
	
	  
	
}