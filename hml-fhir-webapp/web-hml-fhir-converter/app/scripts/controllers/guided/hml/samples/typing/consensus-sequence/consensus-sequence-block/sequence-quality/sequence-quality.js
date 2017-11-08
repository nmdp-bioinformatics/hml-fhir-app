/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function sequenceQuality ($scope, $uibModal, objectModelFactory, usSpinnerService, modelUpdater, hmlService) {
        /* jshint validthis:true */
        var sequenceQualityCtrl = this,
            parentCtrl = $scope.parentCtrl;

        usSpinnerService.stop('index-spinner');

        sequenceQualityCtrl.scope = $scope;
        sequenceQualityCtrl.hml = parentCtrl.hml;
        sequenceQualityCtrl.sampleIndex = parentCtrl.sampleIndex;
        sequenceQualityCtrl.parentCollectionPropertyAllocation = returnPropertyLocator();
        sequenceQualityCtrl.sequenceQuality = {};

        sequenceQualityCtrl.addSequenceQuality = function () {
            usSpinnerService.spin('index-spinner');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/guided/hml/samples/typing/consensus-sequence/consensus-sequence-block/sequence-quality/sequence-quality-add-edit.html',
                controller: 'sequenceQualityAddEdit',
                controllerAs: 'sequenceQualityAddEditCtrl',
                resolve: {
                    sequenceQuality: function () {
                        return objectModelFactory.getModelByName('SequenceQuality');
                    },
                    hmlModel: function () {
                        return sequenceQualityCtrl.hml;
                    },
                    parentCollectionPropertyAllocation: function () {
                        return sequenceQualityCtrl.parentCollectionPropertyAllocation;
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
                        updatedModel = modelUpdater.updateModel(sequenceQualityCtrl.hml, propertyMap, result);

                    modelUpdater.removeTempIds(updatedModel);
                    hmlService.updateHml(updatedModel).then(function (hmlResult) {
                        sequenceQualityCtrl.hml = hmlResult;
                        parentCtrl.hml = sequenceQualityCtrl.hml;
                        usSpinnerService.stop('index-spinner');
                        sequenceQualityCtrl.sequenceQuality = modelUpdater.returnObjectFromHml(propertyMap, sequenceQualityCtrl.hml);
                    });
                }
            });
        };

        function returnPropertyLocator () {
            return [
                { propertyString: 'samples', propertyIndex: sequenceQualityCtrl.sampleIndex, isArray: true },
                { propertyString: 'typing', propertyIndex: -1, isArray: false },
                { propertyString: 'consensusSequence', propertyIndex: -1, isArray: false },
                { propertyString: 'consensusSequenceBlock', propertyIndex: -1, isArray: false },
                { propertyString: 'sequenceQuality', propertyIndex: -1, isArray: false }
            ];
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('sequenceQuality', sequenceQuality);
    sequenceQuality.$inject = ['$scope', '$uibModal', 'objectModelFactory', 'usSpinnerService', 'modelUpdater', 'hmlService'];
}());