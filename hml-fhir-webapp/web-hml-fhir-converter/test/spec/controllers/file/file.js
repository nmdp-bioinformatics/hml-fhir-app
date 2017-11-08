/**
 * Created by abrown3 on 12/8/16.
 */
'use strict';

describe('Controller: file', function () {
   beforeEach(module('hmlFhirAngularClientApp'));

   var fileCtrl,
       scope;

   beforeEach(inject(function ($controller, $rootScope) {
       scope = $rootScope.$new();
       fileCtrl = $controller('file', {
           $scope: scope,
           $uibModalInstance: {},
           title: 'Title'
       });
   }));

   it('Title should equal the value of the resolved string', function () {
      expect(fileCtrl.title).toBe('Title');
   });
});