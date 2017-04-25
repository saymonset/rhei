package com.fileupload;

import java.io.File;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alfresco.main.ExceuteCmisMain;
import com.exception.ExceptionCmis;


/**
 * TEST UPLOAD FILE IN ALFRESCO
 * @author sirodrig
 *
 */
public class UploadFileTest {
	private static Log log = LogFactory.getLog(UploadFileTest.class);
	
	
	
	public static void main(String[] args) {
		UploadFileTest uploadFileTest= new UploadFileTest();
		 
			uploadFileTest.findDocumento( );
		
	 
		
//		uploadFileTest.listDocs();
//		System.out.println("Hola mujndo");

	}
	 void upoad(){
		File _upFileFile = new File("D:/simon/cliente.xls");
		ExceuteCmisMain exceuteCmisMain= new ExceuteCmisMain("http://alfresco-desa.intra.bcv/alfresco/cmisatom","AlfrescoRHEI","alfresco15");
		try {
			exceuteCmisMain.uploadFileCmis( "/User Homes/alfrescoRHEI/", _upFileFile);
		} catch (Exception e) {
			try {
				exceuteCmisMain.uploadVersionFileCmis(
						"/User Homes/alfrescoRHEI", _upFileFile,_upFileFile.getName());
			} catch (ExceptionCmis e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}
	
	 void listDocs(){
		ExceuteCmisMain exceuteCmisMain= new ExceuteCmisMain("http://alfresco-desa.intra.bcv/alfresco/cmisatom","AlfrescoRHEI","alfresco15");
		try {
			
			List<Document>list=exceuteCmisMain.listaDocs( "/User Homes/AlfrescoRHEI");  
			for(Document doc:list){
				System.out.println(doc.getName());
			}
			
			
		} catch (Exception e) {
			 log.error(e.toString());

		}
	}
	 
	 void findDocumento(){
		 ExceuteCmisMain exceuteCmisMain= new ExceuteCmisMain("http://alfresco-desa.intra.bcv/alfresco/cmisatom","AlfrescoRHEI","alfresco15");
		 try {
			Document  doc=exceuteCmisMain.findDocumento( "/User Homes/AlfrescoRHEI", "cliente.xls");
			log.info(doc.getName());
		} catch (ExceptionCmis e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	
	
	

	
	
}
