package com.kdbrian.rickmorty.presentation.ui.util

import com.kdbrian.rickmorty.util.IDLE_CHARACTER_SWAP_DELAY

data class MainUiState(
    val autoPlay : Boolean = false,
    val autoPlayDelay : Long = IDLE_CHARACTER_SWAP_DELAY
)