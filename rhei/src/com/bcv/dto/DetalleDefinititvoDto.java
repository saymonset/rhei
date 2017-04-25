/**
 * 
 */
package com.bcv.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 22/08/2016 11:02:20
 * 2016
 * mail : oraclefedora@gmail.com
 */
public class DetalleDefinititvoDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nombreColegio;
	private String trabajador;
	private String nombreNino;
	private String montoMensualPagTrab;
	private String concepto;
	private String montoTotalStr;
	private String codigo;
	private String nuSolicitud;
	private Date fecha;
	private String coRepStatus;

	public String getNombreColegio() {
		return nombreColegio;
	}
	public void setNombreColegio(String nombreColegio) {
		this.nombreColegio = nombreColegio;
	}
	public String getTrabajador() {
		return trabajador;
	}
	public void setTrabajador(String trabajador) {
		this.trabajador = trabajador;
	}
	public String getNombreNino() {
		return nombreNino;
	}
	public void setNombreNino(String nombreNino) {
		this.nombreNino = nombreNino;
	}
	public String getMontoMensualPagTrab() {
		return montoMensualPagTrab;
	}
	public void setMontoMensualPagTrab(String montoMensualPagTrab) {
		this.montoMensualPagTrab = montoMensualPagTrab;
	}
	public String getConcepto() {
		return concepto;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	public String getMontoTotalStr() {
		return montoTotalStr;
	}
	public void setMontoTotalStr(String montoTotalStr) {
		this.montoTotalStr = montoTotalStr;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNuSolicitud() {
		return nuSolicitud;
	}
	public void setNuSolicitud(String nuSolicitud) {
		this.nuSolicitud = nuSolicitud;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getCoRepStatus() {
		return coRepStatus;
	}
	public void setCoRepStatus(String coRepStatus) {
		this.coRepStatus = coRepStatus;
	}
}
