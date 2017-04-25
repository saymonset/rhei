package model.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the TIPO_PAGO database table.
 * 
 */
@Entity
@Table(name="TIPO_PAGO")
@NamedQuery(name="TipoPago.findAll", query="SELECT t FROM TipoPago t")
public class TipoPago implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="CO_TI_PAGO")
	private long coTiPago;

	@Column(name="NB_TIPO_PAGO")
	private String nbTipoPago;

	//bi-directional many-to-one association to RelacionPago
	@OneToMany(mappedBy="tipoPago")
	private List<RelacionPago> relacionPagos;

	public TipoPago() {
	}

	public long getCoTiPago() {
		return this.coTiPago;
	}

	public void setCoTiPago(long coTiPago) {
		this.coTiPago = coTiPago;
	}

	public String getNbTipoPago() {
		return this.nbTipoPago;
	}

	public void setNbTipoPago(String nbTipoPago) {
		this.nbTipoPago = nbTipoPago;
	}

	public List<RelacionPago> getRelacionPagos() {
		return this.relacionPagos;
	}

	public void setRelacionPagos(List<RelacionPago> relacionPagos) {
		this.relacionPagos = relacionPagos;
	}

	public RelacionPago addRelacionPago(RelacionPago relacionPago) {
		getRelacionPagos().add(relacionPago);
		relacionPago.setTipoPago(this);

		return relacionPago;
	}

	public RelacionPago removeRelacionPago(RelacionPago relacionPago) {
		getRelacionPagos().remove(relacionPago);
		relacionPago.setTipoPago(null);

		return relacionPago;
	}

}