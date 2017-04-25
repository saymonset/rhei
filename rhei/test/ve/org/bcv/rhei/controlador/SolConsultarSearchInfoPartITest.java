package ve.org.bcv.rhei.controlador;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.negocio.SolConsultarSearchInfoPartI;

/**
 * Se hace la prueba para consultar solicitud
 * @author sirodrig
 *
 */
public class SolConsultarSearchInfoPartITest {
	private static Logger log = Logger.getLogger(SolConsultarSearchInfoPartITest.class
			.getName());
	/**
	 * Solo usado para hacer test de la aplicacion
	 * @param cedula del empledo
	 * @param codEmp, codigo del empleado
	 * @param codigoBenef, codigo del beneficiario
	 * @param compania, compania BCV
	 * @param nroRifCentroEdu, rif del centro de educacion
	 * @throws ServletException
	 * @throws IOException
	 */
	//17236342
public static void main(String[] args) {
	try {
		int nroSolicitudNotKnow=0;
		SolConsultarSearchInfoPartI solConsultarSearchInfoPartI = new SolConsultarSearchInfoPartI(
				"14176742","14674","11843","01","J306356029-C",nroSolicitudNotKnow);
		ShowResultToView showResultToView=solConsultarSearchInfoPartI.ejecutar();
		log.info("showResultToView.getLocalidadBCV()="+showResultToView.getLocalidadBCV());
	} catch (ServletException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
}
