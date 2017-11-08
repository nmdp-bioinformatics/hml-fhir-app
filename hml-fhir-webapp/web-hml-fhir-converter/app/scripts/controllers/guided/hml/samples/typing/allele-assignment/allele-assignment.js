/**
 * Created by abrown3 on 2/12/17.
 */
(function () {
    'use strict';

    function alleleAssignment ($scope, $uibModal, appConfig, usSpinnerService, objectModelFactory, modelUpdater, hmlService) {
        /* jshint validthis:true */
        var alleleAssignmentCtrl = this,
            parentCtrl = $scope.parentCtrl;

        alleleAssignmentCtrl.scope = $scope;
        alleleAssignmentCtrl.hml = parentCtrl.hml;
        alleleAssignmentCtrl.sampleIndex = parentCtrl.sampleIndex;
        alleleAssignmentCtrl.parentCollectionPropertyAllocation = returnPropertyLocator();
        alleleAssignmentCtrl.alleleAssignment = {};

        usSpinnerService.stop('index-spinner');

        alleleAssignmentCtrl.addAlleleAssignment = function () {
            usSpinnerService.spin('index-spinner');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/guided/hml/samples/typing/allele-assignment/allele-assignment-add-edit.html',
                controller: 'alleleAssignmentAddEdit',
                controllerAs: 'alleleAssignmentAddEditCtrl',
                resolve: {
                    alleleAssignment: function () {
                        return objectModelFactory.getModelByName('AlleleAssignment');
                    },
                    hmlModel: function () {
                        return alleleAssignmentCtrl.hml;
                    },
                    parentCollectionPropertyAllocation: function () {
                        return alleleAssignmentCtrl.parentCollectionPropertyAllocation;
                    },
                    edit: function () {
                        return  false;
                    }
                }
            });

            modalInstance.result.then(function (result) {
                if (result) {
                    usSpinnerService.spin('index-spinner');
                    var propertyMap = modelUpdater.convertPropertyMapToRamda(returnPropertyMap()),
                        updatedModel = modelUpdater.updateModel(alleleAssignmentCtrl.hml, propertyMap, result);

                    modelUpdater.removeTempIds(updatedModel);
                    hmlService.updateHml(updatedModel).then(function (hmlResult) {
                        alleleAssignmentCtrl.hml = hmlResult;
                        parentCtrl.hml = alleleAssignmentCtrl.hml;
                        usSpinnerService.stop('index-spinner');
                        alleleAssignmentCtrl.alleleAssignment = modelUpdater.returnObjectFromHml(propertyMap, alleleAssignmentCtrl.hml);
                    });
                }
            });
        };

        function returnPropertyLocator() {
            return setLocatorIndexes(appConfig.propertiesParentMap.alleleAssignmentParent);
        }

        function setLocatorIndexes(config) {
            for (var i = 0; i < config.length; i++) {
                if (config[i].propertyString === 'samples') {
                    config[i].propertyIndex = alleleAssignmentCtrl.sampleIndex;
                }
            }

            return config;
        }

        function returnPropertyMap () {
            return [
                { propertyString: 'samples', propertyIndex: alleleAssignmentCtrl.sampleIndex, isArray: true },
                { propertyString: 'typing', propertyIndex: -1, isArray: false },
                { propertyString: 'alleleAssignment', propertyIndex: -1, isArray: false }
            ];
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('alleleAssignment', alleleAssignment);
    alleleAssignment.$inject = ['$scope', '$uibModal', 'appConfig', 'usSpinnerService', 'objectModelFactory', 'modelUpdater', 'hmlService'];
}());