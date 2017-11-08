/**
 * Created by abrown3 on 2/11/17.
 */
(function () {
    'use strict';

    function indexCollection () {
        var factory = {
            getCollectionItemById: function (collection, collectionPropertyId, itemId) {
                var collectionArray = [];

                if (collectionPropertyId) {
                    collectionArray = collection[collectionPropertyId];
                } else {
                    collectionArray = collection;
                }

                if (!collectionArray) {
                    return {};
                }

                for (var i = 0; i < collectionArray.length; i++) {
                    if (collectionArray[i].id === itemId) {
                        return collectionArray[i];
                    }
                }

                return {};
            },

            getCollectionItemIndex: function (collection, collectionPropertyId, itemId) {
                var collectionArray = [];

                if (collectionPropertyId) {
                    collectionArray = collection[collectionPropertyId];
                } else {
                    collectionArray = collection;
                }

                if (!collectionArray) {
                    return -1;
                }

                for (var i = 0; i < collectionArray.length; i++) {
                    if (collectionArray[i].id === itemId) {
                        return i;
                    }
                }

                return -1;
            }
        };

        return factory;
    }

    angular.module('hmlFhirAngularClientApp.factories').factory('indexCollection', indexCollection);
    indexCollection.$inject = [];
}());