/**
 * Created by abrown3 on 1/23/17.
 */
(function () {
    'use strict';

    function versionsTerminology($scope, versionService, $uibModal, toaster, objectModelFactory, gridCellTemplateFactory) {
        /* jshint validthis: true */
        var versionsTerminologyCtrl = this,
            dateColumnTemplate = gridCellTemplateFactory.createDateCellTemplate(),
            activeColumnTemplate = gridCellTemplateFactory.createActiveCellTemplate(),
            deleteColumnTemplate = gridCellTemplateFactory.createDeleteCellTemplate();

        versionsTerminologyCtrl.scope = $scope;
        versionsTerminologyCtrl.maxQuery = 10;
        versionsTerminologyCtrl.gridOptions = {
            data: [],
            enableSorting: true,
            showGridFooter: true,
            enableCellEditOnFocus: true,
            appScopeProvider: versionsTerminologyCtrl,
            columnDefs: [
                { name: 'id', field: 'id', visible: false },
                { name: 'name', field: 'name', displayName: 'Name:', cellTooltip: function (row) { return row.entity.name; }, headerTooltip: function(col) { return col.displayName; } },
                { name: 'description', field: 'description', displayName: 'Description:', cellTooltip: function (row) { return row.entity.description ;}, headerTooltip: function(col) { return col.displayName; } },
                { name: 'dateCreated', field: 'dateCreated', displayName: 'Date Created:', cellTemplate: dateColumnTemplate, cellTooltip: function (row) { return gridCellTemplateFactory.parseDate(row.entity.dateCreated); }, headerTooltip: function(col) { return col.displayName; } },
                { name: 'active', field: 'active', displayName: 'Modify', enableColumnMenu: false, cellTemplate: activeColumnTemplate, headerTooltip: function(col) { return col.displayName; } },
                { field: 'delete', displayName: 'Delete', maxWidth: 60, enableColumnMenu: false, cellTemplate: deleteColumnTemplate }
            ]
        };

        versionsTerminologyCtrl.deleteItem = function (typingTestName) {
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/common/confirmation-modal.html',
                controller: 'confirmationModal',
                controllerAs: 'confirmationModalCtrl',
                resolve: {
                    title: function () {
                        return 'Remove Version from Database?'
                    },
                    message: function () {
                        return 'This action cannot be undone, please ensure you would like to remove the entry with Name: ' +
                            typingTestName.name + ' and Description: ' + typingTestName.description + ' that was Created on: ' +
                            typingTestName.dateCreated;
                    }
                }
            });

            modalInstance.result.then(function (result) {
                if (result) {
                    versionService.removeSingleVersionTerminology(version).then(function (res) {
                        getVersions();

                        if (res) {
                            toaster.pop({
                                type: 'info',
                                body: 'Successfully deleted Version entry.'
                            });
                        }
                    });
                }
            })
        };

        versionsTerminologyCtrl.addItem = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                controller: 'versionsTerminologyAddEditModal',
                controllerAs: 'versionsTerminologyAddEditModalCtrl',
                templateUrl: 'views/settings/hml/versions/terminology/versions-terminology-add-edit-modal.html',
                resolve: {
                    title: function () {
                        return 'Add Version Item';
                    },
                    version: function () {
                        return objectModelFactory.getModelByName('Version');
                    },
                    edit: function () {
                        return false;
                    }
                }
            });

            modalInstance.result.then(function (result) {
                if (result) {
                    getVersions();

                    toaster.pop({
                        type: 'info',
                        body: 'Successfully added Version entry.'
                    });
                }
            });
        };

        versionsTerminologyCtrl.editItem = function (version) {
            var modalInstance = $uibModal.open({
                animation: true,
                controller: 'versionsTerminologyAddEditModal',
                controllerAs: 'versionsTerminologyAddEditModalCtrl',
                templateUrl: 'views/settings/hml/versions/terminology/versions-terminology-add-edit-modal.html',
                resolve: {
                    title: function () {
                        return 'Edit Version Item';
                    },
                    version: function () {
                        return version;
                    },
                    edit: function () {
                        return true;
                    }
                }
            });

            modalInstance.result.then(function (result) {
                if (result) {
                    getVersions();

                    toaster.pop({
                        type: 'info',
                        body: 'Successfully edited Version entry.'
                    });
                }
            });
        };

        getVersions();

        function getVersions() {
            versionService.getVersionTerminology(versionsTerminologyCtrl.maxQuery).then(function (versions) {
                versionsTerminologyCtrl.gridOptions.data = versions;
            });
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('versionsTerminology', versionsTerminology);
    versionsTerminology.$inject = ['$scope', 'versionService', '$uibModal', 'toaster', 'objectModelFactory', 'gridCellTemplateFactory'];
}());
