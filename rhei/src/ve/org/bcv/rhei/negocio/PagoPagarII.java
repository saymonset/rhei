package ve.org.bcv.rhei.negocio;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.bcv.dao.jdbc.FacturaDao;
import com.bcv.dao.jdbc.PeriodoEscolarDao;
import com.bcv.dao.jdbc.RelacionDePagosDao;
import com.bcv.dao.jdbc.SolicitudDao;
import com.bcv.dao.jdbc.impl.FacturaDaoImpl;
import com.bcv.dao.jdbc.impl.PeriodoEscolarDaoImpl;
import com.bcv.dao.jdbc.impl.RelacionDePagosDaoImpl;
import com.bcv.dao.jdbc.impl.SolicitudDaoImpl;
import com.bcv.model.Factura;
import com.bcv.model.PeriodoEscolar;
import com.bcv.model.RelacionDePagos;
import com.bcv.model.Solicitud;

import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.util.Constantes;

/**
 * Pagamos y guardamos la factura
 * 
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com 12/03/2015 18:00:48
 * 
 */
public class PagoPagarII extends SolAction implements Serializable {
	private FacturaDao facturaDao = new FacturaDaoImpl();
	private RelacionDePagosDao relacionDePagosDao = new RelacionDePagosDaoImpl();
	private PeriodoEscolarDao periodoEscolarDao = new PeriodoEscolarDaoImpl();
	private SolicitudDao solicitudDao = new SolicitudDaoImpl();

	private static Logger log = Logger.getLogger(PagoPagarII.class.getName());

	public PagoPagarII(HttpServletRequest request,
			HttpServletResponse response, ShowResultToView showResultToView,
			ServletContext sc) throws ServletException, IOException {
		super(request, response, showResultToView, sc,
				Constantes.PAGOCONVENCIONAL);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ShowResultToView ejecutar() throws ServletException, IOException {
		try {
			/** Cargamos la solicitud en getShowResultToView() */

			Solicitud solicitud = solicitudDao.BscarSolConCodEmpCodBenfNrif(
					getShowResultToView().getCodEmp(), getShowResultToView()
							.getCodigoBenef(), getShowResultToView()
							.getNroRifCentroEdu());
			boolean isActiva = false;
			if (!StringUtils.isEmpty(solicitud.getCo_status())
					&& !"D".equalsIgnoreCase(solicitud.getCo_status())) {
				isActiva = true;
			}

			if (!isActiva) {
				getShowResultToView().setMensaje("fracaso1");
				getShowResultToView().setViene(null);
			} else {
				/** Validamos si existe una factura igual */
				/*** Pagamos */
				pagoConvencionalPagar(getShowResultToView().getCodEmp(),
						getShowResultToView().getCodigoBenef(), getRequest(),
						getShowResultToView());

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getShowResultToView().setAccion(Constantes.PAGOCONVENCIONAL);

		getShowResultToView().setDisabled(Constantes.DISABLED);
		return getShowResultToView();
	}

	/**
	 * @param codEmplado
	 * @param codigoBenef
	 * @param request
	 * @param showResultToView
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public ShowResultToView pagoConvencionalPagar(String codEmplado,
			String codigoBenef, HttpServletRequest request,
			ShowResultToView showResultToView) throws ServletException,
			IOException {

		int ced = 0;
		int codBenef = 0;
		String mensaje = "";
		String txtcomplemento = "";
		String txtobservacionespagado = "";
		Double montocomplemento = 0d;
		Factura factura = new Factura();
		RelacionDePagos pago = new RelacionDePagos();
		
		HttpSession session = request.getSession(false);
		String nombreUsuario=session.getAttribute("nombreUsuario")!=null?(String)session.getAttribute("nombreUsuario"):null;
		String isPagado = "";

		try {

			String showCheckCompklement = request
					.getParameter("showCheckCompklement") != null
					? (String) request.getParameter("showCheckCompklement")
					: null;
			String[] mesescomplementos = request
					.getParameterValues("mesecomplemento");

			isPagado = request.getParameter("pagado") == null
					? ""
					: (String) request.getParameter("pagado");

			txtobservacionespagado = request
					.getParameter("txtobservacionespagado") != null
					? (String) request.getParameter("txtobservacionespagado")
					: "";

			txtcomplemento = request.getParameter("txtcomplementoTxtArea") != null
					? (String) request.getParameter("txtcomplementoTxtArea")
					: "";
			try {
				montocomplemento = Double.valueOf(Double.parseDouble(request
						.getParameter("montocomplemento").toString()
						.replace(".", "").replace(",", ".")));
			} catch (Exception e) {
				montocomplemento = 0d;
			}

			factura.setTxtcomplemento(txtcomplemento.trim());
			factura.setMontocomplemento(montocomplemento);

			if (!StringUtils.isEmpty(showResultToView.getCedula())) {
				ced = new Integer(showResultToView.getCedula());
			}
			if (!StringUtils.isEmpty(showResultToView.getCodigoBenef())) {
				codBenef = new Integer(showResultToView.getCodigoBenef());
			}

			/** Fecha Factura */
			factura.setFechaFactura(request.getParameter("fechaFactura"));

			/** nroRif */
			factura.setNroRifProv(request.getParameter("nroRif"));

			/** monto factura */
			factura.setMontoFactura(Double.valueOf(Double.parseDouble(request
					.getParameter("montoFactura").toString().replace(".", "")
					.replace(",", "."))));

			pago.setNroSolicitud(Integer.parseInt(request
					.getParameter("nroSolicitud")));

			/** Nro Factura */
			factura.setNroFactura(request.getParameter("nroFactura"));
			if (StringUtils.isEmpty(factura.getNroFactura())) {
				factura.setNroFactura(Constantes.FACTURA + "-"
						+ pago.getNroSolicitud());
			}
			/** nro de control */
			factura.setNroControl(request.getParameter("nroControl"));
			if (StringUtils.isEmpty(factura.getNroControl())) {
				factura.setNroControl(Constantes.CONTROL + "-"
						+ pago.getNroSolicitud());
			}

			/** conceptoPago es mes o matricula **/
			/** Estamos pagando los meses ------------ */
			List<Integer> conceptoPagoArray = new ArrayList<Integer>();
			String[] mesesPorPagar = null;

			// isPagado
			List<Integer> mesMatriMenArray = new ArrayList<Integer>();
			try {

				mesesPorPagar = request.getParameterValues("mesesPorPagar");
				if (mesesPorPagar != null) {
					boolean isnulo = true;
					for (int j = 0; j < mesesPorPagar.length; j++) {
						if (StringUtils.isNumeric(mesesPorPagar[j])) {
							isnulo = false;
							break;
						}
					}
					if (isnulo) {
						mesesPorPagar = null;
					}
				}

			} catch (Exception e) {
				// TODO: handle exception
			}

			/** Guardamos meses de los complementos ys us montos individuales */
			Map<String, Double> mesesPorComplIndividualMonto = new HashMap<String, Double>();
			boolean montComplementoTraeValor=false;
			 
			if (mesesPorPagar == null || mesesPorPagar.length == 0) {
				if (mesescomplementos != null) {
					List<String> mesesComplementoAuxiliar = new ArrayList<String>();
					/** Aqui analizaremos todos lso meses complementos */
					Enumeration<String> parameterNames = request
							.getParameterNames();
					while (parameterNames.hasMoreElements()) {
						String paramName = parameterNames.nextElement();
						if (super.obtenerIdMes(paramName) > 0) {
							String[] paramValues = request
									.getParameterValues(paramName);
							for (int i = 0; i < paramValues.length; i++) {
								String paramValue = paramValues[i];
								if (!StringUtils.isEmpty(paramValue)) {
									Double montIndividual = Double
											.valueOf(Double
													.parseDouble(paramValue
															.toString()
															.replace(".", "")
															.replace(",", ".")));
                                   if (montIndividual!=null && montIndividual!=0){
                                	   montComplementoTraeValor=true;
                                	   mesesPorComplIndividualMonto.put(
    											super.obtenerIdMes(paramName) + "",
    											montIndividual);
                                	   mesesComplementoAuxiliar.add(super.obtenerIdMes(paramName) + "");
                                		//**Borrar, solo imprimir valor para test*//
    									Double valor = mesesPorComplIndividualMonto
    											.get(Math.abs(super
    													.obtenerIdMes(paramName))
    													+ "");
                                		//**Fin Borrar, solo imprimir valor para test*//
                                   }							 
								
								}

							}
						}
					}
					mesescomplementos= new String[mesesComplementoAuxiliar.size()];
					mesescomplementos = mesesComplementoAuxiliar.toArray(mesescomplementos);
					mesesPorPagar = mesescomplementos;
				}
			}
			if (mesesPorPagar != null) {

				int valorMatricOrMes = 0;
				boolean isPrimeraVez = false;
				for (int j = 0; j < mesesPorPagar.length; j++) {
					valorMatricOrMes = Integer.parseInt(mesesPorPagar[j]);
					mesMatriMenArray.add(valorMatricOrMes);
					/** conceptoPago Matricula */
					/**
					 * matricula pagada es negativo por ser ya pagada, se
					 * multiplica por menos uno para que de positivo la
					 * matricula pagada
					 */
					if (valorMatricOrMes == (Constantes.MATRICULA_PAGADA * -1)) {
						/** Concepto de pago para matricula es cero */
						conceptoPagoArray.add(0);
						/** conceptoPago Mensualidad */
					} else if ((valorMatricOrMes > 0 && (valorMatricOrMes != (Constantes.MATRICULA_PAGADA * -1)))
							&& !isPrimeraVez) {
						isPrimeraVez = true;
						conceptoPagoArray.add(1);
					}
				}
				pago.setMesMatriMenArray(mesMatriMenArray);

			}

			/**
			 * Solo un concepto de pago ha sido seleccionados.. periodo o
			 * matricula, colocamos 0, no sabemos que valor tiene o es 0 o es 1
			 */
			pago.setConceptoPagoArray(conceptoPagoArray);

			/** FIN Estamos pagando los meses ------------ */

			String tramite = request.getParameter("estatusPago") != null
					? (String) request.getParameter("estatusPago")
					: "";
			if (Constantes.T.equalsIgnoreCase(tramite)) {
				pago.setTramite(Constantes.T);
			} else {
				pago.setTramite(Constantes.P);
			}

			log.info("request.getParameter(\"fechaRegistro\")="
					+ request.getParameter("fechaRegistro"));
			SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
			java.util.Date dateObject = dateFormatter.parse(request
					.getParameter("fechaRegistro"));
			pago.setFechaRegistroDePago(new java.sql.Timestamp(dateObject
					.getTime()));

			PeriodoEscolar periodoEscolar = new PeriodoEscolar();
			periodoEscolar.setDescripcion(request
					.getParameter("periodoEscolar"));
			if (StringUtils.isEmpty(periodoEscolar.getDescripcion())) {
				periodoEscolar.setDescripcion(request
						.getParameter("periodoEscolarAux"));
			}
			try {
				periodoEscolar = periodoEscolarDao
						.findPeriodoByDescripcion(periodoEscolar
								.getDescripcion());
				if (periodoEscolar != null) {
					pago.setCodigoPeriodo(Integer.parseInt(periodoEscolar
							.getCodigoPeriodo().toString()));
				}
			} catch (SQLException e) {
				pago.setCodigoPeriodo(0);
			}

			// formaPago
			pago.setCoFormaPago(request.getParameter("formaPago") != null
					? (String) request.getParameter("formaPago")
					: "");

			/** Pago dirigido a centro de educacion inicial o trabajador */
			pago.setReceptorPago(Integer.parseInt(request.getParameter(
					"receptorPago").toString()));

			Double montoMatriculaBCV = Double.valueOf(0.0D);
			Double montoPeriodoBCV = Double.valueOf(0.0D);

			String observaciones = "";
			if (!StringUtils.isEmpty(txtobservacionespagado)) {
				observaciones = txtobservacionespagado;
			}

			pago.setMontoBCV(Double.valueOf(Double.parseDouble(request
					.getParameter("mo_bcv"))));
			pago.setMontoMatricula(Double.valueOf(Double.parseDouble(request
					.getParameter("mo_matricula"))));
			pago.setMontoPeriodo(Double.valueOf(Double.parseDouble(request
					.getParameter("mo_periodo"))));
			pago.setBeneficioCompartido(request
					.getParameter("beneficioCompartido"));

			montoMatriculaBCV = relacionDePagosDao.calcularMontoPagoBCV(
					pago.getMontoBCV(), pago.getBeneficioCompartido(),
					pago.getMontoMatricula());
			montoPeriodoBCV = relacionDePagosDao.calcularMontoPagoBCV(
					pago.getMontoBCV(), pago.getBeneficioCompartido(),
					pago.getMontoPeriodo());

			int mes = -1;
			mes = Integer.parseInt(pago.getFechaRegistroDePago().toString()
					.substring(5, 7));

			/** En este caso estamos evaluandoque si es verdadera */
			/** GUARDAMOS PAGO */
			int tipoPagoConvencional = 1;
			String isComplemento="";
			/**Verificamos que sea un complemento*/
			if (!StringUtils.isEmpty(showCheckCompklement)) {
				isComplemento = "S";
			}
			/**Verificamos que sea un complemento y traega valor, alñguno de sus meses*/
			/**En tal caso que no trtaiga valor alguno de sus meses.. y es complemento, creamos umn error**/
			if ("S".equalsIgnoreCase(isComplemento)&&!montComplementoTraeValor) {
				mensaje = "errorComplemento";
				showResultToView.setMensaje(mensaje);
			}else{
				/** conceptoPago es mes o matricula **/
				/**
				 * ReceptorPago = Pago dirigido a centro de educacion inicial o
				 * trabajador
				 */
				mensaje = relacionDePagosDao.guardarPago(factura,pago.getMontoBCV(),
						montoMatriculaBCV, montoPeriodoBCV, tipoPagoConvencional,
						observaciones, pago.getMesMatriMenArray(),
						pago.getNroSolicitud(), pago.getConceptoPagoArray(),
						pago.getReceptorPago(), pago.getTramite(),
						pago.getCodigoPeriodo(), pago.getCoFormaPago(), isPagado,
						showCheckCompklement, mesescomplementos,
						mesesPorComplIndividualMonto,nombreUsuario);
				if (mensaje.equals("Exito")) {
					mensaje = "exito";
					showResultToView.setMensaje(mensaje);
				} else {
					mensaje = "error";
					showResultToView.setMensaje(mensaje);
				}
			}
			
		

		} catch (Exception e) {
			log.error(e.toString());;
		} finally {

		}

		return showResultToView;
	}

}