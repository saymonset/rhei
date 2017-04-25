/**
 * 
 */
package com.bcv.model;

import java.util.Date;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 03/09/2015 10:42:23
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class MovStSolicBei {
	 private int nuSolicitud    ;
	 private String coStatus;
	 private Date feStatus;
	  private String inNivelEscolar;
	  private String  inBenefCompart;
	  private String  inTipoEmpresa;
	  private Double moAporteBcv;
	  private Double moPeriodo;
	  private Double moMatricula ;
	  private Double  moEmpresaRepres;
	  private String  coMoneda;
	  private int coPeriodo;
	  private String  txObservacion;
	  private String  nuTlfFamRelac;
	public int getNuSolicitud() {
		return nuSolicitud;
	}
	public void setNuSolicitud(int nuSolicitud) {
		this.nuSolicitud = nuSolicitud;
	}
	public String getCoStatus() {
		return coStatus;
	}
	public void setCoStatus(String coStatus) {
		this.coStatus = coStatus;
	}
	public Date getFeStatus() {
		return feStatus;
	}
	public void setFeStatus(Date feStatus) {
		this.feStatus = feStatus;
	}
	public String getInNivelEscolar() {
		return inNivelEscolar;
	}
	public void setInNivelEscolar(String inNivelEscolar) {
		this.inNivelEscolar = inNivelEscolar;
	}
	public String getInBenefCompart() {
		return inBenefCompart;
	}
	public void setInBenefCompart(String inBenefCompart) {
		this.inBenefCompart = inBenefCompart;
	}
	public String getInTipoEmpresa() {
		return inTipoEmpresa;
	}
	public void setInTipoEmpresa(String inTipoEmpresa) {
		this.inTipoEmpresa = inTipoEmpresa;
	}
	public Double getMoAporteBcv() {
		return moAporteBcv;
	}
	public void setMoAporteBcv(Double moAporteBcv) {
		this.moAporteBcv = moAporteBcv;
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
	public Double getMoEmpresaRepres() {
		return moEmpresaRepres;
	}
	public void setMoEmpresaRepres(Double moEmpresaRepres) {
		this.moEmpresaRepres = moEmpresaRepres;
	}
	public String getCoMoneda() {
		return coMoneda;
	}
	public void setCoMoneda(String coMoneda) {
		this.coMoneda = coMoneda;
	}
	public int getCoPeriodo() {
		return coPeriodo;
	}
	public void setCoPeriodo(int coPeriodo) {
		this.coPeriodo = coPeriodo;
	}
	public String getTxObservacion() {
		return txObservacion;
	}
	public void setTxObservacion(String txObservacion) {
		this.txObservacion = txObservacion;
	}
	public String getNuTlfFamRelac() {
		return nuTlfFamRelac;
	}
	public void setNuTlfFamRelac(String nuTlfFamRelac) {
		this.nuTlfFamRelac = nuTlfFamRelac;
	}
}
