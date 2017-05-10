/**
 * Created by Guy.Peleg on 5/10/2017.
 */


var healthCheck = angular.module('HealthCheckApp', ['ui.router','ngAnimate']);

healthCheck.config(function($stateProvider) {

    var homepage = {
        name: 'home',
        url: '/home',
        templateUrl: 'html/home.html'
    };




    $stateProvider.state(homepage);

});