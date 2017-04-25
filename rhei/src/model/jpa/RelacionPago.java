package model.jpa;

import java.io.Serializable;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the RELACION_PAGOS database table.
 * 
 */
@Entity
@Table(name="RELACION_PAGOS")
@NamedQuery(name="RelacionPago.findAll", query="SELECT r FROM RelacionPago r")
public class RelacionPago implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RelacionPagoPK id;

	@Column(name="CO_FORMA_PAGO")
	private String coFormaPago;

	@Column(name="CO_MONEDA")
	private String coMoneda;

	@Temporal(TemporalType.DATE)
	@Column(name="FE_REG_PAGO")
	private Date feRegPago;

	@Column(name="IN_TRAMITE")
	private String inTramite;

	@Column(name="MO_TOTAL_PAGO")
	private BigDecimal moTotalPago;

	@Column(name="TX_OBSERVACIONES")
	private String txObservaciones;

	//bi-directional many-to-one association to PeriodoEscolar
	@ManyToOne
	@JoinColumn(name="CO_PERIODO")
	private PeriodoEscolar periodoEscolar;

	//bi-directional many-to-one association to Factura
	@ManyToOne(optional=false)
	@JoinColumn(name="NU_ID_FACTURA",insertable=false, updatable=false)
	private Factura factura;
	
	

	//bi-directional many-to-one association to SolicitudBei
	@ManyToOne(optional=false)
	@JoinColumn(name="NU_SOLICITUD",insertable=false, updatable=false)
	private SolicitudBei solicitudBei;

	//bi-directional many-to-one association to TipoPago
	@ManyToOne
	@JoinColumn(name="CO_TI_PAGO")
	private TipoPago tipoPago;

	public RelacionPago() {
	}

	public RelacionPagoPK getId() {
		return this.id;
	}

	public void setId(RelacionPagoPK id) {
		this.id = id;
	}

	public String getCoFormaPago() {
		return this.coFormaPago;
	}

	public void setCoFormaPago(String coFormaPago) {
		this.coFormaPago = coFormaPago;
	}

	public String getCoMoneda() {
		return this.coMoneda;
	}

	public void setCoMoneda(String coMoneda) {
		this.coMoneda = coMoneda;
	}

	public Date getFeRegPago() {
		return this.feRegPago;
	}

	public void setFeRegPago(Date feRegPago) {
		this.feRegPago = feRegPago;
	}

	public String getInTramite() {
		return this.inTramite;
	}

	public void setInTramite(String inTramite) {
		this.inTramite = inTramite;
	}

	public BigDecimal getMoTotalPago() {
		return this.moTotalPago;
	}

	public void setMoTotalPago(BigDecimal moTotalPago) {
		this.moTotalPago = moTotalPago;
	}

	public String getTxObservaciones() {
		return this.txObservaciones;
	}

	public void setTxObservaciones(String txObservaciones) {
		this.txObservaciones = txObservaciones;
	}

	public PeriodoEscolar getPeriodoEscolar() {
		return this.periodoEscolar;
	}

	public void setPeriodoEscolar(PeriodoEscolar periodoEscolar) {
		this.periodoEscolar = periodoEscolar;
	}

	public Factura getFactura() {
		return this.factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public SolicitudBei getSolicitudBei() {
		return this.solicitudBei;
	}

	public void setSolicitudBei(SolicitudBei solicitudBei) {
		this.solicitudBei = solicitudBei;
	}

	public TipoPago getTipoPago() {
		return this.tipoPago;
	}

	public void setTipoPago(TipoPago tipoPago) {
		this.tipoPago = tipoPago;
	}

}