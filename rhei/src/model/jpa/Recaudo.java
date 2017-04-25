package model.jpa;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the RECAUDO database table.
 * 
 */
@Entity
@NamedQuery(name="Recaudo.findAll", query="SELECT r FROM Recaudo r")
public class Recaudo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="CO_RECAUDO")
	private long coRecaudo;

	@Column(name="DI_HOME_ALFRESCO")
	private String diHomeAlfresco;

	@Column(name="IN_ALFRESCO")
	private String inAlfresco;

	@Column(name="IN_OBLIGATORIO")
	private String inObligatorio;

	@Column(name="NB_DOCUMENTO")
	private String nbDocumento;

	@Column(name="TX_DOCUMENTO")
	private String txDocumento;

	public Recaudo() {
	}

	public long getCoRecaudo() {
		return this.coRecaudo;
	}

	public void setCoRecaudo(long coRecaudo) {
		this.coRecaudo = coRecaudo;
	}

	public String getDiHomeAlfresco() {
		return this.diHomeAlfresco;
	}

	public void setDiHomeAlfresco(String diHomeAlfresco) {
		this.diHomeAlfresco = diHomeAlfresco;
	}

	public String getInAlfresco() {
		return this.inAlfresco;
	}

	public void setInAlfresco(String inAlfresco) {
		this.inAlfresco = inAlfresco;
	}

	public String getInObligatorio() {
		return this.inObligatorio;
	}

	public void setInObligatorio(String inObligatorio) {
		this.inObligatorio = inObligatorio;
	}

	public String getNbDocumento() {
		return this.nbDocumento;
	}

	public void setNbDocumento(String nbDocumento) {
		this.nbDocumento = nbDocumento;
	}

	public String getTxDocumento() {
		return this.txDocumento;
	}

	public void setTxDocumento(String txDocumento) {
		this.txDocumento = txDocumento;
	}

}