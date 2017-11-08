/**
 * Created by abrown3 on 1/18/17.
 */
(function () {
    'use strict';

    function samplesAddEdit ($scope, $uibModalInstance, hmlModel, sample, appConfig, indexCollection, usSpinnerService) {
        /* jshint validthis: true */
        var samplesAddEditCtrl = this

        samplesAddEditCtrl.scope = $scope;
        samplesAddEditCtrl.hml = hmlModel;
        samplesAddEditCtrl.formSubmitted = false;
        samplesAddEditCtrl.selectedSample = sample;
        samplesAddEditCtrl.sampleIndex = getSampleIndex(samplesAddEditCtrl.selectedSample)
        samplesAddEditCtrl.parentCollectionPropertyAllocation = returnPropertyLocator();
        samplesAddEditCtrl.panelData = appConfig.samplePanels;
        samplesAddEditCtrl.expandedPanels = {
            collectionMethods: false,
            properties: false,
            typing: false
        };

        $scope.parentCtrl = samplesAddEditCtrl;
        usSpinnerService.stop('index-spinner');

        samplesAddEditCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        samplesAddEditCtrl.close = function () {
            $uibModalInstance.close();
        };

        samplesAddEditCtrl.add = function (form) {
            samplesAddEditCtrl.formSubmitted = true;

            if (!form.$invalid) {
                samplesAddEditCtrl.formSubmitted = false;
                $uibModalInstance.close(samplesAddEditCtrl.hml);
            }
        };

        samplesAddEditCtrl.togglePanel = function (panelName) {
            samplesAddEditCtrl.expandedPanels[panelName] = !samplesAddEditCtrl.expandedPanels[panelName];
        };

        function getSampleIndex(sample) {
            return indexCollection.getCollectionItemIndex(samplesAddEditCtrl.hml, 'samples', sample.id);
        }

        function returnPropertyLocator() {
            return setLocatorIndexes(appConfig.propertiesParentMap.samplesParent);
        }

        function setLocatorIndexes(config) {
            for (var i = 0; i < config.length; i++) {
                if (config[i].propertyString === 'samples') {
                    config[i].propertyIndex = samplesAddEditCtrl.sampleIndex;
                }
            }

            return config;
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('samplesAddEdit', samplesAddEdit);
    samplesAddEdit.$inject = ['$scope', '$uibModalInstance', 'hmlModel', 'sample', 'appConfig', 'indexCollection', 'usSpinnerService'];
}());