package com.kdbrian.rickmorty.domain.service

import com.kdbrian.rickmorty.domain.model.Episode
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


private typealias Episodes = List<Episode>


interface EpisodeService {
    @GET("location")
    suspend fun episodes(
        @Query("page")
        page: Int? = null
    ) : Response<Episodes>


    @GET("location/{id}")
    suspend fun episodeById(
        @Path("id")
        id: Int
    ) : Response<Episode>
}