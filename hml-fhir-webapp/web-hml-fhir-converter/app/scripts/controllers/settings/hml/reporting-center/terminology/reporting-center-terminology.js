/**
 * Created by abrown3 on 1/12/17.
 */
(function () {
    'use strict';

    function reportingCenterTerminology($scope, reportingCenterService, $uibModal, toaster, objectModelFactory, gridCellTemplateFactory) {
        /* jshint validthis: true */
        var reportingCenterTerminologyCtrl = this,
            dateColumnTemplate = gridCellTemplateFactory.createDateCellTemplate(),
            activeColumnTemplate = gridCellTemplateFactory.createActiveCellTemplate(),
            deleteColumnTemplate = gridCellTemplateFactory.createDeleteCellTemplate();

        reportingCenterTerminologyCtrl.scope = $scope;
        reportingCenterTerminologyCtrl.maxQuery = 10;
        reportingCenterTerminologyCtrl.gridOptions = {
            data: [],
            enableSorting: true,
            showGridFooter: true,
            enableCellEditOnFocus: true,
            appScopeProvider: reportingCenterTerminologyCtrl,
            columnDefs: [
                { name: 'id', field: 'id', visible: false },
                { name: 'context', field: 'context', displayName: 'Context:', cellTooltip: function (row) { return row.entity.context; }, headerTooltip: function(col) { return col.displayName; } },
                { name: 'centerId', field: 'centerId', dsiplayName: 'Reporting Center ID', cellTooltip: function (row) { return row.entity.centerId; }, headerTooltip: function (col) { return col.displayName; } },
                { name: 'dateCreated', field: 'dateCreated', displayName: 'Date Created:', cellTemplate: dateColumnTemplate, cellTooltip: function (row) { return gridCellTemplateFactory.parseDate(row.entity.dateCreated); }, headerTooltip: function(col) { return col.displayName; } },
                { name: 'active', field: 'active', displayName: 'Modify', enableColumnMenu: false, cellTemplate: activeColumnTemplate, headerTooltip: function(col) { return col.displayName; } },
                { field: 'delete', displayName: 'Delete', maxWidth: 60, enableColumnMenu: false, cellTemplate: deleteColumnTemplate }
            ]
        };

        reportingCenterTerminologyCtrl.deleteItem = function (reportingCenter) {
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/common/confirmation-modal.html',
                controller: 'confirmationModal',
                controllerAs: 'confirmationModalCtrl',
                resolve: {
                    title: function () {
                        return 'Remove Reporting Center from Database?'
                    },
                    message: function () {
                        return 'This action cannot be undone, please ensure you would like to remove the entry with Context: ' +
                            reportingCenter.context + ' that was Created on: ' + reportingCenter.dateCreated;
                    }
                }
            });

            modalInstance.result.then(function (result) {
                if (result) {
                    reportingCenterService.removeSingleReportingCenterTerminology(reportingCenter).then(function (res) {
                        getReportingCenters();

                        if (res) {
                            toaster.pop({
                                type: 'info',
                                body: 'Successfully deleted Reporting Center entry.'
                            });
                        }
                    });
                }
            })
        };

        reportingCenterTerminologyCtrl.addItem = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                controller: 'reportingCenterTerminologyAddEditModal',
                controllerAs: 'reportingCenterTerminologyAddEditModalCtrl',
                templateUrl: 'views/settings/hml/reporting-center/terminology/reporting-center-terminology-add-edit-modal.html',
                resolve: {
                    title: function () {
                        return 'Add Typing Test Name Item';
                    },
                    reportingCenter: function () {
                        return objectModelFactory.getModelByName('ReportingCenter');
                    },
                    edit: function () {
                        return false;
                    }
                }
            });

            modalInstance.result.then(function (result) {
                if (result) {
                    getReportingCenters();

                    toaster.pop({
                        type: 'info',
                        body: 'Successfully added Reporting Center entry.'
                    });
                }
            });
        };

        reportingCenterTerminologyCtrl.editItem = function (reportingCenter) {
            var modalInstance = $uibModal.open({
                animation: true,
                controller: 'reportingCenterTerminologyAddEditModal',
                controllerAs: 'reportingCenterTerminologyAddEditModalCtrl',
                templateUrl: 'views/settings/hml/reporting-center/terminology/reporting-center-terminology-add-edit-modal.html',
                resolve: {
                    title: function () {
                        return 'Edit Reporting Center Item';
                    },
                    reportingCenter: function () {
                        return reportingCenter;
                    },
                    edit: function () {
                        return true;
                    }
                }
            });

            modalInstance.result.then(function (result) {
                if (result) {
                    getReportingCenters();

                    toaster.pop({
                        type: 'info',
                        body: 'Successfully edited Reporting Center entry.'
                    });
                }
            });
        };

        getReportingCenters();

        function getReportingCenters() {
            reportingCenterService.getReportingCenterTerminology(reportingCenterTerminologyCtrl.maxQuery).then(function (reportingCenters) {
                reportingCenterTerminologyCtrl.gridOptions.data = reportingCenters;
            });
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('reportingCenterTerminology', reportingCenterTerminology);
    reportingCenterTerminology.$inject = ['$scope', 'reportingCenterService', '$uibModal', 'toaster', 'objectModelFactory', 'gridCellTemplateFactory'];
}());
