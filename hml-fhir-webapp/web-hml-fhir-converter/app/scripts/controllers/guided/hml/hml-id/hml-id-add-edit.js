/**
 * Created by abrown3 on 1/18/17.
 */
(function () {
    'use strict';

    function hmlIdAddEdit ($scope, $uibModalInstance, hmlModel, hmlIdService, appConfig, toaster, typeaheadQueryBuilder, hmlService) {
        /* jshint validthis: true */
        var hmlIdAddEditCtrl = this;

        hmlIdAddEditCtrl.scope = $scope;
        hmlIdAddEditCtrl.formSubmitted = false;
        hmlIdAddEditCtrl.maxQuery = { number: 10, text: '10' };
        hmlIdAddEditCtrl.pageNumber = 0;
        hmlIdAddEditCtrl.resultsPerPage = appConfig.resultsPerPage;
        hmlIdAddEditCtrl.autoAdd = appConfig.autoAddOnNoResults;
        hmlIdAddEditCtrl.hml = hmlModel;
        hmlIdAddEditCtrl.selectedHmlIdRootName = null;
        hmlIdAddEditCtrl.selectedHmlIdExtension = null;

        $scope.$on('hmlIdAddEditCtrl.addedExternal.success', function (event, result) {
            $uibModalInstance.close(result);
        });

        if (hmlIdAddEditCtrl.hml !== null) {
            if (hmlIdAddEditCtrl.hml.hmlId !== null) {
                hmlIdAddEditCtrl.selectedHmlIdRootName = hmlIdAddEditCtrl.hml.hmlId.rootName;
                hmlIdAddEditCtrl.selectedHmlIdExtension = hmlIdAddEditCtrl.hml.hmlId.extension;
            }
        }

        hmlIdAddEditCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        hmlIdAddEditCtrl.close = function () {
            $uibModalInstance.close();
        };

        hmlIdAddEditCtrl.save = function (form) {
            hmlIdAddEditCtrl.formSubmitted = true;

            if (!form.$dirty) {
                toaster.pop({
                    type: 'info',
                    body: 'No updates to form, returning.'
                });

                $uibModalInstance.close();
            }

            if (!form.$invalid) {
                if (dirtyObject(hmlIdAddEditCtrl.hml)) {
                    updateHmlId(hmlIdAddEditCtrl.hml);
                    hmlIdAddEditCtrl.formSubmitted = false;
                    hmlService.updateHml(hmlIdAddEditCtrl.hml).then(function (result) {
                        if (result) {
                            $uibModalInstance.close(result);
                        }
                    });
                } else {
                    toaster.pop({
                        type: 'info',
                        body: 'No updates to form, returning.'
                    });

                    $uibModalInstance.close();
                }
            }
        };

        hmlIdAddEditCtrl.selectHmlId = function (item) {
            hmlIdAddEditCtrl.hml.hmlId = item;
            hmlIdAddEditCtrl.selectedHmlIdRootName = hmlIdAddEditCtrl.hml.hmlId.rootName;
            hmlIdAddEditCtrl.selectedHmlIdExtension = hmlIdAddEditCtrl.hml.hmlId.extension;
        };

        hmlIdAddEditCtrl.getHmlIdsByRootName = function (viewValue) {
            return getTypeaheadByTerm('rootName', viewValue)
        };

        hmlIdAddEditCtrl.getHmlIdsByExtension = function (viewValue) {
            return getTypeaheadByTerm('extension', viewValue);
        };

        function updateHmlId(hml) {
            hml.hmlId.extension = hmlIdAddEditCtrl.selectedHmlIdExtension;
            hml.hmlId.rootName = hmlIdAddEditCtrl.selectedHmlIdRootName;
        }

        function dirtyObject(hml) {
            if (hml.hmlId.rootName !== hmlIdAddEditCtrl.selectedHmlIdRootName) {
                return true;
            }

            if (hml.hmlId.extension !== hmlIdAddEditCtrl.selectedHmlIdExtension) {
                return true;
            }

            return false;
        }

        function getTypeaheadByTerm(searchTerm, viewValue) {
            return hmlIdService.getTypeaheadOptions(hmlIdAddEditCtrl.maxQuery.number,
                typeaheadQueryBuilder.buildTypeaheadQueryWithSelectionExclusion(searchTerm, viewValue, false,
                    [], 'id')).then(function (response) {
                if (response.length > 0) {
                    return response;
                }
            });
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('hmlIdAddEdit', hmlIdAddEdit);
    hmlIdAddEdit.$inject = ['$scope', '$uibModalInstance', 'hmlModel', 'hmlIdService', 'appConfig', 'toaster', 'typeaheadQueryBuilder', 'hmlService'];
}());