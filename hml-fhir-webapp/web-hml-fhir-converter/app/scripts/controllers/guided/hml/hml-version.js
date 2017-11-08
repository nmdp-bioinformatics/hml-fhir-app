/**
 * Created by abrown3 on 12/13/16.
 */
(function () {
    'use strict';

    function hmlVersion ($scope, $uibModalInstance, version, versions) {
        /*jshint validthis: true */
        var hmlVersionCtrl = this;

        hmlVersionCtrl.scope = $scope;
        hmlVersionCtrl.versions = versions;
        hmlVersionCtrl.version = version;

        hmlVersionCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        hmlVersionCtrl.close = function () {
            $uibModalInstance.close();
        };

        hmlVersionCtrl.update = function () {
            $uibModalInstance.close(hmlVersionCtrl.version);
        };
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('hmlVersion', hmlVersion);
    hmlVersion.$inject = ['$scope', '$uibModalInstance', 'version', 'versions'];
}());