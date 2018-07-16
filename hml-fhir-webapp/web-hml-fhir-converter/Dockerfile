FROM centos

RUN useradd -ms /bin/bash bower
RUN yum install -y gcc-c++ make
RUN curl --silent --location https://rpm.nodesource.com/setup_6.x | bash -
RUN yum install -y nodejs
RUN yum install -y gcc ruby-devel rubygems
RUN yum install -y git

RUN npm install --g bower
RUN npm install --g grunt grunt-cli

EXPOSE 9002

WORKDIR /usr/src

ADD .jshintrc /usr/src/.jshintrc
ADD app/ /usr/src/app/
ADD bower.json /usr/src/bower.json
ADD Gruntfile.js /usr/src/Gruntfile.js
ADD package.json /usr/src/package.json

RUN gem install compass && \
    npm install && \
    bower install --allow-root && \
    grunt --force

CMD ["grunt", "serve", "--force"]
