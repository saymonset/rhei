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
<title>Principal Variable</title>

		<script type="text/javascript" src="/rhei/js/angular.js"></script>
<script type="text/javascript" src="/rhei/js/angular-resource.js"></script>


<link rel="stylesheet" type="text/css" href="/rhei/css/bootstrap.css" />
<link rel="stylesheet" type="text/css"	href="/rhei/css/bootstrap-theme.css" />

<link rel="stylesheet" type="text/css" href="/rhei/css/menu.css" />
<link rel="stylesheet" type="text/css" href="/rhei/css/estilos.css" />
<!--[if lt IE 8]>
   <style type="text/css">
   li a {display:inline-block;}
   li a {display:block;}
   </style>
   <![endif]-->
<script type="text/javascript" src="/rhei/js/jquery-1.6.1.min.js"></script>
<!-- REMOVE THIS if you're using jQuery UI. -->
<script type="text/javascript" src="/rhei/js/jquery.ui.widget.js"></script>
<script type="text/javascript" src="/rhei/js/jquery-picklist.js"></script>
<script type="text/javascript" src="/rhei/js/funciones.js"></script>
<script type="text/javascript" src="/rhei/js/searchReporteAjax.js"></script>
<script type="text/javascript"
	src="/rhei/js/jquery-ui-1.11.3.custom/jquery-ui.js"></script>
<script type="text/javascript"
	src="/rhei/js/jquery-ui-1.11.3.custom/configCal.js"></script>
<link rel="stylesheet"
	href="/rhei/js/jquery-ui-1.11.3.custom/jquery-ui.css">
		<link type="text/css" href="/rhei/css/jquery-picklist.css" rel="stylesheet" />
<script type="text/javascript" language="JavaScript">
	window.history.forward(1);

	$(function($) {
		
		$("#advanced").pickList(
			{
				sourceListLabel:	"Seleccione",
				targetListLabel:	"Seleccionado",
				addAllLabel:		"Agregar Todos",
				addLabel:			"Agregar",
				removeAllLabel:		"Remover Todos",
				removeLabel:		"Remover",
				sortAttribute:		"value",
				items:
				[
					{
						value: -1,
						label: "",
						selected: false
					}
				]
			});

			// Example of adding a regular item after picklist creation.
			// Note there is no "element" property as that's for rich content only.
			$("#advanced").pickList("insert",
			{
				value: -1,
				label: "",
				selected: true
			});
		
			 
		
	});
</script>
 
	<script>
        var todoApp = angular.module("todoApp", []);
    </script>
	<script type="text/javascript" src="/rhei/js/controllers/todoControllers.js"></script>
	
	
</head>
<body ng-controller="ToDoCtrl"
	onload="clickReporteMenu(); actualizaReloj(); ">

	<jsp:include page="encabezado.jsp" flush="true" />
	
	   
    
	<div class="centraTabla">
		<table cellspacing="0" cellpadding="0">
			<tr>
				<td valign="top"><div class="izquierda"><jsp:include
							page="menu.jsp" flush="true" /></div></td>
				<td>
					<table cellspacing="0" cellpadding="0" class="anchoTabla8">
						<tr>
							<td colspan="1" class="titulo"><fmt:message
									key="benef.titulo" bundle="${lang}" /></td>
						</tr>
						<c:if test="${not empty showResultToView.mensaje}">

							<c:if test="${showResultToView.mensaje=='exito'}">
								<tr>
									<td colspan=1 class=avisoExito><fmt:message
											key="pagos.actualizar.exito" bundle="${lang}" />
								</tr>
							</c:if>

							<c:if test="${showResultToView.mensaje=='fracaso3'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="pagos.convencional.fracaso3" bundle="${lang}" /></td>
								</tr>
							</c:if>

							<c:if test="${showResultToView.mensaje=='fracaso4'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="pago.error.no.data" bundle="${lang}" /></td>
								</tr>
							</c:if>
						</c:if>

						<tr>
							<td colspan="1">
								<h2>
								 
									<fmt:message key="reporte.reportConsulta.individual"	bundle="${lang}" />
							 
								</h2>
							</td>
						</tr>
						<form id="formBuscarDatos" novalidate name="formBuscarDatos" method="post"
							action="/rhei/reporteConsultaIndividualControlador?principal.do=outReport">

							<%@ include file="reporteConsultaIndividual.jsp"%>

			         		 <input type="hidden" name="accion" value="reporteConsultaIndividual0.jsp" />

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