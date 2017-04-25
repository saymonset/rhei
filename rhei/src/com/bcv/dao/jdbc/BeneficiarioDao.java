/**
 * 
 */
package com.bcv.dao.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.bean.ValorNombre;

import com.bcv.model.Beneficiario;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco 10/08/2015 15:02:18 2015 mail :
 *         oraclefedora@gmail.com
 */
public interface BeneficiarioDao {
	ArrayList<String> buscarBeneficiarioII(int cedulaTrabajador, int edadMin,
			int edadMax);
	ArrayList<String> buscarBeneficiarioSinRestriccion(int cedulaTrabajador);
	ArrayList<String> buscarBeneficiario(int cedulaTrabajador, int edadMin,
			int edadMax);
	ArrayList<String> buscarBeneficiarioConSolicitudII(int cedulaTrabajador);
	Beneficiario buscarBeneficiario(int codigo) throws SQLException;
	boolean verificarSolicitud(int codigoEmpleado, String operacion, int codigo);
	ShowResultToView cargarAtributosBeneficiario(
			ShowResultToView showResultToView, int codigo, String apellido,
			String nombre, String fechaNacimento, int edad, String status);
	int calcularEdad(String fecha_nac);
	List<ValorNombre> buscarBeneficiarioByCedula(
			int cedulaTrabajador) ;

}
