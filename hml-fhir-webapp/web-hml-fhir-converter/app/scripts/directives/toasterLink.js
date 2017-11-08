/**
 * Created by abrown3 on 1/18/17.
 */
(function () {
    'use strict';

    function toasterLink () {
        return {
            template: '<span>Queries not returning data you need? Click <button type="button" class="btn btn-link" data-ng-click="directiveData.navFunction()">here</button> to be taken to the {{ directiveData.entityName }} add/edit page.</span>'
        };
    }

    angular.module('hmlFhirAngularClientApp.directives').directive('toasterLink', toasterLink);
    toasterLink.$inject = [];
}());