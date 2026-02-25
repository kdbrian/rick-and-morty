package com.kdbrian.rickmorty.data.remote.repoimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kdbrian.rickmorty.data.local.dao.EpisodesDao
import com.kdbrian.rickmorty.data.paging.EpisodesPagingSource
import com.kdbrian.rickmorty.domain.model.Episode
import com.kdbrian.rickmorty.domain.repo.EpisodeRepo
import com.kdbrian.rickmorty.domain.service.EpisodeService
import com.kdbrian.rickmorty.util.safeApiCall
import kotlinx.coroutines.flow.Flow

class EpisodeRepoImpl(
    private val episodeService: EpisodeService,
    private val episodesDao: EpisodesDao
) : EpisodeRepo {
    override fun episodes(page: Int?): Flow<PagingData<Episode>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            prefetchDistance = 2,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { EpisodesPagingSource(episodeService) },
        initialKey = page
    ).flow

    override suspend fun episodeById(id: Int): Result<Episode> = safeApiCall {
        val episode = episodeService.episodeById(id)
        if (episode.isSuccessful)
            episode.body()
        else
            null
    }

    override fun favouriteEpisodes() = episodesDao.getEpisodes()
}