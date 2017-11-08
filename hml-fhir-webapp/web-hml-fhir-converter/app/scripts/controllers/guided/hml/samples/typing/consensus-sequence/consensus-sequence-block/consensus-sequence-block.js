/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function consensusSequenceBlock ($scope, $uibModal, objectModelFactory, usSpinnerService, modelUpdater, hmlService) {
        /* jshint validthis:true */
        var consensusSequenceBlockCtrl = this,
            parentCtrl = $scope.parentCtrl;

        usSpinnerService.stop('index-spinner');

        consensusSequenceBlockCtrl.scope = $scope;
        consensusSequenceBlockCtrl.hml = parentCtrl.hml;
        consensusSequenceBlockCtrl.sampleIndex = parentCtrl.sampleIndex;
        consensusSequenceBlockCtrl.parentCollectionPropertyAllocation = returnPropertyLocator();
        consensusSequenceBlockCtrl.consensusSequence = {};

        consensusSequenceBlockCtrl.addConsensusSequenceBlock = function () {
            usSpinnerService.spin('index-spinner');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/guided/hml/samples/typing/consensus-sequence/consensus-sequence-block/consensus-sequence-block-add-edit.html',
                controller: 'consensusSequenceBlockAddEdit',
                controllerAs: 'consensusSequenceBlockAddEditCtrl',
                resolve: {
                    consensusSequenceBlock: function () {
                        return objectModelFactory.getModelByName('ConsensusSequenceBlock');
                    },
                    hmlModel: function () {
                        return consensusSequenceBlockCtrl.hml;
                    },
                    parentCollectionPropertyAllocation: function () {
                        return consensusSequenceBlockCtrl.parentCollectionPropertyAllocation;
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
                        updatedModel = modelUpdater.updateModel(consensusSequenceBlockCtrl.hml, propertyMap, result);

                    modelUpdater.removeTempIds(updatedModel);
                    hmlService.updateHml(updatedModel).then(function (hmlResult) {
                        consensusSequenceBlockCtrl.hml = hmlResult;
                        parentCtrl.hml = consensusSequenceBlockCtrl.hml;
                        usSpinnerService.stop('index-spinner');
                        consensusSequenceBlockCtrl.ssp = modelUpdater.returnObjectFromHml(propertyMap, consensusSequenceBlockCtrl.hml);
                    });
                }
            });
        };

        function returnPropertyLocator () {
            return [
                { propertyString: 'samples', propertyIndex: consensusSequenceBlockCtrl.sampleIndex, isArray: true },
                { propertyString: 'typing', propertyIndex: -1, isArray: false },
                { propertyString: 'consensusSequence', propertyIndex: -1, isArray: false },
                { propertyString: 'consensusSequenceBlock', propertyIndex: -1, isArray: true }
            ];
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('consensusSequenceBlock', consensusSequenceBlock);
    consensusSequenceBlock.$inject = ['$scope', '$uibModal', 'objectModelFactory', 'usSpinnerService', 'modelUpdater', 'hmlService'];
}());