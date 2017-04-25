package model.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ST_SOLICITUD_BEI database table.
 * 
 */
@Entity
@Table(name="ST_SOLICITUD_BEI")
@NamedQuery(name="StSolicitudBei.findAll", query="SELECT s FROM StSolicitudBei s")
public class StSolicitudBei implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="CO_STATUS")
	private String coStatus;

	@Column(name="IN_ACTIVO")
	private String inActivo;

	@Column(name="NB_STATUS")
	private String nbStatus;

	//bi-directional many-to-one association to MovStSolicBei
	@OneToMany(mappedBy="stSolicitudBei")
	private List<MovStSolicBei> movStSolicBeis;

	public StSolicitudBei() {
	}

	public String getCoStatus() {
		return this.coStatus;
	}

	public void setCoStatus(String coStatus) {
		this.coStatus = coStatus;
	}

	public String getInActivo() {
		return this.inActivo;
	}

	public void setInActivo(String inActivo) {
		this.inActivo = inActivo;
	}

	public String getNbStatus() {
		return this.nbStatus;
	}

	public void setNbStatus(String nbStatus) {
		this.nbStatus = nbStatus;
	}

	public List<MovStSolicBei> getMovStSolicBeis() {
		return this.movStSolicBeis;
	}

	public void setMovStSolicBeis(List<MovStSolicBei> movStSolicBeis) {
		this.movStSolicBeis = movStSolicBeis;
	}

	public MovStSolicBei addMovStSolicBei(MovStSolicBei movStSolicBei) {
		getMovStSolicBeis().add(movStSolicBei);
		movStSolicBei.setStSolicitudBei(this);

		return movStSolicBei;
	}

	public MovStSolicBei removeMovStSolicBei(MovStSolicBei movStSolicBei) {
		getMovStSolicBeis().remove(movStSolicBei);
		movStSolicBei.setStSolicitudBei(null);

		return movStSolicBei;
	}

}