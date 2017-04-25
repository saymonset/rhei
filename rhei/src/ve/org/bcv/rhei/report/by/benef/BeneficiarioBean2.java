package ve.org.bcv.rhei.report.by.benef;

import java.io.Serializable;
import java.util.List;

public class BeneficiarioBean2 implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 
 
	private String nombrTrab;
	private String apellidoTrab;
	private int cedulaTrab;
	private String tlfNumExt;
	private String tipoEmpleado;
	private int cedulaFamiliar;
	private String nombreFlia;
	private String apellidoFlia;
	private String nuRifProveedor;
	private String nbProveedor;
	private String fechaNacimiento;
	
	private List<FacturaBean3> facturas;
	 
	 
 
	 
	 

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	 
	 
	public String getTipoEmpleado() {
		return tipoEmpleado;
	}
	public void setTipoEmpleado(String tipoEmpleado) {
		this.tipoEmpleado = tipoEmpleado;
	}
	public String getNombreFlia() {
		return nombreFlia;
	}
	public void setNombreFlia(String nombreFlia) {
		this.nombreFlia = nombreFlia;
	}
	public String getApellidoFlia() {
		return apellidoFlia;
	}
	public void setApellidoFlia(String apellidoFlia) {
		this.apellidoFlia = apellidoFlia;
	}
	public String getNuRifProveedor() {
		return nuRifProveedor;
	}
	public void setNuRifProveedor(String nuRifProveedor) {
		this.nuRifProveedor = nuRifProveedor;
	}
	 
	public String getNbProveedor() {
		return nbProveedor;
	}
	public void setNbProveedor(String nbProveedor) {
		this.nbProveedor = nbProveedor;
	}
	 
 
	public int getCedulaFamiliar() {
		return cedulaFamiliar;
	}
	public void setCedulaFamiliar(int cedulaFamiliar) {
		this.cedulaFamiliar = cedulaFamiliar;
	}
	public List<FacturaBean3> getFacturas() {
		return facturas;
	}
	public void setFacturas(List<FacturaBean3> facturas) {
		this.facturas = facturas;
	}


	public String getNombrTrab() {
		return nombrTrab;
	}


	public void setNombrTrab(String nombrTrab) {
		this.nombrTrab = nombrTrab;
	}


	public String getApellidoTrab() {
		return apellidoTrab;
	}


	public void setApellidoTrab(String apellidoTrab) {
		this.apellidoTrab = apellidoTrab;
	}


	public int getCedulaTrab() {
		return cedulaTrab;
	}


	public void setCedulaTrab(int cedulaTrab) {
		this.cedulaTrab = cedulaTrab;
	}


	public String getTlfNumExt() {
		return tlfNumExt;
	}


	public void setTlfNumExt(String tlfNumExt) {
		this.tlfNumExt = tlfNumExt;
	}


	public String getFechaNacimiento() {
		return fechaNacimiento;
	}


	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
 
	 
	
	

}
