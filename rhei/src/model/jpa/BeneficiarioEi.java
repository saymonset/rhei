package model.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the BENEFICIARIO_EI database table.
 * 
 */
@Entity
@Table(name="BENEFICIARIO_EI")
@NamedQuery(name="BeneficiarioEi.findAll", query="SELECT b FROM BeneficiarioEi b")
public class BeneficiarioEi implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private BeneficiarioEiPK id;

	//bi-directional many-to-one association to SolicitudBei
	@OneToMany(mappedBy="beneficiarioEi")
	private List<SolicitudBei> solicitudBeis;

	public BeneficiarioEi() {
	}

	public BeneficiarioEiPK getId() {
		return this.id;
	}

	public void setId(BeneficiarioEiPK id) {
		this.id = id;
	}

	public List<SolicitudBei> getSolicitudBeis() {
		return this.solicitudBeis;
	}

	public void setSolicitudBeis(List<SolicitudBei> solicitudBeis) {
		this.solicitudBeis = solicitudBeis;
	}

	public SolicitudBei addSolicitudBei(SolicitudBei solicitudBei) {
		getSolicitudBeis().add(solicitudBei);
		solicitudBei.setBeneficiarioEi(this);

		return solicitudBei;
	}

	public SolicitudBei removeSolicitudBei(SolicitudBei solicitudBei) {
		getSolicitudBeis().remove(solicitudBei);
		solicitudBei.setBeneficiarioEi(null);

		return solicitudBei;
	}

}