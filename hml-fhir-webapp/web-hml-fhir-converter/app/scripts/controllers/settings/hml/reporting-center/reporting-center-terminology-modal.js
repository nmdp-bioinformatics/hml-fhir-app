/**
 * Created by abrown3 on 1/17/17.
 */
(function () {
    'use strict';

    function reportingCenterTerminologyModal($scope, $uibModalInstance, title) {
        /* jshint validthis: true */
        var reportingCenterTerminologyModalCtrl = this;


        reportingCenterTerminologyModalCtrl.scope = $scope;
        reportingCenterTerminologyModalCtrl.title = title;
        reportingCenterTerminologyModalCtrl.panels = {
            reportingCenter: {
                show: false,
                controller: 'reportingCenterTerminology',
                controllerAs: 'reportingCenterTerminologyCtrl',
                templateUrl: 'views/settings/hml/reporting-center/terminology/reporting-center-terminology.html'
            }
        };

        reportingCenterTerminologyModalCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        reportingCenterTerminologyModalCtrl.close = function () {
            $uibModalInstance.close();
        };

        reportingCenterTerminologyModalCtrl.togglePanels = function (panel) {
            reportingCenterTerminologyModalCtrl.panels[panel].show =
                !reportingCenterTerminologyModalCtrl.panels[panel].show;
        };
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('reportingCenterTerminologyModal', reportingCenterTerminologyModal);
    reportingCenterTerminologyModal.$inject = ['$scope', '$uibModalInstance', 'title'];
}());