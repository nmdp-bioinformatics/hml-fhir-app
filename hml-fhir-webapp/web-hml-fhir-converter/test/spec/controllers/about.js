'use strict';

describe('Controller: about', function () {

  // load the controller's module
  beforeEach(module('hmlFhirAngularClientApp'));

  var aboutCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    aboutCtrl = $controller('about', {
      $scope: scope
      // place here mocked dependencies
    });

    aboutCtrl.awesomeThings = ['1', '2', '3'];
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(aboutCtrl.awesomeThings.length).toBe(3);
  });
});
