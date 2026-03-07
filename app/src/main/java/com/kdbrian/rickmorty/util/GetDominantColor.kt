package com.kdbrian.rickmorty.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.ImageResult
import coil3.request.SuccessResult
import coil3.request.allowHardware
import coil3.util.CoilUtils.result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL


suspend fun loadBitmapFromUrl(url: String): Bitmap? = dispatchIO {
    try {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()
        connection.inputStream.use {
            BitmapFactory.decodeStream(it)
        }
    } catch (_: Exception) {
        null
    }
}


fun extractDominantColor(bitmap: Bitmap): Color {
    val palette = Palette.from(bitmap)
        .resizeBitmapArea(1000)
        .generate()

    val dominant = palette.getDominantColor(android.graphics.Color.GRAY)
    return Color(dominant)
}

suspend fun getDominantColor(imageUrl: String): Color {
    return loadBitmapFromUrl(imageUrl)?.let { bitmap ->
        extractDominantColor(bitmap)
    } ?: Color.LightGray
}