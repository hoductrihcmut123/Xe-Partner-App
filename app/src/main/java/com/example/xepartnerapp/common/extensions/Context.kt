package com.example.xepartnerapp.common.extensions

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

fun Context.showSoftKeyboard(show: Boolean) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    when (show) {
        true -> imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        else -> when (this) {
            is Activity -> imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
        }
    }
}