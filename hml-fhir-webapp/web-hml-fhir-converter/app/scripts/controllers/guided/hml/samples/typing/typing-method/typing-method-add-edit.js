/**
 * Created by abrown3 on 2/12/17.
 */
(function () {
    'use strict';

    function typingMethodAddEdit ($scope, edit, $uibModalInstance, appConfig, typingMethod, hmlModel, parentCollectionPropertyAllocation) {
        /* jshint validthis:true */
        var typingMethodAddEditCtrl = this;

        $scope.parentCtrl = typingMethodAddEditCtrl;

        typingMethodAddEditCtrl.hml = hmlModel;
        typingMethodAddEditCtrl.scope = $scope;
        typingMethodAddEditCtrl.typingMethod = typingMethod;
        typingMethodAddEditCtrl.edit = edit;
        typingMethodAddEditCtrl.panelData = appConfig.typingMethodPanels;
        typingMethodAddEditCtrl.parentCollectionPropertyAllocation = parentCollectionPropertyAllocation;
        typingMethodAddEditCtrl.sampleIndex = getSampleIndex(typingMethodAddEditCtrl.parentCollectionPropertyAllocation)
        typingMethodAddEditCtrl.expandedPanels = {
            sso: false,
            ssp: false,
            sbtSanger: false,
            sbtNgs: false
        };

        typingMethodAddEditCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        typingMethodAddEditCtrl.close = function () {
            $uibModalInstance.close();
        };

        typingMethodAddEditCtrl.add = function () {
            $uibModalInstance.close(typingMethodAddEditCtrl.typingMethod);
        };

        typingMethodAddEditCtrl.togglePanel = function (panelName) {
            typingMethodAddEditCtrl.expandedPanels[panelName] = !typingMethodAddEditCtrl.expandedPanels[panelName];
        };

        function getSampleIndex (propertyMap) {
            var isEqual = function (item) {
                    return item.propertyString === 'samples';
                },
                index = R.findIndex(isEqual, propertyMap);

            return propertyMap[index].propertyIndex;
        }
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('typingMethodAddEdit', typingMethodAddEdit);
    typingMethodAddEdit.$inject = ['$scope', 'edit', '$uibModalInstance', 'appConfig', 'typingMethod', 'hmlModel', 'parentCollectionPropertyAllocation'];
}());