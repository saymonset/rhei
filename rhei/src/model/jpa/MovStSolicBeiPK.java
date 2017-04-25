package model.jpa;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the MOV_ST_SOLIC_BEI database table.
 * 
 */
@Embeddable
public class MovStSolicBeiPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="NU_SOLICITUD", insertable=false, updatable=false)
	private long nuSolicitud;

	@Column(name="CO_STATUS", insertable=false, updatable=false)
	private String coStatus;

	@Temporal(TemporalType.DATE)
	@Column(name="FE_STATUS")
	private java.util.Date feStatus;

	public MovStSolicBeiPK() {
	}
	public long getNuSolicitud() {
		return this.nuSolicitud;
	}
	public void setNuSolicitud(long nuSolicitud) {
		this.nuSolicitud = nuSolicitud;
	}
	public String getCoStatus() {
		return this.coStatus;
	}
	public void setCoStatus(String coStatus) {
		this.coStatus = coStatus;
	}
	public java.util.Date getFeStatus() {
		return this.feStatus;
	}
	public void setFeStatus(java.util.Date feStatus) {
		this.feStatus = feStatus;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MovStSolicBeiPK)) {
			return false;
		}
		MovStSolicBeiPK castOther = (MovStSolicBeiPK)other;
		return 
			(this.nuSolicitud == castOther.nuSolicitud)
			&& this.coStatus.equals(castOther.coStatus)
			&& this.feStatus.equals(castOther.feStatus);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.nuSolicitud ^ (this.nuSolicitud >>> 32)));
		hash = hash * prime + this.coStatus.hashCode();
		hash = hash * prime + this.feStatus.hashCode();
		
		return hash;
	}
}