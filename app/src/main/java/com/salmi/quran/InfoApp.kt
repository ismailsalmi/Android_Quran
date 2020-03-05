package com.salmi.quran

import android.content.Context
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Window

class InfoApp : AppCompatActivity() {
    private var themeValue: Boolean = false
    private var styleValue: Int = 0
    lateinit var windows: Window
    lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        windows = this@InfoApp.window
        val preferencesStyle = getSharedPreferences("FONTS", Context.MODE_PRIVATE)
        styleValue = preferencesStyle.getInt("font", 3)
        if (this.styleValue == 0) {
            setTheme(R.style.AppTheme_1)
        } else if (this.styleValue == 1) {
            setTheme(R.style.AppTheme_2)
        } else if (this.styleValue == 2) {
            setTheme(R.style.AppTheme_3)
        } else if (this.styleValue == 3) {
            setTheme(R.style.AppTheme_4)
        } else if (this.styleValue == 4) {
            setTheme(R.style.AppTheme_5)
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        val preferencesTheme = getSharedPreferences("THEMES", Context.MODE_PRIVATE)
        this.themeValue = preferencesTheme!!.getBoolean("theme", false)
        if (this.themeValue == false) {
            windows.statusBarColor = resources.getColor(R.color.green)
            windows.navigationBarColor = resources.getColor(R.color.green)
            setContentView(R.layout.info_green)
        } else {
            windows.statusBarColor = resources.getColor(R.color.dark)
            windows.navigationBarColor = resources.getColor(R.color.dark)
            setContentView(R.layout.info_dark)
        }

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val directionLayout = resources.configuration
        if (directionLayout.layoutDirection == View.LAYOUT_DIRECTION_RTL) {
            if (this.themeValue == false) {
                toolbar.setNavigationIcon(R.drawable.ic_forward_black)
            } else {
                toolbar.setNavigationIcon(R.drawable.ic_forward_white)
            }
        } else {
            if (this.themeValue == false) {
                toolbar.setNavigationIcon(R.drawable.ic_back_black)
            } else {
                toolbar.setNavigationIcon(R.drawable.ic_back_white)
            }
        }
        toolbar.setNavigationOnClickListener {
            finish()
        }

    }


}
