package com.kdbrian.rickmorty.presentation.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kdbrian.rickmorty.util.shimmer
import com.kdbrian.rickmorty.R
import com.kdbrian.rickmorty.domain.model.CharacterEntity
import com.kdbrian.rickmorty.presentation.ui.theme.Kavoon
import com.kdbrian.rickmorty.presentation.ui.theme.Matemasie
import com.kdbrian.rickmorty.presentation.ui.theme.RickMortyTheme
import com.kdbrian.rickmorty.presentation.ui.util.ThemePreviews
import com.kdbrian.rickmorty.util.getDominantColor


class CharacterCardLargePreviewProvider : PreviewParameterProvider<CharacterEntity> {
    override val values: Sequence<CharacterEntity>
        get() = sequenceOf(
            CharacterEntity.sampleCharacter
        )

}



@Composable
fun CharacterCardLarge(
    modifier: Modifier = Modifier,
    brush: Brush? = null,
    characterEntity: CharacterEntity = CharacterEntity.sampleCharacter,
    imagePlaceHolder: Int? = null,
) {

    val color = produceState(Color.LightGray) {
        value = getDominantColor(characterEntity.image)
    }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (brush != null) {
                            Modifier.background(brush)
                        } else {
                            Modifier
                        }
                    ),
            )

            Crossfade(
                targetState = characterEntity.image.isNotEmpty(),
            ) { state ->
                when (state) {
                    true -> AsyncImage(
                        model = characterEntity.image,
                        modifier = Modifier
                            .fillMaxSize()
                            .blur(
                                48.dp
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
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxSize()
                    .safeDrawingPadding()
                    .padding(/*horizontal = 16.dp,*/ vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {


                Box(
                    modifier = Modifier
                        .align(
                            Alignment.End
                        )
                        .size(200.dp)
                        .clip(
                            RectangleShape
                        )
                        .dropShadow(
                            shadow = Shadow(8.dp, color.value),
                            shape = RectangleShape,
                        )
                ) {
                    AsyncImage(
                        model = characterEntity.image,
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        contentDescription = characterEntity.name,
                        placeholder = imagePlaceHolder?.let {
                            painterResource(it)
                        },
                        error = imagePlaceHolder?.let {
                            painterResource(it)
                        }
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.Black.copy(.65f),
                            shape = RectangleShape
                        )
                        .background(
                            color = color.value.copy(.15f),
                            shape = RectangleShape
                        )
                        .padding(12.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    BasicText(
                        text = characterEntity.name,
                        autoSize = TextAutoSize.StepBased(
                            minFontSize = 48.sp,
                            maxFontSize = 64.sp,
                            stepSize = 12.sp
                        ),
                        style = TextStyle(
                            fontFamily = Matemasie,
                            lineHeight = 48.sp,
                            color = color.value,
                        ),

                        )

                    FlowRow(
                        maxItemsInEachRow = 3,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text(
                            text = characterEntity.gender,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = Kavoon,
                                color = Color.White,
                            ),
                            modifier = Modifier
                                .background(
                                    color = animateColorAsState(
                                        if (characterEntity.gender == "Male") Color(0xFF3F88C5) else if (characterEntity.gender.equals(
                                                "UnKnown",
                                                true
                                            )
                                        ) Color.DarkGray else Color(
                                            0xFFF52F57
                                        )
                                    ).value
                                )
                                .padding(6.dp)
                        )

                        Text(
                            text = characterEntity.status,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = Kavoon,
                                color = Color.White,
                            ),
                            modifier = Modifier
                                .background(
                                    color = animateColorAsState(
                                        if (characterEntity.status == "Alive") Color(0xFF3EC300) else if (characterEntity.status.equals(
                                                "UnKnown",
                                                true
                                            )
                                        ) Color(0xFFC4CBCA) else Color(
                                            0xFF0A0F0D
                                        )
                                    ).value
                                )
                                .padding(6.dp)
                        )


                        Text(
                            text = characterEntity.location.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = Kavoon,
                                color = color.value,
                            ),
                            modifier = Modifier
                                .background(
                                    color = Color(0xFFFFD29D)
                                )
                                .padding(6.dp)
                        )


                        Text(
                            text = characterEntity.species,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = Kavoon,
                                color = color.value,
                            ),
                            modifier = Modifier
                                .background(
                                    color = Color(0xFFFFD29D)
                                )
                                .padding(6.dp)
                        )


                        Text(
                            text = characterEntity.origin.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = Kavoon,
                                color = color.value,
                            ),
                            modifier = Modifier
                                .background(
                                    color = Color(0xFFFFD29D)
                                )
                                .padding(6.dp)
                        )


                    }


                }
            }

        }
}

@ThemePreviews
@Composable
fun CharacterCardLargePrev(
    @PreviewParameter(CharacterCardLargePreviewProvider::class) characterEntity: CharacterEntity
) {
    RickMortyTheme {

        CharacterCardLarge(
            imagePlaceHolder = R.drawable.placeholder,
            characterEntity = characterEntity,
        )
    }
}
