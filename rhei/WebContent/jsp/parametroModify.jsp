<%@ page contentType="text/html; charset=iso-8859-1" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
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
<title>Administraci&oacute;n de Variables</title>
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
<body onload="actualizaReloj();clickAdminMenu();clickParametroMenu();">
	<jsp:include page="encabezado.jsp" flush="true" />
	<div class="centraTabla">
		<table cellspacing="0" cellpadding="0">
			<tr>
				<td valign="top"><div class="izquierda"><jsp:include
							page="menu.jsp" flush="true" /></div></td>
				<td>
					<table cellspacing="0" cellpadding="0" class="anchoTabla5">
						<tr>
							<td colspan="1" class="titulo">SISTEMA DE GESTI&Oacute;N DE
								BENEFICIOS ESCOLARES</td>
						</tr>


						<c:if test="${!empty viene2}">
							<c:if test="${viene2== 'viene2'}">
								<c:if test="${mensaje=='Exito'}">
									<tr>
										<td colspan=1 class=avisoExito>Se proces&oacute;
											exitosamente la operaci&oacute;n realizada</td>
									</tr>
								</c:if>
								<c:if test="${mensaje=='Fracaso'}">
									<tr>
										<td colspan=1 class=aviso>La operaci&oacute;n no pudo ser
											procesada</td>
									</tr>
								</c:if>
								<c:if test="${mensaje=='Error'}">
									<tr>
										<td colspan=1 class=aviso>La operaci&oacute;n no es
											v&aacute;lida, ya existe un registro con los valores claves
											ingresados</td>
									</tr>
								</c:if>

							</c:if>
						</c:if>
						<tr>
							<td colspan="1">
								<h2>Modificaci&oacute;n de Par&aacute;metro</h2>

							</td>
						</tr>

						<tr>
							<td>
								<div class="derecha">
									<form name="formParametro" method="post" action="">
										<table class="anchoTabla3" cellpadding="2" cellspacing="2">
											<tbody>
												<tr>
													<td><label>Nombre:</label></td>
													<td><input name="nombreParametro"
														class="entradaInactiva" value="${nombreParametro}"
														tabindex="1" id="1" maxlength="6" size="6"
														onfocus="blur()" type="text"></td>
													<td><label>Beneficio Escolar:</label></td>
													<td><input name="tipoBeneficio"
														class="entradaInactiva" value="${beneficioEscolar}"
														tabindex="2" id="2" maxlength="4" size="4"
														onfocus="blur()" type="text"></td>
													<td><label>Tipo de Dato: </label></td>
													<td><select name="tipoDato" onfocus="blur()"
														class="entradaInactiva" size="1" tabindex="3" id="3">
															<c:forEach var="tipoDeDato" items="${tiposDeDatos}">
																<option value="${tipoDeDato.valor}"
																	${tipoDato==tipoDeDato.valor?'selected':''}>${tipoDeDato.nombre}</option>
															</c:forEach>
													</select></td>
												</tr>
												<tr>
													<td><label>Valor:</label></td>
													<td colspan="5"><input name="valorParametro"
														class="entrada" value="${valorParametro}" tabindex="4"
														id="4" maxlength="160" size="160"
														onblur="formato_camposMTOBCV()" onfocus="this.value = ''"
														onkeypress="if (event.keyCode == 13) event.returnValue = false;"
														type="text"></td>
												</tr>
												<tr>
													<td><label>Fecha de Vigencia:</label></td>
													<td><input name="fechaVigencia"
														class="entradaInactiva" value="${fechaVigencia}"
														tabindex="5" id="5" maxlength="10" size="10"
														onfocus="blur()" type="text"></td>
													<td><label>Compañía:</label></td>
													<td><select name="compania" class="entradaInactiva"
														size="1" tabindex="6" id="6" onfocus="blur()">
															<c:forEach var="companiaSelect"
																items="${listadoCompanias}">
																<option value="${companiaSelect.valor}"
																	${companiaSelect.valor==compania?'selected':'' }>${companiaSelect.nombre}</option>
															</c:forEach>
													</select></td>
													<td><label>Tipo Empleado:</label></td>
													<td><select name="tipoEmp" class="entradaInactiva"
														size="1" onfocus="blur()" tabindex="7" id="7">
															<c:forEach var="empleado" items="${listadoEmpleados}">
																<option value="${empleado.valor}"
																	${empleado.valor==tipoEmp?'selected':''}>${empleado.nombre}</option>
															</c:forEach>
													</select></td>
												</tr>
												<tr>
													<td><label>Observaciones:</label></td>
													<td colspan="5"><input name="descripcion"
														class="entrada" value="${descripcion}" tabindex="8" id="8"
														maxlength="160" size="160"
														onkeypress="if (event.keyCode == 13) event.returnValue = false;"
														type="text"></td>
												</tr>
											<tr>
													<td colspan="6" id="centrado"> <c:if
															test="${!isParametroMTBCV}">
															<input class="boton_color2" name="crear"
																value="Modificar" tabindex="9" id="9"
																onclick="modifyParam(document.formParametro)"
																type="button">&nbsp; 
														</c:if> <input name="operacion" value="Modificar" type="hidden">&nbsp;<input
															name="tabla" value="parametro" type="hidden">&nbsp;<input
																name="beneficioEscolar" value="${beneficioEscolar}"
																type="hidden">&nbsp; </td>
												</tr>
											</tbody>
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