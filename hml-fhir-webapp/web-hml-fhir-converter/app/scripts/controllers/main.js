(function () {
    'use strict';

    /**
     * @ngdoc function
     * @name hmlFhirAngularClientApp.controller:mainCtrl
     * @description
     * # mainCtrl
     * Controller of the hmlFhirAngularClientApp
     */

    function main($scope, $uibModal, $location) {
        /*jshint validthis: true */
        var mainCtrl = this;

        mainCtrl.scope = $scope;

        mainCtrl.launchFileConverter = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/file/file.html',
                controller: 'file',
                controllerAs: 'fileCtrl',
                resolve: {
                    title: function () {
                        return 'Upload HML file to parse.';
                    }
                }
            });

            modalInstance.result.then(function (result) {
                if (result) {
                    var uploadModal = $uibModal.open({
                        animation: true,
                        templateUrl: 'views/file/upload.html',
                        controller: 'upload',
                        controllerAs: 'uploadCtrl',
                        resolve: {
                            files: function () {
                                return result;
                            }
                        }
                    });

                    uploadModal.result.then(function (uploadResult) {
                        if (uploadResult) {

                        }
                    });
                }
            });
        };

        mainCtrl.launchGuidedUiConverter = function () {
            $location.path('/guided');
        };

        mainCtrl.launchSettings = function () {
            $location.path('/settings');
        };
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('main', main);
    main.$inject = ['$scope', '$uibModal', '$location'];
}());