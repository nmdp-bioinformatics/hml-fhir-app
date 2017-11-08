/**
 * Created by abrown3 on 12/8/16.
 */
'use strict';

describe('Controller: upload', function () {
   beforeEach(module('hmlFhirAngularClientApp'));

   var uploadCtrl,
       scope;

   beforeEach(inject(function ($controller, $rootScope) {
       scope = $rootScope.$new();
       uploadCtrl = $controller('upload', {
           $scope: scope,
           files: [],
           $uibModalInstance: {},
           uploadService: {}
       });

       uploadCtrl.files = [{}];
   }));

   it('Should have a blank object resolved into scope', function () {
       expect(uploadCtrl.files.length).toBe(1);
   });
});