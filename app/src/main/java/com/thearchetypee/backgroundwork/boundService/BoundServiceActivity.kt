package com.thearchetypee.backgroundwork.boundService

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
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
import com.thearchetypee.backgroundwork.BoundServiceButtons
import com.thearchetypee.backgroundwork.getBoundServiceButtons
import com.thearchetypee.backgroundwork.ui.theme.BackgroundWorkTheme

class BoundServiceActivity : ComponentActivity() {
    private var boundService: BoundService? = null
    private var isBound = false

    private val connection by lazy {
        object : ServiceConnection {
            override fun onServiceConnected(className: ComponentName, service: IBinder) {
                val binder: BoundService.LocalBinder = service as BoundService.LocalBinder
                boundService = binder.getService()
                isBound = true
                Toast.makeText(this@BoundServiceActivity, "Bound to BoundService", Toast.LENGTH_SHORT).show()
            }

            override fun onServiceDisconnected(arg0: ComponentName) {
                boundService = null
                isBound = false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BackgroundWorkTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    BoundServiceScreen {
                        when (it) {
                            BoundServiceButtons.GetRandomNumber -> {
                                boundService?.sayRandomNumber()
                            }

                            BoundServiceButtons.SayHello -> {
                                boundService?.sayHello()
                            }

                            BoundServiceButtons.UnbindService -> {
                                if (isBound) {
                                    unbindService(connection)
                                    isBound = false
                                }
                            }

                            BoundServiceButtons.BindService -> {
                                val intent = Intent(this, BoundService::class.java)
                                bindService(intent, connection, Context.BIND_AUTO_CREATE)
                            }
                        }
                    }
                }
            }
        }

    }

    @Composable
    private fun BoundServiceScreen(onClick: (type: BoundServiceButtons) -> Unit) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            for (
            button in getBoundServiceButtons()
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
        super.onDestroy()
        if (isBound) {
            unbindService(connection)
            isBound = false
        }
    }
}