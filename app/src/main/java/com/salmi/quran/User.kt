package com.salmi.quran

import com.google.firebase.database.IgnoreExtraProperties

/**
 * Created by Developper on 12/15/2016.
 */
@IgnoreExtraProperties
data class User(
        val soraName: String? = "",
        val soraLink: String? = ""

)
