package fr.misterassm.kubbit.internal.adapter

interface SubsidyAdapter : FleetingAdapter {

    fun isActive(): Boolean

    fun unregister(): Boolean

    override fun close() {
        unregister()
    }

}
