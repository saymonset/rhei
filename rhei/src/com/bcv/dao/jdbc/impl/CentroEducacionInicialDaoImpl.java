/**
 * 
 */
package com.bcv.dao.jdbc.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ve.org.bcv.rhei.bean.Proveedor;
import ve.org.bcv.rhei.bean.ShowResultToView;

import com.bcv.dao.jdbc.CentroEducacionInicialDao;
import com.bcv.dao.jdbc.ManejadorDB;
import com.bcv.dao.jdbc.SimpleJDBCDaoImpl;
import com.bcv.model.CentroEducacionInicial;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco 10/08/2015 15:48:20 2015 mail :
 *         oraclefedora@gmail.com
 */
public class CentroEducacionInicialDaoImpl
		extends
			SimpleJDBCDaoImpl<CentroEducacionInicial>
		implements
			CentroEducacionInicialDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	  private static Logger log = Logger.getLogger(CentroEducacionInicialDaoImpl.class.getName());
	  

	public List<Proveedor> listaProvEducativos(String companiaAnalista)
			throws ServletException, IOException {
		List<Proveedor> proveedors =  buscarCentroEducacionFiltro(companiaAnalista,"");
	 
 
		return proveedors;
	}
	
	 public List<Proveedor> buscarCentroEducacionFiltro(String co_cia_analista,String nombre)
	    {
		 List<Proveedor> proveedors = new ArrayList<Proveedor>();
		 ManejadorDB manejador =new ManejadorDB();;
	     Connection con = manejador.conexion();
	     PreparedStatement stmt = null;
	     ResultSet rs = null;
	      StringBuilder sql = new StringBuilder("");
	      String localidadCEI = "";
	    
	      try
	      {
	        log.debug("Entro en buscarCentroEducacion");
//	        if (co_cia_analista.compareTo("01") == 0) {
//	          localidadCEI = "C";
//	        } else if (co_cia_analista.compareTo("02") == 0) {
//	          localidadCEI = "M";
//	        } else if (co_cia_analista.compareTo("03") == 0) {
//	          localidadCEI = "Y";
//	        }
	        log.debug("localidadCEI.: " + localidadCEI);
	        sql.append(" SELECT NU_RIF_PROVEEDOR, IN_LOCALIDAD_BCV, NB_PROVEEDOR  FROM  RHEI.PROVEEDOR_CEI WHERE IN_ACTIVO = 0 AND " );
	        sql.append(" ( ");
	        sql.append(" IN_LOCALIDAD_BCV = decode('C', 'C', nvl(null, IN_LOCALIDAD_BCV)," ).append(" nvl('C', IN_LOCALIDAD_BCV))" );
	        sql.append(" OR IN_LOCALIDAD_BCV = decode('M', 'M', nvl(null, IN_LOCALIDAD_BCV)," ).append(" nvl('M', IN_LOCALIDAD_BCV))" );
	        sql.append(" OR IN_LOCALIDAD_BCV = decode('Y', 'Y', nvl(null, IN_LOCALIDAD_BCV)," ).append(" nvl('Y', IN_LOCALIDAD_BCV))" );
	        sql.append(" ) ");
	        
	        if (!StringUtils.isEmpty(nombre)){
	        	  nombre=nombre.replace("'", "");
	        	  String rif=nombre;
	        	  rif=rif.replace("-", "");

	        	sql.append(" and (  ");
		          
		        	  sql.append("  lower(NB_PROVEEDOR) like '%").append(nombre.toLowerCase()).append("%'");	  
		          
		        	  sql.append(" or lower(NU_RIF_PROVEEDOR) like '%").append(rif.toLowerCase()).append("%'");	  
	              
		          sql.append(" ) ");	
	        }
	        
	        
	        
	          sql.append(" ORDER BY NB_PROVEEDOR ASC ");
	        
	        stmt = con.prepareStatement(sql.toString());
	        rs = stmt.executeQuery();
	        Proveedor prov=null;
	        while (rs.next())
	        {
	        	prov= new Proveedor();
	        	prov.setValor(rs.getString("NU_RIF_PROVEEDOR"));
	        	prov.setNombre(rs.getString("NB_PROVEEDOR"));
	        	proveedors.add(prov);
	        }
	      }
	      catch (SQLException e)
	      {
	        e.printStackTrace();
	      }
	      finally
	      {
	        liberarConexion(rs, stmt, con);
	      }
	      return proveedors;
	    }
	 
	  
	 

	    
	    public ShowResultToView cargarAtributosCentroEducativo(ShowResultToView showResultToView,String nroRif,String nombreCentro,String localidad, String direccionCentro, String telefono,String correo)
	    {
	      showResultToView.setNroRif(nroRif);
	      showResultToView.setNombreCentro(nombreCentro);
	      showResultToView.setLocalidadBCV(localidad);
	      showResultToView.setDireccionCentro(direccionCentro);
	      showResultToView.setTlfCentro(telefono);
	      showResultToView.setEmail(correo);
	      return showResultToView;
	    }
	    
	    

	    public CentroEducacionInicial buscarCentroByRif( String nroRif)
	      throws SQLException
	    { 
	    	PreparedStatement pstmt=null;
	    	   ManejadorDB manejador =new ManejadorDB();;
	    	Connection  con = manejador.conexion();; 
	    	CentroEducacionInicial centroEducacionInicial = new CentroEducacionInicial();
		    ResultSet rs = null;
		    
	    	try {
	    		 log.debug("Nro de Rif: " + nroRif);
	    	      
	    	      String sql = "SELECT NU_RIF_PROVEEDOR, INITCAP(NB_PROVEEDOR) AS NB_PROVEEDOR  , DECODE(IN_LOCALIDAD_BCV, 'C', 'Caracas', 'M', 'Maracaibo', 'Y', 'Maracay','NO ASOCIADO') AS IN_LOCALIDAD_BCV, NU_TELEFONO1, TX_E_MAIL, DI_PROVEEDOR  FROM  RHEI.PROVEEDOR_CEI WHERE NU_RIF_PROVEEDOR=?";
	    	      
	    	  
	    	  
	    	  
	    	  
	    	      log.debug("Entre en el método buscarCentro");
	    	      pstmt = con.prepareStatement(sql);
	    	      pstmt.setString(1, nroRif);
	    	      rs = pstmt.executeQuery();
	    	      if (rs.next())
	    	      {
	    	    	  
	    	    	  centroEducacionInicial.setNroRif(rs.getString("NU_RIF_PROVEEDOR"));
	    	        centroEducacionInicial.  setNombreCentro(rs.getString("NB_PROVEEDOR"));
	    	        centroEducacionInicial. setLocalidad(rs.getString("IN_LOCALIDAD_BCV"));
	    	        centroEducacionInicial. setDireccionCentro(rs.getString("DI_PROVEEDOR"));
	    	        if (rs.getString("NU_TELEFONO1") != null) {
	    	        	centroEducacionInicial.   setTelefono(rs.getString("NU_TELEFONO1"));
	    	        }
	    	        if (rs.getString("TX_E_MAIL") != null) {
	    	        	centroEducacionInicial. setCorreo(rs.getString("TX_E_MAIL"));
	    	        }
	    	      }
	    	      rs.close();
			} catch (Exception e) {
				// TODO: handle exception
			}finally {
				liberarConexion(rs, pstmt, con);
			}
	     return centroEducacionInicial;
	    }
	    
	    
	    
	    
	    
	    public CentroEducacionInicial infoCentroEducativo(  String nroRif)
	    	      throws SQLException
	    	    {
	    	CentroEducacionInicial centroEducacionInicial = new CentroEducacionInicial();
	    	      ManejadorDB manejador =new ManejadorDB();;
	    	      ResultSet rs = null;
	    	      Connection con =null;
  	          PreparedStatement pstmt = null;
	    	      try {
	    	    	   con = manejador.conexion();
	    	          pstmt = null;
	    	         
	    	         String sql = "select  C.NU_TELEFONO1,C.NU_TELEFONO2,C.DI_PROVEEDOR,c.IN_LOCALIDAD_BCV,C.NB_PROVEEDOR,c.NU_RIF_PROVEEDOR,C.TX_E_MAIL from RHEI.PROVEEDOR_CEI c where C.NU_RIF_PROVEEDOR=?";
	    	         
	    	         log.debug("Entre en el método buscarCentro");
	    	         pstmt = con.prepareStatement(sql);
	    	         pstmt.setString(1, nroRif);
	    	         rs = pstmt.executeQuery();
	    	         if (rs.next())
	    	         {
	    	        	 centroEducacionInicial. setNroRif(rs.getString("NU_RIF_PROVEEDOR"));
	    	           centroEducacionInicial.  setNombreCentro(rs.getString("NB_PROVEEDOR"));
	    	           centroEducacionInicial. setLocalidad(rs.getString("IN_LOCALIDAD_BCV"));
	    	           if (!StringUtils.isEmpty(rs.getString("DI_PROVEEDOR"))){
	    	        	   centroEducacionInicial. setDireccionCentro(rs.getString("DI_PROVEEDOR").toLowerCase());   
	    	           }
	    	           
	    	           
	    	           if (rs.getString("NU_TELEFONO1") != null) {
	    	        	   centroEducacionInicial.setTelefono(rs.getString("NU_TELEFONO1"));
	    	           }
	    	           if (rs.getString("NU_TELEFONO2") != null) {
	    	        	   centroEducacionInicial.setTelefono2(rs.getString("NU_TELEFONO2"));
	    	           }
	    	           if (rs.getString("TX_E_MAIL") != null) {
	    	        	   centroEducacionInicial. setCorreo(rs.getString("TX_E_MAIL"));
	    	           }
	    	         }
	    	         rs.close();
	    		} catch (Exception e) {
	    			// TODO: handle exception
	    		}finally {
	    			liberarConexion(rs, pstmt, con);
	    		}
	    	     return centroEducacionInicial;
	    	    }
	    
	    
	    public CentroEducacionInicial buscarCentroByLocalidad(  String localidad,String nroRif)
	    	      throws SQLException
	    	    {
	    	CentroEducacionInicial centroEducacionInicial = new CentroEducacionInicial();
	    	      ManejadorDB manejador =new ManejadorDB();;
	    	      ResultSet rs = null;
	    	      Connection con =null;
    	          PreparedStatement pstmt = null;
	    	      try {
	    	    	   con = manejador.conexion();
	    	          pstmt = null;
	    	         
	    	         String sql = "SELECT NU_RIF_PROVEEDOR, INITCAP(NB_PROVEEDOR) AS NB_PROVEEDOR  , DECODE(IN_LOCALIDAD_BCV, 'C', 'Caracas', 'M', 'Maracaibo', 'Y', 'Maracay','NO ASOCIADO') AS IN_LOCALIDAD_BCV, NU_TELEFONO1, TX_E_MAIL, DI_PROVEEDOR  FROM  RHEI.PROVEEDOR_CEI WHERE NU_RIF_PROVEEDOR=?";  
	    	         
	    	         
	    	         log.debug("Entre en el método buscarCentro");
	    	         pstmt = con.prepareStatement(sql);
	    	         pstmt.setString(1, nroRif);
	    	         rs = pstmt.executeQuery();
	    	         if (rs.next())
	    	         {
	    	        	 centroEducacionInicial. setNroRif(rs.getString("NU_RIF_PROVEEDOR"));
	    	           centroEducacionInicial.  setNombreCentro(rs.getString("NB_PROVEEDOR"));
	    	           centroEducacionInicial. setLocalidad(rs.getString("IN_LOCALIDAD_BCV"));
	    	           centroEducacionInicial. setDireccionCentro(rs.getString("DI_PROVEEDOR"));
	    	           
	    	           if (rs.getString("NU_TELEFONO1") != null) {
	    	        	   centroEducacionInicial.setTelefono(rs.getString("NU_TELEFONO1"));
	    	           }
	    	           if (rs.getString("TX_E_MAIL") != null) {
	    	        	   centroEducacionInicial. setCorreo(rs.getString("TX_E_MAIL"));
	    	           }
	    	         }
	    	         rs.close();
	    		} catch (Exception e) {
	    			// TODO: handle exception
	    		}finally {
	    			liberarConexion(rs, pstmt, con);
	    		}
	    	     return centroEducacionInicial;
	    	    }

		/* (non-Javadoc)
		 * @see com.bcv.dao.jdbc.CentroEducacionInicialDao#cargarAtributosCentroEducativo(ve.org.bcv.rhei.bean.ShowResultToView, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
		 */
	 
		public ShowResultToView cargarAtributosCentroEducativo(
				ShowResultToView showResultToView, String nroRif,
				String nombreCentro, String localidad, String telefono,
				String correo) {
			 
			return null;
		}
	    
	
}
