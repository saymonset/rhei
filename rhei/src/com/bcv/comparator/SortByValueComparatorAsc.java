/**
 * 
 */
package com.bcv.comparator;

import java.util.Comparator;

import ve.org.bcv.rhei.bean.ValorNombre;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 12/08/2015 08:48:56
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class SortByValueComparatorAsc implements Comparator<ValorNombre>{

 
	public int compare(ValorNombre o1, ValorNombre o2) {
		// TODO Auto-generated method stub
	 
		return (o1.getValor().compareTo(o2.getValor())) ;
	}


}
