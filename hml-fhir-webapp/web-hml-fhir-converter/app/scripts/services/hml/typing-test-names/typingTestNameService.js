/**
 * Created by abrown3 on 1/4/17.
 */
(function () {
    'use strict';

    function typingTestNameService($http, $q, httpHeaderTransform, appConfig) {
        var service = {
            getTypingTestNameTerminology: function (maxReturn, pageNumber) {
                maxReturn = maxReturn !== null && maxReturn > 0 ? maxReturn.toString() : '10';
                pageNumber = pageNumber !== null && pageNumber > -1 ? pageNumber.toString() : '0';

                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'typingTestName/' + maxReturn + '/' + pageNumber,
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

            addSingleTypingTestNameTerminology: function (typingTestName) {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'typingTestName',
                    headers = httpHeaderTransform.postHeaderForResourceServer();

                $http({
                    method: 'POST',
                    url: url,
                    data: [ typingTestName ],
                    headers: headers
                }).success(function (res) {
                    defer.resolve(res);
                });

                return defer.promise;
            },

            removeSingleTypingTestNameTerminology: function (typingTestName) {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'typingTestName',
                    headers = httpHeaderTransform.postHeaderForResourceServer();

                $http({
                    method: 'DELETE',
                    url: url,
                    data: typingTestName,
                    headers: headers
                }).success(function (res) {
                   defer.resolve(res);
                });

                return defer.promise;
            },

            updateSingleTypingTestNameTerminology: function (typingTestName) {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'typingTestName',
                    headers = httpHeaderTransform.postHeaderForResourceServer();

                $http({
                    method: 'PUT',
                    url: url,
                    data: typingTestName,
                    headers: headers
                }).success(function (res) {
                    defer.resolve(res);
                });

                return defer.promise;
            },

            getTypeaheadOptions: function (maxResults, query) {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'typingTestName/' + maxResults,
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

    angular.module('hmlFhirAngularClientApp.services').service('typingTestNameService', typingTestNameService);
    typingTestNameService.$inject = ['$http', '$q', 'httpHeaderTransform', 'appConfig'];
}());