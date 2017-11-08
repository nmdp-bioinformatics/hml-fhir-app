/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function glstring ($scope, $uibModal, objectModelFactory, usSpinnerService, modelUpdater, hmlService) {
        /* jshint validthis:true */
        var glstringCtrl = this,
            parentCtrl = $scope.parentCtrl;

        glstringCtrl.scope = $scope;
        glstringCtrl.hml = parentCtrl.hml;
        glstringCtrl.sampleIndex = parentCtrl.sampleIndex;
        glstringCtrl.parentCollectionPropertyAllocation = returnPropertyLocator();
        glstringCtrl.glstring = {};

        usSpinnerService.stop('index-spinner');

        glstringCtrl.addGlString = function () {
            usSpinnerService.spin('index-spinner');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/guided/hml/samples/typing/allele-assignment/glstring/glstring-add-edit.html',
                controller: 'glstringAddEdit',
                controllerAs: 'glstringAddEditCtrl',
                resolve: {
                    glstring: function () {
                        return objectModelFactory.getModelByName('GlString');
                    },
                    hmlModel: function () {
                        return glstringCtrl.hml;
                    },
                    parentCollectionPropertyAllocation: function () {
                        return glstringCtrl.parentCollectionPropertyAllocation;
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
                        updatedModel = modelUpdater.updateModel(glstringCtrl.hml, propertyMap, result);

                    modelUpdater.removeTempIds(updatedModel);
                    hmlService.updateHml(updatedModel).then(function (hmlResult) {
                        glstringCtrl.hml = hmlResult;
                        parentCtrl.hml = glstringCtrl.hml;
                        usSpinnerService.stop('index-spinner');
                        glstringCtrl.glstring = modelUpdater.returnObjectFromHml(propertyMap, glstringCtrl.hml);
                    });
                }
            });
        };

        function returnPropertyLocator () {
            return [
                { propertyString: 'samples', propertyIndex: glstringCtrl.sampleIndex, isArray: true },
                { propertyString: 'typing', propertyIndex: -1, isArray: false },
                { propertyString: 'alleleAssignment', propertyIndex: -1, isArray: false },
                { propertyString: 'glstring', propertyIndex: -1, isArray: false }
            ];
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('glstring', glstring);
    glstring.$inject = ['$scope', '$uibModal', 'objectModelFactory', 'usSpinnerService', 'modelUpdater', 'hmlService'];
}());