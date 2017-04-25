/** ************ Funci�n menu acorde�n ************** */
$(function() {

 

	$("#buscar").click(function() {
		$('.pickList_sourceList li').remove();
		$('.pickList_targetList li').remove();
		buscarSolicitudReporte();
	});

});
/** *********************************************************** */
 

function buscarSolicitudReporte() {

	sendAjax();
	document.getElementById('showSolicitud1').style.display = 'block';
	document.getElementById('showSolicitud2').style.display = 'block';
	document.getElementById('showSolicitud3').style.display = 'block';
	document.getElementById('showmestitlereport').style.display = 'block';
	document.getElementById('showmestitlereport2').style.display = 'block';
	document.getElementById('showmestitlereport3').style.display = 'block';

}

function changeReporteDef(obj) {

	$('.pickList_sourceList li').remove();
	$('.pickList_targetList li').remove();
	document.getElementById('showSolicitud1').style.display = 'none';
	document.getElementById('showSolicitud2').style.display = 'none';
	document.getElementById('showSolicitud3').style.display = 'none';

}

function changeReporte(obj) {

	$('.pickList_sourceList li').remove();
	$('.pickList_targetList li').remove();
	document.getElementById('showSolicitud1').style.display = 'none';
	document.getElementById('showSolicitud2').style.display = 'none';
	document.getElementById('showSolicitud3').style.display = 'none';
	document.getElementById('showmestitlereport').style.display = 'none';
	document.getElementById('showmestitlereport2').style.display = 'none';
	document.getElementById('showmestitlereport3').style.display = 'none';

}

function reporteConsultaByCedulaAjax(keyb) {

	var isSubmit = true;
	var campo = document.formDatosSolicitud;
	var campo2 = document.formBuscarDatosIndividual;
	var filtrarByMesOrComplementoOrAmbos = '';
	var x = document.getElementsByName("mesMatrCompl");

	var i;
	/** filtrarByMesOrComplementoOrAmbos */
	for (i = 0; i < x.length; i++) {
		if (x[i].checked) {
			filtrarByMesOrComplementoOrAmbos = x[i].value;
		}

	}

	if (isSubmit) {

		document.getElementById('benefwait').style.display = 'block';

		// estos campos estan en hideen en reporteFiltroByPeriodoPagoTributo.jsp
		campo2.action = '/rhei/documentOpenRporte';

		campo2.filtrarByMesOrComplementoOrAmbos.value = filtrarByMesOrComplementoOrAmbos;

		campo2.periodoEscolar.value = campo.elements['periodoEscolar'].value;

		campo2.status.value = campo.statusSolicitud.value;

		campo2.cedulaEmpleado.value = campo.cedulaEmpleado.value;

		campo2.numSolicituds.value = campo.nroSolicitud.value;

		campo2.keyReport.value = keyb;

		campo2.submit();

		document.getElementById('benefwait').style.display = 'none';
		document.getElementById('buscarDatos3').disabled = false;

	}

}






function generarReporteIndividualAjax(keyb) {
	var isSubmit = true;
	var campo = document.formBuscarDatos;
	var numSolicituds = '';
	var meses = '';
	var firstTime = false;
	var numSol = '';
	var filtrarByMesOrComplementoOrAmbos = '';
	var pagadoNoPagadoAmboSearchs = '';
	var x = document.getElementsByName("mesMatrCompl");
	var y = document.getElementsByName("pagadoNoPagadoAmbos");
	var i;
	/** filtrarByMesOrComplementoOrAmbos */
	for (i = 0; i < x.length; i++) {
		if (x[i].checked) {
			filtrarByMesOrComplementoOrAmbos = x[i].value;
		}

	}

	/** Filtramos por los pagados o no pagados** */
	for (i = 0; i < y.length; i++) {
		if (y[i].checked) {
			pagadoNoPagadoAmboSearchs = x[i].value;
		}

	}

	if ((campo.cedulaEmpleado.value.length == 0)
			&& !validarVacio(campo.tipoEmpleado)) {
		return false;
	}
	
	if ((campo.cedulaEmpleado.value.length > 0)
			&& !isNumber(campo.cedulaEmpleado.value)) {
		alert('Debe colocar solo n�meros  en la cedula');
		return false;
	}

	if ((keyb == 1 || keyb == 3)
			&& !document.forms[0].elements['periodoEscolar'].value) {
		alert("Debe seleccionar el Per�odo Escolar.");
		isSubmit = false;
	} else if ((keyb == 1 || keyb == 3)
			&& !document.forms[0].elements['receptorPago'].value) {
		alert("Debe seleccionar 'Pago dirigido a'.");
		isSubmit = false;
	}

	if (isSubmit) {
		var proveedor = new Object();
		proveedor.nombre = $('#nombreDefinitivoTransitorio').val();
		proveedor.periodoEscolar = $('#periodoEscolar').val();
		proveedor.valor = keyb;
		document.getElementById('buscarDatos3').disabled = true;
		document.getElementById('benefwait').style.display = 'block';
		$
				.ajax({
					// llamamos al servlet reporteAjax para hacer la peticion de
					// busqueda con ajax
					url : "reporteValidarAjaxServlet",
					type : 'POST',
					dataType : 'json',
					data : JSON.stringify(proveedor),
					contentType : 'application/json',
					mimeType : 'application/json',
					success : function(data) {
						$
								.each(
										data,
										function(index, proveedor) {
											if (proveedor.valor == '1') {
												// estos campos estan en hideen
												// en
												// reporteFiltroByPeriodoPagoTributo.jsp
												campo.action = '/rhei/documentOpenRporte';
												campo.filtrarByMesOrComplementoOrAmbos.value = filtrarByMesOrComplementoOrAmbos;
												campo.pagadoNoPagadoAmboSearchs.value = pagadoNoPagadoAmboSearchs;
												campo.meses.value = meses;
												campo.keyReport.value = keyb;
												campo.numSolicituds.value = numSolicituds;
												campo.submit();
											} else {
												alert('Este nombre '
														+ proveedor.nombre
														+ ' ya se encuentra registrado para el per�odo '
														+ proveedor.periodoEscolar);
											}

										});

					},
					error : function(data, status, er) {
						alert("readyState: " + data.readyState + "\nstatus: "
								+ data.status);
					}
				});

		document.getElementById('benefwait').style.display = 'none';
		document.getElementById('buscarDatos3').disabled = false;

	}

}

function isNumber(n) {
	  return !isNaN(parseFloat(n)) && isFinite(n);
	}

function generarReporteAjax(keyb) {
	var isSubmit = true;
	var campo = document.formBuscarDatos;
	var numSolicituds = '';
	var meses = '';
	var firstTime = false;
	var numSol = '';
	var filtrarByMesOrComplementoOrAmbos = '';
	var x = document.getElementsByName("mesMatrCompl");

	var i;
	/** filtrarByMesOrComplementoOrAmbos */
	for (i = 0; i < x.length; i++) {
		if (x[i].checked) {
			filtrarByMesOrComplementoOrAmbos = x[i].value;
		}

	}

	/** Todos los meses por agarrar y sacar el reporte */
	for (var i = 0; i < campo.mesesid.length; i++) {
		if (campo.mesesid.options[i].selected
				&& campo.mesesid.options[i].value != '') {
			if (firstTime === true) {
				meses += ',';
			}
			meses += campo.mesesid.options[i].value;
			firstTime = true;
		}
	}

	if (!firstTime) {
		alert('Debe seleccionar por lo menos un mes.');
		return false;
	}

	firstTime = false;

	$('.pickList_targetList  li').each(function(index) {

		if (firstTime === true) {
			numSolicituds += ',';
		}
		numSol = $(this).text();
		if (numSol != '0' && numSol != '') {
			numSolicituds += numSol;
			firstTime = true;

		}
	});

	if (!firstTime) {
		alert('Debe seleccionar por lo menos un numero de solicitud.');
		return false;
	}

	if ((keyb == 1 || keyb == 3)
			&& !document.forms[0].elements['periodoEscolar'].value) {
		alert("Debe seleccionar el Per�odo Escolar.");
		isSubmit = false;
	} else if ((keyb == 1 || keyb == 3)
			&& !document.forms[0].elements['receptorPago'].value) {
		alert("Debe seleccionar 'Pago dirigido a'.");
		isSubmit = false;
	}

	if (keyb == 2 && !validarVacio(campo.nombreDefinitivoTransitorio)) {
		isSubmit = false;
	}

	if (isSubmit) {
		var proveedor = new Object();
		proveedor.nombre = $('#nombreDefinitivoTransitorio').val();
		proveedor.periodoEscolar = $('#periodoEscolar').val();
		proveedor.valor = keyb;
		document.getElementById('buscarDatos3').disabled = true;
		document.getElementById('benefwait').style.display = 'block';
		$
				.ajax({
					// llamamos al servlet reporteAjax para hacer la peticion de
					// busqueda con ajax
					url : "reporteValidarAjaxServlet",
					type : 'POST',
					dataType : 'json',
					data : JSON.stringify(proveedor),
					contentType : 'application/json',
					mimeType : 'application/json',
					success : function(data) {
						$
								.each(
										data,
										function(index, proveedor) {
											if (proveedor.valor == '1') {
												// estos campos estan en hideen
												// en
												// reporteFiltroByPeriodoPagoTributo.jsp
												campo.action = '/rhei/documentOpenRporte';
												campo.filtrarByMesOrComplementoOrAmbos.value = filtrarByMesOrComplementoOrAmbos;
												campo.meses.value = meses;
												campo.keyReport.value = keyb;
												campo.numSolicituds.value = numSolicituds;
												campo.submit();
											} else {
												alert('Este nombre '
														+ proveedor.nombre
														+ ' ya se encuentra registrado para el per�odo '
														+ proveedor.periodoEscolar);
											}

										});

					},
					error : function(data, status, er) {
						alert("readyState: " + data.readyState + "\nstatus: "
								+ data.status);
					}
				});

		document.getElementById('benefwait').style.display = 'none';
		document.getElementById('buscarDatos3').disabled = false;

	}

}

function directoReporteAjax(keyb) {

	var isSubmit = true;
	var campo = document.formBuscarDatos;
	var numSolicituds = '';
	var meses = '';
	var firstTime = false;
	var numSol = '';
	var filtrarByMesOrComplementoOrAmbos = '';
	var x = document.getElementsByName("mesMatrCompl");

	if (validarVacio(campo.tipoEmpleado)
			&& validarVacio(campo.nombreDefinitivoTransitorio)) {
		if (isSubmit) {

			var i;
			/**
			 * Seleccionamos si es complemento, mensual o ambos
			 * filtrarByMesOrComplementoOrAmbos
			 */
			for (i = 0; i < x.length; i++) {
				if (x[i].checked) {
					filtrarByMesOrComplementoOrAmbos = x[i].value;
				}
			}
			var proveedor = new Object();
			proveedor.nombre = $('#nombreDefinitivoTransitorio').val();
			proveedor.periodoEscolar = $('#periodoEscolar').val();
			proveedor.valor = keyb;
			document.getElementById('benefwait').style.display = 'block';
			$
					.ajax({
						// llamamos al servlet reporteAjax para hacer la
						// peticion de busqueda con ajax y validar que el nombre y anio no se repita para el miasmo reporte
						url : "reporteValidarAjaxServlet",
						type : 'POST',
						dataType : 'json',
						data : JSON.stringify(proveedor),
						contentType : 'application/json',
						mimeType : 'application/json',
						success : function(data) {
							$
									.each(
											data,
											function(index, proveedor) {
												if (proveedor.valor == '1') {
													document
															.getElementById('benefwait').style.display = 'block';
													// estos campos estan en
													// hideen en
													// reporteFiltroByPeriodoPagoTributo.jsp
													campo.action = '/rhei/documentOpenRporte';
													campo.filtrarByMesOrComplementoOrAmbos.value = filtrarByMesOrComplementoOrAmbos;
													campo.meses.value = meses;
													campo.keyReport.value = keyb;
													campo.numSolicituds.value = numSolicituds;
													document
															.getElementById('buscarDatos3').disabled = true;
													campo.submit();
												} else {
													document
															.getElementById('benefwait').style.display = 'none';
													alert('Este nombre '
															+ proveedor.nombre
															+ ' ya se encuentra registrado para el per�odo '
															+ proveedor.periodoEscolar);

												}

											});

						},
						error : function(data, status, er) {
							document.getElementById('benefwait').style.display = 'none';
							alert("readyState: " + data.readyState
									+ "\nstatus: " + data.status);

						}
					});

		}
	}

}

function waitSeconds(iMilliSeconds) {
	var counter = 0, start = new Date().getTime(), end = 0;
	while (counter < iMilliSeconds) {
		end = new Date().getTime();
		counter = end - start;
	}
}

function generarReporteByFacturaAjax(keyb) {
	alert(' Hol mundo');
}

function sendAjax() {
	document.getElementById('benefwait').style.display = 'block';
	// get inputs
	var proveedor = new Object();
	proveedor.coStatus = $('#status').val();
	proveedor.periodoEscolar = $('#periodoEscolar').val();
	proveedor.receptorPago = $('#receptorPago').val();
	proveedor.coFormaPago = $('#coFormaPago').val();
	proveedor.tipoEmpleado = $('#tipoEmpleado').val();

	$.ajax({
		// llamamos al servlet reporteAjax para hacer la peticion de busqueda
		// con ajax
		url : "reporteAjaxServlet",
		type : 'POST',
		dataType : 'json',
		data : JSON.stringify(proveedor),
		contentType : 'application/json',
		mimeType : 'application/json',

		success : function(data) {

			$('.pickList_sourceList li').remove();
			$('.pickList_targetList li').remove();
			$.each(data, function(index, proveedor) {
				$("#advanced").pickList("insert", {
					value : proveedor.valor,
					label : proveedor.label,
					selected : false
				});
			});

			document.getElementById('showSolicitud1').style.display = 'block';
			document.getElementById('showSolicitud2').style.display = 'block';
			document.getElementById('showSolicitud3').style.display = 'block';

			document.getElementById('benefwait').style.display = 'none';
		},
		error : function(data, status, er) {
			alert("error: " + data + " status: " + status + " er:" + er);
			document.getElementById('benefwait').style.display = 'none';
		}
	});
}
