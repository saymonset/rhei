/** ************ Funci�n menu acorde�n ************** */
$(function() {

	document.getElementById('reporteContableBlock').style.display = 'none';
	$('input[type="checkbox"]')
			.click(
					function() {
						var selectedCountry = new Array();
						document.getElementById('reporteContableBlock').style.display = 'none';
						$('[name="reporteContableCheck"]')
								.each(
										function() {

											if ($(this).prop('checked') == true) {
												selectedCountry.push($(this)
														.val());
												document
														.getElementById('reporteContableBlock').style.display = 'block';
											}
										});
						$("#reporteContableDefinitivo").val(selectedCountry);
					});

 
});
/** *********************************************************** */

function generarReporteContable() {
	var campo = document.formPaginarParametros;
	campo.action = "reporteContableOpenServlet";
	campo.submit();
}
 

 