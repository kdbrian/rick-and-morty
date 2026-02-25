package com.kdbrian.rickmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.kdbrian.rickmorty.presentation.MainViewModel
import com.kdbrian.rickmorty.ui.theme.RickMortyTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val mainViewModel by viewModels<MainViewModel>()
            val characters = mainViewModel.characters().collectAsLazyPagingItems()

            RickMortyTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    repeat(characters.itemCount) {
                        Text(
                            text = characters[it]?.name.toString(),
                            style = MaterialTheme.typography.titleMedium
                        )
                        HorizontalDivider(
                            Modifier.padding(
                                horizontal = 16.dp,
                                vertical = 4.dp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun App(content: @Composable () -> Unit = {}) {
    RickMortyTheme {
        content()
    }
}
