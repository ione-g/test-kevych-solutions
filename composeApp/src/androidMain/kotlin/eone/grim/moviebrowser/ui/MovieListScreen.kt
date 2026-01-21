package eone.grim.moviebrowser.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import eone.grim.moviebrowser.presentation.model.MovieUI
import eone.grim.moviebrowser.presentation.movies.list.MovieListContract
import eone.grim.moviebrowser.presentation.movies.list.MovieListViewModel
import eone.grim.moviebrowser.ui.nav.Route
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject

@Composable
fun MovieListScreen(nav: NavHostController) {
    val vm: MovieListViewModel = koinInject()
    val state by vm.state.collectAsState()

    val listState = rememberLazyListState()

    LaunchedEffect(Unit) { vm.onIntent(MovieListContract.Intent.OnAppear) }
    LaunchedEffect(Unit) {
        vm.effects.collect { eff ->
            when (eff) {
                is MovieListContract.Effect.NavigateToDetails -> nav.navigate(Route.Details.create(eff.id))
                is MovieListContract.Effect.ShowMessage -> { }
            }
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collectLatest { layoutInfo ->
                val totalItems = layoutInfo.totalItemsCount
                if (totalItems == 0) return@collectLatest

                val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: return@collectLatest

                if (lastVisibleIndex >= totalItems - 3) {
                    vm.onIntent(MovieListContract.Intent.OnLoadNextPage)
                }
            }
    }

    when {
        state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        state.error != null -> Column(Modifier.padding(16.dp)) {
            Text("Error: ${state.error}")
            Spacer(Modifier.height(8.dp))
            Button(modifier = Modifier.fillMaxWidth(), onClick = { vm.onIntent(MovieListContract.Intent.OnRetry) }) { Text("Retry") }
        }

        else -> LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp)
            , verticalArrangement = Arrangement.spacedBy(4.dp)

        ) {
            items(state.items, key = { it.id }) { movie ->
                MovieRow(
                    movie = movie,
                    onClick = { vm.onIntent(MovieListContract.Intent.OnMovieClick(movie.id)) },
                )
            }

            item {
                when {
                    state.isLoadingMore -> Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }

                    state.errorMore != null -> Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Error: ${state.errorMore}")
                        Spacer(Modifier.height(8.dp))
                        Button(modifier = Modifier.fillMaxWidth(),onClick = { vm.onIntent(MovieListContract.Intent.OnLoadNextPage) }) {
                            Text("Retry")
                        }
                    }

                    else -> Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun MovieRow(
    movie: MovieUI,
    onClick: () -> Unit,
) {
    ElevatedCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(12.dp)
        ) {
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = null,
                modifier = Modifier.size(72.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(movie.title, style = MaterialTheme.typography.titleMedium)
                Text(
                    movie.overview,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
