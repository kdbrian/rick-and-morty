package com.kdbrian.rickmorty.util

// NetworkStatus.kt
sealed class NetworkStatus {
    object Available : NetworkStatus()
    object Unavailable : NetworkStatus()
    data class CapabilitiesChanged(
        val hasWifi: Boolean,
        val hasCellular: Boolean,
        val isValidated: Boolean
    ) : NetworkStatus()
}