package com.kdbrian.rickmorty.domain.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Location(
    @PrimaryKey
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)