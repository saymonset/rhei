/**
 * 
 */
package com.bcv.comparator;

import java.util.Comparator;

import ve.org.bcv.rhei.bean.ValorNombre;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 26/08/2015 15:04:09
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class SortByNameComparatorAsc  implements Comparator<ValorNombre>{

 
	public int compare(ValorNombre o1, ValorNombre o2) {
		// TODO Auto-generated method stub
	 
		return (o1.getNombre().compareTo(o2.getNombre())) ;
	}


}
