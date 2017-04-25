package com.bcv.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class RelacionDePagos implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nroSolicitudsSeparatedByComa;
	private int nroSolicitud;
	private int nroIdFactura;
	private List<Integer> conceptoPagoArray = new ArrayList<Integer>();
	private List<Integer> mesMatriMenArray = new ArrayList<Integer>();
	private int InMesMatricula;
	private String tramite;
	private Timestamp fechaRegistroDePago;
	private int mesReembolso;
	private Double montoTotal;
	private int codigoPeriodo;
	private int receptorPago;
	private Double montoBCV;
	private Double montoMatricula;
	private Double montoPeriodo;
	private String beneficioCompartido;
	private int tipoPago;
	private String observaciones;
	private String coFormaPago;
	private String txDescriPeriodo;

	public int getTipoPago() {
		return this.tipoPago;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setTipoPago(int tipoPago) {
		this.tipoPago = tipoPago;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public int getNroSolicitud() {
		return this.nroSolicitud;
	}

	public int getNroIdFactura() {
		return this.nroIdFactura;
	}
 

	 

	public String getTramite() {
		return this.tramite;
	}

	public Timestamp getFechaRegistroDePago() {
		return this.fechaRegistroDePago;
	}

	public int getMesReembolso() {
		return this.mesReembolso;
	}

	public Double getMontoTotal() {
		return this.montoTotal;
	}

	public int getCodigoPeriodo() {
		return this.codigoPeriodo;
	}

	public int getReceptorPago() {
		return this.receptorPago;
	}

	public Double getMontoBCV() {
		return this.montoBCV;
	}

	public Double getMontoMatricula() {
		return this.montoMatricula;
	}

	public Double getMontoPeriodo() {
		return this.montoPeriodo;
	}

	public String getBeneficioCompartido() {
		return this.beneficioCompartido;
	}

	public void setNroSolicitud(int nroSolicitud) {
		this.nroSolicitud = nroSolicitud;
	}

	public void setNroIdFactura(int nroIdFactura) {
		this.nroIdFactura = nroIdFactura;
	}

	 

 
	public void setTramite(String tramite) {
		this.tramite = tramite;
	}

	public void setFechaRegistroDePago(Timestamp fechaRegistroDePago) {
		this.fechaRegistroDePago = fechaRegistroDePago;
	}

	public void setMesReembolso(int mesReembolso) {
		this.mesReembolso = mesReembolso;
	}

	public void setMontoTotal(Double montoTotal) {
		this.montoTotal = montoTotal;
	}

	public void setCodigoPeriodo(int codigoPeriodo) {
		this.codigoPeriodo = codigoPeriodo;
	}

	public void setReceptorPago(int receptorPago) {
		this.receptorPago = receptorPago;
	}

	public void setMontoBCV(Double montoBCV) {
		this.montoBCV = montoBCV;
	}

	public void setMontoMatricula(Double montoMatricula) {
		this.montoMatricula = montoMatricula;
	}

	public void setMontoPeriodo(Double montoPeriodo) {
		this.montoPeriodo = montoPeriodo;
	}

	public void setBeneficioCompartido(String beneficioCompartido) {
		this.beneficioCompartido = beneficioCompartido;
	}

	public int getInMesMatricula() {
		return InMesMatricula;
	}

	public void setInMesMatricula(int inMesMatricula) {
		InMesMatricula = inMesMatricula;
	}

 
	 
	public String getCoFormaPago() {
		return coFormaPago;
	}

	public void setCoFormaPago(String coFormaPago) {
		this.coFormaPago = coFormaPago;
	}

	public List<Integer> getMesMatriMenArray() {
		return mesMatriMenArray;
	}

	public void setMesMatriMenArray(List<Integer> mesMatriMenArray) {
		this.mesMatriMenArray = mesMatriMenArray;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<Integer> getConceptoPagoArray() {
		return conceptoPagoArray;
	}

	public void setConceptoPagoArray(List<Integer> conceptoPagoArray) {
		this.conceptoPagoArray = conceptoPagoArray;
	}

	public String getTxDescriPeriodo() {
		return txDescriPeriodo;
	}

	public void setTxDescriPeriodo(String txDescriPeriodo) {
		this.txDescriPeriodo = txDescriPeriodo;
	}

	public String getNroSolicitudsSeparatedByComa() {
		return nroSolicitudsSeparatedByComa;
	}

	public void setNroSolicitudsSeparatedByComa(String nroSolicitudsSeparatedByComa) {
		this.nroSolicitudsSeparatedByComa = nroSolicitudsSeparatedByComa;
	}
}
