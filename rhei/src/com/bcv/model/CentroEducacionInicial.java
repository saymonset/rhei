  package com.bcv.model;
  
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

import com.bcv.dao.jdbc.ManejadorDB;

import ve.org.bcv.rhei.bean.Proveedor;
import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.util.Utilidades;
  
  public class CentroEducacionInicial
  {
    private String nroRif;
    private String nombreCentro;
    private String localidad;
    private String telefono;
    private String telefono2;
    private String correo;
    private String nivelEscolaridad;
    private String tipoEducacion;
    private String tipoCentroEdu;
    private Double montoMatricula;
    private Double montoMensualidad;
    private String formaPago;
    private Double montoOtorgadoBCV;
    private String direccionCentro;
   
    
   

	public CentroEducacionInicial()
    {
      this.nroRif = "";
      this.nombreCentro = "";
      this.localidad = "";
      this.telefono = "";
      this.correo = "";
      this.nivelEscolaridad = "";
      this.tipoEducacion = "";
      this.tipoCentroEdu = "";
      this.montoMatricula = Double.valueOf(0.0D);
      this.montoMensualidad = Double.valueOf(0.0D);
      this.formaPago = "";
      this.montoOtorgadoBCV = Double.valueOf(0.0D);
      this.direccionCentro= "";
    }
    
    public String getNroRif()
    {
      return this.nroRif;
    }
    
    public String getNombreCentro()
    {
      return this.nombreCentro;
    }
    
    public String getLocalidad()
    {
      return this.localidad;
    }
    
    public String getTelefono()
    {
      return this.telefono;
    }
    
    public String getCorreo()
    {
      return this.correo;
    }
    
    public String getNivelEscolaridad()
    {
      return this.nivelEscolaridad;
    }
    
    public String getTipoEducacion()
    {
      return this.tipoEducacion;
    }
    
    public String getTipoCentroEdu()
    {
      return this.tipoCentroEdu;
    }
    
    public Double getMontoMatricula()
    {
      return this.montoMatricula;
    }
    
    public Double getMontoMensualidad()
    {
      return this.montoMensualidad;
    }
    
    public String getFormaPago()
    {
      return this.formaPago;
    }
    
    public Double getMontoOtorgadoBCV()
    {
      return this.montoOtorgadoBCV;
    }
    
    public void setNroRif(String nroRif)
    {
      this.nroRif = nroRif;
    }
    
    public void setNombreCentro(String nombreCentro)
    {
      this.nombreCentro = nombreCentro;
    }
    
    public void setLocalidad(String localidad)
    {
      this.localidad = localidad;
    }
    
    public void setTelefono(String telefono)
    {
      this.telefono = telefono;
    }
    
    public void setCorreo(String correo)
    {
      this.correo = correo;
    }
    
    public void setNivelEscolaridad(String nivelEscolaridad)
    {
      this.nivelEscolaridad = nivelEscolaridad;
    }
    
    public void setTipoEducacion(String tipoEducacion)
    {
      this.tipoEducacion = tipoEducacion;
    }
    
    public void setTipoCentroEdu(String tipoCentroEdu)
    {
      this.tipoCentroEdu = tipoCentroEdu;
    }
    
    public void setMontoMatricula(Double montoMatricula)
    {
      this.montoMatricula = montoMatricula;
    }
    
    public void setMontoMensualidad(Double montoMensualidad)
    {
      this.montoMensualidad = montoMensualidad;
    }
    
    public void setFormaPago(String formaPago)
    {
      this.formaPago = formaPago;
    }
    
    public void setMontoOtorgadoBCV(Double montoOtorgadoBCV)
    {
      this.montoOtorgadoBCV = montoOtorgadoBCV;
    }
    
    
    public String getDireccionCentro() {
		return direccionCentro;
	}

	public void setDireccionCentro(String direccionCentro) {
		this.direccionCentro = direccionCentro;
	}

	public String getTelefono2() {
		return telefono2;
	}

	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}
	
	

	
	 
   
    

  }
  