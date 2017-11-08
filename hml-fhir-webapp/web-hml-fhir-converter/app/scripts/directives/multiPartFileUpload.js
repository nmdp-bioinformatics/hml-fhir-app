/**
 * Created by andrew on 5/23/17.
 */
(function () {
   'use strict';

   function multiPartFileUpload ($parse) {
       return {
           restrict: 'A',
           link: function (scope, element, attrs) {
               var model = $parse(attrs.multiPartFileUpload),
                   modelSetter = model.assign;

               element.bind('change', function () {
                    scope.$apply(function () {
                        modelSetter(scope, element[0].files[0]);
                    });
               });
           }
       };
   }

   angular.module('hmlFhirAngularClientApp.directives').directive('multiPartFileUpload', multiPartFileUpload);
   multiPartFileUpload.$inject = ['$parse'];
}());