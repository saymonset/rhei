/** ************ Funci�n menu acorde�n ************** */
$(function() {
	(function($) {
		$.fn.accordion = function(custom) {
			var defaults = {
				keepOpen : false,
				startingOpen : false
			};
			var settings = $.extend({}, defaults, custom);
			if (settings.startingOpen) {
				$(settings.startingOpen).show();
			}

			return this.each(function() {
				var obj = $(this);
				$('li a', obj).click(
						function(event) {
							var elem = $(this).next();
							if (elem.is('ul')) {
								event.preventDefault();
								if (!settings.keepOpen) {
									obj.find('ul:visible').not(elem).not(
											elem.parents('ul:visible'))
											.slideUp();
								}
								elem.slideToggle();
							}
						});
			});
		};

	})(jQuery);

	$('.menu').accordion({
		keepOpen : false,
		startingOpen : '#open'
	});

});

function generarReporteBecaUtile(keyb) {
	var isSubmit = true;
	var campo = document.formBuscarDatos; 

	if (!document.forms[0].elements['periodoEscolar'].value) {
		alert("Debe seleccionar el Per\u00edodo Escolar.");
		isSubmit = false;
	}
	if (!document.forms[0].elements['TX_OBSERVACION'].value) {
		alert("Debe ingresar el nombre del Reporte");
		isSubmit = false;
	}
	if (!document.forms[0].elements['TX_OBSERVACION'].value=="A-Za-z0-9-\s") {
		alert("Debe ingresar solo");
		isSubmit = false;
	}

	if (isSubmit) {
		var formdata = new FormData();
		var xhr = new XMLHttpRequest();

		xhr.open("POST",
				"/rhei/reporteBecaUtileContrlador?principal.do=validateWithAjax"
						+ '&periodoEscolar=' + document.forms[0].elements['periodoEscolar'].value
						+ '&txObservacion='
						+ document.forms[0].elements['TX_OBSERVACION'].value,
				true);

		xhr.send(formdata);

		xhr.onload = function(e) {

			if (this.status == 200) {
				var r = confirm("Una vez darle al boton Aceptar no saldra nuevamente este reporte PARA ESTE PERIODO ESCOLAR HASTA QUE INCLUYA NUEVAS SOLICITUDES" +
						" A SU VEZ DEBE REVISAR EL PARAMETRO DE LA PERSONA ENCARGADA DE REALIZAR EL REPORTE ANTES DE GENERARLO");
				if (r == true) {
					// estos campos estan en hideen
					// en
					campo.action = '/rhei/documentOpenRporte';
					campo.keyReport.value = keyb;
					campo.txObservacion.value = document.forms[0].elements['TX_OBSERVACION'].value;
					campo.status.value ='A'; 
					
					
					campo.submit();

					angular.element(angularRegion).scope().$apply(
							'handleClick()');
				}
			}else if (this.status == 500) {
				alert('Nombre inavlido.. ya existe.');
			}

		};

	}

}



function buscarBecaUtile(keyb,status) {
	var isSubmit = true;
	var campo = document.formBuscarDatos;
	if (!document.forms[0].elements['periodoEscolar'].value) {
		alert("Debe seleccionar el Per\u00edodo Escolar.");
		isSubmit = false;
	}
	if ('A'==status){
		if ( !document.forms[0].elements['tipoEmpleado'].value) {
			alert("Debe seleccionar el Tipo de Empleado.");
			isSubmit = false;
		} 
	}

	if (isSubmit) {

		// estos campos estan en hideen
		// en
		// campo.action = '/rhei/documentOpenRporte';
		campo.action = '/rhei/reporteBecaUtileContrlador?principal.do=reportConsulta';
		campo.status.value=status;
		campo.keyReport.value = keyb;
		campo.submit();
	}
}

/** *********************************************************** */
// NU_SOLICITUD, NU_REF_PAGO , IN_COMPLEMENTO
function deleteFactura(numId, name) {
	confirmar=confirm("Seguro que desea eliminar el registro " + name +"?");  
	if (confirmar) {
		var campo = document.formDatosSolicitud;
		var bandera = true;
		if (bandera) {
			campo.keyNuSolicitudNuFactNuRefPagoInComplemento.value = numId;
			campo.submit();
		}
	}
}

function changeReembolsoComplemento(obj) {
	document.getElementById('txtcomplemento').value = '';

	if ('0' == obj.value) {
		document.getElementById('txtcomplemento').value = 'Complemento ';
	} else if ('1' == obj.value) {
		document.getElementById('txtcomplemento').value = 'Reembolso ';
	}
}

function changeBeneficiario() {
	showDataOfTrabAndBenef();
	document.getElementById('benefwait').style.display = 'block';
}

/**
 * Cambia via ajax los resultados de los recaudo
 * 
 * @param rif
 * @param cedula
 */
function changePeriodoEscolar(rif, cedula) {

	var periodoEscolar = document.formDatosSolicitud.periodoEscolar;
	var periodoEscolarValor = periodoEscolar.options[periodoEscolar.selectedIndex].value;

	var codBenefName = document.formBuscarDatos.codigoBenef;
	var codBenef = codBenefName.options[codBenefName.selectedIndex].value;

	document.getElementById('showUploadRecaudo').style.display = 'none';
	document.getElementById('recaudosListShow').style.display = 'none';
	document.getElementById('messagewait').style.display = 'block';

	var formdata = new FormData();

	var xhr = new XMLHttpRequest();

	xhr
			.open("POST",
					"/rhei/solicitudControladorIncluir?principal.do=listRecaudos"
							+ '&periodoEscolarValor=' + periodoEscolarValor
							+ '&rif=' + rif + '&cedula=' + cedula
							+ '&codBenef=' + codBenef, true);

	xhr.send(formdata);

	xhr.onload = function(e) {

		if (this.status == 200) {
			document.getElementById("message").innerHTML = this.responseText;
			document.getElementById('showUploadRecaudo').style.display = 'none';
			document.getElementById('btnPrincipal').style.display = 'block';
		}
		document.getElementById('messagewait').style.display = 'none';
		document.getElementById('recaudosListShow').style.display = 'block';

	};

}
// Documento de funciones con Java Script Proyecto MEI
/** *********************************************************** */
/** Validar tipo de educacion */
/**
/** La funcion valida si es tipo especial y puede tener mas de 8 a\u00f1os de edad el nino*/
 
function validTipoEduc() {
	var edad = document.getElementById('edadBenfId').value;
	var tipoEduc = document.getElementById('tipoEducacion').value;
	if (tipoEduc == 'R' && edad >= 8) {
		alert('Mayor a 8 a\u00f1os debe ser un ni\u00f1o especial para incluir la solicitud');
		return false;
	}
	return true;
}

function cargarCamposNumericos(origen) {
	var campo = document.formDatosSolicitud;

	/*
	 * var campos2 = new
	 * Array('montoFactura','montoBCV','otrosPagos','montoTotal');
	 */
	/* var dato=""; */
	if (origen == 'solicitud') {
		/*
		 * for(i=0; i<campos1.length; i++){
		 * dato=campo+'.'+campos1[i]+'.'+'value'; dato=formato_numero(dato, 2,
		 * ',', '.'); }
		 */

		campo.montoPeriodo.value = formato_numero(campo.montoPeriodo.value, 2,
				',', '.');
		campo.montoMatricula.value = formato_numero(campo.montoMatricula.value,
				2, ',', '.');
		campo.montoBCV.value = formato_numero(campo.montoBCV.value, 2, ',', '.');
	}
	if (origen == 'pago') {
		/*
		 * for(i=0; i<campos2.length; i++){
		 * dato=campo+'.'+campos2[i]+'.'+'value'; dato=formato_numero(dato, 2,
		 * ',', '.'); }
		 */
		campo.montoFactura.value = formato_numero(campo.montoFactura.value, 2,
				',', '.');
		campo.montoBCV.value = formato_numero(campo.montoBCV.value, 2, ',', '.');
		/*
		 * campo.otrosPagos.value=formato_numero(campo.otrosPagos.value, 2, ',',
		 * '.');
		 */
		campo.montoTotal.value = formato_numero(campo.montoTotal.value, 2, ',',
				'.');
	}
	return;
}

function formato_camposMTOBCV() {
	var campo = document.formParametro;
	// alert('Hola mundo ' + campo.nombreParametro.value);

	if ("MTOBCV" == campo.nombreParametro.value) {
		formatearCampo(campo.valorParametro);
	}
	return true;
}
function formato_camposMTOUTILES() {
	var campo = document.formParametro;
	// alert('Hola mundo ' + campo.nombreParametro.value);

	if ("MTOUTILES" == campo.nombreParametro.value) {
		formatearCampo(campo.valorParametro);
	}
	return true;
}

function formato_camposMontosPagos() {
	formatearCampo(document.formDatosSolicitud.montoPeriodo);
	formatearCampo(document.formDatosSolicitud.montoMatricula);
	formatearCampo(document.formDatosSolicitud.montoBCV);
	formatearCampo(document.formDatosSolicitud.montoFactura);
	formatearCampo(document.formDatosSolicitud.montoAporteEmp);
	return true;
}

function formato_camposMontosSolicitud() {
	formatearCampo(document.formDatosSolicitud.montoBCV);
	formatearCampo(document.formDatosSolicitud.montoPeriodo);
	formatearCampo(document.formDatosSolicitud.montoMatricula);
	formatearCampo(document.formDatosSolicitud.montoAporteEmp);
	return true;
}

function formato_camposMontos() {

	formatearCampo(document.formDatosSolicitud.montoPeriodo);

	formatearCampo(document.formDatosSolicitud.montoMatricula);

	formatearCampo(document.formDatosSolicitud.montoBCV);

	return true;
}
/** *********************************************************** */
function formato_numero(numero, decimales, separador_decimal, separador_miles) {
	numero = numero.toString().replace(",", ".");
	numero = parseFloat(numero);
	if (isNaN(numero)) {
		return "";
	}

	if (decimales !== undefined) {
		// Redondeamos
		numero = numero.toFixed(decimales);
	}

	// Convertimos el punto en separador_decimal
	numero = numero.toString().replace(".",
			separador_decimal !== undefined ? separador_decimal : ",");

	if (separador_miles) {
		// A�adimos los separadores de miles
		var miles = new RegExp("(-?[0-9]+)([0-9]{3})");
		while (miles.test(numero)) {
			numero = numero.replace(miles, "$1" + separador_miles + "$2");
		}
	}

	return numero;
}
/** *********************************************************** */
function formatearCampo(obj) {

	obj.value = formato_numero(obj.value, 2, ',', '.');
	if (obj.value == "") {
		obj.value = 0;
	}
	if (obj.value == "" && obj.name != 'montoBCV' && obj.name != 'montoPeriodo'
			&& obj.name != 'montoTotal' && obj.name != 'pagoPendiente'
			&& obj.name != 'pagoRegistrado') {
		alert("Debe ingresar un n\u00famero");
		obj.focus();
	}
	return;
}

function formatearCampoFromAngular(obj) {
	obj.value = formato_numero(obj.value, 2, ',', '.');

	if (obj.value == "") {
		obj.value = 0;
	}

	return;
}

/** *********************************************************** */
// onblur="validarSeleccion(this)" --> colocarlar en el campo Mes
// de Pago de las pantallas RegistrarPago2 y ActualizarPagosPendientes
function validarSeleccion(obj) {
	var vector = new Array();
	var primerElemento = 0;
	var ultimoElemento = 0;
	var seleccionContinua = true;
	for (i = 0; i < obj.length; i++) {
		if (obj.options[i].selected == true) {
			primerElemento = i;
			break;
		}
	}
	for (i = obj.length - 1; i > primerElemento; i--) {
		if (obj.options[i].selected == true) {
			ultimoElemento = i;
			break;
		}
	}

	for (i = primerElemento + 1; i <= ultimoElemento; i++) {
		if (obj.options[i].selected == false) {
			seleccionContinua = false;
			break;
		}
	}

	if (seleccionContinua == false) {
		alert("Debe seleccionar meses continuos...!!!");
		obj.selectedIndex = -1;
		obj.focus();
	}
}

function clickfechaFin() {
	$("#fechaFin").focus();
}

function clickfechaInicio() {
	$("#fechaInicio").focus();
}

function clickfechaVigencia() {
	$("#fechaVigencia").focus();
}
function clickfechaRegistro() {
	$("#fechaRegistro").focus();
}
function clickfechaFactura() {
	$("#fechaFactura").focus();
}

function clickBenefScolarMenu() {
	$("#beneficioEscolarMenu").click();
}

function clickParametroMenu() {
	$("#parametroMenu").click();
}

function clickPeriodoEscolarMenu() {
	$("#escolarMenu").click();
}

function clickAdminMenu() {
	$("#admMenu").click();
}

function clickGstsolRprteMenu() {
	clickGstsolMenu();
	$("#gstsolRprteMenu").click();
}

function clickRprteMenu() {
	clickGstPagoMenu();
	$("#rprteMenu").click();
}

function clickGstPagoMenu() {
	$("#gstPagoMenu").click();
}

function clickGstPagoRegistrMenu() {
	clickGstPagoMenu();
	$("#gstPagoRegistrMenu").click();
}

function clickIncluirSolicitudMenu() {
	clickEducInicialMenu();
	$("#gstPagoRegistrMenu").click();
}

function clickEducInicialMenu() {
	$("#gstsolMenu").click();
	$("#open").click();
}

function clickGstsolMenu() {
	$("#gstsolMenu").click();
}

function clickReporteInidividual() {
	alert('Hola mundo');
	// $("#gstsolMenu").click();
}

function clickGstActualizar() {
	clickGstPagoMenu();
	$("#gstActualizar").click();
}

function clickGstActualizarMenu() {
	clickGstActualizar();
	$("#gstActualizarMenu").click();
}

function clickReporteMenu() {
	clickGstActualizar();
	$("#gstReporteMenu").click();
}

/** *********************************************************** */

function actualizaReloj() {

	/*
	 * marcacion = new Date(); Hora = marcacion.getHours(); Minutos =
	 * marcacion.getMinutes(); Segundos = marcacion.getSeconds();
	 * 
	 * if (Hora<=9) Hora = "0" + Hora;
	 * 
	 * if (Minutos<=9) Minutos = "0" + Minutos;
	 * 
	 * if (Segundos<=9) Segundos = "0" + Segundos;
	 */

	var Dia = new Array("Domingo", "Lunes", "Martes", "Mi\u00e9rcoles", "Jueves",
			"Viernes", "S\u00e1bado", "Domingo");
	var Mes = new Array("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
			"Julio", "Agosto", "Septiembre", "Octubre", "Noviembre",
			"Diciembre");
	var Hoy = new Date();
	var Anio = Hoy.getFullYear();
	var Fecha = Dia[Hoy.getDay()] + ", " + Hoy.getDate() + " de "
			+ Mes[Hoy.getMonth()] + " de " + Anio + " ";
	var Inicio, Script, Final, Total;

	Inicio = "<span class='reloj'>";

	/* En Reloj le indicamos la Hora, los Minutos y los Segundos */
	Script = Fecha; // + Hora + ":" + Minutos + ":" + Segundos;

	Final = "</span>";

	Total = Inicio + Script + Final;

	/* Capturamos una celda para mostrar el Reloj */
	document.getElementById('Fecha_Reloj').innerHTML = Total;

	/* Indicamos que nos refresque el Reloj cada 1 segundo */
	setTimeout("actualizaReloj()", 1000);
}

/** *********************************************************** */
function actualizarPeriodoEscolar() {
	var campo = document.formDatosSolicitud;
	if (campo.tramite[0].checked == true) {
		campo.periodoEscolar.value = campo.periodoE.value;
		campo.codigoPeriodo.value = campo.codigoPeriodoE.value;
	} else {
		campo.periodoEscolar.value = campo.periodoEV.value;
		campo.codigoPeriodo.value = campo.codigoPeriodoEV.value;
	}
}

/** *********************************************************** */

function validarFormatoFecha(obj) {
	// var re=/^[0-9][0-9][0-9][0-9]\-[0-9][0-9]\-[0-9][0-9]$/;
	// var re=/^[0-9][0-9]\-[0-9][0-9]\-[0-9][0-9][0-9][0-9]$/;
	// !/^(([1-9]){1}([0-9]){1,6}|[0]{1})([.]{1}([0-9]){1,2})$/.test(monto)
	var re = /^\d{2}\-\d{2}\-([1]{1}[9]{1}[0-9]{1}\d{1}|[2]{1}\d{3})$/;
	if (!re.test(obj.value)) {
		alert("La fecha no tiene el formato necesario, esta debe tener el siguiente formato: DD-MM-YYYY.\nEl a\u00f1o ingresado debe estar comprendido entre los l\u00edmites [1900-2999]");
		obj.focus();
		return false;
	} else {
		return true;
	}
}

/** *********************************************************** */

function mascara(d, sep, patron, nums) {
	if (patron == 'fecha') {
		var pat = new Array(2, 2, 4);
	}
	if (d.valant != d.value) {
		val = d.value;
		largo = val.length;
		val = val.split(sep);
		val2 = '';
		for (r = 0; r < val.length; r++) {
			val2 += val[r];
		}
		if (nums) {
			for (z = 0; z < val2.length; z++) {
				if (isNaN(val2.charAt(z))) {
					letra = new RegExp(val2.charAt(z), "g");
					val2 = val2.replace(letra, "");
				}
			}
		}
		val = '';
		val3 = new Array();
		for (s = 0; s < pat.length; s++) {
			val3[s] = val2.substring(0, pat[s]);
			val2 = val2.substr(pat[s]);
		}
		for (q = 0; q < val3.length; q++) {
			if (q == 0) {
				val = val3[q];
			} else {
				if (val3[q] != "") {
					val += sep + val3[q];
				}
			}
		}
		d.value = val;
		d.valant = val;
	}
}
/** *********************************************************** */
function isValidDate(obj) {
	/*
	 * Funcion que devuelve true o false dependiendo de si la fecha es correcta.
	 * 
	 * Recibe un string como fecha con el siguiente formato: yyyymmdd
	 */

	var dteDate;
	var fecha = obj.value.split("-");
	// var fecha= obj.value;
	var dia = fecha[0];
	var mes = fecha[1];
	var anio = fecha[2];
	/*
	 * var anio = fecha.substring(0,4); var mes = fecha.substring(4,6); var dia =
	 * fecha.substring(6);
	 */

	/*
	 * var dia = fecha.substring(0,2); var mes = fecha.substring(2,4); var anio =
	 * fecha.substring(4);
	 */

	// En javascript, el mes empieza en la posicion 0 y termina en la 11 siendo
	// 0 el mes de enero
	// Por esta razon, tenemos que restar 1 al mes
	mes = mes - 1;
	// Establecemos un objeto Data con los valore recibidos
	dteDate = new Date(anio, mes, dia);

	// Si el dia, mes y a�o concuerdan...
	if ((dia == dteDate.getDate()) && (mes == dteDate.getMonth())
			&& (anio == dteDate.getFullYear()))
		return true;
	else {
		alert("La fecha ingresada no es v\u00e1lida, por favor ingrese una fecha v\u00e1lida...");
		obj.value = "";
		obj.focus();
		return false;
	}
	// Si deseamos que devuelva true o false...
	// return ((day==dteDate.getDate()) && (month==dteDate.getMonth()) &&
	// (year==dteDate.getFullYear()));
}

/** *********************************************************** */
function validadorFiltros() {
	var campo = document.FiltrosReportes;
	var respuesta = true;
	var bandera = true;
	var filtros = new Array();
	var indice = 0;
	var i = 0;
	while (bandera) {
		if (campo.elements['cedulaTrab'] && respuesta == true) {
			if (campo.cedulaTrab.value.length == 0) {
				alert("El campo correspondiente a la c\u00e9dula del Trabajador no puede estar vac\u00edo");
				campo.cedulaTrab.focus();
				respuesta = false;
				bandera = false;
			} else {
				if (validarCedula(campo.cedulaTrab) == false) {
					respuesta = false;
					bandera = false;
				} else {
					respuesta = true;
				}
			}
		}
		if (campo.elements['rif'] && respuesta == true) {
			if (campo.rif.value.length == 0) {
				alert("El campo correspondiente al rif del CEI no puede estar vac\u00edo");
				campo.rif.focus();
				respuesta = false;
				bandera = false;
			} else {
				if (validarRif(campo.rif) == false) {
					respuesta = false;
					bandera = false;
				} else {
					respuesta = true;
				}
			}
		}
		if (campo.elements['tipo_empleado'] && respuesta == true) {
			if (document.FiltrosReportes.tipo_empleado.value.length == 0) {
				alert("El campo Tipo de Empleado no puede estar vac\u00edo");
				document.FiltrosReportes.tipo_empleado.focus();
				respuesta = false;
				bandera = false;
			} else {
				respuesta = true;
			}
		}
		if (campo.elements['formaPago'] && respuesta == true) {
			if (document.FiltrosReportes.formaPago.value.length == 0) {
				alert("El campo Forma de Pago no puede estar vac\u00edo");
				document.FiltrosReportes.formaPago.focus();
				respuesta = false;
				bandera = false;
			} else {
				respuesta = true;
			}
		}
		if (campo.elements['tipoConvenio'] && respuesta == true) {
			if (document.FiltrosReportes.tipoConvenio.value.length == 0) {
				alert("El campo Tipo centro no puede estar vac\u00edo");
				document.FiltrosReportes.tipoConvenio.focus();
				respuesta = false;
				bandera = false;
			} else {
				respuesta = true;
			}
		}
		if (campo.elements['tipoEducacion'] && respuesta == true) {
			if (document.FiltrosReportes.tipoEducacion.value.length == 0) {
				alert("El campo Tipo educaci�n no puede estar vac\u00edo");
				document.FiltrosReportes.tipoEducacion.focus();
				respuesta = false;
				bandera = false;
			} else {
				respuesta = true;
			}
		}
		if (campo.elements['compania'] && respuesta == true) {
			if (document.FiltrosReportes.compania.value.length == 0) {
				alert("El campo Compa�ia no puede estar vac\u00edo");
				document.FiltrosReportes.compania.focus();
				respuesta = false;
				bandera = false;
			} else {
				respuesta = true;
			}
		}
		if (campo.elements['mes'] && respuesta == true) {
			if (document.FiltrosReportes.mes.value.length == 0) {
				alert("El campo Mes de registro del pago no puede estar vac\u00edo");
				document.FiltrosReportes.mes.focus();
				respuesta = false;
				bandera = false;
			} else {
				respuesta = true;
			}
		}
		if (campo.elements['anio'] && respuesta == true) {
			if (document.FiltrosReportes.anio.value.length == 0) {
				alert("El campo A�o de registro del pago no puede estar vac\u00edo");
				document.FiltrosReportes.anio.focus();
				respuesta = false;
				bandera = false;
			} else {
				respuesta = true;
			}
		}
		if (campo.elements['inicio'] && respuesta == true) {
			if (document.FiltrosReportes.inicio.value.length == 0) {
				alert("El campo A�o de inicio del per\u00edodo escolar no puede estar vac\u00edo");
				document.FiltrosReportes.inicio.focus();
				respuesta = false;
				bandera = false;
			} else {
				if (validarAnio(document.FiltrosReportes.inicio)) {
					respuesta = true;
				} else {
					respuesta = false;
					bandera = false;
				}
			}
		}
		if (respuesta == true) {
			bandera = false;
		}
	}

	if (respuesta == true) {
		campo.action = '/rhei/GeneradorReportes';
		campo.submit();
	}
}

function reportePagoByFacturaNumSolicitud() {
	var campo2 = document.formDatosSolicitud;
	var frmReporteSolicitud = document.frmReporteSolicitud;
	var periodoEscolar = campo2.elements['periodoEscolar'].value;
	var numSolicitudHidden = campo2.nroSolicitudId.value;
	var nuFacturaHidden = campo2.nroFactura.value;
	frmReporteSolicitud.periodoEscolarHidden.value = periodoEscolar;
	frmReporteSolicitud.numSolicitudHidden.value = numSolicitudHidden;
	frmReporteSolicitud.nuFacturaHidden.value = nuFacturaHidden;
	frmReporteSolicitud.submit();

}

function reporteByFamiliar() {
	var campo2 = document.formBuscarDatos;
	var cedula = campo2.cedula.value;
	var status = campo2.elements['status'].value;
	var codigoBenef = campo2.elements['codigoBenef'].value;
	var periodoEscolar = campo2.elements['periodoEscolar'].value;

	campo2.action = "/rhei/documentOpenRporte";
	if (!cedula) {
		alert("Falta la cedula del empleado.");
		return false;
	}
	if (!periodoEscolar) {
			alert("Debe seleccionar el per\u00edodo Escolar.");
		return false;
	}
	if (!codigoBenef) {
		alert("Debe seleccionar el Beneficiario.");
		return false;
	}

	campo2.submit();

}

function generarReporteFormato() {
	var frmReporteFormato = document.frmReporteFormato;

	frmReporteFormato.submit();
}

function generarReporteNumSolicitud() {
	var isSubmit = true;
	var campo = document.formBuscarDatos;
	var campo2 = document.formDatosSolicitud;
	var frmReporteSolicitud = document.frmReporteSolicitud;

	if (!campo2.elements['periodoEscolar'].value) {
			alert("Debe seleccionar el per\u00edodo Escolar.");
		isSubmit = false;
	}
	if (!campo.cedula.value) {
		alert("Falta la cedula del empleado.");
		isSubmit = false;
	}
	if (!campo.codigoBenef.value) {
		alert("Debe seleccionar el c�digo del beneficiario.");
		isSubmit = false;
	}

	if (isSubmit) {
		var periodoEscolar = campo2.elements['periodoEscolar'].value;
		var cedula = campo.cedula.value;
		var codigoBenef = campo.codigoBenef.value;
		frmReporteSolicitud.periodoEscolarHidden.value = periodoEscolar;
		frmReporteSolicitud.cedulaHidden.value = cedula;
		frmReporteSolicitud.codigoBenefHidden.value = codigoBenef;
		frmReporteSolicitud.submit();
	}

}

/** *********************************************************** */
function removeOptions(selectbox, operacion) {
	var i;
	var contador = 0;
	if (selectbox) {
		for (i = selectbox.options.length - 1; i >= 0; i--) {
			if (selectbox.options[i].selected == false) {
				selectbox.remove(i);
			} else {
				contador++;
			}
		}
		selectbox.size = contador;
		if (operacion == 'consultarPago') {
			selectbox.disabled = true;
			document.formDatosSolicitud.imprimirPago.focus = true;
		}
	}
}
/** *********************************************************** */
function removeAllOptions(selectbox) {
	var i;
	for (i = selectbox.options.length - 1; i >= 0; i--) {
		selectbox.remove(i);
	}
}

/** *********************************************************** */
function modificarSelectConceptoPago(obj) {
	var campo = document.FiltrosReportes;
	// var encontrado = false;
	var conceptoPago = document.getElementById("concepto");
	for (i = 0; i < obj.length; i++) {
		if (obj.options[i].selected == true
				&& (obj.options[i].value == '3' || obj.options[i].value == '2')) {
			removeOptions(conceptoPago, 'generarReporte');
			conceptoPago.options[0].value = '1';
			conceptoPago.options[0].text = 'Per�odo';
			// encontrado = true;
			break;
		}/*
			 * else{ if(obj.options[obj.selectedIndex].value =='1'){
			 * removeAllOptions(conceptoPago); agregarOpcion(conceptoPago);
			 * break; //campo.conceptoPago.options[2].selected = true; } }
			 */
	}
	// obj.options[obj.selectedIndex].value !='3'
	if (obj.options[obj.selectedIndex].value == '1'
			|| obj.options[obj.selectedIndex].value == '-1') {
		removeAllOptions(conceptoPago);
		agregarOpcion(conceptoPago);
		campo.conceptoPago.options[2].selected = true;
	}
}
/** *********************************************************** */
function agregarOpcion(obj) {
	var conceptos = new Array('Matr�cula', 'Per�odo', 'Todos');
	for (i = 0; i < conceptos.length; i++) {
		var op = document.createElement("option");
		if (conceptos[i] == 'Todos') {
			op.value = '-1';
		} else {
			op.value = i;
		}
		// op.value=i;
		op.text = conceptos[i];
		try {
			document.getElementById("concepto").add(op, null);
		} catch (ex) {
			document.getElementById("concepto").add(op);
		}
	}
}
/** *********************************************************** */
/*
 * function restringirMesProrrateo(obj){ var campo =
 * document.formDatosSolicitud; if(obj.selectedIndex == 1){
 * campo.meses.options[11].selected = true; removeOptions(campo.meses,
 * 'actualizarPago'); } }
 */

/*
 * function cambiarComboMes(formulario){ var meses = new Array(); meses[0] = new
 * Array(); meses[1] = new Array('SEPTIEMBRE', 'OCTUBRE', 'NOVIEMBRE',
 * 'DICIEMBRE', 'ENERO', 'FEBRERO', 'MARZO', 'ABRIL', 'MAYO', 'JUNIO', 'JULIO',
 * 'AGOSTO'); meses[2] = new Array('AGOSTO'); var i = 0; var select1 =
 * formulario['tipoPago']; var select2 = formulario['meses']; var vector =
 * meses[select1.selectedIndex]; if(vector.length){
 * select2.length=vector.length; } while(vector[i]){ select2.options[i].text =
 * vector[i]; //alert('Mes: '+select2.options[i].text); if(select1.selectedIndex ==
 * 1){ if(i<=3){ select2.options[i].value = i+9; //alert('Valor:
 * '+select2.options[i].value); }else{ select2.options[i].value = i-3;
 * //alert('Valor: '+select2.options[i].value); } }else{
 * if(select1.selectedIndex == 2){ select2.options[i].value = i+8;
 * //alert('Valor: '+select2.options[i].value); } } i++; }
 * select2.options[0].selected = 1; }
 */

/** *********************************************************** */
function marcarConceptos() { // Modificado el 12-04-2011
	var campo = document.formDatosSolicitud;
	// var desactivar = false;
	try {
		for (i = 0; i < campo.mesesPorPagar.length; i++) {
			if (campo.mesesPorPagar.options[i].selected == true) {
				/** Matricula es igual a 14 */
				if (campo.mesesPorPagar.options[i].value == "14") {
					campo.conceptoPago[0].checked = true;
					// desactivar = false;
				} else {
					campo.conceptoPago[1].checked = true;
					// desactivar = true;
				}
			}
		}
	} catch (e) {
	}

}
/** *********************************************************** */

function marcar() {
	var campo = document.formDatosSolicitud;
	/* Marca la primera opcion */
	/** Matricula es igual a 14 */
	if (campo.mesesPorPagar[1].value == "14" && campo.conceptoPago[0].checked) {
		campo.mesesPorPagar[1].selected = true;
	}
	if (campo.mesesPorPagar[1].value == "14"
			&& campo.conceptoPago[0].checked == false
			&& campo.mesesPorPagar[1].selected) {
		campo.mesesPorPagar[1].selected = false;
	}
	campo.mesesPorPagar.disabled = false;
	/* Corre el cursor uno abajo */
}

/** *********************************************************** */
// Elaborado el 06-04-2011
function calcular(obj, operacion) {
	var contador = 0;
	var pagoMatricula = 0;
	var montoBCV = document.formDatosSolicitud.montoBCV.value;
	var montoPeriodo = document.formDatosSolicitud.mo_periodo.value;
	var montoMatricula = document.formDatosSolicitud.mo_matricula.value;
	var beneficioCompartido = document.formDatosSolicitud.beneficioCompartido.value;
	var montoBCVPeriodo = 0;
	var montoBCVMatricula = 0;

	// Cambiando el signo decimal ',' por '.' en los campos montoBCV y
	// otrosPagos
	montoBCV = montoBCV.replace('.', '');
	montoBCV = montoBCV.replace(',', '.');

	montoBCV = parseFloat(montoBCV);
	montoMatricula = parseFloat(montoMatricula);

	if (operacion == '1') {
		for (i = 1; i < document.formDatosSolicitud.mesesPorPagar.length; i++) {
			// compruebo si la opci�n actual i esta selecciona por el usuario
			if (document.formDatosSolicitud.mesesPorPagar.options[i].selected == true) {
				/** Verificamos si es matricula e incrementamos matricula */
				/** Matricula es igual a 14 */
				if (document.formDatosSolicitud.mesesPorPagar.options[i].value == 14) {
					pagoMatricula++;
				}
				contador = contador + 1;
			}
		}
	}
	if (beneficioCompartido == 'S' || beneficioCompartido == 's') {
		if (0.5 * montoMatricula <= montoBCV) {
			montoBCVMatricula = 0.5 * montoMatricula;
		} else {
			montoBCVMatricula = montoBCV;
		}
		if (0.5 * montoPeriodo <= montoBCV) {
			montoBCVPeriodo = 0.5 * montoPeriodo;
		} else {
			montoBCVPeriodo = montoBCV;
		}
	} else {
		if (beneficioCompartido == 'N' || beneficioCompartido == 'n') {
			if ((montoMatricula <= montoBCV)) {
				montoBCVMatricula = montoMatricula;
			} else {
				montoBCVMatricula = montoBCV;
			}
			if (montoPeriodo <= montoBCV) {
				montoBCVPeriodo = montoPeriodo;
			} else {
				montoBCVPeriodo = montoBCV;
			}
		}

	}
	// alert('montoBCVMatricula='+montoBCVMatricula+',montoBCVPeriodo='+montoBCVPeriodo+',
	// montoMatricula='+montoMatricula+', montoBCV='+montoBCV);
	if (pagoMatricula == 1) {
		/**
		 * Calculamos el monto periodo, contador le restamos 1, porque el
		 * matricula no se cuenta
		 */

		obj.value = parseFloat((contador - 1) * montoBCVPeriodo)
				+ parseFloat(montoBCVMatricula);

		obj.value = formato_numero(obj.value, 2, ',', '.');

	} else {
		obj.value = parseFloat(contador * montoBCVPeriodo);
		/* +parseFloat(otrosPagos); */
		obj.value = formato_numero(obj.value, 2, ',', '.');
	}
	if (operacion == '1') {
		document.formDatosSolicitud.registroPagos.disabled = false;
	}
}

/** *********************************************************** */
// Elaborado el 06-04-2011
function calcularPago(obj, tipoPago) {
	var contador = 0;
	var campo = document.formDatosSolicitud;
	if (tipoPago == '3') {
		var maximoAporteBCV = document.formDatosSolicitud.maximoAporteBCV.value;
		var pagoRegistrado = document.formDatosSolicitud.pagoRegistrado.value;
		var pago = document.formDatosSolicitud.pago.value;
		var porPagar;
	}
	for (i = 1; i < campo.mesesPorPagar.length; i++) {
		// compruebo si la opci�n actual i esta selecciona por el usuario
		if (document.formDatosSolicitud.mesesPorPagar.options[i].selected == true) {
			contador = contador + 1;
		}
	}
	if (tipoPago == '2') {
		campo.montoTotal.value = parseFloat(contador * obj.value);
		campo.montoTotal.value = formato_numero(campo.montoTotal.value, 2, ',',
				'.');
		obj.value = formato_numero(obj.value, 2, ',', '.');
	} else {
		if (tipoPago == '3') {
			maximoAporteBCV = maximoAporteBCV.replace(',', '.');
			pagoRegistrado = pagoRegistrado.replace(',', '.');
			pago = pago.replace(',', '.');
			// pago= pago.replace('.','').replace(',','.');
			/*
			 * alert("Monto Aporte m�ximo BCV: "+maximoAporteBCV); alert("Monto
			 * Registrado: "+pagoRegistrado); alert("Monto A pagar: "+pago);
			 */
			// porPagar=parseFloat(maximoAporteBCV)-parseFloat(pagoRegistrado)-parseFloat(pago);
			porPagar = parseFloat(maximoAporteBCV - pagoRegistrado - pago);
			// alert("Monto por pagar: "+porPagar);
			/*
			 * if(parseFloat(porPagar) < parseFloat('0.0')){ alert("El monto
			 * ingresado no es v\u00e1lido. El monto m�ximo posible a ingresar es:
			 * "+parseFloat(maximoAporteBCV)-parseFloat(pagoRegistrado));
			 * obj.value='0.0'; campo.pagoPendiente.value='0.0'; obj.focus();
			 * }else{ campo.pagoPendiente.value=parseFloat(porPagar); }
			 */
			campo.pagoPendiente.value = parseFloat(porPagar);
			if (porPagar < 0) {
				alert("El monto ingresado no es v\u00e1lido. Debe ingresar un monto menor o igual a: "
						+ parseFloat(maximoAporteBCV - pagoRegistrado));
				obj.value = '0.0';
				campo.pagoPendiente.value = '0.0';
				obj.focus();
			}
			campo.pagoPendiente.value = formato_numero(
					campo.pagoPendiente.value, 2, ',', '.');
			campo.pagoRegistrado.value = formato_numero(
					campo.pagoRegistrado.value, 2, ',', '.');
			obj.value = formato_numero(obj.value, 2, ',', '.');
		}

	}
	document.formDatosSolicitud.registroPagos.disabled = false;

}

/** *********************************************************** */

function changeShowHideReembolsoComplemento(obj) {

	var campo = document.formDatosSolicitud;
	document.getElementById('showtxtComplemento').style.display = 'none';
	document.getElementById('showtxtComplementoArea').style.display = 'none';
	document.getElementById('showComplementoMonto').style.display = 'none';
	document.getElementById('showComplementoMonto2').style.display = 'none';
	document.getElementById('txtcomplementoTxtArea').value = '';
	document.getElementById('showtxtComplementomestext').style.display = 'none';
	document.getElementById('showtxtComplementomes').style.display = 'none';

	if (obj.checked) {
		document.getElementById('showtxtComplemento').style.display = 'block';
		document.getElementById('showtxtComplementoArea').style.display = 'block';
		document.getElementById('showComplementoMonto').style.display = 'block';
		document.getElementById('showComplementoMonto2').style.display = 'block';
		// mesesPorPagar
		for (var i = 0; i < campo.mesesPorPagar.length; i++) {
			if (campo.mesesPorPagar.options[i].value == ''
					|| campo.mesesPorPagar.options[i].selected) {
				if (campo.mesesPorPagar.options[i].value == '') {
					document.getElementById('showtxtComplementomestext').style.display = 'block';
					document.getElementById('showtxtComplementomes').style.display = 'block';
				}
			}
		}

	}
}

function turnOnOffmesApagar(obj) {
	var campo = document.formDatosSolicitud;
	/**
	 * Si existe por lo menos un receptor de pago, activamops el mes de
	 * complemento o no
	 */

	document.getElementById('showtxtComplementomesCheck').style.display = 'block';
	campo.showCheckCompklement.checked = true;
	document.getElementById('showtxtComplemento').style.display = 'block';
	document.getElementById('showtxtComplementoArea').style.display = 'block';
	document.getElementById('showComplementoMonto').style.display = 'block';
	document.getElementById('showComplementoMonto2').style.display = 'block';
	// mesesPorPagar
	for (var i = 0; i < campo.mesesPorPagar.length; i++) {
		if (campo.mesesPorPagar.options[i].value == ''
				|| campo.mesesPorPagar.options[i].selected) {
			if (campo.mesesPorPagar.options[i].value == '') {
				document.getElementById('showtxtComplementomestext').style.display = 'block';
				document.getElementById('showtxtComplementomes').style.display = 'block';
			}
		}
	}

	for (var i = 0; i < campo.mesesPorPagar.length; i++) {
		if (campo.mesesPorPagar.options[i].selected) {
			if (campo.mesesPorPagar.options[i].value != '') {
				campo.showCheckCompklement.checked = false;
				document.getElementById('showtxtComplementomesCheck').style.display = 'none';
				document.getElementById('showtxtComplemento').style.display = 'none';
				document.getElementById('showtxtComplementoArea').style.display = 'none';
				document.getElementById('showComplementoMonto').style.display = 'none';
				document.getElementById('showComplementoMonto2').style.display = 'none';
				document.getElementById('txtcomplementoTxtArea').value = '';
				document.getElementById('showtxtComplementomestext').style.display = 'none';
				document.getElementById('showtxtComplementomes').style.display = 'none';
			}
		}
	}

}

function activarConcepto(obj) {
	var campo = document.formDatosSolicitud;
	if ((obj.options[1].selected) && (obj.options[1].value == "14")) {
		campo.conceptoPago[0].checked = "checked";
	}
	if (!(obj.options[1].selected) && (obj.options[1].value == "14")) {
		campo.conceptoPago[0].checked = false;
	}
	for (i = 1; i < campo.mesesPorPagar.length; i++) {
		if ((obj.options[i].selected) && (obj.options[i].value != "14")) {
			campo.conceptoPago[1].checked = "checked";
			break;
		} else {
			campo.conceptoPago[1].checked = false;
		}
	}
}

/** *********************************************************** */

function verificarCombos3(objeto) {
	var campo = document.formDatosSolicitud;
	if (objeto.value == 'S') {

		campo.tlfconyuge.disabled = false;
		campo.correoConyuge.disabled = false;
		document.getElementById('tlfconyugetxtshow').style.display = 'block';
		document.getElementById('tlfconyugeshow').style.display = 'block';
		document.getElementById('nombreempresatxt').style.display = 'block';
		document.getElementById('nombreempresashow').style.display = 'block';
		document.getElementById('telefonoempresatxt').style.display = 'block';
		document.getElementById('telefonoempresashow').style.display = 'block';
		document.getElementById('empresatxt').style.display = 'block';
		document.getElementById('montoAporteEmptxt').style.display = 'block';
		document.getElementById('montoAporteEmpshow').style.display = 'block';
		document.getElementById('correoconyugetxtshow').style.display = 'block';
		document.getElementById('correoconyugeshow').style.display = 'block';
		document.getElementById('ciConyugetxtshow').style.display = 'block';
		document.getElementById('ciConyugeshow').style.display = 'block';
	}
	if (objeto.value == 'N') {
		campo.correoConyuge.value = "";
		campo.tlfconyuge.disabled = true;
		campo.correoConyuge.disabled = true;
		document.getElementById('tlfconyugetxtshow').style.display = 'none';
		document.getElementById('tlfconyugeshow').style.display = 'none';
		document.getElementById('correoconyugetxtshow').style.display = 'none';
		document.getElementById('nombreempresatxt').style.display = 'none';
		document.getElementById('nombreempresashow').style.display = 'none';
		document.getElementById('telefonoempresatxt').style.display = 'none';
		document.getElementById('telefonoempresashow').style.display = 'none';
		document.getElementById('empresatxt').style.display = 'none';
		document.getElementById('montoAporteEmptxt').style.display = 'none';
		document.getElementById('montoAporteEmpshow').style.display = 'none';
		document.getElementById('correoconyugeshow').style.display = 'none';
		document.getElementById('ciConyugetxtshow').style.display = 'none';
		document.getElementById('ciConyugeshow').style.display = 'none';
	}
}

function showConyuge() {
	var campo = document.formDatosSolicitud;
	if (document.formDatosSolicitud.benefCompartido[0].checked) {
		campo.tlfconyuge.disabled = false;
		campo.correoConyuge.disabled = false;
		document.getElementById('tlfconyugetxtshow').style.display = 'block';
		document.getElementById('tlfconyugeshow').style.display = 'block';
		document.getElementById('nombreempresatxt').style.display = 'block';
		document.getElementById('nombreempresashow').style.display = 'block';
		document.getElementById('telefonoempresatxt').style.display = 'block';
		document.getElementById('telefonoempresashow').style.display = 'block';
		document.getElementById('empresatxt').style.display = 'block';
		document.getElementById('montoAporteEmptxt').style.display = 'block';
		document.getElementById('montoAporteEmpshow').style.display = 'block';
		document.getElementById('correoconyugetxtshow').style.display = 'block';
		document.getElementById('correoconyugeshow').style.display = 'block';
		document.getElementById('ciConyugetxtshow').style.display = 'block';
		document.getElementById('ciConyugeshow').style.display = 'block';
	}

}

/** *********************************************************** */

function chequea2(elemento, boton) {
	var campo = document.formBuscarDatos;
	if (elemento.value != "") {
		boton.disabled = false;
	} else {
		boton.disabled = true;
		campo.codigoBenef.value = "";
		campo.codigoBenef.disabled = true;
	}
}

/** *********************************************************** */

function chequea3(elemento, boton) {
	var campo = document.formBuscarDatos;
	if (elemento.value != "") {
		boton.disabled = false;
	} else {
		boton.disabled = true;
	}
}

function pulsar(e) {
	tecla = (document.all) ? e.keyCode : e.which;
	return (tecla != 13);
}

/** *********************************************************** */

function chequea5(elemento, boton) {
	// var campo = document.formBuscarDatos;
	if (elemento.value != "") {
		boton.disabled = false;
	} else {
		boton.disabled = true;

	}
}
/** *********************************************************** */

function buscarFamiliares() {
	var bandera = false;
	var campo = document.formBuscarDatos;
	if (validarVacio(campo.cedula)) {
		if (validarCedula(campo.cedula)) {
			bandera = true;
		}
	}
	if (bandera) {
		campo.action = '../../rhei/CargarCombosServlet';
		campo.submit();
	}
}

function benefCodPeriodoPagoControlador() {
	var bandera = false;
	var campo = document.formBuscarDatos;
	if (validarVacio(campo.cedula)) {
		if (validarCedula(campo.cedula)) {
			bandera = true;
		}
	}
	if (bandera) {
		campo.action = '/rhei/benefCodPeriodoPagoControlador?principal.do=buscarBenefAndCentrEduc';
		campo.submit();
	}
}

function desincorporarByAnio() {
	var campo = document.formBuscarDatos;
	var periodoEscolar = campo.periodoEscolar;
	var periodoEscolarValor = periodoEscolar.options[periodoEscolar.selectedIndex].value;
	if ('' != periodoEscolarValor) {
    	var msg= "La solicitud por a\u00f1o escolar "+periodoEscolarValor+ " ser\u00e1 desincorporada.\n Desea continuar?";
		if (confirm(msg)) {
			campo.action = '/rhei/solicitudDesincorporarControlador?principal.do=desincorporarSolicitudByAnio&periodoEscolarValor='
					+ periodoEscolarValor;
			campo.submit();
		}
	}
}

function buscarBeneficiario() {
	var bandera = false;
	var campo = document.formBuscarDatos;
	if (validarVacio(campo.cedula)) {
		if (validarCedula(campo.cedula)) {
			bandera = true;
		}
	}
	if (bandera) {

		campo.action = '/rhei/benefAndCentrEducControlador?principal.do=buscarBenefAndCentrEduc';
		campo.submit();
	}
}
/** *********************************************************** */

function buscarFamiliares2() {
	var bandera = false;
	var campo = document.formBuscarDatos;
	if (validarVacio(campo.cedula)) {
		if (validarCedula(campo.cedula)) {
			bandera = true;
		}
	}
	if (bandera) {
		campo.action = '../../rhei/CargarCombosServlet2';
		campo.submit();
	}
}

/** *********************************************************** */

function validar3() {
	var campo = document.formBuscarDatos;
	// alert("Debe oprimir el bot\u00f3n Buscar Familiar");
	campo.buscarDatos2.disabled = true;
	campo.buscarDatos1.disabled = false;
	campo.buscarDatos1.focus();
}
/** *********************************************************** */
function validar4() {
	var campo = document.formBuscarDatos;
	// alert("Debe oprimir el bot\u00f3n Buscar Familiar");
	campo.buscar1.disabled = true;
	campo.buscarDatos1.disabled = false;
	campo.buscarDatos1.focus();
}
/** *********************************************************** */

function buscar1() {
	var bandera = true;
	var formatoCorrecto = false;
	var existe = false, existe2 = false;
	var vacio = true;
	var campo = document.formBuscarDatos;
	if (campo.cedula.value.length != 0) {
		vacio = false;
	}
	if (validarCedula2(campo.cedula)) {
		formatoCorrecto = true;
	}
	if (document.forms[0].elements['codigoBenef']) {
		existe = true;
	}
	if (document.forms[0].elements['nroRifCentroEdu']) {
		existe2 = true;
	}

	if (vacio == true
			|| (vacio == false && formatoCorrecto == false)
			|| (vacio == false && formatoCorrecto == false && existe == false && existe2 == false)) {
		alert("Ingrese un n\u00famero de c\u00e9dula con formato v\u00e1lido...");
		campo.cedula.focus();
		bandera = false;
	}
	if (vacio == false && formatoCorrecto == true && existe == false) {
		alert("Debe oprimir el bot\u00f3n Buscar Beneficiario");
		campo.buscarDatos1.disabled = false;
		campo.buscarDatos1.focus();
		bandera = false;
	}
	if (vacio == false && formatoCorrecto == true && existe == true) {
		if (campo.codigoBenef.value == "") {
			alert("Debe seleccionar un beneficiario de la lista de opciones desplegada...");
			campo.codigoBenef.focus();
			bandera = false;
		}
		if (campo.codigoBenef.value != "" && campo.nroRifCentroEdu.value == "") {
			alert("Debe seleccionar el n\u00famero de rif del instituto educativo de la lista de opciones desplegada...");
			campo.nroRifCentroEdu.focus();
			bandera = false;
		}
	}
	if (bandera) {
		campo.action = '../rhei/CargarDatosServlet2';
		campo.submit();
	}
}

/** *********************************************************** */
function buscar2() {
	var bandera = true;
	var formatoCorrecto = false;
	var existe = false;
	var vacio = true, vacio2 = true;
	var campo = document.formBuscarDatos;
	if (campo.cedula.value.length != 0) {
		vacio = false;
	}
	if (validarCedula2(campo.cedula)) {
		formatoCorrecto = true;
	}
	if (document.forms[0].elements['codigoBenef']) {
		existe = true;
	}

	if (campo.numSolicitud.value.length != 0) {
		vacio2 = false;
	}
	if (vacio == true || (vacio == false && formatoCorrecto == false)
			|| (vacio == false && formatoCorrecto == false && existe == false)) {
		alert("Ingrese un n\u00famero de c\u00e9dula con formato v\u00e1lido...");
		campo.numSolicitud.value = "";
		campo.cedula.focus();
		bandera = false;
	}
	if (vacio == false && formatoCorrecto == true && existe == false) {
		alert("Debe oprimir el bot\u00f3n Buscar Beneficiario");
		campo.numSolicitud.value = "";
		campo.buscarDatos1.disabled = false;
		// campo.buscarDatos2.disabled=false;
		campo.buscarDatos1.focus();
		bandera = false;
	}
	if (vacio == false && formatoCorrecto == true && existe == true) {
		if (campo.codigoBenef.value == "") {
			alert("Debe seleccionar un beneficiario de la lista de opciones desplegada...");
			campo.codigoBenef.focus();
			bandera = false;
		}
	}
	if (bandera) {
		campo.action = '/rhei/CargarDatosServlet2';
		campo.submit();
	}
}

function intitucionSend() {
	var bandera = true;
	var campo = document.formDatosSolicitud;
	if (bandera) {
		document.getElementById('benefwait').style.display = 'block';
		campo.submit();
	}
}

function showDataOfTrabAndBenef() {
	var bandera = true;
	var formatoCorrecto = false;
	var existe = false, existe2 = false;
	var vacio = true;
	var campo = document.formBuscarDatos;
	if (campo.cedula.value.length != 0) {
		vacio = false;
	}
	if (validarCedula2(campo.cedula)) {
		formatoCorrecto = true;
	}
	if (document.forms[0].elements['codigoBenef']) {
		existe = true;
	}
	if (document.forms[0].elements['nroRifCentroEdu']) {
		existe2 = true;
	}

	if (vacio == true
			|| (vacio == false && formatoCorrecto == false)
			|| (vacio == false && formatoCorrecto == false && existe == false && existe2 == false)) {
		alert("Ingrese un n\u00famero de c\u00e9dula con formato v\u00e1lido...");
		campo.cedula.focus();
		bandera = false;
	}
	if (vacio == false && formatoCorrecto == true && existe == false) {
		alert("Debe oprimir el bot\u00f3n Buscar Beneficiario");
		campo.buscarDatos1.disabled = false;
		campo.buscarDatos1.focus();
		bandera = false;
	}
	if (vacio == false && formatoCorrecto == true && existe == true) {
		if (campo.codigoBenef.value == "") {
			alert("Debe seleccionar un beneficiario de la lista de opciones desplegada...");
			campo.codigoBenef.focus();
			bandera = false;
		}
		// if (campo.codigoBenef.value != "" && campo.nroRifCentroEdu.value ==
		// "") {
		// alert("Debe seleccionar el n�mero de rif del instituto educativo de
		// la lista de opciones desplegada...");
		// campo.nroRifCentroEdu.focus();
		// bandera = false;
		// }
	}
	if (bandera) {
		campo.submit();
	}
}

function buscarReporteBenef() {
	var bandera = false;

	var isFactura = false;
	var isPeriodoSelec = false;
	var campo = document.formBuscarDatos;

	if (campo.factura.value.length != 0) {
		isFactura = true;
	}

	if (document.forms[0].elements['periodoEscolar']) {
		if (campo.periodoEscolar.value.length != 0) {
			isPeriodoSelec = true;
		}
	}

	/** Debes seleccionar minimo o un codigo beneficiario o un numero factura */
	if ((!isPeriodoSelec) && (!isFactura)) {
		alert("Ingrese un Nro Factura  o debe seleccionar el per\u00edodo Escolar.");		
	} else {
		bandera = true;
	}

	if (bandera) {
		campo.submit();
	}
}

function buscarPagosFacturasId() {
	var bandera = false;
	var campo = document.formBuscarDatos;
	if (campo.factura.value.length != 0) {
		bandera = true;
	}
	if (bandera) {
		document.getElementById('benefwait').style.display = 'block';
		campo.submit();
	}

}

function buscarPagosFacturas() {
	var bandera = false;
	var isCodigoBenef = false;
	var isFactura = false;
	var isPeriodoSelec = false;
	var isMeseSelec = false;
	var campo = document.formBuscarDatos;

	if (document.forms[0].elements['codigoBenef']) {
		if (campo.codigoBenef.value.length != 0) {
			isCodigoBenef = true;
		}
	}

	/** Debes seleccionar minimo o un codigo beneficiario o un numero factura */
	if (((!isCodigoBenef))) {
		alert("Ingrese  un beneficiario de la lista de opciones desplegada...");
	} else {

		bandera = true;

	}

	if (bandera) {
		document.getElementById('benefwait').style.display = 'block';
		campo.submit();
	}
}

function buscarSolicitudFromInstitucion(page) {
	var bandera = false;
	var campo = document.formDatosSolicitud;

	if (validarVacio(campo.numSolicitud)) {
		bandera = true;
	} else {
		campo.numSolicitud.focus();
	}

	if (bandera) {

		document.getElementById('benefwait').style.display = 'block';
		if (page == 'solicitudDesincorporar.jsp') {
			campo.action = '/rhei/solicitudDesincorporarControlador?principal.do=desincorporarInfoDataTrabBenef&accion='
					+ page;
		} else if (page == 'pagoConvRegistrar.jsp') {
			campo.action = '/rhei/pagosControladorReg?principal.do=buscarData&accion='
					+ page;
		} else if (page == 'solicitudConsultar.jsp') {
			campo.action = '/rhei/solicitudControladorConsultar?principal.do=consultInfoDataTrabBenef&accion='
					+ page;
		} else if (page == 'solicitudActualizar.jsp') {
			campo.action = '/rhei/solicitudControladorActualizar?principal.do=actualizarInfoDataTrabBenef&accion='
					+ page;
		}

		campo.submit();
	}
}

function buscarSolicitud() {
	var bandera = false;
	var campo = document.formBuscarDatos;
	if (campo.cedula.value.length != 0 && campo.numSolicitud.length == 0) {
		alert("Debe oprimir el otro bot\u00f3n 'Buscar Datos Solicitud' ya que est\u00e1 filtrando por n\u00famero de c\u00e9dula");
		if (document.forms[0].elements['buscarDatos1']) {
			campo.buscarDatos1.focus();
		} else {
			campo.buscarDatos2.focus();
		}
	} else {
		if (validarVacio(campo.numSolicitud)
				&& (campo.cedula.value.length != 0 || campo.cedula.value.length == 0)) {
			bandera = true;

			campo.cedula.value = "";
			campo.cedula.disabled = true;
		} else {
			campo.numSolicitud.focus();
		}
	}
	if (bandera) {
		document.getElementById('benefwait').style.display = 'block';
		campo.submit();
	}
}

/** *********************************************************** */
function buscar3() {
	var bandera = false;
	var campo = document.formBuscarDatos;
	if (campo.cedula.value.length != 0 && campo.numSolicitud.length == 0) {
		alert("Debe oprimir el otro bot\u00f3n 'Buscar Datos Solicitud' ya que est\u00e1 filtrando por n\u00famero de c\u00e9dula");
		if (document.forms[0].elements['buscarDatos1']) {
			campo.buscarDatos1.focus();
		} else {
			// if(existeCampo2()){
			// campo.codigoBenef.focus();
			// }
			campo.buscarDatos2.focus();
		}
	} else {
		if (validarVacio(campo.numSolicitud)
				&& (campo.cedula.value.length != 0 || campo.cedula.value.length == 0)) {
			bandera = true;
			/*
			 * if (existeCampo2()) { campo.codigoBenef.disabled=true; }
			 */
			campo.cedula.value = "";
			campo.cedula.disabled = true;
		} else {
			campo.numSolicitud.focus();
		}
	}
	if (bandera) {
		campo.action = '/rhei/CargarDatosServlet2';
		campo.submit();
	}
}

/** *********************************************************** */
/* M�todo modificado el 19 de Septiembre 2011 */
/*
 * function buscar4(origen){ var bandera = true; var formatoCorrecto = false;
 * var existe=false; var vacio=true; var campo = document.formBuscarDatos;
 * if(campo.cedula.value.length!=0){ vacio=false; }
 * if(validarCedula2(campo.cedula)){ formatoCorrecto=true; } if
 * (document.forms[0].elements['codigoBenef']) { existe=true; } if(vacio==true ||
 * vacio==false && formatoCorrecto==false){ alert("Ingrese un n\u00famero de c\u00e9dula
 * con formato v\u00e1lido..."); campo.cedula.focus(); bandera=false; }
 * if(vacio==false && formatoCorrecto==true && existe==true &&
 * campo.codigoBenef.value==""){ alert("Debe seleccionar un beneficiario de la
 * lista de opciones desplegada..."); campo.codigoBenef.focus(); bandera=false; }
 * if(vacio==false && formatoCorrecto==true && existe==false){ alert("Debe
 * oprimir el bot\u00f3n Buscar Familiar"); campo.buscarDatos1.disabled=false;
 * campo.buscarDatos1.focus(); bandera=false; } if(bandera && origen !=
 * 'PagoNoConvencional'){ campo.action='../rhei/CargarDatosServlet3';
 * //Modificado el 07-04-2011, original: CargarDatosServlet3 campo.submit();
 * }else{ if(bandera && origen == 'PagoNoConvencional'){
 * campo.action='../rhei/CargarDatosPagoNoConvencional'; //Modificado el
 * 07-04-2011, original: CargarDatosServlet3 campo.submit(); } } }
 */
/** *********************************************************** */
function buscar4() {
	var bandera = true;
	var formatoCorrecto = false;
	var existe = false;
	var vacio = true;
	var campo = document.formBuscarDatos;
	if (campo.cedula.value.length != 0) {
		vacio = false;
	}
	if (validarCedula2(campo.cedula)) {
		formatoCorrecto = true;
	}
	if (document.forms[0].elements['codigoBenef']) {
		existe = true;
	}
	if (vacio == true || vacio == false && formatoCorrecto == false) {
		alert("Ingrese un n\u00famero de c\u00e9dula con formato v\u00e1lido...");
		campo.cedula.focus();
		bandera = false;
	}
	if (vacio == false && formatoCorrecto == true && existe == true
			&& campo.codigoBenef.value == "") {
		alert("Debe seleccionar un beneficiario de la lista de opciones desplegada...");
		campo.codigoBenef.focus();
		bandera = false;
	}
	if (vacio == false && formatoCorrecto == true && existe == false) {
		alert("Debe oprimir el bot\u00f3n Buscar Beneficiario");
		campo.buscarDatos1.disabled = false;
		campo.buscarDatos1.focus();
		bandera = false;
	}
	if (bandera) {
		campo.action = '../rhei/CargarDatosServlet3';
		campo.submit();
	}
}
/** *********************************************************** */
function buscar5() {
	var bandera = true;
	var formatoCorrecto = false;
	var existe = false, existe2 = false;
	var vacio = true, vacio2 = true;
	var campo = document.formBuscarDatos;
	if (campo.cedula.value.length != 0) {
		vacio = false;
	}
	if (validarCedula2(campo.cedula)) {
		formatoCorrecto = true;
	}
	if (document.forms[0].elements['codigoBenef']) {
		existe = true;
	}
	if (document.forms[0].elements['meses']) {
		existe = true;
	}
	/*
	 * if(campo.factura.value.length!=0){ vacio2=false; }
	 */
	if (vacio == true
			|| (vacio == false && formatoCorrecto == false)
			|| (vacio == false && formatoCorrecto == false && existe == false && existe2 == false)) {
		alert("Ingrese un n\u00famero de c\u00e9dula con formato v\u00e1lido...");
		campo.factura.value = "";
		campo.cedula.focus();
		bandera = false;
	}
	if (vacio == false && formatoCorrecto == true && existe == false) {
		alert("Debe oprimir el bot\u00f3n Buscar Beneficiario");
		campo.factura.value = "";
		campo.buscarDatos1.disabled = false;
		campo.buscarDatos1.focus();
		bandera = false;
	}
	if (vacio == false && formatoCorrecto == true && existe == true) {
		if (campo.codigoBenef.value == "") {
			alert("Debe seleccionar un beneficiario de la lista de opciones desplegada...");
			campo.codigoBenef.focus();
			bandera = false;
		}
		if (campo.codigoBenef.value != "" && campo.meses.value == "") {
			alert("Debe seleccionar un mes de la lista de opciones desplegada...");
			campo.meses.focus();
			bandera = false;
		}
	}
	if (bandera) {
		campo.action = '../rhei/CargarDatosServlet3';
		campo.submit();
	}
}

function guardarDatos() {
	var bandera = false;
	var campo = document.formDatosSolicitud;

	if (validarRegistro()) {
		bandera = true;
	}

	if (bandera) {
		campo.action = 'GuardarSolicitudServlet';
		campo.submit();
	}
}

function deletePeriodo() {
	var bandera = false;
	 bandera = confirm ("Seguro desea eliminar el periodo escolar ? ");
	return bandera;
}

function deleteParam() {
	var bandera = false;
	 bandera = confirm ("Seguro desea eliminar el parametro ? ");
	return bandera;
}

function deleteBeneficiario(co_tipo_beneficio) {
	var campo = document.formPaginarBeneficios;

	var bandera = false;
	 bandera = confirm ("Seguro desea eliminar el beneficio "+co_tipo_beneficio+" ?");
	if (bandera) {
		campo.action = '/rhei/benefScolarControlador?principal.do=delete&co_tipo_beneficio='
				+ co_tipo_beneficio;
		campo.submit();
	}
}

function guardarSolicitud() {

	var bandera = true;
	var campo = document.formDatosSolicitud;

	if (campo.benefCompartido[0].checked) {
		bandera = false;
		if (validarVacio(campo.tlfconyuge)) {
			if (validarVacio(campo.ciConyuge)) {
				if (valEmail(campo.correoConyuge)) {
					if (validarVacio(campo.nombreempresa)) {
						if (validarVacio(campo.telefonoempresa)) {
							bandera = true;
						}
					}
				}
			}
		}
	}

	if (bandera) {
		document.getElementById('messagewait').style.display = 'block';
		campo.action = '/rhei/solicitudControladorIncluir?principal.do=incluirSolicitud';
		campo.submit();
	}

}

/** *********************************************************** */

function valEmail(obj) {
	re = /^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+.([a-zA-Z])+([a-zA-Z])$/;
	if (!re.exec(obj.value)) {
		alert("Invalido formato del correo.");
		return false;
	} else {
		return true;
	}
}

function guardarCambios() {
	var bandera = false;
	var banderaConyuge = true;
	var campo = document.formDatosSolicitud;

	/**
	 * Si el usuario tiene un hijo mayor a 7 anos, validamos que sea especial
	 * para registrarlo******
	 */
	if (validTipoEduc()) {
		bandera = validarRegistro();
		if (campo.benefCompartido[0].checked) {
			banderaConyuge = false;
			if (validarVacio(campo.tlfconyuge)) {
				if (validarVacio(campo.ciConyuge)) {
					if (valEmail(campo.correoConyuge)) {
						if (validarVacio(campo.nombreempresa)) {
							if (validarVacio(campo.telefonoempresa)) {
								banderaConyuge = true;
							}
						}
					}
				}
			}
		}
		if (bandera && banderaConyuge) {
			document.getElementById('messagewait').style.display = 'block';
			campo.submit();
		}
	}

}

/** *********************************************************** */
function validarRegistro2() {
	var bandera = false;
	var campo = document.formDatosSolicitud;
	if (validarVacio(campo.montoPeriodo)) {
		if (validarMontoValido(campo.montoPeriodo)) {
			if (validarVacio(campo.montoMatricula)) {
				if (validarMontoValido(campo.montoMatricula)) {
					if (validarVacio(campo.montoBCV)) {
						if (validarMontoValido(campo.montoBCV)) {
							bandera = true;
						}
					}
				}
			}
		}
	}
	return bandera;
}
/** *********************************************************** */
function validarRegistro() {
	var bandera = false;
	var campo = document.formDatosSolicitud;

	if (validarVacio(campo.tipoInstitucion)) {
		if (validarVacio(campo.tipoEducacion)) {
			if (validarVacio(campo.nivelEscolaridad)) {
				if (validarVacio(campo.periodoPago)) {

					if (validarVacio(campo.montoPeriodo)) {
						if (validarMontoValido(campo.montoPeriodo)) {
							if (validarVacio(campo.montoMatricula)) {
								if (validarMontoValido(campo.montoMatricula)) {
									if (validarVacio(campo.montoBCV)) {
										if (validarMontoValido(campo.montoBCV)) {
											bandera = true;
										}
									}
								}
							}
						}

					}
				}
			}
		}
	}

	return bandera;
}

/** *********************************************************** */

function seleccionar_todo(form, grupo, marca) {
	for (i = 0; i < form.elements.length; i++) {
		if (form.elements[i].type == "checkbox") {
			form.elements[i].checked = marca;
		}
	}
}

/** *********************************************************** */
function verificarEleccion() {
	var campo = document.formulario1;
	var bandera = false;

	if (confirm("La(s) solicitud(es) seleccionada(s) ser\u00e1(n) desincorporada(s).\n Desea continuar?")) {
		for (i = 0; i < campo.elements.length; i++) {
			if (campo.elements[i].type == "checkbox"
					&& campo.elements[i].checked == true) {
				bandera = true;
				break;
			}
		}
		if (bandera) {
			// campo.action = 'GuardarSolicitudServlet';
			campo.action = '/rhei/solicitudDesincorporarControlador?principal.do=desincorporarSolicitud';
			campo.submit();
		} else {
			alert("Debe seleccionar el (los) registro(s) que desea eliminar haciendo un click sobre el checkbox correspondiente de la columna 'BORRAR'");
			for (i = 0; i < campo.elements.length; i++) {
				if (campo.elements[i].type == "checkbox") {
					campo.elements[i].focus();
					break;
				}
			}
		}

	}
}
/** *********************************************************** */
function verificarReceptorFacturaII(obj) {
	var campo = document.formDatosSolicitud;

	if (obj.options[0].selected == true) {
          alert("El campo Pago dirigido no puede estar vac\u00edo");
		return false;
		/** Si es centro de educacion, entonces obj.options[1].selected == true */
	} else if (obj.options[1].selected == true) {
		return true;

		/** Si es Trabajador, entonces obj.options[2].selected == true */
	} else if (obj.options[2].selected == true) {
		return true;
	}
}

function verificarReceptorFactura(obj) {
	var campo = document.formDatosSolicitud;
	// campo.montoFactura.value
	if (obj.options[0].selected == true) {
		if (campo.nroFactura.value == 'Pendiente Entrega') {
			if (campo.nroControl.value == 'Pendiente Entrega') {
				if (campo.montoFactura.value == '0,00') {
					return true;
				} else {
					campo.montoFactura.value = '0,00';
					return true;
				}
			} else {
				if (campo.montoFactura.value == '0,00') {
					campo.nroControl.value = '';
					return true;
				} else {
					campo.nroControl.value = '';
					campo.montoFactura.value = '0,00';
					return true;
				}
			}
		} else {
			if (campo.nroControl.value == 'Pendiente Entrega') {
				if (campo.montoFactura.value == '0,00') {
					alert("El valor ingresado en el campo N� Control no es v\u00e1lido. ");
					campo.nroControl.value = '';
					campo.montoFactura.value = '';
					campo.montoFactura.focus();
					return false;
				} else {
					campo.nroControl.value = '';
					return true;
				}
			} else {
				if (campo.montoFactura.value == '0,00') {
					alert("Debe ingresar un monto v\u00e1lido diferente de cero en el campo Monto Factura");
					campo.montoFactura.value = '';
					campo.montoFactura.focus();
					return false;
				} else {
					return true;
				}
			}
		}
	} else {
		if (campo.nroFactura.value == 'Pendiente Entrega') {
			alert("Debe ingresar los datos de la factura ya que est\u00e1 por registrar un pago por reembolso");
			campo.nroFactura.value = '';
			campo.nroControl.value = '';
			campo.montoFactura.value = '';
			campo.nroFactura.focus();
			return false;
		} else {
			if (campo.nroControl.value == 'Pendiente Entrega') {
				if (campo.montoFactura.value == '0,00') {
					alert("El valor ingresado en el campo N� Control no es v\u00e1lido. ");
					campo.nroControl.value = '';
					campo.montoFactura.value = '';
					campo.montoFactura.focus();
					return false;
				} else {
					campo.nroControl.value = '';
					return true;
				}
			} else {
				if (campo.montoFactura.value == '0,00') {
					alert("Debe ingresar un monto v\u00e1lido diferente de cero en el campo Monto Factura");
					campo.montoFactura.value = '';
					campo.montoFactura.focus();
					return false;
				} else {
					return true;
				}
			}
		}
	}
}
/** *********************************************************** */
/** *********************************************************** */
/*
 * function verificarReceptorFactura(){ var campo = document.formDatosSolicitud;
 * if(campo.receptorPago.options[0].selected==true){
 * campo.fechaFactura.disabled=true; campo.nroFactura.disabled=true;
 * campo.nroControl.disabled=true; campo.montoFactura.disabled=true; }else{
 * campo.fechaFactura.disabled=false; campo.nroFactura.disabled=false;
 * campo.nroControl.disabled=false; campo.montoFactura.disabled=false; } return
 * document.formDatosSolicitud.options[selectedIndex].value; }
 */
/** *********************************************************** */

function validarRegistroPago() {
	var campo = document.formDatosSolicitud;
	var bandera = false;
	if (validarFormatoFecha(campo.fechaFactura)) {
		if (isValidDate(campo.fechaFactura)) {
			if (validarVacio(campo.nroFactura)) {
				if (validarVacio(campo.montoFactura)) {
					if (validarMontoValido(campo.montoFactura)) {
						if (validarConcepto(campo.conceptoPago)) {
							if (verificarReceptorFactura(campo.receptorPago)) {
								if (validarMesesAPagar(campo.mesesPorPagar)) {
									/*
									 * if(campo.otrosPagos.value!=""){
									 * if(validarMontoValido(campo.otrosPagos)){
									 * bandera=true; } }else{ bandera=true; }
									 */
									bandera = true;
								}
							}
						}
					}
				}
			}
		}
	}

	if (bandera) {
		campo.action = 'GuardarPagosServlet';
		// campo.action='jsp/principal.jsp';

		campo.submit();
	}
}

function validarActualizarPagoCtrl() {
	var campo = document.formDatosSolicitud;
	var bandera = false;

	if (validarVacio(campo.montoFactura)) {
		if (validarMontoValido(campo.montoFactura)) {
			// if (validarVacio(campo.montoTotal)) {
			// if (validarMontoValido(campo.montoTotal)) {
			// bandera = true;
			// }
			// }
			bandera = true;
		}
	}

	if (bandera) {
		campo.submit();
	}
}

function validarRegistroPagoCtrl() {

	var campo = document.formDatosSolicitud;
	var bandera = false;

	if (validarFormatoFecha(campo.fechaFactura)) {
		if (isValidDate(campo.fechaFactura)) {
			if (verificarReceptorFacturaII(campo.receptorPago)) {
				bandera = true;
			}
		}
	}
	if (campo.pagado.checked) {
		if (document.getElementById('txtobservacionespagado').value == '') {
			alert('El campo observaciones no puede estar vac\u00edo');
			bandera = false;
		}

	}

	if (bandera && validarReembolsoComplemento()) {
		document.getElementById('benefwait').style.display = 'block';
		campo.submit();
	}
}
/** *********************************************************** */
function validarReembolsoComplemento() {
	var exito = false;
	/** Chequeamos que exista un complemento o reembolso a llenar */
	if (document.getElementById('showCheckCompklement').checked) {
		exito = true;
		if (document.getElementById('txtcomplementoTxtArea').value == '') {
			alert('El campo correspondiente a "Descripci\u00f3n de pago" no puede estar vac\u00edo');
			exito = false;

		}
		// if (document.getElementById('montocomplemento').value ==
		// ''||document.getElementById('montocomplemento').value=='0'){
		// alert('El campo correspondiente a "Monto" no puede estar vac�o');
		// exito= false;
		// }
	} else {
		/**
		 * Si no Chequeamos que exista un complemento o reembolso a llenar, por
		 * lo menos que exista un mes a pagar
		 */
		for (i = 1; i < document.formDatosSolicitud.mesesPorPagar.length; i++) {
			if (document.formDatosSolicitud.mesesPorPagar.options[i].selected == true) {
				exito = true;
			}
		}
	}

	return exito;

	//

}

function validarConcepto(obj) {
	var grupo = document.getElementById("form2").conceptoPago;
	if (grupo[0].checked == false && grupo[1].checked == false) {
		alert("Debe ingresar el concepto de 'Mes(es) a Pagar' ");
		grupo[1].focus();
		return false;
	} else {
		if (grupo[0].disabled == false
				&& (grupo[0].checked == false && grupo[1].checked == false)) {
			alert("Debe ingresar al menos un concepto de pago");
			grupo[0].focus();
			return false;
		} else {
			return true;
		}
	}
}
/** *********************************************************** */
function validarMesesAPagar(obj) {
	var campo = document.formDatosSolicitud;
	var respuesta = true;
	if (campo.conceptoPago[0].checked || campo.conceptoPago[1].checked) {
		respuesta = validarVacio(campo.mesesPorPagar);
	}
	return respuesta;

}

/** *********************************************************** */
function enviar(objeto) {
	var campo = document.formBuscarDatos;
	bandera = false;
	if (objeto.name == "buscar1") {
		if (campo.cedula.value == "") {
			alert("Debe introducir el n\u00famero de c\u00e9dula del empleado");
			respuesta = false;
		} else {
			campo.nivelEscolaridad[0].value = "-1";
			campo.edadMin[0].value = "-1";
			campo.edadMax[0].value = "-1";
			respuesta = true;
		}
	}
	if (objeto.name == "buscar2") {
		if (campo.nivelEscolaridad.value == "") {
			alert("Debe introducir el nivel escolar a buscar");
			respuesta = false;
		} else {
			campo.cedula.value = "-1";
			campo.edadMin[0].value = "-1";
			campo.edadMax[0].value = "-1";
			respuesta = true;
		}
	}
	if (objeto.name == "buscar3") {
		if (campo.edadMin.value == "" || campo.edadMax.value == "") {
			alert("Debe seleccionar el rango de edad a buscar");
			respuesta = false;
		} else {
			campo.cedula.value = "-1";
			campo.nivelEscolaridad[0].value = "-1";
			respuesta = true;
		}

	}
	if (bandera) {
		campo.action = '../rhei/CargarTablaServlet';
		campo.submit();
	}
}

/** *********************************************************** */
function enviar() {
	var bandera = true;
	var formatoCorrecto = false;
	var existe = false;
	var vacio = true;
	var campo = document.formBuscarDatos;
	if (campo.cedula.value.length != 0) {
		vacio = false;
	}
	if (validarCedula2(campo.cedula)) {
		formatoCorrecto = true;
	}
	if (document.forms[0].elements['codigoBenef']) {
		existe = true;
	}
	if (vacio == true || (vacio == false && formatoCorrecto == false)
			|| (vacio == false && formatoCorrecto == false && existe == false)) {
		alert("Ingrese un n\u00famero de c\u00e9dula con formato v\u00e1lido...");
		campo.cedula.focus();
		bandera = false;
	}
	if (vacio == false && formatoCorrecto == true && existe == false) {
		alert("Debe oprimir el bot\u00f3n Buscar Beneficiario");
		campo.buscarDatos1.disabled = false;
		campo.buscarDatos1.focus();
		bandera = false;
	}
	if (vacio == false && formatoCorrecto == true && existe == true) {
		if (campo.codigoBenef.value == "") {
			alert("Debe seleccionar un beneficiario de la lista de opciones desplegada...");
			campo.codigoBenef.focus();
			bandera = false;
		}
	}
	if (bandera) {
		/*
		 * campo.nivelEscolaridad[0].value="-1"; campo.edadMin[0].value="-1";
		 * campo.edadMax[0].value="-1";
		 */
		campo.action = '../rhei/CargarTablaServlet';
		campo.submit();
	}
}

/** ****************** Funciones utilizadas ************************** */

/*
 * function validarVacio(obj){ if (obj.value.length==0){
 * if(obj.name=="numSolicitud"){ alert("El campo N� Solicitud no puede estar
 * vac\u00edo"); }else{ if(obj.name=="cedula"){ alert("El campo c\u00e9dula no puede estar
 * vac\u00edo"); }else{ if(obj.name=="nombreParametro"){ alert("El campo 'Nombre' no
 * puede estar vac\u00edo"); }else{ if(obj.name=="tipoBeneficio"){ alert("El campo
 * 'Beneficio Escolar' no puede estar vac\u00edo"); }else{
 * if(obj.name=="valorParametro"){ alert("El campo 'Valor' no puede estar
 * vac\u00edo"); }else{ if(obj.name=="fechaVigencia"){ alert("El campo 'Fecha de
 * Vigencia' no puede estar vac\u00edo"); }else{ if(obj.name=="compania"){ alert("El
 * campo 'Compa&ntilde;&iacute;a' no puede estar vac\u00edo"); }else{
 * if(obj.name=="tipoEmp"){ alert("El campo 'Tipo Empleado' no puede estar
 * vac\u00edo"); }else{ if(obj.name=="descripcion"){ alert("El campo 'Observaciones'
 * no puede estar vac\u00edo"); }else{ alert("El campo " +obj.name+ " no puede estar
 * vac\u00edo"); } } } } } } } } } obj.focus(); return false; }else return true; }
 */

function validarVacio(obj) {

	if (obj.value.length == 0) {
		if (obj.name == "tlfconyuge") {
			alert("El campo Tel\u00e9fono no puede estar vac\u00edo");
		} else

		if (obj.name == "ciConyuge") {
			alert("El campo Cu\00e9dula no puede estar vac\u00edo");
		} else

		if (obj.name == "nombreDefinitivoTransitorio") {
				alert("El campo Nombre no puede estar vac\u00edo");
		}

		else if (obj.name == "nombreempresa") {
				alert("El campo Nombre de la empresa no puede estar vac\u00edo");
		} else if (obj.name == "telefonoempresa") {
					alert("El campo Tel\u00e9fono de la empresa no puede estar vac\u00edo");
		} else if (obj.name == "numSolicitud") {
			alert("El campo N� Solicitud no puede estar vac\u00edo");
		} else {
			if (obj.name == "cedula") {
				alert("El campo C\u00e9dula no puede estar vac\u00edo");
			} else {
				if (obj.name == "montoAporteEmp") {
					alert("El campo Monto Aporte Empresa no puede estar vac\u00edo");
				} else {
					if (obj.name == "montoPeriodo") {
						alert("El campo Monto Mensualidad no puede estar vac\u00edo");
					} else {
						if (obj.name == "montoMatricula") {
							alert("El campo Monto Matr\u00edcula no puede estar vac\u00edo");
						} else {
							if (obj.name == "montoBCV") {
								alert("El campo Monto Otorgado BCV no puede estar vac\u00edo");
							} else {
								if (obj.name == "tipoInstitucion") {
									alert("El campo Tipo de CEI no puede estar vac\u00edo");
								} else {
									if (obj.name == "tipoEducacion") {
										alert("El campo Tipo Educaci\u00f3n no puede estar vac\u00edo");
									} else {
										if (obj.name == "nivelEscolaridad") {
											alert("El campo Nivel Escolaridad no puede estar vac\u00edo");
										} else {
											if (obj.name == "periodoPago") {
												alert("El campo Per\u00edodo de Pago no puede estar vac\u00edo");
											} else {
												if (obj.name == "formaPago") {
													alert("El campo Forma de Pago no puede estar vac\u00edo");
												} else {
													if (obj.name == "nroFactura") {
														alert("El campo N� Factura no puede estar vac\u00edo");
													} else {
														if (obj.name == "montoFactura") {
															alert("El campo Mes(es) a Pagar no puede estar vac\u00edo");
														} else {
															if (obj.name == "mesesPorPagar") {
																alert("El campo Mes(es) a Pagar no puede estar vac\u00edo");
															} else {
																if (obj.name == "nombreParametro") {
																	alert("El campo 'Nombre' no puede estar vac\u00edo");
																} else {
																	if (obj.name == "tipoBeneficio") {
																		alert("El campo 'Beneficio Escolar' no puede estar vac\u00edo");
																	} else {
																		if (obj.name == "valorParametro") {
																			alert("El campo 'Valor' no puede estar vac\u00edo");
																		} else {
																			if (obj.name == "fechaVigencia") {
																				alert("El campo 'Fecha de Vigencia' no puede estar vac\u00edo");
																			} else {
																				if (obj.name == "compania") {
																					alert("El campo 'Compa&ntilde;&iacute;a' no puede estar vac\u00edo");
																				} else {
																					if (obj.name == "tipoEmp") {
																						alert("El campo 'Tipo Empleado' no puede estar vacu00edo");
																					} else {
																						if (obj.name == "descripcion") {
																							alert("El campo 'Observaciones' no puede estar vac\u00edo");
																						} else {
																							alert("El campo "
																									+ obj.name
																									+ " no puede estar vac\u00edo");
																						}
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		obj.focus();
		return false;
	} else
		/** si la longitud del objeto no es igual a cero, pasa por aca */
		return true;
}

/** *********************************************************** */
function validarVacio2(obj) {
	var campo = document.formBuscarDatos;
	if (obj.value.length == 0) {
		alert("El campo " + obj.name + " no puede estar vac\u00edo");
		obj.focus();
		return false;
	} else {
		if (obj.value.length != 0 && obj.disabled == true) {
			campo.cedula.value = "";
			obj.disabled = false;
			obj.focus();
			return false;
		} else {
			return true;
		}
	}

}
/** *********************************************************** */

function validarSiNumero(obj) {
	if (!/^[1-9]{1}([0-9])*$/.test(obj.value)) {
		if (obj.name == "numSolicitud") {
			alert("El valor del campo N� Solicitud no tiene un formato v\u00e1lido");
		} else {
			/*
			 * if(obj.name=="inicio"){ alert("El valor del campo A�o de inicio
			 * del per\u00edodo escolar no tiene un formato v\u00e1lido"); }else{
			 */
			alert("El valor del campo " + obj.name
					+ " no tiene un formato v\u00e1lido");
			// }
		}
		obj.value = "";
		obj.focus();
		return false;
	} else {
		return true;
	}
}

/** *********************************************************** */
function validarMontoValido(obj) {
	// alert('campo.montoPeriodo.value='+campo.montoPeriodo.value);
	// alert('campo.montoMatricula.value=0,00'+campo.montoMatricula.value);
	var monto = obj.value;
	monto = monto.replace(".", "").replace(",", ".");
	// if
	// (!/^([1-9]){1}([0-9]){1,6}([.]{1}([0-9]){1,2}|([0-9]){0,3})$/.test(obj.value))
	if (!/^(([1-9]){1}([0-9]){1,6}|[0]{1})([.]{1}([0-9]){1,2})$/.test(monto)) {
		if (obj.name == "montoAporteEmp") {
			alert("El campo Monto Aporte Empresa no tiene un formato v\u00e1lido");
		} else {
			if (obj.name == "montoPeriodo") {
				alert("El campo Monto Mensualidad no tiene un formato v\u00e1lido");
			} else {
				if (obj.name == "montoMatricula") {
					alert("El campo Monto Matr\u00edcula no tiene un formato v\u00e1lido");
				} else {
					if (obj.name == "montoBCV") {
						alert("El campo Monto Otorgado BCV no tiene un formato v\u00e1lido");
					} else {
						if (obj.name == "montoFactura") {
							alert("El campo Monto Factura no tiene un formato v\u00e1lido");
						} else {
							/*
							 * if(obj.name=="otrosPagos"){ alert("El campo Otros
							 * Pagos no tiene un formato v\u00e1lido"); }else{
							 */
							alert("El campo " + obj.name
									+ " no tiene un formato v\u00e1lido");
							// }
						}
					}
				}
			}
		}
		obj.value = "";
		obj.focus();
		return false;
	} else {
		if (validarMontoNotEqCero(obj)) {
			return true;
		}

	}
}

/** *********************************************************** */
function validarMontoNotEqCero(obj) {
	var monto = obj.value;
	if (monto == '0,00') {
		if (obj.name == "montoAporteEmp") {
			alert("El campo Monto Aporte Empresa no puede estar vac\u00edo");
		} else {
			if (obj.name == "montoPeriodo") {
				alert("El campo Monto Mensualidad no puede estar vac\u00edo");
			} else {
				if (obj.name == "montoMatricula") {
					alert("El campo Monto Matr\u00edcula no puede estar vac\u00edo");
				} else {
					if (obj.name == "montoBCV") {
						alert("El campo Monto Otorgado BCV no puede estar vac\u00edo");
					} else {
						if (obj.name == "montoFactura") {
							//alert("El campo Monto Factura no puede estar vac\u00edo");
							// vac�o");
							//montoFactura es calculado al seleccionar el mes a pagar
							// pagar
							alert("El campo Mes(es) a Pagar no puede estar vac\u00edo");
						} else {

							alert("El campo " + obj.name
									+ " no puede estar vac\u00edo");

						}
					}
				}
			}
		}
		obj.value = "";
		obj.focus();
		return false;
	} else {
		return true;
	}
}

/** *********************************************************** */

/** *********************************************************** */
function validarCedula2(obj) {
	if (!/^[0-9]*$/.test(obj.value)) {
		return false;
	} else {
		return true;
	}
}

/** *********************************************************** */

function validarCedula(obj) {
	if (!/^[0-9]*$/.test(obj.value)) {
		alert("El valor del campo c\u00e9dula no tiene formato v\u00e1lido");
		obj.value = "";
		obj.focus();
		return false;
	} else {
		return true;
	}
}
/** *********************************************************** */
function validarRif(obj) {
	if (!/^[J|V]{1}([0-9]){9}$/.test(obj.value)) {
		alert("El valor del campo rif no tiene formato v\u00e1lido");
		obj.value = "";
		obj.focus();
		return false;
	} else {
		return true;
	}
}
/** *********************************************************** */

function existeCampo() {
	if (document.forms[0].elements['codigoBenef']) {
		if (document.forms[0].elements['nroRifCentroEdu'])
			// alert('no existe el campo');
			return true;
	} else {
		alert('Debe ingresar el n\u00famero de la c\u00e9dula de identidad del empleado y luego oprimir el bot\u00f3n Buscar Familiar');
		return false;
	}
}
/** *********************************************************** */
function existeCampo2() {
	if (document.forms[0].elements['codigoBenef']) {
		return true;
	} else {
		alert('Debe ingresar el n\u00famero de la c\u00e9dula de identidad del empleado y luego oprimir el bot\u00f3n Buscar Familiar');
		return false;
	}
}

/** *********************************************************** */
/** ************ Funci�n para las migas de pan ************** */
/*
 * function breadCrumbs(delimiterStr) { loc2 = window.location.toString();//sets
 * locatiion to string called loc2 loc = loc2.toLowerCase();// sets loc2 to
 * lower case subs = loc.substr(7).split("/"); // Assume the first 7 characters
 * are http:// //Added class="breadCrumbs" so that a style sheet can be used
 * document.write("<a class=\"breadCrumbs\" href=\"" + getLoc(subs.length - 1) +
 * "\">Home</a> " + delimiterStr + " "); a = (loc.indexOf('index.') == -1) ? 1 :
 * 2; if (subs[subs.length-1].length == 0) { a++; } for (i = 1; i < (subs.length -
 * a); i++) { subs[i] = makeCaps(unescape(subs[i])); //Added class="breadCrumbs"
 * so that a style sheet can be used if(i==(subs.length - a-1)){
 * delimiterStr=""; } document.write("<a class=\"breadCrumbs\" href=\"" +
 * getLoc(subs.length - i - 2) + "\">" + subs[i] + "</a> " + delimiterStr + "
 * "); } document.write(document.title); } function makeCaps(a) { g =
 * a.split("_"); for (l = 0; l < g.length; l++) { g[l] =
 * g[l].toUpperCase().slice(0, 1) + g[l].slice(1); } return g.join(" "); }
 * function getLoc(c) { var d = ""; if (c > 0) { for (k = 0; k < c; k++) { d = d +
 * "../"; } } return d; }
 */
/** *********************************************************** */

/** Funciones para el m�dulo de Administraci�n de par�metros * */
function cargarParametro() {
	var campo = document.formBuscarBeneficio;
	var bandera = false;
	if (campo.elements['beneficioEscolar']) {
		if (campo.beneficioEscolar.value == "") {
			alert("Debe seleccionar un beneficio escolar de la lista de opciones desplegada...");
			campo.beneficioEscolar.focus();
			bandera = false;
		} else {
			bandera = true;
		}
	} else {
		bandera = false;
	}
	if (bandera) {
		campo.action = '/rhei/ManejadorVariable';
		campo.submit();
	}
}

/** *********************************************************** */
/** Funciones para el m�dulo de Administraci�n de par�metros * */
function listaDeParam() {

	var campo = document.formBuscarBeneficio;
	var bandera = false;
	if (campo.elements['beneficioEscolar']) {
		if (campo.beneficioEscolar.value == "") {
			alert("Debe seleccionar un beneficio escolar de la lista de opciones desplegada...");
			campo.beneficioEscolar.focus();
			bandera = false;
		} else {
			bandera = true;
		}
	} else {
		bandera = false;
	}
	if (bandera) {
		campo.action = '/rhei/ParametroControlador?principalParametro.do=listaDeParam';
		campo.submit();
	}
}
/** *********************************************************** */

function generarReportePagoTributo(definitivoTransitorio) {
	var isSubmit = true;

	var campo = document.formBuscarDatos;
	if (isSubmit) {

		campo.definitivoTransitorioHidden.value = definitivoTransitorio;
		campo.keyReport.value = 1;
		campo.action = '/rhei/documentOpenPagosTributosRporte';
		campo.submit();
	}

}

function generarDetalleDefinitivoController(definitivoTransitorio) {
	var isSubmit = true;

	var campo = document.formBuscarDatos;
	if (isSubmit) {
		campo.definitivoTransitorioHidden.value = definitivoTransitorio;
		campo.keyReport.value = 1;
		campo.action = '/rhei/detalleDefinitivoController';
		campo.submit();
	}

}

function updateDefinitivoReporte(definitivoTransitorio) {
	var isSubmit = true;
	var campo = document.formBuscarDatos;
	if (isSubmit) {
			  confirmar=confirm("Seguro que desea actualizar?");  
		if (confirmar) {
			campo.definitivoTransitorioHidden.value = definitivoTransitorio;
			campo.action = '/rhei/reporteDefinitivoTransitorioControlador?principal.do=updateDefinitivoReporte';
			campo.submit();
		}
	}

}

function deleteTransitorioReporte(definitivoTransitorio) {
	var isSubmit = true;
	var campo = document.formBuscarDatos;
	if (isSubmit) {
			  confirmar=confirm("Seguro que desea Cancelar el Reporte?");  
		if (confirmar) {
			campo.definitivoTransitorioHidden.value = definitivoTransitorio;
			campo.action = '/rhei/reporteDefinitivoTransitorioControlador?principal.do=deleteTransitorioReporte';
			campo.submit();
		}
	}

}

function cancelarReportePagoTributoDefinitivo(definitivoTransitorio) {
	var isSubmit = true;
	alert('Cancelar reporte');
	var campo = document.formBuscarDatos;
	if (isSubmit) {
		campo.definitivoTransitorioHidden.value = definitivoTransitorio;
		campo.action = '/rhei/documentOpenPagosTributosRporte';
		// campo.submit();
	}

}

function listaDeReporteDefinitivo() {
	var campo = document.formBuscarDatos;
	var bandera = false;
	if (campo.elements['periodoEscolar']) {
		if (campo.periodoEscolar.value == "") {
			alert("Debe seleccionar un per\u00edodo escolar de la lista de opciones desplegada...");
			campo.periodoEscolar.focus();
			bandera = false;
		} else {
			bandera = true;
		}
	} else {
		bandera = false;
	}
	if (bandera) {
		campo.action = '/rhei/reporteDefinitivoTransitorioControlador?principal.do=listaDeParam';
		campo.submit();
	}
}

function generarReporteDefinitivo() {
	var campo = document.formBuscarDatos;

	campo.action = '/rhei/reporteDefinitivoTransitorioControlador?principal.do=reportDefinitivo';
	campo.submit();

}

function paginandoReporteDefinitivo(irPara, tipoParametro, filtroParametro) {

	var campo2 = document.formPaginarParametros;
	campo2.action = '/rhei/reporteDefinitivoTransitorioControlador?principal.do=listaDeParam&beneficioEscolar='
			+ tipoParametro
			+ '&irPara='
			+ irPara
			+ '&filtroParametro='
			+ filtroParametro;
	campo2.submit();
}

/** Funciones para el m�dulo de Administraci�n de par�metros * */
function paginandoParametros(irPara, tipoParametro, filtroParametro) {

	var campo2 = document.formPaginarParametros;
	campo2.action = '/rhei/ParametroControlador?principalParametro.do=listaDeParam&beneficioEscolar='
			+ tipoParametro
			+ '&irPara='
			+ irPara
			+ '&filtroParametro='
			+ filtroParametro;
	campo2.submit();
}

function paginandoBeneficios(irPara) {
	var campo2 = document.formPaginarBeneficios;
	campo2.action = '/rhei/benefScolarControlador?principal.do=principal&irPara='
			+ irPara;
	campo2.submit();
}

function paginandoPeriodoScolar(irPara) {
	var campo2 = document.formPeriodoScolar;
	campo2.action = '/rhei/periodoScolarControlador?principal.do=lista&irPara='
			+ irPara;
	campo2.submit();
}

function convertirAMayuscula(obj) {
	// obj.value = obj.value.toUpperCase();
}
function modifyParam(form) {
	var bandera = false;
	if (form.name == 'formParametro') {
		if (validarVacio(form.nombreParametro)) {
			if (validarVacio(form.tipoBeneficio)) {
				if (validarVacio(form.tipoDato)) {
					if (validarVacio(form.valorParametro)) {
						if (validarVacio(form.fechaVigencia)) {
							if (validarFormatoFecha(form.fechaVigencia)) {
								if (isValidDate(form.fechaVigencia)) {
									if (validarVacio(form.descripcion)) {
										bandera = true;
									}
								}
							}
						}
					}
				}
			}
		}

	}

	if (bandera) {
		form.action = 'ParametroControlador?principalParametro.do=modifyParam';
		form.submit();
	}
}
function addParam(form) {

	var bandera = false;
	if (form.name == 'formParametro') {
		if (validarVacio(form.nombreParametro)) {
			if (validarVacio(form.tipoBeneficio)) {
				if (validarVacio(form.tipoDato)) {
					if (validarVacio(form.valorParametro)) {
						if (validarVacio(form.fechaVigencia)) {
							if (validarFormatoFecha(form.fechaVigencia)) {
								if (isValidDate(form.fechaVigencia)) {
									if (validarVacio(form.descripcion)) {
										bandera = true;
									}
								}
							}
						}
					}
				}
			}
		}

	}

	if (bandera) {
		form.action = 'ParametroControlador?principalParametro.do=addParam';

		form.submit();
	}
}

function addBeneficiario(form) {
	var bandera = false;
	if (validarVacio(form.nombreBeneficio)) {
		if (validarVacio(form.valorBeneficio)) {
			if (validarVacio(form.fechaRegistro)) {
				if (validarFormatoFecha(form.fechaRegistro)) {
					if (isValidDate(form.fechaRegistro)) {
						if (validarVacio(form.estado)) {
							bandera = true;
						}
					}
				}
			}
		}
	}
	if (bandera) {
		form.action = 'benefScolarControlador?principal.do=addParam';
		form.submit();
	}
}

function modifyBeneficiario(form) {
	var bandera = false;
	if (validarVacio(form.nombreBeneficio)) {
		if (validarVacio(form.valorBeneficio)) {
			if (validarVacio(form.fechaRegistro)) {
				if (validarFormatoFecha(form.fechaRegistro)) {
					if (isValidDate(form.fechaRegistro)) {
						if (validarVacio(form.estado)) {
							bandera = true;
						}
					}
				}
			}
		}
	}
	if (bandera) {
		form.action = 'benefScolarControlador?principal.do=modifyParam';
		form.submit();
	}
}

function addPeriodoScolar(form) {

	var bandera = false;

	if (validarVacio(form.descripcionPeriodo)) {
		if (validarVacio(form.fechaInicio)) {
			if (validarFormatoFecha(form.fechaInicio)) {
				if (isValidDate(form.fechaInicio)) {
					if (validarVacio(form.fechaFin)) {
						if (validarFormatoFecha(form.fechaFin)) {
							if (isValidDate(form.fechaFin)) {
								if (fechaIniMenorFechaFin(
										form.fechaInicio.value,
										form.fechaFin.value)) {
									if (validarVacio(form.estado)) {
										bandera = true;
									}
								}
							}
						}
					}

				}
			}
		}
	}

	if (bandera) {
		form.action = 'periodoScolarControlador?principal.do=add';
		form.submit();
	}
}

function fechaIniMenorFechaFin(fechaInicio, fechaFin) {

	var fechaInicioAlrev = invertir(fechaInicio);
	var fechaFinAlrev = invertir(fechaFin);
	if (fechaInicioAlrev < fechaFinAlrev) {
		return true;
	} else {
		alert("La fecha de inicio debe ser menor a la fecha fin.");
		return false;
	}
}
function invertir(cadena) {
	var arrStartDate = cadena.split("-");
	var date1 = arrStartDate[2] + arrStartDate[1] + arrStartDate[0];
	return date1;
}

function modifyPeriodoScolar(form) {

	var bandera = false;

	if (validarVacio(form.descripcionPeriodo)) {
		if (validarVacio(form.fechaInicio)) {
			if (validarFormatoFecha(form.fechaInicio)) {
				if (isValidDate(form.fechaInicio)) {
					if (validarVacio(form.fechaFin)) {
						if (validarFormatoFecha(form.fechaFin)) {
							if (isValidDate(form.fechaFin)) {
								if (validarVacio(form.estado)) {
									bandera = true;
								}
							}
						}
					}

				}
			}
		}
	}

	if (bandera) {
		form.action = 'periodoScolarControlador?principal.do=modify';
		form.submit();
	}
}

function validarFormulario(form) {
	var bandera = false;
	if (form.name == 'formParametro') {
		if (validarVacio(form.nombreParametro)) {
			if (validarVacio(form.tipoBeneficio)) {
				if (validarVacio(form.tipoDato)) {
					if (validarVacio(form.valorParametro)) {
						if (validarVacio(form.fechaVigencia)) {
							if (validarFormatoFecha(form.fechaVigencia)) {
								if (isValidDate(form.fechaVigencia)) {
									// if(validarVacio(form.compania)){
									// if(validarVacio(form.tipoEmp)){
									if (validarVacio(form.descripcion)) {
										bandera = true;
									}
									// }
									// }
								}
							}
						}
					}
				}
			}
		}

	} else {
		if (form.name == 'formBeneficio') {
			if (validarVacio(form.nombreBeneficio)) {
				if (validarVacio(form.valorBeneficio)) {
					if (validarVacio(form.fechaRegistro)) {
						if (validarFormatoFecha(form.fechaRegistro)) {
							if (isValidDate(form.fechaRegistro)) {
								if (validarVacio(form.estado)) {
									bandera = true;
								}
							}
						}
					}
				}
			}
		} else {
			if (form.name == 'formPeriodo') {
				if (validarVacio(form.descripcionPeriodo)) {
					if (validarVacio(form.fechaInicio)) {
						if (validarFormatoFecha(form.fechaInicio)) {
							if (isValidDate(form.fechaInicio)) {
								if (validarVacio(form.fechaFin)) {
									if (validarFormatoFecha(form.fechaFin)) {
										if (isValidDate(form.fechaFin)) {
											if (validarVacio(form.estado)) {
												bandera = true;
											}
										}
									}
								}

							}
						}
					}
				}
			}
		}
	}
	if (bandera) {
		form.action = '/rhei/GuardarVariable';
		// form.action='/rhei/jsp/principal.jsp';
		form.submit();
	}
}

/** *********************************************************** */

/*
 * function adaptarCampos(obj){ var campo = document.formDatosSolicitud; //var
 * tipoPago = 3; var fecha = new Date(); var dia = fecha.getDate(); var mes =
 * fecha.getMonth()+1; var anio = fecha.getFullYear();
 * if(obj.options[0].selected){ if(dia < 10){ dia = "0"+dia; } if(mes < 10){ mes =
 * "0"+mes; } campo.fechaFactura.value = dia+"-"+mes+"-"+anio;
 * //campo.fechaFactura.disabled=true; campo.nroFactura.value = "No tiene";
 * //campo.nroFactura.disabled=true; campo.nroControl.value = "No tiene";
 * //campo.nroControl.disabled=true; campo.montoFactura.value = "0,00";
 * //campo.montoFactura.disabled=true; }else{ if(obj.options[1].selected){
 * campo.fechaFactura.value = ""; //campo.fechaFactura.disabled=false;
 * campo.nroFactura.value = ""; //campo.nroFactura.disabled=false;
 * campo.nroControl.value = ""; //campo.nroControl.disabled=false;
 * campo.montoFactura.value = ""; //campo.montoFactura.disabled=false; } } }
 */

/** *************************************************************** */

function validarPagoNoConvencional() {
	var campo = document.formDatosSolicitud;
	var bandera = false;
	if (validarFormatoFecha(campo.fechaFactura)) {
		if (isValidDate(campo.fechaFactura)) {
			if (validarVacio(campo.nroFactura)) {
				if (validarVacio(campo.nroControl)) {
					if (validarVacio(campo.montoFactura)) {
						formatearCampo(campo.montoFactura);
						if (validarMontoValido(campo.montoFactura)) {
							if (validarVacio(campo.mesesPorPagar)) {
								if (validarVacio(campo.pago)) {
									// if(tipoPago == '2'){
									if (validarMontoValido(campo.pago)) {
										if (validarVacio(campo.observacion)) {
											bandera = true;
										}
									}
									/*
									 * }else{ if(tipoPago == '3'){
									 * if(validarMontoValido(campo.montoTotal)){
									 * if(validarVacio(campo.observacion)){
									 * bandera=true; } } } }
									 */
								}
							}
						}
					}
				}
			}
		}
	}

	if (bandera) {
		campo.action = 'GuardarPagosServlet';
		// campo.action='jsp/principal.jsp';
		campo.submit();
	}
}
