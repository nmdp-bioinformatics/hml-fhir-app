/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function rawReadsAddEdit ($scope, $uibModalInstance, rawRead, hmlModel, parentCollectionPropertyAllocation, usSpinnerService) {
        /* jshint validthis:true */
        var rawReadsAddEditCtrl = this;

        $scope.parentCtrl = rawReadsAddEditCtrl;
        usSpinnerService.stop('index-spinner');

        rawReadsAddEditCtrl.scope = $scope;
        rawReadsAddEditCtrl.formSubmitted = false;
        rawReadsAddEditCtrl.rawRead = rawRead;
        rawReadsAddEditCtrl.hml = hmlModel;
        rawReadsAddEditCtrl.parentCollectionPropertyAllocation = parentCollectionPropertyAllocation;
        rawReadsAddEditCtrl.sampleIndex = getSampleIndex(rawReadsAddEditCtrl.parentCollectionPropertyAllocation);

        rawReadsAddEditCtrl.cancel = function () {
            rawReadsAddEditCtrl.dismiss();
        };

        rawReadsAddEditCtrl.close = function () {
            rawReadsAddEditCtrl.close();
        };

        rawReadsAddEditCtrl.add = function (form) {
            rawReadsAddEditCtrl.formSubmitted = true;

            if (!form.$invalid) {
                rawReadsAddEditCtrl.formSubmitted = false;
                $uibModalInstance.close(rawReadsAddEditCtrl.rawRead);
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

    angular.module('hmlFhirAngularClientApp.controllers').controller('rawReadsAddEdit', rawReadsAddEdit);
    rawReadsAddEdit.$inject = ['$scope', '$uibModalInstance', 'rawRead', 'hmlModel', 'parentCollectionPropertyAllocation', 'usSpinnerService'];
}());