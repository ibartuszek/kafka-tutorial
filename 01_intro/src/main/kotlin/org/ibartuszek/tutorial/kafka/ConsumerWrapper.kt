package org.ibartuszek.tutorial.kafka

import kotlinx.coroutines.delay
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class ConsumerWrapper(properties: Properties, topic: String) {

    private val isConsuming = AtomicBoolean()
    private val stopped = AtomicBoolean()

    private val consumer = KafkaConsumer<String, String>(properties).apply {
        subscribe(listOf(topic))
    }

    suspend fun consume(handleMessage: (ConsumerRecord<String, String>) -> Unit) {
        isConsuming.set(true)
        stopped.set(false)
        while (isConsuming.get()) {
            delay(10)
            consumer.poll(Duration.of(100, ChronoUnit.MILLIS)).forEach(handleMessage)
        }
        stopped.set(true)
    }

    suspend fun close() {
        isConsuming.set(false)
        while (!stopped.get()) {
            delay(20L)
        }
        consumer.close()
    }

}
