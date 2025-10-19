package dev.aurakai.auraframefx.security

import android.app.Service
import android.content.Intent
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntegrityMonitorService : Service() {
    override fun onBind(intent: Intent?): IBinder {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // TODO: Implement service logic
        return START_STICKY
    }
}
