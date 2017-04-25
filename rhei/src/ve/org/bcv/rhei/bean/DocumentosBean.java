package ve.org.bcv.rhei.bean;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;



public class DocumentosBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private File _upFileFile;
	private InputStream inputStream; 
	private String docNomRecaudoTipo;
	private String docFileWithExtension;
	private String txNombreTipo;
	private String version;
	private String notes;
	private String author;
	private String fecha;
	private Date fechaVencimiento;
	private boolean swModificarfechaVencimiento;
	private String fechaVencimientoShowView;
	private boolean swFechaVencimiento;
	private String homeUsuarioAlfresco;
	private String stTipoRecaudo;
	private boolean swObligatorio;
 
	
	private boolean  status;
 
	
 
	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getTxNombreTipo() {
		return txNombreTipo;
	}

	public void setTxNombreTipo(String txNombreTipo) {
		this.txNombreTipo = txNombreTipo;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

 

	public String getDocNomRecaudoTipo() {
		return docNomRecaudoTipo;
	}

	public void setDocNomRecaudoTipo(String docNomRecaudoTipo) {
		this.docNomRecaudoTipo = docNomRecaudoTipo;
	}

	public String getHomeUsuarioAlfresco() {
		return homeUsuarioAlfresco;
	}

	public void setHomeUsuarioAlfresco(String homeUsuarioAlfresco) {
		this.homeUsuarioAlfresco = homeUsuarioAlfresco;
	}
 

	public boolean isSwFechaVencimiento() {
		return swFechaVencimiento;
	}

	public void setSwFechaVencimiento(boolean swFechaVencimiento) {
		this.swFechaVencimiento = swFechaVencimiento;
	}

	public Date getFechaVencimiento() {
		
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public String getDocFileWithExtension() {
		return docFileWithExtension;
	}

	public void setDocFileWithExtension(String docFileWithExtension) {
		this.docFileWithExtension = docFileWithExtension;
	}

	public String getFechaVencimientoShowView() {
	
		return fechaVencimientoShowView;
	}

	public void setFechaVencimientoShowView(String fechaVencimientoShowView) {
		this.fechaVencimientoShowView = fechaVencimientoShowView;
	}

	public File get_upFileFile() {
		return _upFileFile;
	}

	public void set_upFileFile(File _upFileFile) {
		this._upFileFile = _upFileFile;
	}

	public String getStTipoRecaudo() {
		return stTipoRecaudo;
	}

	public void setStTipoRecaudo(String stTipoRecaudo) {
		this.stTipoRecaudo = stTipoRecaudo;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean isSwModificarfechaVencimiento() {
		return swModificarfechaVencimiento;
	}

	public void setSwModificarfechaVencimiento(boolean swModificarfechaVencimiento) {
		this.swModificarfechaVencimiento = swModificarfechaVencimiento;
	}

	public boolean isSwObligatorio() {
		return swObligatorio;
	}

	public void setSwObligatorio(boolean swObligatorio) {
		this.swObligatorio = swObligatorio;
	}

	 
 
 
 

	 

}
