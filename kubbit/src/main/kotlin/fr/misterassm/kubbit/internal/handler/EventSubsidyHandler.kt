package fr.misterassm.kubbit.internal.handler

import fr.misterassm.kubbit.internal.adapter.EventSubsidyAdapter
import fr.misterassm.kubbit.internal.function.FunctionalSubsidyHandler
import org.bukkit.event.Event
import java.util.function.BiConsumer
import java.util.function.Consumer

interface EventSubsidyHandler<T : Event> : FunctionalSubsidyHandler<T, EventSubsidyAdapter<T>> {

    override fun consumer(handler: Consumer<in T>): FunctionalSubsidyHandler<T, EventSubsidyAdapter<T>> = biConsumer { _, u ->
        handler.accept(u)
    }

    override fun biConsumer(handler: BiConsumer<EventSubsidyAdapter<T>, in T>): FunctionalSubsidyHandler<T, EventSubsidyAdapter<T>>
}
