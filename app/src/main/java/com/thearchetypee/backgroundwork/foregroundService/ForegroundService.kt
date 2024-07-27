package com.thearchetypee.backgroundwork.foregroundService

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ForegroundService : Service() {

    companion object {
        private const val CHANNEL_ID = "my_channel"
        private const val NOTIFICATION_ID = 1
        const val STOP_SERVICE_ACTION = "STOP_SERVICE_ACTION"
    }

    private var notificationManager: NotificationManager? = null
    private var coroutineJob: Job? = null
    private val binder = LocalBinder()
    private var isServiceStarted = false

    private var serviceCallback: ServiceCallback? = null

    interface ServiceCallback {
        fun onServiceDestroyed()
    }

    fun setServiceCallback(callback: ServiceCallback) {
        serviceCallback = callback
    }
    inner class LocalBinder : Binder() {
        fun getService(): ForegroundService = this@ForegroundService
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d("ForegroundService", "onBind called")
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("ForegroundService", "onStartCommand called with action: ${intent?.action}, startId: $startId")

        if (intent?.action == STOP_SERVICE_ACTION) {
            Log.d("ForegroundService", "Stop action received")
            stopForegroundService()
        } else if (!isServiceStarted) {
            Log.d("ForegroundService", "Starting service")
            isServiceStarted = true
            startForeground()
            startTimer()
        }

        return START_STICKY
    }

    private fun stopForegroundService() {
        Log.d("ForegroundService", "Stopping foreground service")
        isServiceStarted = false
        coroutineJob?.cancel()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            @Suppress("DEPRECATION")
            stopForeground(true)
        }
        notificationManager?.cancel(NOTIFICATION_ID)
        serviceCallback?.onServiceDestroyed()
        stopSelf()
    }

    fun sayHello() {
        Toast.makeText(this, "Hello from Foreground Service", Toast.LENGTH_SHORT).show()
    }

    private fun startTimer() {
        coroutineJob?.cancel()
        coroutineJob = CoroutineScope(Dispatchers.Default).launch {
            var time = 0L
            while (isActive && isServiceStarted) {
                updateTimer(time.toFormattedTime())
                time += 1000
                delay(1000)
            }
        }
    }

    private fun updateTimer(time: String) {
        if (isServiceStarted) {
            val notification = baseNotification(time).build()
            notificationManager?.notify(NOTIFICATION_ID, notification)
        }
    }

    private fun startForeground() {
        try {
            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "My Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager?.createNotificationChannel(channel)
            }

            val notification = baseNotification("00:00:00.000").build()
            startForeground(NOTIFICATION_ID, notification)
        } catch (e: Exception) {
            Log.e("ForegroundService", "Error starting service: ${e.localizedMessage}")
        }
    }

    private fun getStopServiceIntent(): PendingIntent {
        val stopServiceIntent = Intent(this, ForegroundService::class.java).apply {
            action = STOP_SERVICE_ACTION
        }

        val pendingIntentFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        return PendingIntent.getService(this, 0, stopServiceIntent, pendingIntentFlag)
    }

    private fun baseNotification(time: String) = NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle("Foreground Service")
        .setContentText(time)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop Service", getStopServiceIntent())

    private fun Long.toFormattedTime(): String {
        val hours = this / 3600000
        val minutes = (this % 3600000) / 60000
        val seconds = (this % 60000) / 1000
        val milliseconds = this % 1000
        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds)
    }

    override fun onDestroy() {
        Log.d("ForegroundService", "onDestroy called")
        isServiceStarted = false
        coroutineJob?.cancel()

        super.onDestroy()
    }
}
