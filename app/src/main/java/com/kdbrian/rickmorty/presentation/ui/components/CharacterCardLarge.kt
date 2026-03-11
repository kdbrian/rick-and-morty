package com.kdbrian.rickmorty.presentation.ui.components

 import android.R.attr.fontFamily
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kdbrian.rickmorty.domain.model.CharacterEntity
import com.kdbrian.rickmorty.presentation.ui.theme.Kavoon
import com.kdbrian.rickmorty.presentation.ui.theme.RickMortyTheme
import com.kdbrian.rickmorty.util.getDominantColor
import com.kdbrian.rickmorty.util.shimmer

@Composable
fun CharacterCardLarge(
    modifier: Modifier = Modifier,
    brush: Brush? = null,
    characterEntity: CharacterEntity,
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
                .padding(horizontal = 16.dp, vertical = 24.dp),
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
                    .shadow(
                        8.dp,
                        shape = RectangleShape,
                        ambientColor = color.value
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

            BasicText(
                text = characterEntity.name,
                autoSize = TextAutoSize.StepBased(
                    minFontSize = 72.sp,
                    maxFontSize = 96.sp,
                    stepSize = 16.sp
                ),
                style = TextStyle(
                    fontFamily = Kavoon,
                    lineHeight = 96.sp,
                    color = color.value,
                ),
                modifier = Modifier
                    .background(
                        color = color.value.copy(.15f),
                        shape = RectangleShape
                    )
                    .background(
                        Color.Black.copy(.15f),
                        shape = RectangleShape
                    )
                    .padding(8.dp)
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
