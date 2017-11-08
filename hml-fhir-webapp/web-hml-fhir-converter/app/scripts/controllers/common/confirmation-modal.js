/**
 * Created by abrown3 on 12/13/16.
 */
(function () {
    'use strict';

    function confirmationModal ($scope, $uibModalInstance, title, message) {
        /*jshint validthis: true */
        var confirmationModalCtrl = this;

        confirmationModalCtrl.scope = $scope;
        confirmationModalCtrl.title = title;
        confirmationModalCtrl.message = message;

        confirmationModalCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        confirmationModalCtrl.close = function () {
            $uibModalInstance.close(false);
        };

        confirmationModalCtrl.confirm = function () {
            $uibModalInstance.close(true);
        };
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('confirmationModal', confirmationModal);
    confirmationModal.$inject = ['$scope', '$uibModalInstance', 'title', 'message']
}());