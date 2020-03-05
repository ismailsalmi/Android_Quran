package com.salmi.quran

import android.content.Context

object Styles {
    fun saveStyle(context: Context, getFont: Int) {
        val preferences = context.getSharedPreferences("FONTS", Context.MODE_PRIVATE)
        val editor = preferences!!.edit()
        editor.putInt("font", getFont)
        editor.commit()
    }
}