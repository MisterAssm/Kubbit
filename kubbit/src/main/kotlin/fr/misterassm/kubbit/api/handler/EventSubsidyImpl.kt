package fr.misterassm.kubbit.api.handler

import fr.misterassm.kubbit.internal.adapter.EventSubsidyAdapter
import fr.misterassm.kubbit.internal.function.FunctionalSubsidyHandler
import fr.misterassm.kubbit.internal.handler.EventSubsidyHandler
import org.bukkit.event.Event
import java.util.function.BiConsumer

class EventSubsidyImpl<T : Event> : EventSubsidyHandler<T> {

    override fun register(): EventSubsidyAdapter<T> {
        TODO("Not yet implemented")
    }

    override fun biConsumer(handler: BiConsumer<EventSubsidyAdapter<T>, in T>): FunctionalSubsidyHandler<T, EventSubsidyAdapter<T>> {
        TODO("Not yet implemented")
    }
}
