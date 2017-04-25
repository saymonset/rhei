<div class="panel-body">
	<h2 class="panel-heading">
		<fmt:message key="facturas" bundle="${lang}" />
		<fmt:message key="por" bundle="${lang}" />
		<fmt:message key="detalles" bundle="${lang}" />
	</h2>
	<table class="table table-striped table-bordered">
		<thead>
			<tr>
			<th><fmt:message key="nbPagadoPor" bundle="${lang}" /></th>
				<c:if test="${showResultToView.accion eq 7}">
					<th></th>
				</c:if>
				<th><fmt:message
									key="monto.otorgado.maximo.bcv" bundle="${lang}" /></th>
				<th><fmt:message key="pago.fecha.factura" bundle="${lang}" /></th>
				<th><fmt:message key="pago.num.factura" bundle="${lang}" /></th>
				<th><fmt:message key="observaciones" bundle="${lang}" /></th>
				<th class="text-right"><fmt:message key="pago.monto.factura"
						bundle="${lang}" /></th>
				<th><fmt:message key="pagos.forma" bundle="${lang}" /></th>
				<th><fmt:message key="pago.dirigido" bundle="${lang}" /></th>
				<th><fmt:message key="reporte.definitivo" bundle="${lang}" /></th>
			</tr>
		</thead>
		<!-- accion  7 ES ACTUALIZAR -->
		<tbody>
			<c:forEach var="item" items="${facturas}">
				<tr pago="${item.statusReporteDefinitivo}">
	               <td>${item.nbPagadoPor}</td>
					<c:if test="${showResultToView.accion eq 7}">
						<td><a href="#"
							onclick="javascript: deleteFactura('${item.nroIdFactura}','${item.txObservaciones}'); return false;"><fmt:message
									key="pago.eliminar" bundle="${lang}" /></a></td>
					</c:if>
					<td ng-show="${!item.reembolso}">${item.moAporteBcv}</td>
					<td ng-show="${item.reembolso}"></td>
					<td>${item.fechaFactura}</td>
					<td>${item.nroFactura}</td>
					<td>${item.txObservaciones}</td>
					<td highlight="${item.montoFactura}" class="text-right">${item.stringMontoFactura}</td>
					<td>${item.txFormaPago}</td>
					<td>${item.stringReceptorPago}</td>
					<td>
					${item.nombReportPagDef} 
					<c:if test="${item.coRepStatus!=0}">
					<a href="#" 
						onclick="javascript: generarReportePagoTributo('${item.coRepStatus}'); return false;">
							<input type="image" src="imagenes/pdf.png" alt="${item.nombReportPagDef}" />
					</a> 
					</c:if>
					</td>

				</tr>
			</c:forEach>

		</tbody>
	</table>
</div>
<!-- accion  6 ES CONSULTA -->
<c:if test="${showResultToView.accion eq 6}">
	<div class="panel-body">
		<h2 class="panel-heading">
			<fmt:message key="facturas" bundle="${lang}" />
			<fmt:message key="por" bundle="${lang}" />
			<fmt:message key="cei" bundle="${lang}" />
		</h2>
		<table class="table table-striped table-bordered">
			<thead>
				<tr>
					<th><fmt:message key="nombreColegio" bundle="${lang}" /></th>
					<th><fmt:message key="inst.rif" bundle="${lang}" /></th>
					<th><fmt:message key="trabajador" bundle="${lang}" /></th>
					<th><fmt:message key="nombreNino" bundle="${lang}" /></th>
					<th><fmt:message key="concepto" bundle="${lang}" /></th>
					<th><fmt:message key="monto" bundle="${lang}" /></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="item" items="${listCEI}">
					<tr>
						<td>${item.nombreColegio}</td>
						<td>${item.rif}</td>
						<td>${item.trabajador}</td>
						<td>${item.nombreNino}</td>
						<td>${item.concepto}</td>
						<td>${item.montoTotalStr}</td>
					</tr>
				</c:forEach>

			</tbody>
		</table>
	</div>


	<div class="panel-body">
		<h2 class="panel-heading">
			<fmt:message key="facturas" bundle="${lang}" />
			<fmt:message key="por" bundle="${lang}" />
			<fmt:message key="trabajador" bundle="${lang}" />
		</h2>
		<table class="table table-striped table-bordered">
			<thead>
				<tr>
					<th><fmt:message key="nombreColegio" bundle="${lang}" /></th>
					<th><fmt:message key="cedula" bundle="${lang}" /></th>
					<th><fmt:message key="trabajador" bundle="${lang}" /></th>
					<th><fmt:message key="nombreNino" bundle="${lang}" /></th>
					<th><fmt:message key="concepto" bundle="${lang}" /></th>
					<th><fmt:message key="monto" bundle="${lang}" /></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="item" items="${listEMP}">
					<tr>
						<td>${item.nombreColegio}</td>
						<td>${item.cedula}</td>
						<td>${item.trabajador}</td>
						<td>${item.nombreNino}</td>
						<td>${item.concepto}</td>
						<td>${item.montoTotalStr}</td>
					</tr>
				</c:forEach>

			</tbody>
		</table>
	</div>
</c:if>