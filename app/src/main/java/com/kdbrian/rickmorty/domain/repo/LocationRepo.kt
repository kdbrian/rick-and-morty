package com.kdbrian.rickmorty.domain.repo

import androidx.paging.PagingData
import com.kdbrian.rickmorty.domain.model.Episode
import com.kdbrian.rickmorty.domain.model.Location
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LocationRepo {
    fun locations(
        page: Int? = null
    ): Flow<PagingData<Location>>

    suspend fun locationById(
        id: Int
    ): Result<Location>


    fun favouriteLocations(): Flow<List<Location>>


}