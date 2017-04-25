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
						<td>&nbsp; <select name="codigoBenef" class="entrada"
							size="1" tabindex=""><option value="">Seleccione...</option>
								<c:forEach var="beneficiariosvar"
									items="${showResultToView.beneficiarios}">
									<option value="${beneficiariosvar.valor}"
										${showResultToView.codigoBenef==beneficiariosvar.valor?'selected':''}>${beneficiariosvar.nombre}</option>
								</c:forEach>

						</select>
						</td>
						<td><label>Período Escolar:</label></td>
						<td>&nbsp; <select name="periodoEscolar" class="entrada"
							size="1" tabindex=""><option value="">Seleccione...</option>
								<c:forEach var="prov"
									items="${showResultToView.periodoEscolares}">
									<option value="${prov.valor}"
										${showResultToView.periodoEscolar==prov.valor?'selected':''}>${prov.nombre}</option>
								</c:forEach>

						</select>
						</td>
					
					</tr>
					<tr>
						<td colspan="5"></td>
						<td><input type="button" class="boton_color"
							name="buscarDatos3" value="Buscar Solicitud" tabindex=""
							onclick="buscarPagosFacturas()" />&nbsp;</td>
					</tr>
					<!-- FIN Lista de Beneficiarios (Niños) y Lista de Rif (Colegios)-->
				</c:if>

				<c:if
					test="${empty showResultToView.beneficiarios && empty showResultToView.listadoProv}">
					<!-- Bloque de Nro Solicitud busqueda-->
					
					<tr>
					<td><label>Nro Solicitud</label><br />
						<br /> <span id="nroFacturaNone1" style="display: none;"> <label>Nro
									Factura</label>
						</span></td>
					<td>
					<!-- El nombre nombreAjax es el numroSolicitud esta reflejado en el archivo searchFacturasAjax.js para filtrar el select 
                                                $( "#nombre" ).keypress(function-->
							<input type="text" name="nombreAjax" id="nombreAjax"
							class="entrada texto_der" value="" tabindex="5" maxlength="9"
							size="9"
							onkeypress="if (event.keyCode == 13) event.returnValue = false;"
							onchange="return validarSiNumero(this)" /> <br />
						<br /> <span id="nroFacturaNone2" style="display: none;"> <select
								name="factura" onchange="buscarPagosFacturasId();"
								id="factura" style="width: 400px" class="entrada" size="1"
								tabindex="">
									<option value="0">
										<fmt:message key="select.seleccione" bundle="${lang}" />
									</option>
							</select>
						</span>
					
					</td>
					<td colspan="4">
					<!-- El nombre nombreAjax es el numroSolicitud esta reflejado en el archivo searchFacturasAjax.js para filtrar el select 
                                                $( "#nombre" ).keypress(function-->
					 <input type="button"
							class="boton_color" value="Buscar Facturas" tabindex=""
							id="buscarFacturasAjax" name="buscarFacturasAjax" />
					</td>
				</tr>
				 


					<!-- Fin Bloque de Nro Solicitud busqueda-->
				</c:if>


				<!-- Fin Bloque de Nro Solicitud busqueda-->
			</table>



		</div>
	</td>
</tr>
