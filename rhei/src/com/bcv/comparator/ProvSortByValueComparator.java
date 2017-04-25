/**ssss
 * 
 */
package com.bcv.comparator;
    
import java.util.Comparator;

import ve.org.bcv.rhei.bean.Proveedor;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 07/08/2015 09:35:36
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class ProvSortByValueComparator implements Comparator<Proveedor>{

 
	public int compare(Proveedor o1, Proveedor o2) {
		// TODO Auto-generated method stub
		return (o2.getValue() < o1.getValue() ) ? -1: (o2.getValue() > o1.getValue() ) ? 1:0 ;
	}

}
