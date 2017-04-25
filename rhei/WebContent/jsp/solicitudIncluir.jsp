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
	onload="actualizaReloj();  clickGstsolMenu();   formato_camposMontosSolicitud(); ">
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

							<c:if test="${showResultToView.mensaje=='failDocAlfresco'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="failDocAlfresco" bundle="${lang}" /></br>
										<table>

											<c:forEach var="missing" items="${recaudosMissing}">
												<tr>
													<td colspan=1 class=aviso>${missing.descripcion}</td>
												</tr>
											</c:forEach>
										</table></td>
								</tr>
							</c:if>



							<c:if test="${showResultToView.mensaje=='failVigenteContrato'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="failVigenteContrato" bundle="${lang}" />
										${showResultToView.cedula}</td>
								</tr>
							</c:if>

							<c:if test="${showResultToView.mensaje=='beneficiario_no_hay'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="benef.beneficiarios_no_hay" bundle="${lang}" />
										</td>
								</tr>
							</c:if>

							<c:if test="${showResultToView.mensaje=='solicitud.ced.fail'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="solicitud.ced.fail" bundle="${lang}" />
										${showResultToView.cedula}</td>
								</tr>
							</c:if>
							<c:if
								test="${showResultToView.mensaje=='solicitud.ced.fail.activo_empleado'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="solicitud.ced.fail.activo_empleado" bundle="${lang}" />
										${showResultToView.cedula}</td>
								</tr>
							</c:if>
							<c:if
								test="${showResultToView.mensaje=='solicitud.ced.fail.vacacion_empleado'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="solicitud.ced.fail.vacacion_empleado" bundle="${lang}" />
										${showResultToView.cedula}</td>
								</tr>
							</c:if>

							<c:if
								test="${showResultToView.mensaje=='solicitud.ced.fail.egresado_empleado'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="solicitud.ced.fail.egresado_empleado" bundle="${lang}" />
										${showResultToView.cedula}</td>
								</tr>
							</c:if>
							<c:if
								test="${showResultToView.mensaje=='solicitud.ced.fail.suspendido_empleado'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="solicitud.ced.fail.suspendido_empleado" bundle="${lang}" />
										${showResultToView.cedula}</td>
								</tr>
							</c:if>
							<c:if test="${showResultToView.mensaje=='exito'}">
								<tr>
									<td colspan=1 class=avisoExito><fmt:message
											key="solicitud.exito" bundle="${lang}" />
								</tr>
							</c:if>
							<c:if test="${showResultToView.mensaje=='fracaso'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="solicitud.fracaso" bundle="${lang}" /> <fmt:message
											key="solicitud.numero" bundle="${lang}" />:${showResultToView.nroSolicitud}
									</td>
								</tr>
							</c:if>
							<c:if test="${showResultToView.mensaje=='fracasoeduc'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="solicitud.fracasoeduc" bundle="${lang}" /></td>
								</tr>
							</c:if>
							<c:if test="${showResultToView.mensaje=='error'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="solicitud.error" bundle="${lang}" /></td>
								</tr>
							</c:if>
							<c:if test="${showResultToView.mensaje=='errorPeriodoScolar'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="solicitud.errorPeriodoScolar" bundle="${lang}" /></td>
								</tr>
							</c:if>
						</c:if>
						<tr>
							<td colspan="1">
								<h2>
									<fmt:message key="solicitud.incluir" bundle="${lang}" />
								</h2>
							</td>
						</tr>
						<form id="formBuscarDatos" name="formBuscarDatos" method="post"
							action="/rhei/solicitudControladorIncluir?principal.do=incluirInfoDataTrabBenef">

							<%@ include file="solicitudIngrBlke1.jsp"%>

							<input type="hidden" name="nroRifCentroEdu"
								value="${showResultToView.nroRifCentroEdu}"></input> <input
								type="hidden" name="accion" value="solicitudIncluir.jsp" />
						</form>




						<c:if
							test="${!empty showResultToView.viene && showResultToView.viene==1}">


							<tr>
								<td>

									<form id="formDatosSolicitud" name="formDatosSolicitud"
										method="post" action="">
										<table cellspacing="5" cellpadding="2" class="anchoTabla4">
											<c:if test="${empty showResultToView.mensaje}">


												<%@ include file="solicitudTrabBenefBlke2.jsp"%>

												<%@ include file="solicitudDatoSaveBlke3.jsp"%>

												<tr>
													<td><span id="messagewait" style="display: none;">

															<div style="text-align: center;">
																<img class="bannerLogin" border="0"
																	src="imagenes/ai.gif" alt="" />
																<fmt:message key="wait.hold_on" bundle="${lang}" />
															</div>
													</span></td>
												</tr>

												<tr>
													<td colspan="4" align="center"><span id="btnPrincipal"
														style="display: block;"> <input type="button"
															id="btnIncluir" class="boton_color"
															name="registrarSolicitud"
															value="<fmt:message key="incluir" bundle="${lang}" />  "
															tabindex="55" id="55" onclick="guardarSolicitud()" />
													</span></td>
												</tr>

												<input type="hidden" name="tipoEmpresaHidden"
													value="${showResultToView.tipoEmpresa}" tabindex="50"
													id="50" maxlength="10" size="10" />
												<input type="hidden" name="cedula"
													value="${showResultToView.cedula}" />
												<input type="hidden" name="montoAporteEmpHidden"
													value="${showResultToView.montoAporteEmp}" tabindex="50"
													id="50" maxlength="10" size="10" /> &nbsp;<input
													type="hidden" name="codigoBenef"
													value="${showResultToView.codigoBenef}" tabindex="50"
													id="50" maxlength="10" size="10" /> &nbsp;<input
													type="hidden" name="fechaActual"
													value="${showResultToView.fechaActual}" tabindex="50"
													id="50" maxlength="10" size="10" /> &nbsp;<input
													type="hidden" name="codEmp"
													value="${showResultToView.codEmp}" tabindex="51" id="51"
													maxlength="10" size="10" /> &nbsp;<input type="hidden"
													name="nroRifCentroEdu"
													value="${showResultToView.nroRifCentroEdu}" tabindex="52"
													id="52" maxlength="14" size="14" /> &nbsp;<input
													type="hidden" name="cedBenef"
													value="${showResultToView.cedulaFamiliar}" tabindex="53"
													id="53" maxlength="10" size="10" /> &nbsp;<input
													type="hidden" name="cedEmp"
													value="${showResultToView.cedula}" tabindex="54" id="54"
													maxlength="10" size="10" /> &nbsp;<input type="hidden"
													name="codigoPeriodo"
													value="${showResultToView.codigoPeriodo}" tabindex="55"
													id="55" /> &nbsp;<input type="hidden" name="tipoNomina"
													value="${showResultToView.tipoNomina}" tabindex="56"
													id="56" /> &nbsp;<input type="hidden" name="accion"
													value="solicitudIncluir.jsp" />
											</c:if>
										</table>
									</form>
								</td>
							</tr>
							<c:if test="${empty showResultToView.mensaje}">
								<tr>
									<td></td>

								</tr>
							</c:if>
						</c:if>

						<!--                        EL USUARIO LO LLEVA A LA OFICINA DE BEENEFICIO DE EDUCACION INIIAL -->
						<c:if test="${showResultToView.mensaje=='exito'}">
							<tr>
								<td><%@ include file="reporteNumSolicitud.jsp"%>
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