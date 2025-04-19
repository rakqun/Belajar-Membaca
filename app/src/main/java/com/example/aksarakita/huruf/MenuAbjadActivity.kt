package com.example.aksarakita.huruf

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.aksarakita.MainActivity
import com.example.aksarakita.R
import kotlinx.android.synthetic.main.activity_menu_abjad.btnBackMenuAbjad
import kotlinx.android.synthetic.main.activity_menu_abjad.btnDuaSukuKata
import kotlinx.android.synthetic.main.activity_menu_abjad.btnHomeMenuAbjad
import kotlinx.android.synthetic.main.activity_menu_abjad.btnMenuAbjad
import kotlinx.android.synthetic.main.activity_menu_abjad.btnTigaSukuKata

class MenuAbjadActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var bgSound: MediaPlayer
    private lateinit var soundButton: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_abjad)
        showSystemUI()
        bgSound = MediaPlayer.create(this, R.raw.backsound)
        soundButton = MediaPlayer.create(this, R.raw.button)
//        bgSound.start()
        btnMenuAbjad.setOnClickListener(this)
        btnDuaSukuKata.setOnClickListener(this)
        btnTigaSukuKata.setOnClickListener(this)
        btnHomeMenuAbjad.setOnClickListener(this)
        btnBackMenuAbjad.setOnClickListener(this)

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
            R.id.btnMenuAbjad -> {
                val intent = Intent(this, AbjadActivity::class.java)
                startActivity(intent)
                soundButton.start()
                finish()
            }
            R.id.btnDuaSukuKata -> {
                val intent = Intent(this, DuaSukuActivity::class.java)
                startActivity(intent)
                soundButton.start()
                finish()
            }
            R.id.btnTigaSukuKata -> {
                val intent = Intent(this, TigaKataActivity::class.java)
                startActivity(intent)
                soundButton.start()
                finish()
            }
            R.id.btnHomeMenuAbjad -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                soundButton.start()
                finish()
            }
            R.id.btnBackMenuAbjad -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                soundButton.start()
                finish()
            }
        }
    }

}