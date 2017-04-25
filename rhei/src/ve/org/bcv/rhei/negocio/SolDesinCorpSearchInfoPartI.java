package ve.org.bcv.rhei.negocio;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.download.DocumentOpen;

import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.util.Constantes;

/**
 * Implementamos el comando patron, solicitud desincorporar
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com
 * 25/02/2015 19:20:55
 * 
 */
public class SolDesinCorpSearchInfoPartI extends SolAction implements Serializable {
	private static Log log = LogFactory.getLog(SolDesinCorpSearchInfoPartI.class);
	public SolDesinCorpSearchInfoPartI(HttpServletRequest request,
			HttpServletResponse response, ShowResultToView showResultToView,
			ServletContext sc) throws ServletException, IOException {
		super(request, response, showResultToView, sc,Constantes.DESINCORPORAR);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	 
	public ShowResultToView ejecutar() throws ServletException, IOException {
		cargaTrabajadorBeneficiarioCentroEdu(super.getRequest(), super.getResponse(), super.getShowResultToView(), super.getSc(),
				Constantes.DESINCORPORAR);
		/**Cargamos la solicitud*/
		try {
			log.debug("super.getShowResultToView().getCodEmp())="+super.getShowResultToView().getCodEmp());
			buscarNroSolicitud ( super.getShowResultToView(), Constantes.DESINCORPORAR);
			boolean isActiva=false;
			if ( !StringUtils.isEmpty(getShowResultToView().getCo_status())
					&& !"D".equalsIgnoreCase(getShowResultToView().getCo_status())) {
				isActiva = true;
			}
			/**Para desincorporar, la cuenta debe estar activa*/
		if ( Constantes.NO_HAY_DATA.equals(getShowResultToView().getError())) {

			ShowResultToView showNroSolicitud = new ShowResultToView();
	    	showNroSolicitud.setCodEmp(getShowResultToView().getCodEmp());
	    	showNroSolicitud.setCodigoBenef(getShowResultToView().getCodigoBenef());
	     
	    	/**Realizamos nuevamente la consulta pero con el codigo del empleado y beneficiario, excluyendo al colegio*/
	     	/**Esto con el fin de notificar el ultimo numero de solicitud existente y orientrlos hacer una busqueda exacta*/
	    	buscarNroSolicitud (showNroSolicitud, Constantes.DESINCORPORAR);
	    	
	    	
	    	if ( !StringUtils.isEmpty(showNroSolicitud.getCo_status())
					&& !"D".equalsIgnoreCase(showNroSolicitud.getCo_status())) {
	    		getShowResultToView().setNroSolicitud(showNroSolicitud.getNroSolicitud());
			} 
				getShowResultToView().setMensaje("fracaso1");
		 
	    	
	    	
	    	
			
			
			
		}else if (!isActiva) {
			getShowResultToView().setMensaje("fracaso");
			getShowResultToView().setViene(null);
			
			
		}else{
//			GENERAMOS LA TABLA DE DESINCORPORAR
			getProcesar().generarInfoToDesincorporar(getShowResultToView().getCodEmp(),getShowResultToView().getCodigoBenef(),getRequest(), getShowResultToView());
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getShowResultToView().setAccion(Constantes.DESINCORPORAR);
		return getShowResultToView();
	}
	
	 

	 
 

}
