package com.thearchetypee.backgroundwork.workmanager

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
import androidx.compose.ui.unit.dp
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.thearchetypee.backgroundwork.WorkManagerButtons
import com.thearchetypee.backgroundwork.getWorkManagerButtons
import com.thearchetypee.backgroundwork.ui.theme.BackgroundWorkTheme

class WorkManagerActivity : ComponentActivity() {

    private var workManager: WorkManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        workManager = WorkManager.getInstance(applicationContext)

        setContent {
            BackgroundWorkTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    WorkManagerScreen {
                        when(it) {
                            WorkManagerButtons.StartWorkManager -> {
                                startRepeatingWork()
                            }

                            WorkManagerButtons.StopWorkManager -> {
                                stopRepeatingWork()
                            }

                            WorkManagerButtons.ScheduleWorkManager -> {

                            }
                        }
                    }
                }
            }
        }
    }


    private fun startRepeatingWork() {
        val work = OneTimeWorkRequestBuilder<NotificationWorker>()
            .addTag(NotificationWorker.WORK_TAG)
            .build()
        workManager?.enqueue(work)
    }

    private fun stopRepeatingWork() {
        workManager?.cancelAllWorkByTag(NotificationWorker.WORK_TAG)
    }

    @Composable
    fun WorkManagerScreen(onClick: (type: WorkManagerButtons) -> Unit) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            for (
            button in getWorkManagerButtons()
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
}
