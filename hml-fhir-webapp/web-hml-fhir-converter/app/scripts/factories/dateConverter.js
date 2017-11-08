/**
 * Created by abrown3 on 1/9/17.
 */
(function () {
    'use strict';

    function dateConverter ($moment) {
        var factory = {
            parseDate: function (obj, property) {
                if (obj.constructor === Array) {
                    for (var i = 0; i < obj.length; i++) {
                        obj[i][property] = $moment(obj[i][property]).format('DD/MM/YYYY');
                    }
                }
                else {
                    obj[property] = $moment(obj[property]).format('DD/MM/YYYY');
                }

                return obj;
            }
        };

        return factory;
    }

    angular.module('hmlFhirAngularClientApp.factories').factory('dateConverter', dateConverter);
    dateConverter.$inject = ['$moment'];
}());