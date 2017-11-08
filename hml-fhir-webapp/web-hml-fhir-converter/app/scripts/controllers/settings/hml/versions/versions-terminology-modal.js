/**
 * Created by abrown3 on 1/23/17.
 */
(function () {
    'use strict';

    function versionsTerminologyModal($scope, $uibModalInstance, title) {
        /* jshint validthis: true */
        var versionsTerminologyModalCtrl = this;


        versionsTerminologyModalCtrl.scope = $scope;
        versionsTerminologyModalCtrl.title = title;
        versionsTerminologyModalCtrl.panels = {
            versions: {
                show: false,
                controller: 'versionsTerminology',
                controllerAs: 'versionsTerminologyCtrl',
                templateUrl: 'views/settings/hml/versions/terminology/versions-terminology.html'
            }
        };

        versionsTerminologyModalCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        versionsTerminologyModalCtrl.close = function () {
            $uibModalInstance.close();
        };

        versionsTerminologyModalCtrl.togglePanels = function (panel) {
            versionsTerminologyModalCtrl.panels[panel].show =
                !versionsTerminologyModalCtrl.panels[panel].show;
        };
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('versionsTerminologyModal', versionsTerminologyModal);
    versionsTerminologyModal.$inject = ['$scope', '$uibModalInstance', 'title'];
}());