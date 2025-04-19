package com.example.aksarakita.membaca

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.widget.Toast
import com.example.aksarakita.MainActivity
import com.example.aksarakita.R
import kotlinx.android.synthetic.main.activity_membaca.btnBackMembaca
import kotlinx.android.synthetic.main.activity_membaca.btnHomeMembaca
import kotlinx.android.synthetic.main.activity_membaca.btnPlay
import kotlinx.android.synthetic.main.activity_membaca.tvMembaca
import java.util.Locale

class MembacaActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        private const val REQUEST_CODE_SPEECH_TO_TEXT = 1001
    }
    private val textMembaca = arrayOf(
        "Abang membeli teh manis di warung",
        "Mari menanam pohon bersama-sama",
        "Hari ini hujan deras sehingga daun berjatuhan",
        "Ibu guru mengajar di kelas menggunakan spidol",
        "Film pahlawan spider-man itu sangat hebat"
    )
    private var currentIndex: Int = 0
    private lateinit var soundButton: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_membaca)
        showSystemUI()
        soundButton = MediaPlayer.create(this, R.raw.button)
        btnPlay.setOnClickListener(this)
        btnHomeMembaca.setOnClickListener(this)
        btnBackMembaca.setOnClickListener(this)
    }

    private fun updateText(){
        if (currentIndex < textMembaca.size) {
            tvMembaca.text = textMembaca[currentIndex]
        } else {
            tvMembaca.text = "Hore kamu hebat sudah bisa membaca semuanya, tunggu update lagi yaa"
        }
    }

    private fun nextText() {
        if (currentIndex < textMembaca.size - 1) {
            currentIndex++
            updateText()
        }
    }

    private fun startSpeechToText() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak something...")
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_TO_TEXT)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SPEECH_TO_TEXT && resultCode == RESULT_OK && data != null) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!result.isNullOrEmpty()) {
                val recognizedText = result[0]
                val currentText = textMembaca[currentIndex]
                if (recognizedText.equals(currentText, ignoreCase = true)) {
                    nextText()
                } else {
                    Toast.makeText(this, "Yah Kamu Salah", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnHomeMembaca -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                soundButton.start()
                finish()
            }
            R.id.btnBackMembaca -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                soundButton.start()
                finish()
            }
            R.id.btnPlay -> {
                startSpeechToText()
                soundButton.start()
            }
        }
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
}