/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function referenceDatabaseAddEdit ($scope, appConfig, $uibModalInstance, referenceDatabase, hmlModel, parentCollectionPropertyAllocation, usSpinnerService) {
        /* jshint validthis:true */
        var referenceDatabaseAddEditCtrl = this;

        $scope.parentCtrl = referenceDatabaseAddEditCtrl;
        usSpinnerService.stop('index-spinner');

        referenceDatabaseAddEditCtrl.scope = $scope;
        referenceDatabaseAddEditCtrl.hml = hmlModel;
        referenceDatabaseAddEditCtrl.panelData = appConfig.referenceDatabasePanels;
        referenceDatabaseAddEditCtrl.formSubmitted = false;
        referenceDatabaseAddEditCtrl.parentCollectionPropertyAllocation = parentCollectionPropertyAllocation;
        referenceDatabaseAddEditCtrl.sampleIndex = getSampleIndex(referenceDatabaseAddEditCtrl.parentCollectionPropertyAllocation);
        referenceDatabaseAddEditCtrl.referenceDatabase = referenceDatabase;
        referenceDatabaseAddEditCtrl.expandedPanels = {
            referenceSequence: false
        };

        referenceDatabaseAddEditCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        referenceDatabaseAddEditCtrl.close = function () {
            $uibModalInstance.close();
        };

        referenceDatabaseAddEditCtrl.add = function (form) {
            referenceDatabaseAddEditCtrl.formSubmitted = true;

            if (!form.$invalid) {
                referenceDatabaseAddEditCtrl.formSubmitted = false;
                $uibModalInstance.close(referenceDatabaseAddEditCtrl.referenceDatabase);
            }
        };

        referenceDatabaseAddEditCtrl.togglePanel = function (panelName) {
            referenceDatabaseAddEditCtrl.expandedPanels[panelName] = !referenceDatabaseAddEditCtrl.expandedPanels[panelName];
        };

        function getSampleIndex (propertyMap) {
            var isEqual = function (item) {
                    return item.propertyString === 'samples';
                },
                index = R.findIndex(isEqual, propertyMap);

            return propertyMap[index].propertyIndex;
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('referenceDatabaseAddEdit', referenceDatabaseAddEdit);
    referenceDatabaseAddEdit.$inject = ['$scope', 'appConfig', '$uibModalInstance', 'referenceDatabase', 'hmlModel', 'parentCollectionPropertyAllocation', 'usSpinnerService'];
}());