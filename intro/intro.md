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

### Troubleshooting:

- ssh into the running container with:
```
docker exec -it kafka-1 /bin/bash
```