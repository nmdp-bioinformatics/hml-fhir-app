'use strict';

/**
 * @ngdoc overview
 * @name hmlFhirAngularClientApp
 * @description
 * # hmlFhirAngularClientApp
 *
 * Main module of the application.
 */

angular.module('hmlFhirAngularClientApp.controllers', []);
angular.module('hmlFhirAngularClientApp.services', []);
angular.module('hmlFhirAngularClientApp.directives', []);
angular.module('hmlFhirAngularClientApp.factories', []);
angular.module('hmlFhirAngularClientApp.constants', []);

var app = angular.module('hmlFhirAngularClientApp', [
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'ui.bootstrap',
    'ui.bootstrap.datetimepicker',
    'hmlFhirAngularClientApp.controllers',
    'hmlFhirAngularClientApp.services',
    'hmlFhirAngularClientApp.directives',
    'hmlFhirAngularClientApp.factories',
    'hmlFhirAngularClientApp.constants',
    'ngMaterial',
    'ngAnimate',
    'toaster',
    'cb.x2js',
    'ui.grid',
    'ui.grid.selection',
    'angular-momentjs',
    'angularSpinner',
    'angular-json-tree'
  ]);

  app.config(function ($routeProvider, $locationProvider) {

    $routeProvider
      .when('/', {
          templateUrl: 'views/main.html',
          controller: 'main',
          controllerAs: 'mainCtrl'
      })
      .when('/batch', {
          templateUrl: '/views/file/batch.html',
          controller: 'batch',
          controllerAs: 'batchCtrl'
      })
      .when('/guided', {
          templateUrl: '/views/guided/guided-types.html',
          controller: 'guidedTypes',
          controllerAs: 'guidedTypesCtrl'
      })
      .when('/settings', {
          templateUrl: '/views/settings/settings.html',
          controller: 'settings',
          controllerAs: 'settingsCtrl'
      })
      .when('/settings/hml', {
          templateUrl: '/views/settings/hml/hml-settings.html',
          controller: 'hmlSettings',
          controllerAs: 'hmlSettingsCtrl'
      })
      .when('/settings/fhir', {
          templateUrl: '/views/settings/fhir/fhir-settings.html',
          controller: 'fhirSettings',
          controllerAs: 'fhirSettingsCtrl'
      })
      .when('/settings/miring', {
          templateUrl: '/views/settings/miring/miring-settings.html',
          controller: 'miringSettings',
          controllerAs: 'miringSettingsCtrl'
      })
      .when('/guided/hml', {
          templateUrl: '/views/guided/hml/hml.html',
          controller: 'hml',
          controllerAs: 'hmlCtrl',
          resolve: {
              hmlModel: function (objectModelFactory) {
                  return objectModelFactory.getModelByName('Hml');
              },
              defaultHmlVersion: function (versionService) {
                  return versionService.getDefaultVersion();
              },
              versions: function (versionService) {
                  return versionService.getAllVersions();
              }
          }
      })
      .when('/guided/hml/details/:edit/:hmlId', {
          templateUrl: '/views/guided/hml/hml-add-edit-main.html',
          controller: 'hmlAddEditMain',
          controllerAs: 'hmlAddEditMainCtrl',
          resolve: {
              hmlModel: function (hmlService, $route) {
                  return hmlService.getHml($route.current.params.hmlId);
              },
              edit: function ($route) {
                  return $route.current.params.edit === 'true';
              }
          }
      })
      .when('/guided/fhir', {
          templateUrl: '/views/guided/fhir/fhir.html',
          controller: 'fhir',
          controllerAs: 'fhirCtrl'
      })
      .when('/guided/miring', {
          templateUrl: '/views/guided/miring/miring.html',
          controller: 'miring',
          controllerAs: 'miringCtrl'
      })
      .when('/about', {
          templateUrl: 'views/about.html',
          controller: 'about',
          controllerAs: 'aboutCtrl'
      })
      .otherwise({ redirectTo: '/' });

    //$locationProvider.html5Mode(true);

  });
