


<tr>
	<td colspan="4" class="fondo_seccion"><label class="subtitulo">Registro
			de Pago </label></td>
</tr>

 

<tr>
  <table class="table table-striped table-bordered">
        <thead>
            <tr>
                <th>Numero</th>
                <th>Observaciones</th>
                <th class="text-right">Monto</th>
                <th></th>
            </tr>
        </thead>
        <tbody>
            <tr ng-repeat="item in products">
                <td>{{item.nroFactura}}</td>
                <td>{{item.txObservaciones}}</td>
                <td class="text-right">{{item.montoFactura}}</td>
                <td class="text-center">
                    <button class="btn btn-xs btn-primary"
                            ng-click="deleteProduct(item)">
                        Delete
                    </button>
                    <button class="btn btn-xs btn-primary"
                            ng-click="editOrCreateProduct(item)">
                        Edit
                    </button>
                    <increment item="item" property-name="montoFactura" restful="true"
                               method-name="$save" />
                                
                </td>
            
            </tr>
            
        </tbody>
    </table>

	 
</tr>

 