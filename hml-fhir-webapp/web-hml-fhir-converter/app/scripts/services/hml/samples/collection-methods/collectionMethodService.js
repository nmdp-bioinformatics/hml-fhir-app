/**
 * Created by abrown3 on 1/13/17.
 */
(function () {
    'use strict';

    function collectionMethodService($http, $q, httpHeaderTransform, appConfig) {
        var service = {
            getCollectionMethodTerminology: function (maxReturn, pageNumber) {
                maxReturn = maxReturn !== null && maxReturn > 0 ? maxReturn.toString() : '10';
                pageNumber = pageNumber !== null && pageNumber > -1 ? pageNumber.toString() : '0';

                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'collectionMethod/' + maxReturn + '/' + pageNumber,
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

            addSingleCollectionMethodTerminology: function (collectionMethod) {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'collectionMethod',
                    headers = httpHeaderTransform.postHeaderForResourceServer();

                $http({
                    method: 'POST',
                    url: url,
                    data: [ collectionMethod ],
                    headers: headers
                }).success(function (res) {
                    defer.resolve(res);
                });

                return defer.promise;
            },

            removeCollectionMethodTerminology: function (collectionMethod) {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'collectionMethod',
                    headers = httpHeaderTransform.postHeaderForResourceServer();

                $http({
                    method: 'DELETE',
                    url: url,
                    data: collectionMethod,
                    headers: headers
                }).success(function (res) {
                    defer.resolve(res);
                });

                return defer.promise;
            },

            updateCollectionMethodTerminology: function (collectionMethod) {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'collectionMethod',
                    headers = httpHeaderTransform.postHeaderForResourceServer();

                $http({
                    method: 'PUT',
                    url: url,
                    data: collectionMethod,
                    headers: headers
                }).success(function (res) {
                    defer.resolve(res);
                });

                return defer.promise;
            },

            getTypeaheadOptions: function (maxResults, query) {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'collectionMethod/' + maxResults,
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

    angular.module('hmlFhirAngularClientApp.services').service('collectionMethodService', collectionMethodService);
    collectionMethodService.$inject = ['$http', '$q', 'httpHeaderTransform', 'appConfig'];
}());