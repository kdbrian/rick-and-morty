package com.kdbrian.rickmorty.domain.service

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EpisodeService {
    @GET("location")
    suspend fun episode(
        @Query("page")
        page: Int? = null
    )

    @GET("location/{id}")
    suspend fun episodeById(
        @Path("id")
        id: Int
    )
}