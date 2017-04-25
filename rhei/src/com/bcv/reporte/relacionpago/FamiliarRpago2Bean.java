/**
 * 
 */
package com.bcv.reporte.relacionpago;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 08/07/2015 10:52:06
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class FamiliarRpago2Bean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long nuSolicitud;
	private String cedulaFamiliar;
	private String nombreFlia;
	private String nombre;
	private String apellido;
	private Date fechaNacimiento;
	private int edad;
	private Double moPeriodo;
	private Double moMatricula;
	private Double moAporteBcv;
	private List<FacturaRpago3Bean> facturas;
	
	public long getNuSolicitud() {
		return nuSolicitud;
	}
	public void setNuSolicitud(long nuSolicitud) {
		this.nuSolicitud = nuSolicitud;
	}
	public String getCedulaFamiliar() {
		return cedulaFamiliar;
	}
	public void setCedulaFamiliar(String cedulaFamiliar) {
		this.cedulaFamiliar = cedulaFamiliar;
	}
	public String getNombreFlia() {
		return nombreFlia;
	}
	public void setNombreFlia(String nombreFlia) {
		this.nombreFlia = nombreFlia;
	}
	 
	public int getEdad() {
		return edad;
	}
	public void setEdad(int edad) {
		this.edad = edad;
	}
	public Double getMoPeriodo() {
		return moPeriodo;
	}
	public void setMoPeriodo(Double moPeriodo) {
		this.moPeriodo = moPeriodo;
	}
	public Double getMoMatricula() {
		return moMatricula;
	}
	public void setMoMatricula(Double moMatricula) {
		this.moMatricula = moMatricula;
	}
	public Double getMoAporteBcv() {
		return moAporteBcv;
	}
	public void setMoAporteBcv(Double moAporteBcv) {
		this.moAporteBcv = moAporteBcv;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public List<FacturaRpago3Bean> getFacturas() {
		return facturas;
	}
	public void setFacturas(List<FacturaRpago3Bean> facturas) {
		this.facturas = facturas;
	}
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

}
