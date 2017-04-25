package ve.org.bcv.rhei.negocio;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.util.Constantes;

/**
 * Implementamos el comando patron, solicitud modificar
 * 
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com 19/02/2015 15:31:24
 * 
 */
public class SolActualizarSearchInfoPartI extends SolAction implements
		Serializable {
	private static Logger log = Logger
			.getLogger(SolActualizarSearchInfoPartI.class.getName());

	public SolActualizarSearchInfoPartI(HttpServletRequest request,
			HttpServletResponse response, ShowResultToView showResultToView,
			ServletContext sc) throws ServletException, IOException {
		super(request, response, showResultToView, sc, Constantes.ACTUALIZAR);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ShowResultToView ejecutar() throws ServletException, IOException {
		try {
		
			/**Cargamos la solicitud*/
			buscarNroSolicitud(getShowResultToView(), Constantes.ACTUALIZAR);
			/**Verificamos si es activa*/
			boolean isActiva=false;
			if ( !StringUtils.isEmpty(getShowResultToView().getCo_status())
					&& !"D".equalsIgnoreCase(getShowResultToView().getCo_status())) {
				isActiva = true;
			}
			
//			if (Constantes.NO_HAY_DATA.equals(getShowResultToView()
//					.getError())) {
//				/**Realizamos nuevamente la consulta pero con el codigo del empleado y beneficiario, excluyendo al colegio*/
//				ShowResultToView showNroSolicitud = new ShowResultToView();
//		    	showNroSolicitud.setCodEmp(getShowResultToView().getCodEmp());
//		    	showNroSolicitud.setCodigoBenef(getShowResultToView().getCodigoBenef());
//		     	/**Esto con el fin de notificar el ultimo numero de solicitud existente y orientrlos hacer una busqueda exacta*/
//		    	buscarNroSolicitud (showNroSolicitud, Constantes.ACTUALIZAR);
//		    	getShowResultToView().setNroSolicitud(showNroSolicitud.getNroSolicitud());
//				getShowResultToView().setMensaje(
//						"fracaso1");
//			} else if (!isActiva) {
//				getShowResultToView().setMensaje(
//						"fracaso");
//				getShowResultToView().setViene(null);
//			} else {
				cargaTrabajadorBeneficiarioCentroEdu(super.getRequest(), super.getResponse(), super.getShowResultToView(), super.getSc(),
						Constantes.ACTUALIZAR);
				getShowResultToView().setMensaje(null);
				
			//}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getShowResultToView().setDisabled("");
		getShowResultToView().setAccion(Constantes.ACTUALIZAR);
		return getShowResultToView();
	}

}
