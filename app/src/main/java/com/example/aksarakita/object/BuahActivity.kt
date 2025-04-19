package com.example.aksarakita.`object`

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.example.aksarakita.MainActivity
import com.example.aksarakita.R
import kotlinx.android.synthetic.main.activity_buah.*
import kotlinx.coroutines.*

import java.util.HashMap
import java.util.Locale

class BuahActivity : AppCompatActivity(), TextToSpeech.OnInitListener, View.OnClickListener {
    private var src   = arrayOf(R.drawable.mangga, R.drawable.apel, R.drawable.manggis, R.drawable.ceri, R.drawable.lemon, R.drawable.semangka, R.drawable.nanas, R.drawable.pisang, R.drawable.tomat)
    private var kata1 = arrayOf("ma","a", "ma","ce","le","se","na","pi","to")
    private var kata2 = arrayOf("ng","pe","ng","ri","mo","ma","na","sa","ma")
    private var kata3 = arrayOf("ga","l", "gi","",  "n", "ng","s", "ng","t")
    private var kata4 = arrayOf("",  "",  "s", "",  "",  "ka","",   "",  "")
    private var kataLengkap = arrayOf("mangga","apel","manggis","ceri","lemon","semangka","nanas","pisang","tomat")

    private var currentImageIndex: Int = 0

    private lateinit var tts: TextToSpeech
    private lateinit var animation: Animation
    private lateinit var imgAnimation: Animation
    private lateinit var soundButton: MediaPlayer

    private var ttsJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buah)
        showSystemUI()
        animation = AnimationUtils.loadAnimation(this, R.anim.fade)
        imgAnimation = AnimationUtils.loadAnimation(this, R.anim.blink)

        tts = TextToSpeech(this, this)

        soundButton = MediaPlayer.create(this, R.raw.button)

        btnBackObjectBuah.setOnClickListener(this)
        btnHomeObjectBuah.setOnClickListener(this)

        btnNextHewan.setOnClickListener {
            soundButton.start()
            tampilGambarBerikutnya()
        }

        btnBackHewan.setOnClickListener {
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
                    tvKata4.visibility = View.INVISIBLE
                    tvKata4.visibility = View.GONE
                    delay(getDurationOfSpeech(kata2[currentImageIndex]) + 300)
                    tts.speak(kataLengkap[currentImageIndex], TextToSpeech.QUEUE_ADD, params4)
                } else {
                    tvKata4.visibility = View.INVISIBLE
                    tvKata4.visibility = View.GONE
                    delay(getDurationOfSpeech(kata2[currentImageIndex]) + 300)
                    tvKata3.visibility = View.VISIBLE
                    tvKata3.text = kata3[currentImageIndex]
                    tvKata3.startAnimation(animation)
                    tvKata3.setTextColor(color)
                    tts.speak(kata3[currentImageIndex], TextToSpeech.QUEUE_ADD, params3)

                    if (kata4[currentImageIndex].isEmpty()) {
                        delay(getDurationOfSpeech(kata3[currentImageIndex]) + 1000)
                        tts.speak(kataLengkap[currentImageIndex], TextToSpeech.QUEUE_ADD, params4)
                    } else {
                        delay(getDurationOfSpeech(kata3[currentImageIndex]) + 500)
                        tvKata4.visibility = View.VISIBLE
                        tvKata4.text = kata4[currentImageIndex]
                        tvKata4.startAnimation(animation)
                        tvKata4.setTextColor(color)
                        tts.speak(kata4[currentImageIndex], TextToSpeech.QUEUE_ADD, params3)

                        delay(getDurationOfSpeech(kata4[currentImageIndex]) + 1000)
                        tts.speak(kataLengkap[currentImageIndex], TextToSpeech.QUEUE_ADD, params4)
                    }
                }
            }
        }
        imgObject.visibility = View.VISIBLE
        imgObject.setImageResource(src[currentImageIndex])
        imgObject.animation = imgAnimation
    }

    private fun stopTTS() {
        ttsJob?.cancel()
        tts.stop()
        tvKata1.visibility = View.GONE
        tvKata2.visibility = View.GONE
        tvKata3.visibility = View.GONE
        tvKata4.visibility = View.GONE

        tvKata1.visibility = View.INVISIBLE
        tvKata2.visibility = View.INVISIBLE
        tvKata3.visibility = View.INVISIBLE
        tvKata4.visibility = View.INVISIBLE

        tvKata1.clearAnimation()
        tvKata2.clearAnimation()
        tvKata3.clearAnimation()
        tvKata4.clearAnimation()
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

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnHomeObjectBuah -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                soundButton.start()
                finish()
            }
            R.id.btnBackObjectBuah -> {
                val intent = Intent(this, MenuObjectActivity::class.java)
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
            // Tandai bahwa TextToSpeech telah diinisialisasi
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
}
