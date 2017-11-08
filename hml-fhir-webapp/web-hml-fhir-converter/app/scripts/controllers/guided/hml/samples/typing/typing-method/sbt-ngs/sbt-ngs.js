/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function sbtNgs ($scope, $uibModal, objectModelFactory, usSpinnerService, modelUpdater, hmlService) {
        /* jshint validthis:true */
        var sbtNgsCtrl = this,
            parentCtrl = $scope.parentCtrl;

        usSpinnerService.stop('index-spinner');

        sbtNgsCtrl.scope = $scope;
        sbtNgsCtrl.hml = parentCtrl.hml;
        sbtNgsCtrl.sampleIndex = parentCtrl.sampleIndex;
        sbtNgsCtrl.parentCollectionPropertyAllocation = returnPropertyLocator();
        sbtNgsCtrl.sbtNgs = {};

        sbtNgsCtrl.addSbtNgs = function () {
            usSpinnerService.spin('index-spinner');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/guided/hml/samples/typing/typing-method/sbt-ngs/sbt-ngs-add-edit.html',
                controller: 'sbtNgsAddEdit',
                controllerAs: 'sbtNgsAddEditCtrl',
                resolve: {
                    sbtNgs: function () {
                        return objectModelFactory.getModelByName('SbtNgs');
                    },
                    hmlModel: function () {
                        return sbtNgsCtrl.hml;
                    },
                    parentCollectionPropertyAllocation: function () {
                        return sbtNgsCtrl.parentCollectionPropertyAllocation;
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
                        updatedModel = modelUpdater.updateModel(sbtNgsCtrl.hml, propertyMap, result);

                    modelUpdater.removeTempIds(updatedModel);
                    hmlService.updateHml(updatedModel).then(function (hmlResult) {
                        sbtNgsCtrl.hml = hmlResult;
                        parentCtrl.hml = sbtNgsCtrl.hml;
                        usSpinnerService.stop('index-spinner');
                        sbtNgsCtrl.sbtNgs = modelUpdater.returnObjectFromHml(propertyMap, sbtNgsCtrl.hml);
                    });
                }
            });
        };

        function returnPropertyLocator () {
            return [
                { propertyString: 'samples', propertyIndex: sbtNgsCtrl.sampleIndex, isArray: true },
                { propertyString: 'typing', propertyIndex: -1, isArray: false },
                { propertyString: 'typingMethod', propertyIndex: -1, isArray: false },
                { propertyString: 'sbtNgs', propertyIndex: -1, isArray: false }
            ];
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('sbtNgs', sbtNgs);
    sbtNgs.$inject = ['$scope', '$uibModal', 'objectModelFactory', 'usSpinnerService', 'modelUpdater', 'hmlService'];
}());