/**
 * Created by abrown3 on 12/15/16.
 */

(function () {
    'use strict';

    function properties ($scope, $uibModal, gridCellTemplateFactory, indexCollection, objectModelFactory, guidGenerator, usSpinnerService) {
        /* jshint validthis: true */
        var propertiesCtrl = this,
            parentCtrl = $scope.parentCtrl,
            deleteColumnTemplate = gridCellTemplateFactory.createRemoveCellTemplate();

        usSpinnerService.stop('index-spinner');

        propertiesCtrl.scope = $scope;
        propertiesCtrl.parentCollectionPropertyAllocation = parentCtrl.parentCollectionPropertyAllocation;
        propertiesCtrl.hml = parentCtrl.hml;
        propertiesCtrl.gridOptions = {
            data: getPropertiesFromLocationString(),
            enableSorting: true,
            showGridFooter: true,
            appScopeProvider: propertiesCtrl,
            columnDefs: [
                { name: 'id', field: 'id', visible: false },
                { name: 'name', field: 'name', displayName: 'Name:', cellTooltip: function (row) { return row.entity.name; }, headerTooltip: function(col) { return col.displayName; } },
                { name: 'value', field: 'value', displayName: 'Value:', cellTooltip: function (row) { return row.entity.value; }, headerTooltip: function(col) { return col.displayName; } },
                { name: 'description', field: 'description', displayName: 'Description:', cellTooltip: function (row) { return row.entity.description; }, headerTooltip: function(col) { return col.displayName; } },
                { field: 'delete', displayName: 'Remove', maxWidth: 75, enableColumnMenu: false, cellTemplate: deleteColumnTemplate }
            ]
        };

        propertiesCtrl.addPropertyEntry = function () {
            usSpinnerService.spin('index-spinner');
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/guided/hml/properties/properties-add-edit.html',
                controller: 'propertiesAddEdit',
                controllerAs: 'propertiesAddEditCtrl',
                resolve: {
                    edit: function () {
                        return false;
                    },
                    property: function () {
                        return objectModelFactory.getModelByName('Property');
                    },
                    tempId: function () {
                        return guidGenerator.generateRandomGuid();
                    }
                }
            });

            modalInstance.result.then(function (properties) {
                if (properties) {
                    if (properties.constructor === Array) {
                        for (var i = 0; i < properties.length; i++) {
                            propertiesCtrl.gridOptions.data.push(properties[i]);
                            updateHmlWithPropertyData(properties[i], false);
                        }

                        return;
                    }

                    updateHmlWithPropertyData(properties, false);
                }
            });
        };

        propertiesCtrl.removeProperty = function (property) {
            updateHmlWithPropertyData(property, true);
        };

        function updateHmlWithPropertyData(property, isDelete) {
            var hmlProperties = getThisPropertyArray(),
                propertiesIndex = getPropertyIndex(property);

            if (isDelete) {
                hmlProperties.splice(propertiesIndex, 1);
                return;
            }

            if (propertiesIndex === -1) {
                hmlProperties.push(property);
                return;
            }

            hmlProperties[propertiesIndex] = property;
        }

        function getPropertiesFromLocationString() {
            return getThisPropertyArray();
        }

        function getThisPropertyArray() {
            var propertyArray = [],
                locator = propertiesCtrl.parentCollectionPropertyAllocation;

            if (locator) {
                var obj = propertiesCtrl.hml;

                for (var i = 0; i < locator.length; i++) {
                    obj = obj[locator[i].propertyString];

                    if (locator[i].isArray && locator[i].propertyIndex > -1) {
                        obj = obj[locator[i].propertyIndex]
                    }
                }

                if (!obj) {
                    return [];
                }

                propertyArray = obj;
            }

            return propertyArray;
        }

        function getPropertyIndex (property) {
            return indexCollection.getCollectionItemIndex(getThisPropertyArray(), undefined, property.id);
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('properties', properties);
    properties.$inject = ['$scope', '$uibModal', 'gridCellTemplateFactory', 'indexCollection', 'objectModelFactory', 'guidGenerator', 'usSpinnerService'];
}());
