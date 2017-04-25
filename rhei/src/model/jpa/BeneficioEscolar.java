package model.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the BENEFICIO_ESCOLAR database table.
 * 
 */
@Entity
@Table(name="BENEFICIO_ESCOLAR")
@NamedQuery(name="BeneficioEscolar.findAll", query="SELECT b FROM BeneficioEscolar b")
public class BeneficioEscolar implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="CO_TIPO_BENEFICIO")
	private String coTipoBeneficio;

	@Temporal(TemporalType.DATE)
	@Column(name="FE_REGISTRO")
	private Date feRegistro;

	@Column(name="IN_STATUS")
	private String inStatus;

	@Column(name="TX_BENEFICIO")
	private String txBeneficio;

	//bi-directional many-to-one association to Parametro
	@OneToMany(mappedBy="beneficioEscolar")
	private List<Parametro> parametros;

	public BeneficioEscolar() {
	}

	public String getCoTipoBeneficio() {
		return this.coTipoBeneficio;
	}

	public void setCoTipoBeneficio(String coTipoBeneficio) {
		this.coTipoBeneficio = coTipoBeneficio;
	}

	public Date getFeRegistro() {
		return this.feRegistro;
	}

	public void setFeRegistro(Date feRegistro) {
		this.feRegistro = feRegistro;
	}

	public String getInStatus() {
		return this.inStatus;
	}

	public void setInStatus(String inStatus) {
		this.inStatus = inStatus;
	}

	public String getTxBeneficio() {
		return this.txBeneficio;
	}

	public void setTxBeneficio(String txBeneficio) {
		this.txBeneficio = txBeneficio;
	}

	public List<Parametro> getParametros() {
		return this.parametros;
	}

	public void setParametros(List<Parametro> parametros) {
		this.parametros = parametros;
	}

	public Parametro addParametro(Parametro parametro) {
		getParametros().add(parametro);
		parametro.setBeneficioEscolar(this);

		return parametro;
	}

	public Parametro removeParametro(Parametro parametro) {
		getParametros().remove(parametro);
		parametro.setBeneficioEscolar(null);

		return parametro;
	}

}