/**
 * 
 */
package com.bcv.dto;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 30/03/2016 15:19:26
 * 2016
 * mail : oraclefedora@gmail.com
 */
public class FacturaMontoMesHistorico {
   private String nuIdFactura;
   private Double montoBCV;
   private Double montoPeriodoBCV;
   private Double montoMatriculaBCV;
   private String meses;
public String getNuIdFactura() {
	return nuIdFactura;
}
public void setNuIdFactura(String nuIdFactura) {
	this.nuIdFactura = nuIdFactura;
}
 
public String getMeses() {
	return meses;
}
public void setMeses(String meses) {
	this.meses = meses;
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
}
