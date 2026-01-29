package com.sendspindroid.headless

import android.content.Intent
import android.app.Activity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.sendspindroid.playback.PlaybackService

class BootstrapActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val serviceIntent = Intent(this, PlaybackService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)

        finish()
    }
}
