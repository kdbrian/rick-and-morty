package com.kdbrian.rickmorty.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kdbrian.rickmorty.presentation.ui.theme.RickMortyTheme
import com.kdbrian.rickmorty.presentation.ui.util.ThemePreviews
import com.kdbrian.rickmorty.util.AppEvent


@Composable
fun CharacterFab(
    modifier: Modifier = Modifier,
    background: Color = Color.Transparent,
    contentColor: Color = Color.White,
    containerColor: Color = Color.LightGray,
    characterId: Int = 0,
    isFavorite: Boolean = false,
    onEvent: (AppEvent) -> Unit = {}
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .background(background)
            .padding(vertical = 8.dp, horizontal = 6.dp)
    ) {

        IconButton(
            modifier = Modifier.background(
                color = containerColor,
                shape = CircleShape
            ),
            onClick = {
                onEvent(AppEvent.ToggleFavorite(characterId, !isFavorite))
            }
        ) {
            Icon(
                Icons.Rounded.FavoriteBorder,
                contentDescription = "Toggle Favorite",
                tint = contentColor,
                modifier = Modifier.padding(4.dp)
            )
        }

    }
}

@ThemePreviews
@Composable
fun CharacterFabPrev() {
    RickMortyTheme {

        CharacterFab()
    }
}
