/**
 * Created by abrown3 on 2/13/17.
 */
(function () {
    'use strict';

    function sso ($scope, $uibModal, objectModelFactory, usSpinnerService, modelUpdater, hmlService) {
        /* jshint validthis:true */
        var ssoCtrl = this,
            parentCtrl = $scope.parentCtrl;

        usSpinnerService.stop('index-spinner');

        ssoCtrl.scope = $scope;
        ssoCtrl.hml = parentCtrl.hml;
        ssoCtrl.sampleIndex = parentCtrl.sampleIndex;
        ssoCtrl.parentCollectionPropertyAllocation = returnPropertyLocator();
        ssoCtrl.sso = {};

        ssoCtrl.addSso = function () {
            usSpinnerService.spin('index-spinner');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/guided/hml/samples/typing/typing-method/sso/sso-add-edit.html',
                controller: 'ssoAddEdit',
                controllerAs: 'ssoAddEditCtrl',
                resolve: {
                    sso: function () {
                        return objectModelFactory.getModelByName('Sso');
                    },
                    hmlModel: function () {
                        return ssoCtrl.hml;
                    },
                    parentCollectionPropertyAllocation: function () {
                        return ssoCtrl.parentCollectionPropertyAllocation;
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
                        updatedModel = modelUpdater.updateModel(ssoCtrl.hml, propertyMap, result);

                    modelUpdater.removeTempIds(updatedModel);
                    hmlService.updateHml(updatedModel).then(function (hmlResult) {
                        ssoCtrl.hml = hmlResult;
                        parentCtrl.hml = ssoCtrl.hml;
                        usSpinnerService.stop('index-spinner');
                        ssoCtrl.sso = modelUpdater.returnObjectFromHml(propertyMap, ssoCtrl.hml);
                    });
                }
            });
        };

        function returnPropertyLocator () {
            return [
                { propertyString: 'samples', propertyIndex: ssoCtrl.sampleIndex, isArray: true },
                { propertyString: 'typing', propertyIndex: -1, isArray: false },
                { propertyString: 'typingMethod', propertyIndex: -1, isArray: false },
                { propertyString: 'sso', propertyIndex: -1, isArray: false }
            ];
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('sso', sso);
    sso.$inject = ['$scope', '$uibModal', 'objectModelFactory', 'usSpinnerService', 'modelUpdater', 'hmlService'];
}());