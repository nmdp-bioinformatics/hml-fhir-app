/**
 * Created by abrown3 on 2/13/17.
 */
(function () {
    'use strict';

    function sbtSanger ($scope, $uibModal, objectModelFactory, usSpinnerService, modelUpdater, hmlService) {
        /* jshint validthis:true */
        var sbtSangerCtrl = this,
            parentCtrl = $scope.parentCtrl;

        usSpinnerService.stop('index-spinner');

        sbtSangerCtrl.scope = $scope;
        sbtSangerCtrl.hml = parentCtrl.hml;
        sbtSangerCtrl.sampleIndex = parentCtrl.sampleIndex;
        sbtSangerCtrl.parentCollectionPropertyAllocation = returnPropertyLocator();
        sbtSangerCtrl.sbtSanger = {};

        sbtSangerCtrl.addSbtSanger = function () {
            usSpinnerService.spin('index-spinner');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/guided/hml/samples/typing/typing-method/sbt-sanger/sbt-sanger-add-edit.html',
                controller: 'sbtSangerAddEdit',
                controllerAs: 'sbtSangerAddEditCtrl',
                resolve: {
                    sbtSanger: function () {
                        return objectModelFactory.getModelByName('SbtSanger');
                    },
                    hmlModel: function () {
                        return sbtSangerCtrl.hml;
                    },
                    parentCollectionPropertyAllocation: function () {
                        return sbtSangerCtrl.parentCollectionPropertyAllocation;
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
                        updatedModel = modelUpdater.updateModel(sbtSangerCtrl.hml, propertyMap, result);

                    modelUpdater.removeTempIds(updatedModel);
                    hmlService.updateHml(updatedModel).then(function (hmlResult) {
                        sbtSangerCtrl.hml = hmlResult;
                        parentCtrl.hml = sbtSangerCtrl.hml;
                        usSpinnerService.stop('index-spinner');
                        sbtSangerCtrl.sbtSanger = modelUpdater.returnObjectFromHml(propertyMap, sbtSangerCtrl.hml);
                    });
                }
            });
        };

        function returnPropertyLocator() {
            return [
                { propertyString: 'samples', propertyIndex: sbtSangerCtrl.sampleIndex, isArray: true },
                { propertyString: 'typing', propertyIndex: -1, isArray: false },
                { propertyString: 'typingMethod', propertyIndex: -1, isArray: false },
                { propertyString: 'sbtSanger', propertyIndex: -1, isArray: false }
            ];
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('sbtSanger', sbtSanger);
    sbtSanger.$inject = ['$scope', '$uibModal', 'objectModelFactory', 'usSpinnerService', 'modelUpdater', 'hmlService'];
}());