/**
 * Created by abrown3 on 1/17/17.
 */
(function () {
    'use strict';

    function propertiesTerminologyModal($scope, $uibModalInstance, title) {
        /* jshint validthis: true */
        var propertiesTerminologyModalCtrl = this;


        propertiesTerminologyModalCtrl.scope = $scope;
        propertiesTerminologyModalCtrl.title = title;
        propertiesTerminologyModalCtrl.panels = {
            properties: {
                show: false,
                controller: 'propertiesTerminology',
                controllerAs: 'propertiesTerminologyCtrl',
                templateUrl: 'views/settings/hml/properties/terminology/properties-terminology.html'
            }
        };

        propertiesTerminologyModalCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        propertiesTerminologyModalCtrl.close = function () {
            $uibModalInstance.close();
        };

        propertiesTerminologyModalCtrl.togglePanels = function (panel) {
            propertiesTerminologyModalCtrl.panels[panel].show =
                !propertiesTerminologyModalCtrl.panels[panel].show;
        };
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('propertiesTerminologyModal', propertiesTerminologyModal);
    propertiesTerminologyModal.$inject = ['$scope', '$uibModalInstance', 'title'];
}());