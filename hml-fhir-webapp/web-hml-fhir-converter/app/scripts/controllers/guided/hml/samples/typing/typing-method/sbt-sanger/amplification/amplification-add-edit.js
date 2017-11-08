/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function amplificationAddEdit ($scope, appConfig, $uibModalInstance, amplification, hmlModel, parentCollectionPropertyAllocation, usSpinnerService) {
        /* jshint validthis:true */
        var amplificationAddEditCtrl = this;

        $scope.parentCtrl = amplificationAddEditCtrl;
        usSpinnerService.stop('index-spinner');

        amplificationAddEditCtrl.hml = hmlModel;
        amplificationAddEditCtrl.scope = $scope;
        amplificationAddEditCtrl.panelData = appConfig.amplificationPanels;
        amplificationAddEditCtrl.formSubmitted = false;
        amplificationAddEditCtrl.parentCollectionPropertyAllocation = parentCollectionPropertyAllocation;
        amplificationAddEditCtrl.sampleIndex = getSampleIndex(amplificationAddEditCtrl.parentCollectionPropertyAllocation);
        amplificationAddEditCtrl.amplification = amplification;
        amplificationAddEditCtrl.expandedPanels = {
            sequence: false
        };

        amplificationAddEditCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        amplificationAddEditCtrl.close = function () {
            $uibModalInstance.close();
        };

        amplificationAddEditCtrl.add = function (form) {
            amplificationAddEditCtrl.formSubmitted = true;

            if (!form.$invalid) {
                amplificationAddEditCtrl.formSubmitted = false;
                $uibModalInstance.close(amplificationAddEditCtrl.amplification);
            }
        };

        amplificationAddEditCtrl.togglePanel = function (panelName) {
            amplificationAddEditCtrl.expandedPanels[panelName] = !amplificationAddEditCtrl.expandedPanels[panelName];
        };

        function getSampleIndex (propertyMap) {
            var isEqual = function (item) {
                    return item.propertyString === 'samples';
                },
                index = R.findIndex(isEqual, propertyMap);

            return propertyMap[index].propertyIndex;
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('amplificationAddEdit', amplificationAddEdit);
    amplificationAddEdit.$inject = ['$scope', 'appConfig', '$uibModalInstance', 'amplification', 'hmlModel', 'parentCollectionPropertyAllocation', 'usSpinnerService'];
}());