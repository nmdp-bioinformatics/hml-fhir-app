/**
 * Created by abrown3 on 1/4/17.
 */
(function () {
    'use strict';

    function settings ($scope, $location) {
        /* jshint validthis: true */
        var settingsCtrl = this;

        settingsCtrl.scope = $scope;

        settingsCtrl.launchHml = function () {
            $location.path('/settings/hml');
        };

        settingsCtrl.launchFhir = function () {
            $location.path('/settings/fhir');
        };

        settingsCtrl.launchMiring = function () {
            $location.path('/settings/miring');
        };
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('settings', settings);
    settings.$inject = ['$scope', '$location'];
}());