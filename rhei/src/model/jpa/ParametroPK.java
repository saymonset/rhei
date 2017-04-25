package model.jpa;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the PARAMETRO database table.
 * 
 */
@Embeddable
public class ParametroPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="CO_COMPANIA")
	private String coCompania;

	@Column(name="TIPO_EMP")
	private String tipoEmp;

	@Column(name="CO_TIPO_BENEFICIO", insertable=false, updatable=false)
	private String coTipoBeneficio;

	@Column(name="CO_PARAMETRO")
	private String coParametro;

	@Temporal(TemporalType.DATE)
	@Column(name="FE_VIGENCIA")
	private java.util.Date feVigencia;

	public ParametroPK() {
	}
	public String getCoCompania() {
		return this.coCompania;
	}
	public void setCoCompania(String coCompania) {
		this.coCompania = coCompania;
	}
	public String getTipoEmp() {
		return this.tipoEmp;
	}
	public void setTipoEmp(String tipoEmp) {
		this.tipoEmp = tipoEmp;
	}
	public String getCoTipoBeneficio() {
		return this.coTipoBeneficio;
	}
	public void setCoTipoBeneficio(String coTipoBeneficio) {
		this.coTipoBeneficio = coTipoBeneficio;
	}
	public String getCoParametro() {
		return this.coParametro;
	}
	public void setCoParametro(String coParametro) {
		this.coParametro = coParametro;
	}
	public java.util.Date getFeVigencia() {
		return this.feVigencia;
	}
	public void setFeVigencia(java.util.Date feVigencia) {
		this.feVigencia = feVigencia;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ParametroPK)) {
			return false;
		}
		ParametroPK castOther = (ParametroPK)other;
		return 
			this.coCompania.equals(castOther.coCompania)
			&& this.tipoEmp.equals(castOther.tipoEmp)
			&& this.coTipoBeneficio.equals(castOther.coTipoBeneficio)
			&& this.coParametro.equals(castOther.coParametro)
			&& this.feVigencia.equals(castOther.feVigencia);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.coCompania.hashCode();
		hash = hash * prime + this.tipoEmp.hashCode();
		hash = hash * prime + this.coTipoBeneficio.hashCode();
		hash = hash * prime + this.coParametro.hashCode();
		hash = hash * prime + this.feVigencia.hashCode();
		
		return hash;
	}
}