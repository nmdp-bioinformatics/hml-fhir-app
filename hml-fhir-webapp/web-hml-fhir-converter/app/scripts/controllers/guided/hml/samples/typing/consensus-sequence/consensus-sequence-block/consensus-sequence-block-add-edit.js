/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function consensusSequenceBlockAddEdit ($scope, appConfig, $uibModalInstance, consensusSequenceBlock, hmlModel, parentCollectionPropertyAllocation, usSpinnerService) {
        /* jshint validthis:true */
        var consensusSequenceBlockAddEditCtrl = this;

        $scope.parentCtrl = consensusSequenceBlockAddEditCtrl;
        usSpinnerService.stop('index-spinner');

        consensusSequenceBlockAddEditCtrl.scope = $scope;
        consensusSequenceBlockAddEditCtrl.hml = hmlModel;
        consensusSequenceBlockAddEditCtrl.panelData = appConfig.consensusSequenceBlockPanels;
        consensusSequenceBlockAddEditCtrl.formSubmitted = false;
        consensusSequenceBlockAddEditCtrl.parentCollectionPropertyAllocation = parentCollectionPropertyAllocation;
        consensusSequenceBlockAddEditCtrl.sampleIndex = getSampleIndex(consensusSequenceBlockAddEditCtrl.parentCollectionPropertyAllocation);
        consensusSequenceBlockAddEditCtrl.consensusSequenceBlock = consensusSequenceBlock;
        consensusSequenceBlockAddEditCtrl.expandedPanels = {
            sequence: false,
            variant: false,
            sequenceQuality: false
        };

        consensusSequenceBlockAddEditCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        consensusSequenceBlockAddEditCtrl.close = function () {
            $uibModalInstance.close();
        };

        consensusSequenceBlockAddEditCtrl.add = function (form) {
            consensusSequenceBlockAddEditCtrl.formSubmitted = true;

            if (!form.$invalid) {
                consensusSequenceBlockAddEditCtrl.formSubmitted = false;
                $uibModalInstance.close(consensusSequenceBlockAddEditCtrl.consensusSequenceBlock);
            }
        };

        consensusSequenceBlockAddEditCtrl.togglePanel = function (panelName) {
            consensusSequenceBlockAddEditCtrl.expandedPanels[panelName] = !consensusSequenceBlockAddEditCtrl.expandedPanels[panelName];
        };

        function getSampleIndex (propertyMap) {
            var isEqual = function (item) {
                    return item.propertyString === 'samples';
                },
                index = R.findIndex(isEqual, propertyMap);

            return propertyMap[index].propertyIndex;
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('consensusSequenceBlockAddEdit', consensusSequenceBlockAddEdit);
    consensusSequenceBlockAddEdit.$inject = ['$scope', 'appConfig', '$uibModalInstance', 'consensusSequenceBlock', 'hmlModel', 'parentCollectionPropertyAllocation', 'usSpinnerService'];
}());