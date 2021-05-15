package fr.misterassm.kubbit.internal.adapter

import fr.misterassm.kubbit.internal.function.FleetingConsumer

interface FleetingAdapter: AutoCloseable {

    override fun close()

    fun isClosed() = false

    fun bindConsumer(consumer: FleetingConsumer) {
        consumer.bind(this)
    }
}
