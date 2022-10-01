package org.ibartuszek.tutorial.kafka

import java.time.Instant
import java.util.concurrent.Executors

class App {
    val greeting: String
        get() {
            return "Hello World!"
        }
}

fun main() {
    val executorService = Executors.newFixedThreadPool(1)
    val consumer = ConsumerWrapper(Configuration.consumerProperties(), Configuration.topic)
    executorService.submit(consumer)
    println("Start producer")
    ProducerWrapper(Configuration.producerProperties()).apply {
        val recordMetadata =
            sendMessage(topic = Configuration.topic, message = "${App().greeting} (${Instant.now()})").get()
        println(
            "recordMetadata=${recordMetadata}, " +
                    "offset=${recordMetadata.offset()}, " +
                    "timeStamp=${Instant.ofEpochMilli(recordMetadata.timestamp())}, " +
                    "topic=${recordMetadata.topic()}, " +
                    "partiotion=${recordMetadata.partition()}, " +
                    "serializedKeySize=${recordMetadata.serializedKeySize()}, " +
                    "serializedValueSize=${recordMetadata.serializedValueSize()}"
        )
        close()
    }
    Thread.sleep(5000L)
    consumer.close()
    executorService.shutdown()
}
