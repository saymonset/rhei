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
<script type="text/javascript" src="/rhei/js/funciones.js"></script>
<script type="text/javascript" language="JavaScript">
	window.history.forward(1);
</script>
</head>
<body
	onload="showConyuge(); clickGstsolMenu(); formato_camposMontosSolicitud(); document.formDatosSolicitud.tramite[0].checked = true; actualizaReloj(); 	">

	<jsp:include page="encabezado.jsp" flush="true" />
	<div class="centraTabla">
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
							<c:if test="${incluir=='exito'}">
								<tr>
									<td colspan=1 class=avisoExito><fmt:message
											key="solicitud.exito" bundle="${lang}" />
								</tr>
							</c:if>

					 

						<c:if test="${not empty showResultToView.mensaje}">
						
					

							<c:if test="${showResultToView.mensaje=='solicitud.ced.fail'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="solicitud.ced.fail" bundle="${lang}" />
										${showResultToView.cedula}</td>
								</tr>
							</c:if>
							
							<c:if test="${showResultToView.mensaje=='beneficiario_no_hay'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="solicitud.consult.fracaso" bundle="${lang}" />
										</td>
								</tr>
							</c:if>
							

							<c:if test="${showResultToView.mensaje=='fracaso'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="solicitud.consult.fracaso" bundle="${lang}" /></td>
								</tr>
							</c:if>
							<c:if test="${showResultToView.mensaje=='fracaso1'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="solicitud.consult.fracaso1" bundle="${lang}" /> <c:if
											test="${showResultToView.nroSolicitud!=0}">
											<fmt:message key="solicitud.existe" bundle="${lang}" />${showResultToView.nroSolicitud}
											</c:if></td>
								</tr>
							</c:if>
<!-- 						agregado por david velasquez -->
							<c:if test="${showResultToView.mensaje=='empleado_sin_solicitud'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="solicitud.consult.empleado_sin_solicitud" bundle="${lang}" /> <c:if
											test="${showResultToView.nroSolicitud!=0}">
											<fmt:message key="solicitud.no.existe" bundle="${lang}" />${showResultToView.nroSolicitud}
											</c:if></td>
								</tr>
							</c:if>
<!-- 							hasta aqui lo agregado por david velasquez -->
							<c:if test="${showResultToView.mensaje=='error'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="solicitud.error" bundle="${lang}" /></td>
								</tr>
							</c:if>
						</c:if>
						<tr>
							<td colspan="1">
								<h2>
									<fmt:message key="solicitud.consultar" bundle="${lang}" />
								</h2>
							</td>
						</tr>
						
							

						<!-- BUSCAR INICIO -->

						<form id="formxx1" name="formBuscarDatos" method="post"
							action="/rhei/solicitudControladorConsultar?principal.do=consultInfoDataTrabBenef">
							
							<%@ include file="solicIngrBlke1WithNroSol.jsp"%>
							
							<input type="hidden" name="nroRifCentroEdu"
								value="${showResultToView.nroRifCentroEdu}">
								</input> <input
								type="hidden" name="accion" value="solicitudConsultar.jsp" />
						</form>

						<!-- BUSCAR FIN-->

						
						<c:if
							test="${!empty showResultToView.viene && showResultToView.viene==1}">
							
							<table cellspacing="5" cellpadding="2" class="anchoTabla4">

							<div class="row">
								<div class="col-md-7">

									<!-- 									REPORTE -->
									<%@ include file="reporteNumSolicitud.jsp"%>
								</div>

								<div class="col-md-3">
									<!-- 									REPORTE -->
									<%@ include file="reporteFormato.jsp"%>
								</div>
							 
								</table>
								<form id="formxx2" name="formDatosSolicitud" method="post"
									action="">



									<table cellspacing="5" cellpadding="2" class="anchoTabla4">



										<%@ include file="solicitudTrabBenefBlke2.jsp"%>

										<%@ include file="solicitudDatoSaveBlke3.jsp"%>




										<tr>
											<td colspan="4" align="center"><span id="btnPrincipal"
												style="display: block;"> </span></td>
										</tr>


									</table>

								</form>

							</div>
							</td>
							</tr>
							
							
							<c:if test="${empty showResultToView.mensaje}">
								<tr>
									<td>
									

									</td>

								</tr>
							</c:if>
						</c:if>
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