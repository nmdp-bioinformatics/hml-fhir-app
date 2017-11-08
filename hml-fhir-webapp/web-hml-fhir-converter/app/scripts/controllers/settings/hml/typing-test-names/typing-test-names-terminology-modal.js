/**
 * Created by abrown3 on 1/17/17.
 */
(function () {
    'use strict';

    function typingTestNamesTerminologyModal($scope, $uibModalInstance, title) {
        /* jshint validthis: true */
        var typingTestNamesTerminologyModalCtrl = this;


        typingTestNamesTerminologyModalCtrl.scope = $scope;
        typingTestNamesTerminologyModalCtrl.title = title;
        typingTestNamesTerminologyModalCtrl.panels = {
            typingTestNames: {
                show: false,
                controller: 'typingTestNamesTerminology',
                controllerAs: 'typingTestNamesTerminologyCtrl',
                templateUrl: 'views/settings/hml/typing-test-names/terminology/typing-test-names-terminology.html'
            }
        };

        typingTestNamesTerminologyModalCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        typingTestNamesTerminologyModalCtrl.close = function () {
            $uibModalInstance.close();
        };

        typingTestNamesTerminologyModalCtrl.togglePanels = function (panel) {
            typingTestNamesTerminologyModalCtrl.panels[panel].show =
                !typingTestNamesTerminologyModalCtrl.panels[panel].show;
        };
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('typingTestNamesTerminologyModal', typingTestNamesTerminologyModal);
    typingTestNamesTerminologyModal.$inject = ['$scope', '$uibModalInstance', 'title'];
}());