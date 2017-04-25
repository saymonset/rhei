package model.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the PERIODO_ESCOLAR database table.
 * 
 */
@Entity
@Table(name="PERIODO_ESCOLAR")
@NamedQuery(name="PeriodoEscolar.findAll", query="SELECT p FROM PeriodoEscolar p")
public class PeriodoEscolar implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="CO_PERIODO")
	private long coPeriodo;

	@Temporal(TemporalType.DATE)
	@Column(name="FE_FIN")
	private Date feFin;

	@Temporal(TemporalType.DATE)
	@Column(name="FE_INICIO")
	private Date feInicio;

	@Column(name="IN_PERIODO_ESCOLAR")
	private String inPeriodoEscolar;

	@Column(name="TX_DESCRIP_PERIODO")
	private String txDescripPeriodo;

	//bi-directional many-to-one association to MovStSolicBei
	@OneToMany(mappedBy="periodoEscolar")
	private List<MovStSolicBei> movStSolicBeis;

	//bi-directional many-to-one association to RelacionPago
	@OneToMany(mappedBy="periodoEscolar")
	private List<RelacionPago> relacionPagos;

	public PeriodoEscolar() {
	}

	public long getCoPeriodo() {
		return this.coPeriodo;
	}

	public void setCoPeriodo(long coPeriodo) {
		this.coPeriodo = coPeriodo;
	}

	public Date getFeFin() {
		return this.feFin;
	}

	public void setFeFin(Date feFin) {
		this.feFin = feFin;
	}

	public Date getFeInicio() {
		return this.feInicio;
	}

	public void setFeInicio(Date feInicio) {
		this.feInicio = feInicio;
	}

	public String getInPeriodoEscolar() {
		return this.inPeriodoEscolar;
	}

	public void setInPeriodoEscolar(String inPeriodoEscolar) {
		this.inPeriodoEscolar = inPeriodoEscolar;
	}

	public String getTxDescripPeriodo() {
		return this.txDescripPeriodo;
	}

	public void setTxDescripPeriodo(String txDescripPeriodo) {
		this.txDescripPeriodo = txDescripPeriodo;
	}

	public List<MovStSolicBei> getMovStSolicBeis() {
		return this.movStSolicBeis;
	}

	public void setMovStSolicBeis(List<MovStSolicBei> movStSolicBeis) {
		this.movStSolicBeis = movStSolicBeis;
	}

	public MovStSolicBei addMovStSolicBei(MovStSolicBei movStSolicBei) {
		getMovStSolicBeis().add(movStSolicBei);
		movStSolicBei.setPeriodoEscolar(this);

		return movStSolicBei;
	}

	public MovStSolicBei removeMovStSolicBei(MovStSolicBei movStSolicBei) {
		getMovStSolicBeis().remove(movStSolicBei);
		movStSolicBei.setPeriodoEscolar(null);

		return movStSolicBei;
	}

	public List<RelacionPago> getRelacionPagos() {
		return this.relacionPagos;
	}

	public void setRelacionPagos(List<RelacionPago> relacionPagos) {
		this.relacionPagos = relacionPagos;
	}

	public RelacionPago addRelacionPago(RelacionPago relacionPago) {
		getRelacionPagos().add(relacionPago);
		relacionPago.setPeriodoEscolar(this);

		return relacionPago;
	}

	public RelacionPago removeRelacionPago(RelacionPago relacionPago) {
		getRelacionPagos().remove(relacionPago);
		relacionPago.setPeriodoEscolar(null);

		return relacionPago;
	}

}