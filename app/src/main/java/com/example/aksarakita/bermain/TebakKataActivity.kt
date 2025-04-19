package com.example.aksarakita.bermain

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.Image
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.aksarakita.BackgroundSoundService
import com.example.aksarakita.MainActivity
import com.example.aksarakita.R
import kotlinx.android.synthetic.main.activity_tebak_kata.*
import java.util.Locale

class TebakKataActivity : AppCompatActivity(), View.OnClickListener, TextToSpeech.OnInitListener {
    private val gambarList = arrayOf(
        R.drawable.jerapah, R.drawable.gajah, R.drawable.kucing, R.drawable.monyet, R.drawable.badak
    )

    private val kataBenar = arrayOf(
        "jerapah", "gajah", "kucing", "monyet", "badak"
    )

    private val randomKata = arrayOf(
        "ayam", "anjing", "cicak", "burung", "kodok", "katak", "sapi", "kambing", "rusa", "babi", "bebek"
    )

    private lateinit var textViewList: List<TextView>
    private var score: Int = 0
    private var currentQuestionIndex: Int = 0
    private lateinit var soundButton: MediaPlayer
    private lateinit var tts: TextToSpeech
    private lateinit var animation: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tebak_kata)
        showSystemUI()
        soundButton = MediaPlayer.create(this, R.raw.button)
        animation = AnimationUtils.loadAnimation(this, R.anim.blink)
        tts = TextToSpeech(this, this)

        textViewList = listOf(
            findViewById(R.id.kata1),
            findViewById(R.id.kata2),
            findViewById(R.id.kata3),
            findViewById(R.id.kata4)
        )
        startGame()

        val serviceIntent = Intent(this, BackgroundSoundService::class.java)
        stopService(serviceIntent)

        textViewList.forEach { textView ->
            textView.setOnClickListener {
                val selectedOption = textView.text.toString()
                checkAnswer(selectedOption)
            }
        }

        btnHomeTebak.setOnClickListener(this)
        btnBackTebak.setOnClickListener(this)
    }
    private fun startGame() {
        val kata = kataBenar[currentQuestionIndex]
        tts.speak(kata, TextToSpeech.QUEUE_FLUSH, null, "")

        tvTebak.setImageResource(gambarList[currentQuestionIndex])
        tvTebak.animation = animation
        val correctAnswerIndex = (textViewList.indices).random()
        textViewList[correctAnswerIndex].text = kata

        var optionIndex = 0
        for (i in textViewList.indices) {
            if (i != correctAnswerIndex) {
                textViewList[i].text = randomKata[optionIndex]
                optionIndex++
            }
        }
    }

    private fun showGameResultDialog() {
        val Builder = AlertDialog.Builder(this)
        val dialog = layoutInflater.inflate(R.layout.dialog_scor, null)
        Builder.setView(dialog)
        val builder_ = Builder.show()
        builder_.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val scor = dialog.findViewById<TextView>(R.id.scor)
        val btnOk = dialog.findViewById<LinearLayout>(R.id.btnOk)
        scor.text = score.toString()
        btnOk.setOnClickListener{
            builder_.dismiss()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            soundButton.start()
            finish()
        }
    }

    private fun checkAnswer(selectedOption: String) {
        val kata = kataBenar[currentQuestionIndex]
        if (selectedOption == kata) {
            score += 20
        }
        currentQuestionIndex++
        if (currentQuestionIndex >= kataBenar.size) {
            showGameResultDialog()
        } else {
            startGame()
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

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btnHomeTebak -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                soundButton.start()
                finish()
            }
            R.id.btnBackTebak -> {
                val intent = Intent(this, MenuBermainActivity::class.java)
                startActivity(intent)
                soundButton.start()
                finish()
            }
        }
    }

    override fun onInit(p0: Int) {
        if (p0 == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale("id", "ID"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Bahasa tidak didukung", Toast.LENGTH_SHORT).show()
            }
            tts.speak(kataBenar[currentQuestionIndex], TextToSpeech.QUEUE_FLUSH, null, "")
        } else {
            Toast.makeText(this, "Gagal menginisialisasi Text-to-Speech", Toast.LENGTH_SHORT).show()
        }
    }
}