/**
 * Created by andrew on 6/22/17.
 */
(function () {
  'use strict';

  function downloadService($window, $timeout, $q, appConfig) {
      var service = {
          downloadFhir: function (id, xml) {
            var defer = $q.defer(),
                url = appConfig.conversion_server_url + 'fhir/' + id;

            if (xml) {
              url += '/xml'
            }

            $timeout(function () {
                $window.location = url
            }, 1000).then(function () {
                defer.resolve('success');
            }, function () {
                defer.reject('error');
            });

            return defer.promise;
          },

          downloadHml: function (id, xml) {
            var defer = $q.defer(),
              url = appConfig.conversion_server_url + 'hml/' + id;

            if (xml) {
              url += '/xml'
            }

            $timeout(function () {
              $window.location = url
            }, 1000).then(function () {
              defer.resolve('success');
            }, function () {
              defer.reject('error');
            });

            return defer.promise;
          }
      };

      return service;
  }

  angular.module('webHmlFhirConversionDashboardApp.services').service('downloadService', downloadService);
  downloadService.$inject = ['$window', '$timeout', '$q', 'appConfig'];
}());
