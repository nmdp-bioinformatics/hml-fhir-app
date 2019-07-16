(function () {
  'use strict';

  var appConfig = {
      'conversion_server_url': 'http://serviceconversion:8099/v1/'
  };

  angular.module('webHmlFhirConversionDashboardApp.config').constant('appConfig', appConfig);
}());
