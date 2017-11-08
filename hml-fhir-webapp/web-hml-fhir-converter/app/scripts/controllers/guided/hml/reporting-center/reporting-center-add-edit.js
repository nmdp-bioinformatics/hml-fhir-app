/**
 * Created by abrown3 on 1/18/17.
 */
(function () {
    'use strict';

    function reportingCenterAddEdit ($scope, $uibModalInstance, $uibModal, edit, reportingCenter, selectedReportingCenters, reportingCenterService, appConfig, toaster, typeaheadQueryBuilder, objectModelFactory) {
        /* jshint validthis: true */
        var reportingCenterAddEditCtrl = this;

        reportingCenterAddEditCtrl.scope = $scope;
        reportingCenterAddEditCtrl.formSubmitted = false;
        reportingCenterAddEditCtrl.edit = edit;
        reportingCenterAddEditCtrl.selectedReportingCenterContext = null;
        reportingCenterAddEditCtrl.selectedReportingCenterId= null;
        reportingCenterAddEditCtrl.selectedReportingCenter = null
        reportingCenterAddEditCtrl.maxQuery = { number: 10, text: '10' };
        reportingCenterAddEditCtrl.pageNumber = 0;
        reportingCenterAddEditCtrl.resultsPerPage = appConfig.resultsPerPage;
        reportingCenterAddEditCtrl.autoAdd = appConfig.autoAddOnNoResults;

        $scope.$on('reportingCenterAddEditCtrl.addedExternal.success', function (event, result) {
            $uibModalInstance.close(result);
        });

        if (reportingCenterAddEditCtrl.edit) {
            reportingCenterAddEditCtrl.selectedReportingCenter = reportingCenter;
            reportingCenterAddEditCtrl.selectedReportingCenterContext = reportingCenter.context;
            reportingCenterAddEditCtrl.selectedReportingCenterId= reportingCenter.centerId;
        }

        reportingCenterAddEditCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        reportingCenterAddEditCtrl.close = function () {
            $uibModalInstance.close();
        };

        reportingCenterAddEditCtrl.add = function (form) {
            reportingCenterAddEditCtrl.formSubmitted = true;

            if (!form.$invalid) {
                reportingCenterAddEditCtrl.formSubmitted = false;
                $uibModalInstance.close(reportingCenterAddEditCtrl.selectedReportingCenter);
            }
        };

        reportingCenterAddEditCtrl.selectReportingCenter = function (item) {
            reportingCenterAddEditCtrl.selectedReportingCenter = item;
            reportingCenterAddEditCtrl.selectedReportingCenterContext = item.context;
            reportingCenterAddEditCtrl.selectedReportingCenterId= item.centerId;
        };

        reportingCenterAddEditCtrl.getReportingCentersByContext = function (viewValue) {
            return getReportingCentersByTerm('context', viewValue);
        };

        reportingCenterAddEditCtrl.getReportingCentersById = function (viewValue) {
            return getReportingCentersByTerm('centerId', viewValue);
        };

        reportingCenterAddEditCtrl.reportingCenterChange = function () {
            reportingCenterAddEditCtrl.selectedReportingCenter = null;
        };

        function getReportingCentersByTerm(term, viewValue) {
            return reportingCenterService.getTypeaheadOptions(reportingCenterAddEditCtrl.maxQuery.number,
                typeaheadQueryBuilder.buildTypeaheadQueryWithSelectionExclusion(term, viewValue, false,
                    selectedReportingCenters, 'id')).then(function (response) {
                if (response.length > 0) {
                    return response;
                }

                if (reportingCenterAddEditCtrl.autoAdd) {
                    setTimeout(timeNoResults, appConfig.autoAddOnNoResultsTimer);
                }
            });
        }

        function createTypeAheadItemEntry() {
            var modalInstance = $uibModal.open({
                animation: true,
                controller: 'reportingCenterTerminologyAddEditModal',
                controllerAs: 'reportingCenterTerminologyAddEditModalCtrl',
                templateUrl: 'views/settings/hml/reporting-center/terminology/reporting-center-terminology-add-edit-modal.html',
                resolve: {
                    title: function () {
                        return 'Add Reporting Center Item';
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
                    toaster.pop({
                        type: 'info',
                        body: 'Successfully added Reporting Center entry.'
                    });

                    $scope.$emit('reportingCenterAddEditCtrl.addedExternal.success', result);
                }
            });
        }

        function timeNoResults() {
            if (reportingCenterAddEditCtrl.selectedReportingCenter === null) {

                toaster.pop({
                    type: 'info',
                    title: 'Add / Edit Reporting Center',
                    body: 'Not finding the data you need? Close this notification to be taken to add/edit page.',
                    toasterId: 1,
                    timeout: 0,
                    onHideCallback: function () {
                        createTypeAheadItemEntry();
                    }
                });
            }
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('reportingCenterAddEdit', reportingCenterAddEdit);
    reportingCenterAddEdit.$inject = ['$scope', '$uibModalInstance', '$uibModal', 'edit', 'reportingCenter', 'selectedReportingCenters', 'reportingCenterService', 'appConfig', 'toaster', 'typeaheadQueryBuilder', 'objectModelFactory'];
}());
