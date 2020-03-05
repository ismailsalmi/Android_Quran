package com.salmi.quran

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ArrayAdapter
import android.content.Context
import android.view.LayoutInflater

class ListAdapter(context: Context, users: ArrayList<String>) : ArrayAdapter<String>(context,
        0, users) {
    private val preferencesTheme = context.getSharedPreferences("THEMES", Context.MODE_PRIVATE)
    private val themeValue = preferencesTheme.getBoolean("theme", false)
    val user = users
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val position = position

        if (convertView == null) {
            if (this.themeValue == false) {
                convertView = LayoutInflater.from(context).inflate(R.layout.items_list_green, parent, false)
            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.items_list_dark, parent, false)
            }
        }

        val tvName = convertView!!.findViewById(R.id.titleShow) as TextView
        tvName.setText(user!![position])
        return convertView
    }

}