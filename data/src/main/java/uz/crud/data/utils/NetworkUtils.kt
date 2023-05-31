package uz.crud.data.utils

import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Inject

object NetworkUtils {

    fun isConnect(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val internet = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        return wifi != null && wifi.isConnected || internet != null && internet.isConnected
    }
}