(function () {
    'use strict';

    /**
     * @ngdoc function
     * @name hmlFhirAngularClientApp.controller:aboutCtrl
     * @description
     * # aboutCtrl
     * Controller of the hmlFhirAngularClientApp
     */

    function about ($scope) {
        /*jshint validthis: true */
        var aboutCtrl = this;

        aboutCtrl.scope = $scope;
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('about', about);
    about.$inject = ['$scope'];
}());

