<tr>
	<td>
		<div ng-show="isMostrar" class="panel-body">
			<h2 class="panel-heading">
				<fmt:message key="reporte.reportUtiles" bundle="${lang}" />
			</h2>
			<div class="container">

				<div class="well">
					<div class="form-group">
						<label for="TX_OBSERVACION"><fmt:message
								key="nombreReport" bundle="${lang}" />:</label> <input type="text"
							maxlength="200" class="form-control" name="TX_OBSERVACION"
							id="TX_OBSERVACION" required style="width: 330px; height: 30px;" />
					</div>
					 

					<input type="button" class="btn-primary" name="buscarDatos3"
							id="buscarDatos3"
							value='<fmt:message key="reporte.pagoAndTributos.generar"	bundle="${lang}" />'
							tabindex="" onclick="javascript: generarReporteBecaUtile(3);" />
				</div>



			 

				<table class="table table-striped table-bordered"
					style="height: 35px;">
					<thead>
						<tr>
							<th><fmt:message key="periodo" bundle="${lang}" /></th>
							<th><fmt:message key="trabajador" bundle="${lang}" /></th>
							<th><fmt:message key="cedula" bundle="${lang}" /></th>
							<th><fmt:message key="nombreNino" bundle="${lang}" /></th>
						</tr>
					</thead>
					<!-- accion  7 ES ACTUALIZAR -->
					<tbody>
						<c:forEach var="item" items="${reporteBecaUtiles}">
							<tr>
								<td>${item.periodo}</td>
								<td>${item.trabajador}</td>
								<td>${item.nuCedula}</td>
								<td>${item.nombreNino}</td>
							</tr>
						</c:forEach>

					</tbody>
				</table>
			</div>
	</td>
</tr>