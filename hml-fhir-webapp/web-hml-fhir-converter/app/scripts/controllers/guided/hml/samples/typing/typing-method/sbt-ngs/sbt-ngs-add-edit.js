/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function sbtNgsAddEdit ($scope, appConfig, $uibModalInstance, sbtNgs, hmlModel, parentCollectionPropertyAllocation, usSpinnerService) {
        /* jshint validthis:true */
        var sbtNgsAddEditCtrl = this;

        $scope.parentCtrl = sbtNgsAddEditCtrl;
        usSpinnerService.stop('index-spinner');

        sbtNgsAddEditCtrl.scope = $scope;
        sbtNgsAddEditCtrl.hml = hmlModel;
        sbtNgsAddEditCtrl.panelData = appConfig.sbtNgsPanels;
        sbtNgsAddEditCtrl.formSubmitted = false;
        sbtNgsAddEditCtrl.parentCollectionPropertyAllocation = parentCollectionPropertyAllocation;
        sbtNgsAddEditCtrl.sampleIndex = getSampleIndex(sbtNgsAddEditCtrl.parentCollectionPropertyAllocation);
        sbtNgsAddEditCtrl.sbtNgs = sbtNgs;
        sbtNgsAddEditCtrl.expandedPanels = {
            properties: false,
            amplification: false,
            subAmplification: false
        };

        sbtNgsAddEditCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        sbtNgsAddEditCtrl.close = function () {
            $uibModalInstance.close();
        };

        sbtNgsAddEditCtrl.add = function (form) {
            sbtNgsAddEditCtrl.formSubmitted = true;

            if (!form.$invalid) {
                sbtNgsAddEditCtrl.formSubmitted = false;
                $uibModalInstance.close(sbtNgsAddEditCtrl.sbtNgs);
            }
        };

        sbtNgsAddEditCtrl.togglePanel = function (panelName) {
            sbtNgsAddEditCtrl.expandedPanels[panelName] = !sbtNgsAddEditCtrl.expandedPanels[panelName];
        };

        function getSampleIndex (propertyMap) {
            var isEqual = function (item) {
                    return item.propertyString === 'samples';
                },
                index = R.findIndex(isEqual, propertyMap);

            return propertyMap[index].propertyIndex;
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('sbtNgsAddEdit', sbtNgsAddEdit);
    sbtNgsAddEdit.$inject = ['$scope', 'appConfig', '$uibModalInstance', 'sbtNgs', 'hmlModel', 'parentCollectionPropertyAllocation', 'usSpinnerService'];
}());