package com.salmi.quran

import android.content.Context
import android.view.animation.AnimationUtils

class Animations(context: Context) {

    private val splash_Anim = AnimationUtils.loadAnimation(context, R.anim.zom_in)
    private val listView_Anim = AnimationUtils.loadAnimation(context, R.anim.upp_from_buttom)
    private val buttonPlay_Animation = AnimationUtils.loadAnimation(context, R.anim.rotate_backward)
    private val buttonPause_Animation = AnimationUtils.loadAnimation(context, R.anim.rotate_forward)
    private val button_clickAnimation = AnimationUtils.loadAnimation(context, R.anim.button_click)

    fun splashAnim() = splash_Anim
    fun listViewAnim() = listView_Anim
    fun click_Play() = buttonPlay_Animation
    fun click_Pause() = buttonPause_Animation
    fun click_Animation() = button_clickAnimation

}