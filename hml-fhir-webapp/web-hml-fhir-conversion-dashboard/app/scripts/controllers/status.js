(function () {
  'use strict';

  function status($scope, statusService, downloadService, uploadService, submissionService) {
    /*jshint validthis: true */
    var statusCtrl = this;

    statusCtrl.scope = $scope;
    statusCtrl.statuses = [];
    statusCtrl.submission = {};
    statusCtrl.submissionObservations = [];

    statusCtrl.refresh = function () {
      statusService.getStatus(5).then(function (result) {
          statusCtrl.statuses = result;
      });
    };

    statusCtrl.downloadHmlXml = function (status) {
      downloadService.downloadHml(status.hmlId, true).then(function () {

      });
    };

    statusCtrl.downloadHmlJson = function (status) {
      downloadService.downloadHml(status.hmlId).then(function () {

      });
    };

    statusCtrl.downloadFhirXml = function (status) {
      downloadService.downloadFhir(status.fhirId, true).then(function () {

      });
    };

    statusCtrl.downloadFhirJson = function (status) {
      downloadService.downloadFhir(status.fhirId).then(function () {
          statusCtrl.refresh();
      });
    };

    statusCtrl.convertHml = function () {
      // uploadService.uploadHml(statusCtrl.hmlFile).then(function (result) {
      //   statusCtrl.hmlFile = null;
      //   statusCtrl.refresh();
      // });

        var reader = new FileReader();

        reader.onload = function (e) {
          var contents = e.target.result;
            uploadService.uploadHmlText(contents).then(function (result) {
                statusCtrl.hmlFile = null;
                statusCtrl.refresh();
            });
        };

        reader.readAsText(statusCtrl.hmlFile);


    };

    statusCtrl.convertFhir = function () {
        uploadService.uploadFhir(statusCtrl.fhirFile).then(function (result) {

        });
    };

    statusCtrl.getSubmissionById = function (id) {
        downloadService.downloadFhir(id).then(function () {
          statusCtrl.refresh();
        });
    };

    // statusCtrl.getSubmissionById = function (id) {
    //   submissionService.getSubmission(id).then(function (result) {
    //     statusCtrl.submission = {
    //       fhirId: result._id,
    //       patients: []
    //     };
    //
    //     for (var i = 0; i < result.submissionResult.length; i++) {
    //       var submissionResult = result.submissionResult[i],
    //         diagnosticReportKeys = Object.keys(submissionResult.diagnosticReports),
    //         patient = {
    //           patientId: submissionResult.patientId,
    //           patientUrl: submissionResult.patientResource.url,
    //           diagnosticReport: {
    //             key: diagnosticReportKeys[0],
    //             value: submissionResult.diagnosticReports[diagnosticReportKeys[0]].url
    //           },
    //           observations: [],
    //           specimens: []
    //         },
    //         observationKeys = Object.keys(submissionResult.observations),
    //         specimenKeys = Object.keys(submissionResult.specimens);
    //
    //       for (var j = 0; j < observationKeys.length; j++) {
    //         var key = observationKeys[j],
    //           observation = submissionResult.observations[key];
    //         patient.observations.push({ key: key, value: observation.url });
    //       }
    //
    //       for (var j = 0; j < specimenKeys.length; j++) {
    //         var key = specimenKeys[j],
    //           specimen = submissionResult.specimens[key];
    //         patient.specimens.push({ key: key, value: specimen.url });
    //       }
    //
    //       statusCtrl.submission.patients.push(patient);
    //     }
    //   });
    // };

    statusCtrl.refresh();
  }

  angular.module('webHmlFhirConversionDashboardApp.controllers').controller('status', status);
  status.$inject = ['$scope', 'statusService', 'downloadService', 'uploadService', 'submissionService'];
}());
