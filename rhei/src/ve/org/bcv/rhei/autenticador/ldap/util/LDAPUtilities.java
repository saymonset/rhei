/**
 * 
 */
package ve.org.bcv.rhei.autenticador.ldap.util;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 05/08/2015 10:41:17
 * 2015
 * mail : oraclefedora@gmail.com
 */

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import propiedades.PropertiesUrl;

public class LDAPUtilities
{
  private static Logger log = Logger.getLogger(LDAPUtilities.class);
  
 
//  public  String ARCHIVO_PROPIEDADES = url.toString();
  private static String PATRON_USUARIO_EXTERNO = "bcv.ldap.regex.externalClient";
  
  public static Properties cargarPropiedades()
    throws Exception
  {
    Properties property = new Properties();
    
  
    PropertiesUrl propertiesUrl = new PropertiesUrl();
   // InputStream inputStream = new FileInputStream(nameProperties);
    InputStream inputStream =propertiesUrl.getClass().getResourceAsStream("autenticador.properties");
    if (inputStream == null) {
      throw new Exception("Archivo'" +  "' no puede ser encontrado o no existe");
    }
    property.load(inputStream);
    log.info("ARCHIVO CARGADO");
    log.debug(property);
    
    return property;
  }
  
  public  boolean validarUsuarioExt(String usuario)
    throws Exception
  {
    boolean valid = false;
    
    String patron = cargarPropiedades().getProperty(PATRON_USUARIO_EXTERNO);
    if ((usuario.equalsIgnoreCase("")) || (usuario == null) || (patron.equalsIgnoreCase("")) || (patron == null)) {
      throw new Exception("LO PARAMETROS PARA LA VALIDACION NO PUEDEN SER NULOS O ESTAR VACIOS");
    }
    Pattern p = Pattern.compile(patron);
    Matcher m = p.matcher(usuario);
    valid = m.matches();
    
    log.info("USUARIO VALIDADO");
    return valid;
  }
  
  public static boolean expiredPassword(String fecha)
    throws Exception
  {
    int dia = Integer.parseInt(fecha.substring(6, 8));
    int mes = Integer.parseInt(fecha.substring(4, 6));
    int anio = Integer.parseInt(fecha.substring(0, 4));
    Date fechaD = new GregorianCalendar(anio, mes - 1, dia).getTime();
    boolean resp;
    if (fechaD.getTime() < new Date().getTime()) {
      resp = true;
    } else {
      resp = false;
    }
    log.info("CLAVE VERIFICADA");
    return resp;
  }
}

