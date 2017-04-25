/**
 * 
 */
package com.bcv.model;

import java.io.Serializable;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 14/10/2015 09:45:14
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class ReporteByDefinitivoOrTransitivo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String companiaAnalista;
	private String nroSolicitud;
	private String meses="";
	private String descripPeriodo="";
	private int receptorPago=0;
	private int coFormaPago1=0;
	private String coStatus="";
	private String tipoEmpleado="";
	private int filtrarByMesOrComplementoOrAmbos;
	private boolean isToCreateReportDefinitivo;
	public String getMeses() {
		return meses;
	}
	public void setMeses(String meses) {
		this.meses = meses;
	}
	public String getDescripPeriodo() {
		return descripPeriodo;
	}
	public void setDescripPeriodo(String descripPeriodo) {
		this.descripPeriodo = descripPeriodo;
	}
	public int getReceptorPago() {
		return receptorPago;
	}
	public void setReceptorPago(int receptorPago) {
		this.receptorPago = receptorPago;
	}
	public int getCoFormaPago1() {
		return coFormaPago1;
	}
	public void setCoFormaPago1(int coFormaPago1) {
		this.coFormaPago1 = coFormaPago1;
	}
	public String getCoStatus() {
		return coStatus;
	}
	public void setCoStatus(String coStatus) {
		this.coStatus = coStatus;
	}
	public String getTipoEmpleado() {
		return tipoEmpleado;
	}
	public void setTipoEmpleado(String tipoEmpleado) {
		this.tipoEmpleado = tipoEmpleado;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public int getFiltrarByMesOrComplementoOrAmbos() {
		return filtrarByMesOrComplementoOrAmbos;
	}
	public void setFiltrarByMesOrComplementoOrAmbos(
			int filtrarByMesOrComplementoOrAmbos) {
		this.filtrarByMesOrComplementoOrAmbos = filtrarByMesOrComplementoOrAmbos;
	}
	public String getNroSolicitud() {
		return nroSolicitud;
	}
	public void setNroSolicitud(String nroSolicitud) {
		this.nroSolicitud = nroSolicitud;
	}
	public String getCompaniaAnalista() {
		return companiaAnalista;
	}
	public void setCompaniaAnalista(String companiaAnalista) {
		this.companiaAnalista = companiaAnalista;
	}
	public boolean isToCreateReportDefinitivo() {
		return isToCreateReportDefinitivo;
	}
	public void setToReportDefinitivo(boolean isToCreateReportDefinitivo) {
		this.isToCreateReportDefinitivo = isToCreateReportDefinitivo;
	}
 
}
