package com.kdbrian.rickmorty.di

import android.app.Application
import android.content.Context
import android.util.Log
import com.kdbrian.rickmorty.RickMortyApp
import com.kdbrian.rickmorty.data.local.RickAndMortyDb
import com.kdbrian.rickmorty.data.local.dao.CharactersDao
import com.kdbrian.rickmorty.data.local.dao.EpisodesDao
import com.kdbrian.rickmorty.data.local.dao.LocationsDao
import com.kdbrian.rickmorty.data.remote.Constants
import com.kdbrian.rickmorty.data.remote.repoimpl.CharacterRepoImpl
import com.kdbrian.rickmorty.data.remote.repoimpl.EpisodeRepoImpl
import com.kdbrian.rickmorty.data.remote.repoimpl.LocationRepoImpl
import com.kdbrian.rickmorty.domain.repo.CharacterRepo
import com.kdbrian.rickmorty.domain.repo.EpisodeRepo
import com.kdbrian.rickmorty.domain.repo.LocationRepo
import com.kdbrian.rickmorty.domain.service.CharactersService
import com.kdbrian.rickmorty.domain.service.EpisodeService
import com.kdbrian.rickmorty.domain.service.LocationService
import com.kdbrian.rickmorty.util.ConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val TAG = "CoreModule"

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {


    @Provides
    @Singleton
    fun providesAppDb(
        @ApplicationContext
        context: Context
    ): RickAndMortyDb {
        return RickAndMortyDb.getDb(
            context
        )
    }

    @Provides
    @Singleton
    fun providesConnectivityObserver(
        @ApplicationContext
        context: Context
    ): ConnectivityObserver {
        return ConnectivityObserver(
            context
        )
    }


    @Provides
    @Singleton
    fun providesCharacterDao(
        db: RickAndMortyDb
    ): CharactersDao {
        return db.charactersDao()
    }

    @Provides
    @Singleton
    fun providesLocationsDao(
        db: RickAndMortyDb
    ): LocationsDao {
        return db.locationsDao()
    }

    @Provides
    @Singleton
    fun providesEpisodeDao(
        db: RickAndMortyDb
    ): EpisodesDao {
        return db.episodesDao()
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .apply {
                addInterceptor { chain ->
                    val request = chain.request()
//                    Log.d(TAG, "providesOkHttpClient: Request: ${request.url}")

                    chain.proceed(request)
                }
                retryOnConnectionFailure(true)
            }
            .build()
    }


    @Provides
    @Singleton
    fun providesRetrofitService(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit
            .Builder()
            .apply {
                baseUrl(Constants.baseUrl)
                client(okHttpClient)
                addConverterFactory(GsonConverterFactory.create())
            }
            .build()
    }

    @Provides
    @Singleton
    fun providesCharacterService(
        retrofit: Retrofit
    ): CharactersService {
        return retrofit.create(CharactersService::class.java)
    }

    @Provides
    @Singleton
    fun providesLocationService(
        retrofit: Retrofit
    ): LocationService {
        return retrofit.create(LocationService::class.java)
    }

    @Provides
    @Singleton
    fun providesEpisodeService(
        retrofit: Retrofit
    ): EpisodeService {
        return retrofit.create(EpisodeService::class.java)
    }


    @Provides
    @Singleton
    fun providesCharacterRepo(
        characterService: CharactersService,
        charactersDao: CharactersDao
    ): CharacterRepo {
        return CharacterRepoImpl(
            charactersService = characterService,
            charactersDao = charactersDao
        )
    }


    @Provides
    @Singleton
    fun providesEpisodeRepo(
        episodeService: EpisodeService,
        episodesDao: EpisodesDao
    ): EpisodeRepo {
        return EpisodeRepoImpl(
            episodeService = episodeService,
            episodesDao = episodesDao
        )
    }


    @Provides
    @Singleton
    fun providesLocationRepo(
        locationService: LocationService,
        locationsDao: LocationsDao
    ): LocationRepo {
        return LocationRepoImpl(
            locationService = locationService,
            locationsDao = locationsDao
        )
    }


}