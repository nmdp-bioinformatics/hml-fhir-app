/**
 * Created by abrown3 on 12/15/16.
 */
(function () {
    'use strict';

    function reportingCenter ($scope, $uibModal, gridCellTemplateFactory, hmlService) {
        /* jshint validthis: true */
        var reportingCenterCtrl = this,
            parentCtrl = $scope.hmlModalCtrl,
            deleteColumnTemplate = gridCellTemplateFactory.createRemoveCellTemplate();

        reportingCenterCtrl.scope = $scope;
        reportingCenterCtrl.hml = parentCtrl.hml;
        reportingCenterCtrl.gridOptions = {
            data: reportingCenterCtrl.hml.reportingCenters,
            enableSorting: true,
            showGridFooter: true,
            appScopeProvider: reportingCenterCtrl,
            columnDefs: [
                { name: 'id', field: 'id', visible: false },
                { name: 'context', field: 'context', displayName: 'Context:', cellTooltip: function (row) { return row.entity.context; }, headerTooltip: function(col) { return col.displayName; } },
                { name: 'centerId', field: 'centerId', dsiplayName: 'Reporting Center ID', cellTooltip: function (row) { return row.entity.centerId; }, headerTooltip: function (col) { return col.displayName; } },
                { field: 'delete', displayName: 'Remove', maxWidth: 75, enableColumnMenu: false, cellTemplate: deleteColumnTemplate }
            ]
        };

        $scope.$on('guided:hml:node:update', function () {
            hmlService.updateHml(reportingCenterCtrl.hml).then(function (result) {
                if (result) {
                    $scope.$emit('guided:hml:node:updated', result);
                }
            });
        });

        reportingCenterCtrl.addReportingCenterEntry = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/guided/hml/reporting-center/reporting-center-add-edit.html',
                controller: 'reportingCenterAddEdit',
                controllerAs: 'reportingCenterAddEditCtrl',
                resolve: {
                    edit: function () {
                        return false;
                    },
                    reportingCenter: function () {
                        return undefined;
                    },
                    selectedReportingCenters: function () {
                        var reportingCenters = reportingCenterCtrl.gridOptions.data,
                            idArray = [];

                        for (var i = 0; i < reportingCenters.length; i++) {
                            idArray.push(reportingCenters[i].id)
                        }

                        return idArray;
                    }
                }
            });

            modalInstance.result.then(function (typingTestName) {
                if (typingTestName) {
                    if (typingTestName.constructor === Array) {
                        for (var i = 0; i < typingTestName.length; i++) {
                            reportingCenterCtrl.gridOptions.data.push(typingTestName[i]);
                        }

                        return;
                    }

                    reportingCenterCtrl.gridOptions.data.push(typingTestName);
                }
            });
        };

        reportingCenterCtrl.removeReportingCenter = function (reportingCenter) {
            reportingCenterCtrl.gridOptions.data.splice(getReportingCenterIndex(reportingCenter), 1);
        };

        function getReportingCenterIndex (reportingCenter) {
            for (var i = 0; i < reportingCenterCtrl.gridOptions.data.length; i++) {
                if (reportingCenterCtrl.gridOptions.data[i].id === reportingCenter.id) {
                    return i;
                }
            }

            return undefined;
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('reportingCenter', reportingCenter);
    reportingCenter.$inject = ['$scope', '$uibModal', 'gridCellTemplateFactory', 'hmlService'];
}());