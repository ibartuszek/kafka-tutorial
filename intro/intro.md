# Basic practice with command line and java client

## Command line

- run kafka and zookeeper with docker-compose: `docker-compose up` in the project root directory

### Kafka via command line (from docker container)

- ssh into the running container with:
```
docker exec -it kafka-1 /bin/bash
```

- create a topic:
```
/bin/kafka-topics --bootstrap-server kafka-1:9092 --create --topic helloworld --partitions 3 --replication-factor 3
```

- check the existing topics:
```
/bin/kafka-topics --bootstrap-server kafka-1:9092 --list
```
-check the topic's detailed information:
```
bin/kafka-topics --bootstrap-server kafka-1:9092 --describe --topic helloworld
```

- send a message: (starts a producer)
```
/bin/kafka-console-producer --broker-list localhost:9092 --topic helloworld
```

- listen the topic: (start a consumer from the beginning)
```
/bin/kafka-console-consumer --bootstrap-server localhost:9093 --topic helloworld --from-beginning
```
