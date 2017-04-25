/**
 * 
 */
package ve.org.bcv.rhei.negocio;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 26/08/2015 14:48:41
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class MesesBean {
	/** Sacamos los meses */
private 	String[] mes = { "MATRICULA", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE",
			"DICIEMBRE", "ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO",
			"JUNIO", "JULIO", "AGOSTO" };
private 	String[] meses = { "14", "9", "10", "11", "12", "1", "2", "3", "4", "5",
			"6", "7", "8" };
private String[] mesesAux = { "14", "9", "10", "11", "12", "1", "2", "3", "4",
			"5", "6", "7", "8" };
public String[] getMes() {
	return mes;
}
public void setMes(String[] mes) {
	this.mes = mes;
}
public String[] getMeses() {
	return meses;
}
public void setMeses(String[] meses) {
	this.meses = meses;
}
public String[] getMesesAux() {
	return mesesAux;
}
public void setMesesAux(String[] mesesAux) {
	this.mesesAux = mesesAux;
}
/***
 * 
 * */
}
