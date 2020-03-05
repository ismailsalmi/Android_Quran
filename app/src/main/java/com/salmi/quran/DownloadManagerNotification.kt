package com.salmi.quran

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.support.design.widget.Snackbar
import android.view.View
import java.io.File

/**
 * Created by Salmi on 8/4/2017.
 */

class DownloadManagerNotification(context: Context, view: View,
                                  url: String, name: String, number: String) {
    private val path = "/mnt/sdcard/Elkouchi/"
    private var directory: File? = null

    init {
        directory = File(path)
        directory!!.mkdirs()
        try {
            val allReadyDownload = File(path + "$number.mp3")
            if (allReadyDownload.isFile) {
                Snackbar.make(view, "تم تحميلها سابقا", Snackbar.LENGTH_SHORT).show()
            } else {
                val downloadmanager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                val uri = Uri.parse(url)
                val request = DownloadManager.Request(uri)
                request.setTitle("صورة  $name")
                request.setDescription("جاري التحميل...")
                request.setAllowedOverRoaming(false)
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.
                        NETWORK_MOBILE)
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                request.setDestinationUri(Uri.parse("file://$directory/$number.mp3"))
                downloadmanager!!.enqueue(request)
            }

        } catch (e: Exception) {
            Snackbar.make(view, "خطأ في التحميل", Snackbar.LENGTH_SHORT).show()
        }

    }
}
