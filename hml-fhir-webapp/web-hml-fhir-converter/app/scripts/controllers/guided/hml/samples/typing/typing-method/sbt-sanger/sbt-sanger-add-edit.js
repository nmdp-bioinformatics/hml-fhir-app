/**
 * Created by abrown3 on 2/13/17.
 */
(function () {
    'use strict';

    function sbtSangerAddEdit ($scope, appConfig, $uibModalInstance, sbtSanger, hmlModel, parentCollectionPropertyAllocation, usSpinnerService) {
        /* jshint validthis:true */
        var sbtSangerAddEditCtrl = this;

        $scope.parentCtrl = sbtSangerAddEditCtrl;
        usSpinnerService.stop('index-spinner');

        sbtSangerAddEditCtrl.scope = $scope;
        sbtSangerAddEditCtrl.hml = hmlModel;
        sbtSangerAddEditCtrl.panelData = appConfig.sbtSangerPanels;
        sbtSangerAddEditCtrl.formSubmitted = false;
        sbtSangerAddEditCtrl.parentCollectionPropertyAllocation = parentCollectionPropertyAllocation;
        sbtSangerAddEditCtrl.sampleIndex = getSampleIndex(sbtSangerAddEditCtrl.parentCollectionPropertyAllocation);
        sbtSangerAddEditCtrl.sbtSanger = sbtSanger;
        sbtSangerAddEditCtrl.expandedPanels = {
            properties: false,
            amplification: false,
            subAmplification: false
        };

        sbtSangerAddEditCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        sbtSangerAddEditCtrl.close = function () {
            $uibModalInstance.close();
        };

        sbtSangerAddEditCtrl.add = function (form) {
            sbtSangerAddEditCtrl.formSubmitted = true;

            if (!form.$invalid) {
                sbtSangerAddEditCtrl.formSubmitted = false;
                $uibModalInstance.close(sbtSangerAddEditCtrl.sbtSanger);
            }
        };

        sbtSangerAddEditCtrl.togglePanel = function (panelName) {
            sbtSangerAddEditCtrl.expandedPanels[panelName] = !sbtSangerAddEditCtrl.expandedPanels[panelName];
        };

        function getSampleIndex (propertyMap) {
            var isEqual = function (item) {
                    return item.propertyString === 'samples';
                },
                index = R.findIndex(isEqual, propertyMap);

            return propertyMap[index].propertyIndex;
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('sbtSangerAddEdit', sbtSangerAddEdit);
    sbtSangerAddEdit.$inject = ['$scope', 'appConfig', '$uibModalInstance', 'sbtSanger', 'hmlModel', 'parentCollectionPropertyAllocation', 'usSpinnerService'];
}());