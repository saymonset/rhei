package ve.org.bcv.rhei.bean;

import java.io.Serializable;

/**
 * Permite llevar los documentos recaudos de cada beneficiario
 * @author sirodrig
 *
 */
public class Recaudo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nombre;
	private String descripcion;
	/**Extension del archivo*/
	private String ext;
	private boolean alfresco;
	private boolean obligatorio;
	private String diHomeAlfresco;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
 
	 
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public boolean isAlfresco() {
		return alfresco;
	}
	public void setAlfresco(boolean alfresco) {
		this.alfresco = alfresco;
	}
	public boolean isObligatorio() {
		return obligatorio;
	}
	public void setObligatorio(boolean obligatorio) {
		this.obligatorio = obligatorio;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public String getDiHomeAlfresco() {
		return diHomeAlfresco;
	}
	public void setDiHomeAlfresco(String diHomeAlfresco) {
		this.diHomeAlfresco = diHomeAlfresco;
	}

}
