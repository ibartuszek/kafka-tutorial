package org.ibartuszek.tutorial.kafka

import java.util.*

object Configuration {

    const val topic = "helloworld"

    fun producerProperties(): Properties = Properties().apply {
        addBootstrapServers()
        put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
        put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    }

    private fun Properties.addBootstrapServers() {
        put("bootstrap.servers", "localhost:9092,localhost:9093")
    }

    fun consumerProperties(): Properties = Properties().apply {
        addBootstrapServers()
        put("group.id", "helloconsumer")
        put("enable.auto.commit", "true")
        put("auto.commit.interval.ms", "1000")
        put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    }

}