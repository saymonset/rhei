package com.bcv.dao.jdbc;

import java.sql.SQLException;
import java.util.List;

import com.bcv.model.ReporteBecaUtile;

public interface ReporteBecaUtileDao extends SimpleJDBCDao<ReporteBecaUtile>{
	 List<ReporteBecaUtile> reporteBecaUtileDao(String periodoScolar, String tipoEmpleado, String status) throws SQLException;
	 void updateReporteBecaUtileDao(String periodoScolar,String txObservacion, String tipoEmpleado) throws SQLException ;
	 List<ReporteBecaUtile> reporteBecaUtileDesincorporado(String periodoScolar, String observacion) throws SQLException ;
	 List<ReporteBecaUtile> reporteBecaDesactivados(String periodoScolar) throws SQLException ;
}
