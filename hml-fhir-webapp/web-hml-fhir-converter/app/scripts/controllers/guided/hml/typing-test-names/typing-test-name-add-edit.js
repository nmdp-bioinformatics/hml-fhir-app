/**
 * Created by abrown3 on 12/19/16.
 */
(function () {
    'use strict';

    function typingTestNameAddEdit ($scope, $uibModalInstance, $uibModal, edit, typingTestName, typingTestNameService, appConfig, toaster, selectedTypingTestNames, typeaheadQueryBuilder, objectModelFactory) {
        /* jshint validthis: true */
        var typingTestNameAddEditCtrl = this;

        typingTestNameAddEditCtrl.scope = $scope;
        typingTestNameAddEditCtrl.formSubmitted = false;
        typingTestNameAddEditCtrl.edit = edit;
        typingTestNameAddEditCtrl.selectedTypingTestName = null;
        typingTestNameAddEditCtrl.selectedTypingTest = null
        typingTestNameAddEditCtrl.maxQuery = { number: 10, text: '10' };
        typingTestNameAddEditCtrl.pageNumber = 0;
        typingTestNameAddEditCtrl.resultsPerPage = appConfig.resultsPerPage;
        typingTestNameAddEditCtrl.autoAdd = appConfig.autoAddOnNoResults;

        $scope.$on('typingTestNameAddEditCtrl.addedExternal.success', function (event, result) {
            $uibModalInstance.close(result);
        });

        if (typingTestNameAddEditCtrl.edit) {
            typingTestNameAddEditCtrl.selectedTypingTest = typingTestName;
        }

        typingTestNameAddEditCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        typingTestNameAddEditCtrl.close = function () {
            $uibModalInstance.close();
        };

        typingTestNameAddEditCtrl.add = function (form) {
            typingTestNameAddEditCtrl.formSubmitted = true;

            if (!form.$invalid) {
                typingTestNameAddEditCtrl.formSubmitted = false;
                $uibModalInstance.close(typingTestNameAddEditCtrl.selectedTypingTest);
            }
        };

        typingTestNameAddEditCtrl.selectTypingTest = function (item) {
            typingTestNameAddEditCtrl.selectedTypingTest = item;
        };

        typingTestNameAddEditCtrl.getTypingTestNames = function (viewValue) {
            return typingTestNameService.getTypeaheadOptions(typingTestNameAddEditCtrl.maxQuery.number,
                typeaheadQueryBuilder.buildTypeaheadQueryWithSelectionExclusion('name', viewValue, false,
                    selectedTypingTestNames, 'id')).then(function (response) {
                    if (response.length > 0) {
                        return response;
                    }

                    if (typingTestNameAddEditCtrl.autoAdd) {
                        setTimeout(timeNoResults, appConfig.autoAddOnNoResultsTimer);
                    }
            });
        };

        typingTestNameAddEditCtrl.typingTestChange = function () {
            typingTestNameAddEditCtrl.selectedTypingTest = null;
        };

        function createTypeAheadItemEntry() {
            var modalInstance = $uibModal.open({
                    animation: true,
                    controller: 'typingTestNamesTerminologyAddEditModal',
                    controllerAs: 'typingTestNamesTerminologyAddEditModalCtrl',
                    templateUrl: 'views/settings/hml/typing-test-names/terminology/typing-test-names-terminology-add-edit-modal.html',
                    resolve: {
                        title: function () {
                            return 'Add Typing Test Name Item';
                        },
                        typingTestName: function () {
                            return objectModelFactory.getModelByName('TypingTestName');
                        },
                        edit: function () {
                            return false;
                        }
                    }
                });

            modalInstance.result.then(function (result) {
                if (result) {
                    toaster.pop({
                        type: 'info',
                        body: 'Successfully added Typing Test Name entry.'
                    });

                    $scope.$emit('typingTestNameAddEditCtrl.addedExternal.success', result);
                }
            });
        }

        function timeNoResults() {
            if (typingTestNameAddEditCtrl.selectedTypingTest === null) {

                toaster.pop({
                    type: 'info',
                    title: 'Add / Edit Typing Test Name',
                    body: 'Not finding the data you need? Close this notification to be taken to add/edit page.',
                    toasterId: 1,
                    timeout: 0,
                    onHideCallback: function () {
                        createTypeAheadItemEntry();
                    }
                });
            }
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('typingTestNameAddEdit', typingTestNameAddEdit);
    typingTestNameAddEdit.$inject = ['$scope', '$uibModalInstance', '$uibModal', 'edit', 'typingTestName', 'typingTestNameService', 'appConfig', 'toaster', 'selectedTypingTestNames', 'typeaheadQueryBuilder', 'objectModelFactory'];
}());
