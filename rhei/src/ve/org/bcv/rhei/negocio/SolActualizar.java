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

import com.bcv.dao.jdbc.ConyugeTrabajoDao;
import com.bcv.dao.jdbc.PeriodoEscolarDao;
import com.bcv.dao.jdbc.SolicitudDao;
import com.bcv.dao.jdbc.impl.ConyugeTrabajoDaoImpl;
import com.bcv.dao.jdbc.impl.PeriodoEscolarDaoImpl;
import com.bcv.dao.jdbc.impl.SolicitudDaoImpl;
import com.bcv.model.ConyugeTrabajo;
import com.bcv.model.PeriodoEscolar;
import com.bcv.model.Solicitud;

/**
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com
 * 16/04/2015 15:35:59
 * 
 */
public class SolActualizar extends SolAction implements Serializable {
	private static Logger log = Logger.getLogger(SolActualizar.class
			.getName());
	private SolicitudDao solicitudDao= new SolicitudDaoImpl();
	private PeriodoEscolarDao periodoEscolarDao = new PeriodoEscolarDaoImpl();
	private ConyugeTrabajoDao conyugeTrabajoDao = new ConyugeTrabajoDaoImpl();

	public SolActualizar(HttpServletRequest request,
			HttpServletResponse response, ShowResultToView showResultToView,
			ServletContext sc) throws ServletException, IOException {
		super(request, response, showResultToView, sc,Constantes.ACTUALIZAR);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ShowResultToView ejecutar() throws ServletException, IOException {
		cargaTrabajadorBeneficiarioCentroEdu(super.getRequest(), super.getResponse(), super.getShowResultToView(), super.getSc(),
				Constantes.ACTUALIZAR);
		actualizarSolicitud(getShowResultToView().getCodEmp(),
				getShowResultToView().getCodigoBenef(), getRequest(),
				getResponse(), getShowResultToView());
		return getShowResultToView();
	}

	protected ShowResultToView actualizarSolicitud(String codEmp,
			String codigoBenef, HttpServletRequest request,
			HttpServletResponse response, ShowResultToView showResultToView)
			throws ServletException, IOException {
		String mensaje = "";
		Solicitud solicitud = new Solicitud();
		/** Begin First campo */
		log.debug("nro de solicitud: " + request.getParameter("numSolicitud"));
		solicitud.setNroSolicitud(Integer.parseInt(request
				.getParameter("numSolicitud")));
		log.debug("nro de solicitud: " + solicitud.getNroSolicitud());

		
		/** Consultamos solicitud inicio pra actualizar*/
		try {
			solicitud=solicitudDao.consultarSolicitud(solicitud.getNroSolicitud());
			if (solicitud==null) {
				mensaje = "fracaso2";
				showResultToView.setMensaje(mensaje);
				return showResultToView;
			}

			if (!StringUtils.isEmpty(request.getParameter("formaPago"))) {
				solicitud.setFormaDePago(request.getParameter("formaPago"));
			}
			if (!StringUtils.isEmpty(request.getParameter("nroRif"))) {
				solicitud.setNroRifCentroEdu(request.getParameter("nroRif"));
			}
			if (!StringUtils.isEmpty(request.getParameter("tipoEducacion"))) {
				solicitud.setTipoEducacion(request
						.getParameter("tipoEducacion"));
			}
			if (!StringUtils.isEmpty(request.getParameter("tipoInstitucion"))) {
				solicitud.setTipoCentroEdu(request
						.getParameter("tipoInstitucion"));
			}
			if (!StringUtils.isEmpty(request.getParameter("periodoPago"))) {
				solicitud.setPeriodoDePago(request.getParameter("periodoPago"));
			}
               /**ACTUALIZAMOS LA SOLICITUD*/
//			
			
			    mensaje = solicitudDao.updateCambiosEnSolicitudBEI(solicitud.getFormaDePago(),solicitud.getNroRifCentroEdu(),
			    		solicitud.getTipoEducacion(),solicitud.getTipoCentroEdu(),solicitud.getPeriodoDePago(),solicitud.getNroSolicitud());
	           /**FIN ACTUALIZAMOS LA SOLICITUD*/
			if (Constantes.EXITO.equalsIgnoreCase(mensaje)) {
				/************** Insertamos en la tabla MOS_ST_SOLIC_BEI Solicitud INI ********************************/
				/** Begin 2 campo */
				log.debug("estatus solicitud: "
						+ request.getParameter("co_status"));
				solicitud.setCo_status(request.getParameter("co_status"));
				log.debug("estatus solicitud: " + solicitud.getCo_status());

				String tramite = "";
				if ((tramite.equals("0"))
						&& (!solicitud.getCo_status().equals("R"))) {
					solicitud.setCo_status("C");
					log.debug("estatus solicitud: " + solicitud.getCo_status());
				} else if ((tramite.equals("0"))
						&& (solicitud.getCo_status().equals("R"))) {
					solicitud.setCo_status("R");
					log.debug("estatus solicitud: " + solicitud.getCo_status());
				} else if (tramite.equals("1")) {
					solicitud.setCo_status("R");
					log.debug("estatus solicitud: " + solicitud.getCo_status());
				}

				/** Begin 3er campo */
				/**
				 * Del select html, si es disabled, traera nullel
				 * nivelEscolaridad
				 */
				log.debug("nivel Escolar: "
						+ request.getParameter("nivelEscolaridad"));
				String nivelEscolaridad = request
						.getParameter("nivelEscolaridad") == null ? ""
						: (String) request.getParameter("nivelEscolaridad");
				/**
				 * Auxiliar en caso que traiga null por estar deshabilitado el
				 * select html de nivelEscolaridad
				 */
				if (StringUtils.isEmpty(nivelEscolaridad)) {
					nivelEscolaridad = request.getParameter("nivelEscolar") == null ? ""
							: (String) request.getParameter("nivelEscolar");
				}
				log.debug("nivelEscolar="
						+ request.getParameter("nivelEscolar"));
				solicitud.setNivelEscolar(nivelEscolaridad);
 
				
				if (StringUtils.isEmpty(solicitud.getNivelEscolar())){
					log.debug("nivelEscolaridadH="
							+ request.getParameter("nivelEscolaridadH"));
					nivelEscolaridad = request
							.getParameter("nivelEscolaridadH") == null ? ""
							: (String) request.getParameter("nivelEscolaridadH");

					solicitud.setNivelEscolar(nivelEscolaridad);
				}

				/** 4 parameter */
				log.debug("beneficio compartido: "
						+ request.getParameter("benefCompartido"));
				solicitud.setComparteBeneficio(request
						.getParameter("benefCompartido"));
				log.debug("beneficio compartido: "
						+ solicitud.getComparteBeneficio());
				/** 5 parametro */
				if (request.getParameter("tipoEmpresa") != null) {
					solicitud.setTipoEmpresa(request
							.getParameter("tipoEmpresa"));
					log.debug("tipo empresa: " + solicitud.getTipoEmpresa());
				} else {
					solicitud.setTipoEmpresa("");
					log.debug("tipo empresa: " + solicitud.getTipoEmpresa());
				}
				
				solicitud.setMontoEmpresa(Double.valueOf(0.0D));
				if (request.getParameter("montoAporteEmp") != null) {
					try {
						solicitud.setMontoEmpresa(Double.valueOf(Double
								.parseDouble(request.getParameter("montoAporteEmp")
										.toString().replace(".", "")
										.replace(",", "."))));						
					} catch (Exception e) {
						// TODO: handle exception
					}
				}  
				
				/** 6 parametro */
				solicitud.setMontoAporteBCV(Double.valueOf(0.0D));
				if (request.getParameter("montoBCV")!=null){
					try {
						solicitud
						.setMontoAporteBCV(Double.valueOf(Double
								.parseDouble(request.getParameter("montoBCV")
										.toString().replace(".", "")
										.replace(",", "."))));
					} catch (Exception e) {
						// TODO: handle exception
					}
					
				}

			 
				/** 7 parametro */
				solicitud.setMontoPeriodo(Double.valueOf(0.0D));
				if(request
						.getParameter("montoPeriodo")!=null){
					try {
						solicitud.setMontoPeriodo(Double.valueOf(Double
								.parseDouble(request
										.getParameter("montoPeriodo")
										.toString().replace(".", "")
										.replace(",", "."))));	
					} catch (Exception e) {
						// TODO: handle exception
					}
					
				}
				solicitud.setMontoMatricula(Double.valueOf(0.0D));
				if (request
						.getParameter("montoMatricula")!=null){
					try {
						solicitud
						.setMontoMatricula(Double.valueOf(Double
								.parseDouble(request
										.getParameter("montoMatricula")
										.toString().replace(".", "")
										.replace(",", "."))));	
					} catch (Exception e) {
						// TODO: handle exception
					}
					
				}
				/** 9 parametro */
			
				/** 10 parametro esta interno */
				/** 11tmo parametro */
 
				
				PeriodoEscolar periodoEscolar = new PeriodoEscolar();
				periodoEscolar.setDescripcion(request.getParameter("periodoEscolar"));
				try {
					periodoEscolar = periodoEscolarDao.findPeriodoByDescripcion(periodoEscolar.getDescripcion());
					if (periodoEscolar!=null){
						solicitud.setCodigoPeriodo( Integer.parseInt(periodoEscolar.getCodigoPeriodo().toString()));
					}
				} catch (SQLException e) {
					solicitud.setCodigoPeriodo(0);
				}
				
				

				log.debug("codigoPeriodo: " + solicitud.getCodigoPeriodo());

				/**INICIO ACTUALIZAMOS SOLICITUD*/
				//mensaje = solicitud.guardarCambiosEnMovStSolicBei();
				mensaje = solicitudDao.guardarCambiosEnMovStSolicBei(
						solicitud.getCo_status(),solicitud.getNivelEscolar(),solicitud.getMontoPeriodo(),solicitud.getMontoMatricula(),
						solicitud.getCodigoPeriodo(),solicitud.getComparteBeneficio(),solicitud.getTipoEmpresa(),solicitud.getMontoEmpresa(),solicitud.getNroSolicitud());
				
				
				String nombreempresa=request.getParameter("nombreempresa");
				String telefonoempresa=request.getParameter("telefonoempresa");
				String ciConyuge=request.getParameter("ciConyugehidden");
				String correoConyuge=request.getParameter("correoConyuge");
				String tlfconyuge=request.getParameter("tlfconyuge");
				ConyugeTrabajo conyugeTrabajo = new ConyugeTrabajo();
				int ced=0;
				if (!StringUtils.isEmpty(ciConyuge) &&StringUtils.isNumeric(ciConyuge)){
	                 ced=Integer.parseInt(ciConyuge.trim());				
				}
				conyugeTrabajo.setCiConyuge(ced);
				conyugeTrabajo.setNombreEmpresa(nombreempresa);
				conyugeTrabajo.setTelefonoEmpresa(telefonoempresa);
				conyugeTrabajo.setCorreoConyuge(correoConyuge);
				conyugeTrabajo.setNuTlfTrabajo(tlfconyuge);;
				conyugeTrabajoDao.guardar(conyugeTrabajo.getCiConyuge(),conyugeTrabajo.getNombreEmpresa(),conyugeTrabajo.getTelefonoEmpresa(),conyugeTrabajo.getCorreoConyuge(),conyugeTrabajo.getNuTlfTrabajo());
				/** guradamos solicitud **/
				
				
				/**FIN ACTUALIZAMOS SOLICITUD*/
			}
			/************** Insertamos en la tabla MOS_ST_SOLIC_BEI Solicitud FIN ********************************/
			if (mensaje.equals("errorUpdateCambiosEnSolicitudBEI")) {
				mensaje = "fracaso2";
			} else {
				mensaje = "exito";
			}
			showResultToView.setMensaje(mensaje);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return showResultToView;

	}

}