/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function gsspAddEdit ($scope, appConfig, $uibModalInstance, gssp, hmlModel, parentCollectionPropertyAllocation, usSpinnerService) {
        /* jshint validthis:true */
        var gsspAddEditCtrl = this;

        $scope.parentCtrl = gsspAddEditCtrl;
        usSpinnerService.stop('index-spinner');

        gsspAddEditCtrl.hml = hmlModel;
        gsspAddEditCtrl.scope = $scope;
        gsspAddEditCtrl.panelData = appConfig.gsspPanels;
        gsspAddEditCtrl.formSubmitted = false;
        gsspAddEditCtrl.parentCollectionPropertyAllocation = parentCollectionPropertyAllocation;
        gsspAddEditCtrl.sampleIndex = getSampleIndex(gsspAddEditCtrl.parentCollectionPropertyAllocation);
        gsspAddEditCtrl.gssp = gssp;
        gsspAddEditCtrl.expandedPanels = {
            sequence: false
        };

        gsspAddEditCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        gsspAddEditCtrl.close = function () {
            $uibModalInstance.close();
        };

        gsspAddEditCtrl.add = function (form) {
            gsspAddEditCtrl.formSubmitted = true;

            if (!form.$invalid) {
                gsspAddEditCtrl.formSubmitted = false;
                $uibModalInstance.close(gsspAddEditCtrl.gsspAddEdit());
            }
        };

        gsspAddEditCtrl.togglePanel = function (panelName) {
            gsspAddEditCtrl.expandedPanels[panelName] = !gsspAddEditCtrl.expandedPanels[panelName];
        };

        function getSampleIndex (propertyMap) {
            var isEqual = function (item) {
                    return item.propertyString === 'samples';
                },
                index = R.findIndex(isEqual, propertyMap);

            return propertyMap[index].propertyIndex;
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('gsspAddEdit', gsspAddEdit);
    gsspAddEdit.$inject = ['$scope', 'appConfig', '$uibModalInstance', 'gssp', 'hmlModel', 'parentCollectionPropertyAllocation', 'usSpinnerService'];
}());