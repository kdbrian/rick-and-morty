package com.kdbrian.rickmorty.domain.service

import com.kdbrian.rickmorty.domain.model.Character
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


typealias Characters = List<Character>

interface CharactersService {

    @GET("character")
    suspend fun characters(
        @Query("page")
        page: Int? = null
    ): Response<Characters>


    @GET("character/{id}")
    suspend fun characterById(
        @Path("id")
        id: Int
    ): Response<Character>


}