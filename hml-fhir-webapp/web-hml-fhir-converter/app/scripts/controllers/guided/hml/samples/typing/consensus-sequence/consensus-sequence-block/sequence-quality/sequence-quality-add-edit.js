/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function sequenceQualityAddEdit ($scope, $uibModalInstance, sequenceQuality, hmlModel, parentCollectionPropertyAllocation, usSpinnerService) {
        /* jshint validthis:true */
        var sequenceQualityAddEditCtrl = this;

        $scope.parentCtrl = sequenceQualityAddEditCtrl;
        usSpinnerService.stop('index-spinner');

        sequenceQualityAddEditCtrl.scope = $scope;
        sequenceQualityAddEditCtrl.hml = hmlModel;
        sequenceQualityAddEditCtrl.formSubmitted = false;
        sequenceQualityAddEditCtrl.parentCollectionPropertyAllocation = parentCollectionPropertyAllocation;
        sequenceQualityAddEditCtrl.sampleIndex = getSampleIndex(sequenceQualityAddEditCtrl.parentCollectionPropertyAllocation);
        sequenceQualityAddEditCtrl.sequenceQuality = sequenceQuality;

        sequenceQualityAddEditCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        sequenceQualityAddEditCtrl.close = function () {
            $uibModalInstance.close();
        };

        sequenceQualityAddEditCtrl.add = function (form) {
            sequenceQualityAddEditCtrl.formSubmitted = true;

            if (!form.$invalid) {
                sequenceQualityAddEditCtrl.formSubmitted = false;
                $uibModalInstance.close(sequenceQualityAddEditCtrl.sequenceQuality);
            }
        };

        function getSampleIndex (propertyMap) {
            var isEqual = function (item) {
                    return item.propertyString === 'samples';
                },
                index = R.findIndex(isEqual, propertyMap);

            return propertyMap[index].propertyIndex;
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('sequenceQualityAddEdit', sequenceQualityAddEdit);
    sequenceQualityAddEdit.$inject = ['$scope', '$uibModalInstance', 'sequenceQuality', 'hmlModel', 'parentCollectionPropertyAllocation', 'usSpinnerService'];
}());