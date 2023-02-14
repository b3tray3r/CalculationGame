package com.example.calculation.domain.repository

import com.example.calculation.domain.entity.GameSettings
import com.example.calculation.domain.entity.Level
import com.example.calculation.domain.entity.Question

interface GameRepository {
    fun generateQuestion(
        maxSum: Int,
        countOptions: Int
    ): Question
    fun getGameSettings(level: Level): GameSettings
}