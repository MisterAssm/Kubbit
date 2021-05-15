package fr.misterassm.kubbit.internal.adapter

import org.bukkit.event.Event

interface EventSubsidyAdapter<T : Event> : SubsidyAdapter {

    fun findEventClass(): Class<T>

}
