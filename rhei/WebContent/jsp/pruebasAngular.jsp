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
<html ng-app="exampleApp">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title><fmt:message key="benef.pricipal.variables"	bundle="${lang}" /></title>
<link rel="stylesheet" type="text/css" href="/rhei/css/menu.css" />
<link rel="stylesheet" type="text/css" href="/rhei/css/estilos.css" />

<link rel="stylesheet" type="text/css" href="/rhei/css/bootstrap.css" />
<link rel="stylesheet" type="text/css" href="/rhei/css/bootstrap-theme.css" />
<script type="text/javascript" src="/rhei/js/angular.js"></script>

<!--[if lt IE 8]>
   <style type="text/css">
   li a {display:inline-block;}
   li a {display:block;}
   </style>
   <![endif]-->
<script type="text/javascript" src="/rhei/js/jquery-1.6.1.min.js"></script>
<script type="text/javascript" src="/rhei/js/funciones.js"></script>
<script type="text/javascript"
	src="/rhei/js/jquery-ui-1.11.3.custom/jquery-ui.js"></script>
<script type="text/javascript"
	src="/rhei/js/jquery-ui-1.11.3.custom/configCal.js"></script>
<link rel="stylesheet"
	href="/rhei/js/jquery-ui-1.11.3.custom/jquery-ui.css">
<script type="text/javascript" language="JavaScript">
	window.history.forward(1);
	
	$(function($) {
		$("#fechaInicio").datepicker();
		$("#fechaFin").datepicker();
	});
</script>

    <script>
        angular.module("exampleApp", [])
            .controller("simpleCtrl", function ($scope) {

                $scope.cities = ["London", "New York", "Paris"];

                $scope.city = "London";

                $scope.getCountry = function (city) {
                    switch (city) {
                        case "London":
                            return "UK";
                        case "New York":
                            return "USA";
                    }
                }
            });
    </script>
</head>
<body ng-controller="simpleCtrl">
	<jsp:include page="encabezado.jsp" flush="true" />
	<div class="well">
        <label>Select a City:</label>
        <select ng-options="city for city in cities" ng-model="city">
        </select>
    </div>

    <div class="well">
        <p>The city is: {{city}}</p>
        <p>The country is: {{getCountry(city) || "Unknown"}}</p>
    </div>
	<div class="centraTabla">
		<table cellspacing="0" cellpadding="0">
			<tr>
				<td valign="top"><div class="izquierda"><jsp:include
							page="menu.jsp" flush="true" /></div></td>
				<td>
					<table cellspacing="0" cellpadding="0" class="anchoTabla5">
						<tr>
							<td colspan="1" class="titulo">
							<fmt:message key="benef.titulo"	bundle="${lang}" />
						 </td>
						</tr>


						<c:if test="${!empty viene2}">
							<c:if test="${viene2== 'viene2'}">
								<c:if test="${mensaje=='Exito'}">
									<tr>
										<td colspan=1 class=avisoExito>	
										<fmt:message key="exito"	bundle="${lang}" />
									</tr>
								</c:if>
								<c:if test="${mensaje=='Fracaso'}">
									<tr>
										<td colspan=1 class=aviso>
										<fmt:message key="periodo.fracaso.add"	bundle="${lang}" />
											</td>
									</tr>
								</c:if>
								<c:if test="${mensaje=='Error'}">
									<tr>
										<td colspan=1 class=aviso>
											<fmt:message key="error"	bundle="${lang}" />
										 </td>
									</tr>
								</c:if>
								<c:if test="${mensaje=='Constraint'}">
									<tr>
										<td colspan=1 class=aviso>${infoError}</td>
									</tr>
								</c:if>
							</c:if>
						</c:if>
						<tr>
							<td colspan="1">
								<h2><fmt:message key="periodo.agregar"	bundle="${lang}" /> TEST ANGULAR</h2>

							</td>
						</tr>

						<tr>
							<td>
								<div class="derecha">
									<form name="formPeriodo" method="post" action="">
										<table cellspacing="2" cellpadding="2" class="anchoTabla3">
											<tr>
												<td><label><fmt:message key="codigo"	bundle="${lang}" />:</label></td>
												<td><input type="text" name="codigoPeriodo"
													class="entradaInactiva texto_der" value="" tabindex="1"
													maxlength="9" size="9"
													onkeypress="if (event.keyCode == 13) event.returnValue = false;"
													disabled /></td>
												<td><label><fmt:message key="periodoEscolar"	bundle="${lang}" />:</label></td>
												<td colspan="3"><input type="text"
													name="descripcionPeriodo" class="entrada" value=""
													tabindex="1" maxlength="9" size="9"
													onkeypress="if (event.keyCode == 13) event.returnValue = false;" />
												</td>
											</tr>
											<tr>
												<td><label><fmt:message key="fechaInicio"	bundle="${lang}" />:</label></td>
												<td colspan="1">
												<input name="fechaInicio" class="entrada"
														value=""
														onkeyup="mascara(this,'-','fecha',true)" tabindex="28"
														id="fechaInicio" maxlength="10" size="10" type="text">
														<input type='image' src='/rhei/imagenes/calendar.ico' onClick="clickfechaInicio();return false;" />
												</td>
												<td><label><fmt:message key="fechaFin"	bundle="${lang}" />:</label></td>
												<td colspan="3">
												
												<input name="fechaFin" class="entrada"
														value=""
														onkeyup="mascara(this,'-','fecha',true)" tabindex="28"
														id="fechaFin" maxlength="10" size="10" type="text">
														<input type='image' src='/rhei/imagenes/calendar.ico' onClick="clickfechaFin();return false;" />
											 
												</td>
											</tr>
											<tr>
												<td><label><fmt:message key="estado"	bundle="${lang}" />:</label></td>
												<td colspan="5"><select name="estado" class="entrada"
													size="1" tabindex="4">
														<option value="A"><fmt:message key="activo"	bundle="${lang}" /></option>
														<option value="I"><fmt:message key="inactivo"	bundle="${lang}" /></option>
												</select></td>
											</tr>
											<tr>
												<td colspan="6" id="centrado"><input type="button"
													class="boton_color2" name="crear" value="<fmt:message key="crear"	bundle="${lang}" />"
													tabindex="5" id="5"
													onclick="addPeriodoScolar(document.formPeriodo)" />&nbsp;<input
													type="hidden" name="operacion" value="crear" />&nbsp;<input
													type="hidden" name="tabla" value="periodoEscolar" /></td>
											</tr>
										</table>
									</form>
								</div>
							</td>
						</tr>

					</table>
				</td>
			</tr>
			<tr>
				<td colspan="2" id="alto2"></td>
			</tr>
			<tr>
				<td colspan="2"><jsp:include page="copyright.html" flush="true" />
				</td>
			</tr>
		</table>
	</div>
</body>
</html>