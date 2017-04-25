/**
 * 
 */
package com.bcv.reporte.pagotributo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.bcv.reporte.relacionpago.ProveedorRpago1Bean;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 21/08/2015 11:18:48
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class PagoTributoBean implements Serializable ,Comparable<PagoTributoBean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long nuSolicitud;
	private String nombreColegio;
	private Double moAporteBcv;
	private String trabajador;
	private String nombreNino;
	private String montoMensualPagTrab;
	private String concepto;
	private double monto;
	private String codigo;
	private double montoTotal;
	private BigDecimal montoTotalBiDec;
	private String montoTotalStr;
	
	
 
	
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
 
	public String getConcepto() {
		return concepto;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	public double getMonto() {
		return monto;
	}
	public void setMonto(double monto) {
		this.monto = monto;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public double getMontoTotal() {
		return montoTotal;
	}
	public void setMontoTotal(double montoTotal) {
		this.montoTotal = montoTotal;
	}
	public String getMontoTotalStr() {
		return montoTotalStr;
	}
	public void setMontoTotalStr(String montoTotalStr) {
		this.montoTotalStr = montoTotalStr;
	}
	public String getMontoMensualPagTrab() {
		return montoMensualPagTrab;
	}
	public void setMontoMensualPagTrab(String montoMensualPagTrab) {
		this.montoMensualPagTrab = montoMensualPagTrab;
	}
	public Double getMoAporteBcv() {
		return moAporteBcv;
	}
	public void setMoAporteBcv(Double moAporteBcv) {
		this.moAporteBcv = moAporteBcv;
	}
	public int compareTo(PagoTributoBean o) {
		int comp = 0;
		if (o != null && o.getNombreColegio() != null
				&& o.getNombreColegio().length() > 0) {
			comp = o.getNombreColegio()
					.compareToIgnoreCase(this.getNombreColegio());
		}
		return comp;
	}
	public long getNuSolicitud() {
		return nuSolicitud;
	}
	public void setNuSolicitud(long nuSolicitud) {
		this.nuSolicitud = nuSolicitud;
	}
	public BigDecimal getMontoTotalBiDec() {
		return montoTotalBiDec;
	}
	public void setMontoTotalBiDec(BigDecimal montoTotalBiDec) {
		this.montoTotalBiDec = montoTotalBiDec;
	}
	
	
}
