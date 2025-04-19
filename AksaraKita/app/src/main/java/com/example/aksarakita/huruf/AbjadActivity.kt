package com.example.aksarakita.huruf

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.aksarakita.MainActivity
import com.example.aksarakita.R
import kotlinx.android.synthetic.main.activity_abjad.btnBack
import kotlinx.android.synthetic.main.activity_abjad.btnBackAbjad
import kotlinx.android.synthetic.main.activity_abjad.btnHomeAbjad
import kotlinx.android.synthetic.main.activity_abjad.btnNext
import kotlinx.android.synthetic.main.activity_abjad.huruf
import java.util.Locale

class AbjadActivity : AppCompatActivity(), TextToSpeech.OnInitListener, View.OnClickListener {
    private val imageIds = arrayOf(
        R.drawable.a,
        R.drawable.b,
        R.drawable.c,
        R.drawable.d,
        R.drawable.e,
        R.drawable.f,
        R.drawable.g,
        R.drawable.h,
        R.drawable.i,
        R.drawable.j,
        R.drawable.k,
        R.drawable.l,
        R.drawable.m,
        R.drawable.n,
        R.drawable.o,
        R.drawable.p,
        R.drawable.q,
        R.drawable.r,
        R.drawable.s,
        R.drawable.t,
        R.drawable.u,
        R.drawable.v,
        R.drawable.w,
        R.drawable.x,
        R.drawable.y,
        R.drawable.z
    )
    private var currentImageIndex: Int = 0
    private var currentLetter: Char = 'A'
    private lateinit var tts: TextToSpeech
    private lateinit var animation: Animation
    private lateinit var soundButton: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_abjad)
        showSystemUI()
        animation = AnimationUtils.loadAnimation(this, R.anim.anim_scale)

        tts = TextToSpeech(this, this)
        updateImage()

        soundButton = MediaPlayer.create(this, R.raw.button)

        if (currentImageIndex >= 0){
            btnBack.setOnClickListener {
                soundButton.start()
                previousImage()
            }
        }else{
            btnBack.isEnabled = false
        }

        if (currentImageIndex <= 25){
            btnNext.setOnClickListener {
                soundButton.start()
                nextImage()
            }
        }else{
            btnNext.isEnabled = false
        }

        btnHomeAbjad.setOnClickListener(this)
        btnBackAbjad.setOnClickListener(this)
    }

    private fun updateImage() {
        Handler().postDelayed({
            tts.speak(currentLetter.toString(), TextToSpeech.QUEUE_FLUSH, null, "") }
            ,200
        )
        huruf.setOnClickListener{
            tts.speak(currentLetter.toString(), TextToSpeech.QUEUE_FLUSH, null,"")
        }
        huruf.setImageResource(imageIds[currentImageIndex])
        huruf.animation = animation
    }

    private fun previousImage() {
        if (currentImageIndex > 0) {
            currentLetter--
            currentImageIndex--
            updateImage()
        }
    }

    private fun nextImage() {
        if (currentImageIndex < imageIds.size - 1) {
            currentLetter++
            currentImageIndex++
            updateImage()
        }
    }

    override fun onDestroy() {
        if (tts.isSpeaking) {
            tts.stop()
        }
        tts.shutdown()
        super.onDestroy()
    }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }
    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale("id", "ID"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Bahasa tidak didukung", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Gagal menginisialisasi Text-to-Speech", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnHomeAbjad -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                soundButton.start()
                finish()
            }
            R.id.btnBackAbjad -> {
                val intent = Intent(this, MenuAbjadActivity::class.java)
                startActivity(intent)
                soundButton.start()
                finish()
            }
        }
    }
}