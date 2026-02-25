package com.kdbrian.rickmorty.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kdbrian.rickmorty.domain.model.Location
import com.kdbrian.rickmorty.domain.service.LocationService
import com.kdbrian.rickmorty.util.safeApiCall

class LocationsPagingSource(
    private val locationService: LocationService
) : PagingSource<Int, Location>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Location> {
        return try {
            val page = params.key ?: 1
            var error: String? = null
            val episodes = safeApiCall {
                val response = locationService.locations(page)
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

    override fun getRefreshKey(state: PagingState<Int, Location>): Int? = null


}