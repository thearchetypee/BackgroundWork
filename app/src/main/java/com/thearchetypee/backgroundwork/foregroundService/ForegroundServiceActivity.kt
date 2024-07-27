package com.thearchetypee.backgroundwork.foregroundService

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thearchetypee.backgroundwork.ForegroundServiceButtons
import com.thearchetypee.backgroundwork.getForegroundServiceButtons
import com.thearchetypee.backgroundwork.ui.theme.BackgroundWorkTheme

class ForegroundServiceActivity: ComponentActivity(), ForegroundService.ServiceCallback {

    private var foregroundService: ForegroundService? = null
    private var isBound = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as ForegroundService.LocalBinder
            foregroundService = binder.getService()
            foregroundService?.setServiceCallback(this@ForegroundServiceActivity)
            isBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
        }
    }

    private val serviceIntent by lazy { Intent(this@ForegroundServiceActivity, ForegroundService::class.java) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BackgroundWorkTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    ForegroundServiceScreen {
                        when (it) {
                            ForegroundServiceButtons.StartForegroundService -> {
                                startService(serviceIntent)
                            }

                            ForegroundServiceButtons.StopForegroundService -> {
                                stopForegroundService()
                            }

                            ForegroundServiceButtons.BindService -> {
                                bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)
                            }

                            ForegroundServiceButtons.UnbindService -> {
                                if (isBound) {
                                    unbindService(connection)
                                    isBound = false
                                }
                            }

                            ForegroundServiceButtons.SayHello -> {
                                if (isBound) {
                                   foregroundService?.sayHello()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ForegroundServiceScreen(onClick: (type: ForegroundServiceButtons) -> Unit) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            for (
            button in getForegroundServiceButtons()
            ) {
                Button(
                    onClick = { onClick(button) },
                    modifier = Modifier.size(width = 200.dp, height = 48.dp)
                ) {
                    Text(text = button.name)
                }
            }
        }
    }

    override fun onDestroy() {
        unbindServiceSafely()
        super.onDestroy()
    }

    private fun unbindServiceSafely() {
        if (isBound) {
            unbindService(connection)
            isBound = false
            Log.d("MainActivity", "Service unbound")
            Toast.makeText(this, "Service unbound", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopForegroundService() {
        val intent = Intent(this, ForegroundService::class.java).apply {
            action = ForegroundService.STOP_SERVICE_ACTION
        }
        startService(intent)
    }

    override fun onServiceDestroyed() {
        unbindServiceSafely()
        foregroundService = null
    }
}
