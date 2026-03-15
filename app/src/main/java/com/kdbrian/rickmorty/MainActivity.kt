package com.kdbrian.rickmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.kdbrian.rickmorty.presentation.CounterViewModel
import com.kdbrian.rickmorty.presentation.MainViewModel
import com.kdbrian.rickmorty.presentation.ui.destinations.DiscoveryScreen
import com.kdbrian.rickmorty.presentation.ui.destinations.HomeScreen
import com.kdbrian.rickmorty.presentation.ui.theme.RickMortyTheme
import com.kdbrian.rickmorty.util.dispatchIO
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber


@OptIn(DelicateCoroutinesApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private val counterViewModel: CounterViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.enableEdgeToEdge(window)
        setContent {

            val characters = mainViewModel.characters()
            val snackbarHostState = remember { SnackbarHostState() }


            RickMortyTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackbarHostState
                        )
                    }
                ) { _ ->

                    HomeScreen(
                        characters = characters
                    )
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
