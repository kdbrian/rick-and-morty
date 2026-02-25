package com.kdbrian.rickmorty.data.remote.repoimpl

import com.kdbrian.rickmorty.data.local.dao.CharactersDao
import com.kdbrian.rickmorty.data.local.dao.EpisodesDao
import com.kdbrian.rickmorty.data.local.dao.LocationsDao
import com.kdbrian.rickmorty.domain.model.Character
import com.kdbrian.rickmorty.domain.model.Episode
import com.kdbrian.rickmorty.domain.model.Location
import com.kdbrian.rickmorty.domain.repo.FavouritesRepo
import com.kdbrian.rickmorty.util.dispatchIO

class FavouritesRepoImpl(
    private val characterDao: CharactersDao,
    private val locationDao: LocationsDao,
    private val episodeDao: EpisodesDao
) : FavouritesRepo {

    override val favouriteCharacters
        get() = characterDao.getCharacters()

    override val favouriteLocations
        get() = locationDao.getLocations()

    override val favouriteEpisodes
        get() = episodeDao.getEpisodes()


    override suspend fun toggleCharacterInFavourites(character: Character) = dispatchIO<Unit> {
        if (characterDao.getCharacterById(character.id) != null) characterDao.delete(character)
        else characterDao.insert(character)
    }

    override suspend fun toggleLocationInFavourites(location: Location) = dispatchIO<Unit> {
        if (locationDao.getLocationById(location.name) != null) locationDao.delete(location)
        else locationDao.insert(location)
    }

    override suspend fun toggleEpisodeInFavourites(episode: Episode) = dispatchIO<Unit> {
        if (episodeDao.getEpisodeById(episode.id) != null) episodeDao.delete(episode)
        else episodeDao.insert(episode)
    }

}