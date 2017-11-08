/**
 * Created by abrown3 on 1/4/17.
 */
(function () {
    'use strict';

    function hmlSettings($scope, $q, $uibModal) {
        /* jshint validthis: true */
        var hmlSettingsCtrl = this;

        hmlSettingsCtrl.scope = $scope;

        hmlSettingsCtrl.launchReportingCenter = function () {

        };

        hmlSettingsCtrl.launchHmlId = function () {

        };

        hmlSettingsCtrl.launchTypingTestNames = function () {

        };

        hmlSettingsCtrl.launchSamples = function () {

        };

        hmlSettingsCtrl.launchProperties = function () {

        };

        hmlSettingsCtrl.launchVersions = function () {

        };

        hmlSettingsCtrl.openTerminology = function (nodeName) {
            var modalConfig = {
                title: '',
                viewPath: '',
                controller: ''
            };

            switch (nodeName) {
                case 'samples':
                    modalConfig.title = 'Collection Methods Terminology Settings';
                    modalConfig.viewPath = 'views/settings/hml/samples/sample-terminology-modal.html';
                    modalConfig.controller = 'sampleTerminologyModal';
                    break;
                case 'typingTestNames':
                    modalConfig.title = 'Typing Test Names Terminology Settings';
                    modalConfig.viewPath = 'views/settings/hml/typing-test-names/typing-test-names-terminology-modal.html';
                    modalConfig.controller = 'typingTestNamesTerminologyModal';
                    break;
                case 'reportingCenters':
                    modalConfig.title = 'Reporting Center Terminology Settings';
                    modalConfig.viewPath = 'views/settings/hml/reporting-center/reporting-center-terminology-modal.html';
                    modalConfig.controller = 'reportingCenterTerminologyModal';
                    break;
                case 'properties':
                    modalConfig.title = 'Properties Terminology Settings';
                    modalConfig.viewPath = 'views/settings/hml/properties/properties-terminology-modal.html';
                    modalConfig.controller = 'propertiesTerminologyModal';
                    break;
                case 'hmlId':
                    modalConfig.title = 'HML ID Terminology Settings';
                    modalConfig.viewPath = 'views/settings/hml/hml-id/hml-id-terminology-modal.html';
                    modalConfig.controller = 'hmlIdTerminologyModal';
                    break;
                case 'versions':
                    modalConfig.title = 'Version Terminology Settings';
                    modalConfig.viewPath = 'views/settings/hml/versions/versions-terminology-modal.html';
                    modalConfig.controller = 'versionsTerminologyModal';
                    break;
                default:
                    return;
            }

            openModalSpecific(modalConfig.title, modalConfig.viewPath, modalConfig.controller).then(function (result) {

            });
        };

        function openModalSpecific(title, viewUrl, controller) {
            var defer = $q.defer(),
                modalInstance = $uibModal.open({
                    animation: true,
                    size: 'lg',
                    controller: controller,
                    controllerAs: controller + 'Ctrl',
                    templateUrl: viewUrl,
                    resolve: {
                        title: function() {
                            return title;
                        }
                    }
                });

            modalInstance.result.then(function (result) {
                defer.resolve(result);
            });

            return defer.promise;
        }

        function openModal (title, bodyTemplateUrl) {
            var defer = $q.defer(),
                modalInstance = $uibModal.open({
                    animation: true,
                    controller: 'hmlSettingsModal',
                    controllerAs: 'hmlSettingsModalCtrl',
                    templateUrl: 'views/settings/hml/hml-settings-modal.html',
                    resolve: {
                        title: function () {
                            return title;
                        },
                        bodyTemplateUrl: function () {
                            return bodyTemplateUrl;
                        }
                    }
                });

            modalInstance.result.then(function (result) {
                defer.resolve(result);
            });

            return defer.promise;
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('hmlSettings', hmlSettings);
    hmlSettings.$inject = ['$scope', '$q', '$uibModal'];
}());