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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
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

</head>
<body
	onload="showConyuge(); actualizaReloj(); clickGstPagoRegistrMenu(); formato_camposMontosPagos(); ">

	<jsp:include page="encabezado.jsp" flush="true" />
	<div class="centraTabla">
		<table cellspacing="0" cellpadding="0">
			<tr>
				<td valign="top"><div class="izquierda"><jsp:include
							page="menu.jsp" flush="true" /></div></td>
				<td>
					<table cellspacing="0" cellpadding="0" class="anchoTabla7">
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
									<fmt:message key="actualizar" bundle="${lang}" />
									<fmt:message key="pagos.no.convencionales" bundle="${lang}" />
								</h2>
							</td>
						</tr>
						<form id="form1" name="formBuscarDatos" method="post"
							action="/rhei/pagosControladorActualizar?principal.do=buscarDatActualizar">


							<%@ include file="solicIngrBlke1WithNroFactura.jsp"%>

							<input type="hidden" name="accion" value="pagoActualizar.jsp" />

							<input type="hidden" name="tipoPago" value="1" tabindex="52"
								id="52" />

						</form>


						<c:if
							test="${!empty showResultToView.viene && showResultToView.viene==1}">

							<tr>
<%-- 								<%@ include file="reportePagoByFacturaNumSolicitud.jsp"%> --%>
								<td></td>
							</tr>
							<tr>
								<td>
									<form id="formDatosSolicitud" name="formDatosSolicitud" method="post"
										action="/rhei/pagosControladorActualizar?principal.do=actualizar">
										<table cellspacing="5" cellpadding="2" class="anchoTabla4">

											<c:if test="${empty showResultToView.mensaje}">

												<%@ include file="solicitudTrabBenefBlke2.jsp"%>

												<%@ include file="solicitudDatoSaveBlke3.jsp"%>

<%-- 												<%@ include file="pagos.jsp"%> --%>

<tr>
													<td colspan="4" id="centrado">
														<div class="panel panel-primary">
															<h3 class="panel-heading"><fmt:message
											key="facturas" bundle="${lang}" /></h3>
															<%@ include file="tableView.jsp"%>
														</div>

													</td>
												</tr>


												<tr>
													<td colspan="4" id="centrado"><c:if
															test="${!empty showResultToView.nroFactura && !empty showResultToView.nroControl}">
<!-- 															<input type="button" class="boton_color2" -->
<!-- 																name="actualizarSolicitud" value="Actualizar Pagos" -->
<!-- 																tabindex="56" id="56" -->
<!-- 																onclick="validarActualizarPagoCtrl()" /> -->
														</c:if></td>
												</tr>

												<tr>
													<td colspan="4" id="centrado">
													
													<input name="keyNuSolicitudNuFactNuRefPagoInComplemento" type="hidden"/>
													<input name="mes"
														value="${showResultToView.mes}" tabindex="45" id="45"
														type="hidden"> <input
														name="nuIdFactura" type="hidden"
														value="${showResultToView.nuIdFactura}"></input> <input
														name="receptorPago" type="hidden"
														value="${showResultToView.receptorPago}"></input> <input
														name="periodoEscolarAux"
														value="${showResultToView.periodoEscolar}" type="hidden"></td>
												</tr>
												<tr>
													<td colspan="4" align="center"></td>
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