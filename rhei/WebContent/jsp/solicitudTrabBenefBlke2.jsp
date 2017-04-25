<tr>
	<td colspan="4" class="fondo_seccion"><label class="subtitulo">Datos
			del Trabajador </label></td>
</tr>
<tr>
 
<td><label>N° Solicitud:</label></td>
	<td><c:if
			test="${empty showResultToView.accion || showResultToView.accion!=1}">
			<input type="text" name="nroSolicitud"  
				value="${showResultToView.nroSolicitud}" tabindex="1" id="nroSolicitud"
				maxlength="20" size="20" disabled="disabled" />
		</c:if></td>
 
	<td><label>N° Expediente:</label></td>
	<td><input type="text" name="nroExpediente"
		value="${showResultToView.nroExpediente}" tabindex="2" id="2"
		maxlength="13" size="13" disabled="disabled" /></td>
	 
</tr>
<tr>
	<td><label>Fecha Solicitud:</label></td>
	<td><input type="text" name="fechaSolicitud"
		value="${showResultToView.fechaSolicitud}" tabindex="3" id="3"
		maxlength="15" size="15" disabled="disabled" /></td>
		<td><label>Fecha Actualización:</label></td>
		<td>
		<input type="text" name="fechaActualizacion"
		value="${showResultToView.fechaActual}" tabindex="3" id="3"
		maxlength="15" size="15" disabled="disabled" />
	</td>
	
</tr>




 
	<tr>
		
		<td><label>Estatus Solicitud:</label></td>
		<td><input name="statusSolicitud" value="${showResultToView.nb_status}" tabindex="9"
			id="statusSolicitud" maxlength="20" size="20" disabled="disabled" type="text"/>
		</td>
		<td><label><fmt:message key="cedula" bundle="${lang}" /></label></td>
	<td><input type="text" name="cedulaEmpleado" class="texto_der"
		value="${showResultToView.cedula}" tabindex="7" id="cedulaEmpleado" maxlength="8"
		size="8" disabled="disabled" /></td>
	</tr>
 

<tr>
	<td><label>Apellido:</label></td>
	<td><input type="text" name="apellido"
		value="${showResultToView.apellido}" tabindex="8" id="8"
		maxlength="30" size="30" disabled="disabled" /></td>
	<td><label>Nombre:</label></td>
	<td><input type="text" name="nombre"
		value="${showResultToView.nombre}" tabindex="9" id="9" maxlength="30"
		size="30" disabled="disabled" /></td>
</tr>
<tr>
	<td><label>Compa&ntilde;&iacute;a:</label></td>
	<td><input type="text" name="compania"
		value="${showResultToView.compania}" tabindex="4" id="4"
		maxlength="30" size="30" disabled="disabled" /></td>
	<td><label>Ubicaci&oacute;n:</label></td>
	<td><textarea name="ubicacion" tabindex="12" id="12" rows="2"
			cols="60" disabled="disabled" style="line-height: 32px;">${showResultToView.ubicacion}</textarea></td>
</tr>
<tr>
	<td><label>Cargo:</label></td>
	<td colspan="3"><input type="text" name="cargo"
		value="${showResultToView.cargo}" tabindex="13" id="13" maxlength="50"
		size="50" disabled="disabled" /></td>
</tr>
<tr>
	<td><label>Fecha Ingreso:</label></td>
	<td><input type="text" name="fechaIngreso"
		value="${showResultToView.fechaIngreso}" tabindex="10" id="10"
		maxlength="10" size="10" disabled="disabled" /></td>
	<td><label>Tipo Empleado:</label></td>
	<td><input type="text" name="tipoEmpleado"
		value="${showResultToView.tipoEmpleado}" tabindex="6" id="6"
		maxlength="30" size="30" disabled="disabled" /></td>
</tr>
<tr>
	<td><label>Situaci&oacute;n:</label></td>
	<td><input type="text" name="situacion"
		value="${showResultToView.situacion}" tabindex="5" id="5"
		maxlength="30" size="30" disabled="disabled" /></td>
	<td><label>Fecha Hasta:</label></td>
	<td><input type="text" name="fechaHasta"
		value="${!empty showResultToView.fechaHasta?showResultToView.fechaHasta:"
		" }"
													tabindex="30" id="30" maxlength="10" size="10"
		disabled="disabled" /></td>
</tr>

<tr>
	<td><label><fmt:message key="tlfNumExt" bundle="${lang}" />: </label></td>
	<td><input type="text" name="tlfNumExt"
		value="${showResultToView.tlfNumExt}" tabindex="6" id="tlfNumExtiD"
		maxlength="30" size="30" disabled="disabled" /></td>
	<td><label><fmt:message key="emailBcv" bundle="${lang}" />:</label></td>
	<td><input type="text" name="emailBcv"
		value="${showResultToView.emailBcv}"
													tabindex="7" id="emailBcvId" maxlength="50" size="50"
		disabled="disabled" /></td>
</tr>

 <tr>
	<td><label><fmt:message key="emailPropio" bundle="${lang}" />:</label></td>
	<td><input type="text" name="emailPropio"
		value="${showResultToView.emailPropio}" tabindex="8" id="emailPropioiD"
		maxlength="50" size="50" disabled="disabled" /></td>
	 
</tr>

<tr>
	<td colspan="4" class="fondo_seccion"><label class="subtitulo"><fmt:message key="solicitud.datos.padre.madre" bundle="${lang}" /></label></td>
</tr>
<tr>
	<td><label><fmt:message key="apellido" bundle="${lang}" />:</label></td>
	<td><input type="text" name="apellidoconyuge"
		value="${apellidoconyuge}" 
		maxlength="30" size="30" disabled="disabled" /></td>
	<td><label><fmt:message key="nombre" bundle="${lang}" />:</label></td>
	<td><input type="text" name="nombreconyuge"
		value="${nameconyuge}"  maxlength="30"
		size="30" disabled="disabled" /></td>
</tr>
<tr>
	  <td><label><fmt:message key="solicitud.trabaja.conyuge" bundle="${lang}" />: </label></td>
	<td><label><fmt:message key="si" bundle="${lang}" /></label> <input type="radio" name="benefCompartido" disabled
	  class="entrada" value="S" ${showResultToView.disabled}
		tabindex="" maxlength="1" size="1" onclick="verificarCombos3(this)"
		${showResultToView.benefCompartido=='S'?'checked':''} /> <label><fmt:message key="no" bundle="${lang}" /></label>
		<input type="radio" name="benefCompartido"
		 class="entrada" value="N" disabled
		tabindex="" size="1" maxlength="1"
		${showResultToView.benefCompartido!='S'?'checked':''}
		onclick="verificarCombos3(this)" /></td>
		
		
		<td>
	<span id="ciConyugetxtshow" style="display: none;">
	<label><fmt:message key="cedula" bundle="${lang}" />:</label>
	</span>
	</td>
	<td colspan="3">
	<span id="ciConyugeshow" style="display: none;">
		<input type="text" name="ciConyuge"  id="ciConyuge"  disabled
				value="${cedulaconyuge}" class="ulsinfondo"
				maxlength="20" size="20"
				/>
				<input type="hidden" name="ciConyugehidden" value="${cedulaconyuge}"/>
				</span>
		</td>
		
</tr>
<tr>
<td>
 <span id="tlfconyugetxtshow" style="display: none;">
       <label><fmt:message key="telefono" bundle="${lang}" />:</label>
</span>
</td>
	<td> 
	<span id="tlfconyugeshow" style="display: none;">
	<c:if test="${not empty showResultToView.disabled}">
	<input type="text" name="tlfconyuge" class="ulsinfondo" disabled ${showResultToView.disabled} value="${conyugeTrabajo.nuTlfTrabajo}"/>
	</c:if>
	<c:if test="${empty showResultToView.disabled}">
	<input type="text" name="tlfconyuge" class="entrada" ${showResultToView.disabled} value="${conyugeTrabajo.nuTlfTrabajo}"/>
	</c:if>
	 
	</span>
	 </td>
	<td>
	<span id="correoconyugetxtshow" style="display: none;">
	<label><fmt:message key="correo" bundle="${lang}" />:</label>
	</span>
	</td>
	<td colspan="3">
	<span id="correoconyugeshow" style="display: none;">
	<c:if test="${not empty showResultToView.disabled}">
	<input type="text" name="correoConyuge"  id="correoConyuge"
				value="${conyugeTrabajo.correoConyuge}" class="ulsinfondo" disabled
				${showResultToView.benefCompartido=='S'?'':' disabled'} 
				maxlength="50" size="50"
				/>
	
	</c:if>
	<c:if test="${empty showResultToView.disabled}">
	<input type="text" name="correoConyuge"  id="correoConyuge"
				value="${conyugeTrabajo.correoConyuge}" class="entrada"
				${showResultToView.benefCompartido=='S'?'':' disabled'} 
				maxlength="50" size="50"
				/>
	</c:if>
			
				</span>
		</td>
</tr>
<tr>
<span id="empresatxt" style="display: none;">
	<td colspan="4" class="fondo_seccion">
	<label class="subtitulo"><fmt:message key="empresa" bundle="${lang}" /></label>
	</td>
	</span>
</tr>
<tr>
	  <td>
	  	<span id="nombreempresatxt" style="display: none;">
	  <label><fmt:message key="nombre.empresa" bundle="${lang}" />: </label>
	  </span>
	 
	  </td>
	<td>
		<span id="nombreempresashow" style="display: none;">
			<c:if test="${not empty showResultToView.disabled}">
			<input type="text" name="nombreempresa" class="ulsinfondo" disabled value="${conyugeTrabajo.nombreEmpresa}" 
			maxlength="20" size="20"/> 
			</c:if>
		    <c:if test="${empty showResultToView.disabled}">
			<input type="text" name="nombreempresa" class="entrada" value="${conyugeTrabajo.nombreEmpresa}" 
			maxlength="20" size="20"/> 
     	</c:if>		
			

				</span>
	</td>
	  <td>
	  	<span id="telefonoempresatxt" style="display: none;">
	  <label><fmt:message key="telefono" bundle="${lang}" />: </label>
	  </span>
	  </td>
	<td>
	 	<span id="telefonoempresashow" style="display: none;">
	 		<c:if test="${not empty showResultToView.disabled}">
	<input type="text" name="telefonoempresa"  id="telefonoempresa"
				value="${conyugeTrabajo.telefonoEmpresa}"  class="ulsinfondo" disabled
				maxlength="20" size="20"/> 		
	 		</c:if>
	 <c:if test="${empty showResultToView.disabled}">
	 <input type="text" name="telefonoempresa"  id="telefonoempresa"
				value="${conyugeTrabajo.telefonoEmpresa}"  class="entrada"
				maxlength="20" size="20"/> 		
	 </c:if>
				</span>
	</td>
</tr>


<tr>
	  <td>
	  	<span id="montoAporteEmptxt" style="display: none;">
	  <label><fmt:message key="pago.montocomplemento" bundle="${lang}" />: </label>
	  </span>
	 
	  </td>
	<td>
		<span id="montoAporteEmpshow" style="display: none;">
		<c:if test="${not empty showResultToView.disabled}">
			<input type="text" name="montoAporteEmp" class="ulsinfondo texto_der" id="montoAporteEmp"
			disabled
				value="${showResultToView.montoAporteEmp}"
				onblur="formatearCampo(this)" onfocus="this.value = ''" tabindex=""
				maxlength="90" size="10" />
		</c:if>
	<c:if test="${empty showResultToView.disabled}">
		<input type="text" name="montoAporteEmp" class="entrada texto_der" id="montoAporteEmp"
				value="${showResultToView.montoAporteEmp}"
				onblur="formatearCampo(this)" onfocus="this.value = ''" tabindex=""
				maxlength="90" size="10" />
	</c:if>

				</span>
	</td>
	 
</tr>
 









</tr>

 


<tr>
	<td colspan="6" class="fondo_seccion"><label class="subtitulo">Datos
			del Beneficiario</label></td>
</tr>
<tr>
	<td><label>C&oacute;digo:</label></td>
	<td><input type="text" name="cedulaBenef" class="texto_der"
		value="${showResultToView.cedulaFamiliar}" tabindex="20" id="20"
		maxlength="8" size="8" disabled="disabled" /></td>
	<td><label>Apellido:</label></td>
	<td><input type="text" name="apellidoBenef"
		value="${showResultToView.apellidoFamiliar}" tabindex="21" id="21"
		maxlength="30" size="30" disabled="disabled" /></td>
</tr>
<tr>
	<td><label>Nombre:</label></td>
	<td><input type="text" name="nombreBenef"
		value="${showResultToView.nombreFamiliar}" tabindex="22" id="22"
		maxlength="30" size="30" disabled="disabled" /></td>
	<td><label>Fecha de Nacimiento:</label></td>
	<td><input type="text" name="fechaNacimiento"
		value="${showResultToView.fechaNacimiento}" tabindex="23" id="23"
		maxlength="10" size="10" disabled="disabled" /></td>
</tr>
<tr>
	<td><label>Edad:</label></td>
	<td><input type="text" name="edad" class="texto_der" 
		value="${showResultToView.edad}" tabindex="24" id="edadBenfId" maxlength="2"
		size="2" disabled="disabled" /></td>
	<td><label>Estatus:</label></td>
	<td><input type="text" name="estatus"
		value="${showResultToView.estatus}" tabindex="24" id="24"
		maxlength="7" size="7" disabled="disabled" /></td>
</tr>