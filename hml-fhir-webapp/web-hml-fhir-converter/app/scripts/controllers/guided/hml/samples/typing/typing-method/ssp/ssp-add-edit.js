/**
 * Created by abrown3 on 2/13/17.
 */
(function () {
    'use strict';

    function sspAddEdit ($scope, appConfig, $uibModalInstance, ssp, hmlModel, parentCollectionPropertyAllocation, usSpinnerService) {
        /* jshint validthis:true */
        var sspAddEditCtrl = this;

        $scope.parentCtrl = sspAddEditCtrl;
        usSpinnerService.stop('index-spinner');

        sspAddEditCtrl.scope = $scope;
        sspAddEditCtrl.hml = hmlModel;
        sspAddEditCtrl.panelData = appConfig.sspPanels;
        sspAddEditCtrl.formSubmitted = false;
        sspAddEditCtrl.parentCollectionPropertyAllocation = parentCollectionPropertyAllocation;
        sspAddEditCtrl.sampleIndex = getSampleIndex(sspAddEditCtrl.parentCollectionPropertyAllocation);
        sspAddEditCtrl.ssp = ssp;
        sspAddEditCtrl.expandedPanels = {
            properties: false
        };

        sspAddEditCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        sspAddEditCtrl.close = function () {
            $uibModalInstance.close();
        };

        sspAddEditCtrl.add = function (form) {
            sspAddEditCtrl.formSubmitted = true;

            if (!form.$invalid) {
                sspAddEditCtrl.formSubmitted = false;
                $uibModalInstance.close(sspAddEditCtrl.ssp);
            };
        };

        sspAddEditCtrl.togglePanel = function (panelName) {
            sspAddEditCtrl.expandedPanels[panelName] = !sspAddEditCtrl.expandedPanels[panelName];
        };

        function getSampleIndex (propertyMap) {
            var isEqual = function (item) {
                    return item.propertyString === 'samples';
                },
                index = R.findIndex(isEqual, propertyMap);

            return propertyMap[index].propertyIndex;
        }
    }


    angular.module('hmlFhirAngularClientApp.controllers').controller('sspAddEdit', sspAddEdit);
    sspAddEdit.$inject = ['$scope', 'appConfig', '$uibModalInstance', 'ssp', 'hmlModel', 'parentCollectionPropertyAllocation', 'usSpinnerService'];
}());