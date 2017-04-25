package model.jpa;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the BENEFICIARIO_EI database table.
 * 
 */
@Embeddable
public class BeneficiarioEiPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="CO_EMPLEADO")
	private long coEmpleado;

	@Column(name="CEDULA_FAMILIAR")
	private long cedulaFamiliar;

	public BeneficiarioEiPK() {
	}
	public long getCoEmpleado() {
		return this.coEmpleado;
	}
	public void setCoEmpleado(long coEmpleado) {
		this.coEmpleado = coEmpleado;
	}
	public long getCedulaFamiliar() {
		return this.cedulaFamiliar;
	}
	public void setCedulaFamiliar(long cedulaFamiliar) {
		this.cedulaFamiliar = cedulaFamiliar;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof BeneficiarioEiPK)) {
			return false;
		}
		BeneficiarioEiPK castOther = (BeneficiarioEiPK)other;
		return 
			(this.coEmpleado == castOther.coEmpleado)
			&& (this.cedulaFamiliar == castOther.cedulaFamiliar);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.coEmpleado ^ (this.coEmpleado >>> 32)));
		hash = hash * prime + ((int) (this.cedulaFamiliar ^ (this.cedulaFamiliar >>> 32)));
		
		return hash;
	}
}