package org.ibartuszek.tutorial.kafka

import org.apache.kafka.clients.consumer.KafkaConsumer
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class ConsumerWrapper(properties: Properties, topic: String): Runnable {

    private val isConsuming = AtomicBoolean()
    private val stopped = AtomicBoolean()

    private val consumer = KafkaConsumer<String, String>(properties).apply {
        subscribe(listOf(topic))
    }

    override fun run() {
        println("Start consumer")
        isConsuming.set(true)
        stopped.set(false)
        while (isConsuming.get()) {
            consumer.poll(Duration.of(100, ChronoUnit.MILLIS)).forEach {
                println("Incoming message, offset=${it.offset()}, key=${it.key()}, value=${it.value()}")
            }
        }
        println("Consumer stopped polling messages.")
        stopped.set(true)
    }

    fun close() {
        isConsuming.set(false)
        while (!stopped.get()) {
            Thread.sleep(20L)
        }
        consumer.close()
        println("Consumer closed.")
    }

}
