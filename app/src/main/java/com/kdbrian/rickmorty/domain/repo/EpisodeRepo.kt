package com.kdbrian.rickmorty.domain.repo

import androidx.paging.PagingData
import com.kdbrian.rickmorty.domain.model.Episode
import kotlinx.coroutines.flow.Flow

interface EpisodeRepo {
    fun episodes(
        page: Int? = null
    ) : Flow<PagingData<Episode>>

    suspend fun episodeById(
        id: Int
    ) : Result<Episode>


    fun favouriteEpisodes(): Flow<List<Episode>>



}