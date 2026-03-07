package com.kdbrian.rickmorty.util

import retrofit2.Response
import timber.log.Timber

suspend fun <T> safeApiCall(call: suspend () -> Response<T>): Result<T> {
    return dispatchIO {
        try {
            val result = call()
            val body = result.body()

            Timber.d("safeApiCall: $body")

            if (body != null && result.isSuccessful)
                Result.success(body)
            else
                Result.failure(result.errorBody()?.string()?.let { Exception(it) } ?: Exception())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
