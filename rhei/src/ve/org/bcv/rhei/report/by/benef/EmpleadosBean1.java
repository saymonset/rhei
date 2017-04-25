package ve.org.bcv.rhei.report.by.benef;

import java.io.Serializable;
import java.util.List;

/**
 * LLenamos los datos principales del reporte
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com
 * 07/05/2015 13:31:36
 * 
 */
public class EmpleadosBean1 implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int cedulaTrab;
	private String formatoPeriodo;
	private String nombrTrab;
	private String apellidoTrab;
	private String compania;
	private String descripcion;
	private String tlfNumExt;
 
	private List<BeneficiarioBean2> beneficiarios;
	public int getCedulaTrab() {
		return cedulaTrab;
	}
	public void setCedulaTrab(int cedulaTrab) {
		this.cedulaTrab = cedulaTrab;
	}
	public String getFormatoPeriodo() {
		return formatoPeriodo;
	}
	public void setFormatoPeriodo(String formatoPeriodo) {
		this.formatoPeriodo = formatoPeriodo;
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
	public String getCompania() {
		return compania;
	}
	public void setCompania(String compania) {
		this.compania = compania;
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
	public List<BeneficiarioBean2> getBeneficiarios() {
		return beneficiarios;
	}
	public void setBeneficiarios(List<BeneficiarioBean2> beneficiarios) {
		this.beneficiarios = beneficiarios;
	}
	public String getTlfNumExt() {
		return tlfNumExt;
	}
	public void setTlfNumExt(String tlfNumExt) {
		this.tlfNumExt = tlfNumExt;
	}
}
