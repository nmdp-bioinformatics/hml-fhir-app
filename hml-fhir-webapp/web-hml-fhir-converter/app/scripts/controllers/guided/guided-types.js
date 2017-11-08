/**
 * Created by abrown3 on 12/15/16.
 */
(function () {
   'use strict';

   function guidedTypes ($scope, $location) {
       /* jshintt validthis: true */
       var guidedTypesCtrl = this;

       guidedTypesCtrl.scope = $scope;

       guidedTypesCtrl.launchHml = function () {
           $location.path('/guided/hml');
       };

       guidedTypesCtrl.launchFhir = function () {
           $location.path('/guided/fhir');
       };

       guidedTypesCtrl.launchMiring = function () {
           $location.path('/guided/miring');
       };
   }

   angular.module('hmlFhirAngularClientApp.controllers').controller('guidedTypes', guidedTypes);
   guidedTypes.$inject = ['$scope', '$location'];
}());