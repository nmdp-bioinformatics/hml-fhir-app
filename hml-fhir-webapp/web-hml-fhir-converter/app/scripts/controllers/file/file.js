/**
 * Created by abrown3 on 12/6/16.
 */
(function () {
    'use strict';

    function file ($scope, $uibModalInstance, title) {
        /*jshint validthis: true */
        var fileCtrl = this;

        fileCtrl.scope = $scope;
        fileCtrl.title = title;
        fileCtrl.mode = 1; // this will set it to 'upload' mode.
        fileCtrl.files = [];

        fileCtrl.close = function () {
            $uibModalInstance.close(true);
        };

        fileCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        fileCtrl.instructions = function () {
            if (fileCtrl.mode > 1) {
                fileCtrl.mode = 1;
            } else {
                fileCtrl.mode = 2;  // This sets it to 'instruction' mode.
            }
        };

        fileCtrl.parseFileSize = function (file) {
            return printFileSize(file);
        };

        fileCtrl.removeFileFromList = function (file) {
            var index = getFileIndex(file);
            fileCtrl.files.splice(index, 1);
        };

        fileCtrl.uploadList = function () {
            $uibModalInstance.close(fileCtrl.files);
        };

        $scope.fileInputChange = function (element) {
            for (var i = 0; i < element.files.length; i++) {
                handleFileChange(element.files[i]);
            }

            $scope.$apply();
        };

        function handleFileChange (file) {
            if (!fileExistsInModel(file)) {
                fileCtrl.files.push(file);
            }
        }

        function fileExistsInModel (file) {
            for (var i = 0; i < fileCtrl.files.length; i++) {
                if (file.name === fileCtrl.files[i].name &&
                    file.lastModified === fileCtrl.files[i].lastModified) {
                    return true;
                }
            }

            return false;
        }

        function getFileIndex (file) {
            for (var i = 0; i < fileCtrl.files.length; i++) {
                if (file.name === fileCtrl.files[i].name &&
                    file.lastModified === fileCtrl.files[i].lastModified) {
                    return i;
                }
            }

            return undefined;
        }

        function printFileSize (file) {
            var bytes = file.size;

            if (bytes < 100000) {
                var kb = bytes / 1000;
                return kb + ' Kb';
            }

            if (bytes >= 100000) {
                var mb = bytes / 1000000;
                return mb + ' Mb';
            }
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('file', file);
    file.$inject = ['$scope', '$uibModalInstance', 'title'];
}());