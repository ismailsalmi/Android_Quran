package com.salmi.quran

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.player_dark.*
import java.io.IOException
import java.util.concurrent.TimeUnit

class MainPlayer : AppCompatActivity(), View.OnClickListener {
    private var themeValue: Boolean = false
    private var styleValue: Int = 0
    private lateinit var windows: Window
    private lateinit var toolbar: Toolbar
    private var data: String? = null
    private var name: String? = null
    private var txtname: TextView? = null
    private var startTime = 0.0
    private var finalTime = 0.0
    private val myHandler = Handler()
    private var buplay_pause: ImageView? = null
    private var img_forwardTime: ImageView? = null
    private var img_backwardTime: ImageView? = null
    private var isClicked = true
    private var seekBar: SeekBar? = null
    private var mp = MediaPlayer()
    private var textViewleft: TextView? = null
    private var textViewright: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        windows = this@MainPlayer.window
        val preferencesTheme = getSharedPreferences("THEMES", Context.MODE_PRIVATE)
        this.themeValue = preferencesTheme!!.getBoolean("theme", false)
        if (this.themeValue == false) {
            windows.statusBarColor = resources.getColor(R.color.green)
            windows.navigationBarColor = resources.getColor(R.color.green)
            setContentView(R.layout.player_green)
        } else {
            windows.statusBarColor = resources.getColor(R.color.dark)
            windows.navigationBarColor = resources.getColor(R.color.dark)
            setContentView(R.layout.player_dark)
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
            mp.stop()
            finish()
        }
        txtname = findViewById(R.id.txtvName)
        buplay_pause = findViewById(R.id.buplay_pause)
        textViewleft = findViewById(R.id.textViewleft)
        textViewright = findViewById(R.id.textViewright)

        img_forwardTime = findViewById(R.id.imageView2)
        img_forwardTime!!.setOnClickListener(this)
        img_backwardTime = findViewById(R.id.imageView3)
        img_backwardTime!!.setOnClickListener(this)
        if (this.themeValue == false) {
            img_forwardTime!!.setImageResource(R.drawable.ic_player_forward_black)
            img_backwardTime!!.setImageResource(R.drawable.ic_player_rewind_black)
        } else {
            img_forwardTime!!.setImageResource(R.drawable.ic_player_forward_white)
            img_backwardTime!!.setImageResource(R.drawable.ic_player_rewind_white)
        }


        // Intent Receive
        val `in` = intent.extras
        data = `in`!!.getString("link")
        name = `in`.getString("name")

        txtname!!.text = "صورة " + name

        mp.setOnCompletionListener {
            buplay_pause!!.animation = Animations(this@MainPlayer).click_Play()
            if (this.themeValue == false) {
                buplay_pause!!.setImageResource(R.drawable.ic_play_circle_black)
            } else {
                buplay_pause!!.setImageResource(R.drawable.ic_play_circle_white)
            }

            mp.start()
            buplay_pause!!.animation = Animations(this@MainPlayer).click_Pause()
            if (this.themeValue == false) {
                buplay_pause!!.setImageResource(R.drawable.ic_pause_circle_black)
            } else {
                buplay_pause!!.setImageResource(R.drawable.ic_pause_circle_white)
            }
        }

        ////media player online

        try {
            if (this.themeValue == false) {
                buplay_pause!!.setImageResource(R.drawable.ic_pause_circle_black)
            } else {
                buplay_pause!!.setImageResource(R.drawable.ic_pause_circle_white)
            }

            mp.apply {
                setAudioStreamType(AudioManager.STREAM_MUSIC)
                setDataSource(data)
                prepare()
                start()
            }


            myHandler.postDelayed(runRunnable(), 100)
            finalTime = mp.duration.toDouble()
            startTime = mp.currentPosition.toDouble()
            ////TextLeft Time
            textViewleft!!.text = String.format("%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong()),
                    TimeUnit.MILLISECONDS.toSeconds(finalTime.toLong()) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong())))


        } catch (e: IOException) {
            ErrorsMessage().showMessage(this@MainPlayer.mainplayerLayout, e.message.toString())
        }

        /// Button Play And Pause
        buplay_pause!!.setOnClickListener {
            try {
                if (isClicked == false) {
                    buplay_pause!!.animation = Animations(this@MainPlayer).click_Play()
                    if (this.themeValue == false) {
                        buplay_pause!!.setImageResource(R.drawable.ic_pause_circle_black)
                    } else {
                        buplay_pause!!.setImageResource(R.drawable.ic_pause_circle_white)
                    }

                    mp.start()
                    if (oneTimeOnly == 0) {
                        seekBar!!.max = finalTime.toInt()
                        oneTimeOnly = 1
                    }

                    seekBar!!.progress = startTime.toInt()

                    isClicked = true

                } else {
                    buplay_pause!!.animation = Animations(this@MainPlayer).click_Pause()
                    if (this.themeValue == false) {
                        buplay_pause!!.setImageResource(R.drawable.ic_play_circle_black)
                    } else {
                        buplay_pause!!.setImageResource(R.drawable.ic_play_circle_white)
                    }

                    mp.pause()
                    isClicked = false

                }
            } catch (e: Exception) {
                ErrorsMessage().showMessage(this@MainPlayer.mainplayerLayout, e.message.toString())
            }
        }

        seekBar = findViewById<SeekBar>(R.id.seekBar)
        var progress = 0
        seekBar!!.max = mp.duration
        seekBar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                progress = i
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                mp.seekTo(progress)

            }
        })

        ////Run Thread
        val mythread = thread()
        mythread.start()
    }

    // Method Implement Click

    override fun onClick(view: View) {

        try {
            if (view.id == R.id.imageView2) {

                img_forwardTime!!.animation = Animations(this@MainPlayer).click_Animation()
                val forwardTime = 5000
                val temp = startTime.toInt()

                if (temp + forwardTime <= finalTime) {
                    startTime = startTime + forwardTime
                    mp.seekTo(startTime.toInt())
                }

            } else {
                img_backwardTime!!.animation = Animations(this@MainPlayer).click_Animation()
                val backwardTime = 5000
                val temp = startTime.toInt()

                if (temp - backwardTime > 0) {
                    startTime = startTime - backwardTime
                    mp.seekTo(startTime.toInt())

                }
            }

        } catch (e: Exception) {
            ErrorsMessage().showMessage(this@MainPlayer.mainplayerLayout, e.message.toString())
        }

    }


    ///Thread code
    internal inner class thread : Thread() {

        override fun run() {

            while (mp != null) {

                try {

                    Thread.sleep(1000)

                } catch (e: Exception) {
                }

                seekBar!!.post { seekBar!!.progress = mp.currentPosition }
            }
        }
    }

    /////Back Pressed
    private var curentValue: Long = 0

    override fun onBackPressed() {
        if ((curentValue + 2000) > System.currentTimeMillis()) {
            mp.stop()
            finish()
            super.onBackPressed()
        } else {
            Toast.makeText(this@MainPlayer, "إضغط مرتين للخروج", Toast.LENGTH_SHORT).show()
        }
        curentValue = System.currentTimeMillis()
    }

    companion object {
        private val TAG = "MainPlayer"
        var oneTimeOnly = 0
    }

    internal fun runRunnable(): Runnable {
        /// Runnable for TextView left And Textview Right
        var updateSongTime: Runnable = object : Runnable {
            override fun run() {
                startTime = mp.currentPosition.toDouble()
                ////TextView Right
                textViewright!!.text = String.format("%d:%d",
                        TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                        TimeUnit.MILLISECONDS.toSeconds(startTime.toLong()) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(startTime.toLong())))

                seekBar!!.progress = startTime.toInt()
                myHandler.postDelayed(this, 100)
            }
        }
        return updateSongTime
    }

    override fun onStop() {
        if(mp.isPlaying){
            NotificationBadge.badge(this@MainPlayer, "صورة " + name!!)
        }else{
//            NotificationBadge.cancelNotification()
        }
        super.onStop()
    }

}
