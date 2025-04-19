package com.example.aksarakita.`object`

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.aksarakita.MainActivity
import com.example.aksarakita.R
import kotlinx.android.synthetic.main.activity_menu_object.*

class MenuObjectActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var soundButton: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_object)
        showSystemUI()
        soundButton = MediaPlayer.create(this, R.raw.button)

        btnBackMenuObject.setOnClickListener(this)
        btnHomeMenuObject.setOnClickListener(this)
        btnObjectBuah.setOnClickListener(this)
        btnObjectHewan.setOnClickListener(this)
        btnObjectWarna.setOnClickListener(this)
        btnObjectKeluarga.setOnClickListener(this)
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

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnBackMenuObject ->{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                soundButton.start()
                finish()
            }
            R.id.btnHomeMenuObject -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                soundButton.start()
                finish()
            }
            R.id.btnObjectHewan -> {
                val intent = Intent(this, HewanActivity::class.java)
                startActivity(intent)
                soundButton.start()
                finish()
            }
            R.id.btnObjectBuah -> {
                val intent = Intent(this, BuahActivity::class.java)
                startActivity(intent)
                soundButton.start()
                finish()
            }
            R.id.btnObjectWarna -> {
                val intent = Intent(this, WarnaActivity::class.java)
                startActivity(intent)
                soundButton.start()
                finish()
            }
            R.id.btnObjectKeluarga -> {
                val intent = Intent(this, KeluargaActivity::class.java)
                startActivity(intent)
                soundButton.start()
                finish()
            }
        }
    }
}