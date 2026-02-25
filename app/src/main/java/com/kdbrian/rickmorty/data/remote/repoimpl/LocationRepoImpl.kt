package com.kdbrian.rickmorty.data.remote.repoimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kdbrian.rickmorty.data.paging.LocationsPagingSource
import com.kdbrian.rickmorty.domain.model.Location
import com.kdbrian.rickmorty.domain.repo.LocationRepo
import com.kdbrian.rickmorty.domain.service.LocationService
import com.kdbrian.rickmorty.util.safeApiCall
import kotlinx.coroutines.flow.Flow

class LocationRepoImpl(
    private val locationService: LocationService
) : LocationRepo {
    override fun locations(page: Int?): Flow<PagingData<Location>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            prefetchDistance = 2,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { LocationsPagingSource(locationService) },
        initialKey = page
    ).flow

    override suspend fun locationById(id: Int) = safeApiCall {
        val location = locationService.locationById(id)
        if (location.isSuccessful)
            location.body()
        else
            null
    }


}