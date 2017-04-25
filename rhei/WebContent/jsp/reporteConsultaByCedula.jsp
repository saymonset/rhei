<!-- <!-- Toda esta info va para el controlador BenefAndCentrEducControlador, y se devuelve a la pagina accion que es el hidden -->
<!-- que esta en el form que llama esta pagina -->

<tr>
	<td>
		<div class="derecha">
			<!-- 		Si no consultamos para buscar pagos, abre esta ventana -->
			<table cellspacing="5" cellpadding="2" class="formato10 anchoTabla4">

							<!-- Lista de Beneficiarios (Niños) y Lista de Rif (Colegios)-->

 


 



<tr>
	<td><label><fmt:message key="pago.dirigido"
				bundle="${lang}" />:</label></td>
	<td colspan="10"><select name="receptorPago" class="entrada"
		onchange="changeReporteDef();" id="receptorPago" size="1" tabindex="">
			<c:forEach var="obj" items="${showResultToView.receptorPagos}">
				<option value="${obj.valor}"
					${obj.valor==showResultToView.receptorPago?'selected':''}>${obj.nombre}</option>
			</c:forEach>
	</select></td>
</tr>

 

<tr>
	<td><label><fmt:message key="pagos.forma" bundle="${lang}" />:</label></td>
	<td><select name="coFormaPago" id="coFormaPago"
		class="entrada" onchange="changeReporteDef();">
			<c:forEach var="formaPagoValNom"
				items="${showResultToView.formaPagoValNoms}">
				<option value="${formaPagoValNom.valor}"
					${formaPagoValNom.valor==showResultToView.formaPago?'selected':''}>
					${formaPagoValNom.nombre}</option>
			</c:forEach>
	</select></td>
</tr>

	 
<tr>
	<td><input type="radio" name="mesMatrCompl"  value="0">
	<fmt:message key="reporte.mes.matricula" bundle="${lang}" /><br>
		<input type="radio" name="mesMatrCompl" value="1">
	<fmt:message key="reporte.complemento.rembolso" bundle="${lang}" /><br>
		<input type="radio" name="mesMatrCompl" checked value="2">
	<fmt:message key="reporte.ambos" bundle="${lang}" /></td>
	<td>
	 <input
											type="button" class="boton_color" name="buscarDatos3"  id="buscarDatos3"
											value=<fmt:message key="reporte.pagoAndTributos"	bundle="${lang}" /> tabindex=""
											onclick="javascript: reporteConsultaByCedulaAjax(1);" />
	</td>
</tr>


<input type="hidden" name="filtrarByMesOrComplementoOrAmbos" value="" />
<input type="hidden" name="periodoEscolar" value="" />
<input type="hidden" name="status" value="" />
<input type="hidden" name="cedulaEmpleado" value="" />
<input type="hidden" name="numSolicituds" value="" />




<input type="hidden" name="keyReport" value="" />
				 
				<!-- FIN Lista de Beneficiarios (Niños) y Lista de Rif (Colegios)-->



			</table>
			



		</div>
	</td>
</tr>


				<tr>
					<td><span id="benefwait" style="display: none;">

							<div style="text-align: center;">
								<img class="bannerLogin" border="0" src="imagenes/ai.gif" alt="" />
								<fmt:message key="wait.hold_on" bundle="${lang}" />
							</div>
					</span></td>
				</tr>
