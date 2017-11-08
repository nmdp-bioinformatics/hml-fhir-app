/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function sequence ($scope, $uibModal, indexCollection, objectModelFactory, guidGenerator, usSpinnerService) {
        /* jshint validthis:true */
        var sequenceCtrl = this,
            parentCtrl = $scope.parentCtrl;

        usSpinnerService.stop('index-spinner');

        sequenceCtrl.scope = $scope;
        sequenceCtrl.parentCollectionPropertyAllocation = parentCtrl.parentCollectionPropertyAllocation;
        sequenceCtrl.hml = parentCtrl.hml;

        sequenceCtrl.addSequence = function () {
            usSpinnerService.spin('index-spinner');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/guided/hml/sequence/sequence-add-edit.html',
                controller: 'sequenceAddEdit',
                controllerAs: 'sequenceAddEditCtrl',
                resolve: {
                    edit: function () {
                        return false;
                    },
                    sequence: function () {
                        return objectModelFactory.getModelByName('Sequence');
                    },
                    tempId: function () {
                        return guidGenerator.generateRandomGuid();
                    }
                }
            });

            modalInstance.result.then(function (result) {
                if (result) {

                }
            });

            function updateHmlWithSequenceData (sequence, isDelete) {
                var hmlSequences = getThisSequenceArray(),
                    sequenceIndex = getSequenceIndex(sequence);

                if (isDelete) {
                    hmlSequences.splice(sequenceIndex, 1);
                    return;
                }

                if (sequenceIndex === -1) {
                    hmlSequences.push(sequence);
                    return;
                }

                hmlSequences[sequenceIndex] = sequence;
            }

            function getThisSequenceArray () {
                var sequenceArray = [],
                    locator = sequenceCtrl.parentCollectionPropertyAllocation;

                if (locator) {
                    var obj = sequenceArray.hml;

                    for (var i = 0; i < locator.length; i++) {
                        obj = obj[locator[i].propertyString];

                        if (locator[i].isArray && locator[i].propertyIndex > -1) {
                            obj = obj[locator[i].propertyIndex];
                        }
                    }

                    if (!obj) {
                        return [];
                    }

                    sequenceArray = obj;
                }

                return sequenceArray;
            }

            function getSequenceIndex (sequence) {
                return indexCollection.getCollectionItemIndex(getThisSequenceArray(), undefined, sequence.id);
            }
        };
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('sequence', sequence);
    sequence.$inject = ['$scope', '$uibModal', 'indexCollection', 'objectModelFactory', 'guidGenerator', 'usSpinnerService'];
}());