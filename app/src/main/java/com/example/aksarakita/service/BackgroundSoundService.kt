package com.example.aksarakita

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class BackgroundSoundService : Service() {
    private lateinit var bgSound: MediaPlayer

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        bgSound = MediaPlayer.create(this, R.raw.backsound)
        bgSound.isLooping = true // Untuk memainkan musik dalam loop
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        bgSound.start()
        return START_STICKY
    }

    override fun onDestroy() {
        bgSound.stop()
        bgSound.release()
        super.onDestroy()
    }
}
