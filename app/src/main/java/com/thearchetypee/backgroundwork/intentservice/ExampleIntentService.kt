package com.thearchetypee.backgroundwork.intentservice

import android.app.IntentService
import android.content.Intent
import android.util.Log
import android.widget.Toast


class ExampleIntentService(name: String? = "Example Intent Service") : IntentService(name) {

    @Deprecated("Deprecated in Java")
    override fun onHandleIntent(intent: Intent?) {
        // Handle the intent
        Log.d("ExampleIntentService", "Task in progress")

        // Simulate a long running task by
        // sleeping the thread for 5 seconds
        try {
            Thread.sleep(5000)
        } catch (e: InterruptedException) {
            // Print stack trace if an
            // InterruptedException occurs
            e.printStackTrace()
        }
        Toast.makeText(applicationContext, "Task completed", Toast.LENGTH_SHORT).show()
        Log.d("ExampleIntentService", "Task completed")
    }
}