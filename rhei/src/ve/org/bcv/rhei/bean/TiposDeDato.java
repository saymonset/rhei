package ve.org.bcv.rhei.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

 
/**
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com
 * 26/01/2015 12:56:17
 * Crearemos los tipos de datos a seleccionar
 */
public class TiposDeDato implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String valor;
	private String nombre;
	private List<TiposDeDato> tiposDeDatos;

public static long getSerialversionuid() {
		return serialVersionUID;
	}
public TiposDeDato() {
		super();
		
		
	}
	/**
	 * LLenamos los Tipos de datos
	 */
	private void llenarData(){
		tiposDeDatos= new ArrayList<TiposDeDato>();
		
		TiposDeDato td=new TiposDeDato();
		td.setValor("N");
		td.setNombre("Numérico");
		tiposDeDatos.add(td);
		td=new TiposDeDato();
		td.setValor("C");
		td.setNombre("Alfanumérico");
		tiposDeDatos.add(td);
		td=new TiposDeDato();
		td.setValor("F");
		td.setNombre("Date");
		tiposDeDatos.add(td);
		
	}
	 
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public List<TiposDeDato> getTiposDeDatos() {
		if (tiposDeDatos==null || tiposDeDatos.isEmpty() || tiposDeDatos.size()==0){
			llenarData();
		}
		return tiposDeDatos;
	}
	public void setTiposDeDatos(List<TiposDeDato> tiposDeDatos) {
		this.tiposDeDatos = tiposDeDatos;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	@Override
	public String toString() {
		return "TiposDeDato [valor=" + valor + ", nombre=" + nombre + "]";
	}
	 
 

}
