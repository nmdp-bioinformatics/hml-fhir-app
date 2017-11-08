/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function amplification ($scope, $uibModal, objectModelFactory, usSpinnerService, modelUpdater, hmlService) {
        /* jshint validthis:true */
        var amplificationCtrl = this,
            parentCtrl = $scope.parentCtrl;

        amplificationCtrl.scope = $scope;
        amplificationCtrl.hml = parentCtrl.hml;
        amplificationCtrl.sampleIndex = parentCtrl.sampleIndex;
        amplificationCtrl.parentCollectionPropertyAllocation = returnPropertyLocator();
        amplificationCtrl.amplification = {};

        usSpinnerService.stop('index-spinner');

        amplificationCtrl.addAmplification = function () {
            usSpinnerService.spin('index-spinner');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/guided/hml/samples/typing/typing-method/sbt-sanger/amplification/amplification-add-edit.html',
                controller: 'amplificationAddEdit',
                controllerAs: 'amplificationAddEditCtrl',
                resolve: {
                    amplification: function () {
                        return objectModelFactory.getModelByName('Amplification');
                    },
                    hmlModel: function () {
                        return amplificationCtrl.hml;
                    },
                    parentCollectionPropertyAllocation: function () {
                        return amplificationCtrl.parentCollectionPropertyAllocation;
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
                        updatedModel = modelUpdater.updateModel(amplificationCtrl.hml, propertyMap, result);

                    modelUpdater.removeTempIds(updatedModel);
                    hmlService.updateHml(updatedModel).then(function (hmlResult) {
                        amplificationCtrl.hml = hmlResult;
                        parentCtrl.hml = amplificationCtrl.hml;
                        usSpinnerService.stop('index-spinner');
                        amplificationCtrl.amplification = modelUpdater.returnObjectFromHml(propertyMap, amplificationCtrl.hml);
                    });
                }
            });
        };

        function returnPropertyLocator () {
            return [
                { propertyString: 'samples', propertyIndex: amplificationCtrl.sampleIndex, isArray: true },
                { propertyString: 'typing', propertyIndex: -1, isArray: false },
                { propertyString: 'typingMethod', propertyIndex: -1, isArray: false },
                { propertyString: 'sbtSanger', propertyIndex: -1, isArray: false },
                { propertyString: 'amplification', propertyIndex: -1, isArray: true }
            ];
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('amplification', amplification);
    amplification.$inject = ['$scope', '$uibModal', 'objectModelFactory', 'usSpinnerService', 'modelUpdater', 'hmlService'];
}());