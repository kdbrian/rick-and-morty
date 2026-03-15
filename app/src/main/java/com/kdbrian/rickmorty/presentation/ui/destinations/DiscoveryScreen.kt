package com.kdbrian.rickmorty.presentation.ui.destinations

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.kdbrian.rickmorty.util.shimmer
import com.kdbrian.rickmorty.domain.model.CharacterEntity
import com.kdbrian.rickmorty.presentation.ui.components.CharacterCardLarge
import com.kdbrian.rickmorty.presentation.ui.components.CharacterFab
import com.kdbrian.rickmorty.presentation.ui.theme.RickMortyTheme
import com.kdbrian.rickmorty.presentation.ui.util.ThemePreviews
import com.kdbrian.rickmorty.util.IDLE_CHARACTER_SWAP_DELAY
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow


class DiscoveryScreenPreviewParameterProvider : PreviewParameterProvider<List<CharacterEntity>> {
    override val values: Sequence<List<CharacterEntity>>
        get() = sequenceOf(
            listOf(CharacterEntity.sampleCharacter)
        )
}


@Composable
fun DiscoveryScreen(
    characters: Flow<PagingData<CharacterEntity>>,
) {

    val pagingItems = characters.collectAsLazyPagingItems()
    val pagerState = rememberPagerState { pagingItems.itemCount }

    var isExpanded by remember { mutableStateOf(false) }
    val isFabExpanded = updateTransition(isExpanded)
    
    val scale by isFabExpanded.animateFloat {
        when (it) {
            true -> 1f
            else -> 0.7f
        }
    }

    val alpha by isFabExpanded.animateFloat {
        when (it) {
            true -> 1f
            else -> 0f
        }
    }

    var currentCharacter by remember { mutableStateOf<CharacterEntity?>(null) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(IDLE_CHARACTER_SWAP_DELAY)
            pagerState.scrollToPage(pagerState.currentPage.inc())
        }
    }

    AnimatedContent(
        targetState = pagingItems.loadState.refresh,
    ) {
        when (it) {
            is LoadState.Error -> {
                    Text(
                        "Error : ${it.error.message}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxSize(),
                        textAlign = TextAlign.Center
                    )
            }

            LoadState.Loading -> {
                DiscoveryScreenLoading()
            }

            else -> {
                Scaffold(
                    floatingActionButton = {


                        AnimatedContent(
                            targetState = isExpanded,
                            transitionSpec = {
                                if (targetState) {
                                    (slideInVertically() + scaleIn()) togetherWith fadeOut()
                                } else {
                                    fadeIn() togetherWith (slideOutVertically() + scaleOut())
                                }
                            }
                        ) {
                            when (it) {
                                true -> {
                                    CharacterFab(
                                        characterId = currentCharacter?.id ?: 1,
                                    )
                                }

                                else -> {
                                    IconButton(
                                        modifier = Modifier.clip(
//                                color = color.value,
                                            shape = CircleShape
                                        ),
                                        onClick = {
                                            isExpanded = true
                                        }
                                    ) {
                                        Icon(
                                            Icons.Rounded.FavoriteBorder,
                                            contentDescription = "Toggle Favorite",
                                            tint = Color.White,
                                            modifier = Modifier.padding(4.dp)
                                        )
                                    }

                                }
                            }
                        }


                    }
                ) { paddingValues ->
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                    ) {
                        pagingItems[it]?.let { character ->


                            currentCharacter = character
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {

                                character
                                CharacterCardLarge(
                                    modifier = Modifier.fillMaxSize(),
                                    characterEntity = character
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
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiscoveryScreenPrev(
    @PreviewParameter(DiscoveryScreenPreviewParameterProvider::class) characters: List<CharacterEntity>
) {
    RickMortyTheme {
        DiscoveryScreen(
            characters = MutableStateFlow(PagingData.from(characters))
        )
    }
}

@ThemePreviews
@Composable
fun DiscoveryScreenLoading() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Box(
            modifier = Modifier
                .align(Alignment.End)
                .size(180.dp)
                .shimmer()
        )

        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(18.dp)
//                .background(
//                    color = Color.LightGray,
//                    shape = RoundedCornerShape(50)
//                )
                .shimmer()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(.65f)
                .height(18.dp)

                .shimmer()
                .clip(
                    RoundedCornerShape(50)
                )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(.35f)
                .height(18.dp)
//                .background(
//                    color = Color.LightGray,
//                    shape = RoundedCornerShape(50)
//                )
                .shimmer()
        )


    }
}