/**
 * Created by abrown3 on 1/23/17.
 */
(function () {
    'use strict';

    function versionsTerminologyAddEditModal($scope, $uibModalInstance, version, title, edit, versionService) {
        /* jshint validthis: true */
        var versionsTerminologyAddEditModalCtrl = this;

        versionsTerminologyAddEditModalCtrl.scope = $scope;
        versionsTerminologyAddEditModalCtrl.version = version;
        versionsTerminologyAddEditModalCtrl.title = title;
        versionsTerminologyAddEditModalCtrl.formSubmitted = false;
        versionsTerminologyAddEditModalCtrl.edit = edit;

        versionsTerminologyAddEditModalCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        versionsTerminologyAddEditModalCtrl.close = function () {
            $uibModalInstance.close();
        };

        versionsTerminologyAddEditModalCtrl.save = function () {
            versionsTerminologyAddEditModalCtrl.formSubmitted = true;

            if (!versionsTerminologyAddEditModalCtrl.terminologyForm.$invalid) {
                if (versionsTerminologyAddEditModalCtrl.edit) {
                    versionService.updateSingleVersionTerminology(versionsTerminologyAddEditModalCtrl.version).then(function (result) {
                        if (result) {
                            versionsTerminologyAddEditModalCtrl.formSubmitted = false;
                            $uibModalInstance.close(result);
                        }
                    });
                } else {
                    versionService.addSingleVersionTerminology(versionsTerminologyAddEditModalCtrl.version).then(function (result) {
                        if (result) {
                            versionsTerminologyAddEditModalCtrl.formSubmitted = false;
                            $uibModalInstance.close(result);
                        }
                    });
                }
            }
        };
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('versionsTerminologyAddEditModal', versionsTerminologyAddEditModal);
    versionsTerminologyAddEditModal.$inject = ['$scope', '$uibModalInstance', 'version', 'title', 'edit', 'versionService'];
}());