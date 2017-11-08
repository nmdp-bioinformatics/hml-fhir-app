/**
 * Created by abrown3 on 1/9/17.
 */
(function () {
    'use strict';

    function typingTestNamesTerminologyAddEditModal ($scope, $uibModalInstance, typingTestName, title, edit, typingTestNameService) {
        /* jshint validthis: true */
        var typingTestNamesTerminologyAddEditModalCtrl = this;

        typingTestNamesTerminologyAddEditModalCtrl.scope = $scope;
        typingTestNamesTerminologyAddEditModalCtrl.typingTestName = typingTestName;
        typingTestNamesTerminologyAddEditModalCtrl.title = title;
        typingTestNamesTerminologyAddEditModalCtrl.formSubmitted = false;
        typingTestNamesTerminologyAddEditModalCtrl.edit = edit;

        typingTestNamesTerminologyAddEditModalCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        typingTestNamesTerminologyAddEditModalCtrl.close = function () {
            $uibModalInstance.close();
        };

        typingTestNamesTerminologyAddEditModalCtrl.save = function () {
            typingTestNamesTerminologyAddEditModalCtrl.formSubmitted = true;

            if (!typingTestNamesTerminologyAddEditModalCtrl.terminologyForm.$invalid) {
                if (typingTestNamesTerminologyAddEditModalCtrl.edit) {
                    typingTestNameService.updateSingleTypingTestNameTerminology(typingTestNamesTerminologyAddEditModalCtrl.typingTestName).then(function (result) {
                        if (result) {
                            typingTestNamesTerminologyAddEditModalCtrl.formSubmitted = false;
                            $uibModalInstance.close(result);
                        }
                    });
                } else {
                    typingTestNameService.addSingleTypingTestNameTerminology(typingTestNamesTerminologyAddEditModalCtrl.typingTestName).then(function (result) {
                       if (result) {
                           typingTestNamesTerminologyAddEditModalCtrl.formSubmitted = false;
                           $uibModalInstance.close(result);
                       }
                    });
                }
            }
        };
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('typingTestNamesTerminologyAddEditModal', typingTestNamesTerminologyAddEditModal);
    typingTestNamesTerminologyAddEditModal.$inject = ['$scope', '$uibModalInstance', 'typingTestName', 'title', 'edit', 'typingTestNameService'];
}());