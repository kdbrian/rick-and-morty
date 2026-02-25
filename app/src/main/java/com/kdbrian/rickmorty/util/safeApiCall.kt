package com.kdbrian.rickmorty.util

suspend fun <T> safeApiCall(call: suspend () -> T?): Result<T> {
    return dispatchIO {
        try {
            val result = call()
            if (result == null)
                Result.failure(Exception("Something unexpected happened"))
            else
                Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
