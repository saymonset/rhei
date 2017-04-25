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
<!DOCTYPE HTML>
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
<script type="text/javascript" src="/rhei/js/searchColegiosAjax.js"></script>

<script type="text/javascript" language="JavaScript">
	window.history.forward(1);
</script>
</head>
<body
	onload="actualizaReloj();  clickColeg();  formatearCampo(document.formDatosSolicitud.montoBCV);formato_camposMontos(); ">
	<jsp:include page="encabezado.jsp" flush="true" />
	<div class="centraTabla">
		<table cellspacing="0" cellpadding="0">
			<tr>
				<td valign="top"><div class="izquierda"><jsp:include
							page="menu.jsp" flush="true" /></div></td>
				<td>


					<table cellspacing="0" cellpadding="0" class="anchoTabla5">
						<tr>
							<td colspan="1" class="titulo"><fmt:message
									key="benef.titulo" bundle="${lang}" /></td>
						</tr>
						<c:if test="${not empty showResultToView.mensaje}">

							<c:if test="${showResultToView.mensaje=='exito'}">
								<tr>
									<td colspan=1 class=avisoExito><fmt:message
											key="solicitud.exito" bundle="${lang}" />
								</tr>
							</c:if>
							<c:if test="${showResultToView.mensaje=='fracaso'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="solicitud.consult.fracaso" bundle="${lang}" /></td>
								</tr>
							</c:if>
							
							<c:if test="${showResultToView.mensaje=='solicitud.falta.colegio'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="solicitud.falta.colegio" bundle="${lang}" /></td>
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
									<fmt:message key="${titleBuscar}" bundle="${lang}" />
								</h2>
								<h2>
									<fmt:message key="int.buscar" bundle="${lang}" />
								</h2>

							</td>
						</tr>


						<tr>
							<td>
								<div class="derecha">
									<form id="form2" name="formDatosSolicitud" method="post"
										action="${showResultToView.pagIrAfter}">
										<span id="benefwait" style="display: none;">
											<div style="text-align: center;">
												<img class="bannerLogin" border="0" src="imagenes/ai.gif"
													alt="" />
												<fmt:message key="wait.hold_on" bundle="${lang}" />
											</div>
										</span>
										<table cellspacing="5" cellpadding="2"
											class="formato10 anchoTabla4">



											<tr id="tr_NroRifCentroEduc">
												<td><label id="nroRifCentroEduTxt"><fmt:message
															key="inst.rif" bundle="${lang}" /></label></td>
												<!-- 															llenamos el select via ajax serachColegiosAjax.js, servlet InstitucionAjax.java -->
												<td>&nbsp; <select name="nroRifCentroEdu"
													onchange="intitucionSend();" id="nroRifCentroEdu"
													style="width: 400px" class="entrada" size="1" tabindex="">

														<option value="0">
															<fmt:message key="select.seleccione" bundle="${lang}" />
														</option>

												</select>


												</td>
												<td colspan="3"></td>
											</tr>
											<tr>
												<td><label><fmt:message
															key="inst.filtrar.nombre" bundle="${lang}" /></label></td>
												<td>
													<!--El filtrarNameCheck esta reflejado en el archivo SearchColegiosAjax.js para filtrar el select 
                                                $( "#nombre" ).keypress(function-->
													<span id="filtrarName1" style="display: block;"> <!--El nombre esta reflejado en el archivo SearchColegiosAjax.js para filtrar el select 
                                                $( "#nombre" ).keypress(function-->
														<input type="text" class="entrada texto_izq" name="nombre"
														onkeypress="if (event.keyCode == 13) event.returnValue = false;"
														tabindex="" id="nombre" value="${showResultToView.nombre}"
														maxlength="79" size="80" />
												</span>
												</td>
												<td>
												<td><input type="button" class="boton_color"
													onkeypress="if (event.keyCode == 13) event.returnValue = false;"
													name="searchColegios" id="searchColegios"
													value="<fmt:message	key="inst.filtrar.colegio" bundle="${lang}" />" /></td>


												</td>
											</tr>

											<c:if test="${!empty actualizarColegio}">
												<input type="hidden" name="numSolicitud" id="numSolicitud"
													value="${numSolicitud}" />
												<input type="hidden" name="accion"
													value="solicitudActualizar.jsp" />
											</c:if>

											<c:if test="${empty actualizarColegio}">
												<tr>
													<td><label class=aviso><fmt:message
																key="inst.rif.no.existe" bundle="${lang}" /></label></td>
													<td>
														<!--El filtrarNameCheck esta reflejado en el archivo SearchColegiosAjax.js para filtrar el select 
                                                $( "#nombre" ).keypress(function-->
														<!--                                                 si es cero, esta en check -->
														<input onchange="intitucionSend();"
														name="sinInstitucionCheck" id="sinInstitucionCheck"
														type="checkbox" value="0" />
													</td>
												</tr>

											</c:if>


										</table>
									</form>
								</div>
							</td>
						</tr>




					</table> <!-- display all articles -->


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