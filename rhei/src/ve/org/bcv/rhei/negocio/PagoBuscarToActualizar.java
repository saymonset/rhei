package ve.org.bcv.rhei.negocio;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.util.Constantes;

/**
 * Clase para actualizar pagos
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com
 * 17/03/2015 18:59:38
 * 
 */
public class PagoBuscarToActualizar extends SolAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(PagoBuscarToActualizar.class
			.getName());

	public PagoBuscarToActualizar(HttpServletRequest request,
			HttpServletResponse response, ShowResultToView showResultToView,
			ServletContext sc) throws ServletException, IOException {
		super(request, response, showResultToView, sc,
				Constantes.PAGOACTUALIZAR);
	}
	 



	/**
	 * 
	 */

	 
	/* 
	 * Nos vamos a la vista actualizar con todo los datos llenos
	 * (non-Javadoc)
	 * @see ve.org.bcv.rhei.controlador.SolAction#ejecutar()
	 */
	public ShowResultToView ejecutar() throws ServletException, IOException {
		
		
		SolAction solAction = new PagoBuscarDataConsultar(super.getRequest(), super.getResponse(),
				super.getShowResultToView(), super.getSc());
		solAction.ejecutar();
		
 
	
		
		/**Fin Buscamos el nombre del mes a traves del enum*/
		super.getShowResultToView().setAccion(Constantes.PAGOACTUALIZAR);
		super.getShowResultToView().setDisabled(Constantes.DISABLED);
		super.getShowResultToView().setDisabledRegPagos(Constantes.DISABLED);
		super.getShowResultToView().setDisabledActualizarPagos(Constantes.DISABLED);
		 
		return super.getShowResultToView();
	}

}
