(function () {
    'use strict';

    function gridCellTemplateFactory($filter) {
        var factory = {
            createRemoveCellTemplate: function () {
                return '<div class="ui-grid-cell-contents centered-heading">' +
                           '<button type="button" class="btn btn-danger btn-xs" data-ng-click="grid.appScope.removeTypingTestName(row.entity)">' +
                               '<i class="glyphicon glyphicon-minus" />' +
                           '</button>' +
                       '</div>';
            },

            createDateCellTemplate: function () {
                return '<div class="ui-grid-cell-contents ng-binding ng-scope" title="{{ row.entity.dateCreated | date : \'medium\' }}">' +
                           '{{ row.entity.dateCreated | date : \'medium\' }}' +
                       '</div>';
            },

            createActiveCellTemplate: function () {
                return '<button type="button" class="btn btn-link" data-ng-click="grid.appScope.editItem(row.entity)">' +
                           'Edit' +
                       '</button>' +
                       '<input type="checkbox" data-ng-model="row.entity.active" data-ng-disabled="true" />&nbsp;<small>Active</small>';
            },

            createDeleteCellTemplate: function () {
                return '<button type="button" class="btn btn-link red-link" data-ng-click="grid.appScope.deleteItem(row.entity)">Delete</button>';
            },

            createCollectionMethodCell: function () {
                return '<div class="ui-grid-cell-contents">' +
                            '<button type="button" class="btn btn-primary btn-xs cell-button" ' +
                                'data-ng-click="grid.appScope.showCollectionMethodData(collectionMethod)"' +
                                'data-ng-repeat="collectionMethod in row.entity.collectionMethods">' +
                                    '{{ collectionMethod.name }}' +
                            '</button>'
                       '</div>';
            },

            parseDate: function (date) {
                $filter('date')(date, 'medium');
            }
        };

        return factory;
    }

    angular.module('hmlFhirAngularClientApp.factories').factory('gridCellTemplateFactory', gridCellTemplateFactory);
    gridCellTemplateFactory.$inject = ['$filter'];
}());