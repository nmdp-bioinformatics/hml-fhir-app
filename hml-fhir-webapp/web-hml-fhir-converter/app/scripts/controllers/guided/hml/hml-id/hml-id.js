/**
 * Created by abrown3 on 12/15/16.
 */
(function () {
    'use strict';

    function hmlId ($scope, $uibModal) {
        /* jshint validthis: true */
        var hmlIdCtrl = this,
            parentCtrl = $scope.hmlModalCtrl;

        hmlIdCtrl.scope = $scope;
        hmlIdCtrl.hml = parentCtrl.hml;

        $scope.$on('guided:hml:node:update', function () {
            $scope.$broadcast('guided:hml:node:updated', hmlIdCtrl.hml);
        });

        hmlIdCtrl.editHmlId = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/guided/hml/hml-id/hml-id-add-edit.html',
                controller: 'hmlIdAddEdit',
                controllerAs: 'hmlIdAddEditCtrl',
                resolve: {
                    hmlModel: function () {
                        return hmlIdCtrl.hml;
                    }
                }
            });

            modalInstance.result.then(function (result) {
                if (result) {

                }
            });
        };
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('hmlId', hmlId);
    hmlId.$inject = ['$scope', '$uibModal'];
}());