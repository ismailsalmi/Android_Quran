package com.salmi.quran

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import kotlinx.android.synthetic.main.splash_dark.*
import java.lang.Exception

class Splash : AppCompatActivity() {
    private var themeValue: Boolean = false
    private lateinit var windows: Window
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        windows = this@Splash.window
        val preferencesTheme = getSharedPreferences("THEMES", Context.MODE_PRIVATE)
        this.themeValue = preferencesTheme!!.getBoolean("theme", false)
        if (this.themeValue == false) {
            windows.statusBarColor = resources.getColor(R.color.green)
            windows.navigationBarColor = resources.getColor(R.color.green)
            setContentView(R.layout.splash_green)
        } else {
            windows.statusBarColor = resources.getColor(R.color.dark)
            windows.navigationBarColor = resources.getColor(R.color.dark)
            setContentView(R.layout.splash_dark)
        }

        val img = findViewById<ImageView>(R.id.imageView)
        img.animation = Animations(this@Splash).splashAnim()

        object : Thread() {
            override fun run() {
                try {

                    sleep(3000)
                    startActivity(Intent(this@Splash, MainActivity::class.java))

                } catch (e: Exception) {
                    ErrorsMessage().showMessage(this@Splash.splashLayout, e.message.toString())
                } finally {
                    finish()
                }
            }
        }.start()

    }

}
