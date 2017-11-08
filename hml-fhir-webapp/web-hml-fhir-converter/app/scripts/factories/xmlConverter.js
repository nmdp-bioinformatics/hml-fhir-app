/**
 * Created by abrown3 on 12/9/16.
 */
(function () {
    'use strict';

    function xmlConverter () {
        var factory = {
            /* jshint ignore:start */
            parseXmlToJson: function (xml) {
                try {
                    var converter = new X2JS({
                        arrayAccessFormPaths: [
                            'miring-report.validation-warnings',
                            'miring-report.samples'
                        ]
                    });

                    return converter.xml_str2json(xml);
                } catch (exception) {
                    return 'Error validating HML xml: ' + exception;
                }
            }
            /* jshint ignore:end */
        };

        return factory;
    }

    angular.module('hmlFhirAngularClientApp.factories').factory('xmlConverter', xmlConverter);
    xmlConverter.$inject = [];
}());