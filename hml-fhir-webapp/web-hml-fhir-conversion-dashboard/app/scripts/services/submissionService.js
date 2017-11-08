(function () {
  'use strict';

  function submissionService($http, $q, headerFactory, appConfig) {
    var service = {
        getSubmission: function (submissionId) {
          var defer = $q.defer(),
            url = appConfig.conversion_server_url + 'submission/' + submissionId + "/true";

          $http({
            method: 'GET',
            url: url,
            headers: headerFactory.conversionServiceHeaders()
          }).then(function (result) {
            defer.resolve(result.data);
          });

          return defer.promise;
        }
    };

    return service;
  }

  angular.module('webHmlFhirConversionDashboardApp.services').service('submissionService', submissionService);
  submissionService.$inject = ['$http', '$q', 'headerFactory', 'appConfig'];
}());
