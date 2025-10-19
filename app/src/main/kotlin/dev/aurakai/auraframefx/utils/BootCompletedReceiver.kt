package dev.aurakai.auraframefx.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

@AndroidEntryPoint
class BootCompletedReceiver : BroadcastReceiver() {

    private val tag = "BootCompletedReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
        }
    }
}
