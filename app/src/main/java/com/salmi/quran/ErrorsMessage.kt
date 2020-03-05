package com.salmi.quran

import android.support.design.widget.Snackbar
import android.view.View

class ErrorsMessage {
    private var view : View? = null
    private var errorMessage : String? = null

    fun showMessage(view : View, exceptionMessage: String){
        this.view = view
        this.errorMessage = exceptionMessage
        Snackbar.make(view!!, this.errorMessage.toString(), Snackbar.LENGTH_SHORT).show()
    }

}