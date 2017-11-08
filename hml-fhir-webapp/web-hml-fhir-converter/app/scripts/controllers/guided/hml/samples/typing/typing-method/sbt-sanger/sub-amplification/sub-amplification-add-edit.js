/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function subAmplificationAddEdit ($scope, appConfig, $uibModalInstance, subAmplification, hmlModel, parentCollectionPropertyAllocation, usSpinnerService) {
        /* jshint validthis:true */
        var subAmplificationAddEditCtrl = this;

        $scope.parentCtrl = subAmplificationAddEditCtrl;
        usSpinnerService.stop('index-spinner');

        subAmplificationAddEditCtrl.hml = hmlModel;
        subAmplificationAddEditCtrl.scope = $scope;
        subAmplificationAddEditCtrl.panelData = appConfig.subAmplificationPanels;
        subAmplificationAddEditCtrl.formSubmitted = false;
        subAmplificationAddEditCtrl.parentCollectionPropertyAllocation = parentCollectionPropertyAllocation;
        subAmplificationAddEditCtrl.sampleIndex = getSampleIndex(subAmplificationAddEditCtrl.parentCollectionPropertyAllocation);
        subAmplificationAddEditCtrl.subAmplification = subAmplification;
        subAmplificationAddEditCtrl.expandedPanels = {
            sequence: false
        };

        subAmplificationAddEditCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        subAmplificationAddEditCtrl.close = function () {
            $uibModalInstance.close();
        };

        subAmplificationAddEditCtrl.add = function (form) {
            subAmplificationAddEditCtrl.formSubmitted = true;

            if (!form.$invalid) {
                subAmplificationAddEditCtrl.formSubmitted = false;
                $uibModalInstance.close(subAmplificationAddEditCtrl.typing);
            }
        };

        subAmplificationAddEditCtrl.togglePanel = function (panelName) {
            subAmplificationAddEditCtrl.expandedPanels[panelName] = !subAmplificationAddEditCtrl.expandedPanels[panelName];
        };

        function getSampleIndex (propertyMap) {
            var isEqual = function (item) {
                    return item.propertyString === 'samples';
                },
                index = R.findIndex(isEqual, propertyMap);

            return propertyMap[index].propertyIndex;
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('subAmplificationAddEdit', subAmplificationAddEdit);
    subAmplificationAddEdit.$inject = ['$scope', 'appConfig', '$uibModalInstance', 'subAmplification', 'hmlModel', 'parentCollectionPropertyAllocation', 'usSpinnerService'];
}());