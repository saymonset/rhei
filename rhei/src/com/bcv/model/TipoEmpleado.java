/**
 * 
 */
package com.bcv.model;

import java.io.Serializable;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 21/08/2015 09:29:29
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class TipoEmpleado  implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tipoEmp;
	private String siglas;
	private String descripcion;
	private String clasificacion;
	private int codigoContable;
	private int codigoCia;
	private int codigoPresupuestario;
	public String getTipoEmp() {
		return tipoEmp;
	}
	public void setTipoEmp(String tipoEmp) {
		this.tipoEmp = tipoEmp;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getClasificacion() {
		return clasificacion;
	}
	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	}
	public int getCodigoContable() {
		return codigoContable;
	}
	public void setCodigoContable(int codigoContable) {
		this.codigoContable = codigoContable;
	}
	public int getCodigoCia() {
		return codigoCia;
	}
	public void setCodigoCia(int codigoCia) {
		this.codigoCia = codigoCia;
	}
	public int getCodigoPresupuestario() {
		return codigoPresupuestario;
	}
	public void setCodigoPresupuestario(int codigoPresupuestario) {
		this.codigoPresupuestario = codigoPresupuestario;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getSiglas() {
		return siglas;
	}
	public void setSiglas(String siglas) {
		this.siglas = siglas;
	}

}
