/**
 * 
 */
package com.bcv.comparator;

import java.util.Comparator;

import com.bcv.reporte.relacionpago.ProveedorRpago1Bean;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 28/10/2015 15:14:57
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class SortProveedorRpago1BeanNumRifAsc  implements Comparator<ProveedorRpago1Bean>{

 
	public int compare(ProveedorRpago1Bean o1, ProveedorRpago1Bean o2) {
		// TODO Auto-generated method stub
	 
		return (o1.getNbProveedor().compareTo(o2.getNbProveedor())) ;
	}


}