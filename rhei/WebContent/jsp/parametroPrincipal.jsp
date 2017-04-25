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
<script type="text/javascript" src="/rhei/js/funciones.js"></script>
<script type="text/javascript" language="JavaScript">
	window.history.forward(1);
</script>
</head>
<body onload="actualizaReloj(); clickAdminMenu();clickParametroMenu();">
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
						<c:if test="${exito=='exito'}">
							<tr>
								<td colspan=1 class=avisoExito><fmt:message
										key="param.exito" bundle="${lang}" /> ${nombreParametro}
							</tr>
						</c:if>
						<c:if test="${fracaso=='fracaso'}">
							<tr>
								<td colspan=1 class=aviso><fmt:message key="param.fracaso"
										bundle="${lang}" /> ${nombreParametro}</td>
							</tr>
						</c:if>
						<c:if test="${tablaParametros == 'No hay registros'}">
							<c:if test="${empty  filtroParametro }">
								<tr>
									<td class=aviso>
									<fmt:message key="param.no.filtro.fracaso"
										bundle="${lang}" /> ${co_tipo_beneficio}
									</td>
								</tr>
							</c:if>
							<c:if test="${not empty  filtroParametro }">
								<tr>
									<td class=aviso>
									<fmt:message key="param.filtro.fracaso"
										bundle="${lang}" /> ${co_tipo_beneficio}
									</td>
								</tr>
							</c:if>

						</c:if>
						
						
						<tr>
							<td colspan="1">

								<h2>
									<fmt:message key="param.lista" bundle="${lang}" />
								</h2>
							</td>
						</tr>
						<tr>
							<td><c:if test="${tabla == 'parametro'}">
									<div class=derecha>
										<form name=formBuscarBeneficio method=post action="">
											<table class='formatoTabla4 formato10'>
												<tr>
													<td><label for="beneficioEscolar"> <fmt:message
																key="benef.seleccionar" bundle="${lang}" />:
													</label></td>
													<c:if test="${empty listaBeneficio}">
												</tr>
												<tr>
													<td align=left class=aviso><fmt:message
															key="aviso.no.hay.reg" bundle="${lang}" /></td>
												</tr>

												</c:if>

												<c:if test="${!empty listaBeneficio}">

													<td><select id="beneficioEscolar"
														name=beneficioEscolar class=entrada size=1 tabindex=""><option
																value="">
																<fmt:message key="select.seleccione" bundle="${lang}" />
															</option>
															<c:forEach var="beneficio" items="${listaBeneficio}">
																<option value="${beneficio}"
																	${beneficio == beneficioEscolar ? 'selected' : ''}>${beneficio}</option>
															</c:forEach>
													</select></td>
													<td><label for="filtroParametro"><fmt:message
																key="param.nombre" bundle="${lang}" />:</label></td>
													<td><input id="filtroParametro" type=text
														class=entrada name=filtroParametro
														value="${filtroParametro}" tabindex="" maxlength=6 size=6 /></td>
													<td><input type=button class=boton_color
														name=buscarParametro
														value='<fmt:message key="buscar"	bundle="${lang}" />'
														tabindex='' onclick="listaDeParam();" /> &nbsp;<input
														type=hidden name=companiaAnalista
														value="${companiaAnalista}" /></td>
													</tr>

												</c:if>
											</table>
										</form>
									</div>

								</c:if></td>
						</tr>

						<c:if test="${not empty viene}">

							<c:if test="${tablaParametros != 'No hay registros'}">
								<tr>
									<td>
										<form name=formPaginarParametros method=post action="">
											${tablaParametros} <input type=hidden name="tabla"
												value="${tabla}" /> <input type=hidden name="titulo"
												value="${titulo}" /> <input type=hidden
												name="companiaAnalista" value="${companiaAnalista}" /> <input
												type=hidden name="cuantosReg" value="${cuantosReg}" /> <input
												type=hidden name="indMenor" value="${indMenor}" /> <input
												type=hidden name="indMayor" value="${indMayor}" />


										</form>
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