 
/** ************ Función menu acordeón ************** */
$(function() {
	
	$("#tr_NroRifCentroEduc").hide(); 
	
	
	$( "#nombre" ).keypress(function() {
	     	$("#tr_NroRifCentroEduc").hide();
		});
	 
	
	$('#searchColegios').click(function() {
    				sendAjax();
    				document.getElementById('benefwait').style.display = 'none';
    				$("#tr_NroRifCentroEduc").show();
    				 $('#nombre').val("");
    });
	
	
	
	
 
	
	
});
/** *********************************************************** */
	


function  clickColeg(){
	$("#educInicial").click();
	    
}

function sendAjax() {
	 
	 
    // get inputs
    var proveedor = new Object();
    proveedor.nombre = $('#nombre').val();
    
    
    $.ajax({
    	//llamamos al servlet institucionAjax para hacer la peticion de busqueda con ajax 
        url: "institucionAjax",
        type: 'POST',
        dataType: 'json',
        data: JSON.stringify(proveedor),
        contentType: 'application/json',
        mimeType: 'application/json',
 
        success: function (data) {
        	//Inicializa la lista desplegable
            $('#nroRifCentroEdu')
            .empty()
            .append('<option value="0">Seleccione...</option>')
            .find('option:first')
            .attr("selected","selected");
       
 
            $.each(data, function (index, proveedor) {
               
 
                $('#nroRifCentroEdu').append('<option value='+proveedor.valor+'>'+proveedor.valor+'->'+proveedor.nombre+'</option>');
 
            });
        },
        error:function(data,status,er) {
            alert("error: "+data+" status: "+status+" er:"+er);
        }
    });
}
 
