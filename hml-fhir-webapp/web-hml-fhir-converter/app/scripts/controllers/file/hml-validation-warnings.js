/**
 * Created by abrown3 on 12/13/16.
 */
(function () {
    'use strict';

    function hmlValidationWarnings ($scope, warnings, file, $uibModalInstance, $uibModal) {
        /*jshint validthis: true */
        var hmlValidationWarningsCtrl = this;

        hmlValidationWarningsCtrl.scope = $scope;
        hmlValidationWarningsCtrl.warnings = warnings;
        hmlValidationWarningsCtrl.accordions = [];

        for (var i = 0; i < hmlValidationWarningsCtrl.warnings.length; i++) {
            var state = {
                id: hmlValidationWarningsCtrl.warnings[i]['miring-result'].xpath,
                mainPanelOpen: false,
                subPanelOpen: false
            };

            hmlValidationWarningsCtrl.accordions.push(state);
        }

        hmlValidationWarningsCtrl.isMainPanelOpen = function (id) {
            for (var j = 0; j < hmlValidationWarningsCtrl.accordions.length; j++) {
                if (hmlValidationWarningsCtrl.accordions[j].id === id) {
                    return hmlValidationWarningsCtrl.accordions[j].mainPanelOpen;
                }
            }

            return false;
        };

        hmlValidationWarningsCtrl.isSubPanelOpen = function (id) {
            for (var j = 0; j < hmlValidationWarningsCtrl.accordions.length; j++) {
                if (hmlValidationWarningsCtrl.accordions[j].id === id) {
                    return hmlValidationWarningsCtrl.accordions[j].subPanelOpen;
                }
            }

            return false;
        };

        hmlValidationWarningsCtrl.toggleIssue = function (id) {
            for (var j = 0; j < hmlValidationWarningsCtrl.accordions.length; j++) {
                if (hmlValidationWarningsCtrl.accordions[j].id === id) {
                    hmlValidationWarningsCtrl.accordions[j].mainPanelOpen = !hmlValidationWarningsCtrl.accordions[j].mainPanelOpen;
                }
            }
        };

        hmlValidationWarningsCtrl.toggleSolution = function (id) {
            for (var j = 0; j < hmlValidationWarningsCtrl.accordions.length; j++) {
                if (hmlValidationWarningsCtrl.accordions[j].id === id) {
                    hmlValidationWarningsCtrl.accordions[j].subPanelOpen = !hmlValidationWarningsCtrl.accordions[j].subPanelOpen;
                }
            }
        };

        hmlValidationWarningsCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        hmlValidationWarningsCtrl.close = function () {
            $uibModalInstance.close();
        };

        hmlValidationWarningsCtrl.parseWarningDescription = function (desc) {
            var regexExp = /\[(\d+, ?)+(\d+)?\]/,
                regex = new RegExp(regexExp),
                matches = desc.match(regex),
                position = matches[0].replace('[', '').replace(']', ''),
                line = position.split(',')[0],
                col = position.split(',')[1],
                text = desc.replace(matches[0] + ' ', '');

            return 'Line: ' + line + ' Column: ' + col + ', ' + text;
        };

        hmlValidationWarningsCtrl.removeFile = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/common/confirmation-modal.html',
                controller: 'confirmationModal',
                controllerAs: 'confirmationModalCtrl',
                resolve: {
                    title: function () {
                        return 'Remove file from conversion';
                    },
                    message: function () {
                        return 'If the displayed warnings are cause for concern. Please select \'Confirm\' below.' +
                            ' This will cause the file to be removed from the uploaded conversion list.';
                    }
                }
            });

            modalInstance.result.then(function (result) {
               if (result) {
                   $uibModalInstance.close(file);
               }
            });
        };
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('hmlValidationWarnings', hmlValidationWarnings);
    hmlValidationWarnings.$inject = ['$scope', 'warnings', 'file', '$uibModalInstance', '$uibModal'];
}());
