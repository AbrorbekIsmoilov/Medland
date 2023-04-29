package com.med.medland.presentation.fragment.otherComponents.connection

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.LiveData
import com.med.medland.data.locale.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import javax.net.SocketFactory

class ConnectivityManager(context: Context) : LiveData<Boolean>() {

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val networkInfo = connectivityManager.activeNetwork
    private val validNetworks : MutableSet<Network> = HashSet()

    private fun checkValidNetworks() { postValue(validNetworks.size > 0) }

    override fun onActive() {
        super.onActive()

        networkCallback = createNetworkCallback()
        val networkRequest = NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        checkConnection()
    }


    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)

            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            val hasInternetCapability = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

            if (hasInternetCapability == true) {
                CoroutineScope(Dispatchers.IO).launch {
                    val hasInternet = DoesNetworkHaveInternet.execute(network.socketFactory)
                    if (hasInternet) {
                        withContext(Dispatchers.Main) {
                            validNetworks.add(network)
                            checkValidNetworks()
                        }
                    }
                }
            }
        }
        override fun onLost(network: Network) {
            super.onLost(network)
            validNetworks.remove(network)
            checkValidNetworks()
        }

    }

    object DoesNetworkHaveInternet {
        fun execute(socketFactory : SocketFactory) : Boolean {
            return try {
                Log.d(Constants.CONNECTIVITY_MANAGER, "execute: PINGING GOOGLE")
                val socket = socketFactory.createSocket() ?: throw IOException("SOCKET is NULL")
                socket.connect(InetSocketAddress("8.8.8.8", 53),1500)
                socket.close()
                Log.d(Constants.CONNECTIVITY_MANAGER, "execute: PING SUCCESS")
                true
            }catch (e : IOException) {
                Log.e(Constants.CONNECTIVITY_MANAGER, "execute: NO INTERNET CONNECTION !!")
                false
            }
        }
    }

    fun checkConnection() {
        if (networkInfo == null) postValue(false)
        else postValue(true)
    }
}