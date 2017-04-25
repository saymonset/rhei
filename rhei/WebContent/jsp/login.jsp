<%@ page contentType="text/html; charset=iso-8859-1" session="true" language="java"%>
<%String aviso="";
if(request.getParameter("aviso")!=null) {
	aviso=request.getParameter("aviso");
}%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<!--<link rel="stylesheet" type="text/css" href="../css/default_login.css" />-->
<link rel="stylesheet" type="text/css" href="/rhei/css/estilo_login.css" />
<script type="text/javascript" language="JavaScript">
   	window.history.forward(1);
</script>
<title>BCV - Sistema de Gesti&oacute;n del Beneficio de Educaci&oacute;n Inicial     </title>
</head>
<body onload="document.form1.login.focus()">
<div class="centraTabla" > 
	<table cellpadding="0" cellspacing="0" class="anchoTabla">
		<tr>
			<td width="578" align="center" valign="top">
				<div align="center">
					
								<table cellpadding="0" cellspacing="0">
									<tr>
										<td colspan="2">
											<div class="centraImagen"> 
												<img class="bannerLogin" border="0" src="/rhei/imagenes/login.jpg" alt="Encabezado Login"/>
											</div>
										</td>
									</tr>
									<tr>
										<td colspan="2" bgcolor="#7B9ABD" height="18" width="100%" >&nbsp;</td>
									</tr>
									<tr>
										<td bgcolor="#CEDBE7" height="245">&nbsp;</td>
										<td bgcolor="#FFFFFF" height="245" valign="top" width="65%"> 
									    	<div class="titulo">
									    		Sistema de Gesti&oacute;n del Beneficio <br/> de Educaci&oacute;n Inicial
									    	</div>
									    	<form name="form1" method="post" action="/rhei/Autenticar">
											  	<table width="170" border="0" align="center">
													<tr>
												  		<td><div class="subtitulo">Login</div></td>
												  		<td><input type="text" name="login" size="10" /></td>
													</tr>
													<tr>
												  		<td><div class="subtitulo">Password</div></td>
												  		<td><input type="password" name="password" size="10" /></td>
													</tr>
													<tr>
														<td colspan="2">
															<div class="margen" >
																<!--<input type="image" src="/rhei/imagenes/entrar.jpg" />-->
																<input type="image" src="/rhei/imagenes/entrar.jpg" alt="Botón Entrar" />
															</div>
														</td>
													</tr>
												</table>
												<div id="centrado">
													<% if(String.valueOf(session.getAttribute("ErrorBlanco")).equals("1")){%>
															<div class="aviso">El login o password no pueden ser vacíos.<br/>Intente nuevamente</div>
													<%	session.setAttribute("ErrorBlanco","0");
														}
														else{
															if(String.valueOf(session.getAttribute("ValorErroneo")).equals("1")){%>
																<div class="aviso">No se ha encontrado un usuario con los valores ingresados.<br/>Por favor verifique su login y su password, e intente nuevamente</div>
													<%		session.setAttribute("ValorErroneo","0");
															}else{
																if(String.valueOf(session.getAttribute("UsuarioNoAutorizado")).equals("1")){%>
																<div class="aviso">Usted no está Autorizado a usar esta aplicación.</div>
													<%		session.setAttribute("UsuarioNoAutorizado","0");
															}
															}
														}
													%>
													<% if(aviso.compareTo("FinSesion")==0||String.valueOf(session.getAttribute("aviso")).equals("FinSesion")){%>
															<div class="aviso">Su sesión de trabajo ha expirado.<br/>Por favor, si desea continuar utilizando esta aplicación debe autenticarse nuevamente!!! </div>
													<%	session.setAttribute("aviso","");}
													%>
												</div>
									  		</form>
										</td>
									</tr>
									<tr>
										<td bgcolor="#7B9ABD" colspan="2" height="18" width="694" >&nbsp;</td>
									</tr>
									<tr>
									  	<td colspan="2" id="alto2">
											
										</td>
									</tr>
								</table>
							
				</div>
			</td>
		</tr>
	</table>
</div>
<jsp:include page="copyright.html" flush="true"/> 
</body>
</html>