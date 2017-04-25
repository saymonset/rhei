package ve.org.bcv.rhei.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com
 * 03/02/2015 15:26:42
 * Datos que van en el select html del reporte
 */
public class PeriodosRpte implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String valor;
	private String nombre;
	private List<PeriodosRpte> periodosRpte;

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
	 
	public List<PeriodosRpte> getPeriodosRpte() {
		return periodosRpte;
	}
	public void setPeriodosRpte(List<PeriodosRpte> periodosRpte) {
		this.periodosRpte = periodosRpte;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
