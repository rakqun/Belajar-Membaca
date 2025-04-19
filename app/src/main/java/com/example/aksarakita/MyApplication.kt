package com.example.aksarakita

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.view.View
import kotlinx.android.synthetic.main.activity_membaca.tvListening

class MyApplication : Application() {
    var activityCount = 0

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(ActivityLifecycleCallback())
    }

    fun isAppInForeground(): Boolean {
        return activityCount > 0
    }

    private inner class ActivityLifecycleCallback : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

        override fun onActivityStarted(activity: Activity) {
            activityCount++
        }

        override fun onActivityResumed(activity: Activity) {}

        override fun onActivityPaused(activity: Activity) {}

        override fun onActivityStopped(activity: Activity) {
            activityCount--
            if (activityCount == 0) {
                // Menghentikan BackgroundSoundService jika tidak ada activity aktif
                val serviceIntent = Intent(this@MyApplication, BackgroundSoundService::class.java)
                stopService(serviceIntent)
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

        override fun onActivityDestroyed(activity: Activity) {}
    }
}
