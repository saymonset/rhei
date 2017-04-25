package com.bcv.model;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 02/07/2015 15:33:39
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class Solicitud implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(Solicitud.class.getName());
	private int idSolicitud;
	private int nroSolicitud;
	private Timestamp fechaSolicitud;
	private int cedula;
	private int codigoEmpleado;
	private int cedulaFamiliar;
	private int edadMin;
	private int edadMax;
	private String formaDePago;
	private String nroRifCentroEdu;
	private String tipoEducacion;
	private String nivelEscolar;
	private String tipoCentroEdu;
	private String periodoDePago;
	private Double montoPeriodo;
	private Double montoAporteBCV;
	private Double montoMatricula;
	private Timestamp fechaSolicitudOriginal;
	private String co_status;
	/**Nombre del estatus que esta en la tabla  RHEI.ST_SOLICITUD_BEI;*/
	private String nb_status;
	private String comparteBeneficio;
	private String tipoEmpresa;
	private Double montoEmpresa;
	private String textoPeriodo;
	private int codigoPeriodo;
	private String localidadCEI;
	private String tipoNomina;

	public int getIdSolicitud() {
		return this.idSolicitud;
	}

	public int getNroSolicitud() {
		return this.nroSolicitud;
	}

	public Timestamp getFechaSolicitud() {
		return this.fechaSolicitud;
	}

	public int getCedula() {
		return this.cedula;
	}

	public int getCodigoEmpleado() {
		return this.codigoEmpleado;
	}

	public int getCedulaFamiliar() {
		return this.cedulaFamiliar;
	}

	public int getEdadMin() {
		return this.edadMin;
	}

	public int getEdadMax() {
		return this.edadMax;
	}

	public String getFormaDePago() {
		return this.formaDePago;
	}

	public String getNroRifCentroEdu() {
		return this.nroRifCentroEdu;
	}

	public String getTipoEducacion() {
		return this.tipoEducacion;
	}

	public String getNivelEscolar() {
		return this.nivelEscolar;
	}

	public String getTipoCentroEdu() {
		return this.tipoCentroEdu;
	}

	public String getPeriodoDePago() {
		return this.periodoDePago;
	}

	public Double getMontoPeriodo() {
		return this.montoPeriodo;
	}

	public Double getMontoAporteBCV() {
		return this.montoAporteBCV;
	}

	public Double getMontoMatricula() {
		return this.montoMatricula;
	}

	public Timestamp getFechaSolicitudOriginal() {
		return this.fechaSolicitudOriginal;
	}

	public String getCo_status() {
		return this.co_status;
	}

	public String getComparteBeneficio() {
		return this.comparteBeneficio;
	}

	public String getTipoEmpresa() {
		return this.tipoEmpresa;
	}

	public Double getMontoEmpresa() {
		return this.montoEmpresa;
	}

	public String getTextoPeriodo() {
		return this.textoPeriodo;
	}

	public int getCodigoPeriodo() {
		return this.codigoPeriodo;
	}

	public String getLocalidadCEI() {
		return this.localidadCEI;
	}

	public void setIdSolicitud(int idSolicitud) {
		this.idSolicitud = idSolicitud;
	}

	public void setNroSolicitud(int nroSolicitud) {
		this.nroSolicitud = nroSolicitud;
	}

	public void setFechaSolicitud(Timestamp fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	public void setCedula(int cedula) {
		this.cedula = cedula;
	}

	public void setCodigoEmpleado(int codigoEmpleado) {
		this.codigoEmpleado = codigoEmpleado;
	}

	public void setCedulaFamiliar(int cedulaFamiliar) {
		this.cedulaFamiliar = cedulaFamiliar;
	}

	public void setEdadMin(int edadMin) {
		this.edadMin = edadMin;
	}

	public void setEdadMax(int edadMax) {
		this.edadMax = edadMax;
	}

	public void setFormaDePago(String formaDePago) {
		this.formaDePago = formaDePago;
	}

	public void setNroRifCentroEdu(String nroRifCentroEdu) {
		this.nroRifCentroEdu = nroRifCentroEdu;
	}

	public void setTipoEducacion(String tipoEducacion) {
		this.tipoEducacion = tipoEducacion;
	}

	public void setNivelEscolar(String nivelEscolar) {
		this.nivelEscolar = nivelEscolar;
	}

	public void setTipoCentroEdu(String tipoCentroEdu) {
		this.tipoCentroEdu = tipoCentroEdu;
	}

	public void setPeriodoDePago(String periodoDePago) {
		this.periodoDePago = periodoDePago;
	}

	public void setMontoPeriodo(Double montoPeriodo) {
		this.montoPeriodo = montoPeriodo;
	}

	public void setMontoAporteBCV(Double montoAporteBCV) {
		this.montoAporteBCV = montoAporteBCV;
	}

	public void setMontoMatricula(Double montoMatricula) {
		this.montoMatricula = montoMatricula;
	}

	public void setFechaSolicitudOriginal(Timestamp fechaSolicitudOriginal) {
		this.fechaSolicitudOriginal = fechaSolicitudOriginal;
	}

	public void setCo_status(String coStatus) {
		this.co_status = coStatus;
	}

	public void setComparteBeneficio(String comparteBeneficio) {
		this.comparteBeneficio = comparteBeneficio;
	}

	public void setTipoEmpresa(String tipoEmpresa) {
		this.tipoEmpresa = tipoEmpresa;
	}

	public void setMontoEmpresa(Double montoEmpresa) {
		this.montoEmpresa = montoEmpresa;
	}

	public void setTextoPeriodo(String textoPeriodo) {
		this.textoPeriodo = textoPeriodo;
	}

	public void setCodigoPeriodo(int codigoPeriodo) {
		this.codigoPeriodo = codigoPeriodo;
	}

	public void setLocalidadCEI(String localidadCEI) {
		this.localidadCEI = localidadCEI;
	}
	
	
	
	




	
	


	

	public static Logger getLog() {
		return log;
	}

	public static void setLog(Logger log) {
		Solicitud.log = log;
	}

	public String getTipoNomina() {
		return tipoNomina;
	}

	public void setTipoNomina(String tipoNomina) {
		this.tipoNomina = tipoNomina;
	}

	public String getNb_status() {
		return nb_status;
	}

	public void setNb_status(String nb_status) {
		this.nb_status = nb_status;
	}
}
