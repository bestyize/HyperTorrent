package com.thewind.util

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.DialogFragment
import com.thewind.hypertorrent.main.globalApplication

/**
 * @author: read
 * @date: 2023/3/17 下午11:10
 * @description:
 */

fun Int.toPx(): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        globalApplication.resources.displayMetrics
    ).toInt()
}

fun Int.spToPx(): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(),
        globalApplication.resources.displayMetrics
    )
}

object ViewUtils {

    fun getScreenWidth(): Int {
        return globalApplication.resources.displayMetrics.widthPixels
    }

    fun getScreenHeight(): Int {
        return globalApplication.resources.displayMetrics.heightPixels
    }

    fun getScreenHeightDivWidth(): Float {
        return getScreenHeight().toFloat() / getScreenWidth().toFloat()
    }

    fun enterImmersiveFullScreenMode(activity: Activity?) {
        activity ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.window?.insetsController?.hide(WindowInsets.Type.systemBars())
        } else {
            val flags =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            activity.window?.decorView?.systemUiVisibility = flags
        }
    }

    fun enterFullScreenMode(activity: Activity?, isLight: Boolean) {
        activity ?: return
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        activity.window?.decorView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        setStatusBarColor(activity, isLight)

    }

    fun exitFullScreenMode(activity: Activity?, isLight: Boolean) {
        activity ?: return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.window?.insetsController?.show(WindowInsets.Type.statusBars())
            activity.window?.statusBarColor = Color.TRANSPARENT
        }
        setStatusBarColor(activity, isLight)
    }

    fun setStatusBarColor(activity: Activity?, isLight: Boolean, color: Int = Color.TRANSPARENT) {
        val window = activity?.window
        window ?: return
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = color
        window.decorView.fitsSystemWindows = true
        val compat = ViewCompat.getWindowInsetsController(activity.window.decorView)
        compat?.isAppearanceLightStatusBars = isLight
    }
}


fun DialogFragment.fillWidth() {
    dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog?.window?.attributes?.gravity = Gravity.BOTTOM
    dialog?.window?.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    )
    dialog?.window?.setWindowAnimations(android.R.style.Animation_InputMethod)
}

fun DialogFragment.fillFullScreen() {
    dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog?.window?.attributes?.gravity = Gravity.TOP
    dialog?.window?.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.MATCH_PARENT
    )
    dialog?.window?.setWindowAnimations(android.R.style.Animation_InputMethod)
}