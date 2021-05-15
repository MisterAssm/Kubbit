package fr.misterassm.kubbit.internal.function

import fr.misterassm.kubbit.internal.modules.FleetingModule

interface FleetingConsumer {

    fun <T : AutoCloseable> bind(fleeting: T): T

    fun <T : FleetingModule> bindFleeting(fleetingModule: T) = fleetingModule.also { it.setup(this) }

}
