todoApp.constant("baseUrl",
		"NO SE USA MAS ESTA URL  CONSTANTE baseUrl !! //https://sigbe-cert.intra.bcv.org.ve/rhei")
//"http://172.24.24.98:8088/rhei")
//"http://localhost:8080/rhei")
//https://sigbe-cert.intra.bcv.org.ve/rhei
		.controller(
				"complementoValidateCtrl",
				function($scope, $http, $resource, baseUrl) {

					$scope.errorPagoMes = false;
					$scope.hayProductos = false;
				
					$scope.sumaTotalMeses = 0;
				
					$scope.productsResource = $resource(baseUrl + ":id", {
						id : "@id"
					}, {
						create : {
							method : "POST"
						},
						save : {
							method : "PUT"
						}
					});

					
					$scope.evaluarValidacion = function(nroSolicitud,
							mesComplementSelect, montoBCV,moMatricula,moPeriodo,urlBaseParam,periodoEscolar) {
						var count = 0;
						str = urlBaseParam.substring(urlBaseParam.length-1, urlBaseParam.length);
						var url=urlBaseParam;
						if ('/'===str){
							url=url+'services/validNuRefPagoServicio/';
						}else{
							url=url+'/services/validNuRefPagoServicio/';
						}
						$http.get(url	+ nroSolicitud + "/"+ mesComplementSelect + "/" + montoBCV+"/"+moMatricula+"/"+moPeriodo+"/"+periodoEscolar).then(function (response) {
		                    $scope.products = response.data;
		                    $scope.hayProductos = true;
		                    
		                });
						$scope.errorPagoMes = false;

					 
						
						return count;
					}

					$scope.warningLevel = function(isCanPayMes) {
						return isCanPayMes ? "label-success" : "label-danger";
					}

					/**Cuanto es lo maximo que podemos pagar*/
					$scope.cuantoApagar = function(acumuladorBCV,
							acumuladorByMes) {
						var result = 0;
						result = acumuladorBCV - acumuladorByMes;
						return result;
					}

					$scope.cerrarHayProductos = function() {
						$scope.hayProductos = false;
					}

					$scope.warningLevelMes = function() {
						return errorPagoMes ? "" : "label-danger";
					}

				 

					$scope.validarMes = function(bcvMonto, montoPago, valorDelMes,
							nameCampo) {
						var bcvMontoAux = parseFloat(bcvMonto);
						var montoPagoAux = parseFloat(montoPago);
						var valorDelMesAux = parseFloat(valorDelMes);

						$scope.errorPagoMes = false;

						if (!bcvMontoAux)
							bcvMontoAux = 0;
						if (!montoPagoAux)
							montoPagoAux = 0;
						if (!valorDelMesAux)
							valorDelMesAux = 0;
						var a = document.getElementById(nameCampo).value;
						var valorMes = parseFloat(a);
						if (!valorMes) {
							valorMes = 0;
						}
						 
						$scope.sumaTotalMeses = $scope.sumaTotalMeses
								+ valorMes;
						var total = 0;
						if (valorMes != 0 && valorDelMesAux != 0) {
							console.log(valorMes + "=a sumamos =" + valorDelMesAux);
							total = parseFloat(montoPagoAux)
									+ parseFloat(valorDelMesAux);
							valorDelMes = 0;

						}

						if (total > bcvMontoAux) {
							document.getElementById(nameCampo).value = '';

							$scope.errorPagoMes = true;
							$scope.errorMontoTotal = total;
						}

					}

				}).directive("unorderedList", function() {
			return function(scope, element, attrs) {
				var data = scope[attrs["unorderedList"]];
				var propertyName = attrs["listProperty"];

			}
		});