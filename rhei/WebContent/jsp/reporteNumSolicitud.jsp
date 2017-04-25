                                   <form name="frmReporteSolicitud" id="frmReporteSolicitud" action="/rhei/reporteNumSolicitudServlet" method="post">

                                       <div style="text-align: center;">
										<fmt:message key="documentoDescargar" bundle="${lang}" />
												<a href="#d"
												onclick="javascript: generarReporteNumSolicitud(); return false;">
												<input type="image" src="imagenes/pdf.png" alt="download" />
											
										</a>
										
										</div>
	                                     <input type="hidden" name="periodoEscolarHidden"	value="${showResultToView.periodoEscolar}" />
	                                     <input type="hidden" name="nroRifCentroEduHidden"	value="${showResultToView.nroRifCentroEdu}" />
	                                     <input type="hidden" name="cedulaHidden"	value="${showResultToView.cedula}" />
	                                     <input type="hidden" name="codigoBenefHidden"	value="${showResultToView.cedBenef}" />
	                                     <input type="hidden" name="keyReport"	value="1" />
											<input type="hidden" name="accion"
												value="solicitudConsultar.jsp" />
										</form>
										
										
										
										
										