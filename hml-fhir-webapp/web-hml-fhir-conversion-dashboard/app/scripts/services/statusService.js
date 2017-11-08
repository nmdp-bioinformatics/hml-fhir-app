(function () {
  'use strict';

  function statusService($http, $q, headerFactory, appConfig) {
      var service = {
          getStatus: function (maxResults) {
              var defer = $q.defer(),
                  url = appConfig.conversion_server_url + 'status/' + maxResults;

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

  angular.module('webHmlFhirConversionDashboardApp.services').service('statusService', statusService);
  statusService.$inject = ['$http', '$q', 'headerFactory', 'appConfig'];
}());
