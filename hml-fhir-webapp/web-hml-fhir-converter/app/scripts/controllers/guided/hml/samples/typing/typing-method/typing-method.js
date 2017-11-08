/**
 * Created by abrown3 on 2/12/17.
 */
(function () {
    'use strict';

    function typingMethod ($scope, $uibModal, objectModelFactory, usSpinnerService, modelUpdater, hmlService) {
        /* jshint validthis:true */
        var typingMethodCtrl = this,
            parentCtrl = $scope.parentCtrl;

        usSpinnerService.stop('index-spinner');

        typingMethodCtrl.scope = $scope;
        typingMethodCtrl.hml = parentCtrl.hml;
        typingMethodCtrl.sampleIndex = parentCtrl.sampleIndex;
        typingMethodCtrl.parentCollectionPropertyAllocation = returnPropertyLocator();
        typingMethodCtrl.typingMethod = {};

        typingMethodCtrl.addTypingMethodEntry = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/guided/hml/samples/typing/typing-method/typing-method-add-edit.html',
                controller: 'typingMethodAddEdit',
                controllerAs: 'typingMethodAddEditCtrl',
                resolve: {
                    typingMethod: function () {
                        return objectModelFactory.getModelByName('TypingMethod');
                    },
                    hmlModel: function () {
                        return typingMethodCtrl.hml;
                    },
                    parentCollectionPropertyAllocation: function () {
                        return typingMethodCtrl.parentCollectionPropertyAllocation;
                    },
                    edit: function () {
                        return false;
                    }
                }
            });

            modalInstance.result.then(function (result) {
                if (result) {
                    usSpinnerService.spin('index-spinner');
                    var propertyMap = modelUpdater.convertPropertyMapToRamda(returnPropertyMap()),
                        updatedModel = modelUpdater.updateModel(typingMethodCtrl.hml, propertyMap, result);

                    modelUpdater.removeTempIds(updatedModel);
                    hmlService.updateHml(updatedModel).then(function (hmlResult) {
                        typingMethodCtrl.hml = hmlResult;
                        parentCtrl.hml = typingMethodCtrl.hml;
                        usSpinnerService.stop('index-spinner');
                        typingMethodCtrl.typingMethod = modelUpdater.returnObjectFromHml(propertyMap, typingMethodCtrl.hml);
                    });
                }
            });
        };

        function returnPropertyLocator () {
            return [
                { propertyString: 'samples', propertyIndex: typingMethodCtrl.sampleIndex, isArray: true },
                { propertyString: 'typing', propertyIndex: -1, isArray: false },
                { propertyString: 'typingMethod', propertyIndex: -1, isArray: false }
            ];
        }

        function returnPropertyMap () {
            return [
                { propertyString: 'samples', propertyIndex: typingCtrl.sampleIndex, isArray: true },
                { propertyString: 'typing', propertyIndex: -1, isArray: false },
                { propertyString: 'typingMethod', propertyIndex: -1, isArray: false }
            ];
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('typingMethod', typingMethod);
    typingMethod.$inject = ['$scope', '$uibModal', 'objectModelFactory', 'usSpinnerService', 'modelUpdater', 'hmlService'];
}());
