package model.jpa;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the RELACION_PAGOS database table.
 * 
 */
@Embeddable
public class RelacionPagoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="NU_SOLICITUD", insertable=false, updatable=false)
	private long nuSolicitud;

	@Column(name="NU_ID_FACTURA", insertable=false, updatable=false)
	private long nuIdFactura;

	@Column(name="IN_CONCEPTO_PAGO")
	private long inConceptoPago;

	@Column(name="IN_RECEPTOR_PAGO")
	private long inReceptorPago;

	@Column(name="NU_REF_PAGO")
	private long nuRefPago;

	public RelacionPagoPK() {
	}
	public long getNuSolicitud() {
		return this.nuSolicitud;
	}
	public void setNuSolicitud(long nuSolicitud) {
		this.nuSolicitud = nuSolicitud;
	}
	public long getNuIdFactura() {
		return this.nuIdFactura;
	}
	public void setNuIdFactura(long nuIdFactura) {
		this.nuIdFactura = nuIdFactura;
	}
	public long getInConceptoPago() {
		return this.inConceptoPago;
	}
	public void setInConceptoPago(long inConceptoPago) {
		this.inConceptoPago = inConceptoPago;
	}
	public long getInReceptorPago() {
		return this.inReceptorPago;
	}
	public void setInReceptorPago(long inReceptorPago) {
		this.inReceptorPago = inReceptorPago;
	}
	public long getNuRefPago() {
		return this.nuRefPago;
	}
	public void setNuRefPago(long nuRefPago) {
		this.nuRefPago = nuRefPago;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RelacionPagoPK)) {
			return false;
		}
		RelacionPagoPK castOther = (RelacionPagoPK)other;
		return 
			(this.nuSolicitud == castOther.nuSolicitud)
			&& (this.nuIdFactura == castOther.nuIdFactura)
			&& (this.inConceptoPago == castOther.inConceptoPago)
			&& (this.inReceptorPago == castOther.inReceptorPago)
			&& (this.nuRefPago == castOther.nuRefPago);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.nuSolicitud ^ (this.nuSolicitud >>> 32)));
		hash = hash * prime + ((int) (this.nuIdFactura ^ (this.nuIdFactura >>> 32)));
		hash = hash * prime + ((int) (this.inConceptoPago ^ (this.inConceptoPago >>> 32)));
		hash = hash * prime + ((int) (this.inReceptorPago ^ (this.inReceptorPago >>> 32)));
		hash = hash * prime + ((int) (this.nuRefPago ^ (this.nuRefPago >>> 32)));
		
		return hash;
	}
}