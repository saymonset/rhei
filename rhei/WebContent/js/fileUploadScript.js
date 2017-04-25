$(document).ready(function() {
 
	
});

var reader;
var progress = document.querySelector('.percent');

function showUploadRecaudo(element) {
	document.getElementById('showUploadRecaudo').style.display = 'block';
	document.getElementById('btnPrincipal').style.display = 'none';
	//btnPrincipal
}
function cancelarAjaxSubmit(){
	document.getElementById('showUploadRecaudo').style.display = 'none';
	document.getElementById('btnPrincipal').style.display = 'block';
	
	
	 var radios = document.getElementsByName('alfrescoOption');
	    for (var i = 0, length = radios.length; i < length; i++) {
	    	
	        if (radios[i].checked) {
	            // do whatever you want with the checked radio
	        	radios[i].checked=false;
	            // only one radio can be logically checked, don't check the rest
	            break;
	        }
	    }

	
}

function downloadDoc(path,nameFile,ext){
	window.location = '/rhei/documentOpen?path='+path+'&nameFile='+nameFile+'&ext='+ext;
   
	return false;
}


function performAjaxSubmit(rif,cedula,url) {
	 
	var diezMega=10485760;
	var periodoEscolar=  document.formDatosSolicitud.periodoEscolar;
	//alert(periodoEscolar+',rif='+rif+',cedula='+cedula+',codBenef='+codBenef);
     
    var periodoEscolarValor = periodoEscolar.options[periodoEscolar.selectedIndex].value;
    
    
    var codBenefName=document.formBuscarDatos.codigoBenef;
	var codBenef=codBenefName.options[codBenefName.selectedIndex].value;
  
    //alert(result);
	
	// Check for the various File API support.
	if (window.File && window.FileReader && window.FileList && window.Blob) {
	  // Great success! All the File APIs are supported.
	} else {
	  alert('The File APIs are not fully supported in this browser.');
	  return false;
	}
	var files = document.getElementById('sampleFile').files;
	 if (!files.length) {
	        alert('Debe seleccionar un recaudo!');
	        return;
	      }
	 
    var sampleFile = document.getElementById("sampleFile").files[0];

    
	 if (!sampleFile.type.match('application/pdf')) {
		   alert('Debe seleccionar solo archivo de tipo formato pdf!');
	        return;
	      }
	 
	 if (sampleFile.size>=diezMega){
		  alert('Debe seleccionar solo archivo de tipo formato pdf menor a 10 MB!');
	        return;
	 }

    
    
    document.getElementById('showUploadRecaudo').style.display = 'none';
    document.getElementById('recaudosListShow').style.display = 'none';
	document.getElementById('messagewait').style.display = 'block';
    
    
    var radios = document.getElementsByName('alfrescoOption');
    var alfrescoOption;
    for (var i = 0, length = radios.length; i < length; i++) {
        if (radios[i].checked) {
            // do whatever you want with the checked radio
            alfrescoOption=radios[i].value;

            // only one radio can be logically checked, don't check the rest
            break;
        }
    }

    var formdata = new FormData();

    formdata.append("sampleFile", sampleFile);

    var xhr = new XMLHttpRequest();       
    xhr.open("POST",url+"?principal.do=uploadFile&alfrescoOption="+alfrescoOption+'&periodoEscolarValor='+periodoEscolarValor+'&rif='+rif+'&cedula='+cedula+'&codBenef='+codBenef, true);
    xhr.send(formdata);
    xhr.onload = function(e) {
        if (xhr.status == 200) {
        	document.getElementById("message").innerHTML=this.responseText;
        	document.getElementById('showUploadRecaudo').style.display = 'none';
         /**Debe haber un id="btnPrincipal" en la pagina solicitudIncluir.jsp, socilitudActualizar.jsp ,  solicitudConsiltar.jsp,
          * o en cualquier llamado de principal de <@ include file="uploadFileAjax.jsp">        	**/
        	document.getElementById('btnPrincipal').style.display = 'block';
        } 
 
    	document.getElementById('messagewait').style.display = 'none';
    	document.getElementById('recaudosListShow').style.display = 'block';

    };    


   
}   