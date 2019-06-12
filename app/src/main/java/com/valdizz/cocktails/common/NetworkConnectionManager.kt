package com.valdizz.cocktails.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

/**
 * Class controlling the availability of Internet connection.
 *
 * @author Vlad Kornev
 */
class NetworkConnectionManager(context: Context) {

    private val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val request = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()
    var isConnected = false

    init {
        connManager.registerNetworkCallback(request, object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network?) {
                isConnected = true
            }

            override fun onUnavailable() {
                isConnected = false
            }
        })
    }
}