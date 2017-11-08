/**
 * Created by abrown3 on 1/23/17.
 */
(function () {
    'use strict';

    function versionService($http, $q, httpHeaderTransform, appConfig) {
        var service = {
            getVersionTerminology: function (maxReturn, pageNumber) {
                maxReturn = maxReturn !== null && maxReturn > 0 ? maxReturn.toString() : '10';
                pageNumber = pageNumber !== null && pageNumber > -1 ? pageNumber.toString() : '0';

                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'version/' + maxReturn + '/' + pageNumber,
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

            getAllVersions: function () {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'version/all',
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

            addSingleVersionTerminology: function (version) {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'version',
                    headers = httpHeaderTransform.postHeaderForResourceServer();

                $http({
                    method: 'POST',
                    url: url,
                    data: [ version ],
                    headers: headers
                }).success(function (res) {
                    defer.resolve(res);
                });

                return defer.promise;
            },

            removeSingleVersionTerminology: function (version) {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'version',
                    headers = httpHeaderTransform.postHeaderForResourceServer();

                $http({
                    method: 'DELETE',
                    url: url,
                    data: version,
                    headers: headers
                }).success(function (res) {
                    defer.resolve(res);
                });

                return defer.promise;
            },

            updateSingleVersionTerminology: function (version) {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'version',
                    headers = httpHeaderTransform.postHeaderForResourceServer();

                $http({
                    method: 'PUT',
                    url: url,
                    data: version,
                    headers: headers
                }).success(function (res) {
                    defer.resolve(res);
                });

                return defer.promise;
            },

            getTypeaheadOptions: function (maxResults, query) {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'version/' + maxResults,
                    headers = httpHeaderTransform.postHeaderForResourceServer();

                $http({
                    method: 'POST',
                    url: url,
                    data: query,
                    headers: headers
                }).success(function (res) {
                    defer.resolve(res);
                });

                return defer.promise;
            },

            getDefaultVersion: function () {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'version/default',
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
        };

        return service;
    }

    angular.module('hmlFhirAngularClientApp.services').service('versionService', versionService);
    versionService.$inject = ['$http', '$q', 'httpHeaderTransform', 'appConfig'];
}());