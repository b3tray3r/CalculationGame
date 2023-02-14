package com.example.calculation.domain.usecases

import com.example.calculation.domain.entity.GameSettings
import com.example.calculation.domain.entity.Level
import com.example.calculation.domain.repository.GameRepository

class GetGameSettingsUseCase(
    private val repository: GameRepository
) {
    operator fun invoke(level: Level):GameSettings {
        return repository.getGameSettings(level)
    }
}