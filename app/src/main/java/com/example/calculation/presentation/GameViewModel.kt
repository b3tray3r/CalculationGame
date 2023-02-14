package com.example.calculation.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calculation.R
import com.example.calculation.data.GameRepositoryImpl
import com.example.calculation.domain.entity.GameResult
import com.example.calculation.domain.entity.GameSettings
import com.example.calculation.domain.entity.Level
import com.example.calculation.domain.entity.Question
import com.example.calculation.domain.usecases.GenerateQuestionUseCase
import com.example.calculation.domain.usecases.GetGameSettingsUseCase

class GameViewModel(
    private val application: Application,
    private val level: Level
    ) : ViewModel() {

    private lateinit var gameSettings: GameSettings

    private val repository = GameRepositoryImpl

    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private var timer: CountDownTimer? = null

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val _percentOfRightAnswers = MutableLiveData<Int>()
    val percentOfRightAnswers: LiveData<Int>
        get() = _percentOfRightAnswers

    private val _progressAnswers = MutableLiveData<String>()
    val progressAnswers: LiveData<String>
        get() = _progressAnswers

    private val _enoughCount = MutableLiveData<Boolean>()
    val enoughCount: LiveData<Boolean>
        get() = _enoughCount

    private val _enoughPercent = MutableLiveData<Boolean>()
    val enoughPercent: LiveData<Boolean>
        get() = _enoughPercent

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    private var countOfAnswers = 0
    private var countOfQuestions = 0

    init {
        startGame()
    }


    private fun startGame() {
        getGameSettings()
        startTimer()
        generateQuestion()
        updateProgress()
    }

    fun chooseAnswers(number: Int) {
        checkAnswer(number)
        updateProgress()
        generateQuestion()
    }

    private fun updateProgress() {
        val percent = calculatePercent()
        _percentOfRightAnswers.value = percent
        _progressAnswers.value = String.format(
            application.getString(R.string.progress_answers),
            countOfAnswers,
            gameSettings.minCountAnswers
        )
        _enoughCount.value = countOfAnswers >= gameSettings.minCountAnswers
        _enoughPercent.value = percent >= gameSettings.minPercentAnswers

    }

    private fun calculatePercent(): Int {
        if (countOfAnswers == 0) return 0
        return ((countOfAnswers / countOfQuestions.toDouble()) * 100).toInt()
    }

    private fun checkAnswer(number: Int) {
        val rightAnswer = question.value?.rightAnswer
        if (number == rightAnswer) {
            countOfAnswers++
        }
        countOfQuestions++
    }

    private fun getGameSettings() {
        this.gameSettings = getGameSettingsUseCase(level)
        _minPercent.value = gameSettings.minPercentAnswers
    }

    private fun startTimer() {
        timer = object : CountDownTimer(
            gameSettings.gameTime * MILIS_IN_SECONDS,
            MILIS_IN_SECONDS
        ) {
            override fun onTick(milisUntilFinished: Long) {
                _formattedTime.value = formatTime(milisUntilFinished)
            }

            override fun onFinish() {
                finishGame()
            }
        }
        timer?.start()

    }

    private fun generateQuestion() {
        _question.value = generateQuestionUseCase(gameSettings.maxSumValue)
    }

    private fun formatTime(milisUntilFinished: Long): String {
        val seconds = milisUntilFinished / MILIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTES)
        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    private fun finishGame() {

        _gameResult.value = GameResult(
            enoughPercent.value == true && enoughPercent.value == true,
            countOfAnswers,
            countOfQuestions,
            gameSettings
        )

    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    companion object {
        const val MILIS_IN_SECONDS = 1000L
        const val SECONDS_IN_MINUTES = 60
    }
}