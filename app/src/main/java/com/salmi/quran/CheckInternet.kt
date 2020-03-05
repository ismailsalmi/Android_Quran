package com.salmi.quran

import android.content.Context
import android.net.ConnectivityManager

class CheckInternet(private val context: Context) {

    val isConnectingToInternet: Boolean
        get() {
            val connectivityManager = context.getSystemService(
                    Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val info = connectivityManager.activeNetworkInfo
                if (info != null && info.isConnected) {
                    return true
                }
            return false
        }
}



