package com.salmi.quran

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.support.design.widget.BottomSheetDialog
import android.view.LayoutInflater
import android.widget.Button

class InternetEnableSheet(context: Context) {
    val sheet = BottomSheetDialog(context)
    val layout = LayoutInflater.from(context).inflate(R.layout.sheet_dialog, null, true)
    init {
        sheet.setContentView(layout)
        sheet.setCancelable(true)
        val buttonWifi = layout.findViewById(R.id.button_wifi) as Button
        val buttonSimCard = layout.findViewById(R.id.button_card_sim) as Button
        buttonWifi.setOnClickListener {
            context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            sheet.dismiss()
        }
        buttonSimCard.setOnClickListener {
            context.startActivity(Intent(Settings.ACTION_DATA_ROAMING_SETTINGS))
            sheet.dismiss()
        }
        sheet.create()
        sheet.show()
    }

}