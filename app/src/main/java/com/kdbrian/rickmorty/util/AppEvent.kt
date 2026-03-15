package com.kdbrian.rickmorty.util

import com.kdbrian.rickmorty.domain.model.CharacterEntity

sealed class AppEvent {

    data class ToggleFavorite(val characterId: Int, val isFavorite: Boolean) : AppEvent()
    data class ToggleAutoPlay(
        val isAutoPlay: Boolean,
        val delay: Long = IDLE_CHARACTER_SWAP_DELAY
    ) : AppEvent()

    data class CurrentCharacter(val characterEntity: CharacterEntity) : AppEvent()


}