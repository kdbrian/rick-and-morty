package com.kdbrian.rickmorty.domain.repo

import com.kdbrian.rickmorty.domain.model.AppCounter
import kotlinx.coroutines.flow.Flow

interface AppCounterRepo {

    fun counter() : Flow<List<AppCounter>>

    suspend fun count(appCounter: AppCounter) : Result<Boolean>
}