/**
 * Created by abrown3 on 1/20/17.
 */
(function () {
    'use strict';

    function typeaheadQueryBuilder() {
        var factory = {
            buildTypeaheadQuery: function (propertyName, viewValue, exclude) {
                return {
                    criteria: [
                        { propertyName: propertyName, queryValue: viewValue, useLike: true, exclude: exclude }
                    ]
                };
            },

            buildTypeaheadQueryWithSelectionExclusion: function (propertyName, viewValue, exclude, selectionArray, selectionProperty) {
                var query = {
                    criteria: [
                        { propertyName: propertyName, queryValue: viewValue, useLike: true, exclude: exclude}
                    ]
                };

                if (selectionArray) {
                    for (var i = 0; i < selectionArray.length; i++) {
                        query.criteria.push({
                            propertyName: selectionProperty,
                            queryValue: selectionArray[i],
                            useLike: false,
                            exclude: true
                        });
                    }
                }

                return query;
            }
        };

        return factory;
    }

    angular.module('hmlFhirAngularClientApp.factories').factory('typeaheadQueryBuilder', typeaheadQueryBuilder);
    typeaheadQueryBuilder.$inject = [];
}());