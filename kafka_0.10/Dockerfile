FROM java:8

VOLUME /usr

ADD create_topics.sh create_topics.sh
ADD kafka_2.12-0.10.2.1/bin/ /usr/bin/

ENTRYPOINT ["sh", "create_topics.sh"]
