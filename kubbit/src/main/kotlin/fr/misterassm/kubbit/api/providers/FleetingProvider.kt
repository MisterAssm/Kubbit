package fr.misterassm.kubbit.api.providers

import fr.misterassm.kubbit.internal.adapter.FleetingAdapter
import fr.misterassm.kubbit.internal.exceptions.FleetingCloseException
import fr.misterassm.kubbit.internal.function.FleetingConsumer
import java.util.*
import java.util.concurrent.ConcurrentLinkedDeque

class FleetingProvider : FleetingAdapter, FleetingConsumer {

    private val closeables: Deque<AutoCloseable> = ConcurrentLinkedDeque()

    fun push(autoCloseable: AutoCloseable) {
        closeables.push(autoCloseable)
    }

    override fun close() {
        var ac: AutoCloseable

        while (closeables.poll().also { ac = it } != null) {
            ac.close()
        }

        throw FleetingCloseException()
    }

    fun cleanUp() {
        closeables.removeIf { ac: AutoCloseable ->
            (ac as? FleetingProvider)?.cleanUp()
            (ac as? FleetingAdapter)?.isClosed() ?: false
        }

    }

    override fun <T : AutoCloseable> bind(fleeting: T): T = fleeting.also { push(it) }
}
