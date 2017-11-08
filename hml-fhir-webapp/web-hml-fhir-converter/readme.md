# hml-fhir-angular-client

This project is generated with [yo angular generator](https://github.com/yeoman/generator-angular)
version 0.15.1.

## Prerequisites

Running this application locally requires Node package manager, this can be obtained at: https://nodejs.org/en/.  It is recommended that you use the 'Current (v 7.2.1)' version of node.js.  Bower will also be required, this can easily be installed by in the build & development section.  

Ruby is required to install compass. Please see build & development section for details.

## Build & development (Build / Run Environment)

Install NPM by following the link above, download node.js and following installation instructions.

Run 'gem install compass' to install compass via Ruby.

Run 'npm install -g bower' to install bower.

Run 'npm install -g grunt grunt-cli' to install grunt.

## Build & development (Application)

cd to the root of this directory.

Run 'npm install' to install applicaiton node packages.

Run 'bower install' to install application bower libraries.

** NOTE: There is a bug in one of the node package files (web-HmlFhirAngularClient/node_modules/grunt-contrib-cssmin/tasks/cssmin.js).  A solution has been commited to this file and included in this release.  Should you recieve an error while building this package with grunt, please validate the change to line 41 (web-HmlFhirAngularClient/node_modules/grunt-contrib-cssmin/tasks/cssmin.js) shows the same as this link: https://github.com/nmdp-bioinformatics/web-HmlFhirAngularClient/blob/master/node_modules/grunt-contrib-cssmin/tasks/cssmin.js.  This is caused by npm overwriting the committed file on 'npm install'.

Run `grunt` for building and `grunt serve` for preview.

## Testing

Running `grunt test` will run the unit tests with karma.
