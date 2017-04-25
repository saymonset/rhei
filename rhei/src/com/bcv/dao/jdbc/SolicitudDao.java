/**
 * 
 */
package com.bcv.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.bean.ValorNombre;

import com.bcv.model.ConyugeTrabajo;
import com.bcv.model.Solicitud;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 02/07/2015 15:27:49
 * 2015
 * mail : oraclefedora@gmail.com
 */
public interface SolicitudDao extends SimpleJDBCDao<Solicitud> {
	Solicitud solicitudBylastCodEmpAndFamily(int codEmplado,
			int codigoBenef,int mes,String anioPeriodo)throws SQLException;
	 List<String> cargarPeriodoByNroSolicitud(int nroSolicitud)  throws SQLException ;
	 List<String> cargarPeriodo(String origen) throws SQLException ;
	 List<String> cargarPeriodoEscolar(String estatusSolicitud) throws SQLException ;
	 List<String> cargarFormaPago() throws SQLException ;
	 List<String> cargarEstadosSolicitud() throws SQLException ;
	 Solicitud BscarSolConCodEmpCodBenf(String codEmplado,
				String codigoBenef) throws SQLException ;
	 Solicitud cedulaEmplYOtrosSolicitud(int nroSolicitud) throws SQLException ;
	 Solicitud BscarSolConCodEmpCodBenfNrif(String codEmplado,
				String codigoBenef,String nroRifCentroEdu) throws SQLException ;
	 boolean activaSolicitud(String codEmplado, String codigoBenef) throws SQLException ;
	 List<String> buscarSolicitud(String filtro,int codigoEmpleado,int cedulaFamiliar,int nroSolicitud) throws SQLException ;
	 List<String> buscarSolicitud(String estatus, String beneficio,
				String parametro) throws SQLException;
	 String filtrarSolicitud(ArrayList<String> lista);
	 String generadorTablaSolicitudes(ArrayList<String> lista);
	 int verificarNroSolicitud(int nroSolicitud) throws SQLException ;
     int obtenerNumeroSolicitud(int codigoEmpleado,int cedulaFamiliar) throws SQLException ;
     int obtenerNumeroSolicitud(String solicitante, String periodo,int codigoEmpleado,int cedulaFamiliar) throws SQLException ;
     int obtenerCodigoEmpleado(int nroSolicitud) throws SQLException ;
     int obtenerCedulaEmpleado(int nroSolicitud) throws SQLException ;
     int obtenerCodigoFamiliar(int nroSolicitud) throws SQLException ;
     Solicitud buscarSolicitud(int nroSolicitud)
    			throws SQLException;
     String buscarFormatoDelPeriodo(Connection con,
    			PreparedStatement pstmt, int codigoPeriodo) throws SQLException;
     public Solicitud buscarSolicitud(String operacion, int nroSolicitud) throws SQLException ;
     Solicitud consultarSolicitud( int nroSolicitud) throws SQLException ;
     HttpServletRequest cargarAtributosSolicitud(HttpServletRequest request,
    			String operacion,Solicitud solicitud ) ;
     String guardarSolicitud(String siglaExpediente, String tipoNomina,int codigoEmpleado,int cedulaFamiliar,String formaDePago,
    			String nroRifCentroEdu,String tipoEducacion,String tipoCentroEdu,String periodoDePago,String localidadCEI
    			,String co_status,String nivelEscolar,String comparteBeneficio,String tipoEmpresa,Double montoAporteBCV,
    			Double montoPeriodo,Double montoMatricula,Double montoEmpresa,int codigoPeriodo,ConyugeTrabajo conyugeTrabajo) throws SQLException ;
     String updateCambiosEnSolicitud(String co_status,String nivelEscolar,Double montoPeriodo,Double montoMatricula,
    			int codigoPeriodo,String comparteBeneficio,String tipoEmpresa,Double montoEmpresa,int nroSolicitud) throws SQLException;
     String guardarCambiosEnSolicitud(int nroSolicitud,String co_status,String nivelEscolar,String comparteBeneficio,String tipoEmpresa,
    			Double montoAporteBCV,Double montoPeriodo,Double montoMatricula,Double montoEmpresa,int codigoPeriodo) throws SQLException ;
     String guardarCambiosEnMovStSolicBei(String co_status,String nivelEscolar,Double montoPeriodo,Double montoMatricula,
    			int codigoPeriodo,String comparteBeneficio,String tipoEmpresa,Double montoEmpresa,int nroSolicitud) throws SQLException;
     String updateCambiosEnSolicitudBEI(String formaDePago,String nroRifCentroEdu,String tipoEducacion,String tipoCentroEdu,
    			String periodoDePago,int nroSolicitud) throws SQLException;
     String guardarCambiosEnSolicitud(String[] listadoNroSolicitudes) throws SQLException ;
     String guardarCambiosEnSolicitud(String[] listaSolicitud,
    			String periodoEscolar, String fecha, int cantidadRegistros) throws SQLException;
     String verificarCambioSalarioMinimo(String tipoBeneficio,
    			int cantCombCiaTipoTrab) throws SQLException ;
     List<ValorNombre> estadosSolicitudLst() throws SQLException ;	 
     String addMovStSolicitudBEI(String[] listadoNroSolicitudes) throws SQLException ;
     ShowResultToView searchEmpleado(String numSolicitud);
     Solicitud solicitudBylastCodEmpAndFamily(int codEmplado,
 			int codigoBenef,  String txDescripPeriodo)
 			throws SQLException;
     List<Solicitud> BscarNrosSolicitudesWithCodEmpStatusCodBenef(String codEmplado,
 			String status,String codigoBenef) throws SQLException;
     int updateColegioSolicitud(String numRif ,int nroSolicitud);
 	boolean isRenovacion(int nroSolicitud)throws SQLException ;
 	  String desincorporarSolicitudByAnio(String codAnio)
			throws SQLException;
 	 String regularOrtEspecial( String numSolicitud)
 			throws SQLException ;
}
