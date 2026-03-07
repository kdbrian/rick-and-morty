package com.kdbrian.rickmorty.ui.destinations

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.compose.LazyPagingItems
import com.kdbrian.rickmorty.domain.model.CharacterEntity
import com.kdbrian.rickmorty.ui.components.CharacterCardLarge
import com.kdbrian.rickmorty.ui.theme.TitleStyleSpan

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

        CharacterCardLarge(
            modifier = Modifier.fillMaxSize(),
            imageUrl = character?.image,
            title = buildAnnotatedString {
                withStyle(
                    TitleStyleSpan.copy()
                ) {
                    append(character?.name ?: "")
                }
            },
        )


    }
}

@Preview(showBackground = true)
@Composable
fun DiscoveryScreenPrev() {
//    DiscoveryScreen()
}
