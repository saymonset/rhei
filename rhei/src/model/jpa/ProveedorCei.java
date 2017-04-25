package model.jpa;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the PROVEEDOR_CEI database table.
 * 
 */
@Entity
@Table(name="PROVEEDOR_CEI")
@NamedQuery(name="ProveedorCei.findAll", query="SELECT p FROM ProveedorCei p")
public class ProveedorCei implements Serializable {
	private static final long serialVersionUID = 1L;
    @Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="DI_PROVEEDOR")
	private String diProveedor;

	@Column(name="IN_ACTIVO")
	private BigDecimal inActivo;

	@Column(name="IN_LOCALIDAD_BCV")
	private String inLocalidadBcv;

	@Column(name="NB_PERS_CONTACTO")
	private String nbPersContacto;

	@Column(name="NB_PROVEEDOR")
	private String nbProveedor;

	@Column(name="NU_FAX")
	private String nuFax;

	@Column(name="NU_RIF_PROVEEDOR")
	private String nuRifProveedor;

	@Column(name="NU_TELEFONO1")
	private String nuTelefono1;

	@Column(name="NU_TELEFONO2")
	private String nuTelefono2;

	@Column(name="TX_E_MAIL")
	private String txEMail;

	public ProveedorCei() {
	}

	public String getDiProveedor() {
		return this.diProveedor;
	}

	public void setDiProveedor(String diProveedor) {
		this.diProveedor = diProveedor;
	}

	public BigDecimal getInActivo() {
		return this.inActivo;
	}

	public void setInActivo(BigDecimal inActivo) {
		this.inActivo = inActivo;
	}

	public String getInLocalidadBcv() {
		return this.inLocalidadBcv;
	}

	public void setInLocalidadBcv(String inLocalidadBcv) {
		this.inLocalidadBcv = inLocalidadBcv;
	}

	public String getNbPersContacto() {
		return this.nbPersContacto;
	}

	public void setNbPersContacto(String nbPersContacto) {
		this.nbPersContacto = nbPersContacto;
	}

	public String getNbProveedor() {
		return this.nbProveedor;
	}

	public void setNbProveedor(String nbProveedor) {
		this.nbProveedor = nbProveedor;
	}

	public String getNuFax() {
		return this.nuFax;
	}

	public void setNuFax(String nuFax) {
		this.nuFax = nuFax;
	}

	public String getNuRifProveedor() {
		return this.nuRifProveedor;
	}

	public void setNuRifProveedor(String nuRifProveedor) {
		this.nuRifProveedor = nuRifProveedor;
	}

	public String getNuTelefono1() {
		return this.nuTelefono1;
	}

	public void setNuTelefono1(String nuTelefono1) {
		this.nuTelefono1 = nuTelefono1;
	}

	public String getNuTelefono2() {
		return this.nuTelefono2;
	}

	public void setNuTelefono2(String nuTelefono2) {
		this.nuTelefono2 = nuTelefono2;
	}

	public String getTxEMail() {
		return this.txEMail;
	}

	public void setTxEMail(String txEMail) {
		this.txEMail = txEMail;
	}

}