 package ve.org.bcv.rhei.util;
 
 import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
 
 public class Utilidades
 {
   private static Logger log = Logger.getLogger(Utilidades.class.getName());
   public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-mm-yyyy");
   public String convertirMesALetras(int numero)
   {
     String nombreMes = "";
     switch (numero)
     {
     case 0: 
       nombreMes = "MAT";
       break;
     case 1: 
       nombreMes = "ENE";
       break;
     case 2: 
       nombreMes = "FEB";
       break;
     case 3: 
       nombreMes = "MAR";
       break;
     case 4: 
       nombreMes = "ABR";
       break;
     case 5: 
       nombreMes = "MAY";
       break;
     case 6: 
       nombreMes = "JUN";
       break;
     case 7: 
       nombreMes = "JUL";
       break;
     case 8: 
       nombreMes = "AGO";
       break;
     case 9: 
       nombreMes = "SEP";
       break;
     case 10: 
       nombreMes = "OCT";
       break;
     case 11: 
       nombreMes = "NOV";
       break;
     case 12: 
       nombreMes = "DIC";
       break;
     }
     return nombreMes;
   }
   
   public static boolean isNumeric(String cadena)
   {
     try
     {
       Integer.parseInt(cadena);
       return true;
     }
     catch (NumberFormatException nfe) {}
     return false;
   }
   
 
   public static String cargarTitulo(String origen, String accion)
   {
     String titulo = "";
     String prefijo = "";
     if (((origen.compareToIgnoreCase("parametro") == 0) || 
       (origen.compareToIgnoreCase("beneficioEscolar") == 0) || 
       (origen.compareToIgnoreCase("periodoEscolar") == 0)) && 
       (accion.compareTo("") == 0)) {
       prefijo = "<h2>Administraci&oacute;n de ";
     } else if (accion.compareToIgnoreCase("crear") == 0) {
       prefijo = "<h2>Creaci&oacute;n de ";
     } else if (accion.compareToIgnoreCase("modificar") == 0) {
       prefijo = "<h2>Modificaci&oacute;n de ";
     }
     titulo = prefijo;
     if ((origen.compareTo("") != 0) && (accion.compareTo("") == 0))
     {
       if (origen.compareToIgnoreCase("parametro") == 0) {
         titulo = titulo + "Par&aacute;metro</h2>";
       } else if (origen.compareToIgnoreCase("beneficioEscolar") == 0) {
         titulo = titulo + "Beneficio Escolar</h2>";
       } else if (origen.compareToIgnoreCase("periodoEscolar") == 0) {
         titulo = titulo + "Per&iacute;odo Escolar</h2>";
       }
     }
     else if ((origen.compareTo("") != 0) && (accion.compareTo("") != 0))
     {
       if (origen.compareToIgnoreCase("parametro") == 0) {
         titulo = titulo + "Par&aacute;metro</h2>";
       } else if (origen.compareToIgnoreCase("beneficioEscolar") == 0) {
         titulo = titulo + "Beneficio Escolar</h2>";
       } else if (origen.compareToIgnoreCase("periodoEscolar") == 0) {
         titulo = titulo + "Per&iacute;odo Escolar</h2>";
       }
     }
     else {
       titulo = "Problemas con la definición del t&iacute;tulo";
     }
     return titulo;
   }
   
   public static String modificarEstiloTextArea(String tipoLetra, int longitud, int tamanoTextArea)
   {
     String estilo = "";
     if (tamanoTextArea == 60)
     {
       if (tipoLetra.compareTo("minuscula") == 0)
       {
         if (longitud <= 38) {
           estilo = "style=\"line-height: 32px;\"";
         } else if ((longitud > 38) && (longitud <= 78)) {
           estilo = "style=\"line-height: 16px;\"";
         } else if (longitud > 78) {
           estilo = "style=\"line-height: 10px;\"";
         }
       }
       else if (tipoLetra.compareTo("mayuscula") == 0) {
         if (longitud <= 34) {
           estilo = "style=\"line-height: 32px;\"";
         } else if ((longitud > 34) && (longitud <= 68)) {
           estilo = "style=\"line-height: 16px;\"";
         } else if (longitud > 68) {
           estilo = "style=\"line-height: 10px;\"";
         }
       }
     }
     else if (tamanoTextArea == 100) {
       if (tipoLetra.compareTo("minuscula") == 0)
       {
         if (longitud <= 70) {
           estilo = "style=\"line-height: 32px;\"";
         } else if ((longitud > 70) && (longitud <= 140)) {
           estilo = "style=\"line-height: 16px;\"";
         } else if (longitud > 140) {
           estilo = "style=\"line-height: 10px;\"";
         }
       }
       else if (tipoLetra.compareTo("mayuscula") == 0) {
         if (longitud <= 57) {
           estilo = "style=\"line-height: 32px;\"";
         } else if ((longitud > 57) && (longitud <= 114)) {
           estilo = "style=\"line-height: 16px;\"";
         } else if (longitud > 114) {
           estilo = "style=\"line-height: 10px;\"";
         }
        }
      }
      return estilo;
    }
   
   public static String convertidorCadena(String cadena)
   {
     String nombreCargo = "";
     String[] palabras = (String[])null;
     cadena = cadena.replaceAll("\"", "'");
     
     palabras = cadena.split(" ");
     for (int i = 0; i < palabras.length; i++) {
       if ((palabras[i].compareTo("Ii") == 0) || (palabras[i].compareTo("Ii(H)") == 0)) {
         palabras[i] = palabras[i].replaceAll("Ii", "II");
       } else if ((palabras[i].compareTo("Iii") == 0) || (palabras[i].compareTo("Iii(H)") == 0)) {
         palabras[i] = palabras[i].replaceAll("Iii", "III");
       } else if ((palabras[i].compareTo("Iv") == 0) || (palabras[i].compareTo("Iv(H)") == 0)) {
         palabras[i] = palabras[i].replaceAll("Iv", "IV");
       } else if ((palabras[i].compareTo("Ofic.Ii(H)") == 0) || (palabras[i].compareTo("Ofic.Ii") == 0) || 
         (palabras[i].compareTo("Sist.Ii(H)") == 0) || (palabras[i].compareTo("Sist.Ii") == 0)) {
         palabras[i] = palabras[i].replaceAll(".Ii", ". II");
       } else if ((palabras[i].compareTo("Ofic.Iii(H)") == 0) || (palabras[i].compareTo("Ofic.Iii") == 0) || 
         (palabras[i].compareTo("Sist.Iii(H)") == 0) || (palabras[i].compareTo("Sist.Iii") == 0)) {
         palabras[i] = palabras[i].replaceAll(".Iii", ". III");
       } else if (palabras[i].compareTo("Rrhh") == 0) {
         palabras[i] = palabras[i].replaceAll("Rrhh", ". RRHH");
       }
     }
     for (int i = 0; i < palabras.length; i++) {
       nombreCargo = nombreCargo + palabras[i] + " ";
     }
     nombreCargo = nombreCargo.trim();
     return nombreCargo;
   }
   
 
  }
  