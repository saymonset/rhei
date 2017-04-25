package ve.org.bcv.rhei.bean;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

public class BeneficiarioValNom implements Serializable {
	private static Logger log = Logger.getLogger(BeneficiarioValNom.class
			.getName());

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String valor;
	private String nombre;
	private List<BeneficiarioValNom> beneficiarios;
	public static Logger getLog() {
		return log;
	}
	public static void setLog(Logger log) {
		BeneficiarioValNom.log = log;
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
	public List<BeneficiarioValNom> getBeneficiarios() {
		return beneficiarios;
	}
	public void setBeneficiarios(List<BeneficiarioValNom> beneficiarios) {
		this.beneficiarios = beneficiarios;
	}
	 

}
