/**
 * 
 */
package com.bcv.reporte.relacionpago;

import java.io.Serializable;
import java.util.List;

/**
 * Bean relacion pago
 * 
 * @author Ing Simon Alberto Rodriguez Pacheco 08/07/2015 10:46:59 2015 mail :
 *         oraclefedora@gmail.com
 */
public class ProveedorRpago1Bean
		implements
			Serializable,
			Comparable<ProveedorRpago1Bean> {

	/**
	 * 
	 */
	private long nuSolicitud;
	private static final long serialVersionUID = 1L;
	private String nbProveedor;
	private String nuRifProveedor;
	private String cedula;
	private String trabajador;
	private String ninio;
	private String nuExtension1;
	private String descripPeriodo;
	private String nuRefPago;
	private String tipoEmp;
	private List<FamiliarRpago2Bean> familiares;
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
	public long getNuSolicitud() {
		return nuSolicitud;
	}
	public void setNuSolicitud(long nuSolicitud) {
		this.nuSolicitud = nuSolicitud;
	}
	public String getDescripPeriodo() {
		return descripPeriodo;
	}
	public void setDescripPeriodo(String descripPeriodo) {
		this.descripPeriodo = descripPeriodo;
	}
	public List<FamiliarRpago2Bean> getFamiliares() {
		return familiares;
	}
	public void setFamiliares(List<FamiliarRpago2Bean> familiares) {
		this.familiares = familiares;
	}
	public String getNinio() {
		return ninio;
	}
	public void setNinio(String ninio) {
		this.ninio = ninio;
	}
	public String getNuRefPago() {
		return nuRefPago;
	}
	public void setNuRefPago(String nuRefPago) {
		this.nuRefPago = nuRefPago;
	}
	public int compareTo(ProveedorRpago1Bean o) {
		int comp = 0;
		if (o != null && o.getNbProveedor() != null
				&& o.getNbProveedor().length() > 0) {
			comp = o.getNbProveedor()
					.compareToIgnoreCase(this.getNbProveedor());
		}
		return comp;
	}
	public String getTipoEmp() {
		return tipoEmp;
	}
	public void setTipoEmp(String tipoEmp) {
		this.tipoEmp = tipoEmp;
	}
}
