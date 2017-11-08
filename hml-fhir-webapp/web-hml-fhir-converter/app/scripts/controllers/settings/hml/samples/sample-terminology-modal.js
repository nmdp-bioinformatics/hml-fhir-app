/**
 * Created by abrown3 on 1/17/17.
 */
(function () {
    'use strict';

    function sampleTerminologyModal($scope, $uibModalInstance, title) {
        /* jshint validthis: true */
        var sampleTerminologyModalCtrl = this;

        sampleTerminologyModalCtrl.scope = $scope;
        sampleTerminologyModalCtrl.title = title;
        sampleTerminologyModalCtrl.panels = {
            collectionMethods: {
                show: false,
                controller: 'collectionMethodsTerminology',
                controllerAs: 'collectionMethodsTerminologyCtrl',
                templateUrl: 'views/settings/hml/samples/terminology/collection-methods-terminology.html'
            }
        };

        sampleTerminologyModalCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        sampleTerminologyModalCtrl.close = function () {
            $uibModalInstance.close();
        };

        sampleTerminologyModalCtrl.togglePanels = function (panel) {
            sampleTerminologyModalCtrl.panels[panel].show =
                !sampleTerminologyModalCtrl.panels[panel].show;
        };
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('sampleTerminologyModal', sampleTerminologyModal);
    sampleTerminologyModal.$inject = ['$scope', '$uibModalInstance', 'title'];
}());
