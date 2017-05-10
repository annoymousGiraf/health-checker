/**
 * Created by Guy.Peleg on 5/10/2017.
 */


var healthCheck = angular.module('HealthCheckApp', ['ui.router']);


healthCheck.controller('mainController', ['$scope', function($scope) {
    $scope.state = "home";
    $scope.updateState = function (state) {
        $scope.state = state;
    }
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
        name: 'health-check',
        url: '/health-check',
        templateUrl: 'html/health-check.html',

    };

    $urlRouterProvider.otherwise('/');
    $stateProvider.state(homepage);
    $stateProvider.state(manage_plugins);
    $stateProvider.state(health_check);
});