package com.kdbrian.rickmorty.domain.model


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Origin(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)