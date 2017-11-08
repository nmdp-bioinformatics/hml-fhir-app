/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function genotypesAddEdit ($scope, appConfig, $uibModalInstance, genotype, hmlModel, parentCollectionPropertyAllocation, usSpinnerService) {
        /* jshint validthis:true */
        var genotypesAddEditCtrl = this;

        $scope.parentCtrl = genotypesAddEditCtrl;
        usSpinnerService.stop('index-spinner');

        genotypesAddEditCtrl.hml = hmlModel;
        genotypesAddEditCtrl.scope = $scope;
        genotypesAddEditCtrl.panelData = appConfig.genotypesPanels;
        genotypesAddEditCtrl.formSubmitted = false;
        genotypesAddEditCtrl.parentCollectionPropertyAllocation = parentCollectionPropertyAllocation;
        genotypesAddEditCtrl.sampleIndex = getSampleIndex(genotypesAddEditCtrl.parentCollectionPropertyAllocation);
        genotypesAddEditCtrl.genotype = genotype;
        genotypesAddEditCtrl.expandedPanels = {
            diploidCombination: false
        };

        genotypesAddEditCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        genotypesAddEditCtrl.close = function () {
            $uibModalInstance.close();
        };

        genotypesAddEditCtrl.add = function () {
            $uibModalInstance.close(genotypesAddEditCtrl.genotype);
        };

        genotypesAddEditCtrl.togglePanel = function (panelName) {
            genotypesAddEditCtrl.expandedPanels[panelName] = !genotypesAddEditCtrl.expandedPanels[panelName];
        };

        function getSampleIndex (propertyMap) {
            var isEqual = function (item) {
                    return item.propertyString === 'samples';
                },
                index = R.findIndex(isEqual, propertyMap);

            return propertyMap[index].propertyIndex;
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('genotypesAddEdit', genotypesAddEdit);
    genotypesAddEdit.$inject = ['$scope', 'appConfig', '$uibModalInstance', 'genotype', 'hmlModel', 'parentCollectionPropertyAllocation', 'usSpinnerService'];
}());