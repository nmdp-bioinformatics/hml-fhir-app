/**
 * Created by abrown3 on 1/12/17.
 */
(function () {
    'use strict';

    function reportingCenterTerminologyAddEditModal($scope, $uibModalInstance, reportingCenter, title, edit, reportingCenterService) {
        /* jshint validthis: true */
        var reportingCenterTerminologyAddEditModalCtrl = this;

        reportingCenterTerminologyAddEditModalCtrl.scope = $scope;
        reportingCenterTerminologyAddEditModalCtrl.reportingCenter = reportingCenter;
        reportingCenterTerminologyAddEditModalCtrl.title = title;
        reportingCenterTerminologyAddEditModalCtrl.formSubmitted = false;
        reportingCenterTerminologyAddEditModalCtrl.edit = edit;

        reportingCenterTerminologyAddEditModalCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        reportingCenterTerminologyAddEditModalCtrl.close = function () {
            $uibModalInstance.close();
        };

        reportingCenterTerminologyAddEditModalCtrl.save = function () {
            reportingCenterTerminologyAddEditModalCtrl.formSubmitted = true;

            if (!reportingCenterTerminologyAddEditModalCtrl.terminologyForm.$invalid) {
                if (reportingCenterTerminologyAddEditModalCtrl.edit) {
                    reportingCenterService.updateReportingCenterTerminology(reportingCenterTerminologyAddEditModalCtrl.reportingCenter).then(function (result) {
                        if (result) {
                            reportingCenterTerminologyAddEditModalCtrl.formSubmitted = false;
                            $uibModalInstance.close(result);
                        }
                    });
                } else {
                    reportingCenterService.addSingleReportingCenterTerminology(reportingCenterTerminologyAddEditModalCtrl.reportingCenter).then(function (result) {
                        if (result) {
                            reportingCenterTerminologyAddEditModalCtrl.formSubmitted = false;
                            $uibModalInstance.close(result);
                        }
                    });
                }
            }
        };
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('reportingCenterTerminologyAddEditModal', reportingCenterTerminologyAddEditModal);
    reportingCenterTerminologyAddEditModal.$inject = ['$scope', '$uibModalInstance', 'reportingCenter', 'title', 'edit', 'reportingCenterService'];
}());