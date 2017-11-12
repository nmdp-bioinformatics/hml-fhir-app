FROM centos

RUN useradd -ms /bin/bash bower
RUN yum install -y gcc-c++ make
RUN curl --silent --location https://rpm.nodesource.com/setup_6.x | bash -
RUN yum install -y nodejs
RUN yum install -y gcc ruby-devel rubygems
RUN yum install -y git

RUN npm install --g bower
RUN npm install --g grunt grunt-cli

EXPOSE 9005

WORKDIR /usr/src

ADD web-hml-fhir-conversion-dashboard/.jshintrc /usr/src/.jshintrc
ADD web-hml-fhir-conversion-dashboard/app/ /usr/src/app/
ADD web-hml-fhir-conversion-dashboard/bower.json /usr/src/bower.json
ADD web-hml-fhir-conversion-dashboard/Gruntfile.js /usr/src/Gruntfile.js
ADD web-hml-fhir-conversion-dashboard/package.json /usr/src/package.json

RUN gem install compass && \
    npm install && \
    bower install --allow-root && \
    grunt --force

CMD ["grunt", "serve", "--force"]
