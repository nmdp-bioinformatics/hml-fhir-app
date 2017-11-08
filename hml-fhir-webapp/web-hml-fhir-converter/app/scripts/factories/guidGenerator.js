/**
 * Created by abrown3 on 12/19/16.
 */
(function () {
    'use strict';

    function guidGenerator () {
        var factory = {
            generateRandomGuid: function () {
                /* jshint ignore:start */
                return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
                    var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r&0x3 | 0x8);
                    return v.toString(16);
                });
                /* jshint ignore:end */
            },

            validateGuid: function (guid) {
                var pattern = /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;
                return pattern.test(guid);
            }
        };

        return factory;
    }

    angular.module('hmlFhirAngularClientApp.factories').factory('guidGenerator', guidGenerator);
    guidGenerator.$inject = [];
}());