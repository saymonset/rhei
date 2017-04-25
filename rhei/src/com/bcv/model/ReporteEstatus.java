/**
 * 
 */
package com.bcv.model;

import java.io.Serializable;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 09/10/2015 08:25:12
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class ReporteEstatus implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long coReporstatus;
	private String nombre;
	private String status;
	private String nbTipo;
	private String txDescriPeriodo;
	private String nbFirmaReporte;
	private String nbAbrevReporte;
	private String nbCoordAdminist;
	private String nbUnidadContabil;
	private String nbCoordBenefSoc;
	private String nbCreadoPor;
	
	//,,
 
	public long getCoReporstatus() {
		return coReporstatus;
	}
	public void setCoReporstatus(long coReporstatus) {
		this.coReporstatus = coReporstatus;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
 
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getTxDescriPeriodo() {
		return txDescriPeriodo;
	}
	public void setTxDescriPeriodo(String txDescriPeriodo) {
		this.txDescriPeriodo = txDescriPeriodo;
	}
	public String getNbTipo() {
		return nbTipo;
	}
	public void setNbTipo(String nbTipo) {
		this.nbTipo = nbTipo;
	}
	public String getNbFirmaReporte() {
		return nbFirmaReporte;
	}
	public void setNbFirmaReporte(String nbFirmaReporte) {
		this.nbFirmaReporte = nbFirmaReporte;
	}
	public String getNbAbrevReporte() {
		return nbAbrevReporte;
	}
	public void setNbAbrevReporte(String nbAbrevReporte) {
		this.nbAbrevReporte = nbAbrevReporte;
	}
	public String getNbCoordAdminist() {
		return nbCoordAdminist;
	}
	public void setNbCoordAdminist(String nbCoordAdminist) {
		this.nbCoordAdminist = nbCoordAdminist;
	}
	public String getNbUnidadContabil() {
		return nbUnidadContabil;
	}
	public void setNbUnidadContabil(String nbUnidadContabil) {
		this.nbUnidadContabil = nbUnidadContabil;
	}
	public String getNbCoordBenefSoc() {
		return nbCoordBenefSoc;
	}
	public void setNbCoordBenefSoc(String nbCoordBenefSoc) {
		this.nbCoordBenefSoc = nbCoordBenefSoc;
	}
	public String getNbCreadoPor() {
		return nbCreadoPor;
	}
	public void setNbCreadoPor(String nbCreadoPor) {
		this.nbCreadoPor = nbCreadoPor;
	}

}
