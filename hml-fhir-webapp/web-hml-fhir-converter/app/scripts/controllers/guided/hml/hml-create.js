/**
 * Created by abrown3 on 1/24/17.
 */
(function () {
    'use strict';

    function hmlCreate ($scope, hml, title, $uibModalInstance, hmlService, versions, created) {
        /* jshint validthis: true */
        var hmlCreateCtrl = this;

        hmlCreateCtrl.scope = $scope;
        hmlCreateCtrl.created = created;
        hmlCreateCtrl.hml = hml;
        hmlCreateCtrl.title = title;
        hmlCreateCtrl.formSubmitted = false;
        hmlCreateCtrl.versions = versions;

        hmlCreateCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        hmlCreateCtrl.close = function () {
            $uibModalInstance.close();
        };

        hmlCreateCtrl.create = function (form) {
            hmlCreateCtrl.formSubmitted = true;

            if (!form.$invalid) {
                hmlCreateCtrl.formSubmitted = false;
                hmlService.createHml(hmlCreateCtrl.hml).then(function (result) {
                    if (result) {
                        $uibModalInstance.close(result);
                    }
                });
            }
        };

        hmlCreateCtrl.edit = function () {
            $uibModalInstance.close(hmlCreateCtrl.hml);
        };
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('hmlCreate', hmlCreate);
    hmlCreate.$inject = ['$scope', 'hml', 'title', '$uibModalInstance', 'hmlService', 'versions', 'created'];
}());