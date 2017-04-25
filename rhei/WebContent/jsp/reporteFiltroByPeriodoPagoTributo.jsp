<!-- Lista de Beneficiarios (Niños) y Lista de Rif (Colegios)-->

<tr>
	<td><label><fmt:message key="estaus" bundle="${lang}" />:</label></td>
	<td colspan="10"><select name="status" id="status" class="entrada"
		onchange="changeReporte();" size="1" tabindex="">
			<c:forEach var="prov" items="${statusLst}">
				<option value="${prov.valor}">${prov.nombre}</option>
			</c:forEach>

	</select></td>
</tr>


<tr>
	<td><label><fmt:message key="periodo" bundle="${lang}" />:</label></td>
	<td colspan="10"><select name="periodoEscolar" id="periodoEscolar"
		class="entrada" onchange="changeReporte();" size="1" tabindex=""><option
				value=""><fmt:message key="select.seleccione"
					bundle="${lang}" /></option>
			<c:forEach var="prov" items="${showResultToView.periodoEscolares}">
				<option value="${prov.valor}"
					${showResultToView.periodoEscolar==prov.valor?'selected':''}>${prov.nombre}</option>
			</c:forEach>

	</select></td>
</tr>


<tr>
	<td><label><fmt:message key="pago.dirigido"
				bundle="${lang}" />:</label></td>
	<td colspan="10"><select name="receptorPago" class="entrada"
		onchange="changeReporte();" id="receptorPago" size="1" tabindex="">
			<c:forEach var="obj" items="${showResultToView.receptorPagos}">
				<option value="${obj.valor}"
					${obj.valor==showResultToView.receptorPago?'selected':''}>${obj.nombre}</option>
			</c:forEach>
	</select></td>
</tr>


<tr>
	<td><label><fmt:message key="tipoEmpleado"
				bundle="${lang}" />:</label></td>
	<td colspan="10"><select name="tipoEmpleado" class="entrada"
		onchange="changeReporte();" id="tipoEmpleado" size="1" tabindex="">

			<c:forEach var="obj" items="${tipoEmpleados}">
				<option value="${obj.valor}">${obj.nombre}</option>
			</c:forEach>
	</select></td>
</tr>



<tr>
	<td><label><fmt:message key="pagos.forma" bundle="${lang}" />:</label></td>
	<td colspan="6"><select name="coFormaPago" id="coFormaPago"
		class="entrada" onchange="changeReporte();">
			<c:forEach var="formaPagoValNom"
				items="${showResultToView.formaPagoValNoms}">
				<option value="${formaPagoValNom.valor}"
					${formaPagoValNom.valor==showResultToView.formaPago?'selected':''}>
					${formaPagoValNom.nombre}</option>
			</c:forEach>
	</select></td>
	<td colspan="4"><input type="button" class="boton_color"
		name="buscar" id="buscar"
		value=<fmt:message key="buscar" bundle="${lang}" /> tabindex=""
		onclick="javascript:buscarSolicitudReporte();" /> &nbsp;</td>
</tr>


<input type="hidden" name="filtrarByMesOrComplementoOrAmbos" value="" />
<input type="hidden" name="meses" value="" />
<input type="hidden" name="keyReport" value="" />
<input type="hidden" name="numSolicituds" value="" />
