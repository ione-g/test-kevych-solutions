package eone.grim.moviebrowser.presentation.movies.details

import eone.grim.moviebrowser.data.images.ImageConfigStore
import eone.grim.moviebrowser.domain.entity.Movie
import eone.grim.moviebrowser.domain.usecase.GetMovieDetails
import eone.grim.moviebrowser.presentation.model.toUI
import eone.grim.moviebrowser.presentation.mvi.MviViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val getDetails: GetMovieDetails,
    private val images: ImageConfigStore,
    private val scope: CoroutineScope
) : MviViewModel<MovieDetailsContract.Intent, MovieDetailsContract.State, MovieDetailsContract.Effect>(
    MovieDetailsContract.State()
) {
    private var domainMovie: Movie? = null

    override fun onIntent(intent: MovieDetailsContract.Intent) {
        when (intent) {
            is MovieDetailsContract.Intent.Load -> load(intent.id)}
    }

    private fun load(id: Long) {
        scope.launch {
            _state.value = MovieDetailsContract.State(isLoading = true)
            try {
                val movie = getDetails(id)
                domainMovie = movie
                _state.value = MovieDetailsContract.State(
                    isLoading = false,
                    movie = movie.toUI(images),
                    error = null
                )
            } catch (t: Throwable) {
                _state.value = MovieDetailsContract.State(isLoading = false, error = t.message ?: "Unknown error")
            }
        }
    }

}