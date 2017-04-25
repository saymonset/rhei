 package ve.org.bcv.rhei.util;
 
 import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import propiedades.PropertiesUrl;
 
 public class Log4JInitServlet
   extends HttpServlet
 {
   private static final long serialVersionUID = 1L;
   private static final int NUM = 512;
   
   public void init(ServletConfig config)
     throws ServletException
   {
     System.out.println("Log4JInitServlet inicializando log4j");
     
     
     
    String log4jLocation =null;// "propiedades/log4j-conf.properties";
     
     PropertiesUrl pUrl= new PropertiesUrl();
//     URL url = pUrl.getClass().getResource("log4j-conf.properties");
//     String log4jLocation =url.toString();
     
     InputStream fuente = pUrl.getClass().getResourceAsStream("log4j-conf.properties");
     try {
       File  WORKING_DIRECTORY=fileDesdeStream(fuente,
                    "log4j-conf",
                    "properties");
       log4jLocation=WORKING_DIRECTORY.getAbsolutePath();
     } catch (Exception e1) {
         // TODO Auto-generated catch block
         e1.printStackTrace();
     }
     
     if (log4jLocation == null)
     {
       System.err.println("*** No se ha proporcionado la ubicación del archivo de configuración del log4j, inicializando con la configuración básica");
       
 
       BasicConfigurator.configure();
     }
     else
     {
    	 
    	 
       File archivo = new File(log4jLocation);
       if (archivo.exists())
       {
         System.out.println("Inicializando log4j con el archivo de configuración ubicado en: " + 
           log4jLocation);
         PropertyConfigurator.configure(log4jLocation);
       }
       else
       {
         System.err.println("*** Archivo de configuración no encontrado, inicializando con la configuración básica");
         
         BasicConfigurator.configure();
       }
     }
     super.init(config);
   }
   
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
     throws ServletException, IOException
   {}
   
   private void setLogLevelWithParameter(PrintWriter out, String logLevel)
   {
     Logger root = Logger.getRootLogger();
     boolean logLevelRecognized = true;
     if ("DEBUG".equalsIgnoreCase(logLevel)) {
       root.setLevel(Level.DEBUG);
     } else if ("INFO".equalsIgnoreCase(logLevel)) {
       root.setLevel(Level.INFO);
     } else if ("WARN".equalsIgnoreCase(logLevel)) {
       root.setLevel(Level.WARN);
     } else if ("ERROR".equalsIgnoreCase(logLevel)) {
       root.setLevel(Level.ERROR);
     } else if ("FATAL".equalsIgnoreCase(logLevel)) {
       root.setLevel(Level.FATAL);
     } else {
       logLevelRecognized = false;
     }
     if (logLevelRecognized) {
       out.println("Nivel del log colocado en: " + logLevel + "<br/>");
     } else {
       out.println("Parámetro logLevel '" + logLevel + "' no es reconocido<br/>");
     }
   }
   
   public static final File fileDesdeStream(final InputStream fuente,
           String nombre,
          final String extension)
          throws Exception {
      byte[] buf = new byte[NUM];
      int len;
      File archivo = null;
      if (fuente == null) {
          throw new Exception("error.sis.fuente_invalida, fuente nula");
      }
      try {
          if (nombre.indexOf(".")!=-1){
              nombre=nombre.substring(0,nombre.indexOf("."));
          }
          archivo = new File(nombre+"."+extension);
              //File.createTempFile(
                //  "archivo_" + identificador + "_", "." + extension);
          archivo.deleteOnExit();
          OutputStream out = new FileOutputStream(archivo);
          while ((len = fuente.read(buf)) > 0) {
              out.write(buf, 0, len);
          }
          out.close();
      } catch (IOException ex) {
       
      }
      return archivo;
  }
   
   private void loadLog4jPropertiesFile(PrintWriter out)
   {
     ServletContext sc = getServletContext();
     String log4jLocation = getInitParameter("log4j-properties-location");
     if (log4jLocation == null)
     {
       out.println("*** No fue localizado el parámetro de inicio 'log4j-properties-location' , inicializando el log4j con la configuración básica<br/>");
       
 
       BasicConfigurator.configure();
     }
     else
     {
       String webAppPath = sc.getRealPath("/");
       String log4jProp = webAppPath + log4jLocation;
       File log4jFile = new File(log4jProp);
       if (log4jFile.exists())
       {
         out.println("Inicializando el log4j el archivo de configuración ubicado en la dirección: " + 
           log4jProp + "<br/>");
         PropertyConfigurator.configure(log4jProp);
       }
       else
       {
         out.println("*** No fue encontrado el archivo de configuración en la ubicación " + 
           log4jProp + " , inicializando el " + 
           "log4j con su configuración básica<br/>");
         BasicConfigurator.configure();
       }
     }
   }
 }
 