/**
 * Created by abrown3 on 1/4/17.
 */
(function () {
    'use strict';

    function hmlSettingsModal ($scope, $uibModalInstance, bodyTemplateUrl, templateController, title) {
        /* jshint validthis: true */
        var hmlSettingsModalCtrl = this,
            controllerData = templateController.getControllerNameByTemplateUrl(bodyTemplateUrl);

        $scope.$on('settings:hml:node:updated', function (event, result) {
            if (result) {
                $uibModalInstance.close(result);
            }
        });

        hmlSettingsModalCtrl.scope = $scope;
        hmlSettingsModalCtrl.title = title;
        hmlSettingsModalCtrl.bodyTemplateUrl = bodyTemplateUrl;
        hmlSettingsModalCtrl.controllerDeclaration = controllerData;

        hmlSettingsModalCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        hmlSettingsModalCtrl.close = function () {
            $uibModalInstance.close();
        };

        hmlSettingsModalCtrl.update = function () {
            $scope.$broadcast('settings:hml:node:update');
        };
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('hmlSettingsModal', hmlSettingsModal);
    hmlSettingsModal.$inject = ['$scope', '$uibModalInstance', 'bodyTemplateUrl', 'templateController', 'title'];
}());