/**
 * Created by abrown3 on 12/15/16.
 */

(function () {
    'use strict';

    function samples ($scope, $uibModal, gridCellTemplateFactory, hmlService, guidGenerator, usSpinnerService) {
        /* jshint validthis: true */
        var samplesCtrl = this,
            parentCtrl = $scope.hmlModalCtrl,
            deleteColumnTemplate = gridCellTemplateFactory.createRemoveCellTemplate(),
            collectionMethodColumnTemplate = gridCellTemplateFactory.createCollectionMethodCell();

        usSpinnerService.stop('index-spinner');
        samplesCtrl.selectedSample = parentCtrl.newModel;
        samplesCtrl.selectedSampleCount = 0;
        samplesCtrl.scope = $scope;
        samplesCtrl.hml = parentCtrl.hml;
        samplesCtrl.gridOptions = {
            data: samplesCtrl.hml.samples,
            enableSorting: true,
            showGridFooter: true,
            enableRowSelection: true,
            enableRowHeaderSelection: false,
            appScopeProvider: samplesCtrl,
            columnDefs: [
                { name: 'id', visible: false },
                { name: 'centerCode', displayName: 'Code:', maxWidth: 80 },
                { name: 'collectionMethods', displayName: 'Collection Methods:', enableColumnMenu: false, cellTemplate: collectionMethodColumnTemplate },
                { field: 'delete', displayName: 'Remove', maxWidth: 75, enableColumnMenu: false, cellTemplate: deleteColumnTemplate }
            ],
            multiSelect: false,
            modifierKeysToMultiSelect: false,
            onRegisterApi: function (gridApi) {
                samplesCtrl.gridApi = gridApi;

                samplesCtrl.gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                    samplesCtrl.selectedSample = row.entity;
                    samplesCtrl.selectedSampleCount = samplesCtrl.gridApi.selection.getSelectedCount();
                });
            }
        };

        $scope.$on('guided:hml:node:update', function () {
            stripTempSampleId(samplesCtrl.selectedSample.id);
            hmlService.updateHml(samplesCtrl.hml).then(function (result) {
                if (result) {
                    $scope.$emit('guided:hml:node:updated', result);
                }
            });
        });

        samplesCtrl.showCollectionMethodData = function (collectionMethod) {
            //TODO: display collection method that was selected
        };

        samplesCtrl.addSampleEntry = function () {
            usSpinnerService.spin('index-spinner');
            handleSampleUpdates(samplesCtrl.selectedSample, false);
            $uibModal.open({
                animation: true,
                templateUrl: 'views/guided/hml/samples/samples-add-edit.html',
                controller: 'samplesAddEdit',
                controllerAs: 'samplesAddEditCtrl',
                resolve: {
                    hmlModel: function () {
                        return samplesCtrl.hml;
                    },
                    sample: function () {
                        return samplesCtrl.selectedSample;
                    }
                }
            });
        };

        samplesCtrl.removeSample = function (sample) {
            handleSampleUpdates(sample, true);
        };

        function handleSampleUpdates(sample, isDelete) {
            if (sample === null) {
                return;
            }

            var index = getSampleIndex(sample);

            if (isDelete) {
                samplesCtrl.hml.samples.splice(index, 1);
                return;
            }

            if (index === -1) {
                samplesCtrl.hml.samples.push(sample);
                return;
            }

            samplesCtrl.hml.samples[index] = sample;
        }

        function getSampleIndex (sample) {
            var sampleId;

            if (sample.id) {
                sampleId = sample.id;
            } else {
                sampleId = sample;
            }

            for (var i = 0; i < samplesCtrl.hml.samples.length; i++) {
                if (samplesCtrl.hml.samples[i].id === sampleId) {
                    return i;
                }
            }

            return -1;
        }

        function stripTempSampleId(sampleId) {
            var isGuid = guidGenerator.validateGuid(sampleId);

            if (!isGuid) {
                return;
            }

            var index = getSampleIndex(sampleId);
            samplesCtrl.hml.samples[index].id = undefined;
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('samples', samples);
    samples.$inject = ['$scope', '$uibModal', 'gridCellTemplateFactory', 'hmlService', 'guidGenerator', 'usSpinnerService'];
}());