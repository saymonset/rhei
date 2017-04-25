<%@ page contentType="text/html; charset=iso-8859-1" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setBundle basename="ve.org.bcv.rhei.util.bundle" var="lang" />
<fmt:setLocale value="es" />
<%
	if (session.isNew()) {
%>
<jsp:forward page="login.jsp">
	<jsp:param name="aviso" value="FinSesion" />
</jsp:forward>
<%
	}
%>
<!DOCTYPE html>
<html ng-app="todoApp">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title><fmt:message key="benef.pricipal.variables"
		bundle="${lang}" /></title>
<script type="text/javascript" src="/rhei/js/angular.js"></script>
<script type="text/javascript" src="/rhei/js/angular-resource.js"></script>
<link rel="stylesheet" type="text/css" href="/rhei/css/bootstrap.css" />
<link rel="stylesheet" type="text/css"
	href="/rhei/css/bootstrap-theme.css" />
<link rel="stylesheet" type="text/css" href="/rhei/css/menu.css" />
<link rel="stylesheet" type="text/css" href="/rhei/css/estilos.css" />
<!--[if lt IE 8]>
   <style type="text/css">
   li a {display:inline-block;}
   li a {display:block;}
   </style>
   <![endif]-->



<script type="text/javascript" src="/rhei/js/jquery-1.6.1.min.js"></script>
<script type="text/javascript" src="/rhei/js/searchFacturasAjax.js"></script>
<script type="text/javascript" src="/rhei/js/searchReporteAjax.js"></script>
<script type="text/javascript" src="/rhei/js/funciones.js"></script>
<script type="text/javascript" src="/rhei/js/detalleDefHistorico.js"></script>
<script type="text/javascript"
	src="/rhei/js/jquery-ui-1.11.3.custom/jquery-ui.js"></script>
<script type="text/javascript"
	src="/rhei/js/jquery-ui-1.11.3.custom/configCal.js"></script>
<link rel="stylesheet"
	href="/rhei/js/jquery-ui-1.11.3.custom/jquery-ui.css">
<script type="text/javascript" language="JavaScript">
	window.history.forward(1);

	$(function($) {
		$("#fechaFactura").datepicker();
	});
</script>



<script>
	var myApp = angular.module("todoApp", []);

	myApp.controller("ToDoCtrl", function($scope) {
	});

	myApp.directive("highlight", function() {
		return function(scope, element, attrs) {
			if (attrs["highlight"] < 0) {
				element.css("color", "red");
			}
		}
	});

	myApp.directive("pago", function() {
		return function(scope, element, attrs) {
			if (attrs["pago"] == 1) {
				element.css("color", "green");
			}
		}
	});
</script>

<script type="text/javascript">
	
</script>


</head>
<body ng-controller="ToDoCtrl" onload="actualizaReloj();  ">

	<jsp:include page="encabezado.jsp" flush="true" />
	<div class="centraTabla" ng-controller="ToDoCtrl">
		<table cellspacing="0" cellpadding="0">
			<tr>
				<td valign="top"><div class="izquierda"><jsp:include
							page="menu.jsp" flush="true" /></div></td>
				<td>
					<table cellspacing="0" cellpadding="0" class="anchoTabla4">
						<tr>
							<td colspan="1" class="titulo"><fmt:message key="reporte.desincorporar" bundle="${lang}"/></td>
						</tr>

						<c:if test="${not empty showResultToView.mensaje}">

							<c:if test="${showResultToView.mensaje=='exito'}">
								<tr>
									<td colspan=1 class=avisoExito><fmt:message
											key="pagos.consultar.exito" bundle="${lang}" />
								</tr>
							</c:if>

							<c:if test="${showResultToView.mensaje=='fracaso3'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="pagos.convencional.fracaso3" bundle="${lang}" /></td>
								</tr>
							</c:if>
						</c:if>
						<form id="form1" name="formBuscarDatos" method="post"
							action="/rhei/detalleDefinitivoController">

							<table id="f1" name="f1"
								class="table table-striped table-bordered">
								<thead>
									<tr>
										<th><fmt:message key="pago.fecha.factura"
												bundle="${lang}" /></th>
										<th><fmt:message key="nombreColegio" bundle="${lang}" /></th>
										<th><fmt:message key="trabajador" bundle="${lang}" /></th>
										<th><fmt:message key="nombreNino" bundle="${lang}" /></th>
										<th><fmt:message key="monto.periodo" bundle="${lang}" /></th>
										<th><fmt:message key="concepto" bundle="${lang}" /></th>
										<th><fmt:message key="monto" bundle="${lang}" /></th>
										<th><fmt:message key="observaciones" bundle="${lang}" /></th>
									</tr>
								</thead>
								<!-- accion  7 ES ACTUALIZAR -->
								<tbody>
									<c:forEach var="item" items="${listaHistorico}">
										<tr class="label-warning">
											<td>${item.fechaStr}</td>
											<td>${item.nombreColegio}</td>
											<td>${item.trabajador}</td>
											<td>${item.nombreNino}</td>
											<td>${item.montoMensualPagTrabStr}</td>
											<td>${item.concepto}</td>
											<td>${item.montoTotalStr}</td>
											<td>${item.observacion}</td>


										</tr>
									</c:forEach>




									<input type="hidden" name="lista" value="${lista}" />
									<input type="hidden" name="coRepStatus" value="${coRepStatus}" />

								</tbody>
							</table>

							<table id="observacionfrm" name="observacionfrm" class="table">
								<tr>

									<!-- 							<td colspan="1"><a href="javascript:seleccionar_todo()">Marcar -->
									<!-- 									todos</a></td> -->
									<!-- 							<td colspan="1"><a href="javascript:deseleccionar_todo()">Marcar -->
									<!-- 									ninguno</a></td> -->

									<td><fmt:message key="observaciones" bundle="${lang}" />
										<textarea STYLE="height: 70px; width: 992px;" rows="10"
											cols="70" id="observacion" name="observacion"
											placeholder="Ingrese la Observacion" value=""></textarea></td>
								</tr>
								<tr>
									<td><input type="button" class="boton_color2"
										name="desincorporar"
										value="<fmt:message key="reporte.desincorporar" bundle="${lang}"/>"
										tabindex="5" id="5"
										onclick="javascript:desincorporarDefinitivo(document.formBuscarDatos)" />
									</td>
								</tr>
							</table>
							<table id="f1" name="f1"
								class="table table-striped table-bordered">
								<thead>
									<tr>
										<c:if test="${showResultToView.accion eq 7}">
											<th></th>
										</c:if>
										<th><fmt:message key="nombreColegio" bundle="${lang}" /></th>
										<th><fmt:message key="trabajador" bundle="${lang}" /></th>
										<th><fmt:message key="nombreNino" bundle="${lang}" /></th>
										<th><fmt:message key="monto.periodo" bundle="${lang}" /></th>
										<th><fmt:message key="concepto" bundle="${lang}" /></th>
										<th><fmt:message key="monto" bundle="${lang}" /></th>
										<th><fmt:message key="codigo" bundle="${lang}" /></th>
										<th><fmt:message key="solicitud.numero" bundle="${lang}" /></th>
									</tr>
								</thead>
								<!-- accion  7 ES ACTUALIZAR -->
								<tbody>
									<c:forEach var="item" items="${lista}">
										<tr>
											<td>${item.nombreColegio}</td>
											<td>${item.trabajador}</td>
											<td>${item.nombreNino}</td>
											<td>${item.montoMensualPagTrab}</td>
											<td>${item.concepto}</td>
											<td>${item.montoTotalStr}</td>
											<td>${item.codigo}</td>
											<td><input type="checkbox" name="numeroDesactivar"
												value="${item.nuSolicitud}" /></td>

										</tr>
									</c:forEach>
									<input type="hidden" name="lista" value="${lista}" />
									<input type="hidden" name="coRepStatus" value="${coRepStatus}" />

								</tbody>
							</table>
							</div>






							</div>


						</form>


					</table>
				</td>
			</tr>
			<tr>
				<td colspan="2" id="alto2"></td>
			</tr>
			<tr>
				<td colspan="2"><jsp:include page="copyright.html" flush="true" /></td>
			</tr>
		</table>

	</div>
</body>
</html>


