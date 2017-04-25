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
					<td colspan="10"><select name="statusAux" id="statusAux"  ng-model="singleSelect"
						class="entrada" size="1" tabindex="">
							<c:forEach var="prov" items="${statusLst}">
								<option value="${prov.valor}"
									${status==prov.valor?'selected':''}>${prov.nombre}</option>
							</c:forEach>

					</select></td>
				</tr>
                
				<tr ng-show="singleSelect=='A' || singleSelect=='D'">
					<td><label><fmt:message key="periodo" bundle="${lang}" />:</label></td>
					<td colspan="10"><select name="periodoEscolar"
						id="periodoEscolar" class="entrada" size="1" tabindex=""><option
								value=""><fmt:message key="select.seleccione"
									bundle="${lang}" /></option>
							<c:forEach var="prov"
								items="${showResultToView.periodoEscolares}">
								<option value="${prov.valor}"
									${periodoScolar==prov.valor?'selected':''}>${prov.nombre}</option>
							</c:forEach>

					</select></td>
				</tr>
				  
				<tr ng-show=singleSelect=='A'>
					<td><label><fmt:message key="tipoEmpleado" bundle="${lang}" />:</label></td>
					<td colspan="10"><select name="tipoEmpleado"
						id="tipoEmpleado" class="entrada" size="1" tabindex=""><option
								value=""><fmt:message key="select.seleccione"
									bundle="${lang}" /></option>
							<c:forEach var="prov"
								items="${tipoEmpleados}">
								<option value="${prov.valor}"
									${tipoEmpleado==prov.valor?'selected':''}>${prov.nombre}</option>
							</c:forEach>

					</select></td>
				</tr>
				 
			
				<tr>
					<td></td>
					<td><input type="button" class="btn-primary"
					    ng-show=singleSelect=='A'
						name="buscarDatos3" id="buscarDatos3"
						value='<fmt:message key="reporte.pagoAndTributos"	bundle="${lang}" />'
						tabindex="" onclick="javascript: buscarBecaUtile(3,'A');" />
						
						<input type="button" class="btn-primary"
					    ng-show=singleSelect=='D'
						name="buscarDatos3" id="buscarDatos3"
						value='<fmt:message key="reporte.pagoAndTributos"	bundle="${lang}" />'
						tabindex="" onclick="javascript: buscarBecaUtile(3,'D');" />
						</td>
				</tr>
				<input type="hidden" name="keyReport" value="" />
				<input type="hidden" name="txObservacion" value="" />
				<input type="hidden" name="status" value="" />
					 





				<!-- FIN Lista de Beneficiarios (Niños) y Lista de Rif (Colegios)-->



			</table>




		</div>
	</td>
</tr>
<div id="angularRegion">
	<c:if test="${not empty reporteBecaUtiles}">
		<%@ include file="reporteBecaUtileActivos.jsp"%>
	</c:if>
		<c:if test="${not empty reporteBecaUtilesDesactivados}">
		<%@ include file="reporteBecaUtileDesincorporados.jsp"%>
	</c:if>
</div>




