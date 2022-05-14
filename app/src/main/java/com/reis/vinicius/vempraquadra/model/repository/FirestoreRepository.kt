package com.reis.vinicius.vempraquadra.model.repository

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.google.firebase.firestore.Source
import java.util.concurrent.atomic.AtomicBoolean

abstract class FirestoreRepository<T>(application: Application) : Repository<T> {
    val isConnected = AtomicBoolean(true)

    data class LongId(
        val value: Long? = null
    )

    init {
        application.applicationContext.getSystemService(ConnectivityManager::class.java).apply {
            val connected = getNetworkCapabilities(activeNetwork)?.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_VALIDATED
            ) ?: false

            isConnected.set(connected)
            registerDefaultNetworkCallback(object: ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    isConnected.set(true)
                }

                override fun onLost(network: Network) {
                    isConnected.set(false)
                }
            })
        }
    }

    protected fun getSource() = if (isConnected.get()) Source.DEFAULT else Source.CACHE
}