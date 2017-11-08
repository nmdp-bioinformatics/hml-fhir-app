/**
 * Created by abrown3 on 12/8/16.
 */
(function () {
    'use strict';

    function upload ($scope, files, $uibModal, $uibModalInstance, uploadService, toaster, $location, conversion) {
        /*jshint validthis: true */
        var uploadCtrl = this;

        uploadCtrl.files = addUploadedBytesToFiles(files);
        uploadCtrl.scope = $scope;

        uploadCtrl.cancel = function () {
            $uibModalInstance.cancel();
        };

        uploadCtrl.close = function () {
            $uibModalInstance.close();
        };

        uploadCtrl.convert = function (file) {
            conversion.convertXmlHmlToObject(file.xml).then(function (hml) {
                
            });
        };

        uploadCtrl.displayMiringWarnings = function (warnings, file) {
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/file/hml-validation-warnings.html',
                controller: 'hmlValidationWarnings',
                controllerAs: 'hmlValidationWarningsCtrl',
                resolve: {
                    warnings: function () {
                        return warnings;
                    },
                    file: function () {
                        return file;
                    }
                }
            });

            modalInstance.result.then(function (result) {
                if (result) {
                    for (var i = 0; i < uploadCtrl.files.length; i++) {
                        if (fileEqual(uploadCtrl.files[i], result)) {
                            uploadCtrl.files.splice(i, 1);
                        }
                    }

                    if (uploadCtrl.files.length === 0) {
                        toaster.pop({
                            type: 'info',
                            body: 'You have no more files left to convert, dismiss this notification to navigate home.',
                            onHideCallback: function () {
                                $uibModalInstance.close();
                                $location.path('/');
                            }
                        });
                    }
                }
            });
        };

        function addUploadedBytesToFiles (files) {
            for (var i = 0; i < files.length; i++) {
                files[i].uploadedBytes = 0;
                files[i].percentageUploaded = 0;
            }

            sendFilesForUpload(files);

            return files;
        }

        function sendFilesForUpload (files) {
            var parsedFiles = [];

            for (var i = 0; i < files.length; i++) {
                var reader = new FileReader();

                reader.fileObj = files[i];

                reader.onload = function (f) {
                    reader.fileObj.xml = f.target.result;
                    parsedFiles.push(reader.fileObj);

                    if (parsedFiles.length === files.length) {
                        for (var j = 0; j < parsedFiles.length; j++) {
                            uploadService.uploadFileToServer(parsedFiles[j]).then(function (file) {
                                for (var k = 0; k < uploadCtrl.files.length; k++) {
                                    if (fileEqual(uploadCtrl.files[k], file)) {
                                        uploadCtrl.files[k].percentageUploaded = 33;
                                    }
                                }
                            });
                        }
                    }
                };

                reader.readAsText(files[i]);
            }
        }

        function fileEqual (file1, file2) {
            return file1.name === file2.name &&
                file1.lastModified === file2.lastModified
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('upload', upload);
    upload.$inject = ['$scope', 'files', '$uibModal', '$uibModalInstance', 'uploadService', 'toaster', '$location', 'conversion'];
}());