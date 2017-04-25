package ve.org.bcv.rhei.negocio;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
 * 16/04/2015 15:05:22
 * 
 */
public class SolIncluir extends SolAction implements Serializable {
	private SolicitudDao solicitudDao= new SolicitudDaoImpl();
	private PeriodoEscolarDao periodoEscolarDao = new PeriodoEscolarDaoImpl();
	private ConyugeTrabajoDao conyugeTrabajoDao = new ConyugeTrabajoDaoImpl();
	private static Log log = LogFactory.getLog(SolIncluir.class);
	
	private HttpServletRequest request;
	public SolIncluir(HttpServletRequest request, HttpServletResponse response,
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
		
		
		
		cargaTrabajadorBeneficiarioCentroEdu(super.getRequest(), super.getResponse(), super.getShowResultToView(), super.getSc(),
				Constantes.INCLUIR);
		guardarSolicitud(getShowResultToView().getCodEmp(),  getShowResultToView().getCodigoBenef(),getRequest(), getResponse(),getShowResultToView());
		
	
		
		
		return getShowResultToView();
	}

	protected ShowResultToView guardarSolicitud(String codEmp,
			String codigoBenef, HttpServletRequest request,
			HttpServletResponse response, ShowResultToView showResultToView)
			throws ServletException, IOException {
		String mensaje="";
		ShowResultToView showCodEmp= new ShowResultToView();
		ConyugeTrabajo conyugeTrabajo = new ConyugeTrabajo();
		showCodEmp.setCodEmp(codEmp);
		showCodEmp.setCodigoBenef(codigoBenef);
		if (!activaSolicitud(showCodEmp)) {
			Solicitud solicitud = new Solicitud();
			String cedEmp = "";
			String siglaExpediente = "";
			String tipoNomina = request.getParameter("tipoNomina");

			solicitud.setTipoCentroEdu(request.getParameter("tipoInstitucion"));
			solicitud.setTipoEducacion(request.getParameter("tipoEducacion"));
			solicitud.setPeriodoDePago(request.getParameter("periodoPago"));
			solicitud.setFormaDePago(request.getParameter("formaPago"));

			log.info(request.getParameter("codEmp"));
			log.info(request.getParameter("cedBenef"));
			if (StringUtils.isNumeric(request
					.getParameter("codEmp"))){
				solicitud.setCodigoEmpleado(Integer.parseInt(request
						.getParameter("codEmp")));
			}


			solicitud.setCedulaFamiliar(Integer.parseInt(request
					.getParameter("cedBenef")));

			solicitud.setNroRifCentroEdu(request
					.getParameter("nroRifCentroEdu"));

			/**por default colocamos C de caracas*/
			solicitud.setLocalidadCEI("C");
			if (request.getParameter("localidad")
					.compareToIgnoreCase("Caracas") == 0) {
				solicitud.setLocalidadCEI("C");
			} else if (request.getParameter("localidad").compareToIgnoreCase(
					"Maracaibo") == 0) {
				solicitud.setLocalidadCEI("M");
			} else if (request.getParameter("localidad").compareToIgnoreCase(
					"Maracay") == 0) {
				solicitud.setLocalidadCEI("Y");
			}

			solicitud.setNivelEscolar(request.getParameter("nivelEscolaridad"));

			cedEmp = request.getParameter("cedEmp");

			tipoNomina = request.getParameter("tipoNomina");

			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

			try {

				Date myDate = format.parse(request.getParameter("fechaActual")
						.toString());
				solicitud.setFechaSolicitud(new Timestamp(myDate.getTime()));
			} catch (Exception e) {
				java.util.Date date = new java.util.Date();
				solicitud.setFechaSolicitud(new Timestamp(date.getTime()));

			}

			solicitud.setMontoPeriodo(0d);

			solicitud.setMontoMatricula(0d);

			solicitud.setMontoAporteBCV(Double.valueOf(Double
					.parseDouble(request.getParameter("montoBCV").toString()
							.replace(".", "").replace(",", "."))));
			
			
			//conyugeTrabajoDao.guardar(nuSolicitud, nombreEmpresa, telefonoEmpresa, nombreConyuge, apellidoConyuge, ciConyuge, tlfConyuge);
			
            
			solicitud.setComparteBeneficio(request
					.getParameter("benefCompartido"));
			
			/**1 privada, 0 publica*/
			String privada="1";
			solicitud.setTipoEmpresa(privada);
			
			
//			ciConyuge
//			tlfconyuge
//			correoConyuge
//			nombreempresa
//			telefonoempresa
//			montoAporteEmp
			if (request.getParameter("montoAporteEmp") != null) {
				solicitud
						.setMontoEmpresa(Double.valueOf(Double
								.parseDouble(request
										.getParameter("montoAporteEmp")
										.toString().replace(".", "")
										.replace(",", "."))));

			} else {
				solicitud.setMontoEmpresa(Double.valueOf(0.0D));

			}
			
  
		
			String nombreempresa=request.getParameter("nombreempresa");
			String telefonoempresa=request.getParameter("telefonoempresa");
			String ciConyuge=request.getParameter("ciConyugehidden");
			String correoConyuge=request.getParameter("correoConyuge");
			String tlfconyuge=request.getParameter("tlfconyuge");
			String montoAporteEmp=request.getParameter("montoAporteEmp");
			

			
			

			PeriodoEscolar periodoEscolar =null;
			try {
				periodoEscolar  =periodoEscolarDao.findPeriodoByDescripcionLast();;
				if (periodoEscolar!=null){
					solicitud.setCodigoPeriodo( Integer.parseInt(periodoEscolar.getCodigoPeriodo().toString()));
				}
			} catch (SQLException e) {
				solicitud.setCodigoPeriodo(0);
			}
			 
			solicitud.setCo_status("A");
			if (cedEmp.compareTo("") != 0) {
				siglaExpediente = "DBS-" + cedEmp;
			}
			
			
			conyugeTrabajo = new ConyugeTrabajo();
			int ced=0;
			if (!StringUtils.isEmpty(ciConyuge) &&StringUtils.isNumeric(ciConyuge)){
                 ced=Integer.parseInt(ciConyuge.trim());				
			}
			conyugeTrabajo.setCiConyuge(ced);
			conyugeTrabajo.setNombreEmpresa(nombreempresa);
			conyugeTrabajo.setTelefonoEmpresa(telefonoempresa);
			conyugeTrabajo.setCorreoConyuge(correoConyuge);
			conyugeTrabajo.setNuTlfTrabajo(tlfconyuge);;
			
			/** guradamos solicitud **/
			
			
			try {
				mensaje = solicitudDao.guardarSolicitud(siglaExpediente,
						tipoNomina, solicitud.getCodigoEmpleado(),solicitud.getCedulaFamiliar(),solicitud.getFormaDePago(),
						solicitud.getNroRifCentroEdu(),solicitud.getTipoEducacion(),solicitud.getTipoCentroEdu(),solicitud.getPeriodoDePago(),solicitud.getLocalidadCEI()
						,solicitud.getCo_status(),solicitud.getNivelEscolar(),solicitud.getComparteBeneficio(),solicitud.getTipoEmpresa(),solicitud.getMontoAporteBCV(),
						solicitud.getMontoPeriodo(),solicitud.getMontoMatricula(),solicitud.getMontoEmpresa(),solicitud.getCodigoPeriodo(),conyugeTrabajo);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (mensaje.equals("Error en inserciones")) {
				mensaje = "error";
			} else {
				mensaje = "exito";
			}
			showResultToView.setMensaje(mensaje);
		} else {
			mensaje = "fracaso";
			showResultToView.setMensaje(mensaje);
		}

		return showResultToView;

	}
}
