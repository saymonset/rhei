/**
 * 
 */
package com.bcv.dao.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.lang3.StringUtils;

import ve.org.bcv.rhei.util.Constantes;

import com.bcv.dao.jdbc.ManejadorDB;
import com.bcv.dao.jdbc.MovStSolicBeiDao;
import com.bcv.dao.jdbc.RelacionDePagosDao;
import com.bcv.dao.jdbc.SimpleJDBCDaoImpl;
import com.bcv.model.MovStSolicBei;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 03/09/2015 10:39:32
 * 2015
 * mail : oraclefedora@gmail.com
 */
public class MovStSolicBeiDaoImpl   extends SimpleJDBCDaoImpl<MovStSolicBei> implements
MovStSolicBeiDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RelacionDePagosDao relacionDePagosDao = new RelacionDePagosDaoImpl();

	public void SearchMontoBCV(String codigoCompania,Double montoBcv){
		ManejadorDB manejador=new ManejadorDB();;
		Connection con = null;	
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			con = manejador.coneccionPool();
			StringBuilder sql = new StringBuilder("");
			sql.append(" select MS.NU_SOLICITUD,MS.IN_BENEF_COMPART,MS.MO_PERIODO,MS.MO_MATRICULA from RHEI.MOV_ST_SOLIC_BEI ms ");
			sql.append(" inner join  RHEI.SOLICITUD_BEI  sb  ");
					sql.append(" on MS.NU_SOLICITUD=SB.NU_SOLICITUD  ");
							sql.append(" inner join  PERSONAL.TODOS_EMPLEADOS pe  ");
									sql.append(" on PE.CODIGO_EMPLEADO=SB.CO_EMPLEADO  ");
											sql.append(" where PE.CO_CIA_FISICA=?  ");
												stmt = con.prepareStatement(sql.toString());
												int posicion=1;
												stmt.setString(posicion++, codigoCompania);
												rs = stmt.executeQuery();
												int numSolicitud=0;
												double moPeriodo=0.0d;
												double moMatricula=0.0d;
												String inBenefCompart="N";
												while (rs.next()) {
													 
													 inBenefCompart= rs.getString("IN_BENEF_COMPART");
													 moPeriodo=rs.getDouble("MO_PERIODO");
													 moMatricula=rs.getDouble("MO_MATRICULA");
													 numSolicitud=rs.getInt("NU_SOLICITUD");
													 /**Actualizar monto bcv*/
													 UpdateMontoBCV(montoBcv, numSolicitud,con);
														/**Actualizarmos el monto de bcv para la tabla  RHEI.RELACION_PAGOS */
													 relacionDePagosDao.SearchMontoBCV(numSolicitud, montoBcv, inBenefCompart, moMatricula, moPeriodo,con);
												}
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			liberarConexion(rs, stmt, con);
		}
	}
	
	public boolean isSolicitudActivaParaRealizarPago(String cedulaFamiliar){
		boolean exito=false;
		ManejadorDB manejador=new ManejadorDB();;
		Connection con = null;	
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			int cedFam=0;
			if (!StringUtils.isEmpty(cedulaFamiliar) && StringUtils.isNumeric(cedulaFamiliar)){
				cedFam=Integer.parseInt(cedulaFamiliar);
			}
			con = manejador.coneccionPool();
			StringBuilder sql = new StringBuilder("");
			sql.append(" select  SB.NU_RIF_PROVEEDOR from RHEI.SOLICITUD_BEI  sb ");
				sql.append(" INNER JOIN RHEI.MOV_ST_SOLIC_BEI mbei ON SB.NU_SOLICITUD=MBEI.NU_SOLICITUD ");
				  sql.append(" WHERE MBEI.CO_STATUS=? AND SB.CEDULA_FAMILIAR=? AND SB.NU_RIF_PROVEEDOR !='0' AND  MBEI.MO_MATRICULA !=0  AND MBEI.MO_PERIODO!=0 ");
												stmt = con.prepareStatement(sql.toString());
												int posicion=1;
												stmt.setString(posicion++, Constantes.ACTIVO_EMPLEADO);
												stmt.setInt(posicion++, cedFam);
												rs = stmt.executeQuery();
												while (rs.next()) {
													exito=true;	 
													break;
												}
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			liberarConexion(rs, stmt, con);
		}
	
		return exito;
	}
	
	
	
	
	
	private int UpdateMontoBCV(Double montoBcv,int numSolicitud,Connection con){
		
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int exito=-1;
		try {
		 

			StringBuilder sql = new StringBuilder("");
			sql.append("UPDATE RHEI.MOV_ST_SOLIC_BEI  set MO_APORTE_BCV=? WHERE NU_SOLICITUD=? and NU_SOLICITUD in (select nu_solicitud from RHEI.RELACION_PAGOS r where R.CO_REP_STATUS is null )");
												stmt = con.prepareStatement(sql.toString());
												int posicion=1;
												stmt.setDouble(posicion++, montoBcv);
												stmt.setInt(posicion++, numSolicitud);
												exito = stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			liberarConexion(rs, stmt, null);
		}
		return exito;
	}

	

}
