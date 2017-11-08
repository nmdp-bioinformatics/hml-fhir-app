/**
 * Created by abrown3 on 3/14/17.
 */
(function () {
    'use strict';

    function conversion ($http, $q, httpHeaderTransform, appConfig) {
        var service = {
            convertXmlHmlToObject: function (xml) {
                var defer = $q.defer(),
                    url = appConfig.resource_server_base_url + 'hml/convert',
                    headers = httpHeaderTransform.postHeaderForResourceServer();

                $http({
                    method: 'POST',
                    url: url,
                    headers: headers,
                    data: xml
                }).success(function (res) {
                    defer.resolve(res);
                });

                return defer.promise;
            }
        };

        return service;
    }

    angular.module('hmlFhirAngularClientApp.services').service('conversion', conversion);
    conversion.$inject = ['$http', '$q', 'httpHeaderTransform', 'appConfig'];
}());