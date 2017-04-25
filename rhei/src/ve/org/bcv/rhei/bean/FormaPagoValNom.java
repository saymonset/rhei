package ve.org.bcv.rhei.bean;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

public class FormaPagoValNom implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(FormaPagoValNom.class
			.getName());
	
	private String valor;
	private String nombre;
	private List<FormaPagoValNom> objetos;
	public static Logger getLog() {
		return log;
	}
	public static void setLog(Logger log) {
		FormaPagoValNom.log = log;
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
	public List<FormaPagoValNom> getObjetos() {
		return objetos;
	}
	public void setObjetos(List<FormaPagoValNom> objetos) {
		this.objetos = objetos;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	 
}
