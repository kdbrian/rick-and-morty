package com.kdbrian.rickmorty.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kdbrian.rickmorty.util.IDLE_CHARACTER_SWAP_DELAY
import java.util.UUID
import kotlin.uuid.Uuid

@Entity
data class AppCounter(
    @PrimaryKey
    val id : String = UUID.randomUUID().toString().split("-").first(),
    val speed : Float = 1f,
    val delay : Long = IDLE_CHARACTER_SWAP_DELAY,
    val timestamp : Long = System.currentTimeMillis()
)
