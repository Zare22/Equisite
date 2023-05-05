package hr.sonsanddaughters.equisite.broadcasts

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.material.snackbar.Snackbar
import hr.sonsanddaughters.equisite.R
import hr.sonsanddaughters.equisite.framework.isOnline

class NetworkChangeBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (!(context!!.isOnline())) showConnectionSnackbar(context)
    }

    private fun showConnectionSnackbar(context: Context) {
        val snackbar = Snackbar.make(
            (context as Activity).findViewById(R.id.drawerLayout),
            R.string.no_internet_connection,
            Snackbar.LENGTH_SHORT
        )

        snackbar.setAction(R.string.dismiss) {
            snackbar.dismiss()
        }

        snackbar.show()
    }
}