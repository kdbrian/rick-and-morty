package com.kdbrian.rickmorty.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext

suspend inline fun <reified T> dispatchIO(crossinline block: suspend () -> T): T =
    withContext(Dispatchers.IO) {
        block()
    }

suspend inline fun <reified T> dispatchIOResult(crossinline block: suspend () -> T): Result<T> =
    withContext(Dispatchers.IO) {
        try {
            val result = block()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }