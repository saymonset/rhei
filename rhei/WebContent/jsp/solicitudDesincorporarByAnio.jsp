<!-- <!-- Toda esta info va para el controlador BenefAndCentrEducControlador, y se devuelve a la pagina accion que es el hidden -->
<!-- que esta en el form que llama esta pagina -->
<c:if test="${empty showResultToView.beneficiarios}">
	<tr>
		<td>
			<div class="derecha">
				<!-- 		Si no consultamos para buscar pagos, abre esta ventana -->
				<table cellspacing="5" cellpadding="2" class="formato10 anchoTabla4">
					<tr>
						<td colspan="3" style="width: 160px;"><label><fmt:message
									key="periodoEscolar" bundle="${lang}" />:</label></td>
						<td style="width: 185px;"><select name="periodoEscolar"
							id="periodoEscolar" class="entrada" size="1" tabindex="143">
								<option value=""><fmt:message key="select.seleccione"
										bundle="${lang}" /></option>
								<c:forEach var="obj"
									items="${showResultToView.periodoEscolares}">
									<option value="${obj.valor}"
										${showResultToView.periodoEscolar==obj.valor?'selected':''}>${obj.nombre}</option>
								</c:forEach>
						</select></td>
						<td colspan="3" style="width: 208px;"><input type="button"
							class="boton_color"
							onkeypress="if (event.keyCode == 13) event.returnValue = false;"
							name="buscarDatos1Anio"
							value="<fmt:message key="solicitud.desincorporar.Byanio"
								bundle="${lang}" />"
							tabindex="" onclick="desincorporarByAnio()" /> &nbsp;&nbsp;</td>
					</tr>
					<!-- Fin Bloque de Nro Solicitud busqueda-->
				</table>



			</div>
		</td>
	</tr>
</c:if>