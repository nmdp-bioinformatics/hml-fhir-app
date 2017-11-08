/**
 * Created by abrown3 on 1/23/17.
 */
(function () {
    'use strict';

    function hmlModelService($http, $q, appConfig, httpHeaderTransform) {
        var service = {
            getModel: function (modelName) {
                var url = '',
                    defer = $q.defer(),
                    headers = httpHeaderTransform.getHeaderForResourceServer(),
                    url = getUrl(modelName);

                $http({
                   method: 'GET',
                   url: url,
                   headers: headers
                }).success(function (result) {
                    defer.resolve(result);
                });

                return defer.promise;
            }
        };

        function getUrl(modelName) {
            var resource = modelName.charAt(0).toLowerCase() + modelName.substr(1, modelName.length);
            return appConfig.resource_server_base_url + resource;
        }

        return service;
    }

    angular.module('hmlFhirAngularClientApp.services').service('hmlModelService', hmlModelService);
    hmlModelService.$inject = ['$http', '$q', 'appConfig', 'httpHeaderTransform']
}());