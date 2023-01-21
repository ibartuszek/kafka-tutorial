package org.ibartuszek.tutorial.kafka

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import java.time.Instant
import java.util.concurrent.CopyOnWriteArrayList

class App {
    val greeting: String
        get() {
            return "Hello World!"
        }
}

private const val RECORDS_SIZE_TO_SEND = 10

fun main() {
    runBlocking {
        sendingReceivingExample()
        delay(2000) // For separate the test cases
        readingFromBeginning()
    }
}

private suspend fun readingFromBeginning() {
    coroutineScope {
        val consumer = ReplayConsumer(Configuration.consumerProperties(), Configuration.topic)
        val consumedMessages = CopyOnWriteArrayList<ConsumerRecord<String, String>>()
        launch {
            consumer.consume(0) { consumedMessages.add(it) }
        }.join()
        println("Consumed messages=${consumedMessages.size} (from beginning)")
        consumer.close()
        println("ReplayConsumer closed.")
    }
}

private suspend fun sendingReceivingExample() {
    coroutineScope {
        val consumer = ConsumerWrapper(Configuration.consumerProperties(), Configuration.topic)
        val consumedMessages = CopyOnWriteArrayList<ConsumerRecord<String, String>>()
        launch {
            consumer.consume {
                consumedMessages.add(it)
                logConsumerRecord(it)
            }
        }
        delay(3000) // To reduce the time between the production and consumption of the messages
        sendIngMessages()
        while (consumedMessages.size < RECORDS_SIZE_TO_SEND) {
            println("Waiting for messages...")
            delay(500)
        }
        consumer.close()
        println("Consumer closed.")
    }
}

private suspend fun sendIngMessages() {
    coroutineScope {
        val producer = ProducerWrapper(Configuration.producerProperties())
        val sendingJobs = (1..RECORDS_SIZE_TO_SEND).map {
            launch {
                logSentRecords(
                    producer.sendMessage(topic = Configuration.topic, message = "${App().greeting} (${Instant.now()})")
                )
            }
        }
        println("Records are sent")
        sendingJobs.joinAll()
        println("Closing producer")
        producer.close()
    }
}

private fun logConsumerRecord(it: ConsumerRecord<String, String>) {
    println("Incoming message at=${Instant.now()}, offset=${it.offset()}, key=${it.key()}, value=${it.value()}")
}

private fun logSentRecords(recordMetadata: RecordMetadata) {
    println(
        "recordMetadata=${recordMetadata}, " +
                "offset=${recordMetadata.offset()}, " +
                "timeStamp=${Instant.ofEpochMilli(recordMetadata.timestamp())}, " +
                "topic=${recordMetadata.topic()}, " +
                "partiotion=${recordMetadata.partition()}, " +
                "serializedKeySize=${recordMetadata.serializedKeySize()}, " +
                "serializedValueSize=${recordMetadata.serializedValueSize()}"
    )
}
