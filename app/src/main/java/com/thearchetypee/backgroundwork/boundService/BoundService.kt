package com.thearchetypee.backgroundwork.boundService

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.widget.Toast

class BoundService : Service() {

    private val binder = LocalBinder()

    private val mGenerator = java.util.Random()

    /** Method for clients.  */
    private val randomNumber: Int
        get() = mGenerator.nextInt(100)
    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    fun sayHello() {
        Toast.makeText(applicationContext, "Hello from BoundService!", Toast.LENGTH_SHORT).show()
    }

    fun sayRandomNumber() {
        Toast.makeText(applicationContext, "Random number: $randomNumber", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(applicationContext, "BoundService destroyed", Toast.LENGTH_SHORT).show()
    }

    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods.
        fun getService(): BoundService = this@BoundService
    }
}