/**
 * Created by abrown3 on 1/13/17.
 */
(function () {
    'use strict';

    function collectionMethodsTerminologyAddEditModal($scope, $uibModalInstance, collectionMethod, title, edit, collectionMethodService) {
        /* jshint validthis: true */
        var collectionMethodsTerminologyAddEditModalCtrl = this;

        collectionMethodsTerminologyAddEditModalCtrl.scope = $scope;
        collectionMethodsTerminologyAddEditModalCtrl.collectionMethod = collectionMethod;
        collectionMethodsTerminologyAddEditModalCtrl.title = title;
        collectionMethodsTerminologyAddEditModalCtrl.formSubmitted = false;
        collectionMethodsTerminologyAddEditModalCtrl.edit = edit;

        collectionMethodsTerminologyAddEditModalCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        collectionMethodsTerminologyAddEditModalCtrl.close = function () {
            $uibModalInstance.close();
        };

        collectionMethodsTerminologyAddEditModalCtrl.save = function () {
            collectionMethodsTerminologyAddEditModalCtrl.formSubmitted = true;

            if (!collectionMethodsTerminologyAddEditModalCtrl.terminologyForm.$invalid) {
                if (collectionMethodsTerminologyAddEditModalCtrl.edit) {
                    collectionMethodService.updateCollectionMethodTerminology(collectionMethodsTerminologyAddEditModalCtrl.collectionMethod).then(function (result) {
                        if (result) {
                            collectionMethodsTerminologyAddEditModalCtrl.formSubmitted = false;
                            $uibModalInstance.close(result);
                        }
                    });
                } else {
                    collectionMethodService.addSingleCollectionMethodTerminology(collectionMethodsTerminologyAddEditModalCtrl.collectionMethod).then(function (result) {
                        if (result) {
                            collectionMethodsTerminologyAddEditModalCtrl.formSubmitted = false;
                            $uibModalInstance.close(result);
                        }
                    });
                }
            }
        };
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('collectionMethodsTerminologyAddEditModal', collectionMethodsTerminologyAddEditModal);
    collectionMethodsTerminologyAddEditModal.$inject = ['$scope', '$uibModalInstance', 'collectionMethod', 'title', 'edit', 'collectionMethodService'];
}());