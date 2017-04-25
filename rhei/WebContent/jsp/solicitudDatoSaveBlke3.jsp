
<tr>
	<td colspan="4" class="fondo_seccion"><label class="subtitulo">
	<fmt:message key="solicitud.datos.del.cei" bundle="${lang}" />
	</label></td>
</tr>
<tr>
    
	<td>
	<c:if test="${showResultToView.nroRifCentroEdu ne '0'}">
	<label><fmt:message key="rif.empresa" bundle="${lang}" />:</label>
	</c:if>
	</td>
	<td>
	   <c:if test="${showResultToView.accion eq '3' and showResultToView.nroRifCentroEdu ne '0'}">
	   <a href='/rhei/intitucionCtrl?principal.do=institucionIrToPag&pagIrAfter=accionActualizarColegio&numSolicitud=+${showResultToView.nroSolicitud}'>${showResultToView.nroRifCentroEdu}
     	</a>
	   </c:if>
	<c:if test="${showResultToView.accion ne '3' and showResultToView.nroRifCentroEdu ne '0'}">
	<input type="text" name="nroRif"
		value="${showResultToView.nroRifCentroEdu}" tabindex="37" id="37"
		maxlength="10" size="10" disabled="disabled" />
		</c:if>
		</td>
    

	<td><label>	<fmt:message key="nombre" bundle="${lang}" />:</label></td>
	<td>
	
	
	   <c:if test="${showResultToView.accion eq '3'}">
	   <a  href='/rhei/intitucionCtrl?principal.do=institucionIrToPag&pagIrAfter=accionActualizarColegio&numSolicitud=+${showResultToView.nroSolicitud}'>${showResultToView.nombreCentro}
     	</a>
	   </c:if>
	
	  <c:if test="${showResultToView.accion ne '3'}">
	<textarea name="nombreCentroEdu" tabindex="38" id="38"
			rows="2" cols="60" disabled="disabled" style="line-height: 32px;">${showResultToView.nombreCentro}</textarea>  
	  </c:if>
	</td>
</tr>
<tr>
	<td><label><fmt:message key="localidad" bundle="${lang}" />:</label></td>
	<td>
	
	  <c:if test="${showResultToView.accion eq '3'}">
	   <a href='/rhei/intitucionCtrl?principal.do=institucionIrToPag&pagIrAfter=accionActualizarColegio&numSolicitud=+${showResultToView.nroSolicitud}'>${showResultToView.localidadBCV}
     	</a>
	   </c:if>
	 <c:if test="${showResultToView.accion ne '3'}">
	<input type="text" name="localidad"
		value="${showResultToView.localidadBCV}" tabindex="39" id="39"
		maxlength="30" size="30" onfocus="blur()" />
	</c:if>
		
	</td>
	<td><label><fmt:message key="telefono" bundle="${lang}" />:</label></td>
	<td>
	
	  <c:if test="${showResultToView.accion eq '3'}">
	   <a href='/rhei/intitucionCtrl?principal.do=institucionIrToPag&pagIrAfter=accionActualizarColegio&numSolicitud=+${showResultToView.nroSolicitud}'>${showResultToView.tlfCentro}
     	</a>
	   </c:if>
	     <c:if test="${showResultToView.accion ne '3'}">
	  <input type="text" name="telefono"
		value="${showResultToView.tlfCentro}" tabindex="40" id="40"
		maxlength="15" size="15" disabled="disabled" />
	   </c:if>
	
		
		</td>
</tr>
<tr>
	<td><label><fmt:message key="correo" bundle="${lang}" />:</label></td>
	<td colspan="3">
	
	 <c:if test="${showResultToView.accion eq '3'}">
	   <a href='/rhei/intitucionCtrl?principal.do=institucionIrToPag&pagIrAfter=accionActualizarColegio&numSolicitud=${showResultToView.nroSolicitud}'>${showResultToView.email} 
     	</a>
	   </c:if>
	 <c:if test="${showResultToView.accion ne '3'}">
	 <input type="text" name="email"
		value="${showResultToView.email}" tabindex="41" id="41" maxlength="60"
		size="60" disabled="disabled" />
	 </c:if>
	
		
	</td>
</tr>

<c:if test="${accion ne 1 and showResultToView.nroRifCentroEdu ne '0'}">


<tr>
	<td><label><fmt:message key="tipo.de.cei" bundle="${lang}" />:</label></td>
	<td><select name="tipoInstitucion" class="entrada" size="1"
		${showResultToView.disabled} tabindex="42" id="42">
			<option value=""><fmt:message key="select.seleccione" bundle="${lang}" /></option>
			<c:forEach var="inst" items="${showResultToView.tipoInstituciones}">
				<option value="${inst.valor}"
					${showResultToView.tipoInstitucion==inst.valor?'selected':''}>${inst.nombre}</option>
			</c:forEach>
	</select></td>
	<td><label><fmt:message key="periodoEscolar" bundle="${lang}" />:</label></td>
	<td>
	
	<select name="periodoEscolar" id="periodoEscolar" class="entrada" size="1" 
	onchange="changePeriodoEscolar('${showResultToView.nroRifCentroEdu}','${showResultToView.cedula}');"
		${showResultToView.disabled} tabindex="43">
			<option value=""><fmt:message key="select.seleccione" bundle="${lang}" /></option>
			<c:forEach var="obj" items="${showResultToView.periodoEscolares}">
				<option value="${obj.valor}"
					${showResultToView.periodoEscolar==obj.valor?'selected':''}>${obj.nombre}</option>
			</c:forEach>
	</select>
		</td>
</tr>
<tr>
	<td><label><fmt:message key="tipo.educacion" bundle="${lang}" />:</label></td>
	<td><select name="tipoEducacion" class="entrada" size="1" id="tipoEducacion"
	onchange=validTipoEduc() 
		${showResultToView.disabled} tabindex="43" id="43">
			<option value=""><fmt:message key="select.seleccione" bundle="${lang}" /></option>
			<c:forEach var="obj" items="${showResultToView.tipoEducacions}">
				<option value="${obj.valor}"
					${showResultToView.tipoEducacion==obj.valor?'selected':''}>${obj.nombre}</option>
			</c:forEach>
	</select></td>
	<td><label><fmt:message key="nivel.escolaridad" bundle="${lang}" />:</label></td>
	<td><select name="nivelEscolaridad" class="entrada" size="1"
		${showResultToView.disabled} tabindex="44"
		id="nivelEscolaridad">
			<option value=""><fmt:message key="select.seleccione" bundle="${lang}" /></option>
			<c:forEach var="obj" items="${showResultToView.nivelEscolaridades}">
				<option value="${obj.valor}"
					${showResultToView.nivelEscolaridad==obj.valor?'selected':''}>${obj.nombre}</option>
			</c:forEach>
	</select></td>
</tr>

<tr>
	<td><label><fmt:message key="periodo.de.pago" bundle="${lang}" />:</label></td>
	<td><select id="periodoPago" name="periodoPago" class="entrada" size="1"
		${showResultToView.disabled} tabindex="45" id="45">
			<option value=""><fmt:message key="select.seleccione" bundle="${lang}" /></option>
			<c:forEach var="obj" items="${showResultToView.periodoPagos}">
				<option value="${obj.valor}"
					${showResultToView.periodoPago==obj.valor?'selected':''}>${obj.nombre}</option>
			</c:forEach>
	</select></td>
	<td><label>
	<fmt:message
									key="monto.periodo" bundle="${lang}" />:
	</label></td>
	<td>
	
	<c:if test="${not empty showResultToView.disabled}">
	   <input type="text" name="montoPeriodo"
		class="texto_der" value="${showResultToView.montoPeriodo}"  
		maxlength="10" size="10" onfocus="blur()" />
		</c:if>
		<c:if test="${empty showResultToView.disabled}">
	  
	  <input type="text" name="montoPeriodo"
		value="${showResultToView.montoPeriodo}"
		class="entrada texto_der" onblur="formatearCampo(this)"
		onfocus="this.value = ''" tabindex="47" id="47" maxlength="12"
		size="12" />
	  
	  
		</c:if>
	
	</td>
 
</tr>

</c:if>


<tr>
<c:if test="${accion ne 1  and showResultToView.nroRifCentroEdu ne '0'}">
	<td><label><fmt:message
									key="monto.matricula" bundle="${lang}" />:</label></td>
	<td>
		
		<c:if test="${not empty showResultToView.disabled}">
	   <input type="text" name="montoMatricula" id="montoMatricula"
		class="texto_der" value="${showResultToView.montoMatricula}"  
		maxlength="10" size="10" onfocus="blur()" />
		</c:if>
		<c:if test="${empty showResultToView.disabled}">
	    <input type="text" name="montoMatricula" id="montoMatricula"
		${showResultToView.disabled}
		value="${showResultToView.montoMatricula}" class="entrada texto_der"
		onblur="formatearCampo(this)" onfocus="this.value = ''" tabindex="48"
		 maxlength="12" size="12"
		onkeyup="chequea3(this,document.formDatosSolicitud.registrarSolicitud)" />
		</c:if>
		
	</td>
</c:if>
 
	<td><label><fmt:message
									key="monto.otorgado.bcv" bundle="${lang}" />:</label></td>
	<td colspan="3"><input type="text" name="montoBCV"
		class="texto_der" value="${showResultToView.montoBCV}" tabindex="49"
		id="montoBCV" maxlength="12" size="12" onfocus="blur()" /></td>
</tr>