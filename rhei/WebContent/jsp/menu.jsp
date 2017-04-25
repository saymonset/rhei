<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<fmt:setBundle basename="ve.org.bcv.rhei.util.bundle" var="lang" />
<fmt:setLocale value="es" />
<%
	String grupo = session.getAttribute("grupo").toString().trim();
	String autenticarAutomatic = session.getAttribute("autenticarAutomatic") != null
			? session.getAttribute("autenticarAutomatic").toString().trim() : "false";
%>
<div>
	<ul class="menu">
		<li><a href="/rhei/jsp/principal.jsp">Inicio</a></li>
		<%
			if (grupo.compareTo("GC_User_RHEI_ADMIN") == 0) {
		%>
		<li><a id='admMenu' href="#"><fmt:message key="menu.adm"
					bundle="${lang}" /></a>
			<ul>
				<li><a id='beneficioEscolarMenu' href="#"><fmt:message
							key="menu.benf_esco" bundle="${lang}" /></a>
					<ul>
						<li><a
							href=/rhei/benefScolarControlador?principal.do=principal
							title='<fmt:message key="benef.lista" bundle="${lang}"/>'
							accesskey='L'> <fmt:message key="benef.lista"
									bundle="${lang}" /></a></li>
						<li><a
							href=/rhei/benefScolarControlador?principal.do=blankParam
							title='<fmt:message key="benef.agregar" bundle="${lang}"/>'
							accesskey='A'> <fmt:message key="benef.agregar"
									bundle="${lang}" /></a></li>

					</ul></li>
				<li><a id='parametroMenu' href="#"><fmt:message
							key="menu.param" bundle="${lang}" /></a>

					<ul>
						<li><a
							href=/rhei/ParametroControlador?principalParametro.do=principalParam
							title='<fmt:message key="param.lista" bundle="${lang}"/>'
							accesskey='L'> <fmt:message key="param.lista"
									bundle="${lang}" /></a></li>
						<c:if test="${not empty vieneParametro}">
							<!--                         <li> -->
							<%--                         <a href='/rhei/ParametroControlador?principalParametro.do=blankParam&beneficioEscolar=${beneficioEscolar}' title='<fmt:message key="param.agregar" bundle="${lang}"/>' --%>
							<%-- 										> <fmt:message key="param.agregar"	bundle="${lang}" /></a> --%>
							<!--                         </li> -->
						</c:if>
					</ul></li>
				<li><a id='escolarMenu' href="#"><fmt:message
							key="menu.periodo" bundle="${lang}" /></a>

					<ul>
						<li><a href=/rhei/periodoScolarControlador?principal.do=lista
							title='<fmt:message key="periodo.lista" bundle="${lang}"/>'>
								<fmt:message key="periodo.lista" bundle="${lang}" />
						</a></li>

						<li><a
							href='/rhei/periodoScolarControlador?principal.do=blank'
							title='<fmt:message key="periodo.agregar" bundle="${lang}"/>'>
								<fmt:message key="periodo.agregar" bundle="${lang}" />
						</a></li>


						<!--                         <li><a href='/rhei/periodoScolarControlador?principal.do=borrar'>Solo pruebas (Borrar en producción)</a></li> -->

					</ul></li>
			</ul></li>
		<%
			}
		%>
		<li><a id='educInicial' href="#"><fmt:message
					key="educacion.inicial" bundle="${lang}" /></a>
			<ul id="open">

				<li><a id='gstsolMenu' href='#'><fmt:message
							key="solicitud.gestion" bundle="${lang}" /></a>
					<ul>
						<%
							if (grupo.compareTo("GC_User_RHEI_ADMIN") == 0) {
						%>
						<li><a
							href='/rhei/cedulaBuscarCtrl?principal.do=cedulaIrToPag'><fmt:message
									key="cedula.buscar" bundle="${lang}" /></a></li>
						<li>
							<%
								}
							%> <a
							href='/rhei/intitucionCtrl?principal.do=institucionIrToPag&pagIrAfter=accionIncluir'><fmt:message
									key="solicitud.incluir" bundle="${lang}" /></a>
						</li>

						<li><a
							href='/rhei/solicitudControladorConsultar?principal.do=consultarIrToPag'><fmt:message
									key="solicitud.consultar" bundle="${lang}" /></a></li>
						<%
							if ((grupo.compareTo("GC_User_RHEI_ADMIN") == 0)) {
						%>
						<li><a
							href='/rhei/solicitudControladorActualizar?principal.do=actualizarIrToPag'><fmt:message
									key="solicitud.actualizar" bundle="${lang}" /></a></li>
						<%
							}
							if ((grupo.compareTo("GC_User_RHEI_ADMIN") == 0)) {
						%>
						<li><a
							href='/rhei/solicitudDesincorporarControlador?principal.do=desincorporarIrToPag'><fmt:message
									key="solicitud.desincorporar" bundle="${lang}" /></a></li>
						<%
							}
						%>

					</ul></li>
				<%
					if ((grupo.compareTo("GC_User_RHEI_ADMIN") == 0)) {
				%>
				<li><a id='gstPagoMenu' href=\"#\"><fmt:message
							key="pagos.gestion" bundle="${lang}" /></a>
					<ul>
						<%
							if ((grupo.compareTo("GC_User_RHEI_ADMIN") == 0)) {
						%>
						<li><a id='gstPagoRegistrMenu' href=#><fmt:message
									key="registrar" bundle="${lang}" /></a>
							<ul>
								<li><a
									href='/rhei/pagosControladorReg?principal.do=pagoConvencional'><fmt:message
											key="pagos.convencionales" bundle="${lang}" /></a></li>
							</ul></li>


						<li><a id='gstConsultar'
							href='/rhei/pagosControladorConsultar?principal.do=consultarPago'><fmt:message
									key="consultar" bundle="${lang}" /></a></li>
						<%
							}
						%>
						<%
							if ((grupo.compareTo("GC_User_RHEI_ADMIN") == 0)) {
						%>
						<li><a id='gstActualizarMenu' href=#><fmt:message
									key="actualizar" bundle="${lang}" /></a>
							<ul>
								<li><a id='gstActualizar'
									href='/rhei/pagosControladorActualizar?principal.do=actualizarPagoIr'><fmt:message
											key="pagos.convencionales" bundle="${lang}" /></a></li>
							</ul></li>
						<%
							}
								if ((grupo.compareTo("GC_User_RHEI_ADMIN") == 0)) {
						%>

						<li><a id='gstReporteMenu' href=#><fmt:message
									key="reporte" bundle="${lang}" /></a>
							<ul>
								<%-- 										    <li><a id='gstreportByFactura' href='/rhei/reportePagosControlador?principal.do=reporteByPeriodo'><fmt:message key="reporte.periodo"	bundle="${lang}" /></a></li> --%>

								<li><a id='reportByPagosandTributos'
									href='/rhei/reporteDefinitivoTransitorioControlador?principal.do=reportByPagosandTributos'><fmt:message
											key="reporte.reportByPagosandTributos" bundle="${lang}" /></a></li>

								<li><a id='reportConsultaIndividual'
									href='/rhei/reporteConsultaIndividualControlador?principal.do=reportConsulta'><fmt:message
											key="reporte.reportConsulta.individual" bundle="${lang}" /></a></li>

								<li><a id='reportUtiles'
									href='/rhei/reporteBecaUtileContrlador'><fmt:message
											key="reporte.reportUtiles" bundle="${lang}" /></a></li>




							</ul></li>
						<%
							}
						%>
					</ul></li>
				<%
					}
				%>

			</ul></li>
		<%
			if (!"true".equalsIgnoreCase(autenticarAutomatic)) {
		%>
		<li><a href="/rhei/CerrarSesionServlet"><fmt:message
					key="salir" bundle="${lang}" /></a></li>

		<%
			}
		%>

	</ul>
</div>