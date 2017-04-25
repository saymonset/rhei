 
/** ************ Función menu acordeón ************** */
$(function() {
 
	
	
	
	
	/**Usado para filtrar con jquery ajax los colegios educativos.. se emplea la
	 * funcion sendAjax()*/
	$( "#nombreAjax").keypress(function() {
			document.getElementById('nroFacturaNone1').style.display = 'none';
			document.getElementById('nroFacturaNone2').style.display = 'none';
		});
	
	$("#buscarFacturasAjax").click(function () {
		if ( $('#nombreAjax').val()!=''){
			sendAjax();
			document.getElementById('nroFacturaNone1').style.display = 'block';
			document.getElementById('nroFacturaNone2').style.display = 'block';
		}else{
			alert('Debe ingresar un nro de solicitud.');
		}
		});
	
});
/** *********************************************************** */
	






function  clickColeg(){
	$("#educInicial").click();
	    
}

function sendAjax() {
	 
	 
    // get inputs
    var proveedor = new Object();
    proveedor.nombre = $('#nombreAjax').val();
    
    
    $.ajax({
    	//llamamos al servlet institucionAjax para hacer la peticion de busqueda con ajax 
        url: "facturasAjax",
        type: 'POST',
        dataType: 'json',
        data: JSON.stringify(proveedor),
        contentType: 'application/json',
        mimeType: 'application/json',
 
        success: function (data) {
        	//Inicializa la lista desplegable
            $('#factura')
            .empty()
            .append('<option value="0">Seleccione...</option>')
            .find('option:first')
            .attr("selected","selected");
            $.each(data, function (index, proveedor) {
                $('#factura').append('<option value='+proveedor.valor+'>'+proveedor.nombre+'</option>');
 
            });
        },
        error:function(data,status,er) {
            alert("error: "+data+" status: "+status+" er:"+er);
        }
    });
}
 
