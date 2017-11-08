/**
 * Created by abrown3 on 12/16/16.
 */
(function () {
    'use strict';

    function dynamicController ($compile) {
        return {
            restrict: 'E',
            link: function (scope, element, attrs) {
                var template = '<div data-ng-include="\'' + attrs.templateurl + '\'" data-ng-controller="' +
                        attrs.controller + ' as ' + attrs.controlleras + '"></div>',
                    cTemplate = $compile(template)(scope);

                    element.append(cTemplate);
            }
        };
    }

    angular.module('hmlFhirAngularClientApp.directives').directive('dynamicController', dynamicController);
    dynamicController.$inject = ['$compile'];
}());