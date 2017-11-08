/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function rawReads ($scope, $uibModal, objectModelFactory, usSpinnerService, modelUpdater, hmlService) {
        /* jshint validthis:true */
        var rawReadsCtrl = this,
            parentCtrl = $scope.parentCtrl;

        rawReadsCtrl.scope = $scope;
        rawReadsCtrl.hml = parentCtrl.hml;
        rawReadsCtrl.sampleIndex = parentCtrl.sampleIndex;
        rawReadsCtrl.parentCollectionPropertyAllocation = returnPropertyLocator();
        rawReadsCtrl.rawReads = [];

        usSpinnerService.stop('index-spinner');

        rawReadsCtrl.addRawRead = function () {
            usSpinnerService.spin('index-spinner');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/guided/hml/samples/typing/typing-method/sbt-ngs/raw-reads/raw-reads-add-edit.html',
                controller: 'rawReadsAddEdit',
                controllerAs: 'rawReadsAddEditCtrl',
                resolve: {
                    rawRead: function () {
                        return objectModelFactory.getModelByName('RawRead');
                    },
                    hmlModel: function () {
                        return rawReadsCtrl.hml;
                    },
                    parentCollectionPropertyAllocation: function () {
                        return rawReadsCtrl.parentCollectionPropertyAllocation;
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
                        updatedModel = modelUpdater.updateModel(rawReadsCtrl.hml, propertyMap, result);

                    modelUpdater.removeTempIds(updatedModel);
                    hmlService.updateHml(updatedModel).then(function (hmlResult) {
                        rawReadsCtrl.hml = hmlResult;
                        parentCtrl.hml = rawReadsCtrl.hml;
                        usSpinnerService.stop('index-spinner');
                        rawReadsCtrl.rawReads = modelUpdater.returnObjectFromHml(propertyMap, rawReadsCtrl.hml);
                    });
                }
            });
        };

        function returnPropertyLocator () {
            return [
                { propertyString: 'samples', propertyIndex: rawReadsCtrl.sampleIndex, isArray: true },
                { propertyString: 'typing', propertyIndex: -1, isArray: false },
                { propertyString: 'typingMethod', propertyIndex: -1, isArray: false },
                { propertyString: 'sbtNgs', propertyIndex: -1, isArray: false },
                { propertyString: 'rawReads', propertyIndex: -1, isArray: true }
            ];
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('rawReads', rawReads);
    rawReads.$inject = ['$scope', '$uibModal', 'objectModelFactory', 'usSpinnerService', 'modelUpdater', 'hmlService'];
}());