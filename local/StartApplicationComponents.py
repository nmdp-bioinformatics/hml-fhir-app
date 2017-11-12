from subprocess import Popen

commands = [
    'sh start_api.sh',
    'sh start_submission_consumer.sh',
    'sh start_conversion_consumer.sh'
]

processes = [Popen(cmd, shell=True) for cmd in commands]
