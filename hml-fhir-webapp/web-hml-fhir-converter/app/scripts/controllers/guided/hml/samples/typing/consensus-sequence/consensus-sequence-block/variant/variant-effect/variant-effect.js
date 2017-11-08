/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function variantEffect ($scope, $uibModal, objectModelFactory, usSpinnerService, modelUpdater, hmlService) {
        /* jshint validthis:true */
        var variantEffectCtrl = this,
            parentCtrl = $scope.parentCtrl;

        variantEffectCtrl.scope = $scope;
        variantEffectCtrl.hml = parentCtrl.hml;
        variantEffectCtrl.sampleIndex = parentCtrl.sampleIndex;
        variantEffectCtrl.parentCollectionPropertyAllocation = returnPropertyLocator();
        variantEffectCtrl.varientEffect = {};

        usSpinnerService.stop('index-spinner');

        variantEffectCtrl.addVariantEffect = function () {
            usSpinnerService.spin('index-spinner');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/guided/hml/samples/typing/consensus-sequence/consensus-sequence-block/variant/variant-effect/variant-effect-add-edit.html',
                controller: 'variantEffectAddEdit',
                controllerAs: 'variantEffectAddEditCtrl',
                resolve: {
                    variantEffect: function () {
                        return objectModelFactory.getModelByName('VariantEffect');
                    },
                    hmlModel: function () {
                        return variantEffectCtrl.hml;
                    },
                    parentCollectionPropertyAllocation: function () {
                        return variantEffectCtrl.parentCollectionPropertyAllocation;
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
                        updatedModel = modelUpdater.updateModel(variantEffectCtrl.hml, propertyMap, result);

                    modelUpdater.removeTempIds(updatedModel);
                    hmlService.updateHml(updatedModel).then(function (hmlResult) {
                        variantEffectCtrl.hml = hmlResult;
                        parentCtrl.hml = variantEffectCtrl.hml;
                        usSpinnerService.stop('index-spinner');
                        variantEffectCtrl.varientEffect = modelUpdater.returnObjectFromHml(propertyMap, variantEffectCtrl.hml);
                    });
                }
            })
        };

        function returnPropertyLocator () {
            return [
                { propertyString: 'samples', propertyIndex: variantEffectCtrl.sampleIndex, isArray: true },
                { propertyString: 'typing', propertyIndex: -1, isArray: false },
                { propertyString: 'consensusSequence', propertyIndex: -1, isArray: false },
                { propertyString: 'consensusSequenceBlock', propertyIndex: -1, isArray: false },
                { propertyString: 'variant', propertyIndex: -1, isArray: false },
                { propertyString: 'variantEffect', propertyIndex: -1, isArray: false }
            ];
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('variantEffect', variantEffect);
    variantEffect.$inject = ['$scope', '$uibModal', 'objectModelFactory', 'usSpinnerService', 'modelUpdater', 'hmlService'];
}());