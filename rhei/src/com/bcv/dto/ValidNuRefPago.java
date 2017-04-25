/**
 * 
 */
package com.bcv.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 29/02/2016 11:21:41
 * 2016
 * mail : oraclefedora@gmail.com
 */
public class ValidNuRefPago implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<NuRefPago> historico;
	private java.math.BigDecimal  bcvMonto;
	private Date bcvVigencia;
	private Boolean isCanPayTotal;
	private BigDecimal acumuladorByMes;
	private BigDecimal acumuladorBCV;
	 
	public List<NuRefPago> getHistorico() {
		return historico;
	}
	public void setHistorico(List<NuRefPago> historico) {
		this.historico = historico;
	}
	public java.math.BigDecimal getBcvMonto() {
		return bcvMonto;
	}
	public void setBcvMonto(java.math.BigDecimal bcvMonto) {
		this.bcvMonto = bcvMonto;
	}
	public Date getBcvVigencia() {
		return bcvVigencia;
	}
	public void setBcvVigencia(Date bcvVigencia) {
		this.bcvVigencia = bcvVigencia;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Boolean getIsCanPayTotal() {
		return isCanPayTotal;
	}
	public void setIsCanPayTotal(Boolean isCanPayTotal) {
		this.isCanPayTotal = isCanPayTotal;
	}
	public BigDecimal getAcumuladorByMes() {
		return acumuladorByMes;
	}
	public void setAcumuladorByMes(BigDecimal acumuladorByMes) {
		this.acumuladorByMes = acumuladorByMes;
	}
	public BigDecimal getAcumuladorBCV() {
		return acumuladorBCV;
	}
	public void setAcumuladorBCV(BigDecimal acumuladorBCV) {
		this.acumuladorBCV = acumuladorBCV;
	}
	 
	 

}
