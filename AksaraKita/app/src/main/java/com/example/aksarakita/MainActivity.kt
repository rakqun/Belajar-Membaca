package com.example.aksarakita

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.aksarakita.bermain.MenuBermainActivity
import com.example.aksarakita.huruf.AbjadActivity
import com.example.aksarakita.huruf.MenuAbjadActivity
import com.example.aksarakita.membaca.MembacaActivity
import kotlinx.android.synthetic.main.activity_main.btnAbjad
import kotlinx.android.synthetic.main.activity_main.btnBermain
import kotlinx.android.synthetic.main.activity_main.btnMembaca
import kotlinx.android.synthetic.main.activity_main.btnObjek

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var bgSound: MediaPlayer
    private lateinit var soundButton: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showSystemUI()
        bgSound = MediaPlayer.create(this, R.raw.backsound)
        soundButton = MediaPlayer.create(this, R.raw.button)
        bgSound.start()

        btnAbjad.setOnClickListener(this)
        btnMembaca.setOnClickListener(this)
        btnBermain.setOnClickListener(this)
        btnObjek.setOnClickListener(this)
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

    override fun onRestart() {
        super.onRestart()
        bgSound.start()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnAbjad -> {
                val intent = Intent(this, MenuAbjadActivity::class.java)
                startActivity(intent)
                soundButton.start()
            }
            R.id.btnMembaca -> {
                val intent = Intent(this, MembacaActivity::class.java)
                startActivity(intent)
                bgSound.stop()
            }
            R.id.btnBermain -> {
                val intent = Intent(this, MenuBermainActivity::class.java)
                startActivity(intent)
                soundButton.start()
            }
            R.id.btnObjek -> {
//                val intent = Intent(this, MenuBermainActivity::class.java)
//                startActivity(intent)
            }
        }
    }
}