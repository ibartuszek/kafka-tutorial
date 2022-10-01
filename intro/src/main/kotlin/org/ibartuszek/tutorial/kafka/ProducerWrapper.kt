package org.ibartuszek.tutorial.kafka

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import java.util.*
import java.util.concurrent.Future

class ProducerWrapper(properties: Properties) {

    private val producer = KafkaProducer<String, String>(properties)

    fun sendMessage(topic: String, key: String? = null, message: String): Future<RecordMetadata> {
        val recordMetadata = producer.send(ProducerRecord(topic, key, message))
        println("Producer has sent key=$key message=$message")
        return recordMetadata
    }

    fun close() {
        producer.close()
    }

}
