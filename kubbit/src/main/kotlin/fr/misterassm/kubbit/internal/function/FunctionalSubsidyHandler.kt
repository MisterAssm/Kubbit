package fr.misterassm.kubbit.internal.function

import java.util.function.BiConsumer
import java.util.function.Consumer

interface FunctionalSubsidyHandler<T, S> {

    fun consumer(handler: Consumer<in T>): FunctionalSubsidyHandler<T, S>

    fun biConsumer(handler: BiConsumer<S, in T>): FunctionalSubsidyHandler<T, S>

    fun register(): S

}
