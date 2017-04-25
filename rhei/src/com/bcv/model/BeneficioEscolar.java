package com.bcv.model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.bcv.dao.jdbc.ManejadorDB;

import ve.org.bcv.rhei.util.Utilidades;

/**
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com
 * 29/01/2015 14:29:26
 * 
 */
public class BeneficioEscolar implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String codigoBeneficio;
	private String descripcion;
	private String fechaRegistro;
	private String condicion;

	 

	public String getCodigoBeneficio() {
		return this.codigoBeneficio;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public String getFechaRegistro() {
		return this.fechaRegistro;
	}

	public String getCondicion() {
		return this.condicion;
	}

	public void setCodigoBeneficio(String codigoBeneficio) {
		this.codigoBeneficio = codigoBeneficio;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}


	
	
	

 
 
  
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	 
	
	
	
}
