package go.task.mvvmwithrxjava.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class Common {
    companion object {
        fun isOnline(context: Context): Boolean {
            val conn = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = conn.activeNetwork ?: return false
                val netCap = conn.getNetworkCapabilities(network) ?: return false
                return when {
                    netCap.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    netCap.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            } else {
                val info = conn.activeNetworkInfo ?: return false
                return info.isConnected
            }
        }
    }
}