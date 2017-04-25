<%
	String grupo = session.getAttribute("grupo").toString().trim(); 
%>
<div class="centraTabla"> 
<table cellpadding="0" cellspacing="0" class="color">
  <tr> 
    <td colspan="2">
    	<div class="centraImagen"> 
			<img class="banner" src="/rhei/imagenes/banner.gif" alt="Banner BCV"/>
      	</div>
    </td>
  </tr>
  <tr>
    <td id="Fecha_Reloj" class="fecha">
    </td>
  	<td class="bienvenida">
    	<span class="bienvenida" >Bienvenido (a) Sr (a) <%= session.getAttribute("nombreUsuario")%><br/>C&oacute;digo Compa&ntilde;&iacute;a: <%=session.getAttribute("companiaAnalista") %></span>
    </td>
  </tr>
</table>
</div>