 package ve.org.bcv.rhei.util;
 
 import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import propiedades.PropertiesUrl;
import ve.org.bcv.rhei.autenticador.ldap.connection.LDAPDelegador;
import ve.org.bcv.rhei.autenticador.ldap.dto.AtributoDTO;
import ve.org.bcv.rhei.autenticador.ldap.util.LDAPUtilities;

import com.bcv.dao.jdbc.BcvDao;
import com.bcv.dao.jdbc.impl.BcvDaoImpl;
import com.novell.ldap.LDAPException;
 
 public class Autenticar
   extends HttpServlet
 {
   private static final long serialVersionUID = -3446155465573221308L;
   private static Logger log = Logger.getLogger(Autenticar.class);
   private static String ATRIBUTOS_USUARIO_LDAP = "bcv.ldap.usuario.atributos";
   private static String inicio = "/jsp/principal.jsp";
   private static String login = "/jsp/login.jsp";
   String redireccion = "";
   private static final String ATTR_NAME = "fullName";
   private static final int POSC_ATTR_NAME = 0;
   private static final String ATTR_MAIL = "mail";
   private static final int POSC_ATTR_MAIL = 1;
   private static final String ATTR_CN = "cn";
   private static final int POSC_ATTR_CN = 2;
   private static final String ATTR_GRUPOS = "groupMembership";
   private static final int POSC_ATTR_GRUPOS = 3;
   private static final String ATTR_DN = "DN";
   private static final int POSC_ATTR_DN = 4;
   private static final int TOTAL_ATTR = 5;
   private static final String PATRON_GRUPO = "(.*)(cn=)([^,]*)(.*)";
   private static final int PATRON_GRUPO_UBICACION = 3;
   private static final String GRUPO_ADMINISTRADOR = "GC_User_RHEI_ADMIN";
   private static final String GRUPO_ANALISTA_SOL = "GC_User_RHEI_Analista_SOL";
   private static final String GRUPO_SOLICITUD = "GC_SOLICITUD";
   private static final String GRUPO_ANALISTA_PAGOS = "GC_User_RHEI_AnalistaPago";
   private static final String GRUPO_ANALISTA = "TODO";
   private static final String GRUPO_MONITOREO = "GC_User_RHEI_Monitoreo";
   private static final String SIN_GRUPO = "SG";
   private BcvDao bcvDaoImpl = new BcvDaoImpl();
   
   public void init()
     throws ServletException
   {}
   
   public void destroy() {}
   
   public void service(HttpServletRequest request, HttpServletResponse response)
     throws ServletException, IOException
   {
     log.debug("--> INGRESO AL SERVICIO");
     
     HttpSession sesion = null;
     RequestDispatcher rd = null;
     String usuario = "";
     String clave = "";
     String grupo = "";
     String nombreUsuario = "";
     String cedulaUsuario = "";
     String companiaAnalista = "";
     String aviso = "FinSesion";
     String redireccion = "/jsp/login.jsp";
     if (request.getSession(false) == null)
     {
       sesion = request.getSession(true);
       sesion.setAttribute("aviso", aviso);
       
       rd = request.getRequestDispatcher(redireccion);
       rd.forward(request, response);
     }
     else
     {
       try
       {
         if ((request.getParameter("login") != null) || (request.getParameter("password") != null))
         {
           usuario = request.getParameter("login").toUpperCase();
           clave = request.getParameter("password");
           log.debug("paso 0: capturando parámetros del formulario de login");
         }
         if ((usuario.equals("")) || (clave.equals("")))
         {
           sesion = request.getSession(true);
           sesion.setAttribute("ErrorBlanco", "1");
           redireccion = login;
           log.debug("redirigiendo a la pagina -> " + redireccion);
         }
         else
         {
           log.debug("paso 1");
           String[] resultadoConsulta = retornarConsulta(usuario, clave);
           
           log.debug("paso 2");
           if (resultadoConsulta == null)
           {
             log.info("ERROR DE LOGIN RESULTADO DE CONSULTA NULL");
             sesion = request.getSession(true);
             sesion.setAttribute("ValorErroneo", "1");
             redireccion = login;
             log.debug("redirigiendo a la pagina -> " + redireccion);
           }
           else
           {
             resultadoConsulta = validarCampoNull(resultadoConsulta);
             for (int i = 0; i < resultadoConsulta.length; i++) {
               log.debug("resultadoConsulta[" + i + "]= " + resultadoConsulta[i]);
             }
             grupo = resultadoConsulta[3];
             
             log.debug("Valor de la variable grupo: " + grupo);
             if (grupo.compareToIgnoreCase("SG") == 0)
             {
               log.info("USUARIO NO AUTORIZADO PARA USAR LA APLICACIÓN");
               sesion = request.getSession(true);
               sesion.setAttribute("UsuarioNoAutorizado", "1");
               redireccion = login;
               log.debug("redirigiendo a la pagina -> " + redireccion);
             }
             else
             {
               nombreUsuario = resultadoConsulta[0];
               cedulaUsuario = resultadoConsulta[2];
               companiaAnalista = bcvDaoImpl.cargarCompaniaAnalista(Integer.valueOf(cedulaUsuario).intValue());
               sesion = request.getSession(true);
 
               if (StringUtils.isEmpty(companiaAnalista)){
            	   companiaAnalista="01";
               }
               /**Siempre vamos a trabjar con esta compania 01**/ 
               companiaAnalista="01";
               sesion.setAttribute("nombreUsuario", nombreUsuario);
               sesion.setAttribute("cedulaUsuario", cedulaUsuario);
               sesion.setAttribute("companiaAnalista", companiaAnalista);
               log.debug("Valor de la variable companiaAnalista guardado en la sesión: " + sesion.getAttribute("companiaAnalista"));
               sesion.setAttribute("grupo", grupo);
               boolean isAdmin=false;
               if("GC_User_RHEI_ADMIN".equalsIgnoreCase(grupo)){
            	   isAdmin=true;
               }
               sesion.setAttribute("isAdmin", isAdmin);

               redireccion = inicio;
               log.debug("redirigiendo a la pagina -> " + redireccion);
             }
           }
         }
         log.debug("redirigiendo a la pagina -> " + redireccion);
         rd = request.getRequestDispatcher(redireccion);
         log.debug("--> SALIO DEL METODO");
         rd.forward(request, response);
       }
       catch (Exception e)
       {
         e.printStackTrace();
         log.error("HA OCURRIDO UNA EXCEPCION ");
         log.error("MENSAJE : " + e.getMessage());
         log.error("CAUSA DE LA EXCEPCION : " + e.getCause());
       }
       catch (Throwable e)
       {
         e.printStackTrace();
         log.error("HA OCURRIDO UNA EXCEPCION ");
         log.error("MENSAJE : " + e.getMessage());
         log.error("CAUSA DE LA EXCEPCION : " + e.getCause());
       }
     }
   }
   
   private String[] retornarConsulta(String usuario, String clave)
     throws UnsupportedEncodingException, LDAPException, Exception
   {
     log.debug("--> INGRESO AL METODO retornarConsulta");
     String[] resultado = new String[5];
     LDAPUtilities lDAPUtilities = new LDAPUtilities();
     LDAPDelegador delegator = new LDAPDelegador(lDAPUtilities.validarUsuarioExt(usuario));
     
     String[] attrUsuario = cargarAtributos();
     log.debug("Tamaño del array 'attrUsuario' :" + attrUsuario.length);
     for (int i = 0; i < attrUsuario.length; i++) {
       log.debug("attrUsuario[" + i + "]= " + attrUsuario[i]);
     }
     if (delegator.autenticarUsuario(usuario, clave))
     {
    	 

//    	 #VARIABLES PARA LA AUTENTICACION DE USUARIOS INTERNOS
//    	 bcv.ldap.usuario.atributos = fullName,groupMembership,cn
    	 
    	 
    	 
       Collection busquedaUsuario = delegator.obtenerAtributos(usuario, attrUsuario);
       if (busquedaUsuario == null)
       {
         log.info("USUARIO NO ENCONTRADO DURANTE LA BUSQUEDA");
         resultado = (String[])null;
       }
       else
       {
         log.info("USUARIO ENCONTRADO");
         resultado = buscarValorDeAtributoUsuario(busquedaUsuario, resultado);
         for (int i = 0; i < resultado.length; i++) {
           log.debug("resultado[" + i + "]= " + resultado[i]);
         }
       }
     }
     else
     {
       resultado = (String[])null;
       log.info("USUARIO NO AUTENTICADO --> " + usuario);
     }
     log.debug("--> SALIO DEL METODO");
     return resultado;
   }
   
   private String[] buscarValorDeAtributoUsuario(Collection busquedaUsuario, String[] resultado)
   {
     log.debug("--> INGRESO AL METODO buscarValorDeAtributoUsuario");
     boolean bandera = false;
     boolean bandera2 = false;
     boolean bandera3 = false;
     boolean bandera4 = false;
     boolean bandera5=false;
     Iterator it = busquedaUsuario.iterator();
     while (it.hasNext())
     {
       AtributoDTO atrib = new AtributoDTO();
       atrib = (AtributoDTO)it.next();
       
       log.debug("Atrinbuto -> " + atrib.getNombre() + " Valor -> " + atrib.getValor());
       if (atrib.getNombre().equals("fullName"))
       {
         if (((ArrayList)atrib.getValor()).size() == 0) {
           resultado[0] = "";
         } else {
           resultado[0] = ((String)((ArrayList)atrib.getValor()).get(0));
         }
       }
       else if (atrib.getNombre().equals("mail"))
       {
         if (((ArrayList)atrib.getValor()).size() == 0) {
           resultado[1] = "";
         } else {
           resultado[1] = ((String)((ArrayList)atrib.getValor()).get(0));
         }
       }
       else if (atrib.getNombre().equals("cn"))
       {
         if (((ArrayList)atrib.getValor()).size() == 0)
         {
           resultado[2] = "";
         }
         else
         {
           Collection valor1 = atrib.getValor();
           Iterator itValor1 = valor1.iterator();
           while (itValor1.hasNext())
           {
             String cnLdap = (String)itValor1.next();
             if (Utilidades.isNumeric(cnLdap)) {
               resultado[2] = cnLdap;
             }
           }
         }
       }
       else if (atrib.getNombre().equals("groupMembership"))
       {
         if (((ArrayList)atrib.getValor()).size() == 0) {
           resultado[3] = "SG";
         } else {
           try
           {
             Pattern patronBuscarGrupo = Pattern.compile("(.*)(cn=)([^,]*)(.*)");
             Collection valor = atrib.getValor();
             Iterator itValor = valor.iterator();
             while (itValor.hasNext())
             {
               String grupoLdap = (String)itValor.next();
               Matcher match = patronBuscarGrupo.matcher(grupoLdap);
               log.debug("------>  " + grupoLdap);
               if (match.matches())
               {
                 log.debug("HIZO MATCH EL GRUPO ");
                 String grupo = match.group(3);
                 log.debug("bandera  -> " + bandera);
                 if (grupo.equals("GC_User_RHEI_ADMIN"))
                 {
                  
                   bandera = true;
                 }
                 else if (!bandera)
                 {
                   if (grupo.equals("GC_User_RHEI_Analista_SOL"))
                   {
                  
                     bandera2 = true;
                     
                   }
                   else if (grupo.equals("GC_User_RHEI_AnalistaPago"))
                   {
              
                     bandera3 = true;
                     
                   }
                   else if (grupo.equals("GC_User_RHEI_Monitoreo"))
                   {
                   
                     bandera4 = true;
                 
                   }else if (grupo
									.equals("G_SAPI_EMPLEADOS")) {
								 
								bandera5 = true;
							}else if (grupo
									.equals("G_SAPI_NOMINA_EJECUTIVA")) {
							 
								bandera5 = true;
							}else if (grupo
									.equals("G_SAPI_OBREROS")) {
							 
								bandera5 = true;
							}else if (grupo
									.equals("G_SAPI_OBREROS_CONTRATADOS")) {
							 
								bandera5 = true;
							}else if (grupo
									.equals("G_SAPI_CONTRATADOS_BCV")) {
							 
								bandera5 = true;
							}else if (grupo
									.equals("G_SAPI_JUBILADOS")) {
								 
								bandera5 = true;
							}
                   
                   
                   
                 }
               }
             }
           
             if (bandera) {
               resultado[3] = "GC_User_RHEI_ADMIN";
             } else if ((bandera2) && (bandera3)) {
               resultado[3] = "GC_SOLICITUD";
              
             } else if ((bandera2) && (!bandera3)) {
               resultado[3] = "GC_SOLICITUD";
             } else if ((!bandera2) && (bandera3)) {
               resultado[3] = "GC_SOLICITUD";
             } else if ((!bandera) && (!bandera2) && 
               (!bandera3) && (bandera4)) {
               resultado[3] = "GC_SOLICITUD";
             }else if(bandera5){
            	  resultado[3] = "GC_SOLICITUD";
             }
             if (resultado[3] != null) {
               continue;
             }
             resultado[3] = "SG";
             log.debug(" ******* NO HIZO MATCH EL GRUPO ******* ");
           }
           catch (Exception e)
           {
             log.error(" Error al extraer Grupo");
             log.error("HA OCURRIDO UNA EXCEPCION ");
             log.error("MENSAJE : " + e.getMessage());
             log.error("CAUSA DE LA EXCEPCION : " + e.getCause());
             resultado[3] = "SG";
           }
         }
       }
       else if (atrib.getNombre().equals("DN")) {
         if (((ArrayList)atrib.getValor()).size() == 0) {
           resultado[4] = "";
         } else {
           resultado[4] = ((String)((ArrayList)atrib.getValor()).get(0));
         }
       }
     }
     log.debug("--> SALIO DEL METODO buscarValorDeAtributoUsuario");
     
     return resultado;
   }
   
   private String[] cargarAtributos()
     throws Exception
   {
     log.debug("--> INGRESO AL METODO cargarAtributos");
     
     String[] atributos = (String[])null;
     
     Properties prop = new Properties();
     PropertiesUrl pUrl= new PropertiesUrl();
     
     prop.load(pUrl.getClass().getResourceAsStream("config.properties"));
    // prop = LDAPUtilities.cargarPropiedades(PROPIEDADES_CLIENTE_FINAL);
     
     String arg = (String)prop.get(ATRIBUTOS_USUARIO_LDAP);
     
     log.debug("Atributos solicitados al LDAP -> " + arg);
     
     atributos = arg.split(",");
     
     log.debug("--> Salio METODO cargarAtributos");
     
     return atributos;
   }
   
   private String[] validarCampoNull(String[] atributoUsuario)
   {
     if (atributoUsuario[0] == null) {
       atributoUsuario[0] = "";
     }
     if (atributoUsuario[1] == null) {
       atributoUsuario[1] = "";
     }
     if (atributoUsuario[2] == null) {
       atributoUsuario[2] = "";
     }
     if (atributoUsuario[3] == null) {
       atributoUsuario[3] = "SG";
     }
     return atributoUsuario;
   }
   
   private static boolean compareTime(String UTCTime)
   {
     Date date = null;Date currentDate = new Date();
     
     DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
     
     boolean resp = false;
     try
     {
       TimeZone tz = TimeZone.getDefault();
       formatter.setTimeZone(tz);
       log.debug("Time Zone ---> " + tz);
       if (UTCTime.equalsIgnoreCase(""))
       {
         date = new Date(currentDate.getTime() - 1000L);
         log.debug("Se forzo la hora solicitada ya que estaba en blanco " + 
           date);
       }
       else
       {
         date = formatter.parse(UTCTime);
       }
       log.debug(" Fecha del sistema --------> " + date);
       log.debug(" Fecha del usuario --------> " + currentDate);
       resp = date.after(currentDate);
     }
     catch (Exception e)
     {
       log.error("HA OCURRIDO UNA EXCEPCION ");
       log.error("MENSAJE : " + e.getMessage());
       log.error("CAUSA DE LA EXCEPCION : " + e.getCause());
       resp = false;
     }
     return resp;
   }
 }

 