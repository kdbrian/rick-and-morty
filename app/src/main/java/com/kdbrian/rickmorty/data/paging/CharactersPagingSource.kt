package com.kdbrian.rickmorty.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kdbrian.rickmorty.domain.model.CharacterEntity
import com.kdbrian.rickmorty.domain.service.CharactersService
import com.kdbrian.rickmorty.util.ResponseWrapper
import com.kdbrian.rickmorty.util.safeApiCall
import timber.log.Timber

class CharactersPagingSource(
    private val charactersService: CharactersService
) : PagingSource<Int, CharacterEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterEntity> {
        return safeApiCall {
            val page = params.key ?: 1
            charactersService.characters(page)
        }.fold(
                onSuccess = { characters ->

                    Timber.d("load: $characters")

                    val page = params.key ?: 1
//                    val nextPage =
//                        characters.responseInfo.next.substringAfter("?page=").toIntOrNull()


                    LoadResult.Page(
                        data = characters.data,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = page.inc()
                    )
                },
                onFailure = {
                    LoadResult.Error(Exception(it.message.toString()))
                }
            )




    }

    override fun getRefreshKey(state: PagingState<Int, CharacterEntity>): Int? {
        return null
    }


}