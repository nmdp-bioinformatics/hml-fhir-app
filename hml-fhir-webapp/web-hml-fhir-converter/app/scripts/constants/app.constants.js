/**
 * Created by abrown3 on 12/9/16.
 */
(function () {
    'use strict';

    var appConfig = {
        'miring_base_url': 'http://localhost:8080/MiringValidator/',
        'fhir_base_url': 'http://fhir.b12x.org/',
        'resource_server_base_url': 'http://localhost:8090/v1/',
        'hml': {
            'version': {
                'name': '1.0.1'
            }
        },
        'resultsPerPage': [
            { number: 5, text: '5' },
            { number: 10, text: '10' },
            { number: 25, text: '25' },
            { number: 50, text: '50' },
            { number: 100, text: '100' }
        ],
        'autoAddOnNoResults' : true,
        'autoAddOnNoResultsTimer': 2500,
        'defaultMaxQueryTypeahead': {
             number: 10, text: '10'
        },
        'propertiesParentMap': {
            'samplesParent': [
                { propertyString: 'samples', propertyIndex: -1, isArray: true },
                { propertyString: 'properties', propertyIndex: -1, isArray: true }
            ],
            'typingParent': [
                { propertyString: 'samples', propertyIndex: -1, isArray: true },
                { propertyString: 'typing', propertyIndex: -1, isArray: false },
                { propertyString: 'properties', propertyIndex: -1, isArray: true }
            ],
            'alleleAssignmentParent': [
                { propertyString: 'samples', propertyIndex: -1, isArray: true },
                { propertyString: 'typing', propertyIndex: -1, isArray: false },
                { propertyString: 'alleleAssignment', propertyIndex: -1, isArray: false },
                { propertyString: 'properties', propertyIndex: -1, isArray: true }
            ],
            'ssoParent': [
                { propertyString: 'samples', propertyIndex: -1, isArray: true },
                { propertyString: 'typing', propertyIndex: -1, isArray: false },
                { propertyString: 'sso', propertyIndex: -1, isArray: false },
                { propertyString: 'properties', propertyIndex: -1, isArray: true }
            ],
            'sspParent': [
                { propertyString: 'samples', propertyIndex: -1, isArray: true },
                { propertyString: 'typing', propertyIndex: -1, isArray: false },
                { propertyString: 'ssp', propertyIndex: -1, isArray: false },
                { propertyString: 'properties', propertyIndex: -1, isArray: true }
            ],
            'sbtSangerParent': [
                { propertyString: 'samples', propertyIndex: -1, isArray: true },
                { propertyString: 'typing', propertyIndex: -1, isArray: false },
                { propertyString: 'sbtSanger', propertyIndex: -1, isArray: false },
                { propertyString: 'properties', propertyIndex: -1, isArray: true }
            ]
        },
        'samplePanels': {
            'collectionMethods': {
                'templateUrl': 'views/guided/hml/samples/collection-methods/collection-methods.html',
                'controller': 'collectionMethods',
                'controllerAs': 'collectionMethodsCtrl'
            },
            'properties': {
                'templateUrl': 'views/guided/hml/properties/properties.html',
                'controller': 'properties',
                'controllerAs': 'propertiesCtrl'
            },
            'typing': {
                'templateUrl': 'views/guided/hml/samples/typing/typing.html',
                'controller': 'typing',
                'controllerAs': 'typingCtrl'
            }
        },
        'typingPanels': {
            'properties': {
                'templateUrl': 'views/guided/hml/properties/properties.html',
                'controller': 'properties',
                'controllerAs': 'propertiesCtrl'
            },
            'alleleAssignment': {
                'templateUrl': 'views/guided/hml/samples/typing/allele-assignment/allele-assignment.html',
                'controller': 'alleleAssignment',
                'controllerAs': 'alleleAssignmentCtrl'
            },
            'typingMethod': {
                'templateUrl': 'views/guided/hml/samples/typing/typing-method/typing-method.html',
                'controller': 'typingMethod',
                'controllerAs': 'typingMethodCtrl'
            },
            'consensusSequence': {
                'templateUrl': 'views/guided/hml/samples/typing/consensus-sequence/consensus-sequence.html',
                'controller': 'consensusSequence',
                'controllerAs': 'consensusSequenceCtrl'
            }
        },
        'alleleAssignmentPanels': {
            'haploid': {
                'templateUrl': 'views/guided/hml/samples/typing/allele-assignment/haploid/haploid.html',
                'controller': 'haploid',
                'controllerAs': 'haploidCtrl'
            },
            'genotypes': {
                'templateUrl': 'views/guided/hml/samples/typing/allele-assignment/genotypes/genotypes.html',
                'controller': 'genotypes',
                'controllerAs': 'genotypesCtrl'
            },
            'glstring': {
                'templateUrl': 'views/guided/hml/samples/typing/allele-assignment/glstring/glstring.html',
                'controller': 'glstring',
                'controllerAs': 'glstringCtrl'
            },
            'properties': {
                'templateUrl': 'views/guided/hml/properties/properties.html',
                'controller': 'properties',
                'controllerAs': 'propertiesCtrl'
            },
        },
        'consensusSequenceBlockPanels': {
            'sequence': {
                'templateUrl': 'views/guided/hml/sequence/sequence.html',
                'controller': 'sequence',
                'controllerAs': 'sequenceCtrl'
            },
            'variant': {
                'templateUrl': 'views/guided/hml/samples/typing/consensus-sequence/consensus-sequence-block/variant/variant.html',
                'controller': 'variant',
                'controllerAs': 'variantCtrl'
            },
            'sequenceQuality': {
                'templateUrl': 'views/guided/hml/samples/typing/consensus-sequence/consensus-sequence-block/sequence-quality/sequence-quality.html',
                'controller': 'sequenceQuality',
                'controllerAs': 'sequenceQualityCtrl'
            }
        },
        'consensusSequencePanels': {
            'referenceDatabase': {
                'templateUrl': 'views/guided/hml/samples/typing/consensus-sequence/reference-database/reference-database.html',
                'controller': 'referenceDatabase',
                'controllerAs': 'referenceDatabaseCtrl'
            },
            'consensusSequenceBlock': {
                'templateUrl': 'views/guided/hml/samples/typing/consensus-sequence/consensus-sequence-block/consensus-sequence-block.html',
                'controller': 'consensusSequenceBlock',
                'controllerAs': 'consensusSequenceBlockCtrl'
            }
        },
        'referenceDatabasePanels': {
            'referenceSequence': {
                'templateUrl': 'views/guided/hml/samples/typing/consensus-sequence/reference-database/reference-sequence/reference-sequence.html',
                'controller': 'referenceSequence',
                'controllerAs': 'referenceSequenceCtrl'
            }
        },
        'variantPanels': {
            'variantEffect': {
                'templateUrl': 'views/guided/hml/samples/typing/consensus-sequence/consensus-sequence-block/variant/variant-effect/variant-effect.html',
                'controller': 'variantEffect',
                'controllerAs': 'variantEffectCtrl'
            }
        },
        'typingMethodPanels': {
            'sso': {
                'templateUrl': 'views/guided/hml/samples/typing/typing-method/sso/sso.html',
                'controller': 'sso',
                'controllerAs': 'ssoCtrl'
            },
            'ssp': {
                'templateUrl': 'views/guided/hml/samples/typing/typing-method/ssp/ssp.html',
                'controller': 'ssp',
                'controllerAs': 'sspCtrl'
            },
            'sbtSanger': {
                'templateUrl': 'views/guided/hml/samples/typing/typing-method/sbt-sanger/sbt-sanger.html',
                'controller': 'sbtSanger',
                'controllerAs': 'sbtSangerCtrl'
            },
            'sbtNgs': {
                'templateUrl': 'views/guided/hml/samples/typing/typing-method/sbt-ngs/sbt-ngs.html',
                'controller': 'sbtNgs',
                'controllerAs': 'sbtNgsCtrl'
            }
        },
        'ssoPanels': {
            'properties': {
                'templateUrl': 'views/guided/hml/properties/properties.html',
                'controller': 'properties',
                'controllerAs': 'propertiesCtrl'
            }
        },
        'sspPanels': {
            'properties': {
                'templateUrl': 'views/guided/hml/properties/properties.html',
                'controller': 'properties',
                'controllerAs': 'propertiesCtrl'
            }
        },
        'genotypesPanels': {
            'diploidCombination': {
                'templateUrl': 'views/guided/hml/samples/typing/allele-assignment/genotypes/diploid-combination/diploid-combination.html',
                'controller': 'diploidCombination',
                'controllerAs': 'diploidCombinationCtrl'
            }
        },
        'diploidCombinationPanels': {
            'locusBlock': {
                'templateUrl': 'views/guided/hml/samples/typing/allele-assignment/genotypes/diploid-combination/locus-block/locus-block.html',
                'controller': 'locusBlock',
                'controllerAs': 'locusBlockCtrl'
            }
        },
        'locusBlockPanels': {
            'alleles': {
                'templateUrl': 'views/guided/hml/samples/typing/allele-assignment/genotypes/diploid-combination/locus-block/alleles/alleles.html',
                'controller': 'alleles',
                'controllerAs': 'allelesCtrl'
            }
        },
        'sbtNgsPanels': {
            'properties': {
                'templateUrl': 'views/guided/hml/properties/properties.html',
                'controller': 'properties',
                'controllerAs': 'propertiesCtrl'
            },
            'rawReads': {
                'templateUrl': 'views/guided/hml/samples/typing/typing-method/sbt-ngs/raw-reads/raw-reads.html',
                'controller': 'rawReads',
                'controllerAs': 'rawReadsCtrl'
            }
        },
        'amplificationPanels': {
            'sequence': {
                'templateUrl': 'views/guided/hml/sequence/sequence.html',
                'controller': 'sequence',
                'controllerAs': 'sequenceCtrl'
            }
        },
        'subAmplificationPanels': {
            'sequence': {
                'templateUrl': 'views/guided/hml/sequence/sequence.html',
                'controller': 'sequence',
                'controllerAs': 'sequenceCtrl'
            }
        },
        'gsspPanels': {
            'sequence': {
                'templateUrl': 'views/guided/hml/sequence/sequence.html',
                'controller': 'sequence',
                'controllerAs': 'sequenceCtrl'
            }
        },
        'sbtSangerPanels': {
            'properties': {
                'templateUrl': 'views/guided/hml/properties/properties.html',
                'controller': 'properties',
                'controllerAs': 'propertiesCtrl'
            },
            'amplification': {
                'templateUrl': 'views/guided/hml/samples/typing/typing-method/sbt-sanger/amplification/amplification.html',
                'controller': 'amplification',
                'controllerAs': 'amplificationCtrl'
            },
            'subAmplification': {
                'templateUrl': 'views/guided/hml/samples/typing/typing-method/sbt-sanger/sub-amplification/sub-amplification.html',
                'controller': 'subAmplification',
                'controllerAs': 'subAmplificationCtrl'
            },
            'gssp': {
                'templateUrl': 'views/guided/hml/samples/typing/typing-method/sbt-sanger/gssp/gssp.html',
                'controller': 'gssp',
                'controllerAs': 'gsspCtrl'
            }
        }
    };

    angular.module('hmlFhirAngularClientApp.constants').constant('appConfig', appConfig);
}());