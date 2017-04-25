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
<title><fmt:message key="benef.pricipal.variables"	bundle="${lang}" /></title>
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
<body onload="actualizaReloj(); clickAdminMenu();clickPeriodoEscolarMenu();">
	<jsp:include page="encabezado.jsp" flush="true" />
	<div class="centraTabla">
		<table cellspacing="0" cellpadding="0">

			<tr>
				<td valign="top"><div class="izquierda"><jsp:include
							page="menu.jsp" flush="true" /></div></td>
				<td>
					<table cellspacing="0" cellpadding="0" class="anchoTabla4">
						<tr>
							<td colspan="1" class="titulo">
							<fmt:message key="benef.titulo"	bundle="${lang}" />
							</td>
						</tr>
						
						<c:if test="${exito=='exito'}">
								<tr>
									<td colspan=1 class=avisoExito><fmt:message key="periodo.exito"
											bundle="${lang}" /> ${descripcion}
								</tr>
							</c:if>
							<c:if test="${fracaso=='fracaso'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="periodo.fracaso" bundle="${lang}" /> ${descripcion}</td>
								</tr>
							</c:if>
						


						<c:if test="${tablaPeriodos == 'No hay registros'}">
							<tr>
								<td class=aviso>${mensaje}</td>
							</tr>
						</c:if>
						<c:if test="${tablaPeriodos != 'No hay registros'}">
							<tr>
								<td>
									<form name=formPeriodoScolar method=post action="">
										${tablaPeriodos} <input type=hidden name="tabla"
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