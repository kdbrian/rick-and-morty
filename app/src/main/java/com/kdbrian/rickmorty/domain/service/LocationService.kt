package com.kdbrian.rickmorty.domain.service

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LocationService {
    @GET("location")
    suspend fun location(
        @Query("page")
        page: Int? = null
    )

    @GET("location/{id}")
    suspend fun locationById(
        @Path("id")
        id: Int
    )
}