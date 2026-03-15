package com.kdbrian.rickmorty.presentation.ui.destinations

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import com.kdbrian.rickmorty.domain.model.CharacterEntity
import kotlinx.coroutines.flow.Flow


@Composable
fun HomeScreen(
    characters : Flow<PagingData<CharacterEntity>>
) {

    Scaffold(

    ) {  paddingValues ->
        DiscoveryScreen(characters)
    }

}

@Preview(showBackground = true)
@Composable
fun HomeScreenPrev() {
//    HomeScreen()
}

