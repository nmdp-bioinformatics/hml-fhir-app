/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function variantAddEdit ($scope, appConfig, $uibModalInstance, variant, hmlModel, parentCollectionPropertyAllocation, usSpinnerService) {
        /* jshint validthis:true */
        var variantAddEditCtrl = this;

        $scope.parentCtrl = variantAddEditCtrl;
        usSpinnerService.stop('index-spinner');

        variantAddEditCtrl.scope = $scope;
        variantAddEditCtrl.hml = hmlModel;
        variantAddEditCtrl.panelData = appConfig.variantPanels;
        variantAddEditCtrl.formSubmitted = false;
        variantAddEditCtrl.parentCollectionPropertyAllocation = parentCollectionPropertyAllocation;
        variantAddEditCtrl.sampleIndex = getSampleIndex(variantAddEditCtrl.parentCollectionPropertyAllocation);
        variantAddEditCtrl.variant = variant;
        variantAddEditCtrl.expandedPanels = {
            variantEffect: false
        };

        variantAddEditCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        variantAddEditCtrl.close = function () {
            $uibModalInstance.close();
        };

        variantAddEditCtrl.add = function (form) {
            variantAddEditCtrl.formSubmitted = true;

            if (!form.$invalid) {
                variantAddEditCtrl.formSubmitted = false;
                $uibModalInstance.close(variantAddEditCtrl.variant);
            }
        };

        variantAddEditCtrl.togglePanel = function (panelName) {
            variantAddEditCtrl.expandedPanels[panelName] = !variantAddEditCtrl.expandedPanels[panelName];
        };

        function getSampleIndex (propertyMap) {
            var isEqual = function (item) {
                    return item.propertyString === 'samples';
                },
                index = R.findIndex(isEqual, propertyMap);

            return propertyMap[index].propertyIndex;
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('variantAddEdit', variantAddEdit);
    variantAddEdit.$inject = ['$scope', 'appConfig', '$uibModalInstance', 'variant', 'hmlModel', 'parentCollectionPropertyAllocation', 'usSpinnerService'];
}());