/**
 * 
 */
package com.bcv.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 29/02/2016 11:15:43
 * 2016
 * mail : oraclefedora@gmail.com
 */
public class NuRefPago implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mes;
	private String mesKey;
	private Date fechaFactura ;
	private java.math.BigDecimal montoPago;
	private java.math.BigDecimal moAporteBcv;
	private java.math.BigDecimal moPeriodo;
	private java.math.BigDecimal moMatricula;
	private Boolean isCanPayMes;
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public Date getFechaFactura() {
		return fechaFactura;
	}
	public void setFechaFactura(Date fechaFactura) {
		this.fechaFactura = fechaFactura;
	}
	public java.math.BigDecimal getMontoPago() {
		return montoPago;
	}
	public void setMontoPago(java.math.BigDecimal montoPago) {
		this.montoPago = montoPago;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Boolean getIsCanPayMes() {
		return isCanPayMes;
	}
	public void setIsCanPayMes(Boolean isCanPayMes) {
		this.isCanPayMes = isCanPayMes;
	}
	public String getMesKey() {
		return mesKey;
	}
	public void setMesKey(String mesKey) {
		this.mesKey = mesKey;
	}
	public java.math.BigDecimal getMoAporteBcv() {
		return moAporteBcv;
	}
	public void setMoAporteBcv(java.math.BigDecimal moAporteBcv) {
		this.moAporteBcv = moAporteBcv;
	}
	public java.math.BigDecimal getMoPeriodo() {
		return moPeriodo;
	}
	public void setMoPeriodo(java.math.BigDecimal moPeriodo) {
		this.moPeriodo = moPeriodo;
	}
	public java.math.BigDecimal getMoMatricula() {
		return moMatricula;
	}
	public void setMoMatricula(java.math.BigDecimal moMatricula) {
		this.moMatricula = moMatricula;
	}
}
