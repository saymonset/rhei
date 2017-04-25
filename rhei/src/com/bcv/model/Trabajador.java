 package com.bcv.model;
 
 import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import ve.org.bcv.rhei.bean.ShowResultToView;
 
 public class Trabajador implements Serializable
 {
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private static Logger log = Logger.getLogger(Trabajador.class.getName());
   private int codigoEmpleado;
   private int cedula;
   private String nombre;
   private String apellido;
   private String situacion;
   private String tipoEmpleado;
   private String tipoEmpleadoCod;
   private Date fechaIngreso;
   private Date fechaHasta;
   private String ubicacion;
   private String cargo;
   private String compania;
   private String nroExpediente;
   private String tipoNomina;
   private String nu_factura;
   /***Num Extension*/
   private String tlfNumExt;
   private String emailPropio;
   private String emailBcv;

   public int getCodigoEmpleado()
   {
     return this.codigoEmpleado;
   }
   
   public int getCedula()
   {
     return this.cedula;
   }
   
   public String getNombre()
   {
     return this.nombre;
   }
   
   public String getApellido()
   {
     return this.apellido;
   }
   
   public String getSituacion()
   {
     return this.situacion;
   }
   
   public String getTipoEmpleado()
   {
     return this.tipoEmpleado;
   }
   
   public Date getFechaIngreso()
   {
     return this.fechaIngreso;
   }
   
   public Date getFechaHasta()
   {
     return this.fechaHasta;
   }
   
   public String getUbicacion()
   {
     return this.ubicacion;
   }
   
   public String getCargo()
   {
     return this.cargo;
   }
   
   public String getCompania()
   {
     return this.compania;
   }
   
   public String getNroExpediente()
   {
     return this.nroExpediente;
   }
   
   public String getTipoNomina()
   {
     return this.tipoNomina;
   }
   
   public void setCodigoEmpleado(int codigoEmpleado)
   {
     this.codigoEmpleado = codigoEmpleado;
   }
   
   public void setCedula(int cedula)
   {
     this.cedula = cedula;
   }
   
   public void setNombre(String nombre)
   {
     this.nombre = nombre;
   }
   
   public void setApellido(String apellido)
   {
     this.apellido = apellido;
   }
   
   public void setSituacion(String situacion)
   {
     this.situacion = situacion;
   }
   
   public void setTipoEmpleado(String tipoEmpleado)
   {
     this.tipoEmpleado = tipoEmpleado;
   }
   
   public void setFechaIngreso(Date fechaIngreso)
   {
     this.fechaIngreso = fechaIngreso;
   }
   
   public void setFechaHasta(Date fechaHasta)
   {
     this.fechaHasta = fechaHasta;
   }
   
   public void setUbicacion(String ubicacion)
   {
     this.ubicacion = ubicacion;
   }
   
   public void setCargo(String cargo)
   {
     this.cargo = cargo;
   }
   
   public void setCompania(String compania)
   {
     this.compania = compania;
   }
   
   public void setNroExpediente(String nroExpediente)
   {
     this.nroExpediente = nroExpediente;
   }
   
   public void setTipoNomina(String tipoNomina)
   {
     this.tipoNomina = tipoNomina;
   }
   

   
 
   

   
   
   
   
   

   
// 
//   public String convertidorCadena(String cadena)
//   {
//     String nombreCargo = "";
//     String[] palabras = (String[])null;
//     cadena = cadena.replaceAll("\"", "'");
//     
//     palabras = cadena.split(" ");
//     for (int i = 0; i < palabras.length; i++) {
//       if ((palabras[i].compareTo("Ii") == 0) || (palabras[i].compareTo("Ii(H)") == 0)) {
//         palabras[i] = palabras[i].replaceAll("Ii", "II");
//       } else if ((palabras[i].compareTo("Iii") == 0) || (palabras[i].compareTo("Iii(H)") == 0)) {
//         palabras[i] = palabras[i].replaceAll("Iii", "III");
//       } else if ((palabras[i].compareTo("Iv") == 0) || (palabras[i].compareTo("Iv(H)") == 0)) {
//         palabras[i] = palabras[i].replaceAll("Iv", "IV");
//       } else if ((palabras[i].compareTo("Ofic.Ii(H)") == 0) || (palabras[i].compareTo("Ofic.Ii") == 0) || 
//         (palabras[i].compareTo("Sist.Ii(H)") == 0) || (palabras[i].compareTo("Sist.Ii") == 0)) {
//         palabras[i] = palabras[i].replaceAll(".Ii", ". II");
//       } else if ((palabras[i].compareTo("Ofic.Iii(H)") == 0) || (palabras[i].compareTo("Ofic.Iii") == 0) || 
//         (palabras[i].compareTo("Sist.Iii(H)") == 0) || (palabras[i].compareTo("Sist.Iii") == 0)) {
//         palabras[i] = palabras[i].replaceAll(".Iii", ". III");
//       } else if (palabras[i].compareTo("Rrhh") == 0) {
//         palabras[i] = palabras[i].replaceAll("Rrhh", ". RRHH");
//       }
//     }
//     for (int i = 0; i < palabras.length; i++) {
//       nombreCargo = nombreCargo + palabras[i] + " ";
//     }
//     nombreCargo = nombreCargo.trim();
//     return nombreCargo;
//   }
//   
   
//        aquí es donde sacamos los datos del trabajador
   
   public ShowResultToView cargarAtributosTrabajador(ShowResultToView showResultToView)
   {
     SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
     showResultToView.setCodigoEmpleado(this.codigoEmpleado+"");
     showResultToView.setCedulaEmpleado(String.valueOf(this.cedula));
     showResultToView.setApellido( this.apellido.toUpperCase());
     showResultToView.setNombre( this.nombre.toUpperCase());
     if (getFechaIngreso() == null) {
    	 showResultToView.setFechaIngreso("");
     } else {
    	 showResultToView.setFechaIngreso(format.format(this.fechaIngreso).toString());
     }
     log.debug("Tipo de empleado: " + getTipoEmpleado());
     if (("Contratado".equalsIgnoreCase(getTipoEmpleado().toUpperCase())) || ("Obrero Contratado".equalsIgnoreCase(getTipoEmpleado().toUpperCase())))
     {
       if (getFechaHasta() == null) {
    	   showResultToView.setFechaHasta("");
       } else {
    	   showResultToView.setFechaHasta( format.format(getFechaHasta()).toString());
       }
     }
     else {
    	 showResultToView.setFechaHasta("");
     }
     
     showResultToView.setTlfNumExt(this.tlfNumExt);
     showResultToView.setEmailBcv(this.emailBcv);
     showResultToView.setEmailPropio(this.emailPropio);
     
     showResultToView.setSituacion(this.situacion);
     showResultToView.setTipoEmpleado(this.tipoEmpleado.toUpperCase());
     showResultToView.setUbicacion(this.ubicacion);
     showResultToView.setCargo(this.cargo);
     showResultToView.setCompania(this.compania);
     showResultToView.setNroExpediente(this.nroExpediente);
     showResultToView.setTipoNomina(this.tipoNomina);
     return showResultToView;
   }
   
   

public static void setLog(Logger log) {
	Trabajador.log = log;
}

public String getNu_factura() {
	return nu_factura;
}



public String getTlfNumExt() {
	return tlfNumExt;
}

public void setTlfNumExt(String tlfNumExt) {
	this.tlfNumExt = tlfNumExt;
}

public String getEmailPropio() {
	return emailPropio;
}

public void setEmailPropio(String emailPropio) {
	this.emailPropio = emailPropio;
}

public String getEmailBcv() {
	return emailBcv;
}

public void setEmailBcv(String emailBcv) {
	this.emailBcv = emailBcv;
}

public String getTipoEmpleadoCod() {
	return tipoEmpleadoCod;
}

public void setTipoEmpleadoCod(String tipoEmpleadoCod) {
	this.tipoEmpleadoCod = tipoEmpleadoCod;
}
 }

