package ve.org.bcv.rhei.negocio;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ve.org.bcv.rhei.bean.ShowResultToView;
import ve.org.bcv.rhei.util.Constantes;

import com.bcv.dao.jdbc.PeriodoEscolarDao;
import com.bcv.dao.jdbc.SolicitudDao;
import com.bcv.dao.jdbc.impl.PeriodoEscolarDaoImpl;
import com.bcv.dao.jdbc.impl.SolicitudDaoImpl;
import com.bcv.model.PeriodoEscolar;

/**
 * Desincorporamos la solicitud
 * 
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com 25/02/2015 19:29:39
 * 
 */
public class SolDesinCorpPartII extends SolAction implements Serializable {
	private SolicitudDao solicitudDao = new SolicitudDaoImpl();
	
	private PeriodoEscolarDao periodoEscolarDao=new PeriodoEscolarDaoImpl();
	public SolDesinCorpPartII(HttpServletRequest request,
			HttpServletResponse response, ShowResultToView showResultToView,
			ServletContext sc) throws ServletException, IOException {
		super(request, response, showResultToView, sc, Constantes.DESINCORPORAR);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ShowResultToView ejecutar() throws ServletException, IOException {
		super.cargaTrabajadorBeneficiarioCentroEdu(super.getRequest(),
				super.getResponse(), super.getShowResultToView(),
				super.getSc(), Constantes.DESINCORPORAR);
		desincorporarSolicitud(getShowResultToView().getCodEmp(),
				getShowResultToView().getCodigoBenef(), getRequest(),
				getResponse(), getShowResultToView());
		return getShowResultToView();
	}

	public ShowResultToView ejecutarByAnioDesincorporar(
			String periodoEscolarValor) throws ServletException, IOException {
		super.cargaTrabajadorBeneficiarioCentroEdu(super.getRequest(),
				super.getResponse(), super.getShowResultToView(),
				super.getSc(), Constantes.DESINCORPORAR);
		String mensaje = "";
		try {
			PeriodoEscolar periodoEscolar = periodoEscolarDao
					.findPeriodoByDescripcion(periodoEscolarValor);
			if (periodoEscolar != null) {
				mensaje = solicitudDao
						.desincorporarSolicitudByAnio(periodoEscolar
								.getCodigoPeriodo());
				getShowResultToView().setMensaje("exito");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getShowResultToView();
	}

	protected ShowResultToView desincorporarSolicitud(String codEmp,
			String codigoBenef, HttpServletRequest request,
			HttpServletResponse response, ShowResultToView showResultToView)
			throws ServletException, IOException {
		String mensaje = "";
		String[] borrar = (String[]) null;
		borrar = request.getParameterValues("borrar");
		try {
			mensaje = solicitudDao.guardarCambiosEnSolicitud(borrar);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (mensaje.equals("error")) {
			showResultToView.setMensaje("fracaso2");
		} else {
			showResultToView.setMensaje("exito");
		}
		return showResultToView;

	}
}