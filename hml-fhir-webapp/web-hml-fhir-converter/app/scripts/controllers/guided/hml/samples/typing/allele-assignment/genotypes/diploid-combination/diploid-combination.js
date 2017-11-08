/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function diploidCombination ($scope, $uibModal, objectModelFactory, usSpinnerService, modelUpdater, hmlService) {
        /* jshint validthis:true */
        var diploidCombinationCtrl = this,
            parentCtrl = $scope.parentCtrl;

        diploidCombinationCtrl.scope = $scope;
        diploidCombinationCtrl.hml = parentCtrl.hml;
        diploidCombinationCtrl.sampleIndex = parentCtrl.sampleIndex;
        diploidCombinationCtrl.parentCollectionPropertyAllocation = returnPropertyLocator();
        diploidCombinationCtrl.diploidCombination = {};

        usSpinnerService.stop('index-spinner');

        diploidCombinationCtrl.addDiploidCombination = function () {
            usSpinnerService.spin('index-spinner');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/guided/hml/samples/typing/allele-assignment/genotypes/diploid-combination/diploid-combination-add-edit.html',
                controller: 'diploidCombinationAddEdit',
                controllerAs: 'diploidCombinationAddEditCtrl',
                resolve: {
                    diploidCombination: function () {
                        return objectModelFactory.getModelByName('DiploidCombination')
                    },
                    hmlModel: function () {
                        return diploidCombinationCtrl.hml;
                    },
                    parentCollectionPropertyAllocation: function () {
                        return diploidCombinationCtrl.parentCollectionPropertyAllocation;
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
                        updatedModel = modelUpdater.updateModel(diploidCombinationCtrl.hml, propertyMap, result);

                    modelUpdater.removeTempIds(updatedModel);
                    hmlService.updateHml(updatedModel).then(function (hmlResult) {
                        diploidCombinationCtrl.hml = hmlResult;
                        parentCtrl.hml = diploidCombinationCtrl.hml;
                        usSpinnerService.stop('index-spinner');
                        diploidCombinationCtrl.diploidCombination = modelUpdater.returnObjectFromHml(propertyMap, diploidCombinationCtrl.hml);
                    });
                }
            });
        };

        function returnPropertyLocator () {
            return [
                { propertyString: 'samples', propertyIndex: diploidCombinationCtrl.sampleIndex, isArray: true },
                { propertyString: 'typing', propertyIndex: -1, isArray: false },
                { propertyString: 'alleleAssignment', propertyIndex: -1, isArray: false },
                { propertyString: 'genotypes', propertyIndex: -1, isArray: true },
                { propertyString: 'diploidCombination', propertyIndex: -1, isArray: false }
            ];
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('diploidCombination', diploidCombination);
    diploidCombination.$inject = ['$scope', '$uibModal', 'objectModelFactory', 'usSpinnerService', 'modelUpdater', 'hmlService'];
}());