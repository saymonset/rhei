/**
 * 
 */
package ve.org.bcv.rhei.autenticador.ldap.connection;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 05/08/2015 10:45:11
 * 2015
 * mail : oraclefedora@gmail.com
 */

import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPAttributeSet;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPModification;
import com.novell.ldap.LDAPSearchResults;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import org.apache.log4j.Logger;
import ve.org.bcv.rhei.autenticador.ldap.dto.AtributoDTO;
import ve.org.bcv.rhei.autenticador.ldap.dto.LDAPDTOConnections;
import ve.org.bcv.rhei.autenticador.ldap.util.LDAPUtilities;

public class LDAPDelegador
{
  private static Logger log = Logger.getLogger(LDAPDelegador.class);
  private LDAPConnection conn;
  private LDAPDTOConnections dto;
  
  public LDAPDelegador(boolean isExternal)
  {
    Properties prop = null;
    try
    {
      prop = LDAPUtilities.cargarPropiedades();
      this.dto = new LDAPDTOConnections(isExternal, prop);
      log.debug("PROPIEDADES CARAGDAS");
    }
    catch (Exception e)
    {
      log.error("OCURRIO UNA EXCEPCION ");
      log.error("MENSAJE --> " + e.getMessage());
      log.error("CAUSA ----> " + e.getCause());
    }
  }
  
  public boolean autenticarUsuario(String usuario, String pass)
    throws UnsupportedEncodingException, LDAPException
  {
    boolean resp = false;
    bind();
    try
    {
      log.debug("autenticando al usuario -> " + usuario);
      
      String filtroBusqueda = "(cn=" + usuario + ")";
      LDAPSearchResults res = this.conn.search("", this.dto.getScope(), filtroBusqueda, null, false);
      LDAPEntry nextEntry = res.next();
      
      LDAPAttribute attr2 = new LDAPAttribute("userPassword", pass);
      resp = this.conn.compare(nextEntry.getDN(), attr2);
    }
    catch (LDAPException e)
    {
      log.error("OCURRIO UNA EXCEPCION ");
      log.error("MENSAJE --> " + e.getMessage());
      log.error("CAUSA ----> " + e.getCause());
      resp = false;
    }
    disconnect();
    log.debug("resultado de la autenticacion del usuario -> " + usuario + " / " + resp);
    log.info("AUTENTICACION DEL USUARIO COMPLETADA");
    
    return resp;
  }
  
  public boolean cambiarPassword(String usuario, String pass, String nuevoPass)
    throws UnsupportedEncodingException, LDAPException
  {
    boolean resp = true;
    
    bind();
    try
    {
      if ((usuario.equalsIgnoreCase("")) || (pass.equalsIgnoreCase("")) || (nuevoPass.equalsIgnoreCase("")) || (usuario == null) || (pass == null) || (nuevoPass == null))
      {
        log.info("LOS PARAMETROS PARA LA OPERACION NO PUEDEN SER VACIOS O NULOS");
        
        throw new Exception("LOS PARAMETROS PARA LA OPERACION NO PUEDEN SER VACIOS O NULOS");
      }
      log.debug("cambiando el password al usuario -> " + usuario);
      
      String filtroBusqueda = "(cn=" + usuario + ")";
      LDAPSearchResults res = this.conn.search("", this.dto.getScope(), filtroBusqueda, null, false);
      LDAPEntry nextEntry = res.next();
      
      LDAPModification[] modifications = new LDAPModification[2];
      
      LDAPAttribute deletePassword = new LDAPAttribute("userPassword", pass);
      modifications[0] = new LDAPModification(1, deletePassword);
      
      LDAPAttribute addPassword = new LDAPAttribute("userPassword", nuevoPass);
      modifications[1] = new LDAPModification(0, addPassword);
      
      this.conn.modify(nextEntry.getDN(), modifications);
    }
    catch (LDAPException e)
    {
      log.error("OCURRIO UNA EXCEPCION ");
      log.error("MENSAJE --> " + e.getMessage());
      log.error("CAUSA ----> " + e.getCause());
      resp = false;
    }
    catch (Exception e)
    {
      log.error("OCURRIO UNA EXCEPCION ");
      log.error("MENSAJE --> " + e.getMessage());
      log.error("CAUSA ----> " + e.getCause());
      resp = false;
    }
    disconnect();
    log.debug("resultado del cambio de password al usuario --> " + usuario + " / " + resp);
    log.info("OPERACION COMPLETADA CON RESULADO " + resp);
    return resp;
  }
  
  private boolean restablecerPassword(String usuario, String setPassword)
    throws UnsupportedEncodingException, LDAPException
  {
    boolean resp = true;
    bind();
    try
    {
      String filtroBusqueda = "(cn=" + usuario + ")";
      
      LDAPSearchResults res = this.conn.search("", this.dto.getScope(), filtroBusqueda, null, false);
      LDAPEntry nextEntry = res.next();
      
      LDAPAttribute attributePassword = new LDAPAttribute("userPassword", setPassword);
      this.conn.modify(nextEntry.getDN(), new LDAPModification(2, attributePassword));
    }
    catch (LDAPException e)
    {
      log.error("OCURRIO UNA EXCEPCION ");
      log.error("MENSAJE --> " + e.getMessage());
      log.error("CAUSA ----> " + e.getCause());
      resp = false;
    }
    disconnect();
    return resp;
  }
  
  public Collection<AtributoDTO> obtenerAtributos(String usuario, String[] atributos)
    throws UnsupportedEncodingException, LDAPException
  {
    bind();
    
    ArrayList attr = null;
    try
    {
      log.debug("obteniendo los atributos para el usuario -> " + usuario);
      
      AtributoDTO atrrDto = null;
      
      String filtroBusqueda = "(cn=" + usuario + ")";
      
      LDAPSearchResults res = this.conn.search("", this.dto.getScope(), filtroBusqueda, atributos, false);
      
      LDAPEntry nextEntry = res.next();
      
      LDAPAttributeSet attributeSet = nextEntry.getAttributeSet();
      
      Iterator allAttributes = attributeSet.iterator();
      if (atributos == null) {
        log.info("Se solicitaron  todos los atributos del usuario ");
      } else {
        log.info("Cantidad de atributos solicitados contra los devueltos " + atributos.length + " / " + attributeSet.size());
      }
      attr = new ArrayList();
      
      atrrDto = new AtributoDTO(nextEntry.getDN());
      
      attr.add(atrrDto);
      while (allAttributes.hasNext())
      {
        LDAPAttribute attribute = (LDAPAttribute)allAttributes.next();
        
        atrrDto = new AtributoDTO();
        
        atrrDto.setNombre(attribute.getName());
        
        atrrDto.setValor(attribute.getStringValueArray());
        
        attr.add(atrrDto);
      }
    }
    catch (LDAPException e)
    {
      log.error("OCURRIO UNA EXCEPCION ");
      log.error("MENSAJE --> " + e.getMessage());
      log.error("CAUSA ----> " + e.getCause());
      attr = null;
    }
    catch (NullPointerException e)
    {
      log.error("OCURRIO UNA EXCEPCION ");
      log.error("MENSAJE --> " + e.getMessage());
      log.error("CAUSA ----> " + e.getCause());
      attr = null;
    }
    disconnect();
    log.info("OPERACION COMPLETADA ");
    return attr;
  }
  
  public Collection<AtributoDTO> obtenerAtributos(String usuario, String base, String[] atributos)
    throws UnsupportedEncodingException, LDAPException
  {
    bind();
    
    ArrayList attr = null;
    try
    {
      log.debug("obteniendo los atributos para el usuario -> " + usuario);
      
      AtributoDTO atrrDto = null;
      
      String filtroBusqueda = "(cn=" + usuario + ")";
      if (base == null) {
        base = "";
      }
      LDAPSearchResults res = this.conn.search(base, this.dto.getScope(), filtroBusqueda, atributos, false);
      
      LDAPEntry nextEntry = res.next();
      
      LDAPAttributeSet attributeSet = nextEntry.getAttributeSet();
      
      Iterator allAttributes = attributeSet.iterator();
      if (atributos == null) {
        log.info("Se solicitaron  todos los atributos del usuario ");
      } else {
        log.info("Cantidad de atributos solicitados contra los devueltos " + atributos.length + " / " + attributeSet.size());
      }
      attr = new ArrayList();
      
      atrrDto = new AtributoDTO(nextEntry.getDN());
      
      attr.add(atrrDto);
      while (allAttributes.hasNext())
      {
        LDAPAttribute attribute = (LDAPAttribute)allAttributes.next();
        
        atrrDto = new AtributoDTO();
        
        atrrDto.setNombre(attribute.getName());
        
        atrrDto.setValor(attribute.getStringValueArray());
        
        attr.add(atrrDto);
      }
    }
    catch (LDAPException e)
    {
      log.error("OCURRIO UNA EXCEPCION ");
      log.error("MENSAJE --> " + e.getMessage());
      log.error("CAUSA ----> " + e.getCause());
      attr = null;
    }
    catch (NullPointerException e)
    {
      log.error("OCURRIO UNA EXCEPCION ");
      log.error("MENSAJE --> " + e.getMessage());
      log.error("CAUSA ----> " + e.getCause());
      attr = null;
    }
    disconnect();
    log.info("OPERACION COMPLETADA ");
    return attr;
  }
  
  private void bind()
    throws LDAPException, UnsupportedEncodingException
  {
    this.conn = new LDAPConnection();
    this.conn.connect(this.dto.getLdapHost(), this.dto.getLdapPort());
    this.conn.bind(this.dto.getVersion(), this.dto.getUser(), this.dto.getPassword().getBytes("UTF8"));
    log.info("CONEXION ESTABLECIDA CON EL LDAP");
  }
  
  private void disconnect()
    throws LDAPException
  {
    this.conn.disconnect();
    log.info("CONEXION CERRADA CON EL LDAP");
  }
}
