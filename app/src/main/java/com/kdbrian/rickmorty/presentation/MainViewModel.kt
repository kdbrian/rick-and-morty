package com.kdbrian.rickmorty.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdbrian.rickmorty.domain.model.CharacterEntity
import com.kdbrian.rickmorty.domain.model.Episode
import com.kdbrian.rickmorty.domain.model.Location
import com.kdbrian.rickmorty.domain.repo.CharacterRepo
import com.kdbrian.rickmorty.domain.repo.EpisodeRepo
import com.kdbrian.rickmorty.domain.repo.LocationRepo
import com.kdbrian.rickmorty.util.ConnectivityObserver
import com.kdbrian.rickmorty.util.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "MainViewModel"

@HiltViewModel
class MainViewModel @Inject constructor(
    private val connectivityObserver: ConnectivityObserver,
    private val characterRepo: CharacterRepo,
    private val locationRepo: LocationRepo,
    private val episodeRepo: EpisodeRepo
) : ViewModel() {


    private val _selectedCharacterEntity = MutableStateFlow<CharacterEntity?>(null)
    val selectedCharacter = _selectedCharacterEntity.asStateFlow()

    private val _selectedLocation = MutableStateFlow<Location?>(null)
    val selectedLocation = _selectedLocation.asStateFlow()

    private val _selectedEpisode = MutableStateFlow<Episode?>(null)
    val selectedEpisode = _selectedEpisode.asStateFlow()

//    private val connectivityObserver = ConnectivityObserver(context)

    val networkStatus: StateFlow<NetworkStatus> = connectivityObserver.networkStatus
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = NetworkStatus.Unavailable
        )

    init {
        Log.d(TAG, "Viewmodel: Created")
    }


    fun characters(page: Int? = 1) = characterRepo.characters(page).onEach { data ->
        Log.d(
            TAG,
            "characters: $data"
        )
    }.launchIn(viewModelScope)

    fun episodes(page: Int? = 1) = episodeRepo.episodes(page)
    fun locations(page: Int? = 1) = locationRepo.locations(page)


    fun characterById(id: Int) {
        viewModelScope.launch {
            characterRepo.characterById(id).getOrNull()?.let {
                _selectedCharacterEntity.value = it
            }
        }
    }

    fun locationById(id: Int) {
        viewModelScope.launch {
            locationRepo.locationById(id).getOrNull()?.let {
                _selectedLocation.value = it
            }
        }
    }

    fun episodeById(id: Int) {
        viewModelScope.launch {
            episodeRepo.episodeById(id).getOrNull()?.let {
                _selectedEpisode.value = it
            }
        }
    }

}