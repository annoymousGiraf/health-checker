/**
 * Created by Guy.Peleg on 5/10/2017.
 */


var healthCheck = angular.module('HealthCheckApp', ['ui.router']);




healthCheck.config(function($stateProvider,$urlRouterProvider) {

    var homepage = {
        name: 'home',
        url: '/',
        templateUrl: 'html/home.html'
    };
    var manage_plugins = {
        name: 'manage_plugins',
        url: '/',
        templateUrl: 'html/manage_plugins.html'
    };


    $urlRouterProvider.otherwise('/');
    $stateProvider.state(homepage);
    $stateProvider.state(manage_plugins);

});