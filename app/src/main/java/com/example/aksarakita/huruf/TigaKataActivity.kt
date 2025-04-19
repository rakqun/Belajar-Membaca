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
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.example.aksarakita.MainActivity
import com.example.aksarakita.R
import kotlinx.android.synthetic.main.activity_tiga_kata.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.HashMap
import java.util.Locale

class TigaKataActivity : AppCompatActivity(), TextToSpeech.OnInitListener, View.OnClickListener {
    private var src1 = arrayOf(
        R.drawable.ba,
        R.drawable.ce
    )
    private var src2 = arrayOf(
        R.drawable.li,
        R.drawable.ma
    )
    private var src3 = arrayOf(
        R.drawable.ta,
        R.drawable.ra
    )

    private var kata1 = arrayOf("ba","ce","cu","di","ke","ge","bu","de","bu","de","de","no","du","pe","me","me","ne")
    private var kata2 = arrayOf("li","ma","ri","ma","ma","ja","da","ri","da","ri","li","vi","ra","pa","na","na","ra")
    private var kata3 = arrayOf("ta","ra","ga","na","na","la","ya","ta","ya","ta","ma","ka","si","ya","ra","ri","ka")
    private var kataLengkap = arrayOf(
        "balita","cemara","curiga","dimana","kemana",
        "gejala","budaya","derita","budaya","derita",
        "delima","novika","durasi","pepaya","menara","menari","neraka")

    private var currentImageIndex: Int = 0
    private var ttsJob: Job? = null

    private lateinit var tts: TextToSpeech
    private lateinit var animation: Animation
    private lateinit var soundButton: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tiga_kata)
        showSystemUI()
        animation = AnimationUtils.loadAnimation(this, R.anim.fade)

        tts = TextToSpeech(this, this)

        soundButton = MediaPlayer.create(this, R.raw.button)

        btnBackTigaSuku.setOnClickListener(this)
        btnHomeTigaSuku.setOnClickListener(this)

        btnNext.setOnClickListener {
            soundButton.start()
            tampilGambarBerikutnya()
        }

        btnBack.setOnClickListener {
            soundButton.start()
            tampilGambarSebelumnya()
        }
    }

    private fun tampilkanGambarSekarang() {

        val utteranceId1 = "Kata1" + System.currentTimeMillis()
        val utteranceId2 = "Kata2" + System.currentTimeMillis()
        val utteranceId3 = "Kata3" + System.currentTimeMillis()
        val utteranceId4 = "KataLengkap" + System.currentTimeMillis()

        val params1 = HashMap<String, String>()
        params1[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = utteranceId1

        val params2 = HashMap<String, String>()
        params2[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = utteranceId2

        val params3 = HashMap<String, String>()
        params3[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = utteranceId3

        val params4 = HashMap<String, String>()
        params4[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = utteranceId4

        val generator = ColorGenerator.MATERIAL
        val color = generator.randomColor

        ttsJob?.cancel() // Cancel any previous speech jobs

        ttsJob = GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.Main) {
                tts.speak(kata1[currentImageIndex], TextToSpeech.QUEUE_FLUSH, params1)
                tvKata1.visibility = View.VISIBLE
                tvKata1.text = kata1[currentImageIndex]
                tvKata1.startAnimation(animation)
                tvKata1.setTextColor(color)

                // Delay untuk memastikan suara 1 telah selesai sebelum memulai suara 2
                delay(getDurationOfSpeech(kata1[currentImageIndex]) + 100)
                tvKata2.visibility = View.VISIBLE
                tvKata2.text = kata2[currentImageIndex]
                tvKata2.setTextColor(color)
                tvKata2.startAnimation(animation)
                tts.speak(kata2[currentImageIndex], TextToSpeech.QUEUE_ADD, params2)

                if (kata3[currentImageIndex].isEmpty()){
                    // Delay untuk memastikan suara 3 telah selesai sebelum memulai suara lengkap
                    tvKata3.visibility = View.INVISIBLE
                    tvKata3.visibility = View.GONE
                    delay(getDurationOfSpeech(kata2[currentImageIndex]) + 300)
                    tts.speak(kataLengkap[currentImageIndex], TextToSpeech.QUEUE_ADD, params4)
                } else {
                    delay(getDurationOfSpeech(kata2[currentImageIndex]) + 300)
                    tvKata3.visibility = View.VISIBLE
                    tvKata3.text = kata3[currentImageIndex]
                    tvKata3.startAnimation(animation)
                    tvKata3.setTextColor(color)
                    tts.speak(kata3[currentImageIndex], TextToSpeech.QUEUE_ADD, params3)
                    delay(getDurationOfSpeech(kata3[currentImageIndex]) + 1000)
                    tts.speak(kataLengkap[currentImageIndex], TextToSpeech.QUEUE_ADD, params4)
                }
            }
        }
    }

    private fun stopTTS() {
        ttsJob?.cancel()
        tts.stop()
        tvKata1.visibility = View.GONE
        tvKata2.visibility = View.GONE
        tvKata3.visibility = View.GONE

        tvKata1.visibility = View.INVISIBLE
        tvKata2.visibility = View.INVISIBLE
        tvKata3.visibility = View.INVISIBLE

        tvKata1.clearAnimation()
        tvKata2.clearAnimation()
        tvKata3.clearAnimation()
    }

    private fun getDurationOfSpeech(text: String): Long {
        return (text.length * 120).toLong()
    }

    private fun tampilGambarSebelumnya() {
        stopTTS()
        if (currentImageIndex > 0) {
            currentImageIndex--
        } else {
            currentImageIndex = kataLengkap.size - 1
        }
        tampilkanGambarSekarang()
    }

    private fun tampilGambarBerikutnya() {
        stopTTS()
        if (currentImageIndex < kataLengkap.size - 1) {
            currentImageIndex++
        } else {
            currentImageIndex = 0
        }
        tampilkanGambarSekarang()
    }

    override fun onInit(p0: Int) {
        if (p0 == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale("id", "ID"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Bahasa tidak didukung", Toast.LENGTH_SHORT).show()
            }
            tampilkanGambarSekarang()
        } else {
            Toast.makeText(this, "Gagal menginisialisasi Text-to-Speech", Toast.LENGTH_SHORT).show()
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

    override fun onDestroy() {
        super.onDestroy()
        stopTTS()
        tts.shutdown()
        soundButton.release()
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btnBackTigaSuku -> {
                val intent = Intent(this, MenuAbjadActivity::class.java)
                startActivity(intent)
                soundButton.start()
                finish()
            }
            R.id.btnHomeTigaSuku -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                soundButton.start()
                finish()
            }
        }
    }
}