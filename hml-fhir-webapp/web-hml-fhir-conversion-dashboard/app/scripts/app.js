'use strict';

/**
 * @ngdoc overview
 * @name webHmlFhirConversionDashboardApp
 * @description
 * # webHmlFhirConversionDashboardApp
 *
 * Main module of the application.
 */

angular.module('webHmlFhirConversionDashboardApp.controllers', []);
angular.module('webHmlFhirConversionDashboardApp.services', []);
angular.module('webHmlFhirConversionDashboardApp.factories', []);
angular.module('webHmlFhirConversionDashboardApp.config', []);
angular.module('webHmlFhirConversionDashboardApp.directives', []);

angular
  .module('webHmlFhirConversionDashboardApp', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'webHmlFhirConversionDashboardApp.controllers',
    'webHmlFhirConversionDashboardApp.services',
    'webHmlFhirConversionDashboardApp.factories',
    'webHmlFhirConversionDashboardApp.config',
    'webHmlFhirConversionDashboardApp.directives'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/main', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl',
        controllerAs: 'main'
      })
      .when('/about', {
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl',
        controllerAs: 'about'
      })
      .when('/status', {
        templateUrl: 'views/status.html',
        controller: 'status',
        controllerAs: 'statusCtrl'
      })
      .otherwise({
        redirectTo: '/status'
      });
  });
