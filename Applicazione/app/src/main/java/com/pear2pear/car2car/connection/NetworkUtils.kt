import android.content.Context
import android.net.ConnectivityManager

class NetworkUtils {

    companion object {
        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE)
                        as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }

        fun wirelessConnection(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE)
                        as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return if (networkInfo != null && networkInfo.type ==
                ConnectivityManager.TYPE_WIFI) {
                networkInfo.isConnected
            } else false
        }
    }

}