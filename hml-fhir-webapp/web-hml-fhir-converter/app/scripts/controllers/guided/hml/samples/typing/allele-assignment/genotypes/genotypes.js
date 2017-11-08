/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function genotypes ($scope, $uibModal, objectModelFactory, usSpinnerService, modelUpdater, hmlService) {
        /* jshint validthis:true */
        var genotypesCtrl = this,
            parentCtrl = $scope.parentCtrl;

        genotypesCtrl.scope = $scope;
        genotypesCtrl.hml = parentCtrl.hml;
        genotypesCtrl.sampleIndex = parentCtrl.sampleIndex;
        genotypesCtrl.parentCollectionPropertyAllocation = returnPropertyLocator();
        genotypesCtrl.genotypes = [];

        usSpinnerService.stop('index-spinner');

        genotypesCtrl.addGenotype = function () {
            usSpinnerService.spin('index-spinner');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/guided/hml/samples/typing/allele-assignment/genotypes/genotypes-add-edit.html',
                controller: 'genotypesAddEdit',
                controllerAs: 'genotypesAddEditCtrl',
                resolve: {
                    genotype: function () {
                        return objectModelFactory.getModelByName('Genotype')
                    },
                    hmlModel: function () {
                        return genotypesCtrl.hml;
                    },
                    parentCollectionPropertyAllocation: function () {
                        return genotypesCtrl.parentCollectionPropertyAllocation;
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
                        updatedModel = modelUpdater.updateModel(genotypesCtrl.hml, propertyMap, result);

                    modelUpdater.removeTempIds(updatedModel);
                    hmlService.updateHml(updatedModel).then(function (hmlResult) {
                        genotypesCtrl.hml = hmlResult;
                        parentCtrl.hml = genotypesCtrl.hml;
                        usSpinnerService.stop('index-spinner');
                        genotypesCtrl.genotypes = modelUpdater.returnObjectFromHml(propertyMap, genotypesCtrl.hml);
                    });
                }
            });
        };

        function returnPropertyLocator () {
            return [
                { propertyString: 'samples', propertyIndex: genotypesCtrl.sampleIndex, isArray: true },
                { propertyString: 'typing', propertyIndex: -1, isArray: false },
                { propertyString: 'alleleAssignment', propertyIndex: -1, isArray: false },
                { propertyString: 'genotypes', propertyIndex: -1, isArray: true }
            ];
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('genotypes', genotypes);
    genotypes.$inject = ['$scope', '$uibModal', 'objectModelFactory', 'usSpinnerService', 'modelUpdater', 'hmlService'];
}());