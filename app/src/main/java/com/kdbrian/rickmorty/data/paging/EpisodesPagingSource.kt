package com.kdbrian.rickmorty.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kdbrian.rickmorty.domain.model.Character
import com.kdbrian.rickmorty.domain.model.Episode
import com.kdbrian.rickmorty.domain.repo.CharacterRepo
import com.kdbrian.rickmorty.domain.service.CharactersService
import com.kdbrian.rickmorty.domain.service.EpisodeService
import com.kdbrian.rickmorty.util.safeApiCall

class EpisodesPagingSource(
    private val episodeService: EpisodeService
) : PagingSource<Int, Episode>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Episode> {
        return try {
            val page = params.key ?: 1
            var error: String? = null
            val episodes = safeApiCall {
                val response = episodeService.episodes(page)
                if (response.isSuccessful)
                    response.body()
                else {
                    error = response.errorBody()?.string()
                    null
                }
            }.getOrNull()

            if (episodes != null) {
                val nextPage = if (episodes.isEmpty()) null else page + 1

                LoadResult.Page(
                    data = episodes,
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

    override fun getRefreshKey(state: PagingState<Int, Episode>): Int? = null


}