
<!-- Estos recaudos los encontramos en clase ListRecaudos metodo generateRecaudos y viene  -->
<!-- en un request.getAtribute por cada controlador -->

<table cellspacing="5" cellpadding="2" class="anchoTabla4">
	<tr>
		<td colspan="4" align="center">
			<div id="message">
				<c:if test="${!empty recaudos}">
										                          ${recaudos}
										                </c:if>
			</div>
		</td>
	</tr>
</table>