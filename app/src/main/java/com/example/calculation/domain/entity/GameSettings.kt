package com.example.calculation.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameSettings(
    val maxSumValue: Int,
    val minCountAnswers:Int,
    val minPercentAnswers: Int,
    val gameTime: Int
): Parcelable