package com.example.aksarakita.bermain

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.aksarakita.MainActivity
import com.example.aksarakita.R
import kotlinx.android.synthetic.main.activity_menu_bermain.btnBackMenuBermain
import kotlinx.android.synthetic.main.activity_menu_bermain.btnHomeMenuBermain
import kotlinx.android.synthetic.main.activity_menu_bermain.btnQuiz
import kotlinx.android.synthetic.main.activity_menu_bermain.btnTebakKata

class MenuBermainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var bgSound: MediaPlayer
    private lateinit var soundButton: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_bermain)
        showSystemUI()
        bgSound = MediaPlayer.create(this, R.raw.backsound)
        soundButton = MediaPlayer.create(this, R.raw.button)
//        bgSound.start()
        btnQuiz.setOnClickListener(this)
        btnTebakKata.setOnClickListener(this)
        btnHomeMenuBermain.setOnClickListener(this)
        btnBackMenuBermain.setOnClickListener(this)
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
            R.id.btnQuiz -> {
                val intent = Intent(this, QuizActivity::class.java)
                startActivity(intent)
                soundButton.start()
            }
            R.id.btnTebakKata -> {
//                val intent = Intent(this, TebakKataActivity::class.java)
//                startActivity(intent)
            }
            R.id.btnHomeMenuBermain -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                soundButton.start()
                finish()
            }
            R.id.btnBackMenuBermain -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                soundButton.start()
                finish()
            }
        }
    }
}