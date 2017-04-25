 package com.bcv.model;
 
 import java.util.Date;
 
 public class Beneficiario
 {
 
   private int codigo;
   private String nombre;
   private String apellido;
   private Date fechaNacimento;
   private int edad;
   private String status;

   
   public Beneficiario()
   {
     this.nombre = "";
     this.apellido = "";
     this.fechaNacimento = null;
     this.edad = 0;
     this.status = "";
   }
   
   public void setApellido(String apellido)
   {
     this.apellido = apellido;
   }
   
   public void setCodigo(int codigo)
   {
     this.codigo = codigo;
   }
   
   public void setEdad(int edad)
   {
     this.edad = edad;
   }
   
   public void setFechanacimento(Date date)
   {
     this.fechaNacimento = date;
   }
   
   public void setNombre(String nombre)
   {
     this.nombre = nombre;
   }
   
   public void setStatus(String status)
   {
     this.status = status;
   }
   
   public String getApellido()
   {
     return this.apellido;
   }
   
   public int getCodigo()
   {
     return this.codigo;
   }
   
   public int getEdad()
   {
     return this.edad;
   }
   
   public Date getFechaNacimento()
   {
     return this.fechaNacimento;
   }
   
   public String getNombre()
   {
     return this.nombre;
   }
   
   public String getStatus()
   {
     return this.status;
   }
   
   
 
  
 
 }
 