package org.ibartuszek.tutorial.kafka

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import java.util.*
import java.util.concurrent.TimeUnit

class ProducerWrapper(properties: Properties) {

    private val producer = KafkaProducer<String, String>(properties)

    fun sendMessage(
        topic: String,
        key: String? = null,
        message: String,
        timeOut: Long = DEFAULT_TIMEOUT
    ): RecordMetadata = producer.send(ProducerRecord(topic, key, message)).get(timeOut, TimeUnit.MILLISECONDS)

    fun close() {
        producer.close()
    }

    companion object {
        private const val DEFAULT_TIMEOUT = 1000L
    }

}
