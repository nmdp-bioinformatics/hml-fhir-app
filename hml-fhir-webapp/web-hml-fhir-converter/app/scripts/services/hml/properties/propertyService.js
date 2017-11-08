/**
 * Created by abrown3 on 1/20/17.
 */
(function () {
    'use strict';

    function propertyService($http, $q, httpHeaderTransform, appConfig) {
        var service = {
            getPropertyTerminology: function (maxReturn, pageNumber) {
                maxReturn = maxReturn !== null && maxReturn > 0 ? maxReturn.toString() : '10';
                pageNumber = pageNumber !== null && pageNumber > -1 ? pageNumber.toString() : '0';

                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'property/' + maxReturn + '/' + pageNumber,
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

            addSinglePropertyTerminology: function (property) {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'property',
                    headers = httpHeaderTransform.postHeaderForResourceServer();

                $http({
                    method: 'POST',
                    url: url,
                    data: [ property ],
                    headers: headers
                }).success(function (res) {
                    defer.resolve(res);
                });

                return defer.promise;
            },

            removeSinglePropertyTerminology: function (property) {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'property',
                    headers = httpHeaderTransform.postHeaderForResourceServer();

                $http({
                    method: 'DELETE',
                    url: url,
                    data: property,
                    headers: headers
                }).success(function (res) {
                    defer.resolve(res);
                });

                return defer.promise;
            },

            updateSinglePropertyTerminology: function (property) {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'property',
                    headers = httpHeaderTransform.postHeaderForResourceServer();

                $http({
                    method: 'PUT',
                    url: url,
                    data: property,
                    headers: headers
                }).success(function (res) {
                    defer.resolve(res);
                });

                return defer.promise;
            },

            getTypeaheadOptions: function (maxResults, query) {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'property/' + maxResults,
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

    angular.module('hmlFhirAngularClientApp.services').service('propertyService', propertyService);
    propertyService.inject = ['$http', '$q', 'httpHeaderTransform', 'appConfig'];
}());