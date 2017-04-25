 todoApp.controller("ToDoCtrl", function ($scope) {
            $scope.prueba='Hola mumndo';
            $scope.isMostrar=true;
            $scope.handleClick = function () {
            	$scope.isMostrar=false;
            }
            
        });


