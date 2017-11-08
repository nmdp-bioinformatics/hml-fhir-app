(function () {
    'use strict';

    function templateController () {
        var factory = {
            getControllerNameByTemplateUrl: function (templateUrl) {
                var urlArray = templateUrl.split('/');

                for (var i = 0; i < urlArray.length; i++) {
                    if (urlArray[i].length === 0) {
                        urlArray.split(i, 1);
                    }
                }

                var htmlFileName = urlArray[urlArray.length - 1],
                    dirtyControllerName = htmlFileName.split('.')[0],
                    htmlControllerName = '';

                for (var j = 0; j < dirtyControllerName.length; j++) {
                    if (htmlFileName[j] === '-') {
                        htmlControllerName += dirtyControllerName[j + 1].toUpperCase();
                        j++;
                    } else {
                        htmlControllerName += dirtyControllerName[j];
                    }
                }

                return {
                    controller: htmlControllerName,
                    controllerAs: htmlControllerName + 'Ctrl'
                };
            }
        };

        return factory;
    }

    angular.module('hmlFhirAngularClientApp.factories').factory('templateController', templateController);
    templateController.$inject = [];
}());