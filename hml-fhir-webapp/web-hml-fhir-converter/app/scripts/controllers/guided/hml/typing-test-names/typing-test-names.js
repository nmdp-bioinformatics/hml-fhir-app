/**
 * Created by abrown3 on 12/15/16.
 */
(function () {
    'use strict';

    function typingTestNames ($scope, $uibModal, gridCellTemplateFactory, hmlService) {
        /* jshint validthis: true */
        var typingTestNamesCtrl = this,
            parentCtrl = $scope.hmlModalCtrl,
            deleteColumnTemplate = gridCellTemplateFactory.createRemoveCellTemplate();

        typingTestNamesCtrl.scope = $scope;
        typingTestNamesCtrl.hml = parentCtrl.hml;
        typingTestNamesCtrl.gridOptions = {
            data: typingTestNamesCtrl.hml.typingTestNames,
            enableSorting: true,
            showGridFooter: true,
            appScopeProvider: typingTestNamesCtrl,
            columnDefs: [
                { name: 'id', field: 'id', visible: false },
                { name: 'name', field: 'name', displayName: 'Name:', cellTooltip: function (row) { return row.entity.name; }, headerTooltip: function(col) { return col.displayName; } },
                { name: 'description', field: 'description', displayName: 'Description:', cellTooltip: function (row) { return row.entity.description ;}, headerTooltip: function(col) { return col.displayName; } },
                { field: 'delete', displayName: 'Remove', maxWidth: 75, enableColumnMenu: false, cellTemplate: deleteColumnTemplate }
            ]
        };

        $scope.$on('guided:hml:node:update', function () {
            hmlService.updateHml(typingTestNamesCtrl.hml).then(function (result) {
                if (result) {
                    $scope.$emit('guided:hml:node:updated', result);
                }
            });
        });

        typingTestNamesCtrl.addTypingTestNameEntry = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/guided/hml/typing-test-names/typing-test-name-add-edit.html',
                controller: 'typingTestNameAddEdit',
                controllerAs: 'typingTestNameAddEditCtrl',
                resolve: {
                    edit: function () {
                        return false;
                    },
                    typingTestName: function () {
                        return undefined;
                    },
                    selectedTypingTestNames: function () {
                        var typingTestNames = typingTestNamesCtrl.hml.typingTestNames,
                            idArray = [];

                        for (var i = 0; i < typingTestNames.length; i++) {
                            idArray.push(typingTestNames[i].id);
                        }

                        return idArray;
                    }
                }
            });

            modalInstance.result.then(function (typingTestName) {
                if (typingTestName) {
                    if (typingTestName.constructor === Array) {
                        for (var i = 0; i < typingTestName.length; i++) {
                            typingTestNamesCtrl.hml.typingTestNames.push(typingTestName[i]);
                        }

                        return;
                    }

                    typingTestNamesCtrl.hml.typingTestNames.push(typingTestName);
                }
            });
        };

        typingTestNamesCtrl.removeTypingTestName = function (typingTestName) {
            typingTestNamesCtrl.hml.typingTestNames.splice(getTypingTestNameIndex(typingTestName), 1);
        };

        function getTypingTestNameIndex (typingTestName) {
            for (var i = 0; i < typingTestNamesCtrl.hml.typingTestNames.length; i++) {
                if (typingTestNamesCtrl.hml.typingTestNames[i].id === typingTestName.id) {
                    return i;
                }
            }

            return undefined;
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('typingTestNames', typingTestNames);
    typingTestNames.$inject = ['$scope', '$uibModal', 'gridCellTemplateFactory', 'hmlService'];
}());