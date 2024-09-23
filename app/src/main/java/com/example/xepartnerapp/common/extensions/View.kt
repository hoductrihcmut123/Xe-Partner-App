package com.example.xepartnerapp.common.extensions

import android.content.Context
import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import com.example.xepartnerapp.common.utils.Constants

private var lastClickTime: Long = 0

fun View.singleClickListener(
    singleClickWithCheckLogin: (() -> Unit)? = null,
    isLoginProvider: Boolean = true,
    hiddenKeyboard: Boolean? = false,
    delayTime: Long = Constants.DELAY_SING_CLICK_300MS,
    singleClick: (() -> Unit)? = null
) {
    setOnClickListener {
        if (SystemClock.elapsedRealtime() - lastClickTime < delayTime) return@setOnClickListener
        else {
            if (isLoginProvider) {
                singleClick?.invoke()
            } else {
                singleClickWithCheckLogin?.invoke()
            }
            when (hiddenKeyboard) {
                true -> context.showSoftKeyboard(false)
                else -> {}
            }
        }
        lastClickTime = SystemClock.elapsedRealtime()
    }
}

fun View.hideKeyboard() {
    val inputMethod: InputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethod.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard() {
    val inputMethod: InputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethod.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun View.setMargins(
    leftMarginPx: Int? = null,
    topMarginPx: Int? = null,
    rightMarginPx: Int? = null,
    bottomMarginPx: Int? = null
) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val params = layoutParams as ViewGroup.MarginLayoutParams
        leftMarginPx?.let { params.leftMargin = it }
        topMarginPx?.let { params.topMargin = it }
        rightMarginPx?.let { params.rightMargin = it }
        bottomMarginPx?.let { params.bottomMargin = it }
        requestLayout()
    }
}

inline fun View.afterMeasured(crossinline f: View.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object :
        ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}