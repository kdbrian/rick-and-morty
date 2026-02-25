package com.kdbrian.rickmorty.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kdbrian.rickmorty.domain.model.Character
import com.kdbrian.rickmorty.domain.repo.CharacterRepo
import com.kdbrian.rickmorty.domain.service.CharactersService
import com.kdbrian.rickmorty.util.safeApiCall

class CharactersPagingSource(
    private val charactersService: CharactersService
) : PagingSource<Int, Character>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        return try {
            val page = params.key ?: 1
            var error: String? = null
            val characters = safeApiCall {
                val response = charactersService.characters(page)
                if (response.isSuccessful)
                    response.body()
                else {
                    error = response.errorBody()?.string()
                    null
                }
            }.getOrNull()

            if (characters != null) {
                val nextPage = if (characters.isEmpty()) null else page + 1

                LoadResult.Page(
                    data = characters,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = nextPage
                )
            } else {
                LoadResult.Error(Exception(error.toString()))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? = null


}