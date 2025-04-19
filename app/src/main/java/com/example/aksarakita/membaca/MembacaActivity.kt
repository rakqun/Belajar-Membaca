package com.example.aksarakita.membaca

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.aksarakita.BackgroundSoundService
import com.example.aksarakita.MainActivity
import com.example.aksarakita.R
import kotlinx.android.synthetic.main.activity_membaca.*
import java.util.Locale

class MembacaActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val REQUEST_CODE_SPEECH_TO_TEXT = 1001
        private const val MAX_READING_COUNT = 5
        const val RecordAudioRequestCode = 1
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
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var speechRecognizerIntent: Intent
    private var isRecording = false
    private var readingCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_membaca)

        if (checkPermission()) {
            setupSpeechRecognizer()
        }

        soundButton = MediaPlayer.create(this, R.raw.button)
        btnPlay.setOnClickListener(this)
        btnHomeMembaca.setOnClickListener(this)
        btnBackMembaca.setOnClickListener(this)
        updateText()

        val serviceIntent = Intent(this, BackgroundSoundService::class.java)
        stopService(serviceIntent)
    }

    private fun setupSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)

        speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}

            override fun onBeginningOfSpeech() {
                tvListening.visibility = View.VISIBLE
                tvListening.text = "ayo mulai membaca..."
            }
            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {}

            override fun onError(error: Int) {}

            override fun onResults(results: Bundle?) {
                pauseRecording()
                val data = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val text = data?.get(0) ?: ""
                val currentText = textMembaca[currentIndex]
                if (text.equals(currentText,true)) {
                    tvListening.text = "Benar...."
                    nextText()
                } else {
                    tvListening.text = "Salah...."
                }
            }
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    private fun startRecording() {
        isRecording = true
        btnPlay.setImageResource(R.drawable.pause)
        tvListening.visibility = View.VISIBLE
        tvListening.text = "ayo mulai membaca..."
        // You can start recording logic here
        speechRecognizer.startListening(speechRecognizerIntent)
    }

    private fun pauseRecording() {
        isRecording = false
        btnPlay.setImageResource(R.drawable.play_white)
        // You can pause recording logic here
        speechRecognizer.stopListening()
    }

    private fun checkPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    RecordAudioRequestCode
                )
            }
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RecordAudioRequestCode && grantResults.isNotEmpty()){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateText(){
        if (currentIndex >= textMembaca.size) {
            tvListening.text = "Hore kamu hebat sudah bisa membaca semuanya, tunggu update lagi yaa"
            disablePlayButton()
        } else {
            tvMembaca.text = textMembaca[currentIndex]
            tvListening.visibility = View.INVISIBLE
        }
    }

    private fun nextText() {
        if (currentIndex < textMembaca.size - 1) {
            currentIndex++
            updateText()
        } else {
            if (readingCount < MAX_READING_COUNT) {
                readingCount++
                currentIndex = 0
                updateText()
            } else {
                tvListening.text = "Anda telah mencapai maksimal membaca. Tetap semangat dan coba lagi nanti!"
                disablePlayButton()
            }
        }
    }

    private fun disablePlayButton() {
        btnPlay.isEnabled = false
        btnPlay.setImageResource(R.drawable.play_white)
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
                if (isRecording) {
                    pauseRecording()
                } else {
                    startRecording()
                }
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
