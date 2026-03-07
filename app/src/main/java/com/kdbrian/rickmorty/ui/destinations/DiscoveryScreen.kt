package com.kdbrian.rickmorty.ui.destinations

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.kdbrian.rickmorty.domain.model.CharacterEntity
import com.kdbrian.rickmorty.ui.components.CharacterCardLarge
import com.kdbrian.rickmorty.ui.theme.TitleStyleSpan
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
                delay(2_500)
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

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding(),
                contentAlignment = Alignment.Center
            ) {

                Row(
                    modifier = Modifier
                        .horizontalScroll(scrollState)
                        .align(Alignment.BottomCenter)/*.systemBarsPadding()*/.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(characters.itemCount) {
                        val selected by remember {
                            derivedStateOf {
                                it == pagerState.currentPage
                            }
                        }


                        Box(
                            modifier = Modifier
                                .width(
                                    animateDpAsState(
                                        targetValue = if (selected) 15.dp else 5.dp
                                    ).value
                                )
                                .height(
                                    10.dp
                                )
                                .background(
                                    color = animateColorAsState(
                                        targetValue = if (selected) Color.White else Color.LightGray
                                    ).value,
                                    shape = RoundedCornerShape(
                                        animateIntAsState(
                                            targetValue = if (selected) 25 else 50
                                        ).value
                                    )
                                )
                        )

                    }
                }
            }


        }


    }
}

@Preview(showBackground = true)
@Composable
fun DiscoveryScreenPrev() {
//    DiscoveryScreen()
}
