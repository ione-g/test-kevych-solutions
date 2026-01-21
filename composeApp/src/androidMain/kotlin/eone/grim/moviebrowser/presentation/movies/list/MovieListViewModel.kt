package eone.grim.moviebrowser.presentation.movies.list

import eone.grim.moviebrowser.data.images.ImageConfigStore
import eone.grim.moviebrowser.domain.usecase.GetPopularMovies
import eone.grim.moviebrowser.presentation.model.toUI
import eone.grim.moviebrowser.presentation.movies.list.MovieListContract.Effect.*
import eone.grim.moviebrowser.presentation.mvi.MviViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MovieListViewModel(
    private val getPopular: GetPopularMovies,
    private val images: ImageConfigStore,
    private val scope: CoroutineScope
) : MviViewModel<MovieListContract.Intent, MovieListContract.State, MovieListContract.Effect>(
    MovieListContract.State()
) {
    private var initialJob: Job? = null
    private var loadMoreJob: Job? = null

    init {

    }

    override fun onIntent(intent: MovieListContract.Intent) {
        when (intent) {
            MovieListContract.Intent.OnAppear -> initialLoad()
            MovieListContract.Intent.OnRetry -> initialLoad()
            is MovieListContract.Intent.OnMovieClick -> scope.launch {
                _effects.send(NavigateToDetails(intent.id))
            }

            MovieListContract.Intent.OnLoadNextPage -> loadNextPage()
        }
    }

    private fun initialLoad() {
        initialJob?.cancel()
        initialJob = scope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                error = null,
                errorMore = null,
                page = 1,
                endReached = false
            )
            try {
                val movies = getPopular(1)
                val ui = movies.map { it.toUI(images) }
                _state.value = _state.value.copy(
                    isLoading = false,
                    items = ui,
                    page = 1,
                    endReached = movies.isEmpty()
                )
            } catch (t: Throwable) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = t.message ?: "Unknown error"
                )
            }
        }
    }

    private fun loadNextPage() {
        val s = _state.value

        if (s.isLoading || s.isLoadingMore || s.endReached) return

        val nextPage = s.page + 1

        loadMoreJob?.cancel()
        loadMoreJob = scope.launch {
            _state.value = s.copy(isLoadingMore = true, errorMore = null)
            try {
                val movies = getPopular(nextPage)
                val ui = movies.map { it.toUI(images) }

                _state.value = _state.value.copy(
                    isLoadingMore = false,
                    items = _state.value.items + ui,
                    page = nextPage,
                    endReached = movies.isEmpty()
                )
            } catch (t: Throwable) {
                _state.value = _state.value.copy(
                    isLoadingMore = false,
                    errorMore = t.message ?: "Unknown error"
                )
            }
        }
    }
}
