package ve.org.bcv.rhei.bean;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

public class Proveedor implements Serializable {
	private static Logger log = Logger.getLogger(Proveedor.class
			.getName());

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String valor;
	private String nombre;
	private String apellido;
	private String periodoEscolar;
	private int coFormaPago;
	private int receptorPago;
	private String tipoEmpleado;
	private String coStatus;
	private int value;
	private String label;
	private Boolean selected;
	private List<Proveedor> proveedors;
	public static Logger getLog() {
		return log;
	}
	public static void setLog(Logger log) {
		Proveedor.log = log;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	 
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public List<Proveedor> getProveedors() {
		return proveedors;
	}
	public void setProveedors(List<Proveedor> proveedors) {
		this.proveedors = proveedors;
	}
	public String getPeriodoEscolar() {
		return periodoEscolar;
	}
	public void setPeriodoEscolar(String periodoEscolar) {
		this.periodoEscolar = periodoEscolar;
	}
	public int getReceptorPago() {
		return receptorPago;
	}
	public void setReceptorPago(int receptorPago) {
		this.receptorPago = receptorPago;
	}
	public int getCoFormaPago() {
		return coFormaPago;
	}
	public void setCoFormaPago(int coFormaPago) {
		this.coFormaPago = coFormaPago;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Boolean getSelected() {
		return selected;
	}
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
	public String getCoStatus() {
		return coStatus;
	}
	public void setCoStatus(String coStatus) {
		this.coStatus = coStatus;
	}
	public String getTipoEmpleado() {
		return tipoEmpleado;
	}
	public void setTipoEmpleado(String tipoEmpleado) {
		this.tipoEmpleado = tipoEmpleado;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

}
