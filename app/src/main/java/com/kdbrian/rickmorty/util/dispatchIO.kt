package com.kdbrian.rickmorty.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext

suspend inline fun <reified T> dispatchIO(crossinline block: suspend () -> T): T = withContext(Dispatchers.IO) {
        block()
}