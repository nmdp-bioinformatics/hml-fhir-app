/**
 * Created by abrown3 on 2/14/17.
 */
(function () {
    'use strict';

    function sequenceAddEdit ($scope, $uibModalInstance, sequence, edit, tempId) {
        /* jshint validthis:true */
        var sequenceAddEditCtrl = this;

        sequenceAddEditCtrl.scope = $scope;
        sequenceAddEditCtrl.formSubmitted = false;
        sequenceAddEditCtrl.sequence = sequence;
        sequenceAddEditCtrl.edit = edit;

        if (!sequenceAddEditCtrl.edit) {
            sequenceAddEditCtrl.sequence.id = tempId;
        }

        sequenceAddEditCtrl.cancel = function () {
            $uibModalInstance.dismiss();
        };

        sequenceAddEditCtrl.close = function () {
            $uibModalInstance.close();
        };

        sequenceAddEditCtrl.add = function (form) {
            sequenceAddEditCtrl.formSubmitted = true;

            if (!form.$invalid) {
                sequenceAddEditCtrl.formSubmitted = false;
                $uibModalInstance.close(sequenceAddEditCtrl.sequence);
            }
        };
    }

    angular.module('hmlFhirAngularClientApp.controllers').controller('sequenceAddEdit', sequenceAddEdit);
    sequenceAddEdit.$inject = ['$scope', '$uibModalInstance', 'sequence', 'edit', 'tempId'];
}());