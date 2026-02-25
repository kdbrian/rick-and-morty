package com.kdbrian.rickmorty.di

import android.app.Application
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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object CoreModule {


    @Provides
    @Singleton
    fun providesApp(): Application {
        return RickMortyApp() as Application
    }

    @Provides
    @Singleton
    fun providesAppDb(
        app: Application
    ): RickAndMortyDb {
        return RickAndMortyDb.getDb(
            app.applicationContext
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
    fun providesRetrofitService(): Retrofit {
        return Retrofit
            .Builder()
            .apply {
                baseUrl(Constants.baseUrl)
                client(OkHttpClient())
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