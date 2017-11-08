/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function gssp ($scope, $uibModal, objectModelFactory, usSpinnerService, modelUpdater, hmlService) {
        /* jshint validthis:true */
        var gsspCtrl = this,
            parentCtrl = $scope.parentCtrl;

        gsspCtrl.scope = $scope;
        gsspCtrl.hml = parentCtrl.hml;
        gsspCtrl.sampleIndex = parentCtrl.sampleIndex;
        gsspCtrl.parentCollectionPropertyAllocation = returnPropertyLocator();
        gsspCtrl.gssp = {};

        usSpinnerService.stop('index-spinner');

        gsspCtrl.addGssp = function () {
            usSpinnerService.spin('index-spinner');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/guided/hml/samples/typing/typing-method/sbt-sanger/gssp/gssp-add-edit.html',
                controller: 'gsspAddEdit',
                controllerAs: 'gsspAddEditCtrl',
                resolve: {
                    gssp: function () {
                        return objectModelFactory.getModelByName('Gssp');
                    },
                    hmlModel: function () {
                        return gsspCtrl.hml;
                    },
                    parentCollectionPropertyAllocation: function () {
                        return gsspCtrl.parentCollectionPropertyAllocation;
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
                        updatedModel = modelUpdater.updateModel(gsspCtrl.hml, propertyMap, result);

                    modelUpdater.removeTempIds(updatedModel);
                    hmlService.updateHml(updatedModel).then(function (hmlResult) {
                        gsspCtrl.hml = hmlResult;
                        parentCtrl.hml = gsspCtrl.hml;
                        usSpinnerService.stop('index-spinner');
                        gsspCtrl.gssp = modelUpdater.returnObjectFromHml(propertyMap, gsspCtrl.hml);
                    });
                }
            });
        };

        function returnPropertyLocator () {
            return [
                { propertyString: 'samples', propertyIndex: gsspCtrl.sampleIndex, isArray: true },
                { propertyString: 'typing', propertyIndex: -1, isArray: false },
                { propertyString: 'typingMethod', propertyIndex: -1, isArray: false },
                { propertyString: 'sbtSanger', propertyIndex: -1, isArray: false },
                { propertyString: 'subAmplification', propertyIndex: -1, isArray: true }
            ];
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('gssp', gssp);
    gssp.$inject = ['$scope', '$uibModal', 'objectModelFactory', 'usSpinnerService', 'modelUpdater', 'hmlService'];
}());