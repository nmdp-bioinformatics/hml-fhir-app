/**
 * Created by abrown3 on 1/24/17.
 */
(function () {
    'use strict';

    function projectService ($http, $q, appConfig, httpHeaderTransform) {
        var service = {

        };

        return service;
    }

    angular.module('hmlFhirAngularClientApp.services').service('projectService', projectService);
    projectService.$inject = ['$http', '$q', 'appConfig', 'httpHeaderTransform']
}());