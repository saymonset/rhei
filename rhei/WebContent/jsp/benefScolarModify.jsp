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
<title><fmt:message key="benef.variables" bundle="${lang}"/></title>
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
<body onload="clickAdminMenu();clickBenefScolarMenu(); actualizaReloj();">
	<jsp:include page="encabezado.jsp" flush="true" />
	<div class="centraTabla">
		<table cellspacing="0" cellpadding="0">
			<tr>
				<td valign="top"><div class="izquierda"><jsp:include
							page="menu.jsp" flush="true" /></div></td>
				<td>
					<table cellspacing="0" cellpadding="0" class="anchoTabla4">
						<tr>
							<td colspan="1" class="titulo"><fmt:message key="benef.titulo" bundle="${lang}"/></td>
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
								<c:if test="${mensaje=='Constraint'}">
									<tr>
										<td colspan=1 class=aviso>${infoError}</td>
									</tr>
								</c:if>
							</c:if>
						</c:if>
						<tr>
							<td colspan="1">
								<h2>
								<fmt:message key="benef.modificar" bundle="${lang}"/>
							</h2>

							</td>
						</tr>

						<tr>
							<td>
								<div class="derecha">
									<form name="formBeneficio" method="post" action="">
										<table cellspacing="2" cellpadding="2" class="anchoTabla3">
											<tr>
												<td><label><fmt:message key="nombre" bundle="${lang}"/>:</label></td>
												<td colspan="5"><input type="text"  class="entradaInactiva"
												onfocus="blur()"
													name="nombreBeneficio"  value="${nombreBeneficio}"
													tabindex="1" id="1" maxlength="4" size="4"
												 /></td>
											</tr>
											<tr>
												<td><label><fmt:message key="descripcion" bundle="${lang}"/>:</label></td>
												<td colspan="5"><input type="text"
													name="valorBeneficio" class="entrada" value="${valorBeneficio}" tabindex="2"
													id="2" maxlength="100" size="100"
													onkeypress="if (event.keyCode == 13) event.returnValue = false;"
													onkeyup="convertirAMayuscula(this);" /></td>
											</tr>
											<tr>
												<td><label><fmt:message key="fecha.registro" bundle="${lang}"/>:</label></td>
												<td><input type="text" name="fechaRegistro"
													class="entradaInactiva"
												onfocus="blur()" value="${fechaRegistro}" tabindex="3" id="3" maxlength="10"
													size="10"   />
												</td>
												<td></td>
												<td colspan="3"><label><fmt:message key="estado" bundle="${lang}"/>:&nbsp;</label>
												
												<select name="estado"   class="entrada"  
														size="1" tabindex="4" id="4">
															<c:forEach var="tipoEstado" items="${tipoEstados}">
																<option value="${tipoEstado.valor}"
																${estado==tipoEstado.valor?'selected':''}
																>${tipoEstado.nombre}</option>
															</c:forEach>
													</select>
												
												
												</td>
											</tr>
											<tr>
												<td colspan="6" id="centrado"><input type="button"
													class="boton_color2" name="crear" value="<fmt:message key="modificar" bundle="${lang}"/>"
													tabindex="5" id="5"
													onclick="modifyBeneficiario(document.formBeneficio)" /> &nbsp;<input
													type="hidden" name="operacion" value="modificar" />&nbsp;<input
													type="hidden" name="tabla" value="beneficioEscolar" /></td>
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