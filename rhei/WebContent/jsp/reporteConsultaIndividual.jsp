<!-- <!-- Toda esta info va para el controlador BenefAndCentrEducControlador, y se devuelve a la pagina accion que es el hidden -->
<!-- que esta en el form que llama esta pagina -->

<tr>
	<td>
		<div class="derecha">
			<!-- 		Si no consultamos para buscar pagos, abre esta ventana -->
			<table cellspacing="5" cellpadding="2" class="formato10 anchoTabla4">

				<!-- Lista de Beneficiarios (Niños) y Lista de Rif (Colegios)-->

				<tr>
					<td><label><fmt:message key="estaus" bundle="${lang}" />:</label></td>
					<td colspan="10"><select name="status" id="status"
						class="entrada" onchange="changeReporteDef();" size="1"
						tabindex="">
							<c:forEach var="prov" items="${statusLst}">
								<option value="${prov.valor}">${prov.nombre}</option>
							</c:forEach>

					</select></td>
				</tr>


				<tr>
					<td><label><fmt:message key="periodo" bundle="${lang}" />:</label></td>
					<td colspan="10"><select name="periodoEscolar"
						id="periodoEscolar" class="entrada" onchange="changeReporteDef();"
						size="1" tabindex=""><option value=""><fmt:message
									key="select.seleccione" bundle="${lang}" /></option>
							<c:forEach var="prov"
								items="${showResultToView.periodoEscolares}">
								<option value="${prov.valor}"
									${showResultToView.periodoEscolar==prov.valor?'selected':''}>${prov.nombre}</option>
							</c:forEach>

					</select></td>
				</tr>


				<tr>
					<td><label><fmt:message key="pago.dirigido"
								bundle="${lang}" />:</label></td>
					<td colspan="10"><select name="receptorPago" class="entrada"
						onchange="changeReporteDef();" id="receptorPago" size="1"
						tabindex="">
							<c:forEach var="obj" items="${showResultToView.receptorPagos}">
								<option value="${obj.valor}"
									${obj.valor==showResultToView.receptorPago?'selected':''}>${obj.nombre}</option>
							</c:forEach>
					</select></td>
				</tr>


				


				<tr ng-hide="hideTipoEmpl() > 0">
					<td><label><fmt:message key="tipoEmpleado"
								bundle="${lang}" />:</label></td>
					<td><select ng-model="selectValue"
					            name="tipoEmpleadoAngular" class="entrada" id="tipoEmpleadoAngular" size="1"
						ng-options="item.place for item in todos">
					</select>
					<input type="hidden" name="tipoEmpleado" id="tipoEmpleado" value="{{selectValue.id}}" />
				 
				
					</td>
				</tr>



				<tr>
					<td><label><fmt:message key="pagos.forma"
								bundle="${lang}" />:</label></td>
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
					<td><label><fmt:message key="reporte.especial"
								bundle="${lang}" />:</label></td>
					<td> <input type="radio" checked name="ninoEspecial" value="0">
						<fmt:message key="no" bundle="${lang}" /><br>
						<input type="radio" name="ninoEspecial" value="1">
						<fmt:message key="si" bundle="${lang}" /><br>
						</td>
				</tr>
				
				<tr>

					<td><label for="nombreDefinitivoTransitorio"><fmt:message
								key="cedula" bundle="${lang}" />:</label></td>
					<td><input id="cedulaEmpleado" type=text 	ng-pattern="onlyNumbers"	onkeypress="if (event.keyCode == 13) event.returnValue = false;"
					onkeyup="if (event.keyCode == 13) event.returnValue = false;"
						ng-model="cedulaEmpleado" class=entrada name="cedulaEmpleado"
						value="${cedulaEmpleado}" tabindex="" maxlength=60 size=60 /></td>

				</tr>
				<tr>
				<td><fmt:message key="filtrado.por" bundle="${lang}" /><br></td>
					<td><input type="radio" name="mesMatrCompl"  value="0">
						<fmt:message key="reporte.mes.matricula" bundle="${lang}" /><br>
						<input type="radio" name="mesMatrCompl" value="1"> <fmt:message
							key="reporte.complemento.rembolso" bundle="${lang}" /><br>
						<input type="radio" name="mesMatrCompl" checked value="2"> <fmt:message
							key="reporte.ambos" bundle="${lang}" /></td>
				</tr>

				<tr>
				<td></td>
					<td><input type="button" class="boton_color"
						name="buscarDatos3" id="buscarDatos3"
						value=<fmt:message key="reporte.pagoAndTributos"	bundle="${lang}" />
						tabindex="" onclick="javascript: generarReporteIndividualAjax(1);" />
						 
					</td>
				</tr>

<input type="hidden" name="pagadoNoPagadoAmboSearchs"
					value="" />
				<input type="hidden" name="filtrarByMesOrComplementoOrAmbos"
					value="" />
				<input type="hidden" name="meses" value="" />
				<input type="hidden" name="keyReport" value="" />
				<input type="hidden" name="numSolicituds" value="" />





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
