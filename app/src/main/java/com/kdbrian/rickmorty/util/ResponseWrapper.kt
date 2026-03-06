package com.kdbrian.rickmorty.util

import kotlinx.serialization.Serializable

@Serializable
data class ResponseWrapper<T>(
    val responseInfo : ResponseInfo,
    val data : List<T>
)

@Serializable
data class ResponseInfo(
    val count : Int,
    val pages : Int,
    val next : String,
    val prev : String,
)