/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function referenceSequence ($scope, $uibModal, objectModelFactory, usSpinnerService, modelUpdater, hmlService) {
        /* jshint validthis:true */
        var referenceSequenceCtrl = this,
            parentCtrl = $scope.parentCtrl;

        usSpinnerService.stop('index-spinner');

        referenceSequenceCtrl.scope = $scope;
        referenceSequenceCtrl.hml = parentCtrl.hml;
        referenceSequenceCtrl.sampleIndex = parentCtrl.sampleIndex;
        referenceSequenceCtrl.parentCollectionPropertyAllocation = returnPropertyLocator();
        referenceSequenceCtrl.referenceSequence = {};

        referenceSequenceCtrl.addReferenceSequence = function () {
            usSpinnerService.spin('index-spinner');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/guided/hml/samples/typing/consensus-sequence/reference-database/reference-sequence/reference-sequence-add-edit.html',
                controller: 'referenceSequenceAddEdit',
                controllerAs: 'referenceSequenceAddEditCtrl',
                resolve: {
                    referenceSequence: function () {
                        return objectModelFactory.getModelByName('ReferenceSequence');
                    },
                    hmlModel: function () {
                        return referenceSequenceCtrl.hml;
                    },
                    parentCollectionPropertyAllocation: function () {
                        return referenceSequenceCtrl.parentCollectionPropertyAllocation;
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
                        updatedModel = modelUpdater.updateModel(referenceSequenceCtrl.hml, propertyMap, result);

                    modelUpdater.removeTempIds(updatedModel);
                    hmlService.updateHml(updatedModel).then(function (hmlResult) {
                        referenceSequenceCtrl.hml = hmlResult;
                        parentCtrl.hml = referenceSequenceCtrl.hml;
                        usSpinnerService.stop('index-spinner');
                        referenceSequenceCtrl.referenceSequence = modelUpdater.returnObjectFromHml(propertyMap, referenceSequenceCtrl.hml);
                    });
                }
            });
        };

        function returnPropertyLocator () {
            return [
                { propertyString: 'samples', propertyIndex: referenceSequenceCtrl.sampleIndex, isArray: true },
                { propertyString: 'typing', propertyIndex: -1, isArray: false },
                { propertyString: 'consensusSequence', propertyIndex: -1, isArray: false },
                { propertyString: 'referenceDatabase', propertyIndex: -1, isArray: false },
                { propertyString: 'referenceSequence', propertyIndex: -1, isArray: false }
            ];
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('referenceSequence', referenceSequence);
    referenceSequence.$inject = ['$scope', '$uibModal', 'objectModelFactory', 'usSpinnerService', 'modelUpdater', 'hmlService'];
}());