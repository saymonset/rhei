<tr>
	<td>
		<div class="derecha">
			<table cellspacing="5" cellpadding="2" class="formato10 anchoTabla4">
				<c:if test="${isAdmin=='true'}">
					<tr>
						<td><label><fmt:message key="cedula" bundle="${lang}" />:</label></td>
						<td><input type="text" class="entrada texto_der" cedula
							name="cedula" tabindex="" value="${showResultToView.cedula}"
							maxlength="8" size="8"
							onkeyup="chequea3(this,document.formBuscarDatos.buscarDatos1)"
							onkeypress="return pulsar(event)" onchange="validar3()"
							onfocus="this.value=''" /></td>
						<td colspan="3"><input type="button" class="boton_color"
							name="buscarDatos1" value="Buscar Beneficiario" tabindex=""
							onclick="buscarBeneficiario()" /> &nbsp;&nbsp;<input
							type="hidden" name="codEmp" value=${showResultToView.codEmp }
							maxlength="10" size="10" /></td>
					</tr>
				</c:if>
				
				<c:if test="${isAdmin!='true'}">
					<input type="hidden" cedula name="cedula"
						value=${showResultToView.cedula } maxlength="10" size="10" />
					<input type="hidden" name="codEmp"
						value=${showResultToView.codEmp } maxlength="10" size="10" />
				</c:if>
				
				<c:if test="${!empty showResultToView.beneficiarios}">
					<tr>
						<td><label>C&oacute;digo:</label></td>
						<td>&nbsp; <select name="codigoBenef" id="codigoBenef"
							class="entrada" onchange="changeBeneficiario();" size="1"
							tabindex=""><option value="">Seleccione...</option>
								<c:forEach var="beneficiariosvar"
									items="${showResultToView.beneficiarios}">
									<option value="${beneficiariosvar.valor}"
										${showResultToView.codigoBenef==beneficiariosvar.valor?'selected':''}>${beneficiariosvar.nombre}</option>
								</c:forEach>

						</select>
						</td>
						<input type="hidden" name="nroRifCentroEdu" id="nroRifCentroEdu"
							value="${showResultToView.nroRifCentroEdu}" />
					</tr>
				</c:if>
				<span id="benefwait" style="display: none;">

					<div style="text-align: center;">
						<img class="bannerLogin" border="0" src="imagenes/ai.gif" alt="" />
						<fmt:message key="wait.hold_on" bundle="${lang}" />
					</div>
				</span>

			</table>
		</div>
	</td>
</tr>
<tr>
	<td></td>
</tr>

