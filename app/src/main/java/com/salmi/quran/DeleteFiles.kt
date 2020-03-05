package com.salmi.quran

import android.app.AlertDialog
import android.content.Context
import android.support.design.widget.Snackbar
import android.view.View
import java.io.File

/**
 * Created by Salmi on 8/5/2017.
 */

class DeleteFiles(context: Context, view: View) {
    private val file: File
    private var dir: File? = null
    private var build: AlertDialog.Builder? = null

    init {
        //delete audio Download
        file = File("/mnt/sdcard/Elkouchi/")
        //delete Dir
        dir = File("/mnt/sdcard/Elkouchi")
        try {
            if (file.isDirectory) {

                var sorahDelete = ""
                if (file.listFiles().size.equals(1)) {
                    sorahDelete = "سورة"
                } else {
                    sorahDelete = "سور"
                }

                build = AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT)
                build!!.setTitle("حذف")
                        .setMessage("سيؤدي هذا إلى مسح (${this.file.listFiles().size}) $sorahDelete.")
                        .setCancelable(false)
                        .setIcon(R.drawable.ic_delete_black_24dp)
                        .setPositiveButton("نعم") { dialogInterface, i ->
                            try {
                                var n = 0
                                for (f in this.file.listFiles()) {
                                    f.delete()
                                    n++
                                }

                                dir!!.delete()
                                Snackbar.make(view, "تم حذف" + " (${n}) " + sorahDelete,
                                        Snackbar.LENGTH_LONG).show()
                            } catch (e: InterruptedException) {
                            }

                        }.setNegativeButton("لا") { dialogInterface, i -> }.show()
            } else {
                Snackbar.make(view, "ليس لديك أي سور محملة.", Snackbar.LENGTH_LONG).show()
            }
        } catch (e: Exception) {

        }
    }


}
