/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function variant ($scope, $uibModal, objectModelFactory, usSpinnerService, modelUpdater, hmlService) {
        /* jshint validthis:true */
        var variantCtrl = this,
            parentCtrl = $scope.parentCtrl;

        usSpinnerService.stop('index-spinner');

        variantCtrl.scope = $scope;
        variantCtrl.hml = parentCtrl.hml;
        variantCtrl.sampleIndex = parentCtrl.sampleIndex;
        variantCtrl.parentCollectionPropertyAllocation = returnPropertyLocator();
        variantCtrl.variant = {};

        variantCtrl.addVariant = function () {
            usSpinnerService.spin('index-spinner');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/guided/hml/samples/typing/consensus-sequence/consensus-sequence-block/variant/variant-add-edit.html',
                controller: 'variantAddEdit',
                controllerAs: 'variantAddEditCtrl',
                resolve: {
                    variant: function () {
                        return objectModelFactory.getModelByName('Variant');
                    },
                    hmlModel: function () {
                        return variantCtrl.hml;
                    },
                    parentCollectionPropertyAllocation: function () {
                        return variantCtrl.parentCollectionPropertyAllocation;
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
                        updatedModel = modelUpdater.updateModel(variantCtrl.hml, propertyMap, result);

                    modelUpdater.removeTempIds(updatedModel);
                    hmlService.updateHml(updatedModel).then(function (hmlResult) {
                        variantCtrl.hml = hmlResult;
                        parentCtrl.hml = variantCtrl.hml;
                        usSpinnerService.stop('index-spinner');
                        variantCtrl.variant = modelUpdater.returnObjectFromHml(propertyMap, variantCtrl.hml);
                    });
                }
            });
        };

        function returnPropertyLocator () {
            return [
                { propertyString: 'samples', propertyIndex: variantCtrl.sampleIndex, isArray: true },
                { propertyString: 'typing', propertyIndex: -1, isArray: false },
                { propertyString: 'consensusSequence', propertyIndex: -1, isArray: false },
                { propertyString: 'consensusSequenceBlock', propertyIndex: -1, isArray: false },
                { propertyString: 'variant', propertyIndex: -1, isArray: false }
            ];
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('variant', variant);
    variant.$inject = ['$scope', '$uibModal', 'objectModelFactory', 'usSpinnerService', 'modelUpdater', 'hmlService'];
}());