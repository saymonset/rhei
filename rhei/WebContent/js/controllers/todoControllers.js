 todoApp.controller("ToDoCtrl", function ($scope) {
            $scope.tipoEmpleadoIndividual=false;
        	$scope.cedulaEmpleado='';
        	$scope.onlyNumbers = /^\d+$/;
            $scope.todos = [
                                     { id: "OBR,OBC", place: "OBRERO", action: "OBRERO,OBRERO CONTRATADO", complete: false },
                                     { id: "EMP,CON,JUB,EJE,SOB", place: "EMPLEADO (EMPLEADO,CONTRATADO,JUBILADO,EJECUTIVO,SOBREVIVIENTE)", action: "EMPLEADO", complete: false } 
                                     ];

            $scope.hideTipoEmpl = function () {
                var count = 0;
                if (angular.isDefined(this.cedulaEmpleado)) {
                	count=this.cedulaEmpleado.length;
                }
                return count;
            }
            
          
            
        });


