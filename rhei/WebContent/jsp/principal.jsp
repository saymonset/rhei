<%@ page contentType="text/html; charset=iso-8859-1" session="true" language="java"  import="java.util.Date"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<%
    String autenticarAutomatic = session.getAttribute("autenticarAutomatic")!=null?session.getAttribute("autenticarAutomatic").toString().trim():"false"; 
	if (session.isNew() && !"true".equalsIgnoreCase(autenticarAutomatic)){%>
		<jsp:forward page="login.jsp">
			<jsp:param name="aviso" value="FinSesion"/>
		</jsp:forward>
 <% }%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Inicio</title>
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
	<fmt:setBundle basename="ve.org.bcv.rhei.util.bundle" var="lang" />
	<fmt:setLocale value="es" />
</head>
<body onload="actualizaReloj()">
<div class="centraTabla" >
    <jsp:include page="encabezado.jsp" flush="true"/>
    <div>
	    <table>
	    	<tr>
	    		<td>
					<jsp:include page="menu.jsp" flush="true"/>
				</td>
	    		<td id="centrado">
					<img class="foto" src="/rhei/imagenes/escuela.jpg" alt="Foto Escuela"/>
				</td>
			</tr>
		</table> 
	</div>
</div>
<jsp:include page="copyright.html" flush="true"/>
</body>
</html>