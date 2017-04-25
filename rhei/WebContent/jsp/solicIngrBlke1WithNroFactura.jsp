<!-- <!-- Toda esta info va para el controlador BenefAndCentrEducControlador, y se devuelve a la pagina accion que es el hidden -->
<!-- que esta en el form que llama esta pagina -->

<tr>
	<td>
		<div class="derecha">
			<span id="benefwait" style="display: none;">
				<div style="text-align: center;">
					<img class="bannerLogin" border="0" src="imagenes/ai.gif" alt="" />
					<fmt:message key="wait.hold_on" bundle="${lang}" />
				</div>
			</span>
			<!-- 		Si no consultamos para buscar pagos, abre esta ventana -->
			<table cellspacing="5" cellpadding="2" class="formato10 anchoTabla4">
				<!--  Bloque Cedula identidad , Boton Buscar Beneficiario -->
				<tr>
					<td><label>C&eacute;dula de Identidad:</label></td>
					<td><input type="text" class="entrada texto_der" name="cedula"
						tabindex="" value="${showResultToView.cedula}" maxlength="8"   
						size="8"
						onkeyup="chequea3(this,document.formBuscarDatos.buscarDatos1)"
						onkeypress="return pulsar(event)"
						onchange="validar3()" onfocus="this.value=''" /></td>


					<td colspan="4"><input type="button" class="boton_color" 
						name="buscarDatos1" value="Buscar Beneficiario" tabindex=""
						onclick="benefCodPeriodoPagoControlador()" /> &nbsp;&nbsp;<input
						type="hidden" name="codEmp" value=${showResultToView.codEmp }
						maxlength="10" size="10" /></td>
				</tr>
				<!-- Fin Bloque Cedula identidad , Boton Buscar Beneficiario-->
				<c:if
					test="${!empty showResultToView.beneficiarios || !empty showResultToView.listadoProv}">
					<!-- Lista de Beneficiarios (Niños) y Lista de Rif (Colegios)-->
					<tr>
						<td><label>C&oacute;digo:</label></td>
						<td>&nbsp; <select name="codigoBenef" class="entrada"  ${showResultToView.disabledRegPagos}
						  onchange="buscarPagosFacturas();"
							size="1" tabindex=""><option value="">Seleccione...</option>
								<c:forEach var="beneficiariosvar"
									items="${showResultToView.beneficiarios}">
									<option value="${beneficiariosvar.valor}"
										${showResultToView.codigoBenef==beneficiariosvar.valor?'selected':''}>${beneficiariosvar.nombre}</option>
								</c:forEach>

						</select>
						</td>
					 
					
					</tr>
				 
					<!-- FIN Lista de Beneficiarios (Niños) y Lista de Rif (Colegios)-->
				</c:if>

				 


				<!-- Fin Bloque de Nro Solicitud busqueda-->
			</table>



		</div>
	</td>
</tr>
