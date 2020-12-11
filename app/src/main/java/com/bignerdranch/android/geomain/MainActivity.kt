package com.bignerdranch.android.geomain

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var resultText: TextView
    private lateinit var cheatButton: Button

    private var numberQuestionsAnswered = 0
    private var right: Float = 0.0F

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate(Bundle?) called")

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView = findViewById(R.id.question_text_view)
        resultText = findViewById(R.id.result_text)
        cheatButton = findViewById(R.id.cheat_button)

        trueButton.setOnClickListener {
            checkAnswer(true)
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
        }

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        cheatButton.setOnClickListener {view ->
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val options = ActivityOptions.makeClipRevealAnimation(view, 0, 0, view.width, view.height)
                startActivityForResult(intent, REQUEST_CODE_CHEAT, options.toBundle())
            }else{
                startActivityForResult(intent, REQUEST_CODE_CHEAT)
            }
        }

        updateQuestion()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode != Activity.RESULT_OK){
            return
        }

        if(requestCode == REQUEST_CODE_CHEAT){
            if(data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) == true) quizViewModel.showUserAnswer()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
        outState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    private fun updateQuestion() {
        val questString = quizViewModel.currentQuestionText
        questionTextView.setText(questString)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer: Boolean = quizViewModel.currentQuestionAnswer
        if (!quizViewModel.currentQuestionAnswerPassed) {

            val messageResId = when {
                quizViewModel.currentQuestionAnswerShow -> R.string.judgment_toast
                userAnswer == correctAnswer -> R.string.correct_toast
                else -> R.string.incorrect_toast
            }


            if (messageResId == R.string.correct_toast) right++

            numberQuestionsAnswered++
            quizViewModel.currentQuestionAnswerPassed2()
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, getString(R.string.answerPassed), Toast.LENGTH_SHORT).show()
        }

        if (numberQuestionsAnswered == 6) {
            val result = 100.0F / 6.0F * right
            resultText.text = getString(R.string.percentage_responses, result.toString())
        }
    }
}