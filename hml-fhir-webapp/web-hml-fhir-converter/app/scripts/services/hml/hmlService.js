/**
 * Created by abrown3 on 1/23/17.
 */
(function () {
    'use strict';

    function hmlService ($http, $q, httpHeaderTransform, appConfig) {
        var service = {
            getHmlTerminology: function (maxReturn, pageNumber) {
                maxReturn = maxReturn !== null && maxReturn > 0 ? maxReturn.toString() : '10';
                pageNumber = pageNumber !== null && pageNumber > -1 ? pageNumber.toString() : '0';

                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'hml/' + maxReturn + '/' + pageNumber,
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

            createHml: function (hml) {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'hml',
                    headers = httpHeaderTransform.postHeaderForResourceServer();

                $http({
                    method: 'POST',
                    url: url,
                    data: [ hml ],
                    headers: headers
                }).success(function (res) {
                    defer.resolve(res);
                });

                return defer.promise;
            },

            removeSingleHmlTerminology: function (hml) {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'hml',
                    headers = httpHeaderTransform.postHeaderForResourceServer();

                $http({
                    method: 'DELETE',
                    url: url,
                    data: hml,
                    headers: headers
                }).success(function (res) {
                    defer.resolve(res);
                });

                return defer.promise;
            },

            getHml: function (hmlId) {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'hml/' + hmlId,
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

            updateHml: function (hml) {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'hml',
                    headers = httpHeaderTransform.postHeaderForResourceServer();

                $http({
                    method: 'PUT',
                    url: url,
                    data: hml,
                    headers: headers
                }).success(function (res) {
                    defer.resolve(res);
                });

                return defer.promise;
            },

            getTypeaheadOptions: function (maxResults, query) {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'hml/' + maxResults,
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

    angular.module('hmlFhirAngularClientApp.services').service('hmlService', hmlService);
    hmlService.$inject = ['$http', '$q', 'httpHeaderTransform', 'appConfig'];
}());