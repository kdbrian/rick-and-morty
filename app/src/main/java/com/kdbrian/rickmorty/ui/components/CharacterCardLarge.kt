package com.kdbrian.rickmorty.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import com.kdbrian.rickmorty.R
import com.kdbrian.rickmorty.ui.theme.RickMortyTheme
import com.kdbrian.rickmorty.util.shimmer

@Composable
fun CharacterCardLarge(
    modifier: Modifier = Modifier,
    brush: Brush? = null,
    imageUrl: String? = null,
    title: AnnotatedString = buildAnnotatedString { },
    imagePlaceHolder: Int? = null,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
                .then(
                    if (brush != null) {
                        Modifier.background(brush)
                    } else {
                        Modifier
                    }
                ),
        )

        Crossfade(imageUrl != null && imageUrl.isNotEmpty()) { state ->
            when (state) {
                true -> AsyncImage(
                    model = imageUrl,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    contentDescription = title.toString(),
                    placeholder = imagePlaceHolder?.let {
                        painterResource(it)
                    },
                    error = imagePlaceHolder?.let {
                        painterResource(it)
                    }
                )

                else -> {
                    Spacer(
                        Modifier
                            .fillMaxSize()
                            .shimmer(
                                durationMillis = 800
                            )
                    )
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun CharacterCardLargePrev() {
    RickMortyTheme {

        CharacterCardLarge(
            imagePlaceHolder = R.drawable.placeholder
        )
    }
}
