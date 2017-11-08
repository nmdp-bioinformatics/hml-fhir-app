(function () {
  'use strict';

  function headerFactory() {
      var factory = {
          conversionServiceHeaders: function () {
              return {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
              };
          },

          uploadFileHeaders: function () {
              return {
                  'Content-Type': undefined
              }
          }
      };

      return factory;
  }

  angular.module('webHmlFhirConversionDashboardApp.factories').factory('headerFactory', headerFactory);
  headerFactory.$inject = [];
}());
