package com.bignerdranch.android.geomain

import androidx.annotation.StringRes

data class Question(@StringRes val textResId: Int, val answer: Boolean, var answerPassed: Boolean, var showAnswer: Boolean)