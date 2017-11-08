/**
 * Created by abrown3 on 12/15/16.
 */
(function () {
    'use strict';

    function fhir ($scope) {
        /* jshint validthis: true */
        var fhirCtrl = this;

        fhirCtrl.scope = $scope;
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('fhir', fhir);
    fhir.$inject = ['$scope'];
}());