package com.kdbrian.rickmorty.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kdbrian.rickmorty.domain.model.CharacterEntity
import com.kdbrian.rickmorty.domain.service.CharactersService
import com.kdbrian.rickmorty.util.safeApiCall
import timber.log.Timber

class CharactersPagingSource(
    private val charactersService: CharactersService
) : PagingSource<Int, CharacterEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterEntity> {
        return try {
            val page = params.key ?: 1
            var error: String? = null
            val characters = safeApiCall {
                val response = charactersService.characters(page)
                if (response.isSuccessful) {
                    val characterEntities = response.body()
                    Timber.d("load: $characterEntities")
                    characterEntities
                } else {
                    error = response.errorBody()?.string()
                    null
                }
            }.getOrNull()

            if (characters != null) {
                val nextPage =
                    characters.responseInfo.next.substringAfter("?page=").toIntOrNull()


                LoadResult.Page(
                    data = characters.data,
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

    override fun getRefreshKey(state: PagingState<Int, CharacterEntity>): Int? {
        return null
    }


}