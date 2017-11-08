/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function allelesAddEdit ($scope, appConfig, $uibModalInstance, allele, hmlModel, parentCollectionPropertyAllocation, usSpinnerService) {
        /* jshint validthis:true */
        var allelesAddEditCtrl = this;

        $scope.parentCtrl = allelesAddEditCtrl;
        usSpinnerService.stop('index-spinner');

        allelesAddEditCtrl.hml = hmlModel;
        allelesAddEditCtrl.scope = $scope;
        allelesAddEditCtrl.formSubmitted = false;
        allelesAddEditCtrl.parentCollectionPropertyAllocation = parentCollectionPropertyAllocation;
        allelesAddEditCtrl.sampleIndex = getSampleIndex(allelesAddEditCtrl.parentCollectionPropertyAllocation);
        allelesAddEditCtrl.allele = allele;

        allelesAddEditCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        allelesAddEditCtrl.close = function () {
            $uibModalInstance.close();
        };

        allelesAddEditCtrl.add = function (form) {
            allelesAddEditCtrl.formSubmitted = true;

            if (!form.$invalid) {
                allelesAddEditCtrl.formSubmitted = false;
                $uibModalInstance.close(allelesAddEditCtrl.allele);
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

    angular.module('hmlFhirAngularClientApp.controllers').controller('allelesAddEdit', allelesAddEdit);
    allelesAddEdit.$inject = ['$scope', 'appConfig', '$uibModalInstance', 'allele', 'hmlModel', 'parentCollectionPropertyAllocation', 'usSpinnerService'];
}());