/**
 * Created by abrown3 on 1/23/17.
 */
(function () {
    'use strict';

    function objectModelFactory(hmlModelService, $q) {
        var factory = {
            getModelByName: function (modelName) {
                var defer = $q.defer();

                hmlModelService.getModel(modelName).then(function (result) {
                    defer.resolve(result);
                });

                return defer.promise;
            }
        };

        return factory;
    }

    angular.module('hmlFhirAngularClientApp.factories').factory('objectModelFactory', objectModelFactory);
    objectModelFactory.$inject = ['hmlModelService', '$q'];
}());