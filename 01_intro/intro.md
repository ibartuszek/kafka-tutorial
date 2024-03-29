# Setup

## Environment variables

To make this work you have to create the following environment variables:
```
export KAFKA_VERSION=$version
export KAFKA_HOME=$kafka
export PATH=${KAFKA_HOME}/bin:${PATH}
```

Where:
- `$version` is your kafka version. E.g.: 3.3.2
- `$kafka` is your kafka install folder. E.g.: /opt/kafka_2.13-3.3.2

## Kafka cluster

Start zookeeper and the kafka servers with docker-compose:
```
docker-compose up
```

# Basic practice with command line and java client

## Command line

- run kafka and zookeeper with docker-compose: `docker-compose up` in the project root directory

### Kafka via command line (with kafka on path)

- create a topic:
```
kafka-topics.sh --bootstrap-server localhost:9092 --create --topic helloworld --partitions 3 --replication-factor 3
```

- check the existing topics:
```
kafka-topics.sh --bootstrap-server localhost:9092 --list
```
-check the topic's detailed information:
```
kafka-topics.sh --bootstrap-server localhost:9092 --describe --topic helloworld
```

- send a message: (starts a producer)
```
kafka-console-producer.sh --broker-list localhost:9092 --topic helloworld
```

- listen the topic: (start a consumer from the beginning)
```
kafka-console-consumer.sh --bootstrap-server localhost:9093 --topic helloworld --from-beginning
```

### Kafka with java client

The small program can run with gradle from the project root directory:
```
./gradlew :intro:run
```

The application send 10 messages, then the consumer reads and print out the messages.
Then the ReplayConsumer reads the messages from the beginning of the topic and print out the consumed
message number.

### Kafka connect

#### With connect standalone

Run first the `connect-to-file-init.sh` script, which will create in `tmp` directory in the intro module:
- The `connect-standalone.properties` contains the bootstrap server address and the connect plugin path
- The `connect-file-source.properties` contains the topic and the source file location. 
- The `connect-file-sink.properties` contains the topic and the sink file location.
- The previous files are copied (and adjusted) from the original kafka file which can be found in the kafka config folder.
- The `helloworld-input.txt` is the input file where every new line became a new message in the broker

To start kafka connect (file source)

- uses `helloworld` topic

```
connect-standalone.sh tmp/connect-standalone.properties tmp/connect-file-source.properties
```

Start a kafka console consumer to test. Then edit the file.

To start kafka connect (file sink)

- uses: `helloworld-sink` topic

```
connect-standalone.sh tmp/connect-standalone.properties tmp/connect-file-sink.properties
```

Start a kafka console producer, send messages. Then check the file content.

NOTE: in case of kafka connect the kafka messages will be jsons e.g.:
```
{"schema":{"type":"string","optional":false},"payload":"Hello world!"}
```
That means we see the json in the consumer in case of file source and in case of sink file we have to send a json 
with the producer.

### Troubleshooting:

- ssh into the running container with:
```
docker exec -it kafka-1 /bin/bash
```