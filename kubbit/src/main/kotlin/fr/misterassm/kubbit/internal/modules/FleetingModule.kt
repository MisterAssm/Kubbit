package fr.misterassm.kubbit.internal.modules

import fr.misterassm.kubbit.internal.function.FleetingConsumer

sealed interface FleetingModule {

    fun setup(consumer: FleetingConsumer)

    fun bindFleetingWith(consumer: FleetingConsumer) {
        consumer.bindFleeting(this)
    }

}
