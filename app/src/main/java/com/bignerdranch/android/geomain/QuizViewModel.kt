package com.bignerdranch.android.geomain

import androidx.lifecycle.ViewModel

//private const val TAG = "QuizViewModel"

class QuizViewModel: ViewModel() {

    var currentIndex = 0
//    var isCheater = false

    private val questionBank = listOf(
        Question(R.string.question_australia, answer = true, answerPassed = false, showAnswer = false),
        Question(R.string.question_oceans, answer = true, answerPassed = false, showAnswer = false),
        Question(R.string.question_mideast, answer = false, answerPassed = false, showAnswer = false),
        Question(R.string.question_africa, answer = false, answerPassed = false, showAnswer = false),
        Question(R.string.question_americas, answer = true, answerPassed = false, showAnswer = false),
        Question(R.string.question_asia, answer = true, answerPassed = false, showAnswer = false)
    )

    val currentQuestionAnswer: Boolean  get() = questionBank[currentIndex].answer
    val currentQuestionText: Int get() = questionBank[currentIndex].textResId
    val currentQuestionAnswerPassed : Boolean get() = questionBank[currentIndex].answerPassed
    val currentQuestionAnswerShow : Boolean get() = questionBank[currentIndex].showAnswer
    fun currentQuestionAnswerPassed2() {
        questionBank[currentIndex].answerPassed = true
    }
    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun showUserAnswer(){
        questionBank[currentIndex].showAnswer = true
    }
}