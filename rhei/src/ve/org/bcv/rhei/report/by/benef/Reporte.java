package ve.org.bcv.rhei.report.by.benef;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import com.bcv.reporte.relacionpago.FacturaRpago3Bean;

import ve.org.bcv.rhei.bean.DocumentosBean;
import ve.org.bcv.rhei.bean.ShowResultToView;

/**
 * Genaremos reportes de irepor 5.6.0
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com
 * 24/04/2015 14:48:22
 * 
 */
public interface Reporte extends Serializable {
	DocumentosBean generar(InputStream jrxml,Map<String, Object> parameters, String fileOut);
	List<?> searchObjects();
	public abstract ShowResultToView ejecutar() throws ServletException, IOException ;
	List<FacturaRpago3Bean> searchFacturas(String descripPeriodo, long nuSolicitud, String companiaAnalista, String filtrarByMesOrComplementoOrAmbos,int receptorPago,int coFormaPago, String meses,int coRepStatus) ;
 
}
