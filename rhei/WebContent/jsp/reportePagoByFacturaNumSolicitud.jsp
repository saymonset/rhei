                                   <form name="frmReporteSolicitud" id="frmReporteSolicitud" action="/rhei/documentOpenRporte" method="post">

                                       <div style="text-align: center;">
										<fmt:message key="documentoDescargar" bundle="${lang}" /></br>
										<a href="#d"
												onclick="javascript: reportePagoByFacturaNumSolicitud(); return false;">
												<input type="image" src="imagenes/pdf.png" alt="download" />
										</a>
										</div>
	                                     <input type="hidden" name="periodoEscolarHidden"	value="" />
	                                     <input type="hidden" name="numSolicitudHidden"	value="" />
	                                     <input type="hidden" name="nuFacturaHidden"	value="" />
	                                     <input type="hidden" name="keyReport"	value="2" />
	                                   
										</form>
										