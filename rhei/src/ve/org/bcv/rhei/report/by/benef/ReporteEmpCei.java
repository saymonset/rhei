/**
 * 
 */
package ve.org.bcv.rhei.report.by.benef;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import com.bcv.reporte.pagotributo.PagoTributoCEI;

import ve.org.bcv.rhei.bean.DocumentosBean;
import ve.org.bcv.rhei.bean.ShowResultToView;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 04/09/2015 15:24:34
 * 2015
 * mail : oraclefedora@gmail.com
 */
public interface ReporteEmpCei extends Serializable {
	DocumentosBean generar(InputStream jrxml,Map<String, Object> parameters, String fileOut);
	List<?> searchObjects();
	public abstract ShowResultToView ejecutar() throws ServletException, IOException ;
	List<PagoTributoCEI> getList(); 
}
