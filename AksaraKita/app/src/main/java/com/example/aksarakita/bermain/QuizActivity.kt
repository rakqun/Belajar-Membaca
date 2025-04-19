package com.example.aksarakita.bermain

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.aksarakita.MainActivity
import com.example.aksarakita.R
import kotlinx.android.synthetic.main.activity_quiz.btnBackQuiz
import kotlinx.android.synthetic.main.activity_quiz.btnHomeQuiz

class QuizActivity : AppCompatActivity(), View.OnClickListener {
    private val wordList = arrayOf(
        "sapi", "kucing", "anjing", "burung", "ikan", "kursi", "anggur", "meja", "sabun", "rambut"
    )

    private val missingLetters = arrayOf(
        "p", "c", "j", "u", "k", "r", "g", "j", "n", "a"
    )

    private lateinit var currentWord: String
    private lateinit var textViewList: List<TextView>
    private var score: Int = 0
    private var currentQuestionIndex: Int = 0
    private lateinit var soundButton: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        showSystemUI()
        soundButton = MediaPlayer.create(this, R.raw.button)

        textViewList = listOf(
            findViewById(R.id.kata1),
            findViewById(R.id.kata2),
            findViewById(R.id.kata3),
            findViewById(R.id.kata4)
        )

        startGame()

        textViewList.forEach { textView ->
            textView.setOnClickListener {
                val selectedOption = textView.text.toString()
                checkAnswer(selectedOption)
            }
        }

        btnHomeQuiz.setOnClickListener(this)
        btnBackQuiz.setOnClickListener(this)
    }

    private fun startGame() {
        currentWord = wordList[currentQuestionIndex]

        val missingLetter = missingLetters[currentQuestionIndex]

        val questionText = currentWord.replace(missingLetter, "_")
        findViewById<TextView>(R.id.tvTebak).text = questionText
        val options = currentWord.toCharArray().toMutableList()
        val correctAnswerIndex = (textViewList.indices).random()
        textViewList[correctAnswerIndex].text = missingLetter

        val shuffledOptions = options.filter { it != missingLetter.single() }.shuffled()
        var optionIndex = 0
        for (i in textViewList.indices) {
            if (i != correctAnswerIndex) {
                textViewList[i].text = shuffledOptions[optionIndex].toString()
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
            finish()
        }
    }

    private fun checkAnswer(selectedOption: String) {
        val missingLetter = missingLetters[currentQuestionIndex]
        if (selectedOption == missingLetter) {
            score += 20
        }
        currentQuestionIndex++
        if (currentQuestionIndex >= wordList.size) {
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

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnHomeQuiz -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                soundButton.start()
                finish()
            }
            R.id.btnBackQuiz -> {
                val intent = Intent(this, MenuBermainActivity::class.java)
                startActivity(intent)
                soundButton.start()
                finish()
            }
        }
    }
}