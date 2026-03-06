package com.kdbrian.rickmorty.util

sealed class NetworkStatus {
    object Available : NetworkStatus()
    object Unavailable : NetworkStatus()
    data class CapabilitiesChanged(
        val hasWifi: Boolean,
        val hasCellular: Boolean,
        val isValidated: Boolean
    ) : NetworkStatus()
}

fun NetworkStatus.isConnected(): Boolean {
    return when (this) {
        is NetworkStatus.Available -> true
        is NetworkStatus.CapabilitiesChanged -> isValidated
        NetworkStatus.Unavailable -> false
    }
}
