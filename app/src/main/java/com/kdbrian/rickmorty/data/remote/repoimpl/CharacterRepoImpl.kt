package com.kdbrian.rickmorty.data.remote.repoimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kdbrian.rickmorty.data.paging.CharactersPagingSource
import com.kdbrian.rickmorty.domain.model.Character
import com.kdbrian.rickmorty.domain.repo.CharacterRepo
import com.kdbrian.rickmorty.domain.service.Characters
import com.kdbrian.rickmorty.domain.service.CharactersService
import com.kdbrian.rickmorty.util.safeApiCall

class CharacterRepoImpl(
    private val charactersService: CharactersService,
) : CharacterRepo {
    override fun characters(page: Int?) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CharactersPagingSource(charactersService) },
            initialKey = page
        ).flow

    override suspend fun characterById(id: Int): Result<Character> = safeApiCall {
        val character = charactersService.characterById(id)
        if (character.isSuccessful)
            character.body()
        else
            null
    }
}