/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function referenceDatabase ($scope, $uibModal, objectModelFactory, usSpinnerService, modelUpdater, hmlService) {
        /* jshint validthis:true */
        var referenceDatabaseCtrl = this,
            parentCtrl = $scope.parentCtrl;

        usSpinnerService.stop('index-spinner');

        referenceDatabaseCtrl.scope = $scope;
        referenceDatabaseCtrl.hml = parentCtrl.hml;
        referenceDatabaseCtrl.sampleIndex = parentCtrl.sampleIndex;
        referenceDatabaseCtrl.parentCollectionPropertyAllocation = returnPropertyLocator();
        referenceDatabaseCtrl.referenceDatabase = {};

        referenceDatabaseCtrl.addReferenceDatabase = function () {
            usSpinnerService.spin('index-spinner');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/guided/hml/samples/typing/consensus-sequence/reference-database/reference-database-add-edit.html',
                controller: 'referenceDatabaseAddEdit',
                controllerAs: 'referenceDatabaseAddEditCtrl',
                resolve: {
                    referenceDatabase: function () {
                        return objectModelFactory.getModelByName('ReferenceDatabase');
                    },
                    hmlModel: function () {
                        return referenceDatabaseCtrl.hml;
                    },
                    parentCollectionPropertyAllocation: function () {
                        return referenceDatabaseCtrl.parentCollectionPropertyAllocation;
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
                        updatedModel = modelUpdater.updateModel(referenceDatabaseCtrl.hml, propertyMap, result);

                    modelUpdater.removeTempIds(updatedModel);
                    hmlService.updateHml(updatedModel).then(function (hmlResult) {
                        referenceDatabaseCtrl.hml = hmlResult;
                        parentCtrl.hml = referenceDatabaseCtrl.hml;
                        usSpinnerService.stop('index-spinner');
                        referenceDatabaseCtrl.referenceDatabase = modelUpdater.returnObjectFromHml(propertyMap, referenceDatabaseCtrl.hml);
                    });
                }
            });
        };

        function returnPropertyLocator() {
            return [
                { propertyString: 'samples', propertyIndex: referenceDatabaseCtrl.sampleIndex, isArray: true },
                { propertyString: 'typing', propertyIndex: -1, isArray: false },
                { propertyString: 'consensusSequence', propertyIndex: -1, isArray: false },
                { propertyString: 'referenceDatabase', propertyIndex: -1, isArray: true }
            ];
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('referenceDatabase', referenceDatabase);
    referenceDatabase.$inject = ['$scope', '$uibModal', 'objectModelFactory', 'usSpinnerService', 'modelUpdater', 'hmlService'];
}());