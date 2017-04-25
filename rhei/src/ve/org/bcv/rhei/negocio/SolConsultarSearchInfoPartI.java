package ve.org.bcv.rhei.negocio;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.util.Constantes;

import com.bcv.dao.jdbc.CompaniaDao;
import com.bcv.dao.jdbc.impl.CompaniaDaoImpl;
import com.bcv.model.Compania;

/**Implementamos el comando patron, solicitud consultar
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com
 * 19/02/2015 16:02:25
 * 
 */
public class SolConsultarSearchInfoPartI extends SolAction implements Serializable {
	private CompaniaDao companiaDao = new CompaniaDaoImpl();
	private String codCompania;
	
	public SolConsultarSearchInfoPartI(){}
	/**
	 * 
	 * @param request
	 * @param response
	 * @param showResultToView
	 * @param sc
	 * @throws ServletException
	 * @throws IOException
	 */
	public SolConsultarSearchInfoPartI(HttpServletRequest request,
			HttpServletResponse response, ShowResultToView showResultToView,
			ServletContext sc) throws ServletException, IOException {
		super(request, response, showResultToView, sc,Constantes.CONSULTAR);
		 
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Solo usado para hacer test de la aplicacion
	 * @param cedula
	 * @param codEmp
	 * @param codigoBenef
	 * @param compania
	 * @param nroRifCentroEdu
	 * @throws ServletException
	 * @throws IOException
	 */
	public SolConsultarSearchInfoPartI(String cedula,String codEmp,String codigoBenef,String compania,String nroRifCentroEdu,int nroSolicitud) throws ServletException, IOException {
		super();
		super.setShowResultToView(new ShowResultToView ());
		super.getShowResultToView().setCedula(cedula);
		super.getShowResultToView().setCodEmp(codEmp);
		super.getShowResultToView().setCodigoBenef(codigoBenef);
		super.getShowResultToView().setCompania(compania);
		super.getShowResultToView().setNroRifCentroEdu(nroRifCentroEdu);
		super.getShowResultToView().setNroSolicitud(nroSolicitud);
		this.codCompania=compania;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	 
	public ShowResultToView ejecutar() throws ServletException, IOException {
		try {
		
			/**Cargamos la solicitud*/
			/**Esta solicitud que consultamos, hace busqueda por el  codigo del empleado y beneficiario, rif del colegio obligatoriamente*/
		buscarNroSolicitud ( getShowResultToView(), Constantes.CONSULTAR);
	
	    if (Constantes.NO_HAY_DATA.equals(getShowResultToView().getError())) {
	    	ShowResultToView showNroSolicitud = new ShowResultToView();
	    	showNroSolicitud.setCodEmp(getShowResultToView().getCodEmp());
	    	showNroSolicitud.setCodigoBenef(getShowResultToView().getCodigoBenef());
	     
	    	/**Realizamos nuevamente la consulta pero con el codigo del empleado y beneficiario, excluyendo al colegio*/
	     	/**Esto con el fin de notificar el ultimo numero de solicitud existente y orientrlos hacer una busqueda exacta*/
	    	buscarNroSolicitud (showNroSolicitud, Constantes.CONSULTAR);
	    	getShowResultToView().setNroSolicitud(showNroSolicitud.getNroSolicitud());
			getShowResultToView().setMensaje("fracaso1");
		}else{
			cargaTrabajadorBeneficiarioCentroEdu(super.getRequest(), super.getResponse(), super.getShowResultToView(), super.getSc(),
					Constantes.CONSULTAR);
			
			/**Carga data del trabajador*/
			setShowResultToView(super.obtenerDatosDelTrabajador(getShowResultToView()));
			/**Carga data de los proveedores*/
			setShowResultToView(super.obtenerProveedores(getShowResultToView())); ;
			/** Buscamos el centro educativo */
			setShowResultToView(super. searchCentroEducativo(getShowResultToView()));
			/** Buscamos el beneficiario */
			setShowResultToView(super.buscarBeneficiarios(getShowResultToView(),
					null, Constantes.CONSULTAR));
			
			 
			
			
			
			
			super.getShowResultToView().setViene("1");
			/**Carga data del trabajador*/
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//disabled
		getShowResultToView().setDisabled(Constantes.DISABLED);
		return getShowResultToView();
	}
	
	
	
 

	 
 

}
