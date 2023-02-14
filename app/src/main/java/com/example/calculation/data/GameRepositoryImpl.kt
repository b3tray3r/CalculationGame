package com.example.calculation.data

import com.example.calculation.domain.entity.GameSettings
import com.example.calculation.domain.entity.Level
import com.example.calculation.domain.entity.Question
import com.example.calculation.domain.repository.GameRepository
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

object GameRepositoryImpl : GameRepository {

    private const val MIN_SUM_VALUE = 2
    private const val MIN_ANSWER_VALUE = 1

    override fun generateQuestion(maxSum: Int, countOptions: Int): Question {
        val sum = Random.nextInt(MIN_SUM_VALUE, maxSum + 1)
        val visibleNum = Random.nextInt(MIN_ANSWER_VALUE, sum)
        val options = HashSet<Int>()
        val rightAnswer = sum - visibleNum
        options.add(rightAnswer)
        val from = max(rightAnswer - countOptions, MIN_ANSWER_VALUE)
        val to = min(maxSum, rightAnswer + countOptions)
        while (options.size < countOptions) {
            options.add(Random.nextInt(from, to))
        }
        return Question(sum, visibleNum, options.toList())
    }

    override fun getGameSettings(level: Level): GameSettings {
        return when (level) {

            Level.EASY -> {
                GameSettings(20, 10, 60, 60)
            }

            Level.NORMAL -> {
                GameSettings(35, 20, 80, 60)
            }

            Level.HARD -> {
                GameSettings(50, 25, 90, 60)
            }
        }
    }


}