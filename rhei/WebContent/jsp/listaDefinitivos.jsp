<!-- <!-- Toda esta info va para el controlador BenefAndCentrEducControlador, y se devuelve a la pagina accion que es el hidden -->
<!-- que esta en el form que llama esta pagina -->
<tr>
	<td>
		<div class="derecha">
			<tr>
				<td>
					<div class=derecha>
						<form id="formBuscarDatos" name="formBuscarDatos" method="post"
							action="">
							<table cellspacing="5" cellpadding="2"
								class="formato10 anchoTabla8">

								<c:if
									test="${!empty periodoEscolares and isAllDefinitivos eq true }">
									<tr>
										<td><label for="beneficioEscolar"> <fmt:message
													key="periodo" bundle="${lang}" />:
										</label></td>
										<td><select name="periodoEscolar" id="periodoEscolar"
											class="entrada" size="1" tabindex=""><option
													value=""><fmt:message key="select.seleccione"
														bundle="${lang}" /></option>
												<c:forEach var="prov" items="${periodoEscolares}">
													<option value="${prov.valor}"
														${periodoEscolar==prov.valor?'selected':''}>${prov.nombre}</option>
												</c:forEach>

										</select></td>
									</tr>
									<tr>

										<td><label for="nombreDefinitivoTransitorio"><fmt:message
													key="nombre" bundle="${lang}" />:</label></td>
										<td><input id="nombreDefinitivoTransitorio" type=text
											class=entrada name=nombreDefinitivoTransitorio
											value="${nombreDefinitivoTransitorio}" tabindex=""
											maxlength=60 size=60 /></td>
										<td><input type=button class=boton_color
											name=buscarParametro
											value='<fmt:message key="buscar"	bundle="${lang}" />'
											tabindex='' onclick="listaDeReporteDefinitivo();" /></td>
									</tr>



									<tr>
										<td align=left colspan="6"><input type=button
											class=boton_color name=generarReporte
											value='<fmt:message key="reporte.pagoAndTributos.generar"	bundle="${lang}" />'
											tabindex='' onclick="generarReporteDefinitivo();" /></td>

									</tr>

									<tr>
										<td align=left colspan="6"></td>


									</tr>

								</c:if>


							</table>

							<input type="hidden" name="accion" value="listaDefinitivos0.jsp" />

							<input type="hidden" name="nroSolicitud" value="" /> <input
								type="hidden" name="vieneFromReporteByPagoAndTributo" value="1" />
							<input type="hidden" name="definitivoTransitorioHidden" value="" />

							<input type="hidden" name="keyReport" value="1" />


						</form>

					</div>

				</td>
			</tr>


			<c:if test="${tablaParametros eq 'No hay registros'}">
				<tr>
					<td>
				<tr>
					<td align=left class=aviso><fmt:message
							key="reporte.definitivo.transitivo" bundle="${lang}" /></td>
				</tr>
	</td>
</tr>

</c:if>
<!--                  onclick=\"javascript: generarReportePagoTributo("codigo");" -->
<!-- lista de detalles del reporte a continuacion -->
<!--                  Link detalles      onclick=\"javascript: generarDetalleDefinitivoController" -->
<!-- ********************************************************************************************************************************** -->
<!-- ***********************************	EMBEBIDO ${tablaParametros} ReporteDefinitivoTransitorioControlador.java->String tablaParametros = generadorTablaPagosTributos() TABLA INICIO REPORTES DEFINITIVOS*********************************************************************************************** -->
<!-- ********************************************************************************************************************************** -->

<!-- 			AQUI ES LA TABLA QUE SE FORMA EN EL CONTROLADOR PARA GEBERAR LOS REPRTES Y ESTA EN CODIGO 
                 javascript: generarReportePagoTributo-->

<!--                  generarReporteContable(); genera reportes comntables -->
<tr>
	<td></td>
</tr>
 <center>
<!--    Trae data en tabla parametros  -->
<c:if test="${tablaParametros != 'No hay registros'}">
	<tr>
		<td>
			<form name="formPaginarParametros" id="formPaginarParametros" method=post action="">
				<div id="reporteContableBlock">
					<a href="#d"
						onclick="javascript: generarReporteContable(); return false;">
						<input type="image" src="imagenes/pdf.png" alt="download" /> <fmt:message
							key="rep.repcontable" bundle="${lang}" /> <input type=hidden
						id="reporteContableDefinitivo" name="reporteContableDefinitivo"
						value="" />
					</a>
					
					
									 
					
					
					
				</div>


				${tablaParametros} <input type=hidden name="tabla" value="${tabla}" />
				<input type=hidden name="titulo" value="${titulo}" /> <input
					type=hidden name="companiaAnalista" value="${companiaAnalista}" />
				<input type=hidden name="cuantosReg" value="${cuantosReg}" /> <input
					type=hidden name="indMenor" value="${indMenor}" /> <input
					type=hidden name="indMayor" value="${indMayor}" />
					<input type=hidden name="subir" value="${subir}" />


			</form>
		</td>
	</tr>
</c:if>
</center>

<!-- ********************************************************************************************************************************** -->
<!-- ***********************************	EMBEBIDO ${tablaParametros} ReporteDefinitivoTransitorioControlador.java->String tablaParametros = generadorTablaPagosTributos() TABLA FIN REPORTES DEFINITIVOS*************************************************************************************** -->
<!-- ********************************************************************************************************************************** -->

<!-- 		Si no consultamos para buscar pagos, abre esta ventana -->











</div>
</td>
</tr>

