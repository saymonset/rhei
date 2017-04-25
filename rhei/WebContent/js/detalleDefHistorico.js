/** ************ Funci�n menu acorde�n ************** */
$(function() {
	 

	 $("#observacion").focus();
	 
	
});
 
 function seleccionar_todo() {
		var radioButTrat = document.getElementsByName("numeroDesactivar");
		for (i = 0; i < radioButTrat.length; i++)
			radioButTrat[i].checked = true;
	}
	function deseleccionar_todo() {
		var radioButTrat = document.getElementsByName("numeroDesactivar");
		for (i = 0; i < radioButTrat.length; i++)
			radioButTrat[i].checked = false;
	}

	function desincorporarDefinitivo(form) {
		var isCheck=false;
		var radioButTrat = document.getElementsByName("numeroDesactivar");
		for (i = 0; i < radioButTrat.length; i++){
			if (radioButTrat[i].checked == true){
				isCheck=true;
				break;
			}			
		}
			

		if (!isCheck ){
			form.observacion.focus();
			alert('Debe seleccionar una solicitud');
		}else if (form.observacion.value.length==0 ) {
			form.observacion.focus();
			alert('Debe colocar una observaci\u00F3n');
		}else if (form.observacion.value.length>460) {
			form.observacion.focus();
			alert('Debe colocar una observaci\u00F3n menor a 460 car\u00e1cteres.');

		}else{
			confirmar = confirm("Seguro desea desincorporar los registros del definitivo ?");
			if (confirmar) {
				form.action = 'detalleDefinitivoController?principal.do=eliminarDefinitivo';
				form.submit();
			}	
		}
		

	}