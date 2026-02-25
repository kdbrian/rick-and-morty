package com.kdbrian.rickmorty.domain.repo

import androidx.paging.PagingData
import com.kdbrian.rickmorty.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepo {

    fun characters(page: Int? = null): Flow<PagingData<Character>>

    suspend fun characterById(
        id: Int
    ): Result<Character>

    fun favouriteCharacters(): Flow<List<Character>>


}