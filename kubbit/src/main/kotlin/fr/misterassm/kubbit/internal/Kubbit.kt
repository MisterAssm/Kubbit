@file:JvmName("Kubbit")
@file:JvmMultifileClass
package fr.misterassm.kubbit.internal

import org.bukkit.Bukkit
import org.bukkit.Server

object Kubbit {

    val server: Server by lazy { Bukkit.getServer() }

    val console by lazy { server.consoleSender }

    val pluginManager by lazy { server.pluginManager }

    val servicesManager by lazy { server.servicesManager }

    val bukkitScheduler by lazy { server.scheduler }

}
