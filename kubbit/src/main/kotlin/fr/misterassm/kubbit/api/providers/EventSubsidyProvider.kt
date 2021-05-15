package fr.misterassm.kubbit.api.providers

import fr.misterassm.kubbit.internal.Kubbit
import fr.misterassm.kubbit.internal.adapter.EventSubsidyAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.EventExecutor
import org.bukkit.plugin.Plugin
import java.util.function.BiConsumer
import java.util.function.BiPredicate
import java.util.function.Predicate

class EventSubsidyProvider<T : Event>(
    private val eventClass: Class<T>,
    private val eventPriority: EventPriority,

    private val exceptionConsumer: BiConsumer<in T, Throwable>,
    private val handleSubclasses: Boolean,

    private val filters: Array<Predicate<T>>,
    private val preExpiryTests: Array<BiPredicate<EventSubsidyAdapter<T>, T>>,
    private val midExpiryTests: Array<BiPredicate<EventSubsidyAdapter<T>, T>>,
    private val postExpiryTests: Array<BiPredicate<EventSubsidyAdapter<T>, T>>,
    private val handlers: Array<BiConsumer<EventSubsidyAdapter<T>, in T>>,

    ) : EventSubsidyAdapter<T>, EventExecutor, Listener {

    @Volatile
    var active = true
    set(value) {
        runBlocking { withContext(Dispatchers.Default) { field = value } }
    }

    override fun findEventClass(): Class<T> = eventClass

    fun register(plugin: Plugin) {
        Kubbit.pluginManager.registerEvent(this.eventClass, this, this.eventPriority, this, plugin, false)
    }

    override fun isActive(): Boolean = active

    override fun unregister(): Boolean {
        return if (!active) {
            active = false
            false
        } else {
            (eventClass.getMethod("getHandlerList").invoke(null) as HandlerList).unregister(this)
            true
        }
    }

    override fun isClosed(): Boolean = !active

    override fun execute(listener: Listener, event: Event) {
        when {
            handleSubclasses -> if (!eventClass.isInstance(event)) return
            event.javaClass != eventClass -> return
            !this.active -> {
                event.handlers.unregister(listener)
                return
            }
        }

        val eventInstance: T = this.eventClass.cast(event)

        preExpiryTests.firstOrNull { it.test(this, eventInstance) }
            ?.let {
                event.handlers.unregister(listener)
                active = false
                return
            }

        try {

            filters.firstOrNull { !it.test(eventInstance) }?.let { return }
            midExpiryTests.firstOrNull { it.test(this, eventInstance) }?.let {
                event.handlers.unregister(listener)
                active = false
                return
            }

            handlers.forEach { it.accept(this, eventInstance) }

        } catch (throwable: Throwable) {
            exceptionConsumer.accept(eventInstance, throwable)
        }

        postExpiryTests.firstOrNull { it.test(this, eventInstance) }?.let {
            event.handlers.unregister(listener)
            active = false
            return
        }

    }
}
