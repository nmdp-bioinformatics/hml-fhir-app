/**
 * Created by abrown3 on 1/18/17.
 */
(function () {
    'use strict';

    function collectionMethodsAddEdit ($scope, $uibModalInstance, $uibModal, collectionMethod, selectedCollectionMethods, collectionMethodService, appConfig, toaster, typeaheadQueryBuilder) {
        /* jshint validthis: true */
        var collectionMethodsAddEditCtrl = this;

        collectionMethodsAddEditCtrl.scope = $scope;
        collectionMethodsAddEditCtrl.formSubmitted = false;
        collectionMethodsAddEditCtrl.selectedCollectionMethodName = null;
        collectionMethodsAddEditCtrl.selectedCollectionMethodDescription = null;
        collectionMethodsAddEditCtrl.selectedCollectionMethod = collectionMethod
        collectionMethodsAddEditCtrl.maxQuery = { number: 10, text: '10' };
        collectionMethodsAddEditCtrl.pageNumber = 0;
        collectionMethodsAddEditCtrl.resultsPerPage = appConfig.resultsPerPage;
        collectionMethodsAddEditCtrl.autoAdd = appConfig.autoAddOnNoResults;

        $scope.$on('collectionMethodsAddEditCtrl.addedExternal.success', function (event, result) {
            $uibModalInstance.close(result);
        });

        collectionMethodsAddEditCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        collectionMethodsAddEditCtrl.close = function () {
            $uibModalInstance.close();
        };

        collectionMethodsAddEditCtrl.add = function (form) {
            collectionMethodsAddEditCtrl.formSubmitted = true;

            if (!form.$invalid) {
                collectionMethodsAddEditCtrl.formSubmitted = false;
                $uibModalInstance.close(collectionMethodsAddEditCtrl.selectedCollectionMethod);
            }
        };

        collectionMethodsAddEditCtrl.selectCollectionMethod = function (item) {
            collectionMethodsAddEditCtrl.selectedCollectionMethod = item;
            collectionMethodsAddEditCtrl.selectedCollectionMethodName = item.name;
            collectionMethodsAddEditCtrl.selectedCollectionMethodDescription = item.description;
        };

        collectionMethodsAddEditCtrl.getCollectionMethodsByName = function (viewValue) {
            return getCollectionMethods('name', viewValue);
        };

        collectionMethodsAddEditCtrl.getCollectionMethodsByDescription = function (viewValue) {
            return getCollectionMethods('description', viewValue);
        };

        collectionMethodsAddEditCtrl.collectionMethodChange = function () {
            collectionMethodsAddEditCtrl.selectedCollectionMethod = null;
        };

        function getCollectionMethods(term, viewValue) {
            return collectionMethodService.getTypeaheadOptions(collectionMethodsAddEditCtrl.maxQuery.number,
                typeaheadQueryBuilder.buildTypeaheadQueryWithSelectionExclusion(term, viewValue, false,
                    selectedCollectionMethods, 'id')).then(function (response) {
                if (response.length > 0) {
                    return response;
                }

                if (collectionMethodsAddEditCtrl.autoAdd) {
                    setTimeout(timeNoResults, appConfig.autoAddOnNoResultsTimer);
                }
            });
        }

        function createTypeAheadItemEntry() {
            var modalInstance = $uibModal.open({
                animation: true,
                controller: 'collectionMethodsTerminologyAddEditModal',
                controllerAs: 'collectionMethodsTerminologyAddEditModalCtrl',
                templateUrl: 'views/settings/hml/samples/collection-methods/terminology/collection-methods-terminology-add-edit-modal.html',
                resolve: {
                    title: function () {
                        return 'Add Collection Method Item';
                    },
                    collectionMethods: function () {
                        return generateCollectionMethods();
                    },
                    edit: function () {
                        return false;
                    }
                }
            });

            modalInstance.result.then(function (result) {
                if (result) {
                    toaster.pop({
                        type: 'info',
                        body: 'Successfully added Collection Method.'
                    });

                    $scope.$emit('collectionMethodsAddEditCtrl.addedExternal.success', result);
                }
            });
        }

        function generateCollectionMethods() {
            return {
                context: null,
                active: true,
                dateCreated: null,
                id: null
            };
        }

        function timeNoResults() {
            if (collectionMethodsAddEditCtrl.selectedTypingTest === null) {

                toaster.pop({
                    type: 'info',
                    title: 'Add / Edit Collection Method',
                    body: 'Not finding the data you need? Close this notification to be taken to add/edit page.',
                    toasterId: 1,
                    timeout: 0,
                    onHideCallback: function () {
                        createTypeAheadItemEntry();
                    }
                });
            }
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('collectionMethodsAddEdit', collectionMethodsAddEdit);
    collectionMethodsAddEdit.$inject = ['$scope', '$uibModalInstance', '$uibModal', 'collectionMethod', 'selectedCollectionMethods', 'collectionMethodService', 'appConfig', 'toaster', 'typeaheadQueryBuilder'];
}());