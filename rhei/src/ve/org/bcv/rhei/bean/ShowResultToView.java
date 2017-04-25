package ve.org.bcv.rhei.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ve.org.bcv.rhei.report.by.benef.BeneficiarioBean2;

/**
 * @author Simon Alberto Rodriguez Pacheco
 * @mail oraclefedora@gmail.com
 * 13/02/2015 15:02:33
 * Clase que me permite llenar los datos de la vista con dicha informacion
 */
public class ShowResultToView implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String urlAccion;
	private String pagIrAfter;
	private int nuIdFactura;
	private int inMesMatricula;
	private  int coPeriodo;
	//TX_DESCRIP_PERIODO
	private String error;
	private String mensaje;
	private String mensajeExito;
	private String cedula;
	private String cedulaEmpleado;
	private String codigoEmpleado;
	private String apellido;
	private String nombre;
	private String fechaIngreso;
	private String fechaHasta;
	private String situacion;
	private String tipoEmpleado;
	private String ubicacion;
	private String cargo;
	private String compania;
	private String nroExpediente;
	private String tipoNomina;
	private String nivelEscolar;
	private String conceptoMesMatricula;
	private String conceptoPagoAdicional;
	private Double montoPagoAdicional;
	
	 private String tipoInstitucion;
	 private String tipoEducacion;
	 private String periodoPago;
	 private String formaPago;
	 private String cedBenef;
     private String nivelEscolaridad;
     private Double montoPeriodo;
     private Double montoMatricula;
     private String benefCompartido;
     private String tipoEmpresa="0";
     private Double montoAporteEmp;
     private String accion;
     private String co_status;
 	 private String nb_status;//DESINCORPORADO, ACTUALIZADO, RENOVADO ,DESINCORPORADO, ACTUALIZACION MASIVA
     private String co_statusCmplto;
     private String tlfNumExt;
     private String emailPropio;
     private String emailBcv;
     private String txtcomplemento;
     private Double montocomplemento;
     
	
	/**Benenficiario ini*/
	private String cedulaFamiliar;
	private String apellidoFamiliar;
	private String nombreFamiliar;
	private String fechaNacimiento;
	private String edad;
	private String estatus;
	/**Beneficiario fin*/
	/**Centro educativo**/
	private String nroRif;
	private String nombreCentro;
	private String localidadBCV;
	private String direccionCentro;
	private String tlfCentro;
	private String email;
	private String numSolicitud;
	/****/
	private int nroSolicitud;
	private String  codEmp;;
	private Double montoBCV;
	private List<BeneficiarioValNom> beneficiarios ;
	private List<Proveedor> listadoProv;
	private List<FormaPagoValNom> formaPagoValNoms;
	private List<ValorNombre> tipoInstituciones;
	private  List<ValorNombre> tipoEducacions;
	private List<ValorNombre> nivelEscolaridades;
	private List<ValorNombre> periodoPagos;
	private List<ValorNombre> periodoEscolares; 
	 private List<ValorNombre> estadosSolicitudLst;
	private String periodoEscolar;
	private List<ValorNombre> meses; 
	private String mes;
 
	private String 	  fechaActual;
	private String fechaFactura;
	private String fechaRegistro;
	private String fechaSolicitud ;
	private int codigoPeriodo;
	private String formatoPeriodo;
	private String viene;
	private String nroRifCentroEdu;
	private boolean isNroRifEmpty;
	private String codigoBenef;
	private String localidad;
	private String formatoPeriodoVigente;
	private int codigoPeriodoVigente;
	/**to desincoporar*/
	/**pagos*/
	private List<ValorNombre> listadoMesesPorPagar;
	private List<ValorNombre> listadoMesesPagados;
	private List<ValorNombre> listadoMesPagByFact;
	private List<ValorNombre> listadoMesPagByFactOnlyComplemento;
	private List<ValorNombre> receptorPagos;
	private List<ValorNombre> coFormaPagos;
	private String tablaGenerada;
	boolean isMatricula = false;
	boolean isPeriodo = false;
	 
	private int tipoPago = 0;;
	
	private double montoFactura;
	private String estatusPago;
	private String nroFactura;
	private int nroIdFactura;
	private String nroControl;
	private String conceptoPago;
	private String receptorPago;
	private double montoTotal;
	private BigDecimal montoTotalBiDec = new BigDecimal(0);;
	private String disabled;
	private String disabledRegPagos;
	private String disabledActualizarPagos;
	private String checkedPeriodo;
	private String checkedMatricula;
	/**showResultToViewToReportLst usada para reportes*/
	private List<BeneficiarioBean2> reporteMatriculaPeriodoLst =new ArrayList<BeneficiarioBean2>();
	private DocumentosBean documentosBean;
	/**Recaudos de los docs**/
	private List<Recaudo> recaudos ;
	private int numJubilados = 0;
	
 
	 
	private List<String> matriPeriodoEnMes = new ArrayList();
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public String getCedula() {
		return cedula;
	}
	public void setCedula(String cedula) {
		this.cedula = cedula;
	}
	public String getCodEmp() {
		return codEmp;
	}
	public void setCodEmp(String codEmp) {
		this.codEmp = codEmp;
	}
	public List<BeneficiarioValNom> getBeneficiarios() {
		return beneficiarios;
	}
	public void setBeneficiarios(List<BeneficiarioValNom> beneficiarios) {
		this.beneficiarios = beneficiarios;
	}
	public List<Proveedor> getListadoProv() {
		return listadoProv;
	}
	public void setListadoProv(List<Proveedor> listadoProv) {
		this.listadoProv = listadoProv;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	 
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public List<FormaPagoValNom> getFormaPagoValNoms() {
		return formaPagoValNoms;
	}
	public void setFormaPagoValNoms(List<FormaPagoValNom> formaPagoValNoms) {
		this.formaPagoValNoms = formaPagoValNoms;
	}
	public List<ValorNombre> getTipoInstituciones() {
		return tipoInstituciones;
	}
	public void setTipoInstituciones(List<ValorNombre> tipoInstituciones) {
		this.tipoInstituciones = tipoInstituciones;
	}
	public List<ValorNombre> getTipoEducacions() {
		return tipoEducacions;
	}
	public void setTipoEducacions(List<ValorNombre> tipoEducacions) {
		this.tipoEducacions = tipoEducacions;
	}
	public List<ValorNombre> getNivelEscolaridades() {
		return nivelEscolaridades;
	}
	public void setNivelEscolaridades(List<ValorNombre> nivelEscolaridades) {
		this.nivelEscolaridades = nivelEscolaridades;
	}
	public List<ValorNombre> getPeriodoPagos() {
		return periodoPagos;
	}
	public void setPeriodoPagos(List<ValorNombre> periodoPagos) {
		this.periodoPagos = periodoPagos;
	}
	public String getFechaActual() {
		return fechaActual;
	}
	public void setFechaActual(String fechaActual) {
		this.fechaActual = fechaActual;
	}
	public String getFechaSolicitud() {
		return fechaSolicitud;
	}
	public void setFechaSolicitud(String fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}
	public int getCodigoPeriodo() {
		return codigoPeriodo;
	}
	public void setCodigoPeriodo(int codigoPeriodo) {
		this.codigoPeriodo = codigoPeriodo;
	}
	public String getFormatoPeriodo() {
		return formatoPeriodo;
	}
	public void setFormatoPeriodo(String formatoPeriodo) {
		this.formatoPeriodo = formatoPeriodo;
	}
	public String getViene() {
		return viene;
	}
	public void setViene(String viene) {
		this.viene = viene;
	}
	public String getNroRifCentroEdu() {
		return nroRifCentroEdu;
	}
	public void setNroRifCentroEdu(String nroRifCentroEdu) {
		this.nroRifCentroEdu = nroRifCentroEdu;
	}
	public String getCodigoBenef() {
		return codigoBenef;
	}
	public void setCodigoBenef(String codigoBenef) {
		this.codigoBenef = codigoBenef;
	}
	public String getCedulaEmpleado() {
		return cedulaEmpleado;
	}
	public void setCedulaEmpleado(String cedulaEmpleado) {
		this.cedulaEmpleado = cedulaEmpleado;
	}
	public String getCodigoEmpleado() {
		return codigoEmpleado;
	}
	public void setCodigoEmpleado(String codigoEmpleado) {
		this.codigoEmpleado = codigoEmpleado;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getFechaIngreso() {
		return fechaIngreso;
	}
	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
	public String getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	public String getSituacion() {
		return situacion;
	}
	public void setSituacion(String situacion) {
		this.situacion = situacion;
	}
	public String getTipoEmpleado() {
		return tipoEmpleado;
	}
	public void setTipoEmpleado(String tipoEmpleado) {
		this.tipoEmpleado = tipoEmpleado;
	}
	public String getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}
	public String getCargo() {
		return cargo;
	}
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	public String getCompania() {
		return compania;
	}
	public void setCompania(String compania) {
		this.compania = compania;
	}
	public String getNroExpediente() {
		return nroExpediente;
	}
	public void setNroExpediente(String nroExpediente) {
		this.nroExpediente = nroExpediente;
	}
	public String getTipoNomina() {
		return tipoNomina;
	}
	public void setTipoNomina(String tipoNomina) {
		this.tipoNomina = tipoNomina;
	}
	public String getCedulaFamiliar() {
		return cedulaFamiliar;
	}
	public void setCedulaFamiliar(String cedulaFamiliar) {
		this.cedulaFamiliar = cedulaFamiliar;
	}
	public String getApellidoFamiliar() {
		return apellidoFamiliar;
	}
	public void setApellidoFamiliar(String apellidoFamiliar) {
		this.apellidoFamiliar = apellidoFamiliar;
	}
	public String getNombreFamiliar() {
		return nombreFamiliar;
	}
	public void setNombreFamiliar(String nombreFamiliar) {
		this.nombreFamiliar = nombreFamiliar;
	}
	public String getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public String getEdad() {
		return edad;
	}
	public void setEdad(String edad) {
		this.edad = edad;
	}
	public String getEstatus() {
		return estatus;
	}
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	public String getNroRif() {
		return nroRif;
	}
	public void setNroRif(String nroRif) {
		this.nroRif = nroRif;
	}
	public String getNombreCentro() {
		return nombreCentro;
	}
	public void setNombreCentro(String nombreCentro) {
		this.nombreCentro = nombreCentro;
	}
	public String getLocalidadBCV() {
		return localidadBCV;
	}
	public void setLocalidadBCV(String localidadBCV) {
		this.localidadBCV = localidadBCV;
	}
	public String getDireccionCentro() {
		return direccionCentro;
	}
	public void setDireccionCentro(String direccionCentro) {
		this.direccionCentro = direccionCentro;
	}
	public String getTlfCentro() {
		return tlfCentro;
	}
	public void setTlfCentro(String tlfCentro) {
		this.tlfCentro = tlfCentro;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNumSolicitud() {
		return numSolicitud;
	}
	public void setNumSolicitud(String numSolicitud) {
		this.numSolicitud = numSolicitud;
	}
	public String getMensajeExito() {
		return mensajeExito;
	}
	public void setMensajeExito(String mensajeExito) {
		this.mensajeExito = mensajeExito;
	}
	public String getTipoInstitucion() {
		return tipoInstitucion;
	}
	public void setTipoInstitucion(String tipoInstitucion) {
		this.tipoInstitucion = tipoInstitucion;
	}
	public String getTipoEducacion() {
		return tipoEducacion;
	}
	public void setTipoEducacion(String tipoEducacion) {
		this.tipoEducacion = tipoEducacion;
	}
	public String getPeriodoPago() {
		return periodoPago;
	}
	public void setPeriodoPago(String periodoPago) {
		this.periodoPago = periodoPago;
	}
	public String getFormaPago() {
		return formaPago;
	}
	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}
	public String getCedBenef() {
		return cedBenef;
	}
	public void setCedBenef(String cedBenef) {
		this.cedBenef = cedBenef;
	}
	public String getNivelEscolaridad() {
		return nivelEscolaridad;
	}
	public void setNivelEscolaridad(String nivelEscolaridad) {
		this.nivelEscolaridad = nivelEscolaridad;
	}
	public Double getMontoPeriodo() {
		return montoPeriodo;
	}
	public void setMontoPeriodo(Double montoPeriodo) {
		this.montoPeriodo = montoPeriodo;
	}
	public Double getMontoMatricula() {
		return montoMatricula;
	}
	public void setMontoMatricula(Double montoMatricula) {
		this.montoMatricula = montoMatricula;
	}
	public Double getMontoBCV() {
		return montoBCV;
	}
	public void setMontoBCV(Double montoBCV) {
		this.montoBCV = montoBCV;
	}
	public String getBenefCompartido() {
		return benefCompartido;
	}
	public void setBenefCompartido(String benefCompartido) {
		this.benefCompartido = benefCompartido;
	}
	public String getTipoEmpresa() {
		return tipoEmpresa;
	}
	public void setTipoEmpresa(String tipoEmpresa) {
		this.tipoEmpresa = tipoEmpresa;
	}
	public Double getMontoAporteEmp() {
		return montoAporteEmp;
	}
	public void setMontoAporteEmp(Double montoAporteEmp) {
		this.montoAporteEmp = montoAporteEmp;
	}
	public String getLocalidad() {
		return localidad;
	}
	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}
	public int getNroSolicitud() {
		return nroSolicitud;
	}
	public void setNroSolicitud(int nroSolicitud) {
		this.nroSolicitud = nroSolicitud;
	}
	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
	}
	public String getFormatoPeriodoVigente() {
		return formatoPeriodoVigente;
	}
	public void setFormatoPeriodoVigente(String formatoPeriodoVigente) {
		this.formatoPeriodoVigente = formatoPeriodoVigente;
	}
	public int getCodigoPeriodoVigente() {
		return codigoPeriodoVigente;
	}
	public void setCodigoPeriodoVigente(int codigoPeriodoVigente) {
		this.codigoPeriodoVigente = codigoPeriodoVigente;
	}
	public String getCo_status() {
		return co_status;
	}
	public void setCo_status(String co_status) {
		this.co_status = co_status;
	}
	public String getNivelEscolar() {
		return nivelEscolar;
	}
	public void setNivelEscolar(String nivelEscolar) {
		this.nivelEscolar = nivelEscolar;
	}
	public String getTablaGenerada() {
		return tablaGenerada;
	}
	public void setTablaGenerada(String tablaGenerada) {
		this.tablaGenerada = tablaGenerada;
	}
	 
	public List<String> getMatriPeriodoEnMes() {
		return matriPeriodoEnMes;
	}
	public void setMatriPeriodoEnMes(List<String> matriPeriodoEnMes) {
		this.matriPeriodoEnMes = matriPeriodoEnMes;
	}
 
	public int getTipoPago() {
		return tipoPago;
	}
	public void setTipoPago(int tipoPago) {
		this.tipoPago = tipoPago;
	}
	public String getCo_statusCmplto() {
		return co_statusCmplto;
	}
	public void setCo_statusCmplto(String co_statusCmplto) {
		this.co_statusCmplto = co_statusCmplto;
	}
	public List<ValorNombre> getListadoMesesPorPagar() {
		return listadoMesesPorPagar;
	}
	public void setListadoMesesPorPagar(List<ValorNombre> listadoMesesPorPagar) {
		this.listadoMesesPorPagar = listadoMesesPorPagar;
	}
	public double getMontoFactura() {
		return montoFactura;
	}
	public void setMontoFactura(double montoFactura) {
		this.montoFactura = montoFactura;
	}
	public String getEstatusPago() {
		return estatusPago;
	}
	public void setEstatusPago(String estatusPago) {
		this.estatusPago = estatusPago;
	}
	public String getNroFactura() {
		return nroFactura;
	}
	public void setNroFactura(String nroFactura) {
		this.nroFactura = nroFactura;
	}
	public String getNroControl() {
		return nroControl;
	}
	public void setNroControl(String nroControl) {
		this.nroControl = nroControl;
	}
	public String getConceptoPago() {
		return conceptoPago;
	}
	public void setConceptoPago(String conceptoPago) {
		this.conceptoPago = conceptoPago;
	}
	public String getReceptorPago() {
		return receptorPago;
	}
	public void setReceptorPago(String receptorPago) {
		this.receptorPago = receptorPago;
	}
	public List<ValorNombre> getReceptorPagos() {
		return receptorPagos;
	}
	public void setReceptorPagos(List<ValorNombre> receptorPagos) {
		this.receptorPagos = receptorPagos;
	}
	public double getMontoTotal() {
		return montoTotal;
	}
	public void setMontoTotal(double montoTotal) {
		this.montoTotal = montoTotal;
	}
	public String getFechaFactura() {
		return fechaFactura;
	}
	public void setFechaFactura(String fechaFactura) {
		this.fechaFactura = fechaFactura;
	}
	public String getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public boolean isMatricula() {
		return isMatricula;
	}
	public void setMatricula(boolean isMatricula) {
		this.isMatricula = isMatricula;
	}
	public boolean isPeriodo() {
		return isPeriodo;
	}
	public void setPeriodo(boolean isPeriodo) {
		this.isPeriodo = isPeriodo;
	}
	public List<ValorNombre> getPeriodoEscolares() {
		return periodoEscolares;
	}
	public void setPeriodoEscolares(List<ValorNombre> periodoEscolares) {
		this.periodoEscolares = periodoEscolares;
	}
	public String getPeriodoEscolar() {
		return periodoEscolar;
	}
	public void setPeriodoEscolar(String periodoEscolar) {
		this.periodoEscolar = periodoEscolar;
	}
	public List<ValorNombre> getMeses() {
		return meses;
	}
	public void setMeses(List<ValorNombre> meses) {
		this.meses = meses;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public int getNroIdFactura() {
		return nroIdFactura;
	}
	public void setNroIdFactura(int nroIdFactura) {
		this.nroIdFactura = nroIdFactura;
	}
	public int getInMesMatricula() {
		return inMesMatricula;
	}
	public void setInMesMatricula(int inMesMatricula) {
		this.inMesMatricula = inMesMatricula;
	}
	public int getCoPeriodo() {
		return coPeriodo;
	}
	public void setCoPeriodo(int coPeriodo) {
		this.coPeriodo = coPeriodo;
	}
	public int getNuIdFactura() {
		return nuIdFactura;
	}
	public void setNuIdFactura(int nuIdFactura) {
		this.nuIdFactura = nuIdFactura;
	}
	public List<ValorNombre> getListadoMesesPagados() {
		return listadoMesesPagados;
	}
	public void setListadoMesesPagados(List<ValorNombre> listadoMesesPagados) {
		this.listadoMesesPagados = listadoMesesPagados;
	}
	public String getDisabled() {
		return disabled;
	}
	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}
	public String getDisabledRegPagos() {
		return disabledRegPagos;
	}
	public void setDisabledRegPagos(String disabledRegPagos) {
		this.disabledRegPagos = disabledRegPagos;
	}
	public String getCheckedPeriodo() {
		return checkedPeriodo;
	}
	public void setCheckedPeriodo(String checkedPeriodo) {
		this.checkedPeriodo = checkedPeriodo;
	}
	public String getCheckedMatricula() {
		return checkedMatricula;
	}
	public void setCheckedMatricula(String checkedMatricula) {
		this.checkedMatricula = checkedMatricula;
	}
	public String getDisabledActualizarPagos() {
		return disabledActualizarPagos;
	}
	public void setDisabledActualizarPagos(String disabledActualizarPagos) {
		this.disabledActualizarPagos = disabledActualizarPagos;
	}
	public String getNb_status() {
		return nb_status;
	}
	public void setNb_status(String nb_status) {
		this.nb_status = nb_status;
	}
	public List<BeneficiarioBean2> getReporteMatriculaPeriodoLst() {
		return reporteMatriculaPeriodoLst;
	}
	public void setReporteMatriculaPeriodoLst(
			List<BeneficiarioBean2> reporteMatriculaPeriodoLst) {
		this.reporteMatriculaPeriodoLst = reporteMatriculaPeriodoLst;
	}
	public DocumentosBean getDocumentosBean() {
		return documentosBean;
	}
	public void setDocumentosBean(DocumentosBean documentosBean) {
		this.documentosBean = documentosBean;
	}
	public List<ValorNombre> getEstadosSolicitudLst() {
		return estadosSolicitudLst;
	}
	public void setEstadosSolicitudLst(List<ValorNombre> estadosSolicitudLst) {
		this.estadosSolicitudLst = estadosSolicitudLst;
	}
	public List<ValorNombre> getListadoMesPagByFact() {
		return listadoMesPagByFact;
	}
	public void setListadoMesPagByFact(List<ValorNombre> listadoMesPagByFact) {
		this.listadoMesPagByFact = listadoMesPagByFact;
	}
	public String getPagIrAfter() {
		return pagIrAfter;
	}
	public void setPagIrAfter(String pagIrAfter) {
		this.pagIrAfter = pagIrAfter;
	}
	public List<Recaudo> getRecaudos() {
		return recaudos;
	}
	public void setRecaudos(List<Recaudo> recaudos) {
		this.recaudos = recaudos;
	}
	public String getTlfNumExt() {
		return tlfNumExt;
	}
	public void setTlfNumExt(String tlfNumExt) {
		this.tlfNumExt = tlfNumExt;
	}
	public String getEmailPropio() {
		return emailPropio;
	}
	public void setEmailPropio(String emailPropio) {
		this.emailPropio = emailPropio;
	}
	public String getEmailBcv() {
		return emailBcv;
	}
	public void setEmailBcv(String emailBcv) {
		this.emailBcv = emailBcv;
	}
	public String getUrlAccion() {
		return urlAccion;
	}
	public void setUrlAccion(String urlAccion) {
		this.urlAccion = urlAccion;
	}
	public String getConceptoMesMatricula() {
		return conceptoMesMatricula;
	}
	public void setConceptoMesMatricula(String conceptoMesMatricula) {
		this.conceptoMesMatricula = conceptoMesMatricula;
	}
	public String getConceptoPagoAdicional() {
		return conceptoPagoAdicional;
	}
	public void setConceptoPagoAdicional(String conceptoPagoAdicional) {
		this.conceptoPagoAdicional = conceptoPagoAdicional;
	}
	public Double getMontoPagoAdicional() {
		return montoPagoAdicional;
	}
	public void setMontoPagoAdicional(Double montoPagoAdicional) {
		this.montoPagoAdicional = montoPagoAdicional;
	}
	public String getTxtcomplemento() {
		return txtcomplemento;
	}
	public void setTxtcomplemento(String txtcomplemento) {
		this.txtcomplemento = txtcomplemento;
	}
	public Double getMontocomplemento() {
		return montocomplemento;
	}
	public void setMontocomplemento(Double montocomplemento) {
		this.montocomplemento = montocomplemento;
	}
	public List<ValorNombre> getCoFormaPagos() {
		return coFormaPagos;
	}
	public void setCoFormaPagos(List<ValorNombre> coFormaPagos) {
		this.coFormaPagos = coFormaPagos;
	}
	public List<ValorNombre> getListadoMesPagByFactOnlyComplemento() {
		return listadoMesPagByFactOnlyComplemento;
	}
	public void setListadoMesPagByFactOnlyComplemento(
			List<ValorNombre> listadoMesPagByFactOnlyComplemento) {
		this.listadoMesPagByFactOnlyComplemento = listadoMesPagByFactOnlyComplemento;
	}
	public boolean isNroRifEmpty() {
		return isNroRifEmpty;
	}
	public void setNroRifEmpty(boolean isNroRifEmpty) {
		this.isNroRifEmpty = isNroRifEmpty;
	}
	public int getNumJubilados() {
		return numJubilados;
	}
	public void setNumJubilados(int numJubilados) {
		this.numJubilados = numJubilados;
	}
	public BigDecimal getMontoTotalBiDec() {
		return montoTotalBiDec;
	}
	public void setMontoTotalBiDec(BigDecimal montoTotalBiDec) {
		this.montoTotalBiDec = montoTotalBiDec;
	}
	 
 
}
