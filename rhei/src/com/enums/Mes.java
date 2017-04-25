package com.enums;

/**
 * Tendremos los dias del mes del anio y la matricula
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com
 * 17/03/2015 12:58:40
 * 
 */
/**lOS NUMEROS NEGATIVOS , REPRESENTAN QUE YA FUERON CANCELADOS*/
public enum Mes {
	MATRICULA(14), SEPTIEMBRE(9), OCTUBRE(10), NOVIEMBRE(11), DICIEMBRE(12),ENERO(1),FEBRERO(2),MARZO(3),ABRIL(4),
	MAYO(5),JUNIO(6),JULIO(7),AGOSTO(8),COMPLEMENTO_REEMBOLSO(13),MATRICULAP(-14),
	SEPTIEMBREP(-9), OCTUBREP(-10), NOVIEMBREP(-11), DICIEMBREP(-12),ENEROP(-1),FEBREROP(-2),MARZOP(-3),ABRILP(-4),
	MAYOP(-5),JUNIOP(-6),JULIOP(-7),AGOSTOP(-8),COMPLEMENTO_REEMBOLSOP(-13)
	;
	private int value;

	private Mes(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}

 