/**
 * Created by abrown3 on 1/12/17.
 */
(function () {
    'use strict';

    function reportingCenterService($http, $q, httpHeaderTransform, appConfig) {
        var service = {
            getReportingCenterTerminology: function (maxReturn, pageNumber) {
                maxReturn = maxReturn !== null && maxReturn > 0 ? maxReturn.toString() : '10';
                pageNumber = pageNumber !== null && pageNumber > -1 ? pageNumber.toString() : '0';

                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'reportingCenter/' + maxReturn + '/' + pageNumber,
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

            addSingleReportingCenterTerminology: function (reportingCenter) {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'reportingCenter',
                    headers = httpHeaderTransform.postHeaderForResourceServer();

                $http({
                    method: 'POST',
                    url: url,
                    data: [ reportingCenter ],
                    headers: headers
                }).success(function (res) {
                    defer.resolve(res);
                });

                return defer.promise;
            },

            removeSingleReportingCenterTerminology: function (reportingCenter) {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'reportingCenter',
                    headers = httpHeaderTransform.postHeaderForResourceServer();

                $http({
                    method: 'DELETE',
                    url: url,
                    data: reportingCenter,
                    headers: headers
                }).success(function (res) {
                    defer.resolve(res);
                });

                return defer.promise;
            },

            updateSingleReportingCenterTerminology: function (reportingCenter) {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'reportingCenter',
                    headers = httpHeaderTransform.postHeaderForResourceServer();

                $http({
                    method: 'PUT',
                    url: url,
                    data: reportingCenter,
                    headers: headers
                }).success(function (res) {
                    defer.resolve(res);
                });

                return defer.promise;
            },

            getTypeaheadOptions: function (maxResults, query) {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'reportingCenter/' + maxResults,
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

    angular.module('hmlFhirAngularClientApp.services').service('reportingCenterService', reportingCenterService);
    reportingCenterService.inject = ['$http', '$q', 'httpHeaderTransform', 'appConfig'];
}());