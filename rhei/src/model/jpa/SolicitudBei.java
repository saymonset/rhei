package model.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the SOLICITUD_BEI database table.
 * 
 */
@Entity
@Table(name="SOLICITUD_BEI")
@NamedQuery(name="SolicitudBei.findAll", query="SELECT s FROM SolicitudBei s")
public class SolicitudBei implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="NU_SOLICITUD")
	private long nuSolicitud;

	@Temporal(TemporalType.DATE)
	@Column(name="FE_SOLICITUD")
	private Date feSolicitud;

	@Column(name="IN_LOCALIDAD_CEI")
	private String inLocalidadCei;

	@Column(name="IN_PERIOD_PAGO")
	private String inPeriodPago;

	@Column(name="IN_PROV_CONVENIDO")
	private String inProvConvenido;

	@Column(name="IN_TP_EDUCACION")
	private String inTpEducacion;

	@Column(name="NU_RIF_PROVEEDOR")
	private String nuRifProveedor;

	@Column(name="TI_NOMINA")
	private String tiNomina;

	//bi-directional many-to-one association to MovStSolicBei
	@OneToMany(mappedBy="solicitudBei")
	private List<MovStSolicBei> movStSolicBeis;

	//bi-directional many-to-one association to RelacionPago
	@OneToMany(mappedBy="solicitudBei")
	private List<RelacionPago> relacionPagos;

	//bi-directional many-to-one association to BeneficiarioEi
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="CEDULA_FAMILIAR", referencedColumnName="CEDULA_FAMILIAR"),
		@JoinColumn(name="CO_EMPLEADO", referencedColumnName="CO_EMPLEADO")
		})
	private BeneficiarioEi beneficiarioEi;

	public SolicitudBei() {
	}

	public long getNuSolicitud() {
		return this.nuSolicitud;
	}

	public void setNuSolicitud(long nuSolicitud) {
		this.nuSolicitud = nuSolicitud;
	}

	public Date getFeSolicitud() {
		return this.feSolicitud;
	}

	public void setFeSolicitud(Date feSolicitud) {
		this.feSolicitud = feSolicitud;
	}

	public String getInLocalidadCei() {
		return this.inLocalidadCei;
	}

	public void setInLocalidadCei(String inLocalidadCei) {
		this.inLocalidadCei = inLocalidadCei;
	}

	public String getInPeriodPago() {
		return this.inPeriodPago;
	}

	public void setInPeriodPago(String inPeriodPago) {
		this.inPeriodPago = inPeriodPago;
	}

	public String getInProvConvenido() {
		return this.inProvConvenido;
	}

	public void setInProvConvenido(String inProvConvenido) {
		this.inProvConvenido = inProvConvenido;
	}

	public String getInTpEducacion() {
		return this.inTpEducacion;
	}

	public void setInTpEducacion(String inTpEducacion) {
		this.inTpEducacion = inTpEducacion;
	}

	public String getNuRifProveedor() {
		return this.nuRifProveedor;
	}

	public void setNuRifProveedor(String nuRifProveedor) {
		this.nuRifProveedor = nuRifProveedor;
	}

	public String getTiNomina() {
		return this.tiNomina;
	}

	public void setTiNomina(String tiNomina) {
		this.tiNomina = tiNomina;
	}

	public List<MovStSolicBei> getMovStSolicBeis() {
		return this.movStSolicBeis;
	}

	public void setMovStSolicBeis(List<MovStSolicBei> movStSolicBeis) {
		this.movStSolicBeis = movStSolicBeis;
	}

	public MovStSolicBei addMovStSolicBei(MovStSolicBei movStSolicBei) {
		getMovStSolicBeis().add(movStSolicBei);
		movStSolicBei.setSolicitudBei(this);

		return movStSolicBei;
	}

	public MovStSolicBei removeMovStSolicBei(MovStSolicBei movStSolicBei) {
		getMovStSolicBeis().remove(movStSolicBei);
		movStSolicBei.setSolicitudBei(null);

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
		relacionPago.setSolicitudBei(this);

		return relacionPago;
	}

	public RelacionPago removeRelacionPago(RelacionPago relacionPago) {
		getRelacionPagos().remove(relacionPago);
		relacionPago.setSolicitudBei(null);

		return relacionPago;
	}

	public BeneficiarioEi getBeneficiarioEi() {
		return this.beneficiarioEi;
	}

	public void setBeneficiarioEi(BeneficiarioEi beneficiarioEi) {
		this.beneficiarioEi = beneficiarioEi;
	}

}