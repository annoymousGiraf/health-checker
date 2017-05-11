/**
 * Created by Guy.Peleg0/2017.
 */


var healthCheck = angular.module('HealthCheckApp', ['ui.router']);


healthCheck.controller('mainController', ['$scope', function($scope) {
    $scope.state = "home";
    $scope.updateState = function (state) {
        $scope.state = state;
    }
}]);


healthCheck.controller('HealthCheckController', ['$scope','health_check_service', function($scope,health_check_service) {
    $scope.plugin_list =[ ] ;
    $scope.results = [];
    $scope.numberOfFailures =0;
    $scope.summary = "Health check hasn't run yet.";


    health_check_service.get_plugins_list().then(function(data)
    {
        $scope.plugin_list = data;
    });

    $scope.run_single_plugin = function (plugin_name) {
        $scope.numberOfFailures =0;
        $scope.run_plugin(plugin_name);
    }

    $scope.run_plugin = function (plugin_name) {
        var icon = "<span class='glyphicon glyphicon-ok-sign'></span>";
        var fieldNameElement = document.getElementById(plugin_name);
        if ($scope.summary === "Health check hasn't run yet."){
            $scope.summary = "";
        }
        fieldNameElement.innerHTML = '<img src="img/loading_icon.gif">';
       health_check_service.run_plugin(plugin_name).then(function (data) {
            var font = "green";
            if (data.status === "Failed"){
                font ="red";
                icon = "<span class='glyphicon glyphicon-remove-sign'></span>";
                document.getElementById(plugin_name+plugin_name+plugin_name).style.display = '';
                $scope.numberOfFailures = $scope.numberOfFailures + 1;

            }
            fieldNameElement.innerHTML = "<p style=\'color:"+font+";\'>"+ icon +"</p>";
           var result = document.getElementById(plugin_name+plugin_name);
           result.innerHTML = "<b>Details: </b>"+ data.details +"<br><b>  Solution Suggestion: </b>" +data.solution_suggestion;
         
        });

    }

    $scope.runAll = function () {
        $scope.numberOfFailures =0;
        angular.forEach($scope.plugin_list, function(value, key) {
          $scope.run_plugin(value.name);

        });
    }


}]);

healthCheck.controller('ManagePluginsController', ['$scope','health_check_service', function($scope,health_check_service) {
    $scope.plugin_list =[];
    health_check_service.get_plugins_list().then(function(data)
    {
        $scope.plugin_list = data;
    });


}]);



healthCheck.config(function($stateProvider,$urlRouterProvider) {

    var homepage = {
        name: 'home',
        url: '/',
        templateUrl: 'html/home.html',

    };

    var manage_plugins = {
        name: 'manage_plugins',
        url: '/manage_plugins',
        templateUrl: 'html/manage_plugins.html',

    };

    var health_check = {
        name: 'health_check',
        url: '/health_check',
        templateUrl: 'html/health-check.html',

    };

    $urlRouterProvider.otherwise('/');
    $stateProvider.state(homepage);
    $stateProvider.state(manage_plugins);
    $stateProvider.state(health_check);
});