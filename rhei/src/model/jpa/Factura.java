package model.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the FACTURA database table.
 * 
 */
@Entity
@NamedQuery(name="Factura.findAll", query="SELECT f FROM Factura f")
public class Factura implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="NU_ID_FACTURA")
	private long nuIdFactura;

	@Column(name="CO_MONEDA")
	private String coMoneda;

	@Column(name="CO_MONEDA_ADICIONA")
	private String coMonedaAdiciona;

	@Temporal(TemporalType.DATE)
	@Column(name="FE_FACTURA")
	private Date feFactura;

	@Column(name="MO_FACTURA")
	private BigDecimal moFactura;

	@Column(name="MO_PAGO_ADICIONAL")
	private double moPagoAdicional;

	@Column(name="NU_CONTROL")
	private String nuControl;

	@Column(name="NU_FACTURA")
	private String nuFactura;

	@Column(name="NU_RIF_PROVEEDOR")
	private String nuRifProveedor;

	@Column(name="TX_CONCEPTO_PAGO")
	private String txConceptoPago;

	//bi-directional many-to-one association to RelacionPago
	@OneToMany(mappedBy="factura")
	private List<RelacionPago> relacionPagos;

	public Factura() {
	}

	public long getNuIdFactura() {
		return this.nuIdFactura;
	}

	public void setNuIdFactura(long nuIdFactura) {
		this.nuIdFactura = nuIdFactura;
	}

	public String getCoMoneda() {
		return this.coMoneda;
	}

	public void setCoMoneda(String coMoneda) {
		this.coMoneda = coMoneda;
	}

	public String getCoMonedaAdiciona() {
		return this.coMonedaAdiciona;
	}

	public void setCoMonedaAdiciona(String coMonedaAdiciona) {
		this.coMonedaAdiciona = coMonedaAdiciona;
	}

	public Date getFeFactura() {
		return this.feFactura;
	}

	public void setFeFactura(Date feFactura) {
		this.feFactura = feFactura;
	}

	public BigDecimal getMoFactura() {
		return this.moFactura;
	}

	public void setMoFactura(BigDecimal moFactura) {
		this.moFactura = moFactura;
	}

	public double getMoPagoAdicional() {
		return this.moPagoAdicional;
	}

	public void setMoPagoAdicional(double moPagoAdicional) {
		this.moPagoAdicional = moPagoAdicional;
	}

	public String getNuControl() {
		return this.nuControl;
	}

	public void setNuControl(String nuControl) {
		this.nuControl = nuControl;
	}

	public String getNuFactura() {
		return this.nuFactura;
	}

	public void setNuFactura(String nuFactura) {
		this.nuFactura = nuFactura;
	}

	public String getNuRifProveedor() {
		return this.nuRifProveedor;
	}

	public void setNuRifProveedor(String nuRifProveedor) {
		this.nuRifProveedor = nuRifProveedor;
	}

	public String getTxConceptoPago() {
		return this.txConceptoPago;
	}

	public void setTxConceptoPago(String txConceptoPago) {
		this.txConceptoPago = txConceptoPago;
	}

	public List<RelacionPago> getRelacionPagos() {
		return this.relacionPagos;
	}

	public void setRelacionPagos(List<RelacionPago> relacionPagos) {
		this.relacionPagos = relacionPagos;
	}

	public RelacionPago addRelacionPago(RelacionPago relacionPago) {
		getRelacionPagos().add(relacionPago);
		relacionPago.setFactura(this);

		return relacionPago;
	}

	public RelacionPago removeRelacionPago(RelacionPago relacionPago) {
		getRelacionPagos().remove(relacionPago);
		relacionPago.setFactura(null);

		return relacionPago;
	}

}