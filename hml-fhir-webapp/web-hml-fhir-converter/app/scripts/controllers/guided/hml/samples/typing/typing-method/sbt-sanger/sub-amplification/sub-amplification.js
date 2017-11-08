/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function subAmplification ($scope, $uibModal, objectModelFactory, usSpinnerService, modelUpdater, hmlService) {
        /* jshint validthis:true */
        var subAmplificationCtrl = this,
            parentCtrl = $scope.parentCtrl;

        subAmplificationCtrl.scope = $scope;
        subAmplificationCtrl.hml = parentCtrl.hml;
        subAmplificationCtrl.sampleIndex = parentCtrl.sampleIndex;
        subAmplificationCtrl.parentCollectionPropertyAllocation = returnPropertyLocator();
        subAmplificationCtrl.subAmplification = {};

        usSpinnerService.stop('index-spinner');

        subAmplificationCtrl.addSubAmplification = function () {
            usSpinnerService.spin('index-spinner');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/guided/hml/samples/typing/typing-method/sbt-sanger/sub-amplification/sub-amplification-add-edit.html',
                controller: 'subAmplificationAddEdit',
                controllerAs: 'subAmplificationAddEditCtrl',
                resolve: {
                    subAmplification: function () {
                        return objectModelFactory.getModelByName('SubAmplification');
                    },
                    hmlModel: function () {
                        return subAmplificationCtrl.hml;
                    },
                    parentCollectionPropertyAllocation: function () {
                        return subAmplificationCtrl.parentCollectionPropertyAllocation;
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
                        updatedModel = modelUpdater.updateModel(subAmplificationCtrl.hml, propertyMap, result);

                    modelUpdater.removeTempIds(updatedModel);
                    hmlService.updateHml(updatedModel).then(function (hmlResult) {
                        subAmplificationCtrl.hml = hmlResult;
                        parentCtrl.hml = subAmplificationCtrl.hml;
                        usSpinnerService.stop('index-spinner');
                        subAmplificationCtrl.subAmplification = modelUpdater.returnObjectFromHml(propertyMap, subAmplificationCtrl.hml);
                    });
                }
            });
        };

        function returnPropertyLocator () {
            return [
                { propertyString: 'samples', propertyIndex: subAmplificationCtrl.sampleIndex, isArray: true },
                { propertyString: 'typing', propertyIndex: -1, isArray: false },
                { propertyString: 'typingMethod', propertyIndex: -1, isArray: false },
                { propertyString: 'sbtSanger', propertyIndex: -1, isArray: false },
                { propertyString: 'subAmplification', propertyIndex: -1, isArray: true }
            ];
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('subAmplification', subAmplification);
    subAmplification.$inject = ['$scope', '$uibModal', 'objectModelFactory', 'usSpinnerService', 'modelUpdater', 'hmlService'];
}());