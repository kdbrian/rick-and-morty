package com.kdbrian.rickmorty.util

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseWrapper<T>(
    @SerializedName("info")
    val responseInfo : ResponseInfo,
    @SerializedName("results")
    val data : List<T>
)

@Serializable
data class ResponseInfo(
    val count : Int,
    val pages : Int,
    val next : String,
    val prev : String,
)