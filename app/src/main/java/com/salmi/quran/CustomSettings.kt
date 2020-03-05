package com.salmi.quran

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.Switch
import kotlinx.android.synthetic.main.settings_dark.*
import java.io.File
import java.lang.Exception

class CustomSettings : AppCompatActivity() {
    private var themeValue: Boolean = false
    private var styleValue: Int = 0
    private lateinit var toolbar: Toolbar
    private lateinit var windows: Window
    private lateinit var darkTheme: Switch
    private lateinit var changeFonts: Button
    private lateinit var clearCache: Button
    private lateinit var moreApps: Button
    private lateinit var shareApp: Button
    private lateinit var directory: Button
    private lateinit var folderSize: Button
    private lateinit var infoApp: Button
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        windows = this@CustomSettings.window
        val preferencesStyle = getSharedPreferences("FONTS",
                Context.MODE_PRIVATE)
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
        val preferencesTheme = getSharedPreferences("THEMES", Context.MODE_PRIVATE)
        this.themeValue = preferencesTheme!!.getBoolean("theme", false)
        if (this.themeValue == false) {
            windows.statusBarColor = resources.getColor(R.color.green)
            windows.navigationBarColor = resources.getColor(R.color.green)
            setContentView(R.layout.settings_green)
        } else {
            windows.statusBarColor = resources.getColor(R.color.dark)
            windows.navigationBarColor = resources.getColor(R.color.dark)
            setContentView(R.layout.settings_dark)
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
            startActivity(Intent(this@CustomSettings, MainActivity::class.java))
            finish()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 19911509)
        }

        darkTheme = findViewById(R.id.darkTheme)
        if (this.themeValue == false) darkTheme.isChecked = false else darkTheme.isChecked = true
        darkTheme.setOnCheckedChangeListener { buttonView, isChecked ->
            try {
                Darktheme.saveTheme(this@CustomSettings, isChecked)
            } catch (e: Exception) {
                ErrorsMessage().showMessage(this@CustomSettings.settingsLayout, e.message.toString())
            } finally {
                recreate()
            }
        }

        changeFonts = findViewById(R.id.changeFonts)
        changeFonts.setOnClickListener {
            val fonts = arrayOf("الخط الأول", "الخط الثاني", "الخط الثالث", "الخط الرابع", "الخط الخامس")
            val builder = AlertDialog.Builder(this@CustomSettings)
            builder.setTitle("إختر الخط المناسب")
            builder.setSingleChoiceItems(fonts, this.styleValue) { dialog, which ->
                Styles.saveStyle(this@CustomSettings, which)
            }
            builder.setPositiveButton("تأكيد") { dialog, which ->
                recreate()
            }
            builder.setNegativeButton("خروج", null)
            builder.create()
            builder.show()
        }

        moreApps = findViewById(R.id.moreApps)
        moreApps.setOnClickListener {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/search?q=Sakaoui")))
            } catch (e: Exception) {
                ErrorsMessage().showMessage(this@CustomSettings.settingsLayout, e.message.toString())
            }
        }

        shareApp = findViewById(R.id.shareApp)
        shareApp.setOnClickListener {
            try {
                val shareLinkApp = Intent(Intent.ACTION_SEND)
                shareLinkApp.type = "text/plain"
                shareLinkApp.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.salmi.quran")
                startActivity(shareLinkApp)
            } catch (e: Exception) {
                ErrorsMessage().showMessage(this@CustomSettings.settingsLayout, e.message.toString())
            }

        }

        directory = findViewById(R.id.deleteDirectory)
        directory.setOnClickListener {
            try {
                DeleteFiles(this@CustomSettings, this@CustomSettings.settingsLayout)
            } catch (e: Exception) {
                ErrorsMessage().showMessage(this@CustomSettings.settingsLayout, e.message.toString())
            }
        }

        folderSize = findViewById(R.id.folderSize)
        val file = File("/mnt/sdcard/Elkouchi/")
        folderSize.text = " حجم السور المحملة ${FilesSize.returnFileSize(file)}"

        infoApp = findViewById(R.id.infoApp)
        infoApp.setOnClickListener {
            try {
                startActivity(Intent(this@CustomSettings, InfoApp::class.java))
            } catch (e: Exception) {
                ErrorsMessage().showMessage(this@CustomSettings.settingsLayout, e.message.toString())
            }
        }
    }

    /////Back Pressed
    override fun onBackPressed() {
        startActivity(Intent(this@CustomSettings, MainActivity::class.java))
        finish()
        super.onBackPressed()
    }
}
