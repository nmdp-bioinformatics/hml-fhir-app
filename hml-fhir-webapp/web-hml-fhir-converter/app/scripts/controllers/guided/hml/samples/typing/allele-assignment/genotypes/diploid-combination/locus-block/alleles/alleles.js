/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function alleles ($scope, $uibModal, objectModelFactory, usSpinnerService, modelUpdater, hmlService) {
        /* jshint validthis:true */
        var allelesCtrl = this,
            parentCtrl = $scope.parentCtrl;

        allelesCtrl.scope = $scope;
        allelesCtrl.hml = parentCtrl.hml;
        allelesCtrl.sampleIndex = parentCtrl.sampleIndex;
        allelesCtrl.parentCollectionPropertyAllocation = returnPropertyLocator();
        allelesCtrl.alleles = [];

        usSpinnerService.stop('index-spinner');

        allelesCtrl.addAllele = function () {
            usSpinnerService.spin('index-spinner');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/guided/hml/samples/typing/allele-assignment/genotypes/diploid-combination/locus-block/alleles/alleles-add-edit.html',
                controller: 'allelesAddEdit',
                controllerAs: 'allelesAddEditCtrl',
                resolve: {
                    allele: function () {
                        return objectModelFactory.getModelByName('Allele')
                    },
                    hmlModel: function () {
                        return allelesCtrl.hml;
                    },
                    parentCollectionPropertyAllocation: function () {
                        return allelesCtrl.parentCollectionPropertyAllocation;
                    },
                    edit: function () {
                        return false;
                    }
                }
            });

            modalInstance.result.then(function (result) {
                if (result) {
                    usSpinnerService.spin('index-spinner');
                    var propertyMap = modelUpdater.convertPropertyMapToRamda(returnPropertyLocator()),
                        updatedModel = modelUpdater.updateModel(allelesCtrl.hml, propertyMap, result);

                    modelUpdater.removeTempIds(updatedModel);
                    hmlService.updateHml(updatedModel).then(function (hmlResult) {
                        allelesCtrl.hml = hmlResult;
                        parentCtrl.hml = allelesCtrl.hml;
                        usSpinnerService.stop('index-spinner');
                        allelesCtrl.alleles = modelUpdater.returnObjectFromHml(propertyMap, allelesCtrl.hml);
                    });
                }
            });
        };

        function returnPropertyLocator () {
            return [
                { propertyString: 'samples', propertyIndex: allelesCtrl.sampleIndex, isArray: true },
                { propertyString: 'typing', propertyIndex: -1, isArray: false },
                { propertyString: 'alleleAssignment', propertyIndex: -1, isArray: false },
                { propertyString: 'genotypes', propertyIndex: -1, isArray: true },
                { propertyString: 'diploidCombination', propertyIndex: -1, isArray: false },
                { propertyString: 'locusBlock', propertyIndex: -1, isArray: false },
                { propertyString: 'alleles', propertyIndex: -1, isArray: true }
            ];
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('alleles', alleles);
    alleles.$inject = ['$scope', '$uibModal', 'objectModelFactory', 'usSpinnerService', 'modelUpdater', 'hmlService'];
}());