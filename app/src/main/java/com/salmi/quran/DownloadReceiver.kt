package com.salmi.quran

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

/**
 * Created by Salmi on 8/4/2017.
 */

class DownloadReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
            Toast.makeText(context, "إنتهى التحميل", Toast.LENGTH_SHORT).show()
        }
    }
}
