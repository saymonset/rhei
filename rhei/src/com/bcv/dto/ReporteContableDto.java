/**
 * 
 */
package com.bcv.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 23/08/2016 15:08:26
 * 2016
 * mail : oraclefedora@gmail.com
 */
public class ReporteContableDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tipoEmp;
	private BigDecimal monto;
	public String getTipoEmp() {
		return tipoEmp;
	}
	public void setTipoEmp(String tipoEmp) {
		this.tipoEmp = tipoEmp;
	}
	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}


}
