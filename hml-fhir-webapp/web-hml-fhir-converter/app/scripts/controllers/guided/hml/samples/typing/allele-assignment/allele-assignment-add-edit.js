/**
 * Created by abrown3 on 2/12/17.
 */
(function () {
    'use strict';

    function alleleAssignmentAddEdit ($scope, appConfig, $uibModalInstance, alleleAssignment, hmlModel, parentCollectionPropertyAllocation, usSpinnerService) {
        /* jshint validthis:true */
        var alleleAssignmentAddEditCtrl = this;

        $scope.parentCtrl = alleleAssignmentAddEditCtrl;
        usSpinnerService.stop('index-spinner');

        alleleAssignmentAddEditCtrl.scope = $scope;
        alleleAssignmentAddEditCtrl.hml = hmlModel;
        alleleAssignmentAddEditCtrl.panelData = appConfig.alleleAssignmentPanels;
        alleleAssignmentAddEditCtrl.formSubmitted = false;
        alleleAssignmentAddEditCtrl.parentCollectionPropertyAllocation = parentCollectionPropertyAllocation;
        alleleAssignmentAddEditCtrl.sampleIndex = getSampleIndex(alleleAssignmentAddEditCtrl.parentCollectionPropertyAllocation);
        alleleAssignmentAddEditCtrl.alleleAssignment = alleleAssignment;
        alleleAssignmentAddEditCtrl.expandedPanels = {
            properties: false,
            haploid: false,
            geotypeList: false,
            glstring: false
        };
        alleleAssignmentAddEditCtrl.dateOptions = { };
        alleleAssignmentAddEditCtrl.dateOpen = false;
        alleleAssignmentAddEditCtrl.dateFormat = 'dd-MMM-yyyy';

        alleleAssignmentAddEditCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        alleleAssignmentAddEditCtrl.close = function () {
            $uibModalInstance.close();
        };

        alleleAssignmentAddEditCtrl.add = function (form) {
            alleleAssignmentAddEditCtrl.formSubmitted = true;

            if (!form.$invalid) {
                alleleAssignmentAddEditCtrl.formSubmitted = false;
                $uibModalInstance.close(alleleAssignmentAddEditCtrl.alleleAssignment);
            }
        };

        alleleAssignmentAddEditCtrl.togglePanel = function (panelName) {
            alleleAssignmentAddEditCtrl.expandedPanels[panelName] = !alleleAssignmentAddEditCtrl.expandedPanels[panelName];
        };

        alleleAssignmentAddEditCtrl.openDatePicker = function () {
            alleleAssignmentAddEditCtrl.dateOpen = !alleleAssignmentAddEditCtrl.dateOpen;
        };

        function getSampleIndex (propertyMap) {
            var isEqual = function (item) {
                    return item.propertyString === 'samples';
                },
                index = R.findIndex(isEqual, propertyMap);

            return propertyMap[index].propertyIndex;
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('alleleAssignmentAddEdit', alleleAssignmentAddEdit);
    alleleAssignmentAddEdit.$inject = ['$scope', 'appConfig', '$uibModalInstance', 'alleleAssignment', 'hmlModel', 'parentCollectionPropertyAllocation', 'usSpinnerService'];
}());