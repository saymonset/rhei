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
<title><fmt:message	key="benef.pricipal.variables" bundle="${lang}" /></title>
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
	onload=" clickGstsolMenu(); document.formDatosSolicitud.tramite[0].checked = true; actualizaReloj(); formatearCampo(document.formDatosSolicitud.montoBCV); formatearCampo(document.formDatosSolicitud.montoAporteEmp);formatearCampo(document.formDatosSolicitud.montoMatricula);formatearCampo(document.formDatosSolicitud.montoPeriodo);	">
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
											key="solicitud.desincorporar.exito" bundle="${lang}" />
								</tr>
							</c:if>
							<c:if test="${showResultToView.mensaje=='solicitud.ced.fail'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="solicitud.ced.fail" bundle="${lang}" />
										${showResultToView.cedula}</td>
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
											key="solicitud.desincorporar.fracaso2" bundle="${lang}" /></td>
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
								<h2><fmt:message key="solicitud.desincorporar" bundle="${lang}" /></h2>
							</td>
						</tr>
						<!-- BUSCAR INICIO -->

						<form id="form1" name="formBuscarDatos" method="post"
							action="/rhei/solicitudDesincorporarControlador?principal.do=desincorporarInfoDataTrabBenef">
							
							<%@ include file="solicIngrBlke1WithNroSol.jsp"%>
							<%@ include file="solicitudDesincorporarByAnio.jsp"%>
							
							
							  <input type ="hidden" name="nroRifCentroEdu" value="${showResultToView.nroRifCentroEdu}"></input>
							<input type="hidden" name="accion"
								value="solicitudDesincorporar.jsp" />
						</form>
						<!-- BUSCAR FIN-->
						<c:if
							test="${!empty showResultToView.viene && showResultToView.viene==1}">
							<c:if test="${not empty showResultToView.tablaGenerada}">
							<form name="formulario1" method="post" action="" >
								<tr>
									<td class=avisoExito>${showResultToView.tablaGenerada}</td>
								</tr>
								<tr>
									<td colspan="4" id="centrado">  <input type="hidden"
										name="accion" value="solicitudDesincorporar.jsp" /> <input
										type="hidden" name="periodoE"
										value="${showResultToView.periodoEscolar}" /> <input
										type="hidden" name="codigoPeriodo"
										value="${showResultToView.codigoPeriodo}" /> <input
										type="hidden" name="codigoPeriodoE"
										value="${showResultToView.codigoPeriodo}" /> <input
										type="hidden" name="periodoEV"
										value="${showResultToView.formatoPeriodoVigente}" /> <input
										type="hidden" name="codigoPeriodoEV"
										value="${showResultToView.codigoPeriodoVigente}" /></td>
								</tr>
								<tr>
									<td colspan="4" align="center"><input type="hidden"
										name="cedula" value="${showResultToView.cedula}" /> <input
										type="hidden" name="codigoBenef"
										value="${showResultToView.codigoBenef}" /> <input
										type="hidden" name="codEmp" value="${showResultToView.codEmp}" />
										<input type="hidden" name="cedBenef"
										value="${showResultToView.cedulaFamiliar}" /> <input
										type="hidden" name="cedEmp" value="${showResultToView.cedula}" />
										<input type="hidden" name="nroRifCentroEdu"
										value="${showResultToView.nroRifCentroEdu}" /> <input
										type="hidden" name="numSolicitud"
										value="${showResultToView.numSolicitud}" /> <input
										type="hidden" name="co_status"
										value="${showResultToView.co_status}" /> <input type="hidden"
										name="nivelEscolar" value="${showResultToView.nivelEscolar}" />

									</td>
								</tr>
								</form>
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