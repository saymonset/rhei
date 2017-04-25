package model.jpa;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the SIGLA_EXPED_BEI database table.
 * 
 */
@Embeddable
public class SiglaExpedBeiPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="CO_EMPLEADO")
	private long coEmpleado;

	@Column(name="TI_NOMINA")
	private String tiNomina;

	public SiglaExpedBeiPK() {
	}
	public long getCoEmpleado() {
		return this.coEmpleado;
	}
	public void setCoEmpleado(long coEmpleado) {
		this.coEmpleado = coEmpleado;
	}
	public String getTiNomina() {
		return this.tiNomina;
	}
	public void setTiNomina(String tiNomina) {
		this.tiNomina = tiNomina;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SiglaExpedBeiPK)) {
			return false;
		}
		SiglaExpedBeiPK castOther = (SiglaExpedBeiPK)other;
		return 
			(this.coEmpleado == castOther.coEmpleado)
			&& this.tiNomina.equals(castOther.tiNomina);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.coEmpleado ^ (this.coEmpleado >>> 32)));
		hash = hash * prime + this.tiNomina.hashCode();
		
		return hash;
	}
}