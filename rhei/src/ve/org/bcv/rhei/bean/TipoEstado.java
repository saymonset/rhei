package ve.org.bcv.rhei.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com
 * 30/01/2015 12:40:28
 * 
 */
public class TipoEstado implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String valor;
	private String nombre;
	private List<TipoEstado> tipoEstados;
//	<option value="A">Activo</option>
//	<option value="I">Inactivo</option>
	/**
	 * LLenamos los Tipos de estados
	 */
	private void llenarData(){
		tipoEstados =new ArrayList<TipoEstado>();
		TipoEstado tipoEstado = new TipoEstado();
		tipoEstado.setValor("A");
		tipoEstado.setNombre("Activo");
		tipoEstados.add(tipoEstado);
		tipoEstado = new TipoEstado();
		tipoEstado.setValor("I");
		tipoEstado.setNombre("Inactivo");
		tipoEstados.add(tipoEstado);
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
	public List<TipoEstado> getTipoEstados() {
		if (tipoEstados==null || tipoEstados.size()==0 || tipoEstados.isEmpty()){
			llenarData();
		}
		return tipoEstados;
	}
	public void setTipoEstados(List<TipoEstado> tipoEstados) {
		this.tipoEstados = tipoEstados;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
