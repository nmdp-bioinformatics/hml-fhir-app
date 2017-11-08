/**
 * Created by abrown3 on 12/15/16.
 */
(function () {
    'use strict';

    function collectionMethods ($scope, $uibModal, gridCellTemplateFactory, indexCollection) {
        /* jshint validthis: true */
        var collectionMethodsCtrl = this,
            parentCtrl = $scope.parentCtrl,
            deleteColumnTemplate = gridCellTemplateFactory.createRemoveCellTemplate();

        collectionMethodsCtrl.scope = $scope;
        collectionMethodsCtrl.sampleId = parentCtrl.selectedSample.id;
        collectionMethodsCtrl.hml = parentCtrl.hml;
        collectionMethodsCtrl.gridOptions = {
            data: getCollectionMethodsBySampleId(collectionMethodsCtrl.sampleId),
            enableSorting: true,
            showGridFooter: true,
            appScopeProvider: collectionMethodsCtrl,
            columnDefs: [
                { name: 'id', field: 'id', visible: false },
                { name: 'name', field: 'name', displayName: 'Name:', cellTooltip: function (row) { return row.entity.name; }, headerTooltip: function(col) { return col.displayName; } },
                { name: 'description', field: 'description', displayName: 'Descriptioin:', cellTooltip: function (row) { return row.entity.description; }, headerTooltip: function(col) { return col.displayName; } },
                { field: 'delete', displayName: 'Remove', maxWidth: 75, enableColumnMenu: false, cellTemplate: deleteColumnTemplate }
            ]
        };

        collectionMethodsCtrl.addCollectionMethodEntry = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/guided/hml/samples/collection-methods/collection-methods-add-edit.html',
                controller: 'collectionMethodsAddEdit',
                controllerAs: 'collectionMethodsAddEditCtrl',
                resolve: {
                    edit: function () {
                        return false;
                    },
                    collectionMethod: function () {
                        return undefined;
                    },
                    selectedCollectionMethods: function () {
                        var collectionMethods = collectionMethodsCtrl.gridOptions.data,
                            idArray = [];

                        for (var i = 0; i < collectionMethods.length; i++) {
                            idArray.push(collectionMethods[i].id)
                        }

                        return idArray;
                    }
                }
            });

            modalInstance.result.then(function (collectionMethods) {
                if (collectionMethods) {
                    if (collectionMethods.constructor === Array) {
                        for (var i = 0; i < collectionMethods.length; i++) {
                            updateHmlWithCollectionData(collectionMethods[i], collectionMethodsCtrl.sampleId, false);
                        }

                        return;
                    }

                    updateHmlWithCollectionData(collectionMethods, collectionMethodsCtrl.sampleId, false);
                }
            });
        };

        collectionMethodsCtrl.removeCollectionMethod = function (collectionMethod) {
            updateHmlWithCollectionData(collectionMethod, collectionMethodsCtrl.sampleId, true);
        };

        function updateHmlWithCollectionData(collectionMethod, sampleId, isDelete) {
            var sample = getSampleById(sampleId),
                collectionMethods = sample.collectionMethods,
                sampleIndex = getSampleIndex(sampleId),
                collectionMethodIndex = getCollectionMethodIndex(collectionMethod.id, collectionMethods);

            if (isDelete) {
                collectionMethodsCtrl.hml.samples[sampleIndex].collectionMethods.splice(collectionMethodIndex, 1);
                return;
            }

            if (collectionMethodIndex === -1) {
                collectionMethodsCtrl.hml.samples[sampleIndex].collectionMethods.push(collectionMethod);
                return;
            }

            collectionMethodsCtrl.hml.samples[sampleIndex].collectionMethods[collectionMethodIndex] = collectionMethod;
        }

        function getCollectionMethodsBySampleId(sampleId) {
            var sample = getSampleById(sampleId);
            return sample.collectionMethods;
        }

        function getCollectionMethodIndex(collectionMethodId, collectionMethods) {
            return indexCollection.getCollectionItemIndex(collectionMethods, undefined, collectionMethodId);
        }

        function getSampleIndex(sampleId) {
            return indexCollection.getCollectionItemIndex(collectionMethodsCtrl.hml, 'samples', sampleId);
        }

        function getSampleById(sampleId) {
            return indexCollection.getCollectionItemById(collectionMethodsCtrl.hml, 'samples', sampleId);
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('collectionMethods', collectionMethods);
    collectionMethods.$inject = ['$scope', '$uibModal', 'gridCellTemplateFactory', 'indexCollection'];
}());