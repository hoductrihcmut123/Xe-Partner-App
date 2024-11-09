package com.example.xepartnerapp.common.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.Settings
import android.view.KeyEvent
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.xepartnerapp.R
import com.example.xepartnerapp.common.extensions.singleClickListener
import com.example.xepartnerapp.databinding.DialogConfirmCancelBinding

/**
 * show single alert dialog
 */
private var showingDialog: Dialog? = null

fun <T : ViewDataBinding> Activity.showCustomDialog(
    @LayoutRes layoutId: Int,
    @DimenRes width: Int = R.dimen._348sdp,
    @DimenRes height: Int = R.dimen._185sdp,
    title: String? = null,
    message: String? = null,
    textPositive: String? = null, positiveListener: (() -> Unit)? = null,
    textNegative: String? = null, negativeListener: (() -> Unit)? = null,
    dismissButton: Boolean = false, canceledOnTouchOutside: Boolean = false,
    dismissAndDoSomething: (() -> Unit)? = null,
    isUsingWidthAndHeight: Boolean = true,
    skipDismissPositive: Boolean = false,
    isForceUpdateDialog: Boolean? = null
): Dialog {
    val inflater = LayoutInflater.from(this)
    val view = DataBindingUtil.inflate<T>(inflater, layoutId, null, false)
    return Dialog(this).apply {
        view.root.let { binding ->
            setContentView(binding)
            setCanceledOnTouchOutside(canceledOnTouchOutside)
            if (showingDialog?.isShowing == true) {
                showingDialog?.dismiss()
            }
            title?.let {
                binding.findViewById<TextView>(R.id.tvTitle)?.text = it
            }
            textPositive?.let {
                binding.findViewById<Button>(R.id.btnPositive)?.apply {
                    text = it
                    singleClickListener(singleClick = {
                        positiveListener?.invoke()
                        if (skipDismissPositive.not()) dismiss()
                    })
                }
            }
            textNegative?.let {
                binding.findViewById<Button>(R.id.btnNegative)?.apply {
                    text = it
                    singleClickListener(singleClick = {
                        negativeListener?.invoke()
                        dismiss()
                    })
                }
            }
        }
        setOnDismissListener {
            dismissAndDoSomething?.invoke()
            showingDialog = null
        }
        setOnKeyListener { _, keyCode, _ -> keyCode == KeyEvent.KEYCODE_BACK }
        showingDialog = this
        if (isUsingWidthAndHeight) {
            window?.setLayout(
                resources.getDimensionPixelSize(width),
                resources.getDimensionPixelSize(height)
            )
        }
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        show()
    }
}

@SuppressLint("StringFormatMatches")
fun Activity.showLocationPermissionDialog() {
    showCustomDialog<DialogConfirmCancelBinding>(
        R.layout.dialog_confirm_cancel,
        title = getString(R.string.PleaseGrantLocation),
        canceledOnTouchOutside = false,
        textPositive = getString(R.string.NotAllowed),
        textNegative = getString(R.string.Allow),
        positiveListener = {}, negativeListener = {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", "com.example.xepartnerapp", null)
            }
            startActivity(intent)
        }
    )
}

@SuppressLint("StringFormatMatches")
fun Activity.showLocationRequestDialog() {
    showCustomDialog<DialogConfirmCancelBinding>(
        R.layout.dialog_confirm_cancel,
        title = getString(R.string.PleaseTurnOnLocation),
        canceledOnTouchOutside = false,
        textPositive = getString(R.string.NotAllowed),
        textNegative = getString(R.string.Allow),
        positiveListener = {}, negativeListener = {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
    )
}
