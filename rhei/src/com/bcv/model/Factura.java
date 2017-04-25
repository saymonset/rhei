  package com.bcv.model;

import java.io.Serializable;
  
  /**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 03/07/2015 14:21:39
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class Factura implements Serializable
  {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long nuSolicitud;
    private int nroIdFactura;
    private String nroFactura;
    private String fechaFactura;
    private String nroRifProv;
    private String nroControl;
    private Double montoFactura;
    private String stringMontoFactura;
    private Double  moTotalPago;
    private String txtcomplemento;
    private Double montocomplemento;
    private String inComplemento;
    
    private int nuRefPago;
    private String txObservaciones;
    private String txConceptoPago;
    private double moPagoAdicional;
    private int inReceptorPago;
    private String stringReceptorPago;
    /**Is pagado en pagos y tributos*/
    private int statusReporteDefinitivo;
    /**Si es pagado, obtendremos el nombre del reporte del pago definitivo donde aparece ekl pago*/
    private String nombReportPagDef;
    private String txFormaPago;
    private int coRepStatus;
    private String moAporteBcv;
    private boolean isReembolso;
    
    private Double montoBCV;
    private Double montoPeriodoBCV;
    private Double montoMatriculaBCV;
    private String nbPagadoPor   ;
    
 
 
 
    
    
    public int getNroIdFactura()
    {
      return this.nroIdFactura;
    }
    
    public String getNroFactura()
    {
      return this.nroFactura;
    }
    
    public String getFechaFactura()
    {
      return this.fechaFactura;
    }
    
    public String getNroRifProv()
    {
      return this.nroRifProv;
    }
    
    public String getNroControl()
    {
      return this.nroControl;
    }
    
    public Double getMontoFactura()
    {
      return this.montoFactura;
    }
    
    public void setNroIdFactura(int nroIdFactura)
    {
      this.nroIdFactura = nroIdFactura;
    }
    
    public void setNroFactura(String nroFactura)
    {
      this.nroFactura = nroFactura;
    }
    
    public void setFechaFactura(String fechaFactura)
    {
      this.fechaFactura = fechaFactura;
    }
    
    public void setNroRifProv(String nroRifProv)
    {
      this.nroRifProv = nroRifProv;
    }
    
    public void setNroControl(String nroControl)
    {
      this.nroControl = nroControl;
    }
    
    public void setMontoFactura(Double montoFactura)
    {
      this.montoFactura = montoFactura;
    }

	public String getTxtcomplemento() {
		return txtcomplemento;
	}

	public void setTxtcomplemento(String txtcomplemento) {
		this.txtcomplemento = txtcomplemento;
	}

	public Double getMontocomplemento() {
		return montocomplemento;
	}

	public void setMontocomplemento(Double montocomplemento) {
		this.montocomplemento = montocomplemento;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getNuRefPago() {
		return nuRefPago;
	}

	public void setNuRefPago(int nuRefPago) {
		this.nuRefPago = nuRefPago;
	}

	public String getTxObservaciones() {
		return txObservaciones;
	}

	public void setTxObservaciones(String txObservaciones) {
		this.txObservaciones = txObservaciones;
	}

	public String getTxConceptoPago() {
		return txConceptoPago;
	}

	public void setTxConceptoPago(String txConceptoPago) {
		this.txConceptoPago = txConceptoPago;
	}

//	public double getMoPagoAdicional() {
//		return moPagoAdicional;
//	}
//
//	public void setMoPagoAdicional(double moPagoAdicional) {
//		this.moPagoAdicional = moPagoAdicional;
//	}

	public Double getMoTotalPago() {
		return moTotalPago;
	}

	public void setMoTotalPago(Double moTotalPago) {
		this.moTotalPago = moTotalPago;
	}

	public String getInComplemento() {
		return inComplemento;
	}

	public void setInComplemento(String inComplemento) {
		this.inComplemento = inComplemento;
	}

	public long getNuSolicitud() {
		return nuSolicitud;
	}

	public void setNuSolicitud(long nuSolicitud) {
		this.nuSolicitud = nuSolicitud;
	}

	public double getMoPagoAdicional() {
		return moPagoAdicional;
	}

	public void setMoPagoAdicional(double moPagoAdicional) {
		this.moPagoAdicional = moPagoAdicional;
	}

	public Double getMontoBCV() {
		return montoBCV;
	}

	public void setMontoBCV(Double montoBCV) {
		this.montoBCV = montoBCV;
	}

	public Double getMontoPeriodoBCV() {
		return montoPeriodoBCV;
	}

	public void setMontoPeriodoBCV(Double montoPeriodoBCV) {
		this.montoPeriodoBCV = montoPeriodoBCV;
	}

	public Double getMontoMatriculaBCV() {
		return montoMatriculaBCV;
	}

	public void setMontoMatriculaBCV(Double montoMatriculaBCV) {
		this.montoMatriculaBCV = montoMatriculaBCV;
	}

	public int getInReceptorPago() {
		return inReceptorPago;
	}

	public void setInReceptorPago(int inReceptorPago) {
		this.inReceptorPago = inReceptorPago;
	}

	public String getStringReceptorPago() {
		return stringReceptorPago;
	}

	public void setStringReceptorPago(String stringReceptorPago) {
		this.stringReceptorPago = stringReceptorPago;
	}

	public String getStringMontoFactura() {
		return stringMontoFactura;
	}

	public void setStringMontoFactura(String stringMontoFactura) {
		this.stringMontoFactura = stringMontoFactura;
	}

 
	public String getNombReportPagDef() {
		return nombReportPagDef;
	}

	public void setNombReportPagDef(String nombReportPagDef) {
		this.nombReportPagDef = nombReportPagDef;
	}

	public String getTxFormaPago() {
		return txFormaPago;
	}

	public void setTxFormaPago(String txFormaPago) {
		this.txFormaPago = txFormaPago;
	}

	public int getStatusReporteDefinitivo() {
		return statusReporteDefinitivo;
	}

	public void setStatusReporteDefinitivo(int statusReporteDefinitivo) {
		this.statusReporteDefinitivo = statusReporteDefinitivo;
	}

	public int getCoRepStatus() {
		return coRepStatus;
	}

	public void setCoRepStatus(int coRepStatus) {
		this.coRepStatus = coRepStatus;
	}

	public String getMoAporteBcv() {
		return moAporteBcv;
	}

	public void setMoAporteBcv(String moAporteBcv) {
		this.moAporteBcv = moAporteBcv;
	}

	public boolean isReembolso() {
		return isReembolso;
	}

	public void setReembolso(boolean isReembolso) {
		this.isReembolso = isReembolso;
	}

	public String getNbPagadoPor() {
		return nbPagadoPor;
	}

	public void setNbPagadoPor(String nbPagadoPor) {
		this.nbPagadoPor = nbPagadoPor;
	}


 

	 

  
	 
    
  }
 
 