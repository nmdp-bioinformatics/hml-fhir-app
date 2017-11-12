from subprocess import Popen
import time


commands = [
    'sh start_zookeeper.sh',
    'sh start_kafka.sh'
]

processes = [Popen(cmd, shell=True) for cmd in commands]

time.sleep(6)

Popen('sh create_topics.sh', shell=True)
