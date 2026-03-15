package com.kdbrian.rickmorty.util

sealed class AppEvent {

    data class ToggleFavorite( val characterId: Int, val isFavorite: Boolean) : AppEvent()


}