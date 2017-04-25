/**
 * 
 */
package com.bcv.reporte.relacionpago;

import java.io.Serializable;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 08/07/2015 11:04:21
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class FacturaRpago3Bean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long nuSolicitud;
	private long nuIdFactura;
	private String txObservaciones;
	private Double moTotalPago;
	private Double moFactura;
	private String txConceptoPago;
	private Double moPagoAdicional;
	   
    private String montoBCV;
    private String montoPeriodoBCV;
    private String montoMatriculaBCV;
    
	public long getNuIdFactura() {
		return nuIdFactura;
	}
	public void setNuIdFactura(long nuIdFactura) {
		this.nuIdFactura = nuIdFactura;
	}
	public String getTxObservaciones() {
		return txObservaciones;
	}
	public void setTxObservaciones(String txObservaciones) {
		this.txObservaciones = txObservaciones;
	}
	public Double getMoTotalPago() {
		return moTotalPago;
	}
	public void setMoTotalPago(Double moTotalPago) {
		this.moTotalPago = moTotalPago;
	}
	public Double getMoFactura() {
		return moFactura;
	}
	public void setMoFactura(Double moFactura) {
		this.moFactura = moFactura;
	}
	public String getTxConceptoPago() {
		return txConceptoPago;
	}
	public void setTxConceptoPago(String txConceptoPago) {
		this.txConceptoPago = txConceptoPago;
	}
	public Double getMoPagoAdicional() {
		return moPagoAdicional;
	}
	public void setMoPagoAdicional(Double moPagoAdicional) {
		this.moPagoAdicional = moPagoAdicional;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public long getNuSolicitud() {
		return nuSolicitud;
	}
	public void setNuSolicitud(long nuSolicitud) {
		this.nuSolicitud = nuSolicitud;
	}
	public String getMontoBCV() {
		return montoBCV;
	}
	public void setMontoBCV(String montoBCV) {
		this.montoBCV = montoBCV;
	}
	public String getMontoPeriodoBCV() {
		return montoPeriodoBCV;
	}
	public void setMontoPeriodoBCV(String montoPeriodoBCV) {
		this.montoPeriodoBCV = montoPeriodoBCV;
	}
	public String getMontoMatriculaBCV() {
		return montoMatriculaBCV;
	}
	public void setMontoMatriculaBCV(String montoMatriculaBCV) {
		this.montoMatriculaBCV = montoMatriculaBCV;
	}
	 
	

}
