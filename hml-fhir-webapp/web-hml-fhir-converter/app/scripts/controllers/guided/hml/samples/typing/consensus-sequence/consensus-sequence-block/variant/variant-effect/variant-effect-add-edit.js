/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function variantEffectAddEdit ($scope, $uibModalInstance, variantEffect, hmlModel, parentCollectionPropertyAllocation, usSpinnerService) {
        /* jshint validthis:true */
        var variantEffectAddEditCtrl = this;

        $scope.parentCtrl = variantEffectAddEditCtrl;
        usSpinnerService.stop('index-spinner');

        variantEffectAddEditCtrl.scope = $scope;
        variantEffectAddEditCtrl.hml = hmlModel;
        variantEffectAddEditCtrl.formSubmitted = false;
        variantEffectAddEditCtrl.parentCollectionPropertyAllocation = parentCollectionPropertyAllocation;
        variantEffectAddEditCtrl.sampleIndex = getSampleIndex(variantEffectAddEditCtrl.parentCollectionPropertyAllocation);
        variantEffectAddEditCtrl.varientEffect = variantEffect;

        variantEffectAddEditCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        variantEffectAddEditCtrl.close = function () {
            $uibModalInstance.close();
        };

        variantEffectAddEditCtrl.add = function (form) {
            variantEffectAddEditCtrl.formSubmitted = true;

            if (!form.$invalid) {
                variantEffectAddEditCtrl.formSubmitted = false;
                $uibModalInstance.close(variantEffectAddEditCtrl.varientEffect);
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

    angular.module('hmlFhirAngularClientApp.controllers').controller('variantEffectAddEdit', variantEffectAddEdit);
    variantEffectAddEdit.$inject = ['$scope', '$uibModalInstance', 'variantEffect', 'hmlModel', 'parentCollectionPropertyAllocation', 'usSpinnerService'];
}());