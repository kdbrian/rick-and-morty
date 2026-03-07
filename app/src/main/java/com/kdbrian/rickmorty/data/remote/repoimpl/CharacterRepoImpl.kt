package com.kdbrian.rickmorty.data.remote.repoimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.kdbrian.rickmorty.data.local.dao.CharactersDao
import com.kdbrian.rickmorty.data.paging.CharactersPagingSource
import com.kdbrian.rickmorty.domain.model.CharacterEntity
import com.kdbrian.rickmorty.domain.repo.CharacterRepo
import com.kdbrian.rickmorty.domain.service.CharactersService
import com.kdbrian.rickmorty.util.safeApiCall

class CharacterRepoImpl(
    private val charactersService: CharactersService,
    private val charactersDao: CharactersDao
) : CharacterRepo {
    override fun characters(page: Int?) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CharactersPagingSource(charactersService) },
            initialKey = page
        ).flow

    override suspend fun characterById(id: Int): Result<CharacterEntity> = safeApiCall {
        charactersService.characterById(id)
    }

    override fun favouriteCharacters() = charactersDao.getCharacters()
}