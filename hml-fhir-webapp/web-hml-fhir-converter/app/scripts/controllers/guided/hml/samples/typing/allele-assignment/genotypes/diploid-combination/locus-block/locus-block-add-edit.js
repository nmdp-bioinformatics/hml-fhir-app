/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function locusBlockAddEdit ($scope, appConfig, $uibModalInstance, locusBlock, hmlModel, parentCollectionPropertyAllocation, usSpinnerService) {
        /* jshint validthis:true */
        var locusBlockAddEditCtrl = this;

        $scope.parentCtrl = locusBlockAddEditCtrl;
        usSpinnerService.stop('index-spinner');

        locusBlockAddEditCtrl.hml = hmlModel;
        locusBlockAddEditCtrl.scope = $scope;
        locusBlockAddEditCtrl.panelData = appConfig.locusBlockPanels;
        locusBlockAddEditCtrl.formSubmitted = false;
        locusBlockAddEditCtrl.parentCollectionPropertyAllocation = parentCollectionPropertyAllocation;
        locusBlockAddEditCtrl.sampleIndex = getSampleIndex(locusBlockAddEditCtrl.parentCollectionPropertyAllocation);
        locusBlockAddEditCtrl.locusBlock = locusBlock;
        locusBlockAddEditCtrl.expandedPanels = {
            alleles: false
        };

        locusBlockAddEditCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        locusBlockAddEditCtrl.close = function () {
            $uibModalInstance.close();
        };

        locusBlockAddEditCtrl.add = function () {
            $uibModalInstance.close(locusBlockAddEditCtrl.locusBlock);
        };

        locusBlockAddEditCtrl.togglePanel = function (panelName) {
            locusBlockAddEditCtrl.expandedPanels[panelName] = !locusBlockAddEditCtrl.expandedPanels[panelName];
        };

        function getSampleIndex (propertyMap) {
            var isEqual = function (item) {
                    return item.propertyString === 'samples';
                },
                index = R.findIndex(isEqual, propertyMap);

            return propertyMap[index].propertyIndex;
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('locusBlockAddEdit', locusBlockAddEdit);
    locusBlockAddEdit.$inject = ['$scope', 'appConfig', '$uibModalInstance', 'locusBlock', 'hmlModel', 'parentCollectionPropertyAllocation', 'usSpinnerService'];
}());