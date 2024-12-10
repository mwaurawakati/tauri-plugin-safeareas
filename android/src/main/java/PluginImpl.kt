package com.plugin.safeareas

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowInsetsController

class PluginImpl {
    fun setColor(hex: String, activity: Activity) {
        val color = Color.parseColor(hex)
        activity.window.statusBarColor = color

        val isLightColor = isColorLight(color)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val appearance = if (isLightColor) {
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            } else {
                0
            }
            activity.window.insetsController?.setSystemBarsAppearance(appearance, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 使用 SystemUiVisibility 设置状态栏图标和文字为深色
            @Suppress("DEPRECATION")
            activity.window.decorView.systemUiVisibility = if (isLightColor) {
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                0
            }
        }
    }

    private fun isColorLight(color: Int): Boolean {
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        val brightness = (0.299 * red + 0.587 * green + 0.114 * blue) / 255
        return brightness > 0.5
    }

    fun setTopBarColor(hex: String, activity: Activity) {
        val color = Color.parseColor(hex)
        activity.window.statusBarColor = color

        val isLightColor = isColorLight(color)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val appearance = if (isLightColor) {
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            } else {
                0
            }
            activity.window.insetsController?.setSystemBarsAppearance(
                appearance,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            @Suppress("DEPRECATION")
            activity.window.decorView.systemUiVisibility = if (isLightColor) {
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                0
            }
        }
    }

    fun setBottomBarColor(hex: String, activity: Activity) {
        val color = Color.parseColor(hex)
        activity.window.navigationBarColor = color

        val isLightColor = isColorLight(color)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val appearance = if (isLightColor) {
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
            } else {
                0
            }
            activity.window.insetsController?.setSystemBarsAppearance(
                appearance,
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @Suppress("DEPRECATION")
            activity.window.decorView.systemUiVisibility = if (isLightColor) {
                View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            } else {
                0
            }
        }
    }
}