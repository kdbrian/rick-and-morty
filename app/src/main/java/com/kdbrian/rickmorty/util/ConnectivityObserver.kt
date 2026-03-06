package com.kdbrian.rickmorty.util

// ConnectivityObserver.kt
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class ConnectivityObserver(context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /**
     * Emits [NetworkStatus] updates whenever network connectivity changes.
     * Uses [callbackFlow] to bridge the callback-based [ConnectivityManager.NetworkCallback]
     * into a cold Flow that is automatically cleaned up when cancelled.
     */
    val networkStatus: Flow<NetworkStatus> = callbackFlow {

        // 1. Define what kind of networks we're interested in
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        // 2. Implement the NetworkCallback — each override sends an event downstream
        val networkCallback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                trySend(NetworkStatus.Available)
            }

            override fun onLost(network: Network) {
                trySend(NetworkStatus.Unavailable)
            }

            override fun onUnavailable() {
                trySend(NetworkStatus.Unavailable)
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                val status = NetworkStatus.CapabilitiesChanged(
                    hasWifi = networkCapabilities
                        .hasTransport(NetworkCapabilities.TRANSPORT_WIFI),
                    hasCellular = networkCapabilities
                        .hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR),
                    isValidated = networkCapabilities
                        .hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                )
                trySend(status)
            }
        }

        // 3. Register the callback to start receiving updates
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        // 4. Emit the current state immediately so collectors don't wait for a change
        val isCurrentlyConnected = connectivityManager.activeNetwork
            ?.let { connectivityManager.getNetworkCapabilities(it) }
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

        trySend(
            if (isCurrentlyConnected) NetworkStatus.Available else NetworkStatus.Unavailable
        )

        // 5. awaitClose unregisters the callback when the Flow is cancelled or the
        //    collector's coroutine scope is destroyed — preventing memory leaks
        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }.distinctUntilChanged() // Suppress duplicate consecutive emissions
}