package com.kdbrian.rickmorty.util

sealed class AppEvent {

    data class ToggleFavorite( val characterId: Int, val isFavorite: Boolean) : AppEvent()
    data class ToggleAutoPlay( val isAutoPlay: Boolean, val delay : Long = IDLE_CHARACTER_SWAP_DELAY) : AppEvent()


}