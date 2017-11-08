/**
 * Created by abrown3 on 2/13/17.
 */
(function () {
    'use strict';

    function ssoAddEdit ($scope, appConfig, $uibModalInstance, sso, hmlModel, parentCollectionPropertyAllocation, usSpinnerService) {
        /* jshint validthis:true */
        var ssoAddEditCtrl = this;

        $scope.parentCtrl = ssoAddEditCtrl;
        usSpinnerService.stop('index-spinner');

        ssoAddEditCtrl.scope = $scope;
        ssoAddEditCtrl.hml = hmlModel;
        ssoAddEditCtrl.panelData = appConfig.ssoPanels;
        ssoAddEditCtrl.formSubmitted = false;
        ssoAddEditCtrl.parentCollectionPropertyAllocation = parentCollectionPropertyAllocation;
        ssoAddEditCtrl.sampleIndex = getSampleIndex(ssoAddEditCtrl.parentCollectionPropertyAllocation);
        ssoAddEditCtrl.sso = sso;
        ssoAddEditCtrl.expandedPanels = {
            properties: false
        };

        ssoAddEditCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        ssoAddEditCtrl.close = function () {
            $uibModalInstance.close();
        };

        ssoAddEditCtrl.add = function (form) {
            ssoAddEditCtrl.formSubmitted = true;

            if (!form.$invalid) {
                ssoAddEditCtrl.formSubmitted = false;
                $uibModalInstance.close(ssoAddEditCtrl.sso);
            };
        };

        ssoAddEditCtrl.togglePanel = function (panelName) {
            ssoAddEditCtrl.expandedPanels[panelName] = !ssoAddEditCtrl.expandedPanels[panelName];
        };

        function getSampleIndex (propertyMap) {
            var isEqual = function (item) {
                    return item.propertyString === 'samples';
                },
                index = R.findIndex(isEqual, propertyMap);

            return propertyMap[index].propertyIndex;
        }
    }


    angular.module('hmlFhirAngularClientApp.controllers').controller('ssoAddEdit', ssoAddEdit);
    ssoAddEdit.$inject = ['$scope', 'appConfig', '$uibModalInstance', 'sso', 'hmlModel', 'parentCollectionPropertyAllocation', 'usSpinnerService'];
}());