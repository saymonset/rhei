/**
 * 
 */
package com.bcv.model;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 10/09/2015 14:58:03
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class ConyugeTrabajo {
	/**
	 * @param nuSolicitud
	 * @param nombreEmpresa
	 * @param telefonoEmpresa
	 * @param ciConyuge
	 * @param tlfConyuge
	 */
	
	private int ciConyuge;
	private String nombreEmpresa;
	private String telefonoEmpresa;
	private String correoConyuge;
	private String nuTlfTrabajo;
    private long codigoConyuge;
	
	
	
	
	
	
/***
 * 
 * int ciConyuge, String nombreEmpresa,
			String telefonoEmpresa,String txCorreoCony,  String nuTlfTrabajo
 * 
 * */
	
	
	public ConyugeTrabajo(){}
	
	public ConyugeTrabajo(int ciConyuge, String nombreEmpresa,
			String telefonoEmpresa,String correoConyuge, String nuTlfTrabajo) {
		super();
		this.ciConyuge = ciConyuge;
		this.nombreEmpresa = nombreEmpresa;
		this.telefonoEmpresa = telefonoEmpresa;
		this.correoConyuge=correoConyuge;
		this.nuTlfTrabajo = nuTlfTrabajo;
	}
	
 

	public String getNombreEmpresa() {
		return nombreEmpresa;
	}

	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}

	public String getTelefonoEmpresa() {
		return telefonoEmpresa;
	}

	public void setTelefonoEmpresa(String telefonoEmpresa) {
		this.telefonoEmpresa = telefonoEmpresa;
	}

 
 
	 

	public String getCorreoConyuge() {
		return correoConyuge;
	}

	public void setCorreoConyuge(String correoConyuge) {
		this.correoConyuge = correoConyuge;
	}
 

	public String getNuTlfTrabajo() {
		return nuTlfTrabajo;
	}

	public void setNuTlfTrabajo(String nuTlfTrabajo) {
		this.nuTlfTrabajo = nuTlfTrabajo;
	}

	public int getCiConyuge() {
		return ciConyuge;
	}

	public void setCiConyuge(int ciConyuge) {
		this.ciConyuge = ciConyuge;
	}

	public long getCodigoConyuge() {
		return codigoConyuge;
	}

	public void setCodigoConyuge(long codigoConyuge) {
		this.codigoConyuge = codigoConyuge;
	}
	
	
}