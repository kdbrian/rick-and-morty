package com.kdbrian.rickmorty.presentation.ui.destinations

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import com.kdbrian.rickmorty.domain.model.CharacterEntity
import com.kdbrian.rickmorty.presentation.ui.util.MainUiState
import com.kdbrian.rickmorty.util.AppEvent
import com.kdbrian.rickmorty.util.IDLE_CHARACTER_SWAP_DELAY
import kotlinx.coroutines.flow.Flow


@Composable
fun HomeScreen(
    mainUiState: MainUiState = MainUiState(),
    characters: Flow<PagingData<CharacterEntity>>,
    onEvent: (AppEvent) -> Unit = {}
) {

    Scaffold(

    ) { paddingValues ->
        DiscoveryScreen(
            characters,
            autoPlaySpeeed = mainUiState.autoPlayDelay,
            autoPlay = mainUiState.autoPlay,
            onEvent = onEvent
        )
    }

}

@Preview(showBackground = true)
@Composable
fun HomeScreenPrev() {
//    HomeScreen()
}

