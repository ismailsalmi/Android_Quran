package com.salmi.quran

import android.content.Context

object Darktheme {
    fun saveTheme(context: Context, boolean : Boolean) {
        val preferences = context.getSharedPreferences("THEMES",
                Context.MODE_PRIVATE)
        val editor = preferences!!.edit()
        editor.putBoolean("theme", boolean)
        editor.commit()

    }
}