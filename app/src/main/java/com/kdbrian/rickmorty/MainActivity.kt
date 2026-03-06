package com.kdbrian.rickmorty

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.kdbrian.rickmorty.presentation.MainViewModel
import com.kdbrian.rickmorty.ui.destinations.DiscoveryScreen
import com.kdbrian.rickmorty.ui.theme.RickMortyTheme
import com.kdbrian.rickmorty.util.dispatchIO
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber


@OptIn(DelicateCoroutinesApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val characters = mainViewModel.characters().collectAsLazyPagingItems()
            val snackbarHostState = remember { SnackbarHostState() }
            LaunchedEffect(characters) {

                if (characters.loadState.refresh is LoadState.Error) {
                    snackbarHostState.showSnackbar(
                        (
                                characters.loadState.refresh as LoadState.Error
                                ).error.localizedMessage ?: "Unknown Error"
                    )
                }
                dispatchIO {
                    mainViewModel.networkStatus.collectLatest { status ->
                        Timber.d("Network Status: $status")
                    }
                }
            }

            RickMortyTheme {
                Scaffold(
                    snackbarHost = {
                        androidx.compose.material3.SnackbarHost(
                            hostState = snackbarHostState
                        )
                    }
                ) { paddingValues ->
                    AnimatedContent(
                        targetState = characters.loadState.refresh is LoadState.Loading,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        if (it) {
                            CircularProgressIndicator()
                        } else {
                            DiscoveryScreen(
                                characters = characters
                            )
                        }
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
