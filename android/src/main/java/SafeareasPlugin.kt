package cc.cinea.safeareas

import android.app.Activity
import app.tauri.annotation.Command
import app.tauri.annotation.InvokeArg
import app.tauri.annotation.TauriPlugin
import app.tauri.plugin.JSObject
import app.tauri.plugin.Plugin
import app.tauri.plugin.Invoke

@InvokeArg
class SetColorArgs {
  lateinit var hex: String
}

@TauriPlugin
class SafeareasPlugin(private val activity: Activity): Plugin(activity) {
    private val implementation = PluginImpl()

    @Command
    fun setColor(invoke: Invoke) {
        val args = invoke.parseArgs(SetColorArgs::class.java)
        implementation.setColor(args.hex, activity)
        invoke.resolve()
    }

    @Command
    fun setTopBarColor(invoke: Invoke) {
        val args = invoke.parseArgs(SetColorArgs::class.java)
        implementation.setTopBarColor(args.hex, activity)
        invoke.resolve()
    }

    @Command
    fun setBottomBarColor(invoke: Invoke) {
        val args = invoke.parseArgs(SetColorArgs::class.java)
        implementation.setBottomBarColor(args.hex, activity)
        invoke.resolve()
    }
}