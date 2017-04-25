/**
 * 
 */
package ve.org.bcv.rhei.autenticador.ldap.dto;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 05/08/2015 10:43:43
 * 2015
 * mail : oraclefedora@gmail.com
 */

import java.io.Serializable;
import java.util.Properties;

public class LDAPDTOConnections
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String ldapHost;
  private int ldapPort;
  private int scope;
  private int version;
  private String user;
  private String password;
  
  public LDAPDTOConnections(boolean isExternal, Properties property)
  {
    this.ldapPort = Integer.parseInt(property.getProperty("bcv.ldap.ldapPort"));
    this.scope = Integer.parseInt(property.getProperty("bcv.ldap.searchScope"));
    this.version = Integer.parseInt(property.getProperty("bcv.ldap.ldapVersion"));
    if (isExternal)
    {
      this.ldapHost = property.getProperty("bcv.ldap.serverExternal");
      this.user = property.getProperty("bcv.ldap.accountLdapExternal");
      this.password = property.getProperty("bcv.ldap.passwordExternal");
    }
    else
    {
      this.ldapHost = property.getProperty("bcv.ldap.serverInternal");
      this.user = property.getProperty("bcv.ldap.accountLdapInternal");
      this.password = property.getProperty("bcv.ldap.passwordInternal");
    }
  }
  
  public String getLdapHost()
  {
    return this.ldapHost;
  }
  
  public void setLdapHost(String ldapHost)
  {
    this.ldapHost = ldapHost;
  }
  
  public int getLdapPort()
  {
    return this.ldapPort;
  }
  
  public void setLdapPort(int ldapPort)
  {
    this.ldapPort = ldapPort;
  }
  
  public int getScope()
  {
    return this.scope;
  }
  
  public void setScope(int scope)
  {
    this.scope = scope;
  }
  
  public int getVersion()
  {
    return this.version;
  }
  
  public void setVersion(int version)
  {
    this.version = version;
  }
  
  public String getUser()
  {
    return this.user;
  }
  
  public void setUser(String user)
  {
    this.user = user;
  }
  
  public String getPassword()
  {
    return this.password;
  }
  
  public void setPassword(String password)
  {
    this.password = password;
  }
}
