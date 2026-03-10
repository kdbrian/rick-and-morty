package com.kdbrian.rickmorty.presentation.ui.destinations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.kdbrian.rickmorty.domain.model.CharacterEntity
import com.kdbrian.rickmorty.presentation.ui.components.CharacterCardLarge
import com.kdbrian.rickmorty.util.IDLE_CHARACTER_SWAP_DELAY
import com.kdbrian.rickmorty.util.getDominantColor
import kotlinx.coroutines.delay

@Composable
fun DiscoveryScreen(
    characters: LazyPagingItems<CharacterEntity>,
) {

    val pagerState = rememberPagerState { characters.itemCount }


    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
    ) {
        val character = characters[it]
        var color by remember { mutableStateOf(Color.Transparent) }

        LaunchedEffect(Unit) {
            while (true) {
                delay(IDLE_CHARACTER_SWAP_DELAY)
                pagerState.scrollToPage(pagerState.currentPage.inc())
            }
        }

        LaunchedEffect(character) {
            character?.let {
                color = getDominantColor(character.image)
            }
        }

        val scrollState = rememberScrollState(it)

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            character?.let {
                CharacterCardLarge(
                    modifier = Modifier.fillMaxSize(),
                    characterEntity = it
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .safeDrawingPadding(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {

            }

        }


    }
}

@Preview(showBackground = true)
@Composable
fun DiscoveryScreenPrev() {
//    DiscoveryScreen()
}
