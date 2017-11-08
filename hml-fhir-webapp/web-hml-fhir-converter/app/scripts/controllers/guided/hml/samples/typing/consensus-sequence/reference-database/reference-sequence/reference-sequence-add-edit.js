/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function referenceSequenceAddEdit ($scope, $uibModalInstance, referenceSequence, hmlModel, parentCollectionPropertyAllocation, usSpinnerService) {
        /* jshint validthis:true */
        var referenceSequenceAddEditCtrl = this;

        $scope.parentCtrl = referenceSequenceAddEditCtrl;
        usSpinnerService.stop('index-spinner');

        referenceSequenceAddEditCtrl.scope = $scope;
        referenceSequenceAddEditCtrl.hml = hmlModel;
        referenceSequenceAddEditCtrl.formSubmitted = false;
        referenceSequenceAddEditCtrl.parentCollectionPropertyAllocation = parentCollectionPropertyAllocation;
        referenceSequenceAddEditCtrl.sampleIndex = getSampleIndex(referenceSequenceAddEditCtrl.parentCollectionPropertyAllocation);
        referenceSequenceAddEditCtrl.referenceSequence = referenceSequence;

        referenceSequenceAddEditCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        referenceSequenceAddEditCtrl.close = function () {
            $uibModalInstance.close();
        };

        referenceSequenceAddEditCtrl.add = function (form) {
            referenceSequenceAddEditCtrl.formSubmitted = true;

            if (!form.$invalid) {
                referenceSequenceAddEditCtrl.formSubmitted = false;
                $uibModalInstance.close(referenceSequenceAddEditCtrl.referenceSequence);
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

    angular.module('hmlFhirAngularClientApp.controllers').controller('referenceSequenceAddEdit', referenceSequenceAddEdit);
    referenceSequenceAddEdit.$inject = ['$scope', '$uibModalInstance', 'referenceSequence', 'hmlModel', 'parentCollectionPropertyAllocation', 'usSpinnerService'];
}());