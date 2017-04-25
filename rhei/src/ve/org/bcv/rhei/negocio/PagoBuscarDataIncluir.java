package ve.org.bcv.rhei.negocio;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.util.Constantes;

import com.bcv.dao.jdbc.RelacionDePagosDao;
import com.bcv.dao.jdbc.SolicitudDao;
import com.bcv.dao.jdbc.impl.RelacionDePagosDaoImpl;
import com.bcv.dao.jdbc.impl.SolicitudDaoImpl;
import com.bcv.model.Solicitud;

/**
 * Registramos el pago convencional
 * 
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com 02/03/2015 19:44:12
 * 
 */
public class PagoBuscarDataIncluir extends SolAction implements Serializable {
	private static Logger log = Logger.getLogger(PagoBuscarDataIncluir.class
			.getName());
	private RelacionDePagosDao relacionDePagosDao= new RelacionDePagosDaoImpl();
	private SolicitudDao solicitudDao= new SolicitudDaoImpl();
	public PagoBuscarDataIncluir(HttpServletRequest request,
			HttpServletResponse response, ShowResultToView showResultToView,
			ServletContext sc) throws ServletException, IOException {
		super(request, response, showResultToView, sc,
				Constantes.PAGOCONVENCIONAL);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see ve.org.bcv.rhei.controlador.SolAction#ejecutar()
	 */
 
	public ShowResultToView ejecutar() throws ServletException, IOException {
		try {
			
			/**Nuevamente cargamos las variables iniciales y otrs que no c pudieron cargar la primera vez*/
			cargaTrabajadorBeneficiarioCentroEdu(super.getRequest(),
					 super.getResponse(), super.getShowResultToView(),super.getSc(),Constantes.PAGOCONVENCIONAL);
			/** Cargamos la solicitud */
			/**Carga solicitud I*/
			Solicitud	solicitud=solicitudDao.BscarSolConCodEmpCodBenfNrif(getShowResultToView().getCodEmp(),
					getShowResultToView().getCodigoBenef(), getShowResultToView().getNroRifCentroEdu());
			
			/**Si no esta activa la solicitud*/
			boolean isActiva=false;
			if ( !StringUtils.isEmpty(solicitud.getCo_status())
					&& !"D".equalsIgnoreCase(solicitud.getCo_status())) {
				isActiva = true;
			}
			
			if (solicitud.getNroSolicitud()==0  ) {
				ShowResultToView showNroSolicitud = new ShowResultToView();
		    	showNroSolicitud.setCodEmp(getShowResultToView().getCodEmp());
		    	showNroSolicitud.setCodigoBenef(getShowResultToView().getCodigoBenef());
		     
		    	/**Realizamos nuevamente la consulta pero con el codigo del empleado y beneficiario, excluyendo al colegio*/
		     	/**Esto con el fin de notificar el ultimo numero de solicitud existente y orientrlos hacer una busqueda exacta*/
		    	solicitud= solicitudDao.BscarSolConCodEmpCodBenf(getShowResultToView().getCodEmp(),
		    			getShowResultToView().getCodigoBenef());
		    	getShowResultToView().setNroSolicitud(solicitud.getNroSolicitud());
				
				
				getShowResultToView().setMensaje(
						"fracaso1");
				
				/**Si no hay data en ese colegio o nunca se ha hecho una solicitud con ese colegio*/
			} else if (!isActiva) {
				
				getShowResultToView().setMensaje(
						"fracaso");
				getShowResultToView().setViene(null);
				
			} else {
				
				/** Si no hay data en la bd, inicializamos valores por defecto */
				if (StringUtils.isEmpty(getShowResultToView().getFechaRegistro())) {
					getShowResultToView()
							.setFechaRegistro(getShowResultToView().getFechaActual());
				}
				if (StringUtils.isEmpty(getShowResultToView().getNroFactura())) {
					getShowResultToView().setNroFactura(Constantes.PENDIENTE_ENTREGAF);
				}
				if (StringUtils.isEmpty(getShowResultToView().getNroControl())) {
					getShowResultToView().setNroControl(Constantes.PENDIENTE_ENTREGAC);
				}
				
				/**cargaMesesMatriculaConceptoDePago AQUI CARGAMOS ENTREOTRAS COSAS LOS MESES A PAGAR*/
				/**Carga Relacion de pago y Factura II*/
				
				super.cargaMesesMatriculaConceptoDePago( getRequest(),
						getShowResultToView(),solicitud.getNroSolicitud(),getShowResultToView().getPeriodoEscolar());
				
				getShowResultToView().setNroSolicitud(solicitud.getNroSolicitud());
				/**--------------Obtenemos Relacion Pagos----------------------------*/
				relacionDePagosDao.obtenerRelacionPagoYfactura(
						solicitud.getNroSolicitud(),getShowResultToView().getNroIdFactura(),
						getShowResultToView().getPeriodoEscolar(), getShowResultToView());
				 
				/**Enc--------------Fin Obtenemos Relacion Pagos----------------------------*/
			
			
				/**Nuevamente cargamos las variables iniciales y otrs que no c pudieron cargar la primera vez*/
				cargaTrabajadorBeneficiarioCentroEdu(super.getRequest(),
						 super.getResponse(), super.getShowResultToView(),super.getSc(),Constantes.PAGOCONVENCIONAL);
				
				
		
				

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**Como vamos a incluir,  el monto total va a ser cero, sera calculado por la pantalla pagoConvencionalReg*/
		SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
		getShowResultToView().setEstatusPago(Constantes.TRAMITADO);
		String fechaRegistro = formato.format(new Date());
		getShowResultToView().setFechaRegistro(fechaRegistro);
		getShowResultToView().setFechaFactura(fechaRegistro);
		getShowResultToView().setMontoTotal(0);
		getShowResultToView().setAccion(Constantes.PAGOCONVENCIONAL);
		getShowResultToView().setDisabled(Constantes.DISABLED);
		/** Vamos a pagar, estos campos los necesitamos libres */
		getShowResultToView().setNroFactura(Constantes.PENDIENTE_ENTREGAF+getShowResultToView().getNroSolicitud());
		getShowResultToView().setNroControl(Constantes.PENDIENTE_ENTREGAC+getShowResultToView().getNroSolicitud());
		getShowResultToView().setMontoFactura(0.0);
		getShowResultToView().setReceptorPago("");
		getShowResultToView().getNombreCentro();

		/** Fin Vamos a pagar, estos campos los necesitamos libres */
		return getShowResultToView();
	}

	

}