package com.example.calculation.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameResult (
    val win: Boolean,
    val countAnswers: Int,
    val countQuestions:Int,
    val gameSettings: GameSettings
        ) : Parcelable