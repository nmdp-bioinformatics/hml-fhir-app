/**
 * Created by abrown3 on 1/20/17.
 */
(function () {
    'use strict';

    function sampleService($http, $q, httpHeaderTransform, appConfig) {
        var service = {
            getSampleTerminology: function (maxReturn, pageNumber) {
                maxReturn = maxReturn !== null && maxReturn > 0 ? maxReturn.toString() : '10';
                pageNumber = pageNumber !== null && pageNumber > -1 ? pageNumber.toString() : '0';

                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'sample/' + maxReturn + '/' + pageNumber,
                    headers = httpHeaderTransform.getHeaderForResourceServer();

                $http({
                    method: 'GET',
                    url: url,
                    headers: headers
                }).success(function (res) {
                    defer.resolve(res);
                });

                return defer.promise;
            },

            addSingleSampleTerminology: function (sample) {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'sample',
                    headers = httpHeaderTransform.postHeaderForResourceServer();

                $http({
                    method: 'POST',
                    url: url,
                    data: [ sample ],
                    headers: headers
                }).success(function (res) {
                    defer.resolve(res);
                });

                return defer.promise;
            },

            removeSingleSampleTerminology: function (sample) {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'sample',
                    headers = httpHeaderTransform.postHeaderForResourceServer();

                $http({
                    method: 'DELETE',
                    url: url,
                    data: sample,
                    headers: headers
                }).success(function (res) {
                    defer.resolve(res);
                });

                return defer.promise;
            },

            updateSingleSampleTerminology: function (sample) {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'sample',
                    headers = httpHeaderTransform.postHeaderForResourceServer();

                $http({
                    method: 'PUT',
                    url: url,
                    data: sample,
                    headers: headers
                }).success(function (res) {
                    defer.resolve(res);
                });

                return defer.promise;
            },

            getTypeaheadOptions: function (maxResults, query) {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'sample/' + maxResults,
                    headers = httpHeaderTransform.postHeaderForResourceServer();

                $http({
                    method: 'POST',
                    url: url,
                    data: query,
                    headers: headers
                }).success(function (res) {
                    defer.resolve(res);
                })

                return defer.promise;
            }
        };

        return service;
    }

    angular.module('hmlFhirAngularClientApp.services').service('sampleService', sampleService);
    sampleService.inject = ['$http', '$q', 'httpHeaderTransform', 'appConfig'];
}());