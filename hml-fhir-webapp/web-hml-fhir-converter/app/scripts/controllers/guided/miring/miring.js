/**
 * Created by abrown3 on 12/15/16.
 */
(function () {
    'use strict';

    function miring ($scope) {
        /* jshint validthis: true */
        var miringCtrl = this;

        miringCtrl.scope = $scope;
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('miring', miring);
    miring.$inject = ['$scope'];
}());
