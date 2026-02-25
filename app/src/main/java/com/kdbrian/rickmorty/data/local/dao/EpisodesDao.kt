package com.kdbrian.rickmorty.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.kdbrian.rickmorty.domain.model.Episode
import kotlinx.coroutines.flow.Flow

@Dao
interface EpisodesDao : BaseDao<Episode> {


    @Query("SELECT * FROM episode")
    fun getEpisodes(): Flow<List<Episode>>

    @Query("SELECT * FROM episode WHERE id = :id LIMIT 1")
    fun getEpisodeById(id: Int): Episode?


}