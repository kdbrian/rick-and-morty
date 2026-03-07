package com.kdbrian.rickmorty.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kdbrian.rickmorty.domain.model.CharacterEntity
import com.kdbrian.rickmorty.ui.theme.Kavoon
import com.kdbrian.rickmorty.ui.theme.RickMortyTheme
import com.kdbrian.rickmorty.util.getDominantColor
import com.kdbrian.rickmorty.util.shimmer

@Composable
fun CharacterCardLarge(
    modifier: Modifier = Modifier,
    brush: Brush? = null,
    characterEntity : CharacterEntity,
    imagePlaceHolder: Int? = null,
) {

    val color = produceState(Color.LightGray) {
        value = getDominantColor(characterEntity.image)
    }

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

        Crossfade(characterEntity.image.isNotEmpty()) { state ->
            when (state) {
                true -> AsyncImage(
                    model = characterEntity.image,
                    modifier = Modifier.fillMaxSize()
                        .blur(
                            16.dp
                        ),
                    contentScale = ContentScale.Crop,
                    contentDescription = characterEntity.name,
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

        Column(
            modifier=Modifier.safeDrawingPadding().padding(12.dp)
        ) {
            Text(
                text = characterEntity.name,
                style = MaterialTheme.typography.displayLarge,
                fontFamily = Kavoon,
                fontSize = 120.sp,
                color = color.value
            )
        }
        
    }
}

@Preview(showBackground = true)
@Composable
fun CharacterCardLargePrev() {
    RickMortyTheme {

//        CharacterCardLarge(
//            imagePlaceHolder = R.drawable.placeholder
//        )
    }
}
