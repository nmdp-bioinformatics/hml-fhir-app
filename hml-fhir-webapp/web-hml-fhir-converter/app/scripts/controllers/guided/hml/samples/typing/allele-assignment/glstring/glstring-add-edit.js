/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function glstringAddEdit ($scope, $uibModalInstance, glstring, hmlModel, parentCollectionPropertyAllocation, usSpinnerService) {
        /* jshint validthis:true */
        var glstringAddEditCtrl = this;

        glstringAddEditCtrl.scope = $scope;
        usSpinnerService.stop('index-spinner');

        glstringAddEditCtrl.hml = hmlModel;
        glstringAddEditCtrl.scope = $scope;
        glstringAddEditCtrl.formSubmitted = false;
        glstringAddEditCtrl.parentCollectionPropertyAllocation = parentCollectionPropertyAllocation;
        glstringAddEditCtrl.sampleIndex = getSampleIndex(glstringAddEditCtrl.parentCollectionPropertyAllocation);
        glstringAddEditCtrl.glstring = glstring;

        glstringAddEditCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        glstringAddEditCtrl.close = function () {
            $uibModalInstance.close();
        };

        glstringAddEditCtrl.add = function () {
            $uibModalInstance.close(glstringAddEditCtrl.glstring);
        };

        function getSampleIndex (propertyMap) {
            var isEqual = function (item) {
                    return item.propertyString === 'samples';
                },
                index = R.findIndex(isEqual, propertyMap);

            return propertyMap[index].propertyIndex;
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('glstringAddEdit', glstringAddEdit);
    glstringAddEdit.$inject = ['$scope', '$uibModalInstance', 'glstring', 'hmlModel', 'parentCollectionPropertyAllocation', 'usSpinnerService'];
}());