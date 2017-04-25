package model.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the CONVENIO_BCV database table.
 * 
 */
@Entity
@Table(name="CONVENIO_BCV")
@NamedQuery(name="ConvenioBcv.findAll", query="SELECT c FROM ConvenioBcv c")
public class ConvenioBcv implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="NU_ID_CONVENIO")
	private long nuIdConvenio;

	@Temporal(TemporalType.DATE)
	@Column(name="FE_DESDE_CONVENIO")
	private Date feDesdeConvenio;

	@Temporal(TemporalType.DATE)
	@Column(name="FE_HASTA_CONVENIO")
	private Date feHastaConvenio;

	@Column(name="NB_CONVENIO")
	private String nbConvenio;

	@Column(name="NU_CONVENIO")
	private String nuConvenio;

	@Column(name="NU_RIF_PROVEEDOR")
	private String nuRifProveedor;

	public ConvenioBcv() {
	}

	public long getNuIdConvenio() {
		return this.nuIdConvenio;
	}

	public void setNuIdConvenio(long nuIdConvenio) {
		this.nuIdConvenio = nuIdConvenio;
	}

	public Date getFeDesdeConvenio() {
		return this.feDesdeConvenio;
	}

	public void setFeDesdeConvenio(Date feDesdeConvenio) {
		this.feDesdeConvenio = feDesdeConvenio;
	}

	public Date getFeHastaConvenio() {
		return this.feHastaConvenio;
	}

	public void setFeHastaConvenio(Date feHastaConvenio) {
		this.feHastaConvenio = feHastaConvenio;
	}

	public String getNbConvenio() {
		return this.nbConvenio;
	}

	public void setNbConvenio(String nbConvenio) {
		this.nbConvenio = nbConvenio;
	}

	public String getNuConvenio() {
		return this.nuConvenio;
	}

	public void setNuConvenio(String nuConvenio) {
		this.nuConvenio = nuConvenio;
	}

	public String getNuRifProveedor() {
		return this.nuRifProveedor;
	}

	public void setNuRifProveedor(String nuRifProveedor) {
		this.nuRifProveedor = nuRifProveedor;
	}

}