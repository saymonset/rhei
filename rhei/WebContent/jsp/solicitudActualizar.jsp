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
	onload="showConyuge(); clickGstsolMenu();  actualizaReloj();  formato_camposMontosSolicitud();">
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

						<c:if test="${not empty showResultToView.mensaje}">

							<c:if test="${showResultToView.mensaje=='exito'}">
								<tr>
									<td colspan=1 class=avisoExito><fmt:message
											key="solicitud.actualizar.exito" bundle="${lang}" />
								</tr>
							</c:if>
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
											key="solicitud.consult.empleado_sin_solicitud" bundle="${lang}" />
										</td>
								</tr>
							</c:if>
							
							
							<c:if test="${showResultToView.mensaje=='solicitud.falta.colegio'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="solicitud.falta.colegio" bundle="${lang}" /></td>
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
							<c:if test="${showResultToView.mensaje=='fracaso2'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="solicitud.actualizar.fracaso2" bundle="${lang}" /></td>
								</tr>
							</c:if>


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
									<fmt:message key="solicitud.actualizar" bundle="${lang}" />
								</h2>
							</td>
						</tr>


						<!-- BUSCAR INICIO -->

						<form id="form1" name="formBuscarDatos" method="post"
							action="/rhei/solicitudControladorActualizar?principal.do=actualizarInfoDataTrabBenef">

							<%@ include file="solicIngrBlke1WithNroSol.jsp"%>

							<input type="hidden" name="nroRifCentroEdu"
								value="${showResultToView.nroRifCentroEdu}"></input> <input
								type="hidden" name="accion" value="solicitudActualizar.jsp" />
						</form>




						<!-- BUSCAR FIN-->

						<c:if
							test="${!empty showResultToView.viene && showResultToView.viene==1}">


							<tr>
								<td><%@ include file="reporteNumSolicitud.jsp"%>
								</td>
							</tr>

							<tr>
								<td>
									<form id="form2" name="formDatosSolicitud" method="post"
										action="/rhei/solicitudControladorActualizar?principal.do=actualizarSolicitud">
										<table cellspacing="5" cellpadding="2" class="anchoTabla4">
											<tr>
												<td colspan="4" class="alinear_der"></td>
											</tr>
											<%@ include file="solicitudTrabBenefBlke2.jsp"%>


											<%@ include file="solicitudDatoSaveBlke3.jsp"%>

											<tr>
												<td><span id="messagewait" style="display: none;">

														<div style="text-align: center;">
															<img class="bannerLogin" border="0" src="imagenes/ai.gif"
																alt="" />
															<fmt:message key="wait.hold_on" bundle="${lang}" />
														</div>
												</span></td>
											</tr>
											<tr>
												<td colspan="4" align="center"><span id="btnPrincipal"
													style="display: block;"> <input type="button"
														class="boton_color2" name="registrarSolicitud"
														value="Guardar Cambios" tabindex="56" id="56"
														onclick="guardarCambios()" />
												</span></td>
											</tr>

											<input type="hidden" name="accion"
												value="solicitudActualizar.jsp" />
											<input type="hidden" name="periodoE"
												value="${showResultToView.periodoEscolar}" />
											<input type="hidden" name="codigoPeriodo"
												value="${showResultToView.codigoPeriodo}" />
											<input type="hidden" name="codigoPeriodoE"
												value="${showResultToView.codigoPeriodo}" />
											<input type="hidden" name="periodoEV"
												value="${showResultToView.formatoPeriodoVigente}" />
											<input type="hidden" name="codigoPeriodoEV"
												value="${showResultToView.codigoPeriodoVigente}" />

											<tr>
												<td colspan="4" align="center"><input type="hidden"
													name="nivelEscolaridadH"
													value="${showResultToView.nivelEscolaridad}" /> <input
													type="hidden" name="cedula"
													value="${showResultToView.cedula}" /> <input type="hidden"
													name="codigoBenef" value="${showResultToView.codigoBenef}" />
													<input type="hidden" name="codEmp"
													value="${showResultToView.codEmp}" /> <input type="hidden"
													name="cedBenef" value="${showResultToView.cedulaFamiliar}" />
													<input type="hidden" name="cedEmp"
													value="${showResultToView.cedula}" /> <input type="hidden"
													name="nroRifCentroEdu"
													value="${showResultToView.nroRifCentroEdu}" /> <input
													type="hidden" name="numSolicitud"
													value="${showResultToView.numSolicitud}" /> <input
													type="hidden" name="co_status"
													value="${showResultToView.co_status}" /> <input
													type="hidden" name="nivelEscolar"
													value="${showResultToView.nivelEscolar}" /></td>
											</tr>



										</table>
									</form>
								</td>
							</tr>


							<tr>
								<td>
							

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
				<td colspan="2"><jsp:include page="copyright.html" flush="true" />
				</td>
			</tr>
		</table>
	</div>
</body>
</html>