(function () {
    'use strict';

    function uploadService($http, $q, appConfig, headerFactory) {
        var service = {
            uploadHml: function (file, prefix) {
                var defer = $q.defer(),
                    url = appConfig.conversion_server_url + 'hml/' + prefix,
                    headers = headerFactory.uploadFileHeaders(),
                    formData = new FormData();

                formData.append('file', file);

                $http.post(url, formData, {
                    transformRequest: angular.identity,
                    headers: headers
                }).then(function (result) {
                    defer.resolve(result.data);
                });

                return defer.promise;
            },

            uploadFhir: function (file) {
                var defer = $q.defer(),
                    url = appConfig.conversion_server_url + 'fhir',
                    headers = headerFactory.uploadFileHeaders(),
                    formData = new FormData();

                formData.append('file', file);

                $http.post(url, formData, {
                    transformRequest: angular.identity,
                    headers: headers
                }).then(function (result) {
                    defer.resolve(result.data);
                });

                return defer.promise;
            }
        };

        return service;
    }

    angular.module('webHmlFhirConversionDashboardApp.services').service('uploadService', uploadService);
    uploadService.$inject = ['$http', '$q', 'appConfig', 'headerFactory'];
}());