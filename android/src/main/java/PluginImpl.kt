package com.plugin.safeareas

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowInsetsController

class PluginImpl {
    fun setColor(input: String, activity: Activity) {
        val color = parseColor(input)
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

    fun setTopBarColor(input: String, activity: Activity) {
        val color = parseColor(input)
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

    fun setBottomBarColor(input: String, activity: Activity) {
        val color = parseColor(input)
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

    private fun parseColor(input: String): Int {
        return try {
            when {
                input.startsWith("rgb(", true) -> {
                    val values = input.removePrefix("rgb(")
                        .removeSuffix(")")
                        .split(",")
                        .map { it.trim().toInt() }
                    Color.rgb(values[0], values[1], values[2])
                }
                input.startsWith("rgba(", true) -> {
                    val values = input.removePrefix("rgba(")
                        .removeSuffix(")")
                        .split(",")
                        .map { it.trim() }
                    Color.argb(
                        (values[3].toFloat() * 255).toInt(), // Alpha value in 0-1 converted to 0-255
                        values[0].toInt(),
                        values[1].toInt(),
                        values[2].toInt()
                    )
                }
                else -> Color.parseColor(input) // Handles HEX and color names
            }
        } catch (e: IllegalArgumentException) {
            // Fallback to a default color in case of an error
            Color.BLACK
        }
    }

    fun enableFullscreenScrolling(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.window.setDecorFitsSystemWindows(false) // Allow content to extend into the system bars
        } else {
            @Suppress("DEPRECATION")
            activity.window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            )
        }
    }

}