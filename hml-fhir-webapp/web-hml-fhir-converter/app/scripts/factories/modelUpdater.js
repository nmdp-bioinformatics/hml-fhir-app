/**
 * Created by abrown3 on 3/7/17.
 */
(function () {
    'use strict';

    function modelUpdater (guidGenerator) {
        /* globals R: true */
        var factory = {
            updateModel: function (model, propertyMap, newValue) {
                var lens = R.lensPath(propertyMap);
                return R.set(lens, newValue, model);
            },

            convertPropertyMapToRamda: function (propertyMap) {
                var parsedMap = [],
                    parseMap = function (item) {
                        parsedMap.push(item.propertyString);
                        if (item.isArray && item.propertyIndex > -1) {
                            parsedMap.push(item.propertyIndex);
                        }
                    };

                R.forEach(parseMap, propertyMap);
                return parsedMap;
            },

            removeTempIds: function (model) {
                var handleArray = function (item) {
                        R.mapObjIndexed(hasNonMongoId, item);
                    },
                    hasNonMongoId = function (value, key, obj) {
                        if (R.type(value) === 'Array') {
                            R.forEach(handleArray, value);
                        }

                        if (R.type(value) === 'Object') {
                            R.mapObjIndexed(hasNonMongoId, value);
                        }

                        if (key === 'id') {
                            if (guidGenerator.validateGuid(value)) {
                                obj[key] = null;
                            }
                        }
                    };

                R.mapObjIndexed(hasNonMongoId, model);
            },

            returnObjectFromHml: function (propertyMap, hml) {
                return R.path(propertyMap, hml);
            }
        };

        return factory;
    }

    angular.module('hmlFhirAngularClientApp.factories').factory('modelUpdater', modelUpdater);
    modelUpdater.$inject = ['guidGenerator'];
}());