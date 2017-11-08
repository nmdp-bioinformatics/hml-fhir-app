/**
 * Created by abrown3 on 2/12/17.
 */
(function () {
    'use strict';

    function consensusSequenceAddEdit ($scope, appConfig, $uibModalInstance, consensusSequence, hmlModel, parentCollectionPropertyAllocation, usSpinnerService) {
        /* jshint validthis:true */
        var consensusSequenceAddEditCtrl = this;

        $scope.parentCtrl = consensusSequenceAddEditCtrl;
        usSpinnerService.stop('index-spinner');

        consensusSequenceAddEditCtrl.scope = $scope;
        consensusSequenceAddEditCtrl.hml = hmlModel;
        consensusSequenceAddEditCtrl.panelData = appConfig.consensusSequencePanels;
        consensusSequenceAddEditCtrl.formSubmitted = false;
        consensusSequenceAddEditCtrl.parentCollectionPropertyAllocation = parentCollectionPropertyAllocation;
        consensusSequenceAddEditCtrl.sampleIndex = getSampleIndex(consensusSequenceAddEditCtrl.parentCollectionPropertyAllocation);
        consensusSequenceAddEditCtrl.consensusSequence = consensusSequence;
        consensusSequenceAddEditCtrl.expandedPanels = {
            referenceDatabase: false,
            consensusSequenceBlock: false
        };
        consensusSequenceAddEditCtrl.dateOptions = { };
        consensusSequenceAddEditCtrl.dateOpen = false;
        consensusSequenceAddEditCtrl.dateFormat = 'dd-MMM-yyyy';

        consensusSequenceAddEditCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        consensusSequenceAddEditCtrl.close = function () {
            $uibModalInstance.close();
        };

        consensusSequenceAddEditCtrl.add = function (form) {
            consensusSequenceAddEditCtrl.formSubmitted = true;

            if (!form.$invalid) {
                consensusSequenceAddEditCtrl.formSubmitted = false;
                $uibModalInstance.close(consensusSequenceAddEditCtrl.consensusSequence);
            }
        };

        consensusSequenceAddEditCtrl.togglePanel = function (panelName) {
            consensusSequenceAddEditCtrl.expandedPanels[panelName] = !consensusSequenceAddEditCtrl.expandedPanels[panelName];
        };

        consensusSequenceAddEditCtrl.openDatePicker = function () {
            consensusSequenceAddEditCtrl.dateOpen = !consensusSequenceAddEditCtrl.dateOpen;
        };

        function getSampleIndex (propertyMap) {
            var isEqual = function (item) {
                    return item.propertyString === 'samples';
                },
                index = R.findIndex(isEqual, propertyMap);

            return propertyMap[index].propertyIndex;
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('consensusSequenceAddEdit', consensusSequenceAddEdit);
    consensusSequenceAddEdit.$inject = ['$scope', 'appConfig', '$uibModalInstance', 'consensusSequence', 'hmlModel', 'parentCollectionPropertyAllocation', 'usSpinnerService'];
}());