/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function diploidCombinationAddEdit ($scope, appConfig, $uibModalInstance, diploidCombination, hmlModel, parentCollectionPropertyAllocation, usSpinnerService) {
        /* jshint validthis:true */
        var diploidCombinationAddEditCtrl = this;

        $scope.parentCtrl = diploidCombinationAddEditCtrl;
        usSpinnerService.stop('index-spinner');

        diploidCombinationAddEditCtrl.hml = hmlModel;
        diploidCombinationAddEditCtrl.scope = $scope;
        diploidCombinationAddEditCtrl.panelData = appConfig.diploidCombinationPanels;
        diploidCombinationAddEditCtrl.formSubmitted = false;
        diploidCombinationAddEditCtrl.parentCollectionPropertyAllocation = parentCollectionPropertyAllocation;
        diploidCombinationAddEditCtrl.sampleIndex = getSampleIndex(diploidCombinationAddEditCtrl.parentCollectionPropertyAllocation);
        diploidCombinationAddEditCtrl.diploidCombination = diploidCombination;
        diploidCombinationAddEditCtrl.expandedPanels = {
            locusBlock: false
        };

        diploidCombinationAddEditCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        diploidCombinationAddEditCtrl.close = function () {
            $uibModalInstance.close();
        };

        diploidCombinationAddEditCtrl.add = function () {
            $uibModalInstance.close(diploidCombinationAddEditCtrl.diploidCombination);
        };

        diploidCombinationAddEditCtrl.togglePanel = function (panelName) {
            diploidCombinationAddEditCtrl.expandedPanels[panelName] = !diploidCombinationAddEditCtrl.expandedPanels[panelName];
        };

        function getSampleIndex (propertyMap) {
            var isEqual = function (item) {
                    return item.propertyString === 'samples';
                },
                index = R.findIndex(isEqual, propertyMap);

            return propertyMap[index].propertyIndex;
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('diploidCombinationAddEdit', diploidCombinationAddEdit);
    diploidCombinationAddEdit.$inject = ['$scope', 'appConfig', '$uibModalInstance', 'diploidCombination', 'hmlModel', 'parentCollectionPropertyAllocation', 'usSpinnerService'];
}());