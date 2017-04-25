/**
 * 
 */
package com.bcv.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 30/09/2015 11:04:38
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class EmpleadoInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nbUrbanizacion;
	private String nbCalleEsq; 
	private String nbVivienda;
	private Date desde;
	private Date	  hasta;
	
	public String getNbUrbanizacion() {
		return nbUrbanizacion;
	}
	public void setNbUrbanizacion(String nbUrbanizacion) {
		this.nbUrbanizacion = nbUrbanizacion;
	}
	public String getNbCalleEsq() {
		return nbCalleEsq;
	}
	public void setNbCalleEsq(String nbCalleEsq) {
		this.nbCalleEsq = nbCalleEsq;
	}
	public String getNbVivienda() {
		return nbVivienda;
	}
	public void setNbVivienda(String nbVivienda) {
		this.nbVivienda = nbVivienda;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Date getDesde() {
		return desde;
	}
	public void setDesde(Date desde) {
		this.desde = desde;
	}
	public Date getHasta() {
		return hasta;
	}
	public void setHasta(Date hasta) {
		this.hasta = hasta;
	} 
}
