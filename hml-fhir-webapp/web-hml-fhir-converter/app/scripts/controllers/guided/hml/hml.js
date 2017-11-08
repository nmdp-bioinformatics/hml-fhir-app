/**
 * Created by abrown3 on 12/13/16.
 */
(function () {
    'use strict';

    function hml ($scope, appConfig, $uibModal, toaster, $location, hmlModel, gridCellTemplateFactory, hmlService, typeaheadQueryBuilder, defaultHmlVersion, versions) {
        /*jshint validthis: true */
        var hmlCtrl = this,
            dateColumnTemplate = gridCellTemplateFactory.createDateCellTemplate(),
            activeCellTemplate = gridCellTemplateFactory.createActiveCellTemplate();

        hmlCtrl.scope = $scope;
        hmlCtrl.hml = hmlModel;
        hmlCtrl.hml.version = defaultHmlVersion;
        hmlCtrl.versions = versions
        hmlCtrl.selectedHmlProjectName = null;
        hmlCtrl.formSubmitted = false;
        hmlCtrl.panelTitle = undefined;
        hmlCtrl.advancedSearchResults = [];
        hmlCtrl.advancedSearchEnabled = false;
        hmlCtrl.maxQuery = appConfig.defaultMaxQueryTypeahead;
        hmlCtrl.pageNumber = 0;
        hmlCtrl.resultsPerPage = appConfig.resultsPerPage;
        hmlCtrl.autoAdd = appConfig.autoAddOnNoResults;

        hmlCtrl.gridOptions = {
            data: [],
            enableSorting: true,
            showGridFooter: true,
            appScopeProvider: hmlCtrl,
            columnDefs: [
                { name: 'id', field: 'id', displayName: 'ID', cellTooltip: function (row) { return row.entity.id; } },
                { name: 'project.name', field: 'project.name', displayName: 'Project Name', cellTooltip: function (row) { return row.entity.project.name; } },
                { name: 'dateCreated', field: 'dateCreated', displayName: 'Date Created', cellTemplate: dateColumnTemplate, cellTooltip: function (row) { return gridCellTemplateFactory.parseDate(row.entity.dateCreated) } },
                { name: 'active', field: 'active', displayName: 'Modify', enableColumnMenu: false, cellTemplate: activeCellTemplate }
            ]
        };

        hmlCtrl.changeHmlVersion = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/guided/hml/hml-version.html',
                controller: 'hmlVersion',
                controllerAs: 'hmlVersionCtrl',
                resolve: {
                    version: function () {
                        return hmlCtrl.hml.version;
                    },
                    versions: function () {
                        return hmlCtrl.versions;
                    },
                    maxQuery: function () {
                        return appConfig.defaultMaxQueryTypeahead;
                    }
                }
            });

            modalInstance.result.then(function (result) {
                if (result) {
                    if (result !== hmlCtrl.hml.version) {
                        toaster.pop({
                            type: 'info',
                            body: 'Successfully changed to HML version: ' + result.name
                        });
                    }

                    hmlCtrl.hml.version = result;
                }
            });
        };

        hmlCtrl.createHml = function (created) {
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'views/guided/hml/hml-create.html',
                controller: 'hmlCreate',
                controllerAs: 'hmlCreateCtrl',
                resolve: {
                    hml: function () {
                        return hmlCtrl.hml;
                    },
                    versions: function () {
                        return hmlCtrl.versions;
                    },
                    title: function () {
                        return 'Create HML Project';
                    },
                    created: function () {
                        return created;
                    }
                }
            });

            modalInstance.result.then(function (result) {
                if (result) {
                    if (result.constructor === Array) {
                        hmlCtrl.hml = result[0];
                    } else {
                        hmlCtrl.hml = result;
                    }

                    loadHmlDetails(hmlCtrl.hml.id, false);
                }
            });
        };

        hmlCtrl.cancelHml = function () {
            $location.path('/');
        };

        hmlCtrl.getHmls = function (viewValue) {
            return hmlService.getTypeaheadOptions(hmlCtrl.maxQuery.number,
                typeaheadQueryBuilder.buildTypeaheadQuery('project.name', viewValue, false)).then(function (response) {
                    if (response.length > 0) {
                        return response;
                    }

                    if (hmlCtrl.autoAdd) {

                    }
                });
        };

        hmlCtrl.selectHml = function (item) {
            hmlCtrl.hml = item;
            hmlCtrl.createHml(true);
        };

        function loadHmlDetails(hmlId, edit) {
            $location.path('/guided/hml/details/' + edit + '/' + hmlId + '/');
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('hml', hml);
    hml.$inject = ['$scope', 'appConfig', '$uibModal', 'toaster', '$location', 'hmlModel', 'gridCellTemplateFactory', 'hmlService', 'typeaheadQueryBuilder', 'defaultHmlVersion', 'versions'];
}());