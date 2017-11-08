/**
 * Created by abrown3 on 2/12/17.
 */
(function () {
    'use strict';

    function consensusSequence ($scope, $uibModal, usSpinnerService, objectModelFactory, modelUpdater, hmlService) {
        /* jshint validthis:true */
        var consensusSequenceCtrl = this,
            parentCtrl = $scope.parentCtrl;

        consensusSequenceCtrl.scope = $scope;
        consensusSequenceCtrl.hml = parentCtrl.hml;
        consensusSequenceCtrl.sampleIndex = parentCtrl.sampleIndex;
        consensusSequenceCtrl.parentCollectionPropertyAllocation = returnPropertyLocator();
        consensusSequenceCtrl.consensusSequence = {};

        usSpinnerService.stop('index-spinner');

        consensusSequenceCtrl.addConsensusSequence = function () {
            usSpinnerService.spin('index-spinner');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/guided/hml/samples/typing/consensus-sequence/consensus-sequence-add-edit.html',
                controller: 'consensusSequenceAddEdit',
                controllerAs: 'consensusSequenceAddEditCtrl',
                resolve: {
                    consensusSequence: function () {
                        return objectModelFactory.getModelByName('ConsensusSequence');
                    },
                    hmlModel: function () {
                        return consensusSequenceCtrl.hml;
                    },
                    parentCollectionPropertyAllocation: function () {
                        return consensusSequenceCtrl.parentCollectionPropertyAllocation;
                    },
                    edit: function () {
                        return  false;
                    }
                }
            });

            modalInstance.result.then(function (result) {
                if (result) {
                    usSpinnerService.spin('index-spinner');
                    var propertyMap = modelUpdater.convertPropertyMapToRamda(returnPropertyLocator()),
                        updatedModel = modelUpdater.updateModel(consensusSequenceCtrl.hml, propertyMap, result);

                    modelUpdater.removeTempIds(updatedModel);
                    hmlService.updateHml(updatedModel).then(function (hmlResult) {
                        consensusSequenceCtrl.hml = hmlResult;
                        parentCtrl.hml = consensusSequenceCtrl.hml;
                        usSpinnerService.stop('index-spinner');
                        consensusSequenceCtrl.consensusSequence = modelUpdater.returnObjectFromHml(propertyMap, consensusSequenceCtrl.hml);
                    });
                }
            });
        };

        function returnPropertyLocator() {
            return [
                { propertyString: 'samples', propertyIndex: consensusSequenceCtrl.sampleIndex, isArray: true },
                { propertyString: 'typing', propertyIndex: -1, isArray: false },
                { propertyString: 'consensusSequence', propertyIndex: -1, isArray: false }
            ];
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('consensusSequence', consensusSequence);
    consensusSequence.$inject = ['$scope', '$uibModal', 'usSpinnerService', 'objectModelFactory', 'modelUpdater', 'hmlService'];
}());