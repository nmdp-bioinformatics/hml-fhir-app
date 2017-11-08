/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function haploidAddEdit ($scope, $uibModalInstance, haploid, hmlModel, parentCollectionPropertyAllocation, usSpinnerService) {
        /* jshint validthis:true */
        var haploidAddEditCtrl = this;

        haploidAddEditCtrl.scope = $scope;
        usSpinnerService.stop('index-spinner');

        haploidAddEditCtrl.hml = hmlModel;
        haploidAddEditCtrl.scope = $scope;
        haploidAddEditCtrl.formSubmitted = false;
        haploidAddEditCtrl.parentCollectionPropertyAllocation = parentCollectionPropertyAllocation;
        haploidAddEditCtrl.sampleIndex = getSampleIndex(haploidAddEditCtrl.parentCollectionPropertyAllocation);
        haploidAddEditCtrl.haploid = haploid;

        haploidAddEditCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        haploidAddEditCtrl.close = function () {
            $uibModalInstance.close();
        };

        haploidAddEditCtrl.add = function (form) {
            haploidAddEditCtrl.formSubmitted = true;

            if (!form.$invalid) {
                haploidAddEditCtrl.formSubmitted = false;
                $uibModalInstance.close(haploidAddEditCtrl.haploid);
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

    angular.module('hmlFhirAngularClientApp.controllers').controller('haploidAddEdit', haploidAddEdit);
    haploidAddEdit.$inject = ['$scope', '$uibModalInstance', 'haploid', 'hmlModel', 'parentCollectionPropertyAllocation', 'usSpinnerService'];
}());