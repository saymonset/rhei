package model.jpa;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the SIGLA_EXPED_BEI database table.
 * 
 */
@Entity
@Table(name="SIGLA_EXPED_BEI")
@NamedQuery(name="SiglaExpedBei.findAll", query="SELECT s FROM SiglaExpedBei s")
public class SiglaExpedBei implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private SiglaExpedBeiPK id;

	@Column(name="CO_SIGLA_EXPED")
	private String coSiglaExped;

	public SiglaExpedBei() {
	}

	public SiglaExpedBeiPK getId() {
		return this.id;
	}

	public void setId(SiglaExpedBeiPK id) {
		this.id = id;
	}

	public String getCoSiglaExped() {
		return this.coSiglaExped;
	}

	public void setCoSiglaExped(String coSiglaExped) {
		this.coSiglaExped = coSiglaExped;
	}

}