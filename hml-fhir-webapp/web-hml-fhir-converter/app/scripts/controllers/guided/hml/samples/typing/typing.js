/**
 * Created by abrown3 on 2/11/17.
 */
(function () {
    'use strict';

    function typing ($scope, $uibModal, appConfig, objectModelFactory, usSpinnerService, modelUpdater, hmlService) {
        /* jshint validthis: true */
        var typingCtrl = this,
            parentCtrl = $scope.parentCtrl;

        typingCtrl.scope = $scope;
        typingCtrl.hml = parentCtrl.hml;
        typingCtrl.sampleIndex = parentCtrl.sampleIndex;
        typingCtrl.parentCollectionPropertyAllocation = returnPropertyLocator();
        typingCtrl.typing = {};

        usSpinnerService.stop('index-spinner');

        typingCtrl.addTypingEntry = function () {
            usSpinnerService.spin('index-spinner');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/guided/hml/samples/typing/typing-add-edit.html',
                controller: 'typingAddEdit',
                controllerAs: 'typingAddEditCtrl',
                resolve: {
                    typing: function () {
                        return objectModelFactory.getModelByName('Typing');
                    },
                    hmlModel: function () {
                        return typingCtrl.hml;
                    },
                    parentCollectionPropertyAllocation: function () {
                        return typingCtrl.parentCollectionPropertyAllocation;
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
                        updatedModel = modelUpdater.updateModel(typingCtrl.hml, propertyMap, result);

                    modelUpdater.removeTempIds(updatedModel);
                    hmlService.updateHml(updatedModel).then(function (hmlResult) {
                        typingCtrl.hml = hmlResult;
                        parentCtrl.hml = typingCtrl.hml;
                        usSpinnerService.stop('index-spinner');
                        typingCtrl.typing = modelUpdater.returnObjectFromHml(propertyMap, typingCtrl.hml);
                    });
                }
            });
        };

        function returnPropertyLocator () {
            return setLocatorIndexes(appConfig.propertiesParentMap.typingParent);
        }

        function setLocatorIndexes (config) {
            for (var i = 0; i < config.length; i++) {
                if (config[i].propertyString === 'samples') {
                    config[i].propertyIndex = typingCtrl.sampleIndex;
                }
            }

            return config;
        }

        function returnPropertyMap () {
            return [
                { propertyString: 'samples', propertyIndex: typingCtrl.sampleIndex, isArray: true },
                { propertyString: 'typing', propertyIndex: -1, isArray: false }
            ];
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('typing', typing);
    typing.$inject = ['$scope', '$uibModal', 'appConfig', 'objectModelFactory', 'usSpinnerService', 'modelUpdater', 'hmlService'];
}());
