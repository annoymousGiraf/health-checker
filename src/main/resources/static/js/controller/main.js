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


    $urlRouterProvider.otherwise('/');
    $stateProvider.state(homepage);

});