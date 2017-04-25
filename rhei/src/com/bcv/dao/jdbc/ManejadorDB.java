package com.bcv.dao.jdbc;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
@Named(value="manejadorDB")
public class ManejadorDB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(ManejadorDB.class
			.getName());
	  private   ManejadorDB instance = null;
	  
	  @Resource(lookup="java:/jdbc/rheiDSPool")
	 private DataSource dataSourceResource;
	   
	  
	   
	   
	   public ManejadorDB() {
	      // Exists only to defeat instantiation.
	   }
	   
	   
 
	   public   ManejadorDB getInstance() {
	      if(instance == null) {
	         instance =new ManejadorDB();
	      }
	      return instance;
	   }
	   
	   

	
  

	public Connection conexion() {
		 Connection con = null;
		try {
			if (con == null) {
				if (getDataSourceResource()!=null){
					 
				}
				 
				con = coneccionPool();
			}

		} catch (SQLException e) {
			log.error(e.toString());
			 
		}
		return con;
	}
	
	 
	
	public Connection coneccionPool() throws  SQLException {
 
	    String DATASOURCE_CONTEXT = "java:/jdbc/rheiDSPool";
	    
	    Connection result = null;
	    try {
	      Context initialContext = new InitialContext();
	      if ( initialContext == null){
	    	  log.error("JNDI problem. Cannot get InitialContext.");
	      }
	      DataSource datasource = (DataSource)initialContext.lookup(DATASOURCE_CONTEXT);
	      if (datasource != null) {
	        result = datasource.getConnection();
	      }
	      else {
	    	  log.error("Failed to lookup datasource.");
	    	 // result= coneccionPoolToTest() ;
	      }
	    }
	    catch ( NamingException ex ) {
	    	log.error("Cannot get connection: " + ex);
	    	//  result= coneccionPoolToTest() ;
	    }
	    catch(SQLException ex){
	    	log.error("Cannot get connection: " + ex);
	    }
	    return result;
		
	}
	
 
	
//	public Connection coneccionPoolToTest() throws  SQLException {
//		Connection connection = null;
//		try {
//			Class.forName("oracle.jdbc.OracleDriver");
//			connection = DriverManager.getConnection(
//			   "jdbc:oracle:thin:@cl-oradtbcv01:1521/orabdd02","admrhei", "admrhei");
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	 
//		 
//
//		 
//		return connection;
//	}



	public DataSource getDataSourceResource() {
		return dataSourceResource;
	}



	public void setDataSourceResource(DataSource dataSourceResource) {
		this.dataSourceResource = dataSourceResource;
	}


 
}