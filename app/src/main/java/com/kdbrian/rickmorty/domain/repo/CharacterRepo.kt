package com.kdbrian.rickmorty.domain.repo

import androidx.paging.PagingData
import com.kdbrian.rickmorty.domain.model.CharacterEntity
import kotlinx.coroutines.flow.Flow

interface CharacterRepo {

    fun characters(page: Int? = null): Flow<PagingData<CharacterEntity>>

    suspend fun characterById(
        id: Int
    ): Result<CharacterEntity>

    fun favouriteCharacters(): Flow<List<CharacterEntity>>


}