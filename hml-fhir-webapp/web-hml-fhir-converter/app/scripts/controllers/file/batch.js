/**
 * Created by andrew on 5/23/17.
 */
(function () {
    'use strict';

    function batch ($scope, uploadService) {
        /*jshint validthis: true */
        var batchCtrl = this;

        batchCtrl.scope = $scope;

        batchCtrl.upload = function () {
            uploadService.uploadMultiPartFile(batchCtrl.fileUpload).then(function (result) {
                var test = result;
          });
        };
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('batch', batch);
    batch.$inject = ['$scope', 'uploadService'];
}());