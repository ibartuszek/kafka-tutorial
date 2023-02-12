package org.ibartuszek.tutorial.kafka

import kotlinx.coroutines.delay
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.TopicPartition
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.*

class ReplayConsumer(properties: Properties, topic: String) {

    private val consumer: KafkaConsumer<String, String>
    private val partitions: List<TopicPartition>

    init {
        consumer = KafkaConsumer<String, String>(properties)
        partitions = consumer.partitionsFor(topic).map {
            TopicPartition(it.topic(), it.partition())
        }
        consumer.assign(partitions)
    }

    suspend fun consume(offset: Long, handleMessage: (ConsumerRecord<String, String>) -> Unit) {
        partitions.forEach {
            consumer.seek(it, offset)
        }
        consumeRecords(handleMessage)
    }

    private suspend fun consumeRecords(handleMessage: (ConsumerRecord<String, String>) -> Unit) {
        var finished = false
        while(!finished) {
            val records = consumer.poll(Duration.of(100, ChronoUnit.MILLIS))
            records.forEach(handleMessage)
            consumer.commitAsync()
            delay(10)
            finished = records.isEmpty
        }
    }

    fun close() {
        consumer.close()
    }

}
