package com.bcv.model;


public class PeriodoEscolar {
	private String codigoPeriodo;
	private String descripcion;
	private String fechaInicio;
	private String fechaFin;
	private String condicion;
	private String nu_factura;
	

	public String getCodigoPeriodo() {
		return this.codigoPeriodo;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public String getFechaInicio() {
		return this.fechaInicio;
	}

	public String getFechaFin() {
		return this.fechaFin;
	}

	public String getCondicion() {
		return this.condicion;
	}

	public void setCodigoPeriodo(String codigoPeriodo) {
		this.codigoPeriodo = codigoPeriodo;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}

	

	

 
		
		

	public String getNu_factura() {
		return nu_factura;
	}

	public void setNu_factura(String nu_factura) {
		this.nu_factura = nu_factura;
	}

 
}
