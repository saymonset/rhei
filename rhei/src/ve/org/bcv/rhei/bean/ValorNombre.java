package ve.org.bcv.rhei.bean;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

public class ValorNombre implements Serializable {
	private static Logger log = Logger.getLogger(ValorNombre.class
			.getName());

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String valor;
	private String nombre;
	
	private List<ValorNombre> valorNombres;
	
	public ValorNombre(String valor, String nombre) {
		super();
		this.valor = valor;
		this.nombre = nombre;
	}
	public static Logger getLog() {
		return log;
	}
	public static void setLog(Logger log) {
		ValorNombre.log = log;
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
	public List<ValorNombre> getValorNombres() {
		return valorNombres;
	}
	public void setValorNombres(List<ValorNombre> valorNombres) {
		this.valorNombres = valorNombres;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
