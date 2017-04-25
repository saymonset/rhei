package ve.org.bcv.rhei.negocio;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.util.Constantes;

import com.bcv.dao.jdbc.FamiliarDao;
import com.bcv.dao.jdbc.impl.FamiliarDaoImpl;
import com.bcv.model.Familiar;

/**
 * Implementamos el comando patron, solicitud incluir
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com
 * 19/02/2015 15:17:29
 * 
 */
public class SolBuscarInfo extends SolAction {
private HttpServletRequest request;
private static Logger log = Logger.getLogger(SolBuscarInfo.class
		.getName());
private FamiliarDao familiarDao= new FamiliarDaoImpl();
	public SolBuscarInfo(HttpServletRequest request, HttpServletResponse response,
			ShowResultToView showResultToView, ServletContext sc)
			throws ServletException, IOException {
		
		super(request, response, showResultToView, sc,Constantes.INCLUIR);
		
		this.request=request;
	}
	
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	public ShowResultToView ejecutar() throws ServletException, IOException {
		

		
		 
		
		if (super.activaSolicitud(getShowResultToView())) {
			getShowResultToView().setMensaje("fracaso");
		}else{
			/**No tienen ninguna solicitud activa.. blanqueamos estos campos para volverlos a llenar, Puede 
			 * haber cargado una solicitud de tipo Desincorporada.. esto lo hace, porque accion hace la funcion de consultar  la 
			 * ultima solicitud*/
			/** * Cargadmos la solicitud si existe */
			try {
				/**Cargamos data*/
				super.cargaTrabajadorBeneficiarioCentroEdu(super.getRequest(), super.getResponse(), super.getShowResultToView(), super.getSc(),
						Constantes.INCLUIR);
				
				if (!StringUtils.isEmpty(getShowResultToView().getCedula())&& StringUtils.isNumeric(getShowResultToView().getCedula())){
					Familiar familiar=familiarDao.consultarConyuge(new Long(getShowResultToView().getCedula()));
					if (familiar!=null){
						request.setAttribute("cedulaconyuge",getShowResultToView().getCedula());	
						request.setAttribute("nameconyuge", familiar.getNombre());
						request.setAttribute("apellidoconyuge", familiar.getApellido());
						
					}
				}
				
				
				buscarNroSolicitud ( getShowResultToView(), Constantes.INCLUIR);
				blanquearAlgunosDatos(getShowResultToView());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		getShowResultToView().setAccion(Constantes.INCLUIR);
		return getShowResultToView();
	}
	
	
	

	 
	/**
	 * @param showResultToView
	 */
	private void blanquearAlgunosDatos(ShowResultToView showResultToView){
		/**No tienen ninguna solicitud activa.. blanqueamos estos campos para volverlos a llenar, Puede 
		 * haber cargado una solicitud de tipo Desincorporada.. esto lo hace, porque accion hace la funcion de consultar  la 
		 * ultima solicitud*/
		showResultToView.setBenefCompartido("");
		showResultToView.setMontoAporteEmp(0.0);
		/**Buscamos datos del Colegio*/
		String nroRifCentroEdu = this.request.getParameter("nroRifCentroEdu") != null ? (String) this.request
				.getParameter("nroRifCentroEdu") : "";
		showResultToView.setNroRifCentroEdu(nroRifCentroEdu);
		try {
			/** por referencia cargamos data en getShowResultToView*/
			super.searchCentroEducativo(showResultToView);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error(e.toString());
		}
		
		showResultToView.setTipoInstitucion("");
		showResultToView.setTipoEducacion("");
		showResultToView.setNivelEscolaridad("");
		showResultToView.setPeriodoPago("");
		showResultToView.setMontoPeriodo(0.0);
		showResultToView.setMontoMatricula(0.0);
		showResultToView.setFormaPago("");
		/**End Buscamos datos del Colegio*/
	}

	

}
