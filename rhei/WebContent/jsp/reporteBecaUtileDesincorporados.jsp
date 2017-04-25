<tr>
	<td>
		<div ng-show="isMostrar" class="panel-body">
			<h2 class="panel-heading">
				<fmt:message key="reporte.reportUtiles" bundle="${lang}" />
			</h2>
			<div class="container">
				<table class="table table-striped table-bordered"
					style="height: 35px;">
					<thead>
						<tr>
							<th><fmt:message key="periodo" bundle="${lang}" /></th>
							<th><fmt:message key="nombreReport" bundle="${lang}" /></th>
							<th></th>
						</tr>
					</thead>
					<!-- accion  7 ES ACTUALIZAR -->
					<tbody>
						<c:forEach var="item" items="${reporteBecaUtilesDesactivados}">
							<tr>
								<td>${item.periodo}</td>
								<td>${item.txObservacion}
								</td>
								<td><a href="#d"
									onclick="javascript: buscarBecaUtileDesincorporados('${item.periodo}','${item.txObservacion}','D'); return false;">
										<input type="image" src="imagenes/pdf.png" alt="download" />
								</a></td>
							</tr>
						</c:forEach>

					</tbody>
				</table>
			</div>
	</td>
</tr>

<script>
function buscarBecaUtileDesincorporados(periodo,txObservacion,status) {
             var campo = document.formBuscarDatos;
             	campo.action = '/rhei/documentOpenRporte';
					campo.keyReport.value = 3;
					campo.periodoEscolar.value=periodo;
					campo.txObservacion.value = txObservacion;
					campo.status.value=status;
					campo.submit();
}
</script>