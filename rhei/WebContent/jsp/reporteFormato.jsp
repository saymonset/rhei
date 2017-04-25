                                   <form name="frmReporteFormato" id="frmReporteFormato" action="/rhei/openFileServlet" method="post">

                                       <div style="text-align: center;">
										<fmt:message key="documentoDescargar1" bundle="${lang}" />
												<a href="#d"
												onclick="javascript: generarReporteFormato(); return false;">
												<input type="image" src="imagenes/pdf.png" alt="download" />
											
										</a>
										
												 
										
										</div>
	                                  
	                                     <input type="hidden" name="cedulaHidden"	value="${showResultToView.cedula}" />
	                                     
										</form>
										
										
										