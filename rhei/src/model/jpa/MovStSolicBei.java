package model.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the MOV_ST_SOLIC_BEI database table.
 * 
 */
@Entity
@Table(name="MOV_ST_SOLIC_BEI")
@NamedQuery(name="MovStSolicBei.findAll", query="SELECT m FROM MovStSolicBei m")
public class MovStSolicBei implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MovStSolicBeiPK id;

	@Column(name="CO_MONEDA")
	private String coMoneda;

	@Column(name="IN_BENEF_COMPART")
	private String inBenefCompart;

	@Column(name="IN_NIVEL_ESCOLAR")
	private String inNivelEscolar;

	@Column(name="IN_TIPO_EMPRESA")
	private String inTipoEmpresa;

	@Column(name="MO_APORTE_BCV")
	private BigDecimal moAporteBcv;

	@Column(name="MO_EMPRESA_REPRES")
	private BigDecimal moEmpresaRepres;

	@Column(name="MO_MATRICULA")
	private BigDecimal moMatricula;

	@Column(name="MO_PERIODO")
	private BigDecimal moPeriodo;

	@Column(name="NU_TLF_FAM_RELAC")
	private String nuTlfFamRelac;

	@Column(name="TX_OBSERVACION")
	private String txObservacion;

	//bi-directional many-to-one association to StSolicitudBei
	@ManyToOne(optional=false)
	@JoinColumn(name="CO_STATUS",insertable=false, updatable=false)
	private StSolicitudBei stSolicitudBei;

	//bi-directional many-to-one association to SolicitudBei
	@ManyToOne(optional=false)
	@JoinColumn(name="NU_SOLICITUD",insertable=false, updatable=false)
	private SolicitudBei solicitudBei;
	

	//bi-directional many-to-one association to PeriodoEscolar
	@ManyToOne
	@JoinColumn(name="CO_PERIODO")
	private PeriodoEscolar periodoEscolar;

	public MovStSolicBei() {
	}

	public MovStSolicBeiPK getId() {
		return this.id;
	}

	public void setId(MovStSolicBeiPK id) {
		this.id = id;
	}

	public String getCoMoneda() {
		return this.coMoneda;
	}

	public void setCoMoneda(String coMoneda) {
		this.coMoneda = coMoneda;
	}

	public String getInBenefCompart() {
		return this.inBenefCompart;
	}

	public void setInBenefCompart(String inBenefCompart) {
		this.inBenefCompart = inBenefCompart;
	}

	public String getInNivelEscolar() {
		return this.inNivelEscolar;
	}

	public void setInNivelEscolar(String inNivelEscolar) {
		this.inNivelEscolar = inNivelEscolar;
	}

	public String getInTipoEmpresa() {
		return this.inTipoEmpresa;
	}

	public void setInTipoEmpresa(String inTipoEmpresa) {
		this.inTipoEmpresa = inTipoEmpresa;
	}

	public BigDecimal getMoAporteBcv() {
		return this.moAporteBcv;
	}

	public void setMoAporteBcv(BigDecimal moAporteBcv) {
		this.moAporteBcv = moAporteBcv;
	}

	public BigDecimal getMoEmpresaRepres() {
		return this.moEmpresaRepres;
	}

	public void setMoEmpresaRepres(BigDecimal moEmpresaRepres) {
		this.moEmpresaRepres = moEmpresaRepres;
	}

	public BigDecimal getMoMatricula() {
		return this.moMatricula;
	}

	public void setMoMatricula(BigDecimal moMatricula) {
		this.moMatricula = moMatricula;
	}

	public BigDecimal getMoPeriodo() {
		return this.moPeriodo;
	}

	public void setMoPeriodo(BigDecimal moPeriodo) {
		this.moPeriodo = moPeriodo;
	}

	public String getNuTlfFamRelac() {
		return this.nuTlfFamRelac;
	}

	public void setNuTlfFamRelac(String nuTlfFamRelac) {
		this.nuTlfFamRelac = nuTlfFamRelac;
	}

	public String getTxObservacion() {
		return this.txObservacion;
	}

	public void setTxObservacion(String txObservacion) {
		this.txObservacion = txObservacion;
	}

	public StSolicitudBei getStSolicitudBei() {
		return this.stSolicitudBei;
	}

	public void setStSolicitudBei(StSolicitudBei stSolicitudBei) {
		this.stSolicitudBei = stSolicitudBei;
	}

	public SolicitudBei getSolicitudBei() {
		return this.solicitudBei;
	}

	public void setSolicitudBei(SolicitudBei solicitudBei) {
		this.solicitudBei = solicitudBei;
	}

	public PeriodoEscolar getPeriodoEscolar() {
		return this.periodoEscolar;
	}

	public void setPeriodoEscolar(PeriodoEscolar periodoEscolar) {
		this.periodoEscolar = periodoEscolar;
	}

}