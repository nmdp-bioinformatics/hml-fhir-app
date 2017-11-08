'use strict';

describe('Controller: main', function () {

  // load the controller's module
  beforeEach(module('hmlFhirAngularClientApp'));

  var mainCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    mainCtrl = $controller('main', {
      $scope: scope
      // place here mocked dependencies
    });

    mainCtrl.awesomeThings = ['1', '2', '3'];
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(mainCtrl.awesomeThings.length).toBe(3);
  });
});
