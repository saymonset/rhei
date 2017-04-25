<!-- Include css styles here -->
<script type="text/javascript" src="/rhei/js/fileUploadScript.js"></script>
<link href="/rhei/css/style.css" rel="stylesheet" type="text/css" />
<form id="formUploadFile">
	<table cellspacing="5" cellpadding="2" class="anchoTabla4">
		<tr>
			<td colspan="4" class="fondo_seccion"><label class="subtitulo">
					<fmt:message key="recaudo.titulo" bundle="${lang}" />
			</label></td>
		</tr>
	</table>

	<span id="showUploadRecaudo" style="display: none;">
		<table cellspacing="5" cellpadding="2" class="anchoTabla4">
			<tr>
				<td align="left"><label for="sampleFile"><fmt:message
							key="recaudo.selec.file" bundle="${lang}" /> </label> &nbsp;<input
					id="sampleFile" name="sampleFile" class="boton_color" type="file" />
					<input type="button" id="uploadBtn" class="boton_color"
					name="uploadBtn"
					value="<fmt:message key="recaudo.guardar" bundle="${lang}" />"
					tabindex="55" id="55"
					onClick="performAjaxSubmit('${showResultToView.nroRifCentroEdu}','${showResultToView.cedula}','${showResultToView.urlAccion}');" />
					<input type="button" id="uploadCancelBtn" class="boton_color"
					name="uploadCancelBtn"
					value="<fmt:message key="cancelar" bundle="${lang}" />"
					tabindex="55" id="55" onClick="cancelarAjaxSubmit();" /></td>
				<td align="left"></td>
			</tr>
		</table>
	</span> <span id="messagewait" style="display: none;">

		<div style="text-align: center;">
			<img class="bannerLogin" border="0" src="imagenes/ai.gif" alt="" />
			<fmt:message key="wait.hold_on" bundle="${lang}" />
		</div>
	</span>

 


</form>

<span id="recaudosListShow" style="display: block;">
	<form id="formRecaudosList" name="formRecaudosList">
		<%@ include file="listFileAjax.jsp"%>
	</form>
</span>
