package com.fileupload;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ve.org.bcv.rhei.bean.Recaudo;

import com.alfresco.main.ExceuteCmisMain;
import com.bcv.dao.jdbc.RecaudoDao;
import com.bcv.dao.jdbc.impl.RecaudosDaoImpl;
import com.google.common.io.Files;

/**
 * Listamos todos los documentos recaudos de alfresco
 * @author sirodrig
 *
 */
public class ListRecaudos extends HttpServlet {
	private static Log log = LogFactory.getLog(ListRecaudos.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RecaudoDao recaudosDao = new RecaudosDaoImpl();
	
	/**
	 * Generamos tabla de recaudos usando el metodo generateRecaudos
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void comun(HttpServletRequest request,
			HttpServletResponse response,String path) throws ServletException, IOException {

		 StringBuilder ajaxUpdateResult = new StringBuilder();;
		 
	
		 
		 boolean readOnly=false;
		boolean titleComeResourcebundle=true;
			ajaxUpdateResult.append(generateRecaudos(path,readOnly,titleComeResourcebundle));
		response.getWriter().print(ajaxUpdateResult);
	}
	
	/**
	 * Generamos tabla de recaudos usando el metodo generateRecaudos
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void comunConsultOnly(HttpServletRequest request,
			HttpServletResponse response,String path) throws ServletException, IOException {

		 StringBuilder ajaxUpdateResult = new StringBuilder();;
		 
		 boolean titleComeResourcebundle=true;
		 boolean readOnly=true;
		ajaxUpdateResult.append(generateRecaudos(path,readOnly,titleComeResourcebundle));
		response.getWriter().print(ajaxUpdateResult);
	}
	
	/**
	 * Generamos la tabla recaudos
	 * @return
	 */
	public String generateRecaudos(String path,boolean readOnly,boolean titleComeResourcebundle){
		return "";
	}
	public String generateRecaudosNOUSADO(String path,boolean readOnly,boolean titleComeResourcebundle){
		ResourceBundle rb = ResourceBundle.getBundle("ve.org.bcv.rhei.util.bundle");
		String valor = rb.getString("recaudo.consignar");
		String descripcion=rb.getString("descripcion");
	    StringBuilder ajaxUpdateResult = new StringBuilder();;
	    
	    /**Crearemos la carpeta a montar el documento***/
			CreateFolderCmis createFolderCmis = new CreateFolderCmis();
            if ( !createFolderCmis.folderFind( path.toString())){
                log.info("No existe carpeta");
                createFolderCmis.createFolder(rb.getString("alfresco.homeUser"),path.toString());	
            }
	    
	    //
			List<Recaudo> recaudos = listRecaudos(path,titleComeResourcebundle);
			ajaxUpdateResult.append("<table cellspacing=\"0\" cellpadding=\"0\" class=\"anchoTabla5\">");
			ajaxUpdateResult.append("<th>").append(valor).append("</th>");
			ajaxUpdateResult.append("<th>").append(descripcion).append("</th>");
			for (Recaudo recaudo:recaudos){
				ajaxUpdateResult.append("<tr>");
				ajaxUpdateResult.append("<td>");
				ajaxUpdateResult.append(recaudo.getDescripcion());					
				ajaxUpdateResult.append("</td>");
				ajaxUpdateResult.append("<td>");
				if (recaudo.isAlfresco()){
					ajaxUpdateResult.append("<a href=\"#d\" onclick=\"Javascript:downloadDoc('").append(path).append("','").append(recaudo.getNombre()).append("','").append(recaudo.getExt()).append("'").append("); return false;\"> ").append("<input type=\"image\" src=\"imagenes/add_attachment.gif\" alt=\"download\" />");
				}
				
				ajaxUpdateResult.append("</td>");
				ajaxUpdateResult.append("</tr>");
			}
			ajaxUpdateResult.append("</table>");
			valor=null;
			descripcion=null;
			return ajaxUpdateResult.toString();
	}
	
 
	
	public List<Recaudo> listRecaudoFromBD(){
		List<Recaudo> recaudos=null;
		try {
			 recaudos=  recaudosDao.recaudosLst();
			if (recaudos==null){
				 recaudos = new ArrayList<Recaudo>();
			}
		} catch (SQLException e) {
			recaudos = new ArrayList<Recaudo>();
			e.printStackTrace();
		}
		return recaudos;
	}
	
	/**
	 * Checked all document mandatory are in alfresco
	 * @param path
	 * @return
	 */
	public  List<Recaudo> checkFillMandatoryInAlfresco(String path){
		 List<Recaudo> recaudosFaltan = new ArrayList<Recaudo>();
		/**Recaudos guardados en alfresco*/
		/**Chequearemos si los recaudos obligatorios los agrego*/
		boolean titleComeResourcebundle=true;
		ListRecaudos listRecaudos = new ListRecaudos();
		List<Recaudo> recaudos =listRecaudos.listRecaudos(path.toString(),titleComeResourcebundle);
		
		for (Recaudo rc:recaudos){
			if (rc.isObligatorio() && !rc.isAlfresco()){
				recaudosFaltan.add(rc);
			}
		}
		 return recaudosFaltan;
	}
	
	public List<Recaudo> listRecaudos(String path,boolean titleComeResourcebundle){
		List<Recaudo> recaudos = new ArrayList<Recaudo>();
		boolean isDoc_planilla_abono_juridica=false;
		boolean isDoc_planilla_abono_natural=false;
		String extDoc_planilla_abono_juridica="";
		String extDoc_planilla_abono_natural="";
		ResourceBundle rb = ResourceBundle.getBundle("ve.org.bcv.rhei.util.bundle");
		//2016-2017/J306356029-C/14176742/11843
		 
		
//		ExceuteCmisMain exceuteCmisMain= new ExceuteCmisMain(rb.getString("alfreco.hostwithcmis")
//				,
//				rb.getString("alfresco.user"), rb.getString("alfresco.passwd"));
//		try {
//			List<Document>list=exceuteCmisMain.listaDocs(rb.getString("alfresco.homeUser")+"/"+path);  
//			for(Document doc:list){
//
//                String fileNameWithOutExt = Files.getNameWithoutExtension(doc.getName());
//                String ext = Files.getFileExtension(doc.getName());
//                
//                
//              if ("doc_planilla_abono_juridica".equalsIgnoreCase(fileNameWithOutExt)){
//                	 extDoc_planilla_abono_juridica=ext;
//                	isDoc_planilla_abono_juridica=true;
//                } else if ("doc_planilla_abono_natural".equalsIgnoreCase(fileNameWithOutExt)){
//                	extDoc_planilla_abono_natural=ext;
//                	isDoc_planilla_abono_natural=true;
//                } 
//			}
//			
//			
//		} catch (Exception e) {
//			 log.error(e.toString());
//
//		}
		
		try {
			Recaudo recaudo =null;
		 
			
			
		 
			

		 
			
		 
			
			
		//	recaudo= recaudosDao.recaudosByName("doc_planilla_abono_juridica") ;
			/**Titulo quje se puede mostrar en html el nombre de los doc con acentos*/
			if (titleComeResourcebundle){
				recaudo.setDescripcion(rb.getString("doc_planilla_abono_juridica"));
			}
			if (recaudo!=null){
				recaudo.setAlfresco(isDoc_planilla_abono_juridica);
		        recaudo.setExt(extDoc_planilla_abono_juridica);
			    recaudos.add(recaudo);
			}
			
			/*************************1*******************/
		
			//recaudo= recaudosDao.recaudosByName("doc_planilla_abono_natural") ;
			/**Titulo quje se puede mostrar en html el nombre de los doc con acentos*/
			if (titleComeResourcebundle){
				recaudo.setDescripcion(rb.getString("doc_planilla_abono_natural"));
			}
			
			if (recaudo!=null){
				recaudo.setAlfresco(isDoc_planilla_abono_natural);
		        recaudo.setExt(extDoc_planilla_abono_natural);
			    recaudos.add(recaudo);
			}
			
			/*************************1*******************/
		
 
		} catch (Exception e) {
                e.printStackTrace();
		}
		
	 

 
		return recaudos;
	}
}
