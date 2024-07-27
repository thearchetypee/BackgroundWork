package com.thearchetypee.backgroundwork

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thearchetypee.backgroundwork.backgroundservice.BackgroundService
import com.thearchetypee.backgroundwork.intentservice.ExampleIntentService
import com.thearchetypee.backgroundwork.ui.theme.BackgroundWorkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BackgroundWorkTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                        Greeting {
                            when (it) {
                                Buttons.IntentService -> {
                                    val exampleIntentService = Intent(this, ExampleIntentService::class.java)
                                    startService(exampleIntentService)
                                }

                                Buttons.Service -> {
                                    val backgroundService = Intent(this, BackgroundService::class.java)
                                    startService(backgroundService)
                                }

                                Buttons.BoundService -> {

                                }

                                Buttons.ForegroundService -> {

                                }

                                Buttons.WorkManager -> {

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Greeting(onClick: (type: Buttons) -> Unit) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        for (
        button in getButtons()
        ) {
            Button(
                onClick = { onClick(button) },
                modifier = Modifier.size(width = 200.dp, height =48.dp)
            ) {
                Text(text = button.name)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BackgroundWorkTheme {
        Greeting {

        }
    }
}