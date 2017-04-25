/**
 * 
 */
package com.bcv.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 22/08/2016 14:45:26
 * 2016
 * mail : oraclefedora@gmail.com
 */
public class DetalleDefHistorico implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long coDefHistorico;
	private String nombreColegio;
	private String trabajador;
	private String nombreNino;
	private BigDecimal montoMensualPagTrab;
	private String montoMensualPagTrabStr;
	private String concepto;
	private BigDecimal montoTotal;
	private String  montoTotalStr;
	private String coRepStatus;
	private Date fecha;
	private String fechaStr;
	private String observacion;
	

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
	 
	public String getNombreColegio() {
		return nombreColegio;
	}
	public void setNombreColegio(String nombreColegio) {
		this.nombreColegio = nombreColegio;
	}
	public BigDecimal getMontoMensualPagTrab() {
		return montoMensualPagTrab;
	}
	public void setMontoMensualPagTrab(BigDecimal montoMensualPagTrab) {
		this.montoMensualPagTrab = montoMensualPagTrab;
	}
	public String getConcepto() {
		return concepto;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	public BigDecimal getMontoTotal() {
		return montoTotal;
	}
	public void setMontoTotal(BigDecimal montoTotal) {
		this.montoTotal = montoTotal;
	}
	public String getCoRepStatus() {
		return coRepStatus;
	}
	public void setCoRepStatus(String coRepStatus) {
		this.coRepStatus = coRepStatus;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getFechaStr() {
		return fechaStr;
	}
	public void setFechaStr(String fechaStr) {
		this.fechaStr = fechaStr;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public long getCoDefHistorico() {
		return coDefHistorico;
	}
	public void setCoDefHistorico(long coDefHistorico) {
		this.coDefHistorico = coDefHistorico;
	}
	public String getMontoTotalStr() {
		return montoTotalStr;
	}
	public void setMontoTotalStr(String montoTotalStr) {
		this.montoTotalStr = montoTotalStr;
	}
	public String getMontoMensualPagTrabStr() {
		return montoMensualPagTrabStr;
	}
	public void setMontoMensualPagTrabStr(String montoMensualPagTrabStr) {
		this.montoMensualPagTrabStr = montoMensualPagTrabStr;
	}
 
	
 
 
}
