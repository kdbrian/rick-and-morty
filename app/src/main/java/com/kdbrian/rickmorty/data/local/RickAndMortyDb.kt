package com.kdbrian.rickmorty.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kdbrian.rickmorty.data.local.dao.CharactersDao
import com.kdbrian.rickmorty.data.local.dao.EpisodesDao
import com.kdbrian.rickmorty.data.local.dao.LocationsDao
import com.kdbrian.rickmorty.domain.model.Episode
import com.kdbrian.rickmorty.domain.model.Location

@Database(
    entities = [
        Character::class,
        Episode::class,
        Location::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class RickAndMortyDb : RoomDatabase() {

    abstract fun charactersDao(): CharactersDao
    abstract fun episodesDao(): EpisodesDao
    abstract fun locationsDao(): LocationsDao


    companion object {

        private val DATABASE_NAME: String = "rick_and_morty_db"
        private val INSTANCE: RickAndMortyDb? = null


        fun getDb(context: Context) = getInstance(context)


        private fun getInstance(context: Context): RickAndMortyDb {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    RickAndMortyDb::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
            }
        }

    }
}