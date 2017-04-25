package model.jpa;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the PARAMETRO database table.
 * 
 */
@Entity
@NamedQuery(name="Parametro.findAll", query="SELECT p FROM Parametro p")
public class Parametro implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ParametroPK id;

	@Column(name="IN_ST_PROCESAMIENT")
	private String inStProcesamient;

	@Column(name="IN_TIPO_PARAMETRO")
	private String inTipoParametro;

	@Column(name="TX_OBSERVACIONES")
	private String txObservaciones;

	@Column(name="TX_VALOR_PARAMETRO")
	private String txValorParametro;

	//bi-directional many-to-one association to BeneficioEscolar
	@ManyToOne(optional=false)
	@JoinColumn(name="CO_TIPO_BENEFICIO",insertable=false, updatable=false)
	private BeneficioEscolar beneficioEscolar;
	public Parametro() {
	}

	public ParametroPK getId() {
		return this.id;
	}

	public void setId(ParametroPK id) {
		this.id = id;
	}

	public String getInStProcesamient() {
		return this.inStProcesamient;
	}

	public void setInStProcesamient(String inStProcesamient) {
		this.inStProcesamient = inStProcesamient;
	}

	public String getInTipoParametro() {
		return this.inTipoParametro;
	}

	public void setInTipoParametro(String inTipoParametro) {
		this.inTipoParametro = inTipoParametro;
	}

	public String getTxObservaciones() {
		return this.txObservaciones;
	}

	public void setTxObservaciones(String txObservaciones) {
		this.txObservaciones = txObservaciones;
	}

	public String getTxValorParametro() {
		return this.txValorParametro;
	}

	public void setTxValorParametro(String txValorParametro) {
		this.txValorParametro = txValorParametro;
	}

	public BeneficioEscolar getBeneficioEscolar() {
		return this.beneficioEscolar;
	}

	public void setBeneficioEscolar(BeneficioEscolar beneficioEscolar) {
		this.beneficioEscolar = beneficioEscolar;
	}

}