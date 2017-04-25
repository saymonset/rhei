package com.fileupload;

import java.io.Serializable;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alfresco.main.ExceuteCmisMain;
import com.exception.ExceptionCmis;

/**
 * Crearemos una carpeta en alfreco con cmis
 * 
 * @author sirodrig
 * 
 */
public class CreateFolderCmis implements Serializable {
	private static Log log = LogFactory.getLog(CreateFolderCmis.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param treeAlfresco
	 *            , es el arbol del home donde se creara la carpeta, se hace en
	 *            un arreglo.. example : /User Homes/AlfrescoRHEI para
	 *            convertirlo en esto String[] treeAlfresco=
	 *            {"User Homes","alfrescoRHEI"};
	 * @param nameNewFolder
	 *            , es el nombre de la carpeta a crear, example: newFolderName
	 * @return
	 */
	public boolean createFolder(String treeAlfrescoName, String nameNewFolder) {
		boolean isExito = true;
		if (StringUtils.isEmpty(nameNewFolder) || StringUtils.isEmpty(treeAlfrescoName)){
			return false;
		}
		
		ResourceBundle rb = ResourceBundle
				.getBundle("ve.org.bcv.rhei.util.bundle");
		ExceuteCmisMain connectBCVTest = new ExceuteCmisMain(
				rb.getString("alfreco.hostwithcmis"),
				rb.getString("alfresco.user"), rb.getString("alfresco.passwd"));
          
		String[] carpToCrear = nameNewFolder.split("/");
		String[] treeAlfresco = null;
		for (int i = 0; i < carpToCrear.length; i++) {
			try {
				
				treeAlfresco = treeAlfrescoName.split("/");
				connectBCVTest.createFolder(treeAlfresco, carpToCrear[i]);
				
			} catch (ExceptionCmis e) {
				isExito = false;
			}
			treeAlfrescoName+="/"+carpToCrear[i];

		}

		return isExito;
	}
	public boolean folderFind(String treeAlfrescoName){
		boolean isExiste=true;
		ResourceBundle rb = ResourceBundle
				.getBundle("ve.org.bcv.rhei.util.bundle");
		
		
		ExceuteCmisMain connectBCVTest = new ExceuteCmisMain(
				rb.getString("alfreco.hostwithcmis"),
				rb.getString("alfresco.user"), rb.getString("alfresco.passwd"));
		
		String[]treeAlfresco=treeAlfrescoName.split("/");
	 
	 
		try {
			connectBCVTest.folderFind(treeAlfresco);//.folderFind(treeAlfresco);
		} catch (ExceptionCmis e) {
			isExiste=false;
		}	
		return isExiste;
	}
	
	

}
