package com.kdbrian.rickmorty.domain.dto

data class CharacterAnalyticsDto(
    val viewers: Long,
    val favoured: Long,
    val isFavourite: Boolean
)
