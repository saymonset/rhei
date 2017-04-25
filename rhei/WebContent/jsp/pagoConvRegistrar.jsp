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
<html ng-app="todoApp">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title><fmt:message key="benef.pricipal.variables"
		bundle="${lang}" /></title>



<script type="text/javascript" src="/rhei/js/angular.js"></script>
<script type="text/javascript" src="/rhei/js/angular-locale_es-ve.js"></script>
<script type="text/javascript" src="/rhei/js/angular-resource.js"></script>
<link rel="stylesheet" type="text/css" href="/rhei/css/bootstrap.css" />
<link rel="stylesheet" type="text/css"	href="/rhei/css/bootstrap-theme.css" />
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
<script type="text/javascript"
	src="/rhei/js/jquery-ui-1.11.3.custom/jquery-ui.js"></script>
<script type="text/javascript"
	src="/rhei/js/jquery-ui-1.11.3.custom/configCal.js"></script>
<link rel="stylesheet"
	href="/rhei/js/jquery-ui-1.11.3.custom/jquery-ui.css">

<script type="text/javascript" src="/rhei/js/jquery.ui.widget.js"></script>
<script type="text/javascript" src="/rhei/js/jquery-picklist.js"></script>

<link type="text/css" href="/rhei/css/jquery-picklist.css"
	rel="stylesheet" />
<script type="text/javascript" language="JavaScript">
	window.history.forward(1);

	$(function($) {
		$("#fechaFactura").datepicker();
	});
</script>

<script>
	var todoApp = angular.module("todoApp", [ "ngResource" ]);
</script>

<script type="text/javascript"
	src="/rhei/js/controllers/todoControllers.js"></script>
<script type="text/javascript"
	src="/rhei/js/controllers/complementoValidateControllers.js"></script>

<style>
span.error {
	color: red;
	font-weight: bold;
}
</style>

</head>
<body ng-controller="complementoValidateCtrl"
	onload="showConyuge(); actualizaReloj(); clickGstPagoRegistrMenu(); formato_camposMontosPagos();   ">

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
											key="pago.exito" bundle="${lang}" />
								</tr>
							</c:if>
							<c:if test="${showResultToView.mensaje=='fracasoFact'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="solicitud.fact.fracaso" bundle="${lang}" />
										${showResultToView.nroFactura}</td>
								</tr>
							</c:if>
							<c:if test="${showResultToView.mensaje=='solicitud.ced.fail'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="solicitud.ced.fail" bundle="${lang}" />
										${showResultToView.cedula}</td>
								</tr>
							</c:if>
<!-- 							agregado por david velasquez -->
							<c:if test="${showResultToView.mensaje=='solicitud.ced.fail.empleado_sin_solicitud'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="solicitud.ced.fail.empleado_sin_solicitud" bundle="${lang}" />
										${showResultToView.cedula}</td>
								</tr>
							</c:if>
							
							
<!-- 							hasta aqui lo garegado por david velasquez -->
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
									<td colspan=1 class=aviso><fmt:message key="pago.fracaso2"
											bundle="${lang}" /> ${showResultToView.mes}</td>
								</tr>
							</c:if>


							<c:if test="${showResultToView.mensaje=='fracaso3'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="pagos.convencional.fracaso3" bundle="${lang}" /></td>
								</tr>
							</c:if>
							<c:if test="${showResultToView.mensaje=='error'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message key="pago.error"
											bundle="${lang}" /></td>
								</tr>
							</c:if>
							<c:if test="${showResultToView.mensaje=='errorComplemento'}"> 
								<tr>
									<td colspan=1 class=aviso><fmt:message key="pago.errorComplemento"
											bundle="${lang}" /></td>
								</tr>
							</c:if>
							<c:if test="${showResultToView.mensaje=='beneficiario_no_hay'}">
								<tr>
									<td colspan=1 class=aviso><fmt:message
											key="pago.error.registar" bundle="${lang}" />
										</td>
								</tr>
							</c:if>
						</c:if>

						<tr>
							<td colspan="1">
								<h2>

									<fmt:message key="registrar" bundle="${lang}" />
									<fmt:message key="pagos.convencionales" bundle="${lang}" />
								</h2>
							</td>
						</tr>
						<tr>
							<td><span id="messagewait" style="display: none;">

									<div style="text-align: center;">
										<img class="bannerLogin" border="0" src="imagenes/ai.gif"
											alt="" />
										<fmt:message key="wait.hold_on" bundle="${lang}" />
									</div>
							</span></td>
						</tr>


						<form id="form1" name="formBuscarDatos" method="post"
							action="/rhei/pagosControladorReg?principal.do=buscarData">

							<%@ include file="solicIngrBlke1WithNroSol.jsp"%>
							
							

							<input type="hidden" name="nroRifCentroEdu"
								value="${showResultToView.nroRifCentroEdu}"></input> <input
								type="hidden" name="accion" value="pagoConvRegistrar.jsp" />
						</form>

						<c:if
							test="${(showResultToView.nroRifCentroEdu eq '0' or showResultToView.montoMatricula eq '0' or showResultToView.montoPeriodo eq '0')}">
							<tr>
								<td colspan="1" class=aviso><fmt:message
										key="cei.actualizar" bundle="${lang}" />&nbsp;</td>
							</tr>
						</c:if>

						<c:if
							test="${!empty showResultToView.viene && showResultToView.viene==1 && empty showResultToView.mensaje  and (showResultToView.nroRifCentroEdu ne '0'  and showResultToView.montoMatricula ne '0' and showResultToView.montoPeriodo ne '0')}">
							<tr>
								<td>
									<form id="formDatosSolicitud" name="formDatosSolicitud"
										method="post"
										action="/rhei/pagosControladorReg?principal.do=convencionalPago">

										<table cellspacing="5" cellpadding="2" class="anchoTabla4">

											<%@ include file="solicitudTrabBenefBlke2.jsp"%>

											<c:if test="${empty showResultToView.mensaje}">

												<%@ include file="solicitudDatoSaveBlke3.jsp"%>
												<tr>
													<td colspan="4" class="fondo_seccion"><label
														class="subtitulo"><fmt:message key="pago.registro"
																bundle="${lang}" /></label></td>
												</tr>
												<tr>
													<td><label><fmt:message key="fecha.registro"
																bundle="${lang}" />:</label></td>
													<td><input type="text" name="fechaRegistro"
														id="fechaRegistro"
														value="${showResultToView.fechaRegistro}" tabindex="26"
														maxlength="8" size="8" onfocus="blur()" /></td>
													<td><label><fmt:message key="pago.status"
																bundle="${lang}" />:</label></td>
													<td><input name="estatusPago"
														value="${showResultToView.estatusPago}" tabindex="27"
														id="27" maxlength="20" size="20" disabled="disabled"
														type="text"></td>
												</tr>
												<tr>
													<td><label><fmt:message
																key="pago.fecha.factura" bundle="${lang}" />:</label></td>
													<td><input name="fechaFactura" class="entrada"
														value="${showResultToView.fechaFactura}"
														onkeyup="mascara(this,'-','fecha',true)" tabindex="28"
														id="fechaFactura" maxlength="10" size="10" type="text">
														<input type='image' src='/rhei/imagenes/calendar.ico'
														onClick="clickfechaFactura();return false;" /></td>

													<td><label><fmt:message key="pago.num.factura"
																bundle="${lang}" />:</label></td>
													<td><input name="nroFactura" class="entrada"
														value="${showResultToView.nroFactura}" tabindex="29"
														id="29" maxlength="20" size="20" type="text"></td>
												</tr>
												<tr>
													<td><label><fmt:message
																key="pago.numero.control" bundle="${lang}" />:</label></td>
													<td><input name="nroControl" class="entrada"
														value="${showResultToView.nroControl}" tabindex="30"
														id="30" maxlength="20" size="20" type="text"></td>

													<td><label><fmt:message
																key="pago.monto.factura" bundle="${lang}" />:</label></td>
													<td><input type="text" name="montoFactura"
														class="texto_der" value="${showResultToView.montoFactura}"
														tabindex="31" id="montoFactura" maxlength="12" size="12"
														onfocus="blur()" /></td>
												</tr>
												<tr>
													<td><label><fmt:message
																key="pago.concepto.pago" bundle="${lang}" />:</label></td>
													<td><input type="checkbox" name="conceptoPago"
														value="0" tabindex="32" id="32" disabled /> <label><fmt:message
																key="matricula" bundle="${lang}" /></label> &nbsp;&nbsp;&nbsp;
														&nbsp;&nbsp;&nbsp; <input name="conceptoPago"
														class="entrada" value="1" disabled tabindex="33" id="33"
														type="checkbox"><label>Período</label></td>
													<td><input type="checkbox" id="pagado" name="pagado"
														ng-model="pagado" value="1" tabindex="32" id="32" />
														&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;<label><fmt:message
																key="pago.cancelado" bundle="${lang}" /></label></td>

												</tr>
												<tr ng-show="pagado">
													<td></td>
													<td></td>
													<td><fmt:message key="observaciones" bundle="${lang}" /></td>
													<td><input type="text" class="form-control2"
														name="txtobservacionespagado" id="txtobservacionespagado" /></td>
												</tr>


												<tr>
													<td><label><fmt:message key="mes.pagar"
																bundle="${lang}" />:</label></td>
													<td><select ng-click="cerrarHayProductos()"
														name="mesesPorPagar" id="mesesPorPagar" width=150px;
														size="15" multiple="multiple" class="entrada"
														tabindex="36"
														onchange="turnOnOffmesApagar(this);activarConcepto(this);calcular(document.formDatosSolicitud.montoFactura, '1');">
															<option value="">
																<fmt:message key="pagos.complementReembolso.show.check"
																	bundle="${lang}" />
															</option>
															<c:forEach var="obj"
																items="${showResultToView.listadoMesesPorPagar}">
																<option value="${obj.valor}">${obj.nombre}</option>
															</c:forEach>


													</select></td>

													<td><label> <fmt:message key="pagos.forma"
																bundle="${lang}" /> :
													</label></td>
													<td><select name="formaPago" class="entrada" size="1"
														tabindex="46" id="46">
															<c:forEach var="formaPagoValNom"
																items="${showResultToView.formaPagoValNoms}">
																<option value="${formaPagoValNom.valor}"
																	${formaPagoValNom.valor==showResultToView.formaPago?'selected':''}>
																	${formaPagoValNom.nombre}</option>
															</c:forEach>
													</select></td>

												</tr>
												<tr>
													<td><label><fmt:message key="pago.dirigido"
																bundle="${lang}" />:</label></td>
													<td><select name="receptorPago" class="entrada"
														onchange="changeReembolsoComplemento(this);"
														id="receptorPago" size="1" tabindex="">
															<option value=""><fmt:message
																	key="select.seleccione" bundle="${lang}" /></option>
															<c:forEach var="obj"
																items="${showResultToView.receptorPagos}">
																<option value="${obj.valor}"
																	${obj.valor==showResultToView.receptorPago?'selected':''}>${obj.nombre}</option>
															</c:forEach>
													</select></td>
												</tr>
												<tr>
													<td></td>
													<td><span id="showtxtComplementomesCheck"
														style="display: none;"> <input type="checkbox"
															id="showCheckCompklement" name="showCheckCompklement"
															onclick="changeShowHideReembolsoComplemento(this);"
															tabindex="32" id="32" /> &nbsp;<label><fmt:message
																	key="pagos.complementReembolso" bundle="${lang}" /></label>
													</span></td>
												</tr>
												<tr>
													<td><span id="showtxtComplementomestext"
														style="display: none;"> <label><fmt:message
																	key="mes" bundle="${lang}" />: </label> 
													</span></td>
													<td><span id="showtxtComplementomes"
														style="display: none;"> 
														<select
															ng-model="mesComplementSelect"
															ng-click="evaluarValidacion('${showResultToView.nroSolicitud}',mesComplementSelect,'${showResultToView.montoBCV}','${showResultToView.montoMatricula}','${showResultToView.montoPeriodo}','${url_complemento}','${showResultToView.periodoEscolar}')"
															name="mesecomplemento" id="mesecomplemento" width=150px;
															size="15" multiple="multiple" class="entrada">
																<c:forEach var="obj" items="${listadoMeses}">
																	<option value="${obj.valor}">${obj.nombre}</option>
																</c:forEach>
														</select>

													</span></td>
													<td ng-show="hayProductos">
														<table class="table table-striped table-bordered">
															<thead>
																<tr>
																	<th>Mes</th>

																	<th>Monto Pago</th>
																	<th></th>
																	<th></th>
																</tr>

															</thead>
															<tbody>
																<tr ng-repeat="item in products.historico">
																	<td>{{item.mes}}</td>
																	<td>{{item.montoPago | number:2}}</td>
																	<td><span class="label label-default"
																		ng-class="warningLevel(item.isCanPayMes)"> </span></td>
																	<td>
																	<input type="text" class="entrada texto_der"
																		ng-blur="validarMes(products.bcvMonto,item.montoPago,item.meskey,item.mes)"
																		ng-show="item.montoPago<products.bcvMonto"
																		name="{{item.mes}}" class="form-control"
																		id="{{item.mes}}" value="" ng-model="item.meskey"
																		onblur="formatearCampoFromAngular(this)"
																		onfocus="this.value = ''" maxlength="12" size="12">
																	</td>

																</tr>
															</tbody>
														</table>
														<span class="error"
														ng-show="errorPagoMes"> El monto {{errorMontoTotal | number:2}} debe ser menor a {{products.bcvMonto | number:2}} </span>
													</td>
												</tr>
											 

												<tr>
													<td><span id="showtxtComplemento"
														style="display: none;"> <label> <fmt:message
																	key="pago.txtdescripcion" bundle="${lang}" /><input
																type="text" name="txtcomplemento" class="texto_der"
																value="" tabindex="31" id="txtcomplemento"
																onfocus="blur()" />:
														</label>
													</span></td>
													<td><span id="showtxtComplementoArea"
														style="display: none;"> <input type="text"
															class="form-control2" name="txtcomplementoTxtArea"
															value="${showResultToView.txtcomplemento}"
															id="txtcomplementoTxtArea" />
													</span></td>
													<td><span id="showComplementoMonto2"
														style="display: none;"> <c:if
																test="${!not empty showResultToView.disabled}">
																<span id="showComplementoMonto" style="display: none;">
																	<label><fmt:message key="pago.montocomplemento"
																			bundle="${lang}" />:</label>
																</span>
																<input type="text" name="montocomplemento"
																	class="texto_der" id="montocomplemento"
																	value="${showResultToView.montocomplemento}"
																	maxlength="10" size="10" onfocus="blur()" />
															</c:if> <c:if test="${!empty showResultToView.disabled}">



																<table
																 
																	class="table table-striped table-bordered">

																	<tbody>
																		<tr>
																			<td ng-show="hayProductos">
																				<table class="table table-striped table-bordered">
																					<thead>
																						<tr>
																							<th>Total Pagado</th>
																							<th>Puede pagar hasta
																							
																							<span id="showComplementoMonto"
																								style="display: none;"> 
<!-- 																								<label -->
<!-- 																									ng-show="cuantoApagar(products.acumuladorBCV,products.acumuladorByMes)>0"> -->
<!-- 																								</label> -->
																							</span>
																							
																							</th>
																							 
																							 
																						</tr>

																					</thead>
																					<tbody>
																						<tr>
																							<td>{{products.acumuladorByMes | number:2}} </td>
																							<td>{{
																								cuantoApagar(products.acumuladorBCV,products.acumuladorByMes)
																								| number:2}}</td>
																						</tr>
																					</tbody>
																				</table>
																			</td>
																		</tr>

																	</tbody>
																</table>




															</c:if>
													</span></td>

												</tr>


												<tr>
													<td colspan="4" id="centrado">
														<!-- 												BOTON REGISTRAR PAGO --> <input
														type="button" class="boton_color2"
														name="registrarSolicitud"
														value="<fmt:message
									key="pagos.registrar" bundle="${lang}" />"
														tabindex="56" id="56" onclick="validarRegistroPagoCtrl()" />
														<!-- 											FIN	BOTON REGISTRAR PAGO --> <input
														type="hidden" name="numSolicitud"
														value="${showResultToView.numSolicitud}" /> <input
														type="hidden" name="nroSolicitud"
														value="${showResultToView.numSolicitud}" /> <input
														type="hidden" name="nroRif"
														value="${showResultToView.nroRifCentroEdu}" /> <input
														type="hidden" name="accion" value="pagoConvRegistrar.jsp" />
														<input name="estatusPago" value="T" tabindex="45" id="45"
														type="hidden"> <input name="mo_periodo"
														value="${showResultToView.montoPeriodo}" tabindex="47"
														id="47" type="hidden"> <input name="mo_matricula"
														value="${showResultToView.montoMatricula}" tabindex="48"
														id="48" type="hidden"> <input
														name="beneficioCompartido"
														value="${showResultToView.benefCompartido}" tabindex="49"
														id="49" type="hidden"> <input
														name="periodoEscolarAux"
														value="${showResultToView.periodoEscolar}" type="hidden">
													</td>
												</tr>
												<tr>
													<td colspan="4" align="center"><input type="hidden"
														name="cedula" value="${showResultToView.cedula}" /> <input
														type="hidden" name="codigoBenef"
														value="${showResultToView.codigoBenef}" /> <input
														type="hidden" name="codEmp"
														value="${showResultToView.codEmp}" /> <input
														type="hidden" name="cedBenef"
														value="${showResultToView.cedulaFamiliar}" /> <input
														type="hidden" name="cedEmp"
														value="${showResultToView.cedula}" /> <input
														type="hidden" name="co_status"
														value="${showResultToView.co_status}" /> <input
														type="hidden" name="nivelEscolar"
														value="${showResultToView.nivelEscolar}" /> <input
														name="mo_bcv" value="${showResultToView.montoBCV}"
														tabindex="51" id="51" type="hidden"></td>

													</td>
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