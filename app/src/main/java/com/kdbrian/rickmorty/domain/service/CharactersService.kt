package com.kdbrian.rickmorty.domain.service

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query







interface CharactersService {

    @GET("character")
    suspend fun character(
        @Query("page")
        page: Int? = null
    )


    @GET("character/{id}")
    suspend fun characterById(
        @Path("id")
        id: Int
    )


}