package com.kdbrian.rickmorty.domain.dto

import com.kdbrian.rickmorty.domain.model.AppCounter

data class AppCounterDto(
    val `val`: Long,
    val speed: Float
)


fun AppCounterDto.toCounter() = AppCounter(
    delay = `val`,
    speed = speed
)

fun AppCounter.toCounterDto() = AppCounterDto(
    `val` = delay,
    speed = speed
)
