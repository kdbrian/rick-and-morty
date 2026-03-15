package com.kdbrian.rickmorty.presentation

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdbrian.rickmorty.R
import com.kdbrian.rickmorty.domain.model.CharacterEntity
import com.kdbrian.rickmorty.domain.model.Episode
import com.kdbrian.rickmorty.domain.model.Location
import com.kdbrian.rickmorty.domain.repo.CharacterRepo
import com.kdbrian.rickmorty.domain.repo.EpisodeRepo
import com.kdbrian.rickmorty.domain.repo.LocationRepo
import com.kdbrian.rickmorty.presentation.ui.util.MainUiState
import com.kdbrian.rickmorty.util.AppEvent
import com.kdbrian.rickmorty.util.ConnectivityObserver
import com.kdbrian.rickmorty.util.NetworkStatus
import com.kdbrian.rickmorty.util.isConnected
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject


private const val TAG = "MainViewModel"

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    connectivityObserver: ConnectivityObserver,
    private val characterRepo: CharacterRepo,
    private val locationRepo: LocationRepo,
    private val context: Context,
    private val episodeRepo: EpisodeRepo
) : ViewModel() {


    private val _selectedCharacterEntity = MutableStateFlow<CharacterEntity?>(null)
    val selectedCharacter = _selectedCharacterEntity.asStateFlow()

    private val _selectedLocation = MutableStateFlow<Location?>(null)
    val selectedLocation = _selectedLocation.asStateFlow()

    private val _selectedEpisode = MutableStateFlow<Episode?>(null)
    val selectedEpisode = _selectedEpisode.asStateFlow()


    val networkStatus: StateFlow<NetworkStatus> = connectivityObserver.networkStatus
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2_000),
            initialValue = NetworkStatus.Unavailable
        )

    private val preferences = context
        .getSharedPreferences(context.getString(R.string.app_settings), Context.MODE_PRIVATE)

    private val _autoPlay = MutableStateFlow(
        preferences
            .getBoolean(context.getString(R.string.auto_play), false)
    )
    val autoPlay = _autoPlay.asStateFlow()

    private val _currentCharacter = MutableStateFlow<CharacterEntity?>(null)
    private val currentCharacter = _currentCharacter.asStateFlow()

    private val _mainUiState = MutableStateFlow(MainUiState())
    val mainUiState =_mainUiState.asStateFlow()

    fun updateAutoPlay(autoPlay: Boolean) {
        viewModelScope.launch {
            preferences.edit {
                putBoolean(context.getString(R.string.auto_play), autoPlay)
            }

            _autoPlay.update { autoPlay }
        }
    }

    init {

//        Json.encodeToString(appSettings)

        Timber.tag(TAG).d("Viewmodel: Created")
    }


    fun characters(page: Int? = 1) =
        networkStatus
            .onEach { status ->
                Timber.tag(TAG).d("Network Status: $status")
            }
            .map { it.isConnected() }
            .distinctUntilChanged()
            .filter { it }
            .flatMapLatest {
                characterRepo.characters(page)
            }

    fun episodes(page: Int? = 1) = networkStatus
        .onEach { status ->
            Timber.tag(TAG).d("Network Status: $status")
        }
        .map { it.isConnected() }
        .distinctUntilChanged()
        .filter { it }
        .flatMapLatest { episodeRepo.episodes(page) }

    fun locations(page: Int? = 1) = networkStatus
        .onEach { status ->
            Timber.tag(TAG).d("Network Status: $status")
        }
        .map { it.isConnected() }
        .distinctUntilChanged()
        .filter { it }
        .flatMapLatest {
            locationRepo.locations(page)
        }

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


    fun onEvent(event: AppEvent) {
        viewModelScope.launch {
            when (event) {
                is AppEvent.ToggleAutoPlay -> {
                    updateAutoPlay(event.isAutoPlay)
                }

                is AppEvent.CurrentCharacter -> {
                    _currentCharacter.update { event.characterEntity }
                }
                is AppEvent.ToggleFavorite -> {

                }
            }
        }
    }

}