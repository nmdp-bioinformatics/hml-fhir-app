/**
 * Created by abrown3 on 12/15/16.
 */
(function () {
    'use strict';

    function hmlModal ($scope, $uibModalInstance, title, bodyTemplateUrl, hmlModel, edit, templateController, newModel, usSpinnerService) {
        /* jshint validthis: true */
        var hmlModalCtrl = this,
            controllerData = templateController.getControllerNameByTemplateUrl(bodyTemplateUrl);

        hmlModalCtrl.newModel = newModel;
        hmlModalCtrl.scope = $scope;
        hmlModalCtrl.title = title;
        hmlModalCtrl.hml = hmlModel;
        hmlModalCtrl.edit = edit;
        hmlModalCtrl.bodyTemplateUrl = bodyTemplateUrl;
        hmlModalCtrl.controllerDeclaration = controllerData;

        $scope.$on('guided:hml:node:updated', function (event, result) {
            if (result) {
                $uibModalInstance.close(result);
            }
        });

        hmlModalCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        hmlModalCtrl.close = function () {
            $uibModalInstance.close();
        };

        hmlModalCtrl.update = function () {
            usSpinnerService.spin('index-spinner');
            $scope.$broadcast('guided:hml:node:update');
        };
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('hmlModal', hmlModal);
    hmlModal.$inject = ['$scope', '$uibModalInstance', 'title', 'bodyTemplateUrl', 'hmlModel', 'edit', 'templateController', 'newModel', 'usSpinnerService']
}());