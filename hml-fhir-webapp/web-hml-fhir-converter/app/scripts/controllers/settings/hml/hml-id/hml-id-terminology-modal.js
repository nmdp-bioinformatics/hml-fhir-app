/**
 * Created by abrown3 on 1/17/17.
 */
(function () {
    'use strict';

    function hmlIdTerminologyModal($scope, $uibModalInstance, title) {
        /* jshint validthis: true */
        var hmlIdTerminologyModalCtrl = this;


        hmlIdTerminologyModalCtrl.scope = $scope;
        hmlIdTerminologyModalCtrl.title = title;
        hmlIdTerminologyModalCtrl.panels = {
            hmlId: {
                show: false,
                controller: 'hmlIdTerminology',
                controllerAs: 'hmlIdTerminologyCtrl',
                templateUrl: 'views/settings/hml/hml-id/terminology/hml-id-terminology.html'
            }
        };

        hmlIdTerminologyModalCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        hmlIdTerminologyModalCtrl.close = function () {
            $uibModalInstance.close();
        };

        hmlIdTerminologyModalCtrl.togglePanels = function (panel) {
            hmlIdTerminologyModalCtrl.panels[panel].show =
                !hmlIdTerminologyModalCtrl.panels[panel].show;
        };
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('hmlIdTerminologyModal', hmlIdTerminologyModal);
    hmlIdTerminologyModal.$inject = ['$scope', '$uibModalInstance', 'title'];
}());