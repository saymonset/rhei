


<tr>
	<td colspan="4" class="fondo_seccion"><label class="subtitulo">Registro
			de Pago </label></td>
</tr>

<tr>
	<td><label>Fecha de Registro:</label></td>
	<td><input type="text" name="fechaRegistro" id="fechaRegistro"
		value="${showResultToView.fechaRegistro}" tabindex="26" maxlength="8"
		size="8" onfocus="blur()" /></td>
	<td><label><fmt:message
											key="pago.fecha.factura" bundle="${lang}" /></label></td>
	<td><c:if test="${not empty showResultToView.disabledRegPagos}">
													   ${showResultToView.fechaFactura}
													</c:if> <c:if test="${empty showResultToView.disabledRegPagos}">
			<input name="fechaFactura" class="entrada"
				value="${showResultToView.fechaFactura}"
				onkeyup="mascara(this,'-','fecha',true)" tabindex="28"
				id="fechaFactura" maxlength="10" size="10" type="text">
		</c:if></td>

</tr>

<tr ng-repeat="item in products">
	<td>{{item.nroFactura}}</td>
	<td>{{item.nroRifProv}}</td>
</tr>


<tr>
	<td><label>Estatus de Pago:</label></td>
	<td><input name="estatusPago"
		value="${showResultToView.estatusPago}" tabindex="27" id="27"
		maxlength="20" size="20" disabled="disabled" type="text"></td>
	<td><c:if
			test="${not empty showResultToView.listadoMesesPorPagar}">
			<label><fmt:message key="pagos.mes.pagar.reembolso"
					bundle="${lang}" /></label>
		</c:if></td>
	<td>

		<ul class="ulsinfondo">
			<c:if test="${not empty showResultToView.listadoMesesPorPagar}">
				<c:forEach var="obj"
					items="${showResultToView.listadoMesesPorPagar}">
					<li>${obj.nombre}</li>
				</c:forEach>
			</c:if>
		</ul>

	</td>

</tr>
<tr>
	<td><label>Concepto de Pago:</label></td>
	<td><label>Matrícula</label> <input type="checkbox"
		name="conceptoPago" ${showResultToView.checkedMatricula} value="0"
		tabindex="32" id="32" disabled /></td>
	<td><label>Período</label> <input name="conceptoPago"
		class="entrada" value="1" disabled tabindex="33"
		${showResultToView.checkedPeriodo} id="33" type="checkbox"></td>


</tr>
<c:if test="${showResultToView.accion!=7}">
	<tr>
		<td><label><fmt:message
											key="pago.dirigido" bundle="${lang}" />:</label></td>
		<td><select name="receptorPago" class="entrada"
			${showResultToView.disabledRegPagos} id="receptor" size="1"
			tabindex="">
				<option value="">Seleccione...</option>
				<c:forEach var="obj" items="${showResultToView.receptorPagos}">
					<option value="${obj.valor}"
						${obj.valor==showResultToView.receptorPago?'selected':''}>${obj.nombre}</option>
				</c:forEach>
		</select></td>
		<td><label>Mes(es) pagados:</label></td>
		<td>
			<ul class="ulsinfondo">
				<c:forEach var="obj" items="${showResultToView.listadoMesesPagados}">
					<li>${obj.nombre}</li>
				</c:forEach>
			</ul>

		</td>

	</tr>
</c:if>
<tr>
	<td><label><fmt:message key="pago.de" bundle="${lang}" />
			<fmt:message key="pago.num.factura" bundle="${lang}" />
			${showResultToView.nroFactura}: </label></td>
	<td>
		<ul class="ulsinfondo">
			<c:forEach var="obj" items="${showResultToView.listadoMesPagByFact}">
				<!-- 			el numero 7 es actualizar pago -->
				<c:if test="${showResultToView.accion=='7'}">
					<li><a href="#"
						onclick="javascript: deleteFactura('${obj.valor}','${obj.nombre}'); return false;">${obj.nombre}</a></li>
				</c:if>
				<!-- 			el numero 7 es actualizar pago -->
				<c:if test="${showResultToView.accion!='7'}">
					<li>${obj.nombre}</li>
				</c:if>
			</c:forEach>
		</ul>

	</td>

	<td><label> <fmt:message
				key="reporte.complemento.rembolso" bundle="${lang}" /> <fmt:message
				key="pago.num.factura" bundle="${lang}" />
			${showResultToView.nroFactura} :
	</label></td>
	<td>
		<ul class="ulsinfondo">
			<c:forEach var="obj"
				items="${showResultToView.listadoMesPagByFactOnlyComplemento}">
				<!-- 			el numero 7 es actualizar pago -->
				<c:if test="${showResultToView.accion=='7'}">
					<li><a href="#"
						onclick="javascript: deleteFactura('${obj.valor}','${obj.nombre}'); return false;">${obj.nombre}</a></li>
				</c:if>
				<!-- 			el numero 7 es actualizar pago -->
				<c:if test="${showResultToView.accion!='7'}">
					<li>${obj.nombre}</li>
				</c:if>


			</c:forEach>
		</ul>

	</td>



</tr>

<tr>
	<td><label> <fmt:message key="pago.num.factura"
				bundle="${lang}" />:
	</label></td>
	<td><c:if
			test="${not empty showResultToView.disabledRegPagos && showResultToView.nroFactura!='Pendiente Entrega'}">
													   ${showResultToView.nroFactura}
													   	 <input name="nroFactura" type="hidden" id="nroFactura"
				value="${showResultToView.nroFactura}"></input>
		</c:if> <c:if
			test="${empty showResultToView.disabledRegPagos || showResultToView.nroFactura=='Pendiente Entrega'}">
			<input name="nroFactura" class="entrada"
				value="${showResultToView.nroFactura}" tabindex="29" id="nroFactura"
				maxlength="20" size="20" type="text">
		</c:if></td>

	<td><label>N° Control:</label></td>
	<td><c:if
			test="${not empty showResultToView.disabledRegPagos && showResultToView.nroControl!='Pendiente Entrega'}">
													  ${showResultToView.nroControl}
													  	 <input name="nroControl" type="hidden"
				value="${showResultToView.nroControl}"></input>
		</c:if> <c:if
			test="${empty showResultToView.disabledRegPagos || showResultToView.nroControl=='Pendiente Entrega'}">
			<input name="nroControl" class="entrada"
				value="${showResultToView.nroControl}" tabindex="30" id="30"
				maxlength="20" size="20" type="text">
		</c:if></td>
</tr>
<tr>
	<td><label><fmt:message key="pago.monto.factura"
				bundle="${lang}" />:</label></td>
	<td><input type="text" name="montoFactura" class="ulsinfondo"
		value="${showResultToView.montoFactura}" id="montoFactura"
		maxlength="12" size="12" onfocus="blur()" /></td>
	<td><label> <fmt:message key="pagos.forma"
				bundle="${lang}" /> :
	</label></td>
	<td><select name="formaPago" class="entrada" size="1"
		${showResultToView.disabledRegPagos} tabindex="46" id="formaPago">
			<option value="">Seleccione...</option>
			<c:forEach var="formaPagoValNom"
				items="${showResultToView.formaPagoValNoms}">
				<option value="${formaPagoValNom.valor}"
					${formaPagoValNom.valor==showResultToView.formaPago?'selected':''}>
					${formaPagoValNom.nombre}</option>
			</c:forEach>
	</select></td>
</tr>




<tr>
	<td><c:if test="${not empty showResultToView.txtcomplemento}">
			<label> <fmt:message key="pago.txtdescripcion"
					bundle="${lang}" /> :
			</label>
		</c:if></td>
	<td><c:if test="${not empty showResultToView.txtcomplemento}">
			<textarea class="entrada" ${showResultToView.disabledRegPagos}
				name="txtcomplementoTxtArea" id="txtcomplementoTxtArea" cols=40
				rows=10>
													${showResultToView.txtcomplemento}
													</textarea>
		</c:if></td>
	<td><c:if test="${not empty showResultToView.txtcomplemento}">
			<label><fmt:message key="pago.montocomplemento"
					bundle="${lang}" />:</label>
		</c:if></td>
	<td><c:if test="${not empty showResultToView.txtcomplemento}">
			<input type="text" name="montocomplemento" class="ulsinfondo"
				value="${showResultToView.montocomplemento}" tabindex="31"
				id="montocomplemento" maxlength="12" size="12" onfocus="blur()" />
		</c:if></td>
</tr>



