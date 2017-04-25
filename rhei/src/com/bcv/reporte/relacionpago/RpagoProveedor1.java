/**
 * 
 */
package com.bcv.reporte.relacionpago;

import java.io.Serializable;

/**
 * Bean relacion pago
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 08/07/2015 10:46:59
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class RpagoProveedor1 implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nbProveedor;
	private String nuRifProveedor;
	private String cedula;
	private String trabajador;
	private String nuExtension1;
	public String getNbProveedor() {
		return nbProveedor;
	}
	public void setNbProveedor(String nbProveedor) {
		this.nbProveedor = nbProveedor;
	}
	public String getNuRifProveedor() {
		return nuRifProveedor;
	}
	public void setNuRifProveedor(String nuRifProveedor) {
		this.nuRifProveedor = nuRifProveedor;
	}
	public String getCedula() {
		return cedula;
	}
	public void setCedula(String cedula) {
		this.cedula = cedula;
	}
	public String getTrabajador() {
		return trabajador;
	}
	public void setTrabajador(String trabajador) {
		this.trabajador = trabajador;
	}
	public String getNuExtension1() {
		return nuExtension1;
	}
	public void setNuExtension1(String nuExtension1) {
		this.nuExtension1 = nuExtension1;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
