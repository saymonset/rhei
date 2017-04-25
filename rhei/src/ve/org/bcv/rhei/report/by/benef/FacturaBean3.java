package ve.org.bcv.rhei.report.by.benef;

import java.io.Serializable;
import java.util.List;

public class FacturaBean3 implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	  private int nroIdFactura;
	private String nroFactura;
	private String nroControl;
	private String nbStatus;
	private double montoFactura;
	private double montoPeriodo;
	
	private List<MesMatriculaBean4>mesMatriculas;
	public String getNroFactura() {
		return nroFactura;
	}
	public void setNroFactura(String nroFactura) {
		this.nroFactura = nroFactura;
	}
	public String getNroControl() {
		return nroControl;
	}
	public void setNroControl(String nroControl) {
		this.nroControl = nroControl;
	}
	public double getMontoFactura() {
		return montoFactura;
	}
	public void setMontoFactura(double montoFactura) {
		this.montoFactura = montoFactura;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public List<MesMatriculaBean4> getMesMatriculas() {
		return mesMatriculas;
	}
	public void setMesMatriculas(List<MesMatriculaBean4> mesMatriculas) {
		this.mesMatriculas = mesMatriculas;
	}
	public int getNroIdFactura() {
		return nroIdFactura;
	}
	public void setNroIdFactura(int nroIdFactura) {
		this.nroIdFactura = nroIdFactura;
	}
	public String getNbStatus() {
		return nbStatus;
	}
	public void setNbStatus(String nbStatus) {
		this.nbStatus = nbStatus;
	}
	public double getMontoPeriodo() {
		return montoPeriodo;
	}
	public void setMontoPeriodo(double montoPeriodo) {
		this.montoPeriodo = montoPeriodo;
	}
	 

}
