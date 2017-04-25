/** ************ Funci�n menu acorde�n ************** */
$(function() {

	$("#tr_resul").hide();

	$("#nombre").keypress(function() {
		$("#tr_resul").hide();
	});
	$("#apellido").keypress(function() {
		$("#tr_resul").hide();
	});

	$('#search').click(function() {
		sendAjax();
		document.getElementById('benefwait').style.display = 'none';
		$("#tr_resul").show();
		$('#nombre').val("");
		$('#apellido').val("");
	});

});
/** *********************************************************** */



function quitarAcentos(str){
	var from = "ÃÀÁÄÂÈÉËÊÍÌÍÏÎÒÓÖÔÙÚÜÛãàáäâèéëêìíïîòóöôùúüûÑñÇç",
	to   = "AAAAAEEEEIIIIIOOOOUUUUaaaaaeeeeiiiioooouuuunncc",
	mapping = {};

	for(var i = 0, j = from.length; i < j; i++ )
		mapping[ from.charAt( i ) ] = to.charAt( i );
	var ret = [];

	for( var i = 0, j = str.length; i < j; i++ ) {
		var c = str.charAt( i );
		if( mapping.hasOwnProperty( str.charAt( i ) ) )
			ret.push( mapping[ c ] );
		else
			ret.push( c );
	}
	return ret.join( '' );
}
 
//quitar espacios en blanco en los extremos de inicio y final de la cadena
function lTrim(sStr) {
	while (sStr.charAt(0) == " ")
		sStr = sStr.substr(1, sStr.length - 1);
	return sStr;
}

function rTrim(sStr) {
	while (sStr.charAt(sStr.length - 1) == " ")
		sStr = sStr.substr(0, sStr.length - 1);
	return sStr;
}

function allTrim(sStr) {
	return rTrim(lTrim(sStr));
}
// quitar espacios en blanco en los extremos de inicio y final de la cadena

 

function getValueCedula() {

	$('#nombre').val($('select[name=llenarResultado]').val());
}

function sendAjax() {

	if ('' == $('#apellido').val()) {
		alert('El apellido es obligatorio');
	} else {
		var proveedor = new Object();
		var objStr = $('#nombre').val();
		if (objStr != null && '' != objStr) {
			proveedor.nombre = quitarAcentos(allTrim(objStr));
		}
		objStr = $('#apellido').val();
		if (objStr != null && '' != objStr) {
			proveedor.apellido = quitarAcentos(allTrim(objStr));
		}

		$.ajax({
			// llamamos al servlet institucionAjax para hacer la peticion de
			// busqueda con ajax
			url : "buscarCedulaAjax",
			type : 'POST',
			dataType : 'json',
			// convert proveedor to json
			data : JSON.stringify(proveedor),
			contentType : 'application/json',
			mimeType : 'application/json',

			success : function(data) {
				// Inicializa la lista desplegable
				$('#llenarResultado').empty().append(
						'<option value="0">Seleccione...</option>').find(
						'option:first').attr("selected", "selected");

				$.each(data, function(index, proveedor) {

					$('#llenarResultado').append(
							'<option value=' + proveedor.valor + '>'
									+ proveedor.valor + '->' + proveedor.nombre
									+ '</option>');

				});
			},
			error : function(data, status, er) {
				alert("error: " + data + " status: " + status + " er:" + er);
			}
		});
	}

}
