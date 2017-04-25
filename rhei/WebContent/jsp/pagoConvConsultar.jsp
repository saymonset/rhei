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

        myApp.controller("ToDoCtrl", function ($scope) {
        });

        

        myApp.directive("highlight", function () {
            return function (scope, element, attrs) {
                if (attrs["highlight"]<0) {
                    element.css("color", "red");
                } 
            }
        });
        
        myApp.directive("pago", function () {
           return function (scope, element, attrs) {
                if (attrs["pago"]==1) {
                    element.css("color", "green");
                } 
            }
        });
         
    </script>

</head>
<body ng-controller="ToDoCtrl"
	onload="showConyuge(); actualizaReloj();clickGstPagoRegistrMenu();  formato_camposMontosPagos();  ">

	<jsp:include page="encabezado.jsp" flush="true" />
	<div class="centraTabla" ng-controller="ToDoCtrl">
		<table cellspacing="0" cellpadding="0">
			<tr>
				<td valign="top"><div class="izquierda"><jsp:include
							page="menu.jsp" flush="true" /></div></td>
				<td>
					<table cellspacing="0" cellpadding="0" class="anchoTabla4">
						<tr>
							<td colspan="1" class="titulo"><fmt:message
									key="benef.titulo" bundle="${lang}" /></td>
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
						<tr>
							<td colspan="1">
								<h2 unorderedList='hay papa'>
									<fmt:message key="pagos.convencional.consultar"
										bundle="${lang}" />  
								</h2>
							 
							</td>
						</tr>

						<form id="form1" name="formBuscarDatos" method="post"
							action="/rhei/pagosControladorConsultar?principal.do=buscarDataPago">

							<%@ include file="solicIngrBlke1WithNroFactura.jsp"%>

							<input type="hidden" name="accion" value="pagoConvConsultar.jsp" />

							<input type="hidden" name="tipoPago" value="1" tabindex="52"
								id="52" />
								
							<input type="hidden" name="nroSolicitud"						value="${showResultToView.nroSolicitud}" />	
                            <input type="hidden"
								name="vieneFromReporteByPagoAndTributo" value="1" /> <input
								type="hidden" name="definitivoTransitorioHidden" value="" /> <input
								type="hidden" name="keyReport" value="1" />

						</form>
 
						<c:if
							test="${!empty showResultToView.viene && showResultToView.viene==1}">

							<tr>
								<td>
								 
									<%-- 								<%@ include file="reportePagoByFacturaNumSolicitud.jsp"%> --%>
								</td>
							</tr>

							<tr>
								<td>
									<form id="form2" name="formDatosSolicitud" method="post"
										action="/rhei/pagosControladorConsultar?principal.do=buscarDataPago">
										<table cellspacing="5" cellpadding="2" class="anchoTabla4">

											<c:if test="${empty showResultToView.mensaje}">

												<%@ include file="solicitudTrabBenefBlke2.jsp"%>

												<%@ include file="solicitudDatoSaveBlke3.jsp"%>




												<tr>
													<td colspan="4" id="centrado">
														<div class="panel panel-primary">
															<h3 class="panel-heading">
																<fmt:message key="facturas" bundle="${lang}" />
															</h3>
															<%@ include file="tableView.jsp"%>
														</div>

													</td>
												</tr>

												<tr>
													<td colspan="4" id="centrado"><input type="hidden"
														name="accion" value="pagoConvRegistrar.jsp" /> <input
														name="estatusPago" value="T" tabindex="45" id="45"
														type="hidden"> <input type="hidden" name="nroRif"
														value="${showResultToView.nroRifCentroEdu}" /> <input
														name="mo_periodo" value="${showResultToView.montoPeriodo}"
														tabindex="47" id="47" type="hidden"> <input
														name="mo_matricula"
														value="${showResultToView.montoMatricula}" tabindex="48"
														id="48" type="hidden"> <input
														name="beneficioCompartido"
														value="${showResultToView.benefCompartido}" tabindex="49"
														id="49" type="hidden"> <input name="codigoPeriodo"
														value="${showResultToView.codigoPeriodo}" tabindex="50"
														id="50" type="hidden"></td>
												</tr>
												<tr>
													<td colspan="4" align="center"><input type="hidden"
														name="cedula" value="${showResultToView.cedula}" /> <input
														type="hidden" name="codigoBenef"
														value="${showResultToView.codigoBenef}" /> <input
														type="hidden" name="codEmp"
														value="${showResultToView.codEmp}" /> <input
														type="hidden" name="cedBenef"
														value="${showResultToView.cedulaFamiliar}" /> <input
														type="hidden" name="cedEmp"
														value="${showResultToView.cedula}" /> <input
														type="hidden" name="co_status"
														value="${showResultToView.co_status}" /> <input
														type="hidden" name="nivelEscolar"
														value="${showResultToView.nivelEscolar}" /> <input
														name="mo_bcv" value="${showResultToView.montoBCV}"
														tabindex="51" id="51" type="hidden"> <input
														name="tipoPago" value="1" tabindex="52" id="52"
														type="hidden"></td>
													</td>
												</tr>
												<tr>
													<td colspan="4" id="centrado">
												</tr>

											</c:if>





										</table>
									</form>
								</td>
							</tr>
						</c:if>
						<c:if
							test="${!empty showResultToView.viene && showResultToView.viene==1}">
							<form id="formBuscarDatosIndividual"
								name="formBuscarDatosIndividual" method="post"
								action="/rhei/reporteConsultaIndividualControlador?principal.do=outReport">

<!--                                   SE SACA LOS REPORTS POR ACA -->
								<%@ include file="reporteConsultaByCedula.jsp"%>

								<input type="hidden" name="accion" value="pagoConvConsultar.jsp" />

							</form>
						</c:if>
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